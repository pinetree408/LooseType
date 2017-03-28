package com.example.leesangyoon.wear;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by leesangyoon on 2017. 3. 29..
 */

public class Logger {
    private static final String TAG = Logger.class.getSimpleName();
    String filePath;
    String fileName;
    File outputFile;

    public Logger() {

    }

    public Logger(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void fileOpen(int userNum, int block) {
        outputFile = new File(filePath + fileName);
        boolean isSuccess = false;
        if(outputFile!=null&&!outputFile.exists()){
            Log.i(TAG , "!file.exists" );
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
            fileOpen(userNum, block);
        }
    }

    public void fileWriteHeader(String header) {
        fileWrite(header);
    }
    public void fileWriteLog(
            int block, int trial, long eventTime,
            String target, String inputKey, int posX1, int posY1,
            String list1, String list2, String list3) {

        String log = block + "," + trial + "," + eventTime + "," +
                target + "," + inputKey + "," + posX1 + "," + posY1 + "," +
                list1 + "," + list2 + ","  + list3 + "\n";
        fileWrite(log);
    }

    private void fileWrite(String log) {
        try {
            FileOutputStream os = new FileOutputStream(filePath + fileName, true);
            os.write(log.getBytes());
            os.close();
        } catch (IOException e) {
            Log.i(TAG, e.toString());
        }
    }
}
