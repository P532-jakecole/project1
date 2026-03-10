package com.project1.project1.Repository;

import com.project1.project1.Trading.Order;
import com.project1.project1.Updating.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeHistory {
    @Autowired
    OrderService orderService;

    //private static final TradeHistory history = new TradeHistory();
    private static final String FILE_NAME = "data/History.txt";
    private static final String NEW_LINE = System.lineSeparator();

    //private TradeHistory() {}

//    public static TradeHistory getHistory(){
//        return history;
//    }

    private static void appendToFile(Path path, String content)
            throws IOException {
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public void save(Order order) {
        try {
            Path path = Paths.get(FILE_NAME);
            String data = order.getOrder() + NEW_LINE;
            appendToFile(path, data);
            orderService.sendHistory(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getOrders() {
        ArrayList<String> orders = new ArrayList<>();
        try{
            Path path = Paths.get(FILE_NAME);
            if(Files.exists(path)){
                List<String> orderList = Files.readAllLines(path, StandardCharsets.UTF_8);
                orders.addAll(orderList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}
