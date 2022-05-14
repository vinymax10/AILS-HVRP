package Metaheuristicas;

import Dados.Instancia;

public class Distancia 
{
	boolean jaNomeouA[];
	boolean jaNomeouB[];
	int topForaRota=0;
	int NumRotas;
	int qtnRotas[];
	No solucaoB[];
	No solucaoA[];
	private Rota rotasA[];
	int topParRotasVet=0;
	Instancia instancia;
	
	public Distancia(Instancia instancia,Config config)
	{
		this.instancia=instancia;
		this.qtnRotas=new int[instancia.getNumRotasMax()];
		this.jaNomeouA=new boolean[instancia.getNumRotasMax()];
		this.jaNomeouB=new boolean[instancia.getNumRotasMax()];
	}
	
	public int distanciaEdge(Solucao a, Solucao b)
	{
		this.solucaoB=b.getSolucao();
		this.rotasA=a.rotas;
		this.solucaoA=a.getSolucao();
		
		int viz;
		int dist=0;
		for (int i = 0; i < solucaoA.length; i++)
		{
			viz=solucaoA[i].prox.nome;
			if(solucaoB[i].prox.nome!=viz&&viz!=solucaoB[i].ant.nome)
				dist++;

			if(solucaoA[i].ant.nome==0)
			{
				if(solucaoB[i].prox.nome!=0&&0!=solucaoB[i].ant.nome)
					dist++;
			}
		}
		
		return dist;
	}
	
	public int distanciaEdge2(Solucao a, Solucao b)
	{
		this.solucaoB=b.getSolucao();
		this.rotasA=a.rotas;
		this.solucaoA=a.getSolucao();

		int distancia=0;
		No no;
		for (int indexRota = 0; indexRota < a.getNumRotas(); indexRota++) 
		{
			no=rotasA[indexRota].inicio.prox;
			
			if(solucaoB[no.nome-1].ant.nome!=0&&solucaoB[no.nome-1].prox.nome!=0)
				distancia++;
			do
			{
				if(solucaoB[no.nome-1].prox.nome!=no.prox.nome&&solucaoB[no.nome-1].ant.nome!=no.prox.nome)
					distancia++;
				
				no=no.prox;
			}
			while(no!=rotasA[indexRota].inicio);
		}
		return distancia;
	}
	
	public int calculaNosForaRota()
	{
		topForaRota=0;
		for (int i = 0; i < solucaoB.length; i++)
		{
			if(solucaoA[i].rota.nomeRota!=solucaoB[i].rota.nomeRota)
				topForaRota++;
		}
//		System.out.println("topForaRota: "+topForaRota);
		return topForaRota;
	}
}
