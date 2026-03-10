package com.project1.project1.User;

public class Holding {
    private String name;
    private double quantity;
    private double price;

    public Holding(String name, double quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public double getValue(){
        return Math.round(quantity*price * 1000) / 1000.0;
    };
    public String getName(){
        return name;
    };
    public double getPrice(){
        return price;
    };
    public double getQuantity(){
        return quantity;
    };

    public void buyUpdate(Holding holding){
        avgPrice(holding.getPrice(), holding.getQuantity());
        addQuantity(holding.getQuantity());
    }

    public void sellUpdate(Holding holding){
        avgPrice(holding.getPrice(), holding.getQuantity());
        removeQuantity(holding.getQuantity());
    }

    private void addQuantity(double quantity){
        this.quantity += quantity;
    }

    public void removeQuantity(double quantity){
        this.quantity -= quantity;
    }

    private void avgPrice(double price, double quantity){
        this.price = ((this.price*this.quantity) + (price*quantity)) / (quantity+this.quantity);
    }

    public String toString(){
        return String.format("%s,%.3f,%.3f", name, quantity, price);
    }
}
