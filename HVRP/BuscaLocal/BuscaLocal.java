package BuscaLocal;

import java.util.Arrays;
import java.util.Random;

import Avaliadores.AvaliadorCusto;
import Avaliadores.AvaliadorFac;
import Avaliadores.ExecutaMovimento;
import BuscaLocalIntra.BuscaLocalIntra;
import Dados.Instancia;
import Metaheuristicas.Config;
import Metaheuristicas.No;
import Metaheuristicas.NoPosMel;
import Metaheuristicas.Rota;
import Metaheuristicas.Solucao;

//Essa busca local registra todas as possiveis melhoras ao longo de uma passada. 
//Depois atualiza cada uma e verifica se ainda pode e se o custo compensa.
//Atualiza��o dinamica Best full

public class BuscaLocal
{
	private Rota rotas[];
	private  NoPosMel melhoras[];
	private  NoPosMel matrixMelhoras[][];
	private NoPosMel noMelhora;
	private int topMelhores=0;
	private int NumRotas;
	
	boolean removeuRota;
	int menorCusto;
	int tipoMov;
	Rota rotaI,rotaJ;
	int posRotaI,posRotaJ;
	No noRotaI,noRotaJ;

	int iterador;
	double f=0;
	
	int numMovimentosInter=6;

	int custoIdaVolta,custoIdaIda,custoVoltaIda,custoVoltaVolta;
	double custo;
	int custoIda,custoVolta;
	No auxRotaI,auxRotaJ;
	boolean trocou=false,trocouGloboal=true;
	double antF;
	Random rand=new Random();
	Rota rotaA,rotaB;
	int aleListMelhoresCustos;
	int pos;
	AvaliadorCusto avaliadorCusto;
	AvaliadorFac avaliadorFac;
	ExecutaMovimento executaMovimento;
	No solucao[];
	int limiteAdj;
	TipoBLIntraBL tipoBLIntraBL;
	BuscaLocalIntra buscaLocalIntra;
	double epsilon;

	public BuscaLocal(Instancia instancia,Config config, BuscaLocalIntra buscaLocalIntra)
	{
		this.avaliadorCusto=new AvaliadorCusto(instancia);
		this.avaliadorFac=new AvaliadorFac();
		this.executaMovimento=new ExecutaMovimento(instancia);
		this.rotas=new Rota[instancia.getNumRotasMax()];
		this.melhoras=new NoPosMel[instancia.getNumRotasMax()*(instancia.getNumRotasMax()-1)/2];
		this.matrixMelhoras=new NoPosMel[instancia.getNumRotasMax()][instancia.getNumRotasMax()];
		
		for (int i = 0; i < matrixMelhoras.length; i++)
		{
			for (int j = i+1; j < matrixMelhoras.length; j++) 
			{
				matrixMelhoras[i][j]=new NoPosMel(avaliadorCusto);
				matrixMelhoras[j][i]=matrixMelhoras[i][j];
			}
		}
		
		this.aleListMelhoresCustos=config.getAleListMelhoresCustos();
		this.limiteAdj=Math.min(config.getLimiteAdj(), instancia.getSize()-1);
		this.tipoBLIntraBL=config.getTipoBLIntraBL();
		this.buscaLocalIntra=buscaLocalIntra;
		this.epsilon=config.getEpsilon();
	}
	
	private void setSolucao(Solucao solucao) 
	{
		this.NumRotas=solucao.NumRotas;
		this.solucao=solucao.getSolucao();
		this.f=solucao.f;
		for (int i = 0; i < rotas.length; i++) 
			this.rotas[i]=solucao.rotas[i];
	}

	private void passaResultado(Solucao solucao) 
	{
		solucao.NumRotas=this.NumRotas;
		solucao.f=this.f;
		for (int i = 0; i < rotas.length; i++) 
			solucao.rotas[i]=this.rotas[i];
	}

	public void imprimirSolucao()
	{
//		System.out.println("f: "+f+ " NumRotas: "+NumRotas+" topInfac: "+topInfac);
		for (int i = 0; i < NumRotas; i++) 
		{
			System.out.println(rotas[i].toString());
		}
	}
	
	public void buscaLocal(Solucao solucao,boolean removerRotasVazias)
	{
		setSolucao(solucao);

		trocouGloboal=true;
		iterador=0;
		
		topMelhores=0;
		trocouGloboal=false;
		iterador++;
			
		for (int i = 0; i < NumRotas; i++) 
			rotas[i].setDemandaAcumulada();
		
		for (int i = 0; i < NumRotas; i++) 
		{
			if(rotas[i].numElements>1&&rotas[i].alterada)
				varrerRotas(rotas[i]);
		}
		
		if(topMelhores>0)
			executa();
		
		if(tipoBLIntraBL==TipoBLIntraBL.NS||tipoBLIntraBL==TipoBLIntraBL.SS)
			BuscaLocalIntra();
		
		passaResultado(solucao);
		if(removerRotasVazias)
			solucao.removeRotasVazias();
	}
	
	//-----------------------------------Factibilizando com o Best Improviment-------------------------------------------
	
	private void executa() 
	{
		while(topMelhores>0)
		{
			pos= rand.nextInt(Math.min(topMelhores,aleListMelhoresCustos));

			Arrays.sort(melhoras,0,topMelhores);
			rotaA=melhoras[pos].rotaA;
			rotaB=melhoras[pos].rotaB;
			
			f+=executaMovimento.aplicar(melhoras[pos]);
			
			if(tipoBLIntraBL==TipoBLIntraBL.SN||tipoBLIntraBL==TipoBLIntraBL.SS)
			{
				BuscaLocalIntra(rotaA);
				BuscaLocalIntra(rotaB);
			}
			
			rotaA.setDemandaAcumulada();
			rotaB.setDemandaAcumulada();
			
			int contAtivos=0;
			for (int k = 0; k < topMelhores; k++) 
			{
				if(melhoras[k].rotaA==rotaA||melhoras[k].rotaB==rotaA||melhoras[k].rotaA==rotaB||melhoras[k].rotaB==rotaB)
				{
					melhoras[k].limpar();
					contAtivos++;
				}
			}
	
			Arrays.sort(melhoras,0,topMelhores);
			topMelhores-=contAtivos;
			
			varrerRotas(rotaA);
			varrerRotas(rotaB);
			
		}
	}
	
	public void varrerRotas(Rota rota)
	{
		if(rota.numElements>1)
		{
			procuraBestCross(rota);
			procuraBestSHIFT(rota);
			procuraBestSWAP(rota);
		}
	}
	
	private void procuraBestSWAP(Rota rota)
	{
		auxRotaI=rota.inicio.prox;
		do
		{
			for (int j = 0; j < limiteAdj; j++) 
			{
				if(auxRotaI.getKnn()[j]!=0&&solucao[auxRotaI.getKnn()[j]-1].rota!=rota)
				{
					auxRotaJ=solucao[auxRotaI.getKnn()[j]-1];
					if(avaliadorFac.ganhoSWAP(auxRotaI, auxRotaJ)>=0)
					{
						custo=avaliadorCusto.custoSWAP(auxRotaI,auxRotaJ);
						noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
						
						if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 4, auxRotaI, auxRotaJ,custo);
						}
					}
				}
			}
			
			auxRotaI=auxRotaI.prox;
		}
		while(auxRotaI!=rota.inicio);
	}
	
	private void procuraBestSHIFT(Rota rota)
	{
		auxRotaI=rota.inicio.prox;
		do
		{
			for (int j = 0; j < limiteAdj; j++) 
			{
				if(auxRotaI.getKnn()[j]==0)
				{
					for (int i = 0; i < NumRotas; i++) 
					{
						if(rotas[i]!=rota)
						{
							auxRotaJ=rotas[i].inicio;
							if(avaliadorFac.ganhoSHIFT(auxRotaI, auxRotaJ)>=0)
							{
								custo=avaliadorCusto.custoSHIFT(auxRotaI,auxRotaJ);
								noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
								
								if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
								{
									if(!noMelhora.ativo)
										melhoras[topMelhores++]=noMelhora;

									noMelhora.setNoMelhora(custo, 7, auxRotaI, auxRotaJ,custo);
								}
							}
						}
					}
				}
				else if(solucao[auxRotaI.getKnn()[j]-1].rota!=rota)
				{
					auxRotaJ=solucao[auxRotaI.getKnn()[j]-1];
					if(avaliadorFac.ganhoSHIFT(auxRotaI, auxRotaJ)>=0)
					{
						custo=avaliadorCusto.custoSHIFT(auxRotaI,auxRotaJ);
						noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
						
						if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 7, auxRotaI, auxRotaJ,custo);
						}
					}
					
					if(avaliadorFac.ganhoSHIFT(auxRotaJ, auxRotaI)>=0)
					{
						custo=avaliadorCusto.custoSHIFT(auxRotaJ, auxRotaI);
						noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
						
						if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 7, auxRotaJ,auxRotaI, custo);
						}
					}
				}
			}
			
			auxRotaI=auxRotaI.prox;
		}
		while(auxRotaI!=rota.inicio);
	}
	
	private void procuraBestCross(Rota rota)
	{
		auxRotaI=rota.inicio;
		do
		{
			for (int j = 0; j < limiteAdj; j++) 
			{
				if(auxRotaI.getKnn()[j]==0)
				{
					for (int i = 0; i < NumRotas; i++) 
					{
						if(rotas[i]!=rota)
						{
							auxRotaJ=rotas[i].inicio;
							if(avaliadorFac.ganhoCross(auxRotaI, auxRotaJ)>=0)
							{
								custo=avaliadorCusto.custoCross(auxRotaI,auxRotaJ);
								noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
								
								if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
								{
									if(!noMelhora.ativo)
										melhoras[topMelhores++]=noMelhora;

									noMelhora.setNoMelhora(custo, 11, auxRotaI, auxRotaJ,custo);
								}
							}
							
							if(avaliadorFac.ganhoCrossInvertido(auxRotaI, auxRotaJ)>=0)
							{
								custo=avaliadorCusto.custoCrossInvertido(auxRotaI,auxRotaJ);
								noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
								
								if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
								{
									if(!noMelhora.ativo)
										melhoras[topMelhores++]=noMelhora;

									noMelhora.setNoMelhora(custo, 12, auxRotaI, auxRotaJ,custo);
								}
							}
						}
					}
				}
				else if(solucao[auxRotaI.getKnn()[j]-1].rota!=rota)
				{
					auxRotaJ=solucao[auxRotaI.getKnn()[j]-1];
					if(avaliadorFac.ganhoCross(auxRotaI, auxRotaJ)>=0)
					{
						custo=avaliadorCusto.custoCross(auxRotaI,auxRotaJ);
						noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
						
						if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 11, auxRotaI, auxRotaJ,custo);
						}
					}
					if(avaliadorFac.ganhoCrossInvertido(auxRotaI, auxRotaJ)>=0)
					{
						custo=avaliadorCusto.custoCrossInvertido(auxRotaI,auxRotaJ);
						noMelhora=matrixMelhoras[auxRotaI.rota.nomeRota][auxRotaJ.rota.nomeRota];
						
						if(((custo-noMelhora.custo)<-epsilon)&&((custo-0)<-epsilon))
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 12, auxRotaI, auxRotaJ,custo);
						}
					}
				}
			}
			
			auxRotaI=auxRotaI.prox;
		}
		while(auxRotaI!=rota.inicio);
	}
	
	private void BuscaLocalIntra(Rota rota)
	{
		f+=buscaLocalIntra.buscaLocalIntra(rota, solucao);
	}
	
	private void BuscaLocalIntra()
	{
		for (int i = 0; i < NumRotas; i++)
		{
			if(rotas[i].alterada)
				f+=buscaLocalIntra.buscaLocalIntra(rotas[i], solucao);
		}
	}
	
	public void buscaLocalIntra(Solucao solucao)
	{
		setSolucao(solucao);
		BuscaLocalIntra();
		passaResultado(solucao);
	}
}
