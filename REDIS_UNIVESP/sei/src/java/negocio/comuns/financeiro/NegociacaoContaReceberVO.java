/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.TipoAcrescimoEnum;
import negocio.comuns.financeiro.enumerador.TipoIntervaloParcelaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * 
 * @author rodrigo
 */
public class NegociacaoContaReceberVO extends SuperVO {

	private Integer codigo;
	private UsuarioVO responsavel;
	private UnidadeEnsinoVO unidadeEnsino;
	private Date data;
	private String tipoPessoa;
	private String tipoRenegociacao;
	private PessoaVO pessoa;
	private Double valor;
	private Double valorTotal;
	private Double juro;
	private Double multa;
	private Double desconto;
	private Double acrescimo;
	private Double acrescimoGeral;	
	private TipoAcrescimoEnum tipoAcrescimoPorParcela;
	private Double acrescimoPorParcela;
	private Double totalAcrescimoPorParcela;
	private Double valorEntrada;
	private Integer nrParcela;
	private Integer intervaloParcela;
	private Date dataBaseParcela;
	private TipoIntervaloParcelaEnum tipoIntervaloParcelaEnum;
	private List<ContaReceberNegociadoVO> contaReceberNegociadoVOs;
	private CentroReceitaVO centroReceita;
	private ContaCorrenteVO contaCorrente;
	private MatriculaVO matriculaAluno;
	private FuncionarioVO funcionario;
	private ParceiroVO parceiro;
	private FornecedorVO fornecedor;
	private DescontoProgressivoVO descontoProgressivoVO;
	private List<ContaReceberVO> novaContaReceber;
	private List listaOpcaoPagamento;
	private String justificativa;
	private String tipoDesconto;
	private CondicaoRenegociacaoVO condicaoRenegociacao;
	private ItemCondicaoRenegociacaoVO itemCondicaoRenegociacao;
	private Boolean liberarRenovacaoAposPagamentoPrimeiraParcela;
	private Boolean liberarRenovacaoAposPagamentoTodasParcelas;
	private ContaCorrenteVO contaCorrenteCaixaEstorno;
	private PessoaVO pessoaComissionada;
	private Double valorTotalJuro;
	private Double valorIsencaoTotalJuro;
	private Double valorIsencaoTotalJuroMaximo;
	private Double valorTotalMulta;
	private Double valorIsencaoTotalMulta;
	private Double valorIsencaoTotalMultaMaximo;
	private Double valorTotalDescontoPerdido;
	private Double valorConcecaoDescontoPerdido;
	private Double valorConcecaoDescontoPerdidoMaximo;
	
	// Transiente
	private List<OpcaoAlunoCondicaoRenegociacaoVO> opcaoAlunoCondicaoRenegociacaoVOs;
	// Transiente
	private GrupoDestinatariosVO grupoDestinatariosVO;
	private List<NegociacaoContaReceberPlanoDescontoVO> negociacaoContaReceberPlanoDescontoVOs;
	private Integer ordemDescontoAluno;
    private Boolean ordemDescontoAlunoValorCheio;
    private Integer ordemPlanoDesconto;
    private Boolean ordemPlanoDescontoValorCheio;
    private Integer ordemConvenio;
    private Boolean ordemConvenioValorCheio;
    private Integer ordemDescontoProgressivo;
    private Boolean ordemDescontoProgressivoValorCheio;
    private ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO;
    
    private Boolean permitirRenegociacaoApenasComCondicaoRenegociacao;
    
    private UsuarioVO responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao;
    private Date dataLiberacaoRenegociarDesativandoCondicaoRenegociacao;
    private Boolean liberarRenegociarDesativandoCondicaoRenegociacao;
    
    private UsuarioVO responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
    private Date dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
    private Boolean liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
     
    //Transient
    private Boolean apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
    private Boolean apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao;
    private boolean liberarIsencaoJuroMultaDescontoAcimaMaximo = false;
    private UsuarioVO usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo;
    private Double valorDescontoCondicaoRenegociacao;
    private Double valorAcrescimoCondicaoRenegociacao;
    private Boolean permitirPagamentoCartaoCreditoVisaoAluno;
    
    private List<ItemCondicaoRenegociacaoVO> itemCondicaoRenegociacaoVOs;

  	private Double valorJuroDesconto;
  	private Double valorMultaDesconto;
  	private Double valorIndiceReajusteDesconto;
  	
  	private AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO;

	public static final long serialVersionUID = 1L;

	public Boolean getTipoAluno() {
		if (getTipoPessoa().equals("AL")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoResponsavelFinanceiro() {
		return getTipoPessoa().equals("RF");
	}
	public Boolean getTipoFornecedor() {
		return getTipoPessoa().equals("FO");
	}

	public Boolean getTipoParceiro() {
		if (getTipoPessoa().equals("PA")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoFuncionario() {
		if (getTipoPessoa().equals("FU")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public void adicionarObjContaReceberNegociadoVOs(ContaReceberNegociadoVO obj) throws Exception {
		Long unidadeFinanceira = getContaReceberNegociadoVOs().stream().filter(p-> p.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo().equals(obj.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo())).count();
		if(Uteis.isAtributoPreenchido(getContaReceberNegociadoVOs()) && !Uteis.isAtributoPreenchido(unidadeFinanceira)){
			throw new Exception("Não é possível adicionar uma conta receber de Unidade Ensino Financeira Diferente.");	
		}
		Iterator i = getContaReceberNegociadoVOs().iterator();
		while (i.hasNext()) {
			ContaReceberNegociadoVO objExistente = (ContaReceberNegociadoVO) i.next();
			if (objExistente.getContaReceber().getCodigo().equals(obj.getContaReceber().getCodigo())) {
				return;
			}
		}
		obj.setNegociacaoContaReceber(this);
		setValor(Uteis.arrendondarForcando2CadasDecimais(getValor() + obj.getValor()));
		setValorTotalJuro(Uteis.arrendondarForcando2CadasDecimais(getValorTotalJuro() + obj.getContaReceber().getJuro()));
		setValorTotalMulta(Uteis.arrendondarForcando2CadasDecimais(getValorTotalMulta() + obj.getContaReceber().getMulta()));
		obj.setNrDiasAtraso(obj.getContaReceber().getNrDiasAtraso());
		calcularValorTotal();
		getContaReceberNegociadoVOs().add(obj);
	}

	public void excluirObjContaReceberNegociadoVOs(ContaReceberNegociadoVO obj) {
		Iterator<ContaReceberNegociadoVO> i = getContaReceberNegociadoVOs().iterator();
		while (i.hasNext()) {
			ContaReceberNegociadoVO objExistente = (ContaReceberNegociadoVO) i.next();
			if (objExistente.getContaReceber().getCodigo().equals(obj.getContaReceber().getCodigo())) {
				setValor(Uteis.arrendondarForcando2CadasDecimais(getValor() - obj.getValor()));
				setValorTotalJuro(Uteis.arrendondarForcando2CadasDecimais(getValorTotalJuro() - obj.getContaReceber().getJuro()));
				setValorTotalMulta(Uteis.arrendondarForcando2CadasDecimais(getValorTotalMulta() - obj.getContaReceber().getMulta()));
				calcularValorTotal();
				i.remove();
				objExistente = null;
				return;
			}
		}
	}

	public void calcularValorTotal() {
        Double valor = getValor();        
        valor = valor + getJuroCalculado();
        valor = valor - getDescontoCalculado();
    	valor = valor + getAcrescimo();    	
        setValorTotal(Uteis.arrendondarForcando2CadasDecimais(valor));
    }

	public void gerarOpcoesPagamento(PerfilEconomicoVO perfilEconomicoVO) throws Exception {
		calcularValorTotal();
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
		configuracaoFinanceiroVO.getCentroReceitaNegociacaoPadrao().setCodigo(getCentroReceita().getCodigo());
		configuracaoFinanceiroVO.setContaCorrentePadraoNegociacao(getContaCorrente().getCodigo());
		montarOpcaoPagamentoDivida(configuracaoFinanceiroVO, perfilEconomicoVO);
	}

	public void montarOpcaoPagamentoDivida(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PerfilEconomicoVO perfilEconomicoVO) throws Exception {
		setListaOpcaoPagamento(new ArrayList());
		Iterator i = perfilEconomicoVO.getPerfilEconomicoCondicaoNegociacaoVOs().iterator();
		while (i.hasNext()) {
			PerfilEconomicoCondicaoNegociacaoVO obj = (PerfilEconomicoCondicaoNegociacaoVO) i.next();
			if ((obj.getCondicaoNegociacao().getValorMinimoValido().doubleValue() > 0) && (obj.getCondicaoNegociacao().getValorMinimoValido().doubleValue() > getValorTotal().doubleValue()) && (obj.getCondicaoNegociacao().getValorMaximoValido().doubleValue() > 0) && (obj.getCondicaoNegociacao().getValorMaximoValido().doubleValue() < getValorTotal().doubleValue())) {
				break;
			}
			Double valor = Uteis.arrendondarForcando2CadasDecimais(getValorTotal() + (getValorTotal() * (obj.getCondicaoNegociacao().getJuro() / 100)));
			Double desconto = Uteis.arrendondarForcando2CadasDecimais(valor * (obj.getCondicaoNegociacao().getDesconto() / 100));
			OpcaoPagamentoDividaVO opcaoPagamentoDividaVO = new OpcaoPagamentoDividaVO();
			String matricula = getMatricula();
			Integer pessoa = getCodigoPessoa();
			opcaoPagamentoDividaVO.montarListaCondicaoPagamento(valor, desconto, obj.getCondicaoNegociacao().getCondicaoPagamento().getParcelaCondicaoPagamentoVOs(), getUnidadeEnsino(), matricula, pessoa, tipoPessoa, configuracaoFinanceiroVO);
			getListaOpcaoPagamento().add(opcaoPagamentoDividaVO);
		}
	}

	public String getMatricula() {
		if (getTipoAluno()) {
			return getMatriculaAluno().getMatricula();
		}
		if (getTipoFuncionario()) {
			return getFuncionario().getMatricula();
		}
		return "";
	}

	public Integer getCodigoPessoa() {
		if (getTipoAluno()) {
			return getMatriculaAluno().getAluno().getCodigo();
		}
		if (getTipoFuncionario()) {
			return getFuncionario().getPessoa().getCodigo();
		}
		return getPessoa().getCodigo();
	}

	public Integer getCodigoPerfilEcnonomico() {
		if (getTipoAluno()) {
			return getMatriculaAluno().getAluno().getPerfilEconomico().getCodigo();
		}
		if (getTipoFuncionario()) {
			return getFuncionario().getPessoa().getPerfilEconomico().getCodigo();
		}
		return getPessoa().getPerfilEconomico().getCodigo();
	}
	
	public boolean isExisteContaReceberTipoOrigemCheque() {
		return getContaReceberNegociadoVOs().stream().anyMatch(p-> p.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor()));
	}

	public Boolean getExisteOpcaoPagamento() {
		if (getListaOpcaoPagamento().isEmpty()) {
			return false;
		}
		return true;
	}

	public Boolean getEditar() {
		if (getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public Boolean getExisteContaReceber() {
		return !getContaReceberNegociadoVOs().isEmpty();
	}

	public Boolean getOpcaoContaReceber() {
		if (getPessoa().getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public Boolean getExistePessoa() {
		if (getPessoa().getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public static void validarDados(NegociacaoContaReceberVO obj) throws ConsistirException {
		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("")) {
			throw new ConsistirException("O campo TIPO PESSOA (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("AL")) {
			if (obj.getMatriculaAluno() == null || obj.getMatriculaAluno().getMatricula().equals("")) {
				throw new ConsistirException("O campo ALUNO (Renegociação Conta Receber) deve ser informado.");
			}
			if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PESSOA (Renegociação Conta Receber) deve ser informado.");
			}
		} else if (obj.getTipoPessoa().equals("FU")) {
			if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo FUNCIONARIO (Renegociação Conta Receber) deve ser informado.");
			}
		}else if (obj.getTipoPessoa().equals("FO")) {
			if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo FORNECEDOR (Renegociação Conta Receber) deve ser informado.");
			}
		}else if (obj.getTipoPessoa().equals("PA")) {
			if (obj.getParceiro() == null || obj.getParceiro().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PARCEIRO (Renegociação Conta Receber) deve ser informado.");
			}
		} else {
			if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PESSOA (Renegociação Conta Receber) deve ser informado.");
			}
		}
		if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CONTA CORRENTE (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getCentroReceita() == null || obj.getCentroReceita().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CENTRO RECEITA (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getJustificativa().equals("")) {
			throw new ConsistirException("O campo JUSTIFICATIVA (Renegociação Conta Receber) deve ser informado.");
		}

		if (obj.getContaReceberNegociadoVOs().isEmpty()) {
			throw new ConsistirException("O campo CONTA RECEBER (Renegociação Conta Receber) deve ser informado.");
		}

		if (obj.getNrParcela().intValue() < 0 || (obj.getNrParcela().equals(0) && !obj.getValorEntrada().equals(obj.getValorTotal()))) {
			throw new ConsistirException("O campo  NÚMERO PARCELA (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getTipoIntervaloParcelaEnum().isIntervaloEntreDias() &&  obj.getIntervaloParcela().intValue() < 0) {
			throw new ConsistirException("O campo INTERVALO PARCELA (Renegociação Conta Receber) deve ser informado.");
		}
		
		if (obj.getTipoIntervaloParcelaEnum().isIntervaloDataBase() &&  obj.getDataBaseParcela() == null) {
			throw new ConsistirException("O campo DATA BASE PARCELA (Renegociação Conta Receber) deve ser informado.");
		}
		
		if (obj.getLiberarRenovacaoAposPagamentoTodasParcelas()) {
			obj.setLiberarRenovacaoAposPagamentoPrimeiraParcela(false);
		}
		for(ContaReceberVO conta: obj.getNovaContaReceber()){
			if(conta.getValor().equals(0.0)){
				throw new ConsistirException("Não é possível gerar uma CONTA RECEBER RENEGOCIADA com valor 0,00.");
			}
		}
		// if(obj.getNovaContaReceber().isEmpty()){
		// throw new
		// ConsistirException("Deve ser gerado as novas contas a pagar (Renegociação Conta Receber).");
		// }

	}

	public static void validarDadosBasicos(NegociacaoContaReceberVO obj) throws ConsistirException {
		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("")) {
			throw new ConsistirException("O campo TIPO PESSOA (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("AL")) {
			if (obj.getMatriculaAluno() == null || obj.getMatriculaAluno().getMatricula().equals("")) {
				throw new ConsistirException("O campo ALUNO (Renegociação Conta Receber) deve ser informado.");
			}
			if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PESSOA (Renegociação Conta Receber) deve ser informado.");
			}
		} else if (obj.getTipoPessoa().equals("FU")) {
			if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo FUNCIONARIO (Renegociação Conta Receber) deve ser informado.");
			}
		} else if (obj.getTipoPessoa().equals("PA")) {
			if (obj.getParceiro() == null || obj.getParceiro().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PARCEIRO (Renegociação Conta Receber) deve ser informado.");
			}
		} else if (obj.getTipoPessoa().equals("FO")) {
			if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo FORNECEDOR (Renegociação Conta Receber) deve ser informado.");
			}
		} else {
			if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo PESSOA (Renegociação Conta Receber) deve ser informado.");
			}
		}
		if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CONTA CORRENTE (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getCentroReceita() == null || obj.getCentroReceita().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CENTRO RECEITA (Renegociação Conta Receber) deve ser informado.");
		}
		if (obj.getJustificativa().equals("")) {
			throw new ConsistirException("O campo JUSTIFICATIVA (Renegociação Conta Receber) deve ser informado.");
		}

		if (obj.getContaReceberNegociadoVOs().isEmpty()) {
			throw new ConsistirException("O campo CONTA RECEBER (Renegociação Conta Receber) deve ser informado.");
		}

	}
	

	public String getDadosPessoaAtiva() {
		String pessoaAtiva = "";
		if (getTipoPessoa().equals("AL")) {
			pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
		} else if (getTipoPessoa().equals("FU")) {
			pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + getFuncionario().getPessoa().getNome() + " - " + getFuncionario().getMatricula();
		} else if (getTipoPessoa().equals("RF")) {
			pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + getPessoa().getNome();
		} else if (getTipoPessoa().equals("FO")) {
			pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + getFornecedor().getNome();
		} else if (getTipoPessoa().equals("PA")) {
			pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + getParceiro().getNome();		
		}
		return pessoaAtiva;
	}

	public String getTipoPessoa_apresentar() {
		if (getTipoPessoa().equals("FU")) {
			return "Funcionario";
		}
		if (getTipoPessoa().equals("AL")) {
			return "Aluno";
		}
		if (getTipoPessoa().equals("PA")) {
			return "Parceiro";
		}
		if (getTipoPessoa().equals("FO")) {
			return "Fornecedor";
		}
		if (getTipoPessoa().equals("RF")) {
			return "Responsável Financeiro";
		}
		return getTipoPessoa();
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

	public List<ContaReceberNegociadoVO> getContaReceberNegociadoVOs() {
		if (contaReceberNegociadoVOs == null) {
			contaReceberNegociadoVOs = new ArrayList<ContaReceberNegociadoVO>(0);
		}
		return contaReceberNegociadoVOs;
	}

	public void setContaReceberNegociadoVOs(List<ContaReceberNegociadoVO> contaReceberNegociadoVOs) {
		this.contaReceberNegociadoVOs = contaReceberNegociadoVOs;
	}

	public String getData_Apresentar() {
		return Uteis.getData(getData());
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
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

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}
	
	public Double getValorTotalSemIsencaoJurosMultaIndice() {
		return Uteis.arrendondarForcando2CadasDecimais(getValorTotal()+getValorDescontoIsencaoJuroMultaIndiceReajuste()-getValorAcrescimoCondicaoRenegociacao()+getValorDescontoCondicaoRenegociacao());
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}	
	
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Integer getIntervaloParcela() {
		if (intervaloParcela == null) {
			intervaloParcela = 0;
		}
		return intervaloParcela;
	}

	public void setIntervaloParcela(Integer intervaloParcela) {
		this.intervaloParcela = intervaloParcela;
	}

	public Integer getNrParcela() {
		if (nrParcela == null) {
			nrParcela = 0;
		}
		return nrParcela;
	}

	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	public Double getValorEntrada() {
		if (valorEntrada == null) {
			valorEntrada = 0.0;
		}
		return valorEntrada;
	}

	public void setValorEntrada(Double valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public CentroReceitaVO getCentroReceita() {
		if (centroReceita == null) {
			centroReceita = new CentroReceitaVO();
		}
		return centroReceita;
	}

	public void setCentroReceita(CentroReceitaVO centroReceita) {
		this.centroReceita = centroReceita;
	}

	public List<ContaReceberVO> getNovaContaReceber() {
		if (novaContaReceber == null) {
			novaContaReceber = new ArrayList<ContaReceberVO>(0);
		}
		return novaContaReceber;
	}

	public void setNovaContaReceber(List<ContaReceberVO> novaContaReceber) {
		this.novaContaReceber = novaContaReceber;
	}

	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public MatriculaVO getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = new MatriculaVO();
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(MatriculaVO matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public List getListaOpcaoPagamento() {
		if (listaOpcaoPagamento == null) {
			listaOpcaoPagamento = new ArrayList(0);
		}
		return listaOpcaoPagamento;
	}

	public void setListaOpcaoPagamento(List listaOpcaoPagamento) {
		this.listaOpcaoPagamento = listaOpcaoPagamento;
	}

	/**
	 * @return the descontoProgressivoVO
	 */
	public DescontoProgressivoVO getDescontoProgressivoVO() {
		if (descontoProgressivoVO == null) {
			descontoProgressivoVO = new DescontoProgressivoVO();
		}
		return descontoProgressivoVO;
	}

	/**
	 * @param descontoProgressivoVO
	 *            the descontoProgressivoVO to set
	 */
	public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
		this.descontoProgressivoVO = descontoProgressivoVO;
	}

	/**
	 * @return the parceiro
	 */
	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	/**
	 * @param parceiro
	 *            the parceiro to set
	 */
	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}

	public String getRequerente() {
		if (getFornecedor().getCodigo() != 0) {
			return getFornecedor().getNome();
		} else if (getParceiro().getCodigo() != 0) {
			return getParceiro().getNome();
		} else {
			if (!getMatriculaAluno().getMatricula().trim().isEmpty()) {
				return getMatriculaAluno().getMatricula() + " - " + getPessoa().getNome();
			}
			return getPessoa().getNome();
		}
	}

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "PO";
		}
		return tipoDesconto;
	}

	public String getTipoDesconto_Apresentar() {
		return TipoDescontoAluno.getDescricao(getTipoDesconto());
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	/**
	 * @return the tipoRenegociacao
	 */
	public String getTipoRenegociacao() {
		if (tipoRenegociacao == null) {
			tipoRenegociacao = "VE";
		}
		return tipoRenegociacao;
	}

	/**
	 * @param tipoRenegociacao
	 *            the tipoRenegociacao to set
	 */
	public void setTipoRenegociacao(String tipoRenegociacao) {
		this.tipoRenegociacao = tipoRenegociacao;
	}

	public List<OpcaoAlunoCondicaoRenegociacaoVO> getOpcaoAlunoCondicaoRenegociacaoVOs() {
		if (opcaoAlunoCondicaoRenegociacaoVOs == null) {
			opcaoAlunoCondicaoRenegociacaoVOs = new ArrayList<OpcaoAlunoCondicaoRenegociacaoVO>(0);
		}
		return opcaoAlunoCondicaoRenegociacaoVOs;
	}

	public void setOpcaoAlunoCondicaoRenegociacaoVOs(List<OpcaoAlunoCondicaoRenegociacaoVO> opcaoAlunoCondicaoRenegociacaoVOs) {
		this.opcaoAlunoCondicaoRenegociacaoVOs = opcaoAlunoCondicaoRenegociacaoVOs;
	}

	public GrupoDestinatariosVO getGrupoDestinatariosVO() {
		if (grupoDestinatariosVO == null) {
			grupoDestinatariosVO = new GrupoDestinatariosVO();
		}
		return grupoDestinatariosVO;
	}

	public void setGrupoDestinatariosVO(GrupoDestinatariosVO grupoDestinatariosVO) {
		this.grupoDestinatariosVO = grupoDestinatariosVO;
	}

	public CondicaoRenegociacaoVO getCondicaoRenegociacao() {
		if (condicaoRenegociacao == null) {
			condicaoRenegociacao = new CondicaoRenegociacaoVO();
		}
		return condicaoRenegociacao;
	}

	public void setCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacao) {
		this.condicaoRenegociacao = condicaoRenegociacao;
	}

	public ItemCondicaoRenegociacaoVO getItemCondicaoRenegociacao() {
		if (itemCondicaoRenegociacao == null) {
			itemCondicaoRenegociacao = new ItemCondicaoRenegociacaoVO();
		}
		return itemCondicaoRenegociacao;
	}

	public void setItemCondicaoRenegociacao(ItemCondicaoRenegociacaoVO itemCondicaoRenegociacao) {
		this.itemCondicaoRenegociacao = itemCondicaoRenegociacao;
	}

	public Boolean getLiberarRenovacaoAposPagamentoPrimeiraParcela() {
		if (liberarRenovacaoAposPagamentoPrimeiraParcela == null) {
			liberarRenovacaoAposPagamentoPrimeiraParcela = true;
		}
		return liberarRenovacaoAposPagamentoPrimeiraParcela;
	}

	public void setLiberarRenovacaoAposPagamentoPrimeiraParcela(Boolean liberarRenovacaoAposPagamentoPrimeiraParcela) {
		this.liberarRenovacaoAposPagamentoPrimeiraParcela = liberarRenovacaoAposPagamentoPrimeiraParcela;
	}

	public Boolean getLiberarRenovacaoAposPagamentoTodasParcelas() {
		if (liberarRenovacaoAposPagamentoTodasParcelas == null) {
			liberarRenovacaoAposPagamentoTodasParcelas = false;
		}
		return liberarRenovacaoAposPagamentoTodasParcelas;
	}

	public void setLiberarRenovacaoAposPagamentoTodasParcelas(Boolean liberarRenovacaoAposPagamentoTodasParcelas) {
		this.liberarRenovacaoAposPagamentoTodasParcelas = liberarRenovacaoAposPagamentoTodasParcelas;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Double getAcrescimo() {
		return getAcrescimoGeral() + getTotalAcrescimoPorParcela();
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}
	
	public ContaCorrenteVO getContaCorrenteCaixaEstorno() {
		if(contaCorrenteCaixaEstorno == null){
			contaCorrenteCaixaEstorno = new ContaCorrenteVO();
		}
		return contaCorrenteCaixaEstorno;
	}

	public void setContaCorrenteCaixaEstorno(ContaCorrenteVO contaCorrenteCaixaEstorno) {
		this.contaCorrenteCaixaEstorno = contaCorrenteCaixaEstorno;
	}
	
	public Date getDataBaseParcela() {		
		return dataBaseParcela;
	}

	public void setDataBaseParcela(Date dataBaseParcela) {
		this.dataBaseParcela = dataBaseParcela;
	}

	public TipoIntervaloParcelaEnum getTipoIntervaloParcelaEnum() {
		if(tipoIntervaloParcelaEnum == null){
			tipoIntervaloParcelaEnum = TipoIntervaloParcelaEnum.DATA_BASE;
		}
		return tipoIntervaloParcelaEnum;
	}

	public void setTipoIntervaloParcelaEnum(TipoIntervaloParcelaEnum tipoIntervaloParcelaEnum) {
		this.tipoIntervaloParcelaEnum = tipoIntervaloParcelaEnum;
	}

	public TipoAcrescimoEnum getTipoAcrescimoPorParcela() {
		if(tipoAcrescimoPorParcela == null){
			tipoAcrescimoPorParcela = TipoAcrescimoEnum.PORCENTAGEM;
		}
		return tipoAcrescimoPorParcela;
	}

	public void setTipoAcrescimoPorParcela(TipoAcrescimoEnum tipoAcrescimoPorParcela) {
		this.tipoAcrescimoPorParcela = tipoAcrescimoPorParcela;
	}

	public Double getAcrescimoPorParcela() {
		if(acrescimoPorParcela == null){
			acrescimoPorParcela = 0.0;
		}
		return acrescimoPorParcela;
	}

	public void setAcrescimoPorParcela(Double acrescimoPorParcela) {
		this.acrescimoPorParcela = acrescimoPorParcela;
	}

	public boolean isTotalAcresimoPorParcelaInformado(){
		return Uteis.isAtributoPreenchido(getTotalAcrescimoPorParcela());
	}
	
	public Double getTotalAcrescimoPorParcela() {
		if(totalAcrescimoPorParcela == null){
			totalAcrescimoPorParcela = 0.0;
		}
		return totalAcrescimoPorParcela;
	}

	public void setTotalAcrescimoPorParcela(Double totalAcrescimoPorParcela) {
		this.totalAcrescimoPorParcela = totalAcrescimoPorParcela;
	}

	public Double getAcrescimoGeral() {
		if(acrescimoGeral == null){
			acrescimoGeral =  0.0;
		}
		return acrescimoGeral;
	}

	public void setAcrescimoGeral(Double acrescimoGeral) {
		this.acrescimoGeral = acrescimoGeral;
	}
		

	public Double getDescontoCalculado(){		
		if (getTipoDesconto().equals("PO") && !getDesconto().equals(0.0)) {
        	return Uteis.arrendondarForcando2CadasDecimais((getValor() * (getDesconto() / 100)));
        }else{
        	return Uteis.arrendondarForcando2CadasDecimais(getDesconto());
        }
	}
	
	public Double getJuroCalculado(){		
		if (getJuro() <= 0 || getJuro() > 100) {
            setJuro(0.0);
            return 0.0;
        } else {
            return Uteis.arrendondarForcando2CadasDecimais((getValor() * (getJuro() / 100)));
        }
	}


	public List<NegociacaoContaReceberPlanoDescontoVO> getNegociacaoContaReceberPlanoDescontoVOs() {
		if(negociacaoContaReceberPlanoDescontoVOs == null){
			negociacaoContaReceberPlanoDescontoVOs = new ArrayList<NegociacaoContaReceberPlanoDescontoVO>(0);
		}
		return negociacaoContaReceberPlanoDescontoVOs;
	}

	public void setNegociacaoContaReceberPlanoDescontoVOs(
			List<NegociacaoContaReceberPlanoDescontoVO> negociacaoContaReceberPlanoDescontoVOs) {
		this.negociacaoContaReceberPlanoDescontoVOs = negociacaoContaReceberPlanoDescontoVOs;
	}
	 /**
     * @return the ordemDescontoAluno
     */
    public Integer getOrdemDescontoAluno() {
        return ordemDescontoAluno;
    }

    /**
     * @param ordemDescontoAluno
     *            the ordemDescontoAluno to set
     */
    public void setOrdemDescontoAluno(Integer ordemDescontoAluno) {
        this.ordemDescontoAluno = ordemDescontoAluno;
    }

    /**
     * @return the ordemPlanoDesconto
     */
    public Integer getOrdemPlanoDesconto() {
        return ordemPlanoDesconto;
    }

    /**
     * @param ordemPlanoDesconto
     *            the ordemPlanoDesconto to set
     */
    public void setOrdemPlanoDesconto(Integer ordemPlanoDesconto) {
        this.ordemPlanoDesconto = ordemPlanoDesconto;
    }

    /**
     * @return the ordemConvenio
     */
    public Integer getOrdemConvenio() {
        return ordemConvenio;
    }

    /**
     * @param ordemConvenio
     *            the ordemConvenio to set
     */
    public void setOrdemConvenio(Integer ordemConvenio) {
        this.ordemConvenio = ordemConvenio;
    }
    

    public Integer obterPosicaoOrdemListaDesconto(List<OrdemDescontoVO> listaOrdemDesconto, String tipoDesconto) {
        for (OrdemDescontoVO ordemDesconto : listaOrdemDesconto) {
            if (ordemDesconto.getDescricaoDesconto().equals(tipoDesconto)) {
                return ordemDesconto.getPosicaoAtual();
            }
        }
        return 0;
    }

    public void aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(List<OrdemDescontoVO> listaOrdemDesconto) {        
        this.setOrdemDescontoAluno(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Desconto Aluno"));
        this.setOrdemPlanoDesconto(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Plano Desconto"));
        this.setOrdemDescontoProgressivo(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Desc.Progressivo"));
    }

    public void alterarOrdemAplicacaoDescontosSubindoItem(OrdemDescontoVO itemSubir) {
        if (itemSubir.getPosicaoAtual().equals(0)) {
            // nao tem mais como subir
        }
        if (itemSubir.getPosicaoAtual().equals(1)) {
            // entao ele vai para a posicao 0, e o que está na posicao 0, vai
            // para 1)
            OrdemDescontoVO itemAcima = getOrdemDescontoVOs().get(0);
            itemAcima.setPosicaoAtual(1);
            itemSubir.setPosicaoAtual(0);
        }          
        Ordenacao.ordenarLista(getOrdemDescontoVOs(), "posicaoAtual");
        aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(getOrdemDescontoVOs());        
    }

    public OrdemDescontoVO obterOrdemDescontoProgressivoVO() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Desc.Progressivo");
        ordem.setValorCheio(this.ordemDescontoProgressivoValorCheio);
        ordem.setPosicaoAtual(this.ordemDescontoProgressivo);
        return ordem;
    }

    public OrdemDescontoVO obterOrdemDescontoVOConvenio() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Convênio");
        ordem.setValorCheio(this.ordemConvenioValorCheio);
        ordem.setPosicaoAtual(this.ordemConvenio);
        return ordem;
    }

    public OrdemDescontoVO obterOrdemDescontoVODescontoAluno() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Desconto Aluno");
        ordem.setValorCheio(this.ordemDescontoAlunoValorCheio);
        ordem.setPosicaoAtual(this.ordemDescontoAluno);
        return ordem;
    }

    public OrdemDescontoVO obterOrdemDescontoVOPlanoDesconto() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Plano Desconto");
        ordem.setValorCheio(this.ordemPlanoDescontoValorCheio);
        ordem.setPosicaoAtual(this.ordemPlanoDesconto);
        return ordem;
    }

    public List<OrdemDescontoVO> ordemDescontoVOs;
    public List<OrdemDescontoVO> getOrdemDescontoVOs() {
    	if(ordemDescontoVOs == null){    		
    		if ((this.ordemPlanoDesconto == null) || (this.ordemPlanoDesconto >= 4)) {
    			this.ordemPlanoDesconto = 0;
    		}
    		if ((this.ordemDescontoProgressivo == null) || (this.ordemDescontoProgressivo > 4)) {
    			this.ordemDescontoProgressivo = 1;
    		}    		
    		ordemDescontoVOs = new ArrayList<OrdemDescontoVO>(0);    		
    		ordemDescontoVOs.add(this.obterOrdemDescontoVOPlanoDesconto());
    		ordemDescontoVOs.add(this.obterOrdemDescontoProgressivoVO());    		       
    		Ordenacao.ordenarLista(ordemDescontoVOs, "posicaoAtual");
    	}
        return ordemDescontoVOs;
    }

    public void alterarOrdemAplicacaoDescontosDescentoItem(OrdemDescontoVO itemDescer) {
        if (itemDescer.getPosicaoAtual().equals(1)) {
        }       
        if (itemDescer.getPosicaoAtual().equals(0)) {
            // entao ele vai para a posicao 1, e o que está na posicao 2, vai
            // para 1)
            OrdemDescontoVO itemAbaixo = getOrdemDescontoVOs().get(1);
            itemAbaixo.setPosicaoAtual(0);
            itemDescer.setPosicaoAtual(1);
        }
        Ordenacao.ordenarLista(getOrdemDescontoVOs(), "posicaoAtual");
        aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(getOrdemDescontoVOs());
    }

    /**
     * @return the ordemDescontoAlunoValorCheio
     */
    public Boolean getOrdemDescontoAlunoValorCheio() {
        if (ordemDescontoAlunoValorCheio == null) {
            ordemDescontoAlunoValorCheio = true;
        }
        return ordemDescontoAlunoValorCheio;
    }

    /**
     * @param ordemDescontoAlunoValorCheio
     *            the ordemDescontoAlunoValorCheio to set
     */
    public void setOrdemDescontoAlunoValorCheio(Boolean ordemDescontoAlunoValorCheio) {
        this.ordemDescontoAlunoValorCheio = ordemDescontoAlunoValorCheio;
    }

    /**
     * @return the ordemPlanoDescontoValorCheio
     */
    public Boolean getOrdemPlanoDescontoValorCheio() {
        if (ordemPlanoDescontoValorCheio == null) {
            ordemPlanoDescontoValorCheio = true;
        }
        return ordemPlanoDescontoValorCheio;
    }

    /**
     * @param ordemPlanoDescontoValorCheio
     *            the ordemPlanoDescontoValorCheio to set
     */
    public void setOrdemPlanoDescontoValorCheio(Boolean ordemPlanoDescontoValorCheio) {
        this.ordemPlanoDescontoValorCheio = ordemPlanoDescontoValorCheio;
    }

    /**
     * @return the ordemConvenioValorCheio
     */
    public Boolean getOrdemConvenioValorCheio() {
        if (ordemConvenioValorCheio == null) {
            ordemConvenioValorCheio = true;
        }
        return ordemConvenioValorCheio;
    }

    /**
     * @param ordemConvenioValorCheio
     *            the ordemConvenioValorCheio to set
     */
    public void setOrdemConvenioValorCheio(Boolean ordemConvenioValorCheio) {
        this.ordemConvenioValorCheio = ordemConvenioValorCheio;
    }

    public void atualizarSituacaoValorCheioOrdemDesconto(OrdemDescontoVO ordemClicou) {
        if (ordemClicou.isDescontoAluno()) {
            this.setOrdemDescontoAlunoValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isPlanoDesconto()) {
            this.setOrdemPlanoDescontoValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isConvenio()) {
            this.setOrdemConvenioValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isDescontoProgressivo()) {
            this.setOrdemDescontoProgressivoValorCheio(ordemClicou.getValorCheio());
        }
    }

    /**
     * @return the ordemDescontoProgressivo
     */
    public Integer getOrdemDescontoProgressivo() {
        return ordemDescontoProgressivo;
    }

    /**
     * @param ordemDescontoProgressivo the ordemDescontoProgressivo to set
     */
    public void setOrdemDescontoProgressivo(Integer ordemDescontoProgressivo) {
        this.ordemDescontoProgressivo = ordemDescontoProgressivo;
    }

    /**
     * @return the ordemDescontoProgressivoValorCheio
     */
    public Boolean getOrdemDescontoProgressivoValorCheio() {
        return ordemDescontoProgressivoValorCheio;
    }

    /**
     * @param ordemDescontoProgressivoValorCheio the ordemDescontoProgressivoValorCheio to set
     */
    public void setOrdemDescontoProgressivoValorCheio(Boolean ordemDescontoProgressivoValorCheio) {
        this.ordemDescontoProgressivoValorCheio = ordemDescontoProgressivoValorCheio;
    }
    
    public void adicionarOrdemAplicacaoDesconto(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro){
    	if(Uteis.isAtributoPreenchido(planoFinanceiroAlunoVO)){
    		setOrdemConvenio(3);
    		setOrdemConvenioValorCheio(true);
    		setOrdemDescontoAluno(2);
    		setOrdemDescontoAlunoValorCheio(true);
    		setOrdemDescontoProgressivo(planoFinanceiroAlunoVO.getOrdemDescontoProgressivo());
    		setOrdemDescontoProgressivoValorCheio(planoFinanceiroAlunoVO.getOrdemDescontoProgressivoValorCheio());
    		setOrdemPlanoDesconto(planoFinanceiroAlunoVO.getOrdemPlanoDesconto());
    		setOrdemPlanoDescontoValorCheio(planoFinanceiroAlunoVO.getOrdemPlanoDescontoValorCheio());
    	}else{
    		setOrdemConvenio(3);
    		setOrdemConvenioValorCheio(true);
    		setOrdemDescontoAluno(2);
    		setOrdemDescontoAlunoValorCheio(true);
    		setOrdemDescontoProgressivo(configuracaoFinanceiro.getOrdemDescontoProgressivo());
    		setOrdemDescontoProgressivoValorCheio(configuracaoFinanceiro.getOrdemDescontoProgressivoValorCheio());
    		setOrdemPlanoDesconto(configuracaoFinanceiro.getOrdemPlanoDesconto());
    		setOrdemPlanoDescontoValorCheio(configuracaoFinanceiro.getOrdemPlanoDescontoValorCheio());
    	}
    	ordemDescontoVOs =  null;
    	int x = 0;
    	for(OrdemDescontoVO ordemDescontoVO: getOrdemDescontoVOs()){
    		ordemDescontoVO.setPosicaoAtual(x);
    		x++;
    	}
    	aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(ordemDescontoVOs);
    	
    }
    

	public boolean isExisteContasRenegociadasDeUnidadeEnsinoFinanceiraDiferentes(){
    	ContaReceberVO contaReceber = null;
    	for (ContaReceberNegociadoVO contaReceberNegociadoVO : getContaReceberNegociadoVOs()) {
    		if(!Uteis.isAtributoPreenchido(contaReceber) || !Uteis.isAtributoPreenchido(contaReceber.getUnidadeEnsinoFinanceira())){
    			contaReceber = contaReceberNegociadoVO.getContaReceber();
			}else if(!contaReceber.getUnidadeEnsinoFinanceira().getCodigo().equals(contaReceberNegociadoVO.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo())){
				return true;
			}
		}
    	
    	return false;
    }
    
    public Boolean getIsApresentarOrdemAplicarDescontos(){
    	return ((getDesconto() > 0 ? 1 : 0) + (Uteis.isAtributoPreenchido(getDescontoProgressivoVO()) ? 1 : 0) + (getNegociacaoContaReceberPlanoDescontoVOs().isEmpty()? 0 : 1)) > 1;
    }


	public ItemCondicaoDescontoRenegociacaoVO getItemCondicaoDescontoRenegociacaoVO() {
		itemCondicaoDescontoRenegociacaoVO = Optional.ofNullable(itemCondicaoDescontoRenegociacaoVO).orElse(new ItemCondicaoDescontoRenegociacaoVO());
		return itemCondicaoDescontoRenegociacaoVO;
	}

	public void setItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO) {
		this.itemCondicaoDescontoRenegociacaoVO = itemCondicaoDescontoRenegociacaoVO;
	}
	
	public boolean isExisteItemCondicaoDescontoRenegociacao(){
		return Uteis.isAtributoPreenchido(getItemCondicaoDescontoRenegociacaoVO());
	}
	
	public Boolean getPermitirRenegociacaoApenasComCondicaoRenegociacao() {
		if (permitirRenegociacaoApenasComCondicaoRenegociacao == null) {
			permitirRenegociacaoApenasComCondicaoRenegociacao = false;
		}
		return permitirRenegociacaoApenasComCondicaoRenegociacao;
	}

	public void setPermitirRenegociacaoApenasComCondicaoRenegociacao(Boolean permitirRenegociacaoApenasComCondicaoRenegociacao) {
		this.permitirRenegociacaoApenasComCondicaoRenegociacao = permitirRenegociacaoApenasComCondicaoRenegociacao;
	}

	public UsuarioVO getResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao() {
		if (responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao == null) {
			responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao = new UsuarioVO();
		}
		return responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao;
	}

	public void setResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao(UsuarioVO responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao) {
		this.responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao = responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao;
	}

	public Date getDataLiberacaoRenegociarDesativandoCondicaoRenegociacao() {
		return dataLiberacaoRenegociarDesativandoCondicaoRenegociacao;
	}

	public void setDataLiberacaoRenegociarDesativandoCondicaoRenegociacao(Date dataLiberacaoRenegociarDesativandoCondicaoRenegociacao) {
		this.dataLiberacaoRenegociarDesativandoCondicaoRenegociacao = dataLiberacaoRenegociarDesativandoCondicaoRenegociacao;
	}

	public Boolean getLiberarRenegociarDesativandoCondicaoRenegociacao() {
		if (liberarRenegociarDesativandoCondicaoRenegociacao == null) {
			liberarRenegociarDesativandoCondicaoRenegociacao = false;
		}
		return liberarRenegociarDesativandoCondicaoRenegociacao;
	}

	public void setLiberarRenegociarDesativandoCondicaoRenegociacao(Boolean liberarRenegociarDesativandoCondicaoRenegociacao) {
		this.liberarRenegociarDesativandoCondicaoRenegociacao = liberarRenegociarDesativandoCondicaoRenegociacao;
	}

	public Boolean getLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
		if (liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao == null) {
			liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = false;
		}
		return liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public void setLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(Boolean liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao) {
		this.liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public Boolean getApresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
		if (apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao == null) {
			apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = false;
		}
		return apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public void setApresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(Boolean apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao) {
		this.apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = apresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public Boolean getApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao() {
		if (apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao == null) {
			apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao = false;
		}
		return apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao;
	}

	public void setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(Boolean apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao) {
		this.apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao = apresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao;
	}

	public UsuarioVO getResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
		if (responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao == null) {
			responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = new UsuarioVO();
		}
		return responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public void setResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(UsuarioVO responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao) {
		this.responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public Date getDataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
		return dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public void setDataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(Date dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao) {
		this.dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}	

	public Double getValorTotalSemJuroMulta() {
		return Uteis.arrendondarForcando2CadasDecimais(getValor() - (getValorTotalJuro() + getValorTotalMulta()));
	}
	
	public PessoaVO getPessoaComissionada() {
		if (pessoaComissionada == null) {
			pessoaComissionada = new PessoaVO();
		}
		return pessoaComissionada;
	}

	public void setPessoaComissionada(PessoaVO pessoaComissionada) {
		this.pessoaComissionada = pessoaComissionada;
	}

	public Double getValorTotalJuro() {
		if (valorTotalJuro == null) {
			valorTotalJuro = 0.0;
		}
		return valorTotalJuro;
	}

	public void setValorTotalJuro(Double valorTotalJuro) {
		this.valorTotalJuro = valorTotalJuro;
	}

	public Double getValorIsencaoTotalJuro() {
		if (valorIsencaoTotalJuro == null) {
			valorIsencaoTotalJuro = 0.0;
		}
		return valorIsencaoTotalJuro;
	}

	public void setValorIsencaoTotalJuro(Double valorIsencaoTotalJuro) {
		this.valorIsencaoTotalJuro = valorIsencaoTotalJuro;
	}

	public Double getValorIsencaoTotalJuroMaximo() {
		if (valorIsencaoTotalJuroMaximo == null) {
			valorIsencaoTotalJuroMaximo = 0.0;
		}
		return valorIsencaoTotalJuroMaximo;
	}

	public void setValorIsencaoTotalJuroMaximo(Double valorIsencaoTotalJuroMaximo) {
		this.valorIsencaoTotalJuroMaximo = valorIsencaoTotalJuroMaximo;
	}	
	
	public Double getValorTotalMulta() {
		if (valorTotalMulta == null) {
			valorTotalMulta = 0.0;
		}
		return valorTotalMulta;
	}

	public void setValorTotalMulta(Double valorTotalMulta) {
		this.valorTotalMulta = valorTotalMulta;
	}

	public Double getValorIsencaoTotalMulta() {
		if (valorIsencaoTotalMulta == null) {
			valorIsencaoTotalMulta = 0.0;
		}
		return valorIsencaoTotalMulta;
	}

	public void setValorIsencaoTotalMulta(Double valorIsencaoTotalMulta) {
		this.valorIsencaoTotalMulta = valorIsencaoTotalMulta;
	}

	public Double getValorIsencaoTotalMultaMaximo() {
		if (valorIsencaoTotalMultaMaximo == null) {
			valorIsencaoTotalMultaMaximo = 0.0;
		}
		return valorIsencaoTotalMultaMaximo;
	}

	public void setValorIsencaoTotalMultaMaximo(Double valorIsencaoTotalMultaMaximo) {
		this.valorIsencaoTotalMultaMaximo = valorIsencaoTotalMultaMaximo;
	}

	public Double getValorTotalDescontoPerdido() {
		if (valorTotalDescontoPerdido == null) {
			valorTotalDescontoPerdido = 0.0;
		}
		return valorTotalDescontoPerdido;
	}

	public void setValorTotalDescontoPerdido(Double valorTotalDescontoPerdido) {
		this.valorTotalDescontoPerdido = valorTotalDescontoPerdido;
	}

	public Double getValorConcecaoDescontoPerdido() {
		if (valorConcecaoDescontoPerdido == null) {
			valorConcecaoDescontoPerdido = 0.0;
		}
		return valorConcecaoDescontoPerdido;
	}

	public void setValorConcecaoDescontoPerdido(Double valorConcecaoDescontoPerdido) {
		this.valorConcecaoDescontoPerdido = valorConcecaoDescontoPerdido;
	}

	public Double getValorConcecaoDescontoPerdidoMaximo() {
		if (valorConcecaoDescontoPerdidoMaximo == null) {
			valorConcecaoDescontoPerdidoMaximo = 0.0;
		}
		return valorConcecaoDescontoPerdidoMaximo;
	}

	public void setValorConcecaoDescontoPerdidoMaximo(Double valorConcecaoDescontoPerdidoMaximo) {
		this.valorConcecaoDescontoPerdidoMaximo = valorConcecaoDescontoPerdidoMaximo;
	}

	public boolean isLiberarIsencaoJuroMultaDescontoAcimaMaximo() {
		return liberarIsencaoJuroMultaDescontoAcimaMaximo;
	}

	public void setLiberarIsencaoJuroMultaDescontoAcimaMaximo(boolean liberarIsencaoJuroMultaDescontoAcimaMaximo) {
		this.liberarIsencaoJuroMultaDescontoAcimaMaximo = liberarIsencaoJuroMultaDescontoAcimaMaximo;
	}

	public UsuarioVO getUsuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo() {
		if (usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo == null) {
			usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo = new UsuarioVO();
		}
		return usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo;
	}

	public void setUsuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo(UsuarioVO usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo) {
		this.usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo = usuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo;
	}

	public Double getValorDescontoCondicaoRenegociacao() {
		if (valorDescontoCondicaoRenegociacao == null) {
			valorDescontoCondicaoRenegociacao = 0.0;
		}
		return valorDescontoCondicaoRenegociacao;
	}

	public void setValorDescontoCondicaoRenegociacao(Double valorDescontoCondicaoRenegociacao) {
		this.valorDescontoCondicaoRenegociacao = valorDescontoCondicaoRenegociacao;
	}

	public Double getValorAcrescimoCondicaoRenegociacao() {
		if (valorAcrescimoCondicaoRenegociacao == null) {
			valorAcrescimoCondicaoRenegociacao = 0.0;
		}
		return valorAcrescimoCondicaoRenegociacao;
	}

	public void setValorAcrescimoCondicaoRenegociacao(Double valorAcrescimoCondicaoRenegociacao) {
		this.valorAcrescimoCondicaoRenegociacao = valorAcrescimoCondicaoRenegociacao;
	}

	public Boolean getPermitirPagamentoCartaoCreditoVisaoAluno() {
		if(permitirPagamentoCartaoCreditoVisaoAluno == null) {
			permitirPagamentoCartaoCreditoVisaoAluno = true;
		}
		return permitirPagamentoCartaoCreditoVisaoAluno;
	}

	public void setPermitirPagamentoCartaoCreditoVisaoAluno(Boolean permitirPagamentoCartaoCreditoVisaoAluno) {
		this.permitirPagamentoCartaoCreditoVisaoAluno = permitirPagamentoCartaoCreditoVisaoAluno;
	}

	public List<ItemCondicaoRenegociacaoVO> getItemCondicaoRenegociacaoVOs() {
		if (itemCondicaoRenegociacaoVOs == null) {
			itemCondicaoRenegociacaoVOs = new ArrayList<>();
		}
		return itemCondicaoRenegociacaoVOs;
	}

	public void setItemCondicaoRenegociacaoVOs(List<ItemCondicaoRenegociacaoVO> itemCondicaoRenegociacaoVOs) {
		this.itemCondicaoRenegociacaoVOs = itemCondicaoRenegociacaoVOs;
	}
	
	public Double getValorJuroDesconto() {
		if (valorJuroDesconto == null) {
			valorJuroDesconto = 0.0;
		}
		return valorJuroDesconto;
	}

	public void setValorJuroDesconto(Double valorJuroDesconto) {
		this.valorJuroDesconto = valorJuroDesconto;
	}

	public Double getValorMultaDesconto() {
		if (valorMultaDesconto == null) {
			valorMultaDesconto = 0.0;
		}
		return valorMultaDesconto;
	}

	public void setValorMultaDesconto(Double valorMultaDesconto) {
		this.valorMultaDesconto = valorMultaDesconto;
	}

	public Double getValorIndiceReajusteDesconto() {
		if (valorIndiceReajusteDesconto == null) {
			valorIndiceReajusteDesconto = 0.0;
		}
		return valorIndiceReajusteDesconto;
	}

	public void setValorIndiceReajusteDesconto(Double valorIndiceReajusteDesconto) {
		this.valorIndiceReajusteDesconto = valorIndiceReajusteDesconto;
	}
	
			
	public Double getValorDescontoIsencaoJuroMultaIndiceReajuste() {	
		return (getValorJuroDesconto() + getValorMultaDesconto() + getValorIndiceReajusteDesconto());	
	}
	
	public Double getValorMinimoEntrada() {
		if(getItemCondicaoRenegociacao().getFaixaEntradaInicial() > 0.0) {
			return Uteis.arrendondarForcando2CadasDecimais(((getValorTotal()) * getItemCondicaoRenegociacao().getFaixaEntradaInicial()) / 100);
		}
		return 0.0;
	}
	
	public Double getValorMaximoEntrada() {
		if(getItemCondicaoRenegociacao().getFaixaEntradaFinal() > 0.0) {
			return Uteis.arrendondarForcando2CadasDecimais(((getValorTotal()) * getItemCondicaoRenegociacao().getFaixaEntradaFinal()) / 100);
		}
		return 0.0; 
	}
	
	public Date dataVencimentoEntrada;
	public Date getDataVencimentoEntrada() {
		if(dataVencimentoEntrada == null) {			
			if(getItemCondicaoRenegociacao().getQtdeDiasEntrada() > 0) {
				dataVencimentoEntrada = UteisData.adicionarDiasEmData(new Date(), getItemCondicaoRenegociacao().getQtdeDiasEntrada());
			}else {
				dataVencimentoEntrada =  new Date();
			}
		}
		return dataVencimentoEntrada;
	}
	
	public void setDataVencimentoEntrada(Date dataVencimentoEntrada) {
		this.dataVencimentoEntrada = dataVencimentoEntrada;
	}

	public String getDataVencimentoEntradaApresentar() {
		return Uteis.getData(getDataVencimentoEntrada());
	}

	public AgenteNegativacaoCobrancaContaReceberVO getAgenteNegativacaoCobrancaContaReceberVO() {
		if(agenteNegativacaoCobrancaContaReceberVO == null) {
			agenteNegativacaoCobrancaContaReceberVO =  new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agenteNegativacaoCobrancaContaReceberVO;
	}

	public void setAgenteNegativacaoCobrancaContaReceberVO(
			AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO) {
		this.agenteNegativacaoCobrancaContaReceberVO = agenteNegativacaoCobrancaContaReceberVO;
	}
	
	
	
}
