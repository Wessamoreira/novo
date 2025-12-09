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

import negocio.comuns.academico.MaterialAlunoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MaterialAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AlunoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>AlunoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MaterialAlunoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MaterialAluno extends ControleAcesso implements MaterialAlunoInterfaceFacade {

    protected static String idEntidade;

    public MaterialAluno() throws Exception {
        super();
        setIdEntidade("MaterialAluno");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirMaterialAlunos(Integer localAulaPrm, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MaterialAlunoVO obj = (MaterialAlunoVO) e.next();
            obj.setLocalAula(localAulaPrm);
            incluir(obj, usuario, configuracaoGeralSistema);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarMaterialAlunos(Integer localAula, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        excluirMaterialAlunos(localAula, usuario, configuracaoGeralSistema);
        incluirMaterialAlunos(localAula, objetos, usuario, configuracaoGeralSistema);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirMaterialAlunos(Integer localAula, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        String sql = "DELETE FROM MaterialAluno WHERE (localAula = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{localAula});
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MaterialAlunoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. 
     * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialAlunoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *            Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MaterialAlunoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialAluno.incluir(getIdEntidade());
            if (obj.getArquivoVO().getPastaBaseArquivoEnum() != null) {
                getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
            }
            final StringBuilder sql = new StringBuilder("");
            sql.append(" INSERT INTO MaterialAluno ( descricao, localAula, arquivo ) ");
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MaterialAlunoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialAlunoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MaterialAlunoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
             // MaterialAluno.alterar(getIdEntidade());
            if (obj.getArquivoVO().getPastaBaseArquivoEnum() != null) {
                if (obj.getArquivoVO().getCodigo() == 0) {
                    getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                } else {
                    getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                }
            }
            final StringBuilder sql = new StringBuilder("");
            sql.append(" UPDATE MaterialAluno SET descricao=?, localAula=?, arquivo=? ");
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
     * Operação responsável por excluir no BD um objeto da classe <code>MaterialAlunoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MaterialAlunoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MaterialAlunoVO obj) throws Exception {
        try {
        	/**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // MaterialAluno.excluir(getIdEntidade());
            String sql = "DELETE FROM MaterialAluno WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AlunoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public MaterialAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
        String sql = "SELECT * FROM MaterialAluno WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>MaterialAlunoVO</code> através do valor do atributo <code>codigo</code> da classe <code>AlunoVO</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>MaterialAlunoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorLocalAula(Integer codigoLocalAula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM MaterialAluno WHERE localAula = ? ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoLocalAula});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). 
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>MaterialAlunoVO</code> resultantes da consulta.
     */
    public static List<MaterialAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MaterialAlunoVO> vetResultado = new ArrayList<MaterialAlunoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados 
     * (<code>ResultSet</code>) em um objeto da classe <code>MaterialAlunoVO</code>.
     *
     * @return O objeto da classe <code>MaterialAlunoVO</code> com os dados devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static MaterialAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MaterialAlunoVO obj = new MaterialAlunoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setLocalAula(dadosSQL.getInt("localAula"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
        montarDadosArquivo(obj);
        obj.setNovoObj(false);
        return obj;
    }

    public static void montarDadosArquivo(MaterialAlunoVO obj) throws Exception {
        if (obj.getArquivoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(),Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
    }

    public void validarDados(MaterialAlunoVO obj) throws Exception {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("Campo Descrição (MaterialAluno) deve ser informado.");
        }
//        if (obj.getData() == null) {
//            throw new ConsistirException("Campo Data (MaterialAluno) deve ser informado.");
//        }
        if (obj.getLocalAula() <= 0) {
            throw new ConsistirException("Código LocalAula (MaterialAluno) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MaterialAluno.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        MaterialAluno.idEntidade = idEntidade;
    }
}
