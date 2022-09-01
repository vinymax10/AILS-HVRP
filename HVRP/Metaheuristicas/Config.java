package Metaheuristicas;

import java.text.DecimalFormat;

import Factibilizadores.TipoCustoFac;
import Pertubacao.HeuristicaAdicao;
import Pertubacao.TipoGetVeiculo;
import Pertubacao.TipoPerturbacao;

public class Config implements Cloneable
{
	private DecimalFormat deci=new DecimalFormat("0.000");
	private double eta;
	private int dBeta;
	
	private int gamma;
	private TipoPerturbacao perturbacao[];
	private HeuristicaAdicao[]heuristicasAdicao;
	//--------------------PR-------------------
	private int varphi;
	private TipoCustoFac tipoCustoFac;
	private int omegaFixo,omegaMin,omegaMax;
	private double epsilon;
	private double alpha;
	private int varicaoQtnRotas;
	private TipoGetVeiculo tipoGetVeiculo;
	private StoppingCriterion stoppingCriterion;
	
	public Config() 
	{
//		----------------------------Main----------------------------
		this.eta=0.2; 
		this.alpha=0.4;
		this.varphi=20;
		this.gamma=20;
		this.dBeta=15;
		
//		----------------------------Outros----------------------------
		this.stoppingCriterion=StoppingCriterion.Time;
		
		this.epsilon=0.01;
		this.varicaoQtnRotas=2;
		this.tipoGetVeiculo=TipoGetVeiculo.ProbProporcional;
		this.perturbacao=new TipoPerturbacao[3];
		this.perturbacao[0]=TipoPerturbacao.Pert23;
		this.perturbacao[1]=TipoPerturbacao.Pert24;
		this.perturbacao[2]=TipoPerturbacao.Pert25;
		
		this.heuristicasAdicao=new HeuristicaAdicao[2];
		heuristicasAdicao[0]=HeuristicaAdicao.DISTANCIA;
		heuristicasAdicao[1]=HeuristicaAdicao.BESTCUSTO;
		this.tipoCustoFac=TipoCustoFac.C_CDG;
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
	
	
	@Override
	public String toString() 
	{
		return "Parameters: "
		+"\nGamma: "+gamma
		+"\nEta: " + deci.format(eta)
		+"\nDBeta: "+dBeta
		+"\nVarphi: "+varphi
		+"\nalpha: "+deci.format(alpha)
		+"\nstoppingCriterion: "+stoppingCriterion.tipo
		+"\n";
	}

	//---------gets and sets-----------

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

	public int getDBeta() {
		return dBeta;
	}

	public void setDBeta(int dBeta) {
		this.dBeta = dBeta;
	}

	public TipoPerturbacao[] getPerturbacao() {
		return perturbacao;
	}

	public void setPerturbacao(TipoPerturbacao[] perturbacao) {
		this.perturbacao = perturbacao;
	}

	public int getGamma() {
		return gamma;
	}

	public void setGamma(int gamma) {
		this.gamma = gamma;
	}

	public HeuristicaAdicao[] getHeuristicasAdicao() {
		return heuristicasAdicao;
	}

	public void setHeuristicasAdicao(HeuristicaAdicao[] heuristicas) {
		this.heuristicasAdicao = heuristicas;
	}

	public int getVarphi() {
		return varphi;
	}

	public void setVarphi(int varphi) {
		this.varphi = varphi;
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

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
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

	public StoppingCriterion getStoppingCriterion() {
		return stoppingCriterion;
	}

	public void setStoppingCriterion(StoppingCriterion stoppingCriterion) {
		this.stoppingCriterion = stoppingCriterion;
	}
	
}
