package negocio.comuns.utilitarias.formula.test.recursos;

public class A {

	private String nome;
	private Integer idade;
	private B b;

	public A(String nome, int idade, B endereco) {
		super();
		this.nome = nome;
		this.idade = idade;
		this.b = endereco;
	}

	public String getNome() {
		return nome;
	}

	public int getIdade() {
		return idade;
	}

	public B getB() {
		return b;
	}

	

}
