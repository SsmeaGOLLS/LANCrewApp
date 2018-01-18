package eamv.dmu17he.lancrewapp.model;

import java.util.Date;

/**
 * Created by Shahnaz Yahyavi on 1/16/2018.
 */

public class Message {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("createdAt")
    private Date createdAt;

    private String message;
    private String crewId;
    private String memberId;



    public Message() {

    }

    @Override
    public String toString() {
        return getText();
    }

    public Message(String id, String message, String crewId, String memberId, Date createdAt) {
        this.id =id;
        this.message =message;
        this.crewId =crewId;
        this.memberId =memberId;
        this.createdAt =createdAt;
    }

    /**
     * Returns the item text
     */
    public String getText() {
        return message;
    }


    public final void setText(String message) {
        this.message = message;
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        this.id = id;
    }


    public String getMemberId() {
        return memberId;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setMemberId(String memberId) {
        this.memberId =memberId;
    }


    public String getCrewId() {
        return crewId;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setCrewId(String crewId) {
        this.crewId =crewId;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt =createdAt;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Message && ((Message) o).id == id;
    }
}
