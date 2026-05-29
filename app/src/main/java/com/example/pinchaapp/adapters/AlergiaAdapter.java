package com.example.pinchaapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.dto.AlergiasDto;

import java.util.List;

public class AlergiaAdapter extends RecyclerView.Adapter<AlergiaAdapter.ViewHolder> {

    private final Context context;
    private final List<AlergiasDto.AlergiaDto> lista;
    private final OnEliminarClick listener;

    public interface OnEliminarClick {
        void onEliminar(AlergiasDto.AlergiaDto alergia);
    }

    public AlergiaAdapter(Context context, List<AlergiasDto.AlergiaDto> lista, OnEliminarClick listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alergia, parent, false);
        Log.d("ADAPTER", "ViewHolder creado");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlergiasDto.AlergiaDto alergia = lista.get(position);
        holder.txtNombre.setText(alergia.getNombre());

        holder.btnEliminar.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            Log.d("CLICK_ELIMINAR", "pos=" + pos + " nombre=" + alergia.getNombre());
            if (pos != RecyclerView.NO_POSITION) {
                listener.onEliminar(lista.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        ImageView btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre   = itemView.findViewById(R.id.txtNombreAlergia);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}