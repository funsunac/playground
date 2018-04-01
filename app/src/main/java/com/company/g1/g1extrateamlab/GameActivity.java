package com.company.g1.g1extrateamlab;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    Spaceship           spaceship;
    final int           PITCH_OFFSET = 5;   // Accelerometer Y-axis offset
    SensorManager       sensorManager;
    Sensor              accelerometer;
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) { }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            spaceship.accelerate(event.values[0], event.values[1] - PITCH_OFFSET);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GameView gameView = new GameView(this);
        setContentView(gameView);

        gameView.post(new Runnable() {
            @Override
            public void run() {
                GameObject.xBound = gameView.getWidth();
                GameObject.yBound = gameView.getHeight();
            }
        });

        spaceship = new Spaceship();
        gameView.spaceship = this.spaceship;

//        gameLayout.setOnTouchListener(new OnRotationListener(){
//            @Override
//            public void onRotation() {
//                spaceship.setRotation(this.getAngle());
//            }
//        });

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else
            sensorManager = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
