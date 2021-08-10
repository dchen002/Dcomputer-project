package com.dajustock.model;

public class Distance {
    //Calculating the difference between two prices by subtracting the two numbers is also known as distance in the knn algorithm
    public static double getDistance(Price a, Price b){
        return Math.abs(a.getClosePrice() - b.getClosePrice());
    }
}
