package com.example.crudsqlite2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {RegistroSensor.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RegistroSensorDao registroSensorDao();

    private static AppDatabase INSTANCE;

    public static final Migration MIGRATION_1_2 =
            new Migration(1, 2) {

                @Override
                public void migrate(
                        @NonNull SupportSQLiteDatabase database) {

                    database.execSQL(
                            "ALTER TABLE historial_sensores " +
                                    "ADD COLUMN estadoAlerta INTEGER NOT NULL DEFAULT 0"
                    );
                }
            };

    public static synchronized AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "escuela.db"
                    )
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }

        return INSTANCE;
    }

    public abstract HistorialDao historialDao();
}