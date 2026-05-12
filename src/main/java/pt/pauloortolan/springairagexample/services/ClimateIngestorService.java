package pt.pauloortolan.springairagexample.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.persistence.ClimateRepository;

@Service
@RequiredArgsConstructor
public class ClimateIngestorService {

    private final ClimateRepository climateRepository;
    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper;

    @Value("${app.climate.file}")
    private Resource oscarNomineeFile;

}
