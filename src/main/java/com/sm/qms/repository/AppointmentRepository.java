package com.sm.qms.repository;

import com.sm.qms.model.database.TableAppointment;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends CrudRepository<TableAppointment, UUID> {

//    Iterable<TableAppointment> findByDate(String date);

    @Query("SELECT * from appointments where date = ?0 ALLOW FILTERING")
    Iterable<TableAppointment> findByDate(String date);
    @AllowFiltering
    Iterable<TableAppointment> findByDateAndStoreId(String date, String storeId);

    List<TableAppointment> findByDateAndVisitorIdAndStoreId(String date, String visitorId, String storeId);
}
