package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.NegociacaoPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>NegociacaoPagamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>NegociacaoPagamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see NegociacaoPagamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class NegociacaoPagamento extends ControleAcesso implements NegociacaoPagamentoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8092051054247566428L;
	protected static String idEntidade;

    public NegociacaoPagamento() throws Exception {
        super();
        setIdEntidade("Pagamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>NegociacaoPagamentoVO</code>.
     */
    public NegociacaoPagamentoVO novo() throws Exception {
        NegociacaoPagamento.incluir(getIdEntidade());
        NegociacaoPagamentoVO obj = new NegociacaoPagamentoVO();
        return obj;
    }
    
    private void distribuirPagamentoContaPagar(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
        zerarValoresPagoContaPagar(obj);
        List<LancamentoContabilVO> listaLancamentoContabilVOs = new ArrayList<>();
        Iterator<FormaPagamentoNegociacaoPagamentoVO> i = obj.getFormaPagamentoNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            FormaPagamentoNegociacaoPagamentoVO pgmnt = i.next();
            distribuirContaPagarNegociacao(obj, pgmnt, listaLancamentoContabilVOs);
        }
        persistirLancamentoContabilPorNegociacaoPagamento(obj, usuario, listaLancamentoContabilVOs);
    }

	private void persistirLancamentoContabilPorNegociacaoPagamento(NegociacaoPagamentoVO obj, UsuarioVO usuario, List<LancamentoContabilVO> listaLancamentoContabilVOs) throws Exception {		
		if(Uteis.isAtributoPreenchido(listaLancamentoContabilVOs)){
			for (LancamentoContabilVO objExistente : listaLancamentoContabilVOs) {
				getFacadeFactory().getLancamentoContabilFacade().persistir(objExistente, false, usuario);
				obj.getContaPagarNegociacaoPagamentoVOs()
				.stream()
				.filter(p-> p.getContaPagar().getCodigo().toString().equals(objExistente.getCodOrigem()))
				.findFirst()
				.ifPresent(e->{
					e.getContaPagar().setLancamentoContabil(true);					
					if (objExistente.getTipoPlanoConta().isCredito()) {
						e.getContaPagar().getListaLancamentoContabeisCredito().add(objExistente);
					} else {
						e.getContaPagar().getListaLancamentoContabeisDebito().add(objExistente);
					}
				});
			}
		}
	}
    
    private void zerarValoresPagoContaPagar(NegociacaoPagamentoVO np) {
        for (ContaPagarNegociacaoPagamentoVO obj : np.getContaPagarNegociacaoPagamentoVOs()) {
            obj.getContaPagar().setSituacao("AP");
            obj.getContaPagar().setValorPagamento(0.0);
            obj.getContaPagar().setValorPago(0.0);
            obj.getContaPagar().setContaPagarPagamentoVOs(new ArrayList<ContaPagarPagamentoVO>(0));
            obj.getContaPagar().getListaLancamentoContabeisCredito().clear();	
            obj.getContaPagar().getListaLancamentoContabeisDebito().clear();
        }
    }

    private void distribuirContaPagarNegociacao(NegociacaoPagamentoVO obj, FormaPagamentoNegociacaoPagamentoVO fpnpVO, List<LancamentoContabilVO> listaLancamentoContabilVOs) throws Exception {
    	obj.getFormaPagamentoNegociacaoPagamentoVOs().size();
    	Double valorFpnpVO = fpnpVO.getValor();
		Iterator<ContaPagarNegociacaoPagamentoVO> j = obj.getContaPagarNegociacaoPagamentoVOs().iterator();
		while (valorFpnpVO > 0 && j.hasNext()) {
			ContaPagarNegociacaoPagamentoVO contaPagarNegociacao = j.next();
			Double valorAPagar = Uteis.arrendondarForcando2CadasDecimais(contaPagarNegociacao.getValorContaPagar() - contaPagarNegociacao.getContaPagar().getValorPago());
//			if (valorAPagar > 0) {
				if (valorFpnpVO >= valorAPagar && !contaPagarNegociacao.getContaPagar().getSituacao().equals("PA")) {
					contaPagarNegociacao.getContaPagar().setValorPagamento(valorAPagar);
					contaPagarNegociacao.getContaPagar().setValorPago(Uteis.arrendondarForcando2CadasDecimais(contaPagarNegociacao.getContaPagar().getValorPago() + valorAPagar));
					contaPagarNegociacao.getContaPagar().setSituacao("PA");
					contaPagarNegociacao.getContaPagar().getContaPagarPagamentoVOs().add(montarDadosContaPagarPagamento(fpnpVO, valorAPagar, obj));
					getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaPagar(listaLancamentoContabilVOs, obj.getData(), contaPagarNegociacao.getContaPagar(), fpnpVO,  valorAPagar, false, obj.getResponsavel());
					valorFpnpVO = Uteis.arrendondarForcando2CadasDecimais(valorFpnpVO - valorAPagar);
				} else if (valorFpnpVO < valorAPagar) {
					contaPagarNegociacao.getContaPagar().setValorPagamento(contaPagarNegociacao.getContaPagar().getValorPagamento() + valorFpnpVO);
					contaPagarNegociacao.getContaPagar().setValorPago(Uteis.arrendondarForcando2CadasDecimais(contaPagarNegociacao.getContaPagar().getValorPago() + valorFpnpVO));
					contaPagarNegociacao.getContaPagar().setSituacao("PP");
					contaPagarNegociacao.getContaPagar().getContaPagarPagamentoVOs().add(montarDadosContaPagarPagamento(fpnpVO, valorFpnpVO, obj));
					getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaPagar(listaLancamentoContabilVOs, obj.getData(), contaPagarNegociacao.getContaPagar(), fpnpVO, valorFpnpVO, false, obj.getResponsavel());
					valorFpnpVO = 0.0;
					break;
				}
//			}
		}
	}

    private ContaPagarPagamentoVO montarDadosContaPagarPagamento(FormaPagamentoNegociacaoPagamentoVO pagamento, Double valorPagamento, NegociacaoPagamentoVO obj) {
        ContaPagarPagamentoVO contaPagarPagamento = new ContaPagarPagamentoVO();
        contaPagarPagamento.setPagamento(pagamento.getCodigo());
        contaPagarPagamento.getResponsavel().setCodigo(obj.getResponsavel().getCodigo());
        contaPagarPagamento.setValorTotalPagamento(valorPagamento);
        contaPagarPagamento.setNegociacaoPagamento(obj.getCodigo());
        contaPagarPagamento.setFormaPagamento(pagamento.getFormaPagamento());
        contaPagarPagamento.setTipoPagamento("CR");
        contaPagarPagamento.setData(obj.getData() );        
        contaPagarPagamento.setNumero(pagamento.getCheque().getNumero());
        contaPagarPagamento.setBanco(pagamento.getCheque().getBanco());
        contaPagarPagamento.setAgencia(pagamento.getCheque().getAgencia());
        contaPagarPagamento.getContaCorrente().setNumero(pagamento.getCheque().getContaCorrente().getNumero());
        contaPagarPagamento.setDataPrevisao(pagamento.getCheque().getDataPrevisao());
        contaPagarPagamento.setDataEmissao(pagamento.getCheque().getDataEmissao());
        contaPagarPagamento.setSacado(pagamento.getCheque().getSacado());
        contaPagarPagamento.setValor(pagamento.getCheque().getValor());
        return contaPagarPagamento;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>NegociacaoPagamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>NegociacaoPagamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
    	this.incluir(obj, true, usuario);
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NegociacaoPagamentoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {
            NegociacaoPagamentoVO.validarDados(obj);
            NegociacaoPagamento.incluir(getIdEntidade(), verificarAcesso, usuario);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.PAGAMENTO, usuario);
            
			for (FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO : obj.getFormaPagamentoNegociacaoPagamentoVOs()) {
				if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(obj.getData(), formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getNumero(), usuario)) {
					throw new Exception("Não é possível realizar o pagamento pela conta corrente " + formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(obj.getData()) + ", pois a conciliação bancária já está finalizada.");
				}							
			}
			
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO NegociacaoPagamento( data, responsavel, valorTotal, valorTroco, caixa, fornecedor, valorTotalPagamento, unidadeEnsino, funcionario, banco, departamento, tipoSacado, aluno, dataRegistro, parceiro, responsavelFinanceiro,operadoraCartao) VALUES ( ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setDouble(3, obj.getValorTotal().doubleValue());
                    sqlInserir.setDouble(4, obj.getValorTroco().doubleValue());
                    if (obj.getCaixa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getCaixa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    sqlInserir.setDouble(7, obj.getValorTotalPagamento().doubleValue());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(10, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(10, 0);
                    }
                    if (obj.getDepartamento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getDepartamento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    sqlInserir.setString(12, obj.getTipoSacado());
                    if (obj.getAluno().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(13, obj.getAluno().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(13, 0);
                    }
                    sqlInserir.setDate(14, Uteis.getDataJDBC(obj.getDataRegistro()));
                    if (obj.getParceiro().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(15, obj.getParceiro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(15, 0);
                    }
                    if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(16, obj.getResponsavelFinanceiro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(16, 0);
                    }
                    if (obj.getOperadoraCartao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(17, obj.getOperadoraCartao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(17, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));

            if (!obj.getNegociacaoPagamentoComValorAPagarZerado()) {
            	getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().incluirPagamentos(obj, usuario);
            	distribuirPagamentoContaPagar(obj, usuario);
            	getFacadeFactory().getContaPagarNegociacaoPagamentoFacade().incluirContaPagarNegociacaoPagamentos(obj.getCodigo(), obj.getTipoSacado(), obj.getContaPagarNegociacaoPagamentoVOs(), verificarAcesso, usuario);
            	atualizarEGravarAdiantamentosUtilizadosComoDescontoNegociacaoPagamento(obj, usuario);
            } else {
            	getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().incluirPagamentos(obj, usuario);
            	atualizarEContasPagarComValorZerado(obj, usuario);
            	atualizarEGravarAdiantamentosUtilizadosComoDescontoNegociacaoPagamento(obj, usuario);
            }
            if (obj.getValorTroco() > 0) {
            	validarGeracaoTroco(obj);
                Integer codigoFornecedor = 0;
                Integer codigoPessoa = 0;
                Integer codigoBanco = 0; 
                Integer codigoParceiro = 0; 
                Integer codigoOperadoraCartao = 0; 
                Integer codigoSacado = 0;
                String nomeSacado = "";
                TipoSacadoExtratoContaCorrenteEnum tipoSacado = null;
                
                if (obj.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
                    codigoFornecedor = obj.getFornecedor().getCodigo();
                    codigoSacado = obj.getFornecedor().getCodigo();
                    nomeSacado = obj.getFornecedor().getNome();
                    tipoSacado = TipoSacadoExtratoContaCorrenteEnum.FORNECEDOR;
                } else if (obj.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
                    codigoPessoa = obj.getFuncionario().getPessoa().getCodigo();
                    codigoSacado = obj.getFuncionario().getPessoa().getCodigo();
                    nomeSacado = obj.getFuncionario().getPessoa().getNome();
                    tipoSacado = TipoSacadoExtratoContaCorrenteEnum.FUNCIONARIO_PROFESSOR;
                } else if (obj.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
					codigoPessoa = obj.getAluno().getCodigo();
					codigoSacado = obj.getAluno().getCodigo();
					nomeSacado = obj.getAluno().getNome();
					tipoSacado = TipoSacadoExtratoContaCorrenteEnum.ALUNO;
                } else if (obj.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
                    codigoPessoa = obj.getResponsavelFinanceiro().getCodigo();
                    codigoSacado = obj.getResponsavelFinanceiro().getCodigo();
                    nomeSacado = obj.getResponsavelFinanceiro().getNome();
                    tipoSacado = TipoSacadoExtratoContaCorrenteEnum.RESPONSAVEL_FINANCEIRO;
                } else if (obj.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
                    codigoBanco = obj.getBanco().getCodigo();
                    codigoSacado = obj.getBanco().getCodigo();
                    obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(codigoBanco, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
                    nomeSacado = obj.getBanco().getNome();
                    tipoSacado = TipoSacadoExtratoContaCorrenteEnum.BANCO;
                } else if (obj.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
                    codigoParceiro = obj.getParceiro().getCodigo();
                    codigoSacado = obj.getParceiro().getCodigo();
                    nomeSacado = obj.getParceiro().getNome();
                    tipoSacado = TipoSacadoExtratoContaCorrenteEnum.PARCEIRO;
				} else if (obj.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
					codigoOperadoraCartao = obj.getOperadoraCartao().getCodigo();
					codigoSacado = obj.getOperadoraCartao().getCodigo();
					nomeSacado = obj.getOperadoraCartao().getNome();
					tipoSacado = TipoSacadoExtratoContaCorrenteEnum.OPERADORA_CARTAO;
				}
            
				if (obj.getContaCorrenteTrocoVO().getContaCaixa()) {
					getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTroco(),
							obj.getContaCorrenteTrocoVO().getCodigo(), TipoMovimentacaoFinanceira.ENTRADA.getValor(),
							"TR", obj.getFormaPagamentoTrocoVO().getCodigo(), obj.getCodigo(),
							obj.getResponsavel().getCodigo(), codigoPessoa, codigoFornecedor, codigoBanco,
							codigoParceiro, null, codigoOperadoraCartao, usuario);
				} else {
					getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(
							obj.getValorTroco(), obj.getData(), OrigemExtratoContaCorrenteEnum.PAGAMENTO, TipoMovimentacaoFinanceira.ENTRADA,
							obj.getCodigo(), null, nomeSacado, codigoSacado, tipoSacado, null, obj.getFormaPagamentoTrocoVO(),
							obj.getContaCorrenteTrocoVO(), obj.getUnidadeEnsino(), obj.getOperadoraCartao(),
							obj.isDesconsiderarConciliacaoBancaria(), null, obj.getBloqueioPorFechamentoMesLiberado(), obj.getResponsavel());
				}
            }
            obj.setValorTrocoEdicao(obj.getValorTroco());
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            for(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO: obj.getFormaPagamentoNegociacaoPagamentoVOs()) {
            	formaPagamentoNegociacaoPagamentoVO.setCodigo(0);
            	formaPagamentoNegociacaoPagamentoVO.setNovoObj(true);
            }
            for(ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO: obj.getContaPagarNegociacaoPagamentoVOs()) {
            	contaPagarNegociacaoPagamentoVO.setCodigo(0);
            	contaPagarNegociacaoPagamentoVO.setNovoObj(true);
            }
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {    	
    	this.alterar(obj, true, usuario);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NegociacaoPagamentoVO obj, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {

            NegociacaoPagamento.alterar(getIdEntidade(), verificarAcesso, usuario);
            NegociacaoPagamentoVO.validarDados(obj);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.PAGAMENTO, usuario);
			
			for (FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO : obj.getFormaPagamentoNegociacaoPagamentoVOs()) {
				if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(obj.getData(), formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getNumero(), usuario)) {
					throw new Exception("Não é possível realizar o pagamento pela conta corrente " + formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(obj.getData()) + ", pois a conciliação bancária já está finalizada.");
				}							
			}
			
			obj.realizarUpperCaseDados();
            final String sql = "UPDATE NegociacaoPagamento set data=?, responsavel=?, valorTotal=?, valorTroco=?, caixa=?, fornecedor=?, valorTotalPagamento=?, unidadeEnsino=?, funcionario=?, banco=?, departamento=?, tipoSacado=?, aluno=?, dataRegistro=?, responsavelFinanceiro = ?, parceiro = ?,operadoracartao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setDouble(3, obj.getValorTotal().doubleValue());
                    sqlAlterar.setDouble(4, obj.getValorTroco().doubleValue());
                    if (obj.getCaixa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getCaixa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    sqlAlterar.setDouble(7, obj.getValorTotalPagamento().doubleValue());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    if (obj.getBanco().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(10, obj.getBanco().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(10, 0);
                    }
                    if (obj.getDepartamento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(11, obj.getDepartamento().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(11, 0);
                    }
                    sqlAlterar.setString(12, obj.getTipoSacado());
                    if (obj.getAluno().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(13, obj.getAluno().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(13, 0);
                    }
                    sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataRegistro()));
                    if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(15, obj.getResponsavelFinanceiro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(15, 0);
                    }
                    if (obj.getParceiro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(16, obj.getParceiro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(16, 0);
                    }
                    if (obj.getOperadoraCartao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(17, obj.getOperadoraCartao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(17, 0);
                    }
                    sqlAlterar.setInt(18, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            Integer codigoFornecedor = 0;
            Integer codigoPessoa = 0;
            Integer codigoBanco = 0;
            Integer codigoParceiro = 0;
            Integer codigoOperadoraCartao = 0;

            if (obj.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
                codigoFornecedor = obj.getFornecedor().getCodigo();
            } else if (obj.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
                codigoPessoa = obj.getFuncionario().getPessoa().getCodigo();
            }else if (obj.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
                    codigoPessoa = obj.getAluno().getCodigo();
            }else if (obj.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
                codigoPessoa = obj.getResponsavelFinanceiro().getCodigo();
            } else if (obj.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
                codigoBanco = obj.getBanco().getCodigo();
            }else if (obj.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
                codigoParceiro = obj.getParceiro().getCodigo();
            }else if (obj.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
            	codigoOperadoraCartao = obj.getOperadoraCartao().getCodigo();
            }

            if (obj.getValorTrocoEdicao().doubleValue() > 0) {
                getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTrocoEdicao(), obj.getCaixa().getCodigo(), "SA", "TR", getFacadeFactory().getFormaPagamentoFacade().consultarPorTipoDinheiro(Uteis.NIVELMONTARDADOS_DADOSBASICOS).getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), codigoPessoa, codigoFornecedor, codigoBanco, codigoParceiro, null, codigoOperadoraCartao, usuario);
            }
            getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().alterarPagamentos(obj, usuario);
            distribuirPagamentoContaPagar(obj, usuario);
            getFacadeFactory().getContaPagarNegociacaoPagamentoFacade().alterarContaPagarNegociacaoPagamentos(obj.getCodigo(), obj.getContaPagarNegociacaoPagamentoVOs(), verificarAcesso, usuario);
            if (obj.getValorTroco() > 0) {
                getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTroco(), obj.getContaCorrenteTrocoVO().getCodigo(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), "TR", obj.getFormaPagamentoTrocoVO().getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), codigoPessoa, codigoFornecedor, codigoBanco, codigoParceiro, null, codigoOperadoraCartao, usuario);
            }
            obj.setValorTrocoEdicao(obj.getValorTroco());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>NegociacaoPagamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>NegociacaoPagamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
        try {
            if (obj.getCodigo().intValue() != 0) {
                NegociacaoPagamento.excluir(getIdEntidade(), true, usuario);
				verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "EXCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario);
				
                validarDadosDataEstorno(obj.getData(), obj.getDataEstorno());
                NegociacaoPagamentoVO.validarDados(obj);
                getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.PAGAMENTO, obj.getCodigo(), obj.isDesconsiderarConciliacaoBancaria(), 0, false, usuario);
                getFacadeFactory().getContaPagarNegociacaoPagamentoFacade().excluirContaPagarNegociacaoPagamentos(obj, usuario);
                atualizarEEstornarAdiantamentosUtilizadosComoDescontoNegociacaoPagamento(obj, usuario);
                getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().excluirPagamentos(obj, usuario);
                String sql = "DELETE FROM NegociacaoPagamento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
                getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            }
        } catch (Exception e) {
            throw e;
        }

    }
    
    public void validarDadosDataEstorno(Date data, Date dataEstorno) throws Exception {
    	if (Uteis.getDataMinutos(dataEstorno).before(Uteis.getDataMinutos(data))) {
    		throw new Exception("A data do estorno não pode ser anterior a data do pagamento.");
    	}
    }


    /**
     * Responsável por realizar uma consulta de <code>NegociacaoPagamento</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Fornecedor</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>NegociacaoPagamentoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeFornecedor(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT NegociacaoPagamento.* FROM NegociacaoPagamento, Fornecedor WHERE NegociacaoPagamento.fornecedor = Fornecedor.codigo and upper( Fornecedor.nome ) like('" + valorConsulta.toUpperCase() + "%')";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT NegociacaoPagamento.* FROM NegociacaoPagamento, Fornecedor WHERE NegociacaoPagamento.fornecedor = Fornecedor.codigo and upper( Fornecedor.nome ) like('" + valorConsulta.toUpperCase() + "%') and NegociacaoPagamento.unidadeEnsino =" + unidadeEnsino.intValue();
        }
        sqlStr += " AND NegociacaoPagamento.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND NegociacaoPagamento.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += "ORDER BY Fornecedor.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>NegociacaoPagamento</code> através do valor do atributo 
     * <code>numero</code> da classe <code>ContaCorrente</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>NegociacaoPagamentoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<NegociacaoPagamentoVO> consultarPorNumeroContaCorrente(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct NegociacaoPagamento.* from NegociacaoPagamento ");
        sqlStr.append("left join formapagamentonegociacaopagamento on formapagamentonegociacaopagamento.negociacaocontapagar = NegociacaoPagamento.codigo ");
        sqlStr.append("left join contacorrente on contacorrente.codigo = formapagamentonegociacaopagamento.contacorrente ");
        sqlStr.append("WHERE formapagamentonegociacaopagamento.contacorrente = ContaCorrente.codigo ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and NegociacaoPagamento.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("AND upper( ContaCorrente.numero ) like('").append(valorConsulta.toUpperCase()).append("%') ");
        sqlStr.append("AND NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataIni)).append("' ");
        sqlStr.append("AND NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        sqlStr.append("ORDER BY NegociacaoPagamento.codigo");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public Integer consultarPorNumeroContaCorrenteTotalRegistros(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuilder sqlStr = new StringBuilder("select count(distinct NegociacaoPagamento.codigo) from NegociacaoPagamento ");
        sqlStr.append("left join formapagamentonegociacaopagamento on formapagamentonegociacaopagamento.negociacaocontapagar = NegociacaoPagamento.codigo ");
        sqlStr.append("left join contacorrente on contacorrente.codigo = formapagamentonegociacaopagamento.contacorrente ");
        sqlStr.append("WHERE formapagamentonegociacaopagamento.contacorrente = ContaCorrente.codigo ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and NegociacaoPagamento.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("AND NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataIni)).append("' ");
        sqlStr.append("AND NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        ////System.out.println(sqlStr.toString());
        SqlRowSet numeroRegistros = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (numeroRegistros.next()) {
            return numeroRegistros.getInt("count");
        } else {
            return 0;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>NegociacaoPagamento</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>NegociacaoPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    /*public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados) throws Exception {
    ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    String sqlStr = "SELECT * FROM NegociacaoPagamento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
    Statement stm = con.createStatement();
    ResultSet tabelaResultado = stm.executeQuery(sqlStr);
    return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>NegociacaoPagamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>NegociacaoPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<NegociacaoPagamentoVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT * FROM NegociacaoPagamento WHERE codigo = " + valorConsulta.intValue();
        if (limite != null) {
            sqlStr += " LIMIT " + limite;
            if (offset != null) {
                sqlStr += "OFFSET " + offset;
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));


    }

    public Integer consultarTotalPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT count(codigo) FROM NegociacaoPagamento WHERE codigo = " + valorConsulta.intValue();

        SqlRowSet numero = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (numero.next()) {
            return numero.getInt("count");
        }
        return 0;



    }

    public List consultarPorNomeBanco(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT * FROM NegociacaoPagamento inner join banco on NegociacaoPagamento.banco = Banco.codigo WHERE upper(banco.nome) like ('%" + valorConsulta.toUpperCase() + "%')";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM NegociacaoPagamento inner join banco on NegociacaoPagamento.banco = Banco.codigo WHERE upper(banco.nome) like ('%" + valorConsulta.toUpperCase() + "%')  and NegociacaoPagamento.unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND NegociacaoPagamento.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND NegociacaoPagamento.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY NegociacaoPagamento.banco";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeFuncionario(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT * FROM NegociacaoPagamento inner join Funcionario on NegociacaoPagamento.funcionario = Funcionario.codigo inner join Pessoa on Funcionario.codigo = Pessoa.codigo WHERE upper(pessoa.nome) like ('" + valorConsulta.toUpperCase() + "%')";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM NegociacaoPagamento inner join Funcionario on NegociacaoPagamento.funcionario = Funcionario.codigo inner join Pessoa on Funcionario.codigo = Pessoa.codigo WHERE upper(pessoa.nome) like ('" + valorConsulta.toUpperCase() + "%')  and NegociacaoPagamento.unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND NegociacaoPagamento.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND NegociacaoPagamento.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY NegociacaoPagamento.funcionario";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("select distinct negociacaopagamento.codigo, fornecedor.nome AS \"fornecedor.nome\", func.nome AS \"funcionario.nome\",");
        str.append(" banco.nome AS \"banco.nome\", pessoa.nome AS \"aluno.nome\", negociacaopagamento.data, negociacaopagamento.valortotal, unidadeensino.nome as unidadeensino, ");
        str.append(" responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", parceiro.nome as \"parceiro.nome\", operadoracartao.nome as \"operadoracartao.nome\", negociacaopagamento.tipoSacado   from negociacaopagamento ");
        //str.append(" inner join contapagarNegociacaoPagamento on contapagarNegociacaoPagamento.negociacaocontapagar = negociacaopagamento.codigo ");
        //str.append(" inner join contapagar on contapagar.codigo = contapagarNegociacaoPagamento.contapagar ");
        str.append(" left join funcionario on funcionario.codigo = negociacaopagamento.funcionario ");
        str.append(" left join fornecedor on fornecedor.codigo = negociacaopagamento.fornecedor ");
        str.append(" left join pessoa func on func.codigo = funcionario.pessoa ");
        str.append(" left join banco on banco.codigo = negociacaopagamento.banco ");
        str.append(" left join pessoa on pessoa.codigo = negociacaopagamento.aluno  ");
        str.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = negociacaopagamento.responsavelFinanceiro  ");
        str.append(" left join parceiro on parceiro.codigo = negociacaopagamento.parceiro  ");
        str.append(" left join operadoracartao on operadoracartao.codigo = negociacaopagamento.operadoracartao  ");
        str.append(" left join unidadeensino on unidadeensino.codigo = negociacaopagamento.unidadeensino  ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaBasicaTotalRegistros() {
        StringBuffer str = new StringBuffer();
        str.append("select count(distinct negociacaopagamento.codigo) from negociacaopagamento ");
        //str.append(" inner join contapagarNegociacaoPagamento on contapagarNegociacaoPagamento.negociacaocontapagar = negociacaopagamento.codigo ");
        //str.append(" inner join contapagar on contapagar.codigo = contapagarNegociacaoPagamento.contapagar ");
        str.append(" left join funcionario on funcionario.codigo = negociacaopagamento.funcionario ");
        str.append(" left join fornecedor on fornecedor.codigo = negociacaopagamento.fornecedor ");
        str.append(" left join pessoa func on func.codigo = funcionario.pessoa ");
        str.append(" left join banco on banco.codigo = negociacaopagamento.banco ");
        str.append(" left join pessoa on pessoa.codigo = negociacaopagamento.aluno  ");
        str.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = negociacaopagamento.responsavelFinanceiro  ");
        str.append(" left join parceiro on parceiro.codigo = negociacaopagamento.parceiro  ");
        str.append(" left join operadoracartao on operadoracartao.codigo = negociacaopagamento.operadoracartao  ");
        return str;
    }

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where pessoa.aluno = true ");
        sqlStr.append(" and sem_acentos(pessoa.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY pessoa.nome");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        ////System.out.println(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }

    public Integer consultaRapidaTotalRegistrosPorNomeAluno(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where pessoa.aluno = true ");
        sqlStr.append(" and sem_acentos(pessoa.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        SqlRowSet numeroRegistros = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
       // //System.out.println(sqlStr.toString());
        if (numeroRegistros.next()) {
            return numeroRegistros.getInt("count");
        }
        return 0;

    }

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeFornecedor(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where sem_acentos(fornecedor.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY fornecedor.nome");

        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");


        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }

    public Integer consultaRapidaPorNomeFornecedorTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where sem_acentos(fornecedor.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        SqlRowSet numeroRegistros = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        ////System.out.println(sqlStr.toString());
        if (numeroRegistros.next()) {
            return numeroRegistros.getInt("count");
        }
        return 0;


    }

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeBanco(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where sem_acentos(banco.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY banco.nome");

        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        ////System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }

    public Integer consultaRapidaPorNomeBancoTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where sem_acentos(banco.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        //System.out.println(sqlStr.toString());
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");

        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;

    }
    
    public List<NegociacaoPagamentoVO> consultaRapidaPorCodigoContaPagar(Integer valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where negociacaopagamento.codigo in (select negociacaocontapagar from contapagarnegociacaopagamento where contapagar = ");
        sqlStr.append(valorConsulta);
        sqlStr.append(") ");
//        sqlStr.append(" AND NegociacaoPagamento.data BETWEEN '");
//        sqlStr.append(Uteis.getDataJDBC(dataIni));
//        sqlStr.append("' AND '");
//        sqlStr.append(Uteis.getDataJDBC(dataFim));
//        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY negociacaopagamento.codigo ");

        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
       ////System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }
    
    public Integer consultaRapidaPorCodigoContaPagarTotalRegistros(Integer valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where negociacaopagamento.codigo in (select negociacaocontapagar from contapagarnegociacaopagamento where contapagar = ");
        sqlStr.append(valorConsulta);
        sqlStr.append(") "); 
//        sqlStr.append(" AND NegociacaoPagamento.data BETWEEN '");
//        sqlStr.append(Uteis.getDataJDBC(dataIni));
//        sqlStr.append("' AND '");
//        sqlStr.append(Uteis.getDataJDBC(dataFim));
//        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
       // //System.out.println(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    
    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeFuncionario(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where sem_acentos(func.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY func.nome");

        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        ////System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }

      public Integer consultaRapidaPorNomeFuncionarioTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where sem_acentos(func.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
       ////System.out.println(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
      
      @Override
      public List<NegociacaoPagamentoVO> consultaRapidaPorNomeParceiro(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
          ControleAcesso.consultar(getIdEntidade(), true, usuario);
          StringBuffer sqlStr = getSQLPadraoConsultaBasica();
          sqlStr.append(" where sem_acentos(parceiro.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
          sqlStr.append(Uteis.getDataJDBC(dataIni));
          sqlStr.append("' AND '");
          sqlStr.append(Uteis.getDataJDBC(dataFim));
          sqlStr.append("'");
          if (unidadeEnsino != null && unidadeEnsino != 0) {
              sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
              sqlStr.append(unidadeEnsino);
          }
          sqlStr.append(" ORDER BY parceiro.nome");

          if (limite != null) {
              sqlStr.append(" LIMIT ").append(limite);
              if (offset != null) {
                  sqlStr.append(" OFFSET ").append(offset);
              }
          }
          ////System.out.println(sqlStr.toString());
          SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
          return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
      }

      @Override
        public Integer consultaRapidaPorNomeParceiroTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
          ControleAcesso.consultar(getIdEntidade(), true, usuario);
          StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
          sqlStr.append(" where sem_acentos(parceiro.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
          sqlStr.append(Uteis.getDataJDBC(dataIni));
          sqlStr.append("' AND '");
          sqlStr.append(Uteis.getDataJDBC(dataFim));
          sqlStr.append("'");
          if (unidadeEnsino != null && unidadeEnsino != 0) {
              sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
              sqlStr.append(unidadeEnsino);
          }

          SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
          ////System.out.println(sqlStr.toString());
          if (resultado.next()) {
              return resultado.getInt("count");
          }
          return 0;
      }
      
      
      @Override
      public List<NegociacaoPagamentoVO> consultaRapidaPorNomeResponsavelFinanceiro(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
          ControleAcesso.consultar(getIdEntidade(), true, usuario);
          StringBuffer sqlStr = getSQLPadraoConsultaBasica();
          sqlStr.append(" where sem_acentos(responsavelFinanceiro.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
          sqlStr.append(Uteis.getDataJDBC(dataIni));
          sqlStr.append("' AND '");
          sqlStr.append(Uteis.getDataJDBC(dataFim));
          sqlStr.append("'");
          if (unidadeEnsino != null && unidadeEnsino != 0) {
              sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
              sqlStr.append(unidadeEnsino);
          }
          sqlStr.append(" ORDER BY responsavelFinanceiro.nome");

          if (limite != null) {
              sqlStr.append(" LIMIT ").append(limite);
              if (offset != null) {
                  sqlStr.append(" OFFSET ").append(offset);
              }
          }
         // //System.out.println(sqlStr.toString());
          SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
          return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
      }

      @Override
        public Integer consultaRapidaPorNomeResponsavelFinanceiroTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
          ControleAcesso.consultar(getIdEntidade(), true, usuario);
          StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
          sqlStr.append(" where sem_acentos(responsavelFinanceiro.nome) ilike(sem_acentos(?)) AND NegociacaoPagamento.data BETWEEN '");
          sqlStr.append(Uteis.getDataJDBC(dataIni));
          sqlStr.append("' AND '");
          sqlStr.append(Uteis.getDataJDBC(dataFim));
          sqlStr.append("'");
          if (unidadeEnsino != null && unidadeEnsino != 0) {
              sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
              sqlStr.append(unidadeEnsino);
          }

          SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
          ////System.out.println(sqlStr.toString());
          if (resultado.next()) {
              return resultado.getInt("count");
          }
          return 0;
      }

    public List<NegociacaoPagamentoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, String campoConsulta) throws Exception {
        List<NegociacaoPagamentoVO> vetResultado = new ArrayList<NegociacaoPagamentoVO>(0);
        while (tabelaResultado.next()) {
            NegociacaoPagamentoVO obj = new NegociacaoPagamentoVO();
            montarDadosBasico(obj, tabelaResultado, campoConsulta);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosBasico(NegociacaoPagamentoVO obj, SqlRowSet dadosSQL, String campoConsulta) throws Exception {
        // Dados da NegociacaoPagamento
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
        obj.setValorTotal(dadosSQL.getDouble("valorTotal"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
        obj.setNivelMontarDados(NivelMontarDados.BASICO);

        if (obj.getTipoSacado().equals("AL")) {
            // Dados da Pessoa
            obj.getAluno().setNome(dadosSQL.getString("aluno.nome"));
        }
        if (obj.getTipoSacado().equals("FO")) {
            //Dados do fornecedor
            obj.getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));
        }
        if (obj.getTipoSacado().equals("FU")) {
            //Dados do Funcionario
            obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("funcionario.nome"));
        }
        if (obj.getTipoSacado().equals("BA")) {
            //Dados do Funcionario
            obj.getBanco().setNome(dadosSQL.getString("banco.nome"));
        }
        if (obj.getTipoSacado().equals("PA")) {
            //Dados do Parceiro
            obj.getParceiro().setNome(dadosSQL.getString("parceiro.nome"));
        }
        if (obj.getTipoSacado().equals("RF")) {
            //Dados do Responsavel Financeiro
            obj.getResponsavelFinanceiro().setNome(dadosSQL.getString("responsavelFinanceiro.nome"));
        }
        if (obj.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
        	//Dados do Responsavel Financeiro
        	obj.getOperadoraCartao().setNome(dadosSQL.getString("operadoracartao.nome"));
        }



    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>NegociacaoPagamentoVO</code> resultantes da consulta.
     */
    public static List<NegociacaoPagamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<NegociacaoPagamentoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>NegociacaoPagamentoVO</code>.
     * @return  O objeto da classe <code>NegociacaoPagamentoVO</code> com os dados devidamente montados.
     */
    public static NegociacaoPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        NegociacaoPagamentoVO obj = new NegociacaoPagamentoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setDataRegistro(dadosSQL.getDate("dataRegistro"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
        obj.setValorTroco(new Double(dadosSQL.getDouble("valorTroco")));
        obj.setValorTotalPagamento(new Double(dadosSQL.getDouble("valorTotalPagamento")));
        obj.getCaixa().setCodigo(new Integer(dadosSQL.getInt("caixa")));
        obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getAluno().setCodigo(new Integer(dadosSQL.getInt("aluno")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setValorTrocoEdicao(obj.getValorTroco());
        obj.getFuncionario().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
        obj.getBanco().setCodigo(new Integer(dadosSQL.getInt("banco")));
        obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
        obj.getResponsavelFinanceiro().setCodigo(new Integer(dadosSQL.getInt("responsavelFinanceiro")));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
        obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
        obj.getOperadoraCartao().setCodigo(dadosSQL.getInt("operadoraCartao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
        	montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
        	montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosCaixa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosAluno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        obj.setContaPagarNegociacaoPagamentoVOs(ContaPagarNegociacaoPagamento.consultarContaPagarNegociacaoPagamentos(obj.getCodigo(), nivelMontarDados, usuario));
        obj.setFormaPagamentoNegociacaoPagamentoVOs(FormaPagamentoNegociacaoPagamento.consultarFormaPagamentoNegociacaoPagamento(obj.getCodigo(), nivelMontarDados, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosOperadoraCartao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosCaixa(obj, nivelMontarDados, usuario);
        montarDadosFornecedor(obj, nivelMontarDados, usuario);
        montarDadosAluno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosFuncionario(obj, nivelMontarDados, usuario);
        montarDadosBanco(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosResponsavelFinanceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FornecedorVO</code> relacionado ao objeto <code>NegociacaoPagamentoVO</code>.
     * Faz uso da chave primária da classe <code>FornecedorVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosOperadoraCartao(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getOperadoraCartao().getCodigo().intValue() == 0) {
            obj.setOperadoraCartao(new OperadoraCartaoVO());
            return;
        }
       obj.setOperadoraCartao(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartao().getCodigo(), nivelMontarDados,usuario));
    }
    
    
    public static void montarDadosFornecedor(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFornecedor().getCodigo().intValue() == 0) {
            obj.setFornecedor(new FornecedorVO());
            return;
        }
        obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>NegociacaoPagamentoVO</code>.
     * Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosFuncionario(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionario().getCodigo().intValue() == 0) {
            obj.setFuncionario(new FuncionarioVO());
            return;
        }
        obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosAluno(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAluno().getCodigo().intValue() == 0) {
            obj.setAluno(new PessoaVO());
            return;
        }
        getFacadeFactory().getPessoaFacade().carregarDados(obj.getAluno(), NivelMontarDados.BASICO, usuario);
    }
    
    public static void montarDadosResponsavelFinanceiro(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelFinanceiro().getCodigo().intValue() == 0) {
            obj.setResponsavelFinanceiro(new PessoaVO());
            return;
        }
        getFacadeFactory().getPessoaFacade().carregarDados(obj.getResponsavelFinanceiro(), NivelMontarDados.BASICO, usuario);
    }

    public static void montarDadosBanco(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getBanco().getCodigo().intValue() == 0) {
            obj.setBanco(new BancoVO());
            return;
        }
        obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }
    
    public static void montarDadosParceiro(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getParceiro().getCodigo().intValue() == 0) {
            obj.setParceiro(new ParceiroVO());
            return;
        }
        obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosDepartamento(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosUnidadeEnsino(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>NegociacaoPagamentoVO</code>.
     * Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCaixa(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCaixa().getCodigo().intValue() == 0) {
            obj.setCaixa(new ContaCorrenteVO());
            return;
        }
        obj.setCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getCaixa().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>NegociacaoPagamentoVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(NegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>NegociacaoPagamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public NegociacaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM NegociacaoPagamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( NegociacaoPagamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public NegociacaoPagamentoVO consultarPorCodigoContaPagar(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), false, usuario);
    	String sql = "SELECT * FROM NegociacaoPagamento WHERE codigo in ( select negociacaocontapagar from contapagarnegociacaopagamento where contapagar = ? )";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados ( NegociacaoPagamento ).");
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return NegociacaoPagamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        NegociacaoPagamento.idEntidade = idEntidade;
    }

    public Integer consultarCodigoUnidadeEnsinoPelaNegociacaoPagamento(Integer codigoNegociacaoPagamento) throws Exception {
        String sqlStr = "SELECT np.unidadeensino FROM negociacaopagamento np WHERE np.codigo = " + codigoNegociacaoPagamento;
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            tabelaResultado.next();
            return tabelaResultado.getInt("unidadeensino");
        } finally {
            sqlStr = null;
        }
    }
    
    @Override
   	public Date consultarDataPagamentoNegociacaoPagamentoPorContaPagar(Integer contaPagar, UsuarioVO usuario) throws Exception {
   		StringBuilder sql = new StringBuilder();
   		sql.append(" select distinct negociacaopagamento.data  as data from negociacaopagamento  ");
   		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo  ");
   		sql.append(" where contapagarnegociacaopagamento.contapagar = ?  ");
   		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contaPagar);
   		return (Date) Uteis.getSqlRowSetTotalizador(rs, "data", TipoCampoEnum.DATA);
   	}
   	
    @Override
    public boolean validarNegociacaoPagamentoExistente(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
   		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
   		StringBuilder sql = new StringBuilder();
   		sql.append("SELECT codigo FROM negociacaopagamento  WHERE codigo = ? ");
   		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
   		return tabelaResultado.next();
   	}
    
    @Override
    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeSacado(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where (sem_acentos(fornecedor.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(func.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(banco.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(pessoa.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(responsavelFinanceiro.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(parceiro.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(operadoracartao.nome) ilike(sem_acentos(?))) ");        
        sqlStr.append(" AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY fornecedor.nome");

        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%");


        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }

    @Override
    public Integer consultaRapidaPorNomeSacadoTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where (sem_acentos(fornecedor.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(func.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(banco.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(pessoa.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(responsavelFinanceiro.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(parceiro.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" or sem_acentos(operadoracartao.nome) ilike(sem_acentos(?))) ");        
        sqlStr.append(" AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        SqlRowSet numeroRegistros = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%", valorConsulta+"%");
        ////System.out.println(sqlStr.toString());
        if (numeroRegistros.next()) {
            return numeroRegistros.getInt("count");
        }
        return 0;


    }
    
    @Override
    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeOperadoraCartao(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where (sem_acentos(operadoracartao.nome) ilike(sem_acentos(?))) ");        
        sqlStr.append(" AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY fornecedor.nome");

        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");


        return montarDadosConsultaRapida(tabelaResultado, campoConsulta);
    }

    @Override
    public Integer consultaRapidaPorNomeOperadoraCartaoTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasicaTotalRegistros();
        sqlStr.append(" where (sem_acentos(operadoracartao.nome) ilike(sem_acentos(?))) ");        
        sqlStr.append(" AND NegociacaoPagamento.data BETWEEN '");
        sqlStr.append(Uteis.getDataJDBC(dataIni));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("  AND negociacaopagamento.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }

        SqlRowSet numeroRegistros = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        ////System.out.println(sqlStr.toString());
        if (numeroRegistros.next()) {
            return numeroRegistros.getInt("count");
        }
        return 0;


    }
    
    @Override
	public Boolean consultarSeExisteNegociacaoPagamentoPorContaPagar(Integer contaPagar, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(negociacaopagamento.codigo) as qtd from negociacaopagamento  ");
   		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo  ");
   		sql.append(" where contapagarnegociacaopagamento.contapagar = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contaPagar);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);

	}
    
	private void validarGeracaoTroco(NegociacaoPagamentoVO negociacaoPagamentoVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(negociacaoPagamentoVO.getFormaPagamentoTrocoVO())) {
			throw new ConsistirException("O campo FORMA PAGAMENTO TROCO (Pagamento) é obrigatório.");
		} else {
			if (!Uteis.isAtributoPreenchido(negociacaoPagamentoVO.getContaCorrenteTrocoVO())) {
				throw new ConsistirException("O campo CONTA CORRENTE TROCO (Pagamento) é obrigatório.");
			}
		}
	}
    
    /**
     * INICIO MERGE EDIGAR 24/05/2018
     */
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarEContasPagarComValorZerado(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
    	List<LancamentoContabilVO>listaLancamentoContabilVOs = new ArrayList<>();
    	for (ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO : obj.getContaPagarNegociacaoPagamentoVOs()) {    		
    		if (contaPagarNegociacaoPagamentoVO.getContaPagar().getValorPrevisaoPagamento().doubleValue() <= 0.0) {
    			contaPagarNegociacaoPagamentoVO.setNegociacaoContaPagar(obj.getCodigo());
        		getFacadeFactory().getContaPagarNegociacaoPagamentoFacade().incluir(contaPagarNegociacaoPagamentoVO, usuario);
        		contaPagarNegociacaoPagamentoVO.getContaPagar().setSituacao("PA");
    			contaPagarNegociacaoPagamentoVO.getContaPagar().setValorPagamento(0.0);
    			contaPagarNegociacaoPagamentoVO.getContaPagar().setValorPago(0.0);
    			getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaPagar(listaLancamentoContabilVOs, obj.getData(), contaPagarNegociacaoPagamentoVO.getContaPagar(), null,  contaPagarNegociacaoPagamentoVO.getContaPagar().getPrevisaoValorPago(), false, obj.getResponsavel());
    			getFacadeFactory().getContaPagarFacade().alterar(contaPagarNegociacaoPagamentoVO.getContaPagar(), false, false, usuario);
    		}
    	}
    	persistirLancamentoContabilPorNegociacaoPagamento(obj, usuario, listaLancamentoContabilVOs);
    }    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarEGravarAdiantamentosUtilizadosComoDescontoNegociacaoPagamento(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
		for (ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO : obj.getContaPagarNegociacaoPagamentoVOs()) {
			if (!contaPagarNegociacaoPagamentoVO.getContaPagar().getListaContaPagarAdiantamentoVO().isEmpty()) {
				ContaPagarVO contaPagarComAdiantamento = contaPagarNegociacaoPagamentoVO.getContaPagar();
				// vamos varrer as contas de adiantamento que foram utilizadas para gravarmos a mesma, firmando o valor da mesma
				// que foi utilizada no adiantamento.
				for (ContaPagarAdiantamentoVO adiantamento : contaPagarComAdiantamento.getListaContaPagarAdiantamentoVO()) {
					getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(adiantamento.getContaPagarUtilizada(), adiantamento.getValorUtilizado(),  usuario , true);
				}
			}
		}
	}    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarEEstornarAdiantamentosUtilizadosComoDescontoNegociacaoPagamento(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
		for (ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO : obj.getContaPagarNegociacaoPagamentoVOs()) {
			if (!contaPagarNegociacaoPagamentoVO.getContaPagar().getListaContaPagarAdiantamentoVO().isEmpty()) {
				ContaPagarVO contaPagarComAdiantamento = contaPagarNegociacaoPagamentoVO.getContaPagar();
				// vamos varrer as contas de adiantamento que foram utilizadas para gravarmos a mesma, persitindo o valor
				// utilizado do adiantamento reduzido do valor uitlizado neste pagamento em especial (valor que fica
				// registrado dentro da tabela ContaPagarAdiantamentoVO - campo: ValorUtilizado
				for (ContaPagarAdiantamentoVO adiantamento : contaPagarComAdiantamento.getListaContaPagarAdiantamentoVO()) {
					adiantamento.getContaPagarUtilizada().setValorUtilizadoAdiantamento( adiantamento.getContaPagarUtilizada().getValorUtilizadoAdiantamento() - adiantamento.getValorUtilizado() );
					getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(adiantamento.getContaPagarUtilizada(), adiantamento.getValorUtilizado(),  usuario , false);
				}
				getFacadeFactory().getContaPagarAdiantamentoFacade().excluirContaPagarAdiantamentos(contaPagarComAdiantamento.getCodigo(), usuario);
				contaPagarComAdiantamento.getListaContaPagarAdiantamentoVO().clear();
				if(contaPagarComAdiantamento.getValorPago() <= 0.0) {
					contaPagarComAdiantamento.setJuro(0.0);
					contaPagarComAdiantamento.setMulta(0.0);
					contaPagarComAdiantamento.setDesconto(0.0);
					contaPagarComAdiantamento.setDescontoPorUsoAdiantamento(0.0);
					contaPagarComAdiantamento.setSituacao("AP");
					getFacadeFactory().getContaPagarFacade().alterar(contaPagarComAdiantamento, false, true, usuario);
					getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(contaPagarComAdiantamento.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, false, usuario);
				}
				
			}
		}
	}       
    
    /*
     * Método responsavel, por distribuir automaticamente todos os adiantamentos disponiveis para
     * abatimento, para as contas a pagar selecionadas para a negociacao de pagamento. Assim, já teremos
     * uma proposta automatica e ideal de distribuicao para o usuario. Permitindo, posteriormente ao mesmo
     * alterar essa distribuicao da melhor forma que desejar. 
     */
    public void realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(NegociacaoPagamentoVO negociacaoPagamento) throws Exception {
    	List<ContaPagarVO> listaAdiantamentosDisponiveisUsarAbatimento = negociacaoPagamento.getListaAdiantamentosUtilizadosAbaterContasPagar();
    	if ((listaAdiantamentosDisponiveisUsarAbatimento == null) || 
        		(listaAdiantamentosDisponiveisUsarAbatimento.isEmpty())) {
        		return; // nao existem adiantamentos para serem considerados
        	}
    	if (negociacaoPagamento.getValorTotal().doubleValue() <= 0) {
    		return; // nao existem contas a pagar disponiveis para abatimento de adiantamento.
    	}
    	if (negociacaoPagamento.getContaPagarNegociacaoPagamentoVOs().isEmpty()) {
    		return; // nao existem contas a pagar disponiveis para abatimento de adiantamento.
    	}
    	double valorTotalAdiantamentoAbatidoContasAPagar = 0.0;
    	int i = 0;
    	while (i < negociacaoPagamento.getContaPagarNegociacaoPagamentoVOs().size()) {
    		ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = negociacaoPagamento.getContaPagarNegociacaoPagamentoVOs().get(i);
    		Double valorAdiantamentoAbatidoEmContaPagar = getFacadeFactory().getContaPagarFacade().realizarDistribuicaoAdiantamentosDisponiveisParaAbaterContaPagar(contaPagarNegociacaoPagamento.getContaPagar(), contaPagarNegociacaoPagamento.getContaPagar().getValorPrevisaoPagamento(), listaAdiantamentosDisponiveisUsarAbatimento, true);
    		contaPagarNegociacaoPagamento.setValorContaPagar(contaPagarNegociacaoPagamento.getContaPagar().getPrevisaoValorPago());
    		valorTotalAdiantamentoAbatidoContasAPagar = valorTotalAdiantamentoAbatidoContasAPagar + valorAdiantamentoAbatidoEmContaPagar;
    		if (valorAdiantamentoAbatidoEmContaPagar <= 0) {
    			// se no ultimo processamento, nenhum valor dos adiantamentos foi utilizado, é por que já podemos
    			// sair da processamento.
    			break;
    		}
    		i++;
    	}
    	negociacaoPagamento.setValorTotalAtiantamentosAbaterContasPagar(valorTotalAdiantamentoAbatidoContasAPagar);
    	negociacaoPagamento.calcularTotal();
    }
    
    public void limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(NegociacaoPagamentoVO negociacaoPagamento, Boolean manterListaAdiantamentosParaNovoProcessamento) throws Exception {
		for (ContaPagarVO adiantamento : negociacaoPagamento.getListaAdiantamentosUtilizadosAbaterContasPagar()){
			// voltando o valor inicial do adiantamento, para novo processamento. uma vez que a lista esta sendo
			// mantida.
			adiantamento.setValorUtilizadoAdiantamento(adiantamento.getValorUtilizadoAdiantamentoBackup());
		}
    	if (!manterListaAdiantamentosParaNovoProcessamento) {
			negociacaoPagamento.setListaAdiantamentosUtilizadosAbaterContasPagar(null);			
		} 
    	for (ContaPagarNegociacaoPagamentoVO cpnp : negociacaoPagamento.getContaPagarNegociacaoPagamentoVOs()) {
    		cpnp.getContaPagar().setDescontoPorUsoAdiantamento(0.0);
    		cpnp.getContaPagar().getListaContaPagarAdiantamentoVO().removeIf(cpa-> cpa.getCodigo().equals(0));
    		cpnp.getContaPagar().setDescontoPorUsoAdiantamento(cpnp.getContaPagar().getListaContaPagarAdiantamentoVO().stream().mapToDouble(ContaPagarAdiantamentoVO::getValorUtilizado).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));	
		}
    	negociacaoPagamento.setValorTotalAtiantamentosAbaterContasPagar(0.0);
    	negociacaoPagamento.calcularTotal();
    }
    
     @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerAdiantamentosDisponiveisParaAbaterContasPagar(NegociacaoPagamentoVO negociacaoPagamento, ContaPagarAdiantamentoVO cpa,UsuarioVO usuario) {
		try {
			for (ContaPagarNegociacaoPagamentoVO cpnp : negociacaoPagamento.getContaPagarNegociacaoPagamentoVOs()) {
				Iterator<ContaPagarAdiantamentoVO> i = cpnp.getContaPagar().getListaContaPagarAdiantamentoVO().iterator();
				while (i.hasNext()) {
					ContaPagarAdiantamentoVO objExistente = (ContaPagarAdiantamentoVO) i.next();
					if(objExistente.getCodigo().equals(cpa.getCodigo())) {
						Uteis.checkState(getFacadeFactory().getContaPagarFacade().verificarExistenciaContaPagarRecebidaEmDuplicidade(cpnp.getContaPagar().getCodigo()), "Não é possível remover esse adiantamento, pois a conta pagar já foi paga.");
						cpnp.getContaPagar().setDescontoPorUsoAdiantamento(cpnp.getContaPagar().getDescontoPorUsoAdiantamento() - cpa.getValorUtilizado());
						getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(cpa.getContaPagarUtilizada(), cpa.getValorUtilizado(),  usuario , false);
						getFacadeFactory().getContaPagarFacade().alterarValorDescontoPorUsoAdiantamento(cpa.getContaPagar(), cpa.getValorUtilizado(),  usuario);
						getFacadeFactory().getContaPagarAdiantamentoFacade().excluir(cpa, usuario);
						i.remove();
					}
				}
			}
			negociacaoPagamento.calcularTotal();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NegociacaoPagamentoVO gerarNegociacaoContaPagarPorContaPagarPorFormaPagamentoNegociacaoPagamentoVO(ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, Date dataPagamento, boolean isControlaAcesso, UsuarioVO usuario) throws Exception {
		NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();
		if (contaPagar.getTipoSacadoEnum().isFuncionario()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
			negociacaoPagamentoVO.setFuncionario(contaPagar.getFuncionario());
		} else if (contaPagar.getTipoSacadoEnum().isParceiro()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.PARCEIRO.getValor());
			negociacaoPagamentoVO.setParceiro(contaPagar.getParceiro());
		} else if (contaPagar.getTipoSacadoEnum().isFornecedor()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
			negociacaoPagamentoVO.setFornecedor(contaPagar.getFornecedor());
		} else if (contaPagar.getTipoSacadoEnum().isBanco()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.BANCO.getValor());
			negociacaoPagamentoVO.setBanco(contaPagar.getBanco());
		} else if (contaPagar.getTipoSacadoEnum().isAluno()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.ALUNO.getValor());
			negociacaoPagamentoVO.setBanco(contaPagar.getBanco());
			negociacaoPagamentoVO.setAluno(contaPagar.getPessoa());
		} else if (contaPagar.getTipoSacadoEnum().isResponsavelFinanceiro()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor());
			negociacaoPagamentoVO.setResponsavelFinanceiro(contaPagar.getResponsavelFinanceiro());
		} else if (contaPagar.getTipoSacadoEnum().isCandidato()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.CANDIDATO.getValor());
			negociacaoPagamentoVO.setBanco(contaPagar.getBanco());
			negociacaoPagamentoVO.setAluno(contaPagar.getPessoa());
		}

		ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
		contaPagarNegociacaoPagamento.setContaPagar(contaPagar);
		contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
		negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
		negociacaoPagamentoVO.calcularTotal();

		formaPagamentoNegociacaoPagamentoVO.setValor(negociacaoPagamentoVO.getValorTotal());
		formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
		negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().add(formaPagamentoNegociacaoPagamentoVO);
		negociacaoPagamentoVO.calcularTotalPago();

		negociacaoPagamentoVO.setData(dataPagamento);
		negociacaoPagamentoVO.setDataRegistro(new Date());
		negociacaoPagamentoVO.setUnidadeEnsino(contaPagar.getUnidadeEnsino());
		negociacaoPagamentoVO.setResponsavel(usuario);
		getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, isControlaAcesso, usuario);
		return negociacaoPagamentoVO;
	}
    
}
