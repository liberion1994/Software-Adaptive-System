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
	  private int valid_a_[];
	  /**
	   *  index_ stores the actual index for selection
	   */
	  private int index_ = 0;
	  private int valid_index_ = 0;
	public InvalidityAwareBinaryTournament2(HashMap<String, Object> parameters) {
		super(parameters);
		dominance_ = new DominanceComparator(); 
	}

	
	public Object execute2(Object object) {
		SolutionSet population = ((SolutionSet[]) object)[1];
		       
		        
		    Solution solution1,solution2;
		    solution1 = population.get(0);
		    solution2 = population.get(1);
		        
		    
			
		    
		   // index_ = (index_ + 2) % population.size();
		        
//		    int flag = dominance_.compare(solution1,solution2);
//		    if (flag == -1)
//		      return solution1;
//		    else if (flag == 1)
//		      return solution2;
//		    else if (solution1.getCrowdingDistance() > solution2.getCrowdingDistance())
//		      return solution1;
//		    else if (solution2.getCrowdingDistance() > solution1.getCrowdingDistance())
//		      return solution2;
//		    else
		      if (PseudoRandom.randDouble()<0.5)
		        return solution1;
		      else
		        return solution2;   
	}
	
	public Object execute(Object object) {
		SolutionSet population = ((SolutionSet[]) object)[1];
		    if (index_ == 0|| (a_ != null && a_.length != population.size())) //Create the permutation
		    {
		      a_= (new jmetal.util.PermutationUtility()).intPermutation(population.size());
		      index_ = 0;
		    }
		            
		        
		    Solution solution1,solution2;
		    solution1 = population.get(a_[index_]);
		    solution2 = (index_+1) >= a_.length? solution1 : population.get(a_[index_+1]);
		        
		    
		    if((index_ + 2) == (population.size()-1)) {
				index_ = 1;
			} else {
				index_ = (index_ + 2) % (population.size());
			}
			
		    
//		    double p1 = ((org.femosaa.core.SASSolution)solution1).getProbabilityToBeNaturallyRepaired();
//		    double p2 = ((org.femosaa.core.SASSolution)solution2).getProbabilityToBeNaturallyRepaired();
//		    
//		    return p1 > p2? solution1 : solution2;
		   // index_ = (index_ + 2) % population.size();
		        
//		    int flag = dominance_.compare(solution1,solution2);
//		    if (flag == -1)
//		      return solution1;
//		    else if (flag == 1)
//		      return solution2;
//		    else if (solution1.getCrowdingDistance() > solution2.getCrowdingDistance())
//		      return solution1;
//		    else if (solution2.getCrowdingDistance() > solution1.getCrowdingDistance())
//		      return solution2;
//		    else
		      if (PseudoRandom.randDouble()<0.5)
		        return solution1;
		      else
		        return solution2;   
	}
	  /**
	  * Performs the operation
	  * @param object Object representing a SolutionSet
	  * @return the selected solution
	  */
	public Object execute1(Object object) {
		SolutionSet[] populations = (SolutionSet[]) object;

		SolutionSet population = populations[0];
		SolutionSet invalidPopulation = populations[1];
		//System.out.print("before " + (index_ + 1) + " : " + (a_ == null ? 0 : a_.length)+"\n");
		if (index_ == 0 || (a_ != null && a_.length != population.size()+invalidPopulation.size())) // Create the permutation
		{
			a_ = (new jmetal.util.PermutationUtility())
					.intPermutation(population.size()+invalidPopulation.size());
			index_ = 0;
		}
		SolutionSet newPopulation = population.union(invalidPopulation);
		int caseFlag = 0;

		
		Solution solution1 = null, solution2 = null;
		if (a_[index_] < population.size()
				&& a_[index_ + 1] < population.size()) {
			//solution1 = population.get(a_[index_]);
			//solution2 = population.get(a_[index_ + 1]);
			caseFlag = 0;
		} else if (a_[index_] >= population.size()
				&& a_[index_ + 1] >= population.size()) {
			//solution1 = invalidPopulation.get(a_[index_]);
			//solution2 = invalidPopulation.get(a_[index_ + 1]);
			caseFlag = 1;
		} else if (a_[index_] < population.size()
				&& a_[index_ + 1] >= population.size()) {
			//solution1 = population.get(a_[index_]);
			//solution2 = invalidPopulation.get(a_[index_ + 1]);
			caseFlag = 2;
		} else if (a_[index_] >= population.size()
				&& a_[index_ + 1] < population.size()) {
			//solution1 = invalidPopulation.get(a_[index_]);
			//solution2 = population.get(a_[index_ + 1]);
			caseFlag = 3;
		}
		
		solution1 = newPopulation.get(a_[index_]);
		solution2 = newPopulation.get(a_[index_ + 1]);

		// TODO put probability for caseFlag = 1.
		//System.out.print("after " + (index_ + 1) + " : " + a_.length+"\n");
		if((index_ + 2) == (population.size()+invalidPopulation.size()-1)) {
			index_ = 1;
		} else {
			index_ = (index_ + 2) % (population.size()+invalidPopulation.size());
		}
		
		
        //System.out.print("index_ " + index_ + ", caseFlag " + caseFlag + " : " +  population.size()+" : " +invalidPopulation.size()+"\n");
		//System.out.print(solution1 + " :  " + solution2 + "\n");
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
//		    double p1 = ((org.femosaa.core.SASSolution)solution1).getProbabilityToBeNaturallyRepaired();
//		    double p2 = ((org.femosaa.core.SASSolution)solution2).getProbabilityToBeNaturallyRepaired();
//		    
//		    return p1 > p2? solution1 : solution2;
			if (PseudoRandom.randDouble() < 0.5)
				return solution1;
			else
				return solution2;
		}
	} // execute

	
	
	 public Object executeValid(Object object)    
	  {
	    SolutionSet population = (SolutionSet)object;
	    if (valid_index_ == 0) //Create the permutation
	    {
	    	valid_a_= (new jmetal.util.PermutationUtility()).intPermutation(population.size());
	    }
	            
	        
	    Solution solution1,solution2;
	    solution1 = population.get(valid_a_[valid_index_]);
	    solution2 = population.get(valid_a_[valid_index_+1]);
	        
	    valid_index_ = (valid_index_ + 2) % population.size();
	        
	    int flag = dominance_.compare(solution1,solution2);
	    if (flag == -1)
	      return solution1;
	    else if (flag == 1)
	      return solution2;
	    else if (solution1.getCrowdingDistance() > solution2.getCrowdingDistance())
	      return solution1;
	    else if (solution2.getCrowdingDistance() > solution1.getCrowdingDistance())
	      return solution2;
	    else
	      if (PseudoRandom.randDouble()<0.5)
	        return solution1;
	      else
	        return solution2;        
	  } // execute
	
	public Object executeInvalid(Object object) {
		SolutionSet population = (SolutionSet)object;
//		if(1==1) {
//			return population.get(0);
//		}
		    if (index_ == 0|| (a_ != null && a_.length != population.size())) //Create the permutation
		    {
		      a_= (new jmetal.util.PermutationUtility()).intPermutation(population.size());
		      index_ = 0;
		    }
		            
		        
		    Solution solution1,solution2;
		    solution1 = population.get(a_[index_]);
		    solution2 = (index_+1) >= a_.length? solution1 : population.get(a_[index_+1]);
		    
		    if((index_ + 2) == (population.size()-1)) {
				index_ = 1;
			} else {
				index_ = (index_ + 2) % (population.size());
			}
			
		    
//		    double p1 = ((org.femosaa.core.SASSolution)solution1).getProbabilityToBeNaturallyRepaired();
//		    double p2 = ((org.femosaa.core.SASSolution)solution2).getProbabilityToBeNaturallyRepaired();
//		    
//		    return p1 > p2? solution1 : solution2;
		   // index_ = (index_ + 2) % population.size();
		        
//		    int flag = dominance_.compare(solution1,solution2);
//		    if (flag == -1)
//		      return solution1;
//		    else if (flag == 1)
//		      return solution2;
//		    else if (solution1.getCrowdingDistance() > solution2.getCrowdingDistance())
//		      return solution1;
//		    else if (solution2.getCrowdingDistance() > solution1.getCrowdingDistance())
//		      return solution2;
//		    else
		      if (PseudoRandom.randDouble()<0.5)
		        return solution1;
		      else
		        return solution2;   
	}
	
	  @Override
	  public Object execute_5(Object object) throws JMException {
	    return null;
	  }

	  @Override
	  public Object execute_6(Object object) throws JMException {
	    return null;
	  }
}
