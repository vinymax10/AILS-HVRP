package Pertubacao;

import java.util.HashMap;

import AjusteOmega.AjusteOmega;
import Dados.Instancia;
import Metaheuristicas.Config;
import Metaheuristicas.Solucao;

//Remocao aleatoria não deixando no mesmo lugar

public class Perturbacao24 extends Perturbacao
{
	public Perturbacao24(Instancia instancia, Config config, HashMap<String, AjusteOmega> configuradoresOmega)
	{
		super(instancia, config, configuradoresOmega);
		this.tipoPerturbacao=TipoPerturbacao.Pert24;
	}

	public void perturbar(Solucao s)
	{
		setSolucao(s);
		
		VariandoNumRotas(s);
//		---------------------------------------------------------------------
		for (int i = 0; i < omega; i++) 
		{
			no=solucao[rand.nextInt(solucao.length)];
			
			if(no.nome!=0)
			{
				no.antOld=no.ant;
				no.proxOld=no.prox;
				f+=no.rota.remove(no);
				bestNo=getNo(no);
				f+=bestNo.rota.addDepois(no, bestNo);
			}
		}

		passaSolucao(s);
	}
	
}
