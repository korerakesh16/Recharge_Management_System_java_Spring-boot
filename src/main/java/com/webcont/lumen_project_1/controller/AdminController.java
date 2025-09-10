package com.webcont.lumen_project_1.controller;
import com.webcont.lumen_project_1.model.RechargePlan;
import com.webcont.lumen_project_1.model.User;
import com.webcont.lumen_project_1.service.RechargePlanService;
import com.webcont.lumen_project_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RechargePlanService rechargePlanService;

    // User Management
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get users: " + e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                User foundUser = user.get();
                foundUser.setPassword(null);
                return ResponseEntity.ok(foundUser);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get user");
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            createdUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        try {
            List<User> users = userService.searchUsers(query);
            users.forEach(user -> user.setPassword(null));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Recharge Plan Management
    @GetMapping("/plans")
    public ResponseEntity<List<RechargePlan>> getAllPlans() {
        try {
            List<RechargePlan> plans = rechargePlanService.getAllPlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<?> getPlanById(@PathVariable Long id) {
        try {
            Optional<RechargePlan> plan = rechargePlanService.getPlanById(id);
            if (plan.isPresent()) {
                return ResponseEntity.ok(plan.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get plan");
        }
    }

    @PostMapping("/plans")
    public ResponseEntity<?> createPlan(@RequestBody RechargePlan plan) {
        try {
            RechargePlan createdPlan = rechargePlanService.createPlan(plan);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create plan");
        }
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<?> updatePlan(@PathVariable Long id, @RequestBody RechargePlan plan) {
        try {
            RechargePlan updatedPlan = rechargePlanService.updatePlan(id, plan);
            return ResponseEntity.ok(updatedPlan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update plan");
        }
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        try {
            rechargePlanService.deletePlan(id);
            return ResponseEntity.ok("Plan deactivated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete plan");
        }
    }

    @PutMapping("/plans/{id}/activate")
    public ResponseEntity<?> activatePlan(@PathVariable Long id) {
        try {
            rechargePlanService.activatePlan(id);
            return ResponseEntity.ok("Plan activated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to activate plan");
        }
    }

    // Dashboard Statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getAdminStats() {
        try {
            Long totalUsers = userService.getTotalUserCount();
            List<RechargePlan> allPlans = rechargePlanService.getAllPlans();
            List<RechargePlan> activePlans = rechargePlanService.getAllActivePlans();
            
            Map<String, Object> stats = Map.of(
                "totalUsers", totalUsers,
                "totalPlans", allPlans.size(),
                "activePlans", activePlans.size(),
                "inactivePlans", allPlans.size() - activePlans.size()
            );
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get admin stats");
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> adminHome() {
        return ResponseEntity.ok("Welcome to Telecom Inventory System - Admin Panel");
    }
}
