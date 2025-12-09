package negocio.facade.jdbc.financeiro;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CentroReceitaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CentroReceitaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CentroReceitaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CentroReceitaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CentroReceita extends ControleAcesso implements CentroReceitaInterfaceFacade {

    protected static String idEntidade;

    public CentroReceita() throws Exception {
        super();
        setIdEntidade("CentroReceita");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CentroReceitaVO</code>.
     */
    public CentroReceitaVO novo() throws Exception {
        CentroReceita.incluir(getIdEntidade());
        CentroReceitaVO obj = new CentroReceitaVO();
        return obj;
    }

    public void gerarIdentificadorMascaraCentroReceita2(CentroReceitaVO obj,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String mascaraCentroReceita = configuracaoFinanceiroVO.getMascaraCategoriaDespesa(), mascaraConsulta = "", mascaraCentroReceitaPrincipal = "";
		if (obj.verificarCentroReceitaPrimeiroNivel()) {
			mascaraConsulta = of(mascaraCentroReceita.split("\\.")).findFirst().orElse("");
		} else {
            CentroReceitaVO unVO = consultarPorChavePrimaria(obj.getCentroReceitaPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (Uteis.isAtributoPreenchido(unVO.getIdentificadorCentroReceita())) {
				mascaraCentroReceitaPrincipal = unVO.getIdentificadorCentroReceita();
				long nivel = mascaraCentroReceitaPrincipal.chars().filter(c -> c == '.').count() + 2;
				mascaraConsulta = of(mascaraCentroReceita.split("\\.")).limit(nivel).collect(joining("."));
			}
		}
		List<CentroReceitaVO> centroReceitaVOs = consultarUltimaMascaraGerada(mascaraConsulta, mascaraCentroReceitaPrincipal, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		List<String> mascaras = null;
		if (Uteis.isAtributoPreenchido(centroReceitaVOs) && Uteis.isAtributoPreenchido(centroReceitaVOs.get(0).getIdentificadorCentroReceita())) {
			mascaras = of(centroReceitaVOs.get(0).getIdentificadorCentroReceita().split("\\.")).collect(toList());
		}
		obj.setIdentificadorCentroReceita(Uteis.realizarGeracaoIdentificador("(Centro de Receita)", mascaraConsulta, mascaraCentroReceitaPrincipal, mascaras));
    }

	private void verificarNivelCentroReceita(CentroReceitaVO obj, UsuarioVO usuario, int niveis) throws Exception {
		CentroReceitaVO principal = getFacadeFactory().getCentroReceitaFacade().consultarPorCodigoCentroReceitaPrincipal(obj.getCentroReceitaPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (obj.obterNrNivelCentroReceita(principal) >= (niveis-1)) {
            throw new Exception("Não é possível CADASTRAR este PLANO DE CONTA DE NÍVEL. Número de níveis permitidos: " + niveis);
        }
	}

    public List<CentroReceitaVO> consultarUltimaMascaraGerada(String mascaraConsulta, String mascaraCentroReceitaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "select * from CentroReceita where char_length(identificadorCentroReceita) = " + mascaraConsulta.length() 
        + " and upper(identificadorCentroReceita) like('" + mascaraCentroReceitaPrincipal.toUpperCase() + "%') order by identificadorCentroReceita DESC LIMIT 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CentroReceitaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CentroReceitaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CentroReceitaVO obj,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
        	verificarNivelCentroReceita(obj, usuario, configuracaoFinanceiroVO.getNrNiveisCentroReceita());
        	if (!obj.getInformarManualmenteIdentificadorCentroReceita()) {
        		gerarIdentificadorMascaraCentroReceita2(obj, usuario, configuracaoFinanceiroVO);
        	}
            CentroReceitaVO.validarDados(obj);
            Uteis.validarIdentificador("(Centro de Receita)", obj.getIdentificadorCentroReceita(), configuracaoFinanceiroVO.getMascaraCentroReceita());
            CentroReceita.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO CentroReceita( descricao, centroReceitaPrincipal, identificadorCentroReceita, departamento, informarmanualmenteidentificadorcentroreceita ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    if (obj.getCentroReceitaPrincipal().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getCentroReceitaPrincipal().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setString(3, obj.getIdentificadorCentroReceita());
                    if (obj.getDepartamento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getDepartamento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setBoolean(5, obj.getInformarManualmenteIdentificadorCentroReceita());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));

            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CentroReceitaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CentroReceitaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CentroReceitaVO obj, final UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
            CentroReceitaVO.validarDados(obj);
            Uteis.validarIdentificador("(Centro de Receita)", obj.getIdentificadorCentroReceita(), configuracaoFinanceiroVO.getMascaraCentroReceita());
            CentroReceita.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE CentroReceita set descricao=?, centroReceitaPrincipal=?, identificadorCentroReceita=?, departamento=?, informarmanualmenteidentificadorcentroreceita=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    if (obj.getCentroReceitaPrincipal().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getCentroReceitaPrincipal().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setString(3, obj.getIdentificadorCentroReceita());
                    if (obj.getDepartamento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getDepartamento().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setBoolean(5, obj.getInformarManualmenteIdentificadorCentroReceita());
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CentroReceitaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CentroReceitaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CentroReceitaVO obj, final UsuarioVO usuarioVO) throws Exception {
        try {
            CentroReceita.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM CentroReceita WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CentroReceita</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Departamento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CentroReceitaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CentroReceitaVO> consultarPorNomeDepartamento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT CentroReceita.* FROM CentroReceita, Departamento WHERE CentroReceita.departamento = Departamento.codigo and upper( Departamento.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Departamento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CentroReceita</code> através do valor do atributo 
     * <code>String identificadorCentroReceita</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CentroReceitaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CentroReceitaVO> consultarPorIdentificadorCentroReceita(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CentroReceita WHERE upper( identificadorCentroReceita ) like upper(?) ORDER BY identificadorCentroReceita";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CentroReceita</code> através do valor do atributo 
     * <code>identificadorCentroReceita</code> da classe <code>CentroReceita</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CentroReceitaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CentroReceitaVO> consultarPorCentroReceitaPrincipal(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CentroReceita WHERE centroReceitaPrincipal >= " + valorConsulta.intValue() + " ORDER BY centroReceitaPrincipal";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CentroReceitaVO> consultarPorCentroReceitaPrincipalFilho(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CentroReceita WHERE centroReceitaPrincipal = " + valorConsulta.intValue() + " ORDER BY centroReceitaPrincipal";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CentroReceitaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CentroReceita WHERE sem_acentos(descricao) ilike(sem_acentos(?)) ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CentroReceitaVO> consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT cr1.* FROM CentroReceita as cr1, CentroReceita as cr2 WHERE cr1.centroReceitaPrincipal = cr2.codigo and upper( cr2.identificadorCentroReceita ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY cr1.identificadorCentroReceita, cr2.identificadorCentroReceita ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CentroReceita</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CentroReceitaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CentroReceitaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CentroReceita WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CentroReceitaVO</code> resultantes da consulta.
     */
    public static List<CentroReceitaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<CentroReceitaVO> vetResultado = new ArrayList<CentroReceitaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CentroReceitaVO</code>.
     * @return  O objeto da classe <code>CentroReceitaVO</code> com os dados devidamente montados.
     */
    public static CentroReceitaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        CentroReceitaVO obj = new CentroReceitaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getCentroReceitaPrincipal().setCodigo((new Integer(dadosSQL.getInt("centroReceitaPrincipal"))));
        obj.setIdentificadorCentroReceita(dadosSQL.getString("identificadorCentroReceita"));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
        obj.setInformarManualmenteIdentificadorCentroReceita(dadosSQL.getBoolean("informarmanualmenteidentificadorcentroreceita"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        //montarDadosCentroReceitaPrincipal(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCentroReceitaPrincipal(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DepartamentoVO</code> relacionado ao objeto <code>CentroReceitaVO</code>.
     * Faz uso da chave primária da classe <code>DepartamentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDepartamento(CentroReceitaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, nivelMontarDados,usuario));
    }
    
    
    public static void montarDadosCentroReceitaPrincipal(CentroReceitaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceitaPrincipal().getCodigo().intValue() == 0) {
            obj.setCentroReceitaPrincipal(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceitaPrincipal(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaPrincipal().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CentroReceitaVO</code> relacionado ao objeto <code>CentroReceitaVO</code>.
     * Faz uso da chave primária da classe <code>CentroReceitaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
//    public static void montarDadosCentroReceitaPrincipal(CentroReceitaVO obj, int nivelMontarDados) throws Exception {
//        if (obj.getCentroReceitaPrincipal().intValue() == 0) {
//            obj.setCentroReceitaPrincipal(0);
//            return;
//        }
//       
//        obj.setCentroReceitaPrincipal(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceitaPrincipal().intValue(), nivelMontarDados));
//    }
    /**
     * Operação responsável por localizar um objeto da classe <code>CentroReceitaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CentroReceitaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
       return getAplicacaoControle().getCentroReceitaVO(codigoPrm, usuario);
    }
    
    @Override
    public CentroReceitaVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CentroReceita WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CentroReceita ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public CentroReceitaVO consultarPorCodigoCentroReceitaPrincipal(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CentroReceita WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            return new CentroReceitaVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CentroReceita.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CentroReceita.idEntidade = idEntidade;
    }
    
    @Override
    public List<CentroReceitaVO> consultarCentroReceitaVinculadoContaReceber(List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
    	StringBuilder sql = new StringBuilder("select centroReceita.* from centroReceita ");
    	sql.append(" left join centroReceita as centroReceitaSuperior on centroReceitaSuperior.codigo = centroReceita.centroReceitaPrincipal ");
    	sql.append(" where exists ( ");
    	sql.append(" select CentroResultadoOrigem.codigo from centroResultadoOrigem where CentroResultadoOrigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' and CentroResultadoOrigem.centroReceita = centroReceita.codigo ");
    	if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs)){
    		int x = 0;
    		for(UnidadeEnsinoVO unidadeEnsinoVO: unidadeEnsinoVOs){    			
    			if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()){
    				if(x==0){
    					sql.append(" and CentroResultadoOrigem.unidadeensino in (0 ");    					
    				}
    				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
    				x++;
    			}
				if (x > 0) {
					sql.append(" ) ");
				}
    		}
    	}
    	sql.append(" limit 1 ) ORDER BY centroReceita.identificadorCentroReceita, centroReceitaSuperior.identificadorCentroReceita ");    	
    	return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
    }
    	
    
}
