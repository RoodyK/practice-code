package com.event.listener;

import com.event.listener.event.OrderEvent;
import com.event.service.SendMailService;
import com.event.service.SendTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
