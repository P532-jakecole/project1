package com.project1.project1;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.project1.project1.Feed.Feed;
import com.project1.project1.Feed.FeedObject;
import com.project1.project1.Feed.Stock;
import com.project1.project1.Notification.ConsoleNotify;
import com.project1.project1.Notification.NotificationService;
import com.project1.project1.Pricing.Market;
import com.project1.project1.Repository.PendingOrders;
import com.project1.project1.Repository.TradeHistory;
import com.project1.project1.Trading.MarketOrder;
import com.project1.project1.Trading.Order;
import com.project1.project1.Trading.OrderFactory;
import com.project1.project1.Updating.FeedService;
import com.project1.project1.User.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
class ConsoleNotifyTest {

    @Mock
    FeedService feedService;

    @Mock
    Feed feed;

    @Mock
    Market market;

    @Mock
    PendingOrders pendingOrders;

    @Mock
    Portfolio portfolio;

    @Mock
    NotificationService notificationService;

    @Mock
    TradeHistory tradeHistory;

    ConsoleNotify consoleNotify;

    OrderFactory orderFactory;

    @BeforeEach
    void setup() {
        consoleNotify = new ConsoleNotify(notificationService);

        orderFactory = new OrderFactory(
                feed,
                market,
                pendingOrders,
                portfolio,
                notificationService,
                tradeHistory
        );
    }

    @Test
    void tradeMessageIsDispatchedToNotificationService() {

        // Arrange
        String message = "trade,BUY,AAPL,Market,100.00,2";

        // Act
        consoleNotify.sendNotification(message);

        // Assert
        verify(notificationService).sendNotification(message);
    }

    @Test
    void orderSuccessfulMessageIsDispatchedToNotificationService() {
        // Arrange
        FeedObject stock = new Stock("AAPL", 150.0, feedService);

        when(feed.getObject("AAPL")).thenReturn(stock);
        when(portfolio.getCashBalance()).thenReturn(200.0);

        // Act
        Order order = orderFactory.createOrder("Market", "buy", "AAPL", 150.0, 1);

        // Assert
        verify(notificationService).sendNotification(
                String.format("trade,%s", order.getOrder())
        );
    //    assertEquals(consoleNotify.sendNotification(String.format("balance,%.2f,%.2f", 150.0, 1.0)), "Market order of type buy: AAPL At 150.00 for 1.00 shares.");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {

            // Act
            consoleNotify.sendNotification(
                    String.format("trade,%s", order.getOrder())
            );

            // Assert
            String output = outputStream.toString().trim();
            assertTrue(output.contains("Market order of type buy: AAPL At 150.00 for 1.00 shares."));

        } finally {

            System.setOut(originalOut);
        }
    }

    @Test
    void orderUnsuccessfulMessageIsDispatchedToNotificationService() {
        // Arrange
        FeedObject stock = new Stock("AAPL", 150.0, feedService);

        when(feed.getObject("AAPL")).thenReturn(stock);
        when(portfolio.getCashBalance()).thenReturn(100.0);

        // Act
        Order order = orderFactory.createOrder("Market", "buy", "AAPL", 150.0, 1);

        // Assert
        assertNull(order);
        verify(notificationService).sendNotification(
                String.format("balance,%.2f,%.2f,%.2f", 150.0, 1.0,100.0)
        );


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {

            // Act
            consoleNotify.sendNotification(
                    String.format("balance,%.2f,%.2f,%.2f", 150.0, 1.0,100.0)
            );

            // Assert
            String output = outputStream.toString().trim();
            System.setOut(originalOut);
            System.out.println(output);
            assertTrue(output.contains("Error: Order couldn't be filled as you have insufficient funds. Balance: 100.00 Order Cost: 150.0"));

        } finally {

            System.setOut(originalOut);
        }
    }
}
