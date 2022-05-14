package Metaheuristicas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import BuscaLocalIntra.BuscaLocalIntra;
import Dados.Arquivo;
import Dados.Instancia;
import Dados.Instancias;
import Dados.Ponto;
import Dados.Veiculo;
import Pertubacao.TipoGetVeiculo;

public class Solucao implements Comparable<Solucao>
{
	private Ponto pontos[];
	Instancia instancia;
	Config config;
	protected int size;
	No solucao[];

	protected int inicio;
	protected No deposito;
	public Rota rotas[];
	public int NumRotas;
	protected int NumRotasMin;
	protected int NumRotasMax;
	public double f=0;
	protected Random rand=new Random();
	public int distancia;
	double epsilon;
	private Veiculo veiculos[];
	private Veiculo listVeiculos[];

	int topVeiculos;
	TipoGetVeiculo tipoGetVeiculo;
	
//	-----------Comparadores-----------
	
	BuscaLocalIntra buscaLocalIntra;
	
	public Solucao(Instancia instancia,Config config) 
	{
		this.instancia=instancia;
		this.config=config;
		this.pontos=instancia.getPontos();
		int deposito=instancia.getDeposito();
		this.size=instancia.getSize()-1;
		this.solucao=new No[size];
		this.NumRotasMin=instancia.getNumRotasMin();
		this.NumRotas=NumRotasMin;
		this.NumRotasMax=instancia.getNumRotasMax();
		this.deposito=new No(pontos[deposito],instancia);
		this.epsilon=config.getEpsilon();
		this.rotas=new Rota[NumRotasMax];
		int cont=0;
		this.tipoGetVeiculo=config.getTipoGetVeiculo();
		
		if(instancia.getVariante()==Variante.FSMD||instancia.getVariante()==Variante.FSMF||instancia.getVariante()==Variante.FSMFD)
		{
			veiculos=new Veiculo[instancia.getNumTiposVeiculos()];
			listVeiculos=new Veiculo[instancia.getNumTiposVeiculos()+NumRotasMax];

			topVeiculos=veiculos.length;
			for (int i = 0; i < veiculos.length; i++) 
				veiculos[i]=new Veiculo(instancia.getTiposVeiculos()[i]);
		
			for (int i = 0; i < rotas.length; i++) 
			{
				rotas[i]=new Rota(instancia, this.deposito,i,new Veiculo(instancia.getTiposVeiculos()[rand.nextInt(instancia.getTiposVeiculos().length)]));
			}
		}
		else
		{
			veiculos=new Veiculo[NumRotasMax];
			topVeiculos=0;
			for (int i = 0; i < instancia.getTiposVeiculos().length; i++) 
			{
				for (int j = 0; j < instancia.getTiposVeiculos()[i].getM(); j++) 
					veiculos[topVeiculos++]=new Veiculo(instancia.getTiposVeiculos()[i]);
			}
			
			cont=0;
			for (int i = 0; i < rotas.length; i++) 
				rotas[i]=new Rota(instancia, this.deposito,i,veiculos[cont++]);
		}
		
		cont=0;
		for (int i = 0; i < (solucao.length+1); i++)
		{
			if(i!=deposito)
			{
				solucao[cont]=new No(pontos[i],instancia);
				cont++;
			}
		}
	}
	
	public Veiculo getVeiculoAleatorio()
	{
		return veiculos[rand.nextInt(veiculos.length)];
	}
	
	public Veiculo getMenorVeiculo()
	{
		return veiculos[veiculos.length-1];
	}
	
	public Veiculo getVeiculo()
	{
		switch(tipoGetVeiculo)
		{
			case Ale: 		return getVeiculoAle();
			case ProbMenor: return getVeiculoProbMenor();
			case ProbMaior: return getVeiculoProbMaior();
			case ProbProporcional: return getVeiculoProbProporcional();
		}
		return null;
	}
	
	public Veiculo getVeiculoAle()
	{
		return veiculos[rand.nextInt(veiculos.length)];
	}
	
	public Veiculo getVeiculoProbMenor()
	{
		int pos=rand.nextInt(veiculos.length*veiculos.length);
		int lin=pos/veiculos.length;
		int col=pos%veiculos.length;
		
		if(lin>col)
			pos=col;
		else
			pos=lin;
		
		return veiculos[veiculos.length-1-pos];
	}
	
	public Veiculo getVeiculoProbMaior()
	{
		int pos=rand.nextInt(veiculos.length*veiculos.length);
		int lin=pos/veiculos.length;
		int col=pos%veiculos.length;
		
		if(lin>col)
			pos=col;
		else
			pos=lin;
		
		return veiculos[pos];
	}
	
	public Veiculo getVeiculoProbProporcional()
	{
		int cont=0;
		for (int i = 0; i < NumRotas; i++) 
			listVeiculos[cont++]=rotas[i].getVeiculo();
		
		for (int i = 0; i < veiculos.length; i++)
			listVeiculos[cont++]=veiculos[i];
		
		
		return listVeiculos[rand.nextInt(cont)];
	}
	
	public Veiculo getVeiculoMaisCapacidade(Veiculo veiculo)
	{
		for (int i = veiculos.length-1; i >=0 ; i--) 
		{
			if(veiculos[i].getQ()>veiculo.getQ())
				return veiculos[i];
		}
		
		return veiculos[rand.nextInt(veiculos.length)];
	}
	
	public Veiculo getVeiculoFit(Rota rota)
	{
		for (int i = veiculos.length-1; i >=0 ; i--) 
		{
			if(veiculos[i].getQ()>=rota.demandaTotal)
				return veiculos[i];
		}
		
		return veiculos[0];
	}
	
	public void construirSolucao(int numRotas)
	{
		this.NumRotas=numRotas;
		construirClienteRota();
	}
	
	public void construirClienteRota()
	{
		int index;
		No no,bestNo;
		f=0;
		
		No naoInseridos[]=new No[size];
		int contNaoInseridos=0;
		
		for (int i = 0; i < size; i++) 
		{
			solucao[i].limpar();
			naoInseridos[contNaoInseridos++]=solucao[i];
		}
		
		for (int i = 0; i < NumRotas; i++)
		{
			f+=rotas[i].limpar();
			
			index=rand.nextInt(contNaoInseridos);
			
			f+=rotas[i].addNoFinal(naoInseridos[index]);
			
			no=naoInseridos[index];
			naoInseridos[index]=naoInseridos[contNaoInseridos-1];
			naoInseridos[--contNaoInseridos]=no;
		}
		
		while(contNaoInseridos>0) 
		{
			index=rand.nextInt(contNaoInseridos);
			no=naoInseridos[index];
			bestNo=getBestKNNNo(no);
			f+=bestNo.rota.addDepois(no, bestNo);
			naoInseridos[index]=naoInseridos[contNaoInseridos-1];
			naoInseridos[--contNaoInseridos]=no;
		}
	}
	
	protected No getBestKNNNo(No no)
	{
		double bestCusto=Double.MAX_VALUE;
		No aux,bestNo=null;
		double custo,custoAnt;
		for (int i = 0; i < solucao.length; i++) 
		{
			aux=solucao[i];
			if(aux.jaInserido)
			{
				
				custo=instancia.dist(aux.nome,no.nome,aux.rota)+instancia.dist(no.nome,aux.prox.nome,aux.rota)-instancia.dist(aux.nome,aux.prox.nome,aux.rota);
				if(custo<bestCusto)
				{
					bestCusto=custo;
					bestNo=aux;
				}
			}
		}
		custo=instancia.dist(bestNo.nome,no.nome,bestNo.rota)+instancia.dist(no.nome,bestNo.prox.nome,bestNo.rota)-instancia.dist(bestNo.nome,bestNo.prox.nome,bestNo.rota);
		custoAnt=instancia.dist(bestNo.ant.nome,no.nome,bestNo.rota)+instancia.dist(no.nome,bestNo.nome,bestNo.rota)-instancia.dist(bestNo.ant.nome,bestNo.nome,bestNo.rota);
		if(custo<custoAnt)
			return bestNo;
		
		return bestNo.ant;
	}

	public void clone(Solucao referencia)
	{
//		System.out.println("referencia\n"+referencia.toStringMeu());
		this.NumRotas=referencia.NumRotas;
		this.f=referencia.f;
		
		for (int i = 0; i < rotas.length; i++)
		{
			rotas[i].nomeRota=i;
			referencia.rotas[i].nomeRota=i;
		}
		
		for (int i = 0; i < rotas.length; i++)
		{
			rotas[i].demandaTotal=referencia.rotas[i].demandaTotal;
			rotas[i].fRota=referencia.rotas[i].fRota;
			rotas[i].numElements=referencia.rotas[i].numElements;
			rotas[i].alterada=referencia.rotas[i].alterada;
			
			rotas[i].veiculo.setF(referencia.rotas[i].getVeiculo().getF());
			rotas[i].veiculo.setQ(referencia.rotas[i].getVeiculo().getQ());
			rotas[i].veiculo.setR(referencia.rotas[i].getVeiculo().getR());
			
			if(referencia.rotas[i].inicio.ant==null)
				rotas[i].inicio.ant=null;
			else if(referencia.rotas[i].inicio.ant.nome==0)
				rotas[i].inicio.ant=rotas[i].inicio;
			else
				rotas[i].inicio.ant=solucao[referencia.rotas[i].inicio.ant.nome-1];
			
			if(referencia.rotas[i].inicio.prox==null)
				rotas[i].inicio.prox=null;
			else if(referencia.rotas[i].inicio.prox.nome==0)
				rotas[i].inicio.prox=rotas[i].inicio;
			else
				rotas[i].inicio.prox=solucao[referencia.rotas[i].inicio.prox.nome-1];
		}
		
		for (int i = 0; i < solucao.length; i++)
		{
			solucao[i].rota=rotas[referencia.solucao[i].rota.nomeRota];
			solucao[i].jaInserido=referencia.solucao[i].jaInserido;
			
			if(referencia.solucao[i].ant.nome==0)
				solucao[i].ant=rotas[referencia.solucao[i].ant.rota.nomeRota].inicio;
			else
				solucao[i].ant=solucao[referencia.solucao[i].ant.nome-1];
				
			if(referencia.solucao[i].prox.nome==0)
				solucao[i].prox=rotas[referencia.solucao[i].prox.rota.nomeRota].inicio;
			else
				solucao[i].prox=solucao[referencia.solucao[i].prox.nome-1];
		}
		
//		System.out.println("this\n"+this.toStringMeu());
	}
	
	public String toStringMeu() 
	{
		String str="size: " + size;
		str+="\n"+"deposito: " + deposito;
		str+="\nNumRotas: "+NumRotas;

		str+="\nf: "+f;
//		System.out.println(str);
		for (int i = 0; i < NumRotas; i++) 
		{
//			System.out.println(str);
			str+="\n"+rotas[i];
		}
		
		return str;
	}
	
	
	@Override
	public String toString() 
	{
		String str="";
		for (int i = 0; i < NumRotas; i++) 
		{
			str+=rotas[i].toString2()+"\n";
		}
		str+="Cost "+f+"\n";
		return str;
	}
	
	public boolean auditoria(String local, boolean factibildiade, boolean rotaVazia)
	{
		double f;
		double somaF=0;
		int somaNumElements=0;
		boolean erro=false;
		
		for (int i = 0; i < NumRotas; i++)
		{
			rotas[i].findErro();
			f=rotas[i].F();
			somaF+=f;
			somaNumElements+=rotas[i].numElements;
			
			if(Math.abs(f-rotas[i].fRota)>epsilon)
			{
				System.out.println("-------------------"+local+" ERRO no fRota-------------------"
				+ "\n"+rotas[i].toString()+"\nf esperado: "+f);
				erro=true;
			}
			
			if(rotaVazia&&rotas[i].inicio==rotas[i].inicio.prox)
			{
				System.out.println("-------------------"+local+" ERRO-------------------"
				+"Rota vazia: "+rotas[i].toString());
				erro=true;
			}
			
			if(rotas[i].inicio.nome!=0)
			{
				System.out.println("-------------------"+local+" ERRO-------------------"
				+"Rota iniciando sem deposito: "+rotas[i].toString());
				erro=true;
			}
			
			if(factibildiade&&!rotas[i].isFactivel())
			{
				System.out.println("-------------------"+local+" ERRO-------------------"
				+"Rota infactivel: "+rotas[i].toString());
				erro=true;
			}

		}
		if(Math.abs(somaF-this.f)>epsilon)
		{
			erro=true;
			System.out.println("-------------------"+local+" ERRO somatorio Total-------------------");
			System.out.println("Espedado: "+somaF+" obtido: "+this.f);
		}
		
		if((somaNumElements-NumRotas)!=size)
		{
			erro=true;
			System.out.println("-------------------"+local+" ERRO quantidade Elementos-------------------");
			System.out.println("Espedado: "+size+" obtido: "+(somaNumElements-NumRotas));
		}
		return erro;
	}
	
	public void removeRotasVazias()
	{
		for (int i = 0; i < NumRotas; i++) 
		{
			if(rotas[i].inicio==rotas[i].inicio.prox)
			{
				removeRota(i);
				i--;
			}
		}
	}
	
	private void removeRota(int index)
	{
		Rota aux=rotas[index];
		
		if(instancia. getVariante()==Variante.HVRPFD||instancia. getVariante()==Variante.FSMFD||instancia. getVariante()==Variante.FSMF)
		{
			f-=rotas[index].getVeiculo().getF();
		}
		
		if(index!=NumRotas-1)
		{
			rotas[index]=rotas[NumRotas-1];
			rotas[NumRotas-1]=aux;
		}
		NumRotas--;
	}
	
	//------------------------carregaSolution-------------------------

	public void carregaSolution(String nome)
	{
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(nome));
			String str []= null;
			String linha;
			
			linha=in.readLine();
			str=linha.split(" ");
			
//			int custo=Integer.valueOf(str[0]);
			for (int i = 0; i < 3; i++) 
				in.readLine();

			int indexRota=0;
			linha=in.readLine();
			str=linha.split(" ");
			
			System.out.println("-------------- str.length: "+str.length);
			for (int i = 0; i < str.length; i++) 
			{
				System.out.print(str[i]+"-");
			}
			System.out.println();
			
			do
			{
				rotas[indexRota].addNoFinal(deposito.clone());
				for (int i = 9; i < str.length-1; i++)
				{
					System.out.println("add: "+solucao[Integer.valueOf(str[i].trim())-1]+" na rota: "+rotas[indexRota].nomeRota);
					f+=rotas[indexRota].addNoFinal(solucao[Integer.valueOf(str[i])-1]);
				}
				indexRota++;
				linha=in.readLine();
				if(linha!=null)
					str=linha.split(" ");
			}
			while(linha!=null);
			
//			System.out.println("f: "+f+" esperado: "+custo);
		} 
		catch (IOException e) {
	    	System.out.println("Erro ao Ler Arquivo");
	    }
	}
	
	public void carregaSolution1(String nome)
	{
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(nome));
			String str []= null;
			
			str=in.readLine().split(" ");
			int indexRota=0;
			while(!str[0].equals("Cost"))
			{
				for (int i = 2; i < str.length; i++)
				{
//					System.out.println("add: "+solucao[Integer.valueOf(str[i])-1]+" na rota: "+rotas[indexRota].nomeRota);
					f+=rotas[indexRota].addNoFinal(solucao[Integer.valueOf(str[i])-1]);
				}
				indexRota++;
				str=in.readLine().split(" ");
			}
//			int cost=Integer.valueOf(str[1]);
			
//			System.out.println("f: "+f+" esperado: "+cost);
		} 
		catch (IOException e) {
	    	System.out.println("Erro ao Ler Arquivo");
	    }
	}
	
	public void carregaSolutionMinha(String nome)
	{
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(nome));
			String str []= null;
			String linha;
			
			for (int i = 0; i < 5; i++) 
				in.readLine();

			int indexRota=0;
			linha=in.readLine();
			str=linha.split("->");
			
//			System.out.println("-------------- str.length: "+str.length);
//			for (int i = 0; i < str.length; i++) 
//			{
//				System.out.print(str[i]+"-");
//			}
//			System.out.println();
			
			do
			{
				for (int i = 1; i < str.length; i++)
				{
					System.out.println(str[i].trim().split("d: ")[0].split("n:")[1].trim());
//					System.out.println("add: "+solucao[Integer.valueOf(str[i].trim())-1]+" na rota: "+rotas[indexRota].nomeRota);
					f+=rotas[indexRota].addNoFinal(solucao[Integer.valueOf(str[i].trim().split("d: ")[0].split("n:")[1].trim())-1]);
				}
//				System.out.println("--------------------------------------------------------");
				indexRota++;
				linha=in.readLine();
				if(linha!=null)
					str=linha.split("->");
				
				NumRotas=indexRota;
			}
			while(linha!=null);
			
//			System.out.println("f: "+f+" esperado: "+custo);
		} 
		catch (IOException e) {
	    	System.out.println("Erro ao Ler Arquivo");
	    }
	}
	
	@Override
	public int compareTo(Solucao x) 
	{
		if(this.f<x.f)
			return -1;
		else if(this.f>x.f)
			return 1;
		return 0;
	}

	public Rota[] getRotas() {
		return rotas;
	}

	public int getNumRotas() {
		return NumRotas;
	}

	public No getDeposito() {
		return deposito;
	}


	public No[] getSolucao() {
		return solucao;
	}

	public int getNumRotasMax() {
		return NumRotasMax;
	}

	public void setNumRotasMax(int numRotasMax) {
		NumRotasMax = numRotasMax;
	}

	public int getNumRotasMin() {
		return NumRotasMin;
	}

	public void setNumRotasMin(int numRotasMin) {
		NumRotasMin = numRotasMin;
	}

	public void escrecerSolucao(String end)
	{
		Arquivo arq=new Arquivo(end);
		arq.escrever(this.toString());
		arq.finalizar();
	}
	
	public static void main(String[] args) 
	{
		Instancias instancias=new Instancias();

		int pos=32;	

		Config config =new Config();
		Instancia instancia=new Instancia(
		"Instancias//"+instancias.instancias[pos].nome+".txt",
		config,instancias.instancias[pos].rounded,instancias.instancias[pos].variante);

		Solucao solucao=new Solucao(instancia,config);
		solucao.construirSolucao(instancia.getNumRotasMin());
//		solucao.carregaSolution1(instancias.instancias[pos].nome+".sol");
		solucao.auditoria("aqui", false, false);
		System.out.println(solucao.toStringMeu());
	}
}
