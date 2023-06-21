package simplecalendar;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Programming Assignment 4 TimeInterval class file
 * 
 * @author Aung Paing Soe
 * @version 1.0 4/23/23
 */

//I used code from TimeInterval.java file from Programming Assignment 1 for this assignment.

/**
 *  A Java class that provides the time interval of an event
 */
public class TimeInterval {

	private LocalTime startTime;
	private LocalTime endTime;
	
	/**
	 * Constructor for objects of TimeInterval class
	 * 
	 * @param s starting time of the event
	 * @param e ending time of the event
	 */
	public TimeInterval(LocalTime s, LocalTime e) {
		startTime = s;
		endTime = e;
	}
	
	/**
	 * Returns the start time of this time interval
	 * 
	 * @return the start time
	 */
	public LocalTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Returns the end time of this time interval
	 * 
	 * @return the end time
	 */
	public LocalTime getEndTime() {
		return endTime;
	}
	
	/**
	 * Checks if a time interval is in conflict with this time interval.
	 * 
	 * @param a the time interval to check
	 * @return a boolean value of true if there is a conflict; false otherwise
	 */
	public boolean conflict(TimeInterval a) {
		LocalTime aEnd = a.getEndTime();
		LocalTime aStart = a.getStartTime();
		LocalTime thisStart = this.getStartTime();
		LocalTime thisEnd = this.getEndTime();
		if (thisStart.isAfter(aStart) && thisStart.isBefore(aEnd)) {
			return true;
		}
		else if (thisStart.isBefore(aStart) && thisEnd.isAfter(aStart)) {
			return true;
		}
		else if (thisStart.isAfter(aStart) && thisEnd.isBefore(aEnd)) {
			return true;
		}
		else if (thisStart.equals(aStart) || thisEnd.equals(aEnd)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Prints the start and end time of this time interval
	 * 
	 * @param start the start time
	 * @param end the end time
	 */
	public void printIntervals(LocalTime start, LocalTime end) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("kk:mm");
		System.out.print(format.format(start) + " - ");
		System.out.print(format.format(end) + "  ");
	}
}