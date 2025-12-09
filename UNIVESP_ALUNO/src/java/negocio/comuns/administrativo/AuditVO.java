package negocio.comuns.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class AuditVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer event_id;
	private String schema_name;
	private String session_user_name;
	private UsuarioVO usuarioLogadoVO;
	private Date action_tstamp_clk;
	private Long transaction_id;
	private String client_addr;
	private String action;
	private JSONObject row_data;
	private JSONObject changed_fields;
	private List<AtributoJsonVO> listaAtributoJsonRow_dataVOs;
	private List<AtributoJsonVO> listaAtributoJsonChanged_fieldsVOs;
	

	public AuditVO() {

	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getEvent_id() {
		if (event_id == null) {
			event_id = 0;
		}
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

	public String getSchema_name() {
		if (schema_name == null) {
			schema_name = "";
		}
		return schema_name;
	}

	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}

	public String getSession_user_name() {
		if (session_user_name == null) {
			session_user_name = "";
		}
		return session_user_name;
	}

	public void setSession_user_name(String session_user_name) {
		this.session_user_name = session_user_name;
	}

	public UsuarioVO getUsuarioLogadoVO() {
		if (usuarioLogadoVO == null) {
			usuarioLogadoVO = new UsuarioVO();
		}
		return usuarioLogadoVO;
	}

	public void setUsuarioLogadoVO(UsuarioVO usuarioLogadoVO) {
		this.usuarioLogadoVO = usuarioLogadoVO;
	}

	public Long getTransaction_id() {
		if (transaction_id == null) {
			transaction_id = 0L;
		}
		return transaction_id;
	}

	public void setTransaction_id(Long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getClient_addr() {
		if (client_addr == null) {
			client_addr = "";
		}
		return client_addr;
	}

	public void setClient_addr(String client_addr) {
		this.client_addr = client_addr;
	}

	public String getAction() {
		if (action == null) {
			action = "";
		}
		return action;
	}
	
	public String getAction_Apresentar() {
		if (getAction().equals("U")) {
			return "Alteração";
		}
		if (getAction().equals("I")) {
			return "Inserção";
		}
		if (getAction().equals("D")) {
			return "Exclusão";
		}
		return "";
	}

	public void setAction(String action) {
		this.action = action;
	}

	public JSONObject getRow_data() {
		if (row_data == null) {
			row_data = new JSONObject();
		}
		return row_data;
	}

	public void setRow_data(JSONObject row_data) {
		this.row_data = row_data;
	}

	public JSONObject getChanged_fields() {
		if (changed_fields == null) {
			changed_fields = new JSONObject();
		}
		return changed_fields;
	}

	public void setChanged_fields(JSONObject changed_fields) {
		this.changed_fields = changed_fields;
	}

	public Date getAction_tstamp_clk() {
		return action_tstamp_clk;
	}

	public void setAction_tstamp_clk(Date action_tstamp_clk) {
		this.action_tstamp_clk = action_tstamp_clk;
	}

	public List<AtributoJsonVO> getListaAtributoJsonRow_dataVOs() {
		if (listaAtributoJsonRow_dataVOs == null) {
			listaAtributoJsonRow_dataVOs = new ArrayList<AtributoJsonVO>(0);
		}
		return listaAtributoJsonRow_dataVOs;
	}

	public void setListaAtributoJsonRow_dataVOs(List<AtributoJsonVO> listaAtributoJsonRow_dataVOs) {
		this.listaAtributoJsonRow_dataVOs = listaAtributoJsonRow_dataVOs;
	}

	public List<AtributoJsonVO> getListaAtributoJsonChanged_fieldsVOs() {
		if (listaAtributoJsonChanged_fieldsVOs == null) {
			listaAtributoJsonChanged_fieldsVOs = new ArrayList<AtributoJsonVO>(0);
		}
		return listaAtributoJsonChanged_fieldsVOs;
	}

	public void setListaAtributoJsonChanged_fieldsVOs(List<AtributoJsonVO> listaAtributoJsonChanged_fieldsVOs) {
		this.listaAtributoJsonChanged_fieldsVOs = listaAtributoJsonChanged_fieldsVOs;
	}
	
}
