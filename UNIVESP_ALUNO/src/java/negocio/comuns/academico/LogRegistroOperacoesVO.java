package negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import negocio.comuns.academico.enumeradores.OperacaoLogRegistroOperacoesEnum;
import negocio.comuns.academico.enumeradores.TabelaLogRegistroOperacoesEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class LogRegistroOperacoesVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private TabelaLogRegistroOperacoesEnum tabelaLogRegistroOperacoes;
	private OperacaoLogRegistroOperacoesEnum operacaoLogRegistroOperacoes;
	private UsuarioVO responsavel;
	private Date dataOperacao;
	private JSONArray row_data;
	private JSONArray changed_fields;
	private String observacao;
	
	public LogRegistroOperacoesVO() {
		
	}
	
	public LogRegistroOperacoesVO(JSONObject jsonObjectRowData, JSONObject jsonObjectChangedFields) {
		this.getRow_data().add(jsonObjectRowData);
		this.getChanged_fields().add(jsonObjectChangedFields);
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

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataOperacao() {
		if (dataOperacao == null) {
			dataOperacao = new Date();
		}
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public JSONArray getRow_data() {
		if (row_data == null) {
			row_data = new JSONArray();
		}
		return row_data;
	}

	public void setRow_data(JSONArray row_data) {
		this.row_data = row_data;
	}

	public JSONArray getChanged_fields() {
		if (changed_fields == null) {
			changed_fields = new JSONArray();
		}
		return changed_fields;
	}

	public void setChanged_fields(JSONArray changed_fields) {
		this.changed_fields = changed_fields;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public OperacaoLogRegistroOperacoesEnum getOperacaoLogRegistroOperacoes() {
		if (operacaoLogRegistroOperacoes == null) {
			operacaoLogRegistroOperacoes = OperacaoLogRegistroOperacoesEnum.ALTERACAO_SITUACAO_MANUAL_MATRICULA;
		}
		return operacaoLogRegistroOperacoes;
	}

	public void setOperacaoLogRegistroOperacoes(OperacaoLogRegistroOperacoesEnum operacaoLogRegistroOperacoes) {
		this.operacaoLogRegistroOperacoes = operacaoLogRegistroOperacoes;
	}

	public TabelaLogRegistroOperacoesEnum getTabelaLogRegistroOperacoes() {
		if (tabelaLogRegistroOperacoes == null) {
			tabelaLogRegistroOperacoes = TabelaLogRegistroOperacoesEnum.MATRICULA;
		}
		return tabelaLogRegistroOperacoes;
	}

	public void setTabelaLogRegistroOperacoes(TabelaLogRegistroOperacoesEnum tabelaLogRegistroOperacoes) {
		this.tabelaLogRegistroOperacoes = tabelaLogRegistroOperacoes;
	}

}
