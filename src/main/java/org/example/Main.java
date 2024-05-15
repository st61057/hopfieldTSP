package org.example;

import org.example.Genetic.GeneticAlgorithm;
import org.example.Genetic.Loader;
import org.example.Genetic.Population;
import org.example.Genetic.GeneticUtils;
import org.example.HillClimb.HillClimb;
import org.example.Hopfield.HopfieldConfig;
import org.example.Hopfield.HopfieldNetwork;
import org.example.Hopfield.HopfieldUtils;
import org.example.Hopfield.Route;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Path p = Paths.get("Pubs.csv");
        Path folder = p.getParent();
        System.out.println(folder);

        Loader loader = new Loader("C:\\Users\\Niko\\Desktop\\HopfieldNetworkTSP\\src\\main\\java\\org\\example\\Pubs.csv");
        ArrayList<Pub> defaultLoadedList = loader.loadFile();
        GeneticUtils geneticUtils = new GeneticUtils();
        Population defaultPopulation = geneticUtils.createInitialPopulation(2000, defaultLoadedList);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(defaultLoadedList, 10, 0.05, 5);
        System.out.println("Genetic algorithm result: " + geneticAlgorithm.geneticEvolution(defaultPopulation, 150).getDistance());

        HillClimb hillClimb = new HillClimb(5);
        Route resultHillClimb = hillClimb.simpleHillClimb(new Route(defaultLoadedList), 100);
        System.out.println("Hill climb result: " + hillClimb.calculateDistance(resultHillClimb));

        Route route = new Route(defaultLoadedList);
        HopfieldNetwork hopfieldNetwork = new HopfieldNetwork(route, new HopfieldConfig());
        int[] resultHopfield = hopfieldNetwork.run(100);
        if (resultHopfield != null) {
            HopfieldUtils hopfieldUtils = new HopfieldUtils(defaultLoadedList);
            System.out.println("Hopfield result : " + hopfieldUtils.calculateHopfieldDistance(resultHopfield));
        }

    }
}