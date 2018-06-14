package net.ngorham.tallycounter;

/**
 * Tally Counter
 * Tally.java
 * POJO
 * Purpose: Provides local storage and access to a Tally
 *
 * @author Neil Gorham
 * @version 1.0 06/10/2018
 *
 */

public class Tally {
    //Private variables
    private int id;
    private String name;
    private double count;
    private double startCount;
    private double incrementBy;
    private String category;
    private int color;
    private String createdOn;
    private String lastModified;

    //Constructor
    public Tally(int id, String name, double count, double startCount,
                 double incrementBy, String category, int color,
                 String createdOn, String lastModified){
        setId(id);
        setName(name);
        setCount(count);
        setStartCount(startCount);
        setIncrementBy(incrementBy);
        setCategory(category);
        setColor(color);
        setCreatedOn(createdOn);
        setLastModified(lastModified);
    }

    //Setter methods
    public void setId(int id){ this.id = id; }

    public void setName(String name){ this.name = name; }

    public void setCount(double count){ this.count = count; }

    public void setStartCount(double count){ this.startCount = count; }

    public void setIncrementBy(double incrementBy){ this.incrementBy = incrementBy; }

    public void setCategory(String category){ this.category = category; }

    public void setColor(int color){ this.color = color; }

    public void setCreatedOn(String createdOn){ this.createdOn = createdOn; }

    public void setLastModified(String lastModified){ this.lastModified = lastModified; }

    //Getter methods
    public int getId(){ return id; }

    public String getName(){ return name; }

    public double getCount(){ return count; }

    public double getStartCount(){ return startCount; }

    public double getIncrementBy(){ return incrementBy; }

    public String getCategory(){ return category; }

    public int getColor(){ return color; }

    public String getCreatedOn(){ return createdOn; }

    public String getLastModified(){ return lastModified; }

    public String toString(){
        return "id = " + id
                + "\nname = " + name
                + "\ncount = " + count
                + "\nstartCount = " + startCount
                + "\nincrementBy = " + incrementBy
                + "\ncategory = " + category
                + "\ncolor = " + color
                + "\ncreatedOn = " + createdOn
                + "\nlastModified = " + lastModified;
    }
}
