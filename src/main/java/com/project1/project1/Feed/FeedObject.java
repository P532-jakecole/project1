package com.project1.project1.Feed;

import com.project1.project1.Pricing.Observer;
import com.project1.project1.Pricing.Subject;

public interface FeedObject extends Observer, Subject {
    String getName();
    double getPrice();
}
