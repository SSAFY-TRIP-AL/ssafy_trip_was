package com.ssafy.tripbaton.domain.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.tripbaton.domain.auth.dto.OAuthUserProfile;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USER_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.client-secret:}")
    private String kakaoClientSecret;

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;

    public OAuthUserProfile getUserProfile(String provider, String code, String redirectUri) {
        if ("kakao".equalsIgnoreCase(provider)) {
            return getKakaoProfile(code, redirectUri);
        }
        if ("google".equalsIgnoreCase(provider)) {
            return getGoogleProfile(code, redirectUri);
        }
        throw new CustomException(ErrorCode.OAUTH_FAILED);
    }

    private OAuthUserProfile getKakaoProfile(String code, String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        if (kakaoClientSecret != null && !kakaoClientSecret.isBlank()) {
            params.add("client_secret", kakaoClientSecret);
        }

        String accessToken = requestAccessToken(KAKAO_TOKEN_URL, params);
        JsonNode user = requestUserInfo(KAKAO_USER_URL, accessToken);

        JsonNode account = user.path("kakao_account");
        JsonNode profile = account.path("profile");
        JsonNode properties = user.path("properties");

        String providerId = textOrNull(user, "id");
        if (providerId == null) {
            throw new CustomException(ErrorCode.OAUTH_FAILED);
        }
        String nickname = firstNonNull(textOrNull(profile, "nickname"), textOrNull(properties, "nickname"));
        String email = textOrNull(account, "email");
        String profileImage = firstNonNull(
                textOrNull(profile, "profile_image_url"),
                textOrNull(properties, "profile_image"));

        return new OAuthUserProfile(providerId, nickname, email, profileImage);
    }

    private OAuthUserProfile getGoogleProfile(String code, String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        String accessToken = requestAccessToken(GOOGLE_TOKEN_URL, params);
        JsonNode user = requestUserInfo(GOOGLE_USER_URL, accessToken);

        String providerId = textOrNull(user, "id");
        if (providerId == null) {
            throw new CustomException(ErrorCode.OAUTH_FAILED);
        }
        String name = textOrNull(user, "name");
        String email = textOrNull(user, "email");
        String profileImage = textOrNull(user, "picture");

        return new OAuthUserProfile(providerId, name, email, profileImage);
    }

    private String requestAccessToken(String tokenUrl, MultiValueMap<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            JsonNode body = restTemplate.postForObject(tokenUrl, request, JsonNode.class);
            if (body == null || !body.hasNonNull("access_token")) {
                throw new CustomException(ErrorCode.OAUTH_FAILED);
            }
            return body.get("access_token").asText();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.OAUTH_FAILED);
        }
    }

    private JsonNode requestUserInfo(String userUrl, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            JsonNode body = restTemplate.exchange(userUrl, HttpMethod.GET, request, JsonNode.class).getBody();
            if (body == null) {
                throw new CustomException(ErrorCode.OAUTH_FAILED);
            }
            return body;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.OAUTH_FAILED);
        }
    }

    private String textOrNull(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value == null || value.isNull()) ? null : value.asText();
    }

    private String firstNonNull(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }
}
