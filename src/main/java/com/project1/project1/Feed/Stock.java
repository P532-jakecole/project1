package com.project1.project1.Feed;


import com.project1.project1.Pricing.Observer;
import com.project1.project1.Updating.FeedService;

import java.util.ArrayList;

public class Stock implements FeedObject {

    private String stockName;
    private double stockPrice;

    private ArrayList<Observer> observers = new ArrayList<>();

    private FeedService feedService;

    public Stock(String stockName, double stockPrice, FeedService feedService) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.feedService = feedService;
    }

    public String getName() {
        return stockName;
    }

    public double getPrice(){
        return stockPrice;
    }

    public void update(double price){
        this.stockPrice = price;
        notifyObserver(price);

        feedService.sendUpdate(this);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {}

    public void notifyObserver(double price) {
        if(observers.size() > 0){
            for (Observer observer : observers) {
                observer.update(price);
            }
        }
    }
}
