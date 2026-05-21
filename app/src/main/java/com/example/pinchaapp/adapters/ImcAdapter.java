package com.example.pinchaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;

import org.jspecify.annotations.NonNull;

import java.util.List;
import com.example.pinchaapp.database.entities.IMCEntity;

public class ImcAdapter
        extends RecyclerView.Adapter<ImcAdapter.ViewHolder> {

    List<IMCEntity> lista;

    public ImcAdapter(List<IMCEntity> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imc, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        IMCEntity imc = lista.get(position);

        holder.txtIMC.setText(
                "IMC: " + String.format("%.1f", imc.getImc())
        );

        holder.txtCategoria.setText(imc.getCategoria());

        holder.txtFecha.setText(imc.getFecha());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtIMC, txtCategoria, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIMC =
                    itemView.findViewById(R.id.txtIMC);

            txtCategoria =
                    itemView.findViewById(R.id.txtCategoria);

            txtFecha =
                    itemView.findViewById(R.id.txtFecha);
        }
    }
}
