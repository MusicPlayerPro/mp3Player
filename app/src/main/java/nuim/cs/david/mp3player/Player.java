package nuim.cs.david.mp3player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.io.File;
import java.util.ArrayList;


public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    Thread updateSeekBar;
    Button rot;
    Animation rotate;
    SeekBar sb;
    Button btPlay, btFF, btRW, btNxt, btPre, btshf, btrep;
    ImageView robt;
    ArcSeekBar ArcSeekBar;




    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private boolean isShuffle = false;
    private boolean isRepeat = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();

        ArcSeekBar = (ArcSeekBar) findViewById(R.id.seekBar2);


        int[] array = getResources().getIntArray(R.array.gradient);
        ArcSeekBar.setProgressGradient(array);

        rotate = AnimationUtils.loadAnimation(this, R.anim.rotation);
        rotate.setFillAfter(true);

        rot = (Button) findViewById(R.id.btrot);
        rot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView imv = (ImageView) findViewById(R.id.robt);
                //imv.setRotation(imv.getRotation() + 45);
                imv.startAnimation(rotate);

            }
            });

        ArcSeekBar.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int i) {

            }
        });

        







        robt = (ImageView) findViewById(R.id.robt);
        btPlay = (Button) findViewById(R.id.btPlay);
        btFF = (Button) findViewById(R.id.btFF);
        btRW = (Button) findViewById(R.id.btRW);
        btNxt = (Button) findViewById(R.id.btNxt);
        btPre = (Button) findViewById(R.id.btPre);
        btshf = (Button) findViewById(R.id.btshf);
        btrep = (Button) findViewById(R.id.btrep);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btRW.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPre.setOnClickListener(this);
        btshf.setOnClickListener(this);
        btrep.setOnClickListener(this);

        sb = (SeekBar) findViewById(R.id.seekBar3);
        updateSeekBar = new Thread()
        {
           @Override
            public void run(){
               int totalDuration = mp.getDuration();
               int currentPosition = 0;
               while(currentPosition < totalDuration) {
                   try {
                       sleep(500);
                       currentPosition = mp.getCurrentPosition();
                       sb.setProgress(currentPosition);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
               //super.run();
           }
        };

        if(mp!= null)
        {
            mp.stop();
            mp.release();

        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());

        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btPlay:
                if(mp.isPlaying()){
                mp.pause();
                btPlay.setBackgroundResource(R.drawable.playbtn);
                }
                else {
                    mp.start();
                    btPlay.setBackgroundResource(R.drawable.pausebtn);
                }
                    break;

            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btRW:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position+1)%mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPre:
                mp.stop();
                mp.release();
                position = (position-1<0)? mySongs.size()-1: position-1;
                /*if(position-1 < 0)
                {
                    position = mySongs.size()-1;
                }
                else{
                    position = position-1;
                }*/
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.btrep:
                if(mp.isLooping()) {
                    mp.setLooping(true);
                    Toast.makeText(getApplicationContext(), "This is my Toast message!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    mp.setLooping(false);
                }
                break;
            case R.id.btshf:


        }

    }




    private void initControls() {
        try {

            volumeSeekBar = (SeekBar) findViewById(R.id.seekBar2);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,0);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }
}

