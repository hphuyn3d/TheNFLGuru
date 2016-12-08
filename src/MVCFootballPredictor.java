public class MVCFootballPredictor {
	/**
	 * Main to run the application
	 * @author Alex Bindas & Hung Huynh
	 * Date: December 7, 2016
	 */
	public static void main(String[] args) {

		try {
			FootballView view = new FootballView();
			FootballModel model = new FootballModel();
			FootballController controller = new FootballController(view, model);
			view.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
