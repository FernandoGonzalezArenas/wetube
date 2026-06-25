package com.wetube.admin.repository;

import com.wetube.admin.entity.AdminEntity;
import com.wetube.admin.entity.ReportEntity;
import com.wetube.admin.entity.ReportStatus;
import com.wetube.admin.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
List<ReportEntity> findByStatus(ReportStatus status);
List<ReportEntity> findByType(ReportType type);
List<ReportEntity> findByTargetIdAndTypeAndStatus(Long targetId, ReportType type, ReportStatus status);

}
