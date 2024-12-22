package com.cwc.certificate.repository;

import com.cwc.certificate.model.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog,Integer> {
    List<ActivityLog> findAllByOrderByActionTimestampDesc(Pageable pageable);
}
