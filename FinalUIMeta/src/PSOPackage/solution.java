package PSOPackage;


import java.util.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.*;
import java.lang.*;
import java.io.*;


public class solution implements Comparable<solution>{
	protected int[] position;
	protected int evaluation=0;
	protected boolean satifaction=false;
	protected float velocity;
	protected int[] pbest;
	protected int size,lenght;
	String cnf;

	public int compareTo(solution comparesolution) {
		int compareevaluation = ((solution) comparesolution).evaluation;
		return compareevaluation-this.evaluation;
	}

	public solution(int lenght,int size, String cnf){
		this.size=size;
		this.lenght = lenght;
		position=new int[size];
		this.cnf = cnf;
	}


	public int[] solutionaleatoir(){
		int[] literal=new int[this.size];
	    int rand = 0;
	    for(int i=0;i<this.size;i++){
	    	rand = ThreadLocalRandom.current().nextInt(0,2);
	        if(rand == 1) literal[i]=i+1 ;else literal[i]=-(i+1);
	    }
	    return literal;
    }

	public int evaluation(int [] solution) throws IOException{
		File temp =new File(cnf);
        Scanner file= new Scanner(temp);
        String line =file.nextLine();
        boolean sat=true;
        int linenumber=0;
        int nbrclausesatif=0;
        while(line != null){
            linenumber++;
            String[] table=line.split(" ");
            if(linenumber > 8 && linenumber<=333) {
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
        }
        return nbrclausesatif;
	}

	  public void printintsolutiont(){
	  for(int i=0;i<this.size;i++) System.out.print(this.position[i] +"\t ");

	  }

      public void printevaluation() {
    	  System.out.println("\n\n evaluation :: "+this.evaluation);
      }

}








