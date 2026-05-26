package com.example.pinchaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.dto.CentroDto;

import java.util.List;
import java.util.Locale;

public class CentroAdapter extends RecyclerView.Adapter<CentroAdapter.CentroViewHolder> {

    private final List<CentroDto> centros;
    private final OnCentroClickListener listener;

    // Interfaz para manejar el clic en un centro
    public interface OnCentroClickListener {
        void onCentroClick(CentroDto centro);
    }

    public CentroAdapter(List<CentroDto> centros, OnCentroClickListener listener) {
        this.centros = centros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CentroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_centro, parent, false);
        return new CentroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroViewHolder holder, int position) {
        CentroDto centro = centros.get(position);

        holder.txtNombre.setText(centro.getNombre());
        holder.txtDireccion.setText(centro.getDireccion() != null
                ? centro.getDireccion() : "Sin dirección");
        holder.txtHorario.setText(centro.getHorario() != null
                ? centro.getHorario() : "Horario no disponible");

        // Mostrar distancia solo si viene del endpoint de cercanía
        if (centro.getDistanciaKm() > 0) {
            holder.txtDistancia.setText(
                    String.format(Locale.getDefault(), "%.1f km", centro.getDistanciaKm()));
        } else {
            holder.txtDistancia.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCentroClick(centro);
        });
    }

    @Override
    public int getItemCount() {
        return centros.size();
    }

    static class CentroViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDireccion, txtHorario, txtDistancia;

        CentroViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre    = itemView.findViewById(R.id.txtNombreCentro);
            txtDireccion = itemView.findViewById(R.id.txtDireccionCentro);
            txtHorario   = itemView.findViewById(R.id.txtHorarioCentro);
            txtDistancia = itemView.findViewById(R.id.txtDistancia);
        }
    }
}