package org.example.Hopfield;

import org.example.Pub;

import java.util.ArrayList;

import static org.example.Hopfield.HopfieldNetwork.R;

public class HopfieldUtils {

    private ArrayList<Pub> pubs;

    public HopfieldUtils(ArrayList<Pub> pubs) {
        this.pubs = pubs;
    }

    public double calculateHopfieldDistance(int[] tour) {

        Route route = getRouteResult(tour);
        double distance = 0;
        for (int i = 0; i < route.getCities().size() - 1; i++) {
            distance += countDistance(route.getCities().get(i), route.getCities().get(i + 1));
        }
        distance += countDistance(route.getCities().get(route.getCities().size() - 1), route.getCities().get(0));
        return distance;
    }

    private double countDistance(Pub firstPub, Pub secondPub) {
        return R * Math.acos(Math.sin(firstPub.getLat()) * Math.sin(secondPub.getLat()) + Math.cos(firstPub.getLon() - secondPub.getLon()) * Math.cos(secondPub.getLat()) * Math.cos(firstPub.getLat()));
    }

    private Route getRouteResult(int[] tour) {
        ArrayList<Pub> foundedSolution = new ArrayList<>();
        for (int i = 0; i < tour.length; i++) {
            foundedSolution.add(pubs.get(tour[i]));
        }
        return new Route(foundedSolution);
    }
}
