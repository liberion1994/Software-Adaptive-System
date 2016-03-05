package jmetal.problems.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.problems.SASSolution;
import jmetal.util.JMException;

public class DummySASSolution extends SASSolution{
	


	public final static double[][] optionalVariables = 
	{
			{0,1,2},
			{0,1,2,3,4,5,6,8,10},
			{2,4,6,8,10,12,14,16,18,20},
			{5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100},
			{5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100}
	};
	
	public  static int[][] vars = 
	{
		{0,optionalVariables[0].length-1},	
		{0,optionalVariables[1].length-1},
		{0,optionalVariables[2].length-1},
		{0,optionalVariables[3].length-1},
		
		{0,optionalVariables[4].length-1},
	};

	public final static int numberOfObjectives_ = 2;

	// Key = variable index of dependent variable, the VarEntity has the same order as the original values array.
	private final static Map<Integer, VarEntity[]> map = new HashMap<Integer, VarEntity[]>();

	static {
		map.put(1,
				new VarEntity[] {
						new VarEntity(0, new int[] {0}, null),
						new VarEntity(0,
								new int[] {1,2,3,4 }, null),
						new VarEntity(0,
								new int[] {5,6,7,8}, null) });
	}
	
	
	
	public DummySASSolution(Problem problem) throws ClassNotFoundException {
		super(problem);
		// TODO Auto-generated constructor stub
	}

	public DummySASSolution(Problem problem, Variable[] variables) throws ClassNotFoundException {
		super(problem, variables);
		// TODO Auto-generated constructor stub
	}


	public DummySASSolution(Solution solution) {
		super(solution);
		// TODO Auto-generated constructor stub
	}
	
	public DummySASSolution() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public double[] getObjectiveValuesFromIndexValue(int[] var) {
		double[] x = new double[optionalVariables.length];
		int numberOfVariables_ = optionalVariables.length;
		for (int i = 0; i < x.length; i++) {

				x[i] = optionalVariables[i][var[i]];
//				if (i == 1 && var[i-1] == 0) {
//					System.out.print("var[0]= " + var[i-1] + " x[0]= " + x[i-1] +
//							" var[1]= " + var[i] + " x[1]= " + x[i] + "\n");
//				}
			
		}

		double[] f = new double[numberOfObjectives_];
		int k = numberOfVariables_ - numberOfObjectives_ + 1;

		double g = 0.0;
		for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++)
			g += (x[i] - 0.5) * (x[i] - 0.5)
					- Math.cos(20.0 * Math.PI * (x[i] - 0.5));

		g = 100 * (k + g);
		for (int i = 0; i < numberOfObjectives_; i++)
			f[i] = (1.0 + g) * 0.5;

		for (int i = 0; i < numberOfObjectives_; i++) {
			for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)
				f[i] *= x[j];
			if (i != 0) {
				int aux = numberOfObjectives_ - (i + 1);
				f[i] *= 1 - x[aux];
			} // if
		}

		return f;
	}

	@Override
	public int getUpperBoundforVariable(int index) throws JMException {
		if (map.containsKey(index)) {
			VarEntity v = map.get(index)[(int) super.getDecisionVariables()[map.get(index)[0].getVarIndex()].getValue()];
			return v.getOptionalValues(super.getDecisionVariables()).length - 1;
		} else {
			return optionalVariables[index].length - 1;
		}
	
	}

	@Override
	public int getLowerBoundforVariable(int index) throws JMException {
			return 0;		
	}

	public int translateIntoIndexInMainVariable(int index, int subIndex) throws JMException {
		if (map.containsKey(index)) {
			VarEntity v = map.get(index)[(int) super.getDecisionVariables()[map.get(index)[0].getVarIndex()].getValue()];
			return v.getOptionalValues(super.getDecisionVariables())[subIndex];
		} else {
			return subIndex;
		}
	}
	
	@Override
	public int[] getMainVariablesByDependentVariable(int index) {
		if (map.containsKey(index)) {
			Integer[] ints = map.get(index)[0]
					.getMainVariablesByDependentVariable(new ArrayList<Integer>());
			int[] result = new int[ints.length];

			for (int i = 0; i < result.length; i++) {
				result[i] = ints[i];
			}

			return result;
		}
		return null;
	}

	
	public void initVariables(int... vars){
		Variable[] variable_ = super.getDecisionVariables();
		for (int i = 0; i < vars.length; i++) {
			try {
				variable_[i] = new Int(vars[i], (int)variable_[i].getLowerBound(), 
						(int)variable_[i].getUpperBound());
			} catch (JMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static class VarEntity {
		
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
			if (next == null) {
				ind.add(varIndex);
				return ind.toArray(new Integer[ind.size()]);
			} else {
				return next[0].getMainVariablesByDependentVariable(ind);
			}
		}
		
	}
}
