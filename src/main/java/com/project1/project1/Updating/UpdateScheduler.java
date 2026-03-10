package com.project1.project1.Updating;

import com.project1.project1.Pricing.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateScheduler {
    @Autowired
    private Market market;

    @Scheduled(fixedRate = 5000)
    public void updatePrice() {
        market.notifyObserver();
    }
}
