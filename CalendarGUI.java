package simplecalendar;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.time.*;
import javax.swing.event.*;
import java.time.format.*;

/**
 * Programming Assignment 4 CalendarGUI class File
 * 
 * @author Aung Paing Soe
 * @version 1.0 4/22/23
 */

/**
 * A Java class that models a calendar application using GUI
 */
public class CalendarGUI implements ChangeListener {
	
	public static final Dimension grid = new Dimension(70, 70);
	public static final int FRAME_WIDTH = 2000;
	public static final int FRAME_HEIGHT = 900;
	
	private SimpleCalendar cal;
	private LocalDate current;
	private LocalDate clicked;
	private JFrame frame;
	private JPanel monthView;
	private JPanel header;
	private JPanel calendarGridsPanel;
	private JPanel buttons;
	private JLabel monthLabel;
	private JPanel dayView;
	private JLabel dayViewHeader;
	private JTextArea dayEvents;
	private CreateGUI createFrame;
	
	/**
	 * Constructs the look and feel of this calendar application, which includes a month view and a day view
	 * 
	 * @param c the calendar model
	 * @param writeFile the file to write all scheduled events after quitting the application
	 */
	public CalendarGUI(SimpleCalendar c, File writeFile) {
		cal = c;
		frame = new JFrame();
		frame.setTitle("Simple Calendar");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		current = LocalDate.now();
		clicked = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL YYYY");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE M/dd");
		
		monthView = new JPanel();
		monthView.setLayout(new BorderLayout());
		
		buttonsGUI(formatter, dateFormatter, writeFile);
		calendarGridGUI(dateFormatter);
		monthViewLabelGUI(formatter);
		monthView.add(header, BorderLayout.NORTH);
		monthView.add(calendarGridsPanel, BorderLayout.CENTER);
		dayViewGUI(dateFormatter);
		
		frame.add(buttons, BorderLayout.NORTH);
		frame.add(monthView, BorderLayout.WEST);
		frame.add(dayView, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * A helper method that draws the labels on the month view, which includes the month and year label
	 * and the week label
	 * 
	 * @param yearMonth the formatter to be used when labeling month and year of this calendar
	 */
	private void monthViewLabelGUI(DateTimeFormatter yearMonth) {
		header = new JPanel();
		header.setLayout(new BorderLayout());
		monthLabel = new JLabel(yearMonth.format(current), SwingConstants.CENTER);
		monthLabel.setFont(new Font(monthLabel.getFont().getName(), Font.BOLD, 20));
		JLabel weekLabel = new JLabel("     Sun       Mon        Tue        Wed       Thu         Fri         Sat");
		weekLabel.setFont(new Font(monthLabel.getFont().getName(), Font.PLAIN, 16));
		header.add(new JLabel("\n"), BorderLayout.CENTER);
		header.add(weekLabel, BorderLayout.SOUTH);
		header.add(monthLabel, BorderLayout.NORTH);
	}
	
	/**
	 * A helper method that draws the grids of the calendar of the month view
	 * 
	 * @param dayFormatter the formatter to be used when displaying the day of week and the date
	 */
	private void calendarGridGUI(DateTimeFormatter dayFormatter) {
		calendarGridsPanel = new JPanel();
		calendarGridsPanel.setLayout(new GridLayout(0,7));
		LocalDate firstDay = LocalDate.of(current.getYear(), current.getMonth(), 1);
		int firstDayOfWeek = firstDay.getDayOfWeek().getValue();
		int daysInMonth = current.lengthOfMonth();
		
		if (firstDayOfWeek != 7) {
			for (int i = 0; i < firstDayOfWeek; i++) {
				JButton space = new JButton();
				space.setPreferredSize(grid);
				space.setMinimumSize(grid);
				calendarGridsPanel.add(space);
			}
		}

		int day = 1;
		while (day <= daysInMonth) {
			LocalDate temp = LocalDate.of(current.getYear(), current.getMonth(), day);
			String dayInStr = Integer.toString(day);
			if (temp.equals(current)) {
				JButton today = new JButton(dayInStr);
				today.setPreferredSize(grid);
				today.setMinimumSize(grid);
				today.setBackground(Color.LIGHT_GRAY);
				today.setOpaque(true);
				today.setBorderPainted(false);
				today.addActionListener(e -> {
					clicked = LocalDate.of(current.getYear(), current.getMonth(), Integer.parseInt(today.getText()));
					dayViewHeader.setText(dayFormatter.format(clicked));
					dayEvents.setText(cal.dayView(clicked));
				});
				calendarGridsPanel.add(today);
			}
			else {
				JButton aDay = new JButton(dayInStr);
				aDay.setPreferredSize(grid);
				aDay.setMinimumSize(grid);
				aDay.addActionListener(e -> {
					clicked = LocalDate.of(current.getYear(), current.getMonth(), Integer.parseInt(aDay.getText()));
					dayViewHeader.setText(dayFormatter.format(clicked));
					dayEvents.setText(cal.dayView(clicked));
				});
				calendarGridsPanel.add(aDay);
			}
			day++;
		}
	}
	
	/**
	 * A helper method that draws buttons for moving forward and backward, creating events, and quitting the
	 * calendar application
	 * 
	 * @param yearMonth the formatter to be used when labeling month and year of this calendar
	 * @param day the formatter to be used when displaying the day of week and the date
	 * @param toWrite the file to write all scheduled events after quitting the application
	 */
	private void buttonsGUI(DateTimeFormatter yearMonth, DateTimeFormatter day, File toWrite) {
		buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		JButton create = new JButton("CREATE");
		create.setAlignmentX(Component.CENTER_ALIGNMENT);
		create.setBackground(Color.RED);
		create.setForeground(Color.WHITE);
		create.setOpaque(true);
		create.setBorderPainted(false);
		create.addActionListener(e -> {
			createFrame = new CreateGUI(cal, clicked);
		});
		JPanel arrows = new JPanel();
		arrows.setLayout(new BorderLayout());
		JButton left = new JButton("<");
		left.setAlignmentX(Component.CENTER_ALIGNMENT);
		left.addActionListener(e -> {
			current = current.minusDays(1);
			monthLabel.setText(yearMonth.format(current));
			dayViewHeader.setText(day.format(current));
			dayEvents.setText(cal.dayView(current));
			monthView.remove(calendarGridsPanel);
			calendarGridGUI(day);
			monthView.add(calendarGridsPanel, BorderLayout.CENTER);
			monthView.revalidate();
			frame.remove(monthView);
			frame.add(monthView, BorderLayout.WEST);
			frame.revalidate();
			frame.setVisible(true);
			
		});
		JButton right = new JButton(">");
		right.setAlignmentX(Component.CENTER_ALIGNMENT);
		right.addActionListener(e -> {
			current = current.plusDays(1);
			monthLabel.setText(yearMonth.format(current));
			dayViewHeader.setText(day.format(current));
			dayEvents.setText(cal.dayView(current));
			monthView.remove(calendarGridsPanel);
			calendarGridGUI(day);
			monthView.add(calendarGridsPanel, BorderLayout.CENTER);
			monthView.revalidate();
			frame.remove(monthView);
			frame.add(monthView, BorderLayout.WEST);
			frame.revalidate();
			frame.setVisible(true);
		});
		JButton quit = new JButton("QUIT");
		quit.setAlignmentX(Component.CENTER_ALIGNMENT);
		quit.addActionListener(e -> {
			try {
				cal.writeFile(toWrite);
			} catch (IOException e1) {
				System.out.println("The file cannot be written");
			}
			System.exit(0);
		});
		arrows.setLayout(new BorderLayout());
		arrows.add(left, BorderLayout.WEST);
		arrows.add(Box.createHorizontalStrut(345));
		arrows.add(right, BorderLayout.EAST);
		
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
		options.add(create);
		options.add(Box.createHorizontalStrut(500));
		options.add(quit);
		
		buttons.add(arrows, BorderLayout.WEST);
		buttons.add(options, BorderLayout.EAST);
	}
	
	/**
	 * A helper method that draws the day view of this calendar 
	 * 
	 * @param dayFormatter the formatter to be used when displaying the day of week and the date
	 */
	private void dayViewGUI(DateTimeFormatter dayFormatter) {
		dayView = new JPanel();
		dayView.setLayout(new BorderLayout());
		dayViewHeader = new JLabel(dayFormatter.format(current), SwingConstants.CENTER);
		dayViewHeader.setFont(new Font(monthLabel.getFont().getName(), Font.BOLD, 20));
		
		dayEvents = new JTextArea();
		dayEvents.setFont(new Font(dayEvents.getName(), Font.PLAIN, 20));
		dayEvents.setText(cal.dayView(current));
		dayView.add(dayViewHeader, BorderLayout.NORTH);
		dayView.add(dayEvents, BorderLayout.CENTER);
	}

	/**
	 * Updates the day view that displays all scheduled events, including newly created events
	 * 
	 * @param e the changed event, which in this case is when a new event is created
	 */
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		dayEvents.setText("");
		dayEvents.setText(cal.dayView(clicked));
	}

}
