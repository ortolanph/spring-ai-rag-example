package pt.pauloortolan.springairagexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleAIService {

    private static final PromptTemplate MEANING_OF_LIFE_IN_LANGUAGE = new PromptTemplate("What is the answer to life, the universe and everything in {language}?");
    private final ChatClient chatClient;

    public String sayMeaningOfLifeIn(String language) {
        log.info("SimpleAIService:sayMeaningOfLifeIn(language={}))", language);

        Prompt prompt = MEANING_OF_LIFE_IN_LANGUAGE
                .create(Map.of("language", language));

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
