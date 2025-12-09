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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.ead.enumeradores.UsoQuestaoEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AvaliacaoOnlineQuestaoInterfaceFacade;

/**
 * @author Victor Hugo 10/10/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoOnlineQuestao extends ControleAcesso implements AvaliacaoOnlineQuestaoInterfaceFacade, Serializable {

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
		AvaliacaoOnlineQuestao.idEntidade = idEntidade;
	}

	public AvaliacaoOnlineQuestao() throws Exception {
		super();
		setIdEntidade("AvaliacaoOnlineQuestao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(avaliacaoOnlineQuestaoVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO avaliacaoonlinequestao(" + " avaliacaoonline, questao, nota, ordemapresentacao)" + " VALUES (?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			avaliacaoOnlineQuestaoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);

					sqlInserir.setInt(1, avaliacaoOnlineQuestaoVO.getAvaliacaoOnlineVO().getCodigo());
					sqlInserir.setInt(2, avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo());
					sqlInserir.setDouble(3, avaliacaoOnlineQuestaoVO.getNota());
					sqlInserir.setInt(4, avaliacaoOnlineQuestaoVO.getOrdemApresentacao());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						avaliacaoOnlineQuestaoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			avaliacaoOnlineQuestaoVO.setNovoObj(Boolean.TRUE);
			avaliacaoOnlineQuestaoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (avaliacaoOnlineQuestaoVO.getCodigo() == 0) {
			incluir(avaliacaoOnlineQuestaoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(avaliacaoOnlineQuestaoVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = " UPDATE avaliacaoonlinequestao" + " SET avaliacaoonline=?, questao=?, nota=?, ordemapresentacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setInt(1, avaliacaoOnlineQuestaoVO.getAvaliacaoOnlineVO().getCodigo());
					sqlAlterar.setInt(2, avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo());
					sqlAlterar.setDouble(3, avaliacaoOnlineQuestaoVO.getNota());
					sqlAlterar.setInt(4, avaliacaoOnlineQuestaoVO.getOrdemApresentacao());
					sqlAlterar.setInt(5, avaliacaoOnlineQuestaoVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM avaliacaoonlinequestao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, avaliacaoOnlineQuestaoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AvaliacaoOnlineQuestaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AvaliacaoOnlineQuestaoVO obj = new AvaliacaoOnlineQuestaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("avaliacaoonline"));
		obj.getQuestaoVO().setCodigo(tabelaResultado.getInt("questao"));
		obj.setNota(tabelaResultado.getDouble("nota"));
		obj.setOrdemApresentacao(tabelaResultado.getInt("ordemapresentacao"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setQuestaoVO(getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(obj.getQuestaoVO().getCodigo()));
			return obj;
		}
		return obj;
	}

	@Override
	public List<AvaliacaoOnlineQuestaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AvaliacaoOnlineQuestaoVO> vetResultado = new ArrayList<AvaliacaoOnlineQuestaoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinequestao WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineQuestaoVO();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<AvaliacaoOnlineQuestaoVO> consultarAvaliacaoOnlineQuestaoComQuestaoPorAvaliacaoOnline(Integer codigo, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select avaliacaoonlinequestao.codigo as \"avaliacaoonlinequestao.codigo\" , avaliacaoonlinequestao.avaliacaoonline as \"avaliacaoonlinequestao.avaliacaoonline\", ");
		sb.append(" avaliacaoonlinequestao.nota as \"avaliacaoonlinequestao.nota\" , avaliacaoonlinequestao.ordemapresentacao as \"avaliacaoonlinequestao.ordemapresentacao\", ");
		sb.append(" questao.codigo as \"questao.codigo\", questao.enunciado as \"questao.enunciado\" ");
		sb.append(" from avaliacaoonlinequestao  ");
		sb.append(" inner join questao on avaliacaoonlinequestao.questao = questao.codigo");
		sb.append(" where avaliacaoonlinequestao.avaliacaoonline = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<AvaliacaoOnlineQuestaoVO> vetResultado = new ArrayList<AvaliacaoOnlineQuestaoVO>();
		while (rs.next()) {
			AvaliacaoOnlineQuestaoVO obj = new AvaliacaoOnlineQuestaoVO();
			obj.setCodigo(rs.getInt("avaliacaoonlinequestao.codigo"));
			obj.getAvaliacaoOnlineVO().setCodigo(rs.getInt("avaliacaoonlinequestao.avaliacaoonline"));
			obj.getQuestaoVO().setCodigo(rs.getInt("questao.codigo"));
			obj.getQuestaoVO().setEnunciado(rs.getString("questao.enunciado"));
			obj.setNota(rs.getDouble("avaliacaoonlinequestao.nota"));
			obj.setOrdemApresentacao(rs.getInt("avaliacaoonlinequestao.ordemapresentacao"));			
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<AvaliacaoOnlineQuestaoVO> consultarPorAvaliacaoOnline(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "select * from avaliacaoonlinequestao where avaliacaoonline = " + codigo + " order by ordemapresentacao";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		
		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}

	@Override
	public void validarQuandoSelecionarQuestoesFixasRadomicamente(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		if (avaliacaoOnlineVO.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("Informe Uma Disciplina");
		}

		if ((matriculaPeriodoTurmaDisciplinaVO == null || matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo().equals(0)) && avaliacaoOnlineVO.getConteudoVO().getCodigo().equals(0)) {
			throw new Exception("Informe Um Conteúdo");
		}
	}

	@Override
	public List<AvaliacaoOnlineQuestaoVO> gerarQuestoesRandomicamente(AvaliacaoOnlineVO avaliacaoOnlineVO, Integer qtdeNivelFacil, Integer qtdeNivelMedio, Integer qtdeNivelDificil, Integer qtdeQualquerNivel, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception {
		if (avaliacaoOnlineVO.getTipoUso().equals(TipoUsoEnum.DISCIPLINA)) {
			validarQuandoSelecionarQuestoesFixasRadomicamente(avaliacaoOnlineVO, matriculaPeriodoTurmaDisciplinaVO);
		}
		Uteis.checkState(avaliacaoOnlineVO.getTipoUso().isTurma() && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDisciplinaVO()), "Por favor informe a discipina.");
		List<QuestaoVO> questaoVOs = new ArrayList<QuestaoVO>();
		Integer disciplina =  Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDisciplinaVO().getCodigo()) ? avaliacaoOnlineVO.getDisciplinaVO().getCodigo() : matriculaPeriodoTurmaDisciplinaVO != null && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getDisciplina()) ? matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo() : 0;
		Integer conteudo = matriculaPeriodoTurmaDisciplinaVO != null && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getConteudo()) ? matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo() : Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getConteudoVO().getCodigo()) ? avaliacaoOnlineVO.getConteudoVO().getCodigo() :  0;
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
			questaoVOs = getFacadeFactory().getQuestaoFacade().consultarQuestoesPorDisciplinaRandomicamente(matriculaPeriodoTurmaDisciplinaVO, 0, avaliacaoOnlineVO.getUnidadeConteudoVO().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil(), avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio(), avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil(), avaliacaoOnlineVO.getQuantidadeQualquerNivelQuestao(), UsoQuestaoEnum.ONLINE, avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum(), avaliacaoOnlineVO.getRegraDistribuicaoQuestaoEnum(), avaliacaoOnlineVO.getPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno(), matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), 0, avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), avaliacaoOnlineTemaAssuntoVOs, usuarioVO);			
		} else {
			questaoVOs = getFacadeFactory().getQuestaoFacade().consultarQuestoesPorDisciplinaRandomicamente(matriculaPeriodoTurmaDisciplinaVO, 0, avaliacaoOnlineVO.getUnidadeConteudoVO().getCodigo(),disciplina, qtdeNivelFacil, qtdeNivelMedio, qtdeNivelDificil, qtdeQualquerNivel, UsoQuestaoEnum.ONLINE, avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum(), avaliacaoOnlineVO.getRegraDistribuicaoQuestaoEnum(), avaliacaoOnlineVO.getPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno(),  conteudo, 0, avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), avaliacaoOnlineTemaAssuntoVOs, usuarioVO);			
		}
		AvaliacaoOnlineQuestaoVO obj = null;
		int x = 0;
		if(!avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
			x = avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().size();
		}
		
		for (QuestaoVO questaoVO : questaoVOs) {
			obj = new AvaliacaoOnlineQuestaoVO();
			Ordenacao.ordenarLista(questaoVO.getOpcaoRespostaQuestaoVOs(), "ordemApresentacao");
			obj.setQuestaoVO(questaoVO);
			obj.setOrdemApresentacao(++x);
			boolean adicionado = false;
			for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO : avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs()) {
				if(avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo().equals(questaoVO.getCodigo())) {
					adicionado = true;
					break;
				}
			}
			if(adicionado) {
				obj.setOrdemApresentacao(--x);
				continue;
			}
			avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().add(obj);
		}
		if(avaliacaoOnlineVO.getTipoGeracaoProvaOnline().isFixo()) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoFacil(Long.valueOf(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().stream().filter(t -> t.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.FACIL)).count()).intValue());
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoMedio(Long.valueOf(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().stream().filter(t -> t.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.MEDIO)).count()).intValue());
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoDificil(Long.valueOf(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().stream().filter(t -> t.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.DIFICIL)).count()).intValue());
		}
		Ordenacao.ordenarLista(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs(), "ordemApresentacao");
		return avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs();
	}

	@Override
	public void persistirQuestoesAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("delete from avaliacaoOnlineQuestao where avaliacaoOnline = ? and codigo not in (0 ");
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO : avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs()) {
			avaliacaoOnlineQuestaoVO.setAvaliacaoOnlineVO(avaliacaoOnlineVO);
			persistir(avaliacaoOnlineQuestaoVO, false, usuarioVO);
			sql.append(",").append(avaliacaoOnlineQuestaoVO.getCodigo());
		}
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), avaliacaoOnlineVO.getCodigo());
	}

	@Override
	public void exluirQuestaoAvaliacaoOnline(List<AvaliacaoOnlineQuestaoVO> avaliacaoOnlineQuestaoVOs, UsuarioVO usuarioVO) throws Exception {
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO : avaliacaoOnlineQuestaoVOs) {
			excluir(avaliacaoOnlineQuestaoVO, false, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAvaliacaoOnlineQuestao(final Integer codigoAvaliacaoOnline, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM avaliacaoonlinequestao WHERE avaliacaoonline = "+codigoAvaliacaoOnline+" " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql);
		} catch (Exception e) {
			throw e;
		}
	}
}
