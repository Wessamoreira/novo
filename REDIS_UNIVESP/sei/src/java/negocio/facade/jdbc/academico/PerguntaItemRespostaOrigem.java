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

import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PerguntaItemRespostaOrigemInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerguntaItemRespostaOrigemVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerguntaItemRespostaOrigemVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PerguntaItemRespostaOrigemVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class PerguntaItemRespostaOrigem extends ControleAcesso implements PerguntaItemRespostaOrigemInterfaceFacade {

	 protected static String idEntidade;

	    public PerguntaItemRespostaOrigem() throws Exception {
	        super();
	        setIdEntidade("PerguntaItemRespostaOrigem");
	    }

	    /**
	     * Operação responsável por retornar um novo objeto da classe <code>PerguntaVO</code>.
	     */
	    public PerguntaItemRespostaOrigemVO novo() throws Exception {
	    	PerguntaItemRespostaOrigem.incluir(getIdEntidade());
	    	PerguntaItemRespostaOrigemVO obj = new PerguntaItemRespostaOrigemVO();
	        return obj;
	    }
	    
	    /**
	     * Operação responsável por incluir no banco de dados um objeto da classe <code>PerguntaItemRespostaOrigemVO</code>.
	     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	     * para realizar esta operacão na entidade.
	     * Isto, através da operação <code>incluir</code> da superclasse.
	     * @param obj  Objeto da classe <code>PerguntaItemRespostaOrigem</code> que será gravado no banco de dados.
	     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	     */
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void incluir(final PerguntaItemRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
	        try {        	
	            //PerguntaVO.validarDados(obj);
	        	//PerguntaRespostaOrigem.incluir(getIdEntidade(), usuario);
	        	incluir(obj, "perguntaItemRespostaOrigem", new AtributoPersistencia()
						.add("perguntaRespostaOrigemPrincipal", obj.getPerguntaRespostaOrigemPrincipalVO())
						.add("perguntaRespostaOrigem", obj.getPerguntaRespostaOrigemVO())
						.add("perguntaItem", obj.getPerguntaItemVO())
						.add("ordem", obj.getOrdem())
						, usuario);
	            obj.setNovoObj(Boolean.FALSE);
	        } catch (Exception e) {
	            obj.setNovoObj(Boolean.TRUE);
	            throw e;
	        }
	    }
	    
	    /**
	     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PerguntaItemRespostaOrigemVO</code>.
	     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	     * para realizar esta operacão na entidade.
	     * Isto, através da operação <code>alterar</code> da superclasse.
	     * @param obj    Objeto da classe <code>PerguntaItemRespostaOrigemVO</code> que será alterada no banco de dados.
	     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	     */
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void alterar(final PerguntaItemRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
	        try {
	           // PerguntaVO.validarDados(obj);
	        	//PerguntaRespostaOrigem.alterar(getIdEntidade(), usuario);
	        	alterar(obj, "perguntaItemRespostaOrigem", new AtributoPersistencia()
						.add("perguntaRespostaOrigemPrincipal", obj.getPerguntaRespostaOrigemPrincipalVO())
						.add("perguntaRespostaOrigem", obj.getPerguntaRespostaOrigemVO())
						.add("perguntaItem", obj.getPerguntaItemVO())
						.add("ordem", obj.getOrdem())
						,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario); 

	        } catch (Exception e) {
	            throw e;
	        }
	    }
	    
	    /**
	     * Operação reponsável por definir um novo valor para o identificador desta classe.
	     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	     */
	    public void setIdEntidade(String idEntidade) {
	    	PerguntaItemRespostaOrigem.idEntidade = idEntidade;
	    }
	    
		/**
		 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
		 */
		public static String getIdEntidade() {
			return PerguntaItemRespostaOrigem.idEntidade;
		}
		
		public List<PerguntaItemRespostaOrigemVO> consultarPorPerguntaRespostaOrigem(Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception{
			StringBuffer str = new StringBuffer();
			str.append(" select * from perguntaitemrespostaorigem ");
			str.append(" WHERE perguntaitemrespostaorigem.perguntarespostaorigemprincipal = ").append(codPerguntaRespostaOrigem);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		}
		
		public List<PerguntaItemRespostaOrigemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
			List<PerguntaItemRespostaOrigemVO> vetResultado = new ArrayList<PerguntaItemRespostaOrigemVO>(0);
			while (tabelaResultado.next()) {
				PerguntaItemRespostaOrigemVO obj = new PerguntaItemRespostaOrigemVO();
				montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
				vetResultado.add(obj);
			}
			return vetResultado;
		}
		
		private void montarDados(PerguntaItemRespostaOrigemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
			// Dados do PerguntaItemRespostaOrigemVO
			obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
			obj.getPerguntaItemVO().setCodigo(dadosSQL.getInt("perguntaitem"));
			obj.setOrdem(dadosSQL.getInt("ordem"));
			obj.getPerguntaRespostaOrigemVO().setCodigo(dadosSQL.getInt("perguntarespostaorigem"));
			
			obj.setPerguntaRespostaOrigemVO(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(obj.getPerguntaRespostaOrigemVO().getCodigo(), nivelMontarDados, usuario));
			obj.setPerguntaItemVO(getFacadeFactory().getPerguntaItemInterfaceFacade().consultarPorChavePrimaria(obj.getPerguntaItemVO().getCodigo(), nivelMontarDados, usuario));
							
		}					
		
		public void reorganizarOrdemPerguntaItemRespostaOrigem(List<List<PerguntaItemRespostaOrigemVO>> listaPerguntaItemRespostaOrigemAdicionadas, UsuarioVO usuario) throws Exception{
			Integer ordem = 1;
			for (List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigem : listaPerguntaItemRespostaOrigemAdicionadas) {
				for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigem : listaPerguntaItemRespostaOrigem) {
					if(!perguntaItemRespostaOrigem.getOrdem().equals(ordem)) {
						perguntaItemRespostaOrigem.setOrdem(ordem);
						alterarOrdemPerguntaIemRespostaOrigem(perguntaItemRespostaOrigem, usuario);
					}					
				}
				ordem++;
				
			}
		}
		
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void alterarOrdemPerguntaIemRespostaOrigem(final PerguntaItemRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
	        try {

	            final String sql = "UPDATE perguntaItemRespostaOrigem set ordem = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
	            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

	                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                    PreparedStatement sqlAlterar = con.prepareStatement(sql);               

	                    sqlAlterar.setInt(1, obj.getOrdem());                 
	                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
	                    return sqlAlterar;
	                }
	            });

	        } catch (Exception e) {
	            throw e;
	        }
	    }
	    
		public List<PerguntaItemRespostaOrigemVO> consultarPerguntaItemRespostaOrigemAexcluir(List<PerguntaItemRespostaOrigemVO> listPerguntaItemRespostaOrigemVO, Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception{
			StringBuffer str = new StringBuffer();
			str.append(" select * from perguntaitemrespostaorigem ");
			str.append(" WHERE perguntaRespostaOrigemPrincipal = ").append(codPerguntaRespostaOrigem);
					str.append(" and codigo not in(");
					boolean virgula = false;
					for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : listPerguntaItemRespostaOrigemVO) {
						if (!virgula) {
							str.append(perguntaItemRespostaOrigemVO.getCodigo());
						} else {
							str.append(", ").append(perguntaItemRespostaOrigemVO.getCodigo());
						}
						virgula = true;

					}
					str.append(")");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		}

}
