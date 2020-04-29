package com.example.myfirstandroidthingsapplication;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Gpio;

import java.io.IOException;

public class BlinkActivity extends Activity {
    private static final String TAG = "BlinkActivity";
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;
    private static final String LED_PIN_NAME = ""; // GPIO port wired to the LED

    private Handler handler = new Handler();

    private Gpio ledGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Step 1. Create GPIO connection.
        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            ledGpio = manager.openGpio(LED_PIN_NAME);
            // Step 2. Configure as an output.
            ledGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            // Step 4. Repeat using a handler.
            handler.post(blinkRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Step 4. Remove handler events on close.
        handler.removeCallbacks(blinkRunnable);

        // Step 5. Close the resource.
        if (ledGpio != null) {
            try {
                ledGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }

    private Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            // Exit if the GPIO is already closed
            if (ledGpio == null) {
                return;
            }

            try {
                // Step 3. Toggle the LED state
                ledGpio.setValue(!ledGpio.getValue());

                // Step 4. Schedule another event after delay.
                handler.postDelayed(blinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };
}