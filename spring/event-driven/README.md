# 스프링 이벤트

이벤트 기반 아키텍처는 시스템 내에서 발생하는 이벤트를 통해서 컴포넌트 간의 결합도를 낮추는 방법으로, 스프링에서는 이벤트 발행(publish)과 구독(subscribe) 기능을 통해서 컴포넌트 간의 느슨한 결합을 도와준다.  
<br/>

### 결합도가 높은 주문 메서드 작성

다음 코드는 주문을 하는 코드이다. 예제를 위해 실제 주문 코드는 간소화했다.

```java
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    private final SendMailService mailService;
    private final SendTalkService talkService;

    @Transactional
    public void create(OrderRequest orderRequest, Long memberId) {
        // 주 관심사: 주문 로직
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Order order = Order.createOrder(orderRequest, member);
        orderRepository.save(order);

        // 비 관심사: 주문 완료 후 알림메일, 알림톡 전송
        mailService.sendMail(order.getMemberName(), order.getProductName());
        talkService.sendTalk(order.getMemberName(), order.getProductName());
    }
}
```
<br/>

OrderService 코드는 다음과 같은 문제점이 있다.

- <strong>단일 책임 원칙(SRP) 위배</strong>: 주문 로직은 주문 생성이라는 주된 책임 이외에도, 메일 전송 및 알림톡 전송이라는 추가적인 책임을 가지고 있다.
- <strong>유지 보수성 저하</strong>: 메일 전송이나, 알림톡 전송 로직이 변경된다면 OrderService 클래스의 해당 부분을 직접 수정해야 하는 문제와, 테스트 시 주문 생성뿐 아니라 메일 전송, 알림톡 전송까지 고려해서 코드를 작성해야 하는 문제가 있다.
- <strong>확장성 저하</strong>: 새로운 알림 서비스나 기능이 추가되었을 때 OrderService 클래스에 직접 로직을 추가해야 하는 문제가 있으며, 이는 클래스가 점점 비대해지게 된다. 그리고 메일 전송, 알림톡 전송 로직이 고정되어 있어 특정 조건에 따른 알림 방식을 선택하는 것이 복잡하다.  

<br/>

코드에서 주된 관심사는 주문 생성이며, 알림메일과 알림톡을 전송하는 것은 주문과 관련없는 비관심사다. 또한 메일과 톡을 전송하는 로직은 메일 서버나 메시지 서버의 응답 지연으로 전체 로직 응답 시간에 문제가 생기므로 비동기로 처리하는 것이 좋다.  
주문 로직에 알림메일, 알림톡 로직이 강결합된 문제를 해결하기 위해서 스프링의 이벤트 발행과 구독을 사용한 이벤트 기반 아키텍처를 구현할 것이다.
<br/>

이제 주문 로직에서 발생한 문제점들을 스프링의 이벤트 발행과 구독을 통해서 해결할 것이다.  

<br/>
<br/>

## 스프링 이벤트 발행과 구독

스프링에서 이벤트 발행은 ApplicationEventPublisher 객체를 사용하며, 이 객체는 스프링 컨테이너 ApplicationContext 객체에서 제공하며, 기본 구현은 AbstractApplicationContext 객체에 포함되어 있다.  

```java
public class AbstractApplicationContext {
    public void publishEvent(ApplicationEvent event) {
        // 실제 이벤트를 처리하는 내부 메서드 호출
        this.publishEvent(event, (ResolvableType)null);
    }

    public void publishEvent(Object event) {
        // ApplicationEvent로 변환 후 이벤트를 처리
        this.publishEvent(event, (ResolvableType)null);
    }
}
```
<br/>

스프링 4.2 이전 버전에서는 이벤트 객체를 정의하기 위해서는 ApplicationEvent 객체를 상속받아 구현해야 했지만, 4.2 버전 부터는 상속받지 않아도 된다.  
그리고 이벤트 구독도 ApplicationListener 인터페이스를 구현하는 방식에서 @EventListener 애노테이션을 사용해서 보다 편하게 구현할 수 있다.  

<br/>

### 이벤트 발행

이벤트 발행은 특정 이벤트가 발생했을 때 다른 컴포넌트들에게 알리는 행위다. 이벤트를 발행하는 주체를 발행자라고 하며, 발행자는 이벤트 객체를 생성하고 이를 ApplicationContext에 게시한다.  

ApplicationEventPublisher 객체는 스프링 컨텍스트에서 이벤트를 발행하는데 사용되며, 애플리케이션 내의 다른 컴포넌트가 이벤트를 수신하고 처리할 수 있게 해준다. 이를 통해서 모듈 간의 결합도를 낮추고 확장성을 높일 수 있다.  
publishEvent() 메서드를 통해 이벤트를 발행할 수 있으며 인자로 이벤트 객체의 인스턴스를 전달한다.  


```java
// 이벤트 객체
public class OrderEvent {
    private String memberName;
    private String productName;
    
    // ...
}
```

```java
@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void create(OrderRequest orderRequest, Long memberId) {
        // 주문 로직
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Order order = Order.createOrder(orderRequest, member);
        orderRepository.save(order);

        // 이벤트 발행 적용
        eventPublisher.publishEvent(OrderEvent.of(order));
    }
}
```

<br/>

### 이벤트 구독

이벤트 구독은 특정 이벤트가 발행되었을 때 이를 처리하는 행위다. 이벤트를 구독하는 주체는 이벤트 리스너라고 하며, 특정 타입의 이벤트를 수신하고 처리하는 역할을 한다.  
스프링에서는 @EventListener 애노테이션을 사용해서 이벤트 리스너를 정의한다.  

```java
@Async
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final SendMailService mailService;
    private final SendTalkService talkService;

    @EventListener
    public void sendMailWhenOrderComplete(OrderEvent event) {
        mailService.sendMail(event.getMemberName(), event.getProductName());
    }

    @EventListener
    public void sendTalkWhenOrderComplete(OrderEvent event) {
        talkService.sendTalk(event.getMemberName(), event.getProductName());
    }
}
```
<br/>

이벤트를 구독할 메서드에 @EventListener를 적용하고 바동기로 처리하기 위해 @Async를 사용했다.  
@Async 애노테이션을 적용하면 JVM이 스레드를 사용해서 비동기로 처리하게 되며 스프링에서는 ThreadPoolTaskExecutor 객체를 커스터마이징해서 사용할 수 있고 @EnableAsync로 비동기를 활성화 해야한다.

```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("Custom Task-");
        taskExecutor.initialize();

        return taskExecutor;
    }
}
```
<br/>

ThreadPoolTaskExecutor 객체는 스프링에서 제공하는 클래스로, 스프링 컨텍스트와 통합되어 사용할 수 있게 설계됐다.  
자바에서 ThreadPoolExecutor를 사용해본적이 있다면 기능을 이해하는 것은 어렵지 않을 것이다.  

<br/>

로그를 확인하면 주문을 처리하는 로직은 톰캣이 생성한 스레드에서 처리되고, 알림메일, 알림톡 로직은 비동기로 JVM 내에서 관리되는 스레드가 처리한 것을 확인할 수 있다.  

![로그](/images/event-driven-log.png)

<br/>

## 이벤트 발행과 구독의 장단점 및 사용

### 장점 
- 이벤트 발행과 구독을 사용하면 모듈 간의 직접적인 의존성을 낮출 수 있어 결합도가 감수하므로, 코드의 유연성이 높아지고 유지보수가 편해진다.
- 이벤트를 비동기로 처리하며, 요청 스레드의 실행시간을 줄이고 응답성을 높인다.
- 새로운 이벤트 리스너를 추가하는 것이 어렵지 않으므로 기능 확장에 좋다.
- 비즈니스 로직과 부가 기능을 처리하는 로직을 분리해서 관심사를 나눌 수 있다.  

<br/>

### 단점
- 이벤트가 비동기적으로 처리되는 경우, 이벤트가 언제, 어디서 발생했는지 추적하는 것이 어렵기 때문에 디버깅이 힘들 수 있다.  
- 이벤트 기반 아키텍처를 도입하면 코드의 구조가 좀 더 복잡해질 수 있다.
- 비동기 처리를 할 때 서버의 성능을 고려해서 설계해야 한다. 큐의 부하나 병목을 주의해야 한다.  

<br/>

### 언제 사용해야 하는가?
이벤트의 발행과 구독은 애플리케이션에서 모듈의 관심사를 분리해서 느슨한 결합을 만들 때 유용하다. 즉, 컴포넌트간의 직접적인 의존성을 줄이기 때문에 테스트에 용이하고, 유지보수성이 높아진다. 주문을 완료하고 로그 등록과 같은 이벤트가 추가되도 주문 로직은 영향을 받지 않게 된다.  
결론적으로 구현된 객체가 너무 많은 책임을 가진다면, 이벤트 발행과 구독을 통해 관심사를 분리해서 비즈니스 로직을 명확하게 분리시킬 수 있다.

<br/>