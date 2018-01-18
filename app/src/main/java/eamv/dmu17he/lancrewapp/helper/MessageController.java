package eamv.dmu17he.lancrewapp.helper;

import java.util.ArrayList;

import eamv.dmu17he.lancrewapp.model.Message;

/**
 * Created by Shahnaz Yahyavi on 1/9/2018.
 */

public class MessageController
{
    private final String memberID="Ole";
    private final String crewID="ewwefwef";
    private ArrayList<Message> messages;


    public MessageController()
    {
        messages = new ArrayList<Message>();
    }

    public void sendMessage(String message)
    {

    }

    public void receiveMessages()
    {

    }

    public String getCrewId() {return crewID;}
    public String getMemberId() {return memberID;}
    public ArrayList<Message> getMessages() {return messages;}
}