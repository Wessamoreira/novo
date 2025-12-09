package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class ScriptExecutadoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3239138034696644025L;

	private Integer codigo;
	private String nome;
	private Date dataExecucao;
	private String sql;
	private String versaoSistema;
	private Boolean sucesso;
	private String mensagemErro;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataExecucao() {
		if (dataExecucao == null) {
			dataExecucao = new Date();
		}
		return dataExecucao;
	}

	public void setDataExecucao(Date dataExecucao) {
		this.dataExecucao = dataExecucao;
	}

	public String getSql() {
		if (sql == null) {
			sql = "";
		}
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getVersaoSistema() {
		if (versaoSistema == null) {
			versaoSistema = "";
		}
		return versaoSistema;
	}

	public void setVersaoSistema(String versaoSistema) {
		this.versaoSistema = versaoSistema;
	}

	public Boolean getSucesso() {
		if (sucesso == null) {
			sucesso = Boolean.TRUE;
		}
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}
}
