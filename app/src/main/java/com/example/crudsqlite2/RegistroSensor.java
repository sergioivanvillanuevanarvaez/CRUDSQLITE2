package com.example.crudsqlite2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial_sensores")
public class RegistroSensor {

    @PrimaryKey(autoGenerate = true)
    private int id;

    String tipoSensor;
    String valorLectura;
    String timestamp;
    private boolean estadoAlerta;

    public RegistroSensor() {

        this.tipoSensor = tipoSensor;
        this.valorLectura = valorLectura;
        this.timestamp = timestamp;
        this.estadoAlerta = estadoAlerta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public String getValorLectura() {
        return valorLectura;
    }

    public void setValorLectura(String valorLectura) {
        this.valorLectura = valorLectura;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEstadoAlerta() {
        return estadoAlerta;
    }

    public void setEstadoAlerta(boolean estadoAlerta) {
        this.estadoAlerta = estadoAlerta;
    }
}