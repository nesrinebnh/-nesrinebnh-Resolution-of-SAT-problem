package PSOPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Collections;


public class pso {

	private int[] Gbest;
	private int gbestevaluation;
	float gbestVelocity;
    solution[] swarm;
	private double w,c1,c2;
	private double r1,r2;
	private int max_iter;
	private int pop_size;
	private int clausenumber;
	private int size;
    private boolean solutionoptimalexiste;
	private solution solutionoptimal;
	String cnf;
	ArrayList<Number> fitnessList = new ArrayList<Number>();
    ArrayList<String> time = new ArrayList<String>();
    int lenght;

	public pso(String cnf, int size,int lenght,int pop_size, int max_iter,double c1,double c2, double w) {
		this.pop_size=pop_size;
		this.lenght = lenght;
		this.cnf = cnf;
		this.max_iter = max_iter;
		this.gbestevaluation=0;
		this.size=size;
		this.Gbest=new int[this.size];
		this.swarm=new solution[this.pop_size];
		this.c1=c1;
		this.c2=c2;
		this.w=w;
		this.r1=(float)(Math.random()*(((this.size-1)-0)+1))+0;
		this.r2=(float)(Math.random()*(((this.size-1)-0)+1))+0;
		for(int i=0;i<pop_size;i++){
			solution o= new solution(this.lenght,this.size,cnf);
			int[] liter=o.solutionaleatoir();
			o.position = liter;
			o.velocity = (int)(Math.random()*(((this.size-1)-0)+1))+0;
			o.pbest = liter;
			try {
				o.evaluation = o.evaluation(o.position);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.swarm[i]=o;
		}
		this.updateGbest();
	}


	public int distance(int[] x,int[] y) {
		int cpt=0;
	   	for(int i=0; i<x.length;i++)
    		if(x[i] != y[i] ) cpt++;

		return cpt;
	}


	public int updatevelocity(int indice) {
		 float part1,part2,part3;
		 int new_velocity;
	     part1=(float)(this.w*this.swarm[indice].velocity);
	     part2=(float)(this.c1*this.r1*(distance(this.swarm[indice].pbest,this.swarm[indice].position)));
	     part3=(float)(this.c2*this.r2*(distance(this.Gbest,this.swarm[indice].position)));
	     new_velocity= (int)((part1+part2+part3)%this.size);
	     return new_velocity;
		}

	public int[] updateposition(int velocity,int indice) {
		int[] before=this.swarm[indice].position;
		int[] after=before;

		for(int i=0; i< velocity;i++) {
			after[i]=before[i]*(-1);
		}
		return after;
	}

	public void updateparticle(int indice_particle) {
		int old_evaluation=this.swarm[indice_particle].evaluation;
		int new_velocity=this.updatevelocity(indice_particle);
	    int[] new_position=this.updateposition(new_velocity, indice_particle);
        this.swarm[indice_particle].velocity = new_velocity;
	    this.swarm[indice_particle].position = new_position;
	    try {
	    	this.swarm[indice_particle].evaluation = this.swarm[indice_particle].evaluation(this.swarm[indice_particle].position);
		} catch (IOException e) {
			e.printStackTrace();
		}

	    try {
	       if(this.swarm[indice_particle].evaluation >  this.swarm[indice_particle].evaluation(this.swarm[indice_particle].pbest)) ;
	       this.swarm[indice_particle].pbest = this.swarm[indice_particle].position;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void updateGbest() {
		solution[] solution=this.swarm;
		for(int i=0;i<this.pop_size;i++) {
			if(solution[i].evaluation > this.gbestevaluation) {
				this.Gbest = solution[i].position;
				this.gbestevaluation = solution[i].evaluation;
				this.gbestVelocity = solution[i].velocity;
				if(this.gbestevaluation == this.clausenumber) {
					this.solutionoptimalexiste = true;
					this.solutionoptimal = solution[i];
				}
			}
		}
	}

	public Object[] RUN_PSO() {
		ArrayList<Integer> array = new ArrayList<>();
		int i=0;
		while (i< this.max_iter && this.solutionoptimalexiste == false ) {
			for(int j=0; j< this.pop_size;j++){

				this.updateparticle(j);
			}
	        this.updateGbest();
	        this.fitnessList.add(this.gbestevaluation);
	        this.time.add(String.valueOf(System.nanoTime()/ 1000000000));
	        array.add(this.gbestevaluation);
			i++;
		}
		Object[]  obj = new Object[]{this.gbestevaluation, this.Gbest, i, fitnessList, time};
		return obj;
	}

}
