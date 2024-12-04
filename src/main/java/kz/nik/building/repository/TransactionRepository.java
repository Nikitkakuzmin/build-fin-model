package kz.nik.building.repository;

import kz.nik.building.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByProjectId(Long projectId);
    List<Transaction> findByProjectIdOrderByDateAsc(Long projectId);
}