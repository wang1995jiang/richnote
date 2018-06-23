package com.example.notetest;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends CarmeraAndGall implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    LinearLayout linearLayout;
    ImageView add,galleryOpen,carmaOpen,video,music;
    View gallery,carma,videoView,musicView;
    private InputMethodManager mInputMethodManager;
    boolean isOpen=true;
    Dialog dialog;
    int musicId=-1,videoId=-1;
    MediaPlayer mediaPlayer=new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()){
            String [] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用该应用",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    init();
                }else {
                    Toast.makeText(this,"发生未知错误！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    private void init() {
        final Context context=this;
        MediaScannerConnection.scanFile(this, new String[] { Environment.getExternalStorageDirectory().getAbsolutePath() }, null, null);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (NoteTool.getMusics().size()==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NoteTool.scanAllAudioFiles(context);
                }
            }).start();
        }

        if (NoteTool.getVideoInfos().size()==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NoteTool.getVideoFile(NoteTool.getVideoInfos(),Environment.getExternalStorageDirectory());// 获得视频文件
                }
            }).start();
        }

        add=(ImageView)findViewById(R.id.add);
        galleryOpen=(ImageView) findViewById(R.id.gallery_open);
        carmaOpen=(ImageView) findViewById(R.id.carma_open);
        video=(ImageView) findViewById(R.id.video);
        music=(ImageView) findViewById(R.id.music);
        gallery=(View) findViewById(R.id.openG);
        carma=(View) findViewById(R.id.openC);
        videoView=(View) findViewById(R.id.openV);
        musicView=(View) findViewById(R.id.openM);


        gallery.setVisibility(View.GONE);
        carma.setVisibility(View.GONE);
        galleryOpen.setVisibility(View.GONE);
        carmaOpen.setVisibility(View.GONE);
        video.setVisibility(View.GONE);
        music.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        musicView.setVisibility(View.GONE);

        linearLayout=(LinearLayout)findViewById(R.id.linear);

        add.setOnClickListener(this);
        gallery.setOnClickListener(this);
        carma.setOnClickListener(this);
        videoView.setOnClickListener(this);
        musicView.setOnClickListener(this);

        linearLayout.addView(getEditext(linearLayout));
    }

    public View getEditext(LinearLayout root){
        View view= LayoutInflater.from(this).inflate(R.layout.editext_layout,root,false);
        EditText editText=(EditText) view.findViewById(R.id.editext);

        editText.setFocusable(true);//设置输入框可聚集
        editText.setFocusableInTouchMode(true);//设置触摸聚焦
        editText.requestFocus();//请求焦点
        editText.findFocus();//获取焦点
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法

        editText.addTextChangedListener(new TextChange(editText,root));
        editText.setTag(NoteTool.getIdEditext());
        NoteTool.getEdis().add(editText);

        view.setTag(NoteTool.getIdEditext());
        NoteTool.getEditTexts().add(view);

        NoteTool.setIdEditext(NoteTool.getIdEditext()+1);
        NoteTool.setNumberE(NoteTool.getNumberE()+1);
        return view;
    }

    public View getImageView(LinearLayout root,String url){
        View view= LayoutInflater.from(this).inflate(R.layout.image_layout,root,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.image);
        Glide.with(this).load(url).into(imageView);

        Image image=new Image(NoteTool.getIdImage(),url);

        view.setTag(image);
        NoteTool.getImages().add(view);

        NoteTool.setIdImage(NoteTool.getIdImage()+1);
        NoteTool.setNumberI(NoteTool.getNumberI()+1);
        return view;
    }

    public View getMusicView(LinearLayout root,Music music,int id){
        View view= LayoutInflater.from(this).inflate(R.layout.music_layout,root,false);
        ImageView control=(ImageView) view.findViewById(R.id.control_music);
        TextView name=(TextView) view.findViewById(R.id.name_music);
        TextView singer=(TextView) view.findViewById(R.id.singer_music);

        name.setText(music.getTitle());
        singer.setText(music.getArtist());
        control.setOnClickListener(this);

        control.setTag(id);
        view.setTag(music);
        NoteTool.getMusicView().add(view);

        return view;
    }

    public View getVideoView(LinearLayout root,VideoInfo videoInfo,int id){
        View view= LayoutInflater.from(this).inflate(R.layout.video_layout,root,false);

        View stop=(View) view.findViewById(R.id.stop_video);
        VideoView videoView=(VideoView) view.findViewById(R.id.video);
        ImageView imageView=(ImageView) view.findViewById(R.id.cover_video);
        ImageView play=(ImageView) view.findViewById(R.id.video_play);
        ImageView full=(ImageView) view.findViewById(R.id.full_screen_play);

        videoView.setOnCompletionListener(this);
        imageView.setImageBitmap(videoInfo.getBitmap());

        play.setTag(id);
        stop.setTag(id);
        full.setTag(id);
        full.setOnClickListener(this);
        stop.setOnClickListener(this);
        play.setOnClickListener(this);

        stop.setVisibility(View.GONE);
        full.setVisibility(View.GONE);
        view.setTag(videoInfo);
        NoteTool.getVideoView().add(view);
        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                setAdd();
                break;
            case R.id.openG:
                goGallery();
                break;
            case R.id.openC:
                goCarmera();
                break;
            case R.id.openV:
                if (NoteTool.getMusics().size()==0){
                    Toast.makeText(this,"还没有搜索到相应的视频文件",Toast.LENGTH_SHORT).show();
                }else {
                    choiceDialog(false);
                }
                break;
            case R.id.openM:
                if (NoteTool.getMusics().size()==0){
                    Toast.makeText(this,"还没有搜索到相应的音乐文件",Toast.LENGTH_SHORT).show();
                }else {
                    choiceDialog(true);
                }

                break;
            case R.id.linear_iteam_music:
                linearLayout.addView(getMusicView(linearLayout,NoteTool.getMusics().get((int) v.getTag()),NoteTool.getMusicView().size()));
                linearLayout.addView(getEditext(linearLayout));
                dialog.dismiss();
                break;
            case R.id.rela_video:
                linearLayout.addView(getVideoView(linearLayout,NoteTool.getVideoInfos().get((int) v.getTag()),NoteTool.getVideoView().size()));
                linearLayout.addView(getEditext(linearLayout));
                dialog.dismiss();
                break;
            case R.id.control_music:
                startMusic((int) v.getTag());
                break;
            case R.id.stop_video:
            case R.id.video_play:
                startVideo((int) v.getTag());
                break;
            case R.id.full_screen_play:
                setFullPlay((int) v.getTag());
            default:break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!path.isEmpty()){
            linearLayout.addView(getImageView(linearLayout,path));
            linearLayout.addView(getEditext(linearLayout));
            path="";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void setAdd(){
        if (isOpen){
            galleryOpen.setVisibility(View.VISIBLE);
            carmaOpen.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.VISIBLE);
            carma.setVisibility(View.VISIBLE);
            video.setVisibility(View.VISIBLE);
            music.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            musicView.setVisibility(View.VISIBLE);
            NoteTool.setTranslateAnimation(galleryOpen,carmaOpen,video,music,add);
            isOpen=false;
        }else {
            galleryOpen.setVisibility(View.GONE);
            carmaOpen.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
            carma.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            music.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            musicView.setVisibility(View.GONE);

            NoteTool.clearAnimate(galleryOpen);
            NoteTool.clearAnimate(carmaOpen);
            NoteTool.clearAnimate(video);
            NoteTool.clearAnimate(music);
            NoteTool.clearAnimate(add);

            isOpen=true;
        }
    }

    private void setFullPlay(int id){
        String videoUrl=((VideoInfo) NoteTool.getVideoView().get(id).getTag()).getPath();
        Intent openVideo = new Intent(Intent.ACTION_VIEW);
        openVideo.setDataAndType(Uri.parse(videoUrl), "video/*");
        startActivityForResult(openVideo,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){

                    if (Build.VERSION.SDK_INT>=19){
                        path= handleImageOnKitKat(photoUri);

                    }else {
                        path=handleImageBeforeKitKat(photoUri);

                    }
                }
                break;
            case 2:
                if (resultCode==RESULT_OK){
                    photoUri=data.getData();
                    if (Build.VERSION.SDK_INT>=19){
                        path=handleImageOnKitKat(photoUri);

                    }else {
                        path=handleImageBeforeKitKat(photoUri);

                    }

                }
                break;
            case 3:
                if (resultCode==RESULT_OK){
                    handleFrontVideo();
                }
            default:break;

        }
    }

    private void handleFrontVideo()
    {
        View viewFront=NoteTool.getVideoView().get(videoId);
        ImageView playFront=(ImageView) viewFront.findViewById(R.id.video_play);
        ImageView coverFront=(ImageView) viewFront.findViewById(R.id.cover_video);
        VideoView videoViewFront=(VideoView) viewFront.findViewById(R.id.video);
        View stopFront=(View) viewFront.findViewById(R.id.stop_video);
        ImageView fullFront=(ImageView) viewFront.findViewById(R.id.full_screen_play);

        videoViewFront.suspend();
        playFront.setVisibility(View.VISIBLE);
        coverFront.setVisibility(View.VISIBLE);
        stopFront.setVisibility(View.GONE);
        fullFront.setVisibility(View.GONE);
    }
    private void startMusic(int id){
        View view=NoteTool.getMusicView().get(id);
        ImageView imageView=(ImageView) view.findViewById(R.id.control_music);
        Music music=(Music) view.getTag();

        if (musicId==-1){
            imageView.setImageResource(R.drawable.stop_music);
            prepareMusic(music.getUrl());
            mediaPlayer.start();
        }else {
            if (musicId==id){
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imageView.setImageResource(R.drawable.play_music);
                }else {
                    mediaPlayer.start();
                    imageView.setImageResource(R.drawable.stop_music);
                }
            }else {
                mediaPlayer.reset();
                prepareMusic(music.getUrl());
                mediaPlayer.start();
                imageView.setImageResource(R.drawable.stop_music);
                ImageView imageView1=(ImageView) NoteTool.getMusicView().get(musicId).findViewById(R.id.control_music);
                imageView1.setImageResource(R.drawable.play_music);
            }
        }
        musicId=id;

    }

    private void startVideo(int id){
        View view=NoteTool.getVideoView().get(id);
        View stop=(View) view.findViewById(R.id.stop_video);
        ImageView full=(ImageView) view.findViewById(R.id.full_screen_play);
        ImageView play=(ImageView) view.findViewById(R.id.video_play);
        ImageView cover=(ImageView) view.findViewById(R.id.cover_video);
        VideoView videoView=(VideoView)view.findViewById(R.id.video);
        VideoInfo videoInfo=(VideoInfo)view.getTag();

        if (videoId==-1){
            videoView.setVideoPath(videoInfo.getPath());
            play.setVisibility(View.GONE);
            cover.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            full.setVisibility(View.VISIBLE);
            videoView.start();
        }else {
            if (videoId==id){
                if (videoView.isPlaying()){
                    videoView.pause();
                    play.setVisibility(View.VISIBLE);
                    cover.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.GONE);
                    full.setVisibility(View.GONE);
                }else {
                    videoView.start();

                    play.setVisibility(View.GONE);
                    cover.setVisibility(View.GONE);
                    stop.setVisibility(View.VISIBLE);
                    full.setVisibility(View.VISIBLE);
                }
            }else {
                handleFrontVideo();

                videoView.setVideoPath(videoInfo.getPath());
                play.setVisibility(View.GONE);
                cover.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                full.setVisibility(View.VISIBLE);
                videoView.start();
            }
        }

        videoId=id;

    }

    private void prepareMusic(String url){
        File file=new File(url);
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void choiceDialog(boolean isMusic){
        dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.all_music_create, null);

        LinearLayout linearLayout=(LinearLayout) view.findViewById(R.id.linear_create);
        int i=0;

        if (isMusic){
            for (Music music:NoteTool.getMusics()){
                linearLayout.addView(NoteTool.getIteamMusic(this,linearLayout,music,i,this));
                i++;
            }
        }else {
            for (VideoInfo videoInfo:NoteTool.getVideoInfos()){
                linearLayout.addView(NoteTool.getIteamVideo(this,linearLayout,videoInfo,i,this));
                i++;
            }
        }

        //将布局设置给Dialog
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();//显示对话框
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        View view=NoteTool.getVideoView().get(videoId);
        ImageView play=(ImageView) view.findViewById(R.id.video_play);
        ImageView cover=(ImageView) view.findViewById(R.id.cover_video);
        VideoView videoView=(VideoView)view.findViewById(R.id.video);

        play.setVisibility(View.VISIBLE);
        cover.setVisibility(View.VISIBLE);
        videoView.suspend();
    }
}
