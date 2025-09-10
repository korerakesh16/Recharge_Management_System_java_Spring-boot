package com.webcont.lumen_project_1.repository;

import com.webcont.lumen_project_1.model.RechargePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargePlanRepository extends JpaRepository<RechargePlan, Long> {
    
    List<RechargePlan> findByIsActiveTrue();
    
    List<RechargePlan> findByPlanType(RechargePlan.PlanType planType);
    
    List<RechargePlan> findByIsActiveTrueAndPlanType(RechargePlan.PlanType planType);
    
    @Query("SELECT rp FROM RechargePlan rp WHERE rp.isActive = true ORDER BY rp.price ASC")
    List<RechargePlan> findActivePlansOrderByPrice();
    
    @Query("SELECT rp FROM RechargePlan rp WHERE rp.price BETWEEN ?1 AND ?2 AND rp.isActive = true")
    List<RechargePlan> findByPriceRangeAndActive(Double minPrice, Double maxPrice);
}
