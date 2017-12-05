package nuim.cs.david.mp3player;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static  int SPLASH_TIME_OUT = 4000; // splash screen, time it takes to change screen

    private ListView listView;
    static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

        listView = (ListView) findViewById(R.id.list_view1);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST); // an array list of songs on external sd card
            }
        }
                else
                {
                result();
                }

    }
        public void result()
    {
        listView = (ListView) findViewById(R.id.list_view1);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // open music player to play desired song
            }
        });



    }



    public void getMusic()
    {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver.query(uri,null,null,null,null);
        if(songcursor!=null && songcursor.moveToFirst())
        {
            int songTitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                String currentTitle = songcursor.getString(songTitle);
                String currentArtist = songcursor.getString(songArtist);
                String currentLocation = songcursor.getString(songLocation);
                arrayList.add("Title: " + currentTitle + "\n"  + "Artist: " + currentArtist + "\n" + "Location: " + currentLocation);
            }
            while (songcursor.moveToNext());
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
                        result();
                    } else {
                        Toast.makeText(this, "not granted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }
}

