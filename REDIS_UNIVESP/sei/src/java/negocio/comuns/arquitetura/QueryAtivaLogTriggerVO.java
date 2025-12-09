package negocio.comuns.arquitetura;

import java.io.Serializable;

public class QueryAtivaLogTriggerVO implements Serializable {
  
	private static final long serialVersionUID = 1L;

	private Integer pid;
	private String query;
	private String usuarioBancoDados;
	private String nomeAplicacao;
	private String situacao;
	
	

	public Integer getPid() {
		if (pid == null) {
			pid = 0;
		}
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getQuery() {
		if (query == null) {
			query = "";
		}
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getUsuarioBancoDados() {
		if (usuarioBancoDados == null) {
			usuarioBancoDados = "";
		}
		return usuarioBancoDados;
	}

	public void setUsuarioBancoDados(String usuarioBancoDados) {
		this.usuarioBancoDados = usuarioBancoDados;
	}

	public String getNomeAplicacao() {
		if (nomeAplicacao == null) {
			nomeAplicacao = "";
		}
		return nomeAplicacao;
	}

	public void setNomeAplicacao(String nomeAplicacao) {
		this.nomeAplicacao = nomeAplicacao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	
}
