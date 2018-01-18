package eamv.dmu17he.lancrewapp.model;

public class User {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    private String name;

    @com.google.gson.annotations.SerializedName("username")
    private String username;

    private int phoneNumber;

    private String password;

    private String nickName;

    private boolean isAdmin;

    private String crew;

    private int age;

    private String userId;


    public String getId(){
        return id;
    }
    public void setId(String id){
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

    public int getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getNickName(){ return nickName; }
    public void setNickName (String nickName) {this.nickName = nickName;}

    public boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(boolean isAdmin) {this.isAdmin = isAdmin; }

    public String getCrew() {return crew;}
    public void setCrew(String crew) {this.crew = crew;}

    public User(){}

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

