package Dados;

import Metaheuristicas.Variante;

public class DadosInstancia
{
	public String nome;
	public Otimo bestSolution;
	public Otimo bestSolutionSalva;
	public boolean otimo;
	public boolean rounded;
	public Variante variante;
	
	public DadosInstancia(String nome, Variante variante,double bestSolution,boolean rounded, 
	boolean otimo,double bestSolutionSalva) 
	{
		super();
		this.nome = nome;
		this.bestSolution = new Otimo(bestSolution);
		this.rounded=rounded;
		this.otimo = otimo;
		this.bestSolutionSalva=new Otimo(bestSolutionSalva);
		this.variante=variante;
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Otimo getBestSolution() {
		return bestSolution;
	}
	public void setBestSolution(Otimo bestSolution) {
		this.bestSolution = bestSolution;
	}
	public boolean isOtimmo() {
		return otimo;
	}
	public void setOtimmo(boolean otimmo) {
		this.otimo = otimmo;
	}
	public boolean isOtimo() {
		return otimo;
	}
	public void setOtimo(boolean otimo) {
		this.otimo = otimo;
	}

	public boolean isRounded() {
		return rounded;
	}

	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}

	public Variante getVariante() {
		return variante;
	}


	public void setVariante(Variante variante) {
		this.variante = variante;
	}


	public Otimo getBestSolutionSalva() {
		return bestSolutionSalva;
	}

	public void setBestSolutionSalva(Otimo bestSolutionSalva) {
		this.bestSolutionSalva = bestSolutionSalva;
	}


	@Override
	public String toString() {
		return "DadosInstancia [nome=" + nome + ", bestSolution=" + bestSolution.getOtimo() + ", bestSolutionSalva="
				+ bestSolutionSalva.getOtimo() + ", otimo=" + otimo + ", rounded=" + rounded + ", variante=" + variante + "]";
	}
	
	
}
