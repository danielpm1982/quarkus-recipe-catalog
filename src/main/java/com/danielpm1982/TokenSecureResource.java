package com.danielpm1982;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/recipe-app/secured/")
@RequestScoped
public class TokenSecureResource {
    @Inject
    JsonWebToken jwt;
    @Claim(standard = Claims.birthdate)
    String birthdate;
    @GET
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(@Context SecurityContext ctx) {
        return getResponseString(ctx);
    }
    @GET
    @Path("roles-allowed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User", "Admin"})
    public Response helloRolesAllowed(@Context SecurityContext ctx){
        return getResponseString2(ctx);
    }
    private Response getResponseString(SecurityContext ctx) {
        String name;
        if(ctx.getUserPrincipal() == null) {
            name="anonymous";
        } else if(!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new RuntimeException("Principal and JsonWebToken names do not match !");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        String responseMessage = String.format("Hello, %s ! isHttps: %s, authScheme: %s, hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
        JsonObject responseJson = Json.createObjectBuilder()
                .add("responseMessage", responseMessage)
                .build();
        return Response.status(Response.Status.OK).
                entity(responseJson).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    private Response getResponseString2(SecurityContext ctx) {
        String name;
        if(ctx.getUserPrincipal() == null) {
            name="anonymous";
        } else if(!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new RuntimeException("Principal and JsonWebToken names do not match !");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
//        String responseMessage = String.format("Hello, %s ! isHttps: %s, authScheme: %s, hasJWT: %s, birthdate: %s",
//                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt(), jwt.getClaim("birthdate").toString());
        String responseMessage = String.format("Hello, %s ! isHttps: %s, authScheme: %s, hasJWT: %s, birthdate: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt(), birthdate);
        JsonObject responseJson = Json.createObjectBuilder()
                .add("responseMessage", responseMessage)
                .build();
        return Response.status(Response.Status.OK).
                entity(responseJson).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    private boolean hasJwt() {
        return jwt.getClaimNames()!=null;
    }
}

/*
For accessing any secured endpoint above, you gotta have a valid bearer token based on the public/private RSA keys,
available at the paths set at the application.properties. For generating a valid token, run the main method of the
GenerateToken class and copy the generated token to your clipboard. Later, when you call the endpoints above that
demand a security token, pass the token value at the "" header of the Http request, by setting it at the call when
using the Swagger interface or the curl CLI interface. For instance:
*/
//curl -X 'GET' 'http://localhost:8080/recipe-app/secured/roles-allowed' -H 'accept: */*' -H 'Authorization: Bearer [tokenValue]'
