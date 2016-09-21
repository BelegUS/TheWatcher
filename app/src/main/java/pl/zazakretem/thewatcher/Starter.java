package pl.zazakretem.thewatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Starter extends AppCompatActivity {

    private boolean serviceStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!serviceStarted) {
                    startService(new Intent(getApplicationContext(), WatcherService.class));
                    serviceStarted = true;
                }
            }
        });

        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceStarted) {
                    stopService(new Intent(getApplicationContext(), WatcherService.class));
                    serviceStarted = false;
                }
            }
        });
    }

    protected void onResume()
    {
        super.onResume();
        if(serviceStarted) {
            startService(new Intent(this, WatcherService.class));
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(serviceStarted) {
            stopService(new Intent(getApplicationContext(), WatcherService.class));
        }
    }
}
