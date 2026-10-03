package com.example.crudsqlite2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistorialDao {

    @Insert
    void insertar(RegistroSensor registro);

    @Insert
    void insertarLista(List<RegistroSensor> registros);

    @Query("SELECT * FROM historial_sensores ORDER BY timestamp DESC")
    List<RegistroSensor> obtenerHistorial();

    @Query("DELETE FROM historial_sensores")
    void eliminarTodo();
}