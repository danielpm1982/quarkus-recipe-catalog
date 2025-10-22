package com.danielpm1982;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;

@Path("/")
public class GreetingResource {
    private final Logger logger;
    public GreetingResource() {
        logger = LoggerFactory.getLogger(GreetingResource.class);
    }
    @GET
    @Path("recipe-app")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(@QueryParam("from-path") String fromPath) {
        StringBuilder msgStringBuilder = new StringBuilder();
        if(fromPath != null && !fromPath.isBlank()) {
            msgStringBuilder.append("Redirected from path: \"").append(fromPath).append("\" ! ");
        }
        msgStringBuilder.append("Hello World from Quarkus REST ! ");
        msgStringBuilder.append("danielpm1982.com");
        final Value value;
        try(Context polyglot = Context.create()){
            value = polyglot.eval("python", """
                                    from datetime import datetime, timezone
                                    datetime_utc = datetime.now(timezone.utc)
                                    datetime_utc.strftime("%A, %d, %B %Y %I:%M%p")
                                    """);
            if(value != null && !value.asString().isBlank()){
                msgStringBuilder.append(" - Python DateTime now (UTC): ").append(value).append(" !");
            }
        } catch (Exception e){
            logger.error("Error while executing Python command ! {}: {}", e.getClass(), e.getMessage());
        }
        JsonObject responseJson = Json.createObjectBuilder()
                .add("responseMessage", msgStringBuilder.toString())
                .build();
        return Response.status(Response.Status.OK)
                .entity(responseJson)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
    @Path("recipe-app/hello")
    @GET
    public Response hello1() {
        return buildRedirectBaseResponse("/recipe-app/hello");
    }
    @Path("recipe-app/welcome")
    @GET
    public Response hello2() {
        return buildRedirectBaseResponse("/recipe-app/welcome");
    }
    @Path("recipe-app/index")
    @GET
    public Response hello3() {
        return buildRedirectBaseResponse("/recipe-app/index");
    }
    @Path("")
    @GET
    public Response hello4() {
        return buildRedirectBaseResponse("/");
    }
    private Response buildRedirectBaseResponse(String fromPath) {
        String baseEndpoint = "/recipe-app"+"?from-path="+fromPath;
        return Response.status(Response.Status.PERMANENT_REDIRECT)
                .location(URI.create(baseEndpoint))
                .build();
    }
}
