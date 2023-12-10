package com.example.visualisationmodele;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;

import habitation.Ouverture;
import habitation.Piece;
import outils.ModeleSingleton;

public class PieceActivity extends AppCompatActivity implements SensorEventListener {
    private SurfaceView surfaceView;
    private ImageView imageView;
    private Bitmap bitmapNord;
    private Bitmap bitmapSud;
    private Bitmap bitmapEst;
    private Bitmap bitmapOuest;
    private Canvas canvas;
    private String direction;
    private SurfaceHolder surfaceHolder;
    private ArrayList<Rect> rects;
    private Paint paint;
    private Switch capteur;
    private SensorManager sensorManager;
    private Sensor accelerometre;
    private Sensor magnetometre;
    private boolean isSensorEnabled = false;
    private float[] matriceR;
    private float[] accel;
    private float[] magneto;
    private float[] orientation;
    private ImageButton next;
    private ImageButton precedent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);
        init();
        initCapteurs();
    }
    public void init(){
        surfaceView = findViewById(R.id.surfaceView);
        imageView = findViewById(R.id.imageView_Mur);
        rects = new ArrayList<>();
        next = findViewById(R.id.imageButton_next);
        precedent = findViewById(R.id.imageButton_previous);
        surfaceHolder = surfaceView.getHolder();
        surfaceView.setZOrderOnTop(true);
        canvas = new Canvas();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(6);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        bitmapNord= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getBytes().length);
        bitmapOuest= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getBytes().length);
        bitmapSud= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getBytes().length);
        bitmapEst= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getBytes().length);
        direction = "nord";
        imageView.setImageBitmap(bitmapNord);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX(0);
                int y = (int) motionEvent.getY(0);
                Ouverture ouvertureCliquee = getOuvertureClic(x, y);
                Log.i("Piece","X: "+x +" Y : "+y);
                Log.i("Piece","Click");
                if (ouvertureCliquee != null) {
                    Log.i("Piece","ouverture pas null");
                    Piece p = trouverPieceParNom(ouvertureCliquee.getPieceArrivee());
                    verifChangementPiece(p);
                }
                return false;
            }
        });
    }
    public void initCapteurs(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometre = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        capteur = findViewById(R.id.switch_boussole);
        matriceR = new float[9];
        accel = new float[3];
        magneto = new float[3];
        orientation = new float[3];
    }
    public void verifChangementPiece(Piece p) {
        if (p.getMurEst().getNomBitmap() != null && p.getMurSud().getNomBitmap() != null && p.getMurNord().getNomBitmap() != null && p.getMurOuest().getNomBitmap() != null) {
            ModeleSingleton.getInstance().setPieceEnCours(p);
            initNewPiece();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Piece invalide : "+ p.getNom())
                    .setMessage("La pièce doit avoir 4 pièces pour la visiter.")
                    .setNeutralButton("OK",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    public Piece trouverPieceParNom(String nom) {
        for (Piece piece : ModeleSingleton.getInstance().getModeleInstance().getPieceArrayList()) {
            if (piece.getNom().equals(nom)) {
                return piece;
            }
        }
        return null; // Pièce non trouvée
    }
    private Ouverture getOuvertureClic(float x, float y) {
        switch (direction){
            case "nord":
                for (Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getOuvertures()) {
                    if (ouverture.getRect().contains((int) x, (int) y)) {
                        Log.i("Piece",ouverture.toString());
                        return ouverture;
                    }
                }
                break;
            case "est":
                for (Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getOuvertures()) {
                    if (ouverture.getRect().contains((int) x, (int) y)) {
                        Log.i("Piece",ouverture.toString());
                        return ouverture;
                    }
                }
                break;
            case"ouest":
                for (Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getOuvertures()) {
                    if (ouverture.getRect().contains((int) x, (int) y)) {
                        Log.i("Piece",ouverture.toString());
                        return ouverture;
                    }
                }
                break;
            case "sud":
                for (Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getOuvertures()) {
                    if (ouverture.getRect().contains((int) x, (int) y)) {
                        Log.i("Piece",ouverture.toString());
                        return ouverture;
                    }
                }
                break;
        }

        return null;
    }
    public void initNewPiece(){
        bitmapNord= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getBytes().length);
        bitmapOuest= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getBytes().length);
        bitmapSud= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getBytes().length);
        bitmapEst= BitmapFactory.decodeByteArray(ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getBytes(), 0, ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getBytes().length);
        switch (direction){
            case "nord":
                imageView.setImageBitmap(bitmapNord);
                break;
            case "est":
                imageView.setImageBitmap(bitmapEst);
                break;
            case "ouest":
                imageView.setImageBitmap(bitmapOuest);
                break;
            case "sud":
                imageView.setImageBitmap(bitmapSud);
                break;
        }
        affichageRect();
    }
    public void affichageRect(){
        rects.clear();
        enleverAnciensEtiquettes();
        enleverAnciensEtiquettes();
        switch (direction){
            case "nord":
                for(Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurNord().getOuvertures()){
                    rects.add(ouverture.getRect());
                    etiquette(ouverture.getRect(),ouverture.getPieceArrivee());
                }
                break;
            case "sud":
                for(Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurSud().getOuvertures()){
                    rects.add(ouverture.getRect());
                    etiquette(ouverture.getRect(),ouverture.getPieceArrivee());
                }
                break;
            case "ouest":
                for(Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurOuest().getOuvertures()){
                    rects.add(ouverture.getRect());
                    etiquette(ouverture.getRect(),ouverture.getPieceArrivee());
                }
                break;
            case "est":
                for(Ouverture ouverture : ModeleSingleton.getInstance().getPieceEnCours().getMurEst().getOuvertures()){
                    rects.add(ouverture.getRect());
                    etiquette(ouverture.getRect(),ouverture.getPieceArrivee());
                }
                break;
        }
        dessinRectangle();
    }
    private void enleverAnciensEtiquettes() {
        FrameLayout layout = (FrameLayout) surfaceView.getParent();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof RelativeLayout) {
                layout.removeView(child);
            }
        }
    }
    public void etiquette(Rect r,String s){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        int backgroundColor = Color.argb(127, 0, 255, 0); // 50% d'opacité (255 * 0.5)
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                r.width(),
                r.height()
        );
        relativeLayoutParams.leftMargin = r.left;
        relativeLayoutParams.topMargin = r.top;

        ((FrameLayout) surfaceView.getParent()).addView(relativeLayout, relativeLayoutParams);

        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setAllCaps(true);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);

        relativeLayout.setBackgroundColor(backgroundColor);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        relativeLayout.addView(textView, params);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            affichageRect();
        }
    }
    public void dessinRectangle() {
        canvas = surfaceView.getHolder().lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // Efface le contenu précédent
                for (Rect r : rects)
                    canvas.drawRect(r, paint);
            } finally {
                surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    public void nextClick(View view) {
        switch (direction){
            case "nord":
                direction = "est";
                imageView.setImageBitmap(bitmapEst);
                affichageRect();
                break;
            case "sud":
                direction = "ouest";
                imageView.setImageBitmap(bitmapOuest);
                affichageRect();
                break;
            case "ouest":
                direction = "nord";
                imageView.setImageBitmap(bitmapNord);
                affichageRect();
                break;
            case "est":
                direction = "sud";
                imageView.setImageBitmap(bitmapSud);
                affichageRect();
                break;
        }
    }

    public void precedentClick(View view) {
        switch (direction){
            case "nord":
                direction = "ouest";
                imageView.setImageBitmap(bitmapOuest);
                affichageRect();
                break;
            case "sud":
                direction = "est";
                imageView.setImageBitmap(bitmapEst);
                affichageRect();
                break;
            case "ouest":
                direction = "sud";
                imageView.setImageBitmap(bitmapSud);
                affichageRect();
                break;
            case "est":
                direction = "nord";
                imageView.setImageBitmap(bitmapNord);
                affichageRect();
                break;
        }
    }

    public void isTelMoving(float accelx, float accely, float accelz){
        double omegaMagnitude = Math.sqrt(accelx * accelx + accely * accely +accelz * accelz );
        double gravityMagnitude = SensorManager.STANDARD_GRAVITY;
        double tolerance = 0.2;
        if(Math.abs(omegaMagnitude - gravityMagnitude) < tolerance){
            Log.i("Capteur : ","IMMOBILE");
        }else {
            Log.i("Capteur : ","MOBILE");
        }
    }
    public void switchOnClick(View view){
        if(isSensorEnabled){
            sensorManager.unregisterListener(this);
            capteur.setText("Capteur Désactivé");
            isSensorEnabled = false;
            next.setVisibility(View.VISIBLE);
            precedent.setVisibility(View.VISIBLE);

        }else{
            sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometre, SensorManager.SENSOR_DELAY_NORMAL);
            capteur.setText("Capteur Activé");
            isSensorEnabled = true;
            next.setVisibility(View.INVISIBLE);
            precedent.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorEnabled) {
            sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometre, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorEnabled) {
            sensorManager.unregisterListener(this);
        }
    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == accelerometre) {
            Log.i("Capteur", event.sensor.toString());
            float accelx = event.values[0];
            float accely = event.values[1];
            float accelz = event.values[2];
            accel = event.values;
            isTelMoving(accelx, accely, accelz);
        }
        if (event.sensor == magnetometre) {
            magneto = event.values;
            SensorManager.getRotationMatrix(matriceR,null,accel,magneto);
            SensorManager.getOrientation(matriceR,orientation);
            // Convertir l'angle d'azimut de radians à degrés
            float azimuthDeg = (float) Math.toDegrees(orientation[0]);

            if (azimuthDeg >= -45 && azimuthDeg < 45) {
                direction = "nord";
                imageView.setImageBitmap(bitmapNord);
                Log.i("Direction", "NORD");
            } else if (azimuthDeg >= 45 && azimuthDeg < 135) {
                direction = "est";
                imageView.setImageBitmap(bitmapEst);
                Log.i("Direction", "EST");
            } else if (azimuthDeg >= 135 || azimuthDeg < -135) {
                direction = "sud";
                imageView.setImageBitmap(bitmapSud);
                Log.i("Direction", "SUD");
            } else if (azimuthDeg >= -135 && azimuthDeg < -45) {
                direction = "ouest";
                imageView.setImageBitmap(bitmapOuest);
                Log.i("Direction", "OUEST");
            }
            affichageRect();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}