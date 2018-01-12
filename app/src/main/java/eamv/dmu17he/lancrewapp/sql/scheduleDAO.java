package eamv.dmu17he.lancrewapp.sql;

/**
 * Created by jonas on 11-01-2018.
 */

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import eamv.dmu17he.lancrewapp.model.Schedule;
import eamv.dmu17he.lancrewapp.model.User;

@Dao
public interface scheduleDAO {

    @Query("SELECT * FROM schedule")
    List<Schedule> getAll();

    @Insert
    void insertAll(List<Schedule> schedules);

    @Insert
    void insertSchedule(Schedule theSchedule);

    @Update
    void update(Schedule theSchedule);

    @Delete
    void delete(Schedule theSchedule);

    @Query("DELETE FROM Schedule")
    public void deleteTable();

    // @Query("SELECT * FROM Schedule WHERE name LIKE :uname")
   // public User findUserFromName(String uname);

}
