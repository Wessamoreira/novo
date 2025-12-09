package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AvaliacaoOnlineMatriculaQuestaoInterfaceFacade;

/**
 * @author Victor Hugo 10/10/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoOnlineMatriculaQuestao extends ControleAcesso implements AvaliacaoOnlineMatriculaQuestaoInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AvaliacaoOnlineMatriculaQuestao.idEntidade = idEntidade;
	}

	public AvaliacaoOnlineMatriculaQuestao() throws Exception {
		super();
		setIdEntidade("AvaliacaoOnlineMatriculaQuestao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(avaliacaoOnlineMatriculaQuestaoVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO avaliacaoonlinematriculaquestao(" + " avaliacaoonlinematricula, questao, situacaoatividaderesposta," + " ordemapresentacao)VALUES (?, ?, ?,  ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			avaliacaoOnlineMatriculaQuestaoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getCodigo());
					sqlInserir.setInt(2, avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getCodigo());
					sqlInserir.setString(3, avaliacaoOnlineMatriculaQuestaoVO.getSituacaoAtividadeRespostaEnum().name());
					sqlInserir.setInt(4, avaliacaoOnlineMatriculaQuestaoVO.getOrdemApresentacao());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						avaliacaoOnlineMatriculaQuestaoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getAvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade().persistirRepostasQuestoesAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaQuestaoVO, usuarioVO);
		} catch (Exception e) {
			avaliacaoOnlineMatriculaQuestaoVO.setNovoObj(Boolean.TRUE);
			avaliacaoOnlineMatriculaQuestaoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (avaliacaoOnlineMatriculaQuestaoVO.getCodigo() == 0) {
			incluir(avaliacaoOnlineMatriculaQuestaoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(avaliacaoOnlineMatriculaQuestaoVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE avaliacaoonlinematriculaquestao" + " SET avaliacaoonlinematricula=?, questao=?, situacaoatividaderesposta=?," + " ordemapresentacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setInt(1, avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getCodigo());
					sqlAlterar.setInt(2, avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getCodigo());
					sqlAlterar.setString(3, avaliacaoOnlineMatriculaQuestaoVO.getSituacaoAtividadeRespostaEnum().name());
					sqlAlterar.setInt(4, avaliacaoOnlineMatriculaQuestaoVO.getOrdemApresentacao());
					sqlAlterar.setInt(5, avaliacaoOnlineMatriculaQuestaoVO.getCodigo());

					return sqlAlterar;
				}
			});
			getFacadeFactory().getAvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade().persistirRepostasQuestoesAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaQuestaoVO, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AvaliacaoOnlineMatriculaQuestaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AvaliacaoOnlineMatriculaQuestaoVO obj = new AvaliacaoOnlineMatriculaQuestaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getAvaliacaoOnlineMatriculaVO().setCodigo(tabelaResultado.getInt("avaliacaoonlinematricula"));
		obj.getQuestaoVO().setCodigo(tabelaResultado.getInt("questao"));
		obj.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.valueOf(tabelaResultado.getString("situacaoatividaderesposta")));
		obj.setOrdemApresentacao(tabelaResultado.getInt("ordemapresentacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setAvaliacaoOnlineMatriculaRespostaQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade().consultarPorAvaliacaoOnlineMatriculaQuestao(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			obj.setQuestaoVO(getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(obj.getQuestaoVO().getCodigo()));
			return obj;
		}
		return obj;
	}

	@Override
	public List<AvaliacaoOnlineMatriculaQuestaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AvaliacaoOnlineMatriculaQuestaoVO> vetResultado = new ArrayList<AvaliacaoOnlineMatriculaQuestaoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineMatriculaQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinematriculaquestao WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineMatriculaQuestaoVO();
	}

	@Override
	public void persistirQuestoesAvaliacaoOnlineMatricula(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		for (AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO : avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineMatriculaQuestaoVOs()) {
			avaliacaoOnlineMatriculaQuestaoVO.setAvaliacaoOnlineMatriculaVO(avaliacaoOnlineMatriculaVO);
			persistir(avaliacaoOnlineMatriculaQuestaoVO, false, usuarioVO);
		}
	}

	@Override
	public void realizarCorrecaoQuestao(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO) {
		if (avaliacaoOnlineMatriculaQuestaoVO.getSituacaoAtividadeRespostaEnum().equals(SituacaoAtividadeRespostaEnum.ANULADA)) {
			
			avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setAcertouQuestao(true);
			avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setErrouQuestao(false);
			
			if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.FACIL)) {
				avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil());
			} else if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.MEDIO)) {
				avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio());
			} else {
				avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil());
			}
			return;
		}
		avaliacaoOnlineMatriculaQuestaoVO.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.ERROU);
		avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setAcertouQuestao(false);
		avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setErrouQuestao(true);
		avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setQuestaoNaoRespondida(true);
		int quantidadeOpcaoCorretaQuestao = 0;
		int quantidadeOpcaoCorretaMarcada = 0;
		int quantidadeOpcaoMarcadaAluno = 0;
		
		if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getTipoQuestaoEnum().equals(TipoQuestaoEnum.MULTIPLA_ESCOLHA)) {
			for (AvaliacaoOnlineMatriculaRespostaQuestaoVO objExistente : avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaRespostaQuestaoVOs()) {
				if (objExistente.getOpcaoRespostaQuestaoVO().getCorreta()) {
					quantidadeOpcaoCorretaQuestao++;
				}
			}
		}
		
		for (AvaliacaoOnlineMatriculaRespostaQuestaoVO objExistente2 : avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaRespostaQuestaoVOs()) {
			if (objExistente2.getOpcaoRespostaQuestaoVO().getCorreta() && objExistente2.getMarcada()) {
				if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getTipoQuestaoEnum().equals(TipoQuestaoEnum.MULTIPLA_ESCOLHA)) {
					quantidadeOpcaoCorretaMarcada++;
				} else {
					avaliacaoOnlineMatriculaQuestaoVO.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.ACERTOU);
					avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setAcertouQuestao(true);
					avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setErrouQuestao(false);
					avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setQuestaoNaoRespondida(true);
				}
			}
			if (objExistente2.getMarcada()) {
				quantidadeOpcaoMarcadaAluno++;
				avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setQuestaoNaoRespondida(false);
			}
		}
		
		if ((quantidadeOpcaoCorretaQuestao == quantidadeOpcaoCorretaMarcada) && (quantidadeOpcaoMarcadaAluno <= quantidadeOpcaoCorretaQuestao) && avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getTipoQuestaoEnum().equals(TipoQuestaoEnum.MULTIPLA_ESCOLHA)) {
			avaliacaoOnlineMatriculaQuestaoVO.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.ACERTOU);
			avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setAcertouQuestao(true);
			avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setErrouQuestao(false);
		}
		
		if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getQuestaoNaoRespondida()) {
			avaliacaoOnlineMatriculaQuestaoVO.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.NAO_RESPONDIDA);
			avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setAcertouQuestao(false);
			avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().setErrouQuestao(false);
		}
		if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getAcertouQuestao()) {
			if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.FACIL)) {
				avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil());
			} else if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.MEDIO)) {
				avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio());
			} else {
				avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil());
			}
		}
	}
	
	@Override
	public void realizarCorrecaoNotaAvaliacaoOnlineQuestaoAnulada(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, AvaliacaoOnlineMatriculaVO obj) {
		if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.FACIL)) {
			obj.setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil());
		} else if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.MEDIO)) {
			obj.setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio());
		} else {
			obj.setNota(avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getNota() + avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil());
		}
		
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<AvaliacaoOnlineMatriculaQuestaoVO> consultarPorAvaliacaoOnlineMatricula(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinematriculaquestao WHERE avaliacaoonlinematricula = ? order by codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioLogado);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<AvaliacaoOnlineMatriculaQuestaoVO> consultarQuestoesQueAcertouOuErrou(Integer codigoAvaliacaoOnlineMatricula, Integer codigoTemaAssunto, SituacaoAtividadeRespostaEnum situacaoAtividadeRespostaEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select *, ");
		sqlStr.append("(select count(mptd.codigo) from matriculaperiodoturmadisciplina mptd");
		sqlStr.append("	inner join avaliacaoonlinematricula on mptd.codigo = avaliacaoonlinematricula.matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo");
		sqlStr.append("	where matriculaperiodoturmadisciplina.turma = mptd.turma");
		sqlStr.append("	and matriculaperiodoturmadisciplina.ano = mptd.ano");
		sqlStr.append("	and matriculaperiodoturmadisciplina.semestre = mptd.semestre");
		sqlStr.append("	and matriculaperiodoturmadisciplina.disciplina = mptd.disciplina");
		sqlStr.append("	and matriculaperiodoturmadisciplina.codigo != mptd.codigo");
		sqlStr.append("	and avaliacaoonlinematriculaquestao.questao =  questao.codigo");
		sqlStr.append(" and situacaoatividaderesposta in ('ACERTOU', 'CANCELADA') ) as acertouTurma,");
		sqlStr.append("(select count(mptd.codigo) from matriculaperiodoturmadisciplina mptd");
		sqlStr.append("	inner join avaliacaoonlinematricula on mptd.codigo = avaliacaoonlinematricula.matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo");
		sqlStr.append("	where matriculaperiodoturmadisciplina.turma = mptd.turma");
		sqlStr.append("	and matriculaperiodoturmadisciplina.ano = mptd.ano");
		sqlStr.append("	and matriculaperiodoturmadisciplina.semestre = mptd.semestre");
		sqlStr.append("	and matriculaperiodoturmadisciplina.disciplina = mptd.disciplina");
		sqlStr.append("	and matriculaperiodoturmadisciplina.codigo != mptd.codigo");
		sqlStr.append("	and avaliacaoonlinematriculaquestao.questao =  questao.codigo");
		sqlStr.append(" and situacaoatividaderesposta in ('ERROU', 'NAO_RESPONDIDA') ) as errouTurma");
		sqlStr.append(" from avaliacaoonlinematriculaquestao");
		sqlStr.append(" inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
		sqlStr.append(" inner join avaliacaoonlinematricula on  avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = avaliacaoonlinematricula.matriculaperiodoturmadisciplina");
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append(" inner join questaoassunto on questaoassunto.questao = questao.codigo");
		}
		sqlStr.append(" where avaliacaoonlinematricula = ").append(codigoAvaliacaoOnlineMatricula);
		if (situacaoAtividadeRespostaEnum.equals(SituacaoAtividadeRespostaEnum.ACERTOU)) {
			sqlStr.append(" and situacaoatividaderesposta in ('ACERTOU', 'CANCELADA')");
		} else if (situacaoAtividadeRespostaEnum.equals(SituacaoAtividadeRespostaEnum.ERROU)) {
			sqlStr.append(" and situacaoatividaderesposta in ('ERROU', 'NAO_RESPONDIDA')");
		}
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append(" and questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		sqlStr.append(" order by avaliacaoonlinematricula.dataTermino");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AvaliacaoOnlineMatriculaQuestaoVO obj = null;
		List<AvaliacaoOnlineMatriculaQuestaoVO> avaliacaoOnlineMatriculaQuestaoVOs = new ArrayList<AvaliacaoOnlineMatriculaQuestaoVO>();
		while (rs.next()) {
			obj = new AvaliacaoOnlineMatriculaQuestaoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getAvaliacaoOnlineMatriculaVO().setCodigo(rs.getInt("avaliacaoonlinematricula"));
			obj.getQuestaoVO().setCodigo(rs.getInt("questao"));
			obj.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.valueOf(rs.getString("situacaoatividaderesposta")));
			obj.setOrdemApresentacao(rs.getInt("ordemapresentacao"));
			obj.setAcertouTurma(rs.getInt("acertouTurma"));
			obj.setErrouTurma(rs.getInt("errouTurma"));
			obj.setAvaliacaoOnlineMatriculaRespostaQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade().consultarPorAvaliacaoOnlineMatriculaQuestao(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			obj.setQuestaoVO(getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(obj.getQuestaoVO().getCodigo()));
			avaliacaoOnlineMatriculaQuestaoVOs.add(obj);
		}
		return avaliacaoOnlineMatriculaQuestaoVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoPorCodigo(final Integer avaliacaoOnlineMatriculaQuestao, final SituacaoAtividadeRespostaEnum situacao, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE avaliacaoonlinematriculaquestao set situacaoAtividadeResposta=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, situacao.toString());
					sqlAlterar.setInt(++i, avaliacaoOnlineMatriculaQuestao.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<AvaliacaoOnlineMatriculaQuestaoVO> consultarPorAvaliacaoOnlineMatriculaEQuestao(Integer avaliacaoOnlineMatricula, Integer questao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinematriculaquestao WHERE avaliacaoonlinematricula = ? and questao = ? order by codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, avaliacaoOnlineMatricula, questao);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioLogado);
	}
}
