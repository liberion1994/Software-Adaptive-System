package jmetal.metaheuristics.moead;

import java.io.File;
import java.util.Random;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.problems.SAS;
import jmetal.util.PseudoRandom;

public class MOEAD_SAS_PLAIN_main extends MOEAD_SAS_main{
	@Override
	protected Solution findSoleSolutionAfterEvolution(SolutionSet pareto_front) {
		// find the knee point
		Solution kneeIndividual = pareto_front.get(PseudoRandom.randInt(0, pareto_front.size() - 1)); 
		
		for (int i = 0; i < problem.getNumberOfObjectives(); i++)
			System.out.print(kneeIndividual.getObjective(i) + "\n");
		
		
		String str = "data/" +problem.getName()
		+ "M" + problem.getNumberOfObjectives() + "/SAS";
		if(SAS.isTest) 
		Utils.deleteFolder(new File(str+ "/knee_results.dat"));
		SolutionSet set = new SolutionSet(1);
		set.add(kneeIndividual);
		if(SAS.isTest) 
		set.printObjectivesToFile(str + "/knee_results.dat");
		
		return kneeIndividual;
	}
}
