package jmetal.problems.SAS01;

import jmetal.core.*;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.*;


/**
 * Created by keli on 16/2/17.
 */
public class SAS_PROBLEM extends Problem {

    /**
     * Constructor
     */
    public SAS_PROBLEM(String solutionType) throws ClassNotFoundException {
        this(solutionType, 20); // 20 variables by default
    }

    public SAS_PROBLEM(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
        numberOfVariables_   = numberOfVariables.intValue();
        numberOfObjectives_  = 2;
        numberOfConstraints_ = 0;
        problemName_         = "SAS_PROBLEM";

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];

        // Establishes upper and lower limits for the variables
        for (int var = 0; var < numberOfVariables_; var++)
        {
            lowerLimit_[var] = 0.0;
            upperLimit_[var] = 1.0;
        } // for


        if (solutionType.compareTo("Integer") == 0)
            solutionType_ = new IntSolutionType(this);
        else {
            System.out.println("Error: solution type " + solutionType + " invalid") ;
            System.exit(-1) ;
        }
    }

    /**
     * Evaluates a solution.
     * @param solution The solution to evaluate.
     * @throws JMException
     */
    public void evaluate(Solution solution) throws JMException {
        XInt x = new XInt(solution) ;

        double [] f = new double[numberOfObjectives_]  ;

        // objective function????????


        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
    } // evaluate

}
