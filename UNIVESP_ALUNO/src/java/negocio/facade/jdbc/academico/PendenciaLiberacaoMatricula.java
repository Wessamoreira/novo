package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PendenciaLiberacaoMatriculaVO;
import negocio.comuns.academico.enumeradores.MotivoSolicitacaoLiberacaoMatriculaEnum;
import negocio.comuns.academico.enumeradores.SituacaoPendenciaLiberacaoMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.PendenciaLiberacaoMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>InteracaoRequerimentoHistoricoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>InteracaoRequerimentoHistoricoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PendenciaLiberacaoMatriculaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PendenciaLiberacaoMatricula extends ControleAcesso implements PendenciaLiberacaoMatriculaInterfaceFacade {

    protected static String idEntidade;

    public PendenciaLiberacaoMatricula() throws Exception {
        super();
        setIdEntidade("PendenciaLiberacaoMatricula");
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
    public void persistir(final PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
        	incluir(pendenciaLiberacaoMatriculaVO, controlarAcesso, usuario);
        	//atualizarNivelApresentacao(interacaoRequerimentoHistoricoVO, usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	 try {
             //validarDados(pendenciaLiberacaoMatriculaVO);
             final StringBuilder sql = new StringBuilder("INSERT INTO pendenciaLiberacaoMatricula ");
             sql.append(" ( matricula, dataSolicitacao, usuarioSolicitacao, motivoSolicitacao, situacao, motivoIndeferimento, dataIndeferimento, usuarioIndeferimento, dataDeferimento, usuarioDeferimento ) ");
             sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
             pendenciaLiberacaoMatriculaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getMatricula().getMatricula(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getDataSolicitacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getCodigo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getMotivoSolicitacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getSituacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getMotivoIndeferimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getDataIndeferimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getUsuarioIndeferimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getDataDeferimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(pendenciaLiberacaoMatriculaVO.getUsuarioDeferimento(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						pendenciaLiberacaoMatriculaVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
         } catch (Exception e) {
        	 pendenciaLiberacaoMatriculaVO.setNovoObj(true);
        	 pendenciaLiberacaoMatriculaVO.setCodigo(0);
             throw e;
         }
    }
    
    public PendenciaLiberacaoMatriculaVO consultarPendenciaLiberacaoMatriculaPendentePorMatriculaEMotivo(String matricula, MotivoSolicitacaoLiberacaoMatriculaEnum motivoSolicitacaoMatriculaEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT ");
    	sb.append(" pendenciaLiberacaoMatricula.*, usuarioSolicitacao.nome as \"usuarioSolicitacao.nome\", pessoaSolicitacao.codigo as \"pessoaSolicitacao.codigo\" ");
        sb.append(" FROM pendenciaLiberacaoMatricula ");    
        sb.append(" INNER JOIN usuario as usuarioSolicitacao ON pendenciaLiberacaoMatricula.usuarioSolicitacao=usuarioSolicitacao.codigo");
        sb.append(" LEFT JOIN pessoa as pessoaSolicitacao ON usuarioSolicitacao.pessoa=pessoaSolicitacao.codigo ");
        sb.append(" WHERE matricula = '").append(matricula).append("'");   
        sb.append("AND motivoSolicitacao = '").append(motivoSolicitacaoMatriculaEnum).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) { 
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<PendenciaLiberacaoMatriculaVO> consultarPorMatricula(String matricula) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT ");
    	sb.append(" pendenciaLiberacaoMatricula.*, usuarioSolicitacao.nome as \"usuarioSolicitacao.nome\" ");
        sb.append(" FROM pendenciaLiberacaoMatricula ");   
        sb.append("INNER JOIN usuario as usuarioSolicitacao ON pendenciaLiberacaoMatricula.usuarioSolicitacao=usuarioSolicitacao.codigo");
        sb.append(" WHERE matricula = '").append(matricula).append("'");    
        sb.append(" AND situacao = 'PENDENTE'");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }
    
    public PendenciaLiberacaoMatriculaVO consultarOutraPendenciaExistente(String matricula, Integer codigoPendenciaLiberacaoMatricula, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT ");
    	sb.append(" pendenciaLiberacaoMatricula.*, usuarioSolicitacao.nome as \"usuarioSolicitacao.nome\" ");
        sb.append(" FROM pendenciaLiberacaoMatricula ");   
        sb.append("INNER JOIN usuario as usuarioSolicitacao ON pendenciaLiberacaoMatricula.usuarioSolicitacao=usuarioSolicitacao.codigo");
        sb.append(" WHERE matricula = '").append(matricula).append("'"); 
        sb.append(" AND pendenciaLiberacaoMatricula.codigo <>").append(codigoPendenciaLiberacaoMatricula);
        sb.append(" AND situacao = 'PENDENTE'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) { 
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return (montarDadosBasicos(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
    }
    
    public Boolean verificarSeOutraPendenciaExistente(String matricula, Integer codigoPendenciaLiberacaoMatricula) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT ");
    	sb.append(" pendenciaLiberacaoMatricula.codigo");
        sb.append(" FROM pendenciaLiberacaoMatricula ");  
        sb.append(" WHERE matricula = '").append(matricula).append("'"); 
        sb.append(" AND pendenciaLiberacaoMatricula.codigo <>").append(codigoPendenciaLiberacaoMatricula);
        sb.append(" AND situacao = 'PENDENTE'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) { 
			return false;
		}
		return true;
    }
       
    private List<PendenciaLiberacaoMatriculaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception{
        List<PendenciaLiberacaoMatriculaVO> pendenciaLiberacaoMatriculaVOs = new ArrayList<PendenciaLiberacaoMatriculaVO>(0);
        while(rs.next()){
        	pendenciaLiberacaoMatriculaVOs.add(montarDadosBasicos(rs, nivelMontarDados));
        }
        return  pendenciaLiberacaoMatriculaVOs;
    }
    
    private PendenciaLiberacaoMatriculaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception{
    	PendenciaLiberacaoMatriculaVO obj = new PendenciaLiberacaoMatriculaVO();
        obj.setCodigo(rs.getInt("codigo"));
        obj.getMatricula().setMatricula(rs.getString("matricula"));
        obj.setMotivoSolicitacao(MotivoSolicitacaoLiberacaoMatriculaEnum.valueOf(rs.getString("motivoSolicitacao")));
        obj.setDataSolicitacao(rs.getDate("dataSolicitacao"));
        obj.getUsuarioSolicitacao().setCodigo(rs.getInt("usuarioSolicitacao"));
        if(Uteis.isAtributoPreenchido(rs.getString("usuarioSolicitacao.nome"))) {
        	obj.getUsuarioSolicitacao().setNome(rs.getString("usuarioSolicitacao.nome"));
        }
        if(Uteis.isAtributoPreenchido(rs.getInt("pessoaSolicitacao.codigo"))) {
        	obj.getUsuarioSolicitacao().getPessoa().setCodigo(rs.getInt("pessoaSolicitacao.codigo"));
        }
        montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }  
    
    private PendenciaLiberacaoMatriculaVO montarDadosBasicos(SqlRowSet rs, int nivelMontarDados) throws Exception{
    	PendenciaLiberacaoMatriculaVO obj = new PendenciaLiberacaoMatriculaVO();
        obj.setCodigo(rs.getInt("codigo"));
        obj.getMatricula().setMatricula(rs.getString("matricula"));
        obj.setMotivoSolicitacao(MotivoSolicitacaoLiberacaoMatriculaEnum.valueOf(rs.getString("motivoSolicitacao")));
        obj.setDataSolicitacao(rs.getDate("dataSolicitacao"));
        obj.getUsuarioSolicitacao().setCodigo(rs.getInt("usuarioSolicitacao"));
        obj.setSituacao(SituacaoPendenciaLiberacaoMatriculaEnum.valueOf(rs.getString("situacao")));
        if(Uteis.isAtributoPreenchido(rs.getString("usuarioSolicitacao.nome"))) {
        	obj.getUsuarioSolicitacao().setNome(rs.getString("usuarioSolicitacao.nome"));
        }
        return obj;
    } 
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void indeferirLiberacaoPendenciaMatricula(PendenciaLiberacaoMatriculaVO obj, CancelamentoVO cancelamentoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		
		StringBuilder sql = new StringBuilder("UPDATE pendenciaLiberacaoMatricula  set situacao = '").append(SituacaoPendenciaLiberacaoMatriculaEnum.INDEFERIDO).append("'");
		sql.append(", motivoIndeferimento  = '").append(obj.getMotivoIndeferimento()).append("'");
		sql.append(", dataIndeferimento  = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("'");
		sql.append(", usuarioIndeferimento  = ").append(usuario.getCodigo());
		sql.append(" WHERE codigo = ").append(obj.getCodigo());
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());			
		
		
		getFacadeFactory().getCancelamentoFacade().incluir(cancelamentoVO, configuracaoGeralSistemaVO, usuario, false);
		
		if(verificarSeOutraPendenciaExistente(obj.getMatricula().getMatricula(), obj.getCodigo())){
			PendenciaLiberacaoMatriculaVO outraPendenciaLiberacaoMatricula = consultarOutraPendenciaExistente(obj.getMatricula().getMatricula(), obj.getCodigo(), usuario);
			if(Uteis.isAtributoPreenchido(outraPendenciaLiberacaoMatricula)){
				StringBuilder sb = new StringBuilder("UPDATE pendenciaLiberacaoMatricula  set situacao = '").append(SituacaoPendenciaLiberacaoMatriculaEnum.INDEFERIDO).append("'");
				if(obj.getMotivoSolicitacao().equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA)) {
					sb.append(", motivoIndeferimento  = 'Indeferido pelo indeferimento da solicitação de aprovação de liberação financeira'");
				}
				else {
					sb.append(", motivoIndeferimento  = 'Indeferido pelo indeferimento da solicitacao de aprovação de liberação da matrícula após X módulos'");
				}
				sb.append(", dataIndeferimento  = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("'");
				sb.append(", usuarioIndeferimento  = ").append(usuario.getCodigo());
				sb.append(" WHERE codigo = ").append(obj.getCodigo());
				sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				getConexao().getJdbcTemplate().update(sql.toString());		
			}
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deferirLiberacaoPendenciaMatricula(PendenciaLiberacaoMatriculaVO obj, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception{
			
		StringBuilder sql = new StringBuilder("UPDATE pendenciaLiberacaoMatricula  set situacao = '").append(SituacaoPendenciaLiberacaoMatriculaEnum.DEFERIDO).append("'");
		sql.append(", dataDeferimento  = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("'");
		sql.append(", usuarioDeferimento  = ").append(usuario.getCodigo());
		sql.append(" WHERE codigo = ").append(obj.getCodigo());
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());		
		
		if(!verificarSeOutraPendenciaExistente(obj.getMatricula().getMatricula(), obj.getCodigo())){
			matriculaVO.setMatriculaSuspensa(Boolean.FALSE);
			if(obj.getMotivoSolicitacao().equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS)) {				
				matriculaVO.setMotivoCancelamentoSuspensaoMatricula("Liberação da Matrícula após X módulos");
			}else {
				matriculaVO.setMotivoCancelamentoSuspensaoMatricula("Liberação da Matrícula com débitos finaceiros");
			}
			matriculaVO.setDataCancelamentoSuspensaoMatricula(new Date());
			matriculaVO.setResponsavelCancelamentoSuspensaoMatricula(usuario);
			matriculaVO.setBloqueioPorSolicitacaoLiberacaoMatricula(Boolean.FALSE);	

			getFacadeFactory().getMatriculaFacade().alterarMatriculaSuspensao(matriculaVO);
			
			getFacadeFactory().getMatriculaPeriodoFacade().alterar(matriculaPeriodoVO, matriculaVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), usuario);
		}
		
	}
	
	public  void montarDadosMatricula(PendenciaLiberacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.BASICO, usuario));
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoLiberacaoPendenciaMatricula(String matricula, UsuarioVO usuario) throws Exception {
		
		StringBuilder sql = new StringBuilder("UPDATE pendenciaLiberacaoMatricula  set situacao = '").append(SituacaoPendenciaLiberacaoMatriculaEnum.PENDENTE).append("'");
		sql.append(", motivoIndeferimento  = null");
		sql.append(", dataIndeferimento  = null");
		sql.append(", usuarioIndeferimento  = null");
		sql.append(" WHERE matricula = '").append(matricula).append("'");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}
    
    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PendenciaLiberacaoMatricula.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
    	PendenciaLiberacaoMatricula.idEntidade = idEntidade;
    }
}
