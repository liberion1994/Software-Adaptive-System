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
import jmetal.problems.SASSolution;
import jmetal.problems.SASSolutionInstantiator;
import jmetal.problems.SASSolutionType;
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
	private static List VALID_TYPES = Arrays.asList(BinarySolutionType.class, IntSolutionType.class, SASSolutionType.class);
	
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

		if (parent1 instanceof SASSolution) {
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
			((SASSolution)parent1).crossoverWithDependency(parent1, parent2, offSpring[0], offSpring[1]);
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
