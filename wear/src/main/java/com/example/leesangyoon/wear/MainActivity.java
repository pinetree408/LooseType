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

    private BoxInsetLayout mContainerView;

    private TextView resetView;
    private TextView targetView;
    private TextView suggestFirstView;
    private TextView suggestSecondView;
    private KeyboardView keyboardView;

    List<String> suggestResultList;
    Suggestion suggestion;

    String[] targetList;

    String inputString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        suggestion = new Suggestion();
        targetList = Source.dictionary;

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);

        resetView = (TextView) findViewById(R.id.reset);
        resetView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        resetView.setText("RESET");

        targetView = (TextView) findViewById(R.id.target);
        targetView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        int randomTarget = ThreadLocalRandom.current().nextInt(0, targetList.length + 1);
        targetView.setText(targetList[randomTarget]);

        suggestFirstView = (TextView) findViewById(R.id.suggest1);
        suggestFirstView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        suggestSecondView = (TextView) findViewById(R.id.suggest2);
        suggestSecondView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard);

        resetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (resetView.getClass() == v.getClass()) {
                        inputString = "";
                        suggestion.initialize();
                        suggestFirstView.setText("");
                        suggestSecondView.setText("");
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
                        if (suggestFirstView.getText().equals(targetView.getText())) {
                            setNextTarget();
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
                        if (suggestSecondView.getText().equals(targetView.getText())) {
                            setNextTarget();
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
                        double tempX = (double) event.getAxisValue(MotionEvent.AXIS_X);
                        double tempY = (double) event.getAxisValue(MotionEvent.AXIS_Y);

                        String input = keyboardView.getKey(tempX, tempY);
                        inputString += input;

                        String[] params = {
                                String.valueOf(input),
                                String.valueOf(tempX),
                                String.valueOf(tempY)
                        };
                        new SuggestionTask().execute(params);
                    }
                }

                return true;
            }
        });

    }

    public void setNextTarget() {
        inputString = "";
        suggestion.initialize();
        suggestFirstView.setText("");
        suggestSecondView.setText("");
        int randomTarget = ThreadLocalRandom.current().nextInt(0, targetList.length + 1);
        targetView.setText(targetList[randomTarget]);
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
            Log.d(TAG,  "Excution Time : " + ( end - start )/1000.0 );

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

            for (int i = 0; i < suggestedList.size(); i++) {
                String suggested = suggestedList.get(i);
                if (i == 0) {
                    suggestFirstView.setText(suggested);
                } else if (i == 1) {
                    suggestSecondView.setText(suggested);
                } else {
                    break;
                }
            }
        }
    }
}
