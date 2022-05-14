package Metaheuristicas;

public enum Variante 
{
	HVRPFD(0),
	HVRPD(1),
	FSMFD(2),
	FSMF(3),
	FSMD(4);
	
	final int tipo;
	
	Variante(int tipo)
	{
		this.tipo=tipo;
	}
}
