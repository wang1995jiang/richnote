package com.example.notetest;

import android.graphics.Bitmap;

/**
 * Created by 王将 on 2018/3/28.
 */

class Music {
    private int id;//歌曲ID
    private String title;//歌曲标题
    private String album;//歌曲的专辑名
    private String artist;//歌曲的歌手名
    private String url;//歌曲文件的路径
    private int duration;//歌曲的总播放时长
    private Long size;//歌曲文件的大小
    private Bitmap picture;//歌曲文件的封面图片
    public Music(int id, String title, String album, String artist, String url, int duration, Long size, Bitmap picture){
        this.id=id;
        this.title=title;
        this.album=album;
        this.artist=artist;
        this.url=url;
        this.duration=duration;
        this.size=size;
        this.picture=picture;

    }


    public Bitmap getPicture() {
        return picture;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public Long getSize() {
        return size;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }
}
