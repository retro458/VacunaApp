package com.example.pinchaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.dto.CampaniaDto;

import java.util.List;

public class CampaniaAdapter extends RecyclerView.Adapter<CampaniaAdapter.CampaniaViewHolder> {

    private final List<CampaniaDto> campanias;
    private final OnCampaniaClickListener listener;

    // Interfaz para manejar el clic en una campaña
    public interface OnCampaniaClickListener {
        void onCampaniaClick(CampaniaDto campania);
    }

    public CampaniaAdapter(List<CampaniaDto> campanias, OnCampaniaClickListener listener) {
        this.campanias = campanias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CampaniaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campania, parent, false);
        return new CampaniaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampaniaViewHolder holder, int position) {
        CampaniaDto campania = campanias.get(position);

        // 1. El nombre se mantiene igual
        holder.txtNombre.setText(campania.getNombre());

        // 2. La API de C# devuelve IdVacuna (no el nombre directamente).
        // Por ahora ponemos un texto genérico
        holder.txtVacuna.setText("Vacuna programada");

        // 3. El centro ahora viene en la propiedad "lugar"
        holder.txtCentro.setText(campania.getLugar() != null
                ? campania.getLugar() : "Lugar no especificado");

        // 4. La fecha ahora es un solo campo que viene en formato ISO (ej: "2026-10-15T00:00:00")
        String fechaLimpia = "Fecha no definida";
        if (campania.getFecha() != null && !campania.getFecha().isEmpty()) {
            // Cortamos la letra 'T' para mostrar solo "YYYY-MM-DD"
            fechaLimpia = campania.getFecha().split("T")[0];
        }
        holder.txtFechas.setText("Fecha: " + fechaLimpia);

        // Evento clic para abrir el mapa
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCampaniaClick(campania);
        });
    }

    @Override
    public int getItemCount() {
        return campanias.size();
    }

    static class CampaniaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtVacuna, txtCentro, txtFechas;

        CampaniaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreCampania);
            txtVacuna = itemView.findViewById(R.id.txtVacunaCampania);
            txtCentro = itemView.findViewById(R.id.txtCentroCampania);
            txtFechas = itemView.findViewById(R.id.txtFechasCampania);
        }
    }
}