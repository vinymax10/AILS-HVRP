package Metaheuristicas;

import java.io.File;
import java.util.Arrays;

public class LeituraParametros 
{
	
	private String file="";
	private boolean rounded=false;
	private Variante variant=Variante.FSMD;
	private double limit=Double.MAX_VALUE;
	private double best=0;
	private Config config =new Config();
	
	public void lerParametros(String[] args)
	{
		try 
		{
			for (int i = 0; i < args.length-1; i+=2) 
			{
				switch(args[i])
				{
					case "-file": file=getEndereco(args[i+1]);break;
					case "-rounded": rounded=getRound(args[i+1]);break;
					case "-variant": variant=getVariant(args[i+1]);break;
					case "-limit": limit=getLimit(args[i+1]);break;
					case "-best": best=getBest(args[i+1]);break;
					case "-eta": config.setEta(getEta(args[i+1]));break;
					case "-alpha": config.setAlpha(getAlpha(args[i+1]));break;
					case "-varphi": config.setVarphi(getVarphi(args[i+1]));break;
					case "-gamma": config.setGamma(getGamma(args[i+1]));break;
					case "-dBeta": config.setDBeta(getDBeta(args[i+1]));break;
					case "-stoppingCriterion": config.setStoppingCriterion(getStoppingCriterion(args[i+1]));break;
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("File: "+file);
		System.out.println("Rounded: "+rounded);
		System.out.println("Variant: "+variant);
		System.out.println("limit: "+limit);
		System.out.println("Best: "+best);
		System.out.println("LimitTime: "+limit);
		System.out.println(config);
	}
	
	
	public String getEndereco(String texto)
	{
		try 
		{
			File file=new File(texto);
			if(file.exists()&&!file.isDirectory())
				return texto;
			else
				System.err.println("The -file parameter must contain the address of a valid file.");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";	
	}
	
	public boolean getRound(String texto)
	{
		try 
		{
			if(texto.equals("false")||texto.equals("true"))
				rounded=Boolean.valueOf(texto);
			else
				System.err.println("The -rounded parameter must have the values false or true.");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return rounded;
	}
	
	public double getLimit(String texto)
	{
		try 
		{
			limit=Double.valueOf(texto);
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -limit parameter must contain a valid real value.");
		}
		return limit;
	}
	
	public double getBest(String texto)
	{
		try 
		{
			best=Double.valueOf(texto);
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -best parameter must contain a valid real value.");
		}
		return best;
	}
	
	public double getEta(String texto)
	{
		double eta=0.2;
		try 
		{
			if(Double.valueOf(texto)>=0&&Double.valueOf(texto)<=1)
				eta=Double.valueOf(texto);
			else
				throw new java.lang.NumberFormatException();
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -eta parameter must contain a valid real value in the range [0,1].");
		}
		return eta;
	}
	
	public double getAlpha(String texto)
	{
		double alpha=0.4;
		try 
		{
			if(Double.valueOf(texto)>=0&&Double.valueOf(texto)<=1)
				alpha=Double.valueOf(texto);
			else
				throw new java.lang.NumberFormatException();
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -alpha parameter must contain a valid real value in the range [0,1].");
		}
		return alpha;
	}
	
	public int getVarphi(String texto)
	{
		int varphi=20;
		try 
		{
			varphi=Integer.valueOf(texto);
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -varphi parameter must contain a valid integer value.");
		}
		return varphi;
	}
	
	public int getGamma(String texto)
	{
		int gamma=20;
		try 
		{
			gamma=Integer.valueOf(texto);
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -gamma parameter must contain a valid integer value.");
		}
		return gamma;
	}
	
	public int getDBeta(String texto)
	{
		int dBeta=15;
		try 
		{
			dBeta=Integer.valueOf(texto);
		} 
		catch (java.lang.NumberFormatException e) {
			System.err.println("The -dBeta parameter must contain a valid integer value.");
		}
		return dBeta;
	}
	
	public Variante getVariant(String texo)
	{
		try 
		{
			variant=Variante.valueOf(texo);
		} 
		catch (java.lang.IllegalArgumentException e) 
		{
			System.err.println("The -variant parameter must have the values "+Arrays.toString(Variante.values())+".");
		}
		return variant;
	}
	
	public StoppingCriterion getStoppingCriterion(String texo)
	{
		StoppingCriterion stoppingCriterion=StoppingCriterion.Time;
		try 
		{
			stoppingCriterion=StoppingCriterion.valueOf(texo);
		} 
		catch (java.lang.IllegalArgumentException e) 
		{
			System.err.println("The -stoppingCriterion parameter must have the values "+Arrays.toString(StoppingCriterion.values())+".");
		}
		return stoppingCriterion;
	}

	public String getFile() {
		return file;
	}

	public boolean isRounded() {
		return rounded;
	}

	public Variante getVariant() {
		return variant;
	}

	public double getTimeLimit() {
		return limit;
	}

	public double getBest() {
		return best;
	}


	public Config getConfig() {
		return config;
	}
	
}
