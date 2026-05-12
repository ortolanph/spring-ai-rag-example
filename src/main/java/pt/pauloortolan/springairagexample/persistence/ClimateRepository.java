package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ClimateRepository extends CrudRepository<Climate, UUID> {
}