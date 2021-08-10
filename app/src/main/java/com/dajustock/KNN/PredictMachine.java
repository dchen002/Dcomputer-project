package com.dajustock.KNN;

import com.dajustock.model.Price;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class PredictMachine {
    //historicalQuotes is used to store the historical data acquired.
    List<BigDecimal> historicalQuotes = new ArrayList<>();
    ArrayList<Price> prices = new ArrayList<>();
    //list predictions is used to store predictions.
    ArrayList<Price> predictions = new ArrayList<>();
    //Instantiating knnengine
    KNNEngine knnEngine = new KNNEngine();
    public String predict(String symbol) throws KNNException {
        //Download historical data and store the historical closing prices in a Price data structure by reading the stock code entered by the user.
        try {
            prepareData(symbol);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (prices.isEmpty()) {
            return "Seems " + symbol + "is not a valid symbol, try again.";
        }
        //kAndLeastSquare is used to store the k value and the LeastSquare value.
        Map<Integer, Double> kAndLeastSquare = new HashMap<>();
        //Use the for loop to make changes to the k values in the knn algorithm, where a k value between 1 and 10 will be chosen in this project.
        for (int k = 1; k <= 10; k++) {
            //Delete all elements in predictions.
            predictions.clear();
            //A for loop is used to train each loop with different historical closing prices as a training group to produce different LeastSquare values.
            for (int index = 0; index < prices.size(); index++) {
                //Use prices(index) as the test group.
                Price test = prices.get(index);
                ArrayList<Price> training = new ArrayList<>(prices);
                //Use all data within prices except prices(index) as the training group.
                training.remove(index);
                //Store predicted value in list prediction.
                predictions.add(knnEngine.classify(test, training, k));
            }
            for (int i = 0; i < prices.size(); i++) {
                System.out.println(prices.get(i).toString() + "||" + predictions.get(i).toString());
            }
            kAndLeastSquare.put(k, knnEngine.leastSquare(prices, predictions));
        }


        for (int k = 1; k <= 10; k++) {
            System.out.println(k + " : " + kAndLeastSquare.get(k));
        }

        //The smallest LeastSquare is selected by comparing LeastSquares.
        double smallest = kAndLeastSquare.get(1);
        int kToUse = 1;
        for (int k = 2; k <= 10; k++) {
            if (kAndLeastSquare.get(k) < smallest) {
                //The k-value corresponding to the smallest LeastSquare after the comparison is used as the k-value required for subsequent calculations.
                smallest = kAndLeastSquare.get(k);
                kToUse = k;
            }
        }
        System.out.println(kToUse);
        //Making forecasts of future prices.
        Price lastPrice = new Price();
        lastPrice.setClosePrice(historicalQuotes.get(historicalQuotes.size() - 1).doubleValue());
        Price predictedPrice = knnEngine.classify(lastPrice, prices, kToUse);
        prices.clear();
        historicalQuotes.clear();
        return "Based on " + symbol + "'s close price: " + predictedPrice.getClosePrice()
                + ", the predicted price is " + predictedPrice.getNextDayClosePrice();
    }
    //Use the Yahoo API to download daily historical data and store the data in historicalQuotes.
    public void downLoadData(String symbol) throws IOException {

        Stock stock = YahooFinance.get(symbol, true);
        if (null == stock) {
            System.out.println("No data available for symbol " + symbol);
            return;
        }
        //Storage of historical data in historicalQuotes
        List<HistoricalQuote> historicalQuotes = stock.getHistory(Interval.DAILY);
        //Get the closing price in historical data
        for (HistoricalQuote quote : historicalQuotes) {
            this.historicalQuotes.add(quote.getClose());
        }
    }
    //The historical data obtained is stored in the Price data structure.
    public void prepareData(String symbol) throws IOException {
        //Run the downLoadData method to download historical closing price data.
        downLoadData(symbol);
        //Store the closing price in the data structure of Price.
        for (int i = 0; i < historicalQuotes.size() - 1; i++) {
            Price price = new Price(historicalQuotes.get(i).doubleValue(), historicalQuotes.get(i + 1).doubleValue());
            prices.add(price);
        }
    }
}
