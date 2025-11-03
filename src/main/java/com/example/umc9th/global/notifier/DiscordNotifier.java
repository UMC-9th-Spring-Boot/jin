package com.example.umc9th.global.notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 서버에서 발생한 예외(500 Internal Server Error)를 Discord Webhook으로 전송하는 역할
 * ExceptionAdvice에서 예외 발생 시 이 클래스를 호출하여 메시지를 전송
 */
@Slf4j
@Component
public class DiscordNotifier {

    @Value("${webhook.discord-url:}")
    private String discordWebhookUrl; // Discord로 메시지를 보낼 Webhook 주소

    @Value("${spring.profiles.active:local}")
    private String activeProfile; // 현재 실행 중인 환경

    // REST API 요청을 보내기 위한 Spring의 HTTP 클라이언트 객체
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 예외 발생 시 호출되는 핵심 메서드
     * @param e 발생한 예외 객체
     * @param requestUri 에러가 발생한 요청 URI
     */
    public void sendErrorNotification(Exception e, String requestUri) {
        // 1. 환경 분기 - 로컬 환경에서는 알림 보내지 않음
        if (!"prod".equalsIgnoreCase(activeProfile) && !"dev".equalsIgnoreCase(activeProfile)) {
            log.info("[DISCORD-NOTIFY] Skip sending message in {} environment", activeProfile);
            return;
        }

        // 2. 에러 발생 시각을 문자열로 포맷팅
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 3. Discord 메시지 포맷 생성
        String content = String.format(
                "🚨 **서버 에러 발생** 🚨\n" +
                        "```%s```\n" +
                        "📅 발생 시간: %s\n" +
                        "🌍 환경: %s\n" +
                        "📡 요청 URL: %s\n" +
                        "🧩 예외: %s\n" +
                        "💬 메시지: %s",
                e.getClass().getSimpleName(), // 간단한 클래스명
                timestamp, // 발생 시간
                activeProfile, // 현재 환경
                requestUri, // 요청 URL
                e.getClass().getName(), // 예외 클래스 전체 이름
                e.getMessage() // 예외 메시지 (개발자에게 디버깅 정보 제공)
        );

        try {
            // 4. HTTP 요청 헤더 생성
            // Content-Type: application/json 설정 (Discord Webhook은 JSON 형식으로만 메시지를 받음)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 5. Discord Webhook으로 전송할 JSON 본문 생성
            // Discord Webhook은 {"content": "메시지내용"} 형태의 JSON만 인식함
            Map<String, String> payload = Map.of("content", content);

            // 6. HttpEntity로 헤더와 본문을 묶어서 하나의 HTTP 요청 객체로 만듦
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

            // 7. POST 요청 전송 - Discord Webhook에 메시지를 전송함
            // restTemplate.postForEntity(요청URL, 요청본문, 응답타입)
            restTemplate.postForEntity(discordWebhookUrl, entity, String.class);

            log.info("[DISCORD-NOTIFY] Error sent to Discord channel successfully.");

        } catch (Exception ex) {
            log.error("[DISCORD-NOTIFY] Failed to send message to Discord: {}", ex.getMessage());
        }
    }
}
