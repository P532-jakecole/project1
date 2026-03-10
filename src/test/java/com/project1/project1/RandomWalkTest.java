package com.project1.project1;

import com.project1.project1.Pricing.Observer;
import com.project1.project1.Pricing.RandomWalk;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomWalkTest {

    @Mock
    Observer observer;

    @Mock
    Random random;

    @Test
    void updatePriceWithNoChange() {

        // Arrange
        when(observer.getPrice()).thenReturn(100.0);
        when(random.nextDouble()).thenReturn(0.5);

        RandomWalk model = new RandomWalk(random);

        // Act
        double price = model.updatePrice(observer);

        // Assert
        assertEquals(100.0, price);
    }

    @Test
    void updatePriceWithPositiveChange() {

        // Arrange
        when(observer.getPrice()).thenReturn(100.0);
        when(random.nextDouble()).thenReturn(0.8);

        RandomWalk model = new RandomWalk(random);

        // Act
        double price = model.updatePrice(observer);

        // Assert
        assertEquals(101.2, price);
    }

    @Test
    void updatePriceWithNegativeChange() {

        // Arrange
        when(observer.getPrice()).thenReturn(100.0);
        when(random.nextDouble()).thenReturn(0.1);

        RandomWalk model = new RandomWalk(random);

        // Act
        double price = model.updatePrice(observer);

        // Assert
        assertEquals(98.4, price);
    }
}
