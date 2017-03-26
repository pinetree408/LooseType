package com.example.leesangyoon.wear;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import com.example.Suggestion;
import com.example.Source;

public class MainActivity extends WearableActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int REQUEST_CODE_FILE = 1;

    private TextView startTaskView;

    private TextView startView;
    private Timer jobScheduler;

    private ViewGroup taskView;
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

    String filePath;
    String fileName;
    File outputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        checkFileWritePermission();

        suggestion = new Suggestion();
        targetList = Source.dictionary;

        startTaskView = (TextView) findViewById(R.id.main);

        startView = (TextView) findViewById(R.id.start);
        startView.setVisibility(View.GONE);

        taskView = (ViewGroup) findViewById(R.id.task);
        taskView.setVisibility(View.GONE);

        resetView = (TextView) findViewById(R.id.reset);

        targetView = (TextView) findViewById(R.id.target);
        setNextTarget();
        Log.d(TAG, "START    : " + nowTarget);
        fileWrite("START," + nowTarget);

        suggestFirstView = (TextView) findViewById(R.id.suggest1);
        suggestSecondView = (TextView) findViewById(R.id.suggest2);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard);

        filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/";
        fileName = "result.csv";

        fileOpen();

        jobScheduler = new Timer();

        startTaskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (startTaskView.getClass() == v.getClass()) {
                        startTaskView.setVisibility(View.GONE);
                        startTask();
                    }
                }
                return true;
            }

        });

        resetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (resetView.getClass() == v.getClass()) {
                        Log.d(TAG, "RESET    : " + nowTarget);
                        fileWrite("RESET," + nowTarget);
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
                            fileWrite("SELECT1,RIGHT");
                            targetInitialize();
                            setNextTarget();
                            startTask();

                            Log.d(TAG, "START    : " + nowTarget);
                            fileWrite("START," + nowTarget);
                        } else{
                            Log.d(TAG, "SELECT1  : WRONG");
                            fileWrite("SELECT1,WRONG");
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
                            Log.d(TAG, "SELECT2  : RIGHT");
                            fileWrite("SELECT2,RIGHT");
                            targetInitialize();
                            setNextTarget();
                            startTask();

                            Log.d(TAG, "START    : " + nowTarget);
                            fileWrite("START," + nowTarget);
                        } else{
                            Log.d(TAG, "SELECT2  : WRONG");
                            fileWrite("SELECT2,WRONG");
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
                        fileWrite("INPUT,key : " + params[0] + ",postion : " + params[1] + "-" + params[2]);

                        Double wpm = calculateWPM();
                        targetView.setText(String.format("%.2f", wpm));
                        Log.d(TAG, "WPM      : " + wpm);
                        fileWrite( "WPM," + wpm);
                    }
                }

                return true;
            }
        });
    }

    public void startTask() {
        startView.setText(nowTarget);
        startView.setVisibility(View.VISIBLE);
        taskView.setVisibility(View.GONE);

        jobScheduler.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        startView.setVisibility(View.GONE);
                                        taskView.setVisibility(View.VISIBLE);
                                    }
                                }
                        );
                    }
                },
                2000);
    }

    public void fileOpen() {
        outputFile = new File(filePath + fileName);
        boolean isSuccess = false;
        if(outputFile!=null&&!outputFile.exists()){
            Log.i( TAG , "!file.exists" );
            try {
                isSuccess = outputFile.createNewFile();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            } finally{
                Log.i(TAG, "isSuccess = " + isSuccess);
            }
        }else{
            Log.i( TAG , "file.exists" );
        }
    }

    public void fileWrite(String log) {
        log = log + "\n";
        try {
            FileOutputStream os = new FileOutputStream(filePath + fileName, true);
            os.write(log.getBytes());
            os.close();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FILE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Log.d(TAG, "Permission always deny");
                }
                break;
        }
    }

    public void checkFileWritePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_FILE);
                // MY_PERMISSION_REQUEST_STORAGE is an
                // app-defined int constant
            } else {
            }
        } else {
        }
    }
}
