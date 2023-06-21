package simplecalendar;

import java.io.*;

/**
 * Programming Assignment 4 SimpleCalendarTester class File
 * 
 * @author Aung Paing Soe
 * @version 1.0 4/23/23
 */

/**
 * A Java class that runs the calendar application with GUI
 */
public class SimpleCalendarTester{
	
	/**
	 * The main method that runs the calendar application
	 * 
	 * @param args command-line arguments
	 * @throws IOException when there is an input error in the file, if the file exists
	 */
	public static void main(String[] args) throws IOException {
        
		File readAndWriteFile = new File("events.txt");
		SimpleCalendar cal = new SimpleCalendar();
		if (readAndWriteFile.exists()) {
			cal.readFile(readAndWriteFile);
		}
		CalendarGUI gui = new CalendarGUI(cal, readAndWriteFile);
		cal.attach(gui);
	}
}