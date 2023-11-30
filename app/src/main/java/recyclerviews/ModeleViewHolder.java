package recyclerviews;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.visualisationmodele.R;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import habitation.Modele;
import outils.ModeleSingleton;

public class ModeleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nom;

    public ModeleViewHolder(View itemView) {
        super(itemView);
        nom = itemView.findViewById(R.id.modele_nom);
        itemView.setOnClickListener(this);
    }


    public TextView getNom() {
        return nom;
    }

    public void setNom(TextView nom) {
        this.nom = nom;
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(itemView.getContext())
                .setTitle(nom.getText().toString())
                .setMessage("Que voulez vous faire avec ce modèle ?")
                .setNeutralButton("Charger le modèle", (dialog, which) -> chargerModele())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void chargerModele(){
        String chemin = "/data/user/0/com.example.creationmodele/files/"+nom.getText().toString();
        try {
            // Créer un objet ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


            // Lire le fichier JSON et le convertir en objet de la classe MaClasse
            Modele modeleCharger = objectMapper.readValue(new File(chemin), Modele.class);
            ModeleSingleton.getInstance().getModeleInstance().remplacementModele(modeleCharger);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
