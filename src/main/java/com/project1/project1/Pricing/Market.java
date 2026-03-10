package com.project1.project1.Pricing;

import com.project1.project1.Feed.FeedObject;
import com.project1.project1.Updating.FeedService;
import com.project1.project1.Updating.OrderService;
import com.project1.project1.Trading.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class Market implements Subject {
    @Autowired
    private FeedService feedService;
    @Autowired
    private OrderService orderService;

    private HashMap<Observer, PricingModel> observers;

    private Market() {
        observers = new HashMap<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.put(observer, new RandomWalk(new Random()));
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        if (observer instanceof FeedObject object) {
            feedService.sendUpdate(object);
        }
        if(observer instanceof Order order){
            orderService.sendUpdate(order);
        }
    }

    @Override
    public void notifyObserver() {
        HashMap<Observer, PricingModel> copy = new HashMap<>(observers);

        for (Map.Entry<Observer, PricingModel> entry : copy.entrySet()) {

            Observer observer = entry.getKey();
            PricingModel pricingModel = entry.getValue();

            if(pricingModel == null) continue;

            double newPrice = pricingModel.updatePrice(observer);

            observer.update(newPrice);

            if (observer instanceof FeedObject object) {
                feedService.sendUpdate(object);
            }
        }
    }
}
