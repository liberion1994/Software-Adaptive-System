package org.femosaa.seed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.femosaa.core.SASAlgorithmAdaptor;
import org.femosaa.core.SASSolutionInstantiator;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;

public class Seeder {

	private static List<Solution> seed = new ArrayList<Solution>();

	private static List<Entry> history = new ArrayList<Entry>();
	// private static SolutionSet solutions = new SolutionSet();
	private static List<Integer> categorical_index = new ArrayList<Integer>();
	private static double[] feature_max;
	private static double[] feature_min;

	private static Solution preSolution;
	private static double[] preFeatures;
	private Operator mutationOperator;
	
	private static final boolean isModelAnalytical = false;
	private static final boolean IS_PRINT = true;
	
	public static boolean isRandom = false;
	
	public Seeder(Operator mutationOperator) {
		super();
		this.mutationOperator = mutationOperator;
	}

	public static void priorFeatures(double[] preFeatures) {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
		Seeder.preFeatures = preFeatures;
		// System.out.print(Seeder.preFeatures + "1****\n");
		if (feature_max == null) {
			feature_max = new double[preFeatures.length];
			feature_min = new double[preFeatures.length];
			System.arraycopy(preFeatures, 0, feature_max, 0, preFeatures.length);
			System.arraycopy(preFeatures, 0, feature_min, 0, preFeatures.length);
		} else {

			for (int i = 0; i < preFeatures.length; i++) {
				if (preFeatures[i] < feature_min[i]) {
					feature_min[i] = preFeatures[i];
				}

				if (preFeatures[i] > feature_max[i]) {
					feature_max[i] = preFeatures[i];
				}
			}
		}
	}

	public static void priorSolution(Solution preSolution) {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
		Seeder.preSolution = preSolution;
		
		if(isRandom) {
			seed.add(preSolution);
		}
	}

	public static void posteriorObjetive(double[] values) {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
		for (int i = 0; i < values.length; i++) {
			preSolution.setObjective(i, values[i]);
		}

		history.add(new Entry(preFeatures, preSolution));
		// System.out.print(Seeder.preFeatures + "****\n");
		Seeder.preSolution = null;
		Seeder.preFeatures = null;
	}

	public static void clearSeed() {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
		seed.clear();
		history.clear();
	    feature_max=null;
		feature_min=null;

		preSolution=null;
		preFeatures=null;
	}

	/**
	 * This is for testing only
	 * 
	 * @param s
	 */
	public static void addSeed(Solution s) {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
		seed.add(s);
	}
	
	private void diversity1(List<Solution> newSeed, int seedSize){
		Set<Solution> set = new HashSet<Solution>();
		for (int k = 0; k < seedSize; k++) {
			
			if(newSeed.size() == 0) {
				newSeed.add(seed.get(k));
				set.add(seed.get(k));
			} else {
				
				
				double global_largest_d = Double.MIN_VALUE;
				Solution global = null;
				for (Solution s : seed) {
					
					if(set.contains(s)) {
						continue;
					}
					
					double largest_d = Double.MAX_VALUE;
					for(Solution subS : newSeed) {
						//if(!s.equals(subS)) {
							double d = org.femosaa.core.Utils.calculateEuclideanDistance(s, subS);
							if(d < largest_d) {
								largest_d = d;
							}
						//}
					}
					
					
					if(largest_d > global_largest_d) {
						global_largest_d = largest_d;
						global = s;
					}
					
				}
				
				newSeed.add(global);
				set.add(global);
				
			}
		}
	}
	
	private void diversity2(List<Solution> newSeed, int seedSize){
		Set<Solution> set = new HashSet<Solution>();
		for (int k = 0; k < seedSize; k++) {
			
			if(newSeed.size() == 0) {
				newSeed.add(seed.get(k));
				set.add(seed.get(k));
			} else {
				
				
				double global_largest_d = Double.MIN_VALUE;
				Solution global = null;
				for (Solution s : seed) {
					
					if(set.contains(s)) {
						continue;
					}
					
					double largest_d = Double.MAX_VALUE;
					for(Solution subS : newSeed) {
						//if(!s.equals(subS)) {
							double d = org.femosaa.core.Utils.calculateEuclideanDistance(s, subS);
							largest_d += d;
						//}
					}
					
					largest_d = largest_d/newSeed.size();
					if(largest_d > global_largest_d) {
						global_largest_d = largest_d;
						global = s;
					}
					
				}
				
				newSeed.add(global);
				set.add(global);
				
			}
		}
	}

	public void seeding(SolutionSet population,
			SASSolutionInstantiator factory, Problem problem_,
			int populationSize) throws JMException {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
		
		
		if(isModelAnalytical){
			for (Entry e : history) {
				Solution s = e.solution;
				// If analytical model, which is 100% accurate, then we can do this.
				problem_.evaluate(s);
			}
			
			calculateRank(Seeder.preFeatures);
		}

		int seedSize = seed.size() < populationSize ? seed.size() : (int) (populationSize * 1);
		//int seedSize = seed.size() == 0? 0 : 1;
		
		if(isRandom) {
			Collections.shuffle(seed);
		} else {
			
			
			List<Solution> newSeed = new ArrayList<Solution>();
			diversity2(newSeed, seedSize);
			seed = newSeed;
		}
		
		
	
	

		
		for (int k = 0; k < seedSize; k++) {

			if (population.size() >= seedSize) {
				break;
			}
			
			int l = k;//seed.size() - 1 - k;//new java.util.Random().nextInt(seedSize);

			Solution newSolution = factory.getSolution(problem_);
			for (int i = 0; i < seed.get(l).getDecisionVariables().length; i++) {
				newSolution.getDecisionVariables()[i].setValue(seed.get(l)
						.getDecisionVariables()[i].getValue());
			}
			// mutationOperator.execute(newSolution);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			population.add(newSolution);
		}
		
		if(IS_PRINT) {
		for (int i = 0; i < population.size(); i++) {
			//System.out.print("all**** " + map.get(s) + "\n");
			System.out.print("seeding**** " + population.get(i) + ", original: " + seed.get(i) + "\n");
		}
		}
		while (population.size() < populationSize) {
			Solution newSolution = factory.getSolution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			population.add(newSolution);
		}
		if(IS_PRINT) {
		for (int i = 0; i < population.size(); i++) {
			//System.out.print("all**** " + map.get(s) + "\n");
			System.out.print("final pop**** " + population.get(i) + "\n");
		}
		}
	}

	public static void calculateRank(double[] currentFeatures) {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
	
		if(1==2) {
			calculateRankWithoutPD(currentFeatures);
			return;
		}
		
		//System.out.print("========== start ranking ============\n");
		
		if(isRandom) {
			return;
		}
		
		seed.clear();
		final Map<Solution, Double> map = new HashMap<Solution, Double>();

		SolutionSet solutions = new SolutionSet(history.size());

		for (Entry e : history) {
			Solution s = e.solution;
			// System.out.print(currentFeatures + "\n");
			// System.out.print(e.features + "\n");
			double d = org.femosaa.core.Utils.calculateMixedDistance(
					currentFeatures, e.features, feature_max, feature_min,
					categorical_index);
			solutions.add(s);
			seed.add(s);
			map.put(s, d);
		}

		Ranking ranking = new Ranking(solutions);
		Collections.sort(seed, new Comparator() {

			@Override
			public int compare(Object a1, Object a2) {
				// TODO Auto-generated method stub
				return map.get(a1) < map.get(a2) ? -1 : 1;
			}

		});
		
//		for (Solution s : seed) {
//			System.out.print("d**** " + map.get(s) + "\n");
//		}

		map.clear();

		for (int k = 0; k < seed.size(); k++) {
			Solution s = (Solution) seed.get(k);

			double r = -1.0;
			for (int i = 0; i < ranking.getNumberOfSubfronts(); i++) {
				for (int j = 0; j < ranking.getSubfront(i).size(); j++) {
					if (s.equals(ranking.getSubfront(i).get(j))) {
						r = i + 1;
						break;
					}
				}

				if (r > 0) {
					break;
				}
			}

			map.put(s, (k+1)*1.0);// (k+1)*r
		}

		Collections.sort(seed, new Comparator() {

			@Override
			public int compare(Object a1, Object a2) {
				// TODO Auto-generated method stub
				return map.get(a1) < map.get(a2) ? -1 : 1;
			}

		});
		
		if(IS_PRINT) {
		for (Solution s : seed) {
			System.out.print("all**** " + map.get(s) + "\n");
			System.out.print("ranking**** " + s + "\n");
		}
		}
	}
	
	
	public static void calculateRankWithoutPD(double[] currentFeatures) {
		if (!SASAlgorithmAdaptor.isSeedSolution) {
			return;
		}
	
		
		//System.out.print("========== start ranking ============\n");
		
		if(isRandom) {
			return;
		}
		
		seed.clear();
		final Map<Solution, Double> map = new HashMap<Solution, Double>();

		SolutionSet solutions = new SolutionSet(history.size());

		for (Entry e : history) {
			Solution s = e.solution;
			// System.out.print(currentFeatures + "\n");
			// System.out.print(e.features + "\n");
			double d = org.femosaa.core.Utils.calculateMixedDistance(
					currentFeatures, e.features, feature_max, feature_min,
					categorical_index);
			solutions.add(s);
			seed.add(s);
			map.put(s, d);
		}

		//Ranking ranking = new Ranking(solutions);
		Collections.sort(seed, new Comparator() {

			@Override
			public int compare(Object a1, Object a2) {
				// TODO Auto-generated method stub
				return map.get(a1) < map.get(a2) ? -1 : 1;
			}

		});
		
//		for (Solution s : seed) {
//			System.out.print("d**** " + map.get(s) + "\n");
//		}

		map.clear();

		for (int k = 0; k < seed.size(); k++) {
			Solution s = (Solution) seed.get(k);

			double r = s.getObjective(0) * s.getObjective(1);

			map.put(s, (k+1)*r);
		}

		Collections.sort(seed, new Comparator() {

			@Override
			public int compare(Object a1, Object a2) {
				// TODO Auto-generated method stub
				return map.get(a1) < map.get(a2) ? -1 : 1;
			}

		});
		
		if(IS_PRINT) {
		for (Solution s : seed) {
			System.out.print("all**** " + map.get(s) + "\n");
			System.out.print("ranking**** " + s + "\n");
		}
		}
	}

	private static class Entry {
		// This should be the measured value
		protected double[] features;
		// This is the index inline with the Solution in MOEA/EA.
		// this include both environment and SAS status
		// protected double[] solution;
		protected Solution solution;

		public Entry(double[] features, Solution solution) {
			super();
			this.features = features;
			this.solution = solution;
		}
	}

}
