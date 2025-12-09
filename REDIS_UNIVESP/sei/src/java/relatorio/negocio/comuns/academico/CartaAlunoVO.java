/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;


/**
 * @author Otimize-TI
 */
public class CartaAlunoVO {


	protected String nome;
	protected String data;
	protected String endereco;
	protected String bairro;
	protected String cep;
	protected String fone;
	protected String email;
	protected String estado;
	protected String matricula;
	protected String cidade;
	protected String unidadeEnsino;
	protected String caminhoImagem;
	

	public CartaAlunoVO() {
		setNome("");
		setData("");
		setEndereco("");
		setBairro("");
		setCep("");
		setFone("");
		setEmail("");
		setEstado("");
		setMatricula("");
		setCidade("");
		setUnidadeEnsino("");
		setCaminhoImagem("");
	}

	

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}


	/**
	 * @return the dataExtenso
	 */
	public String getData() {
		return data;
	}



	/**
	 * @param dataExtenso the dataExtenso to set
	 */
	public void setData(String data) {
		this.data = data;
	}



	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}



	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}



	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}



	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}



	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}



	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}



	/**
	 * @return the fone
	 */
	public String getFone() {
		return fone;
	}



	/**
	 * @param fone the fone to set
	 */
	public void setFone(String fone) {
		this.fone = fone;
	}



	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}



	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}



	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}



	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}



	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}



	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}



	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}



	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}



	/**
	 * @return the unidadeEnsino
	 */
	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}



	/**
	 * @param unidadeEnsino the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}



	/**
	 * @return the caminhoImagem
	 */
	public String getCaminhoImagem() {
		return caminhoImagem;
	}



	/**
	 * @param caminhoImagem the caminhoImagem to set
	 */
	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}
}
