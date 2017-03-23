package com.example;

import java.util.HashMap;

/**
 * Created by leesangyoon on 2017. 3. 24..
 */

public class Keyboard {

    char[] line1 = {
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'
    };
    char[] line2 = {
            'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'
    };
    char[] line3 = {
            'z', 'x', 'c', 'v', 'b', 'n', 'm'
    };

    HashMap<Character, Double> positionX;
    HashMap<Character, Double> positionY;

    public Keyboard() {
        positionX = new HashMap<>();
        positionY = new HashMap<>();
        keyboardPositionInitilize();
    }

    private void keyboardPositionInitilize() {
        for (char key : line1) {
            positionX.put(key, 14.5 + 31 * String.valueOf(line1).indexOf(key));
            positionY.put(key, 40.5);
        }
        for (char key : line2) {
            positionX.put(key, 35.5 + 31 * String.valueOf(line2).indexOf(key));
            positionY.put(key, 87.5);
        }
        for (char key : line3) {
            positionX.put(key, 157.5 + 31 * String.valueOf(line3).indexOf(key));
            positionY.put(key, 132.5);
        }
    }

}
