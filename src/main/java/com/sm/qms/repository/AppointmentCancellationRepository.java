package com.sm.qms.repository;

import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.sm.qms.model.database.TableAppointmentCancellation;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentCancellationRepository extends CrudRepository<TableAppointmentCancellation, UUID> {


}
