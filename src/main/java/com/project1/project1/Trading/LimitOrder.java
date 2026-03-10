package com.project1.project1.Trading;

import com.project1.project1.Notification.ConsoleNotify;
import com.project1.project1.Notification.NotificationService;
import com.project1.project1.User.Holding;
import com.project1.project1.Pricing.Market;
import com.project1.project1.Pricing.Observer;
import com.project1.project1.Repository.PendingOrders;
import com.project1.project1.Repository.TradeHistory;
import com.project1.project1.User.Portfolio;

import java.time.LocalDateTime;

public class LimitOrder implements Order, Observer {
    private String stock;
    private double price;
    private double quantity;
    private String action;
    private LocalDateTime timestamp;

    private Market market;
    private Portfolio portfolio;
    private PendingOrders pendingOrders;
    private NotificationService notificationService;
    private TradeHistory tradeHistory;

    public LimitOrder(String stock, double price, double quantity, String action, Market market,
                      PendingOrders pendingOrders, Portfolio portfolio, NotificationService notification, TradeHistory tradeHistory) {
        this.stock = stock;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.timestamp = LocalDateTime.now();

        this.market = market;
        this.pendingOrders = pendingOrders;
        this.portfolio = portfolio;
        this.notificationService = notification;
        this.tradeHistory = tradeHistory;

        market.registerObserver(this);
        pendingOrders.save(this);
    }

    @Override
    public void executeOrder() {
        // Reduces Balance by stock * price, Updates Portfolio to include new stock purchase
        if(action.equals("buy")){
            portfolio.reduceCashBalance(getValue());
            portfolio.addHolding(new Holding(getName(), getQuantity(), getPrice()));
        }else if(action.equals("sell")){
            portfolio.addCashBalance(getValue());
            portfolio.removeHolding(new Holding(getName(), getQuantity(), getPrice()));
        }

        // Send notification
        notificationService.sendNotification(String.format("trade,%s", getOrder()));
        // Remove from Observable list
        market.removeObserver(this);
        // Remove from Pending List
        pendingOrders.removeOrder(this);

        // Add stock to trade history table
        setTimestamp();
        tradeHistory.save(this);
    }

    public String getOrder() {
        //return "Limit," + stock + "," + action + "," + price + "," + quantity + "," + timestamp + "," + getValue();
        return String.format("Limit,%s,%s,%f,%f,%s,%f",stock,action,price,quantity,timestamp,getValue());
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTimestamp() {
        timestamp = LocalDateTime.now();
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    @Override
    public void update(double price) {
        if(action.equals("buy") && price < this.price){
            if(portfolio.getCashBalance() < (price*getQuantity())){
                notificationService.sendNotification(String.format("holding,%s", stock));
                // Remove from Observable list
                market.removeObserver(this);
                // Remove from Pending List
                pendingOrders.removeOrder(this);
            }else{
                setPrice(price);
                executeOrder();
            }
        }else if(action.equals("sell") && price > this.price){
            if(portfolio.getHolding(this.getName()) == null || portfolio.getHolding(this.getName()).getQuantity() < getQuantity()){
                notificationService.sendNotification(String.format("balance,%.2f,%.2f,%,2f", price, quantity,portfolio.getCashBalance()));
                // Remove from Observable list
                market.removeObserver(this);
                // Remove from Pending List
                pendingOrders.removeOrder(this);
            }else{
                setPrice(price);
                executeOrder();
            }
        }
    }
}
