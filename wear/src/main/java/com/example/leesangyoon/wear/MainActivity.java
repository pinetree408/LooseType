package com.example.leesangyoon.wear;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.Suggestion;

public class MainActivity extends WearableActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int REQUEST_CODE_FILE = 1;

    private TextView startTaskView;
    private ViewGroup userSelectView;
    private List<TextView> userViewList;

    private TextView startView;
    private Timer jobScheduler;

    private TextView endView;

    private ViewGroup taskLayout;
    private TextView targetView;
    private List<TextView> suggestViewList;
    private KeyboardView keyboardView;

    List<String> suggestResultList;
    Suggestion suggestion;

    String[] targetList;
    String nowTarget;

    String inputString = "";

    Long startInputTime;

    Logger logger;
    String fileFormat = "block, trial, eventTime, target, inputKey, posX1, posY1, list1, list2, list3";

    private int dragThreshold = 30;
    private final double angleFactor = (double) 180/Math.PI;
    private float touchDownX, touchDownY;
    private long touchDownTime;

    int block;
    final int numOfBlock = 6;
    int trial;
    final int numOfTrial = 25;
    long startTime;

    int userNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        checkFileWritePermission();

        suggestion = new Suggestion();
        jobScheduler = new Timer();

        userSelectView = (ViewGroup) findViewById(R.id.user);
        userViewList = new ArrayList<TextView>();
        userViewList.add((TextView) findViewById(R.id.user1));
        userViewList.add((TextView) findViewById(R.id.user2));
        userViewList.add((TextView) findViewById(R.id.user3));
        userViewList.add((TextView) findViewById(R.id.user4));
        userViewList.add((TextView) findViewById(R.id.user5));
        userViewList.add((TextView) findViewById(R.id.user6));
        userViewList.add((TextView) findViewById(R.id.user7));
        userViewList.add((TextView) findViewById(R.id.user8));
        userViewList.add((TextView) findViewById(R.id.user9));
        userViewList.add((TextView) findViewById(R.id.user10));
        userViewList.add((TextView) findViewById(R.id.user11));
        userViewList.add((TextView) findViewById(R.id.user12));

        startTaskView = (TextView) findViewById(R.id.main);

        startView = (TextView) findViewById(R.id.start);

        endView = (TextView) findViewById(R.id.end);

        // Task Layout
        taskLayout = (ViewGroup) findViewById(R.id.task);

        targetView = (TextView) findViewById(R.id.target);
        suggestViewList = new ArrayList<TextView>();
        suggestViewList.add((TextView) findViewById(R.id.suggest1));
        suggestViewList.add((TextView) findViewById(R.id.suggest2));
        suggestViewList.add((TextView) findViewById(R.id.suggest3));
        keyboardView = (KeyboardView) findViewById(R.id.keyboard);

        // User select view init
        userSelectView.setVisibility(View.VISIBLE);
        startTaskView.setVisibility(View.GONE);
        startView.setVisibility(View.GONE);
        taskLayout.setVisibility(View.GONE);
        endView.setVisibility(View.GONE);

        block = 0;
        trial = -1;
        userNum = 1;

        for (int i = 0; i < userViewList.size(); i++) {
            final TextView userView = userViewList.get(i);
            final int index = i + 1;
            userView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (userView.getClass() == v.getClass()) {
                            userNum = index;
                            setTargetList(userNum);
                            String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/";
                            String fileName = "result_" + userNum + "_" + block +  ".csv";
                            Log.d(TAG, filePath + fileName);
                            logger = new Logger(filePath, fileName);
                            logger.fileOpen(userNum, block);
                            logger.fileWriteHeader(fileFormat);

                            userSelectView.setVisibility(View.GONE);
                            startTaskView.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }

            });
        }

        startTaskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (startTaskView.getClass() == v.getClass()) {
                        startTaskView.setVisibility(View.GONE);
                        setNextTarget();
                        startTask();
                    }
                }
                return true;
            }

        });

        for (int i = 0; i < suggestViewList.size(); i++) {
            final TextView suggestView = suggestViewList.get(i);
            final int index = i + 1;
            suggestView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (suggestView.getClass() == v.getClass()) {
                                logger.fileWriteLog(block, trial, (System.currentTimeMillis() - startTime),
                                        nowTarget, String.valueOf(index),
                                        (int) event.getAxisValue(MotionEvent.AXIS_X),
                                        (int) event.getAxisValue(MotionEvent.AXIS_Y),
                                        String.valueOf(suggestView.getText()), "", "");
                                if (suggestView.getText().equals(nowTarget)) {
                                    targetInitialize();
                                    setNextTarget();
                                    startTask();
                                }
                            }
                            break;
                    }
                    return true;
                }

            });
        }

        keyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int tempX = (int) event.getAxisValue(MotionEvent.AXIS_X);
                int tempY = (int) event.getAxisValue(MotionEvent.AXIS_Y);
                long eventTime = System.currentTimeMillis();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (keyboardView.getClass() == v.getClass()) {
                            touchDownTime = eventTime;
                            touchDownX = tempX;
                            touchDownY = tempY;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        long touchTime = eventTime - touchDownTime;
                        int xDir = (int) (touchDownX - tempX);
                        int yDir = (int) (touchDownY - tempY);
                        int len = (int) Math.sqrt(xDir * xDir + yDir * yDir);
                        int speed;
                        if (touchTime > 0) {
                            speed = (int) (len * 1000 / touchTime);
                        } else {
                            speed = 0;
                        }
                        if (len > dragThreshold) {
                            if (speed > 400) {
                                double angle = Math.acos((double) xDir / len) * angleFactor;
                                if (yDir < 0) {
                                    angle = 360 - angle;
                                }
                                angle += 45;
                                int id = (int) (angle / 90);
                                if (id > 3) {
                                    id = 0;
                                }
                                switch (id){
                                    case 0:
                                        //left
                                        break;
                                    case 1:
                                        //top;
                                        logger.fileWriteLog(block, trial, (eventTime - startTime), nowTarget,
                                                "reset", tempX, tempY,
                                                "", "", ""
                                                );
                                        targetInitialize();
                                        break;
                                    case 2:
                                        //right
                                        break;
                                    case 3:
                                        //bottom;
                                        break;
                                }
                            }
                        } else {
                            String[] params = getInputInfo(event);
                            inputString += params[0];

                            new SuggestionTask().execute(params);

                            logger.fileWriteLog(block, trial, (eventTime - startTime), nowTarget,
                                    params[0], tempX, tempY,
                                    "", "", ""
                            );
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void startTask() {
        taskLayout.setVisibility(View.GONE);

        if (trial == numOfTrial) {
            endView.setVisibility(View.VISIBLE);
            endView.setOnTouchListener(null);
            jobScheduler.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            endView.setBackgroundColor(Color.parseColor("#f08080"));
                                        }
                                    }
                            );
                            endView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            if (endView.getClass() == v.getClass()) {

                                                trial = -1;
                                                block++;

                                                logger.fileOpen(userNum, block);
                                                logger.fileWriteHeader(fileFormat);

                                                setNextTarget();
                                                startTask();

                                                runOnUiThread(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                endView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                                endView.setVisibility(View.GONE);
                                                            }
                                                        }
                                                );

                                            }
                                            break;
                                    }
                                    return true;
                                }
                            });
                        }
                    },
                    10000);
        } else {
            startView.setVisibility(View.VISIBLE);
            startView.setOnTouchListener(null);
            startView.setText(nowTarget);

            jobScheduler.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            startView.setBackgroundColor(Color.parseColor("#f08080"));
                                        }
                                    }
                            );
                            startView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            if (startView.getClass() == v.getClass()) {
                                                startTime = System.currentTimeMillis();
                                                logger.fileWriteLog(block, trial, startTime, nowTarget, "start", -1, -1, "", "", "");
                                                runOnUiThread(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                startView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                                startView.setVisibility(View.GONE);
                                                                taskLayout.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                );

                                            }
                                            break;
                                    }
                                    return true;
                                }
                            });
                        }
                    },
                    2000);
        }
    }

    public void setNextTarget() {
        trial++;
        nowTarget = targetList[trial + (block * 25)];
        targetView.setText(nowTarget);
    }

    public void targetInitialize() {
        inputString = "";
        suggestion.initialize();

        for (TextView suggestView : suggestViewList) {
            suggestView.setText("");
        }

        startInputTime = null;
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
            long eventTime = System.currentTimeMillis();

            suggestResultList = suggestion.getSuggestion(params[0],
                    Double.parseDouble(params[1]), Double.parseDouble(params[2]), inputString);

            logger.fileWriteLog(block, trial, (eventTime - startTime), nowTarget,
                    "", -1, -1,
                    suggestResultList.get(0), suggestResultList.get(1), suggestResultList.get(2)
                    );

            if (params[0].equals(String.valueOf(inputString.charAt(inputString.length()-1)))) {
                return suggestResultList;
            }

            return ret;
        }

        @Override
        protected void onPostExecute(final List<String> suggestedList) {
            super.onPostExecute(suggestedList);

            for (TextView suggestView : suggestViewList) {
                suggestView.setText("");
            }

            if (suggestedList.size() > 0) {
                for (int i = 0; i < suggestViewList.size(); i++){
                    suggestViewList.get(i).setText(suggestedList.get(i));
                }
            }
        }
    }

    public void setTargetList(int userNum) {
        switch (userNum) {
            case 1:
                //3
                targetList = Source.set3;
                break;
            case 2:
                //1
                targetList = Source.set1;
                break;
            case 3:
                //2
                targetList = Source.set2;
                break;
            case 4:
                //3
                targetList = Source.set3;
                break;
            case 5:
                //1
                targetList = Source.set1;
                break;
            case 6:
                //2
                targetList = Source.set2;
                break;
            case 7:
                //3
                targetList = Source.set3;
                break;
            case 8:
                //1
                targetList = Source.set1;
                break;
            case 9:
                //1
                targetList = Source.set1;
                break;
            case 10:
                //2
                targetList = Source.set2;
                break;
            case 11:
                //3
                targetList = Source.set3;
                break;
            case 12:
                //2
                targetList = Source.set2;
                break;
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
