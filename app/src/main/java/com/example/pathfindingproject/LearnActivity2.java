package com.example.pathfindingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.widget.ImageView;


public class LearnActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn2);

        ImageView imagine1 = findViewById(R.id.imagine1);
        imagine1.setImageResource(R.drawable.bfs);

        TextView textView = findViewById(R.id.textViewLearn);
        String text = getTextFromRawResource(R.raw.breadth_first_search_text);
        textView.setText(text);
        // Găsește butonul "Înapoi" în layout-ul activității
        Button backButton = findViewById(R.id.buttonBack);

        // Setează un listener pentru butonul "Înapoi"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea CourseActivity
                Intent intent = new Intent(LearnActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Funcție pentru citirea textului dintr-un fișier din directorul res/raw
    private String getTextFromRawResource(int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}