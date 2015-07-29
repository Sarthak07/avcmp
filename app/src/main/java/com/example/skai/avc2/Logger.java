package com.example.skai.avc2;

/**
 * Created by user on 7/28/2015.
 */
import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class Logger {
    final static String TAG_LOG = "videocompressor_log";
    public static void log(String tag, String msg){
//		Log.e(tag,msg);
    }
    public static void log(String msg){
//		Log.e(TAG_LOG,msg);
    }
    public static void log(Exception e){
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
//		Log.e(TAG_LOG,sw.getBuffer().toString());
    }

    public static void log(int msg){
//		Log.e(TAG_LOG,msg+"");
    }
}

