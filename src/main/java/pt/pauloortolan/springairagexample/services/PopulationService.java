package pt.pauloortolan.springairagexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.pojo.PopulationCountResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopulationService {

    private final ChatClient raggedChatClient;

    public PopulationCountResponse getMostPopultatedCountries() {
        log.info("PopulationService::getMostPopultatedCountries()");

        return raggedChatClient
                .prompt("What are the 10 most populated countries in the world? Answer with a list of country names and their population, separated by a colon, one per line. On the category field, put the label ´Top 10 Most Populated Countries´")
                .call()
                .entity(PopulationCountResponse.class);
    }
}
