package com.webcont.lumen_project_1.service;

import com.webcont.lumen_project_1.model.Recharge;
import com.webcont.lumen_project_1.model.RechargePlan;
import com.webcont.lumen_project_1.model.User;
import com.webcont.lumen_project_1.repository.RechargeRepository;
import com.webcont.lumen_project_1.repository.RechargePlanRepository;
import com.webcont.lumen_project_1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private RechargePlanRepository rechargePlanRepository;

    @Autowired
    private UserRepository userRepository;

    public Recharge performRecharge(Long userId, Long planId, String paymentMethod) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<RechargePlan> planOpt = rechargePlanRepository.findById(planId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (planOpt.isEmpty()) {
            throw new RuntimeException("Recharge plan not found");
        }

        User user = userOpt.get();
        RechargePlan plan = planOpt.get();

        if (!plan.getIsActive()) {
            throw new RuntimeException("Selected plan is not active");
        }

        // Check if user has sufficient balance (if implementing wallet system)
        if (user.getBalance() < plan.getPrice()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct amount from user balance
        user.setBalance(user.getBalance() - plan.getPrice());
        userRepository.save(user);

        // Create recharge record
        Recharge recharge = new Recharge(user, plan, plan.getPrice(), paymentMethod);
        return rechargeRepository.save(recharge);
    }

    @Transactional(readOnly = true)
    public List<Recharge> getUserRechargeHistory(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return rechargeRepository.findByUserOrderByRechargeDateDesc(userOpt.get());
    }

    public List<Recharge> getActiveRecharges(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return rechargeRepository.findActiveRechargesByUser(userOpt.get(), LocalDateTime.now());
    }

    public Optional<Recharge> getRechargeByTransactionId(String transactionId) {
        return rechargeRepository.findByTransactionId(transactionId);
    }

    public List<Recharge> getAllRecharges() {
        return rechargeRepository.findAll();
    }

    public void updateExpiredRecharges() {
        List<Recharge> expiredRecharges = rechargeRepository.findExpiredRecharges(LocalDateTime.now());
        for (Recharge recharge : expiredRecharges) {
            recharge.setStatus(Recharge.Status.EXPIRED);
            rechargeRepository.save(recharge);
        }
    }

    public Long getTotalRechargeCount(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return rechargeRepository.countRechargesByUser(userOpt.get());
    }

    public Double getTotalAmountSpent(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Double total = rechargeRepository.getTotalAmountSpentByUser(userOpt.get());
        return total != null ? total : 0.0;
    }

    public Recharge cancelRecharge(Long rechargeId) {
        Optional<Recharge> rechargeOpt = rechargeRepository.findById(rechargeId);
        if (rechargeOpt.isEmpty()) {
            throw new RuntimeException("Recharge not found");
        }

        Recharge recharge = rechargeOpt.get();
        if (recharge.getStatus() != Recharge.Status.ACTIVE) {
            throw new RuntimeException("Only active recharges can be cancelled");
        }

        // Refund amount to user balance
        User user = recharge.getUser();
        user.setBalance(user.getBalance() + recharge.getAmountPaid());
        userRepository.save(user);

        // Update recharge status
        recharge.setStatus(Recharge.Status.CANCELLED);
        return rechargeRepository.save(recharge);
    }
}
