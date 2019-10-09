import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
public class Hotel
{
    /////////////////////////////////////////////////////////////////////////////////////////
    // Instance Variables
    /////////////////////////////////////////////////////////////////////////////////////////
    // TODO 7: Add code to create instance variables encapsulating info found in Hotels.txt...
    long id; //ID for particular hotel
    String name; //name of hotel
    String addr; //address of hotel
    String cityName; //city that the hotel is located
    String stAbbrev; //street as an abbreviation that the hotel is located
    double price; //price per night in hotel
    //private static final String fileName = "Reservation.txt"; 
    ArrayList<Reservation> reservations = new ArrayList<Reservation>(); //setting up an arraylist for reservations to be used by methods below
    ArrayList<Hotel> hotels = new ArrayList<Hotel>(); //setting up an arrayliset for hotels to be used by methods below
    boolean canBook = true; //boolean for whether or not a reservation can be made or not
    DecimalFormat df = new DecimalFormat("0.00");
    /////////////////////////////////////////////////////////////////////////////////////////
    // Overloaded Constructor
    /////////////////////////////////////////////////////////////////////////////////////////
    public Hotel(long id, String name, String addr, String cityName, String stAbbrev, double price) //The constructor can be called to input information as a hotel object
    {
        // TODO 8: Add code to finish overloaded constructor (works as expected)...
        //Making the inputed information as a value for each of the Instance variables
        this.id = id;
        this.name = name;
        this.addr = addr;
        this.cityName = cityName;
        this.stAbbrev = stAbbrev;
        this.price = price;
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // Default constructor
    /////////////////////////////////////////////////////////////////////////////////////////
    public Hotel()
    {
        // TODO 9: Add code to finish default constructor (you are free to pick default values)...
        //Setting default values for each of the Instance variables if no value is inputed into the object
        this.id = 1;
        this.name = "pP";
        this.addr = "5749 Black Oaks";
        this.cityName = "Azusa";
        this.stAbbrev = "CA";
        this.price = 179.00;
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // Class Methods
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    // This method takes in a new reservation and compares it against 
    // all other reservations in this hotels reservations ArrayList.
    // Returns true if the new reservation can be made; returns false
    // if the new reserveration (newRes) will conflict with an old
    // reservation.
    /////////////////////////////////////////////////////////////////////////////////////////
    public boolean canBook(Reservation newRes)
    {
        // TODO 10: Add code to complete method...
        //checking to make sure that the new reservation does not overlap any other reservation
        for (Reservation r: reservations) { //Cycling through each reservation in the reservations array list
            if (r.getInMonth() != newRes.getInMonth()) { //checking if check in month is not equal to check out month
                canBook = true; // if they are not equal than the reservations do not overlap
            }
            else { //if the check in months are the same
                if (r.getInDay() < newRes.getInDay() && r.getOutDay() > newRes.getOutDay()) { //checking if new reservation takes place after other reservation's check in day
                    canBook = false; //new reservation starts within the overlap period
                }
                else if (r.getInDay() > newRes.getInDay() && r.getOutDay() < newRes.getOutDay()){ //checking if new reservation takes place before other reservation's check in day
                    canBook = false;//new reservation ends within the overlap period
                }
                else if (r.getInDay() <= newRes.getInDay() && r.getOutDay() <= newRes.getOutDay()) {
                    canBook = false;
                }
                else if (r.getInDay() >= newRes.getInDay() && r.getOutDay() >= newRes.getOutDay()) {
                    canBook = false;
                }
            }
        }
        return canBook; //returns either true or false depending on if the reservation overlaps or not
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // Adds the new reservation (newRes) to the ArrayList of reservations
    // (instance variables)
    /////////////////////////////////////////////////////////////////////////////////////////
    public void addReservation(Reservation newRes)
    {
        // TODO 11: Add code to complete method...
        if (canBook = true) { //if the new reservation does not overlap other reservations
            reservations.add(newRes); //the new reservation is added to the array list
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // SIMPLE method that uses the previous two methods (canBook() and addReservation()). If
    // canBook() returns true, calls addReservation() to add newRes and returns true;
    // otherwise, returns false.
    /////////////////////////////////////////////////////////////////////////////////////////
    public boolean addResIfCanBook(Reservation newRes)
    {
        // TODO 12: Add code to complete method...
        boolean ifCanBook = false;
        if (canBook(newRes) == true) { //if canBook method returns true (new reservation can be made)
            addReservation(newRes); //adds reservation using the method above
            ifCanBook = true; //returns true since the reservation is added
        }
        return ifCanBook; ///reservation is added since it can be booked
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // Generating display String for printout (kind of like the toString() method). Should be
    // in the following form:
    //      1) Azusa Inn (23 Main St., Azusa, CA) @ $159.00/night
    //
    // NOTE: In this example, the "1)" is the uniqueId of this hotel, "Azusa Inn", is the 
    // name of the hotel, etc., etc...
    /////////////////////////////////////////////////////////////////////////////////////////
    public String toDisplayString()
    {
        // TODO 13: Add code to complete method...
        // printing out hotel information
        return getUniqueId() + " " + getHotelName() + " (" + getAddr() + ", " + getCityName() + ", " + getStAbbrev() + ") @ " + df.format(getPricePerNight()) + "/night";
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters
    /////////////////////////////////////////////////////////////////////////////////////////
    // TODO 14: Add code to create ALL getters/setters (REMINDER: Eclipse can do this for you
    // if you've already created the instance variables)...
    public long getUniqueId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getHotelName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getStAbbrev() {
        return stAbbrev;
    }
    public void setStAbbrev(String stAbbrev) {
        this.stAbbrev = stAbbrev;
    }
    public double getPricePerNight() {
        
        return price;
        
    }
    public void setPrice(double price) {
        this.price = price;
    }
