package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by alexj on 1/11/2018.
 */

public class Controller {
    ArrayList<Hall> halls = new ArrayList<Hall>();


    // set comment , row, column, hallname,






    public int getNumberOfHalls(){
        return halls.size();
    }

    public int getNumberOfSpacesInHall(int hallIndex){
        return halls.get(hallIndex).getSizeOfSpaces();
    }

    public ArrayList<Integer> getColumnRowNumber(String hallName, String userName){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        Space tempSpace = getHallByName(hallName).getSpaceByUserName(userName);
        temp.add(tempSpace.getColumn());
        temp.add(tempSpace.getRow());
        return temp;
    }

    public String getHallName(int hallIndex) {
        return halls.get(hallIndex).getHallName();
    }

    public LocalDateTime getTimeByHallNameUserName(String hallName, String userName){
        return getHallByName(hallName).getSpaceByUserName(userName).getTime();
    }

    private Hall getHallByName(String hallName){
        for(Hall h: halls){
            if(h.getHallName() == hallName){
                return h;
            }
        }
        return null;
    }

    // hallName, time, row, column

    private void addEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        SleepingSpaceEntity h = new SleepingSpaceEntity(hallName, column, row, userName, time, comment, pokeCounter);
    }

    private void getEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        SleepingSpaceEntity h = new SleepingSpaceEntity(hallName, column, row, userName, time, comment, pokeCounter);
    }






























    private static final Controller ourInstance = new Controller();

    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
    }
}
