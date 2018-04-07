# The MOEA for Search-based Self-Adaptive Software Engine (SSASE)
This is the repository for the MOEA based optimization component exploited by [Search-based Self-Adaptive Software Engine (SSASE)](https://github.com/taochen/ssase). It specifically contains the tailored algorithms for the following work:

- - - -

## MOEA with feature dependency injection and knee selection
The repository contains the code for the optimizer component exploited by [FEMOSAA](https://github.com/taochen/ssase#femosaa-feature-guided-and-knee-driven-multi-objective-optimization-for-self-adaptive-software-at-runtime) (Feature guided and knEe driven Multi-objective Optimization for Self-Adaptive softwAre at runtime). It contains the source code of some studied MOEAs, our feature dependency aware mutation/crossover operators and knee selection method. 

Here we have only implemented some representative MOEAs, others, which have not be implemented here, can be easily integrated with our dependency aware operators and knee selection in FEMOSAA. Details are explained as below:

### Dependency Aware Mutation and Crossover Operators

By analyzing and extracting dependencies from the Feature Model of a self-adaptive software, we are able to inject the dependency into the mutation/crossover operators of the EA/MOEA. Such information of dependencies can guide the search, providing more chances for finding better solutions. We have currently injected dependency to a modified bit-flip mutation operator and uniform crossover operator, but the injection mechanism itself is generic and is compatible with a ranges of reproduction operators. More details can be found in the following publications:

 > * T. Chen, K. Li, R. Bahsoon, and X. Yao. 2018. FEMOSAA: Feature Guided and Knee Driven Multi-Objective Optimization for Self-Adaptive Software. ACM Transactions on Software Engineering and Methodology (2018). in press.

Source code directory:
   * [org/femosaa/core/](https://github.com/JerryI00/Software-Adaptive-System/tree/master/src/org/femosaa/core)

### Knee Solution Selection

We have designed method to find knee solutions, which are usually the most preferable ones, for optimizing self-adaptive software at runtime. This can eliminate the needs of specifying weights on conflicting objective while achieving a balanced trade-off. More details can be found in the following publications:

 > * T. Chen, K. Li, R. Bahsoon, and X. Yao. 2018. FEMOSAA: Feature Guided and Knee Driven Multi-Objective Optimization for Self-Adaptive Software. ACM Transactions on Software Engineering and Methodology (2018). in press.


Source code directory (the kneeSelection function in a particular MOEA):
   * [src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java#L887)

### MOEA/D-STM: Multi-Objective Evolutionary Algorithm based Decomposition with STable-Matching model

Unlike the classic aggregation methods which only specify and solve one aggregation at a time, MOEA/D decomposes the original multi-objective problem into several subproblems, each of which is an additive or Tchebycheff aggregation with automatically generated weights. Then, it leverages the population-based technique in classic EA/MOEA to solve these subproblems collaboratively. As a recent MOEA/D variant, MOEA/D-STM achieves better balance between convergence and diversity by modifying the survival selection of the original MOEA/D. The algorithm details can be found in the following publication:

 > * K.Li, Q.Zhang, S.Kwong, M.Li, and R.Wang,“Stablematching-based selection in evolutionary multiobjective optimization,” IEEE Trans. Evolutionary Computation, vol. 18, no. 6, pp. 909–923, 2014.Source code directory:
   * [src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java)


### NSGA-II: Non-dominated Sort Genetic Algorithm-II

As one of the most popular MOEAs, NSGA-II at first uses the non-dominated sorting to divide the population into several non-domination lev- els. Solutions in the first several levels have a higher priority to survive to the next iteration. If the size of the current non-dominated solution set exceeds the pre- defined threshold, NSGA-II uses the crowding distance, a density estimation technique, to trim the population. The algorithm details can be found in the following publication:

 > * K. Deb, A. Pratap, S. Agarwal, and T. Meyarivan, “A fast and elitist multi-objective genetic algorithm: Nsga-ii,” Trans. Evol. Comp, vol. 6, no. 2, pp. 182–197, April 2002.Source code directory:
   * [src/jmetal/metaheuristics/nsgaII/NSGAII_SAS.java](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/jmetal/metaheuristics/nsgaII/NSGAII_SAS.java)


### IBEA: Indicator Based Evolutionary Algorithm

The basic idea of IBEA is to firstly define the optimization goal in terms of a binary performance measure/indicator, which is then used to guide the survival selection process. In this way, IBEA transfers a multi-objective optimization problem into a new single- objective optimization problem, w.r.t. the chosen indica- tor, to facilitate the fitness assignment procedure. The algorithm details can be found in the following publication:

 > * E.Zitzler and S.Ku ̈nzli,“Indicator-based selection in multi-objective search,” in Proc. of PPSN VIII: the 8th International conference on Parallel Problem Solving from Nature, 2004, pp. 832–842.Source code directory:
   * [src/jmetal/metaheuristics/ibea/IBEA_SAS.java](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/jmetal/metaheuristics/ibea/IBEA_SAS.java)

- - - -

## Seeding MOEA for SAS
The repository contains the code for an seeding component exploited by [Seeding-for-SAS](https://github.com/taochen/ssase#seeding-seeding-the-search-based-multi-objective-sas). 

Source code directory:
   * [src/org/femosaa/seed](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/org/femosaa/seed)

- - - -



Although most of our code is based on Jmetal, we have extracted the necessary source code that we need and included them together with our code. This repository is Maven compatible using the provided pom.xml file.