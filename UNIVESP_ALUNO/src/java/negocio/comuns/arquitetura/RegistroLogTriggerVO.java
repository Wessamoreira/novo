package negocio.comuns.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

public class RegistroLogTriggerVO implements Serializable {
	
	private Long id;
	private Long transaction;
	private String tabela;
	private Date data;
	private String usuario;
	private String query;
	private String action;
	private List<CampoLogTriggerVO> campos;	
	
	public String getDataApresentar() {
		if (getData() == null) {
			return "";
		} else {
			return Uteis.getDataComHora(getData());
		}
	}

	public List<CampoLogTriggerVO> getCampos() {
		if (campos == null) {
			campos = new ArrayList<CampoLogTriggerVO>(0);
		}
		return campos;
	}

	public void setCampos(List<CampoLogTriggerVO> campos) {
		this.campos = campos;
	}

	public Long getId() {
		if (id == null) {
			id = 0L;
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTransaction() {
		if (transaction == null) {
			transaction = 0L;
		}
		return transaction;
	}

	public void setTransaction(Long transaction) {
		this.transaction = transaction;
	}

	public String getUsuario() {
		if (usuario == null) {
			usuario = "";
		}
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public String getAction() {
		if (action == null) {
			action = "";
		}
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTabela() {
		if (tabela == null) {
			tabela = "";
		}
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
	
	

}
