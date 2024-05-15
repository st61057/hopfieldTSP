package org.example.Hopfield;

import org.example.Pub;

import java.util.ArrayList;

public class Route {

    ArrayList<Pub> cities;

    public Route(ArrayList<Pub> cities) {
        this.cities = cities;
    }

    public ArrayList<Pub> getCities() {
        return cities;
    }

    public void setCities(ArrayList<Pub> cities) {
        this.cities = cities;
    }

    public int getCountCitiesCount() {
        return cities.size();
    }
}
