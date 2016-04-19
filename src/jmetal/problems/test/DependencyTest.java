package jmetal.problems.test;

import jmetal.core.Problem;
import jmetal.problems.SAS;
import junit.framework.Assert;
import junit.framework.TestCase;

public class DependencyTest extends TestCase {

	
	public void testUpperBound(){
		Problem p;
		DummySASSolution dss = null;
		try {
			//p = new SAS("SASSolutionType",new DummySASSolutionInstantiator(), DummySASSolution.vars, 0, 0);
			//dss = new DummySASSolution(p);
	
			double a = 50.0;
			double b = 58.0;
			
			System.out.print(a/b);
		
//		Assert.assertEquals(dss.getUpperBoundforVariable(0), 2);
//		Assert.assertEquals(dss.getLowerBoundforVariable(0), 0);
//		
//		dss.initVariables(0, 1, 1, 1, 1);
//		Assert.assertEquals(dss.getUpperBoundforVariable(1), -1);
//		Assert.assertEquals(dss.getLowerBoundforVariable(1), -1);
//		
//		dss.initVariables(1, 1, 1, 1, 1);
//		Assert.assertEquals(dss.getUpperBoundforVariable(1), 4);
//		Assert.assertEquals(dss.getLowerBoundforVariable(1), 0);
//		
//		dss.initVariables(2, 1, 1, 1, 1);
//		Assert.assertEquals(dss.getUpperBoundforVariable(1), 5);
//		Assert.assertEquals(dss.getLowerBoundforVariable(1), 0);
//		
//		
//		Assert.assertEquals(dss.getMainVariablesByDependentVariable(1)[0], 0);
//		Assert.assertEquals(dss.getMainVariablesByDependentVariable(1).length, 1);
		//Assert.assertEquals(dss.getUpperBoundforVariable(1), 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
}
