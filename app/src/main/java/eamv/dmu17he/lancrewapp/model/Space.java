package eamv.dmu17he.lancrewapp.model;

/**
 * Created by alexj on 1/11/2018.
 */

public class Space {
    int id;
    WakeUp wakeUp;
    String userName;
    int column;
    int row;

    public Space(int column, int row){
        this.column = column;
        this.row = row;
    }
}
