package org.femosaa.operator;

import java.util.Comparator;
import java.util.HashMap;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.BinaryTournament2;
import jmetal.operators.selection.Selection;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.comparators.DominanceComparator;

public class InvalidityAwareBinaryTournament2 extends Selection{
	
	  
	  /**
	   * dominance_ store the <code>Comparator</code> for check dominance_
	   */
	  private Comparator dominance_;
	  
	  /**
	   * a_ stores a permutation of the solutions in the solutionSet used
	   */
	  private int a_[];
	  
	  /**
	   *  index_ stores the actual index for selection
	   */
	  private int index_ = 0;

	public InvalidityAwareBinaryTournament2(HashMap<String, Object> parameters) {
		super(parameters);
		dominance_ = new DominanceComparator(); 
	}

	  /**
	  * Performs the operation
	  * @param object Object representing a SolutionSet
	  * @return the selected solution
	  */
	public Object execute(Object object) {
		SolutionSet[] populations = (SolutionSet[]) object;

		SolutionSet population = populations[0];
		SolutionSet invalidPopulation = populations[1];
		if (index_ == 0) // Create the permutation
		{
			a_ = (new jmetal.util.PermutationUtility())
					.intPermutation(population.size());
		}

		int caseFlag = 0;

		Solution solution1 = null, solution2 = null;
		if (a_[index_] < population.size()
				&& a_[index_ + 1] < population.size()) {
			solution1 = population.get(a_[index_]);
			solution2 = population.get(a_[index_ + 1]);
			caseFlag = 0;
		} else if (a_[index_] >= population.size()
				&& a_[index_ + 1] >= population.size()) {
			solution1 = invalidPopulation.get(a_[index_]);
			solution2 = invalidPopulation.get(a_[index_ + 1]);
			caseFlag = 1;
		} else if (a_[index_] < population.size()
				&& a_[index_ + 1] >= population.size()) {
			solution1 = population.get(a_[index_]);
			solution2 = invalidPopulation.get(a_[index_ + 1]);
			caseFlag = 2;
		} else if (a_[index_] >= population.size()
				&& a_[index_ + 1] < population.size()) {
			solution1 = invalidPopulation.get(a_[index_]);
			solution2 = population.get(a_[index_ + 1]);
			caseFlag = 3;
		}

		// TODO put probability for caseFlag = 1.

		index_ = (index_ + 2) % population.size();

		if (caseFlag == 0) {

			int flag = dominance_.compare(solution1, solution2);
			if (flag == -1)
				return solution1;
			else if (flag == 1)
				return solution2;
			else if (solution1.getCrowdingDistance() > solution2
					.getCrowdingDistance())
				return solution1;
			else if (solution2.getCrowdingDistance() > solution1
					.getCrowdingDistance())
				return solution2;
			else if (PseudoRandom.randDouble() < 0.5)
				return solution1;
			else
				return solution2;
		} else {
			if (PseudoRandom.randDouble() < 0.5)
				return solution1;
			else
				return solution2;
		}
	} // execute

	
	  @Override
	  public Object execute_5(Object object) throws JMException {
	    return null;
	  }

	  @Override
	  public Object execute_6(Object object) throws JMException {
	    return null;
	  }
}
