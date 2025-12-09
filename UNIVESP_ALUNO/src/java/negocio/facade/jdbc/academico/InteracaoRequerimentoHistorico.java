package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.InteracaoRequerimentoHistoricoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>InteracaoRequerimentoHistoricoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>InteracaoRequerimentoHistoricoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see InteracaoRequerimentoHistoricoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class InteracaoRequerimentoHistorico extends ControleAcesso implements InteracaoRequerimentoHistoricoInterfaceFacade {

    protected static String idEntidade;

    public InteracaoRequerimentoHistorico() throws Exception {
        super();
        setIdEntidade("InteracaoRequerimentoHistorico");
    }

    private void validarDados(InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoVO) throws ConsistirException {
        ConsistirException ex = new ConsistirException();
        if(interacaoRequerimentoHistoricoVO.getInteracao().trim().isEmpty() || Uteis.retiraTags(interacaoRequerimentoHistoricoVO.getInteracao()).trim().isEmpty()
                || interacaoRequerimentoHistoricoVO.getInteracao().equals("Envie uma nova interação")){
            ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ForumInteracao_interacao"));
        }
        if(!ex.getListaMensagemErro().isEmpty()){
            throw ex;
        }
    }
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(final InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
        	incluir(interacaoRequerimentoHistoricoVO, controlarAcesso, usuario);
        	atualizarNivelApresentacao(interacaoRequerimentoHistoricoVO, usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	 try {
             validarDados(interacaoRequerimentoHistoricoVO);
             final StringBuilder sql = new StringBuilder("INSERT INTO interacaoRequerimentoHistorico ");
             sql.append(" ( interacao, dataInteracao, usuarioInteracao, requerimentoHistorico, interacaoRequerimentoHistoricoPai ) ");
             sql.append(" VALUES ( ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
             interacaoRequerimentoHistoricoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                 public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                     int x = 1;
                     PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                     sqlInserir.setString(x++, interacaoRequerimentoHistoricoVO.getInteracao());
                     sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(interacaoRequerimentoHistoricoVO.getDataInteracao()));
                     sqlInserir.setInt(x++, interacaoRequerimentoHistoricoVO.getUsuarioInteracao().getCodigo());
                     sqlInserir.setInt(x++, interacaoRequerimentoHistoricoVO.getRequerimentoHistorico().getCodigo());
                     if (interacaoRequerimentoHistoricoVO.getInteracaoRequerimentoHistoricoPai().getCodigo().intValue() != 0) {
                    	 sqlInserir.setInt(x++, interacaoRequerimentoHistoricoVO.getInteracaoRequerimentoHistoricoPai().getCodigo());
                     } else {
  						sqlInserir.setNull(x++, 0);
  					 }
                     return sqlInserir;
                 }
             }, new ResultSetExtractor<Integer>() {

                 public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                     if (arg0.next()) {
                    	 interacaoRequerimentoHistoricoVO.setNovoObj(Boolean.FALSE);
                         return arg0.getInt("codigo");
                     }
                     return null;
                 }
             }));
         } catch (Exception e) {
        	 interacaoRequerimentoHistoricoVO.setNovoObj(true);
             throw e;
         }
    }
    
    public List<InteracaoRequerimentoHistoricoVO> consultarPorRequerimentoHistorico(Integer requerimentoHistorico, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT ");
    	sb.append(" interacaoRequerimentoHistorico.*, ");
        sb.append(" UsuarioInteracao.nome AS \"usuarioInteracao.nome\", UsuarioInteracao.tipoUsuario AS \"UsuarioInteracao.tipoUsuario\", ");
        sb.append(" pessoaCriacao.codigo AS \"pessoaCriacao.codigo\", pessoaCriacao.nome AS \"pessoaCriacao.nome\", pessoaCriacao.CPF AS \"pessoaCriacao.CPF\", ");
        sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\" ");
        sb.append(" FROM interacaoRequerimentoHistorico ");        
        sb.append(" inner join Usuario  as UsuarioInteracao on interacaoRequerimentoHistorico.usuarioInteracao = UsuarioInteracao.codigo ");
        sb.append(" left join Pessoa  as pessoaCriacao on pessoaCriacao.codigo = UsuarioInteracao.pessoa ");
        sb.append(" left join Arquivo on Arquivo.codigo = pessoaCriacao.arquivoImagem ");
        sb.append(" WHERE requerimentoHistorico = ").append(requerimentoHistorico);
        sb.append(" order by nivelApresentacao, dataInteracao ");        
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados);
    }
       
    private List<InteracaoRequerimentoHistoricoVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados){
        List<InteracaoRequerimentoHistoricoVO> interacaoRequerimentoHistoricoVOs = new ArrayList<InteracaoRequerimentoHistoricoVO>(0);
        while(rs.next()){
        	interacaoRequerimentoHistoricoVOs.add(montarDados(rs, nivelMontarDados));
        }
        return  interacaoRequerimentoHistoricoVOs;
    }
    
    private InteracaoRequerimentoHistoricoVO montarDados(SqlRowSet rs, int nivelMontarDados){
    	InteracaoRequerimentoHistoricoVO obj = new InteracaoRequerimentoHistoricoVO();
        obj.setCodigo(rs.getInt("codigo"));
        obj.getRequerimentoHistorico().setCodigo(rs.getInt("requerimentoHistorico"));
        obj.setDataInteracao(rs.getTimestamp("dataInteracao"));
        obj.setInteracao(rs.getString("interacao"));
        obj.getUsuarioInteracao().setCodigo(rs.getInt("usuarioInteracao"));
        obj.getUsuarioInteracao().setNome(rs.getString("usuarioInteracao.nome"));
        obj.getUsuarioInteracao().setTipoUsuario(rs.getString("usuarioInteracao.tipoUsuario"));
        obj.setExcluido(rs.getBoolean("excluido"));
        obj.setNivelApresentacao(rs.getString("nivelApresentacao"));
        obj.getInteracaoRequerimentoHistoricoPai().setCodigo(rs.getInt("interacaoRequerimentoHistoricoPai"));
        if (Uteis.isAtributoPreenchido(obj.getInteracaoRequerimentoHistoricoPai().getCodigo())) {
        	obj.setCodigoInteracaoRequerimentoHistoricoPai(obj.getInteracaoRequerimentoHistoricoPai().getCodigo());
        }
        obj.getUsuarioInteracao().getPessoa().setCodigo(rs.getInt("pessoaCriacao.codigo"));
        obj.getUsuarioInteracao().getPessoa().setNome(rs.getString("pessoaCriacao.nome"));
        obj.getUsuarioInteracao().getPessoa().setCPF(rs.getString("pessoaCriacao.CPF"));
        if(rs.getInt("arquivo.codigo") > 0){
	        obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setCodigo(rs.getInt("arquivo.codigo"));
	        obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setPastaBaseArquivo(rs.getString("arquivo.pastaBaseArquivo").replaceAll("\\\\", "/"));
	        obj.getUsuarioInteracao().getPessoa().getArquivoImagem().setNome(rs.getString("arquivo.nome"));
        }
        return obj;
    }
    
    @Override
    public Integer consultarTotalRegistroPorRequerimentoHistorico(Integer requerimentoHistorico) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT count(codigo) as qtde from interacaoRequerimentoHistorico ");
        sb.append(" WHERE requerimentoHistorico = ").append(requerimentoHistorico);               
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            return rs.getInt("qtde");
        }
        return 0;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarNivelApresentacao(InteracaoRequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception {        
    	StringBuilder sb = new StringBuilder("UPDATE interacaoRequerimentoHistorico set nivelApresentacao = '");
    	try {
    		if(Uteis.isAtributoPreenchido(obj.getInteracaoRequerimentoHistoricoPai().getNivelApresentacao())) {
    			int qtdNivel = StringUtils.countMatches(obj.getInteracaoRequerimentoHistoricoPai().getNivelApresentacao(), ".");
    			if(qtdNivel == 4){
        			String nivel = obj.getInteracaoRequerimentoHistoricoPai().getNivelApresentacao().substring(0, obj.getInteracaoRequerimentoHistoricoPai().getNivelApresentacao().lastIndexOf(".")+1);
        			sb.append(nivel).append(obj.getCodigo()).append("'");
        		}else{
        			sb.append(obj.getInteracaoRequerimentoHistoricoPai().getNivelApresentacao()).append(".").append(obj.getCodigo()).append("'");
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
    
    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return InteracaoRequerimentoHistorico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
    	InteracaoRequerimentoHistorico.idEntidade = idEntidade;
    }

	@Override
	public void alterarVisualizacaoInteracaoHistrico(RequerimentoVO requerimento, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		try {
			sql.append("update interacaoRequerimentoHistorico set interacaojalida = true where codigo in (select interacaoRequerimentoHistorico.codigo from interacaoRequerimentoHistorico ");
			sql.append("inner join requerimentohistorico on requerimentohistorico.codigo = interacaoRequerimentoHistorico.requerimentohistorico ");
			sql.append("inner join requerimento on requerimento.codigo = requerimentohistorico.requerimento ");
			sql.append("inner join usuario on usuario.codigo = interacaoRequerimentoHistorico.usuarioInteracao ");
				if(!requerimento.getPessoa().getCodigo().equals(usuarioLogado.getPessoa().getCodigo())) {
					sql.append( "and usuario.pessoa = requerimento.pessoa ");
				}else {
					sql.append(" and usuario.pessoa != requerimento.pessoa ");
				}
		sql.append(" and interacaojalida = false and requerimento.codigo = ").append(requerimento.getCodigo()).append(" )").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
    	getConexao().getJdbcTemplate().update(sql.toString());
		} finally {
			sql= null;
		}
	}
}
