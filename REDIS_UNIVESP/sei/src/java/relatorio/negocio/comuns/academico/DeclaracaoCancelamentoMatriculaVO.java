/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;


/**
 * 
 * @author Otimize-TI
 */
public class DeclaracaoCancelamentoMatriculaVO {


	protected String nome;
	protected String rg;
	protected String cpf;
	protected String periodoLetivo;
	protected String curso;
	protected String dataSituacao;
	protected String dataExtenso;
	protected String matricula;
	protected String unidadeEnsino;

	public DeclaracaoCancelamentoMatriculaVO() {
		setNome("");
		setRg("");
		setCpf("");
		setPeriodoLetivo("");
		setCurso("");
		setDataSituacao("");
		setDataExtenso("");
		setMatricula("");
		setUnidadeEnsino("");
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
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}

	/**
	 * @param rg
	 *            the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * @param cpf
	 *            the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the periodoLetivo
	 */
	public String getPeriodoLetivo() {
		return periodoLetivo;
	}

	/**
	 * @param periodoLetivo
	 *            the periodoLetivo to set
	 */
	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	/**
	 * @return the curso
	 */
	public String getCurso() {
		return curso;
	}

	/**
	 * @param curso
	 *            the curso to set
	 */
	public void setCurso(String curso) {
		this.curso = curso;
	}

	/**
	 * @return the dataSituacao
	 */
	public String getDataSituacao() {
		return dataSituacao;
	}



	/**
	 * @param dataSituacao the dataSituacao to set
	 */
	public void setDataSituacao(String dataSituacao) {
		this.dataSituacao = dataSituacao;
	}



	/**
	 * @return the dataExtenso
	 */
	public String getDataExtenso() {
		return dataExtenso;
	}



	/**
	 * @param dataExtenso the dataExtenso to set
	 */
	public void setDataExtenso(String dataExtenso) {
		this.dataExtenso = dataExtenso;
	}



	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
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

}
