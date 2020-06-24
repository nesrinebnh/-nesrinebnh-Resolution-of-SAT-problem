package GAPackage;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

public class Application  {
	static IHM ihm;
	public Application(IHM _ih){
		this.ihm = _ih;
	}


    private void lunchAlgorithm(IGA pb,int popsize,int maxiter,int cr, int mr, int size, int lenght) throws Exception{
        Algorithm algo;
        algo = new GASatisfaction(popsize,maxiter, size, lenght);
        int[] parameters = new int[]{cr,mr};
        algo.setParameters(parameters);
        algo.solve(pb, ihm);

    }

    public void run(int size, int lenght, int popsize,int maxiter,int cr, int mr, String cnf) throws Exception{
        Sat pb = new Sat(size, lenght, cnf);
        lunchAlgorithm(pb,popsize, maxiter, cr,mr, size, lenght);

    }



}
