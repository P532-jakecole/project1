package com.project1.project1.Repository;

import com.project1.project1.Trading.Order;
import com.project1.project1.Trading.OrderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRepository {
    private static final String DATABASE_NAME = "data/History.txt";
    @Autowired
    OrderFactory orderFactory;

    public OrderRepository() {
        File ducksImagesDirectory = new File("data/");
        if(!ducksImagesDirectory.exists()) {
            ducksImagesDirectory.mkdirs();
        }
        File orderFile = new File("data/History.txt");
        if(!orderFile.exists()) {
            try{
                orderFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File holdingFile = new File("data/Holdings.txt");
        if(!orderFile.exists()) {
            try{
                holdingFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File pendingFile = new File("data/Pending.txt");
        if(!orderFile.exists()) {
            try{
                pendingFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final String NEW_LINE = System.lineSeparator();
    private static void appendToFile(Path path, String content)
            throws IOException {
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public void save(String[] order) throws IOException {
        orderFactory.createOrder(order[0], order[1], order[2], Double.parseDouble(order[3]), Double.parseDouble(order[4]));
    }


    public List<String> findAll() throws IOException {
        Path path = Paths.get(DATABASE_NAME);
        List<String> data = Files.readAllLines(path);
        return data;
    }
}
