package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.academico.PendenciaTipoDocumentoTipoRequerimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PendenciaTipoDocumentoTipoRequerimentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RequerimentoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>RequerimentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MaterialRequerimentoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PendenciaTipoDocumentoTipoRequerimento extends ControleAcesso implements PendenciaTipoDocumentoTipoRequerimentoInterfaceFacade {

    protected static String idEntidade;

    public PendenciaTipoDocumentoTipoRequerimento() throws Exception {
        super();
        setIdEntidade("PendenciaTipoDocumentoTipoRequerimento");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPendenciasTipoDocumentoTipoRequerimento(Integer tipoRequerimento, List<PendenciaTipoDocumentoTipoRequerimentoVO> listaPendenciaTipoDocumentoTipoRequerimento, UsuarioVO usuario) throws Exception {
		Iterator<PendenciaTipoDocumentoTipoRequerimentoVO> e = listaPendenciaTipoDocumentoTipoRequerimento.iterator();
		while (e.hasNext()) {
			PendenciaTipoDocumentoTipoRequerimentoVO obj = (PendenciaTipoDocumentoTipoRequerimentoVO) e.next();
			obj.getTipoRequerimento().setCodigo(tipoRequerimento);

			incluir(obj, usuario);
		}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPendenciasTipoDocumentoTipoRequerimento(Integer tipoRequerimento, List<PendenciaTipoDocumentoTipoRequerimentoVO> listaPendenciaTipoDocumentoTipoRequerimento, UsuarioVO usuario) throws Exception {
    	excluirPendenciaTipoDocumentoTipoRequerimentoVOs(tipoRequerimento, listaPendenciaTipoDocumentoTipoRequerimento, usuario);
		for(PendenciaTipoDocumentoTipoRequerimentoVO obj:listaPendenciaTipoDocumentoTipoRequerimento){
			obj.getTipoRequerimento().setCodigo(tipoRequerimento);
			if(obj.getNovoObj()){
				incluir(obj, usuario);
			}
		}
    }
    
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPendenciaTipoDocumentoTipoRequerimentoVOs(Integer tipoRequerimento, List<PendenciaTipoDocumentoTipoRequerimentoVO> listaPendenciaTipoDocumentoTipoRequerimento,  UsuarioVO usuario) throws Exception {
		PendenciaTipoDocumentoTipoRequerimento.excluir(getIdEntidade());
		String sql = "DELETE FROM pendenciaTipoDocumentoTipoRequerimento WHERE (tipoRequerimento = ?) ";
				if(listaPendenciaTipoDocumentoTipoRequerimento != null) {
					sql+= "and codigo not in (0 ";
					for(PendenciaTipoDocumentoTipoRequerimentoVO obj:listaPendenciaTipoDocumentoTipoRequerimento){
						sql += ", "+obj.getCodigo();
					}
					sql += ")";
				} 
				sql += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { tipoRequerimento });
	}
	
    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MaterialRequerimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. 
     * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialRequerimentoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *            Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PendenciaTipoDocumentoTipoRequerimentoVO obj, UsuarioVO usuario) throws Exception {
        try {
            /**
    		 * @author Ana Claudia
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */

            final StringBuilder sql = new StringBuilder("");
            sql.append(" INSERT INTO pendenciaTipoDocumentoTipoRequerimento  (tipoDocumento, tipoRequerimento) ");
            sql.append(" VALUES ( ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(cont++, obj.getTipoDocumento().getCodigo());
                    sqlInserir.setInt(cont++, obj.getTipoRequerimento().getCodigo());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(false);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(false);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    public List<PendenciaTipoDocumentoTipoRequerimentoVO> consultarPorTipoDocumentoTipoRequerimento(Integer tipoRequerimento,  Integer nivelMontarDados, UsuarioVO usuario) throws Exception{
    	StringBuilder sqlStr = new StringBuilder("SELECT * from pendenciaTipoDocumentoTipoRequerimento ");
		sqlStr.append(" WHERE tipoRequerimento = ").append(tipoRequerimento);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
      
		List<PendenciaTipoDocumentoTipoRequerimentoVO> vetResultado = new ArrayList<PendenciaTipoDocumentoTipoRequerimentoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
		
    }
    
    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados 
     * (<code>ResultSet</code>) em um objeto da classe <code>MaterialRequerimentoVO</code>.
     *
     * @return O objeto da classe <code>MaterialRequerimentoVO</code> com os dados devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static PendenciaTipoDocumentoTipoRequerimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
    	PendenciaTipoDocumentoTipoRequerimentoVO obj = new PendenciaTipoDocumentoTipoRequerimentoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getTipoDocumento().setCodigo(dadosSQL.getInt("tipoDocumento"));
        obj.getTipoRequerimento().setCodigo(dadosSQL.getInt("tipoRequerimento"));    
        obj.setNovoObj(false);
        return obj;
    }

    
    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PendenciaTipoDocumentoTipoRequerimento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
    	PendenciaTipoDocumentoTipoRequerimento.idEntidade = idEntidade;
    }
        
}
