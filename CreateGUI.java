package simplecalendar;

import java.time.*;
import java.time.format.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Programming Assignment 4 CreateGUI class File
 * 
 * @author Aung Paing Soe
 * @version 1.0 4/23/23
 */

/*
 * A Java class that draws a frame and provides a display that allows users to create an event.
 */
public class CreateGUI extends JFrame{

	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 150;
	public static final int EVENT_BOX_WIDTH = 400;
	public static final int EVENT_BOX_HEIGHT = 50;
	
	private SimpleCalendar calendar;
	private JTextField dateBox;
	private JTextField eventName;
	private JTextField startTime;
	private JTextField endTime;
	private JPanel time;
	private JButton save;
	private JLabel successLabel;
	
	/**
	 * Constructs the frame that prompts user input for creating new events
	 * 
	 * @param c simple calendar model
	 * @param date the date the user wishes to create an event in
	 */
	public CreateGUI(SimpleCalendar c, LocalDate date) {
		calendar = c;
		setTitle("Create An Event");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new BorderLayout());
		
		eventName = new JTextField();
		eventName.setPreferredSize(new Dimension(EVENT_BOX_WIDTH, EVENT_BOX_HEIGHT));
		eventName.setMinimumSize(new Dimension(EVENT_BOX_WIDTH, EVENT_BOX_HEIGHT));
		eventName.setFont(new Font(eventName.getFont().getName(), Font.PLAIN, 26));
		
		dateBoxGUI(date);
		saveButtonGUI(date);
		timeGUI();
		
		JPanel bottomLine = new JPanel();
		bottomLine.setLayout(new BoxLayout(bottomLine, BoxLayout.X_AXIS));
		bottomLine.add(dateBox);
		bottomLine.add(Box.createHorizontalStrut(50));
		bottomLine.add(time);
		bottomLine.add(Box.createHorizontalStrut(50));
		bottomLine.add(save);
		
		JPanel topLine = new JPanel();
		topLine.setLayout(new BorderLayout());
		topLine.add(successLabel, BorderLayout.NORTH);
		topLine.add(eventName, BorderLayout.CENTER);
		
		JLabel instruction = new JLabel("Please enter your event name and your starting time and ending time (in 24-hour format) below: ");
		add(instruction, BorderLayout.NORTH);
		add(topLine, BorderLayout.CENTER);
		add(bottomLine, BorderLayout.SOUTH);
		pack();
		setVisible(true);
		
	}
	
	/**
	 * A helper method that draws a text box with the date the user wishes to create a new event
	 * 
	 * @param date the date the user wishes to create a new event in
	 */
	private void dateBoxGUI(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		dateBox = new JTextField(formatter.format(date));
		dateBox.setPreferredSize(new Dimension(125,50));
		dateBox.setMinimumSize(new Dimension(125,50));
		dateBox.setFont(new Font(dateBox.getFont().getName(), Font.PLAIN, 20));
		dateBox.setEditable(false);
		dateBox.setBackground(Color.WHITE);
	}
	
	/**
	 * A helper method that draws a save buttons and when clicked, it saves the event to the model
	 * and displays it to the day view
	 * 
	 * @param d the date the user wishes to create a new event in
	 */
	private void saveButtonGUI(LocalDate d) {
		save = new JButton("SAVE");
		successLabel = new JLabel();
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, 20));
		save.setPreferredSize(new Dimension(100, 50));
		save.setMinimumSize(new Dimension(100, 50));
		save.setBackground(Color.RED);
		save.setForeground(Color.WHITE);
		save.setOpaque(true);
		save.setBorderPainted(false);
		save.addActionListener(e -> {
			Event toAdd = calendar.createEvent(eventName.getText(), d, startTime.getText(), endTime.getText());
			String returnMessage = calendar.addOneTime(d, toAdd);
			if (returnMessage.equals("There is a time conflict; The event cannot be added!")) {
				successLabel.setText(returnMessage);
				successLabel.setBackground(Color.WHITE);
				successLabel.setForeground(Color.RED);
			}
			else {
				dispose();
			}
		});
	}
	
	/**
	 * A helper method that draws two text boxes, which the user enters for starting time and ending time
	 */
	private void timeGUI() {
		time = new JPanel();
		time.setLayout(new BorderLayout());
		startTime = new JTextField();
		startTime.setPreferredSize(new Dimension(125,50));
		startTime.setMinimumSize(new Dimension(125,50));
		startTime.setFont(new Font(startTime.getFont().getName(), Font.PLAIN, 20));
		JLabel to = new JLabel("to");
		endTime = new JTextField();
		endTime.setPreferredSize(new Dimension(125,50));
		endTime.setMinimumSize(new Dimension(125,50));
		endTime.setFont(new Font(endTime.getFont().getName(), Font.PLAIN, 20));
		time.add(startTime, BorderLayout.WEST);
		time.add(to, BorderLayout.CENTER);
		time.add(endTime, BorderLayout.EAST);
	}
}
