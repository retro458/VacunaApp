package com.example.pinchaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.database.entities.Carnet;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CarnetAdapter extends RecyclerView.Adapter<CarnetAdapter.VH> {

    private List<Carnet> lista = new ArrayList<>();
    private final OnCarnetClick listener;

    public interface OnCarnetClick {
        void onClick(Carnet c);
    }

    public CarnetAdapter(OnCarnetClick listener) {
        this.listener = listener;
    }

    public void setLista(List<Carnet> nueva) {
        this.lista = nueva;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carnet, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Carnet c = lista.get(pos);
        h.tvClinica.setText(c.getClinica());
        h.tvNombre.setText(c.getNombrePropietario());
        h.tvFecha.setText("Registrado: " + c.getFechaRegistro());
        h.itemView.setOnClickListener(v -> listener.onClick(c));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvClinica, tvNombre, tvFecha;

        VH(View v) {
            super(v);
            tvClinica = v.findViewById(R.id.tvClinica);
            tvNombre  = v.findViewById(R.id.tvNombrePropietario);
            tvFecha   = v.findViewById(R.id.tvFecha);
        }
    }
}
