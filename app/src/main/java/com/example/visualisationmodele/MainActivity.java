package com.example.visualisationmodele;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chargerModele = findViewById(R.id.button_charger_modele);
        Button visualisationModele = findViewById(R.id.button_visualiser_modele);

        sendIntent(visualisationModele,VisualisationModele.class);
        sendIntent(chargerModele,ChargementModele.class);
    }
    public void sendIntent(Button button, Class c){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,c);
                startActivity(intent);
            }
        });
    }
}