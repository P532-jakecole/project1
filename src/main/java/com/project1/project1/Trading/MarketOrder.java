package com.project1.project1.Trading;

import com.project1.project1.Notification.ConsoleNotify;
import com.project1.project1.User.Holding;
import com.project1.project1.Notification.NotificationService;
import com.project1.project1.Repository.TradeHistory;
import com.project1.project1.User.Portfolio;

import java.time.LocalDateTime;

public class MarketOrder implements Order{
    private String stock;
    private double price;
    private double quantity;
    private String action;
    private LocalDateTime timestamp;

    private Portfolio portfolio;

    public MarketOrder(String stock, double price, double quantity, String action, Portfolio portfolio) {
        this.stock = stock;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.portfolio = portfolio;
    }

    @Override
    public void executeOrder() {
        // Changes Balance by stock * price
        if(action.equals("buy")){
            portfolio.reduceCashBalance(getValue());
            portfolio.addHolding(new Holding(getName(), getQuantity(), getPrice()));
        }else if(action.equals("sell")){
            portfolio.addCashBalance(getValue());
            portfolio.removeHolding(new Holding(getName(), getQuantity(), getPrice()));
        }
    }

    public String getOrder() {
        return String.format("Market,%s,%s,%f,%f,%s,%f",stock,action,price,quantity,timestamp,getValue());
    }

    public double getValue() {
        return Math.round(price * quantity * 1000.0) / 1000.0;
    }

    public String getName() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public String getAction() {
        return action;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

}
