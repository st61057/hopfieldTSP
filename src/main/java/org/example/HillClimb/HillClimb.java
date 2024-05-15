package org.example.HillClimb;

import org.example.Hopfield.Route;
import org.example.Pub;

import java.util.ArrayList;
import java.util.Random;

import static org.example.Hopfield.HopfieldNetwork.R;

public class HillClimb {

    Random random;

    public HillClimb(int seed) {
        this.random = new Random(seed);
    }

    public Route simpleHillClimb(Route route, int count) {
        Route currentBest = duplicateRoute(route.getCities());

        int counter = 0;
        do {
            Route temp = duplicateRoute(currentBest.getCities());
            mutation(temp);

            if (calculateDistance(temp) < calculateDistance(currentBest)) {
                currentBest = duplicateRoute(temp.getCities());
            }
            counter++;

        } while (counter < count);

        return currentBest;
    }

    public double calculateDistance(Route route) {
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

    private Route duplicateRoute(ArrayList<Pub> pubsList) {
        ArrayList<Pub> duplicatePath = new ArrayList<>();
        for (Pub pub : pubsList) {
            duplicatePath.add(pub);
        }
        return new Route(duplicatePath);
    }

    private void mutation(Route path) {
        int randomSwapPosition = getRandomNumber(0, path.getCities().size());
        Pub randomSelectedPub = path.getCities().get(randomSwapPosition);
        path.getCities().remove(randomSwapPosition);

        int tempPosition;
        do {
            tempPosition = getRandomNumber(0, path.getCities().size());
        } while (tempPosition == randomSwapPosition);
        path.getCities().add(tempPosition, randomSelectedPub);
    }

    private int getRandomNumber(int min, int max) {
        return new Random().nextInt((max - min)) + min;
    }

}
