package FXML;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

public class StatController implements Initializable{
	@FXML
    private LineChart<Number, Number> line;

    @FXML
    private TextField mean, variance, std;

    @FXML
    private NumberAxis xAxis ;

    @FXML
    private NumberAxis yAxis ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void user(ArrayList<Number> fitnessList, ArrayList<String> time) throws ParseException{



		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		double mean = 0;
        for(int i =0;i<fitnessList.size();i++){
        	series.getData().add(new XYChart.Data<Number, Number>(NumberFormat.getInstance().parse(time.get(i)), fitnessList.get(i)));
        	mean +=  fitnessList.get(i).doubleValue();
        }



        mean = mean / fitnessList.size();
        double variance =0, sum =0;
        for(int i =0;i<fitnessList.size();i++){
        	double diff = fitnessList.get(i).doubleValue()-mean;
        	sum += Math.pow(diff, 2);
        }

        this.mean.setText(String.valueOf(mean));
        variance = sum/(fitnessList.size()-1);
        this.variance.setText(String.valueOf(variance));
        variance = Math.sqrt(variance);
        this.std.setText(String.valueOf(variance));
        xAxis.setLabel("Time(nano)");
        xAxis.setForceZeroInRange(false);
        yAxis.setLabel("F(x)");
        yAxis.setForceZeroInRange(false);
        //line.getData().add(series);
        line.setCreateSymbols(false);
        line.setAnimated(true);
		line.getData().addAll(series);


	}

}
