package Dados;

import Metaheuristicas.Variante;

public class Instancias 
{
	public DadosInstancia instancias[];
	
	public Instancias()
	{
		this.instancias=new DadosInstancia[67];
		
//		HVRPFD
		this.instancias[0]=new DadosInstancia("Taillard_13",Variante.HVRPFD,3185.09,false,true,Double.MAX_VALUE);
		this.instancias[1]=new DadosInstancia("Taillard_14",Variante.HVRPFD,10107.53,false,true,Double.MAX_VALUE);
		this.instancias[2]=new DadosInstancia("Taillard_15",Variante.HVRPFD,3065.29,false,true,Double.MAX_VALUE);
		this.instancias[3]=new DadosInstancia("Taillard_16",Variante.HVRPFD,3265.41,false,true,Double.MAX_VALUE);
		this.instancias[4]=new DadosInstancia("Taillard_17",Variante.HVRPFD,2076.96,false,true,Double.MAX_VALUE);
		this.instancias[5]=new DadosInstancia("Taillard_18",Variante.HVRPFD,3743.58,false,true,Double.MAX_VALUE);
		this.instancias[6]=new DadosInstancia("Taillard_19",Variante.HVRPFD,10420.34,false,false,Double.MAX_VALUE);
		this.instancias[7]=new DadosInstancia("Taillard_20",Variante.HVRPFD,4760.675673619461,false,false,Double.MAX_VALUE);
		
//		HVRPD
		this.instancias[8]=new DadosInstancia("Taillard_13",Variante.HVRPD,1517.84,false,true,Double.MAX_VALUE);
		this.instancias[9]=new DadosInstancia("Taillard_14",Variante.HVRPD,607.53,false,true,Double.MAX_VALUE);
		this.instancias[10]=new DadosInstancia("Taillard_15",Variante.HVRPD,1015.29,false,true,Double.MAX_VALUE);
		this.instancias[11]=new DadosInstancia("Taillard_16",Variante.HVRPD,1144.94,false,true,Double.MAX_VALUE);
		this.instancias[12]=new DadosInstancia("Taillard_17",Variante.HVRPD,1061.96,false,true,Double.MAX_VALUE);
		this.instancias[13]=new DadosInstancia("Taillard_18",Variante.HVRPD,1823.58,false,true,Double.MAX_VALUE);
		this.instancias[14]=new DadosInstancia("Taillard_19",Variante.HVRPD,1117.51,false,false,Double.MAX_VALUE);
		this.instancias[15]=new DadosInstancia("Taillard_20",Variante.HVRPD,1534.17,false,true,Double.MAX_VALUE);

//		FSMFD
		this.instancias[16]=new DadosInstancia("Taillard_03",Variante.FSMFD,1144.22,false,true,Double.MAX_VALUE);
		this.instancias[17]=new DadosInstancia("Taillard_04",Variante.FSMFD,6437.33,false,true,Double.MAX_VALUE);
		this.instancias[18]=new DadosInstancia("Taillard_05",Variante.FSMFD,1322.26,false,true,Double.MAX_VALUE);
		this.instancias[19]=new DadosInstancia("Taillard_06",Variante.FSMFD,6516.47,false,true,Double.MAX_VALUE);
		this.instancias[20]=new DadosInstancia("Taillard_13",Variante.FSMFD,2964.65,false,true,Double.MAX_VALUE);
		this.instancias[21]=new DadosInstancia("Taillard_14",Variante.FSMFD,9126.90,false,true,Double.MAX_VALUE);
		this.instancias[22]=new DadosInstancia("Taillard_15",Variante.FSMFD,2634.96,false,true,Double.MAX_VALUE);
		this.instancias[23]=new DadosInstancia("Taillard_16",Variante.FSMFD,3168.92,false,true,Double.MAX_VALUE);
		this.instancias[24]=new DadosInstancia("Taillard_17",Variante.FSMFD,2004.48,false,true,Double.MAX_VALUE);
		this.instancias[25]=new DadosInstancia("Taillard_18",Variante.FSMFD,3147.99,false,true,Double.MAX_VALUE);
		this.instancias[26]=new DadosInstancia("Taillard_19",Variante.FSMFD,8661.81,false,true,Double.MAX_VALUE);
		this.instancias[27]=new DadosInstancia("Taillard_20",Variante.FSMFD,4153.02,false,false,Double.MAX_VALUE);
		
//		FSMF
		this.instancias[28]=new DadosInstancia("Taillard_03",Variante.FSMF,961.03,false,true,Double.MAX_VALUE);
		this.instancias[29]=new DadosInstancia("Taillard_04",Variante.FSMF,6437.33,false,true,Double.MAX_VALUE);
		this.instancias[30]=new DadosInstancia("Taillard_05",Variante.FSMF,1007.05,false,true,Double.MAX_VALUE);
		this.instancias[31]=new DadosInstancia("Taillard_06",Variante.FSMF,6516.47,false,true,Double.MAX_VALUE);
		this.instancias[32]=new DadosInstancia("Taillard_13",Variante.FSMF,2406.36,false,true,Double.MAX_VALUE);
		this.instancias[33]=new DadosInstancia("Taillard_14",Variante.FSMF,9119.03,false,true,Double.MAX_VALUE);
		this.instancias[34]=new DadosInstancia("Taillard_15",Variante.FSMF,2586.37,false,true,Double.MAX_VALUE);
		this.instancias[35]=new DadosInstancia("Taillard_16",Variante.FSMF,2720.43,false,true,Double.MAX_VALUE);
		this.instancias[36]=new DadosInstancia("Taillard_17",Variante.FSMF,1734.53,false,true,Double.MAX_VALUE);
		this.instancias[37]=new DadosInstancia("Taillard_18",Variante.FSMF,2369.65,false,true,Double.MAX_VALUE);
		this.instancias[38]=new DadosInstancia("Taillard_19",Variante.FSMF,8661.81,false,false,Double.MAX_VALUE);
		this.instancias[39]=new DadosInstancia("Taillard_20",Variante.FSMF,4029.61,false,true,Double.MAX_VALUE);
		
//		FSMD
		this.instancias[40]=new DadosInstancia("Taillard_03",Variante.FSMD,623.22,false,true,Double.MAX_VALUE);
		this.instancias[41]=new DadosInstancia("Taillard_04",Variante.FSMD,387.18,false,true,Double.MAX_VALUE);
		this.instancias[42]=new DadosInstancia("Taillard_05",Variante.FSMD,742.87,false,true,Double.MAX_VALUE);
		this.instancias[43]=new DadosInstancia("Taillard_06",Variante.FSMD,415.03,false,true,Double.MAX_VALUE);
		this.instancias[44]=new DadosInstancia("Taillard_13",Variante.FSMD,1491.86,false,true,Double.MAX_VALUE);
		this.instancias[45]=new DadosInstancia("Taillard_14",Variante.FSMD,603.21,false,true,Double.MAX_VALUE);
		this.instancias[46]=new DadosInstancia("Taillard_15",Variante.FSMD,999.82,false,true,Double.MAX_VALUE);
		this.instancias[47]=new DadosInstancia("Taillard_16",Variante.FSMD,1131.00,false,true,Double.MAX_VALUE);
		this.instancias[48]=new DadosInstancia("Taillard_17",Variante.FSMD,1038.60,false,true,Double.MAX_VALUE);
		this.instancias[49]=new DadosInstancia("Taillard_18",Variante.FSMD,1800.80,false,true,Double.MAX_VALUE);
		this.instancias[50]=new DadosInstancia("Taillard_19",Variante.FSMD,1105.44,false,false,Double.MAX_VALUE);
		this.instancias[51]=new DadosInstancia("Taillard_20",Variante.FSMD,1530.43,false,true,Double.MAX_VALUE);
		
//		HVRPD
		this.instancias[52]=new DadosInstancia("Li_H1",Variante.HVRPD,12050.077097957217,false,false,12050.077097957217);
		this.instancias[53]=new DadosInstancia("Li_H2",Variante.HVRPD,10130.296097905273,false,false,10130.296097905273);
		this.instancias[54]=new DadosInstancia("Li_H3",Variante.HVRPD,16192.259872102375,false,false,16192.259872102375);
		this.instancias[55]=new DadosInstancia("Li_H4",Variante.HVRPD, 17273.75229459486,false,false, 17273.75229459486);
		this.instancias[56]=new DadosInstancia("Li_H5",Variante.HVRPD, 23024.581894314037,false,false,23024.581894314037);

//		HVRPD
		this.instancias[57]=new DadosInstancia("N1",Variante.HVRPD,2233.895993004797,false,false,2233.895993004797);
		this.instancias[58]=new DadosInstancia("N2",Variante.HVRPD,2851.936485347874,false,false,2851.936485347874);
		this.instancias[59]=new DadosInstancia("N3",Variante.HVRPD,2378.990997839265,false,false,2378.990997839265);
		this.instancias[60]=new DadosInstancia("N4",Variante.HVRPD,1839.2167382327573,false,false,1839.2167382327573);
		this.instancias[61]=new DadosInstancia("N5",Variante.HVRPD,2047.8059062423004,false,false,2047.8059062423004);

//		FSMD
		this.instancias[62]=new DadosInstancia("N1",Variante.FSMD,2211.626938975959,false,false,2211.626938975959);
		this.instancias[63]=new DadosInstancia("N2",Variante.FSMD,2811.3736693213345,false,false,2811.3736693213345);
		this.instancias[64]=new DadosInstancia("N3",Variante.FSMD,2234.570816251865,false,false,2234.570816251865);
		this.instancias[65]=new DadosInstancia("N4",Variante.FSMD,1822.7788904272184,false,false,1822.7788904272184);
		this.instancias[66]=new DadosInstancia("N5",Variante.FSMD,2016.7888123569246,false,false,2016.7888123569246);

	}
	
	public String toString()
	{
		String str="";
		for(DadosInstancia dadosInstancia : instancias)
		{
			str+=dadosInstancia+"\n";
		}
		return str;
	}
	
	public static void main(String[] args) 
	{
		Instancias instancias=new Instancias();
		
		System.out.println(instancias);
		
	}
}
