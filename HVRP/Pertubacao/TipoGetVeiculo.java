package Pertubacao;

public enum TipoGetVeiculo 
{
	Ale(0),
	ProbMenor(1),
	ProbMaior(2),
	ProbProporcional(3);
	
	final int tipo;
	
	TipoGetVeiculo(int tipo)
	{
		this.tipo=tipo;
	}

}
