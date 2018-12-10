package com.adev.android.legomindfuck.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.adev.android.legomindfuck.Motor;
import com.adev.android.legomindfuck.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.adev.android.legomindfuck.Activity.PlayMenuActivity.sColorSensor;
import static com.adev.android.legomindfuck.Activity.PlayMenuActivity.sUltrasonicSensor;

import static com.adev.android.legomindfuck.Activity.MainMenuActivity.ev3;
import static com.adev.android.legomindfuck.Statistics.players;

public class MotorActivity extends AppCompatActivity {

    private Motor mMotorBase;
    private Motor mMotorBraccio;
    private Motor mMotorMano;

    private ImageButton mButtonBaseLeft;
    private ImageButton mButtonBaseRight;

    private ImageButton mButtonBraccioLeft;
    private ImageButton mButtonBraccioRight;

    private ImageButton mButtonManoLeft;
    private ImageButton mButtonManoRight;

    private Switch mSpeedSwitch;

    private Button mPickUpButton;
    private Button mPutDownButton;
    private Button mCheckColorButton;
    private Button mStopButton;

    private TextView mColorCheck;

    private int mSpeed = 5;

    Timer timer = new Timer();
    private Double time = 0.0;
    private TextView timeText;

    //Minuti e secondi del timer sono stati spostati come variabili di classe in quanto vengono passate a endgame.class
    private Double mins = 0.0;
    private Double secs = 0.0;

    //variabili per la colorazione dei blocchi
    private Integer numberofblock = 0;
    private Integer blockcolor;
    private ImageView[] blockplaced = new ImageView[4];
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Blocchi checked
        blockplaced[0]=findViewById(R.id.blockPlaced_1);
        blockplaced[1]=findViewById(R.id.blockPlaced_2);
        blockplaced[2]=findViewById(R.id.blockPlaced_3);
        blockplaced[3]=findViewById(R.id.blockPlaced_4);


        mStopButton = findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ev3.sendMessage("#apstop#");
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), EndGameActivity.class);
                        i.putExtra("min", mins);
                        i.putExtra("sec", secs);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });

        timeText = findViewById(R.id.seconds_textBox);

        timer.scheduleAtFixedRate(new TimerTask() {

            private String min = "min: ";
            private String sec = "  sec: ";


            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mins = time / 60;
                        String[] formatM = mins.toString().split("\\.");
                        secs = time % 60;
                        String[] formatS = secs.toString().split("\\.");
                        timeText.setText(min + formatM[0] + sec + formatS[0] + "," + formatS[1].charAt(0));
                        time += 0.1;
                    }

                });
            }
        }, 100, 100);

        //if (ev3 == null) ev3 = new SocketManager();
        //ev3.openSocket();

        if (mMotorBase == null) {
            mMotorBase = new Motor(1, 180);
        }

        if (mMotorBraccio == null) {
            mMotorBraccio = new Motor(4, 180);
        }

        if (mMotorMano == null) {
            mMotorMano = new Motor(7, 180);
        }

        mButtonBaseLeft = findViewById(R.id.motor1_left);
        mButtonBaseLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ev3.sendMessage(mMotorBase.motorOn(mSpeed, "-"));
                        // PRESSED
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ev3.sendMessage(mMotorBase.motorOff());
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        mButtonBaseRight = findViewById(R.id.motor1_right);
        mButtonBaseRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ev3.sendMessage(mMotorBase.motorOn(mSpeed, "+"));
                        // PRESSED
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ev3.sendMessage(mMotorBase.motorOff());
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        mButtonBraccioLeft = findViewById(R.id.motor2_left);
        mButtonBraccioLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ev3.sendMessage(mMotorBraccio.motorOn(mSpeed, "+"));
                        // PRESSED
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ev3.sendMessage(mMotorBraccio.motorOff());
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        mButtonBraccioRight = findViewById(R.id.motor2_right);
        mButtonBraccioRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ev3.sendMessage(mMotorBraccio.motorOn(mSpeed, "-"));
                        // PRESSED
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ev3.sendMessage(mMotorBraccio.motorOff());
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        mButtonManoLeft = findViewById(R.id.motor3_left);
        mButtonManoLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ev3.sendMessage(mMotorMano.motorOn(mSpeed, "+"));
                        // PRESSED
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ev3.sendMessage(mMotorMano.motorOff());
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        mButtonManoRight = findViewById(R.id.motor3_right);
        mButtonManoRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ev3.sendMessage(mMotorMano.motorOn(mSpeed, "-"));
                        // PRESSED
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ev3.sendMessage(mMotorMano.motorOff());
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });

        mSpeedSwitch = findViewById(R.id.speed_switch);
        mSpeedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSpeed == 5) {
                    mSpeed = 15;
                } else {
                    mSpeed = 5;
                }
            }
        });

        mPickUpButton = findViewById(R.id.pick_up_button);
        mPickUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            ev3.sendMessage(mMotorBraccio.move(3, 10, "+"));
                            sleep(100);
                            ev3.sendMessage(mMotorMano.move(20, 20, "+"));
                            sleep(500);
                            ev3.sendMessage(mMotorBraccio.move(4, 10, "-"));
                            sleep(400);
                            ev3.sendMessage(mMotorMano.move(10, 20, "-"));
                            sleep(400);
                            ev3.sendMessage(mMotorBraccio.move(5, 10, "-"));
                            sleep(500);
                            ev3.sendMessage(mMotorMano.move(25, 20, "-"));
                            sleep(600);
                            ev3.sendMessage(mMotorBraccio.move(20, 15, "+"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
            }
        });

        mPutDownButton = findViewById(R.id.put_down_button);
        mPutDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            ev3.sendMessage(mMotorBraccio.move(4, 5, "-"));
                            sleep(250);
                            ev3.sendMessage(mMotorMano.move(8, 20, "+"));
                            sleep(250);
                            ev3.sendMessage(mMotorBraccio.move(2, 5, "+"));
                            sleep(250);
                            ev3.sendMessage(mMotorMano.move(8, 20, "+"));
                            sleep(250);
                            ev3.sendMessage(mMotorBraccio.move(2, 5, "+"));
                            sleep(250);
                            ev3.sendMessage(mMotorMano.move(10, 20, "+"));
                            sleep(250);
                            ev3.sendMessage(mMotorBraccio.move(15, 10, "+"));
                            sleep(300);
                            ev3.sendMessage(mMotorMano.move(15, 20, "-"));
                            sleep(300);
                            ev3.sendMessage(mMotorBraccio.move(9, 10, "-"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();

                mColorCheck.setVisibility(View.INVISIBLE);
                mCheckColorButton.setVisibility(View.VISIBLE);
            }
        });

        mCheckColorButton = findViewById(R.id.check_color_button);
        mCheckColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ev3.sendMessage("#arub#");
                numberofblock += 1;
                Thread u = new Thread() {

                    @Override
                    public void run() {
                        try {
                            synchronized (sUltrasonicSensor) {
                                sUltrasonicSensor.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (sUltrasonicSensor.getDistance() < 19) ev3.sendMessage(mMotorBraccio.motorOn(5, "-"));
                        else if (sUltrasonicSensor.getDistance() > 19) ev3.sendMessage(mMotorBraccio.motorOn(5, "+"));

                        try {
                            synchronized (sUltrasonicSensor.mutex) {
                                sUltrasonicSensor.mutex.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ev3.sendMessage(mMotorBraccio.motorOff());

                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                u.start();

                try {
                    u.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ev3.sendMessage("#arcb#");

                ev3.sendMessage(mMotorMano.motorOn(5, "+"));

                Thread t = new Thread() {
                    private int prevColor = 9;
                    private int cycles = 0;

                    @Override
                    public void run() {

                        try {
                            synchronized (sColorSensor) {
                                sColorSensor.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ev3.sendMessage(mMotorMano.motorOff());

                        while (prevColor != sColorSensor.getColor() || prevColor == 0) {

                            prevColor = sColorSensor.getColor();

                            ev3.sendMessage(mMotorMano.move(3, 4, "+"));

                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            cycles++;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeColorCheck(prevColor);
                            }
                        });

                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ev3.sendMessage(mMotorMano.move(10 + cycles * 3, 20, "-"));

                    }
                };
                t.start();
            }
        });

        mColorCheck = (TextView) findViewById(R.id.color_check);
    }

    private void changeColorCheck(int c) {
        switch (c) {
            case 1:
                mColorCheck.setBackgroundColor(getResources().getColor(R.color.mColorBlack));
                mColorCheck.setText("Nero");
                blockcolor = R.color.mColorBlack;
                break;
            case 2:
                mColorCheck.setBackgroundColor(getResources().getColor(R.color.mColorBlue));
                mColorCheck.setText("Blu");
                blockcolor = R.color.mColorBlue;
                break;
            case 4:
                mColorCheck.setBackgroundColor(getResources().getColor(R.color.mColorYellow));
                mColorCheck.setText("Giallo");
                blockcolor = R.color.mColorYellow;
                break;
            case 5:
                mColorCheck.setBackgroundColor(getResources().getColor(R.color.mColorRed));
                mColorCheck.setText("Rosso");
                blockcolor = R.color.mColorRed;
                break;
            default:
                break;
        }
        mColorCheck.setVisibility(View.VISIBLE);
        mCheckColorButton.setVisibility(View.INVISIBLE);
        //Aggiunta per vedere i blocchi presi (potrebbe essere migliorata in futuro)

        switch (numberofblock%4){
            case 1:
                blockplaced[0].setImageResource(blockcolor);
                break;
            case 2:
                blockplaced[1].setImageResource(blockcolor);
                break;
            case 3:
                blockplaced[2].setImageResource(blockcolor);
                break;
            case 0:
                blockplaced[3].setImageResource(blockcolor);
                break;
            default:
                break;
        }
    }

}

/*
    STATISTICHE DI GIOCO :

        + TEMPO DI GIOCO SP
        + TEMPO DI GIOCO MP
        + DIFFICOLTA' IN MATTONCINI (diamo un punteggio in base all'altezza della torre da costruire)
        +
 */