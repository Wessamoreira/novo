package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsavel por manter os dados da entidade GrupoLancamentoFolhaPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TemplateEventoFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private BigDecimal valor;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if (eventoFolhaPagamento == null) {
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamentoVO() {
		if (templateLancamentoFolhaPagamentoVO == null)
			templateLancamentoFolhaPagamentoVO = new TemplateLancamentoFolhaPagamentoVO();
		return templateLancamentoFolhaPagamentoVO;
	}

	public void setTemplateLancamentoFolhaPagamentoVO(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO) {
		this.templateLancamentoFolhaPagamentoVO = templateLancamentoFolhaPagamentoVO;
	}
}