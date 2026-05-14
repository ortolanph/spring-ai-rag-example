package pt.pauloortolan.springairagexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.pojo.PopulationCountResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopulationService {

    private final ChatClient raggedChatClient;

    private static final PromptTemplate TOP_TEN_POPULATED_COUNTRIES_PROMPT = new PromptTemplate("""
            Extract the top 10 populated countries from the provided context documents.
            For each country found, provide its rank, name, and 2022 population.
            Return all countries found in the context, ordered by rank."
            """);


    public PopulationCountResponse getMostPopultatedCountries() {
        log.info("PopulationService::getMostPopultatedCountries()");

        return raggedChatClient
                .prompt(TOP_TEN_POPULATED_COUNTRIES_PROMPT.create())
                .call()
                .entity(PopulationCountResponse.class);
    }
}
