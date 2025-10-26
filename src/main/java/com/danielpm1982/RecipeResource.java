package com.danielpm1982;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestPath;

@Path("/recipe-app/recipes")
public class RecipeResource {
    @RestClient
    private RecipeService recipeService;
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipes(){
        return Response.status(Response.Status.OK)
                .entity(recipeService.getAllRecipesFromExternalAPI())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipeById(@RestPath Long id){
        return Response.status(Response.Status.OK)
                .entity(recipeService.getRecipeByIdFromExternalAPI(id))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
