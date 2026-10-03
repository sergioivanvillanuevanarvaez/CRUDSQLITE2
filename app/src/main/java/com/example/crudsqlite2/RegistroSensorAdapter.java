package com.example.crudsqlite2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegistroSensorAdapter
        extends RecyclerView.Adapter<RegistroSensorAdapter.ViewHolder> {

    private List<RegistroSensor> lista;

    public RegistroSensorAdapter(List<RegistroSensor> lista) {
        this.lista = lista;
    }

    public void actualizarLista(List<RegistroSensor> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_registro_sensor,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        RegistroSensor registro = lista.get(position);

        holder.txtTipoSensor.setText(
                "Sensor: " + registro.getTipoSensor()
        );

        holder.txtValor.setText(
                "Valor: " + registro.getValorLectura()
        );

        holder.txtFecha.setText(
                "Fecha: " + registro.getTimestamp()
        );

        if (registro.isEstadoAlerta()) {

            holder.txtAlerta.setText(
                    "Alerta: SI"
            );

        } else {

            holder.txtAlerta.setText(
                    "Alerta: NO"
            );
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTipoSensor;
        TextView txtValor;
        TextView txtFecha;
        TextView txtAlerta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTipoSensor =
                    itemView.findViewById(
                            R.id.txtTipoSensor
                    );

            txtValor =
                    itemView.findViewById(
                            R.id.txtValor
                    );

            txtFecha =
                    itemView.findViewById(
                            R.id.txtFecha
                    );

            txtAlerta =
                    itemView.findViewById(
                            R.id.txtAlerta
                    );
        }
    }
}