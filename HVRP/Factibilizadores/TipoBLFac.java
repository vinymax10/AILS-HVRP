package Factibilizadores;

public enum TipoBLFac 
{
	NNN(1),
	NNS(2),
	NSN(3),
	SNN(4),
	NSS(5),
	SNS(6),
	SSN(7),
	SSS(8);
	
	final int tipo;
	
	TipoBLFac(int tipo)
	{
		this.tipo=tipo;
	}

}
