package com.project1.project1.Trading;

public interface Order{
    void executeOrder();
    String getOrder();
    double getPrice();
    String getName();
    double getQuantity();
}
