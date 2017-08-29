package org.femosaa.core;

import java.util.ArrayList;
import java.util.List;

import jmetal.core.Solution;
import jmetal.util.JMException;

public class Utils {

	
	
	public static double calculateMixedDistance(double[] f1, double[] f2, double[] max, double[] min, List<Integer> categorical) {
		
		if(f1.length != f2.length) {
			throw new RuntimeException("f1 and f2 are not equal!");
		}
		
		double n = 0.0;
		double c = 0.0;
		
		for (int i = 0; i < f1.length; i++) {
			
			if(categorical.contains(i)) {
				c += f1[i] != f2[i]? 1 : 0;
				
			} else {
				double v1 = max[i] != min[i]? (f1[i] - min[i])/(max[i] - min[i]) : 0.5;
				double v2 = max[i] != min[i]? (f2[i] - min[i])/(max[i] - min[i]) : 0.5;
				
				n += Math.abs(v1 - v2);
			}
			
			
		}
		
		return (c+1)*(n+0.5);
		
	}
	
	
	public static double calculateEuclideanDistance(Solution s1, Solution s2) {

		double d = 0.0;

		for (int i = 0; i < s1.getDecisionVariables().length; i++) {
			double v1 = 0d, v2 = 0d;
			try {
				v1 = s1.getDecisionVariables()[i].getValue();
				v2 = s2.getDecisionVariables()[i].getValue();
			} catch (JMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			d += Math.pow((v1-v2),2);
			
		}

		return Math.pow(d, 0.5);
	}
}
