package AjusteOmega;

import Metaheuristicas.Config;

public class AOFixo implements AjusteOmega
{
	double omega,omegaMin,omegaMax;
	double omegaReal;
	
	public AOFixo(Config config,int size) 
	{
		this.omegaMin=1;
		this.omegaMax=size-2;
		this.omega = config.getOmegaFixo();
		this.omega=Math.min(omegaMax, Math.max(omega, omegaMin));
	}
	
	public double getOmegaReal() 
	{
		omegaReal=omega;
		omegaReal=Math.min(omegaMax, Math.max(omegaReal, omegaMin));
		
		return omegaReal;
	}

	@Override
	public void setDistancia(double distBL) {
	}
}
