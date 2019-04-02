package com.ialex.stmvumeter.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ialex on 4/3/17.
 */

public class BitmapUtils {

    public static void saveBitmap(Bitmap bmp, File out) throws IOException {
        FileOutputStream fOut = new FileOutputStream(out);

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
    }
}
