package Pertubacao;

import java.util.HashMap;
import java.util.Random;

import AjusteOmega.AjusteOmega;
import Dados.Instancia;
import Dados.Veiculo;
import Metaheuristicas.Config;
import Metaheuristicas.No;
import Metaheuristicas.Rota;
import Metaheuristicas.Solucao;
import Metaheuristicas.Variante;

public abstract class Perturbacao 
{
	protected Rota rotas[];
	protected int NumRotas;
	protected No solucao[];
	protected double f=0;
	protected Random rand=new Random();
	protected int NumRotasMin;
	protected int NumRotasMax;
	public double omega;
	AjusteOmega configuradorOmegaEscolhido;
	Config config;
	protected No candidatos[];
	protected int ordem[];
	protected int contCandidatos;

	HeuristicaAdicao[]heuristicasAdicao;
	public HeuristicaAdicao heuristicaAdicaoEscolhida;
	
	No no,ant,prox;
	
	public TipoPerturbacao tipoPerturbacao;
	int size;
	HashMap<String, AjusteOmega> configuradoresOmega;
	
	double bestCusto;
	int bestDist;
	int numIterUpdate;
	int posHeuEscolhida;
	
	double custo;
	double custoAnt;
	double distancia,distanciaBest;
	
	No bestNo,aux;
	Instancia instancia;
	int limiteAdj;
	Veiculo veiculoAuxVeiculo;
	double alfa;
	int indexA,indexB;
	int varicaoQtnRotas;
	boolean variouTipoVeiculo;
	
	public Perturbacao(Instancia instancia,Config config,
	HashMap<String, AjusteOmega> configuradoresOmega) 
	{
		this.config=config;
		this.instancia=instancia;
		this.heuristicasAdicao=config.getHeuristicasAdicao();
		this.NumRotasMin=instancia.getNumRotasMin();
		this.NumRotasMax=instancia.getNumRotasMax();
		this.size=instancia.getSize()-1;
		this.candidatos=new No[size];
		this.ordem=new int[size];
		this.configuradoresOmega=configuradoresOmega;
		this.numIterUpdate=config.getGamma();
		this.limiteAdj=config.getVarphi();
		this.veiculoAuxVeiculo=new Veiculo();
		this.alfa=config.getAlpha();
		this.varicaoQtnRotas=config.getVaricaoQtnRotas();
	}

	protected void mudaOrdem(int topOrdem)
	{
		int aux;
		for (int i = 0; i < topOrdem; i++)
		{
			indexA=rand.nextInt(topOrdem);
			indexB=rand.nextInt(topOrdem);
			aux=ordem[indexA];
			ordem[indexA]=ordem[indexB];
			ordem[indexB]=aux;
		}
	}
	
	public void perturbar(Solucao s){}
	
	protected void setSolucao(Solucao s)
	{
		this.NumRotas=s.getNumRotas();
		this.rotas=s.rotas;
		this.solucao=s.getSolucao();
		this.f=s.f;
		for (int i = 0; i < NumRotas; i++) 
			rotas[i].alterada=false;
	}
	
	protected void passaSolucao(Solucao s)
	{
		s.f=f;
		s.NumRotas=this.NumRotas;
	}
	
	protected void VariandoNumRotas(Solucao s)
	{
		variarTipoVeiculo(s);
		int passo=1;

		configuradorOmegaEscolhido=configuradoresOmega.get(tipoPerturbacao+""+NumRotas);
		omega=configuradorOmegaEscolhido.getOmegaReal();
		omega=Math.min(omega, size);
		
		posHeuEscolhida=rand.nextInt(heuristicasAdicao.length);
		heuristicaAdicaoEscolhida=heuristicasAdicao[posHeuEscolhida];

//		variando quantidade rotas
//		if(rand.nextDouble()<probVaricaoQtnRotas)
		if(rand.nextDouble()<alfa)
		{
			if(this.NumRotas>NumRotasMin&&this.NumRotas<NumRotasMax)
			{
				if(rand.nextBoolean())
					this.NumRotas-=passo;
				else 
					this.NumRotas+=passo;
			}
			else if(this.NumRotas>NumRotasMin)
				this.NumRotas-=passo;
			else if(this.NumRotas<NumRotasMax)
				this.NumRotas+=passo;
		}

		int deixaFora=-1;
		if(NumRotas<s.getNumRotas())
		{
			deixaFora=rand.nextInt(s.getNumRotas());
			contCandidatos=0;
			
			no=rotas[deixaFora].inicio.prox;
			while(no!=rotas[deixaFora].inicio)
			{
				prox=no.prox;
				candidatos[contCandidatos++]=no;
				no.antOld=no.ant;
				no.proxOld=no.prox;
				f+=rotas[deixaFora].remove(no);
				no=prox;
			}
			
			Rota aux=rotas[deixaFora];
			
			if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMFD||instancia. getVariante()==Variante.FSMF)
				f-=aux.getVeiculo().getF();
			
			rotas[deixaFora]=rotas[s.getNumRotas()-1];
			rotas[s.getNumRotas()-1]=aux;
			
			for (int i = 0; i < contCandidatos; i++) 
			{
				no=candidatos[i];
				bestNo=getNo(no);
				f+=bestNo.rota.addDepois(no, bestNo);
			}
			
//			omega=Math.max(0, omega-contCandidatos);
		}
		else if(NumRotas>s.getNumRotas())
		{
			f+=rotas[NumRotas-1].limpar();
		}
		
		if(variouTipoVeiculo)
			omega=0;
	}
	
	private void variarTipoVeiculo(Solucao s)
	{
		if(rand.nextDouble()<alfa)
		{
			variouTipoVeiculo=true;
			for (int i = 0; i < varicaoQtnRotas; i++) 
			{
				if(instancia. getVariante()==Variante.FSMD||instancia. getVariante()==Variante.FSMF||instancia. getVariante()==Variante.FSMFD)
				{
					if(rand.nextBoolean())
					{
						indexA=rand.nextInt(NumRotas);
						f+=rotas[indexA].alterarVeiculo(s.getVeiculo());
					}
					else
					{
						indexB=NumRotas+rand.nextInt(rotas.length-NumRotas);
						rotas[indexB].alterarVeiculo(s.getVeiculo());
					}
				}
				else
				{
					indexA=rand.nextInt(rotas.length);
					Veiculo veiculoA=rotas[indexA].getVeiculo();
					indexB=rand.nextInt(rotas.length);
					Veiculo veiculoB=rotas[indexB].getVeiculo();
					
					veiculoAuxVeiculo.setF(veiculoA.getF());
					veiculoAuxVeiculo.setQ(veiculoA.getQ());
					veiculoAuxVeiculo.setR(veiculoA.getR());
					
					if(indexA<NumRotas)
						f+=rotas[indexA].alterarVeiculo(veiculoB);
					else
						rotas[indexA].alterarVeiculo(veiculoB);
					
					if(indexB<NumRotas)
						f+=rotas[indexB].alterarVeiculo(veiculoAuxVeiculo);
					else
						rotas[indexB].alterarVeiculo(veiculoAuxVeiculo);
				}
			}
		}
		else
			variouTipoVeiculo=false;
	}
	
	protected No getNo(No no)
	{
		switch(heuristicaAdicaoEscolhida)
		{
			case DISTANCIA: return getBestKNNNo2(no,1);
			case BESTCUSTO: return getBestKNNNo2(no,limiteAdj);
			default: return null;
		}
	}
	
	private boolean criterioSelecao(No no,No ant,No prox)
	{
		if(ant.nome!=no.antOld.nome&&ant.nome!=no.proxOld.nome&&prox.nome!=no.antOld.nome&&prox.nome!=no.proxOld.nome) 
			return true;
		return false;
	}
	
	protected No getBestKNNNo2(No no,int limite)
	{
		bestCusto=Double.MAX_VALUE;
		distanciaBest=Double.MAX_VALUE;
		boolean entrou=false;
		bestNo=null;
		int cont=0;
		entrou=false;
		for (int i = 0; i < no.knn.length&&cont<limite; i++) 
		{
			if(no.knn[i]==0)
			{
				for (int j = 0; j < NumRotas; j++) 
				{
					aux=rotas[j].inicio;
					if(criterioSelecao(no,aux,aux.prox))
					{
						entrou=true;
						custo=instancia.dist(aux.nome,no.nome,aux.rota)+instancia.dist(no.nome,aux.prox.nome,aux.rota)-instancia.dist(aux.nome,aux.prox.nome,aux.rota);
						if(custo<bestCusto)
						{
							bestCusto=custo;
							bestNo=aux;
						}
					}
				}
				if(entrou)
					cont++;
			}
			else
			{
				aux=solucao[no.knn[i]-1];
				if(aux.jaInserido&&criterioSelecao(no,aux,aux.prox))
				{
					cont++;
					custo=instancia.dist(aux.nome,no.nome,aux.rota)+instancia.dist(no.nome,aux.prox.nome,aux.rota)-instancia.dist(aux.nome,aux.prox.nome,aux.rota);
					if(custo<bestCusto)
					{
						bestCusto=custo;
						bestNo=aux;
					}
				}
			}
		}
		
		if(bestNo==null)
		{
			for (int i = 0; i < solucao.length; i++) 
			{
				aux=solucao[i];
				if(aux.jaInserido&&criterioSelecao(no,aux,aux.prox))
				{
					custo=instancia.dist(aux.nome,no.nome,aux.rota)+instancia.dist(no.nome,aux.prox.nome,aux.rota)-instancia.dist(aux.nome,aux.prox.nome,aux.rota);
					if(custo<bestCusto)
					{
						bestCusto=custo;
						bestNo=aux;
					}
				}
			}
		}
		
		custo=instancia.dist(bestNo.nome,no.nome,bestNo.rota)+instancia.dist(no.nome,bestNo.prox.nome,bestNo.rota)-instancia.dist(bestNo.nome,bestNo.prox.nome,bestNo.rota);
		custoAnt=instancia.dist(bestNo.ant.nome,no.nome,bestNo.rota)+instancia.dist(no.nome,bestNo.nome,bestNo.rota)-instancia.dist(bestNo.ant.nome,bestNo.nome,bestNo.rota);
		if(custo<custoAnt)
			return bestNo;
		
		return bestNo.ant;
	}
	
	public int getPosHeuEscolhida() {
		return posHeuEscolhida;
	}

	public AjusteOmega getConfiguradorOmegaEscolhido() {
		return configuradorOmegaEscolhido;
	}
	
	public TipoPerturbacao getTipoPerturbacao() {
		return tipoPerturbacao;
	}
	
}