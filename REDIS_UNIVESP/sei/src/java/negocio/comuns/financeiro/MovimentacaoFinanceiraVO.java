package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
/**
 * Reponsável por manter os dados da entidade MovimentacaoFinanceira. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
import negocio.comuns.utilitarias.dominios.SituacaoMovimentacaoFinanceiraEnum;

public class MovimentacaoFinanceiraVO extends SuperVO {

	private Integer codigo;
	private Date data;
	// private String tipoMovimentacaoFinanceira;
	// private String tipoMoeda;
	private Double valor;
	/**
	 * Atributo responsável por manter os objetos da classe <code>MovimentacaoFinanceiraChqR</code>.
	 */
	private List<MovimentacaoFinanceiraItemVO> movimentacaoFinanceiraItemVOs;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Usuario </code>.
	 */
	private UsuarioVO responsavel;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>ContaCorrente </code>.
	 */
	private ContaCorrenteVO contaCorrenteOrigem;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>ContaCorrente </code>.
	 */
	private ContaCorrenteVO contaCorrenteDestino;
	private String descricao;
	private boolean somenteContaDestino;
	private String situacao;
	private String motivoRecusa;
	private Boolean responsavelContaCorrenteDestino;
	private boolean desconsiderandoContabilidadeConciliacao = false;

	/**
	 * transient
	 */
	private boolean pularMapaPendenciaMovimentacaoFinanceira = false;
	private boolean desconsiderarConciliacaoBancaria = false;
	private boolean lancamentoContabil = false;
	private List<LancamentoContabilVO> listaLancamentoContabeisDebito;
	private List<LancamentoContabilVO> listaLancamentoContabeisCredito;	

	private UnidadeEnsinoVO unidadeEnsinoVO;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>MovimentacaoFinanceira</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public MovimentacaoFinanceiraVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>MovimentacaoFinanceiraVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(MovimentacaoFinanceiraVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (Movimentação Financeira) deve ser informado.");
		}
		if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL (Movimentação Financeira) deve ser informado.");
		}
		if (obj.isSomenteContaDestino() == false) {
			if ((obj.getContaCorrenteOrigem() == null) || (obj.getContaCorrenteOrigem().getCodigo().intValue() == 0)) {
				throw new ConsistirException("O campo CONTA CORRENTE ORIGEM (Movimentação Financeira) deve ser informado.");
			}
		}
		if ((obj.getContaCorrenteDestino() == null) || (obj.getContaCorrenteDestino().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CONTA CORRENTE DESTINO (Movimentação Financeira) deve ser informado.");
		}
		if (obj.getContaCorrenteDestino().getCodigo().intValue() == obj.getContaCorrenteOrigem().getCodigo()) {
			throw new ConsistirException("O campo CONTA CORRENTE ORIGEM (Movimentação Financeira) deve ser diferente de CONTA CORRENTE DESTINO (Movimentação Financeira).");
		}
		if (obj.getMovimentacaoFinanceiraItemVOs().isEmpty()) {
			throw new ConsistirException("Os Itens da Movimentação financeira devem ser informados.");
		}
		if (obj.getUnidadeEnsinoVO() == null || obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			throw new ConsistirException("A unidade de ensino deve ser informada.");
		}
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>MovimentacaoFinanceiraItemVO</code> ao List <code>movimentacaoFinanceiraItemVOs</code>. Utiliza o atributo padrão de consulta da classe <code>MovimentacaoFinanceiraChqR</code> - getChqRLog().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>MovimentacaoFinanceiraItemVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjMovimentacaoFinanceiraItemVOs(MovimentacaoFinanceiraItemVO obj) throws Exception {
		MovimentacaoFinanceiraItemVO.validarDados(obj);
		int index = 0;
		Iterator i = getMovimentacaoFinanceiraItemVOs().iterator();
		while (i.hasNext()) {
			MovimentacaoFinanceiraItemVO objExistente = (MovimentacaoFinanceiraItemVO) i.next();
			if (objExistente.getFormaPagamento().getTipo().equals("CH")
					&& obj.getFormaPagamento().getTipo().equals(objExistente.getFormaPagamento().getTipo())) {
				if (objExistente.getCheque().getCodigo().equals(obj.getCheque().getCodigo())) {
					getMovimentacaoFinanceiraItemVOs().set(index, obj);
					return;
				}
			} else if ((obj.getFormaPagamento().getTipo().equals("DI")) && (objExistente.getFormaPagamento().getTipo().equals("DI"))) {
				objExistente.setValor(objExistente.getValor() + obj.getValor());
				setValor(obj.getValor() + getValor());
				return;
			}
			index++;
		}
		setValor(obj.getValor() + getValor());
		getMovimentacaoFinanceiraItemVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>MovimentacaoFinanceiraItemVO</code> no List <code>movimentacaoFinanceiraItemVOs</code>. Utiliza o atributo padrão de consulta da classe <code>MovimentacaoFinanceiraChqR</code> - getChqRLog().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param chqRLog
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjMovimentacaoFinanceiraItemVOs(MovimentacaoFinanceiraItemVO obj) throws Exception {
		int index = 0;
		Iterator i = getMovimentacaoFinanceiraItemVOs().iterator();
		while (i.hasNext()) {
			MovimentacaoFinanceiraItemVO objExistente = (MovimentacaoFinanceiraItemVO) i.next();
			if (objExistente.getFormaPagamento().getTipo().equals("CH") && obj.getFormaPagamento().getTipo().equals(objExistente.getFormaPagamento().getTipo())) {
				if (objExistente.getCheque().getCodigo().equals(obj.getCheque().getCodigo())) {
					setValor(Uteis.arrendondarForcando2CadasDecimais(getValor() - obj.getValor()));
					getMovimentacaoFinanceiraItemVOs().remove(index);
					return;
				}
			} else if (objExistente.getFormaPagamento().getTipo().equals("DI") && obj.getFormaPagamento().getTipo().equals(objExistente.getFormaPagamento().getTipo())) {
				getMovimentacaoFinanceiraItemVOs().remove(index);
				setValor(Uteis.arrendondarForcando2CadasDecimais(getValor() - obj.getValor()));
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe <code>MovimentacaoFinanceiraItemVO</code> no List <code>movimentacaoFinanceiraItemVOs</code>. Utiliza o atributo padrão de consulta da classe <code>MovimentacaoFinanceiraChqR</code> - getChqRLog().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param chqRLog
	 *            Parâmetro para localizar o objeto do List.
	 */
	public MovimentacaoFinanceiraItemVO consultarObjMovimentacaoFinanceiraItemVO(Integer chqRLog) throws Exception {
		Iterator i = getMovimentacaoFinanceiraItemVOs().iterator();
		while (i.hasNext()) {
			MovimentacaoFinanceiraItemVO objExistente = (MovimentacaoFinanceiraItemVO) i.next();
			if (objExistente.getCheque().getCodigo().equals(chqRLog)) {
				return objExistente;
			}
		}
		return null;
	}

	public Boolean getEdicao() {
		if (getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com ( <code>MovimentacaoFinanceira</code>).
	 */
	public ContaCorrenteVO getContaCorrenteDestino() {
		if (contaCorrenteDestino == null) {
			contaCorrenteDestino = new ContaCorrenteVO();
		}
		return (contaCorrenteDestino);
	}

	/**
	 * Define o objeto da classe <code>ContaCorrente</code> relacionado com ( <code>MovimentacaoFinanceira</code>).
	 */
	public void setContaCorrenteDestino(ContaCorrenteVO obj) {
		this.contaCorrenteDestino = obj;
	}

	/**
	 * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com ( <code>MovimentacaoFinanceira</code>).
	 */
	public ContaCorrenteVO getContaCorrenteOrigem() {
		if (contaCorrenteOrigem == null) {
			contaCorrenteOrigem = new ContaCorrenteVO();
		}
		return (contaCorrenteOrigem);
	}

	/**
	 * Define o objeto da classe <code>ContaCorrente</code> relacionado com ( <code>MovimentacaoFinanceira</code>).
	 */
	public void setContaCorrenteOrigem(ContaCorrenteVO obj) {
		this.contaCorrenteOrigem = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Usuario</code> relacionado com ( <code>MovimentacaoFinanceira</code>).
	 */
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Usuario</code> relacionado com ( <code>MovimentacaoFinanceira</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public List<MovimentacaoFinanceiraItemVO> getMovimentacaoFinanceiraItemVOs() {
		if (movimentacaoFinanceiraItemVOs == null) {
			movimentacaoFinanceiraItemVOs = new ArrayList<MovimentacaoFinanceiraItemVO>(0);
		}
		return movimentacaoFinanceiraItemVOs;
	}

	public void setMovimentacaoFinanceiraItemVOs(List<MovimentacaoFinanceiraItemVO> movimentacaoFinanceiraItemVOs) {
		this.movimentacaoFinanceiraItemVOs = movimentacaoFinanceiraItemVOs;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return (valor);
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	//
	// public String getTipoMoeda() {
	// return (tipoMoeda);
	// }
	// /**
	// * Operação responsável por retornar o valor de apresentação de um
	// atributo com um domínio específico.
	// * Com base no valor de armazenamento do atributo esta função é capaz de
	// retornar o
	// * de apresentação correspondente. Útil para campos como sexo,
	// escolaridade, etc.
	// */
	// public String getTipoMoeda_Apresentar() {
	// if (tipoMoeda.equals("CH")) {
	// return "Cheque";
	// }
	// if (tipoMoeda.equals("DI")) {
	// return "Dinheiro";
	// }
	// return (tipoMoeda);
	// }
	//
	// public void setTipoMoeda( String tipoMoeda ) {
	// this.tipoMoeda = tipoMoeda;
	// }
	// public String getTipoMovimentacaoFinanceira() {
	// return (tipoMovimentacaoFinanceira);
	// }
	//
	// /**
	// * Operação responsável por retornar o valor de apresentação de um
	// atributo com um domínio específico.
	// * Com base no valor de armazenamento do atributo esta função é capaz de
	// retornar o
	// * de apresentação correspondente. Útil para campos como sexo,
	// escolaridade, etc.
	// */
	// public String getTipoMovimentacaoFinanceira_Apresentar() {
	// if (tipoMovimentacaoFinanceira.equals("S")) {
	// return "Saída";
	// }
	// if (tipoMovimentacaoFinanceira.equals("E")) {
	// return "Entrada";
	// }
	// return (tipoMovimentacaoFinanceira);
	// }
	//
	// public void setTipoMovimentacaoFinanceira( String
	// tipoMovimentacaoFinanceira ) {
	// this.tipoMovimentacaoFinanceira = tipoMovimentacaoFinanceira;
	// }
	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return (data);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getDataComHora(data));
	}

	public void setData(Date data) {
		this.data = data;
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

	/**
	 * @return the somenteContaDestino
	 */
	public boolean isSomenteContaDestino() {
		return somenteContaDestino;
	}

	/**
	 * @param somenteContaDestino
	 *            the somenteContaDestino to set
	 */
	public void setSomenteContaDestino(boolean somenteContaDestino) {
		this.somenteContaDestino = somenteContaDestino;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "PE";
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {
		return SituacaoMovimentacaoFinanceiraEnum.getDescricao(getSituacao());
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMotivoRecusa() {
		if (motivoRecusa == null) {
			motivoRecusa = "";
		}
		return motivoRecusa;
	}

	public void setMotivoRecusa(String motivoRecusa) {
		this.motivoRecusa = motivoRecusa;
	}

	public boolean getIsSituacaoPendente() {
		return (getSituacao().equals("PE"));
	}
	
	public boolean isSituacaoFinalizada() {
		return (getSituacao().equals("FI"));
	}

	public Boolean getResponsavelContaCorrenteDestino() {
		if (responsavelContaCorrenteDestino == null) {
			responsavelContaCorrenteDestino = false;
		}
		return responsavelContaCorrenteDestino;
	}

	public void setResponsavelContaCorrenteDestino(boolean responsavelContaCorrenteDestino) {
		this.responsavelContaCorrenteDestino = responsavelContaCorrenteDestino;
	}

	public boolean getIsSituacaoPendenteResponsavelContaCorrenteDestino() {
		return (getSituacao().equals("PE") && getResponsavelContaCorrenteDestino());
	}

	public boolean isPularMapaPendenciaMovimentacaoFinanceira() {
		return pularMapaPendenciaMovimentacaoFinanceira;
	}

	public void setPularMapaPendenciaMovimentacaoFinanceira(boolean pularMapaPendenciaMovimentacaoFinanceira) {
		this.pularMapaPendenciaMovimentacaoFinanceira = pularMapaPendenciaMovimentacaoFinanceira;
	}

	public boolean isDesconsiderarConciliacaoBancaria() {
		return desconsiderarConciliacaoBancaria;
	}

	public void setDesconsiderarConciliacaoBancaria(boolean desconsiderarConciliacaoBancaria) {
		this.desconsiderarConciliacaoBancaria = desconsiderarConciliacaoBancaria;
	}

	public boolean isLancamentoContabil() {
		return lancamentoContabil;
	}

	public void setLancamentoContabil(boolean lancamentoContabil) {
		this.lancamentoContabil = lancamentoContabil;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisDebito() {
		if (listaLancamentoContabeisDebito == null) {
			listaLancamentoContabeisDebito = new ArrayList<>();
		}
		return listaLancamentoContabeisDebito;
	}

	public void setListaLancamentoContabeisDebito(List<LancamentoContabilVO> listaLancamentoContabeisDebito) {
		this.listaLancamentoContabeisDebito = listaLancamentoContabeisDebito;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisCredito() {
		if (listaLancamentoContabeisCredito == null) {
			listaLancamentoContabeisCredito = new ArrayList<>();
		}
		return listaLancamentoContabeisCredito;
	}

	public void setListaLancamentoContabeisCredito(List<LancamentoContabilVO> listaLancamentoContabeisCredito) {
		this.listaLancamentoContabeisCredito = listaLancamentoContabeisCredito;
	}

	public Double getTotalLancamentoContabeisCredito() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisCredito()) {
			valor = valor + lc.getValor();
		}
		return valor;
	}

	public Double getTotalLancamentoContabeisDebito() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisDebito()) {
			valor = valor + lc.getValor();
		}
		return valor;
	}

	public Double getTotalLancamentoContabeisCreditoTipoValorMov() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisCredito()) {
			if (lc.getTipoValorLancamentoContabilEnum().isMovimentacaoFinanceira()) {
				valor = valor + lc.getValor();
			}
		}
		return valor;
	}

	public Double getTotalLancamentoContabeisDebitoTipoValorMov() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisDebito()) {
			if (lc.getTipoValorLancamentoContabilEnum().isMovimentacaoFinanceira()) {
				valor = valor + lc.getValor();
			}
		}
		return valor;
	}

	public boolean isLancamentoContabilValido(List<LancamentoContabilVO> listaOrigem, List<LancamentoContabilVO> listaDestino) {
		for (LancamentoContabilVO lc : listaOrigem) {
			if (listaDestino.stream().noneMatch(p -> p.getPlanoContaVO().getCodigo().equals(lc.getPlanoContaVO().getCodigo()))) {
				return true;
			}
		}
		return false;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	public boolean isDesconsiderandoContabilidadeConciliacao() {
		return desconsiderandoContabilidadeConciliacao;
	}

	public void setDesconsiderandoContabilidadeConciliacao(boolean desconsiderandoContabilidadeConciliacao) {
		this.desconsiderandoContabilidadeConciliacao = desconsiderandoContabilidadeConciliacao;
	}
	

}
