package com.example.sush.mediaplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class SongList extends AppCompatActivity {

    RecyclerView songlist;
    static String item[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        getSupportActionBar().setTitle(R.string.mysongs);

        songlist = findViewById(R.id.programmingList);//listview
        runtimePermission();


    }


    public void runtimePermission()//used for taking permissions when the app starts
    {
        Dexter.withActivity(this).
                withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).//mention in manifest also
                withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        display();//if permission is granted
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();//if permission denied for the first time....then continue asking permission for the next time

                    }
                }).check();

    }


    public ArrayList<File> findSong(File file) //function that takes path as a parameter and returns an Array of type File having files required by you
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singlefile:files)//going deep in the files
        {
            if(singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findSong(singlefile));
            }

            else
            {
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav"))//the type of file you require
                {
                    arrayList.add(singlefile);
                }
            }
        }

        return arrayList;// Array of Files having .mp3 and .wav as extension
    }


    void display()
    {

        final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());//recieving array of songs

        item = new String[mySong.size()];

        for(int i=0;i<mySong.size();i++)
        {
            item[i] = mySong.get(i).getName().toString().replace(".mp3","").replace(".wav","");
            //Example ---- replacing song.mp3 with song
        }

        songlist.setLayoutManager(new LinearLayoutManager(this));
        songlist.setAdapter(new AdapterForList(this,mySong));

    }

    @Override
    protected void onDestroy() {

        if(MainActivity.mediaPlayer != null && MainActivity.mediaPlayer.isPlaying()) {

            MainActivity.mediaPlayer.stop();
            MainActivity.mediaPlayer.release();
            MainActivity.mediaPlayer = null;
        }
       /* MainActivity.thread.interrupt();
        MainActivity.thread=null;*/

        super.onDestroy();
    }

}
