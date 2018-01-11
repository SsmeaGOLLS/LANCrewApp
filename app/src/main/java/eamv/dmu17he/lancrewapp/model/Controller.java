package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by alexj on 1/11/2018.
 */

public class Controller {
    ArrayList<Hall> halls = new ArrayList<Hall>();

    public int getNumberOfHalls(){
        return halls.size();
    }

    public int getSpaces(int hallIndex){
        return halls.get(hallIndex).getSizeOfSpaces();
    }

    public String getHallName(int hallIndex) {
        return halls.get(hallIndex).getName();
    }

    public LocalDateTime getTimeByWakeUpId(int hallIndex, int column, int row){
        return halls.get(hallIndex).getSpaceByColumnRow(column, row);
    }




    private void addEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        Entity h = new Entity(hallName, column, row, userName, time, comment, pokeCounter);
    }

    private void getEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        Entity h = new Entity(hallName, column, row, userName, time, comment, pokeCounter);
    }






























    private static final Controller ourInstance = new Controller();

    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
    }
}
