package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsavel por manter os dados da entidade GrupoLancamentoFolhaPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class GrupoLancamentoFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 680229763423295280L;

	private Integer codigo;
	private String nome;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento;

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

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamento() {
		if (templateLancamentoFolhaPagamento == null) {
			templateLancamentoFolhaPagamento = new TemplateLancamentoFolhaPagamentoVO();
		}
		return templateLancamentoFolhaPagamento;
	}

	public void setTemplateLancamentoFolhaPagamento(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento) {
		this.templateLancamentoFolhaPagamento = templateLancamentoFolhaPagamento;
	}
}
