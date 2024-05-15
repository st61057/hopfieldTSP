package org.example.Genetic;

import org.example.Pub;

import java.util.ArrayList;

public class Path implements Comparable<Path> {

    static final double R = 6371;
    ArrayList<Pub> pubsList = new ArrayList<>();
    double distance;

    public Path(ArrayList<Pub> pubsList) {
        this.pubsList = pubsList;
        this.distance = calculateDistance();
    }

    public ArrayList<Pub> getPubsList() {
        return pubsList;
    }

    public double getDistance() {
        return distance;
    }

    public Path duplicatePath() {
        ArrayList<Pub> duplicatePath = new ArrayList<>();
        for (Pub pub : pubsList) {
            duplicatePath.add(pub);
        }
        return new Path(duplicatePath);
    }

    public double countDistance(Pub firstPub, Pub secondPub) {
        return R * Math.acos(Math.sin(firstPub.getLat()) * Math.sin(secondPub.getLat()) + Math.cos(firstPub.getLon() - secondPub.getLon()) * Math.cos(secondPub.getLat()) * Math.cos(firstPub.getLat()));
    }

    public double calculateDistance() {
        double distance = 0;
        for (int i = 0; i < pubsList.size() - 1; i++) {
            distance += countDistance(pubsList.get(i), pubsList.get(i + 1));
        }
        distance += countDistance(pubsList.get(pubsList.size() - 1), pubsList.get(0));
        return distance;
    }

    @Override
    public int compareTo(Path path) {
        double currentPath = this.distance;
        double comparedPath = path.calculateDistance();
        if (currentPath == comparedPath) {
            return 0;
        } else if (currentPath < comparedPath) {
            return -1;
        }
        return 1;
    }

}
