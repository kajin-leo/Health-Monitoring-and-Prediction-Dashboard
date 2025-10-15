package com.cs79_1.interactive_dashboard.Controller;

import com.cs79_1.interactive_dashboard.DTO.DietaryIntake.FoodIntakeByCategory;
import com.cs79_1.interactive_dashboard.DTO.DietaryIntake.FoodIntakeResultDto;
import com.cs79_1.interactive_dashboard.Entity.WeeklyIntake;
import com.cs79_1.interactive_dashboard.Repository.WeeklyIntakeRepository;
import com.cs79_1.interactive_dashboard.Security.SecurityUtils;
import com.cs79_1.interactive_dashboard.Service.StaticInfoService.FoodIntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/food-intake")
public class FoodIntakeController {

    @Autowired
    private FoodIntakeService foodIntakeService;

    @Autowired
    private WeeklyIntakeRepository weeklyIntakeRepository;

    @GetMapping("/rings")
    public FoodIntakeResultDto getFoodIntakeRings() {
        long userId = SecurityUtils.getCurrentUserId();
        return foodIntakeService.calculateFoodIntake(userId);
    }

    @GetMapping("/intake-by-category")

    public List<FoodIntakeByCategory> getWeeklyIntakeByUser() {

        long userId = SecurityUtils.getCurrentUserId();
        WeeklyIntake intake = weeklyIntakeRepository.findByUserId(userId);
        List<FoodIntakeByCategory> result = new ArrayList<>();
        result.add(new FoodIntakeByCategory("Grains", intake.getCereals()));
        result.add(new FoodIntakeByCategory("Vegetables", intake.getVegetablesAndLegumes()));
        result.add(new FoodIntakeByCategory("Fruit", intake.getFruit()));
        result.add(new FoodIntakeByCategory("Dairy", intake.getDairy()));
        result.add(new FoodIntakeByCategory("Meat", intake.getMeatFishPoultryEggs()));
        return result;
    }
}
