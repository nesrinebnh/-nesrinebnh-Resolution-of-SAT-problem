package GAPackage;

import java.io.IOException;

public interface IGA {
    Clause solutionAleatoire() throws Exception;
    int Evaluation(int[] solution) throws Exception;
    boolean getIsSatisfaction();
    boolean getSolutionoptimalexiste();
    void setSolutionoptimalexiste(boolean val, Clause sol);
    void setSolutionoptimal(Clause sol);
    Clause getSolutionOptimal();
}
