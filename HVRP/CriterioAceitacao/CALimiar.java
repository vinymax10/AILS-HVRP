package CriterioAceitacao;

import Metaheuristicas.Config;
import Metaheuristicas.Solucao;

public class CALimiar extends CA
{
	public CALimiar(Config config)
	{
		super(config);
	}
	
	public boolean aceitaSolucao(Solucao solucao, double distanciaBLEdge)
	{
		update(solucao.f);
		
		limiarF=(int)(teto+(eta*(mFBL.getMediaDinam()-teto)));
		if(solucao.f<=limiarF)
		{
			qtnPassouReal++;
			return true;
		}
		else
			return false;
	}
	
	@Override
	public double getEta() {
		return eta;
	}

	@Override
	public double getLimiarF() {
		return limiarF;
	}
	
	public void setEta(double eta) {
		this.eta = eta;
	}
	
	public void setFluxoIdeal(double fluxoIdeal) {}
}
