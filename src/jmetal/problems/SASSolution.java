package jmetal.problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * Need to ensure that the order of variables is the same as control primitives, and that
 * the main variables always ahead of the dependent variables.
 * 
 * @author tao
 *
 */
public abstract class SASSolution extends Solution {
	
	
	// Key = variable index of dependent variable, the VarEntity has the same order as the original values array.
	protected final static Map<Integer, VarEntity[]> mutationMap = new HashMap<Integer, VarEntity[]>();
	
	// Key = variable index, Value = list of main/dependent variable index.
	protected final static Map<Integer, List<Integer>> crossoverMap = new HashMap<Integer, List<Integer>>();

	protected static double[][] optionalVariables;
	
	//public abstract double getVariableValueFromIndexValue(int indexValue);

	public SASSolution(Problem problem) throws ClassNotFoundException {
		super(problem);
		// TODO Auto-generated constructor stub
	}
	
	public SASSolution(Problem problem, Variable[] variables) throws ClassNotFoundException {
		super(problem, variables);
		// TODO Auto-generated constructor stub
	}

	public SASSolution(Solution solution) {
		super(solution);
		// TODO Auto-generated constructor stub
	}
	
	public SASSolution() {
		super();
		// TODO Auto-generated constructor stub
	}

	public abstract double[] getObjectiveValuesFromIndexValue();

	public abstract double getVariableValueFromIndex(int index);
	
	private int getUpperBoundforVariable(int index) throws JMException {
		if (mutationMap.containsKey(index)) {
			VarEntity v = mutationMap.get(index)[(int) super.getDecisionVariables()[mutationMap.get(index)[0].getVarIndex()].getValue()];
			return v.getOptionalValues(super.getDecisionVariables()).length - 1;
		} else {
			return optionalVariables[index].length - 1;
		}
	
	}

	
	private int getLowerBoundforVariable(int index) throws JMException {
			return 0;		
	}

	private int translateIntoIndexInMainVariable(int index, int subIndex) throws JMException {
		if (mutationMap.containsKey(index)) {
			VarEntity v = mutationMap.get(index)[(int) super.getDecisionVariables()[mutationMap.get(index)[0].getVarIndex()].getValue()];
			return v.getOptionalValues(super.getDecisionVariables())[subIndex];
		} else {
			return subIndex;
		}
	}
	
	
	private int[] getMainVariablesByDependentVariable(int index) {
		if (mutationMap.containsKey(index)) {
			Integer[] ints = mutationMap.get(index)[0]
					.getMainVariablesByDependentVariable(new ArrayList<Integer>());
			int[] result = new int[ints.length];

			for (int i = 0; i < result.length; i++) {
				result[i] = ints[i];
			}

			return result;
		}
		return null;
	}

	private List<Integer> getVariableNeedCrossover(int index) {
		
		if (crossoverMap.containsKey(index)) {
			return crossoverMap.get(index);
		}
		
		List<Integer> list = new ArrayList<Integer>();
		for (Map.Entry<Integer, VarEntity[]> entity : mutationMap.entrySet()) {
			if (index == entity.getKey()) {
				entity.getValue()[0].getMainVariablesByDependentVariable(list);
			} else {
				
				VarEntity v = entity.getValue()[0];
				
				do {
					if(index == v.varIndex) {
						if(!list.contains(entity.getKey())) {
							list.add(entity.getKey());
						}
						
						break;
					}
					v = v.next == null? null : v.next[0];
				} while(v != null);
				
			}
		}
		

		crossoverMap.put(index, list);
		
		return list;
	}
	
	public void mutateWithDependency() throws JMException{
		for (int i = 0; i < super.getDecisionVariables().length; i++) {
			this.mutateWithDependency(i);
		}
	}
	
	public void mutateWithDependency(int i) throws JMException{
		boolean isMutate = false;
		int value = (int)super.getDecisionVariables()[i].getValue();	
		int upper = this.getUpperBoundforVariable(i);
		int lower = this.getLowerBoundforVariable(i);
		int traUpper = this.translateIntoIndexInMainVariable(i, upper);
		int traLower = this.translateIntoIndexInMainVariable(i, lower);
		if(value > traUpper || value < traLower) {
			isMutate = true;
		}
		
		if (isMutate) {
		
		
			int v = (int) (PseudoRandom.randInt(
					// In the implementation of SASSolution, we can ensure the right boundary is 
					// always used even under variable dependency.
					lower,
					upper));
		
			v = this.translateIntoIndexInMainVariable(i, v);
			super.getDecisionVariables()[i].setValue(v);
		}
	}
	
	public void crossoverWithDependency(Solution parent1, Solution parent2, Solution offSpring1, Solution offSpring2) throws JMException{
		for (int i = 0; i < parent1.numberOfVariables(); i++) {
			this.crossoverWithDependency(i, parent1, parent2, offSpring1, offSpring2);
		}
	}
	
	public void crossoverWithDependency(int i, Solution parent1,
			Solution parent2, Solution offSpring1, Solution offSpring2)
			throws JMException {

		if (i >= parent1.getDecisionVariables().length) {
			return;
		}

		// If it swap and they are originally unequal.
		if (offSpring1.getDecisionVariables()[i].getValue() != parent1
				.getDecisionVariables()[i].getValue()) {
			List<Integer> list = ((SASSolution) parent1)
					.getVariableNeedCrossover(i);
			for (Integer j : list) {

				// swap all main/dependent variable, if they have not been swapped.
				if (offSpring1.getDecisionVariables()[j].getValue() == parent1
						.getDecisionVariables()[j].getValue()
						&& parent1.getDecisionVariables()[j].getValue() != parent2
								.getDecisionVariables()[j].getValue()) {
					int valueX1 = (int) parent1.getDecisionVariables()[j]
							.getValue();
					int valueX2 = (int) parent2.getDecisionVariables()[j]
							.getValue();
					offSpring1.getDecisionVariables()[j].setValue(valueX2);
					offSpring2.getDecisionVariables()[j].setValue(valueX1);
					// Ensure that the main/dependent variable of the newly swapped variable are also swapped.
					this.crossoverWithDependency(j, parent1, parent2,
							offSpring1, offSpring2);
				}
			}
		}

	}
	
	
	protected static class VarEntity {
		
		private int varIndex;
		private VarEntity[] next;
		// This correspond to the index in the original set
		private int[] optionalValues;
//		private double[] dependentOptionalValues;
//		
//		public VarEntity(int index, double[] optionalValues, double[] dependentOptionalValues) {
//			super();
//			this.index = index;
//			this.optionalValues = optionalValues;
//			this.dependentOptionalValues = dependentOptionalValues;
//		}

		public VarEntity(int varIndex, int[] optionalValues, VarEntity[] next) {
			super();
			this.varIndex = varIndex;
			this.optionalValues = optionalValues;
			this.next = next;
		}
		
		public int getVarIndex(){
			return varIndex;
		}
		
		public int[] getOptionalValues(Variable[] variables){
			if (next == null) {
				return optionalValues;
			} else {
				try {
					return next[(int)variables[next[0].getVarIndex()].getValue()].getOptionalValues(variables);
				} catch (JMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
		public Integer[] getMainVariablesByDependentVariable(List<Integer> ind){
			ind.add(varIndex);
			if (next == null) {				
				return ind.toArray(new Integer[ind.size()]);
			} else {
				return next[0].getMainVariablesByDependentVariable(ind);
			}
		}
		
	}
}
