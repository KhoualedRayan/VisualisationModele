package com.example.visualisationmodele;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import habitation.Piece;
import outils.ModeleSingleton;
import recyclerviews.AdaptateurPiece;

public class VisualisationModele extends AppCompatActivity {
    private ArrayList<Piece> pieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation_modele);
        TextView textView = findViewById(R.id.textView_nomModele);
        textView.setText(textView.getText()+ModeleSingleton.getInstance().getModeleInstance().getNom());
        this.pieces = new ArrayList<>();
        initPieces();
    }
    private void initPieces(){
        for (Piece p : ModeleSingleton.getInstance().getModeleInstance().getPieceArrayList()){
            pieces.add(p);
        }
        Log.i("EDITION MODELE NB PIECES", String.valueOf(ModeleSingleton.getInstance().getModeleInstance().getPieceArrayList().size()));
        AdaptateurPiece ad = new AdaptateurPiece(pieces);
        RecyclerView rc = findViewById(R.id.recylcerView_Pieces);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(ad);
    }
}