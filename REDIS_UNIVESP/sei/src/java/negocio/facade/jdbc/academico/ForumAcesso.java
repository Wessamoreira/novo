package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ForumVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ForumAcessoInterfaceFacade;

@Repository
@Lazy
public class ForumAcesso extends ControleAcesso implements ForumAcessoInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = 7187087804763390259L;

    @Override
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void incluir(final ForumVO forumVO, final UsuarioVO usuario) throws Exception {
        try {            
            final StringBuilder sql = new StringBuilder("INSERT INTO ForumAcesso ");
            sql.append(" (forum, dataAcesso, usuarioAcesso) ");
            sql.append(" VALUES ( ?, ?, ?)").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(x++, forumVO.getCodigo());
                    sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlInserir.setInt(x++, usuario.getCodigo());                                       
                    return sqlInserir;
                }
            });            
        } catch (Exception e) {            
            throw e;
        }

    }
    
    @Override
	public List<PessoaVO> consultarAlunoQueAcessaramForum(Integer forum, UsuarioVO usuario) throws Exception {
		List<PessoaVO> lista = new ArrayList<PessoaVO>();
		StringBuilder sb = new StringBuilder("select pessoa.codigo as  \"pessoa.codigo\", pessoa.nome as  \"pessoa.nome\" ");
		sb.append(" from forumAcesso ");
		sb.append(" inner join usuario on usuario.codigo = forumAcesso.usuarioacesso ");
		sb.append(" inner join pessoa on pessoa.codigo = usuario.pessoa and pessoa.aluno = true");
		sb.append(" where forumAcesso.forum = ").append(forum);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(rs.getInt("pessoa.codigo"));
			obj.setNome(rs.getString("pessoa.nome"));
			lista.add(obj);
		}
		return lista;
	}

}
