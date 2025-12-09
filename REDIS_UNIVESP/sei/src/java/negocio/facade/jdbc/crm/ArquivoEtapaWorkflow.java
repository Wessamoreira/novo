package negocio.facade.jdbc.crm;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ArquivoEtapaWorkflowVO;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboArquivoEtapaWorkflowEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.ArquivoEtapaWorkflowInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ArquivoEtapaWorkflowVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ArquivoEtapaWorkflowVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ArquivoEtapaWorkflowVO
 * @see ControleAcesso
 * @see Workflow
 */
@Repository
@Scope("singleton")
@Lazy
public class ArquivoEtapaWorkflow extends ControleAcesso implements ArquivoEtapaWorkflowInterfaceFacade {

    protected static String idEntidade;

    public ArquivoEtapaWorkflow() throws Exception {
        super();
        setIdEntidade("Workflow");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ArquivoEtapaWorkflowVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ArquivoEtapaWorkflowVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ArquivoEtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        getFacadeFactory().getArquivoFacade().incluir(obj.getArquivo(), usuarioLogado, configuracaoGeralSistemaVO);
        final String sql = "INSERT INTO ArquivoEtapaWorkflow( etapaWorkflow, arquivo,  curso ) VALUES ( ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getEtapaWorkflow().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getEtapaWorkflow().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getArquivo().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getArquivo().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getCurso().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ArquivoEtapaWorkflowVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ArquivoEtapaWorkflowVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ArquivoEtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        getFacadeFactory().getArquivoFacade().alterar(obj.getArquivo(), usuarioLogado, configuracaoGeralSistemaVO);
        final String sql = "UPDATE ArquivoEtapaWorkflow set etapaWorkflow=?, arquivo=?, curso=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getEtapaWorkflow().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getEtapaWorkflow().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getArquivo().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getArquivo().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getCurso().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ArquivoEtapaWorkflowVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ArquivoEtapaWorkflowVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ArquivoEtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        ArquivoEtapaWorkflow.excluir(getIdEntidade());
        getFacadeFactory().getArquivoFacade().excluir(obj.getArquivo(), usuarioLogado, configuracaoGeralSistemaVO);
        String sql = "DELETE FROM ArquivoEtapaWorkflow WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
//        sqlExcluir.execute();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ArquivoEtapaWorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(ArquivoEtapaWorkflowVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getArquivo().getNome().equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_ArquivoWorkflow_nome"));
        }
        if (obj.getArquivo().getDescricao().equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_ArquivoWorkflow_descricao"));
        }
//        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
//            throw  new Exception(UteisJSF.internacionalizar("msg_ArquivoWorkflow_curso"));
//        }
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>ArquivoEtapaWorkflowVO</code>.
     */
    public void validarUnicidade(List<ArquivoEtapaWorkflowVO> lista, ArquivoEtapaWorkflowVO obj) throws ConsistirException {
        for (ArquivoEtapaWorkflowVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(ArquivoEtapaWorkflowVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela ArquivoEtapaWorkflowCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<ArquivoEtapaWorkflowVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboArquivoEtapaWorkflowEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return getFacadeFactory().getArquivoEtapaWorkflowFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArquivoEtapaWorkflowEnum.NOME_WORKFLOW.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getArquivoEtapaWorkflowFacade().consultarPorNomeEtapaWorkflow(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArquivoEtapaWorkflowEnum.NOME.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getArquivoEtapaWorkflowFacade().consultarPorNome(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArquivoEtapaWorkflowEnum.DESCRICAO.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getArquivoEtapaWorkflowFacade().consultarPorDescricao(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArquivoEtapaWorkflowEnum.NOME_CURSO.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getArquivoEtapaWorkflowFacade().consultarPorNomeCurso(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>ArquivoEtapaWorkflow</code> através do valor do atributo
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT ArquivoEtapaWorkflow.* FROM ArquivoEtapaWorkflow, Curso WHERE ArquivoEtapaWorkflow.curso = Curso.codigo and upper( Curso.nome ) like(?) ORDER BY Curso.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ArquivoEtapaWorkflow</code> através do valor do atributo
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM ArquivoEtapaWorkflow, Arquivo WHERE Arquivo.codigo = ArquivoEtapaWorkflow.arquivo upper( Arquivo.descricao ) like(?) ORDER BY descricao";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ArquivoEtapaWorkflow</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM ArquivoEtapaWorkflow , Arquivo WHERE Arquivo.codigo = ArquivoEtapaWorkflow.arquivo upper( arquivo.nome ) like(?) ORDER BY nome";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ArquivoEtapaWorkflow</code> através do valor do atributo
     * <code>nome</code> da classe <code>Workflow</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeEtapaWorkflow(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT ArquivoEtapaWorkflow.* FROM ArquivoEtapaWorkflow, EtapaWorkflow WHERE ArquivoEtapaWorkflow.etapaWorkflow = EtapaWorkflow.codigo and upper( EtapaWorkflow.nome ) like(?) ORDER BY EtapaWorkflow.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ArquivoEtapaWorkflow</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ArquivoEtapaWorkflow WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ArquivoEtapaWorkflowVO</code>.
     * @return  O objeto da classe <code>ArquivoEtapaWorkflowVO</code> com os dados devidamente montados.
     */
    public static ArquivoEtapaWorkflowVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ArquivoEtapaWorkflowVO obj = new ArquivoEtapaWorkflowVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getEtapaWorkflow().setCodigo(new Integer(dadosSQL.getInt("etapaWorkflow")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>ArquivoEtapaWorkflowVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(ArquivoEtapaWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>ArquivoEtapaWorkflowVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosArquivo(ArquivoEtapaWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivo().getCodigo().intValue() == 0) {
            obj.setArquivo(new ArquivoVO());
            return;
        }
        obj.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimariaConsultaCompleta(obj.getArquivo().getCodigo(), nivelMontarDados, usuario));
        obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ArquivoEtapaWorkflowVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ArquivoEtapaWorkflow</code>.
     * @param <code>workflow</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    /**
     * Operação responsável por alterar todos os objetos da <code>ArquivoEtapaWorkflowVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirArquivoEtapaWorkflows</code> e <code>incluirArquivoEtapaWorkflows</code> disponíveis na classe <code>ArquivoEtapaWorkflow</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarArquivoEtapaWorkflows(Integer etapaWorkflow, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        StringBuilder listaCodigo = new StringBuilder();
        realizarConsultaArquivoAptoParaExcluir(etapaWorkflow, objetos, listaCodigo);
        String str = "DELETE FROM ArquivoEtapaWorkflow WHERE etapaWorkflow = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ArquivoEtapaWorkflowVO objeto = (ArquivoEtapaWorkflowVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(str, new Object[]{etapaWorkflow});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ArquivoEtapaWorkflowVO objeto = (ArquivoEtapaWorkflowVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuarioLogado, configuracaoGeralSistemaVO);
            } else {
                alterar(objeto, usuarioLogado, configuracaoGeralSistemaVO);
            }
        }
        if(!listaCodigo.toString().isEmpty()){
            realizarExclusaoArquivo(listaCodigo, usuarioLogado, configuracaoGeralSistemaVO);
        }        
    }

    public void realizarConsultaArquivoAptoParaExcluir(Integer etapaWorkflow, List objetos, StringBuilder listaCodigo) throws Exception {
        String str = "Select arquivo FROM ArquivoEtapaWorkflow WHERE etapaWorkflow = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ArquivoEtapaWorkflowVO objeto = (ArquivoEtapaWorkflowVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str, new Object[]{etapaWorkflow});
        while (resultado.next()) {
            listaCodigo.append(resultado.getString("arquivo")).append(" ,");
        }
    }

    public void realizarExclusaoArquivo(StringBuilder listaCodigo, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        List<ArquivoVO> lista = getFacadeFactory().getArquivoFacade().consultarPorVariosCodigo(listaCodigo.substring(0, (listaCodigo.length() - 2)), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
        for (ArquivoVO arquivoVO : lista) {
            getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuarioLogado, configuracaoGeralSistemaVO);
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ArquivoEtapaWorkflowVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>crm.Workflow</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirArquivoEtapaWorkflows(Integer etapaWorkflow, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ArquivoEtapaWorkflowVO obj = (ArquivoEtapaWorkflowVO) e.next();
            obj.getEtapaWorkflow().setCodigo(etapaWorkflow);
            incluir(obj, usuarioLogado, configuracaoGeralSistemaVO);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ArquivoEtapaWorkflowVO</code> relacionados a um objeto da classe <code>crm.Workflow</code>.
     * @param workflow  Atributo de <code>crm.Workflow</code> a ser utilizado para localizar os objetos da classe <code>ArquivoEtapaWorkflowVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ArquivoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarArquivoEtapaWorkflows(Integer etapaWorkflow, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ArquivoEtapaWorkflow.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM ArquivoEtapaWorkflow WHERE etapaWorkflow = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{etapaWorkflow});
        while (resultado.next()) {
            ArquivoEtapaWorkflowVO novoObj = new ArquivoEtapaWorkflowVO();
            novoObj = ArquivoEtapaWorkflow.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ArquivoEtapaWorkflowVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ArquivoEtapaWorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ArquivoEtapaWorkflow WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ArquivoEtapaWorkflow ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<ArquivoEtapaWorkflowVO> consultarUnicidade(ArquivoEtapaWorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ArquivoEtapaWorkflow.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ArquivoEtapaWorkflow.idEntidade = idEntidade;
    }
}
