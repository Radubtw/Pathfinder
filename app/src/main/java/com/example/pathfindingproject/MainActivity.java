package com.example.pathfindingproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startLearning(View view) {
        Intent intent = new Intent(this, LearnActivity.class);
        startActivity(intent);
    }

    public void startLearning2(View view) {
        Intent intent = new Intent(this, LearnActivity2.class);
        startActivity(intent);
    }

    public void startVisualizing(View view) {
        Intent intent = new Intent(this, VisualizationActivity.class);
        startActivity(intent);
    }

    public void quitApp(View view) {
        finishAffinity();
        //System.exit(0);
    }
}
