package com.github.enviableyapper0.dao;

import com.github.enviableyapper0.beans.FoodItem;
import com.github.enviableyapper0.beans.FoodType;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class FoodDAO {
    private Connection dbConnection;
    private Statement statement;

    public FoodDAO() throws SQLException {
        this.dbConnection = DriverManager.getConnection("jdbc:sqlite:EasyOrder.db");
        this.statement = this.dbConnection.createStatement();
    }

    public List<FoodItem> getAllFood() throws SQLException {
        List<FoodItem> foodItemList = new LinkedList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM Food");

        while (rs.next()) {
            foodItemList.add(new FoodItem(rs.getInt(1), rs.getString(2),
                    rs.getDouble(4), FoodType.values()[rs.getInt(3)]));
        }
        rs.close();
        return foodItemList;
    }

    @Override
    protected void finalize() throws Throwable {
        try{
            statement.close();
        } catch (Exception e) { /*Nothing*/ }
        try{
            dbConnection.close();
        } catch (Exception e) { /*Nothing*/ }
    }
}
