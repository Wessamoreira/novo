package negocio.comuns.compras;

import java.util.Objects;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class CategoriaProdutoVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	private CategoriaDespesaVO categoriaDespesa;
	private TramiteCotacaoCompraVO tramiteCotacaoCompra = new TramiteCotacaoCompraVO();
	private CategoriaProdutoVO categoriaProdutoPai;
	private Boolean obrigarDataNecessidadeRequisicao;
	private QuestionarioVO questionarioAberturaRequisicao;
	private QuestionarioVO questionarioEntregaRequisicao;
	
	private boolean filtrarCategoriaProduto;

	public CategoriaProdutoVO() {
		super();
	}

	public static void validarDados(CategoriaProdutoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Categoria do Produto) deve ser informado.");
		}

		if (obj.isPossuiTramiteEspecifico() && Objects.isNull(obj.getTramiteCotacaoCompra().getCodigo())) {
			throw new ConsistirException("Quando o campo (Possui Trâmite Cotação Especifico	) for marcado, o campo (Tramite Cotação Compra) deve ser selecionado.");
		}
		;
		if (obj.getCategoriaDespesa() == null || obj.getCategoriaDespesa().getCodigo() == null || obj.getCategoriaDespesa().getCodigo().equals(0)) {
			throw new ConsistirException("O campo CATEGORIA DE DESPESA (Categoria do Produto) deve ser informado.");
		}

		if (obj.isPossuiTramiteEspecifico() && Objects.isNull(obj.getTramiteCotacaoCompra().getCodigo()) && obj.getTramiteCotacaoCompra().getCodigo() == 0) {
			throw new ConsistirException("O campo TRÂMITE COTAÇÃO (Categoria do Produto) deve ser informado.");
		}
	}

	public CategoriaProdutoVO getCategoriaProdutoPai() {
		return categoriaProdutoPai;
	}

	public void setCategoriaProdutoPai(CategoriaProdutoVO categoriaProdutoPai) {
		this.categoriaProdutoPai = categoriaProdutoPai;
	}

	public boolean isPossuiTramiteEspecifico() {
		return Uteis.isAtributoPreenchido(getTramiteCotacaoCompra());
	}	

	public TramiteCotacaoCompraVO getTramiteCotacaoCompra() {
		if (tramiteCotacaoCompra == null) {
			tramiteCotacaoCompra = new TramiteCotacaoCompraVO();
		}
		return tramiteCotacaoCompra;
	}

	public void setTramiteCotacaoCompra(TramiteCotacaoCompraVO tramiteCotacaoCompra) {
		this.tramiteCotacaoCompra = tramiteCotacaoCompra;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CategoriaDespesaVO getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}


	public Boolean getObrigarDataNecessidadeRequisicao() {
		return obrigarDataNecessidadeRequisicao;
	}


	public void setObrigarDataNecessidadeRequisicao(Boolean obrigarDataNecessidadeRequisicao) {
		this.obrigarDataNecessidadeRequisicao = obrigarDataNecessidadeRequisicao;
	}
	
	public QuestionarioVO getQuestionarioAberturaRequisicao() {
		if (questionarioAberturaRequisicao == null) {
			questionarioAberturaRequisicao = new QuestionarioVO();
		}
		return questionarioAberturaRequisicao;
	}


	public void setQuestionarioAberturaRequisicao(QuestionarioVO questionarioAberturaRequisicao) {
		this.questionarioAberturaRequisicao = questionarioAberturaRequisicao;
	}


	public QuestionarioVO getQuestionarioEntregaRequisicao() {
		if (questionarioEntregaRequisicao == null) {
			questionarioEntregaRequisicao = new QuestionarioVO();
		}
		return questionarioEntregaRequisicao;
	}


	public void setQuestionarioEntregaRequisicao(QuestionarioVO questionarioEntregaRequisicao) {
		this.questionarioEntregaRequisicao = questionarioEntregaRequisicao;
	}

	public boolean isFiltrarCategoriaProduto() {
		return filtrarCategoriaProduto;
	}

	public void setFiltrarCategoriaProduto(boolean filtrarCategoriaProduto) {
		this.filtrarCategoriaProduto = filtrarCategoriaProduto;
	}
}