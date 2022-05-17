package Metaheuristicas;

import java.text.DecimalFormat;
import java.util.Arrays;

import Factibilizadores.TipoCustoFac;
import Pertubacao.HeuristicaAdicao;
import Pertubacao.TipoGetVeiculo;
import Pertubacao.TipoPerturbacao;

public class Config implements Cloneable
{
	DecimalFormat deci=new DecimalFormat("0.000");
	double eta;
	int distM;
	
	int numIterUpdate;
	TipoPerturbacao perturbacao[];
	HeuristicaAdicao[]heuristicasAdicao;
	//--------------------PR-------------------
	int limiteAdj;
	TipoCustoFac tipoCustoFac;
	int omegaFixo,omegaMin,omegaMax;
	double epsilon;
	double alfa;
	int varicaoQtnRotas;
	TipoGetVeiculo tipoGetVeiculo;

	public Config() 
	{
//		----------------------------Main----------------------------
		this.limiteAdj=20;
		this.numIterUpdate=20;
//		----------------------------Controle de diversidade----------------------------
		this.distM=15;
		this.epsilon=0.01;

		this.alfa=0.4;
		this.varicaoQtnRotas=2;
		this.tipoGetVeiculo=TipoGetVeiculo.ProbProporcional;
		
//		----------------------------Criterio aceitacao----------------------------
		this.eta=0.2; 
		
//		----------------------------Perturbacao----------------------------
		
		this.perturbacao=new TipoPerturbacao[3];
		this.perturbacao[0]=TipoPerturbacao.Pert23;
		this.perturbacao[1]=TipoPerturbacao.Pert24;
		this.perturbacao[2]=TipoPerturbacao.Pert25;
		
		this.heuristicasAdicao=new HeuristicaAdicao[2];
		heuristicasAdicao[0]=HeuristicaAdicao.DISTANCIA;
		heuristicasAdicao[1]=HeuristicaAdicao.BESTCUSTO;
		
//		----------------------------Fase2 AILS----------------------------
		
//		----------------------------FAC----------------------------
		this.tipoCustoFac=TipoCustoFac.C_CDG;
		
		
//		----------------------------Outros----------------------------
		this.omegaFixo=22;
		this.omegaMin=25;
		this.omegaMax=30;
		
	}
	
	public Config clone()
	{
		try {
			return (Config) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//---------gets and sets-----------
	
	@Override
	public String toString() 
	{
		return "Config "
		+"\n----------------------------Main----------------------------"
		+"\nnumIterUpdate: "+numIterUpdate
		+"\neta: " + deci.format(eta)
		+"\ndistM: "+distM
		+"\nlimiteAdj: "+limiteAdj
		+"\nepsilon: " + deci.format(epsilon)
		+"\nvaricaoQtnRotas: "+varicaoQtnRotas

		
		+"\n----------------------------diversidade----------------------------"
		+"\nomegaFixo: "+omegaFixo
		+"\nprobVaricaoRotas: "+deci.format(alfa)
		+"\ntipoGetVeiculo: "+tipoGetVeiculo
		+"\nomegaMin: "+omegaMin
		+"\nomegaMax: "+omegaMax
		
		
		+"\n----------------------------PR----------------------------"
		
		+"\n----------------------------Heuristica----------------------------"
		+"\nperturbacao: "+Arrays.toString(perturbacao)
		+"\nheuristicasAdicao: "+Arrays.toString(heuristicasAdicao)
		
		
		+"\n----------------------------FAC----------------------------"
		+"\nTipoCustoFac: "+tipoCustoFac
		
		;  
	}

	public DecimalFormat getDeci() {
		return deci;
	}

	public void setDeci(DecimalFormat deci) {
		this.deci = deci;
	}

	public double getEta() {
		return eta;
	}

	public void setEta(double eta) {
		this.eta = eta;
	}

	public int getDistM() {
		return distM;
	}

	public void setDistM(int distM) {
		this.distM = distM;
	}

	public TipoPerturbacao[] getPerturbacao() {
		return perturbacao;
	}

	public void setPerturbacao(TipoPerturbacao[] perturbacao) {
		this.perturbacao = perturbacao;
	}

	public int getNumIterUpdate() {
		return numIterUpdate;
	}

	public void setNumIterUpdate(int numIterUpdate) {
		this.numIterUpdate = numIterUpdate;
	}

	public HeuristicaAdicao[] getHeuristicasAdicao() {
		return heuristicasAdicao;
	}

	public void setHeuristicasAdicao(HeuristicaAdicao[] heuristicas) {
		this.heuristicasAdicao = heuristicas;
	}

	public int getLimiteAdj() {
		return limiteAdj;
	}

	public void setLimiteAdj(int limiteAdj) {
		this.limiteAdj = limiteAdj;
	}

	public TipoCustoFac getTipoCustoFac() {
		return tipoCustoFac;
	}

	public void setTipoCustoFac(TipoCustoFac tipoCustoFac) {
		this.tipoCustoFac = tipoCustoFac;
	}

	public int getOmegaFixo() {
		return omegaFixo;
	}

	public void setOmegaFixo(int omegaFixo) {
		this.omegaFixo = omegaFixo;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public double getAlfa() {
		return alfa;
	}

	public void setAlfa(double alfa) {
		this.alfa = alfa;
	}

	public int getVaricaoQtnRotas() {
		return varicaoQtnRotas;
	}

	public void setVaricaoQtnRotas(int varicaoQtnRotas) {
		this.varicaoQtnRotas = varicaoQtnRotas;
	}

	public TipoGetVeiculo getTipoGetVeiculo() {
		return tipoGetVeiculo;
	}

	public void setTipoGetVeiculo(TipoGetVeiculo tipoGetVeiculo) {
		this.tipoGetVeiculo = tipoGetVeiculo;
	}

	public int getOmegaMin() {
		return omegaMin;
	}

	public void setOmegaMin(int omegaMin) {
		this.omegaMin = omegaMin;
	}

	public int getOmegaMax() {
		return omegaMax;
	}

	public void setOmegaMax(int omegaMax) {
		this.omegaMax = omegaMax;
	}
	
	
}
