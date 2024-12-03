package kz.nik.building.repository;

import kz.nik.building.model.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {
    List<Cost> findByProjectId(Long projectId);
}