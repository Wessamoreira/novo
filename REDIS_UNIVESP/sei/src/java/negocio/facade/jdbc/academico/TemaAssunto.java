package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.TemaAssuntoDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TemaAssuntoInterfaceFacade;

/**
 * @author Victor Hugo 27/02/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class TemaAssunto extends ControleAcesso implements TemaAssuntoInterfaceFacade {
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
		TemaAssunto.idEntidade = idEntidade;
	}

	public TemaAssunto() throws Exception {
		super();
		setIdEntidade("TemaAssunto");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(temaAssuntoVO);		
		if (temaAssuntoVO.getCodigo() == 0) {
			incluir(temaAssuntoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(temaAssuntoVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(temaAssuntoVO);
			TemaAssunto.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "insert into temaassunto(nome, abreviatura) values(?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			temaAssuntoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, temaAssuntoVO.getNome());
					sqlInserir.setString(2, temaAssuntoVO.getAbreviatura());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						temaAssuntoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			temaAssuntoVO.setNovoObj(Boolean.TRUE);
			temaAssuntoVO.setCodigo(0);
			throw e;
		}
	}

	public void validarDados(TemaAssuntoVO temaAssuntoVO) throws Exception {
		if (temaAssuntoVO.getNome().isEmpty()) {
			throw new Exception("Informe o nome do Assunto");
		}
		if (temaAssuntoVO.getAbreviatura().isEmpty()) {
			throw new Exception("Informe a abreviatura do Assunto");
		}
		if(temaAssuntoVO.getAbreviatura().length() > 20) {
			throw new Exception("A Abreviatura deve ter menos de 20 caracteres.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TemaAssunto.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE temaassunto set nome = ?, abreviatura = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, temaAssuntoVO.getNome());
					sqlAlterar.setString(2, temaAssuntoVO.getAbreviatura());
					sqlAlterar.setInt(3, temaAssuntoVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TemaAssunto.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM temaassunto WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, temaAssuntoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public TemaAssuntoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		TemaAssuntoVO obj = new TemaAssuntoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setNome(tabelaResultado.getString("nome"));
		obj.setAbreviatura(tabelaResultado.getString("abreviatura"));
		return obj;
	}

	public List<TemaAssuntoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<TemaAssuntoVO> vetResultado = new ArrayList<TemaAssuntoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	public List<TemaAssuntoVO> consultarPorNome(String nomeTemaAssunto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM temaassunto WHERE lower (sem_acentos(nome)) like(sem_acentos('%" + nomeTemaAssunto.toLowerCase() + "%')) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public TemaAssuntoVO consultarPorChavePrimaria(Integer codigoTemaAssunto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM temaassunto WHERE codigo = " + codigoTemaAssunto + " ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return new TemaAssuntoVO();
	}

	@Override
	public List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoPorNome(String nomeTemaAssunto, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, temaassunto.nome as nomeassunto, temaassunto.abreviatura as abreviatura ");
		sqlStr.append(" from temaassuntodisciplina");
		sqlStr.append(" inner join temaassunto on  temaassuntodisciplina.temaassunto = temaassunto.codigo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = temaassuntodisciplina.disciplina");
		sqlStr.append(" WHERE lower (sem_acentos(temaassunto.nome)) like(sem_acentos('%").append(nomeTemaAssunto.toLowerCase()).append("%')) ORDER BY temaassunto.nome");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		TemaAssuntoDisciplinaVO temaAssuntoDisciplina = null;
		List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinas = new ArrayList<TemaAssuntoDisciplinaVO>();
		while (rs.next()) {
			temaAssuntoDisciplina = new TemaAssuntoDisciplinaVO();
			temaAssuntoDisciplina.getDisciplinaVO().setCodigo(rs.getInt("codigodisciplina"));
			temaAssuntoDisciplina.getDisciplinaVO().setNome(rs.getString("nomedisciplina"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setNome(rs.getString("nomeassunto"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setAbreviatura(rs.getString("abreviatura"));
			temaAssuntoDisciplinas.add(temaAssuntoDisciplina);
		}
		return temaAssuntoDisciplinas;
	}
	
	@Override
	public List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoDaDisciplinaPorCodigoDisciplina(Integer codigoDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, temaassunto.nome as nomeassunto, temaassunto.abreviatura as abreviatura ");
		sqlStr.append(" from temaassuntodisciplina");
		sqlStr.append(" inner join temaassunto on  temaassuntodisciplina.temaassunto = temaassunto.codigo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = temaassuntodisciplina.disciplina");
		sqlStr.append(" WHERE disciplina.codigo = ").append(codigoDisciplina);
		sqlStr.append(" ORDER BY temaassunto.nome");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		TemaAssuntoDisciplinaVO temaAssuntoDisciplina = null;
		List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinas = new ArrayList<TemaAssuntoDisciplinaVO>();
		while (rs.next()) {
			temaAssuntoDisciplina = new TemaAssuntoDisciplinaVO();
			temaAssuntoDisciplina.getDisciplinaVO().setCodigo(rs.getInt("codigodisciplina"));
			temaAssuntoDisciplina.getDisciplinaVO().setNome(rs.getString("nomedisciplina"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setNome(rs.getString("nomeassunto"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setAbreviatura(rs.getString("abreviatura"));
			temaAssuntoDisciplinas.add(temaAssuntoDisciplina);
		}
		return temaAssuntoDisciplinas;
	}
	
	@Override
	public List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoPorNomeDisciplina(String nomeDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, temaassunto.nome as nomeassunto, temaassunto.abreviatura as abreviatura ");
		sqlStr.append(" from temaassuntodisciplina");
		sqlStr.append(" inner join temaassunto on  temaassuntodisciplina.temaassunto = temaassunto.codigo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = temaassuntodisciplina.disciplina");
		sqlStr.append(" WHERE lower (sem_acentos(disciplina.nome)) like(sem_acentos('%").append(nomeDisciplina.toLowerCase()).append("%')) ORDER BY temaassunto.nome");


		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		TemaAssuntoDisciplinaVO temaAssuntoDisciplina = null;
		List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinas = new ArrayList<TemaAssuntoDisciplinaVO>();
		while (rs.next()) {
			temaAssuntoDisciplina = new TemaAssuntoDisciplinaVO();
			temaAssuntoDisciplina.getDisciplinaVO().setCodigo(rs.getInt("codigodisciplina"));
			temaAssuntoDisciplina.getDisciplinaVO().setNome(rs.getString("nomedisciplina"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setNome(rs.getString("nomeassunto"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setAbreviatura(rs.getString("abreviatura"));
			temaAssuntoDisciplinas.add(temaAssuntoDisciplina);
		}
		return temaAssuntoDisciplinas;
	}

	@Override
	public List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoPorAbreviatura(String abreviaturaAssunto, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, temaassunto.nome as nomeassunto, temaassunto.abreviatura as abreviatura");
		sqlStr.append(" from temaassuntodisciplina");
		sqlStr.append(" inner join temaassunto on  temaassuntodisciplina.temaassunto = temaassunto.codigo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = temaassuntodisciplina.disciplina");
		sqlStr.append(" WHERE lower (sem_acentos(temaassunto.abreviatura)) like(sem_acentos('%").append(abreviaturaAssunto.toLowerCase()).append("%')) ORDER BY temaassunto.nome");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		TemaAssuntoDisciplinaVO temaAssuntoDisciplina = null;
		List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinas = new ArrayList<TemaAssuntoDisciplinaVO>();
		while (rs.next()) {
			temaAssuntoDisciplina = new TemaAssuntoDisciplinaVO();
			temaAssuntoDisciplina.getDisciplinaVO().setCodigo(rs.getInt("codigodisciplina"));
			temaAssuntoDisciplina.getDisciplinaVO().setNome(rs.getString("nomedisciplina"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setNome(rs.getString("nomeassunto"));
			temaAssuntoDisciplina.getTemaAssuntoVO().setAbreviatura(rs.getString("abreviatura"));
			temaAssuntoDisciplinas.add(temaAssuntoDisciplina);
		}
		return temaAssuntoDisciplinas;
	}

	@Override
	public List<TemaAssuntoDisciplinaVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (valorConsulta.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		if(campoConsulta.equals("codigoDisciplina")) {
			return consultarTemaAssuntoDaDisciplinaPorCodigoDisciplina(Integer.parseInt(Uteis.removeCaractersEspeciais(valorConsulta)), nivelMontarDados, usuarioVO);
		} if(campoConsulta.equals("nomeDisciplina")) {
			return consultarTemaAssuntoPorNomeDisciplina(Uteis.removeCaractersEspeciais(valorConsulta), nivelMontarDados, usuarioVO);
		} else if (campoConsulta.equals("abreviatura")) {
			return consultarTemaAssuntoPorAbreviatura(Uteis.removeCaractersEspeciais(valorConsulta), nivelMontarDados, usuarioVO);
		} else if (campoConsulta.equals("nomeAssunto")) {
			return consultarTemaAssuntoPorNome(Uteis.removeCaractersEspeciais(valorConsulta), nivelMontarDados, usuarioVO);
		}
		return new ArrayList<TemaAssuntoDisciplinaVO>();
	}

	@Override
	public List<TemaAssuntoVO> consultarTemaAssuntoPorCodigoDisciplina(Integer codigoDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct temaassunto.codigo as codigotema, temaassunto.nome as nometema, temaassunto.abreviatura as abreviaturatema");
		sqlStr.append(" from temaassunto");
		sqlStr.append(" inner join temaassuntodisciplina on  temaassuntodisciplina.temaassunto = temaassunto.codigo");
		sqlStr.append(" inner join disciplina on  disciplina.codigo = temaassuntodisciplina.disciplina");
		sqlStr.append(" where disciplina.codigo = ").append(codigoDisciplina);

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		TemaAssuntoVO temaAssuntoVO = null;
		List<TemaAssuntoVO> temaAssuntoVOs = new ArrayList<TemaAssuntoVO>();
		while (rs.next()) {
			temaAssuntoVO = new TemaAssuntoVO();
			temaAssuntoVO.setCodigo(rs.getInt("codigotema"));
			temaAssuntoVO.setNome(rs.getString("nometema"));
			temaAssuntoVO.setAbreviatura(rs.getString("abreviaturatema"));
			temaAssuntoVOs.add(temaAssuntoVO);
		}
		return temaAssuntoVOs;
	}

	@Override
	public List<TemaAssuntoVO> consultarTemaAssuntoPorNomeECodigoDisciplina(String nomeTemaAssunto, Integer codigoDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (nomeTemaAssunto.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		if (codigoDisciplina.equals(0)) {
			throw new Exception("Seleciona uma Disciplina primeiro.");
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select temaassunto.codigo as codigotema, temaassunto.nome as nometema, temaassunto.abreviatura as abreviaturatema");
		sqlStr.append(" from temaassunto");
		sqlStr.append(" inner join temaassuntodisciplina on  temaassuntodisciplina.temaassunto = temaassunto.codigo");
		sqlStr.append(" inner join disciplina on  disciplina.codigo = temaassuntodisciplina.disciplina");
		sqlStr.append(" WHERE lower (sem_acentos(temaassunto.nome)) like(sem_acentos('%" + Uteis.removeCaractersEspeciais(nomeTemaAssunto.toLowerCase()) + "%'))");
		sqlStr.append(" and disciplina.codigo = ").append(codigoDisciplina);
		sqlStr.append("  ORDER BY temaassunto.nome");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		TemaAssuntoVO temaAssuntoVO = null;
		List<TemaAssuntoVO> temaAssuntoVOs = new ArrayList<TemaAssuntoVO>();
		while (rs.next()) {
			temaAssuntoVO = new TemaAssuntoVO();
			temaAssuntoVO.setCodigo(rs.getInt("codigotema"));
			temaAssuntoVO.setNome(rs.getString("nometema"));
			temaAssuntoVO.setAbreviatura(rs.getString("abreviaturatema"));
			temaAssuntoVOs.add(temaAssuntoVO);
		}
		return temaAssuntoVOs;
	}
	
	@Override
	public Integer consultarQuantidadeAssuntoPorConteudo(Integer codigoConteudo, UsuarioVO usuarioVO) throws Exception {
		String count = " select count(distinct unidadeconteudo.temaassunto) as qtdeAssuntos from unidadeconteudo where unidadeconteudo.conteudo = " + codigoConteudo;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(count);
		Integer qtdeTemaAssunto = 0;
		if (rs.next()) {
			qtdeTemaAssunto = rs.getInt("qtdeAssuntos");
		}
		return qtdeTemaAssunto;
	}
	
	@Override
	public List<Integer> consultarTemasAssuntosPorConteudo(Integer codigoUnidadeConteudo, Integer codigoConteudo, Integer codigoMatriculaPeriodoTurmaDisciplina, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, UsuarioVO usuarioVO) throws Exception {
		StringBuilder temaAssunto = new StringBuilder();
		temaAssunto.append(" select unidadeconteudo.temaassunto as temaassunto from unidadeconteudo ");
		if(politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS)) {
			temaAssunto.append(" inner join conteudoregistroacesso on conteudoregistroacesso.unidadeconteudo = unidadeconteudo.codigo and conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		}
		temaAssunto.append(" where unidadeconteudo.conteudo = ").append(codigoConteudo);
		if(politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)) {
			temaAssunto.append(" and  unidadeconteudo.codigo = ").append(codigoUnidadeConteudo);
		}
		temaAssunto.append(" order by temaassunto");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(temaAssunto.toString());
		List<Integer> codigosTemasAssuntos = new ArrayList<Integer>();
		Integer codigo;
		while (rs.next()) {
			codigo = rs.getInt("temaassunto");
			codigosTemasAssuntos.add(codigo);
		}
		return codigosTemasAssuntos;
	}
}
