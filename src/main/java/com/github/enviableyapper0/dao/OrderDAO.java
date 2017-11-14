package com.github.enviableyapper0.dao;

import com.github.enviableyapper0.beans.FoodItem;
import com.github.enviableyapper0.beans.FoodType;
import com.github.enviableyapper0.beans.Order;

import javax.ws.rs.NotFoundException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class OrderDAO {
    private Connection dbConnection;
    private Statement statement;

    public OrderDAO() throws SQLException {
        this.dbConnection = DriverManager.getConnection("jdbc:sqlite:EasyOrder.db");
        this.statement = this.dbConnection.createStatement();
    }

    public List<Order> getAllOrder() throws SQLException {
        return getQueriedListOfOrders("SELECT OrderId, TableNum, FoodId, Name, Price, Type, Quantity FROM OrderInstance INNER JOIN Food ON OrderInstance.Id = Food.id");
    }

    public int addOrder(Order order) throws SQLException {
        order.setId(getHighestOrderId() + 1);
        for (FoodItem food : order.getFoods()) {
            statement.execute("INSERT INTO OrderInstance (OrderId, TableNum, FoodId, Quantity) VALUES (" +
                    order.getId() + ", " +
                    order.getTableNum() + "," +
                    food.getID() + "," +
                    food.getQuantity()
                    + ")");
        }
        return order.getId();
    }

    public Order getOrder(int id) throws SQLException, NotFoundException {
        ResultSet rs = statement.executeQuery("SELECT OrderId, TableNum, FoodId, Name, Price, Type, Quantity FROM OrderInstance INNER JOIN Food ON OrderInstance.Id = Food.id WHERE OrderId = " + id);
        if (rs.next()) {
            if (getHighestOrderId() <= id){
                return null;
            } else {
                throw new NotFoundException();
            }
        }
        Order order = new Order(rs.getInt(1), rs.getInt(2));

        do {
            order.getFoods().add(new FoodItem(rs.getInt(3), rs.getString(4), rs.getDouble(5), FoodType.values()[rs.getInt(6)]));
        } while (rs.next());
        return order;
    }

    public void deleteOrder(int id) throws SQLException {
        statement.execute("DELETE FROM OrderInstance WHERE OrderId = " + id);
    }

    public List<Order> getTableOrders(int tableNum) throws SQLException {
        return getQueriedListOfOrders("SELECT OrderId, TableNum, FoodId, Name, Price, Type, Quantity FROM OrderInstance INNER JOIN Food ON OrderInstance.Id = Food.id WHERE TableNum = " + tableNum);
    }

    public void deleteTableOrders(int tableNum) throws SQLException {
        statement.execute("DELETE FROM OrderInstace WHERE TableNum = " + tableNum);
    }

    public boolean deleteIndividualFoodItem(int orderId, int foodIndex) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT id FROM OrderInstance WHERE orderId = " + orderId);
        for (int i = 0; i <= foodIndex; i++) {
            if (!rs.next()) {
                return false;
            }
        }
        statement.execute("DELETE FROM OrderInstace WHERE id = " + rs.getInt(1));
        return true;
    }

    private int getHighestOrderId() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT OrderId FROM OrderInstance ORDER BY OrderId DESC LIMIT 1;");
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    private List<Order> getQueriedListOfOrders(String sql) throws SQLException {
        List<Order> orders = new LinkedList<>();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            Order order = new Order(rs.getInt(1), rs.getInt(2));
            if (orders.contains(order)) {
                order = orders.get(orders.indexOf(order));
            }
            order.getFoods().add(new FoodItem(rs.getInt(3), rs.getString(4), rs.getDouble(5), FoodType.values()[rs.getInt(6)]));
        }
        return orders;
    }
}
