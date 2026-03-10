package com.project1.project1.Pricing;

import com.project1.project1.Feed.FeedObject;
import com.project1.project1.Updating.FeedService;
import com.project1.project1.Updating.OrderService;
import com.project1.project1.Trading.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class Market implements Subject {
    @Autowired
    private FeedService feedService;
    @Autowired
    private OrderService orderService;

    //private static final Market market = new Market();
    private HashMap<Observer, PricingModel> observers;
//    private PricingModel pricingModel;
    private Market() {
        observers = new HashMap<>();
//        pricingModel = new RandomWalk();
    }

//    public static Market getInstance() {
//        return market;
//    }

    @Override
    public void registerObserver(Observer observer) {
        observers.put(observer, new RandomWalk(new Random()));
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        if (observer instanceof FeedObject object) {
            feedService.sendUpdate(object);
        }else if(observer instanceof Order order){
            orderService.sendUpdate(order);
        }
    }

    @Override
    public void notifyObserver() {
        HashMap<Observer, PricingModel> copy = new HashMap<>(observers);

        for (Observer observer : copy.keySet()) {
            PricingModel pricingModel = copy.get(observer);
            observer.update(pricingModel.updatePrice(observer));
            if (observer instanceof FeedObject object) {
                feedService.sendUpdate(object);
            }
        }
    }
}
