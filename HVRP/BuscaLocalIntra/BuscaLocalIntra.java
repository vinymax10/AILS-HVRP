package BuscaLocalIntra;

import Avaliadores.AvaliadorCusto;
import Avaliadores.ExecutaMovimento;
import Dados.Instancia;
import Metaheuristicas.Config;
import Metaheuristicas.No;
import Metaheuristicas.NoPosMel;
import Metaheuristicas.Rota;

public class BuscaLocalIntra 
{
	private  NoPosMel melhora;
	No inicio;
	int i,j;
	int numElements=0;
	int tipoMov;
	int iterador;
	double menorCusto;
	double antF;
	No auxSai,auxEntra;
	double custo,custoInvertido,custoIdaIda,custoIdaVolta,custoVoltaIda,custoVoltaVolta;
	boolean trocou=false;
	int difCusto;
	AvaliadorCusto avaliadorCusto;
	ExecutaMovimento executaMovimento;
	double epsilon;

	public BuscaLocalIntra(Instancia instancia,Config config)
	{
		this.avaliadorCusto=new AvaliadorCusto(instancia);
		this.executaMovimento=new ExecutaMovimento(instancia);
		this.melhora=new NoPosMel(avaliadorCusto);
		this.epsilon=config.getEpsilon();
	}
	
	private void setRota(Rota rota) 
	{
		this.antF=rota.fRota;
		this.inicio=rota.inicio;
		this.numElements=rota.numElements;
	}

	public double buscaLocalIntra(Rota rota,No solucao[])
	{
		setRota(rota);

		iterador=0;
		trocou=true;
		while(trocou)
		{
//			System.out.println("iterador: "+iterador);
			iterador++;
			trocou=false;
			menorCusto=0;
			auxSai=inicio;
			i=0;
			do
			{
				i++;
				auxEntra=inicio;
				j=0;
				do
				{
					j++;
					
					//2Opt
					if(auxSai!=auxEntra&&auxEntra!=auxSai.prox)
					{
						custo=avaliadorCusto.custo2Opt(auxSai,auxEntra);
						if((custo-menorCusto)<-epsilon)
						{
							menorCusto=custo;
							tipoMov=10;
							melhora.setNoMelhora(menorCusto, tipoMov, auxSai, auxEntra,0,0,menorCusto);
							trocou=true;
						}
					}
					
					//SHIFT
					if(numElements>2&&auxSai!=auxEntra&&auxSai!=auxEntra.prox)
					{
						custo=avaliadorCusto.custoSHIFT(auxSai,auxEntra);
						if((custo-menorCusto)<-epsilon)
						{
							menorCusto=custo;
							tipoMov=7;
							melhora.setNoMelhora(menorCusto, tipoMov, auxSai, auxEntra,0,0,menorCusto);
							trocou=true;
						}
					}
					
					//SWAP
					if(i<j&&numElements>2&&auxEntra!=auxSai&&auxEntra.prox!=auxSai)
					{
						custo=avaliadorCusto.custoSWAP(auxSai,auxEntra);
						if((custo-menorCusto)<-epsilon)
						{
							menorCusto=custo;
							tipoMov=4;
							melhora.setNoMelhora(menorCusto, tipoMov, auxSai, auxEntra,0,0,menorCusto);
							trocou=true;
						}
					}
					
					auxEntra=auxEntra.prox;
				}
				while(auxEntra!=inicio);
				
				auxSai=auxSai.prox;
			}
			while(auxSai!=inicio);
			
			if(trocou)
			{
				executaMovimento.aplicar(melhora);
			}
		}
		
		return rota.fRota-antF;
	}
	
}
