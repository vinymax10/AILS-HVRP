package Pertubacao;

import java.util.HashMap;

import AjusteOmega.AjusteOmega;
import Dados.Instancia;
import Metaheuristicas.Config;
import Metaheuristicas.No;
import Metaheuristicas.Solucao;

//Remocao concentrica não deixando no mesmo lugar

public class Perturbacao23 extends Perturbacao
{
	public Perturbacao23(Instancia instancia, Config config, HashMap<String, AjusteOmega> configuradoresOmega)
	{
		super(instancia, config, configuradoresOmega);
		this.tipoPerturbacao=TipoPerturbacao.Pert23;
	}

	public void perturbar(Solucao s)
	{
		setSolucao(s);
//		System.out.println("inicio\n"+s.toStringMeu());
		VariandoNumRotas(s);
		
//		---------------------------------------------------------------------
		contCandidatos=0;
		No referencia=solucao[rand.nextInt(solucao.length)];
		for (int i = 0; i < omega&&i < referencia.knn.length; i++) 
		{
			if(referencia.knn[i]!=0)
			{
				no=solucao[referencia.knn[i]-1];
				candidatos[contCandidatos]=no;
				ordem[contCandidatos]=contCandidatos;
				contCandidatos++;
				no.antOld=no.ant;
				no.proxOld=no.prox;
			}
		}
		
		mudaOrdem(contCandidatos);
		
		for (int i = 0; i < contCandidatos; i++) 
		{
			no=candidatos[ordem[i]];
			f+=no.rota.remove(no);
			bestNo=getNo(no);
			f+=bestNo.rota.addDepois(no, bestNo);
		}
		
		
		
		passaSolucao(s);
	}
}
