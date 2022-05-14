package AjusteOmega;

import Metaheuristicas.Config;
import Metaheuristicas.Media;

public class AODist implements AjusteOmega
{
	double omega,omegaMin,omegaMax;
	Media distBLMedia;
	int iterador=0;
	double distObtida;
	Media omegaMedio;
	double omegaReal;
	int numIterUpdate;
	double distIdeal;
	
	public AODist(Config config, int size) 
	{
		this.omega = config.getDistM();
		this.numIterUpdate = config.getNumIterUpdate();
		this.omegaMin=1;
		this.omegaMax=size-2;
		this.omegaMedio=new Media(config.getNumIterUpdate());
		this.distBLMedia=new Media(config.getNumIterUpdate());

		this.distIdeal=config.getDistM();
	}
	
	public void ajusteOmega()
	{
		distObtida=distBLMedia.getMediaDinam();

		omega+=((omega/distObtida*distIdeal)-omega);

		omega=Math.min(omegaMax, Math.max(omega, omegaMin));
		
		omegaMedio.setValor(omega);
		
		iterador=0;
	}
	
	public void setDistancia(double distBL)
	{
		iterador++;
		
		distBLMedia.setValor(distBL);

		if(iterador%numIterUpdate==0)
			ajusteOmega();
	}
	
	public double getOmegaReal() 
	{
		omegaReal=omega;
		omegaReal=Math.min(omegaMax, Math.max(omegaReal, omegaMin));
		return omegaReal;
	}

}
