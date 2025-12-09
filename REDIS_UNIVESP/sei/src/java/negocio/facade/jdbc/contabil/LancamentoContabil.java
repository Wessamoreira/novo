package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.OrdenarConfiguracaoContabilRegraEnum;
import negocio.comuns.contabil.enumeradores.SituacaoLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoValorLancamentoContabilEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoDesconto;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.LancamentoContabilInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class LancamentoContabil extends ControleAcesso implements LancamentoContabilInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4555116183108268182L;
	protected static String idEntidade = "LancamentoContabil";

	public LancamentoContabil() {
		super();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDados(LancamentoContabilVO obj) {
		if (Uteis.isAtributoPreenchido(obj.getIntegracaoContabilVO())) {
			throw new StreamSeiException("Não é possivel realizar essa operação, pois os lançamentos já estão em uma integração contábil de código: " + obj.getIntegracaoContabilVO().getCodigo());
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(LancamentoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		List<LancamentoContabilCentroNegocioVO> listaTemp = new ArrayList<>();
		listaTemp.addAll(obj.getListaCentroNegocioAcademico());
		listaTemp.addAll(obj.getListaCentroNegocioAdministrativo());
		validarSeRegistroForamExcluidoDasListaSubordinadas(listaTemp, "LancamentoContabilCentroNegocio", "LancamentoContabil", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getLancamentoContabilCentroNegocioFacade().persistir(listaTemp, false, usuarioVO);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final LancamentoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			LancamentoContabil.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO LancamentoContabil (unidadeEnsino, planoconta, contacorrente, dataregistro, nomelancamento, ");
			sql.append("    valor, tiposacado, fornecedor, funcionario, banco, pessoa, parceiro, tipoorigemlancamentocontabil, ");
			sql.append("    codorigem, tipovalorlancamentocontabil, situacaoLancamentoContabil, dataCompensacao, tipoPlanoConta, categoriaproduto,   ");
			sql.append("    imposto ) ");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setInt(++i, obj.getPlanoContaVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteVO())) {
						sqlInserir.setInt(++i, obj.getContaCorrenteVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlInserir.setString(++i, obj.getNomeLancamento());
					sqlInserir.setDouble(++i, obj.getValor());
					sqlInserir.setString(++i, obj.getTipoSacado().name());
					if (Uteis.isAtributoPreenchido(obj.getFornecedorVO())) {
						sqlInserir.setInt(++i, obj.getFornecedorVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioVO())) {
						sqlInserir.setInt(++i, obj.getFuncionarioVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getBancoVO())) {
						sqlInserir.setInt(++i, obj.getBancoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
						sqlInserir.setInt(++i, obj.getPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getParceiroVO())) {
						sqlInserir.setInt(++i, obj.getParceiroVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoOrigemLancamentoContabilEnum().name());
					sqlInserir.setString(++i, obj.getCodOrigem());
					sqlInserir.setString(++i, obj.getTipoValorLancamentoContabilEnum().name());
					sqlInserir.setString(++i, obj.getSituacaoLancamentoContabilEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getDataCompensacao())) {
						sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataCompensacao()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoPlanoConta().name());
					Uteis.setValuePreparedStatement(obj.getCategoriaProdutoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getImpostoVO(), ++i, sqlInserir);					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final LancamentoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			LancamentoContabil.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE LancamentoContabil ");
			sql.append("   SET unidadeEnsino=?, planoconta=?, contacorrente=?, dataregistro=?, nomelancamento=?, ");
			sql.append("    valor=?, tiposacado=?, fornecedor=?, funcionario=?, banco=?, pessoa=?, ");
			sql.append("    parceiro=?, tipoorigemlancamentocontabil=?, codorigem=?, tipovalorlancamentocontabil=?, ");
			sql.append("    situacaoLancamentoContabil =?, datacompensacao=?, tipoPlanoConta = ?, ");
			sql.append("    categoriaProduto =?, imposto=? ");
			sql.append("    WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getPlanoContaVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getContaCorrenteVO())) {
						sqlAlterar.setInt(++i, obj.getContaCorrenteVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlAlterar.setString(++i, obj.getNomeLancamento());
					sqlAlterar.setDouble(++i, obj.getValor());
					sqlAlterar.setString(++i, obj.getTipoSacado().name());
					if (Uteis.isAtributoPreenchido(obj.getFornecedorVO())) {
						sqlAlterar.setInt(++i, obj.getFornecedorVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioVO())) {
						sqlAlterar.setInt(++i, obj.getFuncionarioVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getBancoVO())) {
						sqlAlterar.setInt(++i, obj.getBancoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
						sqlAlterar.setInt(++i, obj.getPessoaVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getParceiroVO())) {
						sqlAlterar.setInt(++i, obj.getParceiroVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTipoOrigemLancamentoContabilEnum().name());
					sqlAlterar.setString(++i, obj.getCodOrigem());
					sqlAlterar.setString(++i, obj.getTipoValorLancamentoContabilEnum().name());
					sqlAlterar.setString(++i, obj.getSituacaoLancamentoContabilEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getDataCompensacao())) {
						sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataCompensacao()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTipoPlanoConta().name());
					Uteis.setValuePreparedStatement(obj.getCategoriaProdutoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImpostoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarSeLancamentoContabilFoiExcluido(List<LancamentoContabilVO> lista, Map<TipoOrigemLancamentoContabilEnum, String> mapaOrigemLancmentoContabil, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		String filtros = preencherFiltrosParaLancamentoContabilExcluido(lista, mapaOrigemLancmentoContabil);
		sb.append(" Select integracaoContabil, tipoorigemlancamentocontabil, codorigem FROM LancamentoContabil WHERE integracaoContabil is not null");
		sb.append(filtros);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		validarSeLancamentoContabilAptaExclusao(usuario, rs);
		sb = new StringBuilder("DELETE FROM LancamentoContabil where 1=1  ");
		sb.append(filtros);
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
	}

	private String preencherFiltrosParaLancamentoContabilExcluido(List<LancamentoContabilVO> lista, Map<TipoOrigemLancamentoContabilEnum, String> mapaOrigemLancmentoContabil) {
		StringBuilder sb = new StringBuilder("");
		boolean adicionarOr = false;
		sb.append(" and (");
		for (Map.Entry<TipoOrigemLancamentoContabilEnum, String> mapa : mapaOrigemLancmentoContabil.entrySet()) {
			if (adicionarOr) {
				sb.append(" or ");
			}
			sb.append("( codorigem in (").append(mapa.getValue()).append(")  ");
			sb.append(" and tipoorigemlancamentocontabil ='").append(mapa.getKey().name()).append("')");
			adicionarOr = true;
		}
		sb.append(") ");
		if (Uteis.isAtributoPreenchido(lista)) {
			sb.append(" and codigo not in ( 0  ");
			for (LancamentoContabilVO obj : lista) {
				if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
					sb.append(", ").append(obj.getCodigo());
				}
			}
			sb.append(") ");
		}
		return sb.toString();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarLancamentoContabilPorIntegracaoContabil(IntegracaoContabilVO ic, boolean anular, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(" UPDATE LancamentoContabil ");
		if (anular) {
			sb.append(" SET integracaoContabil= null , ");
			sb.append(" situacaolancamentocontabil= '").append(SituacaoLancamentoContabilEnum.COMPENSADO.name()).append("' ");
			sb.append(" WHERE integracaoContabil = ").append(ic.getCodigo()).append(" ");
		} else {
			sb.append(" SET integracaoContabil= ").append(ic.getCodigo()).append(", ");
			sb.append(" situacaolancamentocontabil= '").append(SituacaoLancamentoContabilEnum.CONTABILIZADO.name()).append("' ");
			sb.append(" WHERE codigo in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(ic.getListaLancamentoContabil())).append(")");
		}
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoLancamentoContabilPorCheque(List<Integer> cheques, boolean isRecebimento, UsuarioVO usuario) throws Exception {
		if (!cheques.isEmpty()) {
			StringBuilder sql = new StringBuilder("Update lancamentocontabil set ");
			sql.append(" situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.COMPENSADO.name()).append("', ");
			sql.append(" datacompensacao = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' ");
			sql.append(" where codigo in ( ");
			sql.append(" select distinct lancamentocontabil.codigo from lancamentocontabil ");
			if (isRecebimento) {
				sql.append(" inner join ContaReceberRecebimento on ContaReceberRecebimento.contareceber::varchar  = lancamentocontabil.codorigem  ");
				sql.append(" inner join FormaPagamentoNegociacaoRecebimento on ContaReceberRecebimento.FormaPagamentoNegociacaoRecebimento = FormaPagamentoNegociacaoRecebimento.codigo ");
				sql.append(" where lancamentocontabil.situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.AGUARDANDO_COMPENSACAO.name()).append("' ");
				sql.append(" and lancamentocontabil.tipoorigemlancamentocontabil = '").append(TipoOrigemLancamentoContabilEnum.RECEBER.name()).append("' ");
				sql.append(" and FormaPagamentoNegociacaoRecebimento.cheque in ( ").append(UteisTexto.converteListaInteiroParaString(cheques)).append(") ");
			} else {
				sql.append(" inner join contapagarpagamento on contapagarpagamento.contapagar::varchar  = lancamentocontabil.codorigem  ");
				sql.append(" inner join formapagamentonegociacaopagamento on contapagarpagamento.formapagamentonegociacaopagamento = formapagamentonegociacaopagamento.codigo ");
				sql.append(" where lancamentocontabil.situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.AGUARDANDO_COMPENSACAO.name()).append("' ");
				sql.append(" and lancamentocontabil.tipoorigemlancamentocontabil = '").append(TipoOrigemLancamentoContabilEnum.PAGAR.name()).append("' ");
				sql.append(" and formapagamentonegociacaopagamento.cheque in ( ").append(UteisTexto.converteListaInteiroParaString(cheques)).append(") ");

			}
			sql.append(" ) ");
			getConexao().getJdbcTemplate().execute(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoLancamentoContabilPorMapaPendenciaCartaoCredito(List<Integer> listaMapaPendenciaCartaoCreditoVO, Date dataBaixa, UsuarioVO usuario) throws Exception {
		if (!listaMapaPendenciaCartaoCreditoVO.isEmpty()) {
			StringBuilder sql = new StringBuilder("Update lancamentocontabil set ");
			sql.append(" situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.COMPENSADO.name()).append("', ");
			sql.append(" datacompensacao = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' ");
			sql.append(" where codigo in ( ");
			sql.append(" select distinct lancamentocontabil.codigo from lancamentocontabil ");
			sql.append(" inner join ContaReceberRecebimento on ContaReceberRecebimento.contareceber::varchar  = lancamentocontabil.codorigem  ");
			sql.append(" inner join FormaPagamentoNegociacaoRecebimento on ContaReceberRecebimento.FormaPagamentoNegociacaoRecebimento = FormaPagamentoNegociacaoRecebimento.codigo ");
			sql.append(" where lancamentocontabil.situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.AGUARDANDO_COMPENSACAO.name()).append("' ");
			sql.append(" and lancamentocontabil.tipoorigemlancamentocontabil = '").append(TipoOrigemLancamentoContabilEnum.RECEBER.name()).append("' ");
			sql.append(" and FormaPagamentoNegociacaoRecebimento.formaPagamentoNegociacaoRecebimentoCartaoCredito in ( ").append(UteisTexto.converteListaInteiroParaString(listaMapaPendenciaCartaoCreditoVO)).append(") ");

			sql.append(" ) ");
			getConexao().getJdbcTemplate().execute(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorListaCodOrigemTipoOrigem(String listaCodOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LancamentoContabil.excluir(getIdEntidade(), verificarAcesso, usuario);
			StringBuilder sb = new StringBuilder("");
			sb.append(" Select integracaoContabil, tipoorigemlancamentocontabil, codorigem FROM LancamentoContabil ");
			sb.append(" WHERE (codorigem in (").append(listaCodOrigem).append(") ");
			sb.append(" and tipoorigemlancamentocontabil = '").append(tipoOrigem).append("') ");
			sb.append(" and integracaoContabil is not null ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			validarSeLancamentoContabilAptaExclusao(usuario, rs);
			String sql = "DELETE FROM LancamentoContabil WHERE (codorigem in (" + listaCodOrigem + ") and tipoorigemlancamentocontabil = '" + tipoOrigem.name() + "')" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCodOrigemTipoOrigem(String codOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			LancamentoContabil.excluir(getIdEntidade(), verificarAcesso, usuario);
			StringBuilder sb = new StringBuilder("");
			sb.append(" Select integracaoContabil, tipoorigemlancamentocontabil, codorigem FROM LancamentoContabil ");
			sb.append(" WHERE (codorigem = ? ");
			sb.append(" and tipoorigemlancamentocontabil = ?) ");
			sb.append(" and integracaoContabil is not null ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), codOrigem, tipoOrigem.name());
			validarSeLancamentoContabilAptaExclusao(usuario, rs);
			getConexao().getJdbcTemplate().update("DELETE FROM LancamentoContabil WHERE (codorigem = ? and tipoorigemlancamentocontabil = ?) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), codOrigem, tipoOrigem.name());
		} catch (Exception e) {
			throw new StreamSeiException(e.getLocalizedMessage());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LancamentoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LancamentoContabil.excluir(getIdEntidade(), verificarAcesso, usuario);
			StringBuilder sb = new StringBuilder("");
			sb.append(" Select integracaoContabil, tipoorigemlancamentocontabil, codorigem FROM LancamentoContabil WHERE (codorigem = '").append(obj.getCodOrigem()).append("' ");
			sb.append(" and tipoorigemlancamentocontabil = '").append(obj.getTipoOrigemLancamentoContabilEnum().name()).append("') ");
			sb.append(" and integracaoContabil is not null ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			validarSeLancamentoContabilAptaExclusao(usuario, rs);
			String sql = "DELETE FROM LancamentoContabil WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarSeLancamentoContabilAptaExclusao(UsuarioVO usuario, SqlRowSet rs) {
		if (rs.next()) {
			StringBuilder sb = new StringBuilder("");
			sb.append("O Lançamento Contábil de origem ");
			sb.append(UteisJSF.internacionalizarEnum(TipoOrigemLancamentoContabilEnum.valueOf(rs.getString("tipoorigemlancamentocontabil"))));
			sb.append(" do código  ").append(rs.getString("codorigem"));
			sb.append(" está vinculado a integração contábil de código ").append(rs.getInt("integracaoContabil"));
			sb.append(" por isso não é possível realizar essa operação. ");
			throw new StreamSeiException(sb.toString());
		}
	}

	private void validarDadosParaGeracaoLancamentoContabil(UnidadeEnsinoVO unidadeEnsino, Double valor) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(unidadeEnsino), "O campo Unidade Ensino deve ser Informado");
		Uteis.checkState(!Uteis.isAtributoPreenchido(valor), "O campo Valor deve ser Informado");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarLancamentoContabilPorContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, Double valorPagamento, Date dataBaixa, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(valorPagamento)) {
			ConfiguracaoContabilVO conf = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(contaReceber.getUnidadeEnsinoFinanceira().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioLogado);
			if (Uteis.isAtributoPreenchido(conf)) {
				List<TipoRegraContabilEnum> lista = new ArrayList<>();
				lista.add(TipoRegraContabilEnum.RECEBIMENTO);
				lista.add(TipoRegraContabilEnum.DESCONTO);
				lista.add(TipoRegraContabilEnum.JURO_MULTA_ACRESCIMO);
				lista.add(TipoRegraContabilEnum.TAXA_CARTAOES);
				getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(conf, lista, usuarioLogado);
				if (excluirLancamentoExistente) {
					excluirPorCodOrigemTipoOrigem(contaReceber.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.RECEBER, false, usuarioLogado);
				}
				validarDadosParaGeracaoLancamentoContabil(contaReceber.getUnidadeEnsinoFinanceira(), contaReceber.getValor());
				CursoVO curso = getFacadeFactory().getContaReceberFacade().obterCursoVerificandoTurmaOrMatricula(contaReceber, usuarioLogado);
				TurnoVO turno = getFacadeFactory().getContaReceberFacade().obterTurnoVerificandoTurmaOrMatricula(contaReceber, usuarioLogado);
				gerarLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, usuarioLogado);
				valorPagamento = gerarLancamentoContabilRegrasTaxaCartoesContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, valorPagamento, fpnrVO, curso, turno, usuarioLogado);
				valorPagamento = gerarLancamentoContabilRegrasJuroMultaAcrescimoContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, valorPagamento, fpnrVO, curso, turno, usuarioLogado);
				if (valorPagamento > 0.0) {
					gerarLancamentoContabilRegrasRecebimento(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, valorPagamento, curso, turno, usuarioLogado);
				}
			}
		}
	}

	private void gerarLancamentoContabilRegrasRecebimento(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataBaixa, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, Double valorPagamento, CursoVO curso, TurnoVO turno, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasRecebimento())) {
			Collections.sort(conf.getListaContabilRegrasRecebimento(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasRecebimento()) {
				if ((regras.getTipoOrigemContaReceber() == null || (Uteis.isAtributoPreenchido(regras.getTipoOrigemContaReceber()) && regras.getTipoOrigemContaReceber().getDescricao().equals(contaReceber.getTipoOrigem_apresentar()))) && (regras.getTipoSacadoReceber() == null || (Uteis.isAtributoPreenchido(regras.getTipoSacadoReceber()) && regras.getTipoSacadoReceber().equals(contaReceber.getTipoPessoa_apresentar()))) && (regras.getCodigoSacado() == 0 || (Uteis.isAtributoPreenchido(regras.getCodigoSacado()) && regras.getCodigoSacado().equals(contaReceber.getCodigoPessoaSacado()))) && (!Uteis.isAtributoPreenchido(regras.getContaCorrenteOrigemVO()) || (regras.getContaCorrenteOrigemVO().getCodigo().equals(fpnrVO.getContaCorrente().getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getCursoVO()) || (regras.getCursoVO().getCodigo().equals(curso.getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getTurnoVO()) || (regras.getTurnoVO().getCodigo().equals(turno.getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regras.getCentroReceitaVO()) || (regras.getCentroReceitaVO().getCodigo().equals(contaReceber.getCentroReceita().getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getFormaPagamentoVO()) || (regras.getFormaPagamentoVO().getCodigo().equals(fpnrVO.getFormaPagamento().getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getOperadoraCartaoVO()) || (regras.getOperadoraCartaoVO().getCodigo().equals(fpnrVO.getOperadoraCartaoVO().getCodigo())))) {
					for (ConfiguracaoContabilRegraPlanoContaVO ccrp : regras.getListaConfiguracaoContabilRegraPlanoContaVO()) {
						ccrp.setConfiguracaoContabilRegraVO(regras);
						gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaCreditoVO(), dataBaixa, contaReceber, fpnrVO, curso, TipoValorLancamentoContabilEnum.CONTA_RECEBER, valorPagamento, TipoPlanoContaEnum.CREDITO, usuarioLogado);
						gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaDebitoVO(), dataBaixa, contaReceber, fpnrVO, curso, TipoValorLancamentoContabilEnum.CONTA_RECEBER, valorPagamento, TipoPlanoContaEnum.DEBITO, usuarioLogado);
					}
					break;
				}
			}
		}
	}

	private void gerarLancamentoContabilRegrasDescontosContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataBaixa, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TurnoVO turno, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasDesconto())) {
			Collections.sort(conf.getListaContabilRegrasDesconto(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			if (Uteis.isAtributoPreenchido(contaReceber.getValorDescontoAlunoJaCalculado()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isAluno() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorDescontoAlunoJaCalculado()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorDescontoAlunoJaCalculado(), TipoDesconto.ALUNO, TipoValorLancamentoContabilEnum.ALUNO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getValorDescontoProgressivo()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isProgressivo() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorDescontoProgressivo()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorDescontoProgressivo(), TipoDesconto.PROGRESSIVO, TipoValorLancamentoContabilEnum.PROGRESSIVO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getValorDescontoInstituicao()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isInstitucional() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorDescontoInstituicao()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorDescontoInstituicao(), TipoDesconto.INSTITUCIONAL, TipoValorLancamentoContabilEnum.INSTITUCIONAL, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getValorDescontoConvenio()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isConvenio() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorDescontoConvenio()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorDescontoConvenio(), TipoDesconto.CONVENIO, TipoValorLancamentoContabilEnum.CONVENIO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getValorCusteadoContaReceber()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isCusteadoConvenio() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorCusteadoContaReceber()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorCusteadoContaReceber(), TipoDesconto.CUSTEADO_CONVENIO, TipoValorLancamentoContabilEnum.CUSTEADO_CONVENIO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getValorDescontoRateio()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isRateio() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorDescontoRateio()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorDescontoRateio(), TipoDesconto.RATEIO, TipoValorLancamentoContabilEnum.RATEIO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getValorCalculadoDescontoLancadoRecebimento()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isRecebimento() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getValorCalculadoDescontoLancadoRecebimento()))) {
				preencherLancamentoContabilRegrasDescontosContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getValorCalculadoDescontoLancadoRecebimento(), TipoDesconto.RECEBIMENTO, TipoValorLancamentoContabilEnum.RECEBIMENTO, usuarioLogado);
			}
		}
	}

	private void preencherLancamentoContabilRegrasDescontosContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataBaixa, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TurnoVO turno, Double valorPagamento, TipoDesconto tipoDesconto, TipoValorLancamentoContabilEnum tipoValorLancamentoContabilEnum, UsuarioVO usuarioLogado) throws Exception {
		for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasDesconto()) {
			if ((regras.getTipoDescontoEnum() == null || (Uteis.isAtributoPreenchido(regras.getTipoDescontoEnum()) && regras.getTipoDescontoEnum().equals(tipoDesconto))) && (!Uteis.isAtributoPreenchido(regras.getCursoVO()) || (regras.getCursoVO().getCodigo().equals(curso.getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getTurnoVO()) || (regras.getTurnoVO().getCodigo().equals(turno.getCodigo())))) {
				for (ConfiguracaoContabilRegraPlanoContaVO ccrp : regras.getListaConfiguracaoContabilRegraPlanoContaVO()) {
					ccrp.setConfiguracaoContabilRegraVO(regras);
					gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaCreditoVO(), dataBaixa, contaReceber, fpnrVO, curso, tipoValorLancamentoContabilEnum, valorPagamento, TipoPlanoContaEnum.CREDITO, usuarioLogado);
					gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaDebitoVO(), dataBaixa, contaReceber, fpnrVO, curso, tipoValorLancamentoContabilEnum, valorPagamento, TipoPlanoContaEnum.DEBITO, usuarioLogado);
				}
				break;
			}
		}
	}

	private Double gerarLancamentoContabilRegrasTaxaCartoesContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataBaixa, ContaReceberVO contaReceber, Double valorPagamento, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TurnoVO turno, UsuarioVO usuarioLogado) throws Exception {
		if(Uteis.isAtributoPreenchido(fpnrVO) 
				&& ((fpnrVO.getFormaPagamento().isCartaoDebito() && fpnrVO.getContaCorrenteOperadoraCartaoVO().isUtilizaTaxaCartaoDebito())
					 ||(fpnrVO.getFormaPagamento().isCartaoCredito() && fpnrVO.getContaCorrenteOperadoraCartaoVO().isUtilizaTaxaCartaoCredito())) 
				&& Uteis.isAtributoPreenchido(fpnrVO.getTaxaDeOperacao())){
			ConfiguracaoContabilRegraVO regras = obterRegraContabilParaTaxaCartoesContaReceber(conf, fpnrVO, curso, turno);
			if (Uteis.isAtributoPreenchido(regras) ) {
				Double taxaOperadora = Uteis.arrendondarForcandoCadasDecimais(valorPagamento * (fpnrVO.getTaxaDeOperacao() / 100), 2);
				if (Uteis.isAtributoPreenchido(taxaOperadora)) {
					for (ConfiguracaoContabilRegraPlanoContaVO ccrp : regras.getListaConfiguracaoContabilRegraPlanoContaVO()) {
						ccrp.setConfiguracaoContabilRegraVO(regras);
						gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaCreditoVO(), dataBaixa, contaReceber, fpnrVO, curso, TipoValorLancamentoContabilEnum.CONTA_RECEBER, taxaOperadora, TipoPlanoContaEnum.CREDITO, usuarioLogado);
						gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaDebitoVO(), dataBaixa, contaReceber, fpnrVO, curso, TipoValorLancamentoContabilEnum.CONTA_RECEBER, taxaOperadora, TipoPlanoContaEnum.DEBITO, usuarioLogado);
					}
					valorPagamento = Uteis.arrendondarForcando2CadasDecimais(valorPagamento - taxaOperadora);
				}
			}	
		}
		return valorPagamento;
	}

	public ConfiguracaoContabilRegraVO obterRegraContabilParaTaxaCartoesContaReceber(ConfiguracaoContabilVO conf, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TurnoVO turno) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasTaxaCartoes())) {
			Collections.sort(conf.getListaContabilRegrasTaxaCartoes(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasTaxaCartoes()) {
				if ((!Uteis.isAtributoPreenchido(regras.getCursoVO()) || (regras.getCursoVO().getCodigo().equals(curso.getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regras.getTurnoVO()) || (regras.getTurnoVO().getCodigo().equals(turno.getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regras.getFormaPagamentoVO()) || (regras.getFormaPagamentoVO().getCodigo().equals(fpnrVO.getFormaPagamento().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regras.getOperadoraCartaoVO()) || (regras.getOperadoraCartaoVO().getCodigo().equals(fpnrVO.getOperadoraCartaoVO().getCodigo())))) {
					return regras;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	private Double gerarLancamentoContabilRegrasJuroMultaAcrescimoContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataBaixa, ContaReceberVO contaReceber, Double valorPagamento, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TurnoVO turno, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasJuroMultaAcrescimo())) {
			Collections.sort(conf.getListaContabilRegrasJuroMultaAcrescimo(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			if (Uteis.isAtributoPreenchido(contaReceber.getJuro())
					&& !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isJuro() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getJuro()))
					&& preencherLancamentoContabilRegrasJuroMultaAcrescimoContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getJuro(), TipoDesconto.JURO, TipoValorLancamentoContabilEnum.JURO, usuarioLogado)) {
				valorPagamento = Uteis.arrendondarForcando2CadasDecimais(valorPagamento - contaReceber.getJuro());
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getMulta())
					&& !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isMulta() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getMulta()))
					&& preencherLancamentoContabilRegrasJuroMultaAcrescimoContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getMulta(), TipoDesconto.MULTA, TipoValorLancamentoContabilEnum.MULTA, usuarioLogado)) {
				valorPagamento = Uteis.arrendondarForcando2CadasDecimais(valorPagamento - contaReceber.getMulta());
			}
			if (Uteis.isAtributoPreenchido(contaReceber.getAcrescimo())
					&& !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isAcrescimo() && p.getCodOrigem().equals(contaReceber.getCodigo().toString()) && p.getValor().equals(contaReceber.getAcrescimo()))
					&& preencherLancamentoContabilRegrasJuroMultaAcrescimoContaReceber(listaLancamentoContabilVOs, conf, dataBaixa, contaReceber, fpnrVO, curso, turno, contaReceber.getAcrescimo(), TipoDesconto.ACRESCIMO, TipoValorLancamentoContabilEnum.ACRESCIMO, usuarioLogado)) {
				valorPagamento = Uteis.arrendondarForcando2CadasDecimais(valorPagamento - contaReceber.getAcrescimo());
			}
		}
		return valorPagamento;
	}

	private boolean preencherLancamentoContabilRegrasJuroMultaAcrescimoContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataBaixa, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TurnoVO turno, Double valorPagamento, TipoDesconto tipoDesconto, TipoValorLancamentoContabilEnum tipoValorLancamentoContabilEnum, UsuarioVO usuarioLogado) throws Exception {
		for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasJuroMultaAcrescimo()) {
			if ((regras.getTipoDescontoEnum() == null || (Uteis.isAtributoPreenchido(regras.getTipoDescontoEnum()) && regras.getTipoDescontoEnum().equals(tipoDesconto))) && (!Uteis.isAtributoPreenchido(regras.getCursoVO()) || (regras.getCursoVO().getCodigo().equals(curso.getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getTurnoVO()) || (regras.getTurnoVO().getCodigo().equals(turno.getCodigo())))) {
				for (ConfiguracaoContabilRegraPlanoContaVO ccrp : regras.getListaConfiguracaoContabilRegraPlanoContaVO()) {
					ccrp.setConfiguracaoContabilRegraVO(regras);
					gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaCreditoVO(), dataBaixa, contaReceber, fpnrVO, curso, tipoValorLancamentoContabilEnum, valorPagamento, TipoPlanoContaEnum.CREDITO, usuarioLogado);
					gerarLancamentoContabilPorRegraContaReceber(listaLancamentoContabilVOs, ccrp, ccrp.getPlanoContaDebitoVO(), dataBaixa, contaReceber, fpnrVO, curso, tipoValorLancamentoContabilEnum, valorPagamento, TipoPlanoContaEnum.DEBITO, usuarioLogado);
				}
				return true;
			}
		}
		return false;
	}

	private void gerarLancamentoContabilPorRegraContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilRegraPlanoContaVO ccrp, PlanoContaVO planoConta, Date dataBaixa, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, CursoVO curso, TipoValorLancamentoContabilEnum tipoValor, Double valorPagamento, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		Date dataCompensacao = new Date();
		if (ccrp.getConfiguracaoContabilRegraVO().isConsiderarValorDataCompensacao()
				&& Uteis.isAtributoPreenchido(fpnrVO)
				&& (fpnrVO.getFormaPagamento().isCheque() && tipoValor.isCheque())) {
			lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.AGUARDANDO_COMPENSACAO);
		} else {
			lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
			if (Uteis.isAtributoPreenchido(fpnrVO) && fpnrVO.getDataCredito() != null && (fpnrVO.getFormaPagamento().isDinheiro() || fpnrVO.getFormaPagamento().isCartaoDebito() || fpnrVO.getFormaPagamento().isBoletoBancario() || fpnrVO.getFormaPagamento().isDebitoEmConta())) {
				dataCompensacao = fpnrVO.getDataCredito();
			}else if (Uteis.isAtributoPreenchido(fpnrVO) &&  (fpnrVO.getFormaPagamento().isCartaoCredito())) {
				dataCompensacao = dataBaixa;
			}
		}
		if (Uteis.isAtributoPreenchido(fpnrVO)) {
			preencherLancamentoContabilPorContaReceber(lc, planoConta, dataBaixa, contaReceber, tipoValor, fpnrVO, valorPagamento, curso, fpnrVO.getContaCorrente(), true, tipoPlanoContaEnum, dataCompensacao, usuarioLogado);
		} else {
			preencherLancamentoContabilPorContaReceber(lc, planoConta, dataBaixa, contaReceber, tipoValor, null, valorPagamento, curso, null, true, tipoPlanoContaEnum, dataCompensacao, usuarioLogado);
		}
		preencherListaLancamentoContabilVO(listaLancamentoContabilVOs, lc);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherLancamentoContabilPorContaReceber(LancamentoContabilVO lc, PlanoContaVO planoConta, Date dataBaixa, ContaReceberVO contaReceber, TipoValorLancamentoContabilEnum tipoValor, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, Double valorPagamento, CursoVO curso, ContaCorrenteVO contaCorrente, boolean isPreencherRaterio, TipoPlanoContaEnum tipoPlanoContaEnum, Date dataCompensacao, UsuarioVO usuarioLogado) throws Exception {
		lc.setPlanoContaVO(planoConta);
		lc.setCodOrigem(contaReceber.getCodigo().toString());
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.RECEBER);
		lc.setTipoValorLancamentoContabilEnum(tipoValor);
		lc.setUnidadeEnsinoVO(contaReceber.getUnidadeEnsinoFinanceira());
		lc.setDataRegistro(dataBaixa);
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		if (lc.getSituacaoLancamentoContabilEnum().isCompensado()) {
			lc.setDataCompensacao(dataCompensacao);
		}
		lc.setValor(valorPagamento);
		if (contaReceber.getTipoFuncionario()) {
			lc.setTipoSacado(TipoSacado.FUNCIONARIO_PROFESSOR);
			lc.setFuncionarioVO(contaReceber.getFuncionario());
		} else if (contaReceber.getTipoAluno()) {
			lc.setTipoSacado(TipoSacado.ALUNO);
			lc.setPessoaVO(contaReceber.getPessoa());
		} else if (contaReceber.getTipoResponsavelFinanceiro()) {
			lc.setTipoSacado(TipoSacado.RESPONSAVEL_FINANCEIRO);
			lc.setPessoaVO(contaReceber.getResponsavelFinanceiro());
		} else if (contaReceber.getTipoParceiro()) {
			lc.setTipoSacado(TipoSacado.PARCEIRO);
			lc.setParceiroVO(contaReceber.getParceiroVO());
		} else if (contaReceber.getTipoFornecedor()) {
			lc.setTipoSacado(TipoSacado.FORNECEDOR);
			lc.setFornecedorVO(contaReceber.getFornecedor());
		} else if (contaReceber.getTipoCandidato()) {
			lc.setTipoSacado(TipoSacado.CANDIDATO);
			lc.setPessoaVO(contaReceber.getCandidato());
		}
		if (Uteis.isAtributoPreenchido(contaCorrente)) {
			lc.setContaCorrenteVO(contaCorrente);
		}
		if (isPreencherRaterio) {
			LancamentoContabilCentroNegocioVO lccn = new LancamentoContabilCentroNegocioVO();
			lccn.setLancamentoContabilVO(lc);
			lccn.setUnidadeEnsinoVO(lc.getUnidadeEnsinoVO());
			lccn.setPercentual(100.00);
			lccn.setValor(Uteis.arrendondarForcando2CadasDecimais(valorPagamento));
			lccn.setTipoCentroNegocioEnum(TipoCentroNegocioEnum.ACADEMICO);
			lccn.setCodigoContabil(curso.getCodigoContabil());
			lccn.setNomeContabil(curso.getNomeContabil());
			lccn.setNivelContabil(curso.getNivelContabil());
			lccn.setCursoVO(curso);
			if (Uteis.isAtributoPreenchido(contaReceber.getListaCentroResultadoOrigem()) && contaReceber.getListaCentroResultadoOrigem().size() == 1) {
				lccn.setCentroResultadoAdministrativo(contaReceber.getListaCentroResultadoOrigem().get(0).getCentroResultadoAdministrativo());
			}

			lc.getListaCentroNegocioAcademico().add(lccn);
			lc.validarTotalizadoDoLancamentoContabilCentroNegociacao(valorPagamento);
		}

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarLancamentoContabilPorContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorPagamento, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoContabilVO conf = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(contaPagar.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioLogado);
		if (Uteis.isAtributoPreenchido(conf)) {
			List<TipoRegraContabilEnum> lista = new ArrayList<>();
			lista.add(TipoRegraContabilEnum.DESCONTO_PAGAR);
			lista.add(TipoRegraContabilEnum.JURO_MULTA_PAGAR);
			lista.add(TipoRegraContabilEnum.PAGAMENTO);
			lista.add(TipoRegraContabilEnum.SACADO);
			getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(conf, lista, usuarioLogado);
			if (excluirLancamentoExistente) {
				excluirPorCodOrigemTipoOrigem(contaPagar.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, false, usuarioLogado);
			}
			validarDadosParaGeracaoLancamentoContabil(contaPagar.getUnidadeEnsino(), contaPagar.getValor());
			gerarLancamentoContabilRegrasDescontoContaPagar(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, usuarioLogado);
			valorPagamento = gerarLancamentoContabilRegrasJuroMultaAcrescimoContaPagar(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, valorPagamento, usuarioLogado);
			if (valorPagamento > 0.0) {
				gerarLancamentoContabilRegrasPagamento(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, valorPagamento, usuarioLogado);
			}
		}
	}

	private void gerarLancamentoContabilRegrasPagamento(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorPagamento, UsuarioVO usuarioLogado) throws Exception {
		if (contaPagar.getTipoOrigemEnum().isAdiantamento()) {
			ConfiguracaoContabilRegraVO regras = obterRegraContabilParaContaPagar(conf, contaPagar, fpnpVO);
	        if (Uteis.isAtributoPreenchido(regras)) {
	            gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, regras.getPlanoContaVO(), dataPagamento, contaPagar, fpnpVO, TipoValorLancamentoContabilEnum.CONTA_PAGAR, valorPagamento, TipoPlanoContaEnum.CREDITO, true, usuarioLogado);
                geraLancamentoContabilRegrasContaPagarAdiantamento(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, valorPagamento, usuarioLogado);
	        }
		}else {
			Map<Integer, Double> mapaValorPorPlanoConta = new HashMap<>();
			for (CentroResultadoOrigemVO cro : contaPagar.getListaCentroResultadoOrigemVOs()) {
				ConfiguracaoContabilRegraVO regras = obterRegraContabilParaContaPagarComCentroResultadoOrigem(conf, contaPagar, cro, fpnpVO);
				if (Uteis.isAtributoPreenchido(regras)) {
					Double valorTemp = mapaValorPorPlanoConta.containsKey(regras.getPlanoContaVO().getCodigo()) ? mapaValorPorPlanoConta.get(regras.getPlanoContaVO().getCodigo()) : 0.0;
					mapaValorPorPlanoConta.put(regras.getPlanoContaVO().getCodigo(), Uteis.arrendondarForcando2CadasDecimais(valorTemp + ((valorPagamento * cro.getPorcentagem()) / 100)));
				}
			}
			if (!mapaValorPorPlanoConta.isEmpty()) {
				validarMapaValorPorPlanoConta(valorPagamento, mapaValorPorPlanoConta);
				mapaValorPorPlanoConta.entrySet().stream().forEach(p -> {
					try {
						PlanoContaVO planoContaVO = getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(p.getKey(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
						gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, planoContaVO, dataPagamento, contaPagar, fpnpVO, TipoValorLancamentoContabilEnum.CONTA_PAGAR, p.getValue(), TipoPlanoContaEnum.CREDITO, true, usuarioLogado);
					} catch (Exception e) {
						throw new StreamSeiException(e);
					}
				});
				geraLancamentoContabilRegrasContaPagarSacado(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, valorPagamento, usuarioLogado);
			}
		}
	}

	private Double gerarLancamentoContabilRegrasJuroMultaAcrescimoContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorPagamento, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasJuroMultaPagar())) {
			if (Uteis.isAtributoPreenchido(contaPagar.getJuro()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isJuro() && p.getCodOrigem().equals(contaPagar.getCodigo().toString()) && p.getValor().equals(contaPagar.getJuro()))) {
				valorPagamento = preencherLancamentoContabilRegrasJuroMultaAcrescimoContaPagar(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, contaPagar.getJuro(), valorPagamento, TipoDesconto.JURO, TipoValorLancamentoContabilEnum.JURO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaPagar.getMulta()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isMulta() && p.getCodOrigem().equals(contaPagar.getCodigo().toString()) && p.getValor().equals(contaPagar.getMulta()))) {
				valorPagamento = preencherLancamentoContabilRegrasJuroMultaAcrescimoContaPagar(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, contaPagar.getMulta(), valorPagamento, TipoDesconto.MULTA, TipoValorLancamentoContabilEnum.MULTA, usuarioLogado);
			}
		}
		return valorPagamento;
	}

	private Double preencherLancamentoContabilRegrasJuroMultaAcrescimoContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorJuroMulta, Double valorPagamento, TipoDesconto tipoDesconto, TipoValorLancamentoContabilEnum tipoValorLancamentoContabilEnum, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoContabilRegraVO regras = obterRegraContabilParaJuroMultaAcrescimoContaPagar(conf, tipoDesconto);
		if (Uteis.isAtributoPreenchido(regras)) {
			valorPagamento = Uteis.arrendondarForcando2CadasDecimais(valorPagamento - valorJuroMulta);
			gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, regras.getPlanoContaVO(), dataPagamento, contaPagar, fpnpVO, tipoValorLancamentoContabilEnum, valorJuroMulta, TipoPlanoContaEnum.DEBITO, true, usuarioLogado);
			if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasPagamento())) {
				Collections.sort(conf.getListaContabilRegrasPagamento(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
				ConfiguracaoContabilRegraVO regrasContaPagar = obterRegraContabilParaContaPagar(conf, contaPagar, fpnpVO);
				if (Uteis.isAtributoPreenchido(regrasContaPagar)) {
					gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, regrasContaPagar.getPlanoContaVO(), dataPagamento, contaPagar, fpnpVO, tipoValorLancamentoContabilEnum, valorJuroMulta, TipoPlanoContaEnum.CREDITO, true, usuarioLogado);
					return valorPagamento;
				}
			}
			throw new StreamSeiException("Existe um lançamento contábil para Juro/Multa porem não foi encontrado nenhuma configuração contábil da  regra de pagamento");
		}
		return valorPagamento;
	}

	private void gerarLancamentoContabilRegrasDescontoContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasDescontoPagar())) {
			if (Uteis.isAtributoPreenchido(contaPagar.getDesconto()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isPagamento() && p.getCodOrigem().equals(contaPagar.getCodigo().toString()) && p.getValor().equals(contaPagar.getDesconto()))) {
				preencherLancamentoContabilRegrasDescontoContaPagar(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, contaPagar.getDesconto(), TipoDesconto.PAGAMENTO, TipoValorLancamentoContabilEnum.PAGAMENTO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(contaPagar.getDescontoPorUsoAdiantamento()) && !listaLancamentoContabilVOs.stream().anyMatch(p -> p.getTipoValorLancamentoContabilEnum().isAdiantamento() && p.getCodOrigem().equals(contaPagar.getCodigo().toString()) && p.getValor().equals(contaPagar.getDescontoPorUsoAdiantamento()))) {
				preencherLancamentoContabilRegrasDescontoContaPagar(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, contaPagar.getDescontoPorUsoAdiantamento(), TipoDesconto.ADIANTAMENTO, TipoValorLancamentoContabilEnum.ADIANTAMENTO, usuarioLogado);
			}
		}
	}

	private void preencherLancamentoContabilRegrasDescontoContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorDesconto, TipoDesconto tipoDesconto, TipoValorLancamentoContabilEnum tipoValorLancamentoContabilEnum, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoContabilRegraVO regras = obterRegraContabilParaDescontoContaPagar(conf, contaPagar.getTipoSacadoEnum(), contaPagar.getCodigoPessoaSacado(), tipoDesconto);
		if (Uteis.isAtributoPreenchido(regras)) {
			gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, regras.getPlanoContaVO(), dataPagamento, contaPagar, fpnpVO, tipoValorLancamentoContabilEnum, valorDesconto, TipoPlanoContaEnum.CREDITO, true, usuarioLogado);
			geraLancamentoContabilRegrasContaPagarSacado(listaLancamentoContabilVOs, conf, dataPagamento, contaPagar, fpnpVO, valorDesconto, usuarioLogado);

		}
	}

	private void geraLancamentoContabilRegrasContaPagarSacado(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorPagamento, UsuarioVO usuarioLogado) {
		Uteis.checkState(contaPagar.getListaCentroResultadoOrigemVOs().isEmpty(), "Não foi localizado o Centro de Resultado.");
		Map<Integer, Double> mapaValorPorPlanoConta = new HashMap<>();
		for (CentroResultadoOrigemVO cro : contaPagar.getListaCentroResultadoOrigemVOs()) {
			ConfiguracaoContabilRegraVO regras = obterRegraContabilParaSacadoContaPagarPorCentroResultado(conf, contaPagar.getTipoSacadoEnum(), contaPagar.getCodigoPessoaSacado(), cro);
			if (Uteis.isAtributoPreenchido(regras)) {
				Double valorTemp = mapaValorPorPlanoConta.containsKey(regras.getPlanoContaVO().getCodigo()) ? mapaValorPorPlanoConta.get(regras.getPlanoContaVO().getCodigo()) : 0.0;
				mapaValorPorPlanoConta.put(regras.getPlanoContaVO().getCodigo(), Uteis.arrendondarForcando2CadasDecimais(valorTemp + ((valorPagamento * cro.getPorcentagem()) / 100)));
			}
		}

		if (!mapaValorPorPlanoConta.isEmpty()) {
			validarMapaValorPorPlanoConta(valorPagamento, mapaValorPorPlanoConta);
			mapaValorPorPlanoConta.entrySet().stream().forEach(p -> {
				try {
					PlanoContaVO planoContaVO = getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(p.getKey(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
					gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, planoContaVO, dataPagamento, contaPagar, fpnpVO, TipoValorLancamentoContabilEnum.CONTA_PAGAR, p.getValue(), TipoPlanoContaEnum.DEBITO, true, usuarioLogado);
				} catch (Exception e) {
					throw new StreamSeiException(e);
				}
			});
			return;
		}
		throw new StreamSeiException("Não foi encontrado nenhuma configuração contábil para o " + contaPagar.getFavorecido_Apresentar());
	}

	private void geraLancamentoContabilRegrasContaPagarAdiantamento(List<LancamentoContabilVO> listaLancamentoContabilVOs, ConfiguracaoContabilVO conf, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorPagamento, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoContabilRegraVO regras = obterRegraContabilParaAdiantamentoContaPagar(conf, contaPagar, fpnpVO);
		if (Uteis.isAtributoPreenchido(regras)) {
			gerarLancamentoContabilPorRegraContaPagar(listaLancamentoContabilVOs, regras.getPlanoContaVO(), dataPagamento, contaPagar, fpnpVO, TipoValorLancamentoContabilEnum.CONTA_PAGAR, valorPagamento, TipoPlanoContaEnum.DEBITO, true, usuarioLogado);
			return;
		}
		throw new StreamSeiException("Não foi encontrado nenhuma configuração contábil para o  adiantamento da conta pagar de código " + contaPagar.getCodigo());
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaContaPagarComCentroResultadoOrigem(ConfiguracaoContabilVO conf, ContaPagarVO contaPagar, CentroResultadoOrigemVO cro, FormaPagamentoNegociacaoPagamentoVO fpnpVO) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasPagamento())) {
			Collections.sort(conf.getListaContabilRegrasPagamento(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regrasContaPagar : conf.getListaContabilRegrasPagamento()) {
				if ((regrasContaPagar.getOrigemContaPagar() == null
						|| (Uteis.isAtributoPreenchido(regrasContaPagar.getOrigemContaPagar()) && regrasContaPagar.getOrigemContaPagar().getDescricao().equals(contaPagar.getTipoOrigem_Apresentar())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getContaCorrenteOrigemVO()) || (regrasContaPagar.getContaCorrenteOrigemVO().getCodigo().equals(fpnpVO.getContaCorrente().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getCategoriaDespesaVO()) || (regrasContaPagar.getCategoriaDespesaVO().getCodigo().equals(cro.getCategoriaDespesaVO().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getFormaPagamentoVO()) || (regrasContaPagar.getFormaPagamentoVO().getCodigo().equals(fpnpVO.getFormaPagamento().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getOperadoraCartaoVO()) || (regrasContaPagar.getOperadoraCartaoVO().getCodigo().equals(fpnpVO.getOperadoraCartaoVO().getCodigo())))) {
					return regrasContaPagar;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaContaPagar(ConfiguracaoContabilVO conf, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasPagamento())) {
			Collections.sort(conf.getListaContabilRegrasPagamento(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regrasContaPagar : conf.getListaContabilRegrasPagamento()) {
				if ((regrasContaPagar.getOrigemContaPagar() == null
						|| (Uteis.isAtributoPreenchido(regrasContaPagar.getOrigemContaPagar()) && regrasContaPagar.getOrigemContaPagar().getDescricao().equals(contaPagar.getTipoOrigem_Apresentar())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getContaCorrenteOrigemVO()) || (Uteis.isAtributoPreenchido(fpnpVO) && Uteis.isAtributoPreenchido(fpnpVO.getContaCorrente().getCodigo()) && regrasContaPagar.getContaCorrenteOrigemVO().getCodigo().equals(fpnpVO.getContaCorrente().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getCategoriaDespesaVO()) || (contaPagar.validarSeExisteCategoriaDespesaParaCentroResultadoOrigem(regrasContaPagar.getCategoriaDespesaVO())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getFormaPagamentoVO()) || (Uteis.isAtributoPreenchido(fpnpVO) && Uteis.isAtributoPreenchido(fpnpVO.getFormaPagamento().getCodigo()) && regrasContaPagar.getFormaPagamentoVO().getCodigo().equals(fpnpVO.getFormaPagamento().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getOperadoraCartaoVO()) || (Uteis.isAtributoPreenchido(fpnpVO) &&  Uteis.isAtributoPreenchido(fpnpVO.getOperadoraCartaoVO().getCodigo()) &&  regrasContaPagar.getOperadoraCartaoVO().getCodigo().equals(fpnpVO.getOperadoraCartaoVO().getCodigo())))) {
					return regrasContaPagar;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaJuroMultaAcrescimoContaPagar(ConfiguracaoContabilVO conf, TipoDesconto tipoDesconto) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasJuroMultaPagar())) {
			Collections.sort(conf.getListaContabilRegrasJuroMultaPagar(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasJuroMultaPagar()) {
				if ((regras.getTipoDescontoEnum() == null || (Uteis.isAtributoPreenchido(regras.getTipoDescontoEnum()) && regras.getTipoDescontoEnum().equals(tipoDesconto)))) {
					return regras;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaDescontoContaPagar(ConfiguracaoContabilVO conf, TipoSacado tipoSacado, Integer codigoSacado, TipoDesconto tipoDesconto) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasDescontoPagar())) {
			Collections.sort(conf.getListaContabilRegrasDescontoPagar(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasDescontoPagar()) {
				if ((regras.getTipoDescontoEnum() == null || (Uteis.isAtributoPreenchido(regras.getTipoDescontoEnum()) && regras.getTipoDescontoEnum().equals(tipoDesconto))) && (regras.getTipoSacadoPagar() == null || (Uteis.isAtributoPreenchido(regras.getTipoSacadoPagar()) && regras.getTipoSacadoPagar().equals(tipoSacado))) && (regras.getCodigoSacado() == 0 || (Uteis.isAtributoPreenchido(regras.getCodigoSacado()) && regras.getCodigoSacado().equals(codigoSacado)))) {
					return regras;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaSacadoContaPagarPorCentroResultado(ConfiguracaoContabilVO conf, TipoSacado tipoSacado, Integer codigoSacado, CentroResultadoOrigemVO cro) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasSacado())) {
			Collections.sort(conf.getListaContabilRegrasSacado(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regrasSacado : conf.getListaContabilRegrasSacado()) {
				if ((regrasSacado.getTipoSacadoPagar() == null || (Uteis.isAtributoPreenchido(regrasSacado.getTipoSacadoPagar()) && regrasSacado.getTipoSacadoPagar().equals(tipoSacado)))
						&& (regrasSacado.getCodigoSacado() == 0 || (Uteis.isAtributoPreenchido(regrasSacado.getCodigoSacado()) && regrasSacado.getCodigoSacado().equals(codigoSacado)))
						&& (!Uteis.isAtributoPreenchido(regrasSacado.getCategoriaDespesaVO()) || (regrasSacado.getCategoriaDespesaVO().getCodigo().equals(cro.getCategoriaDespesaVO().getCodigo())))) {
					return regrasSacado;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaSacadoContaPagar(ConfiguracaoContabilVO conf, TipoSacado tipoSacado, Integer codigoSacado) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasSacado())) {
			Collections.sort(conf.getListaContabilRegrasSacado(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regrasSacado : conf.getListaContabilRegrasSacado()) {
				if ((regrasSacado.getTipoSacadoPagar() == null || (Uteis.isAtributoPreenchido(regrasSacado.getTipoSacadoPagar()) && regrasSacado.getTipoSacadoPagar().equals(tipoSacado)))
						&& (regrasSacado.getCodigoSacado() == 0 || (Uteis.isAtributoPreenchido(regrasSacado.getCodigoSacado()) && regrasSacado.getCodigoSacado().equals(codigoSacado)))) {
					return regrasSacado;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	public ConfiguracaoContabilRegraVO obterRegraContabilParaAdiantamentoContaPagar(ConfiguracaoContabilVO conf, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasPagamento())) {
			Collections.sort(conf.getListaContabilRegrasPagamento(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regrasContaPagar : conf.getListaContabilRegrasPagamento()) {				
				if (((Uteis.isAtributoPreenchido(regrasContaPagar.getOrigemContaPagar()) && regrasContaPagar.getOrigemContaPagar().getDescricao().equals(contaPagar.getTipoOrigem_Apresentar())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getContaCorrenteOrigemVO()) || (regrasContaPagar.getContaCorrenteOrigemVO().getCodigo().equals(fpnpVO.getContaCorrente().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getCategoriaDespesaVO()) || (contaPagar.validarSeExisteCategoriaDespesaParaCentroResultadoOrigem(regrasContaPagar.getCategoriaDespesaVO())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getFormaPagamentoVO()) || (regrasContaPagar.getFormaPagamentoVO().getCodigo().equals(fpnpVO.getFormaPagamento().getCodigo())))
						&& (!Uteis.isAtributoPreenchido(regrasContaPagar.getOperadoraCartaoVO()) || (regrasContaPagar.getOperadoraCartaoVO().getCodigo().equals(fpnpVO.getOperadoraCartaoVO().getCodigo())))) {
					return regrasContaPagar;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	private void validarMapaValorPorPlanoConta(Double valorPagamento, Map<Integer, Double> mapaValorPorPlanoConta) {
		Double somaPercentual = mapaValorPorPlanoConta.values().stream().mapToDouble(p -> p).sum();
		if (somaPercentual < valorPagamento) {
			for (Entry<Integer, Double> mapa : mapaValorPorPlanoConta.entrySet()) {
				mapa.setValue(mapa.getValue() + (valorPagamento - somaPercentual));
				break;
			}
		}
	}

	private void gerarLancamentoContabilPorRegraContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, PlanoContaVO planoContaVO, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, TipoValorLancamentoContabilEnum tipoValor, Double valorPagamento, TipoPlanoContaEnum tipoPlanoContaEnum, boolean isCalculoPorcentagem, UsuarioVO usuarioLogado) {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		if ((Uteis.isAtributoPreenchido(fpnpVO) && (fpnpVO.getFormaPagamento().isCheque() || fpnpVO.getFormaPagamento().isInformaOperadoraCartao()))) {
			lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.AGUARDANDO_COMPENSACAO);
		} else {
			lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
		}
		if (Uteis.isAtributoPreenchido(fpnpVO)) {
			preencherLancamentoContabilPorContaPagar(lc, planoContaVO, dataPagamento, contaPagar, tipoValor, valorPagamento, fpnpVO.getContaCorrente(), true, tipoPlanoContaEnum, isCalculoPorcentagem, usuarioLogado);
		} else {
			preencherLancamentoContabilPorContaPagar(lc, planoContaVO, dataPagamento, contaPagar, tipoValor, valorPagamento, null, true, tipoPlanoContaEnum, isCalculoPorcentagem, usuarioLogado);
		}
		preencherListaLancamentoContabilVO(listaLancamentoContabilVOs, lc);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherLancamentoContabilPorContaPagar(LancamentoContabilVO lc, PlanoContaVO planoConta, Date dataPagamento, ContaPagarVO contaPagar, TipoValorLancamentoContabilEnum tipoValor, Double valorPagamento, ContaCorrenteVO contaCorrente, boolean isPreencherRaterio, TipoPlanoContaEnum tipoPlanoContaEnum, boolean isCalculoPorcentagem, UsuarioVO usuarioLogado) {
		lc.setPlanoContaVO(planoConta);
		lc.setCodOrigem(contaPagar.getCodigo().toString());
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.PAGAR);
		lc.setTipoValorLancamentoContabilEnum(tipoValor);
		lc.setUnidadeEnsinoVO(contaPagar.getUnidadeEnsino());
		lc.setDataRegistro(dataPagamento);
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		if (lc.getSituacaoLancamentoContabilEnum().isCompensado()) {
			lc.setDataCompensacao(lc.getDataRegistro());
		}
		lc.setValor(valorPagamento);
		lc.setTipoSacado(contaPagar.getTipoSacadoEnum());
		if (contaPagar.getTipoSacadoEnum().isAluno()) {
			lc.setPessoaVO(contaPagar.getPessoa());
		} else if (contaPagar.getTipoSacadoEnum().isBanco()) {
			lc.setBancoVO(contaPagar.getBanco());
		} else if (contaPagar.getTipoSacadoEnum().isFornecedor()) {
			lc.setFornecedorVO(contaPagar.getFornecedor());
		} else if (contaPagar.getTipoSacadoEnum().isFuncionario()) {
			lc.setFuncionarioVO(contaPagar.getFuncionario());
		} else if (contaPagar.getTipoSacadoEnum().isParceiro()) {
			lc.setParceiroVO(contaPagar.getParceiro());
		}
		if (Uteis.isAtributoPreenchido(contaCorrente)) {
			lc.setContaCorrenteVO(contaCorrente);
		}
		if (!contaPagar.getTipoOrigemEnum().isAdiantamento() && isPreencherRaterio) {
			preencherLancamentoContabilPorCentroNegocioOrigem(lc, contaPagar.getListaCentroResultadoOrigemVOs(), valorPagamento, isCalculoPorcentagem);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarLancamentoContabilPorMovimentacaoFinanceira(MovimentacaoFinanceiraVO mov, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoContabilVO conf = null;
		UnidadeEnsinoVO unidadEnsino = null;

		unidadEnsino = mov.getUnidadeEnsinoVO();
		conf = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(unidadEnsino.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);

		if (Uteis.isAtributoPreenchido(conf)) {
			List<TipoRegraContabilEnum> lista = new ArrayList<>();
			lista.add(TipoRegraContabilEnum.MOVIMENTACAO_FINANCEIRA);
			getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(conf, lista, usuarioLogado);
			if (excluirLancamentoExistente) {
				excluirPorCodOrigemTipoOrigem(mov.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA, false, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasMovimentacaoFinanceira())) {
				Collections.sort(conf.getListaContabilRegrasMovimentacaoFinanceira(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
				for (MovimentacaoFinanceiraItemVO movItem : mov.getMovimentacaoFinanceiraItemVOs()) {
					if (mov.getContaCorrenteOrigem().getCodigo() != null && !mov.getContaCorrenteOrigem().getCodigo().equals(0)) {
						ConfiguracaoContabilRegraVO regrasOrigem = obterRegraContabilParaMovimentacaoFinanceira(conf, mov.getContaCorrenteOrigem(), movItem.getFormaPagamento());
						if (Uteis.isAtributoPreenchido(regrasOrigem)) {
							gerarLancamentoContabilPorRegraMovimentacaoFinanceira(regrasOrigem.getPlanoContaVO(), unidadEnsino, mov, mov.getContaCorrenteOrigem(), movItem.getValor(), TipoPlanoContaEnum.CREDITO, usuarioLogado);
						}
					}
					ConfiguracaoContabilRegraVO regrasDestino = obterRegraContabilParaMovimentacaoFinanceira(conf, mov.getContaCorrenteDestino(), movItem.getFormaPagamento());
					if (Uteis.isAtributoPreenchido(regrasDestino)) {
						gerarLancamentoContabilPorRegraMovimentacaoFinanceira(regrasDestino.getPlanoContaVO(), unidadEnsino, mov, mov.getContaCorrenteDestino(), movItem.getValor(), TipoPlanoContaEnum.DEBITO, usuarioLogado);
					}
				}
				validarSeLancamentoContabilMovimentacaoFinanceiroValido(mov, usuarioLogado);
			}
		}
	}

	private void validarSeLancamentoContabilMovimentacaoFinanceiroValido(MovimentacaoFinanceiraVO mov, UsuarioVO usuarioLogado) throws Exception {
		Boolean lancamentoContabilValido = false;
		if (mov.isSomenteContaDestino()
				&& mov.getContaCorrenteDestino().getTipoContaCorrenteEnum().isCaixa()
				&& isFuncionalidadePerfilAcessoDoUsuario(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_LANCAMENTO_CONTABIL_MOVIMENTACAO_FINANCEIRA_SOMENTE_DESTINO.getValor(), usuarioLogado)) {
			lancamentoContabilValido = true;
			mov.getListaLancamentoContabeisCredito().clear();
			mov.getListaLancamentoContabeisDebito().clear();
		} else if (!lancamentoContabilValido && mov.getContaCorrenteOrigem().getTipoContaCorrenteEnum().isCaixa() && mov.getContaCorrenteDestino().getTipoContaCorrenteEnum().isCaixa()) {
			verificarListaDeLancamentoContabeisMovimentacaoFinanceira(mov, true);
		} else if (!lancamentoContabilValido && (!mov.getContaCorrenteOrigem().getTipoContaCorrenteEnum().isCaixa() || !mov.getContaCorrenteDestino().getTipoContaCorrenteEnum().isCaixa())) {
			verificarListaDeLancamentoContabeisMovimentacaoFinanceira(mov, false);
		}
		for (LancamentoContabilVO lc : mov.getListaLancamentoContabeisCredito()) {
			persistir(lc, false, usuarioLogado);
		}
		for (LancamentoContabilVO lc : mov.getListaLancamentoContabeisDebito()) {
			persistir(lc, false, usuarioLogado);
		}
	}

	private void verificarListaDeLancamentoContabeisMovimentacaoFinanceira(MovimentacaoFinanceiraVO mov, boolean isContasCorrentesCaixa) {
		Boolean lancamentoContabilValido = mov.isLancamentoContabilValido(mov.getListaLancamentoContabeisCredito(), mov.getListaLancamentoContabeisDebito());
		if (!lancamentoContabilValido) {
			lancamentoContabilValido = mov.isLancamentoContabilValido(mov.getListaLancamentoContabeisDebito(), mov.getListaLancamentoContabeisCredito());
			if (!lancamentoContabilValido && isContasCorrentesCaixa) {
				mov.getListaLancamentoContabeisCredito().clear();
				mov.getListaLancamentoContabeisDebito().clear();
			} else if (!lancamentoContabilValido && !isContasCorrentesCaixa) {
				throw new StreamSeiException("Os Planos de Contas do lançamento contábil para a movimentação financeira são iguais por isso não é possivel realizar essa operação.");
			}
		}
	}

	private void gerarLancamentoContabilPorRegraMovimentacaoFinanceira(PlanoContaVO planoConta, UnidadeEnsinoVO unidadEnsino, MovimentacaoFinanceiraVO mov, ContaCorrenteVO contaCorrente, Double valorPagamento, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		preencherLancamentoContabilPorMovimentacaoFinanceira(unidadEnsino, lc, planoConta, mov, contaCorrente, valorPagamento, tipoPlanoContaEnum, usuarioLogado);
		if (lc.getTipoPlanoConta().isCredito()) {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().preencherListaLancamentoContabilVO(mov.getListaLancamentoContabeisCredito(), lc);
		} else {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().preencherListaLancamentoContabilVO(mov.getListaLancamentoContabeisDebito(), lc);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherLancamentoContabilPorMovimentacaoFinanceira(UnidadeEnsinoVO unidadEnsino, LancamentoContabilVO lc, PlanoContaVO planoConta, MovimentacaoFinanceiraVO mov, ContaCorrenteVO contaCorrente, Double valorPagamento, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception {
		lc.setPlanoContaVO(planoConta);
		lc.setCodOrigem(mov.getCodigo().toString());
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA);
		lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
		lc.setTipoValorLancamentoContabilEnum(TipoValorLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA);
		lc.setUnidadeEnsinoVO(unidadEnsino);
		lc.setDataRegistro(mov.getData());
		if (lc.getSituacaoLancamentoContabilEnum().isCompensado()) {
			lc.setDataCompensacao(lc.getDataRegistro());
		}
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		lc.setValor(valorPagamento);
		lc.setTipoSacado(TipoSacado.RESPONSAVEL_FINANCEIRO);
		lc.setPessoaVO(mov.getResponsavel().getPessoa());
		lc.setContaCorrenteVO(contaCorrente);
		lc.setNomeLancamento("Vlr. ref. Conta corrente  - " + contaCorrente.getNomeApresentacaoSistema());
	}

	public ConfiguracaoContabilRegraVO obterRegraContabilParaMovimentacaoFinanceira(ConfiguracaoContabilVO conf, ContaCorrenteVO contaCorrente, FormaPagamentoVO formaPagamentoVO) {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasMovimentacaoFinanceira())) {
			Collections.sort(conf.getListaContabilRegrasMovimentacaoFinanceira(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasMovimentacaoFinanceira()) {
				if ((!Uteis.isAtributoPreenchido(regras.getContaCorrenteOrigemVO()) || (regras.getContaCorrenteOrigemVO().getCodigo().equals(contaCorrente.getCodigo()))) && (!Uteis.isAtributoPreenchido(regras.getFormaPagamentoVO()) || (regras.getFormaPagamentoVO().getCodigo().equals(formaPagamentoVO.getCodigo())))) {
					return regras;
				}
			}
		}
		return new ConfiguracaoContabilRegraVO();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gerarLancamentoContabilPorNotaFiscalEntradaImposto(NotaFiscalEntradaVO nfe, UsuarioVO usuarioLogado) {
		try {
			ConfiguracaoContabilVO conf = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(nfe.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioLogado);
			if (Uteis.isAtributoPreenchido(conf) && !nfe.getTipoNotaFiscalEntradaEnum().isProduto()) {
				List<TipoRegraContabilEnum> lista = new ArrayList<>();
				lista.add(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO);
				lista.add(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_IMPOSTO);
				getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(conf, lista, usuarioLogado);
				for (NotaFiscalEntradaImpostoVO nfei : nfe.getListaNotaFiscalEntradaImposto()) {
					if (nfei.isRetido()) {
						if (Uteis.isAtributoPreenchido(nfe)) {
							removeLancamentoContabilExistenteParaExclusao(nfe, null, nfei.getImpostoVO().getCodigo());
						}
						validarDadosParaGeracaoLancamentoContabil(nfe.getUnidadeEnsinoVO(), nfei.getValor());
						gerarLancamentoContabilRegrasNotaFiscalEntradaImposto(conf, nfe, nfei, usuarioLogado);
					}
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void gerarLancamentoContabilRegrasNotaFiscalEntradaImposto(ConfiguracaoContabilVO conf, NotaFiscalEntradaVO nfe, NotaFiscalEntradaImpostoVO nfei, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasNotaFiscalEntradaImposto())) {
			Collections.sort(conf.getListaContabilRegrasNotaFiscalEntradaImposto(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasNotaFiscalEntradaImposto()) {
				if ((!Uteis.isAtributoPreenchido(regras.getImpostoVO()) || (regras.getImpostoVO().getCodigo().equals(nfei.getImpostoVO().getCodigo())))) {
					gerarLancamentoContabilPorRegraNotaFiscalEntradaImposto(regras.getPlanoContaVO(), nfe, nfei, TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_IMPOSTO, TipoPlanoContaEnum.CREDITO, usuarioLogado);
					Map<Integer, List<NotaFiscalEntradaItemVO>> mapCategoriaProduto = nfe.getMapaCategoriaProdutoNotaFiscal();
					Double valorProporcional = Uteis.arrendondarForcando2CadasDecimais(nfei.getValor() / mapCategoriaProduto.size());
					Double valorDiferenca = Uteis.arrendondarForcando2CadasDecimais((valorProporcional * mapCategoriaProduto.size()) - nfei.getValor());
					int cont = 1;
					for (Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto : mapCategoriaProduto.entrySet()) {
						if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto())) {
							Collections.sort(conf.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
							for (ConfiguracaoContabilRegraVO regrasCategoriaProduto : conf.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto()) {
								if ((!Uteis.isAtributoPreenchido(regrasCategoriaProduto.getCategoriaProdutoVO()) || regrasCategoriaProduto.getCategoriaProdutoVO().getCodigo().equals(mapaCategoriaProduto.getKey()))) {
									if (cont == mapCategoriaProduto.size() && mapCategoriaProduto.size() > 1) {
										if (valorDiferenca > 0.0) {
											valorProporcional = Uteis.arrendondarForcando2CadasDecimais(valorProporcional - valorDiferenca);
										} else if (valorDiferenca < 0.0) {
											valorProporcional = Uteis.arrendondarForcando2CadasDecimais(valorProporcional + (valorDiferenca * -1));
										}
									}
									gerarLancamentoContabilPorRegraNotaFiscalEntradaItem(regrasCategoriaProduto.getPlanoContaVO(), nfe, mapaCategoriaProduto, valorProporcional, TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO, TipoPlanoContaEnum.DEBITO, usuarioLogado);
								}
							}
						}
						cont++;
					}
					break;
				}
			}
		}
	}

	private void gerarLancamentoContabilPorRegraNotaFiscalEntradaImposto(PlanoContaVO planoConta, NotaFiscalEntradaVO nfe, NotaFiscalEntradaImpostoVO nfei, TipoValorLancamentoContabilEnum tipoValor, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		// os campos abaixo sao preenchido aki pois na rotina de preenchimento e chamada
		// pelo metodo adicionar que tb alterar o valor do lancamento
		lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);		
		lc.setDataRegistro(nfe.getDataEmissao());
		lc.setDataCompensacao(nfe.getDataEntrada());
		lc.setValor(nfei.getValor());
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA);
		lc.setTipoValorLancamentoContabilEnum(tipoValor);
		preencherLancamentoContabilPorNotaFiscalEntradaImposto(lc, planoConta, nfe, nfei, tipoPlanoContaEnum, usuarioLogado);
		if (lc.getTipoPlanoConta().isCredito()) {
			preencherListaLancamentoContabilVO(nfe.getListaLancamentoContabeisCredito(), lc);
		} else {
			preencherListaLancamentoContabilVO(nfe.getListaLancamentoContabeisDebito(), lc);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherLancamentoContabilPorNotaFiscalEntradaImposto(LancamentoContabilVO lc, PlanoContaVO planoConta, NotaFiscalEntradaVO nfe, NotaFiscalEntradaImpostoVO nfei, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws StreamSeiException {
		lc.setPlanoContaVO(planoConta);
		lc.setImpostoVO(nfei.getImpostoVO());
		lc.setUnidadeEnsinoVO(nfe.getUnidadeEnsinoVO());
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		lc.setTipoSacado(TipoSacado.FORNECEDOR);
		lc.setFornecedorVO(nfe.getFornecedorVO());
		lc.setNomeLancamento("Tributo Retido  - " + nfe.getCodigo() + " - " + nfe.getFornecedorVO().getNome());
		preencherLancamentoContabilPorCentroNegocioOrigem(lc, nfe.getListaCentroResultadoOrigemVOs(), nfe.getTotalNotaEntrada(), false);

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gerarLancamentoContabilPorNotaFiscalEntradaItem(NotaFiscalEntradaVO nfe, UsuarioVO usuarioLogado) {
		try {
			if(!Uteis.isAtributoPreenchido(nfe.getConfiguracaoContabilVO())) {				
				nfe.setConfiguracaoContabilVO(getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(nfe.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioLogado));
			}
			if (Uteis.isAtributoPreenchido(nfe.getConfiguracaoContabilVO())) {
				List<TipoRegraContabilEnum> lista = new ArrayList<>();
				lista.add(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO);
				lista.add(TipoRegraContabilEnum.SACADO);
				getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(nfe.getConfiguracaoContabilVO(), lista, usuarioLogado);
				for (Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto : nfe.getMapaCategoriaProdutoNotaFiscal().entrySet()) {
					if (Uteis.isAtributoPreenchido(nfe)) {
						removeLancamentoContabilExistenteParaExclusao(nfe, mapaCategoriaProduto.getKey(), null);
					}
					validarDadosParaGeracaoLancamentoContabil(nfe.getUnidadeEnsinoVO(), nfe.calcularValorContabilPorMapaCategoriaProduto(mapaCategoriaProduto));
					Double valorLancamento = Uteis.arrendondarForcando2CadasDecimais(nfe.calcularValorContabilPorMapaCategoriaProduto(mapaCategoriaProduto));
					gerarLancamentoContabilRegrasNotaFiscalEntradaCategoriaProduto(nfe.getConfiguracaoContabilVO(), nfe, mapaCategoriaProduto, valorLancamento, usuarioLogado);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void gerarLancamentoContabilRegrasNotaFiscalEntradaCategoriaProduto(ConfiguracaoContabilVO conf, NotaFiscalEntradaVO nfe, Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto, Double valorLancamento, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto())) {
			Collections.sort(conf.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasNotaFiscalEntradaCategoriaProduto()) {
				if ((!Uteis.isAtributoPreenchido(regras.getCategoriaProdutoVO()) || regras.getCategoriaProdutoVO().getCodigo().equals(mapaCategoriaProduto.getKey()))) {
					gerarLancamentoContabilPorRegraNotaFiscalEntradaItem(regras.getPlanoContaVO(), nfe, mapaCategoriaProduto, valorLancamento, TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO, TipoPlanoContaEnum.DEBITO, usuarioLogado);
					geraLancamentoContabilRegrasNotaFiscalEntradaSacado(conf, nfe, mapaCategoriaProduto, valorLancamento, usuarioLogado);
					break;
				}
			}
		}
	}

	private void geraLancamentoContabilRegrasNotaFiscalEntradaSacado(ConfiguracaoContabilVO conf, NotaFiscalEntradaVO nfe, Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto, Double valorLancamento, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasSacado())) {
			Collections.sort(conf.getListaContabilRegrasSacado(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (ConfiguracaoContabilRegraVO regrasSacado : conf.getListaContabilRegrasSacado()) {
				if ((regrasSacado.getTipoSacadoPagar() == null || (Uteis.isAtributoPreenchido(regrasSacado.getTipoSacadoPagar()) && regrasSacado.getTipoSacadoPagar().equals(TipoSacado.FORNECEDOR))) && (regrasSacado.getCodigoSacado() == 0 || (Uteis.isAtributoPreenchido(regrasSacado.getCodigoSacado()) && regrasSacado.getCodigoSacado().equals(nfe.getFornecedorVO().getCodigo())))) {
					gerarLancamentoContabilPorRegraNotaFiscalEntradaItem(regrasSacado.getPlanoContaVO(), nfe, mapaCategoriaProduto, valorLancamento, TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO, TipoPlanoContaEnum.CREDITO, usuarioLogado);
					return;
				}
			}
			throw new StreamSeiException("Não foi encontrado nenhuma configuração contábil para o fornecedor " + nfe.getFornecedorVO().getNome());
		} else {
			throw new StreamSeiException("Não foi encontrado nenhuma configuração contábil para o fornecedor " + nfe.getFornecedorVO().getNome());
		}
	}

	private void gerarLancamentoContabilPorRegraNotaFiscalEntradaItem(PlanoContaVO planoConta, NotaFiscalEntradaVO nfe, Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto, Double valorLancamentoContabil, TipoValorLancamentoContabilEnum tipoValor, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		// os campos abaixo sao preenchido aki pois na rotina de preenchimento e chamada
		// pelo metodo adicionar que tb alterar o valor do lancamento
		lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
		lc.setDataRegistro(nfe.getDataEmissao());
		lc.setDataCompensacao(nfe.getDataEntrada());
		lc.setValor(valorLancamentoContabil);
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA);
		lc.setTipoValorLancamentoContabilEnum(tipoValor);
		preencherLancamentoContabilPorNotaFiscalEntradaItem(lc, planoConta, nfe, mapaCategoriaProduto, tipoPlanoContaEnum, usuarioLogado);
		if (lc.getTipoPlanoConta().isCredito()) {
			preencherListaLancamentoContabilVO(nfe.getListaLancamentoContabeisCredito(), lc);
		} else {
			preencherListaLancamentoContabilVO(nfe.getListaLancamentoContabeisDebito(), lc);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherLancamentoContabilPorNotaFiscalEntradaItem(LancamentoContabilVO lc, PlanoContaVO planoConta, NotaFiscalEntradaVO nfe, Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws StreamSeiException {
		lc.setPlanoContaVO(planoConta);
		lc.getCategoriaProdutoVO().setCodigo(mapaCategoriaProduto.getKey());
		lc.setUnidadeEnsinoVO(nfe.getUnidadeEnsinoVO());
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		lc.setTipoSacado(TipoSacado.FORNECEDOR);
		lc.setFornecedorVO(nfe.getFornecedorVO());
		lc.setNomeLancamento("Vlr. ref. NOTA FISCAL - " + nfe.getCodigo());
		preencherLancamentoContabilPorCentroNegocioOrigem(lc, nfe.getListaCentroResultadoOrigemVOs(), nfe.getTotalNotaEntrada(), false);
	}

	private void preencherLancamentoContabilPorCentroNegocioOrigem(LancamentoContabilVO lc, List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVO, Double valorTotalLancamento, boolean isCalculoPorcentagem) {
		for (CentroResultadoOrigemVO cro : listaCentroResultadoOrigemVO) {
			LancamentoContabilCentroNegocioVO lccn = new LancamentoContabilCentroNegocioVO();
			lccn.setLancamentoContabilVO(lc);
			lccn.setUnidadeEnsinoVO(cro.getUnidadeEnsinoVO());
			lccn.setCentroResultadoAdministrativo(cro.getCentroResultadoAdministrativo());
			lccn.setPercentual(cro.getPorcentagem());
			if (isCalculoPorcentagem) {
				lccn.setValor(Uteis.arrendondarForcando2CadasDecimais((cro.getPorcentagem() * valorTotalLancamento)) / 100);
			} else {
				lccn.setValor(cro.getValor());
			}
			if (cro.getTipoNivelCentroResultadoEnum().isUnidadeEnsino() || cro.getTipoNivelCentroResultadoEnum().isDepartamento()) {
				lccn.setTipoCentroNegocioEnum(TipoCentroNegocioEnum.ADMINISTRATIVO);
				lccn.setDepartamentoVO(cro.getDepartamentoVO());
				preencherListaLancamentoContabilCentroNegociacaoVO(lc.getListaCentroNegocioAdministrativo(), lccn);
			} else {
				lccn.setTipoCentroNegocioEnum(TipoCentroNegocioEnum.ACADEMICO);
				lccn.setCursoVO(cro.getCursoVO());
				lccn.setTurnoVO(cro.getTurnoVO());
				lccn.setTurmaVO(cro.getTurmaVO());
				preencherListaLancamentoContabilCentroNegociacaoVO(lc.getListaCentroNegocioAcademico(), lccn);
			}
		}
		lc.validarTotalizadoDoLancamentoContabilCentroNegociacao(lc.getValor());
	}

	private void removeLancamentoContabilExistenteParaExclusao(NotaFiscalEntradaVO nfe, Integer categoriaProduto, Integer imposto) {
		Iterator<LancamentoContabilVO> i = nfe.getListaLancamentoContabeisCredito().iterator();
		percorrerLancamentoParaExclusao(categoriaProduto, imposto, i);
		i = nfe.getListaLancamentoContabeisDebito().iterator();
		percorrerLancamentoParaExclusao(categoriaProduto, imposto, i);
	}

	private void percorrerLancamentoParaExclusao(Integer categoriaProduto, Integer imposto, Iterator<LancamentoContabilVO> i) {
		while (i.hasNext()) {
			LancamentoContabilVO objExistente = i.next();
			if ((Uteis.isAtributoPreenchido(categoriaProduto) && objExistente.getCategoriaProdutoVO().getCodigo().equals(categoriaProduto)) || (Uteis.isAtributoPreenchido(imposto) && objExistente.getImpostoVO().getCodigo().equals(imposto))) {
				validarDados(objExistente);
				i.remove();
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addLancamentoContabilCentroNegociacao(LancamentoContabilVO lc, LancamentoContabilCentroNegocioVO lccn, TipoCentroNegocioEnum tipoCategoriaRateio, UsuarioVO usuarioLogado) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(lccn.getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado para o Rateio.");
		Uteis.checkState((tipoCategoriaRateio.isAdministrativo() && !Uteis.isAtributoPreenchido(lccn.getDepartamentoVO())), "O campo Departamento deve ser informado para o Rateio.");
		Uteis.checkState((tipoCategoriaRateio.isAcademico() && !Uteis.isAtributoPreenchido(lccn.getDepartamentoVO()) && !Uteis.isAtributoPreenchido(lccn.getCursoVO())), "O campo Curso deve ser informado para o Rateio.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(lccn.getPercentual()), "O campo Percentual deve ser informado para o Rateio.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(lccn.getValor()), "O campo Valor deve ser informado para o Rateio.");
		lccn.setLancamentoContabilVO(lc);
		lccn.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(lccn.getUnidadeEnsinoVO().getCodigo(), false, usuarioLogado));
		if (lccn.isRateioAcademico()) {
			lccn.setCodigoContabil(lccn.getCursoVO().getCodigoContabil());
			lccn.setNomeContabil(lccn.getCursoVO().getNomeContabil());
			lccn.setNivelContabil(lccn.getCursoVO().getNivelContabil());
			preencherListaLancamentoContabilCentroNegociacaoVO(lc.getListaCentroNegocioAcademico(), lccn);
		} else {
			lccn.setCodigoContabil(lccn.getDepartamentoVO().getCodigoContabil());
			lccn.setNomeContabil(lccn.getDepartamentoVO().getNomeContabil());
			lccn.setNivelContabil(lccn.getDepartamentoVO().getNivelContabil());
			preencherListaLancamentoContabilCentroNegociacaoVO(lc.getListaCentroNegocioAdministrativo(), lccn);
		}
		if (lccn.isRateioAcademico() && (lc.getTotalCentroNegocioAcademico() > lc.getValor())) {
			String msg = "O Total do Rateio Acadêmico " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lc.getTotalCentroNegocioAcademico(), ",") + " não podem ser maior que o valor do lançamento contábil " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lc.getValor(), ",") + ".";
			removeLancamentoContabilCentroNegociacaoVO(lc, lccn, usuarioLogado);
			throw new StreamSeiException(msg);
		}
		if (lccn.isRateioAdministrativo() && (lc.getTotalCentroNegocioAdministrativo() > lc.getValor())) {
			String msg = "O Total do Rateio Administrativo " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lc.getTotalCentroNegocioAdministrativo(), ",") + " não podem ser maior que o valor do lançamento contábil " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(lc.getValor(), ",") + ".";
			removeLancamentoContabilCentroNegociacaoVO(lc, lccn, usuarioLogado);
			throw new StreamSeiException(msg);
		}
	}

	private void preencherListaLancamentoContabilCentroNegociacaoVO(List<LancamentoContabilCentroNegocioVO> lista, LancamentoContabilCentroNegocioVO lancamento) {
		int index = 0;
		for (LancamentoContabilCentroNegocioVO objExistente : lista) {
			if (objExistente.equalsCampoSelecaoLista(lancamento)) {
				if (!lancamento.isEdicaoManual()) {
					lancamento.setValor(objExistente.getValor() + lancamento.getValor());
					lancamento.setPercentual(objExistente.getPercentual() + lancamento.getPercentual());
				}
				lista.set(index, lancamento);
				return;
			}
			index++;
		}
		lista.add(lancamento);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removeLancamentoContabilCentroNegociacaoVO(LancamentoContabilVO lc, LancamentoContabilCentroNegocioVO lccn, UsuarioVO usuario) throws Exception {
		Iterator<LancamentoContabilCentroNegocioVO> i = null;
		if (lccn.isRateioAcademico()) {
			i = lc.getListaCentroNegocioAcademico().iterator();
		} else {
			i = lc.getListaCentroNegocioAdministrativo().iterator();
		}
		while (i.hasNext()) {
			LancamentoContabilCentroNegocioVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(lccn)) {
				i.remove();
				return;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherListaLancamentoContabilVO(List<LancamentoContabilVO> lista, LancamentoContabilVO lancamento) {
		int index = 0;
		for (LancamentoContabilVO objExistente : lista) {
			if (objExistente.equalsCampoSelecaoLista(lancamento)) {
				if (!objExistente.isEdicaoManual()) {
					lancamento.setValor(lancamento.getValor() + objExistente.getValor());
					lancamento.recalcularRaterioLancamentoContabil();
				}
				lista.set(index, lancamento);
				return;
			}
			index++;
		}
		lista.add(lancamento);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removeLancamentoContabilVO(List<LancamentoContabilVO> lista, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception {
		Iterator<LancamentoContabilVO> i = lista.iterator();
		while (i.hasNext()) {
			LancamentoContabilVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(lancamento)) {
				i.remove();
				return;
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(String codOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(codigo) as qtd from lancamentocontabil ");
		sqlStr.append(" WHERE codorigem = '").append(codOrigem).append("' and tipoorigemlancamentocontabil = '").append(tipoOrigem.name()).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, "qtd", TipoCampoEnum.INTEIRO);
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT lc.codigo as \"lc.codigo\", lc.dataregistro as \"lc.dataregistro\", lc.nomelancamento as \"lc.nomelancamento\", lc.tipoorigemlancamentocontabil as \"lc.tipoorigemlancamentocontabil\", ");
		sql.append(" lc.codorigem as \"lc.codorigem\", lc.valor as \"lc.valor\", lc.tiposacado as \"lc.tiposacado\", lc.tipovalorlancamentocontabil as \"lc.tipovalorlancamentocontabil\", ");
		sql.append(" lc.situacaoLancamentoContabil as \"lc.situacaoLancamentoContabil\", ");
		sql.append(" lc.datacompensacao as \"lc.datacompensacao\",  ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");
		sql.append(" integracaocontabil.codigo as \"integracaocontabil.codigo\", integracaocontabil.lote as \"integracaocontabil.lote\", ");

		sql.append(" planoconta.codigo as \"planoconta.codigo\", planoconta.identificadorplanoconta as \"planoconta.identificadorplanoconta\", planoconta.descricao as \"planoconta.descricao\", lc.tipoPlanoConta as \"lc.tipoPlanoConta\", ");
		sql.append(" planoconta.codigoPatrimonial as \"planoconta.codigoPatrimonial\",  planoconta.codigoReduzido as \"planoconta.codigoReduzido\", ");
		sql.append(" contaCorrente.codigo as \"contaCorrente.codigo\", contaCorrente.numero as \"contaCorrente.numero\", contacorrente.carteira as \"contacorrente.carteira\", contaCorrente.digito as \"contaCorrente.digito\", ");
		sql.append(" contacorrente.nomeApresentacaoSistema as \"contacorrente.nomeApresentacaoSistema\", contacorrente.contacaixa as \"contacorrente.contacaixa\",  ");
		sql.append(" contacorrente.tipoContaCorrente as \"contacorrente.tipoContaCorrente\", ");
		sql.append(" agencia.numeroAgencia as \"agencia.numeroAgencia\",  agencia.numero as \"agencia.numero\", agencia.codigo as \"agencia.codigo\", agencia.digito as \"agencia.digito\", ");
		sql.append(" bancocorrente.codigo as \"bancocorrente.codigo\", bancocorrente.nrbanco as \"bancocorrente.nrbanco\", bancocorrente.nome as \"bancocorrente.nome\", bancocorrente.digito as \"bancocorrente.digito\", ");
		sql.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\", fornecedor.cnpj as \"fornecedor.cnpj\", fornecedor.cpf as \"fornecedor.cpf\", ");
		sql.append(" funcionario.codigo as \"funcionario.codigo\", funcionario.matricula as \"funcionario.matricula\",");
		sql.append(" pesfunc.codigo as \"pesfunc.codigo\", pesfunc.nome as \"pesfunc.nome\", pesfunc.cpf as \"pesfunc.cpf\",  ");
		sql.append(" banco.codigo as \"banco.codigo\", banco.nrbanco as \"banco.nrbanco\", banco.nome as \"banco.nome\", banco.digito as \"banco.digito\",");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.cpf as \"pessoa.cpf\", ");
		sql.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", parceiro.cnpj as \"parceiro.cnpj\", parceiro.cpf as \"parceiro.cpf\", ");
		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\", ");

		sql.append(" contareceber.codigo as \"contareceber.codigo\", contareceber.nossonumero as \"contareceber.nossonumero\", ");
		sql.append(" contareceber.datavencimento as \"contareceber.datavencimento\", contareceber.parcela as \"contareceber.parcela\", ");
		sql.append(" contareceber.tipoorigem as \"contareceber.tipoorigem\",  ");

		sql.append(" contapagar.codigo as \"contapagar.codigo\", contapagar.parcela as \"contapagar.parcela\", contapagar.nossonumero as \"contapagar.nossonumero\", ");
		sql.append(" contapagar.numeroNotaFiscalEntrada as \"contapagar.numeroNotaFiscalEntrada\",  contapagar.nrDocumento as \"contapagar.nrDocumento\",   ");
		sql.append(" contapagar.tipoOrigem as \"contapagar.tipoOrigem\", contapagar.codigoNotaFiscalEntrada as \"contapagar.codigoNotaFiscalEntrada\", ");

		sql.append(" notafiscalentrada.codigo as \"notafiscalentrada.codigo\", notafiscalentrada.numero as \"notafiscalentrada.numero\", ");

		sql.append(" imposto.codigo as \"imposto.codigo\", imposto.nome as \"imposto.nome\" ");

		sql.append(" FROM lancamentocontabil lc ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = lc.unidadeensino ");
		sql.append(" inner join planoconta on planoconta.codigo = lc.planoconta ");
		sql.append(" left join contacorrente on contacorrente.codigo = lc.contacorrente ");
		sql.append(" left join agencia ON agencia.codigo = contaCorrente.agencia ");
		sql.append(" left join banco bancocorrente ON agencia.banco = bancocorrente.codigo ");
		sql.append(" left join fornecedor on fornecedor.codigo = lc.fornecedor ");
		sql.append(" left join funcionario on funcionario.codigo = lc.funcionario ");
		sql.append(" left join pessoa pesfunc on pesfunc.codigo = funcionario.pessoa ");
		sql.append(" left join banco  ON banco.codigo = lc.banco ");
		sql.append(" left join pessoa on pessoa.codigo = lc.pessoa ");
		sql.append(" left join parceiro on parceiro.codigo = lc.parceiro");
		sql.append(" left join categoriaproduto on categoriaproduto.codigo = lc.categoriaproduto");
		sql.append(" left join imposto on imposto.codigo = lc.imposto");
		sql.append(" left join integracaocontabil on integracaocontabil.codigo = lc.integracaocontabil ");
		sql.append(" left join contareceber on contareceber.codigo = lc.codorigem::integer and tipoorigemlancamentocontabil = 'RECEBER' ");
		sql.append(" left join contapagar on contapagar.codigo = lc.codorigem::integer and tipoorigemlancamentocontabil = 'PAGAR' ");
		sql.append(" left join notafiscalentrada on notafiscalentrada.codigo = lc.codorigem::integer and tipoorigemlancamentocontabil = 'NOTA_FISCAL_ENTRADA' ");
		sql.append(" left join movimentacaofinanceira on movimentacaofinanceira.codigo = lc.codorigem::integer and tipoorigemlancamentocontabil = 'MOVIMENTACAO_FINANCEIRA' ");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LancamentoContabilVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lc.codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY lc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LancamentoContabilVO> consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(String codOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, TipoPlanoContaEnum tipoPlanoConta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lc.codorigem = '").append(codOrigem).append("' and lc.tipoorigemlancamentocontabil = '").append(tipoOrigem.name()).append("' ");
		sqlStr.append(" and lc.tipoPlanoConta = '").append(tipoPlanoConta.name()).append("'");
		sqlStr.append(" ORDER BY lc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LancamentoContabilVO> consultaRapidaPorListaCodOrigemPorTipoOrigemPorTipoPlanoConta(String listaCodOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, TipoPlanoContaEnum tipoPlanoConta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lc.codorigem in (").append(listaCodOrigem).append(") and lc.tipoorigemlancamentocontabil = ? ");
		sqlStr.append(" and lc.tipoPlanoConta = ? ");
		sqlStr.append(" ORDER BY lc.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), tipoOrigem.name(), tipoPlanoConta.name());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaLancamentoContabilPorIntegracaoContabil(IntegracaoContabilVO integracaoContabil, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lc.integracaoContabil = ").append(integracaoContabil.getCodigo()).append(" ");
		sqlStr.append(" ORDER BY lc.codigo, lc.codorigem, lc.tipoorigemlancamentocontabil desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		montarDadosConsultaBasica(integracaoContabil, tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaLoteLancamentoContabilParaProcessamento(IntegracaoContabilVO integracaoContabil, LayoutIntegracaoVO layoutIntegracaoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  ");
		sqlStr.append(" ((lc.integracaoContabil is null and lc.situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.COMPENSADO.name()).append("') ");
		if (Uteis.isAtributoPreenchido(integracaoContabil.getCodigo())) {
			sqlStr.append(" or (lc.situacaolancamentocontabil = '").append(SituacaoLancamentoContabilEnum.CONTABILIZADO.name()).append("' and lc.integracaocontabil = ").append(integracaoContabil.getCodigo()).append(") ");
		}
		sqlStr.append(" )");
		if (integracaoContabil.getTipoGeracaoIntegracaoContabilEnum().isUnidadeEnsino()) {
			sqlStr.append(" and unidadeensino.codigo = ").append(integracaoContabil.getUnidadeEnsinoVO().getCodigo());
		} else if (integracaoContabil.getTipoGeracaoIntegracaoContabilEnum().isCodigoIntegracao()) {
			sqlStr.append(" and unidadeensino.codigointegracaocontabil = '").append(integracaoContabil.getCodigoIntegracaoContabil()).append("' ");
		}
		sqlStr.append(" and lc.datacompensacao >= '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(integracaoContabil.getDataInicio())).append("' ");
		sqlStr.append(" and lc.datacompensacao <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(integracaoContabil.getDataTermino())).append("' ");
		if (layoutIntegracaoVO.getTipoLayoutPlanoContaEnum().isCredito()) {
			sqlStr.append(" and lc.tipoPlanoConta = '").append(TipoPlanoContaEnum.CREDITO.name()).append("' ");
		} else if (layoutIntegracaoVO.getTipoLayoutPlanoContaEnum().isDebito()) {
			sqlStr.append(" and lc.tipoPlanoConta = '").append(TipoPlanoContaEnum.DEBITO.name()).append("' ");
		}

		StringBuilder tipoOrigemFiltro = new StringBuilder();
		if (integracaoContabil.isOrigemContaPagar()) {
			UteisTexto.addCampoParaClausaIn(tipoOrigemFiltro, TipoOrigemLancamentoContabilEnum.PAGAR.name(), true);
		}
		if (integracaoContabil.isOrigemContaReceber()) {
			UteisTexto.addCampoParaClausaIn(tipoOrigemFiltro, TipoOrigemLancamentoContabilEnum.RECEBER.name(), true);
		}
		if (integracaoContabil.isOrigemNotaFiscalEntrada()) {
			UteisTexto.addCampoParaClausaIn(tipoOrigemFiltro, TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA.name(), true);
		}
		if (integracaoContabil.isOrigemMovFinanceira()) {
			UteisTexto.addCampoParaClausaIn(tipoOrigemFiltro, TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA.name(), true);
		}
		if (integracaoContabil.isOrigemNegociacaoContaPagar()) {
			UteisTexto.addCampoParaClausaIn(tipoOrigemFiltro, TipoOrigemLancamentoContabilEnum.NEGOCIACAO_CONTA_PAGAR.name(), true);
		}
		if (integracaoContabil.isOrigemMapaPendenciaCartao()) {
			UteisTexto.addCampoParaClausaIn(tipoOrigemFiltro, TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO.name(), true);
		}
		if (!tipoOrigemFiltro.toString().isEmpty()) {
			sqlStr.append(" and lc.tipoorigemlancamentocontabil in (").append(tipoOrigemFiltro.toString()).append(") ");
		}

		sqlStr.append(" ORDER BY lc.tipoorigemlancamentocontabil, lc.datacompensacao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		montarDadosConsultaBasica(integracaoContabil, tabelaResultado, nivelMontarDados, usuario);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void montarDadosConsultaBasica(IntegracaoContabilVO integracaoContabil, SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		integracaoContabil.getListaLancamentoContabil().clear();
		integracaoContabil.getListaLancamentoContabeisCredito().clear();
		integracaoContabil.getListaLancamentoContabeisDebito().clear();
		integracaoContabil.setOrigemMovFinanceira(false);
		integracaoContabil.setOrigemContaPagar(false);
		integracaoContabil.setOrigemContaReceber(false);
		integracaoContabil.setOrigemMapaPendenciaCartao(false);
		integracaoContabil.setOrigemNegociacaoContaPagar(false);
		integracaoContabil.setOrigemNotaFiscalEntrada(false);
		while (tabelaResultado.next()) {
			LancamentoContabilVO lc = new LancamentoContabilVO();
			montarDadosBasico(lc, tabelaResultado, nivelMontarDados, usuario);
			lc.getListaLancamentoContabilCentroNegocioVO().clear();
			lc.getListaLancamentoContabilCentroNegocioVO().addAll(lc.getListaCentroNegocioAcademico());
			lc.getListaLancamentoContabilCentroNegocioVO().addAll(lc.getListaCentroNegocioAdministrativo());
			lc.setIntegracaoContabilVO(integracaoContabil);
			integracaoContabil.setValorLote(integracaoContabil.getValorLote() + lc.getValor());
			if (lc.getTipoPlanoConta().isCredito()) {
				integracaoContabil.getListaLancamentoContabeisCredito().add(lc);
			} else {
				integracaoContabil.getListaLancamentoContabeisDebito().add(lc);
			}
			integracaoContabil.getListaLancamentoContabil().add(lc);
			if (lc.getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira()) {
				integracaoContabil.setOrigemMovFinanceira(true);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar()) {
				integracaoContabil.setOrigemNegociacaoContaPagar(true);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isPagar()) {
				integracaoContabil.setOrigemContaPagar(true);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isReceber()) {
				integracaoContabil.setOrigemContaReceber(true);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada()) {
				integracaoContabil.setOrigemNotaFiscalEntrada(true);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isCartaoCredito()) {
				integracaoContabil.setOrigemMapaPendenciaCartao(true);
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LancamentoContabilVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<LancamentoContabilVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			LancamentoContabilVO obj = new LancamentoContabilVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(LancamentoContabilVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("lc.codigo")));
		obj.setDataRegistro(dadosSQL.getTimestamp("lc.dataregistro"));
		obj.setDataCompensacao(dadosSQL.getTimestamp("lc.datacompensacao"));
		obj.setNomeLancamento(dadosSQL.getString("lc.nomelancamento"));
		obj.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.valueOf(dadosSQL.getString("lc.tipoorigemlancamentocontabil")));
		obj.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.valueOf(dadosSQL.getString("lc.SituacaoLancamentoContabil")));
		obj.setCodOrigem(dadosSQL.getString("lc.codorigem"));
		obj.setValor(dadosSQL.getDouble("lc.valor"));
		obj.setTipoSacado(TipoSacado.valueOf(dadosSQL.getString("lc.tiposacado")));
		obj.setTipoValorLancamentoContabilEnum(TipoValorLancamentoContabilEnum.valueOf(dadosSQL.getString("lc.tipovalorlancamentocontabil")));

		obj.getUnidadeEnsinoVO().setCodigo((dadosSQL.getInt("unidadeensino.codigo")));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));

		obj.getPlanoContaVO().setCodigo((dadosSQL.getInt("planoconta.codigo")));
		obj.getPlanoContaVO().setDescricao(dadosSQL.getString("planoconta.descricao"));
		obj.getPlanoContaVO().setIdentificadorPlanoConta(dadosSQL.getString("planoconta.identificadorPlanoConta"));
		obj.getPlanoContaVO().setCodigoPatrimonial(dadosSQL.getString("planoconta.codigoPatrimonial"));
		obj.getPlanoContaVO().setCodigoReduzido(dadosSQL.getInt("planoconta.codigoReduzido"));
		obj.setTipoPlanoConta(TipoPlanoContaEnum.valueOf(dadosSQL.getString("lc.tipoPlanoConta")));

		obj.getContaCorrenteVO().setCodigo((dadosSQL.getInt("contacorrente.codigo")));
		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contacorrente.numero"));
		obj.getContaCorrenteVO().setDigito(dadosSQL.getString("contacorrente.digito"));
		obj.getContaCorrenteVO().setCarteira(dadosSQL.getString("contacorrente.carteira"));
		obj.getContaCorrenteVO().setNomeApresentacaoSistema(dadosSQL.getString("contacorrente.nomeApresentacaoSistema"));
		obj.getContaCorrenteVO().setContaCaixa(dadosSQL.getBoolean("contacorrente.contacaixa"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("contacorrente.tipoContaCorrente"))) {
			obj.getContaCorrenteVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contacorrente.tipoContaCorrente")));
		}
		obj.getContaCorrenteVO().getAgencia().setCodigo(dadosSQL.getInt("agencia.codigo"));
		obj.getContaCorrenteVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agencia.numeroAgencia"));
		obj.getContaCorrenteVO().getAgencia().setNumero(dadosSQL.getString("agencia.numero"));
		obj.getContaCorrenteVO().getAgencia().setDigito(dadosSQL.getString("agencia.digito"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("bancocorrente.codigo"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("bancocorrente.nome"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNrBanco(dadosSQL.getString("bancocorrente.nrBanco"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setDigito(dadosSQL.getString("bancocorrente.digito"));

		obj.getFornecedorVO().setCodigo((dadosSQL.getInt("fornecedor.codigo")));
		obj.getFornecedorVO().setNome(dadosSQL.getString("fornecedor.nome"));
		obj.getFornecedorVO().setCNPJ(dadosSQL.getString("fornecedor.cnpj"));
		obj.getFornecedorVO().setCPF(dadosSQL.getString("fornecedor.cpf"));

		obj.getFuncionarioVO().setCodigo((dadosSQL.getInt("funcionario.codigo")));
		obj.getFuncionarioVO().setMatricula(dadosSQL.getString("funcionario.matricula"));
		obj.getFuncionarioVO().getPessoa().setCodigo((dadosSQL.getInt("pesfunc.codigo")));
		obj.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("pesfunc.nome"));
		obj.getFuncionarioVO().getPessoa().setCPF(dadosSQL.getString("pesfunc.cpf"));

		obj.getBancoVO().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getBancoVO().setNome(dadosSQL.getString("banco.nome"));
		obj.getBancoVO().setNrBanco(dadosSQL.getString("banco.nrBanco"));
		obj.getBancoVO().setDigito(dadosSQL.getString("banco.digito"));

		obj.getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaVO().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getPessoaVO().setCPF(dadosSQL.getString("pessoa.cpf"));

		obj.getParceiroVO().setCodigo((dadosSQL.getInt("parceiro.codigo")));
		obj.getParceiroVO().setNome(dadosSQL.getString("parceiro.nome"));
		obj.getParceiroVO().setCNPJ(dadosSQL.getString("parceiro.cnpj"));
		obj.getParceiroVO().setCPF(dadosSQL.getString("parceiro.cpf"));

		obj.getCategoriaProdutoVO().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getCategoriaProdutoVO().setNome(dadosSQL.getString("categoriaProduto.nome"));

		obj.getImpostoVO().setCodigo((dadosSQL.getInt("imposto.codigo")));
		obj.getImpostoVO().setNome(dadosSQL.getString("imposto.nome"));

		getFacadeFactory().getLancamentoContabilCentroNegocioFacade().consultaRapidaPorLancamentoContabilVO(obj, false, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
		obj.getIntegracaoContabilVO().setCodigo((dadosSQL.getInt("integracaocontabil.codigo")));
		obj.getIntegracaoContabilVO().setLote((dadosSQL.getInt("integracaocontabil.lote")));
		if (obj.getTipoOrigemLancamentoContabilEnum().isReceber()) {
			obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber.codigo"));
			obj.getContaReceberVO().setNossoNumero(dadosSQL.getString("contareceber.nossonumero"));
			obj.getContaReceberVO().setParcela(dadosSQL.getString("contareceber.parcela"));
			obj.getContaReceberVO().setTipoOrigem(dadosSQL.getString("contareceber.tipoorigem"));
			obj.getContaReceberVO().setDataVencimento(dadosSQL.getDate("contareceber.datavencimento"));
		} else if (obj.getTipoOrigemLancamentoContabilEnum().isPagar()) {
			obj.getContaPagarVO().setCodigo(dadosSQL.getInt("contapagar.codigo"));
			obj.getContaPagarVO().setNossoNumero(dadosSQL.getLong("contapagar.nossonumero"));
			obj.getContaPagarVO().setParcela(dadosSQL.getString("contapagar.parcela"));
			obj.getContaPagarVO().setNumeroNotaFiscalEntrada(dadosSQL.getString("contapagar.numeroNotaFiscalEntrada"));
			obj.getContaPagarVO().setCodigoNotaFiscalEntrada(dadosSQL.getString("contapagar.codigoNotaFiscalEntrada"));
			obj.getContaPagarVO().setNrDocumento(dadosSQL.getString("contapagar.nrDocumento"));
			obj.getContaPagarVO().setTipoOrigem(dadosSQL.getString("contapagar.tipoorigem"));
		} else if (obj.getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada()) {
			obj.getNotaFiscalEntradaVO().setCodigo(dadosSQL.getInt("notafiscalentrada.codigo"));
			obj.getNotaFiscalEntradaVO().setNumero(dadosSQL.getLong("notafiscalentrada.numero"));
		}
		/**
		 * Gets obrigatorio para preencher atributos que serao colocados no XML
		 */
		obj.getNomeSacado();
		obj.getCnpjSacado();
		obj.getNaturezaLancamento();
		obj.getIndTitulo();
		obj.getViaArrecadacao();
		obj.getBancoArrecadacao();
		obj.getAgenciaArrecadacao();
		obj.getContaArrecadacao();
		obj.getDigitoContaArrecadacao();
		obj.getTipoArrecadacao();
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return LancamentoContabil.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LancamentoContabil.idEntidade = idEntidade;
	}

	private LancamentoContabilVO realizarPreenchimentoLancamentoContabilPorNegociacaoContaPagar(NegociacaoContaPagarVO negociacaoContaPagarVO, List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, PlanoContaVO planoConta, TipoValorLancamentoContabilEnum tipoValor, Double valor, TipoPlanoContaEnum tipoPlanoContaEnum, boolean isCalculoPorcentagem) {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
		lc.setPlanoContaVO(planoConta);
		lc.setCodOrigem(negociacaoContaPagarVO.getCodigo().toString());
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.NEGOCIACAO_CONTA_PAGAR);
		lc.setTipoValorLancamentoContabilEnum(tipoValor);
		lc.setUnidadeEnsinoVO(negociacaoContaPagarVO.getUnidadeEnsino());
		lc.setDataRegistro(new Date());
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		if (lc.getSituacaoLancamentoContabilEnum().isCompensado()) {
			lc.setDataCompensacao(lc.getDataRegistro());
		}
		lc.setValor(valor);
		lc.setTipoSacado(negociacaoContaPagarVO.getTipoSacado());
		if (negociacaoContaPagarVO.getTipoSacado().isAluno()) {
			lc.setPessoaVO(negociacaoContaPagarVO.getPessoa());
		} else if (negociacaoContaPagarVO.getTipoSacado().isBanco()) {
			lc.setBancoVO(negociacaoContaPagarVO.getBancoVO());
		} else if (negociacaoContaPagarVO.getTipoSacado().isFornecedor()) {
			lc.setFornecedorVO(negociacaoContaPagarVO.getFornecedor());
		} else if (negociacaoContaPagarVO.getTipoSacado().isFuncionario()) {
			lc.setFuncionarioVO(negociacaoContaPagarVO.getFuncionario());
		} else if (negociacaoContaPagarVO.getTipoSacado().isParceiro()) {
			lc.setParceiroVO(negociacaoContaPagarVO.getParceiro());
		}
		if (tipoValor.equals(TipoValorLancamentoContabilEnum.JURO)) {
			lc.setNomeLancamento("Valor Referente ao Juro");
		} else if (tipoValor.equals(TipoValorLancamentoContabilEnum.MULTA)) {
			lc.setNomeLancamento("Valor Referente a Multa");
		} else if (tipoValor.equals(TipoValorLancamentoContabilEnum.PAGAMENTO)) {
			lc.setNomeLancamento("Valor Referente ao Desconto");
		}
		if (centroResultadoOrigemVOs != null && !centroResultadoOrigemVOs.isEmpty()) {
			preencherLancamentoContabilPorCentroNegocioOrigem(lc, centroResultadoOrigemVOs, valor, isCalculoPorcentagem);
		}
		return lc;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarLancamentoContabilPorNegociacaoContaPagar(NegociacaoContaPagarVO negociacaoContaPagarVO, Boolean forcarRecarregamentoConfiguracaoContabil, UsuarioVO usuarioLogado) throws Exception {
		negociacaoContaPagarVO.getLancamentoContabilCreditoVOs().clear();
		negociacaoContaPagarVO.getLancamentoContabilDebitoVOs().clear();
		if (negociacaoContaPagarVO.getDesconto() > 0 || negociacaoContaPagarVO.getJuro() > 0 || negociacaoContaPagarVO.getMulta() > 0) {
			if (!Uteis.isAtributoPreenchido(negociacaoContaPagarVO.getConfiguracaoContabilVO()) || forcarRecarregamentoConfiguracaoContabil) {
				negociacaoContaPagarVO.setConfiguracaoContabilVO(getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(negociacaoContaPagarVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioLogado));
				if (Uteis.isAtributoPreenchido(negociacaoContaPagarVO.getConfiguracaoContabilVO())) {
					List<TipoRegraContabilEnum> lista = new ArrayList<>();
					lista.add(TipoRegraContabilEnum.DESCONTO_PAGAR);
					lista.add(TipoRegraContabilEnum.JURO_MULTA_PAGAR);
					lista.add(TipoRegraContabilEnum.SACADO);
					getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(negociacaoContaPagarVO.getConfiguracaoContabilVO(), lista, usuarioLogado);
				} else {
					if (forcarRecarregamentoConfiguracaoContabil) {
						throw new Exception("Não foi encontrado uma CONFIGURAÇÃO CONTÁBIL definida para a unidade de ensino desta negociação.");
					}
				}
			}
			if (Uteis.isAtributoPreenchido(negociacaoContaPagarVO.getConfiguracaoContabilVO())) {
				List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = new ArrayList<CentroResultadoOrigemVO>(0);
				for (ContaPagarVO contaPagarVO : negociacaoContaPagarVO.getContaPagarGeradaVOs()) {
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarAgrupamentoCentroResultadoOrigemVOSomandoValor(centroResultadoOrigemVOs, contaPagarVO.getListaCentroResultadoOrigemVOs(), true);
				}
				if (!centroResultadoOrigemVOs.isEmpty()) {
					Double valorTotalCentroResultados = centroResultadoOrigemVOs.stream().mapToDouble(CentroResultadoOrigemVO::getValor).sum();
					Map<String, Double> mapPorcentagemCentroResultado = new HashMap<String, Double>(0);
					centroResultadoOrigemVOs.forEach(cr -> {
						mapPorcentagemCentroResultado.put(cr.toString(), Uteis.arrendondarForcando2CadasDecimais((cr.getValor() * 100) / valorTotalCentroResultados));
					});
					// aqui soma o percentual para ver se deu 100%, caso não tenha dado é jogado a
					// diferença no 1 centro de resultado
					Double somaPercentual = mapPorcentagemCentroResultado.values().stream().mapToDouble(p -> p).sum();
					if (somaPercentual < 100) {
						mapPorcentagemCentroResultado.put(centroResultadoOrigemVOs.get(0).toString(), Uteis.arrendondarForcando2CadasDecimais(mapPorcentagemCentroResultado.get(centroResultadoOrigemVOs.get(0).toString()) + (100 - somaPercentual)));
					}
					for (CentroResultadoOrigemVO centroResultadoOrigemVO : centroResultadoOrigemVOs) {
						centroResultadoOrigemVO.setPorcentagem(mapPorcentagemCentroResultado.get(centroResultadoOrigemVO.toString()));
					}
				}

				gerarLancamentoContabilRegrasJuroMultaAcrescimoNegociacaoContaPagar(negociacaoContaPagarVO.getConfiguracaoContabilVO(), negociacaoContaPagarVO, centroResultadoOrigemVOs, usuarioLogado);
				gerarLancamentoContabilRegrasDescontoNegociacaoContaPagar(negociacaoContaPagarVO.getConfiguracaoContabilVO(), negociacaoContaPagarVO, centroResultadoOrigemVOs, usuarioLogado);
			}
		}
	}

	private void gerarLancamentoContabilRegrasJuroMultaAcrescimoNegociacaoContaPagar(ConfiguracaoContabilVO conf, NegociacaoContaPagarVO negociacaoContaPagarVO, List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasJuroMultaPagar())) {
			if (Uteis.isAtributoPreenchido(negociacaoContaPagarVO.getJuro())) {
				preencherLancamentoContabilRegrasJuroMultaAcrescimoNegociacaoContaPagar(conf, negociacaoContaPagarVO, centroResultadoOrigemVOs, negociacaoContaPagarVO.getJuro(), TipoDesconto.JURO, TipoValorLancamentoContabilEnum.JURO, usuarioLogado);
			}
			if (Uteis.isAtributoPreenchido(negociacaoContaPagarVO.getMulta())) {
				preencherLancamentoContabilRegrasJuroMultaAcrescimoNegociacaoContaPagar(conf, negociacaoContaPagarVO, centroResultadoOrigemVOs, negociacaoContaPagarVO.getMulta(), TipoDesconto.MULTA, TipoValorLancamentoContabilEnum.MULTA, usuarioLogado);
			}
		}
	}

	private void preencherLancamentoContabilRegrasJuroMultaAcrescimoNegociacaoContaPagar(ConfiguracaoContabilVO conf, NegociacaoContaPagarVO negociacaoContaPagarVO, List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, Double valor, TipoDesconto tipoDesconto, TipoValorLancamentoContabilEnum tipoValor, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoContabilRegraVO regras = obterRegraContabilParaJuroMultaAcrescimoContaPagar(conf, tipoDesconto);
		if (Uteis.isAtributoPreenchido(regras)) {
			LancamentoContabilVO lancamentoContabilDebito = realizarPreenchimentoLancamentoContabilPorNegociacaoContaPagar(negociacaoContaPagarVO, centroResultadoOrigemVOs, regras.getPlanoContaVO(), tipoValor, valor, TipoPlanoContaEnum.DEBITO, true);
			if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasSacado())) {
				Collections.sort(conf.getListaContabilRegrasSacado(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());

				ConfiguracaoContabilRegraVO regrasContaPagar = obterRegraContabilParaSacadoContaPagar(conf, negociacaoContaPagarVO.getTipoSacado(), negociacaoContaPagarVO.getCodigoPessoaSacado());
				if (Uteis.isAtributoPreenchido(regrasContaPagar)) {
					LancamentoContabilVO lancamentoContabilCredito = realizarPreenchimentoLancamentoContabilPorNegociacaoContaPagar(negociacaoContaPagarVO, centroResultadoOrigemVOs, regrasContaPagar.getPlanoContaVO(), tipoValor, valor, TipoPlanoContaEnum.CREDITO, true);
					negociacaoContaPagarVO.getLancamentoContabilCreditoVOs().add(lancamentoContabilCredito);
					negociacaoContaPagarVO.getLancamentoContabilDebitoVOs().add(lancamentoContabilDebito);
					return;
				}
			}
			throw new StreamSeiException("Existe um lançamento contábil para Juro/Multa porem não foi encontrado nenhuma configuração contábil da  regra de sacado");
		}

	}

	private void gerarLancamentoContabilRegrasDescontoNegociacaoContaPagar(ConfiguracaoContabilVO conf, NegociacaoContaPagarVO negociacaoContaPagarVO, List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, UsuarioVO usuarioLogado) throws Exception {

		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasDescontoPagar()) && Uteis.isAtributoPreenchido(negociacaoContaPagarVO.getDesconto())) {
			ConfiguracaoContabilRegraVO regras = obterRegraContabilParaDescontoContaPagar(conf, negociacaoContaPagarVO.getTipoSacado(), negociacaoContaPagarVO.getCodigoPessoaSacado(), TipoDesconto.PAGAMENTO);
			if (Uteis.isAtributoPreenchido(regras)) {
				LancamentoContabilVO lancamentoContabilCredito = realizarPreenchimentoLancamentoContabilPorNegociacaoContaPagar(negociacaoContaPagarVO, centroResultadoOrigemVOs, regras.getPlanoContaVO(), TipoValorLancamentoContabilEnum.PAGAMENTO, negociacaoContaPagarVO.getDesconto(), TipoPlanoContaEnum.CREDITO, true);
				ConfiguracaoContabilRegraVO regrasContaPagar = obterRegraContabilParaSacadoContaPagar(conf, negociacaoContaPagarVO.getTipoSacado(), negociacaoContaPagarVO.getCodigoPessoaSacado());
				if (Uteis.isAtributoPreenchido(regrasContaPagar)) {
					LancamentoContabilVO lancamentoContabilDebito = realizarPreenchimentoLancamentoContabilPorNegociacaoContaPagar(negociacaoContaPagarVO, centroResultadoOrigemVOs, regrasContaPagar.getPlanoContaVO(), TipoValorLancamentoContabilEnum.PAGAMENTO, negociacaoContaPagarVO.getDesconto(), TipoPlanoContaEnum.DEBITO, true);
					negociacaoContaPagarVO.getLancamentoContabilCreditoVOs().add(lancamentoContabilCredito);
					negociacaoContaPagarVO.getLancamentoContabilDebitoVOs().add(lancamentoContabilDebito);
					return;
				}
				throw new StreamSeiException("Existe um lançamento contábil para DESCONTO porem não foi encontrado nenhuma configuração contábil da  regra de sacado");

			}
		}

	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarLancamentoContabilPorMapaPendenciaCartaoCredito(Map<Integer, List<MapaPendenciaCartaoCreditoVO>> map, Date dataBaixa, boolean abaterTaxaExtratoContaCorrente, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception {
		for (Map.Entry<Integer, List<MapaPendenciaCartaoCreditoVO>> mapa : map.entrySet()) {
			ConfiguracaoContabilVO conf = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(mapa.getKey(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioLogado);
			if (Uteis.isAtributoPreenchido(conf)) {
				List<TipoRegraContabilEnum> lista = new ArrayList<>();
				lista.add(TipoRegraContabilEnum.CARTAO_CREDITO);
				getFacadeFactory().getConfiguracaoContabilRegraFacade().consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(conf, lista, usuarioLogado);					
				gerarLancamentoContabilRegrasMapaPendenciaCartaoCredito(mapa, conf, dataBaixa, abaterTaxaExtratoContaCorrente, excluirLancamentoExistente, usuarioLogado);
			}
		}
	}

	private void gerarLancamentoContabilRegrasMapaPendenciaCartaoCredito(Map.Entry<Integer, List<MapaPendenciaCartaoCreditoVO>> mapa, ConfiguracaoContabilVO conf, Date dataBaixa, boolean abaterTaxaExtratoContaCorrente, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(conf.getListaContabilRegrasCartaoCredito())) {
			Collections.sort(conf.getListaContabilRegrasCartaoCredito(), OrdenarConfiguracaoContabilRegraEnum.ATRIBUTO_PREENCHIDO.desc());
			for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartao : mapa.getValue()) {
				if (excluirLancamentoExistente) {
					excluirPorCodOrigemTipoOrigem(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, false, usuarioLogado);
				}
				Double taxa = abaterTaxaExtratoContaCorrente ? mapaPendenciaCartao.getValorTaxaUsar() : 0.0;
				Double valorPagamento = Uteis.arrendondarForcandoCadasDecimais(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela() - taxa, 2);
				for (ConfiguracaoContabilRegraVO regras : conf.getListaContabilRegrasCartaoCredito()) {
					if ((!Uteis.isAtributoPreenchido(regras.getContaCorrenteOrigemVO()) || (regras.getContaCorrenteOrigemVO().getCodigo().equals(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo())))
							&& (!Uteis.isAtributoPreenchido(regras.getFormaPagamentoVO()) || (regras.getFormaPagamentoVO().getCodigo().equals(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo())))
							&& (!Uteis.isAtributoPreenchido(regras.getOperadoraCartaoVO()) || (regras.getOperadoraCartaoVO().getCodigo().equals(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getCodigo())))) {
						for (ConfiguracaoContabilRegraPlanoContaVO ccrp : regras.getListaConfiguracaoContabilRegraPlanoContaVO()) {
							ccrp.setConfiguracaoContabilRegraVO(regras);
							gerarLancamentoContabilPorRegraMapaPendenciaCartaoCredito(ccrp.getPlanoContaCreditoVO(), mapa.getKey(), mapaPendenciaCartao, valorPagamento ,  dataBaixa,  TipoPlanoContaEnum.CREDITO, usuarioLogado);
							gerarLancamentoContabilPorRegraMapaPendenciaCartaoCredito(ccrp.getPlanoContaDebitoVO(),mapa.getKey(), mapaPendenciaCartao, valorPagamento, dataBaixa, TipoPlanoContaEnum.DEBITO, usuarioLogado);
						}						
						break;
					}
				}
				mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setLancamentoContabil(true);
			}
		}
	}
	
	private void gerarLancamentoContabilPorRegraMapaPendenciaCartaoCredito(PlanoContaVO planoConta, Integer unidadeEnsino, MapaPendenciaCartaoCreditoVO mapaPendenciaCartao , Double valorPagamento, Date dataBaixa,  TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception {
		LancamentoContabilVO lc = new LancamentoContabilVO();
		lc.setPlanoContaVO(planoConta);
		lc.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);		
		lc.setCodOrigem(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().toString());
		lc.setTipoPlanoConta(tipoPlanoContaEnum);
		lc.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO);
		lc.setTipoValorLancamentoContabilEnum(TipoValorLancamentoContabilEnum.CARTAO_CREDITO);
		lc.getUnidadeEnsinoVO().setCodigo(unidadeEnsino);
		lc.setContaCorrenteVO(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO());
		lc.setDataRegistro(dataBaixa);
		lc.setDataCompensacao(dataBaixa);
		lc.setValor(valorPagamento);
		if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoParceiro()) {
			lc.setTipoSacado(TipoSacado.PARCEIRO);
			lc.setParceiroVO(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO());
		} else if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoFornecedor()) {
			lc.setTipoSacado(TipoSacado.FORNECEDOR);
			lc.setFornecedorVO(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor());
		}else{
			lc.setPessoaVO(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa());
			if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoFuncionario()) {
				lc.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa().getCodigo(),  0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
				lc.setTipoSacado(TipoSacado.FUNCIONARIO_PROFESSOR);
			}
			if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoAluno()) {
				lc.setTipoSacado(TipoSacado.ALUNO);
			}
			if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoResponsavelFinanceiro()) {
				lc.setTipoSacado(TipoSacado.RESPONSAVEL_FINANCEIRO);
			}
			if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoRequerente()) {
				lc.setTipoSacado(TipoSacado.REQUERENTE);
			}
			if (mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoCandidato()) {
				lc.setTipoSacado(TipoSacado.CANDIDATO);
			}
		}
		persistir(lc, false, usuarioLogado);
		if(lc.getTipoPlanoConta().isCredito()){
			preencherListaLancamentoContabilVO(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getListaLancamentoContabeisCredito(), lc);	
		}else if(lc.getTipoPlanoConta().isDebito()){
			preencherListaLancamentoContabilVO(mapaPendenciaCartao.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getListaLancamentoContabeisDebito(), lc);
		}
	}

}
