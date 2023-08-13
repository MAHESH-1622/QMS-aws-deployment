package com.sm.qms.repository;

import com.sm.qms.model.database.TableAdmin;
import com.sm.qms.model.database.TableStore;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<TableAdmin, String> {

@AllowFiltering
    Optional<TableAdmin> findByEmail(String email);
@AllowFiltering
    Optional<TableAdmin> findByPhone(String phone);

@AllowFiltering
    List<TableAdmin> findByStoreId(String storeId);
@AllowFiltering
    Optional<TableStore> findById(List<String> adminId);
}
