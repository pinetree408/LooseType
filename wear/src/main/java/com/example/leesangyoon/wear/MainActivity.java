package com.example.leesangyoon.wear;

import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.example.Suggestion;
import com.example.Source;

public class MainActivity extends WearableActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView resetView;
    private TextView targetView;
    private TextView suggestFirstView;
    private TextView suggestSecondView;
    private KeyboardView keyboardView;

    List<String> suggestResultList;
    Suggestion suggestion;

    String[] targetList;
    String nowTarget;

    String inputString = "";

    Long startInputTime;
    Long totalInputTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        suggestion = new Suggestion();
        targetList = Source.dictionary;

        resetView = (TextView) findViewById(R.id.reset);

        targetView = (TextView) findViewById(R.id.target);
        setNextTarget();
        Log.d(TAG, "START    : " + nowTarget);

        suggestFirstView = (TextView) findViewById(R.id.suggest1);
        suggestSecondView = (TextView) findViewById(R.id.suggest2);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard);

        resetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (resetView.getClass() == v.getClass()) {
                        Log.d(TAG, "RESET    : " + nowTarget);
                        targetInitialize();
                        targetView.setText(nowTarget);
                    }
                }
                return true;
            }

        });

        suggestFirstView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (suggestFirstView.getClass() == v.getClass()) {
                        if (suggestFirstView.getText().equals(nowTarget)) {
                            Log.d(TAG, "SELECT1  : RIGHT");
                            targetInitialize();
                            setNextTarget();
                            Log.d(TAG, "START    : " + nowTarget);
                        } else{
                            Log.d(TAG, "SELECT1  : WRONG");
                        }
                    }
                }
                return true;
            }

        });

        suggestSecondView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (suggestSecondView.getClass() == v.getClass()) {
                        if (suggestSecondView.getText().equals(nowTarget)) {
                            Log.d(TAG, "SELECT1  : RIGHT");
                            targetInitialize();
                            setNextTarget();
                            Log.d(TAG, "START    : " + nowTarget);
                        } else{
                            Log.d(TAG, "SELECT1  : WRONG");
                        }
                    }
                }
                return true;
            }

        });


        keyboardView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (keyboardView.getClass() == v.getClass()) {

                        String[] params = getInputInfo(event);
                        new SuggestionTask().execute(params);
                        inputString += params[0];
                        Log.d(TAG, "INPUT    : key - " + params[0] + " postion - " + params[1] + "," + params[2]);

                        Double wpm = calculateWPM();
                        targetView.setText(String.format("%.2f", wpm));
                        Log.d(TAG, "WPM      : " + wpm);
                    }
                }

                return true;
            }
        });
    }

    public void setNextTarget() {
        int randomTarget = ThreadLocalRandom.current().nextInt(0, targetList.length + 1);
        nowTarget = targetList[randomTarget];
        targetView.setText(nowTarget);
    }

    public void targetInitialize() {
        inputString = "";
        suggestion.initialize();

        suggestFirstView.setText("");
        suggestSecondView.setText("");

        startInputTime = null;
        totalInputTime = 0L;
    }

    public Double calculateWPM() {
        Double wpm = 0.0;
        if (startInputTime == null) {
            startInputTime = System.currentTimeMillis();
        } else {
            totalInputTime = System.currentTimeMillis() - startInputTime;
            wpm = ((inputString.length() - 1) / (totalInputTime / 1000.0 / 60)) / 5;
        }
        return wpm;
    }

    public String[] getInputInfo(MotionEvent event) {
        double tempX = (double) event.getAxisValue(MotionEvent.AXIS_X);
        double tempY = (double) event.getAxisValue(MotionEvent.AXIS_Y);

        String input = keyboardView.getKey(tempX, tempY);

        String[] params = {
                String.valueOf(input),
                String.valueOf(tempX),
                String.valueOf(tempY)
        };
        return params;
    }


    public class SuggestionTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected List<String> doInBackground(String... params) {

            List<String> ret = new ArrayList<String>();

            long start = System.currentTimeMillis();
            suggestResultList = suggestion.getSuggestion(params[0],
                    Double.parseDouble(params[1]), Double.parseDouble(params[2]));
            long end = System.currentTimeMillis();
            //Log.d(TAG,  "Suggestion Excution Time : " + ( end - start )/1000.0 );

            if (params[0].equals(String.valueOf(inputString.charAt(inputString.length()-1)))) {
                return suggestResultList;
            }

            return ret;
        }

        @Override
        protected void onPostExecute(final List<String> suggestedList) {
            super.onPostExecute(suggestedList);

            suggestFirstView.setText("");
            suggestSecondView.setText("");

            if (suggestedList.size() > 0) {
                suggestFirstView.setText(suggestedList.get(0));
                suggestSecondView.setText(suggestedList.get(1));
            }
        }
    }
}
