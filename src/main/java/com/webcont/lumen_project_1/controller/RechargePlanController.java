package com.webcont.lumen_project_1.controller;

import com.webcont.lumen_project_1.model.RechargePlan;
import com.webcont.lumen_project_1.service.RechargePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/plans")
public class RechargePlanController {

    @Autowired
    private RechargePlanService rechargePlanService;

    @GetMapping("/")
    public ResponseEntity<List<RechargePlan>> getAllActivePlans() {
        try {
            List<RechargePlan> plans = rechargePlanService.getAllActivePlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<RechargePlan>> getAllPlans() {
        try {
            List<RechargePlan> plans = rechargePlanService.getAllPlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
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

    @GetMapping("/type/{planType}")
    public ResponseEntity<List<RechargePlan>> getPlansByType(@PathVariable RechargePlan.PlanType planType) {
        try {
            List<RechargePlan> plans = rechargePlanService.getPlansByType(planType);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<RechargePlan>> getPlansByPriceRange(
            @RequestParam Double minPrice, 
            @RequestParam Double maxPrice) {
        try {
            List<RechargePlan> plans = rechargePlanService.getPlansByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/sorted-by-price")
    public ResponseEntity<List<RechargePlan>> getPlansOrderByPrice() {
        try {
            List<RechargePlan> plans = rechargePlanService.getPlansOrderByPrice();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
