package com.webcont.lumen_project_1.service;

import com.webcont.lumen_project_1.model.RechargePlan;
import com.webcont.lumen_project_1.repository.RechargePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RechargePlanService {

    @Autowired
    private RechargePlanRepository rechargePlanRepository;

    public List<RechargePlan> getAllActivePlans() {
        return rechargePlanRepository.findByIsActiveTrue();
    }

    public List<RechargePlan> getAllPlans() {
        return rechargePlanRepository.findAll();
    }

    public Optional<RechargePlan> getPlanById(Long id) {
        return rechargePlanRepository.findById(id);
    }

    public List<RechargePlan> getPlansByType(RechargePlan.PlanType planType) {
        return rechargePlanRepository.findByIsActiveTrueAndPlanType(planType);
    }

    public List<RechargePlan> getPlansOrderByPrice() {
        return rechargePlanRepository.findActivePlansOrderByPrice();
    }

    public List<RechargePlan> getPlansByPriceRange(Double minPrice, Double maxPrice) {
        return rechargePlanRepository.findByPriceRangeAndActive(minPrice, maxPrice);
    }

    public RechargePlan createPlan(RechargePlan plan) {
        return rechargePlanRepository.save(plan);
    }

    public RechargePlan updatePlan(Long id, RechargePlan updatedPlan) {
        Optional<RechargePlan> existingPlan = rechargePlanRepository.findById(id);
        if (existingPlan.isPresent()) {
            RechargePlan plan = existingPlan.get();
            plan.setName(updatedPlan.getName());
            plan.setDescription(updatedPlan.getDescription());
            plan.setPrice(updatedPlan.getPrice());
            plan.setValidityDays(updatedPlan.getValidityDays());
            plan.setData(updatedPlan.getData());
            plan.setVoice(updatedPlan.getVoice());
            plan.setSms(updatedPlan.getSms());
            plan.setPlanType(updatedPlan.getPlanType());
            plan.setIsActive(updatedPlan.getIsActive());
            return rechargePlanRepository.save(plan);
        }
        throw new RuntimeException("Plan not found with id: " + id);
    }

    public void deletePlan(Long id) {
        Optional<RechargePlan> plan = rechargePlanRepository.findById(id);
        if (plan.isPresent()) {
            RechargePlan existingPlan = plan.get();
            existingPlan.setIsActive(false); // Soft delete
            rechargePlanRepository.save(existingPlan);
        } else {
            throw new RuntimeException("Plan not found with id: " + id);
        }
    }

    public void activatePlan(Long id) {
        Optional<RechargePlan> plan = rechargePlanRepository.findById(id);
        if (plan.isPresent()) {
            RechargePlan existingPlan = plan.get();
            existingPlan.setIsActive(true);
            rechargePlanRepository.save(existingPlan);
        } else {
            throw new RuntimeException("Plan not found with id: " + id);
        }
    }
}
