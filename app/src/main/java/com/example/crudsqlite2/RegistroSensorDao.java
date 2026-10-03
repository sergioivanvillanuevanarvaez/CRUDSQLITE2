package com.example.crudsqlite2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RegistroSensorDao {

    @Insert
    void insertar(RegistroSensor registro);

    @Insert
    void insertarLista(List<RegistroSensor> registros);

    @Query("SELECT * FROM historial_sensores ORDER BY id DESC")
    List<RegistroSensor> obtenerHistorial();

    @Query("DELETE FROM historial_sensores")
    void eliminarTodo();

    @Query("SELECT COUNT(*) FROM historial_sensores")
    int contarRegistros();
}