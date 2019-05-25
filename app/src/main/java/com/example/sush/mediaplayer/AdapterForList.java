package com.example.sush.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AdapterForList extends RecyclerView.Adapter<AdapterForList.ProgrammingViewHolder> {

    public ArrayList<File> song;
    private Context context;
    String[] sName;
    public AdapterForList(Context context,ArrayList<File> song,String[] sName)
    {
        this.song=song;
        this.context=context;
        this.sName = sName;
    }

    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.resourcefile,parent,false);       //view created
        return new ProgrammingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProgrammingViewHolder holder, final int position) {

        String title = sName[position];                                              //view objects are bind with data
        holder.textView.setText(title);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(context,MainActivity.class);
                i.putExtra("song",song);
                i.putExtra("pos",position);
                i.putExtra("songName",sName);
                context.startActivity(i);

                if(MainActivity.mediaPlayer!=null)
                {
                    MainActivity.mediaPlayer.stop();//if mediaplayer have a song then it stops as soon as the the next song gets selected
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return song.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        LinearLayout parentLayout;
        public ProgrammingViewHolder(View itemView) {               //view sent to be kept in a viewholder

            super(itemView);
            parentLayout = itemView.findViewById(R.id.resourceFileId);
            textView = itemView.findViewById(R.id.textView);
        }
    }

}
