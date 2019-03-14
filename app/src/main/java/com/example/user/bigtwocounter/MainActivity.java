package com.example.user.bigtwocounter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText left1,left2,left3,left4,name1,name2,name3,name4;
    TextView score1,score2,score3,score4;
    ListView history;

    ArrayList<String> listItems=new ArrayList<String>();
    MyCustomAdapter adapter;
    TinyDB tinydb;
    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        left1=findViewById(R.id.left1);
        left2=findViewById(R.id.left2);
        left3=findViewById(R.id.left3);
        left4=findViewById(R.id.left4);
        score1=findViewById(R.id.score1);
        score2=findViewById(R.id.score2);
        score3=findViewById(R.id.score3);
        score4=findViewById(R.id.score4);
        name1=findViewById(R.id.name1);
        name2=findViewById(R.id.name2);
        name3=findViewById(R.id.name3);
        name4=findViewById(R.id.name4);
        history=findViewById(R.id.history);
        video=findViewById(R.id.video);
        /*adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);*/
        adapter=new MyCustomAdapter(listItems,this, MainActivity.this);
        history.setAdapter(adapter);
        tinydb = new TinyDB(this);
        if(tinydb.getAll().size()!=0){
            name1.setText(tinydb.getString("1name"));
            name2.setText(tinydb.getString("2name"));
            name3.setText(tinydb.getString("3name"));
            name4.setText(tinydb.getString("4name"));
            score1.setText(tinydb.getString("1score"));
            score2.setText(tinydb.getString("2score"));
            score3.setText(tinydb.getString("3score"));
            score4.setText(tinydb.getString("4score"));
            if(Integer.parseInt(score1.getText().toString())==0)
                score1.setTextColor(Color.BLACK);
            else if(Integer.parseInt(score1.getText().toString())>0)
                score1.setTextColor(Color.GREEN);
            else
                score1.setTextColor(Color.RED);
            if(Integer.parseInt(score2.getText().toString())==0)
                score2.setTextColor(Color.BLACK);
            else if(Integer.parseInt(score2.getText().toString())>0)
                score2.setTextColor(Color.GREEN);
            else
                score2.setTextColor(Color.RED);
            if(Integer.parseInt(score3.getText().toString())==0)
                score3.setTextColor(Color.BLACK);
            else if(Integer.parseInt(score3.getText().toString())>0)
                score3.setTextColor(Color.GREEN);
            else
                score3.setTextColor(Color.RED);
            if(Integer.parseInt(score4.getText().toString())==0)
                score4.setTextColor(Color.BLACK);
            else if(Integer.parseInt(score4.getText().toString())>0)
                score4.setTextColor(Color.GREEN);
            else
                score4.setTextColor(Color.RED);
            ArrayList<String> copy=tinydb.getListString("history");
            for(String i:copy){
                listItems.add(i);
            }
            adapter.notifyDataSetChanged();
        }
        TextWatcher textChangeListener=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tinydb.putString("1name",name1.getText().toString());
                tinydb.putString("2name",name2.getText().toString());
                tinydb.putString("3name",name3.getText().toString());
                tinydb.putString("4name",name4.getText().toString());
                tinydb.putString("1score",score1.getText().toString());
                tinydb.putString("2score",score2.getText().toString());
                tinydb.putString("3score",score3.getText().toString());
                tinydb.putString("4score",score4.getText().toString());
            }
        };
        name1.addTextChangedListener(textChangeListener);
        name2.addTextChangedListener(textChangeListener);
        name3.addTextChangedListener(textChangeListener);
        name4.addTextChangedListener(textChangeListener);
    }

    public void Submit(View view){
        final int[] cardsLeft=new int[4];
        try {
            cardsLeft[0] = Integer.parseInt(left1.getText().toString());
            cardsLeft[1] = Integer.parseInt(left2.getText().toString());
            cardsLeft[2] = Integer.parseInt(left3.getText().toString());
            cardsLeft[3] = Integer.parseInt(left4.getText().toString());
            if(cardsLeft[0]!=0 && cardsLeft[1]!=0 && cardsLeft[2]!=0 && cardsLeft[3]!=0){
                Toast.makeText(this,"No one won ah?",Toast.LENGTH_SHORT).show();
                return;
            }
            if(cardsLeft[0]>13 || cardsLeft[1]>13 || cardsLeft[2]>13 || cardsLeft[3]>13){
                Toast.makeText(this,"Wah what game you playing, why cards left got more than 13 one?",Toast.LENGTH_SHORT).show();
                return;
            }
            if(cardsLeft[0]<0 || cardsLeft[1]<0 || cardsLeft[2]<0 || cardsLeft[3]<0){
                Toast.makeText(this,"Why got negative cards left one?",Toast.LENGTH_SHORT).show();
                return;
            }
            int zeroes=0;
            for(int i=0;i<4;i++){
                if(cardsLeft[i]==0)
                    zeroes++;
            }
            if(zeroes!=1){
                Toast.makeText(this,"Wah so many winners, you think this is poker, can split pot ah?",Toast.LENGTH_SHORT).show();
                return;
            }
            listItems.add(0,cardsLeft[0] + ", " + cardsLeft[1] + ", " + cardsLeft[2] + ", " + cardsLeft[3]);
        }catch (Exception ex){
            Toast.makeText(this,"Fill in the blanks with integers.",Toast.LENGTH_SHORT).show();
            return;
        }
        adapter.notifyDataSetChanged();
        int thirteens=0;
        int winner=0;
        for(int i=0;i<4;i++){
            if(cardsLeft[i]==13)
                thirteens++;
            else
                winner=i;
        }
        final int dragonMan=winner;
        if(thirteens==3){
            AlertDialog alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("wtf Dragon?")
                    .setMessage("Did you just Dragon their ass?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int difference[]={0,0,0,0};
                            for(int i=0;i<4;i++){
                                difference[i]=-65;
                            }
                            difference[dragonMan]=195;
                            findViewById(R.id.gifLayout).setVisibility(View.VISIBLE);
                            findViewById(R.id.fadingtext).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.fadingtext)).setText("DRAGON");
                            Glide.with(getApplicationContext()).load(R.drawable.dragon).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into((ImageView)findViewById(R.id.gif));
                            CountDownTimer timer = new CountDownTimer(5000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    findViewById(R.id.gifLayout).setVisibility(View.GONE);
                                    findViewById(R.id.fadingtext).setVisibility(View.GONE);
                                }
                            }.start();
                            updaeScores(difference);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            calculateScores(cardsLeft);
                        }
                    })
                    .show();
        }
        else{
            calculateScores(cardsLeft);
        }
    }

    public void calculateScores(int[] cardsLeft){
        int[] difference={0,0,0,0};
        boolean two=false, three=false;
        for (int i = 0; i < 4; i++) {
            for (int e = i + 1; e < 4; e++) {
                difference[i] += cardsLeft[e] - cardsLeft[i];
                difference[e] -= cardsLeft[e] - cardsLeft[i];
                if (cardsLeft[e] >= 11 || cardsLeft[i] >= 11) {
                    difference[i] += cardsLeft[e] - cardsLeft[i];
                    difference[e] -= cardsLeft[e] - cardsLeft[i];
                    if (cardsLeft[e] == 13 || cardsLeft[i] == 13) {
                        three=true;
                        difference[i] += cardsLeft[e] - cardsLeft[i];
                        difference[e] -= cardsLeft[e] - cardsLeft[i];
                    }
                    else{
                        two=true;
                    }
                }
            }
        }
        if(three){
            video.setVisibility(View.VISIBLE);
            findViewById(R.id.fadingtext).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.fadingtext)).setText("TRIPLE");
            String path = "android.resource://" + getPackageName() + "/" + R.raw.triple;
            video.setVideoURI(Uri.parse(path));
            video.start();
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp)
                {
                    findViewById(R.id.fadingtext).setVisibility(View.GONE);
                    findViewById(R.id.videoLayout).setVisibility(View.GONE);
                }
            });
        }
        else if(two){
            findViewById(R.id.fadingtext).setVisibility(View.VISIBLE);
            findViewById(R.id.videoLayout).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.fadingtext)).setText("TIMES TWO");
            String path = "android.resource://" + getPackageName() + "/" + R.raw.times_two;
            video.setVideoURI(Uri.parse(path));
            video.start();
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp)
                {
                    findViewById(R.id.fadingtext).setVisibility(View.GONE);
                    findViewById(R.id.videoLayout).setVisibility(View.GONE);
                }
            });
        }
        updaeScores(difference);
    }

    public void updaeScores(int[] difference){
        score1.setText(String.valueOf(Integer.parseInt(score1.getText().toString())+difference[0]));
        if(Integer.parseInt(score1.getText().toString())==0)
            score1.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score1.getText().toString())>0)
            score1.setTextColor(Color.GREEN);
        else
            score1.setTextColor(Color.RED);
        score2.setText(String.valueOf(Integer.parseInt(score2.getText().toString())+difference[1]));
        if(Integer.parseInt(score2.getText().toString())==0)
            score2.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score2.getText().toString())>0)
            score2.setTextColor(Color.GREEN);
        else
            score2.setTextColor(Color.RED);
        score3.setText(String.valueOf(Integer.parseInt(score3.getText().toString())+difference[2]));
        if(Integer.parseInt(score3.getText().toString())==0)
            score3.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score3.getText().toString())>0)
            score3.setTextColor(Color.GREEN);
        else
            score3.setTextColor(Color.RED);
        score4.setText(String.valueOf(Integer.parseInt(score4.getText().toString())+difference[3]));
        if(Integer.parseInt(score4.getText().toString())==0)
            score4.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score4.getText().toString())>0)
            score4.setTextColor(Color.GREEN);
        else
            score4.setTextColor(Color.RED);
        left1.setText("");
        left2.setText("");
        left3.setText("");
        left4.setText("");
        Toast.makeText(this,"Scores updated",Toast.LENGTH_SHORT).show();
        tinydb.putListString("history", listItems);
        tinydb.putString("1score",score1.getText().toString());
        tinydb.putString("2score",score2.getText().toString());
        tinydb.putString("3score",score3.getText().toString());
        tinydb.putString("4score",score4.getText().toString());
        tinydb.putString("1name",name1.getText().toString());
        tinydb.putString("2name",name2.getText().toString());
        tinydb.putString("3name",name3.getText().toString());
        tinydb.putString("4name",name4.getText().toString());
    }

    public void remove(int position){
        int[] cardsLeft=new int[4];
        for(int i=0;i<4;i++){
            cardsLeft[i]=Integer.parseInt(listItems.get(position).split(", ")[i]);
        }
        int difference[]={0,0,0,0};
        int thirteens=0;
        int winner=0;
        for(int i=0;i<4;i++){
            if(cardsLeft[i]==13)
                thirteens++;
            else
                winner=i;
        }
        if(thirteens==3){
            for(int i=0;i<4;i++){
                difference[i]=-65;
            }
            difference[winner]=195;
        }
        else {
            for (int i = 0; i < 4; i++) {
                for (int e = i + 1; e < 4; e++) {
                    difference[i] += cardsLeft[e] - cardsLeft[i];
                    difference[e] -= cardsLeft[e] - cardsLeft[i];
                    if (cardsLeft[e] >= 11 || cardsLeft[i] >= 11) {
                        difference[i] += cardsLeft[e] - cardsLeft[i];
                        difference[e] -= cardsLeft[e] - cardsLeft[i];
                        if (cardsLeft[e] == 13 || cardsLeft[i] == 13) {
                            difference[i] += cardsLeft[e] - cardsLeft[i];
                            difference[e] -= cardsLeft[e] - cardsLeft[i];
                        }
                        else{
                        }
                    }
                }
            }
        }
        score1.setText(String.valueOf(Integer.parseInt(score1.getText().toString())-difference[0]));
        if(Integer.parseInt(score1.getText().toString())==0)
            score1.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score1.getText().toString())>0)
            score1.setTextColor(Color.GREEN);
        else
            score1.setTextColor(Color.RED);
        score2.setText(String.valueOf(Integer.parseInt(score2.getText().toString())-difference[1]));
        if(Integer.parseInt(score2.getText().toString())==0)
            score2.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score2.getText().toString())>0)
            score2.setTextColor(Color.GREEN);
        else
            score2.setTextColor(Color.RED);
        score3.setText(String.valueOf(Integer.parseInt(score3.getText().toString())-difference[2]));
        if(Integer.parseInt(score3.getText().toString())==0)
            score3.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score3.getText().toString())>0)
            score3.setTextColor(Color.GREEN);
        else
            score3.setTextColor(Color.RED);
        score4.setText(String.valueOf(Integer.parseInt(score4.getText().toString())-difference[3]));
        if(Integer.parseInt(score4.getText().toString())==0)
            score4.setTextColor(Color.BLACK);
        else if(Integer.parseInt(score4.getText().toString())>0)
            score4.setTextColor(Color.GREEN);
        else
            score4.setTextColor(Color.RED);
        left1.setText("");
        left2.setText("");
        left3.setText("");
        left4.setText("");
        listItems.remove(position);
        Toast.makeText(this,"Scores updated",Toast.LENGTH_SHORT).show();
        tinydb.putListString("history", listItems);
        tinydb.putString("1score",score1.getText().toString());
        tinydb.putString("2score",score2.getText().toString());
        tinydb.putString("3score",score3.getText().toString());
        tinydb.putString("4score",score4.getText().toString());
        adapter.notifyDataSetChanged();
    }

    public void closeVideo(View v){
        video.stopPlayback();
        findViewById(R.id.fadingtext).setVisibility(View.GONE);
        findViewById(R.id.videoLayout).setVisibility(View.GONE);
    }

    public void closeGif(View v){
        findViewById(R.id.fadingtext).setVisibility(View.GONE);
        findViewById(R.id.gifLayout).setVisibility(View.GONE);
    }

    public void restartGame(View v){
        AlertDialog alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Restart Game")
                .setMessage("All data will be erased")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listItems.clear();
                        adapter.notifyDataSetChanged();
                        name1.setText("");
                        name2.setText("");
                        name3.setText("");
                        name4.setText("");
                        score1.setText("0");
                        score2.setText("0");
                        score3.setText("0");
                        score4.setText("0");
                        score1.setTextColor(Color.BLACK);
                        score2.setTextColor(Color.BLACK);
                        score3.setTextColor(Color.BLACK);
                        score4.setTextColor(Color.BLACK);
                        tinydb.clear();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
