package GAPackage;

import java.io.IOException;
import java.util.ArrayList;

public abstract class GA extends Algorithm {
    private ArrayList<Clause> population ;

    private int pop_size = 0;
    private final static int SIZE = 75;
    private int generation=0;

    @Override
    public final void solve(IGA pb, IHM ihm) throws Exception{
        super.solve(pb,ihm);
        population = new ArrayList<>();
        /** generate initial population and evaluate its fitness*/
        pop_size = setPopSize();
        //System.out.println("generating random solutions");
        for(int i=0;i<pop_size;i++){
            Clause newSol = problem.solutionAleatoire();
            population.add(newSol);
            if(i==0) problem.setSolutionoptimal(newSol);
        }


        Long start = System.nanoTime();
        while(stopCriteria(problem, start)){
        	population = selection(population, problem);
            population= croisement(population);
            population = mutation(population);
            /*for(Clause c:population){
            	System.out.println(c.evaluation);
            }*/
            generation++;
            incrementer();

        }



        sendResults(problem.getSolutionOptimal(),ihm,generation);
        //System.out.print(problem.getSolutionOptimal().evaluation);
        //sendResults(problem.getSolutionOptimal(),ihm,generation);
    }



    protected abstract int setPopSize();
    protected abstract boolean stopCriteria(IGA problem, Long start);
    protected abstract ArrayList<Clause> croisement(ArrayList<Clause> population) throws Exception;
    protected abstract ArrayList<Clause> selection(ArrayList<Clause> population, IGA problem);
    protected abstract ArrayList<Clause> mutation(ArrayList<Clause> population);
    protected abstract Integer incrementer();



}
