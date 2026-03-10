package com.project1.project1.Feed;

import com.project1.project1.Pricing.Observer;

public interface FeedObject extends Observer {
    String getName();
    double getPrice();
}
