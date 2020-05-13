package com.example.myfirstandroidthingsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the PeripheralManager
 * For example, the snippet below will open a GPIO pin and set it to HIGH:
 * <p>
 * PeripheralManager manager = PeripheralManager.getInstance();
 * try {
 * Gpio gpio = manager.openGpio("BCM6");
 * gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * gpio.setValue(true);
 * } catch (IOException e) {
 * Log.e(TAG, "Unable to access GPIO");
 * }
 * <p>
 * You can find additional examples on GitHub: https://github.com/androidthings
 */
public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SingleLED();
    }

    private void SingleLED(){
        //Oversigt over tilgængelige perifere enheder
        PeripheralManager manager = PeripheralManager.getInstance();

        //Switch forbindes med xml switch
        Switch ledSwitch = findViewById(R.id.ledSwitch);
        try {
            //Åbner en GPIO pin 18
            final Gpio ledGpio = manager.openGpio("BCM18");
            //Sætter retningen for pin 18 til DIRECTION_OUT_INITIALLY_LOW aka setValue(false)
            ledGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            //Lytter om switchen ændrer sig og sætter LED til samme stadie
            ledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        //LED sættes til samme stadie som switch
                        ledGpio.setValue(isChecked);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(HomeActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
