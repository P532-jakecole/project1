package com.project1.project1.Pricing;

public interface Observer {
    void update(double price);
    double getPrice();
    String getName();
}
