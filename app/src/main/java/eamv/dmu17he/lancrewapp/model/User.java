package eamv.dmu17he.lancrewapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "fullName")
    private String name;

    @ColumnInfo(name = "userName")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "phoneNumber")
    private int phoneNumber;

    @ColumnInfo(name = "initials")
    private String initials;

    @ColumnInfo(name = "nickName")
    private String nickName;

    @ColumnInfo(name = "isAdmin")
    private boolean isAdmin;

    //@ColumnInfo(name = "Crew")
    //private String crew;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getInitials() { return initials; }

    public void setInitials(String initials){
        this.initials = initials;
    }

    public String getNickName(){ return nickName; }

    public void setNickName (String nickname) {this.nickName = nickName;}

    public boolean getIsAdmin() { return isAdmin; }

    public void setIsAdmin(boolean isAdmin) {this.isAdmin = isAdmin; }

    //public String getCrew() {return crew;}

    //public void setCrew(String crew) {this.crew = crew;}


    public User(){}


    @Ignore
    public User(String userName, String password){
        this.username=userName;
        this.password=password;
    }

}

