@Service
public class PopulationIngestorService extends BaseIngestorService<Population> {

    private final PopulationRepository populationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.population.file}")
    private Resource populationFile;

    public PopulationIngestorService(VectorStore vectorStore,
                                     PopulationRepository repository) {
        super(vectorStore);
        this.populationRepository = repository;
    }

    @Override
    protected Resource getFile() { return populationFile; }

    @Override
    protected void save(Population p) { populationRepository.save(p); }

    @Override
    protected Document toDocument(Population p) {
        try {
            return new Document(objectMapper.writeValueAsString(p), p.toMetadata());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Population parseRecord(NamedCsvRecord r) throws DocumentLoadingException {
        try {
            return new Population(
                UUID.randomUUID(),
                r.getField("Rank").isEmpty() ? null : Integer.parseInt(r.getField("Rank")),
                r.getField("CCA3"),
                // ... rest of the fields
            );
        } catch (Exception e) {
            throw new DocumentLoadingException(r.toString(), e.getMessage());
        }
    }
}