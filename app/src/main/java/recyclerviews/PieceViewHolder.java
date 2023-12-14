package recyclerviews;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualisationmodele.PieceActivity;
import com.example.visualisationmodele.R;

import java.util.Iterator;

import habitation.Piece;
import outils.ModeleSingleton;

public class PieceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView nom;
    private final Context context;

    public void setNom(TextView nom) {
        this.nom = nom;
    }
    public PieceViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        nom = itemView.findViewById(R.id.piece_nom);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(itemView.getContext())
                .setTitle(nom.getText().toString())
                .setMessage("Que voulez vous faire avec cette pièce ?")
                .setPositiveButton("Visiter la pièce", (dialog, which) -> visiterPiece())

                .setNegativeButton("Annuler", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public TextView getNom() {
        return nom;
    }
    private void visiterPiece(){
        nom.setInputType(InputType.TYPE_CLASS_TEXT );
        Iterator<Piece> iterator = ModeleSingleton.getInstance().getModeleInstance().getPieceArrayList().iterator();

        while (iterator.hasNext()) {
            Piece p = iterator.next();
            if (p.getNom().equals(nom.getText().toString())) {
                if(p.getMurEst().getNomBitmap()!= null &&p.getMurSud().getNomBitmap()!= null &&p.getMurNord().getNomBitmap()!= null &&p.getMurOuest().getNomBitmap()!= null ){
                    ModeleSingleton.getInstance().setPieceEnCours(p);
                    Intent i = new Intent(context, PieceActivity.class);
                    context.startActivity(i);
                }else {
                    new AlertDialog.Builder(context)
                            .setTitle("Piece invalide")
                            .setMessage("La pièce doit avoir 4 pièces pour la visiter.")
                            .setNeutralButton("OK",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        }

    }
}
