package negocio.facade.jdbc.ead;

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
import negocio.comuns.ead.GestaoEventoConteudoTurmaResponsavelAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.FuncaoResponsavelAtaEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class GestaoEventoConteudoTurmaResponsavelAta extends ControleAcesso  implements  GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1646773918189407651L;
	
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		GestaoEventoConteudoTurmaResponsavelAta.idEntidade = idEntidade;
	}

	public GestaoEventoConteudoTurmaResponsavelAta() throws Exception {
		super();
		setIdEntidade("GestaoEventoConteudoTurmaResponsavelAta");
	}
	
	public void validarDados(GestaoEventoConteudoTurmaResponsavelAtaVO obj) throws Exception {
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirGestaoEventoConteudoTurmaResponsavelAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO responsavel, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().adicionarGestaoEventoConteudoTurmaResponsavelAtaVO(turma, responsavel, usuario);
		persistir(responsavel, verificarAcesso, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGestaoEventoConteudoTurmaResponsavelAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO responsavel,  UsuarioVO usuario) throws Exception {		
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().removerGestaoEventoConteudoTurmaResponsavelAtaVO(turma, responsavel);
		StringBuilder sb = new StringBuilder("DELETE FROM gestaoeventoconteudoturmaresponsavelata where codigo =  ").append(responsavel.getCodigo()).append(" ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<GestaoEventoConteudoTurmaResponsavelAtaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (GestaoEventoConteudoTurmaResponsavelAtaVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(GestaoEventoConteudoTurmaResponsavelAtaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}

	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GestaoEventoConteudoTurmaResponsavelAtaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE gestaoeventoconteudoturmaresponsavelata");
			sql.append(" SET gestaoeventoconteudoturma=?, aluno=?, funcao=? ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getGestaoEventoConteudoTurmaVO().getCodigo());
					sqlAlterar.setInt(2, obj.getAluno().getCodigo());
					sqlAlterar.setString(3, obj.getFuncao().getName());
					sqlAlterar.setInt(4, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuarioVO);
			};
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final GestaoEventoConteudoTurmaResponsavelAtaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO gestaoeventoconteudoturmaresponsavelata(gestaoeventoconteudoturma, aluno, funcao) ");
			sql.append(" VALUES (?, ?, ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getGestaoEventoConteudoTurmaVO().getCodigo());
					sqlInserir.setInt(2, obj.getAluno().getCodigo());
					sqlInserir.setString(3, obj.getFuncao().getName());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarSeUsuarioResponsavelFuncaoAta(Integer gestaoEventoTurma, FuncaoResponsavelAtaEnum funcao, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select (case when codigo <> 0  then true else false end) as validacao  ");
		sb.append(" from gestaoeventoconteudoturmaresponsavelata  ");
		sb.append("	where gestaoeventoconteudoturma = ").append(gestaoEventoTurma);
		sb.append("	and  aluno = ").append(usuarioLogado.getPessoa().getCodigo());
		sb.append("	and funcao = '").append(funcao.name()).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Boolean existeAvaliacao = false;
		if(tabelaResultado.next()){
			existeAvaliacao = tabelaResultado.getBoolean("validacao");	
		}
		return existeAvaliacao;
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudoTurmaResponsavel() {
		StringBuilder sb = new StringBuilder("SELECT gectra.codigo as \"gectra.codigo\",  ");
		sb.append(" gectra.gestaoeventoconteudoturma as \"gectra.gestaoeventoconteudoturma\",  gectra.funcao as \"gectra.funcao\", ");
		sb.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\"");
		sb.append(" from gestaoeventoconteudoturmaresponsavelata as gectra      ");
		sb.append(" inner join pessoa on pessoa.codigo = gectra.aluno ");
		return sb;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaResponsavelAtaVO> consultarPorCodigoGestaoEventoConteudoTurmaVO(Integer codigoGestaoEventoConteudoTurmaVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudoTurmaResponsavel();
		sb.append(" WHERE gestaoeventoconteudoturma = ?");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), codigoGestaoEventoConteudoTurmaVO), nivelMontarDados, usuarioLogado));
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaResponsavelAtaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<GestaoEventoConteudoTurmaResponsavelAtaVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaResponsavelAtaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaResponsavelAtaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		GestaoEventoConteudoTurmaResponsavelAtaVO obj = new GestaoEventoConteudoTurmaResponsavelAtaVO();
		obj.setCodigo(rs.getInt("gectra.codigo"));
		obj.getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("gectra.gestaoeventoconteudoturma"));
		obj.setFuncao(FuncaoResponsavelAtaEnum.valueOf(rs.getString("gectra.funcao")));
		obj.getAluno().setCodigo(rs.getInt("pessoa.codigo"));
		obj.getAluno().setNome(rs.getString("pessoa.nome"));
		obj.setNovoObj(false);
		return obj;
	}
	
	

}
