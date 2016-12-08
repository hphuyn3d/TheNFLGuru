import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
/**
 * Controller class for the NFL Guru
 * @author Alex Bindas & Hung Huynh
 * Date: December 7, 2016
 */
public class FootballController {
	private FootballView view;
	private FootballModel model;
	private Object homeTeam;
	private Object awayTeam;

	/**
	 * FootballController constructor
	 * @param FootballView view
	 * @param FootballModel model
	 */
	public FootballController(FootballView view, FootballModel model) {
		this.view = view;
		this.model = model;
		this.view.setVisible(true);
		this.view.addPrecitionButtonListener(new PredicitonButtonListener());
		this.view.addHomeTeamSelectionListener(new HomeTeamSelectionListener());
		this.view.addAwayTeamSelectionListener(new AwayTeamSelectionListener());
	}

	/**
	 * Action listener for the Prediction Button
	 */
	private class PredicitonButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			model.intialize();
			homeTeam = getHomeTeam();
			awayTeam = getAwayTeam();
			getAwayTeam();
			
			//Calls each method in the model that will run the algorithm
			if(!homeTeam.equals(awayTeam)){
				model.comparePoints(homeTeam, awayTeam);
				model.comparePassing(homeTeam, awayTeam);
				model.compareRushing(homeTeam, awayTeam);

				Object winningTeam = model.getResult(homeTeam, awayTeam);
				
				//The home team wins message will pop up
				if (winningTeam == homeTeam){
					JOptionPane.showMessageDialog(view, "The winner is... the " + homeTeam.toString() + " !!!");
				}
				//The away team wins message will pop up
				else{
					JOptionPane.showMessageDialog(view, "The winner is..." + awayTeam.toString() + " !!!");
				}
			}
			//You cannot predict the outcome of the same team, so error pops up
			else if(homeTeam.equals(awayTeam)){
				JOptionPane.showMessageDialog(view, "Please select 2 different teams.");
			}
		}
	}

	/**
	 * Action listener for the home team drop down menu selection
	 */
	private class HomeTeamSelectionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			view.updateHomeTeamImage(getHomeTeam().toString());
		}
	}		

	/**
	 * Action listener for the away team drop down menu selection
	 */
	private class AwayTeamSelectionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			view.updateAwayTeamImage(getAwayTeam().toString());
		}
	}

	/**
	 * Gets the home team selected
	 * @return homeTeam
	 */
	public Object getHomeTeam(){
		this.homeTeam = view.getHomeTeamSelection();
		return homeTeam;
	}

	/**
	 * Gets the away team selected
	 * @return awayTeam
	 */
	public Object getAwayTeam(){
		this.awayTeam = view.getAwayTeamSelection();
		return awayTeam;
	}

}

