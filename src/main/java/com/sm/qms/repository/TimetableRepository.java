package com.sm.qms.repository;

import com.sm.qms.model.database.TableTimetable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends CrudRepository<TableTimetable, String> {
    Optional<TableTimetable> findByStoreId(String storeId);

    List<TableTimetable> findAllByStoreId(String storeId);
}
