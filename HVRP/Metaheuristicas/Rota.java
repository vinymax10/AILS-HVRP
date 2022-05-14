package Metaheuristicas;

import java.util.Random;

import Dados.Instancia;
import Dados.Veiculo;

public class Rota implements Comparable<Rota>
{
	public int deposito;
	public No inicio;
	public boolean podeMelhorar;
	public boolean alterada;
	public int demandaTotal=0;
	public double fRota=0,antF;
	public int numElements=0;
	public int nomeRota;
	String cor;
	No ant,prox;
	No aux;
	
	//------------------------------------------------
	public double custo;
	public int distancia;
	//------------------------------------------------
    Instancia instancia;
    Veiculo veiculo;
    
	public Rota(Instancia instancia, No deposito,int nomeRota,Veiculo veiculo) 
	{
		this.instancia=instancia;
		this.veiculo=veiculo;
		this.deposito=deposito.nome;
		this.nomeRota=nomeRota;
		this.inicio=null;
		addNoFinal(deposito.clone());
		
		Random rand=new Random();
		this.cor="#"+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255));
	}
	
	public double limpar()
	{
		inicio.ant=inicio;
		inicio.prox=inicio;
		
		demandaTotal=0;
		
		if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMFD||instancia. getVariante()==Variante.FSMF)
			fRota=veiculo.getF();
		else
			fRota=0;
		
		numElements=1;
		alterada=true;
		return fRota;
	}
	
	public double alterarVeiculo(Veiculo outro)
	{
		antF=fRota;
		alterada=true;
		if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMD
		||instancia. getVariante()==Variante.HVRPD||instancia. getVariante()==Variante.FSMFD)
		{
			if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMFD)
			{
				fRota-=veiculo.getF();
				fRota/=veiculo.getR();
				fRota*=outro.getR();
				fRota+=veiculo.getF();
			}
			else
			{
				fRota/=veiculo.getR();
				fRota*=outro.getR();
			}
		}
		
		if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMFD||instancia. getVariante()==Variante.FSMF)
		{
			fRota-=veiculo.getF();
			fRota+=outro.getF();
		}
		veiculo.setF(outro.getF());
		veiculo.setQ(outro.getQ());
		veiculo.setR(outro.getR());
		
		return fRota-antF;
	}
	
	public void setDemandaAcumulada()
	{
		inicio.demandaAcumulada=0;
		aux=inicio.prox;
		do
		{
			aux.demandaAcumulada=aux.ant.demandaAcumulada+aux.demanda;
			aux=aux.prox;
		}
		while(aux!=inicio);
	}
	
	public void inverterRota()
	{
		aux=inicio;
		No ant=inicio.ant;
		No prox=inicio.prox;
		do
		{
			aux.ant=prox;
			aux.prox=ant;
			aux=prox;
			ant=aux.ant;
			prox=aux.prox;
		}
		while(aux!=inicio);
	}
	
	public double F()
	{
		double f;
		if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMFD||instancia. getVariante()==Variante.FSMF)
			f=veiculo.getF();
		else
			f=0;
		
		No no=inicio;
		do
		{
			f+=instancia.dist(no.nome,no.prox.nome,this);
			no=no.prox;
		}
		while(no!=inicio);
		
		f+=instancia.dist(no.nome,inicio.nome,this);
		
		return f;
	}
	
	public void findErro()
	{
		int cont=0;
		No aux=inicio;
		
		do
		{
			if(aux.ant==null||aux.prox==null)
			{
				System.out.println("Erro no null No: "+aux+" em:\n"+this.toString());
				System.out.println(this);
			}
			if(aux.rota!=this)
				System.out.println("Erro no : "+aux+" com rota Errada:\n"+this.toString());

			cont++;
			aux=aux.prox;
		}
		while(aux!=inicio);
		if(cont!=numElements)
			System.out.println("Erro no numElements \n"+this.toString());

//		if(cont==0)
//			System.out.println("Erro so tem um elemento\n"+this.toString());
	}
	
	//------------------------Visualizacao-------------------------

	@Override
	public String toString() 
	{
		String str="Rota: "+nomeRota;
		str+=" f: "+fRota;
		str+=" espaco: "+espacoLivre();
		str+=" size: "+numElements+" ";
//		str+=" alterada: "+alterada+" = ";
		str+=" veiculo: "+veiculo+" ";

		No aux=inicio;
		do
		{
			str+=aux+"->";
//			System.out.println(str);
			aux=aux.prox;
		}
		while(aux!=inicio);
		
		return str;
	}
	
	public String toString2() 
	{
		String str="Type: "+veiculo.getType()+" Route #"+(nomeRota+1)+": ";
		No aux=inicio.prox;
		do
		{
			str+=aux.nome+" ";
			aux=aux.prox;
		}
		while(aux!=inicio);
		
		return str;
	}
	
	public boolean isFactivel()
	{
		if((veiculo.getQ()-demandaTotal)>=0)
			return true;
		return false;
	}
	
	public int espacoLivre()
	{
		return veiculo.getQ()-demandaTotal;
	}
	
	public void setDeposito(int deposito) {
		this.deposito = deposito;
	}

	public int getDemandaTotal() {
		return demandaTotal;
	}

	public void setDemandaTotal(int demandaTotal) {
		this.demandaTotal = demandaTotal;
	}

	public int getNumElements() {
		return numElements;
	}

	@Override
	public int compareTo(Rota x) 
	{
		return nomeRota-x.nomeRota;
	}

	public double remove(No no) 
	{
		custo=no.custoRemocao();
//		System.out.println("custo: "+custo+" fRota: "+fRota);
		
		if(no==inicio)
			inicio=no.ant;
		
		alterada=true;
		
		no.jaInserido=false;
		
		fRota+=custo;
		numElements--;
		
		ant=no.ant;
		prox=no.prox;
		
		ant.prox=prox;
		prox.ant=ant;
		
		demandaTotal-=no.demanda;
		
		no.rota=null;
		no.ant=null;
		no.prox=null;
		
//		setDemandaAcumulada();
		
		return custo;
	}

	//------------------------Add No-------------------------
	
	public double addNoFinal(No no)
	{
		no.rota=this;
		if(inicio==null)
		{
			inicio=no;
			inicio.ant=no;
			inicio.prox=no;
			numElements++;
			demandaTotal=0;
			fRota=0;
			return 0;
		}
		else if(no.nome==0)
		{
			aux=inicio.ant;
			inicio=no;
			return addDepois(no, aux); 
		}
		else 
		{
			aux=inicio.ant;
			return addDepois(no, aux); 
		}
	}
	
	public double addDepois(No no1, No no2) 
	{
		custo=no1.custoInserirApos(no2);
		
		alterada=true;
		no1.jaInserido=true;
		
		numElements++;
		fRota+=custo;
		
		demandaTotal+=no1.demanda;

		no1.rota=this;
		
		prox=no2.prox;
		no2.prox=no1;
		no1.ant=no2;
		
		prox.ant=no1;
		no1.prox=prox;
		
		if(no1.nome==0)
			inicio=no1;
		
//		setDemandaAcumulada();
		
		return custo;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
}

