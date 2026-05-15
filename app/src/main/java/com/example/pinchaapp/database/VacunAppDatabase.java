package com.example.pinchaapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pinchaapp.database.dao.PerfilHumanoDao;
import com.example.pinchaapp.database.dao.PerfilMascotaDao;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.PerfilHumano;
import com.example.pinchaapp.database.entities.PerfilMascota;
import com.example.pinchaapp.database.entities.Usuario;

@Database(
        entities = {
                Usuario.class,
                PerfilHumano.class,
                PerfilMascota.class
        },
        version = 2,
        exportSchema = false
)
public abstract class VacunAppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract PerfilHumanoDao perfilHumanoDao();
    public abstract PerfilMascotaDao perfilMascotaDao();

    // para que solo exista una instancia de la BD
    private static VacunAppDatabase instancia;

    public static VacunAppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    VacunAppDatabase.class,
                    "vacunapp_db"
            ).fallbackToDestructiveMigration().build();
        }
        return instancia;
    }
}