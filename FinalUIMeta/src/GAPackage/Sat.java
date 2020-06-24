package GAPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Sat implements IGA {
    boolean satisafaction=false;
    Clause OptimalSolution = null;
    int size,lenght;

    String cnf = "";
    public Sat(int size, int lenght,String cnf){
    	this.cnf = cnf;
    	this.size = size;
    	this.lenght = lenght;
    }


    @Override
    public Clause solutionAleatoire() throws Exception {
        int[] chromosomnes=new int[size];
        int rand = 0;
        for(int i=0;i<size;i++){
            rand = ThreadLocalRandom.current().nextInt(0,2);
            if(rand == 1) chromosomnes[i]=i+1 ;else chromosomnes[i]=-(i+1);
        }

        int evaluation  = this.Evaluation(chromosomnes);
        return new Clause(size,chromosomnes, evaluation);
    }

    @Override
    public int Evaluation(int [] solution) throws Exception{
    	File temp =new File(cnf);
        Scanner file= new Scanner(temp);
        String line =file.nextLine();
        boolean sat=true;
        int linenumber=0;
        int nbrclausesatif=0;

        while(line != null){
            linenumber++;
            String[] table=line.split(" ");
            if(linenumber > 8 && linenumber<=333) {    // test on only clause
                //System.out.println(line);
            	boolean b=false;
                int indice;
                int[] liter=solution;
                int[] clause=new int[4];
                int j=0;
                for(int i=0;i<table.length;i++){
                	if(!table[i].isEmpty()){
                		clause[j]=Integer.parseInt(table[i]);
                		j++;
                	}

                }
                for(int i=0;i<3;i++){
                    if(clause[i] < 0)indice= -(clause[i]);else indice=clause[i];
                    if(liter[indice-1] == clause[i]){ b = true;}
                }
                if(b== true) {nbrclausesatif++; }
                else {sat=false;}
            }
            if(linenumber== (this.lenght+8)) return nbrclausesatif;
            line =file.nextLine();
            //System.out.println(line);
        }




        return nbrclausesatif;
      }





    @Override
    public boolean getIsSatisfaction() {
        return this.satisafaction;
    }

    @Override
    public boolean getSolutionoptimalexiste() {
        return false;
    }

    @Override
    public void setSolutionoptimalexiste(boolean val, Clause sol) {
        sol.satifaction = true;
    }

    @Override
    public void setSolutionoptimal(Clause sol) {
        this.OptimalSolution = sol;
    }

	@Override
	public Clause getSolutionOptimal() {
		// TODO Auto-generated method stub
		return this.OptimalSolution;
	}


}
