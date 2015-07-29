package com.example.skai.avc2;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    static final String APP_PATH = Environment.getExternalStorageDirectory()+File.separator+"VideoCompressor";
    ImageView thumbnail;
    Button btnInput;
    TextView tvOutput;
    EditText etNewname;
    mServices ser;
    mServices2 ser2;

    private static final String AD_UNIT_ID = "a153ce583559107";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File dumpFolder = new File(APP_PATH);
        if (!dumpFolder.exists()) {
            dumpFolder.mkdirs();
        }
//        Button btnSelect = (Button)this.findViewById(R.id.btnselect);
        tvOutput = (TextView)this.findViewById(R.id.tvoutputfolder);
        tvOutput.setText(APP_PATH);
        etNewname = (EditText)this.findViewById(R.id.etoutputname);
        btnInput = (Button)this.findViewById(R.id.btninputfile);
        btnInput.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                mediaChooser.setType("video/*");
                startActivityForResult(mediaChooser, 1);
            }
        });

        Button btnConvert = (Button)this.findViewById(R.id.btnconvert);
        thumbnail = (ImageView)this.findViewById(R.id.thumbnail);

        btnConvert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View vv) {
                try {
                    Video v = new Video();
                    v.setSrcPath(btnInput.getText().toString());
                    v.setDesPath(tvOutput.getText().toString() + File.separator + etNewname.getText().toString() + ".mp4");
                    v.setLimitedSize("10");
                    Intent intent = new Intent(MainActivity.this, mServices.class);
                    intent.putExtra("video", v);
                    MainActivity.this.startService(intent);
                } catch (Exception e) {
                    Logger.log(e);
                }
            }
        });

        Button btnConvertt = (Button)this.findViewById(R.id.btnconvertt);
        thumbnail = (ImageView)this.findViewById(R.id.thumbnail);

        btnConvert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View vvv) {
                try {
                    Video2 v2 = new Video2();
                    v2.setSrcPath(btnInput.getText().toString());
                    v2.setDesPath(tvOutput.getText().toString() + File.separator + etNewname.getText().toString() + ".mp4");
                    v2.setLimitedSize("10");
                    Intent intent = new Intent(MainActivity.this, mServices2.class);
                    intent.putExtra("video", v2);
                    MainActivity.this.startService(intent);
                } catch (Exception e) {
                    Logger.log(e);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try{

            Logger.log("requestCode"+requestCode);
            Logger.log("resultCode"+resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==0) return;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            Pattern p = Pattern.compile(".*"+File.separator+"(.*)\\..*");
            Matcher m = p.matcher(filePath);
            if(m.matches()){
                etNewname.setText(m.group(1));
            }
            btnInput.setText(filePath);
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(filePath,Thumbnails.MINI_KIND);
            thumbnail.setImageBitmap(bm);
            Logger.log(filePath);
            cursor.close();
        }catch(Exception e){
            Logger.log(e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}