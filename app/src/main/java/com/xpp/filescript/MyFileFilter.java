package com.xpp.filescript;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by xpp on 2018/5/3.
 */

public class MyFileFilter implements FilenameFilter {
    private String[] types;
    private String[] key_words;
    private int min_size;
    private int max_size;

    MyFileFilter(String[] types, String[] key_words, int min_size, int max_size) throws Exception {
        if(0 > min_size||min_size >= max_size) throw new Exception("invalid size range!");
        this.types = types;
        this.key_words = key_words;
        this.min_size = 0 > min_size?0:min_size * 1024 * 1024;
        this.max_size = max_size * 1024 * 1024;
    }

    public MyFileFilter(String[] types, String[] key_words){
        this.types = types;
        this.key_words = key_words;
        this.min_size = -1;
    }

    public MyFileFilter(String[] types, String[] key_words, int min_size){
        this.types = types;
        this.key_words = key_words;
        this.min_size = 0 > min_size?0:min_size * 1024 * 1024;
        this.max_size = -1;
    }

    @Override
    public boolean accept(File dir, String name) {
        if(-1 != this.min_size){
            File file = new File(dir.getAbsolutePath() + "/" + name);        //检查文件大小
            if (-1 == this.max_size){
                if (this.min_size > file.length())
                    return false;
            }
            else {
                if (this.max_size < file.length() || this.min_size > file.length())
                    return false;
            }
        }
        boolean flag1 = true;
        if(null != this.key_words){
            flag1 = false;
            for (String key_word : this.key_words) {
                if (name.contains(key_word)){                                            //匹配关键词
                    flag1 = true;
                    break;
                }
            }
        }
        boolean flag2 = true;
        if (null != this.types) {
            flag2 = false;
            for (String type : this.types) {
                if (name.endsWith(type)) {                                                  //匹配文件类型
                    flag2 = true;
                    break;
                }
            }
        }
        return flag1&&flag2;
    }
}
