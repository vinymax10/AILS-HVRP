package Dados;

public class Veiculo implements Comparable<Veiculo>
{
	private int type;
	private int q;
	private int f;
	private double r;
	
	public Veiculo() {
		super();
	}

	public Veiculo(TipoVeiculo tipoVeiculo) 
	{
		super();
		this.type=tipoVeiculo.getType();
		this.q = tipoVeiculo.getQ();
		this.f = tipoVeiculo.getF();
		this.r = tipoVeiculo.getR();
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

	@Override
	public String toString() {
		return "Veiculo [type=" + type + ", q=" + q + ", f=" + f + ", r=" + r + "]";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int compareTo(Veiculo o) 
	{
		return o.q-q;
	}
	
}
