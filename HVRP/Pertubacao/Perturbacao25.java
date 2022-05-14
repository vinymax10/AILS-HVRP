package Pertubacao;

import java.util.HashMap;

import AjusteOmega.AjusteOmega;
import Dados.Instancia;
import Metaheuristicas.Config;
import Metaheuristicas.No;
import Metaheuristicas.Solucao;

//Remocao sequencial não deixando no mesmo lugar

public class Perturbacao25 extends Perturbacao
{
	public Perturbacao25(Instancia instancia, Config config, HashMap<String, AjusteOmega> configuradoresOmega)
	{
		super(instancia, config,configuradoresOmega);
		this.tipoPerturbacao=TipoPerturbacao.Pert25;
	}

	public void perturbar(Solucao s)
	{
		setSolucao(s);
		
		VariandoNumRotas(s);
		
//		---------------------------------------------------------------------
		int contSizeString;
		double sizeString;
		No noInicial;
		
		contCandidatos=0;
		while(contCandidatos<(int)omega)
		{
			sizeString=Math.min(Math.max(1, size),(int)omega-contCandidatos);
			no=solucao[rand.nextInt(size)];
			noInicial=no.ant;
			
			if(noInicial.nome==0)
				noInicial=noInicial.ant;
			
			contSizeString=0;
			while(noInicial.nome!=no.nome&&contSizeString<sizeString)
			{
				if(no.nome==0)
					no=no.prox;
				
				contSizeString++;
				ordem[contCandidatos]=contCandidatos;
				candidatos[contCandidatos++]=no;
				no.antOld=no.ant;
				no.proxOld=no.prox;
				
				no=no.prox;
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
