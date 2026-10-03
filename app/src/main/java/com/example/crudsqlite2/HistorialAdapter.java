package com.example.crudsqlite2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter
        extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private List<RegistroSensor> lista;

    public HistorialAdapter(List<RegistroSensor> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_historial,
                        parent,
                        false
                );

        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HistorialViewHolder holder,
            int position) {

        RegistroSensor registro = lista.get(position);

        holder.tvTipo.setText(
                "Sensor: " + registro.tipoSensor
        );

        holder.tvValor.setText(
                "Valor: " + registro.valorLectura
        );

        try {

            long tiempo =
                    Long.parseLong(registro.timestamp);

            SimpleDateFormat formato =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss",
                            Locale.getDefault()
                    );

            String fecha =
                    formato.format(new Date(tiempo));

            holder.tvFecha.setText(
                    "Fecha y hora: " + fecha
            );

        } catch (Exception e) {

            holder.tvFecha.setText(
                    "Fecha y hora: " + registro.timestamp
            );
        }
    }

    @Override
    public int getItemCount() {

        if (lista == null) {
            return 0;
        }

        return lista.size();
    }

    public static class HistorialViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTipo;
        TextView tvValor;
        TextView tvFecha;

        public HistorialViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvTipo =
                    itemView.findViewById(
                            R.id.tv_tipo_sensor
                    );

            tvValor =
                    itemView.findViewById(
                            R.id.tv_valor_sensor
                    );

            tvFecha =
                    itemView.findViewById(
                            R.id.tv_fecha_sensor
                    );
        }
    }
}