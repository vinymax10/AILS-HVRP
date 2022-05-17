package Metaheuristicas;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import AjusteOmega.AODist;
import AjusteOmega.AjusteOmega;
import BuscaLocal.BuscaLocal;
import BuscaLocalIntra.BuscaLocalIntra;
import CriterioAceitacao.CA;
import CriterioAceitacao.CALimiar;
import Dados.Instancia;
import Dados.Instancias;
import Factibilizadores.Factibilizador;
import Pertubacao.HeuristicaAdicao;
import Pertubacao.Perturbacao;
import Pertubacao.PerturbacaoBuild;

public class AILS 
{
	Solucao solucao,solucaoReferencia;
	Solucao melhorSolucao;

	Instancia instancia;
	Distancia distEntreSolucoes;
	double melhorF;
	double MAX;
	double otimo;
	
	Config config;
	
	int numIterUpdate;
	int iterador;
	long inicio,inicioLastBestSol;
	long ini;
	double tempoMF,tempoFinal;
	int iteradorMF;
	 
	Media mFBL;
	
	DecimalFormat deci=new DecimalFormat("0.0000");
	Random rand=new Random();
	

	HashMap<String,AjusteOmega>configuradoresOmega=new HashMap<String,AjusteOmega>();

	double distanciaBLEdge;
	
	PerturbacaoBuild perturbacaoBuild;
	Perturbacao[] perturbadores;
	Perturbacao perturbacaoEscolhida;
	
	Factibilizador factibilizador;
	
	BuscaLocal buscaLocal;
	int numRotasMin;
	int numRotasMax;

	boolean aceitoCriterio,isMelhorSolucao;
	HeuristicaAdicao heuristicaAdicao;
	BuscaLocalIntra buscaLocalIntra;
	CA criterioAceitacao;
	
	boolean print=true;
	boolean factibilizou;

	double epsilon;
	
	public AILS(Instancia instancia,Config config,double d,double MAX)
	{
		this.instancia=instancia;

		this.buscaLocalIntra=new BuscaLocalIntra(instancia,config);
		this.epsilon=config.getEpsilon();
		
		this.solucao =new Solucao(instancia,config);
		this.solucaoReferencia =new Solucao(instancia,config);
		this.melhorSolucao =new Solucao(instancia,config);
		
		this.numIterUpdate=config.getNumIterUpdate();
		this.config=config;
		this.otimo=d;
		this.MAX=MAX;
		this.melhorF=Integer.MAX_VALUE;
		
		this.mFBL=new Media(numIterUpdate);

		this.perturbacaoBuild=new PerturbacaoBuild(); 
		this.numRotasMin=instancia.getNumRotasMin();
		this.numRotasMax=instancia.getNumRotasMax();
		this.factibilizador=new Factibilizador(instancia,config,buscaLocalIntra);
		this.buscaLocal=new BuscaLocal(instancia,config,buscaLocalIntra);
		
		this.criterioAceitacao=new CALimiar(config);
		
		this.distEntreSolucoes=new Distancia(instancia,config);
		
		this.perturbadores=new Perturbacao[config.getPerturbacao().length];

		AjusteOmega novo;
		for (int i = 0; i < config.getPerturbacao().length; i++) 
		{
			for (int K = numRotasMin; K <= numRotasMax; K++) 
			{
				novo=new AODist(config,instancia.getSize());
				configuradoresOmega.put(config.getPerturbacao()[i]+""+K, novo);
			}
		}
		
		for (int i = 0; i < perturbadores.length; i++) 
		{
			this.perturbadores[i]=perturbacaoBuild.ConstruirPerturbacao(instancia, config, 
			config.getPerturbacao()[i],configuradoresOmega);
		}
	}

	public void procurar()
	{
		iterador=0;
		inicio=System.currentTimeMillis();
		inicioLastBestSol=inicio;

		do 
		{
			solucaoReferencia.construirSolucao(numRotasMin);
			factibilizou=factibilizador.factibilizar(solucaoReferencia);
		}
		while (!factibilizou);
		
		buscaLocal.buscaLocal(solucaoReferencia,true);
		
		melhorSolucao.clone(solucaoReferencia);
		
//		while(melhorF>otimo&&MAX>iterador)
//		while((melhorF-otimo)>epsilon&&(MAX*instancia.getSize())>(System.currentTimeMillis()-inicioLastBestSol)/1000)
//		while(melhorF>otimo&&MAX>(System.currentTimeMillis()-inicio)/1000)
//		while((melhorF-otimo)>epsilon&&(MAX*instancia.getSize())>(System.currentTimeMillis()-inicio)/1000)
		while(melhorF>otimo&&MAX>(iterador-iteradorMF))
		{
			isMelhorSolucao=false;
			iterador++;
			
			perturbacao();

			buscaLocal();
			
			update();
			
			criterioAceitacao(solucao);
			
		}
		
		tempoFinal=(double)(System.currentTimeMillis()-inicio)/1000;
		
	}
	
	private void perturbacao()
	{
		do 
		{
			solucao.clone(solucaoReferencia);
			perturbacaoEscolhida=perturbadores[rand.nextInt(perturbadores.length)];
			perturbacaoEscolhida.perturbar(solucao);
			factibilizou=factibilizador.factibilizar(solucao);
		}
		while (!factibilizou);
	}
	
	private void buscaLocal()
	{
		buscaLocal.buscaLocal(solucao,true);
		mFBL.setValor(solucao.f);
		distanciaBLEdge=distEntreSolucoes.distanciaEdge(solucao,solucaoReferencia);
	}
	
	private void update()
	{
		analisaSolucaoAlto(false,"",solucao);
		
		perturbacaoEscolhida.getConfiguradorOmegaEscolhido().setDistancia(distanciaBLEdge);
	}
	
	public void criterioAceitacao(Solucao solucao)
	{
		if(criterioAceitacao.aceitaSolucao(solucao,distanciaBLEdge))
		{
			aceitoCriterio=true;
			solucaoReferencia.clone(solucao);
		}
		else
			aceitoCriterio=false;
	}
	
	public void analisaSolucao(boolean PR,String metodo,Solucao solucao2)
	{
		if((solucao2.f-melhorF)<-epsilon)
		{		
			isMelhorSolucao=true;
			melhorF=solucao2.f;
			
			inicioLastBestSol=System.currentTimeMillis();
			tempoMF=(double)(System.currentTimeMillis()-inicio)/1000;
			iteradorMF=iterador;
			
			melhorSolucao.clone(solucao2);

			if(print)
				System.out.println("melhorF: "+melhorF+" K: "+solucao2.NumRotas
				+" tempoMF: "+tempoMF+" gap: "+deci.format(getGap())
				+" metodo: "+metodo
				+" iteradorMF: "+iteradorMF
				+" HeuAdd: "+perturbacaoEscolhida.heuristicaAdicaoEscolhida
				);
			
		}
	}
	
	public static void main(String[] args) 
	{
		Instancias instancias=new Instancias();
		
		int pos=66;	

		Config config =new Config();
		Instancia instancia=new Instancia(
		"Instancias//"+instancias.instancias[pos].nome+".txt",config,
		instancias.instancias[pos].rounded,instancias.instancias[pos].variante);
		
		System.out.println(instancias.instancias[pos].nome
		+" otimo: "+instancias.instancias[pos].bestSolution.getOtimo());
		AILS igas=new AILS(instancia,config,instancias.instancias[pos].bestSolution.getOtimo(),20000);
		igas.procurar();
	}
	
	public Solucao getMelhorSolucao() {
		return melhorSolucao;
	}

	public double getMelhorF() {
		return melhorF;
	}

	public double getTempoMF() {
		return tempoMF;
	}
	
	public double getTempoFinal() {
		return tempoFinal;
	}

	public int getIteradorMF() {
		return iteradorMF;
	}

	public Media getMediaBL() {
		return mFBL;
	}

	public double getGap()
	{
		return 100*(double)((double)melhorF-otimo)/otimo;
	}
	
	public double getGapMBLDinan()
	{
		return 100*(double)(mFBL.mediaDinam-(double)melhorF)/(double)melhorF;
	}
	
	public double getGapMBLGlobal()
	{
		return 100*(double)(mFBL.mediaGlobal-(double)melhorF)/(double)melhorF;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print)
	{
		this.print = print;
	}

	public Solucao getSolucao() {
		return solucao;
	}

	public int getIterador() {
		return iterador;
	}

	public void analisaSolucaoAlto(boolean PR,String sufixo,Solucao solucao2)
	{
		analisaSolucao(PR,perturbacaoEscolhida.getTipoPerturbacao()+sufixo,solucao2);
	}

	public CA getCriterioAceitacao() {
		return criterioAceitacao;
	}

	public Perturbacao[] getPerturbadores() {
		return perturbadores;
	}
}
