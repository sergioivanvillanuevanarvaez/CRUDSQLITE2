package com.example.crudsqlite2;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // ============================
    // FIREBASE
    // ============================

    private DatabaseReference databaseReference;

    // ============================
    // BRILLO LED
    // ============================

    private SeekBar seekBarBrillo;
    private TextView tvBrillo;

    // ============================
    // MOTOR
    // ============================

    private ImageButton btnIniciarMotor;
    private ImageButton btnApagarMotor;
    private TextView tvEstadoMotor;

    // ============================
    // LED
    // ============================

    private Switch switchLed;
    private TextView tvEstadoLed;

    // ============================
    // SENSORES
    // ============================

    private TextView tvLdr;
    private TextView tvIr;

    // ============================
    // HISTORIAL
    // ============================

    private Button btnVerHistorial;

    // ============================
    // ROOM
    // ============================

    private AppDatabase appDatabase;
    private RegistroSensorDao historialDao;

    private ExecutorService executorService;

    // ============================
    // CONTROL LED
    // ============================

    private boolean actualizandoLed = false;

    // ============================
    // VARIABLES
    // ============================

    private int ultimoLdr = 0;
    private int ultimoInfrarrojo = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // ============================
        // FIREBASE
        // ============================

        try {

            FirebaseApp.initializeApp(this);

        } catch (Exception e) {

            e.printStackTrace();

        }

        // ============================
        // LAYOUT
        // ============================

        setContentView(R.layout.activity_main);

        // ============================
        // ROOM
        // ============================

        appDatabase =
                AppDatabase.getInstance(
                        getApplicationContext()
                );

        historialDao =
                appDatabase.registroSensorDao();

        executorService =
                Executors.newSingleThreadExecutor();

        // ============================
        // REFERENCIAS XML
        // ============================

        btnIniciarMotor =
                findViewById(R.id.btn_iniciar_motor);

        btnApagarMotor =
                findViewById(R.id.btn_apagar_motor);

        tvEstadoMotor =
                findViewById(R.id.tv_estado_motor);

        switchLed =
                findViewById(R.id.switch_led);

        tvEstadoLed =
                findViewById(R.id.tv_estado_led);

        tvLdr =
                findViewById(R.id.tv_ldr);

        tvIr =
                findViewById(R.id.tv_ir);

        seekBarBrillo =
                findViewById(R.id.seekBarBrillo);

        tvBrillo =
                findViewById(R.id.tv_brillo);

        // ============================
        // BOTÓN VER HISTORIAL
        // ============================

        btnVerHistorial =
                findViewById(R.id.btn_historial);

        btnVerHistorial.setOnClickListener(
                v -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Abriendo historial...",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent =
                            new Intent(
                                    MainActivity.this,
                                    HistorialActivity.class
                            );

                    startActivity(intent);
                }
        );

        // ============================
        // FIREBASE
        // ============================

        try {

            databaseReference =
                    FirebaseDatabase
                            .getInstance(
                                    "https://esp32-75224-default-rtdb.firebaseio.com/"
                            )
                            .getReference("tarjeta");

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error al conectar Firebase",
                    Toast.LENGTH_LONG
            ).show();

            e.printStackTrace();

            return;
        }

        // ============================
        // ESTADO INICIAL
        // ============================

        btnIniciarMotor.setVisibility(
                View.VISIBLE
        );

        btnApagarMotor.setVisibility(
                View.GONE
        );

        tvEstadoMotor.setText(
                "Motor detenido"
        );

        switchLed.setChecked(false);

        tvEstadoLed.setText(
                "LED apagado"
        );

        tvLdr.setText(
                "0%"
        );

        tvIr.setText(
                "Sin detección"
        );

        tvBrillo.setText(
                "Brillo: 0%"
        );

        // =====================================================
        // SEEKBAR BRILLO
        // =====================================================

        seekBarBrillo.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {

                        tvBrillo.setText(
                                "Brillo: "
                                        + progress
                                        + "%"
                        );

                        if (fromUser) {

                            databaseReference
                                    .child("dispositivos")
                                    .child("led")
                                    .child("brillo")
                                    .setValue(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar) {
                    }
                }
        );

        // =====================================================
        // BOTÓN ENCENDER MOTOR
        // =====================================================

        btnIniciarMotor.setOnClickListener(
                v -> {

                    databaseReference
                            .child("dispositivos")
                            .child("motor")
                            .child("estado")
                            .setValue("on")
                            .addOnSuccessListener(
                                    unused -> {

                                        tvEstadoMotor.setText(
                                                "Motor encendido"
                                        );

                                        btnIniciarMotor
                                                .setVisibility(
                                                        View.GONE
                                                );

                                        btnApagarMotor
                                                .setVisibility(
                                                        View.VISIBLE
                                                );

                                        Toast.makeText(
                                                MainActivity.this,
                                                "Motor encendido",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }
                            )
                            .addOnFailureListener(
                                    e -> {

                                        Toast.makeText(
                                                MainActivity.this,
                                                "Error al encender motor",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }
                            );
                }
        );

        // =====================================================
        // BOTÓN APAGAR MOTOR
        // =====================================================

        btnApagarMotor.setOnClickListener(
                v -> {

                    databaseReference
                            .child("dispositivos")
                            .child("motor")
                            .child("estado")
                            .setValue("off")
                            .addOnSuccessListener(
                                    unused -> {

                                        tvEstadoMotor.setText(
                                                "Motor detenido"
                                        );

                                        btnApagarMotor
                                                .setVisibility(
                                                        View.GONE
                                                );

                                        btnIniciarMotor
                                                .setVisibility(
                                                        View.VISIBLE
                                                );

                                        Toast.makeText(
                                                MainActivity.this,
                                                "Motor apagado",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }
                            )
                            .addOnFailureListener(
                                    e -> {

                                        Toast.makeText(
                                                MainActivity.this,
                                                "Error al apagar motor",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }
                            );
                }
        );

        // =====================================================
        // SWITCH LED
        // =====================================================

        switchLed.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (actualizandoLed) {

                        return;
                    }

                    if (isChecked) {

                        tvEstadoLed.setText(
                                "LED encendido"
                        );

                        databaseReference
                                .child("dispositivos")
                                .child("led")
                                .child("estado")
                                .setValue("on");

                    } else {

                        tvEstadoLed.setText(
                                "LED apagado"
                        );

                        databaseReference
                                .child("dispositivos")
                                .child("led")
                                .child("estado")
                                .setValue("off");
                    }
                }
        );

        // =====================================================
        // ESCUCHAR ESTADO LED
        // =====================================================

        databaseReference
                .child("dispositivos")
                .child("led")
                .child("estado")
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    DataSnapshot snapshot) {

                                if (!snapshot.exists()) {

                                    return;
                                }

                                String estado =
                                        snapshot.getValue(
                                                String.class
                                        );

                                if (estado == null) {

                                    return;
                                }

                                actualizandoLed = true;

                                if (estado.equalsIgnoreCase("on")) {

                                    switchLed.setChecked(true);

                                    tvEstadoLed.setText(
                                            "LED encendido"
                                    );

                                } else {

                                    switchLed.setChecked(false);

                                    tvEstadoLed.setText(
                                            "LED apagado"
                                    );
                                }

                                actualizandoLed = false;
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error) {

                                Toast.makeText(
                                        MainActivity.this,
                                        "Error leyendo LED",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );

        // =====================================================
        // ESCUCHAR BRILLO
        // =====================================================

        databaseReference
                .child("dispositivos")
                .child("led")
                .child("brillo")
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    DataSnapshot snapshot) {

                                if (!snapshot.exists()) {

                                    return;
                                }

                                Integer brillo =
                                        snapshot.getValue(
                                                Integer.class
                                        );

                                if (brillo == null) {

                                    return;
                                }

                                seekBarBrillo.setProgress(
                                        brillo
                                );

                                tvBrillo.setText(
                                        "Brillo: "
                                                + brillo
                                                + "%"
                                );
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error) {

                            }
                        }
                );

        // =====================================================
        // ESCUCHAR MOTOR
        // =====================================================

        databaseReference
                .child("dispositivos")
                .child("motor")
                .child("estado")
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    DataSnapshot snapshot) {

                                if (!snapshot.exists()) {

                                    return;
                                }

                                String estado =
                                        snapshot.getValue(
                                                String.class
                                        );

                                if (estado == null) {

                                    return;
                                }

                                if (estado.equalsIgnoreCase("on")) {

                                    tvEstadoMotor.setText(
                                            "Motor encendido"
                                    );

                                    btnIniciarMotor
                                            .setVisibility(
                                                    View.GONE
                                            );

                                    btnApagarMotor
                                            .setVisibility(
                                                    View.VISIBLE
                                            );

                                } else {

                                    tvEstadoMotor.setText(
                                            "Motor detenido"
                                    );

                                    btnIniciarMotor
                                            .setVisibility(
                                                    View.VISIBLE
                                            );

                                    btnApagarMotor
                                            .setVisibility(
                                                    View.GONE
                                            );
                                }
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error) {

                            }
                        }
                );

        // =====================================================
        // ESCUCHAR LDR
        // =====================================================

        databaseReference
                .child("sensores")
                .child("ldr")
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    DataSnapshot snapshot) {

                                if (!snapshot.exists()) {

                                    return;
                                }

                                Object valor =
                                        snapshot.getValue();

                                if (valor == null) {

                                    return;
                                }

                                try {

                                    ultimoLdr =
                                            Integer.parseInt(
                                                    String.valueOf(
                                                            valor
                                                    )
                                            );

                                } catch (Exception e) {

                                    ultimoLdr = 0;
                                }

                                tvLdr.setText(
                                        ultimoLdr + "%"
                                );

                                // ============================
                                // GUARDAR EN ROOM
                                // ============================

                                guardarLectura(
                                        "LDR",
                                        String.valueOf(
                                                ultimoLdr
                                        )
                                );
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error) {

                                tvLdr.setText(
                                        "Error"
                                );
                            }
                        }
                );

        // =====================================================
        // ESCUCHAR INFRARROJO
        // =====================================================

        databaseReference
                .child("sensores")
                .child("infrarrojo")
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    DataSnapshot snapshot) {

                                if (!snapshot.exists()) {

                                    return;
                                }

                                Integer valor =
                                        snapshot.getValue(
                                                Integer.class
                                        );

                                if (valor == null) {

                                    return;
                                }

                                ultimoInfrarrojo =
                                        valor;

                                if (valor == 1) {

                                    tvIr.setText(
                                            "Detectado"
                                    );

                                } else {

                                    tvIr.setText(
                                            "Sin detección"
                                    );
                                }

                                // ============================
                                // GUARDAR EN ROOM
                                // ============================

                                guardarLectura(
                                        "Infrarrojo",
                                        String.valueOf(
                                                valor
                                        )
                                );
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error) {

                                tvIr.setText(
                                        "Error"
                                );
                            }
                        }
                );
    }

    // =====================================================
    // GUARDAR LECTURA EN ROOM
    // =====================================================

    private void guardarLectura(
            String tipoSensor,
            String valorLectura) {

        if (historialDao == null) {

            return;
        }

        if (executorService == null) {

            return;
        }

        executorService.execute(
                () -> {

                    RegistroSensor registro =
                            new RegistroSensor();

                    registro.tipoSensor =
                            tipoSensor;

                    registro.valorLectura =
                            valorLectura;

                    registro.timestamp =
                            String.valueOf(String.valueOf(System.currentTimeMillis()));

                    historialDao.insertar(
                            registro
                    );
                }
        );
    }

    // =====================================================
    // CERRAR ROOM
    // =====================================================

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (executorService != null) {

            executorService.shutdown();
        }
    }
}