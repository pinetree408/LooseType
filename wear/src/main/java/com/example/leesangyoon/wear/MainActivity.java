package com.example.leesangyoon.wear;

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
    private View keyboardView;

    List<String> suggestResultList;
    Suggestion suggestion;

    String inputString = "";

    int testIndex;
    char[] testInput = {
            'm', 'a', 'r', 'c', 's'
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);

        editTextView = (TextView) findViewById(R.id.edit);
        suggestView = (TextView) findViewById(R.id.suggest);
        keyboardView = (View) findViewById(R.id.keyboard);

        suggestion = new Suggestion();

        testIndex = 0;

        keyboardView.setOnTouchListener(new View.OnTouchListener() {   //터치 이벤트 리스너 등록(누를때와 뗐을때를 구분)

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (keyboardView.getClass() == v.getClass()) {

                        long start = System.currentTimeMillis();
                        suggestResultList = suggestion.getSuggestion(String.valueOf(testInput[testIndex]));
                        long end = System.currentTimeMillis();
                        Log.d(TAG,  "Excution Time : " + ( end - start )/1000.0 );

                        inputString += String.valueOf(testInput[testIndex]);
                        editTextView.setText(inputString);
                        suggestView.setText(suggestResultList.toString());
                        testIndex++;
                        if (testIndex == testInput.length) {
                            testIndex = 0;
                            inputString = "";
                            suggestion.suggestionInitilize();
                        }
                    }
                }

                return true;
            }
        });

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {

        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            suggestView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mContainerView.setBackground(null);
            suggestView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}
