package com.yubaba.studify.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatResponse(String userMessage) throws Exception {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

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

}
