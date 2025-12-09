package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ForumInteracaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ForumInteracaoGostadoInterfaceFacade;

@Repository
@Lazy
public class ForumInteracaoGostado extends ControleAcesso implements ForumInteracaoGostadoInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -8757526757103031388L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ForumInteracaoVO forumInteracaoVO, final UsuarioVO usuario) throws Exception {
        try {                       
            final StringBuilder sql = new StringBuilder("INSERT INTO ForumInteracaoGostado ");
            sql.append(" (forumInteracao, dataGostou, usuarioGostou) ");
            sql.append(" VALUES ( ?, ?, ?)").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(x++, forumInteracaoVO.getCodigo());
                    sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlInserir.setInt(x++, usuario.getCodigo());                                       
                    return sqlInserir;
                }
            });
            forumInteracaoVO.setJaGostado(true);
        } catch (Exception e) {
            forumInteracaoVO.setNovoObj(true);
            throw e;
        }

    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final ForumInteracaoVO forumInteracaoVO, final UsuarioVO usuario) throws Exception {
        try {                       
            final StringBuilder sql = new StringBuilder("DELETE FROM ForumInteracaoGostado ");
            sql.append(" WHERE forumInteracao = ").append(forumInteracaoVO.getCodigo()).append(" and usuarioGostou =  ").append(usuario.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));           
            getConexao().getJdbcTemplate().execute(sql.toString());
            forumInteracaoVO.setJaGostado(false);
        } catch (Exception e) {
            forumInteracaoVO.setNovoObj(true);
            throw e;
        }

    }
    

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public Boolean consultarForumIntegracaoGostadoPorUsuario(UsuarioVO usuarioManterVO, UsuarioVO usuarioLogado) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * from ForumInteracaoGostado where usuarioGostou = ").append(usuarioManterVO.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorUsuario(final UsuarioVO usuario) throws Exception {
		final StringBuilder sql = new StringBuilder("DELETE FROM ForumInteracaoGostado ");
		sql.append(" WHERE usuarioGostou =  ").append(usuario.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sql.toString());
	}

}
