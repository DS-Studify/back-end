package com.yubaba.studify.controller;

import com.yubaba.studify.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final ChatGptService chatGptService;

    @PostMapping
    public String chat(@RequestBody String message) throws Exception {
        return chatGptService.getChatResponse(message);
    }

}
