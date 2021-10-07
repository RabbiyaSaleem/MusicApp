 package com.uog.fyp.e.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.uog.fyp.e.musicapp.Foreground;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button playbtn,nextbtn,prevbtn,forwdbtn,rewbtn;
    TextView txtstart,txtstop,txtsng;
    SeekBar seekbar;
    CircleLineVisualizer circle;

    String sname;
    public static final  String Extra_Name="song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mysongs;
    Thread updateseekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playbtn=findViewById(R.id.playbtn);
        nextbtn=findViewById(R.id.nextbtn);
        prevbtn=findViewById(R.id.prevbtn);
        forwdbtn=findViewById(R.id.forwdbtn);
        rewbtn=findViewById(R.id.rewbtn);
        txtsng=findViewById(R.id.txtsng);
        /*txtstart=findViewById(R.id.txtstart);
        txtstop=findViewById(R.id.txtstop);*/
        seekbar=findViewById(R.id.seekbar);
        circle=findViewById(R.id.circle);

        if (mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysongs=(ArrayList) bundle.getParcelableArrayList("songs");
        String songname=i.getStringExtra("songname");
        position=bundle.getInt("pos",0);
        txtsng.setSelected(true);
        Uri uri=Uri.parse(mysongs.get(position).toString());
        sname=mysongs.get(position).getName();
        txtsng.setText(sname);
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        updateseekbar=new Thread()
        {
            @Override
            public void run(){
                int totalDuration=mediaPlayer.getDuration();
                int currentPosition=0;
                while (currentPosition<totalDuration)
                {
                    try{
                        sleep(500);
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException| IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekbar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekbar.getProgressDrawable().setColorFilter(getResources()
                .getColor(R.color.design_default_color_primary),PorterDuff.Mode.MULTIPLY);
        seekbar.getThumb().setColorFilter(getResources().
                getColor(R.color.design_default_color_primary),PorterDuff.Mode.SRC_IN);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                {
                    playbtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    stopService();
                    mediaPlayer.pause();

                }
                else {
                    playbtn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                     //startService()
                    startService();
                    mediaPlayer.start();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextbtn.performClick();
            }
        });
           nextbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  mediaPlayer.stop();
                  mediaPlayer.release();
                   position=((position+1)%mysongs.size());
                   Uri u=Uri.parse(mysongs.get(position).toString());
                   mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                   sname=mysongs.get(position).getName();
                   txtsng.setText(sname);
                   playbtn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                   mediaPlayer.start();

               }
           });
           prevbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   mediaPlayer.stop();
                   mediaPlayer.release();
                   position=((position-1)<0)?(mysongs.size()-1):(position-1);
                   Uri u=Uri.parse(mysongs.get(position).toString());
                   mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                   sname=mysongs.get(position).getName();
                   txtsng.setText(sname);
                   prevbtn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                   mediaPlayer.start();
               }
           });
    }
    public void startService() {
        Intent serviceIntent = new Intent(this, Foreground.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, Foreground.class);
        stopService(serviceIntent);
    }
}