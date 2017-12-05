package nuim.cs.david.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;



public class HomeActivity extends AppCompatActivity {




    SeekBar seekBar;
    MediaPlayer player;
    int pause;
    Handler handler;
    Runnable runnable;
    Button play;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        handler = new Handler();

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        player = MediaPlayer.create(getApplicationContext(), R.raw.jar);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);


                seekBar.setMax(player.getDuration());
                playCycle();
                player.start();
        play = (Button) findViewById(R.id.playbtn);
        final MediaPlayer player = MediaPlayer.create(HomeActivity.this, R.raw.jar);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                    play.setBackgroundResource(R.drawable.playbtn);
                } else {
                    player.start();
                    play.setBackgroundResource(R.drawable.pausebtn);
                }
            }
        });




        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input)
            {
                if(input)
                {
                    player.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

    }
    public void playCycle()
    {
        seekBar.setProgress(player.getCurrentPosition());

        if(player.isPlaying())
        {
            runnable = new Runnable()
            {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        player.start();

    }
    @Override
    protected void onPause()
    {
       super.onPause();
       player.pause();


    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        player.release();
        handler.removeCallbacks(runnable);
    }


    public void playSong(View v) // plays song
    {
       if(player == null)
       {
           player = MediaPlayer.create(this, R.raw.jar);
           player.start();
       }
       else if(!player.isPlaying()) // if paused, plays from resumed position
       {
        player.seekTo(pause);
        player.start();
       }
    }

    public void pauseSong(View v) // pauses song
    {
        player.pause();
        pause = player.getCurrentPosition();
    }

    public void stopSong(View v)
    {
        if(player == null)
        {
            Toast.makeText(getApplicationContext(),"Song is already stopped", Toast.LENGTH_LONG).show();
        }
        else{
            player.stop();
            player=null;
        }

    }

}
