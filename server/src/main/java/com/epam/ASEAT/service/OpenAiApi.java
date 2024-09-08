package com.epam.ASEAT.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class OpenAiApi {
    @Value("${openai.api_key}")
    private String apiKey;

    // GPT-4 model version
    private static final String GPT_4_MODEL = "gpt-4";

    public String getTextCompletion(String userMsg, String systemMsg) {
        log.info("User message: {}", userMsg);
        log.info("System message: {}", systemMsg);

        // Ensure the API key is available
        if (StringUtils.isEmpty(apiKey)) {
            throw new IllegalStateException("OpenAI API key is not configured properly.");
        }

        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

        // Prepare messages for the API call
        List<ChatMessage> messages = new ArrayList<>();
        if (StringUtils.isNotEmpty(systemMsg)) {
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMsg));
        }
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMsg));

        // Create a request with GPT-4 model
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(GPT_4_MODEL)
                .messages(messages)
                .build();

        log.info("OpenAI request: {}", request);

        // Get the response from OpenAI
        ChatMessage message = service.createChatCompletion(request).getChoices().get(0).getMessage();
        log.info("OpenAI response: {}", message);

        return message.getContent();
    }

    // Overloaded method when there is no system message
    public String getTextCompletion(String userMsg) {
        return this.getTextCompletion(userMsg, null);
    }
}
