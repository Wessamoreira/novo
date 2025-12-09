package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ForumPessoaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ForumPessoaInterfaceFacade;

@Repository
@Lazy
public class ForumPessoa extends ControleAcesso implements ForumPessoaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2921564204641634239L;
	protected static String idEntidade;

	private void validarDados(ForumPessoaVO obj) throws ConsistirException {
		ConsistirException ex = new ConsistirException();
		if (!Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ForumPessoa_pessoa"));
		}
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirForumPessoa(ForumVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		for (ForumPessoaVO forumPessoa : obj.getForumPessoaVOs()) {
			if(!Uteis.isAtributoPreenchido(forumPessoa)){
				incluir(forumPessoa, controlarAcesso, usuario);	
			}else{
				alterar(forumPessoa, controlarAcesso, usuario);
			}
		}

	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ForumPessoaVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO forumpessoa ");
			sql.append(" ( forum, pessoa ) ");
			sql.append(" VALUES ( ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(x++, obj.getForumVO().getCodigo().intValue());
					sqlInserir.setInt(x++, obj.getPessoaVO().getCodigo().intValue());
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
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		} finally {

		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ForumPessoaVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder("UPDATE forumpessoa ");
			sql.append(" set forum = ?, pessoa=?  ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(x++, obj.getForumVO().getCodigo().intValue());
					sqlAlterar.setInt(x++, obj.getPessoaVO().getCodigo().intValue());
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		} finally {
			
		}
	}
	
	private StringBuilder getSelectCompleto() {
		StringBuilder sb = new StringBuilder("SELECT forumPessoa.codigo as  \"forumPessoa.codigo\", forumPessoa.pessoa as  \"forumPessoa.pessoa\", forumPessoa.forum as  \"forumPessoa.forum\", ");
		sb.append(" pessoa.nome as \"pessoa.nome\"");
		sb.append(" FROM ForumPessoa ");
		sb.append(" inner join pessoa on pessoa.codigo = forumPessoa.pessoa ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ForumPessoaVO> consultarForumPessoa(Integer forum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		StringBuilder sb = getSelectCompleto();
		sb.append(" WHERE forumPessoa.forum = ?");
		try {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { forum.intValue() });
			return montarDadosConsulta(resultado,  nivelMontarDados, usuario);
		} finally {
			sb = null;
			resultado = null;
		}

	}

	private List<ForumPessoaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ForumPessoaVO> forumPessoaVOs = new ArrayList<ForumPessoaVO>(0);
		while (rs.next()) {
			forumPessoaVOs.add(montarDados(rs,  nivelMontarDados, usuarioLogado));
		}
		return forumPessoaVOs;
	}

	private ForumPessoaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ForumPessoaVO obj = new ForumPessoaVO();
		obj.setCodigo(rs.getInt("forumPessoa.codigo"));
		obj.getForumVO().setCodigo(rs.getInt("forumPessoa.forum"));
		obj.getPessoaVO().setCodigo(rs.getInt("forumPessoa.pessoa"));
		obj.getPessoaVO().setNome(rs.getString("pessoa.nome"));
//		if(nivelMontarDados ==  Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS){
//			obj.getPessoaVO().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicas(obj.getPessoaVO().getCodigo(), false, false, usuarioLogado));	
//		}
		
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ForumPessoa.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ForumPessoa.idEntidade = idEntidade;
	}

}