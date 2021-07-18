package com.dajustock.KNN;


import com.dajustock.model.Distance;
import com.dajustock.model.Neighbour;
import com.dajustock.model.Price;

import java.util.ArrayList;
import java.util.Arrays;

public class KNNEngine {

    public Price classify(Price test, ArrayList<Price> train, int K){
        Neighbour[] neighbours = new Neighbour[train.size()];
        for (int i = 0; i < neighbours.length; i++){
            double distance = Distance.getDistance(train.get(i), test);
            double neighbourNextDayPrice = train.get(i).getNextDayClosePrice();
            neighbours[i] = new Neighbour(distance, neighbourNextDayPrice);
        }

        Arrays.sort(neighbours);

        double sumOfNeighbours = 0.0;

        for (int i = 0; i < K; i++) {
            sumOfNeighbours += neighbours[i].getPrice();
        }
        return new Price(test.getClosePrice(), sumOfNeighbours / K);
    }

    public double leastSquare(ArrayList<Price> prediction, ArrayList<Price> train) throws KNNException {
        double leastSquare = 0.0;
        if (!(prediction.size() == train.size())) {
            throw new KNNException("Test and Training set must have same size");
        }
        for (int i = 0; i < prediction.size(); i++) {
            double priceDifference = prediction.get(i).getNextDayClosePrice() - train.get(i).getNextDayClosePrice();
            leastSquare += Math.pow(priceDifference, 2);
        }
        return leastSquare;
    }
}
