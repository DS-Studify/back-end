package com.yubaba.studify.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final ObjectMapper mapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatResponse(String userMessage) throws Exception {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // JSON 생성
        ObjectNode requestJson = mapper.createObjectNode();
        requestJson.put("model", model);
        ArrayNode messagesArray = requestJson.putArray("messages");
        ObjectNode messageNode = messagesArray.addObject();
        messageNode.put("role", "user");
        messageNode.put("content", userMessage);

        String jsonBody = mapper.writeValueAsString(requestJson);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            JsonNode jsonNode = mapper.readTree(result);

            if (jsonNode.has("error")) {
                throw new RuntimeException("OpenAI API Error: " + jsonNode.get("error").get("message").asText());
            }

            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        }
    }

    public String getFeedback(JsonNode studyResult) throws Exception {
        OkHttpClient client = new OkHttpClient();

        // 요청값 그대로 문자열 변환
        String dataString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(studyResult);

        String prompt = """
            다음은 한 학생의 공부 상태별 누적 시간(초) 데이터입니다.
            
            상태 목록:
            1. 공부 중 = 실제 공부한 시간 (2.바른 자세 + 3.집중 안 되는 자세 포함)
            2. 바른 자세 = 올바른 자세로 공부한 시간
            3. 집중 안 되는 자세 = 산만하거나 흐트러진 자세
            4. 졸음 자세 = 졸고 있는 자세
            5. 자리비움 = 공부 자리에서 떠난 시간
            6. 공부 중지 = 위에 해당하지 않는 상태, 책상에 앉아있었지만 공부하지 않은 시간(졸음 자세, 자리비움 제외)
    
            분석 데이터:
            %s
    
            이 데이터를 분석해서 시간과도 관련지어 학생의 학습 태도와 집중 상태를 평가하고,
            개선할 수 있는 구체적인 조언을 5줄 이내로 문장으로 작성하세요.
            시간은 총 초로 언급하지 않고 시간/분/초 단위로 바꿔서 말하세요. 0시간일 땐 분/초만 말하세요.
            위 상태 목록의 번호 또한 언급하지 마세요.
        """.formatted(dataString);

        // 요청 JSON 생성
        ObjectNode requestJson = mapper.createObjectNode();
        requestJson.put("model", model);
        ArrayNode messages = requestJson.putArray("messages");
        ObjectNode userMsg = messages.addObject();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);

        String jsonBody = mapper.writeValueAsString(requestJson);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            JsonNode jsonNode = mapper.readTree(result);

            if (jsonNode.has("error")) {
                throw new RuntimeException("OpenAI API Error: " + jsonNode.get("error").get("message").asText());
            }
            if (!jsonNode.has("choices") || jsonNode.get("choices").isEmpty()) {
                throw new RuntimeException("No choices returned from OpenAI API: " + result);
            }

            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        }
    }

}
