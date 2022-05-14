package Factibilizadores;

import java.util.Arrays;
import java.util.Random;

import Avaliadores.AvaliadorCusto;
import Avaliadores.AvaliadorFac;
import Avaliadores.ExecutaMovimento;
import BuscaLocalInstra.BuscaLocalIntra;
import Dados.Instancia;
import Dados.Veiculo;
import Metaheuristicas.Config;
import Metaheuristicas.No;
import Metaheuristicas.NoPosMel;
import Metaheuristicas.Rota;
import Metaheuristicas.Solucao;
import Metaheuristicas.Variante;

//Factibilizador Best8 simples. custo>=0 custo+1/ganho e custo<0 -> custo*ganho
//Permite a troca de uma rota infactivel pela factibilizacao da outra.
//Adiciona somente o melhor movimento para cada combina��o de duas rotas.
//Dinamico.

public class Factibilizador 
{
	private Rota rotas[];
	private  NoPosMel melhoras[];
	private  NoPosMel matrixMelhoras[][];

	private NoPosMel noMelhora;

	private int topMelhores=0;
	private int NumRotas;
	int numTrocas=0;
	
	boolean removeuRota;
	double menorCusto;
	int tipoMov;
	Rota rotaI,rotaJ;
	int posRotaI,posRotaJ;
	No noRotaI,noRotaJ;

	int iterador,iterador2;
	double f=0;
	
	int numMovimentos=6;
	private int ordemMetodo[];
	
	double custoIdaVolta,custoIdaIda,custoVoltaIda,custoVoltaVolta;
	double custoIda,custoVolta;
	No auxSai,auxEntra;
	int ganho;
	double custo;
	double custoAvaliacao;
	TipoBLFac tipoBLFac;
	Rota rotaA,rotaB;
	int aleListMelhoresCustos;
	int pos;
	Random rand=new Random();
	TipoCustoFac tipoCustoFac;
	
	TipoCustoFac tipoCustoFacs[]={TipoCustoFac.C_C, TipoCustoFac.C_CDG, 
	TipoCustoFac.CMG_C, TipoCustoFac.CMG_CDG, TipoCustoFac.G_CDG};
	
	AvaliadorCusto avaliadorCusto;
	AvaliadorFac avaliadorFac;
	ExecutaMovimento executaMovimento;
	No solucao[];
	int limiteAdj;
	int qtnNegativos;
	double antF;
	BuscaLocalIntra buscaLocalIntra;
	double epsilon;
	Instancia instancia;
	
	public Factibilizador(Instancia instancia,Config config, BuscaLocalIntra buscaLocalIntra)
	{
		this.instancia=instancia;
		this.avaliadorCusto=new AvaliadorCusto(instancia);
		this.avaliadorFac=new AvaliadorFac();
		this.executaMovimento=new ExecutaMovimento(instancia);
		this.rotas=new Rota[instancia.getNumRotasMax()];
		this.melhoras=new NoPosMel[instancia.getNumRotasMax()*(instancia.getNumRotasMax()-1)/2];
		this.matrixMelhoras=new NoPosMel[instancia.getNumRotasMax()][instancia.getNumRotasMax()];
		
		for (int i = 0; i < matrixMelhoras.length; i++)
		{
			for (int j = 0; j < matrixMelhoras.length; j++) 
			{
				matrixMelhoras[i][j]=new NoPosMel(avaliadorCusto);
				matrixMelhoras[j][i]=matrixMelhoras[i][j];
			}
		}
		
		this.tipoBLFac=config.getTipoBLFac();
		this.aleListMelhoresCustos=config.getAleListMelhoresCustos();
		this.ordemMetodo=new int [numMovimentos];
		for (int i = 0; i < ordemMetodo.length; i++) 
			ordemMetodo[i]=i;
		
		this.tipoCustoFac=config.getTipoCustoFac();
		this.limiteAdj=Math.min(config.getLimiteAdj(), instancia.getSize()-1);
		
		this.buscaLocalIntra=buscaLocalIntra;
		this.epsilon=config.getEpsilon();
	}
	
	private boolean factivel() 
	{
		for (int i = 0; i < NumRotas; i++)
		{
			if(rotas[i].espacoLivre()<0)
				return false;
		}
		return true;
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
	
	public boolean factibilizar(Solucao solucao)
	{
//		System.out.println("----------------INICIO------------------");
		setSolucao(solucao);
		numTrocas=0;
		boolean factivel=false;
		boolean aumentouQ=false;
		Veiculo veiculo;
		boolean naoConseguiFactibilizar=false;
		
//		tipoCustoFac=tipoCustoFacs[rand.nextInt(tipoCustoFacs.length)];
		
		if(tipoBLFac==TipoBLFac.SNN||tipoBLFac==TipoBLFac.SSN||tipoBLFac==TipoBLFac.SNS||tipoBLFac==TipoBLFac.SSS)
			BuscaLocalIntra();
		
		iterador=0;
		iterador2=0;
		do
		{
			topMelhores=0;
			iterador++;
//			System.out.println("iterador: "+iterador);
			for (int i = 0; i < NumRotas; i++) 
				rotas[i].setDemandaAcumulada();
			
			for (int i = 0; i < NumRotas; i++) 
			{
				if(rotas[i].alterada)
					varrerRotas(rotas[i]); //pode varrer somente infac
			}
			
			if(topMelhores>0)
				executa();

			if(factivel())
			{
				if(tipoBLFac==TipoBLFac.NNS||tipoBLFac==TipoBLFac.NSS||tipoBLFac==TipoBLFac.SNS||tipoBLFac==TipoBLFac.SSS)
					BuscaLocalIntra();
				
				factivel=true;
			}
			else
			{
				if(rotas.length==NumRotas&&
				(instancia.getVariante()==Variante.FSMD||instancia.getVariante()==Variante.FSMF||instancia.getVariante()==Variante.FSMFD))
				{
					aumentouQ=false;
					for (int i = 0; i < NumRotas&&!aumentouQ; i++) 
					{
						if(!rotas[i].isFactivel())
						{
							veiculo=solucao.getVeiculoMaisCapacidade(rotas[i].getVeiculo());
							if(veiculo.getQ()>rotas[i].getVeiculo().getQ())
							{
								f+=rotas[i].alterarVeiculo(veiculo);
								aumentouQ=true;
							}
						}
					}
					
					if(!aumentouQ)
					{
						for (int i = 0; i < NumRotas&&!aumentouQ; i++) 
						{
							veiculo=solucao.getVeiculoMaisCapacidade(rotas[i].getVeiculo());
							if(veiculo.getQ()>rotas[i].getVeiculo().getQ())
							{
								f+=rotas[i].alterarVeiculo(veiculo);
								aumentouQ=true;
							}
						}
					}
				}
				else
				{
					if(NumRotas==rotas.length)
						naoConseguiFactibilizar=true;
					else
					{
						NumRotas++;
						f+=rotas[NumRotas-1].limpar();
					}
				}
				
			}
//			System.out.println("NumRotas: "+NumRotas+
//			" naoConseguiFactibilizar: "+naoConseguiFactibilizar+" factivel: "+factivel);
		}
		while(!factivel&&!naoConseguiFactibilizar);
		
		passaResultado(solucao);
		solucao.removeRotasVazias();
		
		return factivel;
		
	}
	
	//-----------------------------------Factibilizando com o Best Improviment-------------------------------------------
	
	private void executa() 
	{
		while(topMelhores>0)
		{
			Arrays.sort(melhoras,0,topMelhores);
			
			qtnNegativos=0;
			for (int i = 0; i < topMelhores; i++) 
			{
				if(melhoras[i].custoAvaliacao<0)
					qtnNegativos++;
			}
//			
			if(qtnNegativos==0)
				qtnNegativos=topMelhores;
			
			pos= rand.nextInt(Math.min(qtnNegativos,aleListMelhoresCustos));
//			pos=rand.nextInt(qtnNegativos);
			
//			iterador2++;
//			System.out.println("iterador: "+iterador+" iterador2: "+iterador2);
//			System.out.println("------------------------------");
//			for (int i = 0; i < topMelhores; i++) 
//			{
//				System.out.println(melhoras[i]);
//			}
			rotaA=melhoras[pos].rotaA;
			rotaB=melhoras[pos].rotaB;
			f+=executaMovimento.aplicar(melhoras[pos]);
			
			if(tipoBLFac==TipoBLFac.NSN||tipoBLFac==TipoBLFac.NSS||tipoBLFac==TipoBLFac.SSN||tipoBLFac==TipoBLFac.SSS)
			{
				BuscaLocalIntra(rotaA);
				BuscaLocalIntra(rotaB);
			}
			
			rotaA.setDemandaAcumulada();
			rotaB.setDemandaAcumulada();
			
			int contAtivos=0;
			for (int k = 0; k < topMelhores; k++) 
			{
				if(melhoras[k].rotaA==rotaA||melhoras[k].rotaA==rotaB||melhoras[k].rotaB==rotaA||melhoras[k].rotaB==rotaB)
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
//		System.out.println("Varrendo rota: "+rota);
		if(rota.getNumElements()>1)
		{
			procuraBestCross(rota); 
			procuraBestSHIFT(rota);
			procuraBestSWAP(rota);
		}
	}
	
	public void calcCusto()
	{
		switch(tipoCustoFac)
		{
			case C_C: 
						custoAvaliacao=custo;
						break;
			case C_CDG: 
						if(custo>=0)
							custoAvaliacao=((double)custo+1)/ganho;
						else
							custoAvaliacao=custo;
						break;
		
			case CMG_CDG:
						if(custo>=0)
							custoAvaliacao=((double)custo+1)/ganho;
						else
							custoAvaliacao=(double)custo*ganho;
						break;
			
			case CDG_CDG:
						if(custo>=0)
							custoAvaliacao=((double)custo+1)/ganho;
						else
							custoAvaliacao=(double)custo/ganho;
						break;
						
			case CMG_C: 
						if(custo<0)
							custoAvaliacao=(double)custo*ganho;
						else
							custoAvaliacao=custo;
						break;
			case G_CDG: 
						if(custo>=0)
							custoAvaliacao=((double)custo+1)/ganho;
						else
							custoAvaliacao=-ganho;
						break;
		}
	}
	
	private void procuraBestSWAP(Rota rota)
	{
		auxSai=rota.inicio.prox;
		do
		{
			for (int j = 0; j < limiteAdj; j++) 
			{
				if(auxSai.getKnn()[j]!=0&&auxSai.rota.isFactivel()^solucao[auxSai.getKnn()[j]-1].rota.isFactivel())
				{
					auxEntra=solucao[auxSai.getKnn()[j]-1];
					ganho=avaliadorFac.ganhoSWAP(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoSWAP(auxSai, auxEntra);
						calcCusto();
						
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 4, auxSai, auxEntra,custoAvaliacao,ganho);
						}
					}
				}
			}
			auxSai=auxSai.prox;
		}
		while(auxSai!=rota.inicio);
	}
	
	private void procuraBestSHIFT(Rota rota)
	{
		auxSai=rota.inicio.prox;
		do
		{
			for (int i = 0; i < NumRotas; i++) 
			{
				if(!auxSai.rota.isFactivel()&&rotas[i].isFactivel())
				{
					auxEntra=rotas[i].inicio;
					ganho=avaliadorFac.ganhoSHIFT(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoSHIFT(auxSai, auxEntra);
						calcCusto();
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
//						System.out.println(noMelhora);
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 7, auxSai, auxEntra,custoAvaliacao,ganho);
						}
					}
				}
			}
			
			for (int j = 0; j < limiteAdj; j++) 
			{
				if(auxSai.getKnn()[j]!=0&&!auxSai.rota.isFactivel()&&solucao[auxSai.getKnn()[j]-1].rota.isFactivel())
				{
					auxEntra=solucao[auxSai.getKnn()[j]-1];
					ganho=avaliadorFac.ganhoSHIFT(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoSHIFT(auxSai, auxEntra);
						calcCusto();
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 7, auxSai, auxEntra,custoAvaliacao,ganho);
						}
					}
				}
				
				if(auxSai.getKnn()[j]!=0&&auxSai.rota.isFactivel()&&!solucao[auxSai.getKnn()[j]-1].rota.isFactivel())
				{
					auxEntra=solucao[auxSai.getKnn()[j]-1];
					ganho=avaliadorFac.ganhoSHIFT(auxEntra, auxSai);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoSHIFT(auxEntra, auxSai);
						calcCusto();
						
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 7, auxEntra ,auxSai ,custoAvaliacao,ganho);
						}
					}
				}
				
			}
			auxSai=auxSai.prox;
		}
		while(auxSai!=rota.inicio);
	}
	
	private void procuraBestCross(Rota rota)
	{
		auxSai=rota.inicio;
		do
		{
			for (int i = 0; i < NumRotas; i++) 
			{
				if(auxSai.rota.isFactivel()^rotas[i].isFactivel())
				{
					auxEntra=rotas[i].inicio;
					ganho=avaliadorFac.ganhoCross(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoCross(auxSai, auxEntra);
						calcCusto();
						
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 11, auxSai, auxEntra,custoAvaliacao,ganho);
						}
						
					}
					ganho=avaliadorFac.ganhoCrossInvertido(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoCrossInvertido(auxSai, auxEntra);
						calcCusto();
						
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 12, auxSai, auxEntra,custoAvaliacao,ganho);
						}
					}
				}
			}
			
			for (int j = 0; j < limiteAdj; j++) 
			{
				if(auxSai.getKnn()[j]!=0&&auxSai.rota.isFactivel()^solucao[auxSai.getKnn()[j]-1].rota.isFactivel())
				{
					auxEntra=solucao[auxSai.getKnn()[j]-1];
					ganho=avaliadorFac.ganhoCross(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoCross(auxSai, auxEntra);
						calcCusto();
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 11, auxSai, auxEntra,custoAvaliacao,ganho);
						}
					}
					ganho=avaliadorFac.ganhoCrossInvertido(auxSai, auxEntra);
					if(ganho>0)
					{
						custo=avaliadorCusto.custoCrossInvertido(auxSai, auxEntra);
						calcCusto();
						noMelhora=matrixMelhoras[auxSai.rota.nomeRota][auxEntra.rota.nomeRota];
						
						if(custoAvaliacao<noMelhora.custoAvaliacao)
						{
							if(!noMelhora.ativo)
								melhoras[topMelhores++]=noMelhora;

							noMelhora.setNoMelhora(custo, 12, auxSai, auxEntra,custoAvaliacao,ganho);
						}
					}
				}
			}
			auxSai=auxSai.prox;
		}
		while(auxSai!=rota.inicio);
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
