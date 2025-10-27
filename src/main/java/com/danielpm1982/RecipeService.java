package com.danielpm1982;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;
import java.time.temporal.ChronoUnit;

@RegisterRestClient(baseUri = "https://dummyjson.com/")
public interface RecipeService {
    public static final String MESSAGE_ERROR_JSON_STRING = "{\"error\": \"Fallback !\"}";
    @GET
    @Path("recipes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(value = 3, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getAllRecipesFromExternalAPIFallback")
    @CircuitBreaker(requestVolumeThreshold = 2, failureRatio = 0.5, delay = 3L, delayUnit = ChronoUnit.SECONDS,
            successThreshold = 2)
    String getAllRecipesFromExternalAPI();
    @SuppressWarnings("unused")
    default String getAllRecipesFromExternalAPIFallback() {
        return MESSAGE_ERROR_JSON_STRING;
    }
    @GET
    @Path("recipes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(value = 3, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getRecipeByIdFromExternalAPIFallback")
    @CircuitBreaker(requestVolumeThreshold = 2, failureRatio = 0.5, delay = 3L, delayUnit = ChronoUnit.SECONDS,
            successThreshold = 2)
    String getRecipeByIdFromExternalAPI(@RestPath Long id);
    @SuppressWarnings("unused")
    default String getRecipeByIdFromExternalAPIFallback(@RestPath Long id) {
        return MESSAGE_ERROR_JSON_STRING;
    }
}
