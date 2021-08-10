package com.dajustock.prediction;

import com.dajustock.KNN.KNNException;
import com.dajustock.KNN.PredictMachine;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;

public class PredictMachineTest extends TestCase {

    PredictMachine predictMachine = new PredictMachine();

    @Test
    public void testDownLoadData() throws IOException {
        predictMachine.downLoadData("AAPL");
    }

    @Test
    public void testPredict() throws KNNException {
        predictMachine.predict("AAPL");
    }

}