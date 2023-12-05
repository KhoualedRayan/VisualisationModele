package com.example.visualisationmodele;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import habitation.Modele;
import outils.ModeleSingleton;
import recyclerviews.AdaptateurModele;

public class ChargementModele extends AppCompatActivity {

    private ArrayList<Modele> modeles;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement_modele);
        modeles = new ArrayList<>();
        performFileSearch();
    }
    public void performFileSearch() {
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
                // L'URI sélectionné par l'utilisateur
                Uri uri = data.getData();

                // Utilisez l'URI pour lire le fichier
                if (uri != null) {
                    try {
                        // Obtenez le nom du fichier à partir de l'URI
                        String fileName = getFileName(uri);
                        Log.i("Selected File", "File Name: " + fileName);

                        // Créez un fichier dans le répertoire des fichiers de l'application
                        File file = new File(getFilesDir(), fileName);

                        // Lisez le contenu du fichier et stockez-le dans le fichier local
                        readFileContent(uri, file);
                        if (file.isFile() && fileName.toLowerCase().endsWith(".json")) {
                            Log.i("CHARGEMENTMODELE : ", "Fichier JSON trouvé : " + file.getAbsolutePath());
                            Modele m = new Modele();
                            m.setNom(fileName);
                            modeles.add(m);
                            ModeleSingleton.getInstance().getModeleInstance().remplacementModele(m);
                            Log.i("CHARGEMENTMODELE : ", ModeleSingleton.getInstance().getModeleInstance().toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getFileName(Uri uri) {
        // Obtenez le nom du fichier à partir de l'URI
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            fileName = cursor.getString(displayNameIndex);
            cursor.close();
        }
        return fileName;
    }

    private void readFileContent(Uri uri, File file) throws IOException {
        // Utilisez un BufferedReader pour lire le contenu du fichier
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Écrivez le contenu du fichier dans le fichier local
            // Vous pouvez également traiter le contenu JSON ici selon vos besoins
            // Par exemple, si le fichier contient du JSON, vous pouvez utiliser une bibliothèque JSON pour le parser.

            // Pour l'exemple, nous allons simplement lire et afficher chaque ligne du fichier
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // Affichez le contenu dans les logs
            Log.i("File Content", content.toString());
        }
    }


}
