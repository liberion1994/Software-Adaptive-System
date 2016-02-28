package jmetal.operators.crossover;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import jmetal.core.*;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.*;
import jmetal.metaheuristics.moead.SASSolution;
import jmetal.metaheuristics.moead.SASSolutionInstantiator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;

public class UniformCrossoverSAS extends Crossover {
	
	private SASSolutionInstantiator factory;
	
	public UniformCrossoverSAS(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters
				.get("jmetal.metaheuristics.moead.SASSolutionInstantiator") == null) {
			System.err
					.print("jmetal.metaheuristics.moead.SASSolutionInstantiator has not been instantiated in UniformCrossoverSAS\n");
		} else {
			factory = (SASSolutionInstantiator) parameters
					.get("jmetal.metaheuristics.moead.SASSolutionInstantiator");
		}
		if (parameters.get("probability") != null)
			crossoverProbability_ = (Double) parameters.get("probability");
	}

	/**
	 * Valid solution types to apply this operator
	 */
	private static List VALID_TYPES = Arrays.asList(BinarySolutionType.class, IntSolutionType.class);
	
	private Double crossoverProbability_ = null;
	
	/**
	 * Perform the crossover operation
	 */
	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
		Solution[] offSpring = new Solution[2];
		if (parent1 instanceof SASSolution) {
			offSpring[0] = factory.getSolution(parent1);
			offSpring[1] = factory.getSolution(parent2);
		} else {
			offSpring[0] = new Solution(parent1);
			offSpring[1] = new Solution(parent2);
		}
		
		int valueX1;
		int valueX2;

		/**
		 * Assuming that there is a many-to-many relationship between 
		 * dependent variable and main variable, the workflow here is:
		 * 
		 *  
		 * If the value of main variable is swapped, then so do the values for all dependent variables.
		 * However, this rules is disregarded if the values of all main variables of the parents are equivalent.
		 * */
		
		// 0 = unchanged, 1 otherwise
		int[] temp = new int[parent1.getDecisionVariables().length];
		
		if (parent1 instanceof SASSolution) {
			for (int i = 0; i < parent1.numberOfVariables(); i++) {
				temp[i] = 0;
				if (PseudoRandom.randDouble() < crossoverProbability_) {
					int[] main = ((SASSolution)parent1).getMainVariablesByDependentVariable(i);
					
					// meaning that the ith variable is a dependent variable.
					if (main.length != 0) {
						boolean isAllowed = true;
						for (int j = 0; j < main.length; j ++) {
							if (temp[j] == 0 && parent1.getDecisionVariables()[j].getValue() != 
								parent2.getDecisionVariables()[j].getValue()) {
								isAllowed = false;
								break;
							}
						}
						
						if (!isAllowed) {
							continue;
						}
					}
					
					temp[i] = 1;
					
					valueX1 = (int) parent1.getDecisionVariables()[i]
							.getValue();
					valueX2 = (int) parent2.getDecisionVariables()[i]
							.getValue();
					offSpring[0].getDecisionVariables()[i].setValue(valueX2);
					offSpring[1].getDecisionVariables()[i].setValue(valueX1);
				}
			}
		} else {

			for (int i = 0; i < parent1.numberOfVariables(); i++) {
				if (PseudoRandom.randDouble() < crossoverProbability_) {
					valueX1 = (int) parent1.getDecisionVariables()[i]
							.getValue();
					valueX2 = (int) parent2.getDecisionVariables()[i]
							.getValue();
					offSpring[0].getDecisionVariables()[i].setValue(valueX2);
					offSpring[1].getDecisionVariables()[i].setValue(valueX1);
				}
			}
		}
		try {
			
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("UniformCrossoverSAS.doCrossover: Cannot perfom " + "UniformCrossoverSAS");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doCrossover()");
		}
		
		return offSpring;
	}
	
	

	@Override
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		if (!(VALID_TYPES.contains(parents[0].getType().getClass())
				&& VALID_TYPES.contains(parents[1].getType().getClass()))) {

			Configuration.logger_.severe("UniformCrossoverSAS.execute: the solutions "
					+ "are not of the right type. The type should be 'Binary' or 'Int', but " + parents[0].getType()
					+ " and " + parents[1].getType() + " are obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		if (parents.length < 2) {
			Configuration.logger_.severe("UniformCrossoverSAS.execute: operator " + "needs two parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}

		Solution[] offSpring;
		offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);

		// -> Update the offSpring solutions
		for (int i = 0; i < offSpring.length; i++) {
			offSpring[i].setCrowdingDistance(0.0);
			offSpring[i].setRank(0);
		}
		
		return offSpring;
	}

	@Override
	public Object execute_5(Object object) throws JMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object execute_6(Object object) throws JMException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
