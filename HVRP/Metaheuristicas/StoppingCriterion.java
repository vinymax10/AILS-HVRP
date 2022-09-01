package Metaheuristicas;

public enum StoppingCriterion 
{
	Time("Total time"),
	Iteration("Total iteration"),
	IterationWithoutImprovement("Iteration without improvement");
	
	final String tipo;
	
	StoppingCriterion(String tipo)
	{
		this.tipo=tipo;
	}
}
