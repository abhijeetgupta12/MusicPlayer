package com.example.sush.mediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button prev,play,next;
    private TextView righttext,lefttext,marque;
    private SeekBar seekBar;
    public static MediaPlayer mediaPlayer;
    public static Thread thread;

    //Adddddddd

    int position;
    String[] sName;
    ArrayList mySongs;//Arraylist<File>


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("@@@","Hello");

        Objects.requireNonNull(getSupportActionBar()).setTitle("Now Playing");

        setID();//assigning all id's

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b!=null)//retrieving values from intent
        {
            position = b.getInt("pos", 0);
            sName = b.getStringArray("songName");
            mySongs = (ArrayList)b.getParcelableArrayList("song");
        }
        marque.setText(sName[position]);
        marque.setSelected(true);

        Uri u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        play();



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying())
                {
                    pause();
                }
                else
                {
                    play();
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer=null;

                if(position<=0)
                {
                    position=sName.length;
                }

                Uri u = Uri.parse(mySongs.get(--position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                String songname = sName[position];
                marque.setText(songname);
                play();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer=null;

                if(position==sName.length-1)
                {
                    position=-1;
                }

                Uri u = Uri.parse(mySongs.get(++position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                String songname = sName[position];
                marque.setText(songname);
                play();
            }
        });


          seekBar.setMax(mediaPlayer.getDuration());
          seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
              @Override
              public void onProgressChanged(SeekBar seekBar, int seekbarprogress, boolean touchfromUser) {
                  if(touchfromUser)
                  {
                      mediaPlayer.seekTo(seekbarprogress);
                  }
                  SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                  int currentPos = mediaPlayer.getCurrentPosition();
                  int finalPos = mediaPlayer.getDuration();

                  lefttext.setText(dateFormat.format(new Date(currentPos-30*60*1000)));
                  righttext.setText(dateFormat.format(new Date(finalPos-30*60*1000)));
              }

              @Override
              public void onStartTrackingTouch(SeekBar seekBar) {

              }

              @Override
              public void onStopTrackingTouch(SeekBar seekBar) {

              }
          });


    }


    public void play()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.start();
            updateThread();
            play.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    public void pause()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
            play.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }


    public void setID()
    {
       marque = findViewById(R.id.marque);
       prev = (Button) findViewById(R.id.prevID);
       play = (Button) findViewById(R.id.playID);
       next = (Button) findViewById(R.id.nextID);
       righttext = (TextView) findViewById(R.id.righttext);
       lefttext = (TextView) findViewById(R.id.lefttext);
       seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.about:
                Intent i = new Intent(MainActivity.this, about.class);
                startActivity(i);
                break;


            case R.id.exit:
                MainActivity.this.finish();
                break;
        }
        return true;
    }



    public void updateThread()
    {

         thread = new Thread()
         {
             @Override
             public void run() {
                 try {
                     while (mediaPlayer != null && mediaPlayer.isPlaying()) {


                         thread.sleep(500);
                         runOnUiThread(new Runnable() { //use to access the elements of main UI Thread
                             @Override
                             public void run() {

                                 int newPos = mediaPlayer.getCurrentPosition();
                                 int max = mediaPlayer.getDuration();
                                 seekBar.setMax(max);
                                 seekBar.setProgress(newPos);

                                 if(String.valueOf(new SimpleDateFormat("mm:ss")
                                         .format((new Date(mediaPlayer.getCurrentPosition()-30*60*1000))))
                                         .compareTo(new SimpleDateFormat("mm:ss")
                                         .format((new Date(mediaPlayer.getDuration()-30*60*1000-1000))))==0)
                                 {
                                     mediaPlayer.stop();
                                     Uri u = Uri.parse(mySongs.get(++position).toString());
                                     mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                                     String songname = sName[position];
                                     marque.setText(songname);
                                     play();

                                 }


                                 lefttext.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                                   .format((new Date(mediaPlayer.getCurrentPosition()-30*60*1000)))));

                                                            }
                         });

                     }
                     }catch(InterruptedException e){
                         e.printStackTrace();
                     }


             }
         };
         thread.start();

    }



    @Override
    protected void onDestroy() {
       /* if(mediaPlayer != null && mediaPlayer.isPlaying()) {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }*/
        thread.interrupt();
       thread = null;
        super.onDestroy();
    }

}

