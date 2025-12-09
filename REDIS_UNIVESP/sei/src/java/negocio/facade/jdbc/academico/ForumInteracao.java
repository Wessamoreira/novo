package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ForumInteracaoVO;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumInteracaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ForumInteracaoInterfaceFacade;

@Repository
@Lazy
public class ForumInteracao extends ControleAcesso implements ForumInteracaoInterfaceFacade {


    /**
     * 
     */
    private static final long serialVersionUID = -2025472072854701870L;

    private void validarDados(ForumInteracaoVO forumInteracaoVO) throws ConsistirException {
        ConsistirException ex = new ConsistirException();
        if(forumInteracaoVO.getInteracao().trim().isEmpty() || Uteis.retiraTags(forumInteracaoVO.getInteracao()).trim().isEmpty()
                || forumInteracaoVO.getInteracao().equals("Envie uma nova interação")){
            ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ForumInteracao_interacao"));
        }
        if(!ex.getListaMensagemErro().isEmpty()){
            throw ex;
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ForumInteracaoVO forumInteracaoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {        
        getConexao().getJdbcTemplate().update("UPDATE forumInteracao set excluido = true where codigo = "+forumInteracaoVO.getCodigo()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        forumInteracaoVO.setExcluido(true);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarNivelApresentacao(ForumInteracaoVO obj, UsuarioVO usuario) throws Exception {        
    	StringBuilder sb = new StringBuilder("UPDATE forumInteracao set nivelApresentacao = '");
    	try {
    		if(Uteis.isAtributoPreenchido(obj.getForumInteracaoPai().getNivelApresentacao())) {
    			int qtdNivel = StringUtils.countMatches(obj.getForumInteracaoPai().getNivelApresentacao(), ".");
    			if(qtdNivel == 4){
        			String nivel = obj.getForumInteracaoPai().getNivelApresentacao().substring(0, obj.getForumInteracaoPai().getNivelApresentacao().lastIndexOf(".")+1);
        			sb.append(nivel).append(obj.getCodigo()).append("'");
        		}else{
        			sb.append(obj.getForumInteracaoPai().getNivelApresentacao()).append(".").append(obj.getCodigo()).append("'");
        		}	
    		} else {
    			sb.append(obj.getCodigo()).append("'");
    		}
        	sb.append("where codigo = ");
        	sb.append(obj.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        	getConexao().getJdbcTemplate().update(sb.toString());	
		} finally {
			sb= null;
		}
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(final ForumInteracaoVO forumInteracaoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
        	incluir(forumInteracaoVO, controlarAcesso, usuario);
        	atualizarNivelApresentacao(forumInteracaoVO, usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ForumInteracaoVO forumInteracaoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	 try {
             validarDados(forumInteracaoVO);
             final StringBuilder sql = new StringBuilder("INSERT INTO forumInteracao ");
             sql.append(" ( interacao, dataInteracao, usuarioInteracao, forum, forumInteracaoPai ) ");
             sql.append(" VALUES ( ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
             forumInteracaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                 public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                     int x = 1;
                     PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                     sqlInserir.setString(x++, forumInteracaoVO.getInteracao());
                     sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(forumInteracaoVO.getDataInteracao()));
                     sqlInserir.setInt(x++, forumInteracaoVO.getUsuarioInteracao().getCodigo());
                     sqlInserir.setInt(x++, forumInteracaoVO.getForum());
                     if (forumInteracaoVO.getForumInteracaoPai().getCodigo().intValue() != 0) {
                    	 sqlInserir.setInt(x++, forumInteracaoVO.getForumInteracaoPai().getCodigo().intValue());
 					 } else {
 						sqlInserir.setNull(x++, 0);
 					 }
                     return sqlInserir;
                 }
             }, new ResultSetExtractor<Integer>() {

                 public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                     if (arg0.next()) {
                         forumInteracaoVO.setNovoObj(Boolean.FALSE);
                         return arg0.getInt("codigo");
                     }
                     return null;
                 }
             }));
         } catch (Exception e) {
             forumInteracaoVO.setNovoObj(true);
             throw e;
         }
    }
    
    
    
    private String getSelectCompleto(Integer usuario) {
        StringBuilder sb = new StringBuilder("SELECT forumInteracao.*, ");
        sb.append(" UsuarioInteracao.nome AS \"usuarioInteracao.nome\", ");
        sb.append(" pessoaCriacao.codigo AS \"pessoaCriacao.codigo\", pessoaCriacao.nome AS \"pessoaCriacao.nome\", ");
        sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\",arquivo.cpfrequerimento AS \"arquivo.cpfrequerimento\", ");
        sb.append(" (select count(forumInteracaoGostado.*) from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = forumInteracao.codigo  ) as qtdeGostado, ");
        sb.append(" (select case when count(forumInteracaoGostado.*) > 0 then true else false end jaGostado from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = forumInteracao.codigo and forumInteracaoGostado.usuarioGostou = ").append(usuario).append(" limit 1) as jaGostado ");
        sb.append(" FROM forumInteracao ");        
        sb.append(" inner join Usuario  as UsuarioInteracao on forumInteracao.usuarioInteracao = UsuarioInteracao.codigo ");
        sb.append(" left join Pessoa  as pessoaCriacao on pessoaCriacao.codigo = UsuarioInteracao.pessoa ");
        sb.append(" left join Arquivo on Arquivo.codigo = pessoaCriacao.arquivoImagem ");        
        return sb.toString();
    }

    @Override
    public List<ForumInteracaoVO> consultarPorForum(Integer forum, OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao, Integer limite, Integer pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder(getSelectCompleto(usuario.getCodigo()));
        sb.append(" WHERE forum = ").append(forum);
        sb.append(" order by nivelApresentacao ");
        if(limite != null && limite>0){
            sb.append(" limit ").append(limite).append(" offset ").append(pagina);
        }        
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
    }
    
    @Override
    public Integer consultarTotalRegistroPorForum(Integer forum) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT count(codigo) as qtde from forumInteracao ");
        sb.append(" WHERE forum = ").append(forum);               
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            return rs.getInt("qtde");
        }
        return 0;
    }
    
    @Override
    public List<ForumInteracaoVO> consultarPorForumPorUsuario(Integer forum, Integer usuarioInteracao , Integer limite, Integer pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder(" WITH RECURSIVE arvore_foruminteracao (codigo, usuariointeracao, datainteracao, excluido, forum, interacao, foruminteracaopai, nivelapresentacao) AS ");
        sb.append(" (");
        sb.append(" SELECT foruminteracao.codigo, usuariointeracao, datainteracao, excluido, forum, interacao, foruminteracaopai, nivelapresentacao  ");
        sb.append("  FROM foruminteracao ");
        sb.append(" inner join usuario  as usuario on foruminteracao.usuarioInteracao = usuario.codigo ");
        sb.append(" inner join pessoa  as pessoa on pessoa.codigo = usuario.pessoa ");
        sb.append(" WHERE foruminteracao.forum = ").append(forum).append(" and pessoa.codigo = ").append(usuarioInteracao);
        
        sb.append(" UNION ");
        sb.append(" SELECT foruminteracao.codigo, foruminteracao.usuariointeracao, foruminteracao.datainteracao,  ");
        sb.append(" foruminteracao.excluido, foruminteracao.forum, foruminteracao.interacao, foruminteracao.foruminteracaopai, foruminteracao.nivelapresentacao FROM foruminteracao  ");
        sb.append(" INNER JOIN arvore_foruminteracao ON foruminteracao.codigo = arvore_foruminteracao.foruminteracaopai ");
        sb.append(" ) ");
        sb.append(" select arvore_foruminteracao.codigo, usuariointeracao, datainteracao, excluido, forum, interacao, foruminteracaopai, nivelapresentacao, ");
        sb.append(" UsuarioInteracao.nome AS \"usuarioInteracao.nome\", ");
        sb.append(" pessoaCriacao.codigo AS \"pessoaCriacao.codigo\", pessoaCriacao.nome AS \"pessoaCriacao.nome\", ");
        sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\", arquivo.cpfrequerimento AS \"arquivo.cpfrequerimento\", ");
        sb.append(" (select count(1) from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = arvore_foruminteracao.codigo  ) as qtdeGostado, ");
        sb.append(" (select case when count(1) > 0 then true else false end jaGostado from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = arvore_foruminteracao.codigo and forumInteracaoGostado.usuarioGostou = ").append(usuario.getCodigo()).append(" limit 1) as jaGostado ");
        sb.append(" from arvore_foruminteracao ");
        sb.append(" inner join Usuario  as UsuarioInteracao on arvore_foruminteracao.usuarioInteracao = UsuarioInteracao.codigo ");
        sb.append(" left join Pessoa  as pessoaCriacao on pessoaCriacao.codigo = UsuarioInteracao.pessoa ");
        sb.append(" left join Arquivo on Arquivo.codigo = pessoaCriacao.arquivoImagem ");
        sb.append(" order by nivelApresentacao ");
        if(limite != null && limite>0){
            sb.append(" limit ").append(limite).append(" offset ").append(pagina);
        }
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List<ForumInteracaoVO> forumInteracaoVOs = new ArrayList<ForumInteracaoVO>(0);
        while(rs.next()){
        	ForumInteracaoVO obj = montarDados(rs);
        	if(obj.getUsuarioInteracao().getPessoa().getCodigo().equals(usuarioInteracao) && !obj.getExcluido()){
        		obj.setCssQtdRegistroForum("background-color: #e8e8e8;");
        	}
            forumInteracaoVOs.add(obj);
        }
        return  forumInteracaoVOs;
    }
    
    @Override
    public Integer consultarTotalRegistroPorForumPorUsuario(Integer forum, Integer usuarioInteracao) throws Exception {
    	StringBuilder sb = new StringBuilder(" WITH RECURSIVE arvore_foruminteracao (codigo, usuariointeracao, datainteracao, excluido, forum, interacao, foruminteracaopai, nivelapresentacao) AS ");
        sb.append(" (");
        sb.append(" SELECT foruminteracao.codigo, usuariointeracao, datainteracao, excluido, forum, interacao, foruminteracaopai, nivelapresentacao  ");
        sb.append("  FROM foruminteracao ");
        sb.append(" inner join usuario  as usuario on foruminteracao.usuarioInteracao = usuario.codigo ");
        sb.append(" inner join pessoa  as pessoa on pessoa.codigo = usuario.pessoa ");
        sb.append(" WHERE foruminteracao.forum = ").append(forum).append(" and pessoa.codigo = ").append(usuarioInteracao);
        
        sb.append(" UNION ");
        sb.append(" SELECT foruminteracao.codigo, foruminteracao.usuariointeracao, foruminteracao.datainteracao,  ");
        sb.append(" foruminteracao.excluido, foruminteracao.forum, foruminteracao.interacao, foruminteracao.foruminteracaopai, foruminteracao.nivelapresentacao FROM foruminteracao  ");
        sb.append(" INNER JOIN arvore_foruminteracao ON foruminteracao.codigo = arvore_foruminteracao.foruminteracaopai ");
        sb.append(" ) ");
        sb.append(" select count(codigo) as qtde from arvore_foruminteracao; ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            return rs.getInt("qtde");
        }
        return 0;
    }
    
    
    private List<ForumInteracaoVO> montarDadosConsulta(SqlRowSet rs){
        List<ForumInteracaoVO> forumInteracaoVOs = new ArrayList<ForumInteracaoVO>(0);
        while(rs.next()){
            forumInteracaoVOs.add(montarDados(rs));
        }
        return  forumInteracaoVOs;
    }
    
    private ForumInteracaoVO montarDados(SqlRowSet rs){
        ForumInteracaoVO obj = new ForumInteracaoVO();
        obj.setCodigo(rs.getInt("codigo"));
        obj.setForum(rs.getInt("forum"));
        obj.setDataInteracao(rs.getTimestamp("dataInteracao"));
        obj.setJaGostado(rs.getBoolean("jaGostado"));
        obj.setQtdeGostado(rs.getInt("qtdeGostado"));
        obj.setExcluido(rs.getBoolean("excluido"));
        obj.setInteracao(rs.getString("interacao"));
        obj.setNivelApresentacao(rs.getString("nivelApresentacao"));
        obj.getForumInteracaoPai().setCodigo(rs.getInt("forumInteracaoPai"));
        obj.getUsuarioInteracao().setCodigo(rs.getInt("usuarioInteracao"));
        obj.getUsuarioInteracao().setNome(rs.getString("usuarioInteracao.nome"));
        obj.getUsuarioInteracao().getPessoa().setCodigo(rs.getInt("pessoaCriacao.codigo"));
        obj.getUsuarioInteracao().getPessoa().setNome(rs.getString("pessoaCriacao.nome"));
        if(rs.getInt("arquivo.codigo") > 0){
        obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setCodigo(rs.getInt("arquivo.codigo"));
        obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setPastaBaseArquivo(rs.getString("arquivo.pastaBaseArquivo").replaceAll("\\\\", "/"));
        obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setNome(rs.getString("arquivo.nome"));
        if (Uteis.isAtributoPreenchido(rs.getString("arquivo.cpfrequerimento"))) {
          	obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setCpfRequerimento(rs.getString("arquivo.cpfrequerimento"));
		}else {
			obj.getUsuarioInteracao().getPessoa().setCPF("");
		}
        }
        return obj;
    }

    @Override
    public Integer consultarTotalGostado(Integer codigoForumInteracao) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(forumInteracaoGostado.*) from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = ").append(codigoForumInteracao);               
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            return rs.getInt("count");
        }
        return 0;
    }
    
    public List<ForumInteracaoVO> consultarPorForumPorCodigoUsuarioLogado(Integer forum, OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao, Integer limite, Integer pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder(getStringParaSelectConcatenado(usuario.getCodigo()));
        sb.append(" WHERE forum = ").append(forum);
        sb.append(" order by nivelApresentacaoPrincipal desc , nivelapresentacao )");
        if(limite != null && limite>0){
            sb.append(" limit ").append(limite).append(" offset ").append(pagina);
        }        
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
    }
    
    private String getStringParaSelectConcatenado(Integer usuario) {
    	StringBuilder sb = new StringBuilder("( SELECT ");
    	sb.append(" forumInteracao.*, ");
        sb.append(" UsuarioInteracao.nome AS \"usuarioInteracao.nome\", ");
        sb.append(" pessoaCriacao.codigo AS \"pessoaCriacao.codigo\", pessoaCriacao.nome AS \"pessoaCriacao.nome\", ");
        sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\",arquivo.cpfrequerimento AS \"arquivo.cpfrequerimento\", ");
        sb.append(" (select count(forumInteracaoGostado.*) from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = forumInteracao.codigo  ) as qtdeGostado, ");
        sb.append(" (select case when count(forumInteracaoGostado.*) > 0 then true else false end jaGostado from forumInteracaoGostado where forumInteracaoGostado.forumInteracao = forumInteracao.codigo and forumInteracaoGostado.usuarioGostou = ").append(usuario).append(" limit 1) as jaGostado,");
        sb.append(" case when strpos(nivelapresentacao, '.') = 0 then nivelapresentacao else substring(nivelapresentacao,0,strpos(nivelapresentacao, '.')) end as nivelApresentacaoPrincipal");
        sb.append(" FROM forumInteracao ");        
        sb.append(" inner join Usuario  as UsuarioInteracao on forumInteracao.usuarioInteracao = UsuarioInteracao.codigo ");
        sb.append(" left join Pessoa  as pessoaCriacao on pessoaCriacao.codigo = UsuarioInteracao.pessoa ");
        sb.append(" left join Arquivo on Arquivo.codigo = pessoaCriacao.arquivoImagem ");
        return sb.toString();
    }
}
