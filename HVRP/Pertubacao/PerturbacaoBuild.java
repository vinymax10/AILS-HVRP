package Pertubacao;

import java.util.HashMap;

import AjusteOmega.AjusteOmega;
import Dados.Instancia;
import Metaheuristicas.Config;

public class PerturbacaoBuild 
{
	Perturbacao perturbacao;
	
	public Perturbacao ConstruirPerturbacao(Instancia instancia, Config config,
	TipoPerturbacao tipoPerturbacao, HashMap<String, AjusteOmega> configuradoresOmega)//
	{
		switch(tipoPerturbacao)
		{
			case Pert23: return new Perturbacao23(instancia, config,configuradoresOmega); 
			case Pert24: return new Perturbacao24(instancia, config,configuradoresOmega); 
			case Pert25: return new Perturbacao25(instancia, config,configuradoresOmega); 

			default:
			break;
		}
		return null;
	}
	
}
