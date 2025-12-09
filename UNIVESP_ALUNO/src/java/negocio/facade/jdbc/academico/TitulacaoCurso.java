package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TitulacaoCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TitulacaoCursoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TitulacaoCursoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TitulacaoCursoVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class TitulacaoCurso extends ControleAcesso implements TitulacaoCursoInterfaceFacade {

    protected static String idEntidade;
    private Hashtable itemTitulacaoCursos;

    public TitulacaoCurso() throws Exception {
        super();
        setIdEntidade("TitulacaoCurso");
        setItemTitulacaoCursos(new Hashtable(0));
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TitulacaoCursoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TitulacaoCursoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TitulacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            incluir(getIdEntidade(), true, usuario);
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO TitulacaoCurso( curso ) VALUES ( ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getCurso().getCodigo().intValue());

                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getItemTitulacaoCursoFacade().incluirItemTitulacaoCursos(obj.getCodigo(), obj.getItemTitulacaoCursoVOs());
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TitulacaoCursoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TitulacaoCursoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TitulacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            alterar(getIdEntidade(), true, usuario);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE TitulacaoCurso set curso=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getCurso().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getItemTitulacaoCursoFacade().alterarItemTitulacaoCursos(obj.getCodigo(), obj.getItemTitulacaoCursoVOs());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TitulacaoCursoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TitulacaoCursoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TitulacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuario);
            getFacadeFactory().getItemTitulacaoCursoFacade().excluirItemTitulacaoCursos(obj.getCodigo());
            String sql = "DELETE FROM TitulacaoCurso WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param TitulacaoCursoVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(TitulacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuario);
        } else {
            alterar(obj, usuario);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>TitulacaoCursoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(TitulacaoCursoVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if ((obj.getCurso() == null)
                || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new Exception(UteisJSF.internacionalizar("msg_TitulacaoCurso_curso"));
        }
        if (obj.getItemTitulacaoCursoVOs().isEmpty()) {
            throw new Exception(UteisJSF.internacionalizar("msg_TitulacaoCurso_itemTitulacaoCurso"));
        }


    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>TitulacaoCursoVO</code>.
     */
    public void validarUnicidade(List<TitulacaoCursoVO> lista, TitulacaoCursoVO obj) throws ConsistirException {
        for (TitulacaoCursoVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(TitulacaoCursoVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela TitulacaoCursoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<TitulacaoCursoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//        if (campoConsulta.equals("CODIGO")) {
//            if (valorConsulta.trim().equals("")) {
//                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
//            }
//            if (valorConsulta.equals("")) {
//                valorConsulta = "0";
//            }
//            int valorInt = Integer.parseInt(valorConsulta);
//            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
//        }
        if (campoConsulta.equals("NOME_CURSO")) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorNomeCurso(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>TitulacaoCursoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT TitulacaoCurso.* FROM TitulacaoCurso ");
        sqlSb.append("INNER JOIN curso on TitulacaoCurso.curso = curso.codigo ");
        sqlSb.append(" WHERE curso.nome ilike'").append(valorConsulta).append("'");
        sqlSb.append(" ORDER BY Curso.nome");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString()), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TitulacaoCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TitulacaoCurso WHERE codigo >= ?  ORDER BY codigo";
        ////System.out.println(sqlStr.toString());
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    public List<TitulacaoCursoVO> consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TitulacaoCurso WHERE curso = ?  ORDER BY codigo";
        ////System.out.println(sqlStr.toString());
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    public TitulacaoCursoVO consultarPorCodigoCursoUnico(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM TitulacaoCurso WHERE curso = ?  ORDER BY codigo DESC LIMIT 1";
//        PreparedStatement sqlConsultar = con.prepareStatement(sql);
//        sqlConsultar.setInt(1, codigoPrm.intValue() );
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            return new TitulacaoCursoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TitulacaoCursoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TitulacaoCursoVO</code>.
     * @return  O objeto da classe <code>TitulacaoCursoVO</code> com os dados devidamente montados.
     */
    public static TitulacaoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TitulacaoCursoVO obj = new TitulacaoCursoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.setNovoObj(false);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
//        obj.setItemTitulacaoCursoVOs(consultarItemTitulacaoCursos(obj.getCodigo(), nivelMontarDados));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        montarDadosCurso(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>ItemTitulacaoCursoVO</code>
     * ao List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>ItemTitulacaoCursoVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjItemTitulacaoCursoVOs(TitulacaoCursoVO objTitulacaoCursoVO, ItemTitulacaoCursoVO obj) throws Exception {
        getFacadeFactory().getItemTitulacaoCursoFacade().validarDados(obj);
        obj.setTitulacaoCurso(objTitulacaoCursoVO);
        int index = 0;
        for (ItemTitulacaoCursoVO objExistente : objTitulacaoCursoVO.getItemTitulacaoCursoVOs()) {
            if ((objExistente.getTitulacao().equals(obj.getTitulacao()) && objExistente.getSegundaTitulacao().equals(obj.getSegundaTitulacao()))
                    || (objExistente.getTitulacao().equals(obj.getSegundaTitulacao()) && objExistente.getSegundaTitulacao().equals(obj.getTitulacao()))) {
                if ((objTitulacaoCursoVO.getPorcentagemTotalUsada() - objExistente.getQuantidade() + obj.getQuantidade()) > 100) {
                    throw new ConsistirException("A quantidade total não pode ultrapassar 100%.");
                }
                objTitulacaoCursoVO.getItemTitulacaoCursoVOs().set(index, obj);
                return;
            }
            index++;
        }
        if ((objTitulacaoCursoVO.getPorcentagemTotalUsada() + obj.getQuantidade()) > 100) {
            throw new ConsistirException("A quantidade total não pode ultrapassar 100%.");
        }
        objTitulacaoCursoVO.getItemTitulacaoCursoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>ItemTitulacaoCursoVO</code>
     * no List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
     * @param titulacao  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItemTitulacaoCursoVOs(TitulacaoCursoVO objTitulacaoCursoVO, String titulacao) throws Exception {
        int index = 0;
        for (ItemTitulacaoCursoVO objExistente : objTitulacaoCursoVO.getItemTitulacaoCursoVOs()) {
            if (objExistente.getTitulacao().equals(titulacao)) {
                objTitulacaoCursoVO.getItemTitulacaoCursoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>ItemTitulacaoCursoVO</code>
     * no List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
     * @param titulacao  Parâmetro para localizar o objeto do List.
     */
    public ItemTitulacaoCursoVO consultarObjItemTitulacaoCursoVO(TitulacaoCursoVO objTitulacaoCursoVO, String titulacao) throws Exception {
        for (ItemTitulacaoCursoVO objExistente : objTitulacaoCursoVO.getItemTitulacaoCursoVOs()) {
            if (objExistente.getTitulacao().equals(titulacao)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>TitulacaoCursoVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(TitulacaoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(new Curso().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por adicionar um objeto da <code>ItemTitulacaoCursoVO</code> no Hashtable <code>ItemTitulacaoCursos</code>.
     * Neste Hashtable são mantidos todos os objetos de ItemTitulacaoCurso de uma determinada TitulacaoCurso.
     * @param obj  Objeto a ser adicionado no Hashtable.
     */
    public void adicionarObjItemTitulacaoCursos(ItemTitulacaoCursoVO obj) throws Exception {
        getItemTitulacaoCursos().put(obj.getTitulacao() + "", obj);
        //adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por remover um objeto da classe <code>ItemTitulacaoCursoVO</code> do Hashtable <code>ItemTitulacaoCursos</code>.
     * Neste Hashtable são mantidos todos os objetos de ItemTitulacaoCurso de uma determinada TitulacaoCurso.
     * @param Titulacao Atributo da classe <code>ItemTitulacaoCursoVO</code> utilizado como apelido (key) no Hashtable.
     */
    public void excluirObjItemTitulacaoCursos(String Titulacao) throws Exception {
        getItemTitulacaoCursos().remove(Titulacao + "");
        //excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TitulacaoCursoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TitulacaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TitulacaoCurso WHERE codigo = ?";
//        PreparedStatement sqlConsultar = con.prepareStatement(sql);
//        sqlConsultar.setInt(1, codigoPrm.intValue() );
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( TitulacaoCurso ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<TitulacaoCursoVO> consultarUnicidade(TitulacaoCursoVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    public Hashtable getItemTitulacaoCursos() {
        if (itemTitulacaoCursos == null) {
            itemTitulacaoCursos = new Hashtable(0);
        }
        return (itemTitulacaoCursos);
    }

    public void setItemTitulacaoCursos(Hashtable itemTitulacaoCursos) {
        this.itemTitulacaoCursos = itemTitulacaoCursos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TitulacaoCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        TitulacaoCurso.idEntidade = idEntidade;
    }
}
