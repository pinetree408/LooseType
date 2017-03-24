package com.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Suggestion {

    String[] dictionary;
    Keyboard keyboard;

    double insertionCost;
    double deletionCost;
    double equalCost;
    double substitutionXCost;
    double substitutionYCost;

    HashMap<String, double[]> suggestionMap;
    int inputIndex;

    public Suggestion() {

        dictionary = Source.dictionary;
        Arrays.sort(dictionary);
        keyboard = new Keyboard();

        insertionCost = 0.705375;
        deletionCost = 1.472625;
        equalCost = -0.12426;
        substitutionXCost = Math.pow(0.02625, 2);
        substitutionYCost = Math.pow(0.02, 2);

        suggestionMap = new HashMap<>();
        for (String source : dictionary) {
            int sourceLen = source.length() + 1;
            double[] distance = new double[sourceLen];
            for (int i = 0; i < sourceLen; i++) distance[i] = i;
            suggestionMap.put(source, distance);
        }

        inputIndex = 0;
    }

    public void initialize() {
        inputIndex = 0;
        for (String source : dictionary) {
            int sourceLen = source.length() + 1;
            double[] distance = new double[sourceLen];
            for (int i = 0; i < sourceLen; i++) distance[i] = i;
            suggestionMap.put(source, distance);
        }
    }

    public List<String> getSuggestion(String input, double x, double y) {

        inputIndex++;

        List<String> suggetedResult = new ArrayList<String>();
        List<String> min1Target = new ArrayList<String>();
        List<String> min2Target = new ArrayList<String>();

        double minDist1 = Double.POSITIVE_INFINITY;
        double minDist2 = Double.POSITIVE_INFINITY;

        for (String source: dictionary) {
            double computedDist = computeLevenshteinDistanceChar(source, input.charAt(0), x, y);
            if (computedDist < minDist1) {
                minDist2 = minDist1;
                min2Target.clear();
                min2Target.addAll(min1Target);
                minDist1 = computedDist;
                min1Target.clear();
                min1Target.add(source);
            } else if (computedDist == minDist1) {
                minDist2 = computedDist;
                min1Target.add(source);
            } else if (computedDist == minDist2) {
                min2Target.add(source);
            }
        }

        if (min1Target.size() > 1) {
            suggetedResult.add(min1Target.get(0));
            suggetedResult.add(min1Target.get(1));
        } else {
            suggetedResult.add(min1Target.get(0));
            suggetedResult.add(min2Target.get(0));
        }

        return suggetedResult;
    }

    public double computeLevenshteinDistanceChar(String source, char target, double x, double y) {

        int sourceLen = source.length() + 1;

        double[] distance = suggestionMap.get(source);
        double[] newDistance = new double[sourceLen];
        newDistance[0] = 1;

        for (int i = 1; i < sourceLen; i++) {
            double costDeletion = newDistance[i-1] + deletionCost;
            double costInsertion = distance[i] + insertionCost;
            double costSubstitution = distance[i-1] + getPenelty(source.charAt(i - 1), target, x, y);

            newDistance[i] = minimum(costDeletion, costInsertion, costSubstitution);
        }

        suggestionMap.put(source, newDistance);

        return newDistance[sourceLen-1];
    }

    public double getPenelty(char a, char b, double x, double y) {
        if(a == b) {
            return equalCost;
        }

        double distX = keyboard.positionX.get(a) - x;
        double distY = keyboard.positionY.get(a) - y;

        double ret = Math.sqrt(
                (substitutionXCost * Math.pow(distX, 2)) +
                        (substitutionYCost * Math.pow(distY, 2)));

        return ret;
    }

    private double minimum(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

}
