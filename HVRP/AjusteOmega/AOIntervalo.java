package AjusteOmega;

import java.util.Random;

import Metaheuristicas.Config;
import Metaheuristicas.Media;

public class AOIntervalo implements AjusteOmega
{
	double omega,omegaMin,omegaMax;
	Media omegaMedio;
	
	double omegaReal;
	Random rand=new Random();
	int size;
	
	public AOIntervalo( Config config,int size) 
	{
		this.omegaMin=config.getOmegaMin();
		this.omegaMax=config.getOmegaMax();
		this.omegaMedio=new Media(config.getGamma());
		this.size=size;
		this.omega = config.getOmegaFixo();
		this.omega=Math.min(size-2, Math.max(omega, 1));
	}
	
	public void setDistancia(double distBL)
	{
		omega=omegaMin;
		if((omegaMax-omegaMin)>0)
			omega+=rand.nextInt((int)(omegaMax-omegaMin));
	}
	
	public double getOmegaReal() 
	{
		omegaReal=omega;
		omegaReal=Math.min(size-2, Math.max(omegaReal, 1));
		
		return omegaReal;
	}

	public Media getOmegaMedio() 
	{
		return omegaMedio;
	}

}
