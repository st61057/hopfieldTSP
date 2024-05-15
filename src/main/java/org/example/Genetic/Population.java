package org.example.Genetic;

public class Population {
    Path[] solutions;

    public Population(int size) {
        this.solutions = new Path[size];
    }

    public Population(Path[] solutions) {
        this.solutions = solutions;
    }

    public void addPath(Path path, int position) {
        solutions[position] = path;
    }

    public Path calculateBestRoute() {
        double totalDistance = 0;

        Path bestPathFound = null;
        double bestPathDistance = Double.MAX_VALUE;

        for (int i = 0; i < solutions.length - 1; i++) {

            Path pathSolution = solutions[i];

            for (int j = 0; j < pathSolution.getPubsList().size() - 1; j++) {
                totalDistance += pathSolution.countDistance(pathSolution.getPubsList().get(j), pathSolution.getPubsList().get(j + 1));
            }

            if (totalDistance < bestPathDistance) {
                bestPathFound = solutions[i].duplicatePath();
                bestPathDistance = bestPathFound.getDistance();
            }
        }

        return bestPathFound;
    }

}
