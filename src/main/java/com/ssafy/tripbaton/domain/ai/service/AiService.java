package com.ssafy.tripbaton.domain.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tripbaton.domain.ai.dto.AiSummaryResponseDto;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AiSummaryResponseDto getSummary(String locationName, String category) {
        String prompt = buildPrompt(locationName, category);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "response_format", Map.of("type", "json_object")
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String raw = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
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
            throw new CustomException(ErrorCode.AI_REQUEST_FAILED);
        }
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
