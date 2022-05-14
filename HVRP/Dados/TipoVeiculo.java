package Dados;

public class TipoVeiculo implements Comparable<TipoVeiculo>
{
	private int type;
	private int q;
	private int f;
	private double r;
	private int m;
	
	public TipoVeiculo(int type) {
		super();
		this.type = type;
	}

	public int getQ() 
	{
		return q;
	}
	
	public void setQ(int q) {
		this.q = q;
	}
	
	public int getF() {
		return f;
	}
	
	public void setF(int f) {
		this.f = f;
	}
	
	public double getR() {
		return r;
	}
	
	public void setR(double r) {
		this.r = r;
	}
	
	public int getM() {
		return m;
	}
	
	public void setM(int m) {
		this.m = m;
	}

	@Override
	public String toString() {
		return "TipoVeiculo [type=" + type + ", q=" + q + ", f=" + f + ", r=" + r + ", m=" + m + "]";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int compareTo(TipoVeiculo o) 
	{
		return o.q-q;
	}
	
}
