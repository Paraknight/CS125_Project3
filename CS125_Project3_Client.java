/* CS 125 - Intro to Computer Science II
 * File Name: CS125_Project3_Client.java
 * Project 3 - Due 2/24/2019
 * Instructor: Dr. Dan Grissom
 * 
 * Name: Brandon Watanabe
 * Description: Project 3 Is a File I/O program for a hotel manager that takes in input (reservations) from user and stores it in Reservations.txt
 * it allows user to make reservations for 5 different hotels listed in Hotels.txt 
 */
​
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
​
public class CS125_Project3_Client
{
	//private static final String fileName = "Reservation.txt";
​
	private static ArrayList<Hotel> hotels = new ArrayList<Hotel>();
	private static ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	DecimalFormat df = new DecimalFormat("0.00");
​
	public static void main(String[] args)
	{
		// Your program should always output your name and the lab/problem
		// number. DO NOT DELETE OR COMMENT OUT. Replace with relevant info.
		// TODO 15: Update your name...
		System.out.println("Brandon Watanabe");
		System.out.println("Project 3");
		System.out.println("");
​
		// Create scanner
		Scanner scan = new Scanner(System.in);
​
		// Perform initial reading of data
		ArrayList<Hotel> hotels = getHotelsFromFile("Hotels.txt");
		ArrayList<Reservation> reservations = readReservationsFromFile("Reservations.txt");
​
		// Associate each reservation with the appropriate hotel
		assignReservationsToCorrectHotel(reservations, hotels);
​
		// Print out welcome 
		System.out.println("Welcome to the Parental Paradise Hotel Chain.");
		Hotel selectedHotel = null;
​
		// Prompt with hotel list and repeat until get valid hotel selection
		do
		{       
			System.out.println("Please select from one of the following hotels (select the number and press enter) to make a reservation: ");
			for (Hotel h : hotels)
				System.out.println("\t" + h.toDisplayString());
			System.out.print("\nYour selection: ");
			// Read user input & get hotel
			int choice = scan.nextInt();
			for (Hotel h : hotels)
				if (h.getUniqueId() == choice)
					selectedHotel = h;
			if (selectedHotel == null)
				System.out.println("Invalid hotel choice. Please try again...");
		} while (selectedHotel == null);
		// Create variables for reservation
		int month;
		int checkinDay;
		int checkoutDay;
		boolean invalidDateRange = true;
		// Prompt for check-in month and check-in/check-out day until get valid selection
		do
		{
			System.out.println("Please enter details about your reservation request: ");
			System.out.print("\tMonth (1-12): ");
			month = scan.nextInt();
			System.out.print("\tCheck-in day: ");
			checkinDay = scan.nextInt();
			System.out.print("\tCheck-out day: ");
			checkoutDay = scan.nextInt();
			if (month <= 0 || month > 12)
				System.out.println("Invalid month choice. Please try again...");
			else if (checkinDay <= 0 || checkoutDay <= 0)
				System.out.println("Invalid check-in or check-out choice; must be greater than 0. Please try again...");
			else if (checkinDay > 31 || checkoutDay > 31)
				System.out.println("Invalid check-in or check-out choice; must be greater than 0. Please try again...");
			else if (checkoutDay == checkinDay)
				System.out.println("Invalid check-in or check-out range; must stay at least one night. Please try again...");
			else if (checkoutDay <= checkinDay)
				System.out.println("Invalid check-in or check-out range; cannot checkout before checking in. Please try again...");
			else
			{
				invalidDateRange = false;
			}
		} while (invalidDateRange);
		// Create reservation from user input
		Reservation newRes = new Reservation(selectedHotel.getUniqueId(), month, month, checkinDay, checkoutDay);
​
		// Try to add reservation
		if (selectedHotel.addResIfCanBook(newRes))
		{
			// Then add new reservation to global reservations list and write out to file
			System.out.println("Reservation successfully added: " + selectedHotel.getHotelName() + ": " + newRes.getFormattedDisplayString(selectedHotel.getPricePerNight()));
			reservations.add(newRes);
			writeReservationFile("Reservations.txt", reservations);
		}
		else
		{
			System.out.println("Could not add the reservation (" + newRes.getFormattedDisplayString() + ") to " + selectedHotel.getHotelName() + " b/c of a conflict.");
			System.out.println("Please re-run the program to try a new date.");
		}
​
		// Exit program
		System.out.println("\nThank you for using the Parental Paradise Hotel Manager.");
	}
​
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// This method takes in a list of hotels and reservations. At this point, the hotel
	// objects should have an empty ArrayList of Reservations (as an instance variable).
	// This method cycles through the reservations and assigns them to the hotel with
	// matching uniqueId as the resrevation's hotelId.
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void assignReservationsToCorrectHotel(ArrayList<Reservation> reservations, ArrayList<Hotel> hotels)
	{
		// TODO 16: Add code to complete method...
​
		//comparing the id of the hotel and the reservation
		for (Reservation r: reservations) {
			for (Hotel h: hotels) {
​
				if (r.getHotId() == h.getUniqueId()) { //comparing reservation ID with Hotel unique ID
					h.addReservation(r); //adds reservation to hotel
				}
			}
		}
​
	}
​
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Reads Hotels from fileName and returns as a new ArrayList of hotels.
	//
	// Uses plain-text input.
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private static ArrayList<Hotel> getHotelsFromFile(String fileName)
	{
		// TODO 17: Add code to complete method...
​
		// Declare input objects
		FileInputStream fis = null;
		Scanner fScan = null;
​
		//String file = fileName;
		// reads hotels from file and adds them to hotels ArrayList.....
		try {
			// Make a connection to the file
			fis = new FileInputStream(fileName);
			fScan = new Scanner(fis);
​
			// Read data in from file
			while (fScan.hasNextLine()) {
​
				String line = fScan.nextLine();
​
				// setting values of each of the variables from file
				Scanner lScan = new Scanner(line);
				lScan.useDelimiter(",");
​
				long id = Long.parseLong((lScan.next().substring(6)));
​
				String name = lScan.next();
				String addr = lScan.next().trim();
				String cityName = lScan.next().trim();
				String stAbbrev = lScan.next().trim();
				double price = Double.parseDouble((lScan.next().substring(0, 4)));
​
				//making hotel object using values from file
				Hotel h = new Hotel(id, name, addr, cityName, stAbbrev, price);
				hotels.add(h);
			}
		} catch(FileNotFoundException e) { //If file is not found prints error
			System.out.println("ERROR:" + fileName + " input file not found.");
			e.printStackTrace();
			//		} catch (Exception e) { //unknown error
			//			System.out.println("ERROR: Unknown error occured while attempting to open and read from the file " + fileName);
			//			
			//			System.err.println("TODO 17 line 186");
		} finally {
			try {
				if (fScan != null) fScan.close(); //checking if file has an input or not
				if (fis != null) fis.close();
			} catch (Exception e) { //opening and reading the file caused error
				System.out.println("ERROR: Unknown error occured while attempting to open and read from the file " + fileName);
			}
		}
		return hotels; //returns the hotels array list
	}
​
​
​
​
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Reads Reservations from a given file and stores them into the reservations
	// list. Returns a new ArrayList of reservations read in from file. If no reservations
	// in file found at fileName, should return an empty ArrayList. 
	//
	// Uses serialize for input.
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private static ArrayList<Reservation> readReservationsFromFile(String fileName)
	{
		// TODO 18: Add code to complete method...
​
		// Declare input objects
		FileInputStream fis = null;
		ObjectInputStream ois = null;
​
		// reads hotels from file and adds them to hotels ArrayList.....
		try {
			//Init input objects/classes
			//makes a connection to file
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
​
			// Read data in from file
			while (true) {
				reservations.add( (Reservation)ois.readObject() ); // Adds reservation from file/line into order
			}
		}catch (EOFException e) { 
			System.out.println("STATUS: " + fileName + " successfully read.");
		} catch (FileNotFoundException e) {
			System.out.println(fileName + " not found, but will be created as a new order.");
​
		} catch (Exception e) {
			System.out.println("ERROR: An unknown error occurred.");
			e.printStackTrace(); //showing the error
​
		} finally {
			try {
				// Close input objects
				if (ois != null) ois.close(); //checking if file has an input or not
				if (fis != null) fis.close(); 
			} catch (Exception e) {
				System.out.println("ERROR: Problem occured while closing " + fileName);
				e.printStackTrace(); //showing the error
			}
		}
		return reservations; //returning array list of reservations
	}
​
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Overwrites the file at fileName with the reservations found in globalReservations. 
	// globalReservations should contain the reservations found in the file when the program
	// begin, as well as any new reservation the user made. Returns true upon success; false upon failure
	//
	// Uses serialize for input.
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private static boolean writeReservationFile(String fileName, ArrayList<Reservation> globalReservations)
	{
		// TODO 19: Add code to complete method...
​
		//writeReservationFile("Reservations.txt", reservations);
​
​
		//calling in globalReservations array list and adds all to the reservations file
		globalReservations.addAll(readReservationsFromFile("Reservations.txt"));
​
​
		// Initialize output streams
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean readFail = false; //if the file was unsuccessfully read
​
		try {
			//makes connection to file
			fos = new FileOutputStream("Reservations.txt");
			oos = new ObjectOutputStream(fos);
​
			//writing globalReservations array list into the Reservations.txt
			for (Reservation r : globalReservations)
				oos.writeObject(r);
​
			//printing errors if exceptions detected
		} catch(FileNotFoundException e) {
			System.err.println("ERROR: " + fileName + " file not found.");
			readFail = true; //file unsuccessfully read
		} catch(Exception e) {
			System.err.println("ERROR: An unknown error occured.");
			readFail = true;
		} finally {
			try {
				if (oos != null) oos.close(); //if file does not have an input
				if (fos != null) fos.close();
			} catch (Exception e) {
				System.err.println("ERROR: An unknown error occured.");
				readFail = true; //file unsuccessfully read
			}
		}
​
		// Return whether there was success or not
		if (readFail)
			return false;
		else
			return true; //If we made it here, then the file was read successfully. Return true.
​
	} 
}
​
​
/******************************************************************************
Insert 2 test cases, which represent program input/output for two different
combinations of inputs. You may literally copy and paste your console contents,
but your two test cases should be DIFFERENT from any test cases given in the
Project description itself.
​
------------
Test Case 1:
------------
Brandon Watanabe
Project 3
​
STATUS: Reservations.txt successfully read.
Welcome to the Parental Paradise Hotel Chain.
Please select from one of the following hotels (select the number and press enter) to make a reservation: 
	1  Azusa Inn (23 Main St., Azusa, CA) @ 159.0/night
	2  San Dimas Suits (1456 Apollo Ave., San Dimas, CA) @ 129.00/night
	3  Covina Comfort (211 Crestline St., Covina, CA) @ 109.00/night
	4  Hotel Glendorado (394 W. Third St., Glendora, CA) @ 179.00/night
	5  Pasadena Place (483 Florence St., Pasadena, CA) @ 249.00/night
​
Your selection: 2
Please enter details about your reservation request: 
	Month (1-12): 7
	Check-in day: 10
	Check-out day: 14
Reservation successfully added:  San Dimas Suits: 7/10 - 7/14 @ $129.00 per night
STATUS: Reservations.txt successfully read.
​
Thank you for using the Parental Paradise Hotel Manager.
​
------------
Test Case 2:
------------
Brandon Watanabe
Project 3
​
STATUS: Reservations.txt successfully read.
Welcome to the Parental Paradise Hotel Chain.
Please select from one of the following hotels (select the number and press enter) to make a reservation: 
	1  Azusa Inn (23 Main St., Azusa, CA) @ 159.00/night
	2  San Dimas Suits (1456 Apollo Ave., San Dimas, CA) @ 129.00/night
	3  Covina Comfort (211 Crestline St., Covina, CA) @ 109.00/night
	4  Hotel Glendorado (394 W. Third St., Glendora, CA) @ 179.00/night
	5  Pasadena Place (483 Florence St., Pasadena, CA) @ 249.00/night
​
Your selection: 5
Please enter details about your reservation request: 
	Month (1-12): 3
	Check-in day: 4
	Check-out day: 27
Reservation successfully added:  Pasadena Place: 3/4 - 3/27 @ $249.00 per night
STATUS: Reservations.txt successfully read.
​
Thank you for using the Parental Paradise Hotel Manager.
 ******************************************************************************/