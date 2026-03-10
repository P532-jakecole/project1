package com.project1.project1.Pricing;

import java.util.Random;

public class RandomWalk implements PricingModel{
    private Random rand;

    public RandomWalk(Random rand) {
        this.rand = rand;
    }

    @Override
    public double updatePrice(Observer observer) {
        double price = observer.getPrice();
        double change = (rand.nextDouble() * 0.04) - 0.02;
        double newPrice = price + (price * change);

        return Math.round(newPrice * 1000.0) / 1000.0;
    }
}
