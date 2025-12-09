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

import negocio.comuns.academico.MaterialProfessorVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MaterialProfessorInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProfessorVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ProfessorVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MaterialProfessorVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MaterialProfessor extends ControleAcesso implements MaterialProfessorInterfaceFacade {

    protected static String idEntidade;

    public MaterialProfessor() throws Exception {
        super();
        setIdEntidade("MaterialProfessor");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirMaterialProfessors(Integer localAulaPrm, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MaterialProfessorVO obj = (MaterialProfessorVO) e.next();
            obj.setLocalAula(localAulaPrm);
            incluir(obj, usuario, configuracaoGeralSistema);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarMaterialProfessors(Integer localAula, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        excluirMaterialProfessors(localAula, usuario, configuracaoGeralSistema);
        incluirMaterialProfessors(localAula, objetos, usuario, configuracaoGeralSistema);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirMaterialProfessors(Integer localAula, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        String sql = "DELETE FROM MaterialProfessor WHERE (localAula = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{localAula});
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MaterialProfessorVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. 
     * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialProfessorVO</code> que será gravado no banco de dados.
     * @exception Exception
     *            Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MaterialProfessorVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialProfessor.incluir(getIdEntidade());
            if (obj.getArquivoVO().getPastaBaseArquivoEnum() != null) {
                getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
            }
            final StringBuilder sql = new StringBuilder("");
            sql.append(" INSERT INTO MaterialProfessor ( descricao, localAula, arquivo ) ");
            sql.append(" VALUES ( ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(cont++, obj.getDescricao());
                    sqlInserir.setInt(cont++, obj.getLocalAula());
                    sqlInserir.setInt(cont++, obj.getArquivoVO().getCodigo());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MaterialProfessorVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialProfessorVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MaterialProfessorVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialProfessor.alterar(getIdEntidade());
            if (obj.getArquivoVO().getPastaBaseArquivoEnum() != null) {
                if (obj.getArquivoVO().getCodigo() == 0) {
                    getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                } else {
                    getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                }
            }
            final StringBuilder sql = new StringBuilder("");
            sql.append(" UPDATE MaterialProfessor SET descricao=?, localAula=?, arquivo=? ");
            sql.append(" WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setString(cont++, obj.getDescricao());
                    sqlAlterar.setInt(cont++, obj.getLocalAula());
                    sqlAlterar.setInt(cont++, obj.getCodigo());
                    sqlAlterar.setInt(cont++, obj.getArquivoVO().getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MaterialProfessorVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialProfessorVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MaterialProfessorVO obj) throws Exception {
        try {
        	/**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialProfessor.excluir(getIdEntidade());
            String sql = "DELETE FROM MaterialProfessor WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProfessorVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public MaterialProfessorVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
        String sql = "SELECT * FROM MaterialProfessor WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>MaterialProfessorVO</code> através do valor do atributo <code>codigo</code> da classe <code>ProfessorVO</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>MaterialProfessorVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorLocalAula(Integer codigoLocalAula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM MaterialProfessor WHERE localAula = ? ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoLocalAula});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). 
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>MaterialProfessorVO</code> resultantes da consulta.
     */
    public static List<MaterialProfessorVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MaterialProfessorVO> vetResultado = new ArrayList<MaterialProfessorVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados 
     * (<code>ResultSet</code>) em um objeto da classe <code>MaterialProfessorVO</code>.
     *
     * @return O objeto da classe <code>MaterialProfessorVO</code> com os dados devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static MaterialProfessorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MaterialProfessorVO obj = new MaterialProfessorVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setLocalAula(dadosSQL.getInt("localAula"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
        montarDadosArquivo(obj);
        obj.setNovoObj(false);
        return obj;
    }

    public static void montarDadosArquivo(MaterialProfessorVO obj) throws Exception {
        if (obj.getArquivoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(),Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
    }

    public void validarDados(MaterialProfessorVO obj) throws Exception {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("Campo Descrição (MaterialProfessor) deve ser informado.");
        }
//        if (obj.getData() == null) {
//            throw new ConsistirException("Campo Data (MaterialProfessor) deve ser informado.");
//        }
        if (obj.getLocalAula() <= 0) {
            throw new ConsistirException("Código LocalAula (MaterialProfessor) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MaterialProfessor.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        MaterialProfessor.idEntidade = idEntidade;
    }
}
