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

public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";
    private Gpio ledGpio;

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
            ledGpio = manager.openGpio("BCM18");
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
                        Log.e(TAG, "onCheckedChanged: ", e);
                        Toast.makeText(HomeActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "SingleLED: ", e);
            Toast.makeText(HomeActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Lukker for ledGpio
        if (ledGpio != null) {
            try {
                ledGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }
}
