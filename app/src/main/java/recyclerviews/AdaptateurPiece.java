package recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.example.visualisationmodele.R;

import java.util.ArrayList;

import habitation.Piece;

public class AdaptateurPiece extends RecyclerView.Adapter<PieceViewHolder>{
    private final ArrayList<Piece> pieces;

    public AdaptateurPiece(ArrayList<Piece> pieces){
        this.pieces =pieces;
    }

    @Override
    public PieceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.piece_item, parent, false);
        return new PieceViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(PieceViewHolder pieceViewHolder, int position) {
        pieceViewHolder.getNom().setText(this.pieces.get(position).getNom());
    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }
}
