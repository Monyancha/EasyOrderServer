package com.github.enviableyapper0.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    private List<FoodItem> foods;
    private int id;
    private int tableNum;

    public Order() {
    }

    public Order(int id, int tableNum) {
        this.foods = new ArrayList<>();
        this.id = id;
        this.tableNum = tableNum;
    }

    public List<FoodItem> getFoodItems() {
        return foods;
    }

    public void setFoodItems(List<FoodItem> foods) {
        this.foods = foods;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "foods=" + foods +
                ", id=" + id +
                ", tableNum=" + tableNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
