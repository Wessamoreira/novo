package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;

public class IntegracaoGinfesCursoItemVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private IntegracaoGinfesCursoVO integracao;
	private Integer codigoUnidadeEnsinoCurso;
	private String descricaoCursoTurno;
	private Double valorServico;
	private String itemListaServico;
	private Integer ativo;
	private Integer operacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public IntegracaoGinfesCursoVO getIntegracao() {
		if (integracao == null) {
			integracao = new IntegracaoGinfesCursoVO();
		}
		return integracao;
	}

	public void setIntegracao(IntegracaoGinfesCursoVO integracao) {
		this.integracao = integracao;
	}

	public Integer getCodigoUnidadeEnsinoCurso() {
		if (codigoUnidadeEnsinoCurso == null) {
			codigoUnidadeEnsinoCurso = 0;
		}
		return codigoUnidadeEnsinoCurso;
	}

	public void setCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) {
		this.codigoUnidadeEnsinoCurso = codigoUnidadeEnsinoCurso;
	}

	public String getDescricaoCursoTurno() {
		if (descricaoCursoTurno == null) {
			descricaoCursoTurno = "";
		}
		return descricaoCursoTurno;
	}

	public void setDescricaoCursoTurno(String descricaoCursoTurno) {
		this.descricaoCursoTurno = descricaoCursoTurno;
	}

	public Double getValorServico() {
		if (valorServico == null) {
			valorServico = 0.0;
		}
		return valorServico;
	}

	public void setValorServico(Double valorServico) {
		this.valorServico = valorServico;
	}

	public String getItemListaServico() {
		if (itemListaServico == null) {
			itemListaServico = "";
		}
		return itemListaServico;
	}

	public void setItemListaServico(String itemListaServico) {
		this.itemListaServico = itemListaServico;
	}

	public Integer getAtivo() {
		if (ativo == null) {
			ativo = 0;
		}
		return ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public Integer getOperacao() {
		if (operacao == null) {
			operacao = 0;
		}
		return operacao;
	}

	public void setOperacao(Integer operacao) {
		this.operacao = operacao;
	}
	
	public String getOperacaoApresentar() {
		if (getOperacao().intValue() == 0) {
			return "Incluir";
		} else {
			return "Alterar";
		}
	}
	
}
