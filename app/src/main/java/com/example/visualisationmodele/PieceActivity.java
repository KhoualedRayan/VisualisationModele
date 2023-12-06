package com.example.visualisationmodele;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


import outils.ModeleSingleton;

public class PieceActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private ImageView imageView;
    private Bitmap bitmapNord;
    private Bitmap bitmapSud;
    private Bitmap bitmapEst;
    private Bitmap bitmapOuest;
    private String direction;
    private SurfaceHolder surfaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);
        init();
    }
    public void init(){
        surfaceView = findViewById(R.id.surfaceView);
        imageView = findViewById(R.id.imageView_Mur);
        surfaceHolder = surfaceView.getHolder();
        surfaceView.setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        bitmapNord= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getBytes().length);
        bitmapOuest= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getBytes().length);
        bitmapSud= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getBytes().length);
        bitmapEst= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getBytes().length);
        direction = "nord";
        imageView.setImageBitmap(bitmapNord);
    }

    public void nextClick(View view) {
        switch (direction){
            case "nord":
                direction = "est";
                imageView.setImageBitmap(bitmapEst);
                break;
            case "sud":
                direction = "ouest";
                imageView.setImageBitmap(bitmapOuest);
                break;
            case "ouest":
                direction = "nord";
                imageView.setImageBitmap(bitmapNord);
                break;
            case "est":
                direction = "sud";
                imageView.setImageBitmap(bitmapSud);
                break;
        }
    }

    public void precedentClick(View view) {
        switch (direction){
            case "nord":
                direction = "ouest";
                imageView.setImageBitmap(bitmapOuest);
                break;
            case "sud":
                direction = "est";
                imageView.setImageBitmap(bitmapEst);
                break;
            case "ouest":
                direction = "sud";
                imageView.setImageBitmap(bitmapSud);
                break;
            case "est":
                direction = "nord";
                imageView.setImageBitmap(bitmapNord);
                break;
        }
    }
}