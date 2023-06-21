package simplecalendar;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.event.*;

/**
 * Programming Assignment 4 SimpleCalendar class File
 * 
 * @author Aung Paing Soe
 * @version 1.04/22/23
 */

// I used portions of the code from MyCalendar.java in Programming Assignment 1 in this assignment.

/**
 * A Java class that models a calendar 
 */
public class SimpleCalendar {

	private static TreeMap<LocalDate, ArrayList<Event>> events;
	private static ArrayList<Event> oneTimeEvents;
	private ArrayList<ChangeListener> listeners;
	
	/**
	 * Constructs the SimpleCalendar object by initializing the TreeMap and two ArrayLists
	 */
	public SimpleCalendar() {
		events = new TreeMap<>();
		oneTimeEvents = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	
	/**
	 * Adds the ChangeListener to this model object
	 * 
	 * @param l the ChangeListener to be attached
	 */
	public void attach(ChangeListener l) {
		listeners.add(l);
	}
	
	/**
	 * Returns the TreeMap of dates and scheduled events on that date
	 * 
	 * @return the tree map of dates and scheduled events
	 */
	public TreeMap<LocalDate, ArrayList<Event>> getTreeMap() {
		return events;
	}
	
	/**
	 * Returns the ArrayList of all events scheduled in this calendar
	 * 
	 * @return the ArrayList of all events scheduled in this calendar
	 */
	public ArrayList<Event> getEventList() {
		return oneTimeEvents;
	}
	
	/**
	 * Adds the one time event object to the corresponding date
	 * 
	 * @param d the date of the event
	 * @param e the event object to be added 
	 */
	public String addOneTime(LocalDate d, Event e) {
		String message = "";
		if (!events.containsKey(d)) {
			ArrayList<Event> eventList = new ArrayList<Event>();
			eventList.add(e);	
			events.put(d, eventList);
			oneTimeEvents.add(e);
		}
		else {
			ArrayList<Event> oneTime = events.get(d);
			for (int i = 0; i < oneTime.size(); i++) {
				if (e.conflict(oneTime.get(i))) {
					message = "There is a time conflict; The event cannot be added!";
					return message;
				}
			}
			oneTimeEvents.add(e);
			oneTime.add(e);
			Collections.sort(oneTime);
		}
		Collections.sort(oneTimeEvents);
		message = "The event is successfully added to the calendar.";
		for (ChangeListener l : listeners) {
	         l.stateChanged(new ChangeEvent(this));
	    }
		return message;
	}
	
	/**
	 * Prints the day view of the calendar given the date of an event
	 * 
	 * @param d the date of the event
	 */
	public String dayView(LocalDate d) {
		String returnStr = "";
		if (events.containsKey(d)) {
			ArrayList<Event> today = events.get(d);
			for (int i = 0; i < today.size(); i++) {
				Event toPrint = today.get(i);
				returnStr += "\n" + "\n" + "     " + oneTimeEventToStr(toPrint) + "\n";
			}
		}
		else {
			returnStr = "\n" + "\n" + "    No events are scheduled on this day." + "\n";
		}
		return returnStr;
	}
	
	/**
	 * Provides a string representation of an one time event in this calendar.
	 * 
	 * @param e the one time event object
	 * @return the one time event in string representation
	 */
	private String oneTimeEventToStr(Event e) {
		TimeInterval time = e.getInterval();
		String event = time.getStartTime() + " - " + time.getEndTime() + "          " + e.getEventName();
		return event;
	}
	
	/**
	 * Reads the events.txt and populate the events into the calendar
	 *  
	 * @param f the file to be read
	 * @throws IOException if the file is not found
	 * @throws FileNotFoundException if the file is not found
	 */
	public void readFile(File f) throws IOException {
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			boolean done = false;
			String eventName;
			String dateInfo;
			while (!done) {
				eventName = br.readLine();
				dateInfo = br.readLine();
				if (eventName == null || dateInfo == null) {
					done = true;
					System.out.println();
					System.out.println("Loading is Done!");
				}
				else {
					if (Character.isDigit(dateInfo.charAt(0))) {
						String[] dates = dateInfo.split(" ");
						LocalDate toAdd = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("M/d/yy"));
						Event toSchedule = createEvent(eventName, toAdd, dates[1], dates[2]);
						addOneTime(toAdd, toSchedule);
					}
				}
			}
			br.close();
			fr.close();
		}
		catch (FileNotFoundException x){
			System.out.println("File is not found: " + f);
		}
	}
	
	/**
	 * Writes the persisting events in this calendar to events.txt file.
	 * 
	 * @param f the File to be written to
	 * @throws IOException if there is an error with output
	 */
	public void writeFile(File f) throws IOException {
		FileWriter fw = new FileWriter(f);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("ALL SCHEDULED EVENTS:");
		for (int i = 0; i < oneTimeEvents.size(); i++) {
			Event toPrint = oneTimeEvents.get(i);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
			pw.print("     " + formatter.format(toPrint.getDate()) + "     ");
			pw.println(oneTimeEventToStr(toPrint));
		}
		pw.close();
		fw.close();
	}
	
	/**
	 * Creates an Event object from the given name, start time, and end time in String format
	 * 
	 * @param name the name of the event
	 * @param d the date of the event
	 * @param st the starting time of the event
	 * @param et the ending time of the event
	 * @return the event object
	 */
	public Event createEvent(String name, LocalDate d, String st, String et) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("k:mm");
		LocalTime start = LocalTime.parse(st, formatter);
		LocalTime end = LocalTime.parse(et, formatter);
		TimeInterval t = new TimeInterval(start, end);
		Event e = new Event(name, d, t);
		return e;
	}
}