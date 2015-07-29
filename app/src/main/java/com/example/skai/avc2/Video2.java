package com.example.skai.avc2;

/**
 * Created by user on 7/28/2015.
 */
import java.io.File;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Video2 implements Serializable{

    private static final long serialVersionUID = 1L;
    String srcPath;
    String desPath;
    String limitedSize;
    String name;
    int duration;
    public Video2(){
        duration=0;
    }

    public void setSrcPath(String str){
        this.srcPath = str;
    }

    public void setDesPath(String str){
        this.desPath = str;
        Pattern p = Pattern.compile(".*"+File.separator+"(.*)");
        Matcher m = p.matcher(str);
        if(m.matches())
            this.name = m.group(1);
    }

    public void setLimitedSize(String str){
        this.limitedSize = str;
    }
}

