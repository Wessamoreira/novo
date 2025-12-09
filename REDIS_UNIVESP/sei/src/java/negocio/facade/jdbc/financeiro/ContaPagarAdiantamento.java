package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarAdiantamentoInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy 
public class ContaPagarAdiantamento extends ControleAcesso implements ContaPagarAdiantamentoInterfaceFacade{

    /**
	 * 
	 */
	private static final long serialVersionUID = -9143769833169720714L;
	protected static String idEntidade;

    public ContaPagarAdiantamento() throws Exception {
        setIdEntidade("ContaPagar");
    }

    

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaPagarAdiantamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaPagarAdiantamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaPagarAdiantamentoVO obj, UsuarioVO usuario) throws Exception {
        ContaPagarAdiantamentoVO.validarDados(obj);
        final String sql = "INSERT INTO ContaPagarAdiantamento( dataUsoAdiantamento, valorUtilizado, percenutalContaPagarUtilizada, contaPagar, contaPagarUtilizada, responsavel "
                + ") VALUES (?, ?, ?, ? ,?, ?) returning codigo "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer)getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                int i = 1;
                sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataUsoAdiantamento()));
                sqlInserir.setDouble(i++, obj.getValorUtilizado().doubleValue());
                sqlInserir.setDouble(i++, obj.getPercenutalContaPagarUtilizada().doubleValue());
                if (obj.getContaPagar().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(i++, obj.getContaPagar().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(i++, 0);
                }                
                if (obj.getContaPagarUtilizada().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(i++, obj.getContaPagarUtilizada().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(i++, 0);
                }
                if (obj.getResponsavel().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(i++, obj.getResponsavel().getCodigo().intValue());
                } else {
                	obj.setResponsavel(usuario);
                	sqlInserir.setInt(i++, usuario.getCodigo());
                }                
                return sqlInserir;
            }
        }, new ResultSetExtractor<Object>() {
            public Object extractData(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaPagarAdiantamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaPagarAdiantamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaPagarAdiantamentoVO obj, UsuarioVO usuario) throws Exception {
        ContaPagarAdiantamentoVO.validarDados(obj);
        final String sql = "UPDATE ContaPagarAdiantamento set "
                + " dataUsoAdiantamento=?, valorUtilizado=?, percenutalContaPagarUtilizada=?, contaPagar=?, contaPagarUtilizada=?, responsavel=?  "
                + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
                int i = 1;
                sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataUsoAdiantamento()));
                sqlAlterar.setDouble(i++, obj.getValorUtilizado().doubleValue());
                sqlAlterar.setDouble(i++, obj.getPercenutalContaPagarUtilizada().doubleValue());
                if (obj.getContaPagar().getCodigo().intValue() != 0) {
                	sqlAlterar.setInt(i++, obj.getContaPagar().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(i++, 0);
                }                                
                if (obj.getContaPagarUtilizada().getCodigo().intValue() != 0) {
                	sqlAlterar.setInt(i++, obj.getContaPagarUtilizada().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(i++, 0);
                }
                if (obj.getResponsavel().getCodigo().intValue() != 0) {
                	sqlAlterar.setInt(i++, obj.getResponsavel().getCodigo().intValue());
                } else {
                	obj.setResponsavel(usuario);
                	sqlAlterar.setInt(i++, usuario.getCodigo());
                }                                
                sqlAlterar.setInt(i++, obj.getCodigo().intValue());
                return sqlAlterar;
			}
		}) == 0) {
			incluir(obj,usuario);
		}
        
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaPagarAdiantamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaPagarAdiantamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaPagarAdiantamentoVO obj, UsuarioVO usuario) throws Exception {
        //  ContaPagarAdiantamento.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaPagarAdiantamento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<ContaPagarAdiantamentoVO> consultarContaPagarUtilizadaPorCodigoContaPagar(Integer valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select contapagarAdiantamento.*,");
    	
    	sb.append(" contapagarUtilizada.dataVencimento as \"contapagarUtilizada.dataVencimento\", contapagarUtilizada.valorPago as \"contapagarUtilizada.valorPago\", ");
    	sb.append(" contapagarUtilizada.valorUtilizadoAdiantamento as \"contapagarUtilizada.valorUtilizadoAdiantamento\", contapagarUtilizada.numeroNotaFiscalEntrada as \"contapagarUtilizada.numeroNotaFiscalEntrada\", ");
    	sb.append(" contapagarUtilizada.observacao as \"contapagarUtilizada.observacao\", contapagarUtilizada.parcela as \"contapagarUtilizada.parcela\", ");
    	sb.append(" contapagarUtilizada.descontoPorUsoAdiantamento as \"contapagarUtilizada.descontoPorUsoAdiantamento\",  ");
    	sb.append(" contaPagarUtilizada.situacao as \"contaPagarUtilizada.situacao\",  ");
    	sb.append(" contapagar.dataVencimento as \"contapagar.dataVencimento\", contapagar.valorPago as \"contapagar.valorPago\", ");
    	sb.append(" contapagar.valorUtilizadoAdiantamento as \"contapagar.valorUtilizadoAdiantamento\", contapagar.numeroNotaFiscalEntrada as \"contapagar.numeroNotaFiscalEntrada\", ");
    	sb.append(" contapagar.observacao as \"contapagar.observacao\", contapagar.parcela as \"contapagar.parcela\", ");
    	sb.append(" contapagar.descontoPorUsoAdiantamento as \"contapagar.descontoPorUsoAdiantamento\"  ");
    	sb.append(" from contapagarAdiantamento ");
    	sb.append(" inner join contapagar on contapagar.codigo = contapagarAdiantamento.contapagar ");
    	sb.append(" inner join contapagar as contapagarUtilizada on contapagarUtilizada.codigo = contapagarAdiantamento.contaPagarUtilizada ");
    	sb.append(" where contapagarAdiantamento.contaPagar = ").append(valorConsulta.intValue());
    	sb.append(" ORDER BY contapagarAdiantamento.dataUsoAdiantamento desc");    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<ContaPagarAdiantamentoVO> consultarContaPagarPorCodigoContaPagarUtilizada(Integer valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select contapagarAdiantamento.*,");
    	
    	sb.append(" contapagarUtilizada.dataVencimento as \"contapagarUtilizada.dataVencimento\", contapagarUtilizada.valorPago as \"contapagarUtilizada.valorPago\", ");
    	sb.append(" contapagarUtilizada.valorUtilizadoAdiantamento as \"contapagarUtilizada.valorUtilizadoAdiantamento\", contapagarUtilizada.numeroNotaFiscalEntrada as \"contapagarUtilizada.numeroNotaFiscalEntrada\", ");
    	sb.append(" contapagarUtilizada.observacao as \"contapagarUtilizada.observacao\", contapagarUtilizada.parcela as \"contapagarUtilizada.parcela\", ");
    	sb.append(" contapagarUtilizada.descontoPorUsoAdiantamento as \"contapagarUtilizada.descontoPorUsoAdiantamento\",  ");
    	sb.append(" contapagarUtilizada.situacao as \"contapagarUtilizada.situacao\",  ");
    	
    	
    	sb.append(" contapagar.dataVencimento as \"contapagar.dataVencimento\", contapagar.valorPago as \"contapagar.valorPago\", ");
    	sb.append(" contapagar.valorUtilizadoAdiantamento as \"contapagar.valorUtilizadoAdiantamento\", contapagar.numeroNotaFiscalEntrada as \"contapagar.numeroNotaFiscalEntrada\", ");
    	sb.append(" contapagar.observacao as \"contapagar.observacao\", contapagar.parcela as \"contapagar.parcela\", ");
    	sb.append(" contapagar.descontoPorUsoAdiantamento as \"contapagar.descontoPorUsoAdiantamento\"  ");
    	
    	sb.append(" from contapagarAdiantamento ");
    	sb.append(" inner join contapagar on contapagar.codigo = contapagarAdiantamento.contapagar ");
    	sb.append(" inner join contapagar as contapagarUtilizada on contapagarUtilizada.codigo = contapagarAdiantamento.contaPagarUtilizada ");
    	sb.append(" where contapagarAdiantamento.contapagarutilizada = ").append(valorConsulta.intValue());
    	sb.append(" ORDER BY contapagar.dataVencimento ");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    public static List<ContaPagarAdiantamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<ContaPagarAdiantamentoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public static ContaPagarAdiantamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ContaPagarAdiantamentoVO obj = new ContaPagarAdiantamentoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setDataUsoAdiantamento(dadosSQL.getTimestamp("dataUsoAdiantamento"));
        obj.setValorUtilizado(dadosSQL.getDouble("valorUtilizado"));
        obj.setPercenutalContaPagarUtilizada(dadosSQL.getDouble("percenutalContaPagarUtilizada"));
        obj.getContaPagar().setCodigo((dadosSQL.getInt("contaPagar")));
        obj.getContaPagarUtilizada().setCodigo((dadosSQL.getInt("contaPagarUtilizada")));
        
        obj.getContaPagarUtilizada().setSituacao((dadosSQL.getString("contaPagarUtilizada.situacao")));
        obj.getContaPagarUtilizada().setDataVencimento(dadosSQL.getTimestamp("contapagarUtilizada.dataVencimento"));
        obj.getContaPagarUtilizada().setValorPago(dadosSQL.getDouble("contapagarUtilizada.valorPago"));
        obj.getContaPagarUtilizada().setValorUtilizadoAdiantamento(dadosSQL.getDouble("contapagarUtilizada.valorUtilizadoAdiantamento"));
        obj.getContaPagarUtilizada().setDescontoPorUsoAdiantamento(dadosSQL.getDouble("contapagarUtilizada.descontoPorUsoAdiantamento"));
        obj.getContaPagarUtilizada().setObservacao(dadosSQL.getString("contapagarUtilizada.observacao"));
        obj.getContaPagarUtilizada().setParcela(dadosSQL.getString("contapagarUtilizada.parcela"));
        obj.getContaPagarUtilizada().setNumeroNotaFiscalEntrada(dadosSQL.getString("contapagarUtilizada.numeroNotaFiscalEntrada"));
        
        obj.getContaPagar().setDataVencimento(dadosSQL.getTimestamp("contapagar.dataVencimento"));
        obj.getContaPagar().setValorPago(dadosSQL.getDouble("contapagar.valorPago"));
        obj.getContaPagar().setValorUtilizadoAdiantamento(dadosSQL.getDouble("contapagar.valorUtilizadoAdiantamento"));
        obj.getContaPagar().setDescontoPorUsoAdiantamento(dadosSQL.getDouble("contapagar.descontoPorUsoAdiantamento"));
        obj.getContaPagar().setObservacao(dadosSQL.getString("contapagar.observacao"));
        obj.getContaPagar().setParcela(dadosSQL.getString("contapagar.parcela"));
        obj.getContaPagar().setNumeroNotaFiscalEntrada(dadosSQL.getString("contapagar.numeroNotaFiscalEntrada"));
        
        obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public static void montarDadosResponsavel(ContaPagarAdiantamentoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel() == null || obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }
  
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContaPagarAdiantamentos(Integer contaPagar, UsuarioVO usuario) throws Exception {
    	String sql = "DELETE FROM ContaPagarAdiantamento WHERE (contaPagar = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    	getConexao().getJdbcTemplate().update(sql, new Object[]{contaPagar});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaPagarAdiantamentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaPagarAdiantamentos</code> e <code>incluirContaPagarAdiantamentos</code> disponíveis na classe <code>ContaPagarAdiantamento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarContaPagarAdiantamentos(ContaPagarVO contaPagar, List<ContaPagarAdiantamentoVO> objetos, UsuarioVO usuario) throws Exception {
        for (ContaPagarAdiantamentoVO objeto : objetos) {
        	if (objeto.getCodigo().equals(0)) {
                objeto.setContaPagar(contaPagar);
                incluir(objeto, usuario);
            }
		}
    }

    /**
     * Operação responsável por incluir objetos da <code>ContaPagarAdiantamentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ContaPagar</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirContaPagarAdiantamentos(ContaPagarVO contaPagarPrm, List<ContaPagarAdiantamentoVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<ContaPagarAdiantamentoVO> e = objetos.iterator();
        while (e.hasNext()) {
            ContaPagarAdiantamentoVO obj = e.next();
            obj.setContaPagar(contaPagarPrm);
            persistirDados(obj, usuario);
        }
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirDados(ContaPagarAdiantamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
	}


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContaPagarAdiantamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContaPagarAdiantamento.idEntidade = idEntidade;
    }
}
