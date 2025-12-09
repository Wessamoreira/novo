package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

import appletImpressaoMatricial.LinhaImpressao;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.EmpresaOperadoraCartaoEnum;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisIreport;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.NegociacaoRecebimento.EnviarComunicadoCartaoCredito;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.NegociacaoRecebimentoInterfaceFacade;
import webservice.servicos.BandeiraRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>NegociacaoRecebimentoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>NegociacaoRecebimentoVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see NegociacaoRecebimentoVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class NegociacaoRecebimento extends ControleAcesso implements NegociacaoRecebimentoInterfaceFacade {

	public static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public NegociacaoRecebimento() throws Exception {
		super();
		setIdEntidade("NegociacaoRecebimento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NegociacaoRecebimentoVO novo() throws Exception {
		NegociacaoRecebimento.incluir(getIdEntidade());
		NegociacaoRecebimentoVO obj = new NegociacaoRecebimentoVO();
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public NegociacaoRecebimentoVO criarNegociacaoRecebimentoVOPorBaixaAutomatica(ContaReceberVO contaReceberVO, ContaCorrenteVO contaCorrente, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		negociacaoRecebimentoVO.setResponsavel(usuarioVO);
		//negociacaoRecebimentoVO.setUnidadeEnsino(contaReceberVO.getUnidadeEnsino());
		negociacaoRecebimentoVO.setUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira());
		negociacaoRecebimentoVO.setTipoPessoa(contaReceberVO.getTipoPessoa());
		negociacaoRecebimentoVO.setContaCorrenteCaixa(contaCorrente);
		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			negociacaoRecebimentoVO.setPessoa(contaReceberVO.getResponsavelFinanceiro());
		}else if (contaReceberVO.getPessoa() != null && contaReceberVO.getPessoa().getCodigo() != 0 && !contaReceberVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			if (contaReceberVO.getPessoa().getNome().equals("")) {
				negociacaoRecebimentoVO.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(contaReceberVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}else{
				negociacaoRecebimentoVO.setPessoa(contaReceberVO.getPessoa());
			}
			if(Uteis.isAtributoPreenchido(contaReceberVO.getMatriculaAluno().getMatricula())){
				negociacaoRecebimentoVO.setMatricula(contaReceberVO.getMatriculaAluno().getMatricula());	
			}
		} else if (contaReceberVO.getParceiroVO() != null && contaReceberVO.getParceiroVO().getCodigo() != 0) {
			negociacaoRecebimentoVO.setParceiroVO(contaReceberVO.getParceiroVO());
		} else if (contaReceberVO.getFornecedor() != null && contaReceberVO.getFornecedor().getCodigo() != 0) {
			negociacaoRecebimentoVO.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(contaReceberVO.getFornecedor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			contaReceberVO.setFornecedor(negociacaoRecebimentoVO.getFornecedor());
		}
		negociacaoRecebimentoVO.setValorTotalRecebimento(contaReceberVO.getValorRecebido());
		return negociacaoRecebimentoVO;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoRecebimentoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoRecebimento.incluir(getIdEntidade(), verificarAcesso, usuario);
				NegociacaoRecebimentoVO.validarDados(obj);
				verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario);
				
				for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : obj.getFormaPagamentoNegociacaoRecebimentoVOs()) {
					if(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().isCartaoCredito()) {						
						if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(), formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero(), usuario)) {
							throw new Exception("Não é possível realizar o recebimento na conta corrente " + formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento()) + ", pois a conciliação bancária já está finalizada.");
						}
					}else {
						if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(formaPagamentoNegociacaoRecebimentoVO.getDataCredito(), formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero(), usuario)) {
							throw new Exception("Não é possível realizar o recebimento na conta corrente " + formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(formaPagamentoNegociacaoRecebimentoVO.getDataCredito()) + ", pois a conciliação bancária já está finalizada.");
						}
						
					}
				}						
				
				obj.realizarUpperCaseDados();
				final String sql = "INSERT INTO NegociacaoRecebimento( data, valorTotalRecebimento, valorTotal, responsavel, contaCorrenteCaixa, pessoa, tipoPessoa, matricula, valorTroco, unidadeEnsino, parceiro, observacao, recebimentoBoletoAutomatico, fornecedor, dataregistro) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
					
					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlInserir = arg0.prepareStatement(sql);
						sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
						sqlInserir.setDouble(2, obj.getValorTotalRecebimento().doubleValue());
						sqlInserir.setDouble(3, obj.getValorTotal().doubleValue());
						if(!obj.getResponsavel().getCodigo().equals(0)) {
							sqlInserir.setInt(4, obj.getResponsavel().getCodigo().intValue());						
						} else {
							sqlInserir.setNull(4, 0);
						}
						if (obj.getContaCorrenteCaixa().getCodigo().intValue() != 0) {
							sqlInserir.setInt(5, obj.getContaCorrenteCaixa().getCodigo().intValue());
						} else {
							sqlInserir.setNull(5, 0);
						}
						if (obj.getPessoa().getCodigo().intValue() != 0) {
							sqlInserir.setInt(6, obj.getPessoa().getCodigo().intValue());
						} else {
							sqlInserir.setNull(6, 0);
						}
						sqlInserir.setString(7, obj.getTipoPessoa());
						if (obj.getTipoAluno().booleanValue() || obj.getTipoFuncionario().booleanValue()) {
							sqlInserir.setString(8, obj.getMatricula());
						} else {
							sqlInserir.setNull(8, 0);
						}
						sqlInserir.setDouble(9, obj.getValorTroco().doubleValue());
						if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
							sqlInserir.setDouble(10, obj.getUnidadeEnsino().getCodigo().intValue());
						} else {
							sqlInserir.setNull(10, 0);
						}
						if (obj.getParceiroVO().getCodigo().intValue() != 0) {
							sqlInserir.setInt(11, obj.getParceiroVO().getCodigo().intValue());
						} else {
							sqlInserir.setNull(11, 0);
						}
						sqlInserir.setString(12, obj.getObservacao());
						sqlInserir.setBoolean(13, obj.getRecebimentoBoletoAutomatico());
						if (obj.getFornecedor().getCodigo().intValue() != 0) {
							sqlInserir.setInt(14, obj.getFornecedor().getCodigo().intValue());
						} else {
							sqlInserir.setNull(14, 0);
						}
						sqlInserir.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
						return sqlInserir;
					}
				}, new ResultSetExtractor<Integer>() {
					
					public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
						if (arg0.next()) {
							obj.setNovoObj(Boolean.FALSE);
							return arg0.getInt("codigo");
						}
						return null;
					}
				}));
				if (obj.getValorTroco() > 0) {
					getFacadeFactory().getFluxoCaixaFacade().validarSaldoCaixa(obj.getValorTroco(), obj.getValorTotalRecebimento(), obj.getContaCorrenteCaixa().getCodigo(), usuario);
				}
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().incluirFormaPagamentoNegociacaoRecebimentos(obj, usuario);
				distribuirRecebimentoContaReceber(obj, usuario);
				if (obj.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
					validarDadosContaReceberSituacaoRecebidaOuAReceber(obj, false);
				}
				getFacadeFactory().getContaReceberNegociacaoRecebimentoFacade().incluirContaReceberNegociacaoRecebimentos(obj, obj.getContaReceberNegociacaoRecebimentoVOs(), configuracaoFinanceiro, usuario);
				
				// Altera a situação da conta a receber agrupada para recebida.
				getFacadeFactory().getContaReceberAgrupadaFacade().alterarSituacaoContaAgrupada(obj, SituacaoContaReceber.RECEBIDO.name(), usuario);
				
				if (obj.getValorTroco() > 0) {
					getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTroco(), obj.getContaCorrenteCaixa().getCodigo(), "SA", "TR", getFacadeFactory().getFormaPagamentoFacade().consultarPorTipoDinheiro(Uteis.NIVELMONTARDADOS_DADOSBASICOS).getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getPessoa().getCodigo(), 0, 0, obj.getParceiroVO().getCodigo(), null, 0, usuario);
				}
				getFacadeFactory().getMapaLancamentoFuturoFacade().baixarPendenciaAtravezDeNegociacaoRecebimento(obj, usuario);
				obj.setValorTrocoAlteracao(obj.getValorTroco());
				obj.setNovoObj(Boolean.FALSE);
				if (obj.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
					getFacadeFactory().getContaReceberFacade().baixarContaReceberConcedendoDescontoTotalAMesmaPorNegociacaoRecebimento(obj, "Desconto 100% no recebimento.", usuario, configuracaoFinanceiro, usuario);
				}
				
				if (Uteis.isAtributoPreenchido(obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo())) {
					if (obj.getConfiguracaoRecebimentoCartaoOnlineVO().getUsarConfiguracaoVisaoAdministrativa() 
							&& usuario.getIsApresentarVisaoAdministrativa()
							&& obj.getFormaPagamentoNegociacaoRecebimentoVOs().stream().anyMatch(p-> p.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline())) {
						validarDadosRecebimentoCartaoCreditoMatriculaOnline(obj, usuario);
					}
					if (EmpresaOperadoraCartaoEnum.CIELO.equals(configuracaoFinanceiro.getOperadora())
							&& obj.getFormaPagamentoNegociacaoRecebimentoVOs().stream().anyMatch(p-> p.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline())) {
						getFacadeFactory().getGerenciadorTransacaoCartaoFacade().realizarTransacaoComOperadoraCielo(obj, obj.getFormaPagamentoNegociacaoRecebimentoVOs(), obj.getConfiguracaoRecebimentoCartaoOnlineVO(), usuario);
					} else if (EmpresaOperadoraCartaoEnum.REDE.equals(configuracaoFinanceiro.getOperadora())
							&& obj.getFormaPagamentoNegociacaoRecebimentoVOs().stream().anyMatch(p-> p.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline())) {
						getFacadeFactory().getGerenciadorTransacaoCartaoFacade().realizarTransacaoComOperadoraRede(obj, obj.getFormaPagamentoNegociacaoRecebimentoVOs(), obj.getConfiguracaoRecebimentoCartaoOnlineVO(), usuario);
					} else if(obj.getFormaPagamentoNegociacaoRecebimentoVOs().stream().anyMatch(p-> p.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline())){
						throw new Exception("Empresa Operadora do Pagamento Online não informado! Deve ser verificado a configuração financeira existente.");
					}
				}
				if (obj.getRealizandoPagamentoJobRecorrencia()) {
					Thread enviarComunicadoCartaoCredito = new Thread(new EnviarComunicadoCartaoCredito((NegociacaoRecebimentoVO) obj.clone(), false, usuario));
					enviarComunicadoCartaoCredito.start();
				}
				// Este metodo deve ficar sempre no final do metodo incluir , responsavel por validar a transação;
				verificaSituacaoMatriculaPreMatriculadaParaEfetivacao(obj, configuracaoFinanceiro, usuario);
			
			} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			if (obj.getRealizandoPagamentoJobRecorrencia()) {
				Thread enviarComunicadoCartaoCredito = new Thread(new EnviarComunicadoCartaoCredito((NegociacaoRecebimentoVO) obj.clone(), true, usuario));
				enviarComunicadoCartaoCredito.start();
			}
			throw e;
		}
	}
	
	class EnviarComunicadoCartaoCredito implements Runnable{
		private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
		private UsuarioVO usuarioVO;
		private Boolean ocorreuErro;

		public EnviarComunicadoCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, Boolean ocorreuErro, UsuarioVO usuarioVO) {
			super();
			this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
			this.usuarioVO = usuarioVO;
			this.ocorreuErro = ocorreuErro;
		}

		@Override
		public void run() {
			try {
				ContaReceberVO obj = !negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().isEmpty() ? negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber() : null;
				if (Uteis.isAtributoPreenchido(obj)) {
					if (ocorreuErro) {
						getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().enviarMensagemAvisoErroPagamentoCartaoCredito(obj, usuarioVO);
					} else {
						getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().enviarMensagemAvisoSucessoPagamentoCartaoCredito(obj, usuarioVO);
					}
				}
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificaSituacaoMatriculaPreMatriculadaParaEfetivacao(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>();
		List<Integer> negociacaoContaReceberVOs = new ArrayList<Integer>();
		for (ContaReceberVO contaReceberVO : obj.getContaReceberVOs()) {
			if (contaReceberVO.getTipoOrigem().equals("NCR") && !Uteis.isAtributoPreenchido(contaReceberVO.getMatriculaPeriodo()) && !negociacaoContaReceberVOs.contains(Integer.parseInt(contaReceberVO.getCodOrigem()))) {
				negociacaoContaReceberVOs.add(Integer.parseInt(contaReceberVO.getCodOrigem()));
			}
		}
		for (Integer negociacaoContaReceber : negociacaoContaReceberVOs) {
			contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberMatriculaMensalidadeNegociacao(negociacaoContaReceber, usuario);
			for (ContaReceberVO contaReceberVO : contaReceberVOs) {
				verificaContaSituacaoMatriculaPreMatriculadaParaEfetivacao(configuracaoFinanceiro, usuario, contaReceberVO);	
			}
		}
		Iterator<ContaReceberVO> i = obj.getContaReceberVOs().iterator();
				while (i.hasNext()) {
					ContaReceberVO c = (ContaReceberVO) i.next();
					verificaContaSituacaoMatriculaPreMatriculadaParaEfetivacao(configuracaoFinanceiro, usuario, c);
				}
	}

	private void verificaContaSituacaoMatriculaPreMatriculadaParaEfetivacao(ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, ContaReceberVO c) throws Exception {
		String matriculaAlterada = ""; 
		if (c.getMatriculaPeriodo() != 0 && !matriculaAlterada.contains("["+c.getMatriculaPeriodo()+"]") && (c.getTipoOrigem().equals("MAT") || c.getTipoOrigem().equals("MEN") || c.getTipoOrigem().equals("NCR"))) {
			MatriculaPeriodoVO mp = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(c.getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiro, usuario);
			Boolean controlaParcelaMatricula = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarCondicaoPlanoAlunoControlaMatriculaPorMatriculaPeriodo(c.getMatriculaPeriodo());
			Boolean permiteAlterarSituacao = false;
			if (controlaParcelaMatricula == null) {
				permiteAlterarSituacao = true;
			} else if (controlaParcelaMatricula && c.getTipoOrigem().equals("MAT")) {
				permiteAlterarSituacao = true;
			} else if (!controlaParcelaMatricula && !c.getTipoOrigem().equals("NCR")) {
				permiteAlterarSituacao = true;
			} else if (c.getTipoOrigem().equals("NCR")) {
				permiteAlterarSituacao = getFacadeFactory().getNegociacaoContaReceberFacade().realizarValidacaoAtivacaoMatriculaPeriodoParcelaRenegociada(c.getCodigo(), c.getMatriculaPeriodo(), controlaParcelaMatricula);
			}
			if (mp.getSituacaoMatriculaPeriodo().equals("PR") && configuracaoFinanceiro.getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula().booleanValue() && permiteAlterarSituacao) {
					matriculaAlterada += matriculaAlterada.contains("["+c.getMatriculaPeriodo()+"]");
					if(getFacadeFactory().getMatriculaPeriodoFacade().realizarValidacaoRegraAtivacaoMatriculaPorEntregaDocumentoEContratoMatricula(mp.getMatriculaVO().getMatricula(),   usuario)) {			    	
					    getFacadeFactory().getMatriculaPeriodoFacade().efetivarMatriculaPeriodoPreMatriculada(mp, configuracaoFinanceiro, usuario, true);
					}
					if (configuracaoFinanceiro.getEnviarNotificacaoConsultorMatricula()) {
			        	notificarConsultorMatriculaPaga(c.getMatriculaAluno(), mp.getTurma(), configuracaoFinanceiro, usuario);
			        }							
					notificarAlunoMatriculaPaga(c.getMatriculaAluno(), mp.getTurma(), configuracaoFinanceiro, usuario);						
					getFacadeFactory().getMatriculaFacade().alterarSituacaoFinanceiraMatricula(mp.getMatricula(), "QU");
					getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoFinanceiraMatriculaPeriodo(mp.getCodigo(), "AT");
			} else if (mp.getSituacao().equals("PF") && permiteAlterarSituacao) {
				matriculaAlterada += matriculaAlterada.contains("["+c.getMatriculaPeriodo()+"]");
				if (configuracaoFinanceiro.getEnviarNotificacaoConsultorMatricula()) {
		        	notificarConsultorMatriculaPaga(c.getMatriculaAluno(), mp.getTurma(), configuracaoFinanceiro, usuario);
		        }							
				notificarAlunoMatriculaPaga(c.getMatriculaAluno(), mp.getTurma(), configuracaoFinanceiro, usuario);						
				getFacadeFactory().getMatriculaFacade().alterarSituacaoFinanceiraMatricula(mp.getMatricula(), "QU");
				getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoFinanceiraMatriculaPeriodo(mp.getCodigo(), "AT");
			}

		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoRecebimentoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoRecebimento.alterar(getIdEntidade(), true, usuario);
			NegociacaoRecebimentoVO.validarDados(obj);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario);
			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : obj.getFormaPagamentoNegociacaoRecebimentoVOs()) {
				if(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().isCartaoCredito()) {						
					if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(), formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero(), usuario)) {
						throw new Exception("Não é possível realizar o recebimento na conta corrente " + formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento()) + ", pois a conciliação bancária já está finalizada.");
					}
				}else {
					if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(formaPagamentoNegociacaoRecebimentoVO.getDataCredito(), formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero(), usuario)) {
						throw new Exception("Não é possível realizar o recebimento na conta corrente " + formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(formaPagamentoNegociacaoRecebimentoVO.getDataCredito()) + ", pois a conciliação bancária já está finalizada.");
					}
					
				}
			}
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE NegociacaoRecebimento set data=?, valorTotalRecebimento=?, valorTotal=?, responsavel=?, contaCorrenteCaixa=?, pessoa=?, tipoPessoa=?, matricula=?, valorTroco=?, unidadeEnsino=?, parceiro=?, observacao=?, recebimentoBoletoAutomatico=?, fornecedor = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setDouble(2, obj.getValorTotalRecebimento().doubleValue());
					sqlAlterar.setDouble(3, obj.getValorTotal().doubleValue());
					sqlAlterar.setInt(4, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setInt(5, obj.getContaCorrenteCaixa().getCodigo().intValue());
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getTipoPessoa());
					sqlAlterar.setString(8, obj.getMatricula());
					sqlAlterar.setDouble(9, obj.getValorTroco());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setDouble(10, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (obj.getParceiroVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getParceiroVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, obj.getObservacao());
					sqlAlterar.setBoolean(13, obj.getRecebimentoBoletoAutomatico());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(14, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(14, 0);
					}
					sqlAlterar.setInt(15, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			if (obj.getAlterouConteudo()) {
				if (obj.getValorTrocoAlteracao().doubleValue() > 0) {
					getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTrocoAlteracao(), obj.getContaCorrenteCaixa().getCodigo(), "EN", "TR", getFacadeFactory().getFormaPagamentoFacade().consultarPorTipoDinheiro(Uteis.NIVELMONTARDADOS_DADOSBASICOS).getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getPessoa().getCodigo(), 0, 0, obj.getParceiroVO().getCodigo(), null, 0, usuario);
				}
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().alterarFormaPagamentoNegociacaoRecebimentos(obj, configuracaoFinanceiroVO, usuario);
				distribuirRecebimentoContaReceber(obj, usuario);
				if (obj.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
					validarDadosContaReceberSituacaoRecebidaOuAReceber(obj, false);
				}
				getFacadeFactory().getContaReceberNegociacaoRecebimentoFacade().alterarContaReceberNegociacaoRecebimentos(obj, obj.getContaReceberNegociacaoRecebimentoVOs(), configuracaoFinanceiroVO, usuario);
				if (obj.getValorTroco() > 0) {
					getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTroco(), obj.getContaCorrenteCaixa().getCodigo(), "SA", "TR", getFacadeFactory().getFormaPagamentoFacade().consultarPorTipoDinheiro(Uteis.NIVELMONTARDADOS_DADOSBASICOS).getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getPessoa().getCodigo(), 0, 0, obj.getParceiroVO().getCodigo(), null, 0, usuario);
				}
				obj.setValorTrocoAlteracao(obj.getValorTroco());
				if (obj.getValorTotalRecebimento() >= obj.getValorTotal()) {
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void distribuirRecebimentoContaReceber(NegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		zerarValoresRecebidoContaReceber(obj);
		List<LancamentoContabilVO> listaLancamentoContabilVOs = new ArrayList<>();
		Iterator i = obj.getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			FormaPagamentoNegociacaoRecebimentoVO rcbmnt = (FormaPagamentoNegociacaoRecebimentoVO) i.next();
			Double vlrPgmnt = rcbmnt.getValorRecebimento();
			distribuirContaReceberNegociacao(obj, rcbmnt, vlrPgmnt, listaLancamentoContabilVOs);
		}
		persistirLancamentoContabilPorNegociacaoRecebimento(obj, usuario, listaLancamentoContabilVOs);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void distribuirContaReceberNegociacao(NegociacaoRecebimentoVO obj, FormaPagamentoNegociacaoRecebimentoVO rcbmnt, Double valorRecebimento, List<LancamentoContabilVO> listaLancamentoContabilVOs) throws Exception {
		Iterator j = obj.getContaReceberNegociacaoRecebimentoVOs().iterator();
		while (valorRecebimento.doubleValue() >= 0 && j.hasNext()) {
			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacao = (ContaReceberNegociacaoRecebimentoVO) j.next();
			if (!contaReceberNegociacao.getContaReceber().getSituacao().equals("RE")) {
				Double valorAreceber = Uteis.arrendondarForcando2CadasDecimais(contaReceberNegociacao.getValorTotal().doubleValue() - contaReceberNegociacao.getContaReceber().getValorNegociacao().doubleValue());
				ContaReceberRecebimentoVO contaReceberRecebimento = new ContaReceberRecebimentoVO();
				contaReceberRecebimento.setFormaPagamento(rcbmnt.getFormaPagamento());
				contaReceberRecebimento.setContaReceber(contaReceberNegociacao.getContaReceber().getCodigo());
				contaReceberRecebimento.setResponsavel(obj.getResponsavel());
				contaReceberRecebimento.setMotivo(obj.getMotivoAlteracao());
				contaReceberRecebimento.setFormaPagamentoNegociacaoRecebimento(rcbmnt.getCodigo());
				contaReceberRecebimento.setFormaPagamentoNegociacaoRecebimentoVO(rcbmnt);
				contaReceberRecebimento.setNegociacaoRecebimento(obj.getCodigo());
				contaReceberRecebimento.setTipoRecebimento("CR");
				contaReceberRecebimento.setRecebimentoTerceirizado(contaReceberNegociacao.getContaReceberTerceiro());
				// Adicionado para sempre pegar a data da negociacaoRecebimento,
				// já que ocorria erros quando a conta
				// era baixada por um arquivo de retorno, e a data de
				// recebimento da conta era a do dia atual, sendo que
				// a conta poderia ter sido paga a alguns dias antes.
				contaReceberRecebimento.setDataRecebimeto(obj.getData());
				if (valorAreceber >= 0) {
					if (Uteis.arrendondarForcando2CadasDecimais(valorRecebimento) >= Uteis.arrendondarForcando2CadasDecimais(valorAreceber)) {
						contaReceberNegociacao.getContaReceber().setSituacao("RE");
						contaReceberNegociacao.getContaReceber().setValorNegociacao(valorAreceber);
						contaReceberRecebimento.setValorRecebimento(valorAreceber);
						contaReceberNegociacao.getContaReceber().setValorDescontoRecebido(contaReceberNegociacao.getContaReceber().getValorDescontoAlunoJaCalculado() + contaReceberNegociacao.getContaReceber().getValorDescontoInstituicao() + contaReceberNegociacao.getContaReceber().getValorDescontoConvenio() + contaReceberNegociacao.getContaReceber().getValorDescontoProgressivo() + contaReceberNegociacao.getContaReceber().getValorCalculadoDescontoLancadoRecebimento());
						contaReceberNegociacao.getContaReceber().adicionarObjContaReceberRecebimentoVOs(contaReceberRecebimento);
						getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaReceber(listaLancamentoContabilVOs, contaReceberNegociacao.getContaReceber(), rcbmnt, valorAreceber, obj.getData(), false, obj.getResponsavel());
						valorRecebimento = Uteis.arrendondarForcando2CadasDecimais(valorRecebimento - valorAreceber);
					} else {
						contaReceberNegociacao.getContaReceber().setValorNegociacao(Uteis.arrendondarForcando2CadasDecimais(contaReceberNegociacao.getContaReceber().getValorNegociacao() + valorRecebimento));
						contaReceberRecebimento.setValorRecebimento(valorRecebimento);
						contaReceberNegociacao.getContaReceber().adicionarObjContaReceberRecebimentoVOs(contaReceberRecebimento);
						getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaReceber(listaLancamentoContabilVOs, contaReceberNegociacao.getContaReceber(), rcbmnt, valorRecebimento, obj.getData(), false, obj.getResponsavel());
						valorRecebimento = 0.0;
						break;
					}
				}
			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void zerarValoresRecebidoContaReceber(NegociacaoRecebimentoVO negociacaoRecebimento) {
		for (ContaReceberNegociacaoRecebimentoVO obj : negociacaoRecebimento. getContaReceberNegociacaoRecebimentoVOs()) {
			obj.getContaReceber().setSituacao("AR");
			obj.getContaReceber().setValorNegociacao(0.0);
			obj.getContaReceber().setContaReceberRecebimentoVOs(new ArrayList<ContaReceberRecebimentoVO>(0));
			obj.getContaReceber().getListaLancamentoContabeisCredito().clear();	
	        obj.getContaReceber().getListaLancamentoContabeisDebito().clear();
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void persistirLancamentoContabilPorNegociacaoRecebimento(NegociacaoRecebimentoVO obj, UsuarioVO usuario, List<LancamentoContabilVO> listaLancamentoContabilVOs) throws Exception {		
		if(Uteis.isAtributoPreenchido(listaLancamentoContabilVOs)){
			for (LancamentoContabilVO objExistente : listaLancamentoContabilVOs) {
				getFacadeFactory().getLancamentoContabilFacade().persistir(objExistente, false, usuario);
				obj.getContaReceberNegociacaoRecebimentoVOs()
				.stream()
				.filter(p-> p.getContaReceber().getCodigo().toString().equals(objExistente.getCodOrigem()))
				.findFirst()
				.ifPresent(e->{
					e.getContaReceber().setLancamentoContabil(true);					
					if (objExistente.getTipoPlanoConta().isCredito()) {
						e.getContaReceber().getListaLancamentoContabeisCredito().add(objExistente);
					} else {
						e.getContaReceber().getListaLancamentoContabeisDebito().add(objExistente);
					}
				});
			}
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTipoRecebimentoBoletoAutomatico(final Boolean recebimentoBoletoAutomatico, final Integer codigo, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE negociacaorecebimento set recebimentoBoletoAutomatico=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setBoolean(++i, recebimentoBoletoAutomatico);
					sqlAlterar.setInt(++i, codigo.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoRecebimentoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void excluir(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoRecebimento.excluir(getIdEntidade(), true, usuario);
			if (obj.getCodigo().intValue() != 0) {
				verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "EXCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario);
								
				validarDadosMotivoEstorno(obj);
				getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.RECEBIMENTO, obj.getCodigo(), obj.isDesconsiderarConciliacaoBancaria(), 0, false, usuario);				
				obj.setValorTotalRecebimento(0.0);
				obj.setData(null);
				if (obj.getValorTroco() > 0) {
					getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTroco(), obj.getContaCorrenteCaixa().getCodigo(), "EN", "TR", getFacadeFactory().getFormaPagamentoFacade().consultarPorTipoDinheiro(Uteis.NIVELMONTARDADOS_DADOSBASICOS).getCodigo(), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getPessoa().getCodigo(), 0, 0, obj.getParceiroVO().getCodigo(), null, 0, usuario);
				}
				getFacadeFactory().getContaReceberNegociacaoRecebimentoFacade().excluirContaReceberNegociacaoRecebimentos(obj.getCodigo(), usuario);
				getFacadeFactory().getContaReceberAgrupadaFacade().alterarSituacaoContaAgrupada(obj, SituacaoContaReceber.A_RECEBER.name(), usuario);
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().excluirFormaPagamentoNegociacaoRecebimentos(obj, configuracaoFinanceiroVO, usuario, true);
				for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : obj.getContaReceberNegociacaoRecebimentoVOs()) {
					getFacadeFactory().getProcessamentoArquivoRetornoParceiroExcelFacade().removerVinculoContaReceberEspecifica(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo(), usuario);
					getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.RECEBER, false, usuario);
					getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluirPorContaReceber(contaReceberNegociacaoRecebimentoVO.getContaReceber(), usuario);
					// Altera a SituacaoMatriculaPeriodo para PR (Pré-Matricula) quando é realizado o estorno do Recebimento. 
					// Somente será realizada a ateração se a SituacaoMatriculaPeriodo atual é AT (Ativa).
					// Foi adicionado a validação abaixo pois só deve ser considerado estorno de contas de matricula ou mensalidade se não controlar parcela de matricula
					getFacadeFactory().getMatriculaPeriodoFacade().realizarEstornoSituacaoMatriculaPeriodoAoEstornarContaReceber(contaReceberNegociacaoRecebimentoVO.getContaReceber(), configuracaoFinanceiroVO, usuario );
					if (contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())) {
						executarCriacaoPendenciaChequeDevolvidoExtornoRecebimento(obj, contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodOrigem(), usuario);
					}
				}
				String sql = "DELETE FROM NegociacaoRecebimento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
				List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoPgtoCartaoOnlineVOs =   obj.getFormaPagamentoNegociacaoRecebimentoVOs().stream().filter(t -> 
				(t.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())
						|| t.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor()))
						&& t.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()
						&& !t.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao().isEmpty()).collect(Collectors.toList());
					
				if (formaPagamentoNegociacaoRecebimentoPgtoCartaoOnlineVOs != null && !formaPagamentoNegociacaoRecebimentoPgtoCartaoOnlineVOs.isEmpty()) {
					ConfiguracaoFinanceiroVO configuracaoFinanceiroVO2 = (ConfiguracaoFinanceiroVO) formaPagamentoNegociacaoRecebimentoPgtoCartaoOnlineVOs.get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO().clone();
					EmpresaOperadoraCartaoEnum operadora = configuracaoFinanceiroVO2.getOperadora();
					if (operadora.equals(EmpresaOperadoraCartaoEnum.NENHUM)) {
						configuracaoFinanceiroVO2 = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorChavePrimariaUnica(configuracaoFinanceiroVO2.getCodigo(), usuario);
						 operadora = configuracaoFinanceiroVO2.getOperadora();
					}
					if (EmpresaOperadoraCartaoEnum.CIELO.equals(operadora)) {
						getFacadeFactory().getGerenciadorTransacaoCartaoFacade().cancelarVendaComOperadoraCielo(obj, formaPagamentoNegociacaoRecebimentoPgtoCartaoOnlineVOs, usuario);
					} else if (EmpresaOperadoraCartaoEnum.REDE.equals(operadora)) {
						getFacadeFactory().getGerenciadorTransacaoCartaoFacade().cancelarVendaComOperadoraRede(obj, formaPagamentoNegociacaoRecebimentoPgtoCartaoOnlineVOs, usuario);
							} else {
								throw new Exception("Empresa Operadora do Pagamento Online não informado!");
							}
						}
					
					}

		} catch (Exception e) {
			throw e;
		}
	}
	
	

	public void validarDadosMotivoEstorno(NegociacaoRecebimentoVO obj) throws Exception {
		if (obj.getMotivoAlteracao().equals("")) {
			throw new Exception("O campop MOTIVO ALTERAÇÃO/EXCLUSÃO deve ser informado.");
		}
		if (UteisData.getCompareData(obj.getData(),obj.getDataEstorno() )>0) {
			throw new Exception("A data do estorno não pode ser anterior a data do recebimento.");
		}
	}

	public void validarDadosDataEstorno(Date data, Date dataEstorno) throws Exception {
		if (Uteis.getDataMinutos(dataEstorno).before(Uteis.getDataMinutos(data))) {
			throw new Exception("A data do estorno não pode ser anterior a data do recebimento.");
		}
	}

	/*
	 * Responsável por criar a String de autenticação mecânica composta pelos
	 * seguintes campos 1- Numero do Documento 2- Data pagamento (10/10/10) 3-
	 * Ano e Semestre (2010/2) 4- Numero da Parcela 5- Primeiro Nome da Pessoa
	 * 6- Valor Recebido 7- Usuário Responsavel pela autenticação
	 */
	public List<LinhaImpressao> executarAutenticacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<LinhaImpressao> linhaImpressaos = new ArrayList<LinhaImpressao>();
		LinhaImpressao linhaImpressao = new LinhaImpressao();
		linhaImpressao.setFinalizarLinha(true);
		linhaImpressao.setPosicao(1);
		StringBuilder autenticacao = new StringBuilder(contaReceberNegociacaoRecebimentoVO.getContaReceber().getNrDocumento());
		autenticacao.append("-").append(Uteis.obterDataFormatoTextoddMMyy(negociacaoRecebimentoVO.getData()));
		if (contaReceberNegociacaoRecebimentoVO.getContaReceber().getMatriculaPeriodo().intValue() > 0) {
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(contaReceberNegociacaoRecebimentoVO.getContaReceber().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuario);
			autenticacao.append("-").append(matriculaPeriodoVO.getAno()).append("/").append(matriculaPeriodoVO.getSemestre());
		} else {
			autenticacao.append("-").append(Uteis.getAnoData(contaReceberNegociacaoRecebimentoVO.getContaReceber().getDataVencimento())).append("/").append(Uteis.getSemestreData(contaReceberNegociacaoRecebimentoVO.getContaReceber().getDataVencimento()));
		}
		autenticacao.append("-").append(contaReceberNegociacaoRecebimentoVO.getContaReceber().getParcela());
		autenticacao.append("-").append(Uteis.obterPrimeiroNomeConcatenadoSobreNome(negociacaoRecebimentoVO.getNomePessoaParceiro(), 1));
		autenticacao.append("-").append(contaReceberNegociacaoRecebimentoVO.getValorTotal());
		autenticacao.append("-").append(Uteis.obterPrimeiroNomeConcatenadoSobreNome(usuario.getNome(), 0));
		linhaImpressao.setLinha(autenticacao.toString());
		linhaImpressaos.add(linhaImpressao);
		return linhaImpressaos;
	}

	public void validarDadosContaReceberSituacaoRecebidaOuAReceber(NegociacaoRecebimentoVO obj, boolean erro) {
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : obj.getContaReceberNegociacaoRecebimentoVOs()) {
			if (erro) {
				contaReceberNegociacaoRecebimentoVO.getContaReceber().setSituacao("AR");
			} else {
				contaReceberNegociacaoRecebimentoVO.getContaReceber().setSituacao("RE");
			}
		}
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>NegociacaoRecebimento</code> através do valor do atributo
	 * <code>Integer pessoa</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>NegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorPessoa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT NegociacaoRecebimento.* FROM NegociacaoRecebimento, Pessoa WHERE NegociacaoRecebimento.pessoa = Pessoa.codigo and lower (Pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and NegociacaoRecebimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " AND data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		sqlStr += " AND data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		sqlStr += "ORDER BY pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@SuppressWarnings("static-access")
	public List<NegociacaoRecebimentoVO> consultaRapidaPorPessoa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE contaReceber.nomesacado ilike sem_acentos(?) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimento.unidadeensino = " + unidadeEnsino + ") AND");
		}
		if (!tipoOrigem.equals("")) {
			sql.append(" contareceber.tipoorigem = '").append(tipoOrigem).append("' and ");
		}
		sql.append("       (negociacaorecebimento.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE contaReceber.nomesacado ilike sem_acentos(?) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimentodcc.unidadeensino = " + unidadeEnsino + ") AND");
		}
		if (!tipoOrigem.equals("")) {
			sql.append(" contareceber.tipoorigem = '").append(tipoOrigem).append("' and ");
		}
		sql.append("       (negociacaorecebimentodcc.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		sql.append(" ORDER BY \"Pessoa.nome\", \"Parceiro.nome\"");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), PERCENT + valorConsulta + PERCENT, PERCENT + valorConsulta + PERCENT);
		return montarDadosConsultaBasica(resultado);
	}

	public List consultarPorCpf(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT NegociacaoRecebimento.* FROM NegociacaoRecebimento, Pessoa WHERE NegociacaoRecebimento.pessoa = Pessoa.codigo and lower (Pessoa.cpf) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and NegociacaoRecebimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " AND data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		sqlStr += " AND data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		sqlStr += "ORDER BY pessoa.cpf";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@SuppressWarnings("static-access")
	public List<NegociacaoRecebimentoVO> consultaRapidaPorCpf(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		// StringBuffer sql = getSQLPadraoConsultaBasica();
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE (LOWER(pessoa.cpf) LIKE LOWER('" + valorConsulta + "%') or LOWER(fornecedor.cpf) LIKE LOWER('" + valorConsulta + "%')) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimento.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimento.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE (LOWER(pessoa.cpf) LIKE LOWER('" + valorConsulta + "%') or LOWER(fornecedor.cpf) LIKE LOWER('" + valorConsulta + "%')) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimentodcc.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimentodcc.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append("ORDER BY \"Pessoa.cpf\"");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	/*
	 * public List consultarPorData(Date dataIni,Date dataFim, Integer
	 * unidadeEnsino, boolean controlarAcesso, int nivelMontarDados) throws
	 * Exception { ControleAcesso.consultar(getIdEntidade(), controlarAcesso,
	 * usuario); String sqlStr =
	 * "SELECT * FROM NegociacaoRecebimento WHERE (( data >= '"+Uteis
	 * .getDataJDBCTimestamp
	 * (dataIni)+"') and ( data <= '"+Uteis.getDataJDBCTimestamp
	 * (dataFim)+"' )) "; if(unidadeEnsino.intValue() !=0){ sqlStr +=
	 * " and unidadeEnsino = "+unidadeEnsino.intValue(); } sqlStr +=
	 * "ORDER BY data"; Statement stm = con.createStatement(); ResultSet
	 * tabelaResultado = stm.executeQuery(sqlStr); return
	 * (montarDadosConsulta(tabelaResultado, nivelMontarDados)); }
	 */
	public List consultarPorMatricula(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM NegociacaoRecebimento WHERE lower (matricula) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " AND data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		sqlStr += " AND data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		sqlStr += " ORDER BY matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@SuppressWarnings("static-access")
	public List<NegociacaoRecebimentoVO> consultaRapidaPorMatricula(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE exists (select 1 from contareceber cr where cr.codigo = contareceber.codigo and LOWER(cr.matriculaaluno) like LOWER(?)) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimento.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimento.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE exists (select 1 from contareceber cr where cr.codigo = contareceber.codigo and LOWER(cr.matriculaaluno) like LOWER(?)) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimentodcc.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimentodcc.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append("ORDER BY matricula ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT, valorConsulta + PERCENT);
		return montarDadosConsultaBasica(resultado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>NegociacaoRecebimento</code> através do valor do atributo
	 * <code>Integer contaCorrenteCaixa</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>NegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorContaCorrenteCaixa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT NegociacaoRecebimento.* FROM NegociacaoRecebimento, ContaCorrente WHERE NegociacaoRecebimento.contaCorrenteCaixa = contaCorrente.codigo and lower (contaCorrente.numero) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and NegociacaoRecebimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " AND data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		sqlStr += " AND data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		sqlStr += " ORDER BY ContaCorrente.numero ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List<NegociacaoRecebimentoVO> consultaRapidaPorContaCorrenteCaixa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE (LOWER(contacorrentecaixa.numero) LIKE LOWER('" + valorConsulta + "%')) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimento.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimento.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE (LOWER(contacorrentecaixa.numero) LIKE LOWER('" + valorConsulta + "%')) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimentodcc.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimentodcc.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append("ORDER BY \"ContaCorrenteCaixa.numero\"");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>NegociacaoRecebimento</code> através do valor do atributo
	 * <code>Integer responsavel</code>. Retorna os objetos com valores iguais
	 * ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>NegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorResponsavel(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT NegociacaoRecebimento.* FROM NegociacaoRecebimento, Usuario WHERE NegociacaoRecebimento.responsavel = Usuario.codigo and lower (Usuario.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and NegociacaoRecebimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " AND data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
		sqlStr += " AND data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		sqlStr += " ORDER BY Usuario.nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List<NegociacaoRecebimentoVO> consultaRapidaPorResponsavel(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE (LOWER(usuario.nome) LIKE LOWER('" + valorConsulta + "%')) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimento.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimento.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE (LOWER(usuario.nome) LIKE LOWER('" + valorConsulta + "%')) AND");
		if (unidadeEnsino.intValue() != 0) {
			sql.append("   (negociacaorecebimentodcc.unidadeensino = " + unidadeEnsino + ") AND");
		}
		sql.append("       (negociacaorecebimentodcc.data::Date BETWEEN '" + Uteis.getDataJDBC(dataIni) + "' AND '" + Uteis.getDataJDBC(dataFim) + "') ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append("ORDER BY \"Responsavel.nome\"");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>NegociacaoRecebimento</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>NegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM NegociacaoRecebimento WHERE codigo = " + valorConsulta.intValue() + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@SuppressWarnings("static-access")
	public List<NegociacaoRecebimentoVO> consultaRapidaPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE (negociacaorecebimento.codigo = " + valorConsulta + ") ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE (negociacaorecebimentodcc.codigo = " + valorConsulta + ") ");
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append(" ORDER BY codigo");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>NegociacaoRecebimentoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static List montarDadosConsultaSerasa(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosSerasa(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>.
	 * 
	 * @return O objeto da classe <code>NegociacaoRecebimentoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static NegociacaoRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		NegociacaoRecebimentoVO obj = new NegociacaoRecebimentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setDataRegistro(dadosSQL.getTimestamp("dataRegistro"));
		obj.setValorTotalRecebimento(new Double(dadosSQL.getDouble("valorTotalRecebimento")));
		obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
		obj.setValorTroco(new Double(dadosSQL.getDouble("valorTroco")));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getContaCorrenteCaixa().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteCaixa")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
		obj.setValorTrocoAlteracao(obj.getValorTroco());
		obj.setRecebimentoBoletoAutomatico(dadosSQL.getBoolean("recebimentoBoletoAutomatico"));		
		if (obj.getTipoPessoa().trim().equals("") && !obj.getMatricula().trim().isEmpty()) {
			obj.setTipoPessoa("AL");
		}
		obj.setNovoObj(Boolean.FALSE);
		// Atributo utilizado para controle de envio de notificação SERASA, não
		// remover ou alterar ( Thyago Jayme )
//		try {
//			obj.getParceiroVO().setCodigo(dadosSQL.getInt("codcontarecebernegociacaorecebimento"));
//		} catch (Exception e) {
//		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosCaixa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		obj.setContaReceberNegociacaoRecebimentoVOs(ContaReceberNegociacaoRecebimento.consultarContaReceberNegociacaoRecebimentos(obj.getCodigo(), nivelMontarDados, configuracaoFinanceiro, usuario));
		obj.setFormaPagamentoNegociacaoRecebimentoVOs(FormaPagamentoNegociacaoRecebimento.consultarFormaPagamentoNegociacaoRecebimentos(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosCaixa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	public static NegociacaoRecebimentoVO montarDadosSerasa(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		NegociacaoRecebimentoVO obj = new NegociacaoRecebimentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setValorTotalRecebimento(new Double(dadosSQL.getDouble("valorTotalRecebimento")));
		// obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
		// obj.setValorTroco(new Double(dadosSQL.getDouble("valorTroco")));
		// obj.getResponsavel().setCodigo(new
		// Integer(dadosSQL.getInt("responsavel")));
		// obj.getContaCorrenteCaixa().setCodigo(new
		// Integer(dadosSQL.getInt("contaCorrenteCaixa")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		// obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		// obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
		// obj.setValorTrocoAlteracao(obj.getValorTroco());
		// obj.setRecebimentoBoletoAutomatico(dadosSQL.getBoolean("recebimentoBoletoAutomatico"));
		obj.setNovoObj(Boolean.FALSE);
		// Atributo utilizado para controle de envio de notificação SERASA, não
		// remover ou alterar ( Thyago Jayme )
		try {
			obj.getParceiroVO().setCodigo(dadosSQL.getInt("codcontarecebernegociacaorecebimento"));
		} catch (Exception e) {
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		// if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
		// montarDadosCaixa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
		// usuario);
		// montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// usuario);
		// return obj;
		// }
		// obj.setContaReceberNegociacaoRecebimentoVOs(ContaReceberNegociacaoRecebimento.consultarContaReceberNegociacaoRecebimentos(obj.getCodigo(),
		// nivelMontarDados, configuracaoFinanceiro, usuario));
		// obj.setFormaPagamentoNegociacaoRecebimentoVOs(FormaPagamentoNegociacaoRecebimento.consultarFormaPagamentoNegociacaoRecebimentos(obj.getCodigo(),
		// nivelMontarDados, usuario));
		// if (nivelMontarDados ==
		// Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
		// return obj;
		// }
		// montarDadosCaixa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// usuario);
		// montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
		// usuario);
		// montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// usuario);
		return obj;
	}

	public static void montarDadosResponsavel(NegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(NegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosPessoa(NegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCaixa(NegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrenteCaixa().getCodigo().intValue() == 0) {
			obj.setContaCorrenteCaixa(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrenteCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteCaixa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosParceiro(NegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiroVO().getCodigo().intValue() == 0) {
			obj.setParceiroVO(new ParceiroVO());
			return;
		}
		obj.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiroVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public NegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM NegociacaoRecebimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NegociacaoRecebimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public String consultarDescricaoPorCodigo(Integer codigoPrm) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT observacao FROM NegociacaoRecebimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("observacao");
		}
		return "";
	}

	@SuppressWarnings("static-access")
	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (negociacaorecebimento.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NegociacaoRecebimento ).");
		}
		// Reposiciona cursor para posição anterior ao primeiro registro.
		//tabelaResultado.beforeFirst();
		return tabelaResultado;
	}

	@SuppressWarnings("static-access")
	public SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append(" WHERE (negociacaorecebimento.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NegociacaoRecebimento ).");
		}
		// Reposiciona cursor para posição anterior ao primeiro registro.
		tabelaResultado.beforeFirst();
		return tabelaResultado;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return NegociacaoRecebimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		NegociacaoRecebimento.idEntidade = idEntidade;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT negociacaorecebimento.codigo, negociacaorecebimento.tipopessoa, negociacaorecebimento.pessoa, pessoa.nome AS \"Pessoa.nome\", pessoa.cpf AS \"Pessoa.cpf\", negociacaorecebimento.responsavel, parceiro.codigo AS \"Parceiro.codigo\", parceiro.nome AS \"Parceiro.nome\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.cnpj AS \"Parceiro.cnpj\",");
		sql.append("       usuario.nome AS \"Responsavel.nome\", negociacaorecebimento.matricula, bancocontacorrentecaixa.codigo AS \"BancoContaCorrenteCaixa.codigo\", bancocontacorrentecaixa.nome AS \"BancoContaCorrenteCaixa.nome\",");
		sql.append("       agenciacontacorrentecaixa.codigo AS \"AgenciaContaCorrenteCaixa.codigo\", agenciacontacorrentecaixa.numeroAgencia AS \"AgenciaContaCorrenteCaixa.numeroAgencia\", agenciacontacorrentecaixa.digito AS \"AgenciaContaCorrenteCaixa.digito\", contacorrentecaixa.nomeApresentacaoSistema as \"contacorrentecaixa.nomeApresentacaoSistema\", contacorrentecaixa.digito AS \"ContaCorrenteCaixa.digito\", contacorrentecaixa.carteira AS \"ContaCorrenteCaixa.carteira\",  ");
		sql.append("       negociacaorecebimento.contacorrentecaixa, contacorrentecaixa.numero AS \"ContaCorrenteCaixa.numero\", negociacaorecebimento.data::Date, negociacaorecebimento.valortotal, negociacaorecebimento.observacao, negociacaorecebimento.recebimentoBoletoAutomatico,  ");
		sql.append("       fornecedor.nome as \"fornecedor.nome\", fornecedor.codigo as \"fornecedor.codigo\", negociacaorecebimento.dataRegistro, ");
		sql.append(" negociacaorecebimento.pagamentocomdcc ");
		sql.append(" FROM negociacaorecebimento ");
		sql.append(" LEFT JOIN usuario ON (usuario.codigo = negociacaorecebimento.responsavel) ");
		sql.append(" LEFT JOIN contacorrente as contacorrentecaixa ON (contacorrentecaixa.codigo = negociacaorecebimento.contacorrentecaixa) ");
		sql.append(" LEFT JOIN agencia as agenciacontacorrentecaixa ON (agenciacontacorrentecaixa.codigo = contacorrentecaixa.agencia) ");
		sql.append(" LEFT JOIN banco as bancocontacorrentecaixa ON (bancocontacorrentecaixa.codigo = agenciacontacorrentecaixa.banco) ");
		sql.append(" LEFT JOIN unidadeensino ON (unidadeensino.codigo = negociacaorecebimento.unidadeensino) ");
		sql.append(" LEFT JOIN pessoa ON (pessoa.codigo = negociacaorecebimento.pessoa) ");
		sql.append(" LEFT JOIN parceiro ON (parceiro.codigo = negociacaorecebimento.parceiro) ");
		sql.append(" LEFT JOIN fornecedor ON (fornecedor.codigo = negociacaorecebimento.fornecedor) ");
		return sql;
	}

	private StringBuffer getSQLPadraoConsultaBasicaComContaReceber() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct negociacaorecebimento.codigo as codigo, negociacaorecebimento.tipopessoa, negociacaorecebimento.pessoa, pessoa.nome AS \"Pessoa.nome\", pessoa.cpf AS \"Pessoa.cpf\", negociacaorecebimento.responsavel, parceiro.codigo as \"Parceiro.codigo\", parceiro.nome as \"Parceiro.nome\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.cnpj AS \"Parceiro.cnpj\",");
		sql.append("       usuario.nome AS \"Responsavel.nome\", negociacaorecebimento.matricula as matricula, bancocontacorrentecaixa.codigo AS \"BancoContaCorrenteCaixa.codigo\", bancocontacorrentecaixa.nome AS \"BancoContaCorrenteCaixa.nome\",");
		sql.append("       agenciacontacorrentecaixa.codigo AS \"AgenciaContaCorrenteCaixa.codigo\", agenciacontacorrentecaixa.numeroAgencia AS \"AgenciaContaCorrenteCaixa.numeroAgencia\", agenciacontacorrentecaixa.digito AS \"AgenciaContaCorrenteCaixa.digito\", contacorrentecaixa.nomeApresentacaoSistema as \"contacorrentecaixa.nomeApresentacaoSistema\", contacorrentecaixa.digito AS \"ContaCorrenteCaixa.digito\", contacorrentecaixa.carteira AS \"ContaCorrenteCaixa.carteira\",  ");
		sql.append("       negociacaorecebimento.contacorrentecaixa, contacorrentecaixa.numero AS \"ContaCorrenteCaixa.numero\", negociacaorecebimento.data, negociacaorecebimento.valortotal, negociacaorecebimento.observacao,  negociacaorecebimento.recebimentoBoletoAutomatico, ");
		sql.append("       fornecedor.nome as \"fornecedor.nome\", fornecedor.codigo as \"fornecedor.codigo\", pagamentocomdcc as pagamentocomdcc, negociacaorecebimento.dataRegistro  ");
		sql.append("FROM negociacaorecebimento ");
		sql.append("LEFT JOIN usuario ON (usuario.codigo = negociacaorecebimento.responsavel) ");
		sql.append("LEFT JOIN contacorrente as contacorrentecaixa ON (contacorrentecaixa.codigo = negociacaorecebimento.contacorrentecaixa) ");
		sql.append("LEFT JOIN agencia as agenciacontacorrentecaixa ON (agenciacontacorrentecaixa.codigo = contacorrentecaixa.agencia) ");
		sql.append("LEFT JOIN banco as bancocontacorrentecaixa ON (bancocontacorrentecaixa.codigo = agenciacontacorrentecaixa.banco) ");
		sql.append("LEFT JOIN unidadeensino ON (unidadeensino.codigo = negociacaorecebimento.unidadeensino) ");
		sql.append("LEFT JOIN pessoa ON (pessoa.codigo = negociacaorecebimento.pessoa) ");
		sql.append("LEFT JOIN parceiro ON (parceiro.codigo = negociacaorecebimento.parceiro) ");
		sql.append("LEFT JOIN contaReceberNegociacaoRecebimento ON (contaReceberNegociacaoRecebimento.negociacaorecebimento = negociacaorecebimento.codigo) ");
		sql.append("LEFT JOIN contaReceber ON (contaReceber.codigo = contaReceberNegociacaoRecebimento.contaReceber) ");
		sql.append("LEFT JOIN fornecedor ON (fornecedor.codigo = negociacaorecebimento.fornecedor) ");
		return sql;
	}
	
	private StringBuffer getSQLPadraoConsultaBasicaDCCComContaReceber() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct negociacaorecebimentodcc.codigo as codigo, negociacaorecebimentodcc.tipopessoa, negociacaorecebimentodcc.pessoa, pessoa.nome AS \"Pessoa.nome\", pessoa.cpf AS \"Pessoa.cpf\", negociacaorecebimentodcc.responsavel, parceiro.codigo as \"Parceiro.codigo\", parceiro.nome as \"Parceiro.nome\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.cnpj AS \"Parceiro.cnpj\",");
		sql.append("       usuario.nome AS \"Responsavel.nome\", negociacaorecebimentodcc.matricula as matricula, bancocontacorrentecaixa.codigo AS \"BancoContaCorrenteCaixa.codigo\", bancocontacorrentecaixa.nome AS \"BancoContaCorrenteCaixa.nome\",");
		sql.append("       agenciacontacorrentecaixa.codigo AS \"AgenciaContaCorrenteCaixa.codigo\", agenciacontacorrentecaixa.numeroAgencia AS \"AgenciaContaCorrenteCaixa.numeroAgencia\", agenciacontacorrentecaixa.digito AS \"AgenciaContaCorrenteCaixa.digito\", contacorrentecaixa.nomeApresentacaoSistema as \"contacorrentecaixa.nomeApresentacaoSistema\", contacorrentecaixa.digito AS \"ContaCorrenteCaixa.digito\", contacorrentecaixa.carteira AS \"ContaCorrenteCaixa.carteira\",  ");
		sql.append("       negociacaorecebimentodcc.contacorrentecaixa, contacorrentecaixa.numero AS \"ContaCorrenteCaixa.numero\", negociacaorecebimentodcc.data, negociacaorecebimentodcc.valortotal, negociacaorecebimentodcc.observacao,  false as recebimentoBoletoAutomatico, ");
		sql.append("       fornecedor.nome as \"fornecedor.nome\", fornecedor.codigo as \"fornecedor.codigo\", negociacaorecebimentodcc.pagamentocomdcc, negociacaorecebimentodcc.dataregistro ");
		sql.append("FROM negociacaorecebimentodcc ");
		sql.append("LEFT JOIN usuario ON (usuario.codigo = negociacaorecebimentodcc.responsavel) ");
		sql.append("LEFT JOIN contacorrente as contacorrentecaixa ON (contacorrentecaixa.codigo = negociacaorecebimentodcc.contacorrentecaixa) ");
		sql.append("LEFT JOIN agencia as agenciacontacorrentecaixa ON (agenciacontacorrentecaixa.codigo = contacorrentecaixa.agencia) ");
		sql.append("LEFT JOIN banco as bancocontacorrentecaixa ON (bancocontacorrentecaixa.codigo = agenciacontacorrentecaixa.banco) ");
		sql.append("LEFT JOIN unidadeensino ON (unidadeensino.codigo = negociacaorecebimentodcc.unidadeensino) ");
		sql.append("LEFT JOIN pessoa ON (pessoa.codigo = negociacaorecebimentodcc.pessoa) ");
		sql.append("LEFT JOIN parceiro ON (parceiro.codigo = negociacaorecebimentodcc.parceiro) ");
		sql.append("LEFT JOIN contaReceberNegociacaoRecebimentodcc ON (contaReceberNegociacaoRecebimentodcc.negociacaorecebimentodcc = negociacaorecebimentodcc.codigo) ");
		sql.append("LEFT JOIN contaReceber ON (contaReceber.codigo = contaReceberNegociacaoRecebimentodcc.contaReceber) ");
		sql.append("LEFT JOIN fornecedor ON (fornecedor.codigo = negociacaorecebimentodcc.fornecedor) ");		
		return sql;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT negociacaorecebimento.*, parceiro.codigo as \"Parceiro.codigo\", parceiro.nome as \"Parceiro.nome\", parceiro.cpf AS \"Parceiro.cpf\", parceiro.cnpj AS \"Parceiro.cnpj\", pessoa.nome AS \"Pessoa.nome\", pessoa.cpf AS \"Pessoa.cpf\", usuario.nome AS \"Responsavel.nome\", contacorrentecaixa.numero AS \"ContaCorrenteCaixa.numero\",  contacorrentecaixa.funcionarioResponsavel AS \"ContaCorrenteCaixa.funcionarioResponsavel\",");
		sql.append("       contacorrentecaixa.contacaixa AS \"contacorrentecaixa.contacaixa\", bancocontacorrentecaixa.codigo AS \"BancoContaCorrenteCaixa.codigo\", bancocontacorrentecaixa.nome AS \"BancoContaCorrenteCaixa.nome\",");
		sql.append("       agenciacontacorrentecaixa.codigo AS \"AgenciaContaCorrenteCaixa.codigo\", agenciacontacorrentecaixa.numeroAgencia AS \"AgenciaContaCorrenteCaixa.numeroAgencia\", agenciacontacorrentecaixa.digito AS \"AgenciaContaCorrenteCaixa.digito\", contacorrentecaixa.nomeApresentacaoSistema as \"contacorrentecaixa.nomeApresentacaoSistema\", contacorrentecaixa.digito AS \"ContaCorrenteCaixa.digito\", contacorrentecaixa.carteira AS \"ContaCorrenteCaixa.carteira\", ");
		sql.append("       contareceber.codigo AS \"ContaReceber.codigo\", contareceber.nrdocumento AS \"ContaReceber.nrdocumento\", contareceber.datavencimento AS \"ContaReceber.datavencimento\",");
		sql.append("       contareceber.tipoorigem AS \"ContaReceber.tipoorigem\", contareceber.valor AS \"ContaReceber.valor\", contareceber.valordesconto AS \"ContaReceber.valordesconto\",");
		sql.append("       contareceber.juroporcentagem AS \"ContaReceber.juroporcentagem\", contareceber.juro AS \"ContaReceber.juro\", contareceber.multaporcentagem AS \"ContaReceber.multaporcentagem\",");
		sql.append("       contareceber.multa AS \"ContaReceber.multa\", contarecebernegociacaorecebimento.codigo AS \"ContaReceberNegociacaoRecebimento.codigo\", contarecebernegociacaorecebimento.codigo AS \"ContaReceberNegociacaoRecebimento.codigo\",");
		sql.append("       contarecebernegociacaorecebimento.valortotal AS \"ContaReceberNegociacaoRecebimento.valortotal\", contarecebernegociacaorecebimento.negociacaorecebimento AS \"ContaReceberNegociacaoRecebimento.negociacaorecebimento\", contarecebernegociacaorecebimento.contaReceberTerceiro AS \"contarecebernegociacaorecebimento.contaReceberTerceiro\", ");
		sql.append("       contareceber.valordescontoprogressivo AS \"ContaReceber.valordescontoprogressivo\", contareceber.descontoconvenio AS \"ContaReceber.descontoconvenio\", contareceber.descontoinstituicao AS \"ContaReceber.descontoinstituicao\",");
		sql.append("       formapagamentonegociacaorecebimento.codigo AS \"FormaPagamentoNegociacaoRecebimento.codigo\", formapagamentonegociacaorecebimento.cheque AS \"FormaPagamentoNegociacaoRecebimento.cheque\", formapagamento.codigo AS \"FormaPagamento.codigo\",");

		sql.append("	   formapagamentonegociacaorecebimentocartaocredito.codigo  AS \"formapagamentocartaocredito.codigo\", formapagamentonegociacaorecebimentocartaocredito.numeroparcela AS \"formapagamentocartaocredito.numeroparcela\", ");
		sql.append("	   formapagamentonegociacaorecebimentocartaocredito.valorparcela AS \"formapagamentocartaocredito.valorparcela\", ");
		sql.append("	   formapagamentonegociacaorecebimentocartaocredito.dataemissao AS \"formapagamentocartaocredito.dataemissao\", formapagamentonegociacaorecebimentocartaocredito.datavencimento AS \"formapagamentocartaocredito.datavencimento\", ");
		sql.append("	   formapagamentonegociacaorecebimentocartaocredito.datarecebimento AS \"formapagamentocartaocredito.datarecebimento\", formapagamentonegociacaorecebimentocartaocredito.situacao AS \"formapagamentocartaocredito.situacao\", ");
		sql.append("	   formapagamentonegociacaorecebimentocartaocredito.responsavelpelabaixa AS \"formapagamentocartaocredito.responsavelpelabaixa\", ");

		sql.append("       formapagamento.nome AS \"FormaPagamento.nome\", formapagamento.tipo AS \"FormaPagamento.tipo\", formapagamentonegociacaorecebimento.contacorrente AS \"FormaPagamentoNegociacaoRecebimento.contacorrente\", formapagamentonegociacaorecebimento.valorrecebimento AS \"FormaPagamentoNegociacaoRecebimento.valorrecebimento\",");
		sql.append("       bancocontacorrenteformapagamento.codigo AS \"BancoContaCorrenteFormaPagamento.codigo\", bancocontacorrenteformapagamento.nome AS \"BancoContaCorrenteFormaPagamento.nome\", agenciacontacorrenteformapagamento.codigo AS \"AgenciaContaCorrenteFormaPagamento.codigo\",");
		sql.append("       agenciacontacorrenteformapagamento.numeroAgencia AS \"AgenciaContaCorrenteFormaPagamento.numeroAgencia\", agenciacontacorrenteformapagamento.digito AS \"AgenciaContaCorrenteFormaPagamento.digito\", contacorrenteformapagamento.numero AS \"ContaCorrenteFormaPagamento.numero\",");
		sql.append("       contacorrenteformapagamento.contacaixa AS \"ContaCorrenteFormaPagamento.contacaixa\", contareceber.situacao as \"ContaReceber.situacao\", ");
		sql.append("       cheque.banco AS \"cheque.banco\", cheque.agencia as \"cheque.agencia\", cheque.numeroContaCorrente AS \"cheque.numeroContaCorrente\", cheque.numero AS \"cheque.numero\", ");
		sql.append("       pessoaCheque.nome AS \"pessoaCheque.nome\", pessoaCheque.cpf AS \"pessoaCheque.cpf\", ");
		sql.append("       fornecedor.nome as \"fornecedor.nome\", fornecedor.codigo as \"fornecedor.codigo\", ");
		sql.append("       operadoracartao.nome as \"operadoracartao.nome\", operadoracartao.codigo as \"operadoracartao.codigo\", contacorrentecaixa.tipocontacorrente as \"contacorrentecaixa.tipocontacorrente\" ");
		sql.append("FROM negociacaorecebimento ");
		sql.append("LEFT JOIN usuario ON (usuario.codigo = negociacaorecebimento.responsavel) ");
		sql.append("LEFT JOIN contacorrente AS contacorrentecaixa ON (contacorrentecaixa.codigo = negociacaorecebimento.contacorrentecaixa) ");
		sql.append("LEFT JOIN agencia AS agenciacontacorrentecaixa ON (agenciacontacorrentecaixa.codigo = contacorrentecaixa.agencia) ");
		sql.append("LEFT JOIN banco AS bancocontacorrentecaixa ON (bancocontacorrentecaixa.codigo = agenciacontacorrentecaixa.banco) ");
		sql.append("LEFT JOIN unidadeensino ON (unidadeensino.codigo = negociacaorecebimento.unidadeensino) ");
		sql.append("LEFT JOIN pessoa ON (pessoa.codigo = negociacaorecebimento.pessoa) ");
		sql.append("LEFT JOIN parceiro ON (parceiro.codigo = negociacaorecebimento.parceiro) ");
		sql.append("LEFT JOIN contarecebernegociacaorecebimento ON (contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) ");
		sql.append("LEFT JOIN contareceber ON (contareceber.codigo = contarecebernegociacaorecebimento.contareceber) ");
		sql.append("LEFT JOIN formapagamentonegociacaorecebimento ON (formapagamentonegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) ");
		sql.append("LEFT JOIN operadoracartao ON (operadoracartao.codigo = formapagamentonegociacaorecebimento.operadoracartao) ");
		sql.append("LEFT JOIN formapagamentonegociacaorecebimentocartaocredito ON formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito ");
		sql.append("LEFT JOIN formapagamento ON (formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento) ");
		sql.append("LEFT JOIN contacorrente AS contacorrenteformapagamento ON (contacorrenteformapagamento.codigo = formapagamentonegociacaorecebimento.contacorrente) ");
		sql.append("LEFT JOIN agencia AS agenciacontacorrenteformapagamento ON (agenciacontacorrenteformapagamento.codigo = contacorrenteformapagamento.agencia) ");
		sql.append("LEFT JOIN banco AS bancocontacorrenteformapagamento ON (bancocontacorrenteformapagamento.codigo = agenciacontacorrenteformapagamento.banco) ");
		sql.append("LEFT JOIN cheque ON (cheque.codigo = formapagamentonegociacaorecebimento.cheque) ");
		sql.append("LEFT JOIN pessoa AS pessoaCheque ON (pessoaCheque.codigo = cheque.pessoa) ");
		sql.append("LEFT JOIN fornecedor ON (fornecedor.codigo = negociacaorecebimento.fornecedor) ");
		return sql;
	}

	@Override
	public NegociacaoRecebimentoVO carregarDados(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if(obj.getPagamentoComDCC()) {
			return getFacadeFactory().getNegociacaoRecebimentoDCCFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuario);
		} else {
			return carregarDados(obj, NivelMontarDados.TODOS, usuario);			
		}
	}

	public NegociacaoRecebimentoVO carregarDados(NegociacaoRecebimentoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			return montarDadosBasico(consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), Boolean.FALSE, usuario));
		} else if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			return montarDadosCompleto(consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), Boolean.FALSE, usuario));
		}
		return new NegociacaoRecebimentoVO();
	}

	private NegociacaoRecebimentoVO montarDadosBasico(SqlRowSet dadosSQL) {
		NegociacaoRecebimentoVO obj = new NegociacaoRecebimentoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setTipoPessoa(dadosSQL.getString("tipopessoa"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getPessoa().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getPessoa().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getFornecedor().setCodigo(dadosSQL.getInt("Fornecedor.codigo"));
		obj.getFornecedor().setNome(dadosSQL.getString("Fornecedor.nome"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("Parceiro.codigo"));
		obj.getParceiroVO().setNome(dadosSQL.getString("Parceiro.nome"));
		obj.getParceiroVO().setCNPJ(dadosSQL.getString("Parceiro.cnpj"));
		obj.getParceiroVO().setCPF(dadosSQL.getString("Parceiro.cpf"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getResponsavel().setNome(dadosSQL.getString("Responsavel.nome"));
		obj.getContaCorrenteCaixa().getAgencia().getBanco().setCodigo(dadosSQL.getInt("BancoContaCorrenteCaixa.codigo"));
		obj.getContaCorrenteCaixa().getAgencia().getBanco().setNome(dadosSQL.getString("BancoContaCorrenteCaixa.nome"));
		obj.getContaCorrenteCaixa().getAgencia().setCodigo(dadosSQL.getInt("AgenciaContaCorrenteCaixa.codigo"));
		obj.getContaCorrenteCaixa().getAgencia().setNumeroAgencia(dadosSQL.getString("AgenciaContaCorrenteCaixa.numeroAgencia"));
		obj.getContaCorrenteCaixa().getAgencia().setDigito(dadosSQL.getString("AgenciaContaCorrenteCaixa.digito"));
		obj.getContaCorrenteCaixa().setCodigo(dadosSQL.getInt("contacorrentecaixa"));
		obj.getContaCorrenteCaixa().setNumero(dadosSQL.getString("ContaCorrenteCaixa.numero"));
		obj.getContaCorrenteCaixa().setNumero(dadosSQL.getString("ContaCorrenteCaixa.nomeApresentacaoSistema"));
		obj.getContaCorrenteCaixa().setDigito(dadosSQL.getString("ContaCorrenteCaixa.digito"));
		obj.getContaCorrenteCaixa().setCarteira(dadosSQL.getString("ContaCorrenteCaixa.carteira"));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setDataRegistro(dadosSQL.getTimestamp("dataRegistro"));
		obj.setValorTotal(dadosSQL.getDouble("valortotal"));		
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setRecebimentoBoletoAutomatico(dadosSQL.getBoolean("recebimentoBoletoAutomatico"));
		obj.setPagamentoComDCC(dadosSQL.getBoolean("pagamentocomdcc"));
		return obj;
	}

	private NegociacaoRecebimentoVO montarDadosCompleto(SqlRowSet dadosSQL) throws Exception {
		NegociacaoRecebimentoVO obj = new NegociacaoRecebimentoVO();
		List<ContaReceberNegociacaoRecebimentoVO> listaContaReceberNegociacaoRecebimento = new ArrayList<ContaReceberNegociacaoRecebimentoVO>(0);
		List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimento = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
		try {
			if (dadosSQL.next()) {
				obj = montarDadosBasico(dadosSQL);
				obj.getContaCorrenteCaixa().setContaCaixa(dadosSQL.getBoolean("contacorrentecaixa.contacaixa"));
				obj.getContaCorrenteCaixa().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contacorrentecaixa.tipocontacorrente")));
				obj.getContaCorrenteCaixa().getFuncionarioResponsavel().setCodigo(dadosSQL.getInt("contacorrentecaixa.funcionarioResponsavel"));
				obj.setValorTotalRecebimento(new Double(dadosSQL.getDouble("valortotalrecebimento")));
				obj.setValorTroco(new Double(dadosSQL.getDouble("valortroco")));
				obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeensino")));
				// obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
				// obj.getParceiroVO().setNome(obj.getPessoa().getNome());
				obj.setValorTrocoAlteracao(obj.getValorTroco());
				// Monta dados de entidades vinculadas ou subordinadas.
				// HASH PARA TRATAR 2 LISTAS DE UM MESMO OBJETO
				// TODO ALBERTO 07/12/10 corrigido erro (duplicando contas) ao
				// editar negociacaorecebimento
				Hashtable<Integer, ContaReceberNegociacaoRecebimentoVO> hashtableContaReceberNegociacaoRecebimento = new Hashtable<Integer, ContaReceberNegociacaoRecebimentoVO>(0);
				Hashtable<Integer, FormaPagamentoNegociacaoRecebimentoVO> hashtableFormaPagamentoNegociacaoRecebimento = new Hashtable<Integer, FormaPagamentoNegociacaoRecebimentoVO>(0);
				// TODO ALBERTO 07/12/10 corrigido erro (duplicando contas) ao
				// editar negociacaorecebimento
				do {
					// Conta Receber.
					if (dadosSQL.getInt("ContaReceber.codigo") != 0) {
						ContaReceberVO contaReceber = new ContaReceberVO();
						contaReceber.setCodigo(dadosSQL.getInt("ContaReceber.codigo"));
						contaReceber.setNrDocumento(dadosSQL.getString("ContaReceber.nrdocumento"));
						contaReceber.setDataVencimento(dadosSQL.getDate("ContaReceber.datavencimento"));
						contaReceber.setTipoOrigem(dadosSQL.getString("ContaReceber.tipoorigem"));
						contaReceber.setValor(dadosSQL.getDouble("ContaReceber.valor"));
						contaReceber.setValorDescontoProgressivo(dadosSQL.getDouble("ContaReceber.valordescontoprogressivo"));
						contaReceber.setValorDescontoConvenio(dadosSQL.getDouble("ContaReceber.descontoconvenio"));
						contaReceber.setValorDescontoInstituicao(dadosSQL.getDouble("ContaReceber.descontoinstituicao"));
						contaReceber.setJuroPorcentagem(dadosSQL.getDouble("ContaReceber.juroporcentagem"));
						contaReceber.setJuro(dadosSQL.getDouble("ContaReceber.juro"));
						contaReceber.setMultaPorcentagem(dadosSQL.getDouble("ContaReceber.multaporcentagem"));
						contaReceber.setMulta(dadosSQL.getDouble("ContaReceber.multa"));
						contaReceber.setValorDesconto(dadosSQL.getDouble("ContaReceber.valordesconto"));
						contaReceber.setValor(dadosSQL.getDouble("ContaReceber.valor"));
						contaReceber.setSituacao(dadosSQL.getString("ContaReceber.situacao"));
						obj.getFornecedor().setCodigo(dadosSQL.getInt("Fornecedor.codigo"));
						obj.getFornecedor().setNome(dadosSQL.getString("Fornecedor.nome"));
						// Conta Receber Negociação Recebimento.
						ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimento = new ContaReceberNegociacaoRecebimentoVO();
						contaReceberNegociacaoRecebimento.setCodigo(dadosSQL.getInt("ContaReceberNegociacaoRecebimento.codigo"));
						contaReceberNegociacaoRecebimento.setContaReceber(contaReceber);
						contaReceberNegociacaoRecebimento.setValorTotal(dadosSQL.getDouble("ContaReceberNegociacaoRecebimento.valortotal"));
						contaReceberNegociacaoRecebimento.setNegociacaoRecebimento(dadosSQL.getInt("ContaReceberNegociacaoRecebimento.negociacaorecebimento"));
						contaReceberNegociacaoRecebimento.setContaReceberTerceiro(dadosSQL.getBoolean("contarecebernegociacaorecebimento.contaReceberTerceiro"));
						// TODO ALBERTO 07/12/10 corrigido erro (duplicando
						// contas) ao editar negociacaorecebimento
						if (!hashtableContaReceberNegociacaoRecebimento.containsKey(contaReceberNegociacaoRecebimento.getCodigo()) && contaReceberNegociacaoRecebimento.getCodigo() != 0) {
							listaContaReceberNegociacaoRecebimento.add(contaReceberNegociacaoRecebimento);
						}
						hashtableContaReceberNegociacaoRecebimento.put(contaReceberNegociacaoRecebimento.getCodigo(), contaReceberNegociacaoRecebimento);
						// listaContaReceberNegociacaoRecebimento.add(contaReceberNegociacaoRecebimento);
						// TODO ALBERTO 07/12/10 corrigido erro (duplicando
						// contas) ao editar negociacaorecebimento
					}

					// Forma Pagamento Recebimento.
					if (dadosSQL.getInt("FormaPagamentoNegociacaoRecebimento.codigo") != 0) {
						FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimento = new FormaPagamentoNegociacaoRecebimentoVO();
						formaPagamentoNegociacaoRecebimento.setCodigo(dadosSQL.getInt("FormaPagamentoNegociacaoRecebimento.codigo"));
						formaPagamentoNegociacaoRecebimento = getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().consultarPorChavePrimaria(formaPagamentoNegociacaoRecebimento.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
//						formaPagamentoNegociacaoRecebimento.getCheque().setCodigo(dadosSQL.getInt("FormaPagamentoNegociacaoRecebimento.cheque"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamento().setCodigo(dadosSQL.getInt("FormaPagamento.codigo"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamento().setNome(dadosSQL.getString("FormaPagamento.nome"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamento().setTipo(dadosSQL.getString("FormaPagamento.tipo"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().getAgencia().getBanco().setCodigo(dadosSQL.getInt("BancoContaCorrenteFormaPagamento.codigo"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().getAgencia().getBanco().setNome(dadosSQL.getString("BancoContaCorrenteFormaPagamento.nome"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().getAgencia().setCodigo(dadosSQL.getInt("AgenciaContaCorrenteFormaPagamento.codigo"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().getAgencia().setNumeroAgencia(dadosSQL.getString("AgenciaContaCorrenteFormaPagamento.numeroAgencia"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().getAgencia().setDigito(dadosSQL.getString("AgenciaContaCorrenteFormaPagamento.digito"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().setCodigo(dadosSQL.getInt("FormaPagamentoNegociacaoRecebimento.contacorrente"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().setNumero(dadosSQL.getString("ContaCorrenteFormaPagamento.numero"));
//						formaPagamentoNegociacaoRecebimento.getContaCorrente().setContaCaixa(dadosSQL.getBoolean("ContaCorrenteFormaPagamento.contacaixa"));
//						formaPagamentoNegociacaoRecebimento.setValorRecebimento(dadosSQL.getDouble("FormaPagamentoNegociacaoRecebimento.valorrecebimento"));
//						formaPagamentoNegociacaoRecebimento.getCheque().setBanco(dadosSQL.getString("cheque.banco"));
//						formaPagamentoNegociacaoRecebimento.getCheque().setAgencia(dadosSQL.getString("cheque.agencia"));
//						formaPagamentoNegociacaoRecebimento.getCheque().setNumeroContaCorrente(dadosSQL.getString("cheque.numeroContaCorrente"));
//						formaPagamentoNegociacaoRecebimento.getCheque().setNumero(dadosSQL.getString("cheque.numero"));
//						formaPagamentoNegociacaoRecebimento.getCheque().getPessoa().setNome(dadosSQL.getString("pessoaCheque.nome"));
//						formaPagamentoNegociacaoRecebimento.getCheque().getPessoa().setCPF(dadosSQL.getString("pessoaCheque.cpf"));
//						formaPagamentoNegociacaoRecebimento.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoracartao.codigo"));
//						if(Uteis.isAtributoPreenchido(formaPagamentoNegociacaoRecebimento.getOperadoraCartaoVO().getCodigo())) {
//							formaPagamentoNegociacaoRecebimento.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(formaPagamentoNegociacaoRecebimento.getOperadoraCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
//						}
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(dadosSQL.getInt("formapagamentocartaocredito.codigo"));
//						if(Uteis.isAtributoPreenchido(formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo())) {
//							formaPagamentoNegociacaoRecebimento.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().consultarPorChavePrimaria(formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));							
//						}
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroParcela(dadosSQL.getString("formapagamentocartaocredito.numeroparcela"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(dadosSQL.getDouble("formapagamentocartaocredito.valorparcela"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataEmissao(dadosSQL.getDate("formapagamentocartaocredito.dataemissao"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataVencimento(dadosSQL.getDate("formapagamentocartaocredito.datavencimento"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(dadosSQL.getDate("formapagamentocartaocredito.datarecebimento"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(dadosSQL.getString("formapagamentocartaocredito.situacao"));
//						formaPagamentoNegociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelPelaBaixa().setCodigo(dadosSQL.getInt("formapagamentocartaocredito.responsavelpelabaixa"));
						// TODO ALBERTO 07/12/10 corrigido erro (duplicando
						// contas) ao editar negociacaorecebimento
						if (!hashtableFormaPagamentoNegociacaoRecebimento.containsKey(formaPagamentoNegociacaoRecebimento.getCodigo()) && formaPagamentoNegociacaoRecebimento.getCodigo() != 0) {
							listaFormaPagamentoNegociacaoRecebimento.add(formaPagamentoNegociacaoRecebimento);
						}
						hashtableFormaPagamentoNegociacaoRecebimento.put(formaPagamentoNegociacaoRecebimento.getCodigo(), formaPagamentoNegociacaoRecebimento);
						// TODO ALBERTO 07/12/10 corrigido erro (duplicando
						// contas) ao editar negociacaorecebimento
						// listaFormaPagamentoNegociacaoRecebimento.add(formaPagamentoNegociacaoRecebimento);
					}
				} while (dadosSQL.next());
				// Adiciona lista ao objeto correspondente.
				if (!listaContaReceberNegociacaoRecebimento.isEmpty()) {
					obj.setContaReceberNegociacaoRecebimentoVOs(listaContaReceberNegociacaoRecebimento);
				}
				if (!listaFormaPagamentoNegociacaoRecebimento.isEmpty()) {
					obj.setFormaPagamentoNegociacaoRecebimentoVOs(listaFormaPagamentoNegociacaoRecebimento);
				}
			}
			obj.setNovoObj(false);
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<NegociacaoRecebimentoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<NegociacaoRecebimentoVO> vetResultado = new ArrayList<NegociacaoRecebimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasico(tabelaResultado));
		}
		return vetResultado;
	}

	public List<NegociacaoRecebimentoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
		List<NegociacaoRecebimentoVO> vetResultado = new ArrayList<NegociacaoRecebimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosCompleto(tabelaResultado));
		}
		return vetResultado;
	}

	public List<NegociacaoRecebimentoVO> consultaRapidaPorNossoNumero(String valorConsulta, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasicaComContaReceber();
		sql.append(" WHERE (contaReceber.nossoNumero = '" + valorConsulta + "' ) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and (negociacaorecebimento.unidadeensino = " + unidadeEnsino + ") ");
		}
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("'");
		}
		sql.append(" union all ");
		sql.append(getSQLPadraoConsultaBasicaDCCComContaReceber());
		sql.append(" WHERE (contaReceber.nossoNumero = '" + valorConsulta + "' ) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and (negociacaorecebimentodcc.unidadeensino = " + unidadeEnsino + ") ");
		}
		if (!tipoOrigem.equals("")) {
			sql.append(" and contareceber.tipoorigem = '").append(tipoOrigem).append("' ");
		}
		sql.append("ORDER BY \"Pessoa.nome\" ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public Boolean verificarSeChequeFoiMovimentadoParaOutraContaCaixa(Integer codigoNegociacao) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT c.localizacaocheque <> nr.contacorrentecaixa as resultado FROM negociacaorecebimento nr ");
		sqlStr.append("INNER JOIN formapagamentonegociacaorecebimento fpnr ON nr.codigo = fpnr.negociacaorecebimento ");
		sqlStr.append("INNER JOIN cheque c ON fpnr.cheque = c.codigo ");
		sqlStr.append("INNER JOIN contacorrente cc ON c.localizacaocheque = cc.codigo ");
		sqlStr.append("WHERE nr.codigo = " + codigoNegociacao + " AND c.situacao <> 'BA' ");
		sqlStr.append("GROUP BY resultado ORDER BY resultado DESC LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return false;
		}
		return tabelaResultado.getBoolean("resultado");
	}

	public Integer consultarCodigoUnidadeEnsinoPelaFormaPagamentoNegociacaoRecebimentoCartaoCredito(Integer codigoFormaPagamentoNegociacaoRecebimentoCartaoCredito) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select distinct nr.unidadeensino from negociacaorecebimento nr ");
		sqlStr.append("inner join formapagamentonegociacaorecebimento fpnr on nr.codigo = fpnr.negociacaorecebimento ");
		/**
		 * Este join deve ser matido para atender o modelo antigo do
		 * relacionamento onde a forma de pagamento negociacao recebimento tinha
		 * uma lista de FormaPagamentoNegociacaoRecebimentoCartaoCredito hoje
		 * este relacionamento foi alterado 1 para 1
		 */
		sqlStr.append("inner join formapagamentonegociacaorecebimentocartaocredito fpnrcc on (fpnr.formapagamentonegociacaorecebimentocartaocredito = fpnrcc.codigo ");
		sqlStr.append(" or fpnr.codigo = fpnrcc.formapagamentonegociacaorecebimento) ");
		sqlStr.append("where fpnrcc.codigo = " + codigoFormaPagamentoNegociacaoRecebimentoCartaoCredito);
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			tabelaResultado.next();
			return tabelaResultado.getInt("unidadeensino");
		} catch (Exception e) {
			return 0;
		} finally {
			sqlStr = null;
		}
	}

	public Integer consultarNumeroDeRecebimentosParaUmaMatricula(String matricula) throws Exception {
		String sqlStr = "SELECT count(codigo) FROM negociacaorecebimento WHERE matricula = ?";
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { matricula });
			if (!tabelaResultado.next()) {
				return 0;
			} else {
				return tabelaResultado.getInt("count");
			}
		} finally {
			sqlStr = null;
		}
	}

	public void calcularValorTrocoDeAcordoFormaPagamento(NegociacaoRecebimentoVO obj) throws Exception {
		Double valorOutros = 0.0;
		Boolean formaPagamentoDinheiro = false;
		try {
			obj.setValorTroco(0.0);
			if (Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotalRecebimento()) > Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotal())) {
				for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimento : obj.getFormaPagamentoNegociacaoRecebimentoVOs()) {
					if (!formaPagamentoNegociacaoRecebimento.getFormaPagamento().getTipo().equals("DI")) {
						valorOutros += (Uteis.arredondar(formaPagamentoNegociacaoRecebimento.getValorRecebimento(), 2, 0));
					} else {
						formaPagamentoDinheiro = true;
					}
				}
				if (Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotal()) > Uteis.arrendondarForcando2CadasDecimais(valorOutros)) {
					obj.setValorTroco(Uteis.arredondar(obj.getValorTotalRecebimento() - obj.getValorTotal(), 2, 0));
				} else {
					obj.setValorTroco(0.0);
					if (formaPagamentoDinheiro) {
						throw new Exception("Não é necessária a forma de pagamento em dinheiro para quitar a conta.");
					}
				}
			} else {
				obj.setValorTroco(0.0);
			}
			valorOutros = null;
			formaPagamentoDinheiro = null;
		} catch (Exception e) {
			valorOutros = null;
			formaPagamentoDinheiro = null;
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Date consultaDataRecebimentoPorContaReceberUnica(Integer contaReceber,  UsuarioVO usuario) throws Exception {
		String sql = "";
		sql = "select distinct negociacaorecebimento.data from negociacaorecebimento " 
		+ " inner join contarecebernegociacaorecebimento crnr on crnr.negociacaorecebimento = negociacaorecebimento.codigo "
		+ " where crnr.contareceber = " + contaReceber + "  ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return tabelaResultado.getDate("data");
	}
	
	public NegociacaoRecebimentoVO consultaRapidaPorNossoNumeroUnicaContaReceber(String nossoNumero, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		String sql = "";
		sql = "select negociacaorecebimento.* from negociacaorecebimento " + "inner join contarecebernegociacaorecebimento crnr on crnr.negociacaorecebimento = negociacaorecebimento.codigo " + "inner join contareceber on contareceber.codigo = crnr.contareceber " + "where (contareceber.nossonumero = '" + nossoNumero + "') ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NegociacaoRecebimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@SuppressWarnings("static-access")
	public List<NegociacaoRecebimentoVO> consultaRapidaPorNossoNumeroContaReceber(String nossoNumero, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		String sql = "";
		sql = "select negociacaorecebimento.* from negociacaorecebimento " + "inner join contarecebernegociacaorecebimento crnr on crnr.negociacaorecebimento = negociacaorecebimento.codigo " + "inner join contareceber on contareceber.codigo = crnr.contareceber " + "where (contareceber.nossonumero = '" + nossoNumero + "' OR " + "nossonumero = substring('" + nossoNumero + "', 8, length('" + nossoNumero + "')) OR " + "nossonumero = '0'||substring('" + nossoNumero + "', 9, length('" + nossoNumero + "'))) " + " AND negociacaorecebimento.recebimentoboletoautomatico = true ";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List<NegociacaoRecebimentoVO> consultaRapidaMatriculaNegativadaSerasaQueRealizouPagamento(Date dataPagamento, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		String sql = "";
		sql = " select distinct contarecebernegociacaorecebimento.codigo as codcontarecebernegociacaorecebimento,  matricula.unidadeensino, unidadeensino.responsavelCobrancaUnidade as codigo, matricula.unidadeensino, negociacaorecebimento.matricula, matricula.aluno as pessoa, negociacaorecebimento.data, negociacaorecebimento.valortotalrecebimento from contarecebernegociacaorecebimento   inner join contareceber on contareceber.codigo = contarecebernegociacaorecebimento.contareceber inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento inner join matricula on contareceber.matriculaaluno = matricula.matricula inner join pessoa on matricula.aluno = pessoa.codigo inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino"
				+ " where matricula.matriculaserasa = true and matricula.matriculaVerificadaSerasa = true and (contarecebernegociacaorecebimento.notificarSerasa = false or contarecebernegociacaorecebimento.notificarSerasa is null)" + " and (negociacaorecebimento.data between '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataPagamento, 0, 0, 0)) + "' and '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(new Date(), 00, 00, 00)) + "')";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsultaSerasa(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiro, usuario));
	}

	public NegociacaoRecebimentoVO consultarMatriculaPeloChequeFormaPagamentoNegociacaoRecebimento(Integer cheque) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select nr.* from formapagamentonegociacaorecebimento fpnr ");
		sqlStr.append("inner join negociacaorecebimento nr on nr.codigo = fpnr.negociacaorecebimento ");
		sqlStr.append("where fpnr.cheque = ").append(cheque).append(" limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null);
		}
		return new NegociacaoRecebimentoVO();
	}
	
	@SuppressWarnings("static-access")
	public boolean validarNegociacaoRecebimentoExistente(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT codigo FROM negociacaorecebimento  WHERE codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		return tabelaResultado.next();
	}

	public void validarDadosLogAlteracaoContaCaixaEstorno(Integer contaCaixaPadrao, Integer contaCaixaEstorno) throws Exception {
		if (contaCaixaPadrao.equals(0)) {
			throw new Exception("Não foi encontrado nenhuma conta caixa padrão na negociação recebimento.");
		}
		if (contaCaixaEstorno.equals(0)) {
			throw new Exception("Não foi encontrado nenhuma conta caixa padrão selecionada para estorno.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogAlteracaoContaCaixaEstorno(final String matricula, final Integer contaCaixaPadrao, final Integer contaCaixaEstorno, UsuarioVO usuarioVO) throws Exception {
		validarDadosLogAlteracaoContaCaixaEstorno(contaCaixaPadrao, contaCaixaEstorno);
		final String sql = "INSERT INTO logalteracaoContaCaixaEstorno( matricula, contaCaixaPadrao, contaCaixaEstorno ) VALUES ( ?, ?, ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, matricula);
				sqlInserir.setInt(2, contaCaixaPadrao);
				sqlInserir.setInt(3, contaCaixaEstorno);
				return sqlInserir;
			}
		});
	}

	@Override
	public String realizarGeracaoTextoComprovante(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ImpressoraVO impressoraVO, UsuarioVO usuario) throws Exception {
		StringBuilder texto = new StringBuilder();
		negociacaoRecebimentoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(negociacaoRecebimentoVO.getUnidadeEnsino().getCodigo(), false, usuario));
		texto.append("                                >");
		texto.append(Uteis.removerAcentos(negociacaoRecebimentoVO.getUnidadeEnsino().getNome())).append(">");
		texto.append("CEP: ").append(negociacaoRecebimentoVO.getUnidadeEnsino().getCEP()).append(" END.:").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getUnidadeEnsino().getEndereco())).append(">");
		texto.append(Uteis.removerAcentos(negociacaoRecebimentoVO.getUnidadeEnsino().getSetor())).append(" / ");
		texto.append(Uteis.removerAcentos(negociacaoRecebimentoVO.getUnidadeEnsino().getCidade().getNome())).append(">");
		texto.append("TEL.: ").append(negociacaoRecebimentoVO.getUnidadeEnsino().getTelComercial1()).append("  CNPJ: ").append(negociacaoRecebimentoVO.getUnidadeEnsino().getCNPJ()).append(">");
		texto.append("         					     >");
		texto.append("Data Pgto: ").append(negociacaoRecebimentoVO.getData_Apresentar()).append(">");
		texto.append("Atendente: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getResponsavel().getNome())).append(">");
		texto.append("Cod Pgto: ").append(negociacaoRecebimentoVO.getCodigo()).append(">");
		
		for(ContaReceberNegociacaoRecebimentoVO contaReceberNegociocaoRecebimento : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs())			
		if(contaReceberNegociocaoRecebimento.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())){		  				 
			 ChequeVO cheque = new ChequeVO();
			 cheque = getFacadeFactory().getChequeFacade().consultarChequeDevolvidoPorContaReceberCodOrigemTipoOrigem(contaReceberNegociocaoRecebimento.getContaReceber().getCodOrigem(),contaReceberNegociocaoRecebimento.getContaReceber().getTipoOrigem());                
           if(Uteis.isAtributoPreenchido(cheque)) {
        	   contaReceberNegociocaoRecebimento.setChequeDevolvido(cheque);
        	   texto.append("Dados Cheque  >");
        	   texto.append("Nº Cheque: ").append(cheque.getNumero()).append(">");
        	   texto.append("Agencia: ").append(cheque.getAgencia()).append(">");
        	   texto.append("Conta: ").append(cheque.getNumeroContaCorrente()).append(">");
        	   texto.append("Titular do Cheque: >");
        	   texto.append(UteisIreport.primeiraLetraMaiuscula(Uteis.removerAcentos(cheque.getSacado()))).append(">");
           	 
           }
			     
		}
		texto.append("         					     >");
		texto.append("Recibo de Quitacao").append(">");
		if (negociacaoRecebimentoVO.getTipoAluno()) {
			texto.append("Aluno: ").append(negociacaoRecebimentoVO.getMatricula()).append("-").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getPessoa().getNome()));
			if (!negociacaoRecebimentoVO.getMatricula().trim().isEmpty()) {

				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(negociacaoRecebimentoVO.getMatricula(), negociacaoRecebimentoVO.getUnidadeEnsino().getCodigo(), false, usuario);
				if (matriculaVO != null) {
					texto.append(">");
					texto.append("Curso: ").append(Uteis.removerAcentos(matriculaVO.getCurso().getNome()));
				}
			}
		}
		if (negociacaoRecebimentoVO.getTipoResponsavelFinanceiro()) {
			texto.append("Resp. Finan.: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getPessoa().getNome()));
			texto.append(">");
		}
		if (negociacaoRecebimentoVO.getTipoCandidato()) {
			texto.append("Cand.: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getPessoa().getNome()));
			texto.append(">");
		}
		if (negociacaoRecebimentoVO.getTipoFornecedor()) {
			texto.append("Forn.: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getFornecedor().getNome()));
			texto.append(">");
		}
		if (negociacaoRecebimentoVO.getTipoFuncionario()) {
			texto.append("Func.: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getPessoa().getNome()));
			texto.append(">");
		}
		if (negociacaoRecebimentoVO.getTipoParceiro()) {
			texto.append("Parc.: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getParceiroVO().getNome()));
			texto.append(">");
		}
		if (negociacaoRecebimentoVO.getTipoRequerente()) {
			texto.append("Req.: ").append(Uteis.removerAcentos(negociacaoRecebimentoVO.getPessoa().getNome()));
			texto.append(">");
		} 	
		
		for (ContaReceberNegociacaoRecebimentoVO crnr : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
			if(crnr.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue() >= 0.0){
				crnr.getContaReceber().setAcrescimo(crnr.getContaReceber().getAcrescimo() + crnr.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());
			}else if(crnr.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue() < 0.0){
				crnr.getContaReceber().setValorDescontoRecebido(crnr.getContaReceber().getValorDescontoRecebido() - crnr.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());	
			}			
			texto.append("Titulo: ").append(crnr.getContaReceber().getParcela()).append("  ");
			for(int x = crnr.getContaReceber().getParcela().length() + crnr.getContaReceber().getTipoOrigem_apresentar().length(); x<16;x++){
				texto.append(" ");	
			}
			texto.append(crnr.getContaReceber().getTipoOrigem_apresentar());
			texto.append(">");			
			texto.append("Vcto.: ").append(crnr.getContaReceber().getDataVencimento_Apresentar()).append("   ");			
			texto.append("Valor: ");
			for(int x = Uteis.getDoubleFormatado(crnr.getContaReceber().getValor()).length(); x<16;x++){
				texto.append(" ");	
			}
			texto.append(Uteis.getDoubleFormatado(crnr.getContaReceber().getValor())).append(">");						
			if (crnr.getContaReceber().getValorDescontoRecebido() > 0) {
				texto.append("               Desconto: ");
				for(int x = Uteis.getDoubleFormatado(crnr.getContaReceber().getValorDescontoRecebido()).length(); x<15;x++){
					texto.append(" ");	
				}
				texto.append("-").append(Uteis.getDoubleFormatado(crnr.getContaReceber().getValorDescontoRecebido()));
				texto.append(">");
			}
			if ((crnr.getContaReceber().getJuro() + crnr.getContaReceber().getMulta() + crnr.getContaReceber().getAcrescimo()) > 0) {
				texto.append("              Acrescimo: ");
				for(int x = Uteis.getDoubleFormatado((crnr.getContaReceber().getJuro() + crnr.getContaReceber().getMulta() + crnr.getContaReceber().getAcrescimo())).length(); x<16;x++){
					texto.append(" ");	
				}
				texto.append(Uteis.getDoubleFormatado((crnr.getContaReceber().getJuro() + crnr.getContaReceber().getMulta() + crnr.getContaReceber().getAcrescimo())));
				texto.append(">");
			}
			texto.append("              Sub-Total: ");
			for(int x = Uteis.getDoubleFormatado((crnr.getContaReceber().getValor() - crnr.getContaReceber().getValorDescontoRecebido() +crnr.getContaReceber().getJuro() + crnr.getContaReceber().getMulta() + crnr.getContaReceber().getAcrescimo())).length(); x<16;x++){
				texto.append(" ");	
			}			
				
			
			texto.append(Uteis.getDoubleFormatado((crnr.getContaReceber().getValor() - crnr.getContaReceber().getValorDescontoRecebido() + crnr.getContaReceber().getJuro() + crnr.getContaReceber().getMulta() + crnr.getContaReceber().getAcrescimo())));
			texto.append(">");
			if(crnr.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.valor )||crnr.getContaReceber().getParcela().equals(TipoOrigemContaReceber.OUTROS.valor )) {
				texto.append("  Descrição:          ");
				texto.append(crnr.getContaReceber().getObservacao());
				texto.append(">");
				texto.append("             ");
			}			
			
			
		}
		texto.append("----------------------------------------->");
		texto.append("  Valor Total Pagamento: ");	
		for(int x = Uteis.getDoubleFormatado(negociacaoRecebimentoVO.getValorTotal()).length(); x<16;x++){
			texto.append(" ");	
		}
		texto.append(Uteis.getDoubleFormatado(negociacaoRecebimentoVO.getValorTotal())).append(">");
		texto.append("  Forma Pagto:  ");
		
		for(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO: negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {				
			texto.append(Uteis.removerAcentos(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getNome()));			
			for(int x = formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getNome().length() - Uteis.getDoubleFormatado(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento()).length(); x<13;x++){
				texto.append(" ");	
			}
			texto.append(Uteis.getDoubleFormatado(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento())).append(">");			
			if(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.CARTAO_DE_CREDITO)) {
				texto.append("  Parcelas:         ");	
				texto.append(formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito());
				texto.append(">");
				
			}
			
			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.CHEQUE)) {
				texto.append("  Titular do Cheque:").append(">");
				texto.append(" ");	
				texto.append(UteisTexto.primeiraLetraMaiuscula(Uteis.removerAcentos(formaPagamentoNegociacaoRecebimentoVO.getCheque().getSacado()))).append(">");				
				texto.append("  Banco:       ");		
				texto.append(Uteis.removerAcentos(formaPagamentoNegociacaoRecebimentoVO.getCheque().getBanco()));
				texto.append(">");	
				texto.append("  Agencia:     ");	
				texto.append(Uteis.removerAcentos(formaPagamentoNegociacaoRecebimentoVO.getCheque().getAgencia()));
				texto.append(">");	
				texto.append("  Numero:      ");	
				texto.append(Uteis.removerAcentos(formaPagamentoNegociacaoRecebimentoVO.getCheque().getNumero()));
				texto.append(">");	
				texto.append("  Data de Previsao:    ");	
				texto.append(formaPagamentoNegociacaoRecebimentoVO.getCheque().getDataPrevisao_Apresentar());
				texto.append(">");	
			}
			
			
			
		}
		if(Uteis.isAtributoPreenchido(configuracaoFinanceiroVO) && Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getObservacaoComprovanteRecebimento())){
			texto.append(" 							>");
			texto.append(Uteis.removerAcentos(configuracaoFinanceiroVO.getObservacaoComprovanteRecebimento())).append(">");
		}
		texto.append(" 							>");
		texto.append(" 							>");
		texto.append(" 							>");
		if(Uteis.isAtributoPreenchido(impressoraVO)){
			getFacadeFactory().getPoolImpressaoFacade().incluirPoolImpressao(impressoraVO, FormatoImpressaoEnum.TEXTO, texto.toString(), usuario);
		}
		
		return texto.toString();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaTipoPessoaContaReceberNegociacaoRecebimento(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			sqlStr.append(" UPDATE negociacaorecebimento set pessoa = ").append(contaReceberVO.getPessoa().getCodigo());
		} else {
			sqlStr.append(" UPDATE negociacaorecebimento set pessoa = ").append(contaReceberVO.getResponsavelFinanceiro().getCodigo());
		}
		sqlStr.append(", tipopessoa = '").append(contaReceberVO.getTipoPessoa()).append("' ");
		sqlStr.append(" WHERE codigo IN ( ");
		sqlStr.append(" 	SELECT negociacaorecebimento FROM contarecebernegociacaorecebimento ");
		sqlStr.append(" 	WHERE contareceber = ").append(contaReceberVO.getCodigo());
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public void alterarPessoaTipoPessoaContaReceberNegociacaoRecebimento(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception {
//		StringBuilder sqlStr = new StringBuilder();
//		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
//			sqlStr.append(" UPDATE negociacaorecebimento set pessoa = ").append(contaReceberVO.getPessoa().getCodigo());
//		} else {
//			sqlStr.append(" UPDATE negociacaorecebimento set pessoa = ").append(contaReceberVO.getResponsavelFinanceiro().getCodigo());
//		}
//		sqlStr.append(", tipopessoa = '").append(contaReceberVO.getTipoPessoa()).append("' ");	
//		sqlStr.append(" WHERE codigo IN ( ");
//		sqlStr.append(" 	SELECT negociacaorecebimento FROM contarecebernegociacaorecebimento ");
//		sqlStr.append(" 	WHERE contareceber = ").append(contaReceberVO.getCodigo());
//		sqlStr.append(" ) ");
//		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
//		getConexao().getJdbcTemplate().update(sqlStr.toString());
//	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaNegociacaoRecebimentoUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE NegociacaoRecebimento set pessoa=? WHERE ((pessoa = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarCriacaoPendenciaChequeDevolvidoExtornoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, String codOrigem, UsuarioVO usuario) throws Exception {
		Integer devolucaoCheque = Integer.valueOf(codOrigem);
		DevolucaoChequeVO devolucaoChequeVO = getFacadeFactory().getDevolucaoChequeFacade().consultarPorChavePrimaria(devolucaoCheque, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		devolucaoChequeVO.setContaCaixa(negociacaoRecebimentoVO.getContaCorrenteCaixa());
		devolucaoChequeVO.getCheque().setPago(false);
		getFacadeFactory().getDevolucaoChequeFacade().criarPendenciaChequeDevolvido(devolucaoChequeVO, usuario);
		getFacadeFactory().getDevolucaoChequeFacade().movimentacaoCaixa(devolucaoChequeVO, usuario);
	}
	
	
	public void validarDadosRecebimentoCartaoCreditoMatriculaOnline(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		negociacaoRecebimentoVO.setValorTotalRecebimento(0.0);
		if (!Uteis.isAtributoPreenchido(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO())) {
			throw new Exception("Não Foi Encontrada Uma Configuração de Recebimento de Cartão Online.");
		}
		for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			validarDadosRecebimentoCartaoCreditoPorFormaPagamentoNegociacaoRecebimento(negociacaoRecebimentoVO, obj, usuarioVO);
			negociacaoRecebimentoVO.setValorTotalRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento() + obj.getValorRecebimento());
		}
		if (Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotalRecebimento()) != Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotal())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ValorRecebimentoDiferenteValorTotal"));
		}
	}
	
	public void validarDadosRecebimentoCartaoCreditoPorFormaPagamentoNegociacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getCodigo().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_informarOperadoraCartao").replace("{0}", obj.getQuantidadeCartao().toString()));
		}
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_nomeCartaoCredito").replace("{0}", obj.getQuantidadeCartao().toString()));
		}
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_numeroCartaoCredito").replace("{0}", obj.getQuantidadeCartao().toString()));
		}
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade() == null || obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_mesVencimentoCartaoCredito").replace("{0}", obj.getQuantidadeCartao().toString()));
		}
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade() == null || obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_anoVencimentoCartaoCredito").replace("{0}", obj.getQuantidadeCartao().toString()));
		}
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_codigoVerificacaoVencimentoCartaoCredito").replace("{0}", obj.getQuantidadeCartao().toString()));
		}
		if (!(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getValorMinimoRecebimentoCartaoCredito().equals(0.0)) && Uteis.arrendondarForcando2CadasDecimais(obj.getValorRecebimento()) < Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getValorMinimoRecebimentoCartaoCredito())) {
			throw new Exception(UteisJSF.internacionalizar("msg_RenovarMatricula_valorMinimoRecebimentoExcedido").replace("{0}", obj.getQuantidadeCartao().toString()).replace("{1}", Uteis.formatarDecimalDuasCasas(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getValorMinimoRecebimentoCartaoCredito())));
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(NegociacaoRecebimentoVO negociacaoRecebimentoVO, String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosRecebimentoCartaoCreditoMatriculaOnline(negociacaoRecebimentoVO, usuarioVO);
		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		
		for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			obj.setConfiguracaoRecebimentoCartaoOnlineVO(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO());
			obj.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			obj.setContaCorrente(obj.getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
			obj.setContaCorrenteOperadoraCartaoVO(obj.getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
			obj.setOperadoraCartaoVO(obj.getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
			obj.setCategoriaDespesaVO(obj.getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
			if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
				formaPagamentoNegociacaoRecebimentoVOs.addAll(preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, negociacaoRecebimentoVO, obj.getValorRecebimento(), usuarioVO));
			} else {
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), obj.getConfiguracaoFinanceiroCartaoVO(), new Date(), 1, obj.getQtdeParcelasCartaoCredito(), obj.getValorRecebimento(), usuarioVO);
				formaPagamentoNegociacaoRecebimentoVOs.add(obj);
			}
		}
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().clear();
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().addAll(formaPagamentoNegociacaoRecebimentoVOs);
		if(Uteis.isAtributoPreenchido(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs())){
			negociacaoRecebimentoVO.setContaCorrenteCaixa(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
		}
		ContaReceberVO contaReceberVO =  negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber();
		boolean bloqueiaPagamentoCartaoCredito = getFacadeFactory().getContaReceberFacade().verificaBloqueioEmissaoBoleto(contaReceberVO, usuarioVO);
		if (bloqueiaPagamentoCartaoCredito) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_recebimentoCartaoCreditoBloqueado")
					.replace("{0}", contaReceberVO.getContaCorrenteVO().getBloquearEmissaoBoletoHoraIni())
					.replace("{1}", contaReceberVO.getContaCorrenteVO().getBloquearEmissaoBoletoHoraFim()));
		}

		negociacaoRecebimentoVO.setUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira());
		negociacaoRecebimentoVO.setResponsavel(usuarioVO);
		//negociacaoRecebimentoVO.setPessoa(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		negociacaoRecebimentoVO.setMatricula(matricula);
		negociacaoRecebimentoVO.setTipoPessoa(contaReceberVO.getTipoPessoa());
		if(contaReceberVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			negociacaoRecebimentoVO.setPessoa(contaReceberVO.getResponsavelFinanceiro());
		}else {
			negociacaoRecebimentoVO.setPessoa(contaReceberVO.getPessoa());
		}
		if (negociacaoRecebimentoVO.getCriarRegistroRecorrenciaDCC()) {
			getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().realizarCriacaoRecorrenciaCartaoCreditoDebito(contaReceberVO, negociacaoRecebimentoVO, usuarioVO);
		}
		getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, false, usuarioVO);
	}
	
	@Override
	public void realizarGeracaoFormaPagamentoNegociacaoRecebimentoCartaoCreditoFinanciamentoInstituicao(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception{
		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(formaPagamentoNegociacaoRecebimentoVO, negociacaoRecebimentoVO, formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento(), usuarioVO);
		for(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2: formaPagamentoNegociacaoRecebimentoVOs){
			negociacaoRecebimentoVO.adicionarObjFormaPagamentoNegociacaoRecebimentoVOs(formaPagamentoNegociacaoRecebimentoVO2);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<FormaPagamentoNegociacaoRecebimentoVO> preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacaoRecebimentoVO, Double valorRecebimento, UsuarioVO usuarioVO) throws Exception {	
		List<FormaPagamentoNegociacaoRecebimentoVO> listaTempFpnr = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		for (int nrParcela = 1; nrParcela <= obj.getQtdeParcelasCartaoCredito(); nrParcela++) {
			FormaPagamentoNegociacaoRecebimentoVO novoFpnr = (FormaPagamentoNegociacaoRecebimentoVO) obj.clone();
			novoFpnr.setConfiguracaoRecebimentoCartaoOnlineVO(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO());
			novoFpnr.setQtdeParcelasCartaoCredito(obj.getQtdeParcelasCartaoCredito());
			novoFpnr.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(novoFpnr.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			novoFpnr.setCategoriaDespesaVO(novoFpnr.getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
			novoFpnr.setContaCorrenteOperadoraCartaoVO(novoFpnr.getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
			novoFpnr.setContaCorrente(novoFpnr.getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
			novoFpnr.setOperadoraCartaoVO(novoFpnr.getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
			novoFpnr.setTaxaDeOperacao(novoFpnr.getConfiguracaoFinanceiroCartaoVO().getTaxaBancaria(obj.getQtdeParcelasCartaoCredito(), obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum()));
			novoFpnr.setNegociacaoRecebimentoVO(negociacaoRecebimentoVO);
			novoFpnr.setNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(novoFpnr, obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), novoFpnr.getConfiguracaoFinanceiroCartaoVO(), negociacaoRecebimentoVO.getData(), nrParcela, obj.getQtdeParcelasCartaoCredito(), valorRecebimento, usuarioVO);
			listaTempFpnr.add(novoFpnr);
		}
		return listaTempFpnr;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRecebimentoCartaoCreditoIncricao(NegociacaoRecebimentoVO negociacaoRecebimentoVO, Integer codigoPessoa, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosRecebimentoCartaoCreditoMatriculaOnline(negociacaoRecebimentoVO, usuarioVO);
		negociacaoRecebimentoVO.setUnidadeEnsino(unidadeEnsinoVO);
		negociacaoRecebimentoVO.setContaCorrenteCaixa(configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca());
		negociacaoRecebimentoVO.setResponsavel(usuarioVO);
		negociacaoRecebimentoVO.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoPessoa, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.CANDIDATO.getValor());
		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {				
			formaPagamentoNegociacaoRecebimentoVOs.addAll(preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, negociacaoRecebimentoVO, obj.getValorRecebimento(), usuarioVO));
		}
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().clear();
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().addAll(formaPagamentoNegociacaoRecebimentoVOs);
		incluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, false, usuarioVO);
	}
	
	@Override
	public List<NegociacaoRecebimentoVO> consultar(ControleConsulta controleConsulta, int valorConsultaUnidadeEnsino, String tipoOrigem, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		List<NegociacaoRecebimentoVO> negociacaoRecebimentoVOs = new ArrayList<NegociacaoRecebimentoVO>(0);
		if (controleConsulta.getCampoConsulta().equals("codigo")) {
			if (controleConsulta.getValorConsulta().equals("")) {
				controleConsulta.setValorConsulta("0");
			}
			int valorInt = Integer.parseInt(controleConsulta.getValorConsulta());
			negociacaoRecebimentoVOs = consultaRapidaPorCodigo(new Integer(valorInt), controleConsulta.getDataIni(), controleConsulta.getDataFim(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		if (controleConsulta.getCampoConsulta().equals("matricula")) {
			negociacaoRecebimentoVOs = consultaRapidaPorMatricula(controleConsulta.getValorConsulta(), controleConsulta.getDataIni(), controleConsulta.getDataFim(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		if (controleConsulta.getCampoConsulta().equals("cpf")) {
			negociacaoRecebimentoVOs = consultaRapidaPorCpf(controleConsulta.getValorConsulta(), controleConsulta.getDataIni(), controleConsulta.getDataFim(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		if (controleConsulta.getCampoConsulta().equals("responsavel")) {
			negociacaoRecebimentoVOs = consultaRapidaPorResponsavel(controleConsulta.getValorConsulta(), controleConsulta.getDataIni(), controleConsulta.getDataFim(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		if (controleConsulta.getCampoConsulta().equals("contaCorrenteCaixa")) {
			negociacaoRecebimentoVOs = consultaRapidaPorContaCorrenteCaixa(controleConsulta.getValorConsulta(), controleConsulta.getDataIni(), controleConsulta.getDataFim(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		if (controleConsulta.getCampoConsulta().equals("pessoa")) {
			negociacaoRecebimentoVOs = consultaRapidaPorPessoa(controleConsulta.getValorConsulta(), controleConsulta.getDataIni(), controleConsulta.getDataFim(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		if (controleConsulta.getCampoConsulta().equals("nossoNumero")) {
			negociacaoRecebimentoVOs = consultaRapidaPorNossoNumero(controleConsulta.getValorConsulta(), valorConsultaUnidadeEnsino, tipoOrigem, verificarAcesso, nivelMontarDados, usuarioVO);
		}
		return negociacaoRecebimentoVOs;
	}
	
	@Override
	public void realizarRecebimentoContaProcessada(ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UnidadeEnsinoVO unidadeEnsinoVO, String tipoPessoa, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO) throws Exception {
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(unidadeEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
		UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs2 = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		if(!formaPagamentoNegociacaoRecebimentoVOs.isEmpty()) {
			configuracaoFinanceiroCartaoVO = formaPagamentoNegociacaoRecebimentoVOs.get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO();
		}
		for (FormaPagamentoNegociacaoRecebimentoVO obj : formaPagamentoNegociacaoRecebimentoVOs) {		
			formaPagamentoNegociacaoRecebimentoVOs2.addAll(preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, negociacaoRecebimentoVO, obj.getValorRecebimento(), usuarioVO));
		}
		negociacaoRecebimentoVO.setUnidadeEnsino(unidadeEnsinoVO);
		negociacaoRecebimentoVO.setContaCorrenteCaixa(configuracaoFinanceiroCartaoVO.getConfiguracaoFinanceiroVO().getContaCorrentePadraoControleCobranca());
		negociacaoRecebimentoVO.setResponsavel(usuarioVO);
		negociacaoRecebimentoVO.setTipoPessoa(tipoPessoa);
		if(tipoPessoa.equals(TipoPessoa.ALUNO.getValor())) {
			negociacaoRecebimentoVO.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		} else if(tipoPessoa.equals(TipoPessoa.PARCEIRO.getValor())) {
			negociacaoRecebimentoVO.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(parceiroVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		} else if(tipoPessoa.equals(TipoPessoa.FORNECEDOR.getValor())) {
			negociacaoRecebimentoVO.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(fornecedorVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		NegociacaoRecebimentoVO negociacaoRecebimentoDCCVO = getFacadeFactory().getNegociacaoRecebimentoDCCFacade().consultarPorChavePrimaria(contaReceberNegociacaoRecebimentoVO.getNegociacaoRecebimento(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroCartaoVO.getConfiguracaoFinanceiroVO(), usuarioVO);
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().clear();
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().addAll(formaPagamentoNegociacaoRecebimentoVOs2);
		negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(contaReceberNegociacaoRecebimentoVO);
		negociacaoRecebimentoVO.setValorTotal(contaReceberNegociacaoRecebimentoVO.getValorTotal());
		negociacaoRecebimentoVO.setValorTotalRecebimento(contaReceberNegociacaoRecebimentoVO.getValorTotal());
		negociacaoRecebimentoVO.setMatricula(negociacaoRecebimentoDCCVO.getMatricula());
		incluirRecebimentoBaixaDCC(negociacaoRecebimentoVO, configuracaoFinanceiroCartaoVO.getConfiguracaoFinanceiroVO(), false, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NegociacaoRecebimentoVO consultarRecebimentoProcessoSeletivoAluno(Integer codigoInscricaoProcessoSeletivo, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select negociacaorecebimento.* from procseletivo");
		sqlStr.append(" inner join inscricao on inscricao.procseletivo = procseletivo.codigo");
		sqlStr.append(" inner join pessoa on pessoa.codigo = inscricao.candidato");
		sqlStr.append(" inner join contareceber on contareceber.codigo = inscricao.contareceber");
		sqlStr.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
		sqlStr.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");
		sqlStr.append(" where inscricao.codigo = ").append(codigoInscricaoProcessoSeletivo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new NegociacaoRecebimentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuarioVO));
	}
	
	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NegociacaoRecebimentoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirRecebimentoBaixaDCC(final NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			NegociacaoRecebimento.incluir(getIdEntidade(), verificarAcesso, usuario);
			NegociacaoRecebimentoVO.validarDados(obj);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getData(), obj.getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario);		
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO NegociacaoRecebimento( data, valorTotalRecebimento, valorTotal, responsavel, contaCorrenteCaixa, pessoa, tipoPessoa, matricula, valorTroco, unidadeEnsino, parceiro, observacao, recebimentoBoletoAutomatico, fornecedor) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setDouble(2, obj.getValorTotalRecebimento().doubleValue());
					sqlInserir.setDouble(3, obj.getValorTotal().doubleValue());
					if (!obj.getResponsavel().getCodigo().equals(0)) {
						sqlInserir.setInt(4, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getContaCorrenteCaixa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getContaCorrenteCaixa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getTipoPessoa());
					if (obj.getTipoAluno().booleanValue() || obj.getTipoFuncionario().booleanValue()) {
						sqlInserir.setString(8, obj.getMatricula());
					} else {
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setDouble(9, obj.getValorTroco().doubleValue());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setDouble(10, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (obj.getParceiroVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getParceiroVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setString(12, obj.getObservacao());
					sqlInserir.setBoolean(13, obj.getRecebimentoBoletoAutomatico());
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(14, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(14, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			if (obj.getValorTroco() > 0) {
				getFacadeFactory().getFluxoCaixaFacade().validarSaldoCaixa(obj.getValorTroco(), obj.getValorTotalRecebimento(), obj.getContaCorrenteCaixa().getCodigo(), usuario);
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().incluirFormaPagamentoNegociacaoRecebimentosBaixaDCC(obj, usuario);
			distribuirRecebimentoContaReceber(obj, usuario);
			if (obj.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
				validarDadosContaReceberSituacaoRecebidaOuAReceber(obj, false);
			}
			getFacadeFactory().getContaReceberNegociacaoRecebimentoFacade().incluirContaReceberNegociacaoRecebimentos(obj, obj.getContaReceberNegociacaoRecebimentoVOs(), configuracaoFinanceiro, usuario);
			// Altera a situação da conta a receber agrupada para recebida.
			getFacadeFactory().getContaReceberAgrupadaFacade().alterarSituacaoContaAgrupada(obj, SituacaoContaReceber.RECEBIDO.name(), usuario);
			getFacadeFactory().getMapaLancamentoFuturoFacade().baixarPendenciaAtravezDeNegociacaoRecebimento(obj, usuario);
			obj.setValorTrocoAlteracao(obj.getValorTroco());
			obj.setNovoObj(Boolean.FALSE);
			if (obj.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
				getFacadeFactory().getContaReceberFacade().baixarContaReceberConcedendoDescontoTotalAMesmaPorNegociacaoRecebimento(obj, "Desconto 100% no recebimento.", usuario, configuracaoFinanceiro, usuario);
			}
			verificaSituacaoMatriculaPreMatriculadaParaEfetivacao(obj, configuracaoFinanceiro, usuario);
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().incluirLogBaixaCartaoCreditoDCC(obj.getFormaPagamentoNegociacaoRecebimentoVOs(), SituacaoTransacaoEnum.APROVADO, usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	
	
	public void notificarAlunoMatriculaPaga(final MatriculaVO matriculaVO, final TurmaVO turmaVO, final ConfiguracaoFinanceiroVO configuracaoFinanceiro, final UsuarioVO usuario) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PessoaVO pessoaReceberCopiaEmail = new PessoaVO();			
				try {
					PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_MATRICULA_PAGA_CONFIRMADA, false, matriculaVO.getUnidadeEnsino().getCodigo(), null);
					if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
						ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, usuario, usuario.getUnidadeEnsinoLogado().getCodigo());
						if (!Uteis.isAtributoPreenchido(config.getResponsavelPadraoComunicadoInterno().getCodigo())) {
							return;
						}
						if (!configuracaoFinanceiro.getEmailEnviarNotificacaoConsultorMatricula().equals("")) {
							pessoaReceberCopiaEmail = config.getResponsavelPadraoComunicadoInterno();
							pessoaReceberCopiaEmail.setEmail(configuracaoFinanceiro.getEmailEnviarNotificacaoConsultorMatricula());
						}
						ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
						String mensagemEditada = "";
						mensagemEditada = obterMensagemFormatadaMensagemAlunoMatriculaPaga(matriculaVO, turmaVO, mensagemTemplate.getMensagem());
						String mensagemSMSEditada = obterMensagemFormatadaMensagemAlunoMatriculaPaga(matriculaVO, turmaVO, mensagemTemplate.getMensagemSMS());
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
							comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
						}
						comunicacaoEnviar.setMensagem(mensagemEditada);
						comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoEnviar.setTipoDestinatario("AL");
						comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
						PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
						comunicacaoEnviar.setResponsavel(responsavel);
						// comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberVO.getMatriculaAluno().getAluno(),
						// contaReceberVO.getMatriculaAluno().getConsultor().getPessoa()));
						comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaVO.getAluno(), new PessoaVO()));
						comunicacaoEnviar.setData(new Date());
						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
					}
				} catch (Exception e) {
					// //System.out.print("Erro ao enviar comunicado MATRICULA
					// PAGA CRM => "
					// + e.getMessage());
				}
			}
		}).start();
	}
	
	public void notificarConsultorMatriculaPaga(final MatriculaVO matriculaVO, final TurmaVO turmaVO, final ConfiguracaoFinanceiroVO configuracaoFinanceiro, final UsuarioVO usuario) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_CONSULTOR_MATRICULA_PAGA, false, null);
				if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				PessoaVO pessoaReceberCopiaEmail = new PessoaVO();
				if (matriculaVO.getConsultor().getCodigo() == 0) {
					try {
						matriculaVO.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaConsultorPorMatricula(matriculaVO.getMatricula(), false, usuario));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (matriculaVO.getConsultor().getCodigo() == 0) {
						return;
					}
				} else {
					if (matriculaVO.getConsultor().getPessoa().getCodigo() == 0) {
						try {
							matriculaVO.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaConsultorPorMatricula(matriculaVO.getMatricula(), false, usuario));
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (matriculaVO.getConsultor().getCodigo() == 0) {
							return;
						}
					}
				}
				
					ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, usuario, usuario.getUnidadeEnsinoLogado().getCodigo());

					if (!configuracaoFinanceiro.getEmailEnviarNotificacaoConsultorMatricula().equals("")) {
						pessoaReceberCopiaEmail = config.getResponsavelPadraoComunicadoInterno();
						pessoaReceberCopiaEmail.setEmail(configuracaoFinanceiro.getEmailEnviarNotificacaoConsultorMatricula());
					}
					String mensagemEditada = "";
						mensagemEditada = obterMensagemFormatadaMensagemConsultorMatriculaPaga(matriculaVO, turmaVO, mensagemTemplate.getMensagem());



					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());

					comunicacaoEnviar.setMensagem(mensagemEditada);
					comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setTipoDestinatario("FU");
					comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
					PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
					comunicacaoEnviar.setResponsavel(responsavel);
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaVO.getConsultor().getPessoa(), pessoaReceberCopiaEmail));
					comunicacaoEnviar.setData(new Date());

					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
    }
	

    
    public List<ComunicadoInternoDestinatarioVO> obterListaDestinatarios(PessoaVO pessoa, PessoaVO pessoaReceberCopiaEmail) {
        ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
        destinatario.setCiJaLida(Boolean.FALSE);
        destinatario.setDestinatario(pessoa);
        destinatario.setEmail(pessoa.getEmail());
        destinatario.setNome(pessoa.getNome());
        destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
        List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<ComunicadoInternoDestinatarioVO>();        
        listDestinatario.add(destinatario);
        if (!pessoaReceberCopiaEmail.getEmail().equals("")) {
	        ComunicadoInternoDestinatarioVO destinatario2 = new ComunicadoInternoDestinatarioVO();
	        destinatario2.setCiJaLida(Boolean.FALSE);
	        destinatario2.setDestinatario(pessoaReceberCopiaEmail);
	        destinatario2.setEmail(pessoaReceberCopiaEmail.getEmail());
	        destinatario2.setNome(pessoaReceberCopiaEmail.getNome());
	        destinatario2.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
	        listDestinatario.add(destinatario2);
        }
        return listDestinatario;
    }
    
    public String obterMensagemFormatadaMensagemConsultorMatriculaPaga(MatriculaVO matriculaVO, TurmaVO turmaVO, final String mensagemTemplate) {
        String mensagemTexto = mensagemTemplate;
        mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaVO.getAluno().getNome());
        mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CONSULTOR.name(), matriculaVO.getConsultor().getPessoa().getNome());
        mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaVO.getMatricula());
        mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RENEGOCIACAO.name(), Uteis.getData(new Date()));
        mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), turmaVO.getIdentificadorTurma());
        return mensagemTexto;        
    }

	public String obterMensagemFormatadaMensagemAlunoMatriculaPaga(MatriculaVO matriculaVO, TurmaVO turmaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaVO.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), turmaVO.getIdentificadorTurma());
		return mensagemTexto;
	}    
	
    public ComunicacaoInternaVO inicializarDadosPadrao(ComunicacaoInternaVO comunicacaoEnviar) {
        // Caso o valor seja True, um email sera envida quando a comunicacao for persistida.
        comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
        // Para obter a mensagem do email formatado Usamos um metodo a parte.
        comunicacaoEnviar.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
        comunicacaoEnviar.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
        comunicacaoEnviar.setTipoMarketing(Boolean.FALSE);
        comunicacaoEnviar.setTipoLeituraObrigatoria(Boolean.FALSE);
        comunicacaoEnviar.setDigitarMensagem(Boolean.TRUE);
        return comunicacaoEnviar;

    }
    
    @Override
	public List<BandeiraRSVO> consultarBandeirasDisponiveisPagamentoOnline(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UnidadeEnsinoVO unidadeEnsinoVO, Double valorAReceber) {
		List<BandeiraRSVO> bandeirasDisponiveis = new ArrayList<BandeiraRSVO>();
		try {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
			configuracaoGeralSistemaVO  = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, null, unidadeEnsinoVO.getCodigo());
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsinoVO.getCodigo(), usuarioVO);
			List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(configuracaoFinanceiroVO.getCodigo(),  valorAReceber, "", TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name(), usuarioVO);
			if(Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO) && Uteis.isAtributoPreenchido(configuracaoFinanceiroCartaoVOs)){
				BandeiraRSVO bandeiraRSVO = null;
				for (ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO : configuracaoFinanceiroCartaoVOs) {
					if (configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo().equals("CARTAO_CREDITO")) {
						bandeiraRSVO = new BandeiraRSVO();
						bandeiraRSVO.setCodigo(configuracaoFinanceiroCartaoVO.getCodigo());
						bandeiraRSVO.setBandeira(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar());
						bandeiraRSVO.setBandeiraEnum(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().getName());
						bandeiraRSVO.setNomeCartao(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getNome());
//						bandeiraRSVO.setValorMinimoRecebimento(configuracaoFinanceiroCartaoVO.getValorMinimoParaRecebimentoCartao());
						bandeirasDisponiveis.add(bandeiraRSVO);					
					}
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bandeirasDisponiveis;
	}
    
    @Override
 	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
 	public void alterarUnidadeEnsino (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
 		 final StringBuilder sqlStr = new StringBuilder();
 		 sqlStr.append(" UPDATE negociacaorecebimento SET unidadeensino = t.unidadeensino FROM ( ");
 		 sqlStr.append("SELECT DISTINCT ");
 		 sqlStr.append(" turma.unidadeensino ,negociacaorecebimento.codigo AS negociacaorecebimento  ");
 		 sqlStr.append("FROM contarecebernegociacaorecebimento ");
 		 sqlStr.append(" INNER JOIN negociacaorecebimento ON contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
 		 sqlStr.append(" INNER JOIN contareceber          ON contareceber.codigo                                     = contarecebernegociacaorecebimento.contareceber ");
 		 sqlStr.append(" INNER JOIN turma                 ON contareceber.turma                                      = turma.codigo ");
 		 sqlStr.append("WHERE turma.codigo = ?  ");
 		 sqlStr.append(") AS t WHERE t.negociacaorecebimento = negociacaorecebimento.codigo;");
 		 sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

 		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
 			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
 				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
 				int i = 0;
 				Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlAlterar);
 				return sqlAlterar;
 			}
 		});
 	}
    
}
