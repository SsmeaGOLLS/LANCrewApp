package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Created by alexj on 1/11/2018.
 */

public class WakeUp {
    private int pokeCounter;
    private String id;
    private String time;
    private String comment;

    public WakeUp(String time, String comment){
        this.setTime(time);
        this.setComment(comment);
        pokeCounter = 0;
    }

    public WakeUp(){
        pokeCounter = 0;
    }

    public int getPokeCounter() {
        return pokeCounter;
    }

    public void setPokeCounter(int pokeCounter) {
        this.pokeCounter = pokeCounter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
