package org.example.Genetic;

import org.example.Pub;

import java.util.*;

public class GeneticAlgorithm {

    int tournamentSize;
    GeneticUtils geneticUtils;
    ArrayList<Pub> defaultPubList;
    double elitismSize;
    Random random;

    public GeneticAlgorithm(ArrayList<Pub> defaultPubList, int tournamentSize, double elitismSize, int randomSeed) {
        this.defaultPubList = defaultPubList;
        geneticUtils = new GeneticUtils();
        this.tournamentSize = tournamentSize;
        this.elitismSize = elitismSize;
        this.random = new Random(randomSeed);
    }

    public Path geneticEvolution(Population population, int evolutionCount) {

        Path currentBestPath = population.calculateBestRoute();
        System.out.println("Default best way - " + currentBestPath.getDistance());
        for (int i = 0; i < evolutionCount; i++) {
            population = geneticAlgorithm(population);

            Path populationBestRoute = population.calculateBestRoute();
            if (populationBestRoute.getDistance() < currentBestPath.getDistance()) {
                currentBestPath = populationBestRoute.duplicatePath();
            }
            System.out.println("Evolution count: " + i + " distance: " + currentBestPath.getDistance());
        }
        return currentBestPath;
    }

    public Population geneticAlgorithm(Population population) {
        Population newPopulation = new Population(population.solutions.length);

        int counter = 0;

        for (int i = 0; i < (population.solutions.length / 2); i++) {
            Path firstParent = selection(population);
            Path secondParent = selection(population);
            Pair children = crossover(firstParent, secondParent);

            newPopulation.addPath(children.getFirstPath(), counter++);
            newPopulation.addPath(children.getSecondPath(), counter++);
        }

        if ((population.solutions.length % 2) == 1) {
            Path firstParent = selection(population);
            Path secondParent = selection(population);
            Pair children = crossover(firstParent, secondParent);
            newPopulation.addPath(children.getFirstPath(), counter++);
        }

        ArrayList<Path> temp = elitismSolutions(population.solutions);
        Set<Integer> uniqueNumbers = new HashSet<>();
        while (uniqueNumbers.size() < temp.size()) {
            int randomNumber = random.nextInt((population.solutions.length - 1));
            uniqueNumbers.add(randomNumber);
        }

        Iterator<Integer> iterator = uniqueNumbers.iterator();
        int counterE = 0;
        while (iterator.hasNext()) {
            newPopulation.solutions[iterator.next()] = temp.get(counterE++);
        }

        for (int i = 0; i < newPopulation.solutions.length; i++) {
            if (random.nextInt(2) == 0) {
                mutation(newPopulation.solutions[i]);
            }
        }

        return newPopulation;
    }

    public ArrayList<Path> elitismSolutions(Path[] solutions) {
        ArrayList<Path> elitism = new ArrayList<>(Arrays.asList(solutions));
        Collections.sort(elitism, Comparator.comparingDouble(Path::getDistance));

        int count = (int) (elitismSize * elitism.size());
        return new ArrayList<>(elitism.subList(0, count));
    }

    public Path selection(Population population) {
        ArrayList<Path> selection = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
//            int randomPick = (int) (Math.random() * population.solutions.length);
            int randomPick = getRandomNumber(0, population.solutions.length);
            selection.add(population.solutions[randomPick]);
        }

        int bestCandidate = 0;
        double bestCalculatedCandidate = selection.get(0).calculateDistance();
        for (int i = 0; i < selection.size(); i++) {
            double currentFitness = selection.get(i).calculateDistance();
            if (currentFitness < bestCalculatedCandidate) {
                bestCalculatedCandidate = currentFitness;
                bestCandidate = i;
            }
        }
        return selection.get(bestCandidate);
    }

    public Pair crossover(Path firstParent, Path secondParent) {

//        int delimiter = (int) (Math.random() * (firstParent.getPubsList().size() - 1));
        int delimiter = getRandomNumber(0, firstParent.getPubsList().size() - 1);

        ArrayList<Pub> firstChildPathTemp = new ArrayList<>();
        ArrayList<Pub> secondChildPathTemp = new ArrayList<>();

        for (int i = 0; i < delimiter; i++) {
            firstChildPathTemp.add(firstParent.getPubsList().get(i));
            secondChildPathTemp.add(secondParent.getPubsList().get(i));
        }

        for (int i = delimiter; i < firstParent.getPubsList().size(); i++) {
            firstChildPathTemp.add(secondParent.getPubsList().get(i));
            secondChildPathTemp.add(firstParent.getPubsList().get(i));
        }

        return new Pair(new Path(correction(firstChildPathTemp)), new Path(correction(secondChildPathTemp)));
    }

    private ArrayList<Pub> correction(ArrayList<Pub> path) {

        ArrayList<Pub> tempPubList = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            if (!tempPubList.contains(path.get(i))) {
                tempPubList.add(path.get(i));
            } else {
                tempPubList.add(null);
            }
        }

        ArrayList<Pub> correctPubList = new ArrayList<>();

        for (Pub pub : tempPubList) {
            if (pub != null) {
                correctPubList.add(pub);
            } else {
                for (Pub defaultPub : defaultPubList) {
                    if (!correctPubList.contains(defaultPub) && !tempPubList.contains(defaultPub)) {
                        correctPubList.add(defaultPub);
                        break;
                    }
                }
            }
        }

        return correctPubList;
    }

    public void mutation(Path path) {
        int randomSwapPosition = getRandomNumber(0, path.getPubsList().size());
        Pub randomSelectedPub = path.getPubsList().get(randomSwapPosition);
        path.getPubsList().remove(randomSwapPosition);

        int tempPosition;
        do {
            tempPosition = getRandomNumber(0, path.getPubsList().size());
        } while (tempPosition == randomSwapPosition);
        path.getPubsList().add(tempPosition, randomSelectedPub);
    }

    public int getRandomNumber(int min, int max) {
        return random.nextInt((max - min)) + min;
    }
}
