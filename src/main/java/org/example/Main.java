package org.example;


import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        double alpha = 1;
        double beta = 1;

        ArrayList<City> list = new ArrayList<>();
        list.add(new City(50.05000269303701, 15.768336176013673));
        list.add(new City(50.049125834609114, 15.77038451285136));
        list.add(new City(50.05018546217326, 15.757313549882152));
        list.add(new City(50.02343863754608, 15.771401177529196));
        Route route = new Route(list);

        HopfieldNetworkTSP hopfieldNetworkTSP = new HopfieldNetworkTSP(route, alpha, beta);

    }
}