package CriterioAceitacao;

import Metaheuristicas.Config;
import Metaheuristicas.Media;
import Metaheuristicas.Solucao;

public abstract class CA 
{
	double limiarF;
	Media mFBL;
	int numIterUpdate;
	double eta;
	double teto=Integer.MAX_VALUE,tetoNovo=Integer.MAX_VALUE;
	int qtnPassouReal;
	int iterador=0,ultIterUpdate;
	
	public CA(Config config)
	{
		this.numIterUpdate=config.getNumIterUpdate();
		this.eta=config.getEta();
		this.mFBL=new Media(config.getNumIterUpdate());
	}
	
	public boolean aceitaSolucao(Solucao solucao, double distanciaBLEdge) 
	{
		return false;
	}

	public void update(double f)
	{
		iterador++;
		
		if(iterador%(numIterUpdate)==0)
		{
			teto=tetoNovo;
			tetoNovo=Integer.MAX_VALUE;
		}
		
		if(f<tetoNovo)
			tetoNovo=f;
			
		if(f<teto)
			teto=f;
		
		mFBL.setValor(f);
	}

	public double getEta() {
		return eta;
	}

	public double getLimiarF() {
		return limiarF;
	}

	public void setEta(double eta) {
		this.eta = eta;
	}
	
	public void setFluxoIdeal(double fluxoIdeal) {}
}
