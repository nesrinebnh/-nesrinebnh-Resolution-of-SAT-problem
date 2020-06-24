package GAPackage;

public class Clause {
    public int[] solution;
    public int evaluation;
    public boolean satifaction=false;
    public int size;
    public int[] solutionOptimal;

    public Clause(int size) {
        solution = new int[size];
        this.size = size;
    }
    public Clause(int size,int[] _solution, int fitness){
        solution = new int[size];
        solution = _solution;
        evaluation = fitness;
        this.size = size;
    }

    public String toString(){
    	String str= ""+evaluation+"|";
    	for(int i=0;i<size;i++){
    		str+=solution[i]+"\t";
    	}
    	return str;

    }
}
