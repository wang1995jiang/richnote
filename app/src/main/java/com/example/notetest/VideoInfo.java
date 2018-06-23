package com.example.notetest;

import android.graphics.Bitmap;

/**
 * Created by 王将 on 2018/5/24.
 */

class VideoInfo {
    private String displayName;
    private String path;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }
}
