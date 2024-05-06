package org.example;

import java.util.Random;

public class HopfieldNetworkTSP {
    private double[][] distances;
    private double[][] neurons;
    private int citiesCount;

    private double A, B;
    static final double R = 6371;

    /*
    *
    *https://github.com/search?q=tsp+hopfield+network&type=repositories
    *https://github.com/23BanKAI/ConsoleAlfredTSP/blob/master/ConsoleAlfredTSP/Program.cs
    *
    * */

    public HopfieldNetworkTSP(Route route, double A, double B) {
        this.distances = fillDistanceMatrix(route);
        this.citiesCount = route.getCountCitiesCount();
        this.neurons = new double[citiesCount][citiesCount];
        this.A = A;
        this.B = B;
        initializeNeurons();
    }

    public double[][] fillDistanceMatrix(Route route) {
        int count = route.getCountCitiesCount();
        double[][] distanceMatrix = new double[count][count];

        for (int i = 0; i < count; i++) {
            City currentCity = route.getCities().get(i);
            for (int j = 0; j < count; j++) {
                City calculatedCity = route.getCities().get(j);
                if (currentCity == calculatedCity) {
                    distanceMatrix[i][j] = 0;
                } else {
                    distanceMatrix[i][j] = R * Math.acos(Math.sin(currentCity.getX()) * Math.sin(calculatedCity.getX()) + Math.cos(currentCity.getY() - calculatedCity.getY()) * Math.cos(calculatedCity.getX()) * Math.cos(currentCity.getX()));
                }
            }
        }
        return distanceMatrix;
    }

    private void initializeNeurons() {
        Random random = new Random();
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                if (i == j) {
                    neurons[i][j] = 0;
                } else {
                    neurons[i][j] = random.nextBoolean() ? 1 : -1;
                }
            }
        }
    }

    public void updateNeurons() {
        double[][] newNeurons = new double[citiesCount][citiesCount];

        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                double sum = 0;
                for (int k = 0; k < citiesCount; k++) {
                    if (k != j) {
                        sum += distances[i][k] * neurons[i][k];
                    }
                }
                newNeurons[i][j] = sigmoid(-sum);
            }
        }
        neurons = newNeurons;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double energyFunction() {
        double energy = 0.0;
        double rowSum, colSum;

        // Calculate penalty for row constraints
        for (int x = 0; x < citiesCount; x++) {
            rowSum = 0;
            for (int j = 0; j < citiesCount; j++) {
                rowSum += neurons[x][j];
            }
            energy += A * Math.pow(1 - rowSum, 2);
        }

        // Calculate penalty for column constraints
        for (int j = 0; j < citiesCount; j++) {
            colSum = 0;
            for (int x = 0; x < citiesCount; x++) {
                colSum += neurons[x][j];
            }
            energy += B * Math.pow(1 - colSum, 2);
        }

        return energy;
    }
}


