package negocio.comuns.planoorcamentario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.planoorcamentario.enumeradores.SituacaoPlanoOrcamentarioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.ProvisaoCusto;

/**
 * Reponsável por manter os dados da entidade ItensProvisao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see ProvisaoCusto
 */
public class SolicitacaoOrcamentoPlanoOrcamentarioVO extends SuperVO {

	public static final long serialVersionUID = 1L;

	private Integer codigo;
	private PlanoOrcamentarioVO planoOrcamentario;
	private UnidadeEnsinoVO unidadeEnsino;
	private PessoaVO pessoa;
	private Double valorTotalSolicitado;
	private Double valorTotalAprovado;
	private Double valorConsumido;
	private SituacaoPlanoOrcamentarioEnum situacao;
	private DepartamentoVO departamento;
	private List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> itemSolicitacaoOrcamentoPlanoOrcamentarioVOs;

	//TRANSIENTE
	private BigDecimal saldoRestante;
	private String requisicoes;
	private String requisicaoitens;
	private List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVOs;
	private List<RequisicaoItemVO> requisicaoItemVOs;

	/**
	 * Construtor padrão da classe <code>ItensProvisao</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public SolicitacaoOrcamentoPlanoOrcamentarioVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ItensProvisaoVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(SolicitacaoOrcamentoPlanoOrcamentarioVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}

		if (obj.getPessoa().getCodigo().intValue() == 0) {
			throw new ConsistirException(
					"O campo FUNCIONÁRIO (Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}

		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO (Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}

		if (obj.getPlanoOrcamentario().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo PLANO ORÇAMENTÁRIO (Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}

		if (obj.getDepartamento().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo DEPARTAMENTO (Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}

		if (obj.getValorTotalSolicitado().doubleValue() == 0) {
			throw new ConsistirException("Deve ser informado ao menos um item solicitação de orçamento.");
		}
	}

	/**
	 * @return the pessoa
	 */
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the valorTotalSolicitado
	 */
	public Double getValorTotalSolicitado() {
		if (valorTotalSolicitado == null) {
			valorTotalSolicitado = 0.0;
		}
		return valorTotalSolicitado;
	}

	/**
	 * @param valorTotalSolicitado
	 *            the valorTotalSolicitado to set
	 */
	public void setValorTotalSolicitado(Double valorTotalSolicitado) {
		this.valorTotalSolicitado = valorTotalSolicitado;
	}	


	public String getSituacao_Apresentar() {
		if (getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO)) {
			return "Em Construção";
		}
		if (getSituacao().equals(SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO)) {
			return "Aguardando Aprovação";
		}
		if (getSituacao().equals(SituacaoPlanoOrcamentarioEnum.REVISAO)) {
			return "Revisão";
		}
		if (getSituacao().equals(SituacaoPlanoOrcamentarioEnum.APROVADO)) {
			return "Aprovado";
		}
		return "";
	}

	/**
	 * @return the situacao
	 */
	public SituacaoPlanoOrcamentarioEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}

	/**
	 * @param situacao
	 *            the situacao to set
	 */
	public void setSituacao(SituacaoPlanoOrcamentarioEnum situacao) {
		this.situacao = situacao;
	}

	/**
	 * @return the departamento
	 */
	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	/**
	 * @param departamento
	 *            the departamento to set
	 */
	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return the itemSolicitacaoOrcamentoPlanoOrcamentarioVOs
	 */
	public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs() {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioVOs == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioVOs = new ArrayList<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO>();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioVOs;
	}

	/**
	 * @param itemSolicitacaoOrcamentoPlanoOrcamentarioVOs
	 *            the itemSolicitacaoOrcamentoPlanoOrcamentarioVOs to set
	 */
	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(
			List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> itemSolicitacaoOrcamentoPlanoOrcamentarioVOs) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioVOs = itemSolicitacaoOrcamentoPlanoOrcamentarioVOs;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void emConstrucaoSolicitacaoOrcamentoPlanoOrcamentario() {
		this.setSituacao(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO);
	}

	public void aguardandoAprovacaoSolicitacaoOrcamentoPlanoOrcamentario() {
		this.setSituacao(SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO);
	}

	public void aprovadoSolicitacaoOrcamentoPlanoOrcamentario() {
		this.setSituacao(SituacaoPlanoOrcamentarioEnum.APROVADO);
	}

	/**
	 * @return the planoOrcamentario
	 */
	public PlanoOrcamentarioVO getPlanoOrcamentario() {
		if (planoOrcamentario == null) {
			planoOrcamentario = new PlanoOrcamentarioVO();
		}
		return planoOrcamentario;
	}

	/**
	 * @param planoOrcamentario
	 *            the planoOrcamentario to set
	 */
	public void setPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentario) {
		this.planoOrcamentario = planoOrcamentario;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public boolean getAtivo() {
		return Uteis.isAtributoPreenchido(getSituacao()) && getSituacao().equals("AT") ? true : false;
	}

	public BigDecimal getSaldoRestante() {
		if (saldoRestante == null) {
			saldoRestante = BigDecimal.ZERO;
		}
		return saldoRestante;
	}

	public void setSaldoRestante(BigDecimal saldoRestante) {
		this.saldoRestante = saldoRestante;
	}

	public String getRequisicoes() {
		if (requisicoes == null) {
			requisicoes = "";
		}
		return requisicoes;
	}

	public void setRequisicoes(String requisicoes) {
		this.requisicoes = requisicoes;
	}

	public String getRequisicaoitens() {
		if (requisicaoitens == null)  {
			requisicaoitens = "";
		}
		return requisicaoitens;
	}

	public void setRequisicaoitens(String requisicaoitens) {
		this.requisicaoitens = requisicaoitens;
	}

	
	public List<DetalhamentoPeriodoOrcamentoVO> getDetalhamentoPeriodoOrcamentoVOs() {
		if(detalhamentoPeriodoOrcamentoVOs == null) {
			detalhamentoPeriodoOrcamentoVOs = new ArrayList<DetalhamentoPeriodoOrcamentoVO>(0);
		}
		return detalhamentoPeriodoOrcamentoVOs;
	}

	public void setDetalhamentoPeriodoOrcamentoVOs(List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVOs) {
		this.detalhamentoPeriodoOrcamentoVOs = detalhamentoPeriodoOrcamentoVOs;
	}

	public Double getValorTotalAprovado() {
		if(valorTotalAprovado == null) {
			valorTotalAprovado = 0.0;
		}
		return valorTotalAprovado;
	}

	public void setValorTotalAprovado(Double valorTotalAprovado) {
		this.valorTotalAprovado = valorTotalAprovado;
	}

	public Double getValorConsumido() {
		if(valorConsumido == null) {
			valorConsumido = 0.0;
		}
		return valorConsumido;
	}

	public void setValorConsumido(Double valorConsumido) {
		this.valorConsumido = valorConsumido;
	}
	
	public List<RequisicaoItemVO> getRequisicaoItemVOs() {
		if (requisicaoItemVOs == null) {
			requisicaoItemVOs = new ArrayList<>(0);
		}
		return requisicaoItemVOs;
	}

	public void setRequisicaoItemVOs(List<RequisicaoItemVO> requisicaoItemVOs) {
		this.requisicaoItemVOs = requisicaoItemVOs;
	}	
}
