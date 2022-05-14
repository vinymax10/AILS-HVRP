package Pertubacao;

public enum HeuristicaAdicao 
{
	DISTANCIA(1),
	BESTCUSTO(2);
	
	final int heuristica;
	
	HeuristicaAdicao(int heuristica)
	{
		this.heuristica=heuristica;
	}
}
