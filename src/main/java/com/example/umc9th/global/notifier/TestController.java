package com.example.umc9th.global.notifier;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버의 500 Internal Server Error 발생 시
 * ExceptionAdvice가 정상적으로 에러를 잡고
 * DiscordNotifier를 통해 알림이 전송되는지 확인하기 위한 테스트용 컨트롤러
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 강제로 500 Internal Server Error를 발생시키는 테스트용 API 엔드포인트
     * GET /test/error 요청 시 RuntimeException을 던져 서버 예외 상황을 의도적으로 유발
     * → ExceptionAdvice의 @ExceptionHandler(Exception.class)가 이 예외를 잡아 처리함
     * → DiscordNotifier가 자동으로 Webhook POST 요청을 전송
     */
    @GetMapping("/error")
    public String triggerError() {
        // 강제로 500 에러 발생시키기
        throw new RuntimeException("강제 500 에러 발생 테스트");
    }
}
