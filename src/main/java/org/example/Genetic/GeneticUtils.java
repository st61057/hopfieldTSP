package org.example.Genetic;

import org.example.Pub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneticUtils {

    public Population createInitialPopulation(int count, ArrayList<Pub> defaultPubList) {
        List<Path> pathsList = new ArrayList<>();

        while (pathsList.size() < count) {
            ArrayList<Pub> pub = new ArrayList<>(defaultPubList);
            Collections.shuffle(pub);
            Path path = new Path(pub);
            if (!pathsList.contains(path)) {
                pathsList.add(path);
            }
        }

        Path[] solutions = pathsList.toArray(new Path[0]);
        return new Population(solutions);
    }
}

