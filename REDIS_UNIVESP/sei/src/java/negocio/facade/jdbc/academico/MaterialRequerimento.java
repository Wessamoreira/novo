package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MaterialRequerimentoInterfaceFacade;

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
public class MaterialRequerimento extends ControleAcesso implements MaterialRequerimentoInterfaceFacade {

    protected static String idEntidade;

    public MaterialRequerimento() throws Exception {
        super();
        setIdEntidade("MaterialRequerimento");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirMaterialRequerimentos(Integer RequerimentoPrm, List<MaterialRequerimentoVO> objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		for (MaterialRequerimentoVO materialRequerimentoVO : objetos) {
			materialRequerimentoVO.setRequerimento(RequerimentoPrm);
			if (!Uteis.isAtributoPreenchido(materialRequerimentoVO) && materialRequerimentoVO != null) {
				incluir(materialRequerimentoVO, usuario, configuracaoGeralSistema);
			}
		}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarMaterialRequerimentos(Integer Requerimento, List<MaterialRequerimentoVO> objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        incluirMaterialRequerimentos(Requerimento, objetos, usuario, configuracaoGeralSistema);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirMaterialRequerimentos(List<MaterialRequerimentoVO> materialRequerimentoVOs, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
    	for (MaterialRequerimentoVO materialRequerimentoVO : materialRequerimentoVOs) {
    		excluir(materialRequerimentoVO, usuario, configuracaoGeralSistema);
    	}
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
    public void incluir(final MaterialRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialRequerimento.incluir(getIdEntidade());
            if (obj.getArquivoVO().getPastaBaseArquivoEnum() != null) {
                getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
            }
            if(!Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo())) {
            	throw new Exception("Houve uma falha ao realizar o upload do(s) arquivo(s). Por favor, realize o upload novamente.");
            }
            final StringBuilder sql = new StringBuilder("");
            sql.append(" INSERT INTO MaterialRequerimento ( descricao, Requerimento, arquivo, disponibilizarParaRequerente,  usuarioDisponibilizouArquivo, dataDisponibilizacaoArquivo, requerimentoHistorico) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(cont++, obj.getDescricao());
                    sqlInserir.setInt(cont++, obj.getRequerimento());
                    sqlInserir.setInt(cont++, obj.getArquivoVO().getCodigo());
                    sqlInserir.setBoolean(cont++, obj.getDisponibilizarParaRequerente());
                    sqlInserir.setInt(cont++, obj.getUsuarioDisponibilizouArquivo().getCodigo());
                    sqlInserir.setTimestamp(cont++, Uteis.getDataJDBCTimestamp(obj.getDataDisponibilizacaoArquivo()));
                    if(obj.getRequerimentoHistorico().getCodigo().intValue() != 0) {
                    	sqlInserir.setInt(cont++, obj.getRequerimentoHistorico().getCodigo());
                    }
                    else {
                    	sqlInserir.setNull(cont++, 0);
                    }
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

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MaterialRequerimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialRequerimentoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MaterialRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialRequerimento.alterar(getIdEntidade());
            if (obj.getArquivoVO().getPastaBaseArquivoEnum() != null) {
                if (obj.getArquivoVO().getCodigo() == 0) {
                    getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                } else {
                    getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                }
            }
            final StringBuilder sql = new StringBuilder("");
            sql.append(" UPDATE MaterialRequerimento SET disponibilizarParaRequerente=?, descricao=?, Requerimento=?, arquivo=? ");
            sql.append(" WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setBoolean(cont++, obj.getDisponibilizarParaRequerente());
                    sqlAlterar.setString(cont++, obj.getDescricao());
                    sqlAlterar.setInt(cont++, obj.getRequerimento());
                    sqlAlterar.setInt(cont++, obj.getArquivoVO().getCodigo());
                    sqlAlterar.setInt(cont++, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MaterialRequerimentoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialRequerimentoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MaterialRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
        	/**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialRequerimento.excluir(getIdEntidade());
            String sql = "DELETE FROM MaterialRequerimento WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            if (Uteis.isAtributoPreenchido(obj.getArquivoVO())) {
            	getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RequerimentoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public MaterialRequerimentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
        String sql = "SELECT * FROM MaterialRequerimento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>MaterialRequerimentoVO</code> através do valor do atributo <code>codigo</code> da classe <code>RequerimentoVO</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>MaterialRequerimentoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorRequerimento(Integer codigoRequerimento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM MaterialRequerimento WHERE Requerimento = ? ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoRequerimento});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). 
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>MaterialRequerimentoVO</code> resultantes da consulta.
     */
    public static List<MaterialRequerimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MaterialRequerimentoVO> vetResultado = new ArrayList<MaterialRequerimentoVO>(0);
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
    public static MaterialRequerimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MaterialRequerimentoVO obj = new MaterialRequerimentoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setRequerimento(dadosSQL.getInt("Requerimento"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
        obj.setDisponibilizarParaRequerente(dadosSQL.getBoolean("disponibilizarParaRequerente"));
        obj.getUsuarioDisponibilizouArquivo().setCodigo(dadosSQL.getInt("usuarioDisponibilizouArquivo"));
        obj.setDataDisponibilizacaoArquivo(dadosSQL.getTimestamp("dataDisponibilizacaoArquivo"));
        obj.getRequerimentoHistorico().setCodigo(dadosSQL.getInt("requerimentoHistorico"));
        montarDadosArquivo(obj);
        if(obj.getUsuarioDisponibilizouArquivo().getCodigo() != null && obj.getUsuarioDisponibilizouArquivo().getCodigo() > 0) {
        	montarDadosUsuarioDisponibilizouArquivo(obj);
        }        
        obj.setNovoObj(false);
        return obj;
    }

    public static void montarDadosArquivo(MaterialRequerimentoVO obj) throws Exception {
        if (obj.getArquivoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(),Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
    }
    
    public static void montarDadosUsuarioDisponibilizouArquivo(MaterialRequerimentoVO obj) throws Exception {
        if (obj.getArquivoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setUsuarioDisponibilizouArquivo(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioDisponibilizouArquivo().getCodigo(),Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
    }

    public void validarDados(MaterialRequerimentoVO obj) throws Exception {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("Campo Descrição (MaterialRequerimento) deve ser informado.");
        }
        if (obj.getRequerimento() <= 0) {
            throw new ConsistirException("Código Requerimento (MaterialRequerimento) deve ser informado.");
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDescricaoMaterialRequerimento(MaterialRequerimentoVO obj, UsuarioVO usuario) throws Exception {
        try {
  
            final StringBuilder sql = new StringBuilder("");
            sql.append(" UPDATE MaterialRequerimento SET descricao=? ");
            sql.append(" WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setString(cont++, obj.getDescricao());
                    sqlAlterar.setInt(cont++, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisponibilizarParaRequerenteMaterialRequerimento(MaterialRequerimentoVO obj, UsuarioVO usuario) throws Exception {
        try {
  
            final StringBuilder sql = new StringBuilder("");
            sql.append(" UPDATE MaterialRequerimento SET disponibilizarParaRequerente=? ");
            sql.append(" WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setBoolean(cont++, obj.getDisponibilizarParaRequerente());
                    sqlAlterar.setInt(cont++, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Responsável por realizar uma consulta de <code>MaterialRequerimentoVO</code> através do valor do atributo <code>requerimentoHistorico</code> da classe <code>RequerimentoVO</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>MaterialRequerimentoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorRequerimentoHistorico(Integer codigoRequerimentoHistorico, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM MaterialRequerimento WHERE requerimentoHistorico = ? ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoRequerimentoHistorico});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MaterialRequerimento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        MaterialRequerimento.idEntidade = idEntidade;
    }
    
	public void permitirExcluirArquivoMaterialRequerimento(List<MaterialRequerimentoVO> materialRequerimentoVOs,UsuarioVO usuarioLogado) throws Exception{
		materialRequerimentoVOs.stream().forEach(material -> permitirExlcuirArquivoMaterialRequerimento(material, usuarioLogado));
	}
	
	private void permitirExlcuirArquivoMaterialRequerimento(MaterialRequerimentoVO material,UsuarioVO usuarioLogado) {
		if(material.getUsuarioDisponibilizouArquivo().getCodigo().equals(usuarioLogado.getCodigo())) {
			material.setPermitirExcluirArquivoMaterial(true);
		}else {
			material.setPermitirExcluirArquivoMaterial(false);
		}
	}
    
    
}
