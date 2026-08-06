package com.teakter.notification.repository;

import com.teakter.notification.entity.EmailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLogEntity, Long> {
}
