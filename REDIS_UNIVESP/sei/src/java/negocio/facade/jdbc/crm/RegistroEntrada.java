package negocio.facade.jdbc.crm;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
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
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.enumerador.TipoOrigemCadastroProspectEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.TipoUploadEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.Curso;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.RegistroEntradaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RegistroEntradaVO</code>. Responsável por implementar operações como incluir,
 * alterar, excluir e consultar pertinentes a classe <code>RegistroEntradaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RegistroEntradaVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroEntrada extends ControleAcesso implements RegistroEntradaInterfaceFacade {

    protected static String idEntidade;
    private Hashtable registroEntradaProspectss;

    public RegistroEntrada() throws Exception {
        super();
        setIdEntidade("RegistroEntrada");
        setRegistroEntradaProspectss(new Hashtable(0));
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroEntradaVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto.
     * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>RegistroEntradaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroEntradaVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            RegistroEntrada.incluir(getIdEntidade(), true, usuario);
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO RegistroEntrada( descricao, unidadeEnsino, dataEntrada, cursoEntrada, tipoUpload, delimitador ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataEntrada()));
                    if (obj.getCursoEntrada().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getCursoEntrada().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getTipoUpload().toString());
                    sqlInserir.setString(6, obj.getDelimitador());
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
            //
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroEntradaVO</code>. Sempre utiliza a chave primária da classe como atributo para
     * localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>RegistroEntradaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroEntradaVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            getFacadeFactory().getRegistroEntradaProspectsFacade().alterarRegistroEntradaProspectss(obj, obj.getRegistroEntradaProspectsVOs(), usuario);
            RegistroEntrada.alterar(getIdEntidade(), true, usuario);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE RegistroEntrada set descricao=?, tipoUpload=?, delimitador=?,  unidadeEnsino=?, dataEntrada=?, cursoEntrada=?  WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setString(2, obj.getTipoUpload().toString());
                    sqlAlterar.setString(3, obj.getDelimitador());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataEntrada()));
                    if (obj.getCursoEntrada().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getCursoEntrada().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }

                    sqlAlterar.setInt(7, obj.getCodigo().intValue());

                    return sqlAlterar;
                }
            }));

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroEntradaVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     * 
     * @param obj Objeto da classe <code>RegistroEntradaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroEntradaVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            RegistroEntrada.excluir(getIdEntidade(), true, usuario);
            getFacadeFactory().getRegistroEntradaProspectsFacade().excluirRegistroEntradaProspectss(obj.getCodigo(), usuario);
            String sql = "DELETE FROM RegistroEntrada WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * 
     * @param RegistroEntradaVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(RegistroEntradaVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, controlarAcesso, usuario);
        } else {
            alterar(obj, controlarAcesso, usuario);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>RegistroEntradaVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste
     * método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * 
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public void validarDados(RegistroEntradaVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getDescricao().equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_RegistroEntrada_descricao"));
        }
        if ((obj.getUnidadeEnsino() == null)
                || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new Exception(UteisJSF.internacionalizar("msg_RegistroEntrada_unidadeEnsino"));
        }
        if (obj.getDataEntrada() == null) {
            throw new Exception(UteisJSF.internacionalizar("msg_RegistroEntrada_unidadeEnsino"));
        }

        if (obj.getTipoUpload().equals(TipoUploadEnum.NENHUM)) {
            throw new Exception(UteisJSF.internacionalizar("msgRegistroEntrada_tipoUpload"));
        }

        // if (obj.getTipoUpload().equals(TipoUploadEnum.CSV)) {
        // if (obj.getDelimitador() == null || obj.getDelimitador().equals("")) {
        // throw new Exception(UteisJSF.internacionalizar("msgRegistroEntrada_delimitador"));
        // }
        // }
        if (obj.getRegistroEntradaProspectsVOs().isEmpty()) {
            throw new Exception(UteisJSF.internacionalizar("msgRegistroEntrada_RegistroEmtradaProspects"));
        }

        // if ((obj.getCursoEntrada() == null) ||
        // (obj.getCursoEntrada().getCodigo().intValue() == 0)) {
        // throw new Exception(UteisJSF.internacionalizar("msg_RegistroEntrada_cursoEntrada"));
        // }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>RegistroEntradaVO</code>.
     */
    public void validarUnicidade(List<RegistroEntradaVO> lista, RegistroEntradaVO obj) throws ConsistirException {
        for (RegistroEntradaVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(RegistroEntradaVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setDescricao(obj.getDescricao().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela RegistroEntradaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado
     * campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<RegistroEntradaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
        if (campoConsulta.equals("descricao")) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorDescricao(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario, unidadeEnsinoVO);
        }
        if (campoConsulta.equals("nomeUnidadeEnsino")) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorNomeUnidadeEnsino(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals("curso")) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorNomeCurso(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    private StringBuilder getSQLPadraoConsultaBasica() {
        StringBuilder str = new StringBuilder();
        str.append("SELECT registroEntrada.codigo AS codigo, registroEntrada.descricao AS descricao, registroEntrada.dataEntrada AS dataEntrada, unidadeensino.nome AS unidadeensino, curso.nome AS cursoEntrada");
        str.append(" FROM registroEntrada ");
        str.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = registroEntrada.unidadeensino");
        str.append(" LEFT JOIN curso ON curso.codigo = registroEntrada.cursoEntrada ");
        str.append("WHERE 1=1 ");
        return str;
    }

    private StringBuilder getSQLPadraoConsultaCompleta() {
        StringBuilder str = new StringBuilder();

        str.append("SELECT registroEntrada.codigo AS codigo, registroEntrada.descricao AS descricao, unidadeensino.codigo AS unidadeensino, curso.nome AS cursoEntrada, registroEntrada.dataEntrada AS dataEntrada, prospects.codigo AS prospectscodigo,");
        str.append("registroEntrada.tipoUpload AS tipoUpload, registroEntrada.delimitador AS delimitador, prospects.nome AS prospectsnome, prospects.emailPrincipal AS prospectsemailPrincipal, prospects.telefoneResidencial AS prospectstelefoneResidencial,");
        str.append(" registroEntradaProspects.existeProspects, prospects.nomeBatismo AS prospectsnomebatismo FROM registroEntrada ");

        str.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = registroEntrada.unidadeensino");
        str.append(" LEFT JOIN curso ON curso.codigo = registroEntrada.cursoEntrada ");
        str.append(" LEFT JOIN registroEntradaProspects ON registroEntrada.codigo = registroEntradaProspects.registroEntrada");
        str.append(" LEFT JOIN prospects ON registroEntradaProspects.prospects = prospects.codigo");
        str.append(" WHERE 1=1 ");
        return str;
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntrada</code> através do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>RegistroEntradaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("AND upper( curso ) like('").append(valorConsulta.toUpperCase()).append("') ORDER BY curso");
        return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntrada</code> através do valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>RegistroEntradaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("AND upper( UnidadeEnsino.nome) like('").append(valorConsulta.toUpperCase()).append("') ORDER BY UnidadeEnsino.nome ");
        return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntrada</code> através do valor do atributo <code>String descricao</code>. Retorna os objetos, com início do valor do
     * atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>RegistroEntradaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" AND upper(registroEntrada.descricao) like('").append(valorConsulta.toUpperCase());
        sqlStr.append("%')");
        if (Uteis.isAtributoPreenchido(unidadeEnsinoVO) && Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
        	sqlStr.append(" AND unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
        }
        sqlStr.append(" ORDER BY descricao");
        return (montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado));
    }

    public static List montarDadosConsultaCompletos(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosCompletos(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que
     * realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>RegistroEntradaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsultaRapida(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosSimples(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>RegistroEntradaVO</code>.
     * 
     * @return O objeto da classe <code>RegistroEntradaVO</code> com os dados devidamente montados.
     */
    public static RegistroEntradaVO montarDadosCompletos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        RegistroEntradaVO obj = new RegistroEntradaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setTipoUpload(TipoUploadEnum.valueOf(dadosSQL.getString("tipoUpload").trim()));
        obj.setDelimitador(dadosSQL.getString("delimitador").trim());
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setDataEntrada(dadosSQL.getDate("dataEntrada"));
        obj.getCursoEntrada().setNome(dadosSQL.getString("cursoEntrada"));

        // Dados registro entrada prospects
        RegistroEntradaProspectsVO registroEntradaProspectsVO = null;
        obj.getRegistroEntradaProspectsVOs().clear();
        do {
            registroEntradaProspectsVO = new RegistroEntradaProspectsVO();
            registroEntradaProspectsVO.getProspects().setCodigo(dadosSQL.getInt("prospectscodigo"));
            registroEntradaProspectsVO.getProspects().setNome(dadosSQL.getString("prospectsnome"));
            registroEntradaProspectsVO.getProspects().setNomeBatismo(dadosSQL.getString("prospectsnomebatismo"));
            registroEntradaProspectsVO.getProspects().setEmailPrincipal(dadosSQL.getString("prospectsemailPrincipal"));
            registroEntradaProspectsVO.getProspects().setTelefoneResidencial(dadosSQL.getString("prospectstelefoneResidencial"));
            registroEntradaProspectsVO.setExisteProspects(dadosSQL.getBoolean("existeProspects"));
            registroEntradaProspectsVO.setNovoObj(new Boolean(false));
            obj.getRegistroEntradaProspectsVOs().add(registroEntradaProspectsVO);
            if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("codigo")))) {
                break;
            }
        } while (dadosSQL.next());

        obj.setNovoObj(new Boolean(false));
        return obj;
    }

    public static RegistroEntradaVO montarDadosSimples(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        RegistroEntradaVO obj = new RegistroEntradaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino"));
        obj.setDataEntrada(dadosSQL.getDate("dataEntrada"));
        obj.getCursoEntrada().setNome(dadosSQL.getString("cursoEntrada"));
        obj.setNovoObj(new Boolean(false));
        return obj;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>RegistroEntradaProspectsVO</code> ao List <code>registroEntradaProspectsVOs</code>. Utiliza o atributo
     * padrão de consulta da classe <code>RegistroEntradaProspects</code> - getProspects().getCodigo() - como identificador (key) do objeto no List.
     * 
     * @param obj Objeto da classe <code>RegistroEntradaProspectsVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjRegistroEntradaProspectsVOs(RegistroEntradaVO objRegistroEntradaVO, RegistroEntradaProspectsVO obj) throws Exception {
        getFacadeFactory().getRegistroEntradaProspectsFacade().validarDados(obj);
        obj.setRegistroEntrada(objRegistroEntradaVO);
        int index = 0;
        for (RegistroEntradaProspectsVO objExistente : objRegistroEntradaVO.getRegistroEntradaProspectsVOs()) {
            if (objExistente.getProspects().getCodigo().equals(obj.getProspects().getCodigo())) {
                objRegistroEntradaVO.getRegistroEntradaProspectsVOs().set(index, obj);
                return;
            }
            index++;
        }
        objRegistroEntradaVO.getRegistroEntradaProspectsVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>RegistroEntradaProspectsVO</code> no List <code>registroEntradaProspectsVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>RegistroEntradaProspects</code> - getProspects().getCodigo() - como identificador (key) do objeto no List.
     * 
     * @param prospects Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjRegistroEntradaProspectsVOs(RegistroEntradaVO objRegistroEntradaVO, Integer prospects) throws Exception {
        int index = 0;
        for (RegistroEntradaProspectsVO objExistente : objRegistroEntradaVO.getRegistroEntradaProspectsVOs()) {
            if (objExistente.getProspects().getCodigo().equals(prospects)) {
                objRegistroEntradaVO.getRegistroEntradaProspectsVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>RegistroEntradaProspectsVO</code> no List <code>registroEntradaProspectsVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>RegistroEntradaProspects</code> - getProspects().getCodigo() - como identificador (key) do objeto no List.
     * 
     * @param prospects Parâmetro para localizar o objeto do List.
     */
    public RegistroEntradaProspectsVO consultarObjRegistroEntradaProspectsVO(RegistroEntradaVO objRegistroEntradaVO, Integer prospects) throws Exception {
        for (RegistroEntradaProspectsVO objExistente : objRegistroEntradaVO.getRegistroEntradaProspectsVOs()) {
            if (objExistente.getProspects().getCodigo().equals(prospects)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>RegistroEntradaVO</code>. Faz uso da chave primária da
     * classe <code>CursoVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCursoEntrada(RegistroEntradaVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        if (obj.getCursoEntrada().getCodigo().intValue() == 0) {
            obj.setCursoEntrada(new CursoVO());
            return;
        }
        obj.setCursoEntrada(new Curso().consultarPorChavePrimaria(obj.getCursoEntrada().getCodigo(), nivelMontarDados, false, usuarioLogado));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>RegistroEntradaVO</code>. Faz uso da chave primária
     * da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * 
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(RegistroEntradaVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(new UnidadeEnsino().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuarioLogado));
    }

    /**
     * Operação responsável por adicionar um objeto da <code>RegistroEntradaProspectsVO</code> no Hashtable <code>RegistroEntradaProspectss</code>. Neste Hashtable são mantidos
     * todos os objetos de RegistroEntradaProspects de uma determinada RegistroEntrada.
     * 
     * @param obj Objeto a ser adicionado no Hashtable.
     */
    public void adicionarObjRegistroEntradaProspectss(RegistroEntradaProspectsVO obj) throws Exception {
        getRegistroEntradaProspectss().put(obj.getProspects().getCodigo() + "", obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por remover um objeto da classe <code>RegistroEntradaProspectsVO</code> do Hashtable <code>RegistroEntradaProspectss</code>. Neste Hashtable são
     * mantidos todos os objetos de RegistroEntradaProspects de uma determinada RegistroEntrada.
     * 
     * @param Prospects Atributo da classe <code>RegistroEntradaProspectsVO</code> utilizado como apelido (key) no Hashtable.
     */
    public void excluirObjRegistroEntradaProspectss(Integer Prospects) throws Exception {
        getRegistroEntradaProspectss().remove(Prospects + "");
        // excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroEntradaVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroEntradaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        StringBuilder sql = getSQLPadraoConsultaCompleta();
        sql.append(" AND registroEntrada.codigo =").append(codigoPrm);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroEntrada ).");
        }
        return (montarDadosCompletos(tabelaResultado, nivelMontarDados, usuarioLogado));
    }

    public List<RegistroEntradaVO> consultarUnicidade(RegistroEntradaVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
        return new ArrayList(0);
    }

    public Hashtable getRegistroEntradaProspectss() {
        if (registroEntradaProspectss == null) {
            registroEntradaProspectss = new Hashtable(0);
        }
        return (registroEntradaProspectss);
    }

    public void setRegistroEntradaProspectss(Hashtable registroEntradaProspectss) {
        this.registroEntradaProspectss = registroEntradaProspectss;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroEntrada.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com
     * objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        RegistroEntrada.idEntidade = idEntidade;
    }

    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> realizarLeituraArquivoExcel(FileUploadEvent upload, RegistroEntradaVO registroEntradaVO, UsuarioVO usuarioLogado) throws Exception {
        List<String> listaErros = new ArrayList<String>(0);
        try {
            String extensao = (upload.getUploadedFile().getName().substring((upload.getUploadedFile().getName().lastIndexOf(".") + 1), upload.getUploadedFile().getName().length()));
            if (extensao.equals("xls") || extensao.equals("xlsx")) {
            	
                XSSFWorkbook workbook = new XSSFWorkbook(upload.getUploadedFile().getInputStream());
                if (workbook.getNumberOfSheets() > 1) {
                    throw new Exception("O arquivo informado possui mais de uma planilha. Remova as planilhas não utilizadas.");
                } else {
                    realizarLeituraPlanilha(workbook.getSheetAt(0), registroEntradaVO, usuarioLogado, listaErros);
                }
            } else {
                throw new Exception("Extensão do arquivo não é valida. Somente arquivos em excel serão processados.");
            }
            verificarDuplicidadeNomeEmail(registroEntradaVO.getRegistroEntradaProspectsVOs(), listaErros);
            extensao = null;
            return listaErros;
        } catch (Exception e) {
            throw e;
        }
    }

    public void realizarLeituraPlanilha(XSSFSheet planilha, RegistroEntradaVO registroEntradaVO, UsuarioVO usuarioLogado, List<String> listaErros) throws Exception {
        Boolean erro = false;
        for (int i = 0; i <= planilha.getLastRowNum(); i++) {
            erro = realizarLeituraLinha(planilha.getRow(i), registroEntradaVO, usuarioLogado, i, listaErros);
            if (erro) {
                break;
            }
        }
    }

    public void realizarValidacaoCabecarioExcel(XSSFCell celula) throws Exception {
        if (celula == null) {
            throw new Exception("A planilha informada provavelmente possui menos colunas do que o padrão exigido pelo Sistema.");
        }
        if (celula.getReference().equals("A1") && !celula.getStringCellValue().trim().equalsIgnoreCase("tipo prospect")) {
            throw new Exception("O valor da referencia A1 está errada. O valor deve ser Tipo Prospect");
        }
        if (celula.getReference().equals("B1") && !celula.getStringCellValue().trim().equalsIgnoreCase("nome social")) {
            throw new Exception("O valor da referencia B1 está errada. O valor deve ser Nome Social");
        }
        if (celula.getReference().equals("C1") && !celula.getStringCellValue().trim().equalsIgnoreCase("email")) {
            throw new Exception("O valor da referencia C1 está errada. O valor deve ser Email");
        }
        if (celula.getReference().equals("D1") && !celula.getStringCellValue().trim().equalsIgnoreCase("telefone residencial")) {
            throw new Exception("O valor da referencia D1 está errada. O valor deve ser Telefone Residencial");
        }
        if (celula.getReference().equals("E1") && !celula.getStringCellValue().trim().equalsIgnoreCase("cpf")) {
            throw new Exception("O valor da referencia E1 está errada. O valor deve ser CPF");
        }
        if (celula.getReference().equals("F1") && !celula.getStringCellValue().trim().equalsIgnoreCase("cnpj")) {
            throw new Exception("O valor da referencia F1 está errada. O valor deve ser CNPJ");
        }
        if (celula.getReference().equals("G1") && !celula.getStringCellValue().trim().equalsIgnoreCase("razão social")) {
            throw new Exception("O valor da referencia G1 está errada. O valor deve ser Razão Social");
        }
        if (celula.getReference().equals("H1") && !celula.getStringCellValue().trim().equalsIgnoreCase("inscrição estadual")) {
            throw new Exception("O valor da referencia H1 está errada. O valor deve ser Inscrição Estadual");
        }
        if (celula.getReference().equals("I1") && !celula.getStringCellValue().trim().equalsIgnoreCase("rg")) {
            throw new Exception("O valor da referencia I1 está errada. O valor deve ser RG");
        }
        if (celula.getReference().equals("J1") && !celula.getStringCellValue().trim().equalsIgnoreCase("data nascimento")) {
            throw new Exception("O valor da referencia J1 está errada. O valor deve ser Data Nascimento");
        }
        if (celula.getReference().equals("K1") && !celula.getStringCellValue().trim().equalsIgnoreCase("sexo")) {
            throw new Exception("O valor da referencia K1 está errada. O valor deve ser Sexo");
        }
        if (celula.getReference().equals("L1") && !celula.getStringCellValue().trim().equalsIgnoreCase("cep")) {
            throw new Exception("O valor da referencia L1 está errada. O valor deve ser CEP");
        }
        if (celula.getReference().equals("M1") && !celula.getStringCellValue().trim().equalsIgnoreCase("endereço")) {
            throw new Exception("O valor da referencia M1 está errada. O valor deve ser Endereço");
        }
        if (celula.getReference().equals("N1") && !celula.getStringCellValue().trim().equalsIgnoreCase("complemento")) {
            throw new Exception("O valor da referencia N1 está errada. O valor deve ser Complemento");
        }
        if (celula.getReference().equals("O1") && !celula.getStringCellValue().trim().equalsIgnoreCase("setor")) {
            throw new Exception("O valor da referencia O1 está errada. O valor deve ser Setor");
        }
        if (celula.getReference().equals("P1") && !celula.getStringCellValue().trim().equalsIgnoreCase("telefone comercial")) {
            throw new Exception("O valor da referencia P1 está errada. O valor deve ser Telefone Comercial");
        }
        if (celula.getReference().equals("Q1") && !celula.getStringCellValue().trim().equalsIgnoreCase("telefone recado")) {
            throw new Exception("O valor da referencia Q1 está errada. O valor deve ser Telefone Recado");
        }
        if (celula.getReference().equals("R1") && !celula.getStringCellValue().trim().equalsIgnoreCase("celular")) {
            throw new Exception("O valor da referencia R1 está errada. O valor deve ser Celular");
        }
        if (celula.getReference().equals("S1") && !celula.getStringCellValue().trim().equalsIgnoreCase("skype")) {
            throw new Exception("O valor da referencia S1 está errada. O valor deve ser Skype");
        }
        if (celula.getReference().equals("T1") && !celula.getStringCellValue().trim().equalsIgnoreCase("obs")) {
            throw new Exception("O valor da referencia T1 está errada. O valor deve ser obs.");
        }
        if (celula.getReference().equals("U1") && !celula.getStringCellValue().trim().equalsIgnoreCase("nome batismo")) {
            throw new Exception("O valor da referencia U1 está errada. O valor deve ser Nome Batismo");
        }
    }

    public Boolean realizarLeituraLinha(XSSFRow linha, RegistroEntradaVO registroEntradaVO, UsuarioVO usuarioLogado, int posicaoLinha, List<String> listaErros) throws Exception {
        RegistroEntradaProspectsVO registroEntradaProspectsVO = new RegistroEntradaProspectsVO();
        ProspectsVO prospectsVO = new ProspectsVO();
        prospectsVO.setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum.REGISTROENTRADA);
        int posicaoColuna = 0;
        /* A variavel j vai somente ate a 5 pois nao preciso validar as proximas colunas */
        if (linha == null) {
            return true;
        }
        for (int j = 0; j <= 20; j++) {
            XSSFCell celula = linha.getCell(j);
            if (posicaoLinha == 0) {
                realizarValidacaoCabecarioExcel(celula);
            } else {
                posicaoColuna = executarPreencimentoRegistroEntradaVO(prospectsVO, registroEntradaProspectsVO, celula, usuarioLogado, j, posicaoLinha, listaErros);
                if (posicaoColuna >= 20) {
                    break;
                }
            }
        }
        if (posicaoLinha != 0) {
            adicionarRegistroEntradaProspectsVO(registroEntradaVO, registroEntradaProspectsVO, prospectsVO);
        }
        return false;
    }

    private void adicionarRegistroEntradaProspectsVO(RegistroEntradaVO registro, RegistroEntradaProspectsVO registroEntradaProspectsVO, ProspectsVO prospectsVO) throws Exception {
        if (prospectsVO.getNome() != null && !prospectsVO.getNome().trim().equals("")) {
//                for(RegistroEntradaProspectsVO registroEntradaProspectsVO2: registro.getRegistroEntradaProspectsVOs()){
//                    if(registroEntradaProspectsVO2.getProspects().getNome().equalsIgnoreCase(prospectsVO.getNome())
//                            && registroEntradaProspectsVO2.getProspects().getEmailPrincipal().equalsIgnoreCase(prospectsVO.getEmailPrincipal())){
//                        return;
//                    }
//                }
                prospectsVO.setUnidadeEnsino(registro.getUnidadeEnsino());
                registroEntradaProspectsVO.setProspects(prospectsVO);
                registro.getRegistroEntradaProspectsVOs().add(registroEntradaProspectsVO);
        }
    }

    public int executarPreencimentoRegistroEntradaVO(ProspectsVO prospectsVO, RegistroEntradaProspectsVO registroEntradaProspectsVO, XSSFCell celula, UsuarioVO usuarioLogado, int posicaoColuna, int posicaoLinha, List<String> listaErros) throws Exception {
        try {
            ProspectsVO prospectExistente = new ProspectsVO();

            if (posicaoColuna == 0) {
                if (celula == null || celula.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                    prospectsVO.setTipoProspect(TipoProspectEnum.FISICO);
                } else if (!celula.getStringCellValue().trim().equals("")) {
                    if (!Uteis.removerAcentos(celula.getStringCellValue()).equalsIgnoreCase(TipoProspectEnum.FISICO.toString()) && !Uteis.removerAcentos(celula.getStringCellValue()).equalsIgnoreCase(TipoProspectEnum.JURIDICO.toString())) {
                        posicaoColuna++;
                        posicaoLinha++;
                        listaErros.add("Dado inconsistente na linha: " + posicaoLinha + ", coluna: " + (posicaoColuna));
                        posicaoColuna--;
                        posicaoLinha--;
                    } else {
                        prospectsVO.setTipoProspect(TipoProspectEnum.valueOf(Uteis.removerAcentos(celula.getStringCellValue()).toUpperCase()));
                    }
                }
            } else if (posicaoColuna == 1) {
                prospectsVO.setNome(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getNome().length() > 150){
//                    prospectsVO.setNome(prospectsVO.getNome().substring(0, 149));
//                }
                if (prospectsVO.getNome().equals("") || celula.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                    return 18;
                }
            } else if (posicaoColuna == 2) {
              
                prospectsVO.setEmailPrincipal(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getEmailPrincipal().length() > 100){
//                    prospectsVO.setEmailPrincipal(prospectsVO.getEmailPrincipal().substring(0, 99));
//                }
				if (!Uteis.isAtributoPreenchido(prospectsVO.getEmailPrincipal())) {
					posicaoLinha++;
//					listaErros.add("O valor da coluna email deve ser informado na linha " + posicaoLinha + ".");
					posicaoLinha--;
				} else {
					prospectExistente = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(prospectsVO.getEmailPrincipal(), false, usuarioLogado);
					if (prospectExistente.getCodigo() != null && prospectExistente.getCodigo() != 0) {
						registroEntradaProspectsVO.setExisteProspects(Boolean.TRUE);
						prospectsVO.setCodigo(prospectExistente.getCodigo());
						prospectsVO.setNome(prospectExistente.getNome());
						prospectsVO.setEmailPrincipal(prospectExistente.getEmailPrincipal());
						prospectsVO.setTelefoneResidencial(prospectExistente.getTelefoneResidencial());
						return 18;
					}
				}
            } else if (posicaoColuna == 3) {                
                prospectsVO.setTelefoneResidencial(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                
                if (Uteis.isAtributoPreenchido(prospectsVO.getTelefoneResidencial()) && Uteis.removeCaractersEspeciais(prospectsVO.getTelefoneResidencial()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna Telefone Residencial com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getTelefoneResidencial().length() > 20){
//                    prospectsVO.setTelefoneResidencial(prospectsVO.getTelefoneResidencial().substring(0, 19));
//                }
            } else if (posicaoColuna == 4 && prospectsVO.getFisico()) {                
                prospectsVO.setCpf(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                if (Uteis.isAtributoPreenchido(prospectsVO.getCpf()) && Uteis.removeCaractersEspeciais(prospectsVO.getCpf()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna CPF com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getCpf().length() > 14){
//                    prospectsVO.setCpf(prospectsVO.getCpf().substring(0, 13));
//                }
            } else if (posicaoColuna == 5 && prospectsVO.getJuridico()) {
                prospectsVO.setCnpj(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                if (Uteis.isAtributoPreenchido(prospectsVO.getCnpj()) && Uteis.removeCaractersEspeciais(prospectsVO.getCnpj()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna CNPJ com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getCnpj().length() > 18){
//                    prospectsVO.setCnpj(prospectsVO.getCnpj().substring(0, 17));
//                }
            } else if (posicaoColuna == 6 && prospectsVO.getJuridico()) {
                prospectsVO.setRazaoSocial(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getRazaoSocial().length() > 70){
//                    prospectsVO.setRazaoSocial(prospectsVO.getRazaoSocial().substring(0, 69));
//                }
            } else if (posicaoColuna == 7 && prospectsVO.getJuridico()) {
                prospectsVO.setInscricaoEstadual(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getInscricaoEstadual().length() > 25){
//                    prospectsVO.setInscricaoEstadual(prospectsVO.getInscricaoEstadual().substring(0, 24));
//                }
            } else if (posicaoColuna == 8 && prospectsVO.getFisico()) {
                prospectsVO.setRg(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                if (Uteis.isAtributoPreenchido(prospectsVO.getRg()) && Uteis.removeCaractersEspeciais(prospectsVO.getRg()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna RG com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getRg().length() > 25){
//                    prospectsVO.setRg(prospectsVO.getRg().substring(0, 24));
//                }
            } else if (posicaoColuna == 9 && prospectsVO.getFisico()) {
                prospectsVO.setDataNascimento(realizarObterValorDate(celula));
            } else if (posicaoColuna == 10 && prospectsVO.getFisico()) {
                if (realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros).equalsIgnoreCase("masculino")) {
                    prospectsVO.setSexo("M");
                } else if (realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros).equalsIgnoreCase("feminino")) {
                    prospectsVO.setSexo("F");
                } else {
                    prospectsVO.setSexo("");
                }
            } else if (posicaoColuna == 11) {
                prospectsVO.setCEP(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getCEP().length() > 10){
//                    prospectsVO.setCEP(prospectsVO.getCEP().substring(0, 9));
//                }
            } else if (posicaoColuna == 12) {
                prospectsVO.setEndereco(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getEndereco().length() > 100){
//                    prospectsVO.setEndereco(prospectsVO.getEndereco().substring(0, 99));
//                }
            } else if (posicaoColuna == 13) {
                prospectsVO.setComplemento(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getComplemento().length() > 100){
//                    prospectsVO.setComplemento(prospectsVO.getComplemento().substring(0, 99));
//                }
            } else if (posicaoColuna == 14) {
                prospectsVO.setSetor(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getSetor().length() > 100){
//                    prospectsVO.setSetor(prospectsVO.getSetor().substring(0, 99));
//                }
            } else if (posicaoColuna == 15) {
                prospectsVO.setTelefoneComercial(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                if (Uteis.isAtributoPreenchido(prospectsVO.getTelefoneComercial()) && Uteis.removeCaractersEspeciais(prospectsVO.getTelefoneComercial()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna Telefone Comercial com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getTelefoneComercial().length() > 20){
//                    prospectsVO.setTelefoneComercial(prospectsVO.getTelefoneComercial().substring(0, 19));
//                }
            } else if (posicaoColuna == 16) {
                prospectsVO.setTelefoneRecado(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                if (Uteis.isAtributoPreenchido(prospectsVO.getTelefoneRecado()) && Uteis.removeCaractersEspeciais(prospectsVO.getTelefoneRecado()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna Telefone Recado com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getTelefoneRecado().length() > 20){
//                    prospectsVO.setTelefoneRecado(prospectsVO.getTelefoneRecado().substring(0, 19));
//                }
            } else if (posicaoColuna == 17) {
                prospectsVO.setCelular(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
                if (Uteis.isAtributoPreenchido(prospectsVO.getCelular()) && Uteis.removeCaractersEspeciais(prospectsVO.getCelular()).trim().matches("[a-zA-Z]+")) {
                	throw new Exception("A Coluna Celular com valor " + celula + " está Incorreto.");
                }
//                if(prospectsVO.getCelular().length() > 20){
//                    prospectsVO.setCelular(prospectsVO.getCelular().substring(0, 19));
//                }
            } else if (posicaoColuna == 18) {
                prospectsVO.setSkype(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//                if(prospectsVO.getSkype().length() > 100){
//                    prospectsVO.setSkype(prospectsVO.getSkype().substring(0, 99));
//                }
            } else if (posicaoColuna == 19) {
                String obs = realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros);
                        if(obs != null && !obs.trim().isEmpty()){
                            prospectsVO.getHistoricoFollowUp().setDataregistro(new Date());
                            prospectsVO.getHistoricoFollowUp().getResponsavel().setCodigo(usuarioLogado.getCodigo());
                            prospectsVO.getHistoricoFollowUp().getResponsavel().setNome(usuarioLogado.getNome());
                            prospectsVO.getHistoricoFollowUp().setObservacao(obs.trim());
                        }
            } else if (posicaoColuna == 20) {
                prospectsVO.setNomeBatismo(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
//              if(prospectsVO.getNome().length() > 150){
//                  prospectsVO.setNome(prospectsVO.getNome().substring(0, 149));
//              }
              if (prospectsVO.getNomeBatismo().equals("") || celula.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                  return 18;
              }
          }
        } catch (Exception e) {
            throw e;
        }
        return posicaoColuna;

    }

    public String realizarObterValorString(XSSFCell celula, int coluna, int linha, List<String> listaErros) throws Exception {
        if ((celula == null || XSSFCell.CELL_TYPE_BLANK == celula.getCellType()) && (coluna == 1)) {
            coluna++;
            linha++;
            listaErros.add("Dado inconsistente na linha: " + linha + ", coluna: " + coluna);
            coluna--;
            linha--;
            return null;
        } else if (celula == null || XSSFCell.CELL_TYPE_BLANK == celula.getCellType()) {
            return "";
        } else if (XSSFCell.CELL_TYPE_STRING == celula.getCellType()) {
            return celula.getStringCellValue();
        } else if (XSSFCell.CELL_TYPE_NUMERIC == celula.getCellType()) {
            return new BigDecimal(celula.getNumericCellValue()).toPlainString();
        } else {
            listaErros.add("Valor incorreto para coluna " + celula.getReference());
            return null;
        }
    }

    public Date realizarObterValorDate(XSSFCell celula) throws Exception {
        if (celula == null
                || XSSFCell.CELL_TYPE_BLANK == celula.getCellType()
                || (XSSFCell.CELL_TYPE_STRING == celula.getCellType() && celula.getStringCellValue().isEmpty())) {
            return null;
        } else if (XSSFCell.CELL_TYPE_NUMERIC == celula.getCellType()) {
            if (celula.getDateCellValue().after(new Date())) {
                throw new Exception("Valor incorreto para coluna " + celula.getReference());
            } else {
                return celula.getDateCellValue();
            }
        } else if (XSSFCell.CELL_TYPE_STRING == celula.getCellType()) {
            throw new Exception("Valor incorreto para coluna " + celula.getReference());
        }
        return null;
    }

    public void realizarLeituraArquivoCsv(FileUploadEvent uploadEvent, RegistroEntradaVO entradaVO, String delimitador, UsuarioVO usuarioLogado) throws Exception {
        if (delimitador.isEmpty()) {
            throw new Exception("O campo DELIMITADOR deve ser informado.");
        }
        
//        UploadedFile item = uploadEvent.getUploadedFile().;
        
//        String caminho = uploadEvent.getUploadItem().getFile().getCanonicalPath();
        processarArquivo(uploadEvent.getUploadedFile().getInputStream(), delimitador, entradaVO, usuarioLogado);
    }

    private void processarArquivo(InputStream is, String delimitador, RegistroEntradaVO registroEntradaVO, UsuarioVO usuarioLogado) throws Exception {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String linha = "";
        String st[] = null;
        Boolean jaRealizouValidacao = false;
        try {
            while ((linha = reader.readLine()) != null) {
                st = linha.split(delimitador);
                realizarLeituraCSVDados(registroEntradaVO, st, jaRealizouValidacao, usuarioLogado);
                jaRealizouValidacao = true;
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        } finally {
            
            reader = null;
            linha = null;
            st = null;
            jaRealizouValidacao = null;
        }
    }

    private void realizarLeituraCSVDados(RegistroEntradaVO registroEntradaVO, String[] st, Boolean jaRealizouValidacao, UsuarioVO usuarioLogado) throws Exception {
        ProspectsVO prospectsVO = new ProspectsVO();
        RegistroEntradaProspectsVO registroEntradaProspectsVO = new RegistroEntradaProspectsVO();
        int posicao = 1;

        for (String campo : st) {
            if (!jaRealizouValidacao) {
                realizarValidacaoCabecario(campo, posicao);
            } else {
                posicao = executarPreencimentoRegistroEntradaVOPorArquivoCVS(prospectsVO, registroEntradaProspectsVO, campo, posicao, usuarioLogado);
                if (posicao == 20) {
                    adicionarRegistroEntradaProspectsVO(registroEntradaVO, registroEntradaProspectsVO, prospectsVO);
                    break;
                }
                if (posicao == st.length) {
                    adicionarRegistroEntradaProspectsVO(registroEntradaVO, registroEntradaProspectsVO, prospectsVO);
                    break;
                }
            }
            posicao++;
            campo = null;
        }

    }

    public void realizarValidacaoCabecario(String campo, int posicao) throws Exception {
        if (!campo.trim().equalsIgnoreCase("tipo prospect") && posicao == 1) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Tipo Prospect ");
        }
        if (!campo.trim().equalsIgnoreCase("nome social") && posicao == 2) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Nome Nome ");
        }
        if (!campo.trim().equalsIgnoreCase("email") && posicao == 3) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Email ");
        }
        if (!campo.trim().equalsIgnoreCase("telefone residencial") && posicao == 4) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Telefone Residencial");
        }
        if (!campo.trim().equalsIgnoreCase("cpf") && posicao == 5) {
            throw new Exception("O valor da referencia está errada. O valor deve ser CPF");
        }
        if (!campo.trim().equalsIgnoreCase("cnpj") && posicao == 6) {
            throw new Exception("O valor da referencia está errada. O valor deve ser CNPJ");
        }
        if (!campo.trim().equalsIgnoreCase("razão social") && posicao == 7) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Razão Social");
        }
        if (!campo.trim().equalsIgnoreCase("inscrição estadual") && posicao == 8) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Inscrição Estadual");
        }
        if (!campo.trim().equalsIgnoreCase("rg") && posicao == 9) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Rg");
        }
        if (!campo.trim().equalsIgnoreCase("data nascimento") && posicao == 10) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Data Nascimento");
        }
        if (!campo.trim().equalsIgnoreCase("sexo") && posicao == 11) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Sexo");
        }
        if (!campo.trim().equalsIgnoreCase("cep") && posicao == 12) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Cep");
        }
        if (!campo.trim().equalsIgnoreCase("endereço") && posicao == 13) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Endereço");
        }
        if (!campo.trim().equalsIgnoreCase("complemento") && posicao == 14) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Complemento");
        }
        if (!campo.trim().equalsIgnoreCase("setor") && posicao == 15) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Setor");
        }
        if (!campo.trim().equalsIgnoreCase("telefone comercial") && posicao == 16) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Telefone Comercial");
        }
        if (!campo.trim().equalsIgnoreCase("telefone recado") && posicao == 17) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Telefone Recado");
        }
        if (!campo.trim().equalsIgnoreCase("celular") && posicao == 18) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Celular");
        }
        if (!campo.trim().equalsIgnoreCase("skype") && posicao == 19) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Skype");
        }
        if (!campo.trim().equalsIgnoreCase("nome batismo") && posicao == 2) {
            throw new Exception("O valor da referencia está errada. O valor deve ser Nome Batismo ");
        }
    }

    public int executarPreencimentoRegistroEntradaVOPorArquivoCVS(ProspectsVO prospectsVO, RegistroEntradaProspectsVO registroEntradaProspectsVO, String campo, int posicao, UsuarioVO usuarioLogado) throws Exception {
        ProspectsVO prospectsExistente = new ProspectsVO();
        try {
            if (posicao == 1) {
                if (campo.isEmpty()) {
                    prospectsVO.setTipoProspect(TipoProspectEnum.FISICO);
                } else {
                    prospectsVO.setTipoProspect(TipoProspectEnum.valueOf(Uteis.removerAcentos(campo.toUpperCase())));
                }
            } else if (posicao == 2) {
                if (campo == null || campo.isEmpty()) {
                    throw new Exception("Valor incorreto para coluna " + posicao);
                }
                prospectsVO.setNome(campo);
            } else if (posicao == 3) {
                prospectsVO.setEmailPrincipal(campo);
                prospectsExistente = getFacadeFactory().getProspectsFacade().consultarPorEmail(prospectsVO.getEmailPrincipal(), false, usuarioLogado);
                if (prospectsExistente.getCodigo() != null && prospectsExistente.getCodigo() != 0) {
                    registroEntradaProspectsVO.setExisteProspects(Boolean.TRUE);
                    prospectsVO.setCodigo(prospectsExistente.getCodigo());
                    prospectsVO.setNome(prospectsExistente.getNome());
                    prospectsVO.setEmailPrincipal(prospectsExistente.getEmailPrincipal());
                    prospectsVO.setTelefoneResidencial(prospectsExistente.getTelefoneResidencial());
                    return 20;
                }
            } else if (posicao == 4) {
                if (campo == null || campo.isEmpty()) {
                    throw new Exception("Valor incorreto para coluna " + posicao);
                }
                prospectsVO.setTelefoneResidencial(campo);
            } else if (posicao == 5 && prospectsVO.getFisico()) {
                prospectsVO.setCpf(campo);
            } else if (posicao == 6 && prospectsVO.getJuridico()) {
                prospectsVO.setCnpj(campo);
            } else if (posicao == 7 && prospectsVO.getJuridico()) {
                prospectsVO.setRazaoSocial(campo);
            } else if (posicao == 8 && prospectsVO.getJuridico()) {
                prospectsVO.setInscricaoEstadual(campo);
            } else if (posicao == 9 && prospectsVO.getFisico()) {
                prospectsVO.setRg(campo);
            } else if (posicao == 10 && prospectsVO.getFisico()) {
                prospectsVO.setDataNascimento(Uteis.getData(campo, "dd/MM/yyyy"));
            } else if (posicao == 11 && prospectsVO.getFisico()) {
                if (campo.equalsIgnoreCase("masculino")) {
                    prospectsVO.setSexo("M");
                } else if (campo.equalsIgnoreCase("feminino")) {
                    prospectsVO.setSexo("F");
                } else {
                    prospectsVO.setSexo("");
                }
            } else if (posicao == 12) {
                prospectsVO.setCEP(campo);
            } else if (posicao == 13) {
                prospectsVO.setEndereco(campo);
            } else if (posicao == 14) {
                prospectsVO.setComplemento(campo);
            } else if (posicao == 15) {
                prospectsVO.setSetor(campo);
            } else if (posicao == 16) {
                prospectsVO.setTelefoneComercial(campo);
            } else if (posicao == 17) {
                prospectsVO.setTelefoneRecado(campo);
            } else if (posicao == 18) {
                prospectsVO.setCelular(campo);
            } else if (posicao == 19) {
                prospectsVO.setSkype(campo);
            }else if (posicao == 20) {
                if (campo == null || campo.isEmpty()) {
                    throw new Exception("Valor incorreto para coluna " + posicao);
                }
                prospectsVO.setNomeBatismo(campo);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            prospectsExistente = null;
        }
        return posicao;
    }

    public List preencherListaDeProspectsParaRelatorio(List<ProspectsVO> prospectsVOs) {
        ProspectsVO prospectsVO = new ProspectsVO();
        Integer quantidadeDeRegistro = 0;
        while (quantidadeDeRegistro <= 4) {
            prospectsVO = new ProspectsVO();
            if (quantidadeDeRegistro == 0) {
                prospectsVO.setTipoProspect(TipoProspectEnum.FISICO);
                prospectsVO.setNome("prospect1");
                prospectsVO.setEmailPrincipal("teste@teste.com");
                prospectsVO.setTelefoneResidencial("(62) 3297-3387");
                prospectsVO.setCpf("759.110.861-15");
                prospectsVO.setCnpj("");
                prospectsVO.setRazaoSocial("");
                prospectsVO.setInscricaoEstadual("");
                prospectsVO.setRg("");
                prospectsVO.setDataNascimentoRel("17/02/2011");
                prospectsVO.setSexo("masculino");
                prospectsVO.setCEP("76.659-970");
                prospectsVO.setEndereco("AV. JOSE ROBERTO");
                prospectsVO.setComplemento("");
                prospectsVO.setSetor("Centro");
                prospectsVO.setTelefoneRecado("");
                prospectsVO.setCelular("");
                prospectsVO.setSkype("skype1.com");
                prospectsVO.setNomeBatismo("prospect1");
                prospectsVOs.add(prospectsVO);
                quantidadeDeRegistro++;

            } else if (quantidadeDeRegistro == 1) {
                prospectsVO.setTipoProspect(TipoProspectEnum.FISICO);
                prospectsVO.setNome("prospect fisico 2");
                prospectsVO.setEmailPrincipal("teste1@teste.com");
                prospectsVO.setTelefoneResidencial("(62)3297-3387");
                prospectsVO.setCpf("534.919.711-87");
                prospectsVO.setCnpj("");
                prospectsVO.setRazaoSocial("");
                prospectsVO.setInscricaoEstadual("");
                prospectsVO.setRg("");
                prospectsVO.setDataNascimentoRel("09/06/2011");
                prospectsVO.setSexo("feminino");
                prospectsVO.setCEP("74.475-248");
                prospectsVO.setEndereco("RUA ALECRIM QD. 224 LT. 27");
                prospectsVO.setComplemento("");
                prospectsVO.setSetor("Centro");
                prospectsVO.setTelefoneRecado("");
                prospectsVO.setCelular("");
                prospectsVO.setSkype("");
                prospectsVO.setNomeBatismo("prospect fisico 2");
                prospectsVOs.add(prospectsVO);
                quantidadeDeRegistro++;

            } else if (quantidadeDeRegistro == 2) {
                prospectsVO.setTipoProspect(TipoProspectEnum.NENHUM);
                prospectsVO.setNome("prospect fisico 3");
                prospectsVO.setEmailPrincipal("");
                prospectsVO.setTelefoneResidencial("(62)3297-3387");
                prospectsVO.setCpf("214.889.591-53");
                prospectsVO.setCnpj("");
                prospectsVO.setRazaoSocial("");
                prospectsVO.setInscricaoEstadual("");
                prospectsVO.setRg("");
                prospectsVO.setDataNascimentoRel("29/12/2009");
                prospectsVO.setSexo("");
                prospectsVO.setCEP("");
                prospectsVO.setEndereco("");
                prospectsVO.setComplemento("");
                prospectsVO.setSetor("");
                prospectsVO.setTelefoneRecado("");
                prospectsVO.setCelular("");
                prospectsVO.setSkype("");
                prospectsVO.setNomeBatismo("prospect fisico 2");
                prospectsVOs.add(prospectsVO);
                quantidadeDeRegistro++;

            } else if (quantidadeDeRegistro == 3) {
                prospectsVO.setTipoProspect(TipoProspectEnum.JURIDICO);
                prospectsVO.setNome("prospect fisico 3");
                prospectsVO.setEmailPrincipal("teste2893@teste.com");
                prospectsVO.setTelefoneResidencial("(62)3297-3387");
                prospectsVO.setCpf("");
                prospectsVO.setCnpj("04.776.081/0002-58");
                prospectsVO.setRazaoSocial("A F DA SILVA INDUSTRIA E COMERCIO DE MOVEIS");
                prospectsVO.setInscricaoEstadual("152586580");
                prospectsVO.setRg("");
                prospectsVO.setDataNascimentoRel("09/06/2011");
                prospectsVO.setSexo("masculino");
                prospectsVO.setCEP("74.735-020");
                prospectsVO.setEndereco("RUA ARARAPES CHACARA 01 CASA1");
                prospectsVO.setComplemento("Jardim Califórnia");
                prospectsVO.setSetor("");
                prospectsVO.setTelefoneRecado("");
                prospectsVO.setCelular("");
                prospectsVO.setSkype("");
                prospectsVO.setNomeBatismo("prospect juridico 1");
                prospectsVOs.add(prospectsVO);
                quantidadeDeRegistro++;

            } else if (quantidadeDeRegistro == 4) {
                prospectsVO.setTipoProspect(TipoProspectEnum.JURIDICO);
                prospectsVO.setNome("prospect  juridico 2");
                prospectsVO.setEmailPrincipal("teste1826@teste.com");
                prospectsVO.setTelefoneResidencial("(63)3476-1681");
                prospectsVO.setCpf("");
                prospectsVO.setCnpj("09.441.550/0001-10");
                prospectsVO.setRazaoSocial("SUITE MOTEL LTDA");
                prospectsVO.setInscricaoEstadual("101556411");
                prospectsVO.setRg("");
                prospectsVO.setDataNascimentoRel("09/06/2011");
                prospectsVO.setSexo("masculino");
                prospectsVO.setCEP("74.000-000");
                prospectsVO.setEndereco("AV ANHANGUERA");
                prospectsVO.setComplemento("");
                prospectsVO.setSetor("JARDIM LEBLON");
                prospectsVO.setTelefoneRecado("");
                prospectsVO.setCelular("");
                prospectsVO.setSkype("");
                prospectsVO.setNomeBatismo("prospect  juridico 2");
                prospectsVOs.add(prospectsVO);

                quantidadeDeRegistro++;

            }

        }
        prospectsVO = null;
        quantidadeDeRegistro = null;
        return prospectsVOs;
    }

    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator);
    }
    
    public void verificarDuplicidadeNomeEmail(List<RegistroEntradaProspectsVO> registroEntradaVO, List<String> listaErros) {
    	
    	for(RegistroEntradaProspectsVO obj : registroEntradaVO) {
    		List<RegistroEntradaProspectsVO> registroEntradaVOAux = new ArrayList<RegistroEntradaProspectsVO>();
    		registroEntradaVOAux.addAll(registroEntradaVO);
    		registroEntradaVOAux.remove(obj);
    		for(RegistroEntradaProspectsVO obj2 : registroEntradaVOAux) {
    			if(obj.equalsNomeEmail(obj2)){
    				listaErros.add("O prospect com nome " + obj.getProspects().getNome() + " foi encontrado mais de uma vez com o mesmo e-mail.");
    			}
    		}
    	}
    	
    	
    }
}
