package org.example.Hopfield;

import org.example.Pub;

import java.util.Arrays;
import java.util.Random;

public class HopfieldNetwork {
    private double[][] distances;
    private double[][] neurons;
    private int citiesCount;
    public static final double R = 6371;

    private HopfieldConfig hopfieldConfig;

    public HopfieldNetwork(Route route, HopfieldConfig hopfieldConfig) {
        this.distances = normalizeMatrix(fillDistanceMatrix(route));
        this.citiesCount = route.getCountCitiesCount();
        this.neurons = initializeNeurons(citiesCount);
        this.hopfieldConfig = hopfieldConfig;
    }

    //normalizace => když používám parametry z toho důvodu nemám normalizované data
    public static double[][] normalizeMatrix(double[][] distanceMatrix) {
        double maxDistance = Arrays.stream(distanceMatrix).flatMapToDouble(Arrays::stream).max().orElse(1.0);
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                distanceMatrix[i][j] /= maxDistance;
            }
        }
        return distanceMatrix;
    }

    public double[][] fillDistanceMatrix(Route route) {
        int count = route.getCountCitiesCount();
        double[][] distanceMatrix = new double[count][count];

        for (int i = 0; i < count; i++) {
            Pub currentPub = route.getCities().get(i);
            for (int j = 0; j < count; j++) {
                Pub calculatedPub = route.getCities().get(j);
                if (currentPub == calculatedPub) {
                    distanceMatrix[i][j] = 0;
                } else {
                    distanceMatrix[i][j] = R * Math.acos(Math.sin(currentPub.getLat()) * Math.sin(calculatedPub.getLat())
                            + Math.cos(currentPub.getLon() - calculatedPub.getLon())
                            * Math.cos(calculatedPub.getLat()) * Math.cos(currentPub.getLat()));
                }
            }
        }
        return distanceMatrix;
    }

    private double[][] initializeNeurons(int size) {
        double[][] neurons = new double[size][size];
        Random random = new Random(795);
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                neurons[i][j] = random.nextBoolean() ? 1 : -1;
            }
        }
        return neurons;
    }

    public int[] run(int maxIterations) {
        for (int i = 1; i <= maxIterations; i++) {
            update();
            double currentEnergy = energyFunction();
            System.out.println(String.format("Energy: %.6f, Iteration: %d", currentEnergy, i));
        }
        int[] tour = decodeTour();
        if (tour != null) {
            System.out.println("Successfully decoded TSP tour: " + Arrays.toString(tour));
            return tour;
        } else {
            System.out.println("Unable to find solution");
            return null;
        }
    }

    private int[] decodeTour() {
        double[][] activations = activations();
        int[] tour = new int[citiesCount];
        for (int j = 0; j < citiesCount; j++) {
            int maxIdx = 0;
            for (int i = 1; i < citiesCount; i++) {
                if (activations[i][j] > activations[maxIdx][j]) {
                    maxIdx = i;
                }
            }
            tour[j] = maxIdx;
        }
        if (Arrays.stream(tour).distinct().count() != citiesCount) {
            return null;
        }
        return tour;
    }

    private void update() {
        double[][] inputChange = new double[citiesCount][citiesCount];
        for (int city = 0; city < citiesCount; city++) {
            for (int position = 0; position < citiesCount; position++) {
                inputChange[city][position] = hopfieldConfig.getStep() * getChange(city, position);
            }
        }
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                neurons[i][j] += inputChange[i][j];
            }
        }
    }

    private double getChange(int city, int position) {
        double newState = -neurons[city][position];
        double a_result = hopfieldConfig.getA() * (Arrays.stream(activations()[city]).sum() - sigmoid(neurons[city][position]));
        double b_result = hopfieldConfig.getB() * (Arrays.stream(activations()).mapToDouble(row -> row[position]).sum() - sigmoid(neurons[city][position]));
        double d_result = hopfieldConfig.getD() * _neighbor_weights(city, position);
        return newState - a_result - b_result - d_result;
    }

    private double _neighbor_weights(int city, int position) {
        double sum = 0.0;
        for (int neighbor = 0; neighbor < citiesCount; neighbor++) {
            int nextPos = (position + 1) % citiesCount;
            int prevPos = (position - 1 + citiesCount) % citiesCount;
            double preceding = sigmoid(neurons[neighbor][nextPos]);
            double following = sigmoid(neurons[neighbor][prevPos]);
            sum += distances[city][neighbor] * (preceding + following);
        }
        return sum;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x / hopfieldConfig.getConvergence())); // mění konvergenci k hraničním hodnotám
    }

    private double[][] activations() {
        double[][] result = new double[citiesCount][citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                result[i][j] = sigmoid(neurons[i][j]);
            }
        }
        return result;
    }

    public double energyFunction() {
        double[][] activations = activations();
        double rowSum, colSum;

        // Penalizace za řádky
        double temporaryEnergy = 0.0;
        for (int x = 0; x < citiesCount; x++) {
            rowSum = 0;
            for (int j = 0; j < citiesCount; j++) {
                rowSum += activations[x][j];
            }
            temporaryEnergy += Math.pow(1 - rowSum, 2);
        }
        double rowsResult = 0.5 * hopfieldConfig.getA() * temporaryEnergy;

        // Penalizace za sloupce
        temporaryEnergy = 0.0;
        for (int j = 0; j < citiesCount; j++) {
            colSum = 0;
            for (int x = 0; x < citiesCount; x++) {
                colSum += activations[x][j];
            }
            temporaryEnergy += Math.pow(1 - colSum, 2);
        }
        double columnsResult = 0.5 * hopfieldConfig.getB() * temporaryEnergy;

        // Penalizace za vzdálenosti
        temporaryEnergy = 0.0;
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                int nextJ = (j + 1) % citiesCount;
                temporaryEnergy += activations[i][j] * distances[i][nextJ] * activations[i][nextJ];
            }
        }
        double distanceResult = hopfieldConfig.getD() * temporaryEnergy;

        return rowsResult + columnsResult + distanceResult;
    }
}


