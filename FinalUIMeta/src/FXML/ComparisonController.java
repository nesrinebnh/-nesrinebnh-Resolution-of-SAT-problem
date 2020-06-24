package FXML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import BSOPackage.bso.BSO;
import BSOPackage.main.ClausesSet;
import GAPackage.Application;
import GAPackage.Clause;
import GAPackage.IHM;
import PSOPackage.pso;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComparisonController implements Initializable , IHM{
	 @FXML
    private TextField maxiterGA,popsize,maxiterPSO, cr, mu, C1, C2, W, bees, flip;

    @FXML
    private TextField NumberOfParticles, VMAX, maxiterBSO, chances, search;

    @FXML
    private TextArea warning;

    @FXML
    private BarChart<String, Number> bar;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button go, choose;

    @FXML
    private Label timeGA, timePSO, timeBSO;

    @FXML
    private Label fitnessGA, fitnessPSO, fitnessBSO;

    @FXML
    private GridPane comp;

    int size, lenght;


    String path = "";

    int fitGA = 0, fitPSO = 0, fitBSO = 0;
    String tGA, tPSO, tBSO;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.bar.setVisible(false);
		this.warning.setVisible(true);
		this.comp.setVisible(false);
		this.go.setDisable(true);

	}
	@FXML
	public void close(){
		Stage stage = (Stage) this.go.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void dialog(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText("Setting parameters Example");
		alert.setContentText("Max iteration: 1000\nPop size: 70\nCr: 70\nMu: 30\n\nMax iteration: 160"
				+ "\nnumber of particles: 100\nC1: 1.1\nC2: 1.0\nW:0.5\nVMAX:35\n\n"
				+ "Maxiter: 3000\nnumber of bees: 20\nflip: 70\nmax chances:7\nlocal iterations:35\n");
		alert.showAndWait();
	}

	@FXML
	public void choosingFile() throws IOException{
    	int size_solution=75;
	    double seconds;
	    FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if(file!=null){
        	Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle(null);
    		alert.setHeaderText("File selection");
    		alert.setContentText(file.getPath()+" is selected");
    		alert.showAndWait();
        	this.path = file.getPath();
        	this.extractData();
        	this.go.setDisable(false);

        }else{
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("File selection");
        	alert.setHeaderText("File");
        	alert.setContentText("You have cancled the selection operation");
        	alert.showAndWait();
        }
	}

	@FXML
	public void GAAction() throws Exception{
		this.switchFunctionMain("GA");
	}

	@FXML
	public void PSOAction() throws Exception{
		this.switchFunctionMain("PSO");
	}

	@FXML
	public void BSOAction() throws Exception{
		this.switchFunctionMain("BSO");
	}

	public void switchFunctionMain(String type) throws Exception{
		Stage rootStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/FXML/Home.fxml"));
		Parent root;
		try {
			root = loader.load();
			Scene scene = new Scene(root);
			HomeController buy = loader.getController();
	        buy.user(type);
	        rootStage.setScene(scene);
	        rootStage.show();
	        rootStage.setResizable(false);
	        this.bar.getScene().getWindow().hide();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void extract() throws Exception{
		this.go.setDisable(true);
		this.choose.setDisable(true);

		if(this.maxiterGA.getText().isEmpty() || this.popsize.getText().isEmpty()
				|| this.cr.getText().isEmpty() || this.mu.getText().isEmpty()
				|| this.maxiterPSO.getText().isEmpty() || this.NumberOfParticles.getText().isEmpty()
				|| this.C1.getText().isEmpty() || this.C2.getText().isEmpty()
				|| this.W.getText().isEmpty() || this.maxiterBSO.getText().isEmpty()
				|| this.bees.getText().isEmpty() || this.flip.getText().isEmpty()
				|| this.chances.getText().isEmpty() || this.search.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Parameter input");
        	alert.setContentText("Seems you did not insert a parameter. Please fill all the parameters");
        	alert.showAndWait();
		}else{
			int maxiterGA = Integer.valueOf(this.maxiterGA.getText());
			int popsize = Integer.valueOf(this.popsize.getText());
			int cr = Integer.valueOf(this.cr.getText());
			int mu = Integer.valueOf(this.mu.getText());

			/**PSO*/
			int maxiterPSO = Integer.valueOf(this.maxiterPSO.getText());
			int particles = Integer.valueOf(this.NumberOfParticles.getText());
			double c1 = Double.valueOf(this.C1.getText());
			double c2 = Double.valueOf(this.C2.getText());
			double w = Double.valueOf(this.W.getText());

			/** BSO*/
			int maxiterBSO = Integer.valueOf(this.maxiterBSO.getText());
			int bees = Integer.valueOf(this.bees.getText());
			int flip = Integer.valueOf(this.flip.getText());
			int chances = Integer.valueOf(this.chances.getText());
			int search  = Integer.valueOf(this.search.getText());

			long startTime = 0,endTime = 0;
			startTime = System.currentTimeMillis();

			Application app = new Application(this);
			app.run(this.size,lenght,maxiterGA, popsize,mu, cr,this.path);

			endTime = System.currentTimeMillis();

			tGA = String.valueOf((endTime-startTime)/1000F);

			startTime = System.currentTimeMillis();

			pso p=new pso(this.path,size, lenght,particles,maxiterPSO,c1, c2, w);
		    Object[] obj = p.RUN_PSO();

		    endTime = System.currentTimeMillis();

			tPSO = String.valueOf((endTime-startTime)/1000F);
		    this.fitPSO = (int) obj[0];

		    startTime = System.currentTimeMillis();
		    ClausesSet clset = new ClausesSet(this.path);
		    //Object[] sol =	BSO.searchBSO(clset,31, 121, 11,6, 1000, false);
		   // int flip, int numBees, int maxChances, int numLocalSearch, int maxIterations
			obj =	BSO.searchBSO(clset,flip, bees, chances, search,maxiterBSO, false);

			endTime = System.currentTimeMillis();

			tBSO = String.valueOf((endTime-startTime)/1000F);
			this.fitBSO = (int) obj[4];

			XYChart.Series seriesFIt = new XYChart.Series();
			XYChart.Series seriesTime = new XYChart.Series();
			double mean = 0;
			seriesFIt.setName("fitness");
			seriesTime.setName("time");

			seriesFIt.getData().add(new XYChart.Data<>("GA",this.fitGA));
			seriesTime.getData().add(new XYChart.Data<>("GA",Double.valueOf(tGA)));

			seriesFIt.getData().add(new XYChart.Data<>("PSO",this.fitPSO));
			seriesTime.getData().add(new XYChart.Data<>("PSO",Double.valueOf(tPSO)));

			seriesFIt.getData().add(new XYChart.Data<>("BSO",this.fitBSO));
			seriesTime.getData().add(new XYChart.Data<>("BSO",Double.valueOf(tBSO)));

			xAxis.setLabel("Algorithm");
	        yAxis.setLabel("F(x)");
	        bar.setAnimated(false);
			bar.getData().addAll(seriesFIt, seriesTime);



			this.timeBSO.setText(tBSO);
			this.timeGA.setText(tGA);
			this.timePSO.setText(tPSO);

			this.fitnessBSO.setText(String.valueOf(fitBSO));
			this.fitnessGA.setText(String.valueOf(fitGA));
			this.fitnessPSO.setText(String.valueOf(fitPSO));



			this.bar.setVisible(true);
			this.warning.setVisible(false);
			this.comp.setVisible(true);
			this.go.setDisable(false);
			this.choose.setDisable(false);

		}



	}

	private void extractData() throws FileNotFoundException{



        try {
            File myObj = new File(this.path);
            Scanner myReader = new Scanner(myObj);
            int linenumber = 1;
            String[] data = null ;
            String data2 = "";
            while (myReader.hasNextLine() && linenumber != 9) {
            	linenumber++;
            	data = myReader.nextLine().split(" ");
            }

            this.size = Integer.valueOf(data[2]);
            this.lenght = Integer.valueOf(data[4]);

            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

	}

	@Override
	public void diplayMessage(Clause msg, Integer generation, ArrayList<Number> fitnessList, ArrayList<String> time,
			String avg) throws ParseException {
		// TODO Auto-generated method stub
		this.fitGA = msg.evaluation;
	}

}
