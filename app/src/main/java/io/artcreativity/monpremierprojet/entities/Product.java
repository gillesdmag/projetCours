package io.artcreativity.monpremierprojet.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "price")
    public double price;
    @ColumnInfo(name = "quantityInStock")
    public double quantityInStock;
    @ColumnInfo(name = "alertQuantity")
    public double alertQuantity;
//    @ColumnInfo(name = "serverId")
//    public int serverId;

    public static final String TABLE_NAME = "products";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, " +
            "name VARCHAR(100), description TEXT, price REAL default 0, quantityInStock INTEGER default 0," +
            "alertQuantity INTEGER default 0 )";

    public Product() {
    }

    public Product(String name, String description, double price, double quantityInStock, double alertQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.alertQuantity = alertQuantity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

//    public int getServerId() {
//        return serverId;
//    }
//
//    public void setServerId(int serverId) {
//        this.serverId = serverId;
//    }

    public double getAlertQuantity() {
        return alertQuantity;
    }

    public void setAlertQuantity(double alertQuantity) {
        this.alertQuantity = alertQuantity;
    }

    @Override
    public String toString() {
        return name + " (" + price + "F CFA)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
