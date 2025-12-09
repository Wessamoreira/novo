package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoMovimentoPensaoEnum;

public class HistoricoPensaoVO extends SuperVO {

	private static final long serialVersionUID = 6648336511589430594L;

	private Integer codigo;
	private FuncionarioDependenteVO funcionarioDependente;
	private BigDecimal valor;
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamento;
	private TipoMovimentoPensaoEnum tipoMovimentoPensao;
	
	public enum EnumCampoConsultaHistoricoPensao {
		FUNCIONARIO, MATRICULA_CARGO, MATRICULA_FUNCIONARIO, CARGO;
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

	public FuncionarioDependenteVO getFuncionarioDependente() {
		if (funcionarioDependente == null) {
			funcionarioDependente = new FuncionarioDependenteVO();
		}
		return funcionarioDependente;
	}

	public void setFuncionarioDependente(FuncionarioDependenteVO funcionarioDependente) {
		this.funcionarioDependente = funcionarioDependente;
	}

	public BigDecimal getValor() {
		if (valor == null) {
			valor = BigDecimal.ZERO;
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamento() {
		if (competenciaFolhaPagamento == null) {
			competenciaFolhaPagamento = new CompetenciaFolhaPagamentoVO();
		}
		return competenciaFolhaPagamento;
	}

	public void setCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		this.competenciaFolhaPagamento = competenciaFolhaPagamento;
	}

	public TipoMovimentoPensaoEnum getTipoMovimentoPensao() {
		if (tipoMovimentoPensao == null) {
			tipoMovimentoPensao = TipoMovimentoPensaoEnum.MOVIMENTO;
		}
		return tipoMovimentoPensao;
	}

	public void setTipoMovimentoPensao(TipoMovimentoPensaoEnum tipoMovimentoPensao) {
		this.tipoMovimentoPensao = tipoMovimentoPensao;
	}

}
