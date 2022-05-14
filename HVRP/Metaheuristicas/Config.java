package Metaheuristicas;

import java.text.DecimalFormat;
import java.util.Arrays;

import BuscaLocal.TipoBLIntraBL;
import Factibilizadores.TipoBLFac;
import Factibilizadores.TipoCustoFac;
import Pertubacao.HeuristicaAdicao;
import Pertubacao.TipoGetVeiculo;
import Pertubacao.TipoPerturbacao;
import Pertubacao.TipoVariacaoVeiculo;

public class Config implements Cloneable
{
	DecimalFormat deci=new DecimalFormat("0.000");
	double eta,etaMin;
	double ebEta,ebDistM;
	double fluxoIdeal;
	double distLearning;
	int distElite;
	int distM,distM2;
	
	double variacao;
	int sizeElite;
	int numIterUpdate;
	TipoPerturbacao perturbacao[];
	double alfa;
	HeuristicaAdicao[]heuristicasAdicao;
	TipoBLFac tipoBLFac;
	double ebDistDPM;
	//--------------------PR-------------------
	double ebMatriz;
	int limiteAdj;
	int aleListMelhoresCustos;
	TipoCustoFac tipoCustoFac;
	int numChilds;
	double fatorStocastic;
	int numIterAlta;
	int numIterBaixa;
	int numIterMareCiclo;
	double porcentFCFBL;
	double distTabu,distTriangulacao;
	int omegaFixo,omegaMin,omegaMax;
	TipoBLIntraBL tipoBLIntraBL;
	double epsilon;
	double probVaricaoRotas;
	TipoVariacaoVeiculo tipoVariacaoVeiculo;
	double probVaricaoQtnRotas;
	int varicaoQtnRotas;
	TipoGetVeiculo tipoGetVeiculo;
	int periodocidadeSA;

	public Config() 
	{
//		----------------------------Main----------------------------
		this.limiteAdj=20;
		this.numIterUpdate=20;
//		----------------------------Controle de diversidade----------------------------
		this.distM=15;
		this.distM2=15;
		this.epsilon=0.01;
		this.variacao=0;

		this.probVaricaoRotas=0.4;
		this.probVaricaoQtnRotas=0.4;
		this.varicaoQtnRotas=2;
		this.tipoVariacaoVeiculo=TipoVariacaoVeiculo.Varia11;
		this.tipoGetVeiculo=TipoGetVeiculo.ProbProporcional;
		
//		----------------------------Criterio aceitacao----------------------------
		this.periodocidadeSA=70000;
		this.fluxoIdeal=0.6;
		this.eta=0.2; 
		this.etaMin=0.01; 
		this.numChilds=4;
		
//		----------------------------Perturbacao----------------------------
		
		this.perturbacao=new TipoPerturbacao[3];
		this.perturbacao[0]=TipoPerturbacao.Pert23;
		this.perturbacao[1]=TipoPerturbacao.Pert24;
		this.perturbacao[2]=TipoPerturbacao.Pert25;
		
		this.heuristicasAdicao=new HeuristicaAdicao[2];
		heuristicasAdicao[0]=HeuristicaAdicao.DISTANCIA;
		heuristicasAdicao[1]=HeuristicaAdicao.BESTCUSTO;
		
//		----------------------------Fase2 AILS----------------------------
		this.sizeElite=60;
		
//		----------------------------BL----------------------------
		this.tipoBLIntraBL=TipoBLIntraBL.SN;
		this.aleListMelhoresCustos=1;
		
//		----------------------------FAC----------------------------
		this.tipoCustoFac=TipoCustoFac.C_CDG;
		this.tipoBLFac=TipoBLFac.NSS;
		
		
//		----------------------------Outros----------------------------
		this.omegaFixo=22;
		this.omegaMin=25;
		this.omegaMax=30;
		
		this.distTriangulacao=4;
		this.distTabu=7;
		
		
		this.ebEta=1;
		this.ebDistM=0.5;
		this.distLearning=0.1;
		this.fatorStocastic=10;
		
		this.numIterAlta=200;
		this.numIterBaixa=3000;

		this.distElite=24;
		
		this.alfa=1;
		this.ebDistDPM=0.1;
		this.ebMatriz=0.3;
		this.numIterMareCiclo=2000;

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
	
	public int getSizeElite() {
		return sizeElite;
	}

	public void setSizeElite(int sizeElite) {
		this.sizeElite = sizeElite;
	}

	@Override
	public String toString() 
	{
		return "Config "
		+"\n----------------------------Main----------------------------"
		+"\nnumIterUpdate: "+numIterUpdate
		+"\neta: " + deci.format(eta)
		+"\nfluxoIdeal: " + deci.format(fluxoIdeal)
		+"\ndistM: "+distM
		+"\ndistM2: "+distM2
		+"\nlimiteAdj: "+limiteAdj
		+"\nsizeElite: " + sizeElite
		+"\nepsilon: " + deci.format(epsilon)
		+"\ndistTriangulacao: " + deci.format(distTriangulacao)
		+"\nvaricaoQtnRotas: "+varicaoQtnRotas

		
		+"\n----------------------------eta----------------------------"
		+"\netaMin: "+deci.format(etaMin)
		+"\nebEta: "+deci.format(ebEta)
		+"\ndistLearning: "+deci.format(distLearning)
		+"\nnumChilds: "+deci.format(numChilds)
		+"\nfatorStocastic: "+deci.format(fatorStocastic)
		+"\nnumIterAlta: "+numIterAlta
		+"\nnumIterBaixa: "+numIterBaixa
		+"\ndistTabu: "+distTabu
		+"\nperiodocidadeSA: "+periodocidadeSA

		+"\n----------------------------diversidade----------------------------"
		+"\nnumIterMareCiclo: "+numIterMareCiclo
		+"\nebDistM: "+ebDistM
		+"\nalfa: "+deci.format(alfa)
		+"\nebDistDPM: "+deci.format(ebDistDPM)
		+"\nebMatriz: "+ebMatriz
		+"\nporcentFCFBL: "+deci.format(porcentFCFBL)
		+"\nomegaFixo: "+omegaFixo
		+"\nprobVaricaoRotas: "+deci.format(probVaricaoRotas)
		+"\ntipoVariacaoVeiculo: "+tipoVariacaoVeiculo
		+"\nprobVaricaoQtnRotas: "+deci.format(probVaricaoQtnRotas)
		+"\ntipoGetVeiculo: "+tipoGetVeiculo
		+"\nomegaMin: "+omegaMin
		+"\nomegaMax: "+omegaMax
		
		
		+"\n----------------------------PR----------------------------"
		+"\ndistElite: "+distElite
		
		+"\n----------------------------Heuristica----------------------------"
		+"\nperturbacao: "+Arrays.toString(perturbacao)
		+"\nheuristicasAdicao: "+Arrays.toString(heuristicasAdicao)
		+"\nvariacao: " + deci.format(variacao)
		
		+"\n----------------------------BL----------------------------"
		
		+"\naleListMelhoresCustos: "+aleListMelhoresCustos
		+"\ntipoBLIntraBL: " + tipoBLIntraBL
		
		+"\n----------------------------FAC----------------------------"
		+"\ntipoBLFac: "+tipoBLFac
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

	public double getFluxoIdeal() {
		return fluxoIdeal;
	}

	public void setFluxoIdeal(double fluxoIdeal) {
		this.fluxoIdeal = fluxoIdeal;
	}

	public double getVariacao() {
		return variacao;
	}

	public void setVariacao(double variacao) {
		this.variacao = variacao;
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

	public double getEbEta() {
		return ebEta;
	}

	public void setEbEta(double ebEta) {
		this.ebEta = ebEta;
	}

	public int getDistElite() {
		return distElite;
	}

	public void setDistElite(int distElite) {
		this.distElite = distElite;
	}

	public HeuristicaAdicao[] getHeuristicasAdicao() {
		return heuristicasAdicao;
	}

	public void setHeuristicasAdicao(HeuristicaAdicao[] heuristicas) {
		this.heuristicasAdicao = heuristicas;
	}

	public double getAlfa() {
		return alfa;
	}

	public void setAlfa(double alfa) {
		this.alfa = alfa;
	}

	public TipoBLFac getTipoBLFac() {
		return tipoBLFac;
	}

	public void setTipoBLFac(TipoBLFac tipoBLFac) {
		this.tipoBLFac = tipoBLFac;
	}

	public double getEbDistDPM() {
		return ebDistDPM;
	}

	public void setEbDistDPM(double ebDistDPM) {
		this.ebDistDPM = ebDistDPM;
	}

	public double getEtaMin() {
		return etaMin;
	}

	public void setEtaMin(double etaMin) {
		this.etaMin = etaMin;
	}

	public double getEbMatriz() {
		return ebMatriz;
	}

	public void setEbMatriz(double ebMatriz) {
		this.ebMatriz = ebMatriz;
	}

	public double getDistLearning() {
		return distLearning;
	}

	public void setDistLearning(double distLearning) {
		this.distLearning = distLearning;
	}

	public double getEbDistM() {
		return ebDistM;
	}

	public void setEbDistM(double ebDistM) {
		this.ebDistM = ebDistM;
	}

	public int getLimiteAdj() {
		return limiteAdj;
	}

	public void setLimiteAdj(int limiteAdj) {
		this.limiteAdj = limiteAdj;
	}

	public int getAleListMelhoresCustos() {
		return aleListMelhoresCustos;
	}

	public void setAleListMelhoresCustos(int aleListMelhoresCustos) {
		this.aleListMelhoresCustos = aleListMelhoresCustos;
	}

	public TipoCustoFac getTipoCustoFac() {
		return tipoCustoFac;
	}

	public void setTipoCustoFac(TipoCustoFac tipoCustoFac) {
		this.tipoCustoFac = tipoCustoFac;
	}

	public int getNumChilds() {
		return numChilds;
	}

	public void setNumChilds(int numChilds) {
		this.numChilds = numChilds;
	}

	public double getFatorStocastic() {
		return fatorStocastic;
	}

	public void setFatorStocastic(double fatorStocastic) {
		this.fatorStocastic = fatorStocastic;
	}

	public int getNumIterAlta() {
		return numIterAlta;
	}

	public void setNumIterAlta(int numIterAlta) {
		this.numIterAlta = numIterAlta;
	}

	public int getNumIterBaixa() {
		return numIterBaixa;
	}

	public void setNumIterBaixa(int numIterBaixa) {
		this.numIterBaixa = numIterBaixa;
	}

	public int getNumIterMareCiclo() {
		return numIterMareCiclo;
	}

	public void setNumIterMareCiclo(int numIterMareCiclo) {
		this.numIterMareCiclo = numIterMareCiclo;
	}

	public double getPorcentFCFBL() {
		return porcentFCFBL;
	}

	public void setPorcentFCFBL(double porcentFCFBL) {
		this.porcentFCFBL = porcentFCFBL;
	}

	public double getDistTabu() {
		return distTabu;
	}

	public void setDistTabu(double distTabu) {
		this.distTabu = distTabu;
	}

	public int getOmegaFixo() {
		return omegaFixo;
	}

	public void setOmegaFixo(int omegaFixo) {
		this.omegaFixo = omegaFixo;
	}

	public TipoBLIntraBL getTipoBLIntraBL() {
		return tipoBLIntraBL;
	}

	public void setTipoBLIntraBL(TipoBLIntraBL tipoBLIntraBL) {
		this.tipoBLIntraBL = tipoBLIntraBL;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public double getDistTriangulacao() {
		return distTriangulacao;
	}

	public void setDistTriangulacao(double distTriangulacao) {
		this.distTriangulacao = distTriangulacao;
	}

	public int getDistM2() {
		return distM2;
	}

	public void setDistM2(int distM2) {
		this.distM2 = distM2;
	}

	public double getProbVaricaoRotas() {
		return probVaricaoRotas;
	}

	public void setProbVaricaoRotas(double probVaricaoRotas) {
		this.probVaricaoRotas = probVaricaoRotas;
	}

	public TipoVariacaoVeiculo getTipoVariacaoVeiculo() {
		return tipoVariacaoVeiculo;
	}

	public void setTipoVariacaoVeiculo(TipoVariacaoVeiculo tipoVariacaoVeiculo) {
		this.tipoVariacaoVeiculo = tipoVariacaoVeiculo;
	}

	public double getProbVaricaoQtnRotas() {
		return probVaricaoQtnRotas;
	}

	public void setProbVaricaoQtnRotas(double probVaricaoQtnRotas) {
		this.probVaricaoQtnRotas = probVaricaoQtnRotas;
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

	public int getPeriodocidadeSA() {
		return periodocidadeSA;
	}

	public void setPeriodocidadeSA(int periodocidadeSA) {
		this.periodocidadeSA = periodocidadeSA;
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
