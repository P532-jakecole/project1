package com.project1.project1.Repository;

import com.project1.project1.Pricing.Market;
import com.project1.project1.Pricing.Observer;
import com.project1.project1.Trading.Order;
import com.project1.project1.Trading.OrderService;
import com.project1.project1.User.Holding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PendingOrders {
    @Autowired
    private Market market;
    @Autowired
    OrderService orderService;

    //private static final PendingOrders pending = new PendingOrders();
    private static final String FILE_NAME = "data/Pending.txt";
    private static final String NEW_LINE = System.lineSeparator();

    //private PendingOrders() {}

//    public static PendingOrders getPending(){
//        return pending;
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
            orderService.addPending(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAll() {
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

    public void removeOrder(Order order) {

        try{
            Path path = Paths.get(FILE_NAME);
            if(Files.exists(path)){
                List<String> orderList = Files.readAllLines(path, StandardCharsets.UTF_8);
                Iterator<String> orders = orderList.iterator();

                String[] orderDetail = order.getOrder().split(",");
                String time = orderDetail[orderDetail.length - 1];

                while(orders.hasNext()){
                    String data = orders.next();
                    String[] values = data.split(",");
                    if(values[values.length-1].equals(time)){
                        orders.remove();
                    }
                }

                List<String> updated = new ArrayList<>();
                updated.addAll(orderList);
                Files.write(path, updated, StandardCharsets.UTF_8);
            }

            orderService.removePending(order);

            if(order instanceof Observer){
                market.removeObserver((Observer) order);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
