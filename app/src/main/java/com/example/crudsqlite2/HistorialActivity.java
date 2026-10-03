package com.example.crudsqlite2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerHistorial;

    private AppDatabase appDatabase;
    private RegistroSensorDao historialDao;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historial);

        recyclerHistorial = findViewById(R.id.recycler_historial);

        if (recyclerHistorial == null) {
            Toast.makeText(
                    this,
                    "No se encontró el RecyclerView",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        recyclerHistorial.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerHistorial.setHasFixedSize(true);

        appDatabase = AppDatabase.getInstance(
                getApplicationContext()
        );

        historialDao = appDatabase.registroSensorDao();

        executorService = Executors.newSingleThreadExecutor();

        cargarHistorial();
    }

    private void cargarHistorial() {

        executorService.execute(() -> {

            try {

                List<RegistroSensor> lista =
                        historialDao.obtenerHistorial();

                runOnUiThread(() -> {

                    HistorialAdapter adapter =
                            new HistorialAdapter(lista);

                    recyclerHistorial.setAdapter(adapter);

                });

            } catch (Exception e) {

                runOnUiThread(() -> {

                    Toast.makeText(
                            HistorialActivity.this,
                            "Error al cargar historial: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (executorService != null) {
            executorService.shutdown();
        }
    }
}