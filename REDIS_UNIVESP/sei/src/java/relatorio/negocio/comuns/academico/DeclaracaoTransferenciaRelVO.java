/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;


/**
 * 
 * @author Otimize-TI
 */
public class DeclaracaoTransferenciaRelVO implements Cloneable {

	protected String matricula;
	protected String nome;
	protected String dataNasc;
	protected String naturalidade;
	protected String uf;
	protected String rg;
	protected String cpf;
	protected String nomeDiretor;
	protected String periodoLetivo;
	protected String unidadeEnsino;
	protected String data;
	protected String curso;
	protected Integer textoPadraoDeclaracao;
	
	
	public DeclaracaoTransferenciaRelVO(){
		setMatricula("");
		setNome("");
		setDataNasc("");
		setNaturalidade("");
		setUf("");
		setRg("");
		setCpf("");
		setNomeDiretor("");
		setPeriodoLetivo("");
		setUnidadeEnsino("");
		setData("");
		setCurso("");
	}
	
	public DeclaracaoTransferenciaRelVO getClone() throws Exception{		
		return (DeclaracaoTransferenciaRelVO) super.clone();
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
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the dataNasc
	 */
	public String getDataNasc() {
		return dataNasc;
	}
	/**
	 * @param dataNasc the dataNasc to set
	 */
	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}
	/**
	 * @return the naturalidade
	 */
	public String getNaturalidade() {
		return naturalidade;
	}
	/**
	 * @param naturalidade the naturalidade to set
	 */
	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}
	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}
	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}
	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}
	/**
	 * @param rg the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}

	/**
	 * @return the nomeDiretor
	 */
	public String getNomeDiretor() {
		return nomeDiretor;
	}

	/**
	 * @param nomeDiretor the nomeDiretor to set
	 */
	public void setNomeDiretor(String nomeDiretor) {
		this.nomeDiretor = nomeDiretor;
	}

	/**
	 * @return the periodoLetivo
	 */
	public String getPeriodoLetivo() {
		return periodoLetivo;
	}

	/**
	 * @param periodoLetivo the periodoLetivo to set
	 */
	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
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
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the curso
	 */
	public String getCurso() {
		return curso;
	}

	/**
	 * @param curso the curso to set
	 */
	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	public Integer getTextoPadraoDeclaracao() {
		if(textoPadraoDeclaracao == null){
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}
	
	

}
