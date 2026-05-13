package pt.pauloortolan.springairagexample.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class Configurations {

    private final ChatClient.Builder chatClientBuilder;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    @Bean
    public ChatClient chatClient() {
        return chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public ChatClient raggedChatClient(VectorStore vectorStore) {
        var questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore).build();

        return chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(), questionAnswerAdvisor)
                .build();
    }
}