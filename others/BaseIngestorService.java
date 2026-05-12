@Service
@RequiredArgsConstructor
public abstract class BaseIngestorService<T> {

    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- Template method (the algorithm skeleton) ---
    public final LoadStatistics loadData() throws IngestionException {
        LoadStatistics statistics = LoadStatistics.reset();
        List<Document> documents = new ArrayList<>();

        try {
            Path file = getFile().getFile().toPath();
            CsvReader<NamedCsvRecord> csvReader = CsvReader
                    .builder()
                    .ofNamedCsvRecord(file, StandardCharsets.UTF_8);

            for (NamedCsvRecord record : csvReader) {
                try {
                    T entity = parseRecord(record);
                    save(entity);
                    documents.add(toDocument(entity));
                    statistics = statistics.addLoaded();
                } catch (DocumentLoadingException e) {
                    statistics = statistics.addFailed(e.getFailedDocument(), e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IngestionException(e.getMessage());
        }

        vectorStore.add(documents);
        return statistics;
    }

    // --- Hook methods (subclasses must implement) ---
    protected abstract Resource getFile();
    protected abstract T parseRecord(NamedCsvRecord record) throws DocumentLoadingException;
    protected abstract Document toDocument(T entity);
    protected abstract void save(T entity);
}