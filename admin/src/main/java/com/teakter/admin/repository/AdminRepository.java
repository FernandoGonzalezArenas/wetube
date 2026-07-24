package com.teakter.admin.repository;

import com.teakter.admin.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long>  {
}
