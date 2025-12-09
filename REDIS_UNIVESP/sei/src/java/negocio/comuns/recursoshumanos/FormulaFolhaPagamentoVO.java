package negocio.comuns.recursoshumanos;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;

public class FormulaFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = -9198723431709364628L;

	private Integer codigo;
	private String identificador;
	private String descricao;
	private String formula;
	private AtivoInativoEnum situacao;
	
	//Transiente
	private String resultadoLog;

	public Integer getCodigo() {

		this.codigo = Optional.ofNullable(this.codigo).orElse(Integer.valueOf(0));
		return this.codigo;
	}

	public String getIdentificador() {
		this.identificador = Optional.ofNullable(this.identificador).orElse("");
		return this.identificador;
	}

	public String getDescricao() {
		this.descricao = Optional.ofNullable(this.descricao).orElse("");
		return descricao;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFormula() {
		this.formula = Optional.ofNullable(this.formula).orElse("");
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public AtivoInativoEnum getSituacao() {
		return this.situacao;
	}

	public void setSituacao(AtivoInativoEnum situacao) {
		this.situacao = situacao;
	}

	public String getResultadoLog() {
		return resultadoLog;
	}

	public void setResultadoLog(String resultadoLog) {
		this.resultadoLog = resultadoLog;
	}

	public void validarDados() {
		Preconditions.checkState(!Strings.isNullOrEmpty(this.getIdentificador()), "msg_FormulaFolhaPagamento_identificador_vazio");
		Preconditions.checkState(!Strings.isNullOrEmpty(this.getDescricao()), "msg_FormulaFolhaPagamento_descricao_vazio");
		Preconditions.checkState(!Strings.isNullOrEmpty(this.getFormula()), "msg_FormulaFolhaPagamento_formula_vazio");
	}

}