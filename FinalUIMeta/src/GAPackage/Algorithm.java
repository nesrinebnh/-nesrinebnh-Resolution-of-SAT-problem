package GAPackage;

import java.io.IOException;
import java.text.ParseException;

public abstract class Algorithm {
    protected IGA problem;
    protected IHM ihm;

    public void solve(IGA pb, IHM _ihm) throws Exception{
        this.problem = pb;
        this.ihm = _ihm;
    }

    protected abstract void sendResults(Clause optimalSolution, IHM ihm,Integer generation) throws ParseException;

    protected abstract void setParameters(int[] parameters);



}
