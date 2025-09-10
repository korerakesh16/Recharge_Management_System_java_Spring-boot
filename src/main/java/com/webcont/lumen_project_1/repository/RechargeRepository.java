package com.webcont.lumen_project_1.repository;

import com.webcont.lumen_project_1.model.Recharge;
import com.webcont.lumen_project_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    
    @Query("SELECT r FROM Recharge r JOIN FETCH r.plan WHERE r.user = ?1 ORDER BY r.rechargeDate DESC")
    List<Recharge> findByUserOrderByRechargeDateDesc(User user);
    
    List<Recharge> findByUserAndStatus(User user, Recharge.Status status);
    
    Optional<Recharge> findByTransactionId(String transactionId);
    
    @Query("SELECT r FROM Recharge r WHERE r.user = ?1 AND r.status = 'ACTIVE' AND r.expiryDate > ?2")
    List<Recharge> findActiveRechargesByUser(User user, LocalDateTime currentDate);
    
    @Query("SELECT r FROM Recharge r WHERE r.expiryDate < ?1 AND r.status = 'ACTIVE'")
    List<Recharge> findExpiredRecharges(LocalDateTime currentDate);
    
    @Query("SELECT COUNT(r) FROM Recharge r WHERE r.user = ?1")
    Long countRechargesByUser(User user);
    
    @Query("SELECT SUM(r.amountPaid) FROM Recharge r WHERE r.user = ?1")
    Double getTotalAmountSpentByUser(User user);
}
