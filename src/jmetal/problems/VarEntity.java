package jmetal.problems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jmetal.core.Variable;
import jmetal.util.JMException;


public class VarEntity {
	
	protected int varIndex;
	protected VarEntity[] next;
	// This correspond to the index in the original set
	protected Integer[] optionalValues;
	protected Set<Integer> set;
//	private double[] dependentOptionalValues;
//	
//	public VarEntity(int index, double[] optionalValues, double[] dependentOptionalValues) {
//		super();
//		this.index = index;
//		this.optionalValues = optionalValues;
//		this.dependentOptionalValues = dependentOptionalValues;
//	}

	public VarEntity(int varIndex, Integer[] optionalValues, VarEntity[] next) {
		super();
		this.varIndex = varIndex;
		this.optionalValues = optionalValues;
		if(optionalValues != null) {
			set = new HashSet<Integer>();
			for (Integer i : optionalValues) {
				set.add(i);
			}
			
		}
		this.next = next;
	}
	
	public Integer[] getOptionalValues(){
		return optionalValues;
	}
	
	public VarEntity[] getNext(){
		return next;
	}
	
	public int getVarIndex(){
		return varIndex;
	}
	
	public Integer[] getOptionalValues(Variable[] variables){
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
	
	public Set<Integer> getOptionalValuesSet(Variable[] variables){
		if (next == null) {
			return set;
		} else {
			try {
				return next[(int)variables[next[0].getVarIndex()].getValue()].getOptionalValuesSet(variables);
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
	
	public void extend(VarEntity[] next){
		this.next = next;
		optionalValues = null;
		set = null;
	}
	
	
	public void replace(Integer[] optionalValues){
		this.optionalValues = optionalValues;
		if(optionalValues != null) {
			set.clear();
			for (Integer i : optionalValues) {
				set.add(i);
			}
			
		}
	}

	
}