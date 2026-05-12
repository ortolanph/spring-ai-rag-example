package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OscarRepository extends CrudRepository<OscarNominee, UUID> {
}