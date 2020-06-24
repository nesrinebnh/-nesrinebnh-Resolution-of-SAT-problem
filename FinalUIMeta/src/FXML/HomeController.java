package FXML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import BSOPackage.bso.BSO;
import BSOPackage.main.ClausesSet;
import BSOPackage.main.Solution;
import GAPackage.Application;
import GAPackage.Clause;
import GAPackage.IHM;
import GAPackage.Sat;
import PSOPackage.pso;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController implements Initializable,IHM {

	@FXML
    private TilePane tile;

    @FXML
    private Label name;

    @FXML
    private Button Choose;

    @FXML
    private Button search;

    @FXML
    private GridPane grid, grid2;

    @FXML
    private GridPane cnfGrid, pathGrid, resultsGrid, optimalGrid;

    @FXML
    private TextArea cnf;


    @FXML
    private TextArea path;

    @FXML
    private TextArea solution;

    @FXML
    private TextField fitness;

    @FXML
    private TextField time;

    @FXML
    private TextField rate;

    private String pathFile="",type="BSO";

    private int size = 0, lenght = 0;

    private String[] ga = new String[]{"max iteration", "pop size","mu","cr"};
    private String[] pso = new String[]{"number of particles","max iteration","C1","C2","W","VMAX"};
    private String[] bso = new String[]{"max iteration", "number of bees", "flip","maximum chances","local search"};
    private ArrayList<TextField> array;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    ArrayList<Double> parameters = new ArrayList<>();


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.search.setDisable(true);
		grid2.setVisible(false);
		this.name.setText(type+"'s algorithm");
		tile.setVisible(false);
		this.pathGrid.setVisible(false);
		this.optimalGrid.setVisible(false);
		this.resultsGrid.setVisible(false);
		this.cnfGrid.setVisible(false);
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

	@FXML
	public void comparison(){
		Stage rootStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/FXML/Comparison.fxml"));
		Parent root;
		try {
			root = loader.load();
			Scene scene = new Scene(root);
	        rootStage.setScene(scene);
	        rootStage.show();
	        rootStage.setResizable(false);
	        this.cnf.getScene().getWindow().hide();;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void close(){
		Stage stage = (Stage) this.Choose.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void dialog(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText("Setting parameters Example");
		if(type.equals("GA"))
			alert.setContentText("Max iteration: 1000\nPop size: 70\nCr: 70\n Mu: 30\n");
		else if(type.equals("PSO"))
			alert.setContentText("Max iteration: 160\n number of particles: 100\nC1: 1.1\nC2: 1.0\nW:0.5\nVMAX:35\n");
		else
			alert.setContentText("Maxiter: 3000\nnumber of bees: 20\nflip: 70\nmax chances:7\nlocal iterations:35\n");

		alert.showAndWait();
	}

	@FXML
	public void choosingFile() throws IOException{
    	int size_solution=75;
	    double seconds;
	    FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
        	Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle(null);
    		alert.setHeaderText("File selection");
    		alert.setContentText(file.getPath()+" is selected");
    		alert.showAndWait();
        	this.pathFile = file.getPath();
        	this.extractData();
        	this.cnfGrid.setVisible(true);
        	this.pathGrid.setVisible(true);
        	this.path.setText("Welcome\n"
        			+ dtf.format(LocalDateTime.now()) +" The file "+this.pathFile+" is selected\n\n");
        	this.path.appendText(dtf.format(LocalDateTime.now())+ " The program is reading the file ...\n\n");
        	this.cnf.setText(file.getPath());
        	File temp =new File(this.pathFile);
            Scanner file1= new Scanner(temp);
            String line =file1.nextLine();
            while(!line.isEmpty()){
            	this.cnf.appendText(line+"\n");
                line =file1.nextLine();
            }
            this.path.appendText(dtf.format(LocalDateTime.now())+" The lecture of the file is done\n\n ");
            this.path.appendText(dtf.format(LocalDateTime.now())+" The program is waiting for an input from the user (The setting of the parametters)\n\n");
            grid2.setVisible(true);
            tile.setVisible(true);
            tile.setHgap(10);
    		tile.setVgap(10);
    		tile.getChildren().clear();
    		GridPane pane = new GridPane();
    		pane.setAlignment(Pos.CENTER);
    		pane.setPadding(new Insets(2,2,2,2));
    		array = new ArrayList<>();
    		if(type.equals("GA")){
        		pane.setHgap(ga.length*4);
    			pane.setVgap(ga.length*4);
                for(int i=0 ; i < ga.length; i++){
                	Label label = new Label(ga[i]);
                	label.setStyle("-fx-font-size:14;-fx-text-fill: #00468b;");
                    TextField txt = new TextField();
        			txt.setId(ga[i]);
        			txt.setStyle("-fx-background-color: #8b0046; -fx-border-color:  #00468b; -fx-text-fill: #ffffff;-fx-pref-width: 150;");
        			//order.add("field");
        			array.add(txt);
        			pane.add(label, 0, i);
        			pane.add(txt, 1, i);
                }
    		}else if(type.equals("PSO")){
        		pane.setHgap(pso.length*2);
    			pane.setVgap(pso.length*2);
                for(int i=0 ; i < pso.length; i++){
                	Label label = new Label(pso[i]);
                	label.setStyle("-fx-font-size:14;-fx-text-fill: #00468b;");
                    TextField txt = new TextField();
        			txt.setId(pso[i]);
        			txt.setStyle("-fx-background-color: #8b0046; -fx-border-color:  #00468b; -fx-text-fill: #ffffff;-fx-pref-width: 150;");
        			array.add(txt);
        			pane.add(label, 0, i);
        			pane.add(txt, 1, i);
                }
    		}else{
    			pane.setHgap(bso.length*2);
    			pane.setVgap(bso.length*2);
                for(int i=0 ; i < bso.length; i++){
                	Label label = new Label(bso[i]);
                	label.setStyle("-fx-font-size:14;-fx-text-fill: #00468b;");
                    TextField txt = new TextField();
        			txt.setId(bso[i]);
        			txt.setStyle("-fx-background-color: #8b0046; -fx-border-color:  #00468b; -fx-text-fill: #ffffff;-fx-pref-width: 150;");
        			//order.add("field");
        			array.add(txt);
        			pane.add(label, 0, i);
        			pane.add(txt, 1, i);
                }
    		}

    		tile.getChildren().add(pane);
    		tile.setAlignment(Pos.CENTER);

    		this.search.setDisable(false);


        }else{
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("File selection");
        	alert.setHeaderText("File");
        	alert.setContentText("You have cancled the selection operation");
        	alert.showAndWait();
        }
	}

	@FXML
	public void searchAction() throws Exception{
		System.out.println("Search Action");
		long startTime = 0,endTime = 0;
		boolean error = false;
		this.path.appendText(dtf.format(LocalDateTime.now())+" The user has clicked on search button\n\n");
		this.path.appendText(dtf.format(LocalDateTime.now())+" The program is checking if all the parameters are inserted\n\n");
		for(int i=0; i<this.array.size();i++){
			TextField field = array.get(i);
			if(field.getText().isEmpty()){
				this.path.appendText(dtf.format(LocalDateTime.now())+" The program ended due to an error \n\n");
				Alert alert = new Alert(AlertType.ERROR);
	        	alert.setTitle("Setting parameters");
	        	alert.setHeaderText("The parameter " +field.getId());
	        	alert.setContentText("Seems that the parameter is null. Please insert a value\n\n");
	        	alert.showAndWait();
	        	error = true;
			}
			else{
				this.path.appendText(dtf.format(LocalDateTime.now())+" The parameter "+field.getId()+" is setted to "+field.getText()+"\n\n");
				parameters.add(Double.valueOf(field.getText()));
			}
		}
		if(!error){
			System.out.println("no error");
			this.path.appendText(dtf.format(LocalDateTime.now())+" The algorithm is lunched.. looking for a solution\n\n");

			startTime = System.currentTimeMillis();

			if(type.equals("GA")){
				Application app = new Application(this);
	    		app.run(this.size,lenght,parameters.get(1).intValue(), parameters.get(0).intValue(),parameters.get(3).intValue(), parameters.get(2).intValue(),this.pathFile);
			}else if(type.equals("PSO")){
				pso p=new pso(this.pathFile,this.size, lenght,parameters.get(0).intValue(),parameters.get(1).intValue(),parameters.get(2), parameters.get(3), parameters.get(4));
			    Object[] obj = p.RUN_PSO();


			    this.optimalGrid.setVisible(true);
	    		int[] solution = (int[]) obj[1];
	    		String str="";
	    		for(int i =0 ; i< solution.length; i++){
	    			if(solution[i]>0) str += "1 ";
	    			else str += "0 ";
	    		}
	            this.solution.setText(str);
	            path.appendText(dtf.format(LocalDateTime.now())+" The solution is displayed in the result zone.\n\n");


	            this.resultsGrid.setVisible(true);
	            int evaluation = (int) obj[0];
	            int generation = (int) obj[2];

	            this.fitness.setText(String.valueOf(evaluation));
	            this.rate.setText(String.valueOf(generation));
	            this.switchFunction((ArrayList<Number>) obj[3], (ArrayList<String>) obj[4]);
			}else{
				ClausesSet clset = new ClausesSet(this.pathFile);
				Object[] sol =	BSO.searchBSO(clset,31, 121, 11,6, 1000, false);
				this.optimalGrid.setVisible(true);
				Solution solution = (Solution) sol[0];
	    		String str="";
	    		for(int i =0 ; i< solution.getSolution().size(); i++){
	    			str += solution.getSolution().get(i)+" ";
	    		}
	            this.solution.setText(str);
	            path.appendText(dtf.format(LocalDateTime.now())+" The solution is displayed in the result zone.\n\n");

				this.resultsGrid.setVisible(true);
				int evaluation = (int) sol[4];
	            int generation = (int) sol[3];



	            this.fitness.setText(String.valueOf(evaluation));
	            this.rate.setText(String.valueOf(generation));
	            this.switchFunction((ArrayList<Number>) sol[1], (ArrayList<String>) sol[2]);
			}

    		endTime = System.currentTimeMillis();

    		this.time.setText(String.valueOf((endTime-startTime)/1000F));



    		this.path.appendText(dtf.format(LocalDateTime.now())+" The algorithm has ended in"+((startTime-endTime)/1000F) +"\n\n");



		}

	}

	@Override
	public void diplayMessage(Clause msg, Integer generation, ArrayList<Number> fitnessList, ArrayList<String> time,
			String avg) throws ParseException {
		// TODO Auto-generated method stub
		/*this.finalFitness.setText(String.valueOf(msg.evaluation));
		this.generation.setText(String.valueOf(generation));
		this.avg.setText(avg);*/
        String str="\n";
        //lineChart.getData().clear();
        for(int i=0;i<msg.size;i++){
        	str+= msg.solution[i]+"\t";
        }
        this.optimalGrid.setVisible(true);
        this.solution.setText(str);
        path.appendText(dtf.format(LocalDateTime.now())+" The solution is displayed in the result zone.\n\n");


        this.resultsGrid.setVisible(true);
        this.fitness.setText(String.valueOf(msg.evaluation));
        this.rate.setText(String.valueOf(generation));

        switchFunction(fitnessList, time);

	}

	public void switchFunction(ArrayList<Number> fitnessList, ArrayList<String> time) throws ParseException{
		Stage rootStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/FXML/Stat.fxml"));
		Parent root;
		try {
			root = loader.load();
			Scene scene = new Scene(root);
			StatController buy = loader.getController();
	        buy.user(fitnessList, time);
	        rootStage.setScene(scene);
	        rootStage.show();
	        rootStage.setResizable(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	        this.Choose.getScene().getWindow().hide();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void user(String type){
		this.type = type;
		this.name.setText(type+"'s algorithm");

	}

	private void extractData() throws FileNotFoundException{



        try {
            File myObj = new File(this.pathFile);
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


}
