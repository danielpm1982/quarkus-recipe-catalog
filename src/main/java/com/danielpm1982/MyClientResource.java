package com.danielpm1982;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/recipe-app/clients")
public class MyClientResource {
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyClients(){
        return Response.status(Response.Status.OK)
                .entity(MyClient.listAll())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyClientById(@RestPath Long id){
        MyClient myClient = MyClient.findById(id);
        if(myClient!=null) {
            return Response.status(Response.Status.OK)
                    .entity(myClient)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } else{
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR: No registry found for the requested id ! Try again with a valid id !")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
    @GET
    @Path("find-by-email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findMyClientsByEmail(@RestQuery String email){
        return Response.status(Response.Status.OK)
                .entity(MyClient.findClientByEmail(email))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
    @GET
    @Path("find-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findMyClientsByName(@RestQuery String name){
        return Response.status(Response.Status.OK)
                .entity(MyClient.findClientByName(name))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addMyClient(MyClient myClient){
        myClient.id=null;
        MyClient.persist(myClient); //myClient instance is auto-updated with the actual id from the DB
        return Response.status(Response.Status.CREATED)
                .entity(MyClient.findById(myClient.id))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
    @PUT
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateMyClient(MyClient myClientNew){
        MyClient myClientOld = MyClient.findById(myClientNew.id);
        if(myClientOld!=null && myClientNew.name!=null && myClientNew.email!=null && myClientNew.favoriteRecipeIdList!=null) {
            myClientOld.name=myClientNew.name;
            myClientOld.email=myClientNew.email;
            myClientOld.favoriteRecipeIdList=myClientNew.favoriteRecipeIdList;
            MyClient.persist(myClientOld);
            return Response.status(Response.Status.CREATED)
                    .entity(MyClient.findById(myClientOld.id))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } else{
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: No registry found for the requested id, or any of the required parameter values is missing at the request ! "+
                            "Use a valid id and pass all required parameter values !")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }

    }
    @PATCH
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response patchMyClient(MyClient myClientNew){
        MyClient myClientOld = MyClient.findById(myClientNew.id);
        if(myClientOld!=null) {
            if (myClientNew.name != null && !myClientNew.name.isBlank()) {
                myClientOld.name = myClientNew.name;
            }
            if (myClientNew.email != null && !myClientNew.email.isBlank()) {
                myClientOld.email = myClientNew.email;
            }
            if (myClientNew.favoriteRecipeIdList != null && !myClientNew.favoriteRecipeIdList.isEmpty()) {
                myClientOld.favoriteRecipeIdList = myClientNew.favoriteRecipeIdList;
            }
            MyClient.persist(myClientOld);
            return Response.status(Response.Status.CREATED)
                    .entity(MyClient.findById(myClientOld.id))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } else{
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR: No registry found for the requested id ! Try again with a valid id !")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteMyClient(@RestPath Long id){
        MyClient myClient = MyClient.findById(id);
        if(myClient!=null) {
            MyClient.deleteById(id);
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        } else{
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR: No registry found for the requested id ! Try again with a valid id !")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
}
