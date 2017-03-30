package org.femosaa.core;

import java.io.IOException;
import java.util.Iterator;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public abstract class SASAlgorithmAdaptor {
	
	protected static double MUTATION_RATE = 0.1;
	protected static double CROSSOVER_RATE = 0.9;
	private static final boolean PRINT_SOLUTIONS = false;
	// This can be changed within SSASE
	public static boolean isPreserveInvalidSolution = false;
	
	public Solution execute(SASSolutionInstantiator factory, int[][] vars,
			int numberOfObjectives_, int numberOfConstraints_)
			throws JMException, SecurityException, IOException,
			ClassNotFoundException {
		SolutionSet pareto_front = findParetoFront(factory, vars,
				numberOfObjectives_, numberOfConstraints_);
		
		//logDependencyAfterEvolution(getAllFoundSolutions());
		
		SolutionSet result = correctDependencyAfterEvolution(pareto_front);
		// Means no valid solutions.
		if(result.size() == 0) {
			for (int i = 0; i < pareto_front.size(); i++) {
				// Make sure the solution does not violate dependency.
				// The dependencyMap is temporarily reset within the function.
				 ((SASSolution)pareto_front.get(i)).correctDependency();
				 double[] f = ((SASSolution)pareto_front.get(i)).getObjectiveValuesFromIndexValue();
					
					for (int k = 0; k < f.length ; k ++) {
						((SASSolution)pareto_front.get(i)).setObjective(k, f[k]);
					}
			}
			
		} else {
			pareto_front = result;
		}
		//pareto_front = doRanking(pareto_front);
		pareto_front = filterRequirementsAfterEvolution(pareto_front);
		if(PRINT_SOLUTIONS) {
			printSolutions(pareto_front, numberOfObjectives_);
		}		
		pareto_front = doRanking(pareto_front);
		if(PRINT_SOLUTIONS) {
			printSolutions(pareto_front, numberOfObjectives_);
		}
		printParetoFront(pareto_front);
		//System.out.print("Pareto front size after dependency and requirement check: " + pareto_front.size() + "\n");
		return findSoleSolutionAfterEvolution(pareto_front);
	}

	protected abstract SolutionSet findParetoFront(
			SASSolutionInstantiator factory, int[][] vars,
			int numberOfObjectives_, int numberOfConstraints_)
			throws JMException, SecurityException, IOException,
			ClassNotFoundException;

	protected abstract ApproachType getName();
	
	protected abstract SolutionSet doRanking(SolutionSet pf);
	
	

	/**
	 * This can return knee point or just randomly selected one.
	 * @param pareto_front
	 * @return
	 */
	protected abstract Solution findSoleSolutionAfterEvolution(
			SolutionSet pareto_front);

	
	/**
	 * Used mainly by the existing approach.
	 * @param pareto_front
	 * @return
	 */
	protected SolutionSet correctDependencyAfterEvolution(
			SolutionSet pareto_front) {
		return pareto_front;
	}
	
	protected void printParetoFront(SolutionSet pareto_front) {
    }
	
	
	protected void printSolutions(SolutionSet pareto_front, int numberOfObjectives_) {
		System.out.print("Total number : " + pareto_front.size() + "\n");
		System.out.print("=========== solutions' objective values ===========\n");
		Iterator itr = pareto_front.iterator();
		while(itr.hasNext()) {
			Solution s = (Solution)itr.next();
			String str = "(";
			for(int i = 0; i < numberOfObjectives_; i++) {
				str = (i +1 == numberOfObjectives_)? str + s.getObjective(i) : 
					str + s.getObjective(i) + ",";
			}
			System.out.print(str+ ")\n");
		}
    }
	
//	protected void logDependencyAfterEvolution(
//			SolutionSet pareto_front_without_ranking) {
//	}
	/**
	 * If the requirements (constraints) are considered outside the evolution,
	 * then it should be processed here.
	 * @param pareto_front
	 * @return
	 */
	protected SolutionSet filterRequirementsAfterEvolution(
			SolutionSet pareto_front) {
		return pareto_front;
	}

	protected enum ApproachType {
		MOEAD_STM_D_K, NSGAII, GP, MIP, IBEA
	}
}
