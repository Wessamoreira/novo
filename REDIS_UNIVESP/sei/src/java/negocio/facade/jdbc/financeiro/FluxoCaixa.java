package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.FluxoCaixaVO.EnumCampoConsultaFluxoCaixa;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MovimentacaoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FluxoCaixaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>FluxoCaixaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>FluxoCaixaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see FluxoCaixaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class FluxoCaixa extends ControleAcesso implements FluxoCaixaInterfaceFacade {

	private static final long serialVersionUID = -2994492492908206359L;

	protected static String idEntidade;

	public FluxoCaixa() throws Exception {
		super();
		setIdEntidade("FluxoCaixa");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>FluxoCaixaVO</code>.
	 */
	public FluxoCaixaVO novo() throws Exception {
		FluxoCaixa.incluir(getIdEntidade());
		FluxoCaixaVO obj = new FluxoCaixaVO();
		return obj;
	}

	public void verificarCaixaJaFoiAberto(Integer contaCorrente, UsuarioVO usuario) throws Exception {
		FluxoCaixaVO fluxoCaixa = consultarPorFluxoExistenciaCaixaAberto(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (fluxoCaixa != null && fluxoCaixa.getCodigo().intValue() != 0) {
			throw new Exception("O fluxo de caixa para esta conta corrente está aberta desde " + fluxoCaixa.getDataAbertura_Apresentar() + ".");
		}

	}
	
	public void incluir(final FluxoCaixaVO obj, UsuarioVO usuario) throws Exception {
		this.incluir(obj, usuario, true);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>FluxoCaixaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FluxoCaixaVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FluxoCaixaVO obj, UsuarioVO usuario, boolean validarCaixaAberto) throws Exception {
		try {
			FluxoCaixaVO.validarDados(obj);
			FluxoCaixa.incluir(getIdEntidade(), true, usuario);
			if (validarCaixaAberto) {
				verificarCaixaJaFoiAberto(obj.getContaCaixa().getCodigo(), usuario);
			}
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ABRIR", obj.getDataAbertura(), null, obj.getUnidadeEnsino(), obj.getContaCaixa().getCodigo(), TipoOrigemHistoricoBloqueioEnum.CAIXA, usuario);

			final String sql = "INSERT INTO FluxoCaixa( dataAbertura, responsavelAbertura, contaCaixa, saldoInicial, saldoFinal, dataFechamento, " + "responsavelFechamento, situacao, unidadeEnsino, saldoCartao, saldoDeposito ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataAbertura()));
					if (obj.getResponsavelAbertura().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavelAbertura().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getContaCaixa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getContaCaixa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setDouble(4, obj.getSaldoInicial().doubleValue());
					sqlInserir.setDouble(5, obj.getSaldoFinal().doubleValue());
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getResponsavelFechamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getSituacao());
					if (obj.getUnidadeEnsino().intValue() != 0) {
						sqlInserir.setInt(9, obj.getUnidadeEnsino().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}					
					sqlInserir.setDouble(10, obj.getSaldoCartao());
					sqlInserir.setDouble(11, obj.getSaldoDeposito());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getMovimentacaoCaixaFacade().incluirMovimentacaoCaixas(obj.getCodigo(), obj.getMovimentacaoCaixaVOs(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>FluxoCaixaVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FluxoCaixaVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FluxoCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			FluxoCaixaVO.validarDados(obj);
			FluxoCaixa.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE FluxoCaixa set dataAbertura=?, responsavelAbertura=?, contaCaixa=?, saldoInicial=?, saldoFinal=?, " + "dataFechamento=?, responsavelFechamento=?, situacao=?, unidadeEnsino=?, saldoCartao=?, saldoDeposito=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataAbertura()));
					if (obj.getResponsavelAbertura().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getResponsavelAbertura().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getContaCaixa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getContaCaixa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setDouble(4, obj.getSaldoInicial().doubleValue());
					sqlAlterar.setDouble(5, obj.getSaldoFinal().doubleValue());
					sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getResponsavelFechamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getSituacao());
					sqlAlterar.setInt(9, obj.getUnidadeEnsino().intValue());
					sqlAlterar.setDouble(10, obj.getSaldoCartao());
					sqlAlterar.setDouble(11, obj.getSaldoDeposito());
					sqlAlterar.setInt(12, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void fecharCaixa(final FluxoCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			FluxoCaixaVO.validarDados(obj);
			FluxoCaixa.alterar(getIdEntidade());
			obj.setSituacao("F");
			final String sql = "UPDATE FluxoCaixa set dataAbertura=?, responsavelAbertura=?, contaCaixa=?, saldoInicial=?, dataFechamento=?, " + "responsavelFechamento=?, situacao=?, unidadeEnsino=?, saldoCartao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataAbertura()));
					if (obj.getResponsavelAbertura().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getResponsavelAbertura().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getContaCaixa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getContaCaixa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setDouble(4, obj.getSaldoInicial().doubleValue());
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getResponsavelFechamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getSituacao());
					sqlAlterar.setInt(8, obj.getUnidadeEnsino().intValue());
					sqlAlterar.setDouble(9, obj.getSaldoCartao());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

			getFacadeFactory().getContaCorrenteFacade().alterarSaldoContaCorrente(obj.getContaCaixa().getCodigo(), obj.getSaldoFinal(), usuario);

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSaldoFluxoCaixa(final FluxoCaixaVO obj) throws Exception {
		final String sql = "UPDATE FluxoCaixa set saldoFinal=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getSaldoFinal().doubleValue());
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSaldoInicialFinalFluxoCaixa(final FluxoCaixaVO obj) throws Exception {
		final String sql = "UPDATE FluxoCaixa set saldoFinal=?, saldoInicial=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getSaldoFinal().doubleValue());
				sqlAlterar.setDouble(2, obj.getSaldoInicial().doubleValue());
				sqlAlterar.setInt(3, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FluxoCaixaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FluxoCaixaVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FluxoCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			FluxoCaixa.excluir(getIdEntidade(), true, usuario);
			FluxoCaixaVO.validarDadosExclusao(obj);
			String sql = "DELETE FROM FluxoCaixa WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getMovimentacaoCaixaFacade().excluirMovimentacaoCaixas(obj.getCodigo(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void criarMovimentacaoCaixa(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, ChequeVO chequeVO, Integer operadoraCartao, UsuarioVO usuario) throws Exception {
		// List lista =
		// getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(true,
		// usuario.getPessoa().getCodigo(), unidadeEnsino, new Date(), "A",
		// Uteis.NIVELMONTARDADOS_TODOS, usuario);
		// if (lista.isEmpty()) {
		// return;
		// }
		// FluxoCaixaVO caixa = (FluxoCaixaVO)lista.get(0);
		ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (conta.getContaCaixa()) {
			FluxoCaixaVO caixa = consultarPorFluxoCaixaAberto(new Date(), contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (caixa == null) {
				throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
			}
			if (tipoMovimentacao.equals("EN")) {
				caixa.setSaldoFinal(caixa.getSaldoFinal() + valor);
			} else {
				FormaPagamentoVO formaPagamentoVO = getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(formaPagamento, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				caixa.setSaldoFinal(caixa.getSaldoFinal() - valor);
				if (formaPagamentoVO.getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())) {
					caixa.setSaldoDinheiro(caixa.getSaldoDinheiro() - valor);
					if (caixa.getSaldoDinheiro() < 0) {
						throw new Exception("Saldo insuficiente para realizar este operação na conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
					}
				} else if (formaPagamentoVO.getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
					caixa.setSaldoCheque(caixa.getSaldoCheque() - valor);
					if (caixa.getSaldoCheque() < 0) {
						throw new Exception("Saldo insuficiente para realizar este operação na conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
					}
				}
			}
			
			MovimentacaoCaixaVO obj = getFacadeFactory().getMovimentacaoCaixaFacade().executarGeracaoMovimentacaoCaixa(chequeVO, caixa, pessoa, parceiro, fornecedor, responsavel, operadoraCartao, banco, codigoOrigem, formaPagamento, valor, tipoOrigem, tipoMovimentacao);
			getFacadeFactory().getMovimentacaoCaixaFacade().incluir(obj, usuario);
			alterarSaldoFluxoCaixa(caixa);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void alterarSaldoFluxoCaixaPorMovimentacaoCaixaEstornoMovimentacaoFinanceira(DevolucaoChequeVO devCheque, Double valor, UsuarioVO usuario) throws Exception {
		// List<MovimentacaoCaixaVO> listaMovCaixa =
		// getFacadeFactory().getMovimentacaoCaixaFacade().consultarPorCodigoTipoOrigem(devCheque.getCodigo(),
		// "DC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// for (MovimentacaoCaixaVO movCx : listaMovCaixa) {
		// FluxoCaixaVO flxCx = consultarPorChavePrimaria(movCx.getFluxoCaixa(),
		// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		// List<FluxoCaixaVO> listaFlxCx =
		// consultarPorDataAberturaCodigoContaCaixa(flxCx.getContaCaixa().getCodigo(),
		// flxCx.getDataFechamento(), false,
		// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		// for (FluxoCaixaVO fluxoCaixaVO : listaFlxCx) {
		// if (movCx.getTipoMovimentacao().equals("EN")) {
		// fluxoCaixaVO.setSaldoInicial(fluxoCaixaVO.getSaldoInicial() -
		// movCx.getValor());
		// fluxoCaixaVO.setSaldoFinal(fluxoCaixaVO.getSaldoFinal() -
		// movCx.getValor());
		// } else {
		// fluxoCaixaVO.setSaldoInicial(flxCx.getSaldoInicial() +
		// movCx.getValor());
		// fluxoCaixaVO.setSaldoFinal(fluxoCaixaVO.getSaldoFinal() +
		// movCx.getValor());
		// }
		// alterarSaldoInicialFinalFluxoCaixa(fluxoCaixaVO);
		// }
		// getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(devCheque.getCheque().getValor(),
		// devCheque.getContaCaixa().getCodigo(), "SA", "DC",
		// consultarFormaPagamento(usuario).getCodigo(), devCheque.getCodigo(),
		// devCheque.getResponsavel().getCodigo(),
		// devCheque.getPessoa().getCodigo(), 0, 0, 0, usuario);
		// }

		getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(devCheque.getCheque().getValor(), devCheque.getContaCaixa().getCodigo(), "SA", "DC", consultarFormaPagamento(usuario).getCodigo(), devCheque.getCodigo(), devCheque.getResponsavel().getCodigo(), devCheque.getPessoa().getCodigo(), 0, 0, 0, devCheque.getCheque(), 0, usuario);
	}

	public FormaPagamentoVO consultarFormaPagamento(UsuarioVO usuario) throws Exception {
		List listaContaReceber = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		Iterator i = listaContaReceber.iterator();
		while (i.hasNext()) {
			FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
			if (obj.getTipo().equals("CH")) {
				return obj;
			}
		}
		return new FormaPagamentoVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void alterarSaldoFluxoCaixaPorMovimentacaoCaixaEstornoMovimentacaoFinanceira(MovimentacaoFinanceiraVO movimentacaoFinanceira, boolean possuiPermissaoMovimentarCaixaFechado, UsuarioVO usuario) throws Exception {
		List<MovimentacaoCaixaVO> listaMovCaixa = getFacadeFactory().getMovimentacaoCaixaFacade().consultarPorCodigoTipoOrigem(movimentacaoFinanceira.getCodigo(), "MF", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		for (MovimentacaoCaixaVO movCx : listaMovCaixa) {
			ChequeVO chequeVO = null;
			if(movCx.getCheque() != null && movCx.getCheque()> 0){
				chequeVO = new ChequeVO();
				chequeVO.setNumero(movCx.getNumeroCheque());
				chequeVO.setCodigo(movCx.getCheque());
				chequeVO.setAgencia(movCx.getAgenciaCheque());
				chequeVO.setBanco(movCx.getBanco());
				chequeVO.setNumeroContaCorrente(movCx.getContaCorrenteCheque());
				chequeVO.setDataPrevisao(movCx.getDataPrevisaoCheque());
				chequeVO.setSacado(movCx.getEmitenteCheque());
				if(movCx.getPessoa().getCodigo() > 0){
					chequeVO.getPessoa().setCodigo(movCx.getPessoa().getCodigo());
					chequeVO.getPessoa().setNome(movCx.getSacado());
				}else if(movCx.getFornecedor().getCodigo() > 0){
					chequeVO.getFornecedor().setCodigo(movCx.getFornecedor().getCodigo());
					chequeVO.getFornecedor().setNome(movCx.getSacado());
				}else if(movCx.getParceiro().getCodigo() > 0){
					chequeVO.getParceiro().setCodigo(movCx.getParceiro().getCodigo());
					chequeVO.getParceiro().setNome(movCx.getSacado());
				}
				chequeVO.setEmitentePessoaJuridica(movCx.getPessoaJuridicaCheque());				
				chequeVO.setCpf(movCx.getCpfCnpjCheque());
				chequeVO.setCnpj(movCx.getCpfCnpjCheque());
				chequeVO.setValor(movCx.getValor());
			}
			FluxoCaixaVO flxCx = consultarPorCodigoFluxoCaixaAberto(new Date(), movCx.getFluxoCaixa(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			boolean criouNovoFluxoCaixa = false;
			if (flxCx == null) {
				if (possuiPermissaoMovimentarCaixaFechado) {
					Optional<ContaCorrenteVO> contaCorrenteOpt = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigoFluxoCaixa(movCx.getFluxoCaixa(), false, usuario);
					contaCorrenteOpt.orElseThrow(this::abrirCaixaFechadoExcecao);
					Function<UsuarioVO, Function<ContaCorrenteVO, FluxoCaixaVO>> atualizarFluxoCaixa = user -> conta -> atualizarFluxoCaixa(conta, user);
					flxCx = contaCorrenteOpt.map(atualizarFluxoCaixa.apply(usuario)).filter(Uteis::isAtributoPreenchido).orElseThrow(this::abrirCaixaFechadoExcecao);
					criouNovoFluxoCaixa = true;
				} else {
					throw new Exception("Não existe um fluxo de caixa aberto na data " + Uteis.getData(new Date(), "dd/MM/yyyy") + ", para a conta caixa.");
				}
			} 			
			if (movCx.getTipoMovimentacao().equals("EN")) {
				// Diminui o valor pois está voltando ao valor anterior à
				// movimentação
				validarSaldoDinheiroChequeFluxoCaixa(flxCx, movCx, usuario);
				flxCx.setSaldoFinal(flxCx.getSaldoFinal() - movCx.getValor());
				criarMovimentacaoCaixaEstornoMovimentacaoFinanceira(flxCx.getCodigo(), movCx.getFormaPagamento().getCodigo(), movCx.getCodigoOrigem(), movCx.getResponsavel().getCodigo(), "SA", movCx.getTipoOrigem(), movCx.getValor(), movCx.getPessoa().getCodigo(), movCx.getFornecedor().getCodigo(), movCx.getParceiro().getCodigo(), chequeVO, usuario);
			} else {
				// Aumenta o valor pois está voltando ao valor anterior à
				// movimentação
				flxCx.setSaldoFinal(flxCx.getSaldoFinal() + movCx.getValor());
				criarMovimentacaoCaixaEstornoMovimentacaoFinanceira(flxCx.getCodigo(), movCx.getFormaPagamento().getCodigo(), movCx.getCodigoOrigem(), movCx.getResponsavel().getCodigo(), "EN", movCx.getTipoOrigem(), movCx.getValor(), movCx.getPessoa().getCodigo(), movCx.getFornecedor().getCodigo(), movCx.getParceiro().getCodigo(), chequeVO, usuario);
			}
			alterarSaldoFluxoCaixa(flxCx);
			if (criouNovoFluxoCaixa && Uteis.isAtributoPreenchido(flxCx)) {
				getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(flxCx, usuario);
			}
		}
	}
	
	private Exception abrirCaixaFechadoExcecao() {
		return new Exception("Não foi possível abrir um fluxo de caixa para a data " + Uteis.getData(new Date(), "dd/MM/yyyy") + ", para a conta caixa.");
	}
	
	private FluxoCaixaVO atualizarFluxoCaixa(ContaCorrenteVO contaCorrenteVO, UsuarioVO usuario) {
		try {
			return getFacadeFactory().getMovimentacaoFinanceiraFacade().atualizarFluxoCaixa(contaCorrenteVO, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public void validarSaldoDinheiroChequeFluxoCaixa(FluxoCaixaVO fluxoCaixa, MovimentacaoCaixaVO movimentacaoCaixaVO, UsuarioVO usuarioVO) throws Exception {
		Double valorTotalCheque = 0.0;
		Double valorTotalDinheiro = 0.0;
		movimentacaoCaixaVO.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(movimentacaoCaixaVO.getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		String formaPagamento = movimentacaoCaixaVO.getFormaPagamento().getTipo();

		// fluxoCaixa =
		// getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new
		// Date(), fluxoCaixa.getContaCaixa().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		valorTotalCheque = Uteis.arrendondarForcando2CadasDecimais(getFacadeFactory().getFluxoCaixaFacade().executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(fluxoCaixa.getContaCaixa().getCodigo(), fluxoCaixa.getDataAbertura(), fluxoCaixa.getDataFechamento(), fluxoCaixa.getCodigo(), usuarioVO.getCodigo()));
		valorTotalDinheiro = Uteis.arrendondarForcando2CadasDecimais(getFacadeFactory().getFluxoCaixaFacade().executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(fluxoCaixa.getContaCaixa().getCodigo(), fluxoCaixa.getDataAbertura(), fluxoCaixa.getDataFechamento(), fluxoCaixa.getCodigo(), usuarioVO.getCodigo()));

		if (TipoFormaPagamento.DINHEIRO.getValor().equals(formaPagamento)) {
			Double saldoFluxoCaixaDinheiro = Uteis.arrendondarForcando2CadasDecimais(fluxoCaixa.getSaldoFinal() - valorTotalCheque);
			if (saldoFluxoCaixaDinheiro < valorTotalDinheiro) {
				throw new Exception("O saldo em dinheiro no caixa é insuficiente para realização da movimentação financeira");
			}
		} else if (TipoFormaPagamento.CHEQUE.getValor().equals(formaPagamento)) {
			Double saldoFluxoCaixaCheque = Uteis.arrendondarForcando2CadasDecimais(fluxoCaixa.getSaldoFinal() - valorTotalDinheiro);
			if (saldoFluxoCaixaCheque < valorTotalCheque) {
				throw new Exception("O saldo em cheque no caixa é insuficiente para realização da movimentação financeira");
			}
		}
	}

	public void validarSaldoDinheiroPagamento(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		if (obj.getContaCorrente().getContaCaixa()) {
			Double valorTotalDinheiro = 0.0;

			FluxoCaixaVO fluxoCaixa = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), obj.getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);

			if (fluxoCaixa == null) {
				throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + obj.getContaCorrente().getNumero() + "-" + obj.getContaCorrente().getDigito() + ")");
			}
			valorTotalDinheiro = executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(fluxoCaixa.getContaCaixa().getCodigo(), fluxoCaixa.getDataAbertura(), fluxoCaixa.getDataFechamento(), fluxoCaixa.getCodigo(), usuarioVO.getCodigo());
			if (obj.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())) {
				if (Uteis.arrendondarForcando2CadasDecimais(obj.getValor().doubleValue()) > Uteis.arrendondarForcando2CadasDecimais(valorTotalDinheiro)) {
					throw new Exception("O saldo em dinheiro no caixa é insuficiente para realização do pagamento.");
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void criarMovimentacaoCaixaEstornoMovimentacaoFinanceira(Integer fluxoCaixa, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, String tipoMovimentacao, String tipoOrigem, Double valor, Integer pessoa, Integer fornecedor, Integer parceiro, ChequeVO chequeVO, UsuarioVO usuario) throws Exception {
		MovimentacaoCaixaVO obj = new MovimentacaoCaixaVO();
		obj.setFluxoCaixa(fluxoCaixa);
		obj.getFormaPagamento().setCodigo(formaPagamento);
		obj.setCodigoOrigem(codigoOrigem);
		obj.getResponsavel().setCodigo(responsavel);
		obj.setTipoMovimentacao(tipoMovimentacao);
		obj.setTipoOrigem(tipoOrigem);
		obj.setValor(valor);
		obj.getPessoa().setCodigo(pessoa);
		obj.getFornecedor().setCodigo(fornecedor);
		obj.getParceiro().setCodigo(parceiro);
		
		if (chequeVO != null && chequeVO.getCodigo() > 0) {
			obj.setBanco(chequeVO.getBanco());
			obj.setNumeroCheque(chequeVO.getNumero());
			obj.setCheque(chequeVO.getCodigo());
			obj.setAgenciaCheque(chequeVO.getAgencia());
			obj.setContaCorrenteCheque(chequeVO.getNumeroContaCorrente());
			obj.setEmitenteCheque(chequeVO.getSacado());
			obj.setSacadoCheque(chequeVO.getPessoa().getNome());
			obj.setDataPrevisaoCheque(chequeVO.getDataPrevisao());
			obj.setPessoaJuridicaCheque(chequeVO.getEmitentePessoaJuridica());
			if (chequeVO.getEmitentePessoaJuridica()) {
				obj.setCpfCnpjCheque(chequeVO.getCnpj());
			} else {
				obj.setCpfCnpjCheque(chequeVO.getCpf());
			}
		}
		getFacadeFactory().getMovimentacaoCaixaFacade().incluir(obj, usuario);
	}

	public Boolean consultarFluxoCaixaAberto(Integer codigoCaixa, Integer unidadeEnsino, int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE codigo <> " + codigoCaixa.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " and situacao = 'A' ORDER BY dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return false;
		}
		return (true);
	}

	/**
	 * Responsável por realizar uma consulta de <code>FluxoCaixa</code> através
	 * do valor do atributo <code>numero</code> da classe
	 * <code>ContaCorrente</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>FluxoCaixaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNumeroContaCorrente(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT FluxoCaixa.* FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo and upper( ContaCorrente.numero ) like('" + valorConsulta.toUpperCase() + "%') ";
		if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
			sqlStr += "and FluxoCaixa.situacao = '" + situacao + "' ";
		}
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += "AND fluxoCaixa.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
		}
		if (usuario.getPessoa().getFuncionario() && usuario.getTipoUsuario().equals("FU")) {
			FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuario.getPessoa().getCodigo(), false, usuario);
			sqlStr += "AND (contacorrente.funcionarioresponsavel = " + func.getCodigo() + " or contacorrente.funcionarioresponsavel is null) ";
		}
		sqlStr += "ORDER BY FluxoCaixa.dataabertura desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>FluxoCaixa</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Usuario</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>FluxoCaixaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUsuario(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FluxoCaixa.* FROM FluxoCaixa, Usuario, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo AND FluxoCaixa.responsavelAbertura = Usuario.codigo and lower(Usuario.nome)  like ('" + valorConsulta.toLowerCase() + "%')  ";

		if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
			sqlStr += "and FluxoCaixa.situacao = '" + situacao + "' ";
		}
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += "AND fluxoCaixa.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
		}
		if (usuario.getPessoa().getFuncionario() && usuario.getTipoUsuario().equals("FU")) {
			FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuario.getPessoa().getCodigo(), false, usuario);
			sqlStr += "AND (contacorrente.funcionarioresponsavel = " + func.getCodigo() + " or contacorrente.funcionarioresponsavel is null) ";
		}
		sqlStr += " ORDER BY Usuario.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	// public List consultarPorNumeroContaCorrente(String valorConsulta, String
	// situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int
	// nivelMontarDados, UsuarioVO usuario) throws Exception {
	// ControleAcesso.consultar(getIdEntidade(), true, usuario);
	// String sqlStr =
	// "SELECT FluxoCaixa.* FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo and upper( ContaCorrente.numero ) like('"
	// + valorConsulta.toUpperCase() + "%') ";
	// if (dataIni != null && dataFim != null) {
	// if (situacao != null && !situacao.equals("")) {
	// sqlStr += " and ((FluxoCaixa.dataAbertura >= '" +
	// Uteis.getDataJDBCTimestamp(dataIni) +
	// "') and (FluxoCaixa.dataAbertura <= '" +
	// Uteis.getDataJDBCTimestamp(dataFim) + "')) ";
	// }
	// }
	// if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
	// sqlStr += "and FluxoCaixa.situacao = '" + situacao + "' ";
	// }
	// if (!(usuario.getUnidadeEnsinoLogado() == null ||
	// usuario.getUnidadeEnsinoLogado().getCodigo() == null ||
	// usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
	// sqlStr += "AND fluxoCaixa.unidadeEnsino = " +
	// usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
	// }
	// sqlStr += "ORDER BY ContaCorrente.numero";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	// }
	/**
	 * Responsável por realizar uma consulta de <code>FluxoCaixa</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Usuario</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>FluxoCaixaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List consultarPorNomeUsuario(String valorConsulta, String
	// situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int
	// nivelMontarDados, UsuarioVO usuario) throws Exception {
	// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr =
	// "SELECT FluxoCaixa.* FROM FluxoCaixa, Usuario WHERE FluxoCaixa.responsavelAbertura = Usuario.codigo and lower(Usuario.nome)  like ('"
	// + valorConsulta.toLowerCase() + "%')  ";
	//
	// if (dataIni != null && dataFim != null) {
	// if (situacao != null && !situacao.equals("")) {
	// sqlStr += " and ((FluxoCaixa.dataAbertura >= '" +
	// Uteis.getDataJDBCTimestamp(dataIni) +
	// "') and (FluxoCaixa.dataAbertura <= '" +
	// Uteis.getDataJDBCTimestamp(dataFim) + "')) ";
	// }
	// }
	// if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
	// sqlStr += "and FluxoCaixa.situacao = '" + situacao + "' ";
	// }
	// if (!(usuario.getUnidadeEnsinoLogado() == null ||
	// usuario.getUnidadeEnsinoLogado().getCodigo() == null ||
	// usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
	// sqlStr += "AND fluxoCaixa.unidadeEnsino = " +
	// usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
	// }
	// sqlStr += " ORDER BY Usuario.nome";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	// }
	/**
	 * Responsável por realizar uma consulta de <code>FluxoCaixa</code> através
	 * do valor do atributo <code>Date dataAbertura</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FluxoCaixaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataAbertura(Date prmIni, Date prmFim, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FluxoCaixa.* FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = contaCorrente.codigo AND ((FluxoCaixa.dataAbertura >= '" + Uteis.getDataJDBCTimestamp(prmIni) + "') and (FluxoCaixa.dataAbertura <= '" + Uteis.getDataJDBCTimestamp(prmFim) + "')) ";
		if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
			sqlStr += "and FluxoCaixa.situacao = '" + situacao + "'";
		}
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += "AND fluxoCaixa.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
		}
		if (usuario.getPessoa().getFuncionario() && usuario.getTipoUsuario().equals("FU")) {
			FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuario.getPessoa().getCodigo(), false, usuario);
			sqlStr += "AND (contacorrente.funcionarioresponsavel = " + func.getCodigo() + " or contacorrente.funcionarioresponsavel is null) ";
		}
		sqlStr += " ORDER BY FluxoCaixa.dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorDataAberturaCodigoContaCaixa(Integer contaCaixa, Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FluxoCaixa.* FROM FluxoCaixa WHERE FluxoCaixa.contaCaixa = " + contaCaixa + " AND FluxoCaixa.dataAbertura >= '" + Uteis.getDataJDBCTimestamp(prmIni) + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorFluxoCaixaAbertoHoje(Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE ((dataAbertura >= '" + Uteis.getDataJDBC(prmIni) + " 00:00') and (dataAbertura <= '" + Uteis.getDataJDBC(prmIni) + " 23:59')) and situacao = 'A'  ORDER BY dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public FluxoCaixaVO consultarPorFluxoCaixaAberto(Date prmIni, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE ((dataAbertura >= '" + Uteis.getDataJDBC(prmIni) + " 00:00') and (dataAbertura <= '" + Uteis.getDataJDBC(prmIni) + " 23:59')) and situacao = 'A' and contaCaixa = " + contaCorrente.intValue() + "  ORDER BY dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FluxoCaixaVO consultarPorCodigoFluxoCaixaAberto(Date prmIni, Integer fluxoCaixa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE ((dataAbertura >= '" + Uteis.getDataJDBC(prmIni) + " 00:00') and (dataAbertura <= '" + Uteis.getDataJDBC(prmIni) + " 23:59')) and situacao = 'A' and codigo = " + fluxoCaixa.intValue() + "  ORDER BY dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FluxoCaixaVO consultarUltimoFluxoCaixaPorContaCaixa(Integer contaCaixa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("select * from fluxocaixa where contacaixa = ").append(contaCaixa).append(" order by dataabertura desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));

	}

	@Override
	public FluxoCaixaVO consultarPorFluxoExistenciaCaixaAberto(Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE situacao= 'A' and contaCaixa = " + contaCorrente.intValue() + "  ORDER BY dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>FluxoCaixa</code> através
	 * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FluxoCaixaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE codigo >= " + valorConsulta.intValue() + " ";
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += "AND unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
		}
		sqlStr += "ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>FluxoCaixaVO</code>
	 *         resultantes da consulta.
	 */
	public List<FluxoCaixaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<FluxoCaixaVO> vetResultado = new ArrayList<FluxoCaixaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FluxoCaixaVO</code>.
	 * 
	 * @return O objeto da classe <code>FluxoCaixaVO</code> com os dados
	 *         devidamente montados.
	 */
	public FluxoCaixaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FluxoCaixaVO obj = new FluxoCaixaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataAbertura(dadosSQL.getTimestamp("dataAbertura"));
		obj.getResponsavelAbertura().setCodigo(dadosSQL.getInt("responsavelAbertura"));
		obj.getContaCaixa().setCodigo(dadosSQL.getInt("contaCaixa"));
		obj.setSaldoInicial(dadosSQL.getDouble("saldoInicial"));
		obj.setSaldoFinal(dadosSQL.getDouble("saldoFinal"));
		obj.setDataFechamento(dadosSQL.getTimestamp("dataFechamento"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		// NECESSÁRIO FICAR DENTRO DO FLAG DADOS MINIMOS.
		// montarDadosListaCheque(obj, usuario);
		// obj.setSaldoCheque(montarSaldoCheque(dadosSQL.getInt("contaCaixa"),
		// obj.getChequeVOs()));
		obj.setSaldoCheque(executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo()));
		obj.setSaldoDinheiro(executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo()));
		obj.setSaldoCartao(executarConsultaValorRecebidoCartaoSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo(), "CA"));
		obj.setSaldoBoletoBancario(consultarSaldoBoletoBancario(obj.getCodigo(), obj.getDataAbertura(), obj.getDataFechamento(), obj.getUnidadeEnsino(), false, 0));
		obj.setSaldoCartaoDebito(executarConsultaValorRecebidoCartaoSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo(),"CD"));
		obj.setSaldoCreditoDebitoContaCorrente(executarConsultaValorRecebidoCreditoDebitoContaCorrenteSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo()));
		obj.setSaldoIsencao(executarConsultaValorRecebidoIsencaoSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo()));
		obj.setSaldoPermuta(executarConsultaValorRecebidoPermutaSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo()));
		obj.setSaldoDeposito(executarConsultaValorRecebidoDepositoSaldoFechamentoCaixa(dadosSQL.getInt("contaCaixa"), obj.getDataAbertura(), obj.getDataFechamento(), obj.getCodigo(), usuario.getCodigo()));

		obj.setUnidadeEnsino(dadosSQL.getInt("unidadeEnsino"));
		obj.getResponsavelFechamento().setCodigo(dadosSQL.getInt("responsavelFechamento"));
//		obj.setSaldoDeposito(dadosSQL.getDouble("saldoDeposito"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosResponsavelAbertura(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosContaCaixa(obj, nivelMontarDados, usuario);
			return obj;
		}
		obj.setMovimentacaoCaixaVOs(getFacadeFactory().getMovimentacaoCaixaFacade().consultarPorFluxoCaixaFormaPagamentoDinheiroECheque(obj.getCodigo(), nivelMontarDados, false, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		// montarDadosResponsavelAbertura(obj, nivelMontarDados, usuario);
		montarDadosContaCaixa(obj, nivelMontarDados, usuario);
		montarDadosResponsavelFechamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public Double executarConsultaValorRecebidoCartaoSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario, String tipoCartao) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where (fp.tipo = '").append(tipoCartao).append("')");
			sql.append(" and mc.tipomovimentacao = 'EN' and fc.codigo = " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where (fp.tipo = '").append(tipoCartao).append("')");
			sql.append(" and mc.tipomovimentacao = 'EN' and fc.codigo = " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where (fp.tipo = '").append(tipoCartao).append("')");
			 sql.append(" and mc.tipomovimentacao = 'SA' and fc.codigo = " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where (fp.tipo = '").append(tipoCartao).append("')");
			sql.append(" and mc.tipomovimentacao = 'SA' and fc.codigo = " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}
		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}

	public Double executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}
		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}

	public Double executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + "  and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + "  and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}

	public static Double montarSaldoCheque(Integer codContaCorrente, List cheques) {
		try {
			ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(codContaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			if (conta.getContaCaixa()) {
				Double valorTotalCheque = 0.0;
				Iterator i = cheques.iterator();
				while (i.hasNext()) {
					ChequeVO cheque = (ChequeVO) i.next();
					valorTotalCheque = valorTotalCheque + cheque.getValor();
				}
				return valorTotalCheque;
			} else {
				return 0.0;
			}
		} catch (Exception e) {
			return 0.0;
		}
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UsuarioVO</code> relacionado ao objeto <code>FluxoCaixaVO</code>.
	 * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar
	 * a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosListaCheque(FluxoCaixaVO obj, UsuarioVO usuario) throws Exception {
		List cheques = getFacadeFactory().getChequeFacade().consultarPorLocalizacaoSituacaoCheque(obj.getContaCaixa().getCodigo(), "EC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		obj.setChequeVOs(cheques);
	}

	public static void montarDadosResponsavelFechamento(FluxoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelFechamento().getCodigo().intValue() == 0) {
			obj.setResponsavelFechamento(new UsuarioVO());
			return;
		}
		obj.setResponsavelFechamento(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelFechamento().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ContaCorrenteVO</code> relacionado ao objeto
	 * <code>FluxoCaixaVO</code>. Faz uso da chave primária da classe
	 * <code>ContaCorrenteVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosContaCaixa(FluxoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCaixa().getCodigo().intValue() == 0) {
			obj.setContaCaixa(new ContaCorrenteVO());
			return;
		}
		obj.setContaCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCaixa().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UsuarioVO</code> relacionado ao objeto <code>FluxoCaixaVO</code>.
	 * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar
	 * a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelAbertura(FluxoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAbertura().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelAbertura(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAbertura().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>FluxoCaixaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public FluxoCaixaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM FluxoCaixa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FluxoCaixa ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return FluxoCaixa.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		FluxoCaixa.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSaldoCartaoCaixa(final FluxoCaixaVO obj) throws Exception {
		final String sql = "UPDATE FluxoCaixa set saldoCartao=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getSaldoCartao().doubleValue());
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSaldoDeposito(final FluxoCaixaVO obj) throws Exception {
		final String sql = "UPDATE FluxoCaixa set saldoDeposito=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getSaldoDeposito().doubleValue());
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void criarMovimentacaoCaixaPagamentoCartao(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, Integer operadoraCartao, UsuarioVO usuarioLogado, Boolean alterarSaldoCartao) throws Exception {
		FluxoCaixaVO fluxoCaixa = new FluxoCaixaVO();

		try {

			fluxoCaixa = consultarPorFluxoCaixaAberto(new Date(), contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			if (fluxoCaixa == null) {
				ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
				throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
			}

			if (alterarSaldoCartao) {
				if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.ENTRADA.getValor())) {
					fluxoCaixa.setSaldoCartao(fluxoCaixa.getSaldoCartao() + valor);
				} else {
					fluxoCaixa.setSaldoCartao(fluxoCaixa.getSaldoCartao() - valor);
				}
				alterarSaldoCartaoCaixa(fluxoCaixa);
			} else {
				fluxoCaixa.setSaldoFinal(fluxoCaixa.getSaldoFinal() - valor);
				alterarSaldoFluxoCaixa(fluxoCaixa);
			}

			MovimentacaoCaixaVO movimentacaoCaixaVO = getFacadeFactory().getMovimentacaoCaixaFacade().executarGeracaoMovimentacaoCaixa(null, fluxoCaixa, pessoa, parceiro, fornecedor, responsavel, operadoraCartao, banco, codigoOrigem, formaPagamento, valor, tipoOrigem, tipoMovimentacao);
			getFacadeFactory().getMovimentacaoCaixaFacade().incluir(movimentacaoCaixaVO, usuarioLogado);
		} finally {
			fluxoCaixa = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void criarMovimentacaoCaixaPagamentoDepositoConta(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, UsuarioVO usuarioLogado) throws Exception {
		MovimentacaoCaixaVO movimentacaoCaixaVO = new MovimentacaoCaixaVO();
		FluxoCaixaVO fluxoCaixa = new FluxoCaixaVO();

		try {

			fluxoCaixa = consultarPorFluxoCaixaAberto(new Date(), contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			if (fluxoCaixa == null) {
				ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
				throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
			}
			if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.ENTRADA.getValor())) {
				fluxoCaixa.setSaldoDeposito(fluxoCaixa.getSaldoDeposito() + valor);
			} else {
				fluxoCaixa.setSaldoDeposito(fluxoCaixa.getSaldoDeposito() - valor);
			}
			movimentacaoCaixaVO.setFluxoCaixa(fluxoCaixa.getCodigo());
			movimentacaoCaixaVO.getFormaPagamento().setCodigo(formaPagamento);
			movimentacaoCaixaVO.setCodigoOrigem(codigoOrigem);
			movimentacaoCaixaVO.getResponsavel().setCodigo(responsavel);
			movimentacaoCaixaVO.setTipoMovimentacao(tipoMovimentacao);
			movimentacaoCaixaVO.setTipoOrigem(tipoOrigem);
			movimentacaoCaixaVO.setValor(valor);
			movimentacaoCaixaVO.getPessoa().setCodigo(pessoa);
			movimentacaoCaixaVO.getFornecedor().setCodigo(fornecedor);
			movimentacaoCaixaVO.getParceiro().setCodigo(parceiro);
			getFacadeFactory().getMovimentacaoCaixaFacade().incluir(movimentacaoCaixaVO, usuarioLogado);

			alterarSaldoDeposito(fluxoCaixa);

		} finally {
			movimentacaoCaixaVO = null;
			fluxoCaixa = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void criarMovimentacaoCaixaPagamentoCheque(FormaPagamentoNegociacaoRecebimentoVO fpnrVO, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, Integer operadoraCartao, UsuarioVO usuarioLogado) throws Exception {
		Double saldoContaCorrente = 0.0;
		FluxoCaixaVO fluxoCaixaVO = new FluxoCaixaVO();
		ChequeVO chequeVO = new ChequeVO();

		try {
			fluxoCaixaVO = consultarPorFluxoCaixaAberto(new Date(), contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			chequeVO = getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(fpnrVO.getCheque().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);

			if (fluxoCaixaVO == null) {
				ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
				throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
			}
			if (chequeVO.getSituacao().equals(SituacaoCheque.BANCO.getValor())) {
				saldoContaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarSaldoContaCorrente(chequeVO.getLocalizacaoCheque().getCodigo());
				saldoContaCorrente = saldoContaCorrente - fpnrVO.getValorRecebimento();
				getFacadeFactory().getContaCorrenteFacade().alterarSaldoContaCorrente(chequeVO.getLocalizacaoCheque().getCodigo(), saldoContaCorrente, usuarioLogado);
			}
			if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.ENTRADA.getValor())) {
				fluxoCaixaVO.setSaldoFinal(fluxoCaixaVO.getSaldoFinal() + fpnrVO.getValorRecebimento());
			} else {
				if (!fluxoCaixaVO.getContaCaixa().getCodigo().equals(chequeVO.getLocalizacaoCheque().getCodigo())) {
					ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
					throw new Exception(UteisJSF.internacionalizar("msg_FluxoCaixa_chequeNaoEstaNoCaixa").replace("{0}", conta.getNumero() + "-" + conta.getDigito()));
				}
				fluxoCaixaVO.setSaldoFinal(fluxoCaixaVO.getSaldoFinal() - fpnrVO.getValorRecebimento());
				fluxoCaixaVO.setSaldoCheque(fluxoCaixaVO.getSaldoCheque() - fpnrVO.getValorRecebimento());
				if (fluxoCaixaVO.getSaldoCheque() < 0) {
					ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
					throw new Exception("Saldo insuficiente para realizar este operação na conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
				}
				if (chequeVO.getSituacao().equals(SituacaoCheque.PENDENTE.getValor()) || chequeVO.getSituacao().equals(SituacaoCheque.EM_CAIXA.getValor())) {
					getFacadeFactory().getChequeFacade().inutilizarCheque(chequeVO.getCodigo());
				}
			}
			MovimentacaoCaixaVO movimentacaoCaixaVO = getFacadeFactory().getMovimentacaoCaixaFacade().executarGeracaoMovimentacaoCaixa(chequeVO, fluxoCaixaVO, pessoa, parceiro, fornecedor, responsavel, operadoraCartao, banco, codigoOrigem, formaPagamento, fpnrVO.getValorRecebimento(), tipoOrigem, tipoMovimentacao);
			getFacadeFactory().getMovimentacaoCaixaFacade().incluir(movimentacaoCaixaVO, usuarioLogado);
			alterarSaldoFluxoCaixa(fluxoCaixaVO);
		} finally {
			saldoContaCorrente = null;
			fluxoCaixaVO = null;
			chequeVO = null;
		}

	}

	public Double consultarSaldoBoletoBancario(Integer codigoPrm, Date dataInicio, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" SELECT SUM(fpnr.valorrecebimento) as valorRecebidoBoletoBancario FROM fluxocaixa fc ");
		sql.append(" INNER JOIN contacorrente cc ON fc.contacaixa = cc.codigo ");
		sql.append(" INNER JOIN negociacaorecebimento nr ON nr.contacorrentecaixa = fc.contacaixa ");
		sql.append(" INNER JOIN formapagamentonegociacaorecebimento fpnr ON fpnr.negociacaorecebimento = nr.codigo ");
		sql.append(" INNER JOIN formapagamento fp ON fpnr.formapagamento = fp.codigo ");
		sql.append(" INNER JOIN unidadeensino ue ON ue.codigo = fc.unidadeensino");
		sql.append(" INNER JOIN usuario u ON u.codigo = fc.responsavelabertura ");
		sql.append(" WHERE fp.tipo = 'BO' AND fc.codigo = ").append(codigoPrm);
		sql.append(" AND fc.dataabertura between '").append(Uteis.getDataJDBC(dataInicio) + " 00:00:00").append("' AND '").append(Uteis.getDataJDBC(dataFim) + " 23:59:59").append("' ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" AND ue.codigo = ").append(unidadeEnsino).append(" ");
		}
		if (usuario != null && usuario > 0) {
			sql.append(" AND u.codigo = ").append(usuario).append(" ");
		}
		sql.append(" and nr.data between fc.dataabertura and (case when (fc.responsavelfechamento is null) then now() else fc.datafechamento end) ");
		sql.append(" group by fc.codigo; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valorRecebidoBoletoBancario");
		} else {
			return 0.0;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void validarSaldoCaixa(Double valorTroco, Double valorRecebimento, Integer contaCorrente, UsuarioVO usuario) throws Exception {
		FluxoCaixaVO caixa = consultarPorFluxoCaixaAberto(new Date(), contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (caixa == null) {
			ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
		}
		caixa.setSaldoFinal(caixa.getSaldoFinal() + (Uteis.arredondar(valorRecebimento - valorTroco, 2, 0)));
		if (caixa.getSaldoFinal().doubleValue() < valorTroco) {
			ContaCorrenteVO conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			throw new Exception("Saldo insuficiente para realizar esta operação na conta caixa (" + conta.getNumero() + "-" + conta.getDigito() + ")");
		}
	}
	
	/**
	 * Responsável por executar a geracao da movimentação caixa se realizar alteração no saldo do fluxo de caixa de <code>TipoFormaPagamento</code>
	 * BOLETO_BANCARIO, DEPOSITO, DEBITO_EM_CONTA_CORRENTE, ISENCAO, PERMUTA. Para tal, só é possível realizar geração da movimentação caixa nestes
	 * casos quando a conta caixa estiver selecionada.
	 * 
	 * @author Wellington - 18 de mar de 2016
	 * @param valor
	 * @param contaCorrente
	 * @param tipoMovimentacao
	 * @param tipoOrigem
	 * @param formaPagamento
	 * @param codigoOrigem
	 * @param responsavel
	 * @param pessoa
	 * @param fornecedor
	 * @param banco
	 * @param parceiro
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public synchronized void executarGeracaoMovimentacaoCaixaBoletoBancarioDepositoDebitoContaCorrenteIsencaoPermuta(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, Integer operadoraCartao, UsuarioVO usuario) throws Exception {
		FluxoCaixaVO fluxoCaixaVO = consultarPorFluxoCaixaAberto(new Date(), contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (!Uteis.isAtributoPreenchido(fluxoCaixaVO)) {
			ContaCorrenteVO contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + contaCorrenteVO.getNumero() + "-" + contaCorrenteVO.getDigito() + ")");
		}
		MovimentacaoCaixaVO movimentacaoCaixaVO = getFacadeFactory().getMovimentacaoCaixaFacade().executarGeracaoMovimentacaoCaixa(null, fluxoCaixaVO, pessoa, parceiro, fornecedor, responsavel, operadoraCartao, banco, codigoOrigem, formaPagamento, valor, tipoOrigem, tipoMovimentacao);
		getFacadeFactory().getMovimentacaoCaixaFacade().incluir(movimentacaoCaixaVO, usuario);
	}
	
	public Double executarConsultaValorRecebidoCreditoDebitoContaCorrenteSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'DE' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'DE' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'DE' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'DE' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}
		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}
	
	public Double executarConsultaValorRecebidoIsencaoSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'IS' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'IS' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'IS' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'IS' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}
		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}
	
	public Double executarConsultaValorRecebidoPermutaSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'PE' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'PE' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'PE' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'PE' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}
		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}
	
	public Double executarConsultaValorRecebidoDepositoSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'DC' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'DC' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		sql = new StringBuilder();
		sql.append("select (select sum(valor) from movimentacaocaixa mc ");
		sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
		sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
		sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
		if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
			sql.append("where fp.tipo = 'DC' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "')::numeric as t ");
		} else {
			sql.append("where fp.tipo = 'DC' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "')::numeric as t ");
		}
		SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BigDecimal entrada = BigDecimal.ZERO.setScale(2);
		BigDecimal saida = BigDecimal.ZERO.setScale(2);
		if (tabelaResultado.next()) {
			entrada = tabelaResultado.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado.getBigDecimal("t");
		}
		if (tabelaResultado2.next()) {
			saida = tabelaResultado2.getBigDecimal("t") == null ? BigDecimal.ZERO : tabelaResultado2.getBigDecimal("t");
		}
		return entrada.subtract(saida).doubleValue();
	}

	@Override
	public void atualizarDadosSaldoInicial(FluxoCaixaVO fluxoCaixaVO, UsuarioVO usuario) throws Exception {
		fluxoCaixaVO.reiniciarControleBloqueioCompetencia();
		if (fluxoCaixaVO.getContaCaixa().getCodigo().intValue() != 0) {
			FluxoCaixaVO fluxoCaixaUltimo = getFacadeFactory().getFluxoCaixaFacade().consultarUltimoFluxoCaixaPorContaCaixa(fluxoCaixaVO.getContaCaixa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			fluxoCaixaVO.setContaCaixa((ContaCorrenteVO)getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(fluxoCaixaVO.getContaCaixa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario).clone());
			if (fluxoCaixaUltimo == null) {
				fluxoCaixaVO.setSaldoInicial(fluxoCaixaVO.getContaCaixa().getSaldo());
				fluxoCaixaVO.setSaldoFinal(fluxoCaixaVO.getContaCaixa().getSaldo());
			} else {
				fluxoCaixaVO.setSaldoInicial(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoFinal()));
				fluxoCaixaVO.setSaldoFinal(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoFinal()));
				fluxoCaixaVO.setSaldoDinheiro(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoDinheiro()));
				fluxoCaixaVO.setSaldoCheque(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoCheque()));
				fluxoCaixaVO.setSaldoCartao(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoCartao()));
				fluxoCaixaVO.setSaldoDeposito(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoDeposito()));
			}
			// fluxoCaixaVO.setUnidadeEnsino(getContaCorrenteVO().getUnidadeEnsino().getCodigo());
			if(fluxoCaixaVO.getContaCaixa().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
				throw new Exception("A CONTA CAIXA "+fluxoCaixaVO.getContaCaixa().getDescricaoParaComboBox()+" não possuiu unidade de ensino vinculado a mesma.");
			}
			fluxoCaixaVO.setUnidadeEnsino(fluxoCaixaVO.getContaCaixa().getUnidadeEnsinoContaCorrenteVOs().get(0).getUnidadeEnsino().getCodigo());
		} else {
			fluxoCaixaVO.setSaldoInicial(0.0);
			fluxoCaixaVO.setSaldoFinal(0.0);
			fluxoCaixaVO.setUnidadeEnsino(0);
		}
	}

	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacao) throws Exception {
		StringBuilder sql = new StringBuilder();
		switch (EnumCampoConsultaFluxoCaixa.valueOf(dataModelo.getCampoConsulta())) {
		case CONTA_CAIXA:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + "%");
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + "%");
			sql.append("SELECT FluxoCaixa.* FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo and (sem_acentos( ContaCorrente.numero ) ilike(sem_acentos(?)) or sem_acentos( ContaCorrente.nomeApresentacaoSistema ) ilike(sem_acentos(?)))");
			dataModelo.setListaConsulta(consultarFluxoCaixa(dataModelo, sql, situacao));

			sql = new StringBuilder("");
			sql.append("SELECT COUNT(FluxoCaixa.codigo) as qtde FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo and (sem_acentos( ContaCorrente.numero ) ilike(sem_acentos(?)) or sem_acentos( ContaCorrente.nomeApresentacaoSistema ) ilike(sem_acentos(?)))");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalRegistroFluxoCaixa(dataModelo, sql, situacao));
			break;
		case DATA_ABERTURA:
			sql.append("SELECT FluxoCaixa.* FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = contaCorrente.codigo AND");
			sql.append("((FluxoCaixa.dataAbertura >= '" + Uteis.getDataJDBCTimestamp(dataModelo.getDataIni()) + "') and (FluxoCaixa.dataAbertura <= '" + Uteis.getDataJDBCTimestamp(dataModelo.getDataFim()) + "')) ");
			dataModelo.setListaConsulta(consultarFluxoCaixa(dataModelo, sql, situacao));

			sql = new StringBuilder("");
			sql.append("SELECT COUNT(FluxoCaixa.codigo) as qtde FROM FluxoCaixa, ContaCorrente WHERE FluxoCaixa.contaCaixa = contaCorrente.codigo AND");
			sql.append("((FluxoCaixa.dataAbertura >= '" + Uteis.getDataJDBCTimestamp(dataModelo.getDataIni()) + "') and (FluxoCaixa.dataAbertura <= '" + Uteis.getDataJDBCTimestamp(dataModelo.getDataFim()) + "')) ");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalRegistroFluxoCaixa(dataModelo, sql, situacao));
			break;
		case RESPONSAVEL_ABERTURA:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + "%");
			sql.append("SELECT FluxoCaixa.* FROM FluxoCaixa, Usuario, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo AND FluxoCaixa.responsavelAbertura = Usuario.codigo and lower(Usuario.nome) ilike (?) ");
			dataModelo.setListaConsulta(consultarFluxoCaixa(dataModelo, sql, situacao));

			sql = new StringBuilder("");
			sql.append("SELECT COUNT(FluxoCaixa.codigo) as qtde FROM FluxoCaixa, Usuario, ContaCorrente WHERE FluxoCaixa.contaCaixa = ContaCorrente.codigo AND FluxoCaixa.responsavelAbertura = Usuario.codigo and lower(Usuario.nome)  ilike (?) ");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalRegistroFluxoCaixa(dataModelo, sql, situacao));
			break;
		default:
			break;
		}
	}

	private List<FluxoCaixaVO> consultarFluxoCaixa(DataModelo dataModelo, StringBuilder sql, String situacao) throws Exception {
        if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
			sql.append("and FluxoCaixa.situacao = '" + situacao + "'");
		}

        if (!(dataModelo.getUsuario().getUnidadeEnsinoLogado() == null || dataModelo.getUsuario().getUnidadeEnsinoLogado().getCodigo() == null || dataModelo.getUsuario().getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sql.append("AND fluxoCaixa.unidadeEnsino = " + dataModelo.getUsuario().getUnidadeEnsinoLogado().getCodigo() + " ");
		}

		if (dataModelo.getUsuario().getPessoa().getFuncionario() && dataModelo.getUsuario().getTipoUsuario().equals("FU")) {
			FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(dataModelo.getUsuario().getPessoa().getCodigo(), false, dataModelo.getUsuario());
			sql.append("AND (contacorrente.funcionarioresponsavel = " + func.getCodigo() + " or contacorrente.funcionarioresponsavel is null) ");
		}
		sql.append(" ORDER BY FluxoCaixa.dataAbertura");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

        List<FluxoCaixaVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, dataModelo.getUsuario()));
        }

        return lista;
	}

	private Integer consultarTotalRegistroFluxoCaixa(DataModelo dataModelo, StringBuilder sql, String situacao) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        if (situacao != null && !situacao.equals("") && !situacao.equals("T")) {
			sql.append("and FluxoCaixa.situacao = '" + situacao + "'");
		}

        if (!(dataModelo.getUsuario().getUnidadeEnsinoLogado() == null || dataModelo.getUsuario().getUnidadeEnsinoLogado().getCodigo() == null || dataModelo.getUsuario().getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sql.append("AND fluxoCaixa.unidadeEnsino = " + dataModelo.getUsuario().getUnidadeEnsinoLogado().getCodigo() + " ");
		}

		if (dataModelo.getUsuario().getPessoa().getFuncionario() && dataModelo.getUsuario().getTipoUsuario().equals("FU")) {
			FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(dataModelo.getUsuario().getPessoa().getCodigo(), false, dataModelo.getUsuario());
			sql.append("AND (contacorrente.funcionarioresponsavel = " + func.getCodigo() + " or contacorrente.funcionarioresponsavel is null) ");
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	public FluxoCaixaVO consultarPorFluxoCaixaAbertoEmDataRetroativa(Date prmIni, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE dataAbertura::date < ? and situacao = 'A'  and contaCaixa = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr , Uteis.getDataJDBC(prmIni),contaCorrente );
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public Boolean validarExistenciaFluxoCaixaAbertoDataAtual(Date prmIni, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FluxoCaixa WHERE ((dataAbertura >= '" + Uteis.getDataJDBC(prmIni) + " 00:00') and (dataAbertura <= '" + Uteis.getDataJDBC(prmIni) + " 23:59')) and situacao = 'A' and contaCaixa = " + contaCorrente.intValue() + "  ORDER BY dataAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return false;
		}
		return true;
	}
	
}
