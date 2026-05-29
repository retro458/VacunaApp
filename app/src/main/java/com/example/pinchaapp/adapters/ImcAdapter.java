package com.example.pinchaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;

import org.jspecify.annotations.NonNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IMCEntity imc = lista.get(position);

        holder.txtIMC.setText("IMC: " + String.format("%.1f", imc.getImc()));
        holder.txtCategoria.setText(imc.getCategoria());

        try {
            String fechaRaw = imc.getFecha();
            if (fechaRaw != null && fechaRaw.contains("T")) {
                fechaRaw = fechaRaw.split("T")[0];
            }
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat salida  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.txtFecha.setText(salida.format(entrada.parse(fechaRaw)));
        } catch (Exception e) {
            holder.txtFecha.setText(imc.getFecha()); // si falla muestra como está
        }
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
