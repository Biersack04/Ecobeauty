package com.example.ecobeauty.mydeparture;

import android.content.Context;
import android.util.Log;

import com.example.ecobeauty.main.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    private static File file;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static String assetFilePath(Context context, String assetName) {
        file = new File(context.getFilesDir(), assetName);

        try {
            inputStream = context.getAssets().open(assetName);
            try  {
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
            }catch (Exception e) {
                Log.e(Constants.PYTORCH, Constants.ERROR_PROCESS_ONE + assetName + Constants.FILE_PATH);
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e(Constants.PYTORCH, Constants.ERROR_PROCESS_TWO + assetName + Constants.FILE_PATH);
        }
        return null;
    }
}