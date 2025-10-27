package com.danielpm1982;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Readiness
public class ReadinessCheck implements HealthCheck {
    @RestClient
    private RecipeService recipeService;
    @Override
    public HealthCheckResponse call() {
        if(recipeService.getAllRecipesFromExternalAPI().contains(RecipeService.MESSAGE_ERROR_JSON_STRING)||
                recipeService.getRecipeByIdFromExternalAPI(1L).contains(RecipeService.MESSAGE_ERROR_JSON_STRING)){
            return HealthCheckResponse.down("Readiness Check");
        } else{
            return HealthCheckResponse.up("Readiness Check");
        }
    }
}
