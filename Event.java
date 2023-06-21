package simplecalendar;

import java.time.*;

/**
 * Programming Assignment 4 Event class file
 *
 * @author Aung Paing Soe
 * @version 1.0 4/23/23
 */

// I used the code from Event.java file from Programming Assignment 1 for this assignment.

/**
 * A Java class that models an event and its functions.
 */
public class Event implements Comparable<Event> {
	
	private String eventName;
	private LocalDate date;
	private TimeInterval interval;
	private String daysOfWeek;
	private LocalDate endDate;
	
	/**
	 * Constructs the Event object given the name and the time interval of the event 
	 * 
	 * @param name the name of the event
	 * @param i the time interval of the event
	 * @param d the date of the event
	 */
	public Event(String name, LocalDate d, TimeInterval i) {
		eventName = name;
		date = d;
		interval = i;
	}
	
	/**
	 * Constructs the Event object for recurring events given the name and the array containing 
	 * date information
	 * 
	 * @param name the name of the recurring event
	 * @param dOfW the day(s) of week that are repeating for this recurring event
	 * @param i the time interval of the recurring event
	 * @param start the start date of the recurring event
	 * @param end the end date of the recurring event
	 */
	public Event (String name, String dOfW, TimeInterval i, LocalDate start, LocalDate end) {
		eventName = name;
		interval = i;
		daysOfWeek = dOfW;
		date = start;
		endDate = end;
	}
	
	/**
	 * Returns the name of this event
	 * 
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}
	
	/**
	 * Returns the time interval of this event.
	 * 
	 * @return the time interval
	 */
	public TimeInterval getInterval() {
		return interval;
	}
	
	/**
	 * Returns the date of an one time event or the start date of a recurring event.
	 * 
	 * @return the date of an one time event or the start date of a recurring event
	 */
	public LocalDate getDate() {
		return date;
	}
	
	/** 
	 * Return the days of week that are occurring of a recurring event.
	 * 
	 * @return the days of week of a recurring event
	 */
	public String getDaysOfWeek() {
		return daysOfWeek;
	}
	
	/** 
	 * Return the end date of a recurring event.
	 * 
	 * @return the end date of a recurring event
	 */
	public LocalDate getEnd() {
		return endDate;
	}
	
	/**
	 * Checks whether the event is in conflict with other events under the same date.
	 * 
	 * @param a the event to check
	 * @return boolean value of true or false
	 */
	public boolean conflict(Event a) {
		LocalDate ad = a.getDate();
		LocalDate thisD = this.getDate();
		TimeInterval aTimeInterval = a.getInterval();
		TimeInterval thisTimeInterval = this.getInterval();
		if (ad.equals(thisD))
			return thisTimeInterval.conflict(aTimeInterval);
		else 
			return false;
	}
	
	/**
	 * Compares the two events by its start time 
	 * 
	 * @param o the other event object to be compared to
	 * @return a positive integer if this event comes after the object
	 * 		   a negative integer if this event comes before the object
	 */
	@Override
	public int compareTo(Event o) {
		LocalDate day = o.getDate();
		LocalTime start = o.getInterval().getStartTime();
		int result = this.date.compareTo(day);
		if (result == 0) {
			result = this.interval.getStartTime().compareTo(start);
		}
		return result;
	}
}