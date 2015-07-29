package com.example.skai.avc2;

/**
 * Created by user on 7/28/2015.
 */
import java.util.ArrayList;
import android.media.MediaMetadataRetriever;

public class Converter {
    ArrayList<String> paras;
    OnMessageListener lis;
    Video v;

    public Converter(){
        lis = null;
    }

    private int calBitrate(int duration, int targetSize){
        targetSize = targetSize*1024*1024;
        int audiosize = (120*1000/8)*duration;
        return (targetSize-audiosize)*8/duration;
    }

    public void setVideo(Video v){
        this.v = v;
    }

    public void setOnMessageListener(OnMessageListener lis){
        this.lis = lis;
    }

    public void messageme(String str){
        if(lis!=null)
            lis.onMessage(str,v);
    }

    public void convertVideo(){
        try {
//			v.srcPath = "/storage/sdcard0/external_sd/DCIM/100SHARP/140716_144023.3gp";
//			v.desPath = "/storage/sdcard0/external_sd/DCIM/100SHARP/140716_144023.mp4";
//			v.srcPath = "/mnt/sdcard/android-ffmpeg-tutorial01/aa.3gp";
//			v.desPath = "/mnt/sdcard/android-ffmpeg-tutorial01/aa.mp4";
//			v.limitedSize = "10";
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(v.srcPath);
            String strtime = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            v.duration = Integer.parseInt(strtime);
            int bitrate = calBitrate(v.duration/1000,Integer.parseInt(v.limitedSize));
            paras = new ArrayList<String>();
            paras.add("ffmpeg");

            paras.add("-i");
            paras.add(v.srcPath);

            paras.add("-vcodec");
            paras.add("libx264");

            paras.add("-acodec");
            paras.add("ac3");

            paras.add("-s");
            paras.add("640x360");

            paras.add("-preset");
            paras.add("superfast");

            paras.add("-b:v");
            paras.add(bitrate+"");

            paras.add("-b:a");
            paras.add("120K");

            paras.add("-maxrate");
            paras.add(bitrate+"");

            paras.add("-minrate");
            paras.add(bitrate+"");

            paras.add("-bufsize");
            paras.add(bitrate*2+"");

            paras.add(v.desPath);
            lis.onStart(v);
            convert(paras.toArray(new String[paras.size()]));
            lis.onFinished(v);
//			new Thread(new Runnable(){
//				@Override
//				public void run() {
//
//				}
//			}).start();
        } catch (Exception e) {
            Logger.log(e);
        }
    }
    public native void convert(String[] paras);

    public interface OnMessageListener{
        public void onStart(Video v);
        public void onMessage(String str, Video v);
        public void onFinished(Video v);
    }
}
