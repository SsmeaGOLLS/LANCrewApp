package eamv.dmu17he.lancrewappprototype.sql;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import eamv.dmu17he.lancrewappprototype.model.User;



@Database(entities = {User.class}, version = 1)
public abstract class sqLiteDatabase extends RoomDatabase
{
    //singleton
    private static sqLiteDatabase dbInstance;
    //the Data access objects
    public abstract userDAO uDAO();

    public static sqLiteDatabase getDatabase(Context context)
    {
        if(dbInstance==null)
        {
            //allow mainThreadQueries should be removed later (a specified thread should be used instead of main on android devices)
            dbInstance = Room.databaseBuilder(context, sqLiteDatabase.class, "userDatabase").allowMainThreadQueries().build();
        }
        return dbInstance;
    }

    public static void destroyInstance()
    {
        dbInstance=null;
    }
}