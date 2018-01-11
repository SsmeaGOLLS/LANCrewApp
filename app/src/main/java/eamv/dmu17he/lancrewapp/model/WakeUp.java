package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;

/**
 * Created by alexj on 1/11/2018.
 */

class WakeUp {
    int pokeCounter;
    int id;
    LocalDateTime time;
    String comment;

    public WakeUp(LocalDateTime time, String comment){
        this.time = time;
        this.comment = comment;
    }
}
