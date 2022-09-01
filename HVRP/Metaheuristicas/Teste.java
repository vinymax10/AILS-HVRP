package Metaheuristicas;

import Dados.Instancia;
import Dados.Instancias;

public class Teste 
{
	public static void main(String[] args) 
	{
		Instancias instancias=new Instancias();
		
		int pos=66;	

		Config config =new Config();
		Instancia instancia=new Instancia(
		"Instancias//"+instancias.instancias[pos].nome+".txt",config,
		instancias.instancias[pos].rounded,instancias.instancias[pos].variante);
		
		System.out.println(instancias.instancias[pos].nome
		+" otimo: "+instancias.instancias[pos].bestSolution.getOtimo());
		AILS igas=new AILS(instancia,config,instancias.instancias[pos].bestSolution.getOtimo(),20000);
		igas.procurar();
		
		
	}

}
