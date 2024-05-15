package org.example.Genetic;

public class Pair {

    Path firstPath;
    Path secondPath;

    public Pair(Path firstPath, Path secondPath) {
        this.firstPath = firstPath;
        this.secondPath = secondPath;
    }

    public Path getFirstPath() {
        return firstPath;
    }

    public Path getSecondPath() {
        return secondPath;
    }
}
