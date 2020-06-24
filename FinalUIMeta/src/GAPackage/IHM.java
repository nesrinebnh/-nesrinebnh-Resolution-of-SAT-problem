package GAPackage;

import java.text.ParseException;
import java.util.ArrayList;

public interface IHM {
    void diplayMessage(Clause msg, Integer generation, ArrayList<Number> fitnessList, ArrayList<String> time, String avg) throws ParseException;
}
