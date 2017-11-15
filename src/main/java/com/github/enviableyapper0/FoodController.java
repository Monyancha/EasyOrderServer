package com.github.enviableyapper0;

import com.github.enviableyapper0.dao.FoodDAO;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Singleton
@Path("food")
public class FoodController {
    FoodDAO foodDAO;

    public FoodController() throws SQLException {
        this.foodDAO = new FoodDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFood() {
        try {
            return Response.ok(foodDAO.getAllFood(), MediaType.APPLICATION_JSON).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
