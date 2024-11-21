package com.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class SendMailService {

    public void sendMail(String memberName, String product) {
        try {
            log.info("[{} 알림에일] {}님이 {} 상품을 구매하셨습니다.", LocalDate.now(), memberName, product);
        } catch (Exception e) {
            // 재전송 로직이나 오류 처리
        }
    }
}
