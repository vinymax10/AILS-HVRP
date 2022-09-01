package Dados;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import Metaheuristicas.Config;
import Metaheuristicas.NoKnn;
import Metaheuristicas.Rota;
import Metaheuristicas.Variante;

public class Instancia 
{
	private int size;

	private int knn[][];
	private double dist[][];
	private TipoVeiculo tiposVeiculos[];
	private NoKnn[]VizKnn;
	private int deposito;
	private int numRotasMin;
	private int numRotasMax;
	private Ponto pontos[];
	double coord[][];
	private String nome;
	private double maiorDist=0;
	private double menorDist=Integer.MAX_VALUE;
	boolean CoordEuc2D;
	double distancia;
	int limiteAdj;
	boolean rounded;
	double somaDemanda;
	String str[];
	int nLimiteDist=1000;
	Variante variante;
	int numVeiculos;
	int numTiposVeiculos;
	
	public Instancia(String nome,Config config,boolean rounded,Variante variante) 
	{
		this.nome=nome;
		this.rounded=rounded;
		this.variante=variante;
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(nome));
			
			str=in.readLine().split(":");
			while(!str[0].trim().equals("EOF"))
			{
				switch(str[0].trim())
				{
					case "DIMENSION": size=Integer.valueOf(str[1].trim());break;
					case "EDGE_WEIGHT_TYPE":
					case "EDGE_WEIGHT_FORMAT": setTipoCoord(str[1].trim());break;
					case "VEHICLES": numVeiculos=Integer.valueOf(str[1].trim());break;
					case "VEHICLE_KINDS": setTiposVeiculos(str[1].trim());break;
					case "NODE_COORD_SECTION": leituraCoord(in);break;
					case "DEMAND_SECTION": leituraDemanda(in);break;
					case "DEPOT_SECTION": leituraDeposito(in);break;
					case "CAPACITIES": setCapacidades(in);break;
					case "FIXED_COSTS": setCustosFixos(in);break;
					case "COST_COEFFICIENTS": setCoeficienteCustos(in);break;
					case "VEHICLES_AVAILABLE": setQtnVeiculos(in);break;
				}
				str=in.readLine().split(":");
			}
			
			Arrays.sort(tiposVeiculos);
			
			if(variante==Variante.FSMD||variante==Variante.FSMF||variante==Variante.FSMFD)
			{
				numRotasMin=(int)Math.ceil((double)somaDemanda/tiposVeiculos[0].getQ());
				numRotasMax=size;
			}
			else
			{
				int demandaAcumulada=0;
				numRotasMin=0;
				for (int i = 0; i < tiposVeiculos.length&&demandaAcumulada<somaDemanda; i++) 
				{
					for (int j = 0; j < tiposVeiculos[i].getM()&&demandaAcumulada<somaDemanda; j++)
					{
						demandaAcumulada+=tiposVeiculos[i].getQ();
						numRotasMin++;
					}
				}
				
				numRotasMax=0;
				for (int i = 0; i < tiposVeiculos.length; i++) 
					numRotasMax+=tiposVeiculos[i].getM();
			}
			
			//calculando Distancias
			int limiteAdj=Math.min(config.getVarphi()*2, size-1);
			knn=new int[size][limiteAdj];
			VizKnn=new NoKnn[size-1];
			
			for (int i = 0; i < VizKnn.length; i++) 
				VizKnn[i]=new NoKnn();
			
			if(size<=nLimiteDist)
				dist=new double[size][size];
			
			int cont=0;
			for (int i = 0; i < size; i++) 
			{
				cont=0;
				for (int j = 0; j < size; j++) 
				{
					distancia=distCalc(i,j);
//					System.out.print(distancia+"\t");
					
					if(size<=nLimiteDist)
						dist[i][j]=distancia;
					
					if(i<j)
					{
						if(maiorDist<distancia)
							maiorDist=distancia;
						
						if(menorDist>distancia)
							menorDist=distancia;
					}
					
					if(i!=j)
					{
						VizKnn[cont].dist=distancia;
						VizKnn[cont].nome=j;
						cont++;
					}
				}
//				System.out.println();
				
				Arrays.sort(VizKnn);
				
				for (int j = 0; j < limiteAdj; j++) 
					knn[i][j]=VizKnn[j].nome;
			}
		} 
		catch (IOException e) {
	    	System.out.println("Erro ao Ler Arquivo");
	    }
		VizKnn=null;
	}
	
	

//	public double dist(int i,int j)
//	{
//		if(size<=nLimiteDist)
//			return dist[i][j];
//		else if(i!=j)
//		{
//			if(CoordEuc2D)
//			{
//				if(rounded)
//					distancia=distanciaRounded(pontos[i].x,pontos[i].y,pontos[j].x,pontos[j].y);
//				else
//					distancia=distancia(pontos[i].x,pontos[i].y,pontos[j].x,pontos[j].y);
//			}
//			else
//				distancia=distanciaGeo(coord[i][0],coord[i][1],coord[j][0],coord[j][1]);
//		}
//		else
//			distancia=0;
//		
//		return distancia;
//	}
	
	public int getNumTiposVeiculos() {
		return numTiposVeiculos;
	}

	public double dist(int i,int j,Rota rota)
	{
		if(size<=nLimiteDist)
			distancia=dist[i][j];
		else if(i!=j)
		{
			if(CoordEuc2D)
			{
				if(rounded)
					distancia=distanciaRounded(pontos[i].x,pontos[i].y,pontos[j].x,pontos[j].y);
				else
					distancia=distancia(pontos[i].x,pontos[i].y,pontos[j].x,pontos[j].y);
			}
			else
				distancia=distanciaGeo(coord[i][0],coord[i][1],coord[j][0],coord[j][1]);
		}
		else
			distancia=0;
		
		if(variante==Variante.HVRPFD||variante==Variante.FSMD
		||variante==Variante.HVRPD||variante==Variante.FSMFD)
			return distancia*rota.getVeiculo().getR();
		else
			return distancia;
	}
	
	public double distCalc(int i,int j)
	{
		if(i!=j)
		{
			if(CoordEuc2D)
			{
				if(rounded)
					return distanciaRounded(pontos[i].x,pontos[i].y,pontos[j].x,pontos[j].y);
				else
					return distancia(pontos[i].x,pontos[i].y,pontos[j].x,pontos[j].y);
			}
			else
				return distanciaGeo(coord[i][0],coord[i][1],coord[j][0],coord[j][1]);
		}
		else
			return 0;
		
	}
	
	private double distancia(double x1,double y1,double x2,double y2)
	{
		return Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
	}
	
	private int distanciaRounded(double x1,double y1,double x2,double y2)
	{
		return (int) Math.round(Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2))));
	}
	
	private void setTipoCoord(String str)
	{
		if(str.equals("EUC_2D"))
			CoordEuc2D=true;
		else
		{
			CoordEuc2D=false;
			coord=new double[size][2];
		}
	}
	
	private void setCapacidades(BufferedReader in)
	{
		try {
			str=in.readLine().split("[' '|'\t']");
			for (int i = 0; i < tiposVeiculos.length; i++)
				tiposVeiculos[i].setQ(Integer.valueOf(str[i]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setCustosFixos(BufferedReader in)
	{
		try {
			str=in.readLine().split("[' '|'\t']");
			for (int i = 0; i < tiposVeiculos.length; i++)
				tiposVeiculos[i].setF(Integer.valueOf(str[i]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setCoeficienteCustos(BufferedReader in)
	{
		try {
			str=in.readLine().split("[' '|'\t']");
			for (int i = 0; i < tiposVeiculos.length; i++)
				tiposVeiculos[i].setR(Double.valueOf(str[i]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setQtnVeiculos(BufferedReader in)
	{
		try {
			str=in.readLine().split("[' '|'\t']");
			for (int i = 0; i < tiposVeiculos.length; i++)
				tiposVeiculos[i].setM(Integer.valueOf(str[i]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setTiposVeiculos(String str)
	{
		numTiposVeiculos=Integer.valueOf(str);
		tiposVeiculos=new TipoVeiculo[numTiposVeiculos];
		for (int i = 0; i < tiposVeiculos.length; i++) {
			tiposVeiculos[i]=new TipoVeiculo(i);
		}
	}
	
	private void leituraCoord(BufferedReader in)
	{
		pontos=new Ponto [size];
		for (int i = 0; i < pontos.length; i++) 
			pontos[i]=new Ponto(i);
		
		try {
			for (int i = 0; i < size; i++) 
			{
				if(CoordEuc2D)
				{
					str=in.readLine().split("[' '|'\t']");
					pontos[i].x=Double.valueOf(str[1].trim());
					pontos[i].y=Double.valueOf(str[2].trim());
				}
				else
				{
					str=in.readLine().split("[' '|'\t']");
					coord[i][0]=Double.valueOf(str[1].trim());
					coord[i][1]=Double.valueOf(str[2].trim());
					pontos[i].x=Double.valueOf((int) (coord[i][0]*10000));
					pontos[i].y=Double.valueOf((int) (coord[i][1]*10000));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void leituraDemanda(BufferedReader in)
	{
		try {
			somaDemanda=0;
			for (int i = 0; i < size; i++) 
			{
				str=in.readLine().split("[' '|'\t']");
				pontos[i].demanda=Integer.valueOf(str[1]);
				somaDemanda+=pontos[i].demanda;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void leituraDeposito(BufferedReader in)
	{
		try {
				deposito=Integer.valueOf(in.readLine().trim())-1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int distanciaGeo(double Lat1,double Long1,double Lat2,double Long2)
	{
		// Convers�o de graus pra radianos das latitudes
		double firstLatToRad = Math.toRadians(Lat1);
		double secondLatToRad = Math.toRadians(Lat2);

		// Diferen�a das longitudes
		double deltaLongitudeInRad = Math.toRadians(Long2
		- Long1);

		// C�lcula da dist�ncia entre os pontos
		return (int) Math.round(Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
		* Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
		* Math.sin(secondLatToRad))
		* 6378100);
	}
	
	@Override
	public String toString() {
		return "size=" + size 
				+ "\ndeposito="+ deposito +" numRotasMin: "+numRotasMin;
	}

	public Ponto[] getPontos() {
		return pontos;
	}


	public void setPontos(Ponto[] pontos) {
		this.pontos = pontos;
	}

	public int getNumRotasMin() {
		return numRotasMin;
	}

	public int getNumRotasMax() {
		return numRotasMax;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDeposito() {
		return deposito;
	}

	public void setDeposito(int deposito) {
		this.deposito = deposito;
	}

	public double getMaiorDist() {
		return maiorDist;
	}
	
	public double getMenorDist() {
		return menorDist;
	}

	public String getNome() {
		return nome;
	}

	public int[][] getKnn() {
		return knn;
	}
	
	public int getLimiteAdj() {
		return limiteAdj;
	}

	public TipoVeiculo[] getTiposVeiculos() {
		return tiposVeiculos;
	}

	public Variante getVariante() {
		return variante;
	}

	public void setVariante(Variante variante) {
		this.variante = variante;
	}

	public static void main(String[] args) 
	{
		Config config =new Config();
		new Instancia("Instancias//N2.txt",config,true,Variante.HVRPD);
	}
	
	
}
