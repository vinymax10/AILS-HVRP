package Factibilizadores;

public enum TipoCustoFac 
{
	C_C(0),
	C_CDG(1),
	CMG_C(2),
	CMG_CDG(3),
	G_CDG(4),
	CDG_CDG(5);
	
	final int tipo;
	
	TipoCustoFac(int tipo)
	{
		this.tipo=tipo;
	}

}
