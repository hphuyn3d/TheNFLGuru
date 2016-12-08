import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for the NFL Guru
 * @author Alex Bindas & Hung Huynh
 * Date: December 7, 2016
 */
public class FootballModel {
	
	/**
	 * Connection to the database
	 */
	private Connection conn;
	/**
	 * String query to access home offensive stats
	 */
	private String homeOffenseQuery;
	/**
	 * String query to access home defensive stats
	 */
	private String homeDefenseQuery;
	/**
	 * String query to access away offensive stats
	 */
	private String awayOffenseQuery;
	/**
	 * String query to access away offensive stats
	 */
	private String awayDefenseQuery;
	/**
	 * Holds the total sum of home calculations from all compare methods
	 */
	private Double totalHomeCalc;
	/**
	 * Holds the total sum of away calculations from all compare methods
	 */
	private Double totalAwayCalc;
	
	//Lists that contains the stats pulled from the mySQL database
	List<Double> awayOffenseList = new ArrayList<Double>();
	List<Double> homeOffenseList = new ArrayList<Double>();
	List<Double> awayDefenseList = new ArrayList<Double>();
	List<Double> homeDefenseList = new ArrayList<Double>();

	/**
	 * Connects to the mySQL data base
	 */
	public void connectDataBase(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Football?useSSL=false", "root", "password");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 *Puts each team (home and away) into two separate lists accessed from the database
	 */
	public void createResultLists(){
		//result sets for each home/away (defensive and offensive stats)
		ResultSet homeOffenseResults;
		ResultSet awayOffenseResults;
		ResultSet homeDefenseResults;
		ResultSet awayDefenseResults;
		
		//creates a new statement for each home/away (defensive and offensive stats)
		Statement homeOffenseStatement;
		Statement awayOffenseStatement;
		Statement homeDefenseStatement;
		Statement awayDefenseStatement;
		
		try {
			connectDataBase();
			homeOffenseStatement = conn.createStatement();
			awayOffenseStatement = conn.createStatement();
			homeDefenseStatement = conn.createStatement();
			awayDefenseStatement = conn.createStatement();

			homeOffenseResults = homeOffenseStatement.executeQuery(homeOffenseQuery);
			awayOffenseResults = awayOffenseStatement.executeQuery(awayOffenseQuery);
			homeDefenseResults = homeDefenseStatement.executeQuery(homeDefenseQuery);
			awayDefenseResults = awayDefenseStatement.executeQuery(awayDefenseQuery);
			
			//loops through and adds the stats from all 5 years into a list
			while(homeOffenseResults.next()){
				for (int i = 5; i>=1; i--)
					homeOffenseList.add(homeOffenseResults.getDouble("year_201" + i));
			}
			while(awayOffenseResults.next()){
				for (int i = 5; i>=1; i--)
					awayOffenseList.add(awayOffenseResults.getDouble("year_201" + i));
			}
			while(homeDefenseResults.next()){
				for (int i = 5; i>=1; i--)
					homeDefenseList.add(homeDefenseResults.getDouble("year_201" + i));
			}
			while(awayDefenseResults.next()){
				for (int i = 5; i>=1; i--)
					awayDefenseList.add(awayDefenseResults.getDouble("year_201" + i));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the average of any list
	 * @param <Double> list
	 */
	public Double calcAverage(List<Double> list){
		Double sum = 0.0;

		for (int i=0; i < list.size(); i++)
			sum = sum + list.get(i);
		Double average = sum / list.size();
		return average;
	}

	/**
	 * Runs the Football's Pythagorean Theorem (Created by: Bill James)
	 */
	public void runAlgorithm(){
		Double homeCalculation = (Math.pow(calcAverage(homeOffenseList), 2.37)/(Math.pow(calcAverage(homeOffenseList), 2.37) +
				(Math.pow(calcAverage(homeDefenseList), 2.37)))) * 16 + totalHomeCalc;

		Double awayCalculation = (Math.pow(calcAverage(awayOffenseList), 2.37)/(Math.pow(calcAverage(awayOffenseList), 2.37) + 
				(Math.pow(calcAverage(awayDefenseList), 2.37)))) * 16 + totalAwayCalc;

		setTotalHomeCalc(homeCalculation);
		setTotalAwayCalc(awayCalculation);
	}

	/**
	 * Method that compares the points to the points allowed for each team
	 * @param homeTeam
	 * @param awayTeam
	 */
	public void comparePoints(Object homeTeam, Object awayTeam){
		homeOffenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM offensivepoints WHERE team_name = " 
				+ "\"" + homeTeam.toString() + "\"";

		awayOffenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM offensivepoints WHERE team_name = " 
				+ "\"" + awayTeam.toString() + "\"";

		homeDefenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM defensivepoints WHERE team_name = " 
				+ "\"" + homeTeam.toString() + "\"";

		awayDefenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM defensivepoints WHERE team_name = " 
				+ "\"" + awayTeam.toString() + "\"";

		createResultLists();
		runAlgorithm();
	} 

	/**
	 * Method that compares the rushing yards to the rushing yards allowed for each team
	 * @param homeTeam
	 * @param awayTeam
	 */
	public void compareRushing(Object homeTeam, Object awayTeam){
		homeOffenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM offensiverushing WHERE team_name = " 
				+ "\"" + homeTeam.toString() + "\"";

		awayOffenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM offensiverushing WHERE team_name = " 
				+ "\"" + awayTeam.toString() + "\"";

		homeDefenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM defensiverushing WHERE team_name = " 
				+ "\"" + homeTeam.toString() + "\"";

		awayDefenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM defensiverushing WHERE team_name = " 
				+ "\"" + awayTeam.toString() + "\"";

		createResultLists();
		runAlgorithm();
	}

	/**
	 * Method that compares the passing yards to the passing yards allowed for each team
	 * @param homeTeam
	 * @param awayTeam
	 */
	public void comparePassing(Object homeTeam, Object awayTeam){
		homeOffenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM offensivepassing WHERE team_name = " 
				+ "\"" + homeTeam.toString() + "\"";

		awayOffenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM offensivepassing WHERE team_name = " 
				+ "\"" + awayTeam.toString() + "\"";

		homeDefenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM defensivepassing WHERE team_name = " 
				+ "\"" + homeTeam.toString() + "\"";

		awayDefenseQuery = "SELECT team_name, year_2011, year_2012, year_2013, year_2014, year_2015"
				+ " FROM defensivepassing WHERE team_name = " 
				+ "\"" + awayTeam.toString() + "\"";		

		createResultLists();
		runAlgorithm();
	} 

	/**
	 * Method to get the result & prints the home/away team's total calculation to the console
	 * @return Winning team
	 */
	public Object getResult(Object homeTeam, Object awayTeam) {
		Object winningTeam;
		
		Double homeTeamAdvantage = (getTotalHomeCalc() * 0.073) + getTotalHomeCalc();
		setTotalHomeCalc(homeTeamAdvantage);

		if(getTotalHomeCalc() > getTotalAwayCalc()){
			winningTeam = homeTeam;
		}
		else{
			winningTeam = awayTeam;
		}
		System.out.println("Home:" + getTotalHomeCalc() + " " + homeTeam);
		System.out.println("Away:" + getTotalAwayCalc() + " " + awayTeam);
		System.out.println("------------------------------------");
		return winningTeam;
	}

	/**
	 * Sets the totalHomeCalc calculation
	 */
	public void setTotalHomeCalc(Double totalHome){
		this.totalHomeCalc = totalHome;
	}

	/**
	 * Sets the totalAwayCalc calculation
	 */
	public void setTotalAwayCalc(Double totalAway){
		this.totalAwayCalc = totalAway;
	}

	/**
	 * @return totalHomeCalc
	 */
	public Double getTotalHomeCalc(){
		return this.totalHomeCalc;
	}

	/**
	 * @return totalAwayCalc
	 */
	public Double getTotalAwayCalc(){
		return this.totalAwayCalc;
	}

	/**
	 * Intializes each variable to reset after you click the Predict button
	 */
	public void intialize() {
		setTotalHomeCalc(0.0);
		setTotalAwayCalc(0.0);
		homeOffenseList.clear();
		homeDefenseList.clear();
		awayOffenseList.clear();
		awayDefenseList.clear();
	}
}

