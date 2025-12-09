package negocio.facade.jdbc.financeiro;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.enumerador.NivelAgrupamentoContaReceberEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberAgrupadaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class ContaReceberAgrupada extends ControleAcesso implements ContaReceberAgrupadaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9218893740146643324L;
	private static String idEntidade = "ContaReceberAgrupada";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ContaReceberAgrupadaVO contaReceberAgrupada, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (contaReceberAgrupada.isNovoObj()) {
			incluir(contaReceberAgrupada, validarAcesso, usuarioVO);
		} else {
			alterar(contaReceberAgrupada, validarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ContaReceberAgrupadaVO contaReceberAgrupada, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(contaReceberAgrupada);
		ContaReceberAgrupada.incluir(idEntidade, validarAcesso, usuarioVO);
		final StringBuilder sql = new StringBuilder("INSERT INTO ContaReceberAgrupada ");
		sql.append(" (dataAgrupamento, responsavelAgrupamento, contaCorrente,  unidadeEnsino , situacao, tipoPessoa, matricula, pessoa, ");
		sql.append(" descricao, dataVencimento, valorTotal ) VALUES (?,?,?,?,?,?,?,?,?,?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		contaReceberAgrupada.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(contaReceberAgrupada.getDataAgrupamento()));
				ps.setInt(x++, contaReceberAgrupada.getResponsavelAgrupamento().getCodigo());
				ps.setInt(x++, contaReceberAgrupada.getContaCorrente().getCodigo());
				ps.setInt(x++, contaReceberAgrupada.getUnidadeEnsino().getCodigo());
				ps.setString(x++, contaReceberAgrupada.getSituacao().name());
				ps.setString(x++, contaReceberAgrupada.getTipoPessoa().name());
				if (contaReceberAgrupada.getTipoPessoa().equals(TipoPessoa.ALUNO)) {
					ps.setString(x++, contaReceberAgrupada.getMatricula().getMatricula());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, contaReceberAgrupada.getPessoa().getCodigo());
				ps.setString(x++, contaReceberAgrupada.getDescricao());
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(contaReceberAgrupada.getDataVencimento()));
				ps.setDouble(x++, contaReceberAgrupada.getValorTotal());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		realizarVinculoContaReceberComContaReceberAgrupada(contaReceberAgrupada, usuarioVO);
		realizarCriacaoNossoNumero(contaReceberAgrupada, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarVinculoContaReceberComContaReceberAgrupada(final ContaReceberAgrupadaVO contaReceberAgrupada, UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql = new StringBuilder("UPDATE ContaReceber SET ");
		sql.append(" ContaReceberAgrupada = null ");
		sql.append(" WHERE ContaReceberAgrupada = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setInt(1, contaReceberAgrupada.getCodigo());
				return ps;
			}
		});
		for (final ContaReceberVO contaReceberVO : contaReceberAgrupada.getContaReceberVOs()) {
			final StringBuilder sql2 = new StringBuilder("UPDATE ContaReceber SET ");
			sql2.append(" ContaReceberAgrupada = ? ");
			sql2.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql2.toString());
					ps.setInt(1, contaReceberAgrupada.getCodigo());
					ps.setInt(2, contaReceberVO.getCodigo());
					return ps;
				}
			});
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoNossoNumero(final ContaReceberAgrupadaVO contaReceberAgrupada, UsuarioVO usuarioVO) throws Exception {

		String numeroPreenchidoPosicoesVagas = getFacadeFactory().getContaReceberFacade().executarVerificacaoQuantidadeDigitosNossNumeroPorBanco(contaReceberAgrupada.getCodigo().toString(), contaReceberAgrupada.getContaCorrente().getAgencia().getBanco().getNrBanco(), "OUT", "3", "01");
		if (!numeroPreenchidoPosicoesVagas.equals("")) {
			contaReceberAgrupada.setNossoNumero("3" + numeroPreenchidoPosicoesVagas);
		} else {
			contaReceberAgrupada.setNossoNumero("3" + Uteis.preencherComZerosPosicoesVagas(contaReceberAgrupada.getCodigo().toString(), 9));
		}
		final StringBuilder sql = new StringBuilder("UPDATE ContaReceberAgrupada SET ");
		sql.append(" nossoNumero = ? ");
		sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, contaReceberAgrupada.getNossoNumero());
				ps.setInt(2, contaReceberAgrupada.getCodigo());
				return ps;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ContaReceberAgrupadaVO contaReceberAgrupada, Boolean validarAcesso, final UsuarioVO usuarioVO) throws Exception {
		ContaReceberAgrupada.excluir(idEntidade, validarAcesso, usuarioVO);
		if (contaReceberAgrupada.getSituacao().equals(SituacaoContaReceber.A_RECEBER)) {
			contaReceberAgrupada.getContaReceberVOs().clear();
			realizarVinculoContaReceberComContaReceberAgrupada(contaReceberAgrupada, usuarioVO);
			final StringBuilder sql = new StringBuilder("DELETE FROM ContaReceberAgrupada ");
			sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					ps.setInt(1, contaReceberAgrupada.getCodigo());
					return ps;
				}
			});
		} else {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_excluirRecebido"));
		}
	}

	@Override
	public void removerContaReceber(ContaReceberAgrupadaVO contaReceberAgrupada, ContaReceberVO contaReceberVO) throws Exception {
		if (contaReceberAgrupada.getSituacao().equals(SituacaoContaReceber.A_RECEBER)) {
			int index = 0;
			for (ContaReceberVO contaReceberVO2 : contaReceberAgrupada.getContaReceberVOs()) {
				if (contaReceberVO2.getCodigo().equals(contaReceberVO.getCodigo())) {
					contaReceberAgrupada.setValorTotal(contaReceberAgrupada.getValorTotal() - contaReceberVO2.getValor());
					contaReceberAgrupada.getContaReceberVOs().remove(index);
					return;
				}
				index++;
			}
		} else {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_contaJaRecebido"));
		}
	}

	@Override
	public void adicionarContaReceber(ContaReceberAgrupadaVO contaReceberAgrupada, ContaReceberVO contaReceberVO) throws Exception {
		if (contaReceberAgrupada.getSituacao().equals(SituacaoContaReceber.A_RECEBER)) {
			for (ContaReceberVO contaReceberVO2 : contaReceberAgrupada.getContaReceberVOs()) {
				if (contaReceberVO2.getCodigo().equals(contaReceberVO.getCodigo())) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_contaJaAdicionada"));
				}
			}
			contaReceberAgrupada.setValorTotal(contaReceberAgrupada.getValorTotal() + contaReceberVO.getValor());
			contaReceberAgrupada.getContaReceberVOs().add(contaReceberVO);
		} else {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_contaJaRecebido"));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ContaReceberAgrupadaVO contaReceberAgrupada, Boolean validarAcesso, final UsuarioVO usuarioVO) throws Exception {
		validarDados(contaReceberAgrupada);
		ContaReceberAgrupada.alterar(idEntidade, validarAcesso, usuarioVO);
		final StringBuilder sql = new StringBuilder("UPDATE ContaReceberAgrupada SET ");
		sql.append(" contaCorrente = ?, situacao = ?, descricao = ?, nossoNumero = ?, ");
		sql.append(" valorTotalRecebido = ?, valorTotalJuro = ?, valorTotalMulta = ?, ");
		sql.append(" valorTotalDesconto = ?, dataRecebimento = ?, dataAlteracao = ?, responsavelAlteracao = ?, valorTotal = ? ");
		sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, contaReceberAgrupada.getContaCorrente().getCodigo());
				ps.setString(x++, contaReceberAgrupada.getSituacao().name());
				ps.setString(x++, contaReceberAgrupada.getDescricao());
				ps.setString(x++, contaReceberAgrupada.getNossoNumero());
				ps.setDouble(x++, contaReceberAgrupada.getValorTotalRecebido());
				ps.setDouble(x++, contaReceberAgrupada.getValorTotalJuro());
				ps.setDouble(x++, contaReceberAgrupada.getValorTotalMulta());
				ps.setDouble(x++, contaReceberAgrupada.getValorTotalDesconto());
				if (contaReceberAgrupada.getDataRecebimento() == null) {
					ps.setNull(x++, 0);
				} else {
					ps.setDate(x++, Uteis.getDataJDBC(contaReceberAgrupada.getDataRecebimento()));
				}
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
				ps.setInt(x++, usuarioVO.getCodigo());
				ps.setDouble(x++, contaReceberAgrupada.getValorTotal());
				ps.setInt(x++, contaReceberAgrupada.getCodigo());
				return ps;
			}
		});
		realizarVinculoContaReceberComContaReceberAgrupada(contaReceberAgrupada, usuarioVO);
		realizarCriacaoNossoNumero(contaReceberAgrupada, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoContaAgrupada(final NegociacaoRecebimentoVO negociacaoRecebimentoVO, final String situacao, final UsuarioVO usuarioVO) throws Exception {
		if (!negociacaoRecebimentoVO.getRecebimentoBoletoAutomatico()) {
			if (!negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().isEmpty()) {
				List<ContaReceberNegociacaoRecebimentoVO> lista = negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs();
				Iterator<ContaReceberNegociacaoRecebimentoVO> i = lista.iterator();
				while (i.hasNext()) {				
					ContaReceberNegociacaoRecebimentoVO conta = (ContaReceberNegociacaoRecebimentoVO) i.next();				
					if (conta.getContaReceber().getContaReceberAgrupada().intValue() > 0) {
						this.alterarSituacaoContaAgrupada(negociacaoRecebimentoVO, conta.getContaReceber().getContaReceberAgrupada(), situacao, usuarioVO);
					}
				}
			}
		}
	}

	public void alterarSituacaoContaAgrupada(final NegociacaoRecebimentoVO negociacaoRecebimentoVO, final Integer codigoContaReceberAgrupada, final String situacao, final UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql = new StringBuilder("UPDATE ContaReceberAgrupada SET ");
		sql.append(" situacao = ?, valorTotalRecebido = ?, ");
		// sql.append(" valorTotalJuro = ?, valorTotalMulta = ?, valorTotalDesconto = ?,");
		sql.append(" dataRecebimento = ?, dataAlteracao = ?, responsavelAlteracao = ?, valorTotal = ? ");
		sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, situacao);
				ps.setDouble(x++, negociacaoRecebimentoVO.getValorTotalRecebimento());
				if (negociacaoRecebimentoVO.getData() == null) {
					ps.setNull(x++, 0);
				} else {
					ps.setDate(x++, Uteis.getDataJDBC(negociacaoRecebimentoVO.getData()));
				}
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
				ps.setInt(x++, usuarioVO.getCodigo());
				ps.setDouble(x++, negociacaoRecebimentoVO.getValorTotal());
				// ps.setDouble(x++,
				// negociacaoRecebimentoVO.getValorTotalJuro());
				// ps.setDouble(x++,
				// negociacaoRecebimentoVO.getValorTotalMulta());
				// ps.setDouble(x++,
				// negociacaoRecebimentoVO.getValorTotalDesconto());
				ps.setInt(x++, codigoContaReceberAgrupada);
				return ps;
			}
		});
	}

	public void alterarSituacaoContaAgrupadaArquivoRetorno(final Double valorTotalRecebido, final Double valortotal, final Date dataRecebimento, final Date dataAlteracao, final Integer codigoContaReceberAgrupada, final String situacao, final UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql = new StringBuilder("UPDATE ContaReceberAgrupada SET ");
		sql.append(" situacao = ?, valorTotalRecebido = ?, ");
		sql.append(" dataRecebimento = ?, dataAlteracao = ?, valorTotal = ? ");
		sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, situacao);
				ps.setDouble(x++, valorTotalRecebido);
				if (dataRecebimento == null) {
					ps.setNull(x++, 0);
				} else {
					ps.setDate(x++, Uteis.getDataJDBC(dataRecebimento));
				}
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(dataAlteracao));
				ps.setDouble(x++, valortotal);
				ps.setInt(x++, codigoContaReceberAgrupada);
				return ps;
			}
		});
	}
	
	private void validarDados(ContaReceberAgrupadaVO obj) throws ConsistirException {
		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_unidadeEnsino"));
		}
		if (obj.getTipoPessoa().equals(TipoPessoa.ALUNO) && (obj.getMatricula().getMatricula().trim().isEmpty() || obj.getPessoa().getCodigo() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_matricula"));
		}
		if (obj.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO) && obj.getPessoa().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_responsavelFinanceiro"));
		}

		if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_contaCorrente"));
		}
		if (obj.getContaReceberVOs().size() <= 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_contaReceber"));
		}

	}

	private void validarDadosProcessamento(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, MatriculaVO matriculaVO, TurmaVO turmaVO, PessoaVO responsavelFinanceiro, ContaCorrenteVO contaCorrenteVO, Integer ano, MesAnoEnum mes) throws ConsistirException {
		if (unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo() == null || unidadeEnsinoVO.getCodigo().intValue() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_unidadeEnsino"));
		}

		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.CURSO) && (unidadeEnsinoCursoVO == null || unidadeEnsinoCursoVO.getCodigo() == null || unidadeEnsinoCursoVO.getCodigo().intValue() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_curso"));
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.TURMA) && (turmaVO == null || turmaVO.getCodigo() == null || turmaVO.getCodigo().intValue() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_turma"));
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.RESPONSAVEL_FINANCEIRO) && (responsavelFinanceiro == null || responsavelFinanceiro.getCodigo() == null || responsavelFinanceiro.getCodigo().intValue() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_responsavelFinanceiro"));
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.ALUNO) && (matriculaVO == null || matriculaVO.getMatricula() == null || matriculaVO.getMatricula().trim().isEmpty())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_matricula"));
		}

		if (contaCorrenteVO == null || contaCorrenteVO.getCodigo() == null || contaCorrenteVO.getCodigo().intValue() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_contaCorrente"));
		}
		if (ano == null || ano.toString().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_ano"));
		}
		if (ano.toString().length() != 4) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_anoQuatroDigito"));
		}
		if (ano < Integer.valueOf(Uteis.getAnoDataAtual4Digitos())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_anoAnterior"));
		}
		if (Integer.valueOf(mes.getKey()) < Integer.valueOf(Uteis.getMesDataAtual()) && (ano < Integer.valueOf(Uteis.getAnoDataAtual4Digitos()))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaReceberAgrupada_mesAnterior"));
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<ContaReceberAgrupadaVO> realizarAgrupamentoContaReceber(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, MatriculaVO matriculaVO, TurmaVO turmaVO, PessoaVO responsavelFinanceiro, Boolean agruparBiblioteca, Boolean agruparRequerimento, Boolean agruparContratoReceita, Boolean agruparContasResponsavelFinanceiro, ContaCorrenteVO contaCorrenteVO, Integer ano, MesAnoEnum mes, UsuarioVO usuarioVO) throws Exception {
		validarDadosProcessamento(nivelAgrupamentoContaReceber, unidadeEnsinoVO, unidadeEnsinoCursoVO, matriculaVO, turmaVO, responsavelFinanceiro, contaCorrenteVO, ano, mes);
		Map<TipoPessoa, Map<Object, List<ContaReceberVO>>> contaPorTipoPessoa = consultarDadosParaAgrupamento(nivelAgrupamentoContaReceber, unidadeEnsinoVO, unidadeEnsinoCursoVO, matriculaVO, turmaVO, responsavelFinanceiro, ano, mes);
		List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs = realizarProcessamentoContaReceber(contaPorTipoPessoa, agruparBiblioteca, agruparRequerimento, agruparContratoReceita, contaCorrenteVO, usuarioVO);
		for (ContaReceberAgrupadaVO contaReceberAgrupadaVO : contaReceberAgrupadaVOs) {
			persistir(contaReceberAgrupadaVO, true, usuarioVO);
		}
		return contaReceberAgrupadaVOs;
	}

	/**
	 * Realizar o processamento das contas consultadas verificando as regras de
	 * agrupamento
	 * 
	 * @param contaPorTipoPessoa
	 *            = Lista com as contas separadas por tipo de pessoa (Aluno,
	 *            Responsavel Financeiro)
	 * @param agruparBiblioteca
	 *            = Se true irá agrupar as contas de biblioteca com as contas de
	 *            menor data de vencimento dando preferencia para as Matricula,
	 *            Mensalidades ou Negociacao
	 * @param agruparRequerimento
	 *            = Se true irá agrupar as contas de requerimento com as contas
	 *            de menor data de vencimento dando preferencia para as
	 *            Matricula, Mensalidades ou Negociacao
	 * @param agruparContratoReceita
	 *            = Se true irá agrupar as contas de contrato de receita com as
	 *            contas de menor data de vencimento dando preferencia para as
	 *            Matricula, Mensalidades ou Negociacao *
	 * @param contaCorrenteVO
	 *            = Conta Corrente Padrão para a criação da conta agrupada
	 * @return
	 * @throws Exception
	 */
	private List<ContaReceberAgrupadaVO> realizarProcessamentoContaReceber(Map<TipoPessoa, Map<Object, List<ContaReceberVO>>> contaPorTipoPessoa, Boolean agruparBiblioteca, Boolean agruparRequerimento, Boolean agruparContratoReceita, ContaCorrenteVO contaCorrenteVO, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs = new ArrayList<ContaReceberAgrupadaVO>(0);
		for (TipoPessoa tipoPessoa : contaPorTipoPessoa.keySet()) {
			Map<Object, List<ContaReceberVO>> contas = contaPorTipoPessoa.get(tipoPessoa);
			contaReceberAgrupadaVOs.addAll(realizarProcessamentoContaReceberPorTipoPessoa(tipoPessoa, contas, agruparBiblioteca, agruparRequerimento, agruparContratoReceita, contaCorrenteVO, usuarioVO));
		}
		return contaReceberAgrupadaVOs;
	}

	private List<ContaReceberAgrupadaVO> realizarProcessamentoContaReceberPorTipoPessoa(TipoPessoa tipoPessoa, Map<Object, List<ContaReceberVO>> contasPessoa, Boolean agruparBiblioteca, Boolean agruparRequerimento, Boolean agruparContratoReceita, ContaCorrenteVO contaCorrenteVO, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs = new ArrayList<ContaReceberAgrupadaVO>(0);
		for (Object pessoa : contasPessoa.keySet()) {
			List<ContaReceberVO> contaReceberVOs = contasPessoa.get(pessoa);
			Map<TipoOrigemContaReceber, List<ContaReceberVO>> contaReceberPorOrigem = realizarSeparacaoContaReceberPorOrigem(contaReceberVOs);
			Map<String, List<ContaReceberVO>> contaReceberPorData = realizarAgrupamentoContaReceberPorDataVencimento(contaReceberPorOrigem, agruparBiblioteca, agruparRequerimento, agruparContratoReceita);
			contaReceberAgrupadaVOs.addAll(realizarCriacaoContaReceberAgrupada(contaReceberPorData, contaCorrenteVO, usuarioVO));
		}

		return contaReceberAgrupadaVOs;
	}

	private List<ContaReceberAgrupadaVO> realizarCriacaoContaReceberAgrupada(Map<String, List<ContaReceberVO>> contaReceberPorData, ContaCorrenteVO contaCorrenteVO, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs = new ArrayList<ContaReceberAgrupadaVO>(0);
		ContaReceberAgrupadaVO contaReceberAgrupadaVO = null;
		for (String dataVencimento : contaReceberPorData.keySet()) {
			List<ContaReceberVO> contaReceberVOs = contaReceberPorData.get(dataVencimento);
			if (contaReceberVOs != null && contaReceberVOs.size() > 1) {
				contaReceberAgrupadaVO = new ContaReceberAgrupadaVO();
				contaReceberAgrupadaVO.setContaCorrente(contaCorrenteVO);
				contaReceberAgrupadaVO.setContaReceberVOs(contaReceberVOs);
				contaReceberAgrupadaVO.setDataAgrupamento(new Date());
				contaReceberAgrupadaVO.getResponsavelAgrupamento().setCodigo(usuarioVO.getCodigo());
				contaReceberAgrupadaVO.setDataVencimento(Uteis.getData(dataVencimento, "dd/MM/yy"));
				contaReceberAgrupadaVO.setSituacao(SituacaoContaReceber.A_RECEBER);
				contaReceberAgrupadaVO.setUnidadeEnsino(contaReceberVOs.get(0).getUnidadeEnsino());
				if (contaReceberVOs.get(0).getTipoAluno()) {
					contaReceberAgrupadaVO.setTipoPessoa(TipoPessoa.ALUNO);
					contaReceberAgrupadaVO.setMatricula(contaReceberVOs.get(0).getMatriculaAluno());
					contaReceberAgrupadaVO.setPessoa(contaReceberVOs.get(0).getPessoa());
				} else {
					contaReceberAgrupadaVO.setTipoPessoa(TipoPessoa.RESPONSAVEL_FINANCEIRO);
					contaReceberAgrupadaVO.setPessoa(contaReceberVOs.get(0).getResponsavelFinanceiro());
				}
				StringBuilder descricao = new StringBuilder("");
				for (ContaReceberVO contaReceberVO : contaReceberVOs) {
					contaReceberAgrupadaVO.setValorTotal(contaReceberAgrupadaVO.getValorTotal() + contaReceberVO.getValor());
					if (!descricao.toString().trim().isEmpty()) {
						descricao.append("\n");
					}
					TipoOrigemContaReceber origem = TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem());
					switch (origem) {
					case BIBLIOTECA:
						if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
							descricao.append("Multa de Biblioteca - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						} else {
							descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						}
						break;
					case CONTRATO_RECEITA:
						descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						break;
					case DEVOLUCAO_CHEQUE:
						if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
							descricao.append("Cheque Devolvido - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						} else {
							descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						}
						break;
					case INCLUSAOREPOSICAO:
						if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
							descricao.append("Inclusão/Reposição de disciplina - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						} else {
							descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						}
						break;
					case MATRICULA:
						descricao.append("Parcela de Matrícula (" + contaReceberVO.getMatriculaAluno().getMatricula() + ") no valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						break;
					case MENSALIDADE:
						descricao.append("Parcela " + contaReceberVO.getParcela() + " de Mensalidade (" + contaReceberVO.getMatriculaAluno().getMatricula() + ") no valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						break;
					case NEGOCIACAO:
						descricao.append("Parcela " + contaReceberVO.getParcela() + " de Negociação no valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						break;
					case OUTROS:
						if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
							descricao.append("Outras receitas no valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						} else {
							descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						}
						break;
					case REQUERIMENTO:
						if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
							descricao.append("Taxa de Requerimento - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						} else {
							descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						}
						break;

					default:
						if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
							descricao.append("Outras receitas no valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						} else {
							descricao.append(contaReceberVO.getDescricaoPagamento() + " - valor de R$" + Uteis.getDoubleFormatado(contaReceberVO.getValor()));
						}
						break;
					}

				}
				contaReceberAgrupadaVO.setDescricao(descricao.toString());
				contaReceberAgrupadaVOs.add(contaReceberAgrupadaVO);
			}
		}
		return contaReceberAgrupadaVOs;
	}

	/**
	 * Realiza a separação das conta a receber por origem
	 * 
	 * @param contaReceberVOs
	 * @return
	 */
	private Map<TipoOrigemContaReceber, List<ContaReceberVO>> realizarSeparacaoContaReceberPorOrigem(List<ContaReceberVO> contaReceberVOs) {
		Map<TipoOrigemContaReceber, List<ContaReceberVO>> contaReceberPorOrigem = new HashMap<TipoOrigemContaReceber, List<ContaReceberVO>>(0);
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			if (!contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem()))) {
				contaReceberPorOrigem.put(TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem()), new ArrayList<ContaReceberVO>());
			}
			contaReceberPorOrigem.get(TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem())).add(contaReceberVO);
		}
		return contaReceberPorOrigem;
	}

	/**
	 * Trata cada tipo de origem da conta a receber conforme a seguinte regra: 1
	 * - Origens de Matricula, Mensalidade, NEGOCIACAO, INCLUSAOREPOSICAO,
	 * OUTROS só irão agrupar se vencerem no mesmo dia 2 - Origens de
	 * Requerimento, Biblioteca e Contrato de Receita serão agrupada na conta de
	 * menor vencimento de uma das origens anteriores, porém dependem dos campos
	 * agruparBiblioteca, agruparRequerimento, agruparContratoReceita estarem
	 * definidas como true, caso esteja como false estas serão agrupadas pela
	 * propria data de vencimento da mesma.
	 * 
	 * @param contaReceberPorOrigem
	 * @param agruparBiblioteca
	 * @param agruparRequerimento
	 * @param agruparContratoReceita
	 * @return
	 */
	private Map<String, List<ContaReceberVO>> realizarAgrupamentoContaReceberPorDataVencimento(Map<TipoOrigemContaReceber, List<ContaReceberVO>> contaReceberPorOrigem, Boolean agruparBiblioteca, Boolean agruparRequerimento, Boolean agruparContratoReceita) {
		Map<String, List<ContaReceberVO>> contaReceberPorData = new HashMap<String, List<ContaReceberVO>>(0);
		Date menorData = null;
		for (TipoOrigemContaReceber origem : TipoOrigemContaReceber.values()) {
			if (!origem.equals(TipoOrigemContaReceber.REQUERIMENTO) && !origem.equals(TipoOrigemContaReceber.CONTRATO_RECEITA) && !origem.equals(TipoOrigemContaReceber.BIBLIOTECA)) {
				if (contaReceberPorOrigem.containsKey(origem)) {
					for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(origem)) {
						if (menorData == null || Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(menorData).after(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento()))) {
							menorData = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento());
						}
						if (!contaReceberPorData.containsKey(Uteis.getData(contaReceberVO.getDataVencimento()))) {
							contaReceberPorData.put(Uteis.getData(contaReceberVO.getDataVencimento()), new ArrayList<ContaReceberVO>(0));
						}
						contaReceberPorData.get(Uteis.getData(contaReceberVO.getDataVencimento())).add(contaReceberVO);
					}
					contaReceberPorOrigem.remove(origem).clear();
				}
			}
		}
		/**
		 * Caso não exista nenhuma origem diferente de REQUERIMENTO, BIBLIOTECA
		 * e CONTRATO_RECEITA irá definir a menor data de vencimento para
		 * agrupamento das contas das origens REQUERIMENTO, BIBLIOTECA e
		 * CONTRATO_RECEITA
		 */
		if ((agruparRequerimento || agruparBiblioteca || agruparContratoReceita) && menorData == null) {
			if (contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.REQUERIMENTO)) {
				for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(TipoOrigemContaReceber.REQUERIMENTO)) {
					if (menorData == null || Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(menorData).after(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento()))) {
						menorData = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento());
					}
				}
			}
			if (contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.BIBLIOTECA)) {
				for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(TipoOrigemContaReceber.BIBLIOTECA)) {
					if (menorData == null || Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(menorData).after(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento()))) {
						menorData = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento());
					}
				}
			}
			if (contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.CONTRATO_RECEITA)) {
				for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(TipoOrigemContaReceber.CONTRATO_RECEITA)) {
					if (menorData == null || Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(menorData).after(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento()))) {
						menorData = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(contaReceberVO.getDataVencimento());
					}
				}
			}
		}
		if (contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.REQUERIMENTO)) {
			for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(TipoOrigemContaReceber.REQUERIMENTO)) {
				if (agruparRequerimento) {
					if (!contaReceberPorData.containsKey(Uteis.getData(menorData))) {
						contaReceberPorData.put(Uteis.getData(menorData), new ArrayList<ContaReceberVO>(0));
					}
					contaReceberPorData.get(Uteis.getData(menorData)).add(contaReceberVO);
				} else {
					if (!contaReceberPorData.containsKey(Uteis.getData(contaReceberVO.getDataVencimento()))) {
						contaReceberPorData.put(Uteis.getData(contaReceberVO.getDataVencimento()), new ArrayList<ContaReceberVO>(0));
					}
					contaReceberPorData.get(Uteis.getData(contaReceberVO.getDataVencimento())).add(contaReceberVO);
				}
			}
		}

		if (contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.BIBLIOTECA)) {
			for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(TipoOrigemContaReceber.BIBLIOTECA)) {
				if (agruparBiblioteca) {
					if (!contaReceberPorData.containsKey(Uteis.getData(menorData))) {
						contaReceberPorData.put(Uteis.getData(menorData), new ArrayList<ContaReceberVO>(0));
					}
					contaReceberPorData.get(Uteis.getData(menorData)).add(contaReceberVO);
				} else {
					if (!contaReceberPorData.containsKey(Uteis.getData(contaReceberVO.getDataVencimento()))) {
						contaReceberPorData.put(Uteis.getData(contaReceberVO.getDataVencimento()), new ArrayList<ContaReceberVO>(0));
					}
					contaReceberPorData.get(Uteis.getData(contaReceberVO.getDataVencimento())).add(contaReceberVO);
				}
			}
		}
		if (contaReceberPorOrigem.containsKey(TipoOrigemContaReceber.CONTRATO_RECEITA)) {
			for (ContaReceberVO contaReceberVO : contaReceberPorOrigem.get(TipoOrigemContaReceber.CONTRATO_RECEITA)) {
				if (agruparContratoReceita) {
					if (!contaReceberPorData.containsKey(Uteis.getData(menorData))) {
						contaReceberPorData.put(Uteis.getData(menorData), new ArrayList<ContaReceberVO>(0));
					}
					contaReceberPorData.get(Uteis.getData(menorData)).add(contaReceberVO);
				} else {
					if (!contaReceberPorData.containsKey(Uteis.getData(contaReceberVO.getDataVencimento()))) {
						contaReceberPorData.put(Uteis.getData(contaReceberVO.getDataVencimento()), new ArrayList<ContaReceberVO>(0));
					}
					contaReceberPorData.get(Uteis.getData(contaReceberVO.getDataVencimento())).add(contaReceberVO);
				}
			}
		}

		return contaReceberPorData;
	}

	private Map<TipoPessoa, Map<Object, List<ContaReceberVO>>> consultarDadosParaAgrupamento(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, MatriculaVO matriculaVO, TurmaVO turmaVO, PessoaVO responsavelFinanceiro, Integer ano, MesAnoEnum mes) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.RESPONSAVEL_FINANCEIRO)) {
			sql.append(getSqlConsultaAgrupamentoResponsavelFinanceiro(nivelAgrupamentoContaReceber, unidadeEnsinoVO, unidadeEnsinoCursoVO, turmaVO, responsavelFinanceiro, ano, mes));
			sql.append(" order by responsavelFinanceiro, dataVencimento ");
		} else if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.ALUNO)) {
			sql.append(getSqlConsultaAgrupamentoAluno(nivelAgrupamentoContaReceber, unidadeEnsinoVO, unidadeEnsinoCursoVO, matriculaVO, turmaVO, ano, mes));
			sql.append(" order by matricula, dataVencimento ");
		} else {
			sql.append(getSqlConsultaAgrupamentoResponsavelFinanceiro(nivelAgrupamentoContaReceber, unidadeEnsinoVO, unidadeEnsinoCursoVO, turmaVO, responsavelFinanceiro, ano, mes));
			sql.append(" union all ");
			sql.append(getSqlConsultaAgrupamentoAluno(nivelAgrupamentoContaReceber, unidadeEnsinoVO, unidadeEnsinoCursoVO, matriculaVO, turmaVO, ano, mes));
			sql.append(" order by responsavelFinanceiro, matricula, dataVencimento ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		// Mantem as contas de aluno separados por tipo pessoa (ALUNO e
		// Responsavel Financeiro)
		Map<TipoPessoa, Map<Object, List<ContaReceberVO>>> contaPorTipoPessoa = new HashMap<TipoPessoa, Map<Object, List<ContaReceberVO>>>(0);
		// Mantem as contas de aluno separados por matrícula
		Map<Object, List<ContaReceberVO>> contaTipoAluno = new HashMap<Object, List<ContaReceberVO>>(0);
		// Mantem as contas de aluno separados por responsável financeiro
		Map<Object, List<ContaReceberVO>> contaTipoRespFinan = new HashMap<Object, List<ContaReceberVO>>(0);
		ContaReceberVO contaReceberVO = null;
		while (rs.next()) {
			contaReceberVO = new ContaReceberVO();
			contaReceberVO.setCodigo(rs.getInt("codigo"));
			contaReceberVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
			contaReceberVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
			contaReceberVO.setValor(rs.getDouble("valor"));
			contaReceberVO.setDataVencimento(rs.getDate("dataVencimento"));
			contaReceberVO.setTipoPessoa(rs.getString("tipoPessoa"));
			contaReceberVO.setTipoOrigem(rs.getString("tipoOrigem"));
			contaReceberVO.setDescricaoPagamento(rs.getString("descricaoPagamento"));
			contaReceberVO.getPessoa().setCodigo(rs.getInt("pessoa"));
			contaReceberVO.getPessoa().setNome(rs.getString("nome"));
			contaReceberVO.getResponsavelFinanceiro().setCodigo(rs.getInt("responsavelFinanceiro"));
			contaReceberVO.getResponsavelFinanceiro().setNome(rs.getString("nome"));
			contaReceberVO.getMatriculaAluno().setMatricula(rs.getString("matricula"));
			if (contaReceberVO.getTipoAluno()) {
				if (!contaTipoAluno.containsKey(rs.getString("matricula"))) {
					contaTipoAluno.put(rs.getString("matricula"), new ArrayList<ContaReceberVO>(0));
				}
				contaTipoAluno.get(rs.getString("matricula")).add(contaReceberVO);
			}
			if (contaReceberVO.getTipoResponsavelFinanceiro()) {
				if (!contaTipoRespFinan.containsKey(rs.getString("responsavelFinanceiro"))) {
					contaTipoRespFinan.put(rs.getString("responsavelFinanceiro"), new ArrayList<ContaReceberVO>(0));
				}
				contaTipoRespFinan.get(rs.getString("responsavelFinanceiro")).add(contaReceberVO);
			}
		}
		if (!contaTipoAluno.isEmpty()) {
			contaPorTipoPessoa.put(TipoPessoa.ALUNO, contaTipoAluno);
		}
		if (!contaTipoRespFinan.isEmpty()) {
			contaPorTipoPessoa.put(TipoPessoa.RESPONSAVEL_FINANCEIRO, contaTipoRespFinan);
		}
		return contaPorTipoPessoa;
	}

	private String getSqlConsultaAgrupamentoResponsavelFinanceiro(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, PessoaVO responsavelFinanceiro, Integer ano, MesAnoEnum mes) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select contareceber.codigo, contareceber.valor, contareceber.dataVencimento, contareceber.tipoOrigem, contareceber.tipoPessoa, contareceber.matriculaaluno as matricula, contareceber.unidadeEnsino, contareceber.pessoa, contareceber.responsavelFinanceiro, contaReceber.descricaoPagamento, pessoa.nome, contareceber.parcela, unidadeensino.nome as \"unidadeensino.nome\" ");
		sql.append(" from contareceber   ");
		sql.append(" inner join unidadeensino on contareceber.unidadeensino = unidadeensino.codigo  ");
		sql.append(" inner join pessoa on contareceber.responsavelFinanceiro = pessoa.codigo  ");

		sql.append(" where contareceber.situacao = 'AR' and contareceber.valor>0 ");
		sql.append(" and contareceber.dataVencimento >= current_date and contareceber.contaReceberAgrupada is null ");
		sql.append(" and contareceber.tipoPessoa = 'RF' and contareceber.tipoOrigem not in ('IPS', 'BCC') ");
		sql.append(" and extract(year from contareceber.dataVencimento)::INT = ").append(ano).append(" and extract(month from contareceber.dataVencimento)::INT = ").append(mes.getKey()).append("::INT ");
		sql.append(" and contareceber.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.CURSO)) {
			sql.append(" and contareceber.responsavelfinanceiro in (select  filiacao.pais from matricula   ");
			sql.append(" inner join filiacao on filiacao.aluno = matricula.aluno and responsavelfinanceiro = true ");
			sql.append(" where filiacao.pais =  contareceber.responsavelfinanceiro ");
			sql.append(" and matricula.curso = ").append(unidadeEnsinoCursoVO.getCurso().getCodigo());
			sql.append(" and matricula.turno = ").append(unidadeEnsinoCursoVO.getTurno().getCodigo());
			sql.append(" ) ");
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.TURMA)) {
			sql.append(" and contareceber.responsavelfinanceiro in (select  filiacao.pais from matricula   ");
			sql.append(" inner join filiacao on filiacao.aluno = matricula.aluno and responsavelfinanceiro = true ");
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre) desc, mp.codigo desc limit 1  )");
			sql.append(" where filiacao.pais =  contareceber.responsavelfinanceiro ");
			sql.append(" and matriculaperiodo.turma = ").append(turmaVO.getCodigo());
			sql.append(" ) ");
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.RESPONSAVEL_FINANCEIRO)) {
			sql.append(" and contareceber.responsavelfinanceiro = ").append(responsavelFinanceiro.getCodigo());
		}
		sql.append(" and (select count(cr.codigo) from contaReceber cr ");
		sql.append(" where cr.dataVencimento >= current_date and cr.situacao = 'AR' and contareceber.tipoPessoa = 'RF'  ");
		sql.append(" and extract(year from cr.dataVencimento)::INT = ").append(ano).append(" and extract(month from cr.dataVencimento)::INT = ").append(mes.getKey()).append("::INT ");
		sql.append(" and cr.responsavelFinanceiro =  contareceber.responsavelFinanceiro and cr.unidadeensino =  contareceber.unidadeensino ");
		sql.append(" and cr.contaReceberAgrupada is null ");
		sql.append(" and cr.tipoOrigem not in ('IPS', 'BCC') ");
		sql.append(" ) > 1 ");
		return sql.toString();
	}

	private String getSqlConsultaAgrupamentoAluno(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, MatriculaVO matriculaVO, TurmaVO turmaVO, Integer ano, MesAnoEnum mes) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select contareceber.codigo, contareceber.valor, contareceber.dataVencimento, contareceber.tipoOrigem, contareceber.tipoPessoa, contareceber.matriculaaluno as matricula, contareceber.unidadeEnsino, contareceber.pessoa, ");
		sql.append(" contareceber.responsavelFinanceiro, contaReceber.descricaoPagamento, pessoa.nome, contareceber.parcela,  unidadeensino.nome as \"unidadeensino.nome\" ");
		sql.append(" from contareceber   ");
		sql.append(" inner join unidadeensino on contareceber.unidadeensino = unidadeensino.codigo  ");
		sql.append(" inner join matricula on matricula.matricula = contareceber.matriculaaluno  ");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo  ");
		sql.append(" where contareceber.situacao = 'AR' and contareceber.valor > 0  ");
		sql.append(" and contareceber.dataVencimento >= current_date and contareceber.contaReceberAgrupada is null ");
		sql.append(" and contareceber.tipoPessoa = 'AL' and contareceber.tipoOrigem not in ('IPS', 'BCC') ");
		sql.append(" and extract(year from contareceber.dataVencimento)::INT = ").append(ano).append(" and extract(month from contareceber.dataVencimento)::INT = ").append(mes.getKey()).append("::INT ");
		sql.append(" and contareceber.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.CURSO)) {
			sql.append(" and matricula.curso = ").append(unidadeEnsinoCursoVO.getCurso().getCodigo());
			sql.append(" and matricula.turno = ").append(unidadeEnsinoCursoVO.getTurno().getCodigo());
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.TURMA)) {
			sql.append(" and matricula.matricula in (select  m.matricula from matricula m   ");
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = m.matricula and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = m.matricula order by (mp.ano||'/'||mp.semestre) desc, mp.codigo desc limit 1  )");
			sql.append(" where m.matricula =  matricula.matricula ");
			sql.append(" and matriculaperiodo.turma = ").append(turmaVO.getCodigo());
			sql.append(" ) ");
		}
		if (nivelAgrupamentoContaReceber.equals(NivelAgrupamentoContaReceberEnum.ALUNO)) {
			sql.append(" and matriculaAluno = '").append(matriculaVO.getMatricula()).append("' ");
		}
		sql.append(" and (select count(cr.codigo) from contaReceber cr ");
		sql.append(" where cr.dataVencimento >= current_date and cr.situacao = 'AR' and contareceber.tipoPessoa = 'AL'  ");
		sql.append(" and extract(year from cr.dataVencimento)::INT = ").append(ano).append(" and extract(month from cr.dataVencimento)::INT = ").append(mes.getKey()).append("::INT ");
		sql.append(" and cr.matriculaAluno =  contareceber.matriculaAluno and cr.unidadeensino =  contareceber.unidadeensino ");
		sql.append(" and cr.contaReceberAgrupada is null ");
		sql.append(" and cr.tipoOrigem not in ('IPS', 'BCC') ");
		sql.append(" ) > 1 ");

		return sql.toString();
	}

	@Override
	public List<ContaReceberAgrupadaVO> consultarContaReceberAgrupada(UnidadeEnsinoVO unidadeEnsinoVO, String opcaoConsulta, String valorConsulta, Date dataInicio, Date dataTermino, SituacaoContaReceber situacaoContaReceber, Integer limit, Integer offset, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT ContaReceberAgrupada.*, ");
		sql.append(" pessoa.nome as \"pessoa.nome\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sql.append(" responsavelAgrupamento.nome as \"responsavelAgrupamento.nome\", responsavelAlteracao.nome as \"responsavelAlteracao.nome\"  ");
		sql.append(" from ContaReceberAgrupada ");
		sql.append(" inner join pessoa on pessoa.codigo = ContaReceberAgrupada.pessoa ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = ContaReceberAgrupada.unidadeensino ");
		sql.append(" left join Usuario as  responsavelAgrupamento on responsavelAgrupamento.codigo = ContaReceberAgrupada.responsavelAgrupamento ");
		sql.append(" left join Usuario as  responsavelAlteracao on responsavelAlteracao.codigo = ContaReceberAgrupada.responsavelAlteracao ");
		sql.append(" where 1=1 ");
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() != null && unidadeEnsinoVO.getCodigo() > 0) {
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsinoVO.getCodigo());
		}
		if (opcaoConsulta.equals("MATRICULA")) {
			sql.append(" and matricula like '").append(valorConsulta).append("%'");
		} else if (opcaoConsulta.equals("SACADO")) {
			sql.append(" and sem_acentos(pessoa.nome) ilike sem_acentos('").append(valorConsulta).append("%')");
		}
		sql.append(" and " + realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", false));
		if (situacaoContaReceber != null) {
			sql.append(" and ContaReceberAgrupada.situacao = '").append(situacaoContaReceber.name()).append("' ");
		}
		if (opcaoConsulta.equals("MATRICULA")) {
			sql.append(" order by matricula ");
		} else if (opcaoConsulta.equals("SACADO")) {
			sql.append(" order by pessoa.nome ");
		} else if (opcaoConsulta.equals("NOSSO_NUMERO")) {
			sql.append(" and contaReceberAgrupada.nossonumero like ('").append(valorConsulta).append("%')");
		} else if (opcaoConsulta.equals("NOSSO_NUMERO_CONTA_RECEBER")) {
			sql.append(" and contaReceberAgrupada.codigo in (select distinct contaReceberAgrupada from contareceber where nossonumero like ('").append(valorConsulta).append("%'))");
		}
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO);
	}

	private List<ContaReceberAgrupadaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs = new ArrayList<ContaReceberAgrupadaVO>(0);
		while (rs.next()) {
			contaReceberAgrupadaVOs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return contaReceberAgrupadaVOs;
	}

	private ContaReceberAgrupadaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ContaReceberAgrupadaVO contaReceberAgrupadaVO = new ContaReceberAgrupadaVO();
		contaReceberAgrupadaVO.setNovoObj(false);
		contaReceberAgrupadaVO.setCodigo(rs.getInt("codigo"));
		contaReceberAgrupadaVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		contaReceberAgrupadaVO.getContaCorrente().setCodigo(rs.getInt("contaCorrente"));
		contaReceberAgrupadaVO.getPessoa().setCodigo(rs.getInt("pessoa"));
		contaReceberAgrupadaVO.getResponsavelAgrupamento().setCodigo(rs.getInt("responsavelAgrupamento"));
		contaReceberAgrupadaVO.setDataAgrupamento(rs.getDate("dataAgrupamento"));
		contaReceberAgrupadaVO.setDataAlteracao(rs.getDate("dataAlteracao"));
		contaReceberAgrupadaVO.setDataVencimento(rs.getDate("dataVencimento"));
		contaReceberAgrupadaVO.setDataRecebimento(rs.getDate("dataRecebimento"));
		contaReceberAgrupadaVO.setValorTotalDesconto(rs.getDouble("valorTotalDesconto"));
		contaReceberAgrupadaVO.setValorTotalJuro(rs.getDouble("valorTotalJuro"));
		contaReceberAgrupadaVO.setValorTotalMulta(rs.getDouble("valorTotalMulta"));
		contaReceberAgrupadaVO.setValorTotalRecebido(rs.getDouble("valorTotalRecebido"));
		contaReceberAgrupadaVO.setValorTotal(rs.getDouble("valorTotal"));
		contaReceberAgrupadaVO.setNossoNumero(rs.getString("nossoNumero"));
		contaReceberAgrupadaVO.setDescricao(rs.getString("descricao"));
		contaReceberAgrupadaVO.getMatricula().setMatricula(rs.getString("matricula"));
		contaReceberAgrupadaVO.setTipoPessoa(TipoPessoa.valueOf(rs.getString("tipoPessoa")));
		contaReceberAgrupadaVO.setSituacao(SituacaoContaReceber.valueOf(rs.getString("situacao")));

		contaReceberAgrupadaVO.getResponsavelAlteracao().setCodigo(rs.getInt("responsavelAlteracao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			contaReceberAgrupadaVO.getResponsavelAgrupamento().setNome(rs.getString("responsavelAgrupamento.nome"));
			contaReceberAgrupadaVO.getResponsavelAlteracao().setNome(rs.getString("responsavelAlteracao.nome"));
			contaReceberAgrupadaVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
			contaReceberAgrupadaVO.getPessoa().setNome(rs.getString("pessoa.nome"));

			return contaReceberAgrupadaVO;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return contaReceberAgrupadaVO;
		}
		montarDadosContaCorrente(contaReceberAgrupadaVO, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		montarDadosUnidadeEnsino(contaReceberAgrupadaVO, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		montarDadosMatricula(contaReceberAgrupadaVO, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		montarDadosPessoa(contaReceberAgrupadaVO, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);

		return contaReceberAgrupadaVO;

	}

	@Override
	public void montarDadosContaCorrente(ContaReceberAgrupadaVO contaReceberAgrupadaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (contaReceberAgrupadaVO.getContaCorrente().getCodigo() != 0) {
			contaReceberAgrupadaVO.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaReceberAgrupadaVO.getContaCorrente().getCodigo(), false, nivelMontarDados, usuarioVO));
		}
	}

	public void montarDadosUnidadeEnsino(ContaReceberAgrupadaVO contaReceberAgrupadaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (contaReceberAgrupadaVO.getUnidadeEnsino().getCodigo() != 0) {
			contaReceberAgrupadaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(contaReceberAgrupadaVO.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuarioVO));
		}
	}

	public void montarDadosPessoa(ContaReceberAgrupadaVO contaReceberAgrupadaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (contaReceberAgrupadaVO.getPessoa().getCodigo() != 0) {
			contaReceberAgrupadaVO.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(contaReceberAgrupadaVO.getPessoa().getCodigo(), false, nivelMontarDados, usuarioVO));
		}
	}

	public void montarDadosMatricula(ContaReceberAgrupadaVO contaReceberAgrupadaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (!contaReceberAgrupadaVO.getMatricula().getMatricula().trim().isEmpty()) {
			contaReceberAgrupadaVO.setMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(contaReceberAgrupadaVO.getMatricula().getMatricula(), 0, false, usuarioVO));
		}
	}

	@Override
	public Integer consultarTotalRegistroContaReceberAgrupada(UnidadeEnsinoVO unidadeEnsinoVO, String opcaoConsulta, String valorConsulta, Date dataInicio, Date dataTermino, SituacaoContaReceber situacaoContaReceber) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(ContaReceberAgrupada.codigo) as qtde ");
		sql.append(" from ContaReceberAgrupada ");
		sql.append(" inner join pessoa on pessoa.codigo = ContaReceberAgrupada.pessoa ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = ContaReceberAgrupada.unidadeensino ");
		sql.append(" where 1=1 ");
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() != null && unidadeEnsinoVO.getCodigo() > 0) {
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsinoVO.getCodigo());
		}
		if (opcaoConsulta.equals("MATRICULA")) {
			sql.append(" and matricula like '").append(valorConsulta).append("%'");
		} else if (opcaoConsulta.equals("SACADO")) {
			sql.append(" and sem_acentos(pessoa.nome) ilike sem_acentos('").append(valorConsulta).append("%')");
		} else if (opcaoConsulta.equals("NOSSO_NUMERO")) {
			sql.append(" and contaReceberAgrupada.nossonumero like ('").append(valorConsulta).append("%')");
		} else if (opcaoConsulta.equals("NOSSO_NUMERO_CONTA_RECEBER")) {
			sql.append(" and contaReceberAgrupada.codigo in (select distinct contaReceberAgrupada from contareceber where nossonumero like ('").append(valorConsulta).append("%'))");
		}
		sql.append(" and " + realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", false));
		if (situacaoContaReceber != null) {
			sql.append(" and ContaReceberAgrupada.situacao = '").append(situacaoContaReceber.name()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;

	}

	@Override
	public ContaReceberAgrupadaVO consultaPorChavePrimaria(Integer contaReceberAgrupada, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM ContaReceberAgrupada where codigo =  " + contaReceberAgrupada);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuarioVO);
		}
		throw new Exception("Dados não encontrados (Conta Receber Agrupada). ");
	}
	
	public Map<BigInteger, ContaReceberAgrupadaVO> consultarPorNossoNumeroControleCobranca(RegistroArquivoVO registroArquivoVO, Boolean bancoBrasil) {
		StringBuilder sb = new StringBuilder();
		boolean virgula = false;
		Map<BigInteger, ContaReceberAgrupadaVO> resultado = new HashMap<BigInteger, ContaReceberAgrupadaVO>(0);
		if (registroArquivoVO.getRegistroDetalheContaAgrupadaVOs().isEmpty()) {
			return resultado;
		}
		sb.append("select contareceberagrupada.codigo, nossonumero, valortotal, datavencimento, situacao, pessoa.nome, matricula, tipopessoa, unidadeensino, pessoa.codigo AS \"pessoa\", ");
		sb.append(" valorTotalRecebido, dataRecebimento ");
		sb.append(" from contareceberagrupada ");
		sb.append(" inner join pessoa on pessoa.codigo = contareceberagrupada.pessoa ");
		sb.append(" where nossonumero in('");
		for (RegistroDetalheVO detalhe : registroArquivoVO.getRegistroDetalheContaAgrupadaVOs()) {
			if (!virgula) {
				sb.append(detalhe.getIdentificacaoTituloEmpresa()).append("' ");
			} else {
				sb.append(", '").append(detalhe.getIdentificacaoTituloEmpresa()).append("' ");
			}
			virgula = true;
		}
		sb.append(") ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		
		while (tabelaResultado.next()) {
			ContaReceberAgrupadaVO contaReceberAgrupadaVO = montarDadosControleCobranca(tabelaResultado);
			resultado.put(new BigInteger(contaReceberAgrupadaVO.getNossoNumero()), contaReceberAgrupadaVO);
		}
		return resultado;
	}
	
	public ContaReceberAgrupadaVO montarDadosControleCobranca(SqlRowSet dadosSQL) {
		ContaReceberAgrupadaVO obj = new ContaReceberAgrupadaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNossoNumero(dadosSQL.getString("nossonumero"));
		obj.setValorTotal(dadosSQL.getDouble("valorTotal"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setSituacao(SituacaoContaReceber.valueOf(dadosSQL.getString("situacao")));
		obj.getPessoa().setNome(dadosSQL.getString("nome"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.setTipoPessoa(TipoPessoa.valueOf(dadosSQL.getString("tipopessoa")));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.setValorTotalRecebido(dadosSQL.getDouble("valorTotalRecebido"));
		obj.setDataRecebimento(dadosSQL.getDate("dataRecebimento"));
		return obj;
		
	}

}
