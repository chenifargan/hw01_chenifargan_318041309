package com.example.hw01;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ImageView[][] path;
    private ImageButton[] panel_BTN_LeftOrRight;
    private ImageView[] panel_IMG_hearts;
    private ImageView[] players;
    private int[][] values;
    private int playerPos =1;
    private Random rand = new Random();
    private final int MAX_LIVES = 3;
    private int lives = MAX_LIVES;
    private Timer timer;
    private MediaPlayer spongeBob_crying;
    private boolean updated = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        buttonsListener();
        initViews();
        startTicker();
    }
    private void findViews() {
        panel_IMG_hearts = new ImageView[]{
                findViewById(R.id.panel_IMG_heart1),
                findViewById(R.id.panel_IMG_heart2),
                findViewById(R.id.panel_IMG_heart3)
        };
        panel_BTN_LeftOrRight = new ImageButton[]{
                findViewById(R.id.buttonLeft),
                findViewById(R.id.buttonRight)
        };
        players =new ImageView[]{
                findViewById(R.id.imageViewLeft),
                findViewById(R.id.imageView),
                findViewById(R.id.imageViewRight)
        };
        path = new ImageView[][]{
                {findViewById(R.id.demo_IMG_01), findViewById(R.id.demo_IMG_02), findViewById(R.id.demo_IMG_03)},
                {findViewById(R.id.demo_IMG_11), findViewById(R.id.demo_IMG_12), findViewById(R.id.demo_IMG_13)},
                {findViewById(R.id.demo_IMG_21), findViewById(R.id.demo_IMG_22), findViewById(R.id.demo_IMG_23)},
                {findViewById(R.id.demo_IMG_31), findViewById(R.id.demo_IMG_32), findViewById(R.id.demo_IMG_33)}
        };
        spongeBob_crying = MediaPlayer.create(MainActivity.this, R.raw.bobsound);

        values = new int[path.length+1][path[0].length];



    }
    private void initViews() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] = 0;
            }
        }
        int randomNum = rand.nextInt(3) ;
        values[0][randomNum]=1;
        updateUI();

    }
    private void down() {
        for (int i = values.length - 1; i > 0; i--) {
            values[i][0] = values[i - 1][0];
            values[i][1] = values[i - 1][1];
            values[i][2] = values[i - 1][2];

        }


        downFirstRow();
    }
    private void downFirstRow() {
        values[0][0]=0;
        values[0][1]=0;
        values[0][2]=0;
        if(updated) {
            int randomNum = rand.nextInt(3);
            values[0][randomNum]=1;
            updated = false ;
        }
        else{
            updated = true;
        }
    }

    private void updateUI() {
        for (int i = 0; i < values.length-1; i++) {
            for (int j = 0; j < values[0].length ; j++) {
                if(values[i][j]==1){
                    path[i][j].setVisibility(View.VISIBLE);
                }
                else if(values[i][j]==0){
                    path[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
        if(crash()==1){
            lives--;
            soundCrying();
            updateLivesViews();
            vibrate();
        }
        if(lives==0){
            Toast.makeText(this, "GAME OVER!!!!!", Toast.LENGTH_SHORT).show();
            vibrate();
            finish();
        }
    }

    private void soundCrying() {
        spongeBob_crying.start();
    }

    private void startTicker() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                down();
                                updateUI();

                            }
                        });


                    }
                });
            }
        }, 0, 1000);

    }
    private void buttonsListener(){
        for(int i = 0; i < panel_BTN_LeftOrRight.length; i++){
            final int buttonIndex = i;
            panel_BTN_LeftOrRight[i].setOnClickListener(v -> move(buttonIndex));
        }
    }
    private void move(int buttonIndex){
        if(buttonIndex == 0){
            if(playerPos > 0){
                players[playerPos--].setVisibility(View.INVISIBLE);
                players[playerPos].setVisibility(View.VISIBLE);


            }
        }else{
            if(playerPos < 2){
                players[playerPos++].setVisibility(View.INVISIBLE);
                players[playerPos].setVisibility(View.VISIBLE);


            }
        }
    }
    private void updateLivesViews() {

       panel_IMG_hearts[lives].setVisibility(View.INVISIBLE);
    }
    private int crash(){
        if (lives > 0 ) {
            for (int M = 0; M < values[0].length; M++) {
                if (players[M].getVisibility() == View.VISIBLE && values[4][M] == 1) {
                    return 1;
                }
            }
        }

        return 0;
    }
    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}
