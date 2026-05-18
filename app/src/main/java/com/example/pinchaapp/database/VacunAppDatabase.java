package com.example.pinchaapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.pinchaapp.database.dao.PerfilHumanoDao;
import com.example.pinchaapp.database.dao.PerfilMascotaDao;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.PerfilHumano;
import com.example.pinchaapp.database.entities.PerfilMascota;
import com.example.pinchaapp.database.entities.Usuario;
import com.example.pinchaapp.database.dao.VacunaDao;
import com.example.pinchaapp.database.entities.Vacuna;
import com.example.pinchaapp.database.entities.VacunaHistorial;
import com.example.pinchaapp.database.dao.IMCDao;
import com.example.pinchaapp.database.entities.IMCEntity;

@Database(
        entities = {
                Usuario.class,
                PerfilHumano.class,
                PerfilMascota.class,
                VacunaHistorial.class,
                IMCEntity.class
        },
        version = 5,
        exportSchema = false
)
public abstract class VacunAppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract PerfilHumanoDao perfilHumanoDao();
    public abstract PerfilMascotaDao perfilMascotaDao();
    public abstract VacunaDao vacunaDao();
    public abstract IMCDao imcDao();

    private static VacunAppDatabase instancia;

    public static VacunAppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            VacunAppDatabase.class,
                            "vacunapp_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instancia;
    }
}