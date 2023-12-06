package com.example.visualisationmodele;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import outils.ModeleSingleton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chargerModele = findViewById(R.id.button_charger_modele);
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

    public void visualiserModele(View view) {
        if(ModeleSingleton.getInstance().getModeleInstance().getNom() != null) {
            Intent i = new Intent(this, VisualisationModele.class);
            startActivity(i);
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Impossible de visualiser le modèle.")
                    .setMessage("Veuillez charger un modèle avant de visualiser.")
                    .setNeutralButton("OK",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}