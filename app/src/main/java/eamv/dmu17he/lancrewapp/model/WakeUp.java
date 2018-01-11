package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;

/**
 * Created by alexj on 1/11/2018.
 */

class WakeUp {
    private int pokeCounter;
    private int id;
    private LocalDateTime time;
    private String comment;

    public WakeUp(LocalDateTime time, String comment){
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
