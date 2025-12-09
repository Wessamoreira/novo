package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.ProcSeletivoCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcSeletivoCursoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ProcSeletivoCursoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ProcSeletivoCursoVO
 * @see ControleAcesso
 * @see ProcSeletivo
 */
@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoCurso extends ControleAcesso implements ProcSeletivoCursoInterfaceFacade {

    protected static String idEntidade;

    public ProcSeletivoCurso() throws Exception {
        super();
        setIdEntidade("ProcSeletivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoCursoVO</code>.
     */
    public ProcSeletivoCursoVO novo() throws Exception {
        ProcSeletivoCurso.incluir(getIdEntidade());
        ProcSeletivoCursoVO obj = new ProcSeletivoCursoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoCursoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ProcSeletivoCursoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcSeletivoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProcSeletivoCursoVO.validarDados(obj);
            final String sql = "INSERT INTO ProcSeletivoCurso( procSeletivoUnidadeEnsino, unidadeensinoCurso, numeroVaga, dataInicioProcSeletivoCurso, dataFimProcSeletivoCurso, grupoDisciplinaProcSeletivo ) VALUES ( ?, ?, ?, ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getProcSeletivoUnidadeEnsino().getCodigo().intValue());
                    sqlInserir.setInt(2, obj.getUnidadeEnsinoCurso().getCodigo().intValue());
                    sqlInserir.setInt(3, obj.getNumeroVaga());
					if (obj.getDataInicioProcSeletivoCurso() == null) {
						sqlInserir.setNull(4, 0);
					} else {
						sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataInicioProcSeletivoCurso()));
					}
					if (obj.getDataFimProcSeletivoCurso() == null) {
						sqlInserir.setNull(5, 0);
					} else {
						sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataFimProcSeletivoCurso()));
					}
					if (!Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivo())){
						sqlInserir.setNull(6, 0);
					} else {
						sqlInserir.setInt(6, obj.getGrupoDisciplinaProcSeletivo().getCodigo());
					}
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoCursoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoCursoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProcSeletivoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
        ProcSeletivoCursoVO.validarDados(obj);
        final String sql = "UPDATE ProcSeletivoCurso set procSeletivoUnidadeEnsino = ?,  unidadeEnsinoCurso = ?, numeroVaga = ?, dataInicioProcSeletivoCurso = ?, dataFimProcSeletivoCurso = ?, grupoDisciplinaProcSeletivo=?  WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getProcSeletivoUnidadeEnsino().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getUnidadeEnsinoCurso().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getNumeroVaga());
                if (obj.getDataInicioProcSeletivoCurso() == null) {
                	sqlAlterar.setNull(4, 0);
				} else {
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataInicioProcSeletivoCurso()));
				}
				if (obj.getDataFimProcSeletivoCurso() == null) {
					sqlAlterar.setNull(5, 0);
				} else {
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataFimProcSeletivoCurso()));
				}
				if (!Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivo())){
					sqlAlterar.setNull(6, 0);
				} else {
					sqlAlterar.setInt(6, obj.getGrupoDisciplinaProcSeletivo().getCodigo());
				}
                sqlAlterar.setInt(7, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        })==0){
        	incluir(obj, usuarioVO);
        	return;
        };
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoCursoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoCursoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProcSeletivoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
        ProcSeletivoCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM ProcSeletivoCurso WHERE  (procSeletivoUnidadeEnsino = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivoCurso</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoCursoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivoCurso.* FROM ProcSeletivoCurso, Curso WHERE ProcSeletivoCurso.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivoCurso</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>ProcSeletivo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoCursoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoProcSeletivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivoCurso.* FROM ProcSeletivoCurso, ProcSeletivo WHERE ProcSeletivoCurso.procSeletivo = ProcSeletivo.codigo and ProcSeletivo.descricao like('" + valorConsulta + "%') ORDER BY ProcSeletivo.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoProcSeletivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivoCurso.* FROM ProcSeletivoCurso, ProcSeletivo WHERE ProcSeletivoCurso.procSeletivo = ProcSeletivo.codigo and ProcSeletivo.codigo = " + valorConsulta + " ORDER BY ProcSeletivo.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoProcSeletivoUnidadeEnsinoOpcaoInscicao(Integer processoSele, Integer unidadeEnsino, Date data , int  nivelMontarDados , UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT UnidadeEnsinoCurso.codigo, Curso.nome as nomeCurso,Curso.codigo as codigoCurso, Turno.nome as nomeTurno, Turno.codigo as codigoTurno, ProcSeletivoCurso.numeroVaga  , ProcSeletivoCurso.grupodisciplinaprocseletivo FROM ProcSeletivoCurso, ProcSeletivo,procseletivounidadeensino, UnidadeEnsinoCurso, Curso, Turno");
        sqlStr.append(" WHERE ProcSeletivoCurso.procseletivounidadeensino = procseletivounidadeensino.codigo");
        sqlStr.append(" and procseletivounidadeensino.procseletivo = ProcSeletivo.codigo");
        sqlStr.append(" and ProcSeletivoCurso.UnidadeEnsinoCurso = UnidadeEnsinoCurso.codigo");
        sqlStr.append(" and UnidadeEnsinoCurso.curso = curso.codigo");
        sqlStr.append(" and UnidadeEnsinoCurso.turno = turno.codigo");
        sqlStr.append(" and ProcSeletivo.codigo = ").append(processoSele);
        sqlStr.append(" and procseletivounidadeensino.unidadeEnsino = ").append(unidadeEnsino);
        sqlStr.append(" and (case when procseletivocurso.datainicioprocseletivocurso is not null and procseletivocurso.datafimprocseletivocurso is not null ");
        sqlStr.append(" then ('").append(Uteis.getDataJDBCTimestamp(data)).append("' >= procseletivocurso.datainicioprocseletivocurso and '").append(Uteis.getDataJDBCTimestamp(data)).append("'::Date <= procseletivocurso.datafimprocseletivocurso) else ");
        sqlStr.append(" (procseletivocurso.datainicioprocseletivocurso is null and procseletivocurso.datafimprocseletivocurso is null) end) ");
        sqlStr.append(" ORDER BY Curso.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            ProcSeletivoCursoVO obj = new ProcSeletivoCursoVO();
            obj.getUnidadeEnsinoCurso().setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNumeroVaga(tabelaResultado.getInt("numeroVaga"));
            
            obj.getUnidadeEnsinoCurso().getCurso().setCodigo(tabelaResultado.getInt("codigoCurso"));
            obj.getUnidadeEnsinoCurso().getTurno().setCodigo(tabelaResultado.getInt("codigoTurno"));
            obj.getUnidadeEnsinoCurso().getCurso().setNome(tabelaResultado.getString("nomeCurso"));
            obj.getUnidadeEnsinoCurso().getTurno().setNome(tabelaResultado.getString("nomeTurno"));
            obj.getGrupoDisciplinaProcSeletivo().setCodigo(new Integer(tabelaResultado.getInt("grupoDisciplinaProcSeletivo")));
           if(Uteis.isAtributoPreenchido( obj.getGrupoDisciplinaProcSeletivo().getCodigo())) {        	   
        	   montarDadosGrupoDisciplinaProcSeletivo(obj,nivelMontarDados, usuario);
           }
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List consultarPorCodigoProcSeletivoUnidadeEnsinoInscicao(String valorConsultaCurso, Integer processoSele, Integer unidadeEnsino) throws Exception {
    	
    	StringBuilder sqlStr = new StringBuilder("SELECT UnidadeEnsinoCurso.codigo, unidadeEnsino.nome AS nomeUnidadeEnsino, Curso.codigo AS codigoCurso, Curso.nome as nomeCurso, Turno.nome as nomeTurno, ProcSeletivoCurso.numeroVaga FROM ProcSeletivoCurso, ProcSeletivo,procseletivounidadeensino, UnidadeEnsinoCurso, Curso, Turno, UnidadeEnsino ");
    	sqlStr.append(" WHERE ProcSeletivoCurso.procseletivounidadeensino = procseletivounidadeensino.codigo");
    	sqlStr.append(" and procseletivounidadeensino.procseletivo = ProcSeletivo.codigo");
    	sqlStr.append(" and ProcSeletivoCurso.UnidadeEnsinoCurso = UnidadeEnsinoCurso.codigo");
    	sqlStr.append(" and UnidadeEnsinoCurso.curso = curso.codigo");
    	sqlStr.append(" and UnidadeEnsinoCurso.turno = turno.codigo");
    	sqlStr.append(" and UnidadeEnsinoCurso.unidadeEnsino = unidadeEnsino.codigo");
    	sqlStr.append(" and ProcSeletivo.codigo = ").append(processoSele);
    	sqlStr.append(" and procseletivounidadeensino.unidadeEnsino = ").append(unidadeEnsino);
    	sqlStr.append(" and upper(sem_acentos(curso.nome)) ilike(upper(sem_acentos('").append(valorConsultaCurso).append("%'))) ");
    	sqlStr.append(" ORDER BY Curso.nome");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	List vetResultado = new ArrayList();
    	while (tabelaResultado.next()) {
    		ProcSeletivoCursoVO obj = new ProcSeletivoCursoVO();
    		obj.getUnidadeEnsinoCurso().setCodigo(tabelaResultado.getInt("codigo"));
    		obj.getUnidadeEnsinoCurso().setNomeUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
    		obj.setNumeroVaga(tabelaResultado.getInt("numeroVaga"));
    		obj.getUnidadeEnsinoCurso().getCurso().setNome(tabelaResultado.getString("nomeCurso"));
    		obj.getUnidadeEnsinoCurso().getCurso().setCodigo(tabelaResultado.getInt("codigoCurso"));
    		obj.getUnidadeEnsinoCurso().getTurno().setNome(tabelaResultado.getString("nomeTurno"));
    		vetResultado.add(obj);
    	}
    	return vetResultado;
    }
    
    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoCursoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ProcSeletivoCursoVO</code>.
     * @return  O objeto da classe <code>ProcSeletivoCursoVO</code> com os dados devidamente montados.
     */
    public static ProcSeletivoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoCursoVO obj = new ProcSeletivoCursoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getProcSeletivoUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("procSeletivoUnidadeEnsino")));
        obj.getUnidadeEnsinoCurso().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsinoCurso")));
        obj.getGrupoDisciplinaProcSeletivo().setCodigo(new Integer(dadosSQL.getInt("grupoDisciplinaProcSeletivo")));
        obj.setNumeroVaga(dadosSQL.getInt("numeroVaga"));
        obj.setDataInicioProcSeletivoCurso(dadosSQL.getDate("dataInicioProcSeletivoCurso"));
        obj.setDataFimProcSeletivoCurso(dadosSQL.getDate("dataFimProcSeletivoCurso"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosUnidadeEnsinoCurso(obj, nivelMontarDados, usuario);
        montarDadosGrupoDisciplinaProcSeletivo(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>ProcSeletivoCursoVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsinoCurso(ProcSeletivoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsinoCurso().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsinoCurso(new UnidadeEnsinoCursoVO());
            return;
        }
        getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(obj.getUnidadeEnsinoCurso(), NivelMontarDados.BASICO, usuario);        
    }

    public static void montarDadosGrupoDisciplinaProcSeletivo(ProcSeletivoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getGrupoDisciplinaProcSeletivo().getCodigo().intValue() == 0) {
    		obj.setGrupoDisciplinaProcSeletivo(new GrupoDisciplinaProcSeletivoVO());
    		return;
    	}
    	obj.setGrupoDisciplinaProcSeletivo(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(obj.getGrupoDisciplinaProcSeletivo().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ProcSeletivoCursoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ProcSeletivoCurso</code>.
     * @param <code>procSeletivo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcSeletivoCursos(Integer procSeletivoUnidadeEnsino, UsuarioVO usuarioVO) throws Exception {
        String sql = "DELETE FROM ProcSeletivoCurso WHERE (procSeletivoUnidadeEnsino = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(sql, procSeletivoUnidadeEnsino.intValue());
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ProcSeletivoCursoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirProcSeletivoCursos</code> e <code>incluirProcSeletivoCursos</code> disponíveis na classe <code>ProcSeletivoCurso</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProcSeletivoCursos(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ProcSeletivoCurso WHERE procSeletivoUnidadeEnsino = ").append(procSeletivoUnidadeEnsinoVO.getCodigo()).append(" and codigo not in (0 ");
		for (ProcSeletivoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs()) {
			if (obj.getCodigo() > 0) {
				sql.append(", ").append(obj.getCodigo());
			}
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		for (ProcSeletivoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs()) {
			obj.getProcSeletivoUnidadeEnsino().setCodigo(procSeletivoUnidadeEnsinoVO.getCodigo());
			obj.getProcSeletivoUnidadeEnsino().setProcSeletivo(procSeletivoUnidadeEnsinoVO.getProcSeletivo());
			if (obj.getCodigo() > 0) {
				alterar(obj, usuarioVO);
			} else {
				incluir(obj, usuarioVO);
			}
		}
	}

    /**
     * Operação responsável por incluir objetos da <code>ProcSeletivoCursoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirProcSeletivoCursos(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
        try {
//            if (procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs().isEmpty()) {
//                throw new ConsistirException("Por favor adicionar Curso para a Unidade de Ensino do processo seletivo.");
//            }
            for (ProcSeletivoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs()) {
                obj.getProcSeletivoUnidadeEnsino().setCodigo(procSeletivoUnidadeEnsinoVO.getCodigo());
                obj.getProcSeletivoUnidadeEnsino().setProcSeletivo(procSeletivoUnidadeEnsinoVO.getProcSeletivo());
                incluir(obj, usuarioVO);
            }
        } catch (Exception e) {
            for (ProcSeletivoCursoVO obj : procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs()) {
                obj.setNovoObj(true);
                obj.setCodigo(0);
            }
            throw e;
        }
    }
    

    /**
     * Operação responsável por consultar todos os <code>ProcSeletivoCursoVO</code> relacionados a um objeto da classe <code>processosel.ProcSeletivo</code>.
     * @param procSeletivo  Atributo de <code>processosel.ProcSeletivo</code> a ser utilizado para localizar os objetos da classe <code>ProcSeletivoCursoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ProcSeletivoCursoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarProcSeletivoCursos(Integer procSeletivo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoCurso.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM ProcSeletivoCurso WHERE procSeletivoUnidadeEnsino = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, procSeletivo.intValue());
        while (resultado.next()) {
            ProcSeletivoCursoVO novoObj = new ProcSeletivoCursoVO();
            novoObj = ProcSeletivoCurso.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProcSeletivoCursoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProcSeletivoCursoVO consultarPorChavePrimaria(Integer cursoPrm, Integer procSeletivoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProcSeletivoCurso WHERE curso = ? and procSeletivo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, cursoPrm.intValue(), procSeletivoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProcSeletivoCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ProcSeletivoCurso.idEntidade = idEntidade;
    }
    
    @Override
    public ProcSeletivoCursoVO consultarPorProcSeletivoUnidadeEnsinoCurso(Integer cursoPrm, Integer procSeletivoPrm, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT ProcSeletivoCurso.* FROM ProcSeletivoCurso ");
        sqlStr.append("inner join procseletivounidadeensino psue on psue.codigo = procseletivocurso.procseletivounidadeensino ");
        sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.codigo = procseletivocurso.unidadeensinocurso ");
        sqlStr.append("WHERE unidadeensinocurso.curso = ? and procseletivo = ? and unidadeensinocurso.unidadeensino = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), cursoPrm, procSeletivoPrm, unidadeEnsino);
        if (!tabelaResultado.next()) {
            return new ProcSeletivoCursoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    
    @Override
    public List<ProcSeletivoCursoVO> consultarPorCodigoProcSeletivoUnidadeEnsino(Integer codigoProcessoSeletivo, Integer unidadeEnsino , UsuarioVO usuario) throws Exception {
    	 ControleAcesso.consultar(getIdEntidade(), false, usuario);
    	StringBuilder sqlStr = new StringBuilder("SELECT UnidadeEnsinoCurso.codigo,  ProcSeletivoCurso.dataInicioProcSeletivoCurso  , ProcSeletivoCurso.dataFimProcSeletivoCurso , ");
        sqlStr.append(" Curso.nome as nomeCurso,Curso.codigo as codigoCurso, Turno.nome as nomeTurno, Turno.codigo as codigoTurno, ProcSeletivoCurso.numeroVaga  ,unidadeensino.codigo as codigoUnidadeEnsino , unidadeensino.nome as nomeUnidadeEnsino");
        sqlStr.append(" FROM ProcSeletivoCurso, ProcSeletivo,procseletivounidadeensino, UnidadeEnsinoCurso, Curso, Turno ,unidadeensino");
        sqlStr.append(" WHERE ProcSeletivoCurso.procseletivounidadeensino = procseletivounidadeensino.codigo");
        sqlStr.append(" and procseletivounidadeensino.procseletivo = ProcSeletivo.codigo");
        sqlStr.append(" and ProcSeletivoCurso.UnidadeEnsinoCurso = UnidadeEnsinoCurso.codigo");
        sqlStr.append(" and UnidadeEnsinoCurso.curso = curso.codigo");
        sqlStr.append(" and UnidadeEnsinoCurso.turno = turno.codigo");
        sqlStr.append(" and UnidadeEnsinoCurso.unidadeensino = unidadeensino.codigo");
        sqlStr.append(" and ProcSeletivo.codigo = ").append(codigoProcessoSeletivo);
        if(unidadeEnsino > 0) {     	
            sqlStr.append(" and procseletivounidadeensino.unidadeEnsino = ").append(unidadeEnsino);       
          }       
        sqlStr.append(" ORDER BY Curso.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        
        List<ProcSeletivoCursoVO> vetResultado = new ArrayList<ProcSeletivoCursoVO>();
        while (tabelaResultado.next()) {
            ProcSeletivoCursoVO obj = new ProcSeletivoCursoVO();
            obj.getUnidadeEnsinoCurso().setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNumeroVaga(tabelaResultado.getInt("numeroVaga"));
            obj.setDataInicioProcSeletivoCurso(tabelaResultado.getDate("dataInicioProcSeletivoCurso"));
            obj.setDataFimProcSeletivoCurso(tabelaResultado.getDate("dataFimProcSeletivoCurso"));
            obj.getUnidadeEnsinoCurso().setUnidadeEnsino(tabelaResultado.getInt("codigoUnidadeEnsino"));
            obj.getUnidadeEnsinoCurso().setNomeUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
            obj.getUnidadeEnsinoCurso().getCurso().setCodigo(tabelaResultado.getInt("codigoCurso"));
            obj.getUnidadeEnsinoCurso().getTurno().setCodigo(tabelaResultado.getInt("codigoTurno"));
            obj.getUnidadeEnsinoCurso().getCurso().setNome(tabelaResultado.getString("nomeCurso"));
            obj.getUnidadeEnsinoCurso().getTurno().setNome(tabelaResultado.getString("nomeTurno"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }
    
    @Override
    public ProcSeletivoCursoVO inicializarDadosImportarCandidatoInscricaoProcSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, UsuarioVO usuario) throws Exception {
    	if (Uteis.isAtributoPreenchido(procSeletivoUnidadeEnsinoVO.getProcSeletivo().getCodigo())) {
    		ProcSeletivoCursoVO procSeletivoCursoVO = consultarPorProcSeletivoUnidadeEnsinoCurso(importarCandidatoVO.getCursoVO().getCodigo(), procSeletivoUnidadeEnsinoVO.getProcSeletivo().getCodigo(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    		if (Uteis.isAtributoPreenchido(procSeletivoCursoVO.getCodigo())) {
    			return procSeletivoCursoVO;
    		}
    	}
    	ProcSeletivoCursoVO procSeletivoCursoVO = new ProcSeletivoCursoVO();
    	procSeletivoCursoVO.setUnidadeEnsinoCurso(unidadeEnsinoCursoVO);
    	procSeletivoCursoVO.setProcSeletivoUnidadeEnsino(procSeletivoUnidadeEnsinoVO);
    	procSeletivoCursoVO.setNumeroVaga(100);
//    	procSeletivoCursoVO.setDataInicioProcSeletivoCurso(Uteis.getDataFuturaConsiderandoDataAtual(procSeletivoUnidadeEnsinoVO.getProcSeletivo().getDataInicio(), 2));
//    	procSeletivoCursoVO.setDataFimProcSeletivoCurso(Uteis.getDataPassada(procSeletivoUnidadeEnsinoVO.getProcSeletivo().getDataFim(), 1));
    	return procSeletivoCursoVO;
    }
    
    
    @Override
     public List consultarPorCodigoProcSeletivoUnidadeEnsinoInscricao(String valorConsultaCurso, Integer processoSele, Integer unidadeEnsino) throws Exception {
    	
    	StringBuilder sqlStr = new StringBuilder("select  uc.codigo AS \"codigoUnidadeEnsinoCurso\" , u.nome AS \"nomeUnidadeEnsino\" , c.codigo AS \"codigoCurso\" , c.nome AS \"nomeCurso\" , t.nome AS \"nomeTurno\" , ");
    	sqlStr.append(" psc.numeroVaga AS \"numeroVaga\" ,  psc.procseletivounidadeensino AS \"codigoProcSeletivoUnidadeEnsino\" , c.eixocurso AS  \"codigoEixoCurso\"  from 	ProcSeletivoCurso psc  ");
    	
    	sqlStr.append("  inner join procseletivounidadeensino psu on psu.codigo = psc.procseletivounidadeensino ");
    	sqlStr.append("  inner join ProcSeletivo ps on  ps.codigo = psu.procseletivo ");
    	sqlStr.append("  inner join UnidadeEnsinoCurso uc on uc.codigo = psc.unidadeensinocurso ");
    	sqlStr.append("  inner join curso c on c.codigo = uc.curso ");
    	sqlStr.append("  inner join turno t on t.codigo = uc.turno ");
    	sqlStr.append("  inner join unidadeensino u on u.codigo = uc.unidadeensino ");
    	sqlStr.append("  where ps.codigo = ").append(processoSele);
    	sqlStr.append("  and  psu.unidadeensino =  ").append(unidadeEnsino);
    	sqlStr.append("  and upper(sem_acentos(c.nome)) ilike(upper(sem_acentos('").append(valorConsultaCurso).append("%'))) ");
    	sqlStr.append("  ORDER BY c.nome");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	List vetResultado = new ArrayList();
    	while (tabelaResultado.next()) {
    		ProcSeletivoCursoVO obj = new ProcSeletivoCursoVO();
    		obj.getUnidadeEnsinoCurso().setCodigo(tabelaResultado.getInt("codigoUnidadeEnsinoCurso"));
    		obj.getUnidadeEnsinoCurso().setNomeUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
    		obj.setNumeroVaga(tabelaResultado.getInt("numeroVaga"));
    		obj.getUnidadeEnsinoCurso().getCurso().setNome(tabelaResultado.getString("nomeCurso"));
    		obj.getUnidadeEnsinoCurso().getCurso().setCodigo(tabelaResultado.getInt("codigoCurso"));
    		obj.getUnidadeEnsinoCurso().getTurno().setNome(tabelaResultado.getString("nomeTurno"));
    		obj.getUnidadeEnsinoCurso().getCurso().getEixoCursoVO().setCodigo(tabelaResultado.getInt("codigoEixoCurso"));
    		obj.getProcSeletivoUnidadeEnsino().setCodigo(tabelaResultado.getInt("codigoProcSeletivoUnidadeEnsino"));
    		vetResultado.add(obj);
    	}
    	return vetResultado;
    }
}