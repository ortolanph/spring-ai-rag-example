package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface PopulationRepository extends CassandraRepository<Population, Long> {
}
