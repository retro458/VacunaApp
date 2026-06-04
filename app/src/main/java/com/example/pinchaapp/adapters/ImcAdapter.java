package com.example.pinchaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.dto.ImcDto;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImcAdapter extends RecyclerView.Adapter<ImcAdapter.ViewHolder> {

    List<ImcDto.ImcResponseDto> lista;

    public ImcAdapter(List<ImcDto.ImcResponseDto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imc, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImcDto.ImcResponseDto imc = lista.get(position);

        holder.txtIMC.setText(
                "IMC: " + String.format(Locale.getDefault(), "%.1f", imc.getResultado())
        );

        holder.txtCategoria.setText(imc.getClasificacion());

        if (imc.getFecha() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.txtFecha.setText(sdf.format(imc.getFecha()));
        } else {
            holder.txtFecha.setText("Sin fecha");
        }
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtIMC, txtCategoria, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIMC = itemView.findViewById(R.id.txtIMC);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
