package com.example.pinchaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;

import org.jspecify.annotations.NonNull;
import java.util.List;
import com.example.pinchaapp.database.entities.Alergia;

public class AlergiaAdapter
        extends RecyclerView.Adapter<AlergiaAdapter.ViewHolder> {

    Context context;
    List<Alergia> lista;
    OnEliminarClick listener;

    public interface OnEliminarClick {
        void onEliminar(Alergia alergia);
    }

    public AlergiaAdapter(
            Context context,
            List<Alergia> lista,
            OnEliminarClick listener
    ) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_alergia, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Alergia alergia = lista.get(position);

        holder.txtNombre.setText(alergia.getNombre());

        holder.btnEliminar.setOnClickListener(v ->
                listener.onEliminar(alergia)
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNombre;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre =
                    itemView.findViewById(R.id.txtNombreAlergia);

            btnEliminar =
                    itemView.findViewById(R.id.btnEliminar);
        }
    }
}
