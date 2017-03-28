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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.Suggestion;

public class MainActivity extends WearableActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int REQUEST_CODE_FILE = 1;

    private TextView startTaskView;
    private ViewGroup userView;
    private TextView user1View, user2View, user3View, user4View;
    private TextView user5View, user6View, user7View, user8View;
    private TextView user9View, user10View, user11View, user12View;

    private TextView startView;
    private Timer jobScheduler;

    private TextView endView;

    private ViewGroup taskView;
    private TextView targetView;
    private TextView suggestFirstView;
    private TextView suggestSecondView;
    private TextView suggestThirdView;
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

        block = 0;
        trial = -1;
        userNum = 1;

        suggestion = new Suggestion();
        setTargetList(userNum);
        //StdRandom.shuffle(targetList);

        startTaskView = (TextView) findViewById(R.id.main);
        startTaskView.setVisibility(View.GONE);

        startView = (TextView) findViewById(R.id.start);
        startView.setVisibility(View.GONE);

        endView = (TextView) findViewById(R.id.end);
        endView.setVisibility(View.GONE);

        taskView = (ViewGroup) findViewById(R.id.task);
        taskView.setVisibility(View.GONE);

        userView = (ViewGroup) findViewById(R.id.user);
        user1View = (TextView) findViewById(R.id.user1);
        user2View = (TextView) findViewById(R.id.user2);
        user3View = (TextView) findViewById(R.id.user3);
        user4View = (TextView) findViewById(R.id.user4);
        user5View = (TextView) findViewById(R.id.user5);
        user6View = (TextView) findViewById(R.id.user6);
        user7View = (TextView) findViewById(R.id.user7);
        user8View = (TextView) findViewById(R.id.user8);
        user9View = (TextView) findViewById(R.id.user9);
        user10View = (TextView) findViewById(R.id.user10);
        user11View = (TextView) findViewById(R.id.user11);
        user12View = (TextView) findViewById(R.id.user12);

        targetView = (TextView) findViewById(R.id.target);

        suggestFirstView = (TextView) findViewById(R.id.suggest1);
        suggestSecondView = (TextView) findViewById(R.id.suggest2);
        suggestThirdView = (TextView) findViewById(R.id.suggest3);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard);

        jobScheduler = new Timer();

        startTaskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (startTaskView.getClass() == v.getClass()) {
                        startTaskView.setVisibility(View.GONE);
                        filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/";
                        fileName = "result_" + userNum + "_" + block +  ".csv";
                        fileOpen();
                        fileWrite("block, trial, eventTime, target, inputKey, posX1, posY1, suggestionList");
                        setNextTarget();
                        startTask();
                    }
                }
                return true;
            }

        });

        suggestFirstView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int tempX = (int) event.getAxisValue(MotionEvent.AXIS_X);
                int tempY = (int) event.getAxisValue(MotionEvent.AXIS_Y);
                long eventTime = System.currentTimeMillis();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (suggestFirstView.getClass() == v.getClass()) {
                            String log = block + "," +
                                    trial + "," +
                                    (eventTime - startTime) + "," +
                                    nowTarget + "," +
                                    "1" + "," +
                                    tempX + "," +
                                    tempY + "," +
                                    suggestFirstView.getText()
                                    ;
                            fileWrite(log);
                            if (suggestFirstView.getText().equals(nowTarget)) {
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

        suggestSecondView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int tempX = (int) event.getAxisValue(MotionEvent.AXIS_X);
                int tempY = (int) event.getAxisValue(MotionEvent.AXIS_Y);
                long eventTime = System.currentTimeMillis();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (suggestSecondView.getClass() == v.getClass()) {
                            String log = block + "," +
                                    trial + "," +
                                    (eventTime - startTime) + "," +
                                    nowTarget + "," +
                                    "2" + "," +
                                    tempX + "," +
                                    tempY + "," +
                                    suggestSecondView.getText()
                                    ;
                            fileWrite(log);
                            if (suggestSecondView.getText().equals(nowTarget)) {
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

        suggestThirdView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int tempX = (int) event.getAxisValue(MotionEvent.AXIS_X);
                int tempY = (int) event.getAxisValue(MotionEvent.AXIS_Y);
                long eventTime = System.currentTimeMillis();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (suggestThirdView.getClass() == v.getClass()) {
                            String log = block + "," +
                                    trial + "," +
                                    (eventTime - startTime) + "," +
                                    nowTarget + "," +
                                    "3" + "," +
                                    tempX + "," +
                                    tempY + "," +
                                    suggestThirdView.getText()
                                    ;
                            fileWrite(log);
                            if (suggestThirdView.getText().equals(nowTarget)) {
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
                                        String log = block + "," +
                                                trial + "," +
                                                (eventTime - startTime) + "," +
                                                nowTarget + "," +
                                                "reset" + "," +
                                                tempX + "," +
                                                tempY + "," +
                                                ""
                                                ;
                                        fileWrite(log);

                                        targetInitialize();
                                        targetView.setText(nowTarget);
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
                            String log = block + "," +
                                    trial + "," +
                                    (eventTime - startTime) + "," +
                                    nowTarget + "," +
                                    params[0] + "," +
                                    tempX + "," +
                                    tempY + "," +
                                    ""
                                    ;
                            fileWrite(log);
                        }
                        break;
                }
                return true;
            }
        });

        user1View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user1View.getClass() == v.getClass()) {
                        userNum = 1;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user2View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user2View.getClass() == v.getClass()) {
                        userNum = 2;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user3View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user3View.getClass() == v.getClass()) {
                        userNum = 3;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user4View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user4View.getClass() == v.getClass()) {
                        userNum = 4;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user5View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user5View.getClass() == v.getClass()) {
                        userNum = 5;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user6View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user6View.getClass() == v.getClass()) {
                        userNum = 6;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user7View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user7View.getClass() == v.getClass()) {
                        userNum = 7;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user8View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user8View.getClass() == v.getClass()) {
                        userNum = 8;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user9View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user9View.getClass() == v.getClass()) {
                        userNum = 9;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user10View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user10View.getClass() == v.getClass()) {
                        userNum = 10;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user11View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user11View.getClass() == v.getClass()) {
                        userNum = 11;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
        user12View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (user12View.getClass() == v.getClass()) {
                        userNum = 12;
                        userView.setVisibility(View.GONE);
                        startTaskView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });
    }

    public void startTask() {
        startView.setText(nowTarget);
        startView.setVisibility(View.VISIBLE);
        startView.setOnTouchListener(null);

        taskView.setVisibility(View.GONE);

        if (trial == 25) {
            startView.setVisibility(View.GONE);
            endView.setVisibility(View.VISIBLE);
        } else {
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
                                    // TODO Auto-generated method stub
                                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                        if (startView.getClass() == v.getClass()) {
                                            startTime = System.currentTimeMillis();
                                            String log = block + "," +
                                                    trial + "," +
                                                    startTime + "," +
                                                    nowTarget + "," +
                                                    "start," +
                                                    "," +
                                                    "," +
                                                    "";
                                            fileWrite(log);
                                            runOnUiThread(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startView.setBackgroundColor(Color.parseColor("#ffffff"));
                                                            startView.setVisibility(View.GONE);
                                                            taskView.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                            );

                                        }
                                    }

                                    return true;
                                }
                            });
                        }
                    },
                    2000);
        }
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
            block++;
            fileName = "result_" + userNum + "_" + block +  ".csv";
            fileOpen();
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

    public void setNextTarget() {
        trial++;
        nowTarget = targetList[trial + (block * 25)];
        targetView.setText(nowTarget);
    }

    public void targetInitialize() {
        inputString = "";
        suggestion.initialize();

        suggestFirstView.setText("");
        suggestSecondView.setText("");
        suggestThirdView.setText("");

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
            long eventTime = System.currentTimeMillis();

            suggestResultList = suggestion.getSuggestion(params[0],
                    Double.parseDouble(params[1]), Double.parseDouble(params[2]), inputString);

            String log = block + "," +
                    trial + "," +
                    (eventTime - startTime) + "," +
                    nowTarget + "," +
                    "," +
                    "," +
                    "," +
                    suggestResultList.toString();
                    ;
            fileWrite(log);

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
            suggestThirdView.setText("");

            if (suggestedList.size() > 0) {
                suggestFirstView.setText(suggestedList.get(0));
                suggestSecondView.setText(suggestedList.get(1));
                suggestThirdView.setText(suggestedList.get(2));
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
