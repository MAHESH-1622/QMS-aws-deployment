package com.sm.qms.repository;

import com.sm.qms.model.database.TableSuperAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminRepository extends CrudRepository<TableSuperAdmin, String> {
}
