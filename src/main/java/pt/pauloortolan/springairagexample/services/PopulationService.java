package pt.pauloortolan.springairagexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.pojo.PopulationCountResponse;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopulationService {

    private final ChatClient raggedChatClient;

    private static final PromptTemplate TOP_TEN_POPULATED_COUNTRIES_PROMPT = new PromptTemplate("""
            Extract the top ten (10) most populated countries from the provided Population context documents.
            For each country found, provide its rank, name, and 2022 population.
            Return all countries found in the context, ordered by rank."
            """);

    private static final PromptTemplate TOP_FIVE_POPULATED_COUNTRIES_PROMPT = new PromptTemplate("""
            Extract the top five (5) most populated countries from the provided Population context documents located in the continent {continent}.
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

    public PopulationCountResponse getTopFivePopulatedCountriesByContinent(String continent) {
        log.info("PopulationService::getTopFivePopulatedCountriesByContinent(continent={})", continent);

        Prompt prompt = TOP_FIVE_POPULATED_COUNTRIES_PROMPT
                .create(Map.of("continent", continent));

        return raggedChatClient
                .prompt(prompt)
                .call()
                .entity(PopulationCountResponse.class);
    }
}
