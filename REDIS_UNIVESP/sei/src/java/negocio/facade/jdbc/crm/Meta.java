package negocio.facade.jdbc.crm;

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

import negocio.comuns.administrativo.CampanhaMidiaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.MetaVO;
import negocio.comuns.crm.enumerador.TipoMetaEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboMetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.MetaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MetaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MetaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MetaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Meta extends ControleAcesso implements MetaInterfaceFacade {

    protected static String idEntidade;

    public Meta() throws Exception {
        super();
        setIdEntidade("Meta");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MetaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MetaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MetaVO obj, List objetos, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            Meta.incluir(getIdEntidade(), true, usuarioVO);
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO Meta( descricao, tipoMeta, considerarSabado, metaParaCampanhaCaptacao, cargo ) VALUES ( ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setString(2, obj.getTipoMeta().toString());
                    sqlInserir.setBoolean(3, obj.isConsiderarSabado().booleanValue());
                    sqlInserir.setBoolean(4, obj.isMetaParaCampanhaCaptacao().booleanValue());
                    sqlInserir.setInt(5, obj.getCargo().getCodigo());
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
            getFacadeFactory().getMetaItemFacade().incluirMetaItens(obj.getCodigo(), objetos, usuarioVO);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MetaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MetaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MetaVO obj, List objetos, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            Meta.alterar(getIdEntidade(), true, usuarioVO);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE Meta set descricao=?, tipoMeta=?, considerarSabado=?, metaParaCampanhaCaptacao=?, cargo=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setString(2, obj.getTipoMeta().toString());
                    sqlAlterar.setBoolean(3, obj.isConsiderarSabado().booleanValue());
                    sqlAlterar.setBoolean(4, obj.isMetaParaCampanhaCaptacao().booleanValue());
                    sqlAlterar.setInt(5, obj.getCargo().getCodigo());
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getMetaItemFacade().alterarMetaItens(obj.getCodigo(), objetos, usuarioVO);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MetaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MetaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MetaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            getFacadeFactory().getMetaItemFacade().excluirMetaItens(obj.getCodigo(), usuarioVO);
            Meta.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Meta WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param MetaVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(MetaVO obj, List objetos, UsuarioVO usuarioVO) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, objetos, usuarioVO);
        } else {
            alterar(obj, objetos, usuarioVO);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>MetaVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDadosExclusao(MetaVO obj) throws Exception {
        if (obj.isNovoObj()) {
            throw new Exception("Impossivel excluir, meta ainda não adicionada");
        }
    }

    public void validarDados(MetaVO obj) throws Exception {
        if (obj.getDescricao().equals("")) {
            throw new Exception("O campo DESCRIÇÃO deve ser informado");
        }
        if ((obj.getCargo() == null)
                || (obj.getCargo().getCodigo().intValue() == 0)) {
            throw new Exception("O campo CARGO deve ser informado");
        }
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>MetaVO</code>.
     */
    public void validarUnicidade(List<MetaVO> lista, MetaVO obj) throws ConsistirException {
        for (MetaVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(MetaVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setDescricao(obj.getDescricao().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela MetaCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<MetaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboMetaEnum.DESCRICAO.toString())) {
            return getFacadeFactory().getMetaFacade().consultarPorDescricao(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboMetaEnum.NOME_CARGO.toString())) {
            return getFacadeFactory().getMetaFacade().consultarPorNomeCargo(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>Meta</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Cargo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MetaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        // ControleAcesso.consultar(getIdEntidade(), controlarAcesso);
        String sqlStr = "SELECT Meta.* FROM Meta, Cargo WHERE Meta.cargo = Cargo.codigo and upper( Cargo.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Cargo.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Meta</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MetaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        // ControleAcesso.consultar(getIdEntidade(), controlarAcesso);
        String sqlStr = "SELECT * FROM Meta WHERE upper( descricao ) like upper('" + valorConsulta.toUpperCase() + "%') ORDER BY descricao";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Meta</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MetaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //  ControleAcesso.consultar(getIdEntidade(), controlarAcesso);
        String sqlStr = "SELECT * FROM Meta WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MetaVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>MetaVO</code>.
     * @return  O objeto da classe <code>MetaVO</code> com os dados devidamente montados.
     */
    public static MetaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        MetaVO obj = new MetaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.getCargo().setCodigo(dadosSQL.getInt("cargo"));
        obj.setConsiderarSabado(dadosSQL.getBoolean("considerarSabado"));
        obj.setMetaParaCampanhaCaptacao(dadosSQL.getBoolean("metaParaCampanhaCaptacao"));
        obj.setTipoMeta(TipoMetaEnum.valueOf(dadosSQL.getString("tipoMeta")));
        obj.setNovoObj(false);
        montarDadosCargo(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
            obj.setListaMetaItem(getFacadeFactory().getMetaItemFacade().montarListaMetaItem(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CargoVO</code> relacionado ao objeto <code>MetaVO</code>.
     * Faz uso da chave primária da classe <code>CargoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCargo(MetaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCargo().getCodigo().intValue() == 0) {
            obj.setCargo(new CargoVO());
            return;
        }
        obj.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargo().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MetaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MetaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //    ControleAcesso.consultar(getIdEntidade(), false);
        String sql = "SELECT * FROM Meta WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Meta ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Meta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Meta.idEntidade = idEntidade;
    }
    /**
     * Operação responsável por adicionar um novo objeto da classe <code>RegistroEntradaProspectsVO</code>
     * ao List <code>registroEntradaProspectsVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>RegistroEntradaProspects</code> - getProspects().getCodigo() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>RegistroEntradaProspectsVO</code> que será adiocionado ao Hashtable correspondente.
     */
    
    public List<CampanhaMidiaVO> consultarCampanhaMidiaPorCampanha(Integer campanha, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select campanhamidia.codigo AS \"campanhamidia.codigo\", campanhamidia.impactoEsperado AS \"campanhamidia.impactoEsperado\", campanhamidia.datainiciovinculacao AS \"campanhamidia.datainiciovinculacao\", ");
    	sb.append(" campanhamidia.datafimvinculacao AS \"campanhamidia.datafimvinculacao\", campanhamidia.apresentarpreinscricao AS \"campanhamidia.apresentarpreinscricao\", campanhamidia.campanha AS \"campanhamidia.campanha\",  ");
    	sb.append(" tipoMidiaCaptacao.nomeMidia AS \"tipoMidiaCaptacao.nomeMidia\", tipoMidiaCaptacao.codigo AS \"tipoMidiaCaptacao.codigo\" ");
    	sb.append(" from campanhamidia  ");
    	sb.append(" LEFT JOIN tipoMidiaCaptacao ON tipoMidiaCaptacao.codigo = campanhamidia.tipomidia   ");
    	sb.append(" where campanhamidia.campanha = ").append(campanha);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<CampanhaMidiaVO> campanhaMidiaVOs = new ArrayList<CampanhaMidiaVO>(0);
    	while (tabelaResultado.next()) {
    		CampanhaMidiaVO obj = new CampanhaMidiaVO();
    		obj.setCodigo(tabelaResultado.getInt("campanhamidia.codigo"));
    		obj.setImpactoEsperado(tabelaResultado.getInt("campanhamidia.impactoEsperado"));
    		obj.setDataInicioVinculacao(tabelaResultado.getDate("campanhamidia.datainiciovinculacao"));
    		obj.setDataFimVinculacao(tabelaResultado.getDate("campanhamidia.datafimvinculacao"));
    		obj.setApresentarPreInscricao(tabelaResultado.getBoolean("campanhamidia.apresentarpreinscricao"));
    		obj.getCampanha().setCodigo(tabelaResultado.getInt("campanhamidia.campanha"));
    		
    		obj.getTipoMidia().setNomeMidia(tabelaResultado.getString("tipoMidiaCaptacao.nomeMidia"));
    		obj.getTipoMidia().setCodigo(tabelaResultado.getInt("tipoMidiaCaptacao.codigo"));
    		
			campanhaMidiaVOs.add(obj);
		}
    	return campanhaMidiaVOs;
    }
}
