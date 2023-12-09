package com.example.visualisationmodele;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import habitation.Modele;
import outils.ModeleSingleton;

public class ChargementModele extends AppCompatActivity {

    private ArrayList<Modele> modeles;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement_modele);
        modeles = new ArrayList<>();
        rechercheFichier();
    }
    public void rechercheFichier() {
        // Créez un intent pour ouvrir l'explorateur de fichiers
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        // Lancez l'activité de l'explorateur de fichiers
        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                if (uri != null) {
                    try {
                        String nomFichier = getNomFichier(uri);

                        File file = new File(getFilesDir(), nomFichier);

                        lireContenuFichier(uri, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getNomFichier(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            fileName = cursor.getString(index);
            cursor.close();
        }
        return fileName;
    }

    private void lireContenuFichier(Uri uri, File file) throws IOException {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder contenu = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contenu.append(line).append("\n");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                Modele modeleCharger = objectMapper.readValue(contenu.toString(), Modele.class);
                ModeleSingleton.getInstance().getModeleInstance().remplacementModele(modeleCharger);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }



}
