package com.ssafy.tripbaton.domain.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tripbaton.domain.ai.dto.AiRecommendItemDto;
import com.ssafy.tripbaton.domain.ai.dto.AiRecommendResponseDto;
import com.ssafy.tripbaton.domain.ai.dto.AiSummaryResponseDto;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String OPENAI_URL =
            "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions";

    // body생성하기
    private Map<String, Object> createRequestBody(String prompt) {
        return Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "developer",
                                "content", "Answer in Korean"
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "response_format", Map.of(
                        "type", "json_object"
                )
        );
    }

    public AiSummaryResponseDto getSummary(String locationName, String category) {
        String prompt = buildPrompt(locationName, category);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

//        Map<String, Object> body = Map.of(
//                "model", model,
//                "messages", List.of(Map.of("role", "user", "content", prompt)),
//                "response_format", Map.of("type", "json_object")
//        );
        Map<String, Object> body = createRequestBody(prompt);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String raw = restTemplate.postForObject(
//                    "https://api.openai.com/v1/chat/completions",
                    OPENAI_URL,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(raw);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode parsed = objectMapper.readTree(content);

            String summary = parsed.path("summary").asText();
            List<String> highlights = new ArrayList<>();
            for (JsonNode h : parsed.path("highlights")) {
                highlights.add(h.asText());
            }

            return new AiSummaryResponseDto(locationName, summary, highlights);
        } catch (Exception e) {
            log.error("[AI] OpenAI API 호출 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AI_REQUEST_FAILED);
        }
    }

    public AiRecommendResponseDto getRecommendations(String currentLocation, String category, String preference) {
        String prompt = buildRecommendPrompt(currentLocation, category, preference);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

//        Map<String, Object> body = Map.of(
//                "model", model,
//                "messages", List.of(Map.of("role", "user", "content", prompt)),
//                "response_format", Map.of("type", "json_object")
//        );
        Map<String, Object> body = createRequestBody(prompt);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String raw = restTemplate.postForObject(
//                    "https://api.openai.com/v1/chat/completions",
                    OPENAI_URL,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(raw);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode parsed = objectMapper.readTree(content);

            List<AiRecommendItemDto> recommendations = new ArrayList<>();
            for (JsonNode item : parsed.path("recommendations")) {
                recommendations.add(new AiRecommendItemDto(
                        item.path("locationName").asText(),
                        item.path("reason").asText()
                ));
            }

            return new AiRecommendResponseDto(recommendations);
        } catch (Exception e) {
            log.error("[AI] OpenAI API 호출 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AI_REQUEST_FAILED);
        }
    }

    private String buildRecommendPrompt(String currentLocation, String category, String preference) {
        StringBuilder sb = new StringBuilder();
        sb.append("여행 릴레이의 다음 방문지를 한국어로 3곳 추천해줘.\n");
        sb.append("현재 위치: ").append(currentLocation).append("\n");
        if (category != null && !category.isBlank()) {
            sb.append("릴레이 카테고리: ").append(category).append("\n");
        }
        if (preference != null && !preference.isBlank()) {
            sb.append("여행 성향: ").append(preference).append("\n");
        }
        sb.append("다음 형식의 JSON으로 응답해줘: {\"recommendations\": [{\"locationName\": \"...\", \"reason\": \"...\"}, ...]}");
        return sb.toString();
    }

    private String buildPrompt(String locationName, String category) {
        String categoryPart = (category != null && !category.isBlank())
                ? " (카테고리: " + category + ")"
                : "";
        return "다음 여행지에 대해 한국어로 짧은 소개와 주요 포인트 3가지를 JSON 형식으로 답변해줘.\n" +
                "여행지: " + locationName + categoryPart + "\n" +
                "응답 형식: {\"summary\": \"...\", \"highlights\": [\"...\", \"...\", \"...\"]}";
    }
}
