package com.dajustock.KNN;


import com.dajustock.model.Distance;
import com.dajustock.model.Neighbour;
import com.dajustock.model.Price;

import java.util.ArrayList;
import java.util.Arrays;

public class KNNEngine {

    public Price classify(Price test, ArrayList<Price> train, int K){
        //Construct a list of Neighbour data structures to store distances and prices.
        Neighbour[] neighbours = new Neighbour[train.size()];
        for (int i = 0; i < neighbours.length; i++){
            //Calculate the distance between test data and training group data
            double distance = Distance.getDistance(train.get(i), test);
            double neighbourNextDayPrice = train.get(i).getNextDayClosePrice();
            //Add distances and prices to the list neighbours
            neighbours[i] = new Neighbour(distance, neighbourNextDayPrice);
        }
        //Arrange the data in the list neighbours from smallest to largest
        Arrays.sort(neighbours);

        double sumOfNeighbours = 0.0;
        //Add up the prices in neighbours
        for (int i = 0; i < K; i++) {
            sumOfNeighbours += neighbours[i].getPrice();
        }
        //the prediction value is the value of sumOfNeighbours / K.
        return new Price(test.getClosePrice(), sumOfNeighbours / K);
    }

    //Calculate the value of lastSquare
    public double leastSquare(ArrayList<Price> prediction, ArrayList<Price> train) throws KNNException {
        double leastSquare = 0.0;
        if (!(prediction.size() == train.size())) {
            throw new KNNException("Test and Training set must have same size");
        }
        for (int i = 0; i < prediction.size(); i++) {
            //Subtract the forecast price from the actual price to get the price difference
            double priceDifference = prediction.get(i).getNextDayClosePrice() - train.get(i).getNextDayClosePrice();
            //Square the price difference to get the value of lastSquare
            leastSquare += Math.pow(priceDifference, 2);
        }
        return leastSquare;
    }
}
