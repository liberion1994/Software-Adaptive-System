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
 * Need to ensure that the order of variables is the same as control primitives.
 * 
 * @author tao
 *
 */
public abstract class SASSolution extends Solution {
	
	
	// Key = variable index of dependent variable, the VarEntity has the same order as the original values array.
	protected final static Map<Integer, VarEntity[]> dependencyMap = new HashMap<Integer, VarEntity[]>();
	protected final static Map<Integer, VarEntity[]> validationDependencyMap = new HashMap<Integer, VarEntity[]>();
	// Key = variable index, Value = list of main/dependent variable index.
	protected final static Map<Integer, List<Integer>> crossoverMap = new HashMap<Integer, List<Integer>>();
	
	// Key = variable index, Value = list of dependent variable index.
	protected final static Map<Integer, List<Integer>> mutationMap = new HashMap<Integer, List<Integer>>();

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
	
	
	public static void init(double[][] optionalVariables) {
		SASSolution.optionalVariables = optionalVariables;
	}
	
	public static void clearAndStoreForValidationOnly(){
		validationDependencyMap.putAll(dependencyMap);
		dependencyMap.clear();
	}
	
	public static Map<Integer, VarEntity[]> getDependencyMap(){
		return dependencyMap;
	}

	public static void main(String[] a) {
		System.out.print(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() + "\n");
		//VarEntity[] vars = new VarEntity[1000000]; 
		//VarEntity v1 = new VarEntity(0, null, null);
		for (int i = 0; i < 100000; i ++) {
			VarEntity v1 = new VarEntity(0, null, null);
			//Object obj = new Object();
		//vars[i] = v1;
		}
		System.out.print(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() + "\n");
	}
	
	
	private int getUpperBoundforVariable(int index) throws JMException {
		if (dependencyMap.containsKey(index)) {
			VarEntity v = dependencyMap.get(index)[(int) super.getDecisionVariables()[dependencyMap.get(index)[0].getVarIndex()].getValue()];
			return v.getOptionalValues(super.getDecisionVariables()).length - 1;
		} else {
			return optionalVariables[index].length - 1;
		}
	
	}

	
	private int getLowerBoundforVariable(int index) throws JMException {
			return 0;		
	}

	private int translateIntoIndexInMainVariable(int index, int subIndex) throws JMException {
		if (dependencyMap.containsKey(index)) {
			VarEntity v = dependencyMap.get(index)[(int) super.getDecisionVariables()[dependencyMap.get(index)[0].getVarIndex()].getValue()];
			return v.getOptionalValues(super.getDecisionVariables())[subIndex];
		} else {
			return subIndex;
		}
	}
	
	
	private int[] getMainVariablesByDependentVariable(int index) {
		if (dependencyMap.containsKey(index)) {
			Integer[] ints = dependencyMap.get(index)[0]
					.getMainVariablesByDependentVariable(new ArrayList<Integer>());
			int[] result = new int[ints.length];

			for (int i = 0; i < result.length; i++) {
				result[i] = ints[i];
			}

			return result;
		}
		return null;
	}

	private List<Integer> getMainVariables(int index) {
		
		if (crossoverMap.containsKey(index)) {
			return crossoverMap.get(index);
		}
		
		List<Integer> list = new ArrayList<Integer>();
		for (Map.Entry<Integer, VarEntity[]> entity : dependencyMap.entrySet()) {
			if (index == entity.getKey()) {
				entity.getValue()[0].getMainVariablesByDependentVariable(list);
			} 
		}
		

		crossoverMap.put(index, list);
		
		return list;
	}
	
	private List<Integer> getDependentVariables(int index) {
		
		if (mutationMap.containsKey(index)) {
			return mutationMap.get(index);
		}
		
		List<Integer> list = new ArrayList<Integer>();
		for (Map.Entry<Integer, VarEntity[]> entity : dependencyMap.entrySet()) {
			
				
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
		

		mutationMap.put(index, list);
		
		return list;
	}
	
	public void mutateWithDependency() throws JMException{
		for (int i = 0; i < super.getDecisionVariables().length; i++) {		
			this.mutateWithDependency(i, false);
		}
	}
	
	public void mutateWithDependency(int i, boolean isMutate /*This is can be only true for the initial entrance*/) throws JMException{
	
			
		int upper = this.getUpperBoundforVariable(i);
		int lower = this.getLowerBoundforVariable(i);
		
		//isMutate = !isValid(this, i);
		if (isMutate || !isValid(this, i)) {
		
		
			int v = (int) (PseudoRandom.randInt(
					// In the implementation of SASSolution, we can ensure the right boundary is 
					// always used even under variable dependency.
					lower,
					upper));
		
			v = this.translateIntoIndexInMainVariable(i, v);
			super.getDecisionVariables()[i].setValue(v);
			List<Integer> list = this.getDependentVariables(i);
			for (Integer j : list) {
				this.mutateWithDependency(j, false);
			}
		}
		
	
	}
	
	public void crossoverWithDependency(Solution parent1, Solution parent2, Solution offSpring1, Solution offSpring2) throws JMException{
		for (int i = 0; i < parent1.numberOfVariables(); i++) {
			// Crossover has been completed
			this.crossoverWithDependency(i, parent1, parent2, offSpring1, offSpring2, false);
		}
	}
	
	public void crossoverWithDependency(int i, Solution parent1,
			Solution parent2, Solution offSpring1, Solution offSpring2, boolean isCrossover /*This is can be only true for the initial entrance*/)
			throws JMException {

//		// If has been swapped.
//		if (offSpring1.getDecisionVariables()[i].getValue() == parent2
//				.getDecisionVariables()[i].getValue()
//				&& parent1.getDecisionVariables()[i].getValue() != parent2
//						.getDecisionVariables()[i].getValue()) {
//			return;
//		}

//		
		if (isCrossover && offSpring1.getDecisionVariables()[i].getValue() == parent1
				.getDecisionVariables()[i].getValue()
				&& parent1.getDecisionVariables()[i].getValue() != parent2
						.getDecisionVariables()[i].getValue()) {
			
			int valueX1 = (int) parent1.getDecisionVariables()[i]
			                   							.getValue();
			int valueX2 = (int) parent2.getDecisionVariables()[i]
			                   							.getValue();
			offSpring1.getDecisionVariables()[i].setValue(valueX2);
			offSpring2.getDecisionVariables()[i].setValue(valueX1);
			
		}
		
		
		if (i >= parent1.getDecisionVariables().length) {
			return;
		}

		// If it swap and they are originally unequal. This might not needed.
		if (offSpring1.getDecisionVariables()[i].getValue() == parent2
				.getDecisionVariables()[i].getValue()
				&& parent1.getDecisionVariables()[i].getValue() != parent2
						.getDecisionVariables()[i].getValue()) {
			// This should include both dependent and main variable of this variable.
			List<Integer> mainList = ((SASSolution) parent1)
					.getMainVariables(i);
			
			List<Integer> dependentList = ((SASSolution) parent1)
			.getDependentVariables(i);
			
			// Do mains
			if(!isValid((SASSolution)offSpring1, i) || !isValid((SASSolution)offSpring2, i) ) {
				for (Integer j : mainList) {
					// swap all main variable, if they have not been swapped. This should not occur
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
									offSpring1, offSpring2, false);
							
						} 
//					else {
//							throw new RuntimeException("index " + j + " cause in valid but it has already been swapped!");
//						}
					} 
			}
			
			
			
			// Do dependents
			for (Integer j : dependentList) {
				// swap if it the prior swap causes any variables in the dependency becomes invalid.
				if(!isValid((SASSolution)offSpring1, j) || !isValid((SASSolution)offSpring2, j) ) {
				// swap all dependent variable, if they have not been swapped. 
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
								offSpring1, offSpring2, false);
						
					} 
//				else {
//						throw new RuntimeException("index " + j + " cause in valid but it has already been swapped!");
//					}
				} 
			}
		}

	}
	
	// This should only be used to check, regardless if dependency
	// has been injected or not.
	public boolean isSolutionValid(){
		for (int i = 0; i < super.getDecisionVariables().length; i ++) {
			try {
				if(!checkIsValidOnly(this, i)) {
					return false;
				}
			} catch (JMException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	private boolean checkIsValidOnly(SASSolution s, int i) throws JMException {

		int value = (int) s.getDecisionVariables()[i].getValue();
		int upper = -1;

		if (validationDependencyMap.containsKey(i)) {
			VarEntity v = validationDependencyMap.get(i)[(int) super
					.getDecisionVariables()[validationDependencyMap.get(i)[0]
					.getVarIndex()].getValue()];
			upper = v.getOptionalValues(super.getDecisionVariables()).length - 1;
		} else {
			upper = optionalVariables[i].length - 1;
		}

		int lower = 0;
		int traUpper = -1;
		if (validationDependencyMap.containsKey(i)) {
			VarEntity v = validationDependencyMap.get(i)[(int) super
					.getDecisionVariables()[validationDependencyMap.get(i)[0]
					.getVarIndex()].getValue()];
			traUpper = v.getOptionalValues(super.getDecisionVariables())[upper];
		} else {
			traUpper = upper;
		}

		int traLower = -1;
		if (validationDependencyMap.containsKey(i)) {
			VarEntity v = validationDependencyMap.get(i)[(int) super
					.getDecisionVariables()[validationDependencyMap.get(i)[0]
					.getVarIndex()].getValue()];
			traLower = v.getOptionalValues(super.getDecisionVariables())[lower];
		} else {
			traLower = lower;
		}

		if (value > traUpper || value < traLower) {
			return false;
		}

		return true;
	}
	
	private boolean isValid(SASSolution s, int i) throws JMException{
		
		int value = (int)s.getDecisionVariables()[i].getValue();	
		int upper = s.getUpperBoundforVariable(i);
		int lower = s.getLowerBoundforVariable(i);
		int traUpper = s.translateIntoIndexInMainVariable(i, upper);
		int traLower = s.translateIntoIndexInMainVariable(i, lower);
		
		if  (value > traUpper || value < traLower) {
			return false;
		}
		
		return true;
	}
	

}
