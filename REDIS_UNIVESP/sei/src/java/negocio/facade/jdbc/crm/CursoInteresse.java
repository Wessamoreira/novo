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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboCursoInteresseEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.CursoInteresseInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoInteresseVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CursoInteresseVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CursoInteresseVO
 * @see SuperEntidade
 * @see Prospects
 */
@Repository
@Scope("singleton")
@Lazy
public class CursoInteresse extends ControleAcesso implements CursoInteresseInterfaceFacade {

    protected static String idEntidade;

    public CursoInteresse() throws Exception {
        super();
        setIdEntidade("Prospects");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CursoInteresseVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CursoInteresseVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CursoInteresseVO obj, UsuarioVO usuario) throws Exception {
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO CursoInteresse( curso, dataCadastro, prospects, turno ) VALUES ( ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getCurso().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataCadastro()));
                if (obj.getProspects().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getProspects().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                if (obj.getTurno().getCodigo().intValue() != 0) {
                	sqlInserir.setInt(4, obj.getTurno().getCodigo().intValue());
                } else {
                	sqlInserir.setNull(4, 0);
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursoInteresseVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoInteresseVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CursoInteresseVO obj, UsuarioVO usuario) throws Exception {
        realizarUpperCaseDados(obj);
        final String sql = "UPDATE CursoInteresse set curso=?, dataCadastro=?, prospects=?, turno=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getCurso().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataCadastro()));
                if (obj.getProspects().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getProspects().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                if (obj.getTurno().getCodigo().intValue() != 0) {
                	sqlAlterar.setInt(4, obj.getTurno().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(4, 0);
                }
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CursoInteresseVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoInteresseVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(CursoInteresseVO obj, UsuarioVO usuario) throws Exception {

        CursoInteresse.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoInteresse WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

    }

    public void excluirCursoInteressePorProspect(Integer codProspect, UsuarioVO usuario) throws Exception {

        CursoInteresse.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoInteresse WHERE ((prospects = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{codProspect});

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>CursoInteresseVO</code>.
     */
    public void validarUnicidade(List<CursoInteresseVO> lista, CursoInteresseVO obj) throws ConsistirException {
        for (CursoInteresseVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(CursoInteresseVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela CursoInteresseCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<CursoInteresseVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboCursoInteresseEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return getFacadeFactory().getCursoInteresseFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboCursoInteresseEnum.NOME_CURSO.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getCursoInteresseFacade().consultarPorNomeCurso(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboCursoInteresseEnum.NOME_PROSPECTS.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getCursoInteresseFacade().consultarPorNomeProspects(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoInteresse</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Prospects</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CursoInteresseVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT CursoInteresse.* FROM CursoInteresse, Prospects WHERE CursoInteresse.prospects = Prospects.codigo and upper( Prospects.nome ) like(?) ORDER BY Prospects.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoInteresse</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CursoInteresseVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT CursoInteresse.* FROM CursoInteresse INNER JOIN Curso ON curso.codigo = cursoInteresse.curso WHERE upper( Curso.nome ) like('" + valorConsulta.toUpperCase() + "') ORDER BY Curso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoInteresse</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoInteresseVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoInteresse WHERE codigo >= ?  ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public CursoInteresseVO consultarPorCodigoProspect(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoInteresse WHERE prospects = " + valorConsulta + "  ORDER BY codigo desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        }
        return new CursoInteresseVO();
    }
    
    public CursoInteresseVO consultarPorCodigoProspectCodigoCurso(Integer codigoProspect, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoInteresse WHERE prospects = " + codigoProspect + " AND curso = " + codigoCurso + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        }
        return null;
    }    
    
    @Transactional(readOnly=true)
    public List<CursoInteresseVO> consultarCursosPorCodigoProspect(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoInteresse WHERE prospects = " + valorConsulta + "  ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CursoInteresseVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>CursoInteresseVO</code>.
     * @return  O objeto da classe <code>CursoInteresseVO</code> com os dados devidamente montados.
     */
    public static CursoInteresseVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CursoInteresseVO obj = new CursoInteresseVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
        montarDadosCurso(obj, nivelMontarDados, usuario);
        montarDadosTurno(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        obj.getProspects().setCodigo(new Integer(dadosSQL.getInt("prospects")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>CursoInteresseVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(CursoInteresseVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
    }

    public static void montarDadosTurno(CursoInteresseVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getTurno().getCodigo().intValue() == 0) {
    		obj.setTurno(new TurnoVO());
    		return;
    	}
    	obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
    }
    
    /**
     * Operação responsável por excluir todos os objetos da <code>CursoInteresseVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>CursoInteresse</code>.
     * @param <code>prospects</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirCursoInteresses(Integer prospects, UsuarioVO usuario) throws Exception {
        try {
            CursoInteresse.excluir(getIdEntidade());
            String sql = "DELETE FROM CursoInteresse WHERE ((prospects = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{prospects});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>CursoInteresseVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirCursoInteresses</code> e <code>incluirCursoInteresses</code> disponíveis na classe <code>CursoInteresse</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarCursoInteresses(Integer prospects, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM CursoInteresse WHERE prospects = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CursoInteresseVO objeto = (CursoInteresseVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str, new Object[]{prospects});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CursoInteresseVO objeto = (CursoInteresseVO) e.next();
			objeto.getProspects().setCodigo(prospects);
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>CursoInteresseVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>crm.Prospects</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirCursoInteresses(Integer prospectsPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CursoInteresseVO obj = (CursoInteresseVO) e.next();
            obj.getProspects().setCodigo(prospectsPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>CursoInteresseVO</code> relacionados a um objeto da classe <code>crm.Prospects</code>.
     * @param prospects  Atributo de <code>crm.Prospects</code> a ser utilizado para localizar os objetos da classe <code>CursoInteresseVO</code>.
     * @return List  Contendo todos os objetos da classe <code>CursoInteresseVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarCursoInteresses(Integer prospects, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CursoInteresse.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM CursoInteresse WHERE prospects = " + prospects;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        while (tabelaResultado.next()) {
            CursoInteresseVO novoObj = new CursoInteresseVO();
            novoObj = CursoInteresse.montarDados(tabelaResultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CursoInteresseVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CursoInteresseVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CursoInteresse WHERE codigo = " + codigoPrm;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CursoInteresse ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CursoInteresse.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CursoInteresse.idEntidade = idEntidade;
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alteraCursoInteresse(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception {
		try {		
			String sql3 = "UPDATE CursoInteresse set prospects=? WHERE (prospects = ?)";
			sql3 = sql3 + " and curso not in (select curso from cursointeresse where curso in (SELECT curso FROM CursoInteresse WHERE prospects = " + codProspectRemover + ") and prospects = " + codProspectManter + ")" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			final String sql = sql3;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, codProspectManter);
					sqlAlterar.setInt(2, codProspectRemover);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
}
