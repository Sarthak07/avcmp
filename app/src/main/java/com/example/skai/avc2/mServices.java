package com.example.skai.avc2;

/**
 * Created by user on 7/28/2015.
 */
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.skai.avc2.Converter.OnMessageListener;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class mServices extends IntentService {
    public mServices() {
        super("mServices");
    }
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    ArrayList<Video> listV;

    @Override
    public void onCreate(){
        Logger.log("onCreate");
        listV = new ArrayList<Video>();
        builder = new NotificationCompat.Builder(this)
                .setContentTitle("Video Compressor")
                .setSmallIcon(R.drawable.ic_launcher);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    public void setNotification(int duration, boolean done){
        if(listV.size()>1){
            builder.setContentText("1 Compressing, "+(listV.size()-1)+" Waiting");
        }else{
            builder.setContentText("Converting "+listV.get(0).name);
        }
        if(duration>=0)
            builder.setProgress(listV.get(0).duration, duration, false);

        NotificationCompat.InboxStyle inboxStyle =  new NotificationCompat.InboxStyle();
        for(int i=0; i<listV.size(); i++){
            if(i==0)
                inboxStyle.addLine(listV.get(i).name+(done?" : DONE":" : Compressing"));
            else
                inboxStyle.addLine(listV.get(i).name+" : Waiting");
        }
        builder.setStyle(inboxStyle);
        Intent intent = new Intent(mServices.this,mServices.class);
        intent.putExtra("kill", true);
        builder.setDeleteIntent(PendingIntent.getService(this, 0, intent, 0));
        notificationManager.notify(1115, builder.build());
//    	startForeground(1115, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Logger.log("onstartcommand");
        Bundle b = intent.getExtras();
        if(b.getBoolean("kill", false)){
            this.stopSelf();
            return 0;
        }
        Video v = (Video)b.getSerializable("video");
        listV.add(v);
        return super.onStartCommand(intent, flags, startId);

    }

    public void run(Video v){
        Converter c = new Converter();
        c.setOnMessageListener(new OnMessageListener(){
            Pattern p=null;
            @Override
            public void onMessage(String str, Video v) {
                if(p==null)
                    p = Pattern.compile(".*time=(.*?)\\s.*");
                Matcher mat = p.matcher(str);
                if(mat.matches()){
                    String timestr = mat.group(1);
                    String[] time = timestr.split(":");
                    int h = Integer.parseInt(time[0]);
                    int m = Integer.parseInt(time[1]);
                    String[] second = time[2].split("\\.");
                    int s = Integer.parseInt(second[0]);
                    int cs = Integer.parseInt(second[1]);
                    int curDuration = (((((h*60+m)*60)+s)*100)+cs)*10;
                    Logger.log(curDuration+"/"+v.duration);

                    if(curDuration>v.duration)
                        curDuration=v.duration;
                    setNotification(curDuration, false);
                }
            }

            @Override
            public void onStart(Video v) {

            }

            @Override
            public void onFinished(Video v) {
                //mServices.this.stopSelf();
                setNotification(0,true);
                listV.remove(0);
                if(listV.size()<=0){
                    notificationManager.cancel(1115);
                    mServices.this.stopSelf();
                }
                Logger.log("finished");
            }
        });
        c.setVideo(v);
        c.convertVideo();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.log("onHandleIntent");

        setNotification(0,false);

        run(listV.get(0));
    }

    static {;
        System.loadLibrary("VideoCompressor");
    }
}
