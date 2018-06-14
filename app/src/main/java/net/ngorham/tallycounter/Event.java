package net.ngorham.tallycounter;

/**
 * Tally Counter
 * Event.java
 * POJO
 * Purpose: Provides local storage and access to a Event
 *
 * @author Neil Gorham
 * @version 1.0 06/10/2018
 *
 */

public class Event {
    //Private variables
    private int id;
    private int tallyId;
    private String description;
    private double count;
    private int color;
    private String createdOn;

    //Constructor
    public Event(int id, int tallyId, String description, double count,
                 int color, String createdOn){
        setId(id);
        setTallyId(tallyId);
        setDescription(description);
        setCount(count);
        setColor(color);
        setCreatedOn(createdOn);
    }

    //Setter methods
    public void setId(int id){ this.id = id; }

    public void setTallyId(int tallyId){ this.tallyId = tallyId; }

    public void setDescription(String description){ this.description = description; }

    public void setCount(double count){ this.count = count; }

    public void setColor(int color){ this.color = color; }

    public void setCreatedOn(String createdOn){ this.createdOn = createdOn; }

    //Getter methods
    public int getId(){ return id; }

    public int getTallyId(){ return  tallyId; }

    public String getDescription(){ return description; }

    public double getCount(){ return count; }

    public int getColor(){ return color; }

    public String getCreatedOn(){ return createdOn; }

    public String toString(){
        return "id = " + id
                + "\ntallyId = " + tallyId
                + "\ndescription = " + description
                + "\ncount = " + count
                + "\ncreatedOn = " + createdOn;
    }
}
