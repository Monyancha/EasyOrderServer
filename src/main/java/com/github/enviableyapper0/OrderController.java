package com.github.enviableyapper0;

import com.github.enviableyapper0.beans.FoodItem;
import com.github.enviableyapper0.beans.FoodType;
import com.github.enviableyapper0.beans.Order;
import com.github.enviableyapper0.dao.OrderDAO;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.MemoryImageSource;
import java.util.List;

@Path("order")
public class OrderController {
    /**
     * This method return a sample json object when called by GET request at /order/test
     * @return a sample object
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    public Order getTestFood() {
        Order order = new Order(42, 3);
        order.getFoods().add(new FoodItem("42", "Test Food", 30, FoodType.MAIN_DISH));
        return order;
    }

    /**
     * This method return a list of all order
     * @return list of all order
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getAllOrder() {
        return OrderDAO.getAllOrder();
    }

    /**
     * Get individual order
     * @param id
     * @return an object which the id that was specified with appropriate response
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(final @PathParam("id") int id) {
        Order toReturn = null;
        try {
            toReturn = OrderDAO.getOrder(id);
        } catch (IndexOutOfBoundsException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (toReturn == null) {
            return Response.status(Response.Status.GONE).build();
        }
        return Response.ok(toReturn, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Add an order to the list
     * @param got
     * @return An appropriate response with object id (as integer) in body
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processIt(Order got) {
        got.setId(OrderDAO.addOrder(got));
        return Response.status(Response.Status.CREATED).entity(got.getId()).build();
    }

    /**
     * Delete an object, id will not be recycled for the rest of server instance lifetime
     * @param id
     * @return an appropriate response
     */
    @DELETE
    @Path("{id}")
    public Response deleteOrder(final @PathParam("id") int id) {
        if (OrderDAO.deleteOrder(id)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Get the list of all order that has the specified table number
     * @param id
     * @return List of all Order that has the specified table number with appropriate response
     */
    @GET
    @Path("table/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersFromTable(final @PathParam("id") int id) {
        List<Order> orders = OrderDAO.getTableOrders(id);
        if (orders.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok().build();
    }

    /**
     * Delete all order that has the specified table number
     * @param id
     * @return an appropriate response
     */
    @DELETE
    @Path("table/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTableOrders(final @PathParam("id") int id) {
        if (OrderDAO.deleteTableOrders(id)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
