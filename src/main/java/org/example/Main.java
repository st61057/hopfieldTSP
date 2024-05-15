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

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

//        Loader loader = new Loader("Pubs.csv");
        ArrayList<Pub> defaultLoadedList = defaultPubs()/*.loadFile()*/;
        GeneticUtils geneticUtils = new GeneticUtils();
        Population defaultPopulation = geneticUtils.createInitialPopulation(2000, defaultLoadedList);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(defaultLoadedList, 10, 0.05, 5);
        System.out.println("Genetic algorithm result: " + geneticAlgorithm.geneticEvolution(defaultPopulation, 150).getDistance() + "\n");


        HillClimb hillClimb = new HillClimb(5);
        Route resultHillClimb = hillClimb.simpleHillClimb(new Route(defaultLoadedList), 10000);
        System.out.println("Hill climb result: " + hillClimb.calculateDistance(resultHillClimb) + "\n");

        Route route = new Route(defaultLoadedList);
        HopfieldNetwork hopfieldNetwork = new HopfieldNetwork(route, new HopfieldConfig());
        int[] resultHopfield = hopfieldNetwork.run(1000);
        if (resultHopfield != null) {
            HopfieldUtils hopfieldUtils = new HopfieldUtils(defaultLoadedList);
            System.out.println("Hopfield result : " + hopfieldUtils.calculateHopfieldDistance(resultHopfield));
        } else {
            System.out.println("Hopfield couldn't find result");
        }
    }

    public static ArrayList<Pub> defaultPubs() {
        ArrayList<Pub> list = new ArrayList<>();
        list.add(new Pub(50.05000269303701, 15.768336176013673));
        list.add(new Pub(50.049125834609114, 15.77038451285136));
        list.add(new Pub(50.05018546217326, 15.757313549882152));
        list.add(new Pub(50.046717426661246, 15.754617175997272));
        list.add(new Pub(50.03576605695895, 15.762019243426192));
        list.add(new Pub(50.03467723012164, 15.771524992175227));
        list.add(new Pub(50.02421671114737, 15.756960344438685));
        list.add(new Pub(50.02495206165636, 15.764044082525857));
        list.add(new Pub(50.02346243664456, 15.770900680760617));
        list.add(new Pub(50.01759701022399, 15.771852881056518));
        list.add(new Pub(50.01797412601187, 15.77541896794229));
        list.add(new Pub(50.02343863754608, 15.771401177529196));
        list.add(new Pub(50.026051159587894, 15.770670849174373));
        list.add(new Pub(50.02714786356811, 15.79104864236843));
        list.add(new Pub(50.03033939788676, 15.793528057715656));
        list.add(new Pub(50.03353706898886, 15.797945221413233));
        list.add(new Pub(50.03577981494942, 15.807045697220078));
        list.add(new Pub(50.040723844747006, 15.803131956784018));
        list.add(new Pub(50.041494584398244, 15.806638745420514));
        list.add(new Pub(50.042752237910584, 15.8080152597872));
        list.add(new Pub(50.04739072422009, 15.811329792124459));
        list.add(new Pub(50.04342695971905, 15.821612672502829));
        list.add(new Pub(50.04425556151654, 15.794663306562335));
        list.add(new Pub(50.03499634344097, 15.785070169595775));
        list.add(new Pub(50.03719943948137, 15.786845098709852));
        list.add(new Pub(50.0383680211611, 15.779020157587391));
        list.add(new Pub(50.039032984307156, 15.779653158914929));
        list.add(new Pub(50.03865226819249, 15.778287914526311));
        list.add(new Pub(50.03784332719955, 15.776875775603179));
        list.add(new Pub(50.037208771344226, 15.77560002082079));
        list.add(new Pub(50.03424882768571, 15.776802595957397));
        list.add(new Pub(50.03379426081544, 15.779864563397256));
        list.add(new Pub(50.033802456246995, 15.773529815136897));
        list.add(new Pub(50.03484736297126, 15.774282584819401));
        list.add(new Pub(50.03379190948971, 15.763214225767268));
        return list;
    }
}