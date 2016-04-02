package jmetal.problems;

import java.io.IOException;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public abstract class SASAlgorithmAdaptor {

	public Solution execute(SASSolutionInstantiator factory, int[][] vars,
			int numberOfObjectives_, int numberOfConstraints_)
			throws JMException, SecurityException, IOException,
			ClassNotFoundException {
		SolutionSet pareto_front = findParetoFront(factory, vars,
				numberOfObjectives_, numberOfConstraints_);

		if (ApproachType.MOEAD_STM_D_K.equals(getName())) {
			pareto_front = filterRequirementsAfterEvolution(pareto_front);
		} else if (ApproachType.NSGAII.equals(getName())) {
			pareto_front = filterRequirementsAfterEvolution(pareto_front);
			pareto_front = correctDependencyAfterEvolution(pareto_front);
		} else if (ApproachType.GP.equals(getName())) {
			pareto_front = filterRequirementsAfterEvolution(pareto_front);
			pareto_front = correctDependencyAfterEvolution(pareto_front);
		} else if (ApproachType.MIP.equals(getName())) {
			pareto_front = filterRequirementsAfterEvolution(pareto_front);
			pareto_front = correctDependencyAfterEvolution(pareto_front);
		}

		return findSoleSolutionAfterEvolution(pareto_front);
	}

	protected abstract SolutionSet findParetoFront(
			SASSolutionInstantiator factory, int[][] vars,
			int numberOfObjectives_, int numberOfConstraints_)
			throws JMException, SecurityException, IOException,
			ClassNotFoundException;

	protected abstract ApproachType getName();

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
		MOEAD_STM_D_K, NSGAII, GP, MIP
	}
}
