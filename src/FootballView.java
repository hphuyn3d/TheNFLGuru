import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * View class for the NFL Guru
 * @author Alex Bindas & Hung Huynh
 * Date: December 7, 2016
 */
public class FootballView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> homeDropDown = new JComboBox<String>();
	private JComboBox<String> awayDropDown = new JComboBox<String>();
	private JButton btnPredict = new JButton("Click to Predict the Winner");
	private JLabel homeIcon = new JLabel();
	private JLabel awayIcon = new JLabel();
	private JLabel homeLabel = new JLabel("  Home Team");
	private JLabel awayLabel = new JLabel("Away Team  ");
	protected FootballModel model; 
	public Connection conn;
	private final JSplitPane splitPaneIcon = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, homeIcon, awayIcon);
	private final JSplitPane splitPaneLabel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, homeLabel, awayLabel);


	/**
	 * Creates the application.
	 * Initialize the contents of the frame.
	 */
	public FootballView() {
		try {
			JPanel footballPanel = new JPanel();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(900, 500);
			this.setResizable(true);
			this.setLocationRelativeTo(null);
			this.setTitle("The NFL Guru");
			footballPanel.setLayout(new BorderLayout(0, 0));
			footballPanel.add(btnPredict, BorderLayout.SOUTH);
			footballPanel.add(homeDropDown, BorderLayout.WEST);
			footballPanel.add(awayDropDown, BorderLayout.EAST);

			//Adds the icons for each team
			splitPaneIcon.setLeftComponent(homeIcon);
			splitPaneIcon.setRightComponent(awayIcon);
			splitPaneIcon.setResizeWeight(.5d);
			splitPaneIcon.setBorder(null);
			footballPanel.add(splitPaneIcon, BorderLayout.CENTER);

			//adds away & home labels with specific fonts
			awayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			awayLabel.setFont(new Font("Arial Black", Font.BOLD, 32));
			homeLabel.setHorizontalAlignment(SwingConstants.LEFT);
			homeLabel.setFont(new Font("Arial Black", Font.BOLD, 32));
			splitPaneLabel.setLeftComponent(homeLabel);
			splitPaneLabel.setRightComponent(awayLabel);
			splitPaneLabel.setResizeWeight(.5d);
			footballPanel.add(splitPaneLabel, BorderLayout.NORTH);

			getContentPane().add(footballPanel);

			//sets up the connection to the mySQL database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Football?useSSL=false", "root", "password");

			//SQL statement to retrieve all team names
			Statement stmt = conn.createStatement();
			String allTeams = "SELECT team_name FROM offensivepoints";
			ResultSet rset = stmt.executeQuery(allTeams);

			while(rset.next()){
				homeDropDown.addItem(rset.getString("team_name"));
				awayDropDown.addItem(rset.getString("team_name"));	
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the home team selected
	 * @return homeTeam
	 */
	public Object getHomeTeamSelection(){
		return homeDropDown.getSelectedItem();
	}

	/**
	 * Gets the away team selected
	 * @return awayTeam
	 */
	public Object getAwayTeamSelection(){
		return awayDropDown.getSelectedItem();
	}

	/**
	 * Method to add the action listener to the predict button
	 */
	void addPrecitionButtonListener(ActionListener predicitonListener) {
		btnPredict.addActionListener(predicitonListener);
	}

	/**
	 * Method to add the action listener to the homeTeam drop down selection
	 */
	void addHomeTeamSelectionListener(ActionListener predicitonListener) {
		homeDropDown.addActionListener(predicitonListener);
	}

	/**
	 * Method to add the action listener to the awayTeam drop down selection
	 */
	void addAwayTeamSelectionListener(ActionListener predicitonListener) {
		awayDropDown.addActionListener(predicitonListener);
	}

	/**
	 * Method to update the home team image every time a team is selected
	 * @param String teamName
	 */
	protected void updateHomeTeamImage(String s) {
		ImageIcon icon = createImageIcon("/icon/" + s + ".png");
		if(homeDropDown.getSelectedItem() != null ){
			homeIcon.setIcon(icon);
		}
		else if(homeIcon == null) {
			homeIcon.setText("Image not found");
		}
	}

	/**
	 * Method to update the away team image every time a team is selected
	 * @param String teamName
	 */
	protected void updateAwayTeamImage(String s) {
		ImageIcon icon = createImageIcon("/icon/" + s + ".png");
		if(awayDropDown.getSelectedItem() != null ){
			awayIcon.setIcon(icon);
		}
		else if(awayIcon == null) {
			awayIcon.setText("Image not found");
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FootballView.class.getResource(path);
		if (imgURL != null)
			return new ImageIcon(imgURL);
		else
			System.err.println("Couldn't find file: " + path);
			return null;
	}

}
