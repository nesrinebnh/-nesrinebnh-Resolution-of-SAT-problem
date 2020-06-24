package GAPackage;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

public class GASatisfaction extends GA {
    private int pop_size;
    private int cr;
    private int mr;
    protected int nbIterations = 0;
    private int NB_MAX_ITERATION;
    private boolean sat=false;
    ArrayList<Number> fitnessList = new ArrayList<Number>();
    ArrayList<String> time = new ArrayList<String>();
    double average = 0.0;


    private int NUMBER_CLAUSE;
    int size;
    TextArea fileView;



    public GASatisfaction(int _popsize, int maxiter, int size, int lenght){
        this.pop_size = _popsize;
        this.NB_MAX_ITERATION =maxiter;
        nbIterations = 0;
        this.size = size;
        this.NUMBER_CLAUSE = lenght;

    }

    @Override
    protected int setPopSize() {
        return this.pop_size-1;
    }

    @Override
    protected boolean stopCriteria(IGA problem, Long start) {
        return nbIterations < this.NB_MAX_ITERATION &&  sat == false /*&& (((System.nanoTime()-start)/ 1000000000)<60)*/;
    }

    @Override
    protected ArrayList<Clause> croisement(ArrayList<Clause> populations) throws Exception {
    	/** sort the population array*/
    	/*Collections.sort(populations,(Clause s1, Clause s2)->{
            if(s1.evaluation > s2.evaluation) return -1;
            else if(s1.evaluation == s2.evaluation) return 0;
            else return 1;
        });
    	int num = (int) Math.round((pop_size*cr)/100);*/
    	/** select Parents*/
    	/*ArrayList<Clause> parents = new ArrayList();
    	for(int i=0; i<num; i++){
    		parents.add(populations.get(i));
    	}*/
    	Random r = new Random();
    	ArrayList<Clause> children = new ArrayList<Clause>();
    	/** start the crossover*/
    	for(int i=0; i<pop_size;i++){
    		int crossPoint1 = r.nextInt(size-1);
    		//int crossPoint2 = r.nextInt(size-1);
    		int indexParent1 = r.nextInt(populations.size()-1);
    		int indexParent2 = r.nextInt(populations.size()-1);
    		Clause kid = new Clause(size);
    		Clause kid1 = new Clause(size);
    		Clause Parent1 = populations.get(indexParent1);
    		Clause Parent2 = populations.get(indexParent2);

    		for(int j=0; j<crossPoint1; j++){
    			kid.solution[j] = Parent1.solution[j];
    			kid1.solution[j] = Parent2.solution[j];
    		}

    		for(int j=crossPoint1; j<Parent1.solution.length; j++){
    			kid.solution[j] = Parent2.solution[j];
    			kid1.solution[j] = Parent1.solution[j];
    		}

    		/*if(crossPoint1<crossPoint2){
    			for(int j=0;j<crossPoint1;j++){
    				kid.solution[j] = Parent1.solution[j];
    			}
    			for(int j=crossPoint1; j< crossPoint2; j++){
    				kid.solution[j] = Parent2.solution[j];
    			}
    			for(int j=crossPoint2; j<size; j++){
    				kid.solution[j] = Parent1.solution[j];
    			}
    		}else{
    			for(int j=0;j<crossPoint2;j++){
    				kid.solution[j] = Parent1.solution[j];
    			}
    			for(int j=crossPoint2; j< crossPoint1; j++){
    				kid.solution[j] = Parent2.solution[j];
    			}
    			for(int j=crossPoint1; j<size; j++){
    				kid.solution[j] = Parent1.solution[j];
    			}
    		}*/
    		kid.evaluation = problem.Evaluation(kid.solution);
    		if(kid.evaluation == NUMBER_CLAUSE){
    			sat = true;
    		}
    		children.add(kid);
    		if(kid.evaluation == NUMBER_CLAUSE) sat = true;
    	}
    	//int mutationRate = (int) Math.round((this.mr*pop_size)/100);
    	/** mutation*/
    	/*for(Clause c:children){
    		int mutation = r.nextInt();
    		if(mutationRate<=mutation){
    			int index = r.nextInt(size-1);
    			c.solution[index] = c.solution[index]*-1;
    		}
    		if(c.evaluation == NUMBER_CLAUSE){
    			sat = true;
    			problem.setSolutionoptimalexiste(true, c);
    			problem.setSolutionoptimal(c);
    		}
    	}*/

    	/** survive*/
    	return children;
    }

    @Override
    protected ArrayList<Clause> selection(ArrayList<Clause> populations, IGA problem) {
    	/** sort the population array*/
    	Collections.sort(populations,(Clause s1, Clause s2)->{
            if(s1.evaluation > s2.evaluation) return -1;
            else if(s1.evaluation == s2.evaluation) return 0;
            else return 1;
        });
    	for(Clause c : populations){
    		if(c.evaluation == NUMBER_CLAUSE){
    			sat=true;
    			problem.setSolutionoptimal(c);
    			problem.setSolutionoptimalexiste(true, c);
    		}else{
    			if(problem.getSolutionOptimal().evaluation<c.evaluation){
    				problem.setSolutionoptimal(c);
    			}
    		}
    	}
    	//System.out.println(populations.get(0).evaluation);
    	this.fitnessList.add(populations.get(0).evaluation);
    	this.time.add(String.valueOf(System.nanoTime()/ 1000000000));
    	int num = (int) Math.round((pop_size*cr)/100);
    	/** select Parents*/
    	ArrayList<Clause> parents = new ArrayList();
    	for(int i=0; i<num; i++){
    		parents.add(populations.get(i));
    	}
    	return parents;
    }

    @Override
    protected ArrayList<Clause> mutation(ArrayList<Clause> populations) {
    	int mutationRate = (int) Math.round((this.mr*pop_size)/100);
    	/** mutation*/
    	Random r = new Random();
    	for(Clause c:populations){
    		int mutation = r.nextInt();
    		if(mutationRate<=mutation){
    			int index = r.nextInt(size-1);
    			c.solution[index] = c.solution[index]*-1;
    		}
    		if(c.evaluation == NUMBER_CLAUSE){
    			sat = true;
    			problem.setSolutionoptimalexiste(true, c);
    			problem.setSolutionoptimal(c);
    		}else{
    			if(problem.getSolutionOptimal().evaluation<c.evaluation){
    				problem.setSolutionoptimal(c);
    			}
    		}
    	}

    	return populations;
    }

    @Override
    protected Integer incrementer() {
        nbIterations++;
        return  nbIterations;
    }



    @Override
    protected void setParameters(int[] parameters) {
        this.cr = parameters[0];
        this.mr = parameters[1];


    }

	@Override
	protected void sendResults(Clause optimalSolution, IHM ihm, Integer generation) throws ParseException {
		double avg = this.average/this.nbIterations;
		ihm.diplayMessage(optimalSolution, generation, fitnessList, time, String.valueOf(avg));

	}

	public void diplayToWindow(ArrayList<Clause> population){
        problem.setSolutionoptimal(population.get(0));

	}


}
