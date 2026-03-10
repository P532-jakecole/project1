package com.project1.project1.Feed;


import com.project1.project1.Updating.FeedService;

public class Stock implements FeedObject {

    private String stockName;
    private double stockPrice;

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

        feedService.sendUpdate(this);
    }
}
