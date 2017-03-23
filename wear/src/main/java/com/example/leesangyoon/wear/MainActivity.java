package com.example.leesangyoon.wear;

import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import com.example.Suggestion;

import android.view.MotionEvent;

public class MainActivity extends WearableActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private BoxInsetLayout mContainerView;

    private TextView editTextView;
    private TextView suggestView;
    private KeyboardView keyboardView;

    List<String> suggestResultList;
    Suggestion suggestion;

    String inputString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);

        editTextView = (TextView) findViewById(R.id.edit);
        suggestView = (TextView) findViewById(R.id.suggest);
        keyboardView = (KeyboardView) findViewById(R.id.keyboard);

        suggestion = new Suggestion();

        suggestView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (suggestView.getClass() == v.getClass()) {
                        inputString = "";
                        suggestion.initialize();
                        editTextView.setText(inputString);
                        suggestView.setText("");
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

                        editTextView.setText(inputString);

                    }
                }

                return true;
            }
        });

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }

    public class SuggestionTask extends AsyncTask<String, Void, String> {

        TextView suggestView = (TextView) findViewById(R.id.suggest);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            long start = System.currentTimeMillis();
            suggestResultList = suggestion.getSuggestion(params[0],
                    Double.parseDouble(params[1]), Double.parseDouble(params[2]));
            long end = System.currentTimeMillis();
            Log.d(TAG,  "Excution Time : " + ( end - start )/1000.0 );

            if (params[0].equals(String.valueOf(inputString.charAt(inputString.length()-1)))) {
                return suggestResultList.toString();
            }

            return "";
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

            if (s.length() > 0) {
                suggestView.setText(s);
            }
        }
    }
}
