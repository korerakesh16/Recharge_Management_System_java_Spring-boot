package com.webcont.lumen_project_1.controller;

import com.webcont.lumen_project_1.model.Recharge;
import com.webcont.lumen_project_1.model.RechargePlan;
import com.webcont.lumen_project_1.model.User;
import com.webcont.lumen_project_1.service.RechargeService;
import com.webcont.lumen_project_1.service.RechargePlanService;
import com.webcont.lumen_project_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RechargePlanService rechargePlanService;

    @Autowired
    private RechargeService rechargeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            if (user != null) {
                // Clear password before sending response
                user.setPassword(null);
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user);
            // Clear password before sending response
            createdUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/plans")
    public ResponseEntity<List<RechargePlan>> getAllRechargePlans() {
        try {
            List<RechargePlan> plans = rechargePlanService.getAllActivePlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/plans/type/{planType}")
    public ResponseEntity<List<RechargePlan>> getPlansByType(@PathVariable RechargePlan.PlanType planType) {
        try {
            List<RechargePlan> plans = rechargePlanService.getPlansByType(planType);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/recharges")
    public ResponseEntity<List<Recharge>> getUserRechargeHistory(@PathVariable Long userId) {
        try {
            List<Recharge> recharges = rechargeService.getUserRechargeHistory(userId);
            return ResponseEntity.ok(recharges);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/active-recharges")
    public ResponseEntity<List<Recharge>> getActiveRecharges(@PathVariable Long userId) {
        try {
            List<Recharge> activeRecharges = rechargeService.getActiveRecharges(userId);
            return ResponseEntity.ok(activeRecharges);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{userId}/recharge")
    public ResponseEntity<?> performRecharge(@PathVariable Long userId, @RequestBody Map<String, Object> rechargeRequest) {
        try {
            Long planId = Long.valueOf(rechargeRequest.get("planId").toString());
            String paymentMethod = rechargeRequest.get("paymentMethod").toString();
            
            Recharge recharge = rechargeService.performRecharge(userId, planId, paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED).body(recharge);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Recharge failed");
        }
    }

    @PostMapping("/{userId}/add-balance")
    public ResponseEntity<?> addBalance(@PathVariable Long userId, @RequestBody Map<String, Double> balanceRequest) {
        try {
            Double amount = balanceRequest.get("amount");
            User updatedUser = userService.addBalance(userId, amount);
            updatedUser.setPassword(null);
            updatedUser.setRecharges(null); // Avoid lazy loading serialization issues
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add balance");
        }
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long userId) {
        try {
            Long totalRecharges = rechargeService.getTotalRechargeCount(userId);
            Double totalSpent = rechargeService.getTotalAmountSpent(userId);
            
            Map<String, Object> stats = Map.of(
                "totalRecharges", totalRecharges,
                "totalAmountSpent", totalSpent
            );
            
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get user stats");
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to Telecom Inventory System - User API");
    }
}