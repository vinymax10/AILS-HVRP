package Metaheuristicas;

import Dados.Instancia;
import Dados.Ponto;

public class No implements Cloneable
{
	public No ant,antOld;
	public No prox,proxOld;
	
	public Rota rota;
	public int nome;
	double x,y;
	public int demanda;

	public int knn[];
	public int demandaAcumulada=0;
	public boolean jaInserido;
	public boolean fixoRota;
	public boolean fixoPos;
	Ponto ponto;
	Instancia instancia;
	
	public int nomeRotaDestino;
	public int nomeRotaOrigem;
	public int priority;
	public Rota rotaDestino;
	
	public No(Ponto ponto, Instancia instancia) 
	{
		this.ponto=ponto;
		this.instancia=instancia;
		this.nome = ponto.nome;
		this.x=ponto.x;
		this.y=ponto.y;
		this.demanda = ponto.demanda;

		this.prox=null;
		this.ant=null;
		this.rota=null;
		this.knn=instancia.getKnn()[nome];
	}
	
	public void limpar()
	{
		this.jaInserido=false;
		this.rota=null;
		this.prox=null;
		this.ant=null;
		this.antOld=null;
		this.proxOld=null;
		this.demandaAcumulada=0;
	}
	
	 public No clone() 
	 {
		 No clone = new No(ponto, instancia);
		 clone.ant=ant;
		 clone.prox=prox;
		 clone.rota=rota;
		 return clone; 
	 }
	
	 public double custoRemocao()
	 {
		 return instancia.dist(ant.nome,prox.nome,rota)-instancia.dist(nome,ant.nome,rota)-instancia.dist(nome,prox.nome,rota);
	 }
	 
	 public double custoInserirApos(No no)
	 {
		 if(no==null)
			 System.out.println("no null");
		 
		 return -instancia.dist(no.nome,no.prox.nome,no.rota)+instancia.dist(nome,no.nome,no.rota)+instancia.dist(nome,no.prox.nome,no.rota);
	 }
	 
	@Override
	public String toString() 
	{
		return "|"+nome+" "+demanda+"|";
	}

	public int getDemanda() {
		return demanda;
	}

	public int[] getKnn() {
		return knn;
	}
	
	
}