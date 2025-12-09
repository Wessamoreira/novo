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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.ItemPrestacaoContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaTurmaVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoPrestacaoContaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.PrestacaoContaInterfaceFacade;

@Service
@Scope
@Lazy
public class PrestacaoConta extends ControleAcesso implements PrestacaoContaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8940390727789432948L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PrestacaoContaVO prestacaoContaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO) && Uteis.isAtributoPreenchido(prestacaoContaVO.getUnidadeEnsino().getCodigo())) {
			prestacaoContaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(prestacaoContaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		validarDados(prestacaoContaVO);
		validarDuplicidadeDataCompetenciaTurma(prestacaoContaVO);
//		validarDadosPrestacaoContaPosteriorCompetencia(prestacaoContaVO);

		if (prestacaoContaVO.getCodigo() == null || prestacaoContaVO.getCodigo() == 0) {
			incluir(prestacaoContaVO, validarAcesso, usuarioVO);
		} else {
			alterar(prestacaoContaVO, validarAcesso, usuarioVO);
		}
	}

	private void validarDadosPrestacaoContaPosteriorCompetencia(PrestacaoContaVO prestacaoContaVO) throws ConsistirException {
		int retorno = consultarPrestacaoContasPosteriorCompetencia(prestacaoContaVO);
		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_ExisteCompetenciaPosterior"));
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PrestacaoContaVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			PrestacaoConta.incluir(getIdEntidade(obj.getTipoPrestacaoConta()), validarAcesso, usuario);
			obj.getResponsavelCadastro().setCodigo(usuario.getCodigo());
			obj.getResponsavelCadastro().setNome(usuario.getNome());

			final String sql = "INSERT INTO PrestacaoConta( descricao, dataCadastro, responsavelCadastro, tipoPrestacaoConta, turma, unidadeEnsino, valorTotalRecebimento, valorTotalPagamento, valorTotalPrestacaoContaTurma, dataCompetencia, saldoAnterior, saldoFinal ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlInserir.setInt(3, obj.getResponsavelCadastro().getCodigo());
					sqlInserir.setString(4, obj.getTipoPrestacaoConta().toString());
					if (obj.getTurma().getCodigo() > 0) {
						sqlInserir.setInt(5, obj.getTurma().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo() > 0) {
						sqlInserir.setInt(6, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setDouble(7, obj.getValorTotalRecebimento());
					sqlInserir.setDouble(8, obj.getValorTotalPagamento());
					sqlInserir.setDouble(9, obj.getValorTotalPrestacaoContaTurma());
					sqlInserir.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getDataCompetencia()));
					sqlInserir.setDouble(11, obj.getSaldoAnterior());
					sqlInserir.setDouble(12, obj.getSaldoFinal());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().incluirItemPrestacaoContaCategoriaDespesaVOs(obj.getItemPrestacaoContaCategoriaDespesaVOs(), obj);
			getFacadeFactory().getItemPrestacaoContaTurmaFacade().incluirItemPrestacaoContaTurmaVOs(obj.getItemPrestacaoContaTurmaVOs(), obj);
			getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().incluirItemPrestacaoContaOrigemContaReceberVOs(obj.getItemPrestacaoContaOrigemContaReceberVOs(), obj);
		} catch (Exception e) {
			e = validarExececao(e, obj);
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	public int consultarTotalPrestacaoContaPorDataCompetencia(PrestacaoContaVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(PrestacaoConta.codigo) as qtde FROM PrestacaoConta ");

		if (obj.getTurma() != null) {
			if (obj.getTurma().getCodigo() != 0) {
				sql.append(" INNER JOIN Turma on turma.codigo = PrestacaoConta.turma ");
				sql.append(" WHERE turma.codigo = ").append(obj.getTurma().getCodigo());
			}
		}
		if (obj.getUnidadeEnsino() != null) {
			if (obj.getUnidadeEnsino().getCodigo() != 0) {
				sql.append(" INNER JOIN UnidadeEnsino on unidadeEnsino.codigo = PrestacaoConta.unidadeEnsino ");
				sql.append(" WHERE unidadeEnsino.codigo = ").append(obj.getUnidadeEnsino().getCodigo());
			}
		}

		sql.append(" AND EXTRACT('Month' FROM PrestacaoConta.dataCompetencia) = ").append(String.valueOf(UteisData.getMesData(obj.getDataCompetencia())));
		sql.append(" AND EXTRACT('year' FROM PrestacaoConta.dataCompetencia) = ").append(UteisData.getAnoDataString(obj.getDataCompetencia()));

		if (obj.getCodigo() != null) {
			sql.append(" AND PrestacaoConta.codigo != ").append(obj.getCodigo());
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		if (rs.next()) {
			return rs.getInt("qtde");
		}

		return 0;
	}

	@Override
	public PrestacaoContaVO consultarSaldoAnteriorPorDataCompetencia(PrestacaoContaVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT saldoAnterior, saldoFinal FROM PrestacaoConta ");

		if (obj.getTurma() != null) {
			if (obj.getTurma().getCodigo() != 0) {
				sql.append(" INNER JOIN Turma on turma.codigo = PrestacaoConta.turma ");
				sql.append(" WHERE turma.codigo = ?");
			}
		}
		if (obj.getUnidadeEnsino() != null) {
			if (obj.getUnidadeEnsino().getCodigo() != 0) {
				sql.append(" INNER JOIN UnidadeEnsino on unidadeEnsino.codigo = PrestacaoConta.unidadeEnsino ");
				sql.append(" WHERE unidadeEnsino.codigo = ?");
			}
		}

		sql.append(" AND EXTRACT('Month' FROM PrestacaoConta.dataCompetencia) = ").append(String.valueOf(UteisData.getMesData(obj.getDataCompetencia()) - 1));

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getUnidadeEnsino().getCodigo());

		if (rs.next()) {
			PrestacaoContaVO prestacaoConta = new PrestacaoContaVO();
			prestacaoConta.setSaldoAnterior(rs.getDouble("saldoanterior"));
			prestacaoConta.setSaldoFinal(rs.getDouble("saldoFinal"));
			return prestacaoConta;
		}

		return null;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PrestacaoContaVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			PrestacaoConta.alterar(getIdEntidade(obj.getTipoPrestacaoConta()), validarAcesso, usuario);
			obj.setDataAlteracao(new Date());
			obj.getResponsavelAlteracao().setCodigo(usuario.getCodigo());
			obj.getResponsavelAlteracao().setNome(usuario.getNome());

			final String sql = "UPDATE PrestacaoConta set descricao=?, dataAlteracao = ?, responsavelAlteracao = ? , valorTotalRecebimento=?, valorTotalPagamento = ?, valorTotalPrestacaoContaTurma=?, dataCompetencia=?, saldoAnterior=?, saldoFinal=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
					sqlAlterar.setInt(3, obj.getResponsavelAlteracao().getCodigo());
					sqlAlterar.setDouble(4, obj.getValorTotalRecebimento());
					sqlAlterar.setDouble(5, obj.getValorTotalPagamento());
					sqlAlterar.setDouble(6, obj.getValorTotalPrestacaoContaTurma());
					sqlAlterar.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataCompetencia()));
					sqlAlterar.setDouble(8, obj.getSaldoAnterior());
					sqlAlterar.setDouble(9, obj.getSaldoFinal());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().alterarItemPrestacaoContaCategoriaDespesaVOs(obj.getItemPrestacaoContaCategoriaDespesaVOs(), obj);
			getFacadeFactory().getItemPrestacaoContaTurmaFacade().alterarItemPrestacaoContaTurmaVOs(obj.getItemPrestacaoContaTurmaVOs(), obj);
			getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().alterarItemPrestacaoContaOrigemContaReceberVOs(obj.getItemPrestacaoContaOrigemContaReceberVOs(), obj);
			if (obj.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA) && !obj.getSaldoAntesAlteracao().equals(obj.getSaldo())) {
				realizarAtualizacaoSaldoPrestacaoContaUnidadeEnsino(obj, obj.getCodigo(), obj.getSaldoAntesAlteracao(), obj.getSaldo());
				obj.setSaldoAntesAlteracao(obj.getSaldo());
			}

		} catch (Exception e) {
			e = validarExececao(e, obj);
			throw e;
		}
	}

	@Override
	public int consultarPrestacaoContasPosteriorCompetencia(PrestacaoContaVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(PrestacaoConta.codigo) as qtde FROM PrestacaoConta ");
		sql.append(" WHERE EXTRACT('Month' FROM PrestacaoConta.datacompetencia) > EXTRACT('Month' FROM TIMESTAMP '").append(Uteis.getDataJDBC(obj.getDataCompetencia())).append("')");
		sql.append(" AND PrestacaoConta.tipoPrestacaoConta = '").append(obj.getTipoPrestacaoConta()).append("'");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		if (rs.next()) {
			return rs.getInt("qtde");
		}

		return 0;
	}

	private Exception validarExececao(Exception e, PrestacaoContaVO obj) throws Exception {
		if (e instanceof DuplicateKeyException) {
			DuplicateKeyException duplicateKeyException = (DuplicateKeyException) e;
			if (duplicateKeyException.getMessage().contains("(prestacaocontaturma)=(")) {
				String msg = duplicateKeyException.getMessage().substring(duplicateKeyException.getMessage().indexOf("(prestacaocontaturma)=(") + 23,
						duplicateKeyException.getMessage().length());
				msg = msg.substring(0, msg.indexOf(")"));
				for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO : obj.getItemPrestacaoContaTurmaVOs()) {
					if (itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getCodigo().toString().equals(msg)) {
						e = new Exception("A PRESTAÇÃO CONTA TURMA da turma " + itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getTurma().getIdentificadorTurma() + " com o valor " + Uteis.getDoubleFormatado(itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getSaldo()) + " está sendo utilizada em outra prestação de conta.");
						break;
					}
				}
			} else if (duplicateKeyException.getMessage().contains("(contareceber)=(")) {
				String msg = duplicateKeyException.getMessage().substring(duplicateKeyException.getMessage().indexOf("(contareceber)=(") + 16,
						duplicateKeyException.getMessage().length());
				msg = msg.substring(0, msg.indexOf(")"));
				for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : obj.getItemPrestacaoContaOrigemContaReceberVOs()) {
					for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs()) {
						if (itemPrestacaoContaReceberVO.getContaReceber().getCodigo().toString().equals(msg)) {
							e = new Exception("A CONTA RECEBER da origem " + itemPrestacaoContaOrigemContaReceberVO.getTipoOrigemContaReceber().getDescricao() + " do(a) " + itemPrestacaoContaReceberVO.getContaReceber().getDadosPessoaAtiva() + " com vencimento em " + itemPrestacaoContaReceberVO.getContaReceber().getDataVencimento_Apresentar() + " e valor " + Uteis.getDoubleFormatado(itemPrestacaoContaReceberVO.getContaReceber().getValorRecebido()) + " está sendo utilizada em outra prestação de conta.");
						}
					}
				}
			} else if (duplicateKeyException.getMessage().contains("(contapagar)=(")) {
				String msg = duplicateKeyException.getMessage().substring(duplicateKeyException.getMessage().indexOf("(contapagar)=(") + 14,
						duplicateKeyException.getMessage().length());
				msg = msg.substring(0, msg.indexOf(")"));
				for (ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO : obj.getItemPrestacaoContaCategoriaDespesaVOs()) {
					for (ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO : itemPrestacaoContaCategoriaDespesaVO.getItemPrestacaoContaPagarVOs()) {
						if (itemPrestacaoContaPagarVO.getContaPagar().getCodigo().toString().equals(msg)) {
							e = new Exception("A CONTA PAGAR da categoria de despesa " + itemPrestacaoContaCategoriaDespesaVO.getCategoriaDespesa().getDescricao() + " do(a) " + itemPrestacaoContaPagarVO.getContaPagar().getFavorecido() + " com vencimento em " + itemPrestacaoContaPagarVO.getContaPagar().getDataVencimento_Apresentar() + " e valor " + Uteis.getDoubleFormatado(itemPrestacaoContaPagarVO.getContaPagar().getValorPago()) + " está sendo utilizada em outra prestação de conta.");
						}
					}
				}
			}

		}
		return e;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoSaldoPrestacaoContaUnidadeEnsino(final PrestacaoContaVO obj, Integer prestacaoContaTurma, Double valorAnterior, Double valorAtual) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE PrestacaoConta set valorTotalPrestacaoContaTurma = valorTotalPrestacaoContaTurma - ");
		sql.append(obj.getValorTotalPrestacaoContaTurma() - (valorAnterior + valorAtual));
		double saldoFinal = 0.0;
		if (obj.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
			saldoFinal = obj.getSaldoAnterior() + obj.getValorTotalRecebimento() - obj.getValorTotalPagamento();
		}

		if (obj.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO)) {
			saldoFinal = obj.getSaldoAnterior() + obj.getValorTotalPrestacaoContaTurma() + obj.getValorTotalRecebimento() - obj.getValorTotalPagamento();
		}
		sql.append(" , saldoFinal = ").append(saldoFinal);
		sql.append(" where codigo in (");
		sql.append(" select prestacaoConta from itemPrestacaoContaTurma where prestacaoContaTurma = ").append(prestacaoContaTurma).append(") ");

		getConexao().getJdbcTemplate().execute(sql.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PrestacaoContaVO prestacaoContaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			PrestacaoConta.excluir(getIdEntidade(prestacaoContaVO.getTipoPrestacaoConta()), validarAcesso, usuarioVO);
			String sql = "DELETE FROM PrestacaoConta WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { prestacaoContaVO.getCodigo() });
			getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().excluirItemPrestacaoContaCategoriaDespesaVOs(new ArrayList<ItemPrestacaoContaCategoriaDespesaVO>(), prestacaoContaVO);
			getFacadeFactory().getItemPrestacaoContaTurmaFacade().excluirItemPrestacaoContaTurmaVOs(new ArrayList<ItemPrestacaoContaTurmaVO>(), prestacaoContaVO);
			getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().excluirItemPrestacaoContaOrigemContaReceberVOs(new ArrayList<ItemPrestacaoContaOrigemContaReceberVO>(), prestacaoContaVO);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void validarDados(PrestacaoContaVO obj) throws ConsistirException {
		if (obj.getDescricao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_descricao"));
		}
		if (obj.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA) && obj.getTurma().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_turma"));
		}
		if (obj.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO) && obj.getUnidadeEnsino().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_unidadeEnsino"));
		}
		if (obj.getDataCompetencia() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_dataCompetencia"));
		}
	}

	private StringBuilder getSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PrestacaoConta.*, turma.identificadorTurma, unidadeEnsino.nome \"unidadeEnsino.nome\", ");
		sql.append(" responsavelCadastro.nome as \"responsavelCadastro.nome\", responsavelAlteracao.nome as \"responsavelAlteracao.nome\"");
		sql.append(" FROM PrestacaoConta");
		sql.append(" LEFT JOIN Turma on turma.codigo = PrestacaoConta.turma ");
		sql.append(" LEFT JOIN UnidadeEnsino on UnidadeEnsino.codigo = PrestacaoConta.unidadeEnsino ");
		sql.append(" LEFT JOIN Usuario as responsavelCadastro on responsavelCadastro.codigo = PrestacaoConta.responsavelCadastro ");
		sql.append(" LEFT JOIN Usuario as responsavelAlteracao on responsavelAlteracao.codigo = PrestacaoConta.responsavelAlteracao ");
		return sql;
	}

	@Override
	public List<PrestacaoContaVO> consultar(String descricao, TipoPrestacaoContaEnum tipoPrestacaoContaEnum, TurmaVO turma, UnidadeEnsinoVO unidadeEnsinoVO, Date dataCadastroInicio, Date dataCadastroTermino, Integer limite, Integer pagina, Boolean validarAcesso, UsuarioVO usuarioVO, Date dataCompetenciaInicio, Date dataCompetenciaTermino) throws Exception {
		PrestacaoConta.consultar(getIdEntidade(tipoPrestacaoContaEnum), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" where 1 = 1");
		if (tipoPrestacaoContaEnum != null) {
			sql.append(" and tipoPrestacaoConta = '").append(tipoPrestacaoContaEnum.toString()).append("' ");
		}
		if (turma != null && turma.getCodigo() > 0) {
			sql.append(" and turma = ").append(turma.getCodigo());
		}
		if (descricao != null && !descricao.trim().isEmpty()) {
			sql.append(" and upper(sem_acentos(descricao)) like upper(sem_acentos('%").append(descricao).append("%'))");
		}
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() > 0) {
			sql.append(" and PrestacaoConta.unidadeEnsino = ").append(unidadeEnsinoVO.getCodigo());
		}
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataCadastroInicio, dataCadastroTermino, "dataCadastro", true));
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataCompetenciaInicio, dataCompetenciaTermino, "dataCompetencia", true));
		sql.append(" order by dataCadastro ");
		if (limite != null && limite > 0) {
			sql.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, NivelMontarDados.BASICO, usuarioVO);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>PaizVO</code> resultantes da consulta.
	 */
	public static List<PrestacaoContaVO> montarDadosConsulta(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<PrestacaoContaVO> vetResultado = new ArrayList<PrestacaoContaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>PaizVO</code> .
	 * 
	 * @return O objeto da classe <code>PaizVO</code> com os dados devidamente montados.
	 */
	public static PrestacaoContaVO montarDados(SqlRowSet dadosSQL, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		PrestacaoContaVO obj = new PrestacaoContaVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setValorTotalPagamento(dadosSQL.getDouble("valorTotalPagamento"));
		obj.setValorTotalRecebimento(dadosSQL.getDouble("valorTotalRecebimento"));
		obj.setValorTotalPrestacaoContaTurma(dadosSQL.getDouble("valorTotalPrestacaoContaTurma"));
		obj.setTipoPrestacaoConta(TipoPrestacaoContaEnum.valueOf(dadosSQL.getString("tipoPrestacaoConta")));
		obj.setDataAlteracao(dadosSQL.getDate("dataAlteracao"));
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.setDataCompetencia(dadosSQL.getDate("dataCompetencia"));
		obj.getResponsavelAlteracao().setCodigo(dadosSQL.getInt("responsavelAlteracao"));
		obj.getResponsavelAlteracao().setNome(dadosSQL.getString("responsavelAlteracao.nome"));
		obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
		obj.getResponsavelCadastro().setNome(dadosSQL.getString("responsavelCadastro.nome"));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.setSaldoAntesAlteracao(obj.getSaldo());
		obj.setSaldoAnterior(dadosSQL.getDouble("saldoAnterior"));
		obj.setSaldoFinal(dadosSQL.getDouble("saldoFinal"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados.equals(NivelMontarDados.BASICO)) {
			return obj;
		}
		if (nivelMontarDados.equals(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS)) {
			obj.setItemPrestacaoContaCategoriaDespesaVOs(getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().consultarItemPrestacaoContaCategoriaDespesaPorPrestacaoConta(obj.getCodigo(), nivelMontarDados, usuarioVO));
			obj.setItemPrestacaoContaTurmaVOs(getFacadeFactory().getItemPrestacaoContaTurmaFacade().consultarItemPrestacaoContaTurmaPorPrestacaoConta(obj.getCodigo(), nivelMontarDados, usuarioVO));
			obj.setItemPrestacaoContaOrigemContaReceberVOs(getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().consultarItemPrestacaoContaOrigemContaReceberPorPrestacaoConta(obj.getCodigo(), nivelMontarDados, usuarioVO));
			return obj;
		}
		return obj;
	}

	@Override
	public Integer consultarTotalRegistro(String descricao, TipoPrestacaoContaEnum tipoPrestacaoContaEnum, TurmaVO turma, UnidadeEnsinoVO unidadeEnsinoVO, Date dataCadastroInicio, Date dataCadastroTermino, Date dataCompetenciaInicio, Date dataCompetenciaTermino) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(codigo) as qtde FROM PrestacaoConta where 1 = 1 ");
		if (tipoPrestacaoContaEnum != null) {
			sql.append(" and tipoPrestacaoConta = '").append(tipoPrestacaoContaEnum.toString()).append("' ");
		}
		if (turma != null && turma.getCodigo() > 0) {
			sql.append(" and turma = ").append(turma.getCodigo());
		}
		if (descricao != null && !descricao.trim().isEmpty()) {
			sql.append(" and upper(sem_acentos(descricao)) like upper(sem_acentos('%").append(descricao).append("%'))");
		}
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() > 0) {
			sql.append(" and unidadeEnsino = ").append(unidadeEnsinoVO.getCodigo());
		}

		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataCadastroInicio, dataCadastroTermino, "dataCadastro", true));
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataCompetenciaInicio, dataCompetenciaTermino, "dataCompetencia", true));
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public PrestacaoContaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" where PrestacaoConta.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuarioVO);
		}
		throw new Exception("Dados não econtratados (Prestação Conta).");
	}

	@Override
	public void adicionarVariasItensPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, PrestacaoContaVO prestacaoContaVO, UsuarioVO usuario) throws Exception {
		itemPrestacaoContaPagarVOs.stream().forEach(p -> preencherItemPrestacaoContaCategoriaDespesa(prestacaoContaVO, p, usuario));
	}

	@Override
	public void preencherItemPrestacaoContaCategoriaDespesa(PrestacaoContaVO prestacaoConta, ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO, UsuarioVO usuario) {
		itemPrestacaoContaPagarVO.getContaPagar().setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(itemPrestacaoContaPagarVO.getContaPagar().getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		itemPrestacaoContaPagarVO.getContaPagar().getListaCentroResultadoOrigemVOs()
				.stream()
				.forEach(p -> {
					ItemPrestacaoContaCategoriaDespesaVO ipccdNovo = consultarItemPrestacaoContaCategoriaDespesaVO(prestacaoConta, p.getCategoriaDespesaVO());
					if(!Uteis.isAtributoPreenchido(ipccdNovo.getCategoriaDespesa())){
						ipccdNovo.setCategoriaDespesa(p.getCategoriaDespesaVO());	
					} 
					ItemPrestacaoContaPagarVO ipcp = getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().consultarItemPrestacaoContaCategoriaDespesaVO(ipccdNovo, itemPrestacaoContaPagarVO.getContaPagar());
					if(!Uteis.isAtributoPreenchido(ipcp.getContaPagar())){
						ipcp.setContaPagar(itemPrestacaoContaPagarVO.getContaPagar());
						getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().adicionarItemPrestacaoContaPagarVO(ipccdNovo, ipcp);
					}					
					atualizarItemPrestacaoContaCategoriaDespesaVO(prestacaoConta, ipccdNovo);
				});
	}
	
	@Override
	public void adicionarItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO, CategoriaDespesaVO categoriaDespesa, Double valor, boolean valorInformadoManual) {
		ItemPrestacaoContaCategoriaDespesaVO ipccdNovo = consultarItemPrestacaoContaCategoriaDespesaVO(prestacaoContaVO, categoriaDespesa);
		if(!Uteis.isAtributoPreenchido(ipccdNovo.getCategoriaDespesa())){
			ipccdNovo.setCategoriaDespesa(categoriaDespesa);
		}
		ipccdNovo.setValorInformadoManual(valorInformadoManual);
		ipccdNovo.setValor(valor);
		atualizarItemPrestacaoContaCategoriaDespesaVO(prestacaoContaVO, ipccdNovo);
	}
	
	

	private ItemPrestacaoContaCategoriaDespesaVO consultarItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoConta, CategoriaDespesaVO categoriaDespesaVO) {
		for (ItemPrestacaoContaCategoriaDespesaVO objExistente : prestacaoConta.getItemPrestacaoContaCategoriaDespesaVOs()) {
			if (objExistente.getCategoriaDespesa().getCodigo().equals(categoriaDespesaVO.getCodigo())) {
				return objExistente;
			}
		}
		return new ItemPrestacaoContaCategoriaDespesaVO();
	}

	private void atualizarItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) {
		int index = 0;
		itemPrestacaoContaCategoriaDespesaVO.calcularValorTotalItemPrestacaoContaPagar();
		for (ItemPrestacaoContaCategoriaDespesaVO objExistente : prestacaoContaVO.getItemPrestacaoContaCategoriaDespesaVOs()) {
			if (objExistente.equalsCampoSelecaoLista(itemPrestacaoContaCategoriaDespesaVO)) {
				prestacaoContaVO.getItemPrestacaoContaCategoriaDespesaVOs().set(index, itemPrestacaoContaCategoriaDespesaVO);
				prestacaoContaVO.calcularValorTotalItemPrestacaoContaCategoriaDespesaVO();
				return;
			}
			index++;
		}
		prestacaoContaVO.getItemPrestacaoContaCategoriaDespesaVOs().add(itemPrestacaoContaCategoriaDespesaVO);
		prestacaoContaVO.calcularValorTotalItemPrestacaoContaCategoriaDespesaVO();
	}

	@Override
	public void removerItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) {
		Iterator<ItemPrestacaoContaCategoriaDespesaVO> i = prestacaoContaVO.getItemPrestacaoContaCategoriaDespesaVOs().iterator();
		while (i.hasNext()) {
			ItemPrestacaoContaCategoriaDespesaVO objExistente =  i.next();
			if (objExistente.getCategoriaDespesa().getCodigo().equals(itemPrestacaoContaCategoriaDespesaVO.getCategoriaDespesa().getCodigo())) {
				i.remove();
				prestacaoContaVO.calcularValorTotalItemPrestacaoContaCategoriaDespesaVO();
				return;
			}
		}
	}

	@Override
	public void adicionarItemPrestacaoContaOrigemContaReceberVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) {
		for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO2 : prestacaoContaVO.getItemPrestacaoContaOrigemContaReceberVOs()) {
			if (itemPrestacaoContaOrigemContaReceberVO2.getTipoOrigemContaReceber().equals(
					itemPrestacaoContaOrigemContaReceberVO.getTipoOrigemContaReceber())) {
				prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() - itemPrestacaoContaOrigemContaReceberVO2.getValor() + itemPrestacaoContaOrigemContaReceberVO2.getValorManual());
				if (itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs().isEmpty()) {
					itemPrestacaoContaOrigemContaReceberVO2.setValor(itemPrestacaoContaOrigemContaReceberVO.getValor());
					itemPrestacaoContaOrigemContaReceberVO2.setValorManual(itemPrestacaoContaOrigemContaReceberVO.getValorManual());
				} else {
					for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs()) {
						getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().adicionarItemPrestacaoContaReceberVO(itemPrestacaoContaOrigemContaReceberVO2, itemPrestacaoContaReceberVO);
					}
				}

				prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() + itemPrestacaoContaOrigemContaReceberVO2.getValor() + itemPrestacaoContaOrigemContaReceberVO2.getValorManual());										

				return;
			}
		}
		prestacaoContaVO.getItemPrestacaoContaOrigemContaReceberVOs().add(itemPrestacaoContaOrigemContaReceberVO);
		prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() + itemPrestacaoContaOrigemContaReceberVO.getValor() + itemPrestacaoContaOrigemContaReceberVO.getValorManual());
	}

	@Override
	public void removerItemPrestacaoContaOrigemContaReceberVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) {
		prestacaoContaVO.getItemPrestacaoContaOrigemContaReceberVOs().remove(itemPrestacaoContaOrigemContaReceberVO);
		prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() - (itemPrestacaoContaOrigemContaReceberVO.getValor() + itemPrestacaoContaOrigemContaReceberVO.getValorManual()));

	}

	@Override
	public void adicionarItemPrestacaoContaTurmaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO) {
		for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO2 : prestacaoContaVO.getItemPrestacaoContaTurmaVOs()) {
			if (itemPrestacaoContaTurmaVO2.getPrestacaoContaTurma().getCodigo().intValue() == itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getCodigo().intValue()) {
				return;
			}
		}
		prestacaoContaVO.getItemPrestacaoContaTurmaVOs().add(itemPrestacaoContaTurmaVO);
		prestacaoContaVO.setValorTotalPrestacaoContaTurma(prestacaoContaVO.getValorTotalPrestacaoContaTurma() + itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getSaldoFinal());
	}

	@Override
	public void removerItemPrestacaoContaTurmaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO) {
		prestacaoContaVO.getItemPrestacaoContaTurmaVOs().remove(itemPrestacaoContaTurmaVO);
		prestacaoContaVO.setValorTotalPrestacaoContaTurma(prestacaoContaVO.getValorTotalPrestacaoContaTurma() - itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getSaldoFinal());

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade(TipoPrestacaoContaEnum tipoPrestacaoContaEnum) {
		if (tipoPrestacaoContaEnum.equals(TipoPrestacaoContaEnum.TURMA)) {
			return "PrestacaoContaTurma";
		}
		return "PrestacaoContaUnidadeEnsino";
	}

	@Override
	public List<ItemPrestacaoContaTurmaVO> consultarPrestacaoContaTurmaDisponivelPrestacaoConta(String identificadorTurma, Date dataInicio, Date dataFim, PrestacaoContaVO prestacaoContaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectSqlBasico());
		sql.append(" where PrestacaoConta.tipoPrestacaoConta = '").append(TipoPrestacaoContaEnum.TURMA).append("' ");
		sql.append(" and PrestacaoConta.codigo not in (SELECT prestacaoContaTurma from ItemPrestacaoContaTurma )");
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, Uteis.getDateHoraFinalDia(dataFim), "dataCompetencia", true));
		if (identificadorTurma != null && !identificadorTurma.trim().isEmpty()) {
			sql.append(" and upper(Turma.identificadorTurma) like(upper('%").append(identificadorTurma.trim()).append("%'))");
		}
		int x = 0;
		for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO : prestacaoContaVO.getItemPrestacaoContaTurmaVOs()) {
			if (x == 0) {
				sql.append(" and PrestacaoConta.codigo not in (").append(itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getCodigo());
				x++;
			} else {
				sql.append(", ").append(itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getCodigo());
			}
		}
		if (x > 0) {
			sql.append(") ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaItemPrestacaoContaTurma(rs, NivelMontarDados.BASICO, usuarioVO);
	}

	public List<ItemPrestacaoContaTurmaVO> montarDadosConsultaItemPrestacaoContaTurma(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ItemPrestacaoContaTurmaVO> vetResultado = new ArrayList<ItemPrestacaoContaTurmaVO>(0);
		ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO = null;
		while (tabelaResultado.next()) {
			itemPrestacaoContaTurmaVO = new ItemPrestacaoContaTurmaVO();
			itemPrestacaoContaTurmaVO.setPrestacaoContaTurma(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
			vetResultado.add(itemPrestacaoContaTurmaVO);
		}
		return vetResultado;
	}

	/**
	 * Metodo responsavel por verificar se existe uma prestação de conta para a turma e data da competência informada.
	 * 
	 * @param obj
	 *            <code>PrestacaoContaVO</code>
	 * @throws ConsistirException
	 */
	private void validarDuplicidadeDataCompetenciaTurma(PrestacaoContaVO obj) throws ConsistirException {
		int quantidadePrestacaoContas = consultarTotalPrestacaoContaPorDataCompetencia(obj);

		if (quantidadePrestacaoContas > 0) {
			if (obj.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_Turma_Duplicada"));
			}

			throw new ConsistirException(UteisJSF.internacionalizar("msg_PrestacaoConta_UnidadeEnsino_Duplicada"));
		}
	}

	@Override
	public List<PrestacaoContaVO> consultarDadosGeracaoPrestacaoContaPorTurma(String favorecido, String tipoFiltroData, Date dataInicio, Date dataTermino, String tipoOrigem, PrestacaoContaVO prestacaoContaVO) throws Exception {

		StringBuilder sql = new StringBuilder("SELECT distinct ContaReceber.turma, turma.codigo, turma.identificadorTurma ");
		sql.append(" from ContaReceber");
		sql.append(" left join ItemPrestacaoContaReceber on ContaReceber.codigo = ItemPrestacaoContaReceber.contaReceber ");
		sql.append(" left join funcionario on funcionario.codigo = contaReceber.funcionario ");
		sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sql.append(" left join pessoa on pessoa.codigo = contaReceber.pessoa ");
		sql.append(" left join parceiro on parceiro.codigo = contaReceber.parceiro ");
		sql.append(" left join Matricula on Matricula.matricula = contaReceber.matriculaAluno ");
		sql.append(" left join Pessoa as aluno on Matricula.aluno = aluno.codigo ");
		sql.append(" left join Pessoa as candidato on candidato.codigo = contaReceber.candidato ");
		sql.append(" LEFT JOIN Pessoa as responsavelFinanceiro ON contaReceber.responsavelFinanceiro = responsavelFinanceiro.codigo ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = ( case when contareceber.matriculaperiodo is not null then (contareceber.matriculaperiodo) else ((select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula limit 1) ) end) ");
		sql.append(" inner join turma on turma.codigo =  matriculaPeriodo.turma ");
		if (tipoFiltroData != null && tipoFiltroData.equals("DATA_RECEBIMENTO")) {
			sql.append(" inner join ContaReceberNegociacaoRecebimento on ContaReceberNegociacaoRecebimento.contaReceber = contaReceber.codigo");
			sql.append(" inner join negociacaoRecebimento on negociacaoRecebimento.codigo = ContaReceberNegociacaoRecebimento.negociacaoRecebimento");
		}
		sql.append(" WHERE contaReceber.unidadeEnsino = ").append(prestacaoContaVO.getUnidadeEnsino().getCodigo());
		if (tipoFiltroData != null && tipoFiltroData.equals("DATA_VENCIMENTO")) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", false));
		}
		if (tipoFiltroData != null && tipoFiltroData.equals("DATA_RECEBIMENTO")) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaoRecebimento.data", false));
		}
		if (tipoOrigem != null && !tipoOrigem.trim().isEmpty() && !tipoOrigem.equals("TO")) {
			sql.append(" and contaReceber.tipoOrigem = '").append(tipoOrigem.toUpperCase()).append("' ");
		}
		sql.append(" and contaReceber.situacao = 'RE' ");
		sql.append(" and not exists( select PrestacaoConta.turma from PrestacaoConta where PrestacaoConta.turma = turma.codigo  ");
		sql.append(" and to_char(PrestacaoConta.dataCompetencia, 'MM/yyyy') = '").append(Uteis.getDataMesAnoConcatenado(prestacaoContaVO.getDataCompetencia())).append("')");
		int x = 0;
		for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : prestacaoContaVO.getItemPrestacaoContaOrigemContaReceberVOs()) {
			for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs()) {
				if (x == 0) {
					sql.append(" and contaReceber.codigo not in (").append(itemPrestacaoContaReceberVO.getContaReceber().getCodigo());
					x++;
				} else {
					sql.append(", ").append(itemPrestacaoContaReceberVO.getContaReceber().getCodigo());
				}
			}
		}
		if (x > 0) {
			sql.append(") ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<PrestacaoContaVO> prestacaoContaVOs = new ArrayList<PrestacaoContaVO>(0);
		while (rs.next()) {
			PrestacaoContaVO prestacaoConta = montarDadosGeracaoPrestacaoContaPorTurma(rs, prestacaoContaVO);
			prestacaoContaVOs.add(prestacaoConta);
		}

		return prestacaoContaVOs;
	}

	public static PrestacaoContaVO montarDadosGeracaoPrestacaoContaPorTurma(SqlRowSet dadosSQL, PrestacaoContaVO prestacaoContaVO) throws Exception {
		PrestacaoContaVO obj = new PrestacaoContaVO();
		int codigo = dadosSQL.getInt("codigo");
		obj.setDataCompetencia(prestacaoContaVO.getDataCompetencia());
		obj.setDataCadastro(prestacaoContaVO.getDataCadastro());
		obj.setDescricao(String.valueOf(codigo).concat("-").concat(UteisData.getAnoMes(obj.getDataCompetencia())));
		obj.setResponsavelCadastro(prestacaoContaVO.getResponsavelCadastro());
		obj.setResponsavelAlteracao(prestacaoContaVO.getResponsavelAlteracao());
		obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
		obj.setTipoPrestacaoConta(TipoPrestacaoContaEnum.TURMA);

		return obj;
	}

	@Override
	public void persistirPrestacoesContas(List<PrestacaoContaVO> listaPrestacaoConta, PrestacaoContaVO prestacaoConta) throws Exception {
		try {
			if (!listaPrestacaoConta.isEmpty()) {
				prestacaoConta.setTipoPrestacaoConta(TipoPrestacaoContaEnum.TURMA);
				prestacaoConta.setTurma(listaPrestacaoConta.get(0).getTurma());

				for (PrestacaoContaVO p : listaPrestacaoConta) {
					p.setItemPrestacaoContaCategoriaDespesaVOs(prestacaoConta.getItemPrestacaoContaCategoriaDespesaVOs());
					p.setItemPrestacaoContaOrigemContaReceberVOs(prestacaoConta.getItemPrestacaoContaOrigemContaReceberVOs());
					p.setItemPrestacaoContaTurmaVOs(prestacaoConta.getItemPrestacaoContaTurmaVOs());
					p.setValorTotalRecebimento(p.getItemPrestacaoContaOrigemContaReceberVOs().stream().map(itemPrestacaoContaOrigemReceber -> itemPrestacaoContaOrigemReceber.getValor()).mapToDouble(Double::doubleValue).sum());
					p.setSaldoFinal(p.getSaldoAnterior() + p.getValorTotalRecebimento() - p.getValorTotalPagamento());

					this.persistir(p, Boolean.FALSE, p.getResponsavelCadastro());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Boolean permiteAlterarSaldoAnteriorTurma(UsuarioVO usuario) {
		try {
			this.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("SaldoAnteriorTurma", usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Boolean permiteAlterarSaldoAnteriorUnidadeEnsino(UsuarioVO usuario) {
		try {
			this.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("SaldoAnteriorUnidadeEnsino", usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Boolean permiteIncluirSaldoReceberTurma(UsuarioVO usuario) {
		try {
			this.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("IncluirSaldoReceberTurma", usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Boolean permiteIncluirSaldoReceberUnidadeEnsino(UsuarioVO usuario) {
		try {
			this.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("IncluirSaldoReceberUnidadeEnsino", usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public Boolean permitirAdicionarNovoItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO , UsuarioVO usuario){
		try {
			if (Uteis.isAtributoPreenchido(prestacaoContaVO) && consultarPrestacaoContasPosteriorCompetencia(prestacaoContaVO) > 0 && !validarPermissaoAdicionarNovoItemPrestacao(prestacaoContaVO,usuario)) {
				return false;
			}
			return true;
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	private Boolean validarPermissaoAdicionarNovoItemPrestacao(PrestacaoContaVO prestacaoContaVO , UsuarioVO usuario) throws Exception {
		if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
			return verificarPermissaoFuncionalidadeUsuario("IncluirSaldoReceberTurma", usuario);
		} else {
			return verificarPermissaoFuncionalidadeUsuario("IncluirSaldoReceberUnidadeEnsino", usuario);
		}
	}

}
