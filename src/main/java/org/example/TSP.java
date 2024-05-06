package org.example;

public class TSP {

        private int n; // Number of cities
        private double[][] M; // City matrix
        private double[][] C; // Cost matrix
        private double gamma1; // Weighting constant for constraint I
        private double gamma2; // Weighting constant for constraint II

        public TSP(int numberOfCities, double[][] costMatrix, double g1, double g2) {
            this.n = numberOfCities;
            this.C = costMatrix;
            this.gamma1 = g1;
            this.gamma2 = g2;
            this.M = new double[n][n];
        }

        // Initialize M with random values close to 0 or 1
        public void initializeNetwork() {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    M[i][j] = Math.random() > 0.5 ? 1 : 0;
                }
            }
        }

        // Calculate the energy of the network
        public double calculateEnergy() {
            double energy = 0;

            // Constraint I energy calculation
            for (int x = 0; x < n; x++) {
                double sumRow = 0;
                for (int j = 0; j < n; j++) {
                    sumRow += M[x][j];
                }
                energy += gamma1 * Math.pow(1 - sumRow, 2);
            }

            // Constraint II energy calculation
            for (int j = 0; j < n; j++) {
                double sumCol = 0;
                for (int x = 0; x < n; x++) {
                    sumCol += M[x][j];
                }
                energy += gamma2 * Math.pow(1 - sumCol, 2);
            }

            // Cost function energy calculation
            for (int i = 0; i < n; i++) {
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < n; y++) {
                        if (x != y) {
                            energy += 0.5 * C[x][y] * M[x][i] * (M[y][(i + 1) % n] + M[y][(i - 1 + n) % n]);
                        }
                    }
                }
            }

            return energy;
        }
}
