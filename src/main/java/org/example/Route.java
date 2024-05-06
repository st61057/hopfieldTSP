package org.example;

import java.util.ArrayList;

public class Route {

    ArrayList<City> cities;

    public Route(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public int getCountCitiesCount() {
        return cities.size();
    }
}
