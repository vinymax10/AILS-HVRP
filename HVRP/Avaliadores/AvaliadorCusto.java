package Avaliadores;

import Dados.Instancia;
import Metaheuristicas.No;
import Metaheuristicas.Rota;
import Metaheuristicas.Variante;

public class AvaliadorCusto 
{
	Instancia instancia;
	double custo, saldo;
	Rota aRota, bRota;
	public AvaliadorCusto(Instancia instancia)
	{
		this.instancia=instancia;
	}
	
//	-------------------------------SHIFT------------------------------
	
	public double custoSHIFT(No a, No b)
	{
		 return instancia.dist(a.ant.nome,a.prox.nome,a.rota)-instancia.dist(a.nome,a.ant.nome,a.rota)-instancia.dist(a.nome,a.prox.nome,a.rota)+
				instancia.dist(a.nome,b.nome,b.rota)+instancia.dist(a.nome,b.prox.nome,b.rota)-instancia.dist(b.nome,b.prox.nome,b.rota);
	}
		
//	-------------------------------SWAP------------------------------

	
	public double custoSWAP(No a, No b)
	{
		if(a.prox!=b&&a.ant!=b)
		{
			return 	-(instancia.dist(a.nome,a.ant.nome,a.rota)+instancia.dist(a.nome,a.prox.nome,a.rota)+
					instancia.dist(b.nome,b.ant.nome,b.rota)+instancia.dist(b.nome,b.prox.nome,b.rota))+				
					(instancia.dist(a.nome,b.ant.nome,b.rota)+instancia.dist(a.nome,b.prox.nome,b.rota)+
					instancia.dist(b.nome,a.ant.nome,a.rota)+instancia.dist(b.nome,a.prox.nome,a.rota));
		}
		else
		{
			if(a.prox==b)
				return 	-(instancia.dist(a.nome,a.ant.nome,a.rota)+instancia.dist(b.nome,b.prox.nome,b.rota))+
					(instancia.dist(a.nome,b.prox.nome,b.rota)+instancia.dist(b.nome,a.ant.nome,a.rota));
			else
				return 	-(instancia.dist(b.nome,b.ant.nome,b.rota)+instancia.dist(a.nome,a.prox.nome,a.rota))+
						(instancia.dist(b.nome,a.prox.nome,a.rota)+instancia.dist(a.nome,b.ant.nome,b.rota));
		}
	}
	
//		-------------------------------CROSS------------------------------

	public double custoCross(No a, No b)
	{
		if((instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMD
		||instancia. getVariante()==Variante.HVRPD||instancia. getVariante()==Variante.FSMFD)
		&&Math.abs(a.rota.getVeiculo().getR()-b.rota.getVeiculo().getR())>=0.01)
		{
			custo=-(instancia.dist(a.nome,a.prox.nome,a.rota)+instancia.dist(b.nome,b.prox.nome,b.rota))
			+(instancia.dist(a.nome,b.prox.nome,a.rota)+instancia.dist(b.nome,a.prox.nome,b.rota));

			aRota=a.rota;
			bRota=b.rota;
			
			saldo=0;
			No aux=b.prox;
			while(aux!=bRota.inicio)
			{
				saldo+=instancia.dist(aux.nome,aux.prox.nome,b.rota);
				aux=aux.prox;
			}

			custo+=(saldo/b.rota.getVeiculo().getR()*a.rota.getVeiculo().getR())-saldo;

			saldo=0;
			aux=a.prox;
			while(aux!=aRota.inicio)
			{
				saldo+=instancia.dist(aux.nome,aux.prox.nome,a.rota);
				aux=aux.prox;
			}
			custo+=(saldo/a.rota.getVeiculo().getR()*b.rota.getVeiculo().getR())-saldo;

			return custo;
		}
		else
			return -(instancia.dist(a.nome,a.prox.nome,a.rota)+instancia.dist(b.nome,b.prox.nome,b.rota))
			+(instancia.dist(a.nome,b.prox.nome,a.rota)+instancia.dist(b.nome,a.prox.nome,b.rota));
	}
	 
	public double custoCrossInvertido(No a, No b)
	{
		if((instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMD
		||instancia. getVariante()==Variante.HVRPD||instancia. getVariante()==Variante.FSMFD)
		&&Math.abs(a.rota.getVeiculo().getR()-b.rota.getVeiculo().getR())>=0.01)
		{
			custo=-(instancia.dist(a.nome,a.prox.nome,a.rota)+instancia.dist(b.nome,b.prox.nome,b.rota))
			+(instancia.dist(a.nome,b.nome,a.rota)+instancia.dist(b.prox.nome,a.prox.nome,b.rota));

			aRota=a.rota;
			bRota=b.rota;
			
			saldo=0;
			No aux=b;
			while(aux!=bRota.inicio)
			{
				saldo+=instancia.dist(aux.nome,aux.ant.nome,b.rota);
				aux=aux.ant;
			}

			custo+=(saldo/b.rota.getVeiculo().getR()*a.rota.getVeiculo().getR())-saldo;

			saldo=0;
			aux=a.prox;
			while(aux!=aRota.inicio)
			{
				saldo+=instancia.dist(aux.nome,aux.prox.nome,a.rota);
				aux=aux.prox;
			}
			custo+=(saldo/a.rota.getVeiculo().getR()*b.rota.getVeiculo().getR())-saldo;

			return custo;
		}
		else
			return -(instancia.dist(a.nome,a.prox.nome,a.rota)+instancia.dist(b.nome,b.prox.nome,b.rota))
				 +(instancia.dist(a.nome,b.nome,a.rota)+instancia.dist(b.prox.nome,a.prox.nome,b.rota));
	}
	 
	public double custo2Opt(No a, No b)
	{
		return 	-(instancia.dist(a.nome,a.prox.nome,a.rota)+instancia.dist(b.nome,b.prox.nome,a.rota))+				
				(instancia.dist(a.nome,b.nome,a.rota)+instancia.dist(a.prox.nome,b.prox.nome,a.rota));
	}
	 
}
