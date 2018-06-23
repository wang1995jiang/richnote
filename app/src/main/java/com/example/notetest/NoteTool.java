package com.example.notetest;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王将 on 2018/5/21.
 */

public class NoteTool {
    private static int idEditext=0,idImage=0,numberE=0,numberI=0;
    private static List<View> editTexts=new ArrayList<>();
    private static List<View> images=new ArrayList<>();
    private static List<EditText> edis=new ArrayList<>();
    private static List<Music> musics=new ArrayList<>();
    private static List<VideoInfo> videoInfos=new ArrayList<>();
    private static List<View> musicView=new ArrayList<>();
    private static List<View> videoView=new ArrayList<>();

    public static List<View> getVideoView() {
        return videoView;
    }

    public static List<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    public static List<View> getMusicView() {
        return musicView;
    }

    public static List<Music> getMusics() {
        return musics;
    }

    public static List<EditText> getEdis() {
        return edis;
    }

    public static List<View> getEditTexts() {
        return editTexts;
    }

    public static List<View> getImages() {
        return images;
    }

    public static void setIdImage(int idImage) {
        NoteTool.idImage = idImage;
    }

    public static void setIdEditext(int idEditext) {
        NoteTool.idEditext = idEditext;
    }

    public static void setNumberE(int numberE) {
        NoteTool.numberE = numberE;
    }

    public static void setNumberI(int numberI) {
        NoteTool.numberI = numberI;
    }

    public static int getIdEditext() {
        return idEditext;
    }

    public static int getIdImage() {
        return idImage;
    }

    public static int getNumberE() {
        return numberE;
    }

    public static int getNumberI() {
        return numberI;
    }

    public static View getEditextView(int id){
        View view = null;
        for (View view1:editTexts){
            if (id==(int)view1.getTag()){
                view=view1;
            }
        }
        return view;
    }

    public static int getIdListEditext(View view){
        int id = 0;
        for (int i=0;i<editTexts.size();i++){
            if (view==editTexts.get(i)){
                id=i;
            }
        }
        return id;
    }

    public static int getIdEdi(EditText editText){
        int id=0;
        for (int i=0;i<edis.size();i++){
            if (editText==edis.get(i)){
                id=i;
            }
        }
        return id;
    }

    public static View getImageView(int id){
        View view = null;
        for (View v:images){
            Image image=(Image)v.getTag();
            if (id==image.getId()){
                view=v;
            }
        }
        return view;
    }

    public static void setTranslateAnimation(ImageView galleryOpen,ImageView carmaOpen,ImageView video,ImageView music,ImageView addOpen){
        TranslateAnimation animationGallery=new TranslateAnimation(0,-200,0,0);
        TranslateAnimation animationCarma=new TranslateAnimation(0,200,0,0);
        TranslateAnimation animationMusic=new TranslateAnimation(0,-400,0,0);
        TranslateAnimation animationVideo=new TranslateAnimation(0,400,0,0);
        RotateAnimation animationAdd=new RotateAnimation(0f,45f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        animationMusic.setDuration(1000);
        animationMusic.setFillAfter(true);
        animationVideo.setDuration(1000);
        animationVideo.setFillAfter(true);
        animationCarma.setDuration(1000);
        animationCarma.setFillAfter(true);
        animationGallery.setDuration(1000);
        animationGallery.setFillAfter(true);
        animationAdd.setDuration(1000);
        animationAdd.setFillAfter(true);

        galleryOpen.startAnimation(animationGallery);
        carmaOpen.startAnimation(animationCarma);
        video.startAnimation(animationVideo);
        music.startAnimation(animationMusic);
        addOpen.startAnimation(animationAdd);

    }

    public static void clearAnimate(ImageView imageView){
        if (imageView.animate()!=null)
        {
            imageView.animate().cancel();
        }
        imageView.clearAnimation();
        imageView.setAnimation(null);
    }

    public static void scanAllAudioFiles(Context context) {
//查询媒体数据库
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (size > 1024 * 800) {//大于800K
                    //歌曲编号
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    //歌曲标题
                    String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                    int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    Music music = new Music(id, tilte, album, artist, url, duration, size,getArtwork(context,url));
                    musics.add(music);
                }
                cursor.moveToNext();
            }
        }
    }

    public static Bitmap getArtwork(Context context, String url) {
        Uri selectedAudio = Uri.parse(url);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        myRetriever.setDataSource(context, selectedAudio); // the URI of audio file
        byte[] artwork;

        artwork = myRetriever.getEmbeddedPicture();

        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);

            return bMap;
        } else {

            return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_picture);
        }
    }

    public static String getMusicTime(int second){
        String str="";
        int h,m,s,sAll;
        sAll=second/1000;
        if(sAll>3600){
            h=sAll/3600;
            m=(sAll-h*3600)/60;
            s=(sAll-h*3600)%60;
            str=h+":"+m+":"+s;
        }else {
            m=sAll/60;
            s=sAll%60;
            str=m+":"+s;
        }
        return str;
    }

    public static String getMusicSize(Long size){
        double dSize=(double)size/(1024*1024);
        DecimalFormat df = new DecimalFormat("###.00");
        String result = df.format(dSize);
        return result;
    }

    public static View getIteamMusic(Context context, LinearLayout root, Music music, int id, View.OnClickListener onClickListener){
        View view= LayoutInflater.from(context).inflate(R.layout.item_music,root,false);

        ImageView musicPicture;
        TextView musicTitle,musicAlbum,musicArtist,musicDuration,musicSize;

        LinearLayout linearLayout=(LinearLayout) view.findViewById(R.id.linear_iteam_music);
        musicPicture=(ImageView)view.findViewById(R.id.music_picture);
        musicTitle=(TextView)view.findViewById(R.id.music_title);
        musicAlbum=(TextView)view.findViewById(R.id.music_album);
        musicArtist=(TextView)view.findViewById(R.id.music_artist);
        musicDuration=(TextView)view.findViewById(R.id.music_duration);
        musicSize=(TextView)view.findViewById(R.id.music_size);

        musicPicture.setImageBitmap(music.getPicture());
        musicTitle.setText(music.getTitle());
        musicAlbum.setText(music.getAlbum());
        musicArtist.setText(music.getArtist());
        musicDuration.setText(getMusicTime(music.getDuration()));
        musicSize.setText(getMusicSize(music.getSize())+"M");

        linearLayout.setTag(id);
        linearLayout.setOnClickListener(onClickListener);
        return view;
    }

    public static void getVideoFile(final List<VideoInfo> list, File file) {// 获得视频文件


        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
// sdCard找到视频名称
                   String name = file.getName();
                   int i = name.indexOf('.');
                   if (i != -1) {
                       name = name.substring(i);
                        if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")) {
                        VideoInfo vi = new VideoInfo();
                        vi.setDisplayName(file.getName());
                        vi.setPath(file.getAbsolutePath());
                        vi.setBitmap(getVideoPricute(vi.getPath()));
                        list.add(vi);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });
    }

    public static View getIteamVideo(Context context,LinearLayout root, VideoInfo videoInfo, int id, View.OnClickListener onClickListener){
        View view= LayoutInflater.from(context).inflate(R.layout.video_layout,root,false);

        RelativeLayout relativeLayout=(RelativeLayout) view.findViewById(R.id.rela_video);
        ImageView imageView=(ImageView) view.findViewById(R.id.cover_video);


        imageView.setImageBitmap(videoInfo.getBitmap());

        relativeLayout.setTag(id);
        relativeLayout.setOnClickListener(onClickListener);
        return view;
    }

    public static Bitmap getVideoPricute(String url){
        //创建MediaMetadataRetriever对象
        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
//绑定资源
        mmr.setDataSource(url);
//获取第一帧图像的bitmap对象
        Bitmap bitmap=mmr.getFrameAtTime();
        return bitmap;
    }
}

