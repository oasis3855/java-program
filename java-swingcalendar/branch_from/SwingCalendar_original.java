import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Swing Calender");
//		frame.setSize(400,600);
		frame.add(new SwingCalendar());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}


class SwingCalendar extends JPanel {
	private JSpinner  monthChooser = new JSpinner();
	private JSpinner  yearChooser = new JSpinner();
	
	private JLabel[]    weekLabels = new JLabel[7];
	private JLabel[][]  dayLabels = new JLabel[6][7];
	
	private static final Color WEEK_BG = new Color(240,240,255);
	private static final LineBorder WEEK_BORDER = new LineBorder(new Color(160,160,230), 2, false);
	
	private static final Color DAY_BG = new Color(250,250,255);
	private static final Color DAY_FG = Color.BLACK;
	private static final LineBorder DAY_BORDER = new LineBorder(new Color(40,40,40), 2, false);
	
	private static final Color TODAY_BG = new Color(255, 240, 240);
	private static final Color TODAY_FG = new Color(105,50,50);
	private static final LineBorder TODAY_BORDER = new LineBorder(new Color(175,50,50), 2, false);

	
	private static final Dimension DAY_LABEL_SIZE = new Dimension(50,50);
	private static final String[] WEEK_NAMES = new String[] {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"}; 
	private static final String[] MONTH_NAMES = new String[] {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"}; 
		
	public SwingCalendar() {
		try {
			UIManager.setLookAndFeel(
				"javax.swing.plaf.nimbus.NimbusLookAndFeel"
			);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		/* MONTH CHOOSER */
		monthChooser.setModel(new SpinnerListModel(MONTH_NAMES));
		JTextField tm = ((JSpinner.DefaultEditor)monthChooser.getEditor()).getTextField();
		tm.setHorizontalAlignment(JTextField.CENTER);
		
		ChangeListener monthYearListener = new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				String value = (String) monthChooser.getValue();
				
				int month = 0;
				for( int i=0; i<MONTH_NAMES.length; i++ ){
					if( value.equals(MONTH_NAMES[i]) ){
						month = i;
						break;
					}
				}
				
				refreshDayLabels((int) yearChooser.getValue(), month);
			}
		};
		
		monthChooser.addChangeListener(monthYearListener);
		
		Dimension dm = monthChooser.getPreferredSize();
		dm.width += 10; //dm.height += 1;
		monthChooser.setPreferredSize(dm);
		
		/* YEAR CHOOSER */
		yearChooser.setModel(new SpinnerNumberModel(2012, 0, 5000,1 ));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearChooser, "#");
		yearChooser.setEditor(editor);
		yearChooser.addChangeListener(monthYearListener);
		
		Dimension dy = yearChooser.getPreferredSize();
		dy.width += 10; //dy.height += 1;
		yearChooser.setPreferredSize(dy);
		
		/* DAY PANEL */
		JPanel dayPanel = new JPanel();
		GridLayout layout = new GridLayout(7, 7);
		layout.setHgap(3);
		layout.setVgap(3);
		dayPanel.setLayout(layout);
		
		for( int i=0; i<7; i++) {
			weekLabels[i] = new JLabel(WEEK_NAMES[i]);
			weekLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			weekLabels[i].setVerticalAlignment(SwingConstants.CENTER);
			weekLabels[i].setPreferredSize(DAY_LABEL_SIZE);
			weekLabels[i].setOpaque(true);
			weekLabels[i].setBackground(WEEK_BG);
			weekLabels[i].setBorder(WEEK_BORDER);
			if( i == 0 ) {
				weekLabels[i].setForeground(Color.RED);
			} else if ( i == 6 ) {
				weekLabels[i].setForeground(Color.BLUE);
			}
			
			dayPanel.add(weekLabels[i]);
		}
		
		for( int i=0; i<6; i++) {
			for( int j=0; j<7; j++) {
				dayLabels[i][j] = new JLabel();
				dayLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				dayLabels[i][j].setVerticalAlignment(SwingConstants.CENTER);
				dayLabels[i][j].setOpaque(true);
				dayLabels[i][j].setBackground(DAY_BG);
				dayLabels[i][j].setPreferredSize(DAY_LABEL_SIZE);
				dayLabels[i][j].setBorder(new LineBorder(Color.DARK_GRAY, 2, false));
				dayPanel.add(dayLabels[i][j]);
			}
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel chooserPanel = new JPanel();
		GridLayout l = new GridLayout(1,2);
		l.setHgap(10); l.setVgap(10);
		chooserPanel.add(yearChooser);
		chooserPanel.add(monthChooser);
		
		add(chooserPanel);
		add(dayPanel);
		
		Calendar cal = Calendar.getInstance();
		monthChooser.setValue(MONTH_NAMES[cal.get(Calendar.MONTH)]);
		yearChooser.setValue(cal.get(Calendar.YEAR));
		refreshDayLabels(cal, true);
	}
	
	private void refreshDayLabels(int year, int month) {
		Calendar cal = Calendar.getInstance();
		if( month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR)) {
			refreshDayLabels( cal, true);
		} else {
			cal.set(year, month, 1);
			refreshDayLabels(cal, false);
		}
	}
	
	private void refreshDayLabels(Calendar calendar, boolean currentMonth) {
		int maxDate = calendar.getActualMaximum(Calendar.DATE);
		int today = calendar.get(Calendar.DATE);
		
		calendar.set(Calendar.DATE, 1);
		int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		for( JLabel[] ll : dayLabels) {
			for( JLabel l : ll ) {
				l.setText("");
			}
		}
		
		for( int day=1; day<=maxDate; day++ ) {
			getDayLabel( day, firstDayOfWeek ).setText(Integer.toString(day));
		}

		for( int i=0; i<dayLabels.length; i++) {
			for( int j=0; j<dayLabels[i].length; j++) {
				JLabel l = dayLabels[i][j];
				l.setBackground(DAY_BG);
				l.setForeground(DAY_FG);
				l.setBorder(DAY_BORDER);
				
				if( l.getText().length() != 0 ) {
					l.setVisible(true);
				} else {
					l.setVisible(false);
				}
			}
		}
		
		if( currentMonth ) {
			JLabel l = getDayLabel( today, firstDayOfWeek );
			l.setBackground(TODAY_BG);
			l.setForeground(TODAY_FG);
			l.setBorder(TODAY_BORDER);
		}
	}
	
	private JLabel getDayLabel( int day , int firstDayOfWeek ) {
		return dayLabels[(day + firstDayOfWeek - 2)/7][(day + firstDayOfWeek - 2)%7];
	}
}