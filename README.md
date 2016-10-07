# MOEA/D-STM with feature dependency injection and knee selection
This is the repository for an optimization component exploited by [FEMOSAA](https://github.com/taochen/ssase#femosaa-feature-guided-and-knee-driven-multi-objective-optimization-for-self-adaptive-software-at-runtime) (Feature guided and knEe driven Multi-objective Optimization for Self-Adaptive softwAre at runtime). It contains the source code of MOEA/D-STM, our feature dependency aware mutation/crossover operators and knee selection method. Details are explained as below:

###MOEA/D-STM: Multi-Objective Evolutionary Algorithm based Decomposition with STable-Matching model

Unlike the classic aggregation methods which only specify and solve one aggregation at a time, MOEA/D decomposes the original multi-objective problem into several subproblems, each of which is an additive or Tchebycheff aggregation with automatically generated weights. Then, it leverages the population-based technique in classic EA/MOEA to solve these subproblems collaboratively. As a recent MOEA/D variant, MOEA/D-STM achieves better balance between convergence and diversity by modifying the survival selection of the original MOEA/D. The algorithm details can be found in the following publication:

 > * K.Li, Q.Zhang, S.Kwong, M.Li, and R.Wang,“Stablematching-based selection in evolutionary multiobjective optimization,” IEEE Trans. Evolutionary Computation, vol. 18, no. 6, pp. 909–923, 2014.Source code directory:
   * [src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java)###Dependency Aware Mutation and Crossover Operators

By analyzing and extracting dependencies from the Feature Model of a self-adaptive software, we are able to inject the dependency into the mutation/crossover operators of the EA/MOEA. Such information of dependencies can guide the search, providing more chances for finding better solutions. We have currently injected dependency to a modified bit-flip mutation operator and uniform crossover operator, but the injection mechanism itself is generic and is compatible with a ranges of reproduction operators. The details has been reported in a paper which is currently under submission.

Source code directory:
   * [src/jmetal/problems/](https://github.com/JerryI00/Software-Adaptive-System/tree/master/src/jmetal/problems)

###Knee Solution Selection

We have designed method to find knee solutions, which are usually the most preferable ones, for optimizing self-adaptive software at runtime. This can eliminate the needs of specifying weights on conflicting objective while achieving a balanced trade-off.
The details has been reported in a paper which is currently under submission.

Source code directory (the kneeSelection function):
   * [src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java](https://github.com/JerryI00/Software-Adaptive-System/blob/master/src/jmetal/metaheuristics/moead/MOEAD_STM_SAS.java#L887)

- - - -

Although most of our code is based on Jmetal, we have extracted the necessary source code that we need and included them together with our code. This repository is Maven compatible using the provided pom.xml file.