package org.example.Genetic;

import org.example.Pub;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Loader {

    String pathToFile;

    public Loader(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public ArrayList<Pub> loadFile() {
        try {
            ArrayList<Pub> loadedPubs = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.getClass().getResource(pathToFile).getFile()), Charset.forName("CP1250")));
            br.readLine();// skip hlavičky
            String line;
            while (((line = br.readLine()) != null)) {
                String[] record = line.split(";");
                String[] pubCoordinates = record[2].split(",");
                loadedPubs.add(new Pub(record[1], Double.parseDouble(pubCoordinates[0]), Double.parseDouble(pubCoordinates[1])));
            }
            return loadedPubs;

        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
