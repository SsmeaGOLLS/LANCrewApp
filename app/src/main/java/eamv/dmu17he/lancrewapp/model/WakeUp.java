package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Created by alexj on 1/11/2018.
 */

public class WakeUp {
    private int pokeCounter;
    private int id;
    private Calendar time;
    private String comment;

    public WakeUp(Calendar time, String comment){
        this.setTime(time);
        this.setComment(comment);
    }


    public int getPokeCounter() {
        return pokeCounter;
    }

    public void setPokeCounter(int pokeCounter) {
        this.pokeCounter = pokeCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
