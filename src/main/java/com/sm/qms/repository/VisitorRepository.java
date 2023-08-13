package com.sm.qms.repository;

import com.sm.qms.model.database.TableVisitor;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface VisitorRepository extends CrudRepository<TableVisitor, String> {
    @Query("SELECT COUNT(*)FROM visitors where registrationDate = :registrationDate ALLOW FILTERING")
    @AllowFiltering
    int countByRegistrationDate(@Param("registrationDate")String registrationDate);

    @Query("SELECT * FROM visitors WHERE email = :email ALLOW FILTERING")
    @AllowFiltering
    Optional<TableVisitor> findByEmail(String email);
    @Query("SELECT * FROM visitors WHERE phone = :phone ALLOW FILTERING")
    @AllowFiltering
    Optional<TableVisitor> findByPhone(String phone);
}
