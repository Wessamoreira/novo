package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

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

import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ItemTitulacaoCursoInterfaceFacade;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ItemTitulacaoCursoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ItemTitulacaoCursoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ItemTitulacaoCursoVO
 * @see SuperEntidade
 * @see TitulacaoCurso
 */
@Repository
@Scope("singleton")
@Lazy
public class ItemTitulacaoCurso extends ControleAcesso implements ItemTitulacaoCursoInterfaceFacade {

    protected static String idEntidade;

    public ItemTitulacaoCurso() throws Exception {
        super();
        setIdEntidade("TitulacaoCurso");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ItemTitulacaoCursoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ItemTitulacaoCursoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemTitulacaoCursoVO obj) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO ItemTitulacaoCurso( titulacaoCurso, titulacao, quantidade, segundaTitulacao ) VALUES ( ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getTitulacaoCurso().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getTitulacaoCurso().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getTitulacao());
                sqlInserir.setInt(3, obj.getQuantidade().intValue());
                sqlInserir.setString(4, obj.getSegundaTitulacao());
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ItemTitulacaoCursoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ItemTitulacaoCursoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemTitulacaoCursoVO obj) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "UPDATE ItemTitulacaoCurso set titulacaoCurso=?, titulacao=?, quantidade=?, segundaTitulacao=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getTitulacaoCurso().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getTitulacaoCurso().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getTitulacao());
                sqlAlterar.setInt(3, obj.getQuantidade().intValue());
                sqlAlterar.setString(4, obj.getSegundaTitulacao());
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ItemTitulacaoCursoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ItemTitulacaoCursoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ItemTitulacaoCursoVO obj) throws Exception {
        ItemTitulacaoCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemTitulacaoCurso WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        //excluir().execute();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ItemTitulacaoCursoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(ItemTitulacaoCursoVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getTitulacao().equals("0")) {
            throw new Exception(UteisJSF.internacionalizar("msg_ItemTitulacaoCurso_titulacao"));
        }
        if (obj.getQuantidade().intValue() == 0) {
            throw new Exception(UteisJSF.internacionalizar("msg_ItemTitulacaoCurso_quantidade"));
        }


    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>ItemTitulacaoCursoVO</code>.
     */
    public void validarUnicidade(List<ItemTitulacaoCursoVO> lista, ItemTitulacaoCursoVO obj) throws ConsistirException {
        for (ItemTitulacaoCursoVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(ItemTitulacaoCursoVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setTitulacao(obj.getTitulacao().toUpperCase());
        obj.setSegundaTitulacao(obj.getSegundaTitulacao().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela ItemTitulacaoCursoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<ItemTitulacaoCursoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals("codigo")) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals("CODIGO_TITULACAO_CURSO)")) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigoTitulacaoCurso(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals("nomeTitulacao")) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorTitulacao(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals("QUANTIDADE")) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorQuantidade(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);

        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemTitulacaoCurso</code> através do valor do atributo 
     * <code>Integer quantidade</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorQuantidade(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemTitulacaoCurso WHERE quantidade >= ?  ORDER BY quantidade";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    public List<SelectItem> consultarListaSelectItem(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemTitulacaoCurso ORDER BY quantidade";
        List<ItemTitulacaoCursoVO> resultadoConsulta = montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados);
        List<SelectItem> objs = new ArrayList<SelectItem>();
        objs.add(new SelectItem(0L, ""));

        for (ItemTitulacaoCursoVO objItemTitulacaoCursoVO : resultadoConsulta) {
            if (!objItemTitulacaoCursoVO.getSegundaTitulacao().equals("")) {
                objs.add(new SelectItem(objItemTitulacaoCursoVO.getCodigo(), objItemTitulacaoCursoVO.getTitulacao().toString() + "/" + objItemTitulacaoCursoVO.getSegundaTitulacao().toString()));
            } else {
                objs.add(new SelectItem(objItemTitulacaoCursoVO.getCodigo(), objItemTitulacaoCursoVO.getTitulacao().toString()));
            }
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        return objs;

    }

    /**
     * Responsável por realizar uma consulta de <code>ItemTitulacaoCurso</code> através do valor do atributo 
     * <code>String titulacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTitulacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //SuperEntidade.consultar(getIdEntidade(), controlarAcesso);
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM ItemTitulacaoCurso WHERE upper( titulacao ) like(?) ORDER BY titulacao";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemTitulacaoCurso</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>TitulacaoCurso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoTitulacaoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //SuperEntidade.consultar(getIdEntidade(), controlarAcesso);
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ItemTitulacaoCurso.* FROM ItemTitulacaoCurso, TitulacaoCurso WHERE ItemTitulacaoCurso.titulacaoCurso = TitulacaoCurso.codigo and TitulacaoCurso.codigo >= ? ORDER BY TitulacaoCurso.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemTitulacaoCurso</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemTitulacaoCurso WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ItemTitulacaoCursoVO</code>.
     * @return  O objeto da classe <code>ItemTitulacaoCursoVO</code> com os dados devidamente montados.
     */
    public static ItemTitulacaoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ItemTitulacaoCursoVO obj = new ItemTitulacaoCursoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setTitulacao(dadosSQL.getString("titulacao"));
        obj.setSegundaTitulacao(dadosSQL.getString("segundaTitulacao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.getTitulacaoCurso().setCodigo((dadosSQL.getInt("titulacaoCurso")));
        obj.setQuantidade((dadosSQL.getInt("quantidade")));
        obj.setNovoObj(false);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ItemTitulacaoCursoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ItemTitulacaoCurso</code>.
     * @param <code>titulacaoCurso</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirItemTitulacaoCursos(Integer titulacaoCurso) throws Exception {
        ItemTitulacaoCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemTitulacaoCurso WHERE (titulacaoCurso = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{titulacaoCurso});
//        PreparedStatement sqlExcluir = con.prepareStatement(sql);
//
//        sqlExcluir.setInt(1, titulacaoCurso.intValue());
//        sqlExcluir.execute();
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ItemTitulacaoCursoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirItemTitulacaoCursos</code> e <code>incluirItemTitulacaoCursos</code> disponíveis na classe <code>ItemTitulacaoCurso</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarItemTitulacaoCursos(Integer titulacaoCurso, List objetos) throws Exception {
        String str = "DELETE FROM ItemTitulacaoCurso WHERE titulacaoCurso = " + titulacaoCurso;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ItemTitulacaoCursoVO objeto = (ItemTitulacaoCursoVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemTitulacaoCursoVO objeto = (ItemTitulacaoCursoVO) e.next();
            if (objeto.getTitulacaoCurso().getCodigo().equals(0)) {
                objeto.getTitulacaoCurso().setCodigo(titulacaoCurso);
            }
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto);
            } else {
                alterar(objeto);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ItemTitulacaoCursoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.TitulacaoCurso</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirItemTitulacaoCursos(Integer titulacaoCursoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemTitulacaoCursoVO obj = (ItemTitulacaoCursoVO) e.next();
            obj.getTitulacaoCurso().setCodigo(titulacaoCursoPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ItemTitulacaoCursoVO</code> relacionados a um objeto da classe <code>academico.TitulacaoCurso</code>.
     * @param titulacaoCurso  Atributo de <code>academico.TitulacaoCurso</code> a ser utilizado para localizar os objetos da classe <code>ItemTitulacaoCursoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarItemTitulacaoCursos(Integer titulacaoCurso, int nivelMontarDados) throws Exception {
        ItemTitulacaoCurso.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM ItemTitulacaoCurso WHERE titulacaoCurso = ?";

        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, titulacaoCurso), nivelMontarDados));


//        PreparedStatement sqlConsulta = con.prepareStatement(sql);
//        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
//        sqlConsulta.setInt(1, titulacaoCurso.intValue());
//        ResultSet resultado = sqlConsulta.executeQuery();
//        while (resultado.next()) {
//            ItemTitulacaoCursoVO novoObj = new ItemTitulacaoCursoVO();
//            novoObj = ItemTitulacaoCurso.montarDados(resultado, nivelMontarDados);
//            objetos.add(novoObj);
//        }
        //return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ItemTitulacaoCursoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ItemTitulacaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ItemTitulacaoCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ItemTitulacaoCurso ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public List<ItemTitulacaoCursoVO> consultarUnicidade(ItemTitulacaoCursoVO obj, boolean alteracao) throws Exception {
        // super.verificarPermissaoConsultar(getIdEntidade(), false);
        return new ArrayList(0);
    }

    public void adicionarQtdeNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade, PessoaVO professor) throws Exception {
        Integer qtde = 0;
        Integer maiorNivel = professor.consultarMaiorNivelDaEscolaridade();
        if (maiorNivel >= 2) {
            if (hashMapQtdeNivelEscolaridade.containsKey(maiorNivel)) {
                qtde = hashMapQtdeNivelEscolaridade.get(maiorNivel);
                qtde++;
                hashMapQtdeNivelEscolaridade.put(maiorNivel, qtde);
            } else {
                hashMapQtdeNivelEscolaridade.put(maiorNivel, 1);
            }
        }
    }

    public void calcularQtdeProfessorNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade, Integer qtdeProfessores, TitulacaoCursoVO titulacaoCursoVO) throws Exception {
        if (qtdeProfessores > 0) {
        	int qtdProfessorDoutorOuMestre = 0;						
        	
        	for(ItemTitulacaoCursoVO itemTitulacaoCursoVO: titulacaoCursoVO.getItemTitulacaoCursoVOs()) {
        		itemTitulacaoCursoVO.setQuantidadePreenchida(0);
        		if(hashMapQtdeNivelEscolaridade.containsKey(NivelFormacaoAcademica.getSiglaPorNivel(itemTitulacaoCursoVO.getTitulacao()))) {
        			itemTitulacaoCursoVO.setQuantidadePreenchida(hashMapQtdeNivelEscolaridade.get(NivelFormacaoAcademica.getSiglaPorNivel((itemTitulacaoCursoVO.getTitulacao()))));
        		}
        		if(hashMapQtdeNivelEscolaridade.containsKey(NivelFormacaoAcademica.getSiglaPorNivel((itemTitulacaoCursoVO.getSegundaTitulacao())))) {
        			itemTitulacaoCursoVO.setQuantidadePreenchida(itemTitulacaoCursoVO.getQuantidadePreenchida() + hashMapQtdeNivelEscolaridade.get(NivelFormacaoAcademica.getSiglaPorNivel((itemTitulacaoCursoVO.getSegundaTitulacao()))));
        		}
        		itemTitulacaoCursoVO.setQuantidadePreenchida((itemTitulacaoCursoVO.getQuantidadePreenchida() * 100)/ qtdeProfessores);
        	}
        	
//            titulacaoCursoVO.obterNivelItemTitulacaoCurso();
//            Ordenacao.ordenarLista(titulacaoCursoVO.getItemTitulacaoCursoVOs(), "nivelTitulacao");
//            ItemTitulacaoCursoVO obj;
//            Integer qtdeProfessoresUsados = 0;
//            Double valorQtdeProfessores = 0.0;
//            for (int i = titulacaoCursoVO.getItemTitulacaoCursoVOs().size() - 1; i >= 0; i--) {
//                obj = titulacaoCursoVO.getItemTitulacaoCursoVOs().get(i);
//                valorQtdeProfessores = (obj.getQuantidade() * qtdeProfessores) / 100.0;
//                qtdeProfessoresUsados = Uteis.getParteInteiraDouble(valorQtdeProfessores);
//                if (Uteis.getParteDecimalDoubleDuasCasas(valorQtdeProfessores) > 0) {
//                    qtdeProfessoresUsados++;
//                }
//                obj.setTotalProfessores(qtdeProfessoresUsados);
//                int qtdeProfessoresDisponiveis = 0;
//                int resultado = 0;
//                int menorNivel = obj.getMenorNivelTitulacaoCurso();
//                for (int nivel = 8; nivel >= menorNivel; nivel--) {
//                    if (hashMapQtdeNivelEscolaridade.containsKey(nivel)) {
//                        if (nivel >= obj.getNivelTitulacao() && hashMapQtdeNivelEscolaridade.get(nivel) > 0) {
//                            qtdeProfessoresDisponiveis = hashMapQtdeNivelEscolaridade.get(nivel);
//                            resultado = qtdeProfessoresUsados - qtdeProfessoresDisponiveis;
//                            if (resultado >= 0) {
//                                qtdeProfessoresUsados = resultado;
//                                qtdeProfessoresDisponiveis = 0;
//                            } else {
//                                qtdeProfessoresUsados = 0;
//                                qtdeProfessoresDisponiveis = (resultado * -1);
//                            }
//                            hashMapQtdeNivelEscolaridade.put(nivel, qtdeProfessoresDisponiveis);
//                        } else if (obj.getNivelSegundaTitulacao() > 0 && nivel >= obj.getNivelSegundaTitulacao() && hashMapQtdeNivelEscolaridade.get(nivel) > 0) {
//                            qtdeProfessoresDisponiveis = hashMapQtdeNivelEscolaridade.get(nivel);
//                            resultado = qtdeProfessoresUsados - qtdeProfessoresDisponiveis;
//                            if (resultado >= 0) {
//                                qtdeProfessoresUsados = resultado;
//                                qtdeProfessoresDisponiveis = 0;
//                            } else {
//                                qtdeProfessoresUsados = 0;
//                                qtdeProfessoresDisponiveis = (resultado * -1);
//                            }
//                            hashMapQtdeNivelEscolaridade.put(nivel, qtdeProfessoresDisponiveis);
//                        }
//                    }
//                    if (qtdeProfessoresUsados == 0) {
//                        nivel = 0;
//                        obj.setQuantidadePreenchida(100);
//                    }
//                }
//                if (qtdeProfessoresUsados > 0) {
//                    if (qtdeProfessoresUsados.equals(obj.getTotalProfessores())) {
//                        obj.setQuantidadePreenchida(0);
//                    } else {
//                        obj.setQuantidadePreenchida(Uteis.getParteInteiraDouble(((obj.getTotalProfessores() - qtdeProfessoresUsados) * 100.0) / obj.getTotalProfessores()));
//                    }
//                }
//            }
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ItemTitulacaoCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ItemTitulacaoCurso.idEntidade = idEntidade;
    }
}
