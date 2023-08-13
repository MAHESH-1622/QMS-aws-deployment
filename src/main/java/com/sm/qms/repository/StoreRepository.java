package com.sm.qms.repository;

import com.sm.qms.model.database.TableAdmin;
import com.sm.qms.model.database.TableStore;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends CrudRepository<TableStore, String> {

    @AllowFiltering
    TableStore findByAdminIds(String adminId);

    @AllowFiltering
    TableStore findByAdminIdsContains(String adminId);
}
