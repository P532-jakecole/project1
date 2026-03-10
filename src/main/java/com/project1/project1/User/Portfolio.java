package com.project1.project1.User;

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
import java.util.Iterator;
import java.util.List;

@Service
public class Portfolio {
    @Autowired
    OrderService orderService;

    //private static final Portfolio account = new Portfolio();
    private double cashBalance = 1000;
    private double portfolioBalance = 1000;
    private static final String FILE_NAME = "data/Holdings.txt";
    private static final String NEW_LINE = System.lineSeparator();

    private static void appendToFile(Path path, String content)
            throws IOException {
        content += NEW_LINE;
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public void setCashBalance(double balance) {
        this.cashBalance = balance;
    }

    public void setPortfolioBalance(double portfolioBalance) {
        this.portfolioBalance = portfolioBalance;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void reduceCashBalance(double amount) {
        cashBalance -= amount;
    }

    public void addCashBalance(double amount) {
        cashBalance += amount;
    }

    public List<Holding> getAllHoldings() {
        ArrayList<Holding> holding = new ArrayList<>();

        try{
            Path path = Paths.get(FILE_NAME);
            if(Files.exists(path)){
                List<String> orderList = Files.readAllLines(path, StandardCharsets.UTF_8);
                for(String line : orderList){
                    String[] values = line.split(",");
                    Holding h = new Holding(values[0], Double.parseDouble(values[1]), Double.parseDouble(values[2]));
                    holding.add(h);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return holding;
    }


    public Holding getHolding(String name) {

        Path path = Paths.get(FILE_NAME);
        List<Holding> holdings = getAllHoldings();

        for(Holding h : holdings){
            if(h.getName().equals(name)){
                return h;
            }
        }
        return null;
    }

    public void addHolding(Holding holding) {
        try {

            Path path = Paths.get(FILE_NAME);
            List<Holding> holdings = getAllHoldings();
            boolean found = false;

            for(Holding h : holdings){
                if(h.getName().equals(holding.getName())){
                    h.buyUpdate(holding);
                    found = true;
                    break;
                }
            }

            orderService.addHolding(holding);
            orderService.updateBalance(userInformation());

            if(!found){
                appendToFile(path, holding.toString());
                return;
            }
            Files.write(path, new byte[0]);

            for(Holding h : holdings){
                appendToFile(path, h.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeHolding(Holding holding) {
        try {


            Path path = Paths.get(FILE_NAME);
            List<Holding> holdings = getAllHoldings();
            Iterator<Holding> iterator = holdings.iterator();

            while(iterator.hasNext()){
                Holding h = iterator.next();

                if(h.getName().equals(holding.getName())){
                    h.sellUpdate(holding);
                    orderService.removeHolding(h);
                    if(h.getQuantity() == 0){
                        iterator.remove();
                    }
                    break;
                }
            }
            portfolioBalance = cashBalance - holding.getValue();
            orderService.updateBalance(userInformation());

            List<String> updated = new ArrayList<>();
            for(Holding h : holdings){
                updated.add(h.toString());
            }
            Files.write(path, updated);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getPortfolioBalance(){
        return portfolioBalance;
    }

    public String userInformation(){
        return String.format("%.2f,%.2f", cashBalance, portfolioBalance);
    }
}
