package com.sm.qms.repository;

import com.sm.qms.model.database.TableFeedback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface FeedbackRepository extends CrudRepository<TableFeedback, UUID> {
}
