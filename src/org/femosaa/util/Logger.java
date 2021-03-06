package org.femosaa.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import org.femosaa.core.SASSolution;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

public class Logger {
	public static final String prefix = "/Users/tao/research/monitor/ws-soa/sas/";
	// This attribute is only used for testing
	public static int max_number_of_eval_to_have_only_seed = 0;
	public static synchronized void logSolutionSet(SolutionSet pareto_front, String name){
		File file = null;
		if(!(file = new File(prefix)).exists()){
			file.mkdir();
		} 
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefix
					+ name, true));

			String data = "";
			Iterator itr = pareto_front.iterator();
			while(itr.hasNext()) {
				Solution s = (Solution)itr.next();
				for(int i = 0; i < s.numberOfObjectives(); i++) {
					data +=  s.getObjective(i) + (i ==  s.numberOfObjectives() - 1? "" : ",");
				}
				data += "\n";
			}
			
			bw.write(data);
			bw.write("------------------------\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static synchronized void logSolutionSetWithGeneration(SolutionSet pareto_front, String name, int gen){
		File file = null;
		if(!(file = new File(prefix)).exists()){
			file.mkdir();
		} 
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefix
					+ name, true));

			String data = "";
			Iterator itr = pareto_front.iterator();
			while(itr.hasNext()) {
				Solution s = (Solution)itr.next();
				for(int i = 0; i < s.numberOfObjectives(); i++) {
					data +=  s.getObjective(i) + (i ==  s.numberOfObjectives() - 1? "" : ",");
				}
				data += "\n";
			}
			
			bw.write(data);
			bw.write("------------:" + gen + ":------------\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static synchronized void logSolutionSetWithGenerationAndFuzzyValue(SolutionSet new_pareto_front, SolutionSet old_pareto_front, String name, int gen){
		File file = null;
		if(!(file = new File(prefix)).exists()){
			file.mkdir();
		} 
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefix
					+ name, true));

			String data = "";
			Iterator new_itr = new_pareto_front.iterator();
			Iterator old_itr = old_pareto_front.iterator();
			while(new_itr.hasNext()) {
				Solution s1 = (Solution)new_itr.next();
				Solution s2 = (Solution)old_itr.next();
				boolean isRecorded = true;
				for(int i = 0; i < s1.numberOfObjectives(); i++) {

					if(s2.getObjective(i) == Double.MAX_VALUE || s2.getObjective(i) == (Double.MAX_VALUE/100) || 
							s1.getObjective(i) == Double.MAX_VALUE || s1.getObjective(i) == (Double.MAX_VALUE/100)) {
						isRecorded = false;
						break;
					}
				}
				
				if(!isRecorded) {
					continue;
				}
				
				for(int i = 0; i < s1.numberOfObjectives(); i++) {
					// original:fuzzy
					data +=  s2.getObjective(i) + ":" + s1.getObjective(i) + (i ==  s1.numberOfObjectives() - 1? "" : ",");
				}
				data += "\n";
			}
			
			bw.write(data);
			bw.write("------------:" + gen + ":------------\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static synchronized void logSolutionSetValues(SolutionSet pareto_front, String name){
		File file = null;
		if(!(file = new File(prefix)).exists()){
			file.mkdir();
		} 
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefix
					+ name, true));

			String data = "";
			Iterator itr = pareto_front.iterator();
			while(itr.hasNext()) {
				Solution s = (Solution)itr.next();
				for(int i = 0; i < s.numberOfVariables(); i++) {
					data +=  s.getDecisionVariables()[i].getValue() + (i ==  s.numberOfVariables() - 1? "" : ",");
				}
				data += "\n";
			}
			
			bw.write(data);
			bw.write("------------------------\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static synchronized void logPercentageOfMarkedSolution(SolutionSet pareto_front, String name){
		File file = null;
		if(!(file = new File(prefix)).exists()){
			file.mkdir();
		} 
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefix
					+ name, true));

			String data = "";
			Iterator itr = pareto_front.iterator();
			int no = 0;
			while(itr.hasNext()) {
				Solution s = (Solution)itr.next();
				if(((SASSolution)s).isFromInValid) {
					no++;
				}
			}
			
			bw.write(no + ":"+no/pareto_front.size() + "\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static synchronized void printMarkedSolution(SolutionSet pareto_front, int eval){
		Iterator itr = pareto_front.iterator();
		int no = 0;
		while(itr.hasNext()) {
			Solution s = (Solution)itr.next();
			if(((SASSolution)s).isFromInValid) {
				no++;
			}
		}
		System.out.print("from seed: " + no + "\n");
		if(no == pareto_front.size() && max_number_of_eval_to_have_only_seed == 0) {
			max_number_of_eval_to_have_only_seed = eval;
		}
	}
}
