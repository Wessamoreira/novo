package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ProcSeletivoUnidadeEnsinoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ProcSeletivoUnidadeEnsinoVO
 * @see ControleAcesso
 * @see ProcSeletivo
 */
@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoUnidadeEnsino extends ControleAcesso implements ProcSeletivoUnidadeEnsinoInterfaceFacade {

    protected static String idEntidade;

    public ProcSeletivoUnidadeEnsino() throws Exception {
        super();
        setIdEntidade("ProcSeletivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
     */
    public ProcSeletivoUnidadeEnsinoVO novo() throws Exception {
        ProcSeletivoUnidadeEnsino.incluir(getIdEntidade());
        ProcSeletivoUnidadeEnsinoVO obj = new ProcSeletivoUnidadeEnsinoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcSeletivoUnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProcSeletivoUnidadeEnsinoVO.validarDados(obj);
            final String sql = "INSERT INTO ProcSeletivoUnidadeEnsino( procSeletivo, unidadeEnsino ) VALUES ( ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getProcSeletivo().getCodigo().intValue());
                    sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getProcSeletivoCursoFacade().incluirProcSeletivoCursos(obj, usuarioVO);
            getFacadeFactory().getProcSeletivoUnidadeEnsinoEixoCursoFacade().incluirProcSeletivoEixoCursoVO(obj, usuarioVO);
        } catch (Exception e) {
            obj.setNovoObj(true);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProcSeletivoUnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception {
        ProcSeletivoUnidadeEnsinoVO.validarDados(obj);
        final String sql = "UPDATE ProcSeletivoUnidadeEnsino set procSeletivo = ?, unidadeEnsino = ?  WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getProcSeletivo().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        })==0){
        	incluir(obj, usuarioVO);
        	return;
        };
        getFacadeFactory().getProcSeletivoCursoFacade().alterarProcSeletivoCursos(obj, usuarioVO);
        getFacadeFactory().getProcSeletivoUnidadeEnsinoEixoCursoFacade().alterarProcSeletivoUnidadeEnsinoEixoCurso(obj, usuarioVO);
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProcSeletivoUnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception {
        ProcSeletivoUnidadeEnsino.excluir(getIdEntidade());
        String sql = "DELETE FROM ProcSeletivoUnidadeEnsino WHERE ((procSeletivo = ?) and (unidadeEnsino = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(sql, obj.getProcSeletivo().getCodigo().intValue(), obj.getUnidadeEnsino().getCodigo().intValue());
        Iterator i = obj.getProcSeletivoCursoVOs().iterator();
        while (i.hasNext()) {
            ProcSeletivoCursoVO objs = (ProcSeletivoCursoVO) i.next();
            getFacadeFactory().getProcSeletivoCursoFacade().excluir(objs, usuarioVO);
        }
        Iterator n = obj.getProcSeletivoUnidadeEnsinoEixoCursoVOs().iterator();
        while (n.hasNext()) {
        	ProcSeletivoUnidadeEnsinoEixoCursoVO objs = (ProcSeletivoUnidadeEnsinoEixoCursoVO) n.next();
        	getFacadeFactory().getProcSeletivoUnidadeEnsinoEixoCursoFacade().excluir(objs, usuarioVO);
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivoUnidadeEnsino</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoUnidadeEnsinoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivoUnidadeEnsino.* FROM ProcSeletivoUnidadeEnsino, UnidadeEnsino WHERE ProcSeletivoUnidadeEnsino.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     *
     * @param codigo UnidadeEnsino.codigo
     * @param codigo0 processoSeletivo.codigo
     * @param nivelMontarDados
     * @return
     * @throws java.lang.Exception
     */
    public ProcSeletivoUnidadeEnsinoVO consultarPorCodigoUnidadeEnsino(Integer codigo, Integer codigo0, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProcSeletivoUnidadeEnsino WHERE unidadeEnsino = " + codigo.intValue() + " and procseletivo = " + codigo0;
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!dadosSQL.next()) {
            return new ProcSeletivoUnidadeEnsinoVO();
        }
        return montarDados(dadosSQL, nivelMontarDados, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoUnidadeEnsinoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
     * @return  O objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> com os dados devidamente montados.
     */
    public static ProcSeletivoUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoUnidadeEnsinoVO obj = new ProcSeletivoUnidadeEnsinoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getProcSeletivo().setCodigo(dadosSQL.getInt("procSeletivo"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        obj.setProcSeletivoCursoVOs(ProcSeletivoCurso.consultarProcSeletivoCursos(obj.getCodigo(), nivelMontarDados, usuario));
        Ordenacao.ordenarLista( obj.getProcSeletivoCursoVOs(), "ordenacao");
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        obj.setProcSeletivoUnidadeEnsinoEixoCursoVOs(ProcSeletivoUnidadeEnsinoEixoCurso.consultarProcSeletivoEixoCursoVOs(obj.getCodigo(), nivelMontarDados, usuario));
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>ProcSeletivoUnidadeEnsinoVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(ProcSeletivoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ProcSeletivoUnidadeEnsinoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ProcSeletivoUnidadeEnsino</code>.
     * @param <code>procSeletivo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcSeletivoUnidadeEnsinos(Integer procSeletivo, UsuarioVO usuarioVO) throws Exception {
        String sql = "DELETE FROM ProcSeletivoUnidadeEnsino WHERE (procSeletivo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(sql, new Object[]{procSeletivo.intValue()});
        getFacadeFactory().getProcSeletivoCursoFacade().excluirProcSeletivoCursos(procSeletivo, usuarioVO);
    }

//    public void excluirProcSeletivoUnidadeEnsinosCurso(Integer procSeletivo, List objetos) throws Exception {
//        Iterator i = objetos.iterator();
//        while (i.hasNext()) {
//            ProcSeletivoUnidadeEnsinoVO obj = (ProcSeletivoUnidadeEnsinoVO) i.next();
//            obj.setProcSeletivo(procSeletivo);
//            if (!obj.getCodigo().equals(0)) {
//                excluir(obj);
//            }
//        }
//    }
    /**
     * Operação responsável por alterar todos os objetos da <code>ProcSeletivoUnidadeEnsinoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirProcSeletivoUnidadeEnsinos</code> e <code>incluirProcSeletivoUnidadeEnsinos</code> disponíveis na classe <code>ProcSeletivoUnidadeEnsino</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarProcSeletivoUnidadeEnsinos(ProcSeletivoVO procSeletivoVO, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sql  = new StringBuilder("DELETE FROM ProcSeletivoCurso WHERE procSeletivoUnidadeEnsino in ");
    	sql.append("(select codigo from procSeletivoUnidadeEnsino where procSeletivo = ").append(procSeletivoVO.getCodigo()).append(" and codigo not in (0 ");
    	for(ProcSeletivoUnidadeEnsinoVO obj :procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()){
            if(obj.getCodigo()>0){
            	sql.append(", ").append(obj.getCodigo());
            }
        }
    	sql.append(" ) ) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    	getConexao().getJdbcTemplate().update(sql.toString());
    	
    	sql  = new StringBuilder("DELETE FROM ProcSeletivoUnidadeEnsino WHERE procSeletivo = ").append(procSeletivoVO.getCodigo()).append(" and codigo not in (0 ");
    	for(ProcSeletivoUnidadeEnsinoVO obj :procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()){
            if(obj.getCodigo()>0){
            	sql.append(", ").append(obj.getCodigo());
            }
        }
    	sql.append(") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    	getConexao().getJdbcTemplate().update(sql.toString());
    	for(ProcSeletivoUnidadeEnsinoVO obj :procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()){
            obj.getProcSeletivo().setCodigo(procSeletivoVO.getCodigo());
            obj.getProcSeletivo().setDataInicio(procSeletivoVO.getDataInicio());
			obj.getProcSeletivo().setDataFim(procSeletivoVO.getDataFim());
            if (!obj.getCodigo().equals(0)) {
                alterar(obj, usuarioVO);
            }else{
            	incluir(obj, usuarioVO);
            }
        }        
    }

    /**
     * Operação responsável por incluir objetos da <code>ProcSeletivoUnidadeEnsinoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProcSeletivoUnidadeEnsinos(ProcSeletivoVO procSeletivoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			if (procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs().isEmpty()) {
				throw new ConsistirException("Por favor adicionar Unidade de Ensino para o processo seletivo.");
			}
			for (ProcSeletivoUnidadeEnsinoVO obj : procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()) {
				obj.getProcSeletivo().setCodigo(procSeletivoVO.getCodigo());
				obj.getProcSeletivo().setDataInicio(procSeletivoVO.getDataInicio());
				obj.getProcSeletivo().setDataFim(procSeletivoVO.getDataFim());
				incluir(obj, usuarioVO);
			}
		} catch (Exception e) {
			for (ProcSeletivoUnidadeEnsinoVO obj : procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()) {
				obj.setNovoObj(true);
				obj.setCodigo(0);
			}
			throw e;
		}
	}

    /**
     * Operação responsável por consultar todos os <code>ProcSeletivoUnidadeEnsinoVO</code> relacionados a um objeto da classe <code>processosel.ProcSeletivo</code>.
     * @param procSeletivo  Atributo de <code>processosel.ProcSeletivo</code> a ser utilizado para localizar os objetos da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ProcSeletivoUnidadeEnsinoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List<ProcSeletivoUnidadeEnsinoVO> consultarProcSeletivoUnidadeEnsinos(Integer procSeletivo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoUnidadeEnsino.consultar(getIdEntidade());
        List<ProcSeletivoUnidadeEnsinoVO> objetos = new ArrayList<ProcSeletivoUnidadeEnsinoVO>();
        StringBuilder sql = new StringBuilder("SELECT ProcSeletivoUnidadeEnsino.codigo, ProcSeletivoUnidadeEnsino.procSeletivo, ProcSeletivoUnidadeEnsino.unidadeEnsino, unidadeEnsino.nome as unidadeEnsino_nome, unidadeEnsino.abreviatura as unidadeEnsino_abreviatura ");
        if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
        	sql.append(", unidadeensinocurso.codigo as unidadeensinocurso_codigo, ");
        	sql.append("ProcSeletivoCurso.codigo as ProcSeletivoCurso_codigo, ");
        	sql.append("ProcSeletivoCurso.numeroVaga as ProcSeletivoCurso_numeroVaga, ");
        	sql.append("ProcSeletivoCurso.dataInicioProcSeletivoCurso as ProcSeletivoCurso_dataInicioProcSeletivoCurso, ");
        	sql.append("ProcSeletivoCurso.dataFimProcSeletivoCurso as ProcSeletivoCurso_dataFimProcSeletivoCurso, ");
        	sql.append("curso.codigo as curso_codigo, ");
        	sql.append("curso.nome as curso_nome, ");
        	sql.append("curso.abreviatura as curso_abreviatura, ");
        	sql.append("curso.niveleducacional as curso_niveleducacional, ");
        	sql.append("curso.periodicidade as curso_periodicidade, ");
        	sql.append("turno.codigo as turno_codigo, ");
        	sql.append("turno.nome as turno_nome, ");
        	sql.append("GrupoDisciplinaProcSeletivo.codigo as GrupoDisciplinaProcSeletivo_codigo, ");
        	sql.append("GrupoDisciplinaProcSeletivo.descricao as GrupoDisciplinaProcSeletivo_descricao ");
        }
        sql.append(" FROM ProcSeletivoUnidadeEnsino ");
        sql.append(" inner join unidadeensino on unidadeensino.codigo = ProcSeletivoUnidadeEnsino.unidadeensino ");
        if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
        	sql.append(" left join ProcSeletivoCurso on ProcSeletivoCurso.ProcSeletivoUnidadeEnsino = ProcSeletivoUnidadeEnsino.codigo ");
        	sql.append(" left join unidadeensinocurso on ProcSeletivoCurso.unidadeensinocurso = unidadeensinocurso.codigo ");
        	sql.append(" left join curso on curso.codigo = unidadeensinocurso.curso ");
        	sql.append(" left join turno on turno.codigo = unidadeensinocurso.turno ");
        	sql.append(" left join GrupoDisciplinaProcSeletivo on GrupoDisciplinaProcSeletivo.codigo = ProcSeletivoCurso.GrupoDisciplinaProcSeletivo ");
        }
        sql.append(" WHERE ProcSeletivoUnidadeEnsino.procSeletivo = ? ");
        sql.append(" order by unidadeEnsino_nome ");
        if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
        	 sql.append(", curso_nome, turno_nome ");
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), procSeletivo.intValue());
        if (resultado.next()) {
        	do {
        		if(!objetos.stream().anyMatch(u ->u.getCodigo().equals(resultado.getInt("codigo")))) {
        			ProcSeletivoUnidadeEnsinoVO obj = new ProcSeletivoUnidadeEnsinoVO();
        			obj.setCodigo(resultado.getInt("codigo"));
        			obj.getProcSeletivo().setCodigo(resultado.getInt("procSeletivo"));
        			obj.getUnidadeEnsino().setCodigo(resultado.getInt("unidadeEnsino"));
        			obj.getUnidadeEnsino().setNome(resultado.getString("unidadeEnsino_nome"));
        			obj.getUnidadeEnsino().setAbreviatura(resultado.getString("unidadeEnsino_abreviatura"));
        			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
                    	objetos.add(obj);
                    }else if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
                        objetos.add(obj);
                    }else {
                    	obj.setProcSeletivoUnidadeEnsinoEixoCursoVOs(ProcSeletivoUnidadeEnsinoEixoCurso.consultarProcSeletivoEixoCursoVOs(obj.getCodigo(), nivelMontarDados, usuario));
                    	objetos.add(obj);
                    }
        		}
        		ProcSeletivoUnidadeEnsinoVO obj = objetos.stream().filter(u -> u.getCodigo().equals(resultado.getInt("codigo"))).findFirst().get();
        		if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSMINIMOS && Uteis.isColunaExistente(resultado, "ProcSeletivoCurso_codigo") && Uteis.isAtributoPreenchido(resultado.getInt("ProcSeletivoCurso_codigo"))) {
        			ProcSeletivoCursoVO pCursoVO =  new ProcSeletivoCursoVO();
        			pCursoVO.setNovoObj(false);
        			pCursoVO.setProcSeletivoUnidadeEnsino(obj);
        			pCursoVO.setCodigo(resultado.getInt("ProcSeletivoCurso_codigo"));
        			pCursoVO.setNumeroVaga(resultado.getInt("ProcSeletivoCurso_numeroVaga"));
        			pCursoVO.setDataInicioProcSeletivoCurso(resultado.getDate("ProcSeletivoCurso_dataInicioProcSeletivoCurso"));
        			pCursoVO.setDataFimProcSeletivoCurso(resultado.getDate("ProcSeletivoCurso_dataFimProcSeletivoCurso"));
        			pCursoVO.getUnidadeEnsinoCurso().setUnidadeEnsino(obj.getUnidadeEnsino().getCodigo());
        			pCursoVO.getUnidadeEnsinoCurso().setCodigo(resultado.getInt("unidadeensinocurso_codigo"));
        			pCursoVO.getUnidadeEnsinoCurso().getCurso().setCodigo(resultado.getInt("curso_codigo"));
        			pCursoVO.getUnidadeEnsinoCurso().getCurso().setNome(resultado.getString("curso_nome"));
        			pCursoVO.getUnidadeEnsinoCurso().getCurso().setAbreviatura(resultado.getString("curso_abreviatura"));
        			pCursoVO.getUnidadeEnsinoCurso().getCurso().setPeriodicidade(resultado.getString("curso_periodicidade"));
        			pCursoVO.getUnidadeEnsinoCurso().getCurso().setNivelEducacional(resultado.getString("curso_nivelEducacional"));
        			pCursoVO.getUnidadeEnsinoCurso().getTurno().setCodigo(resultado.getInt("turno_codigo"));
        			pCursoVO.getUnidadeEnsinoCurso().getTurno().setNome(resultado.getString("turno_nome"));
        			
        			pCursoVO.getGrupoDisciplinaProcSeletivo().setCodigo(resultado.getInt("GrupoDisciplinaProcSeletivo_codigo"));
        			pCursoVO.getGrupoDisciplinaProcSeletivo().setDescricao(resultado.getString("GrupoDisciplinaProcSeletivo_descricao"));
        			
        			obj.getProcSeletivoCursoVOs().add(pCursoVO);
        		}
        	}while(resultado.next());
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProcSeletivoUnidadeEnsinoVO consultarPorChavePrimaria(Integer unidadeEnsinoPrm, Integer procSeletivoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProcSeletivoUnidadeEnsino WHERE unidadeEnsino = ? and procSeletivo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, unidadeEnsinoPrm.intValue(), procSeletivoPrm.intValue());
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
        return ProcSeletivoUnidadeEnsino.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ProcSeletivoUnidadeEnsino.idEntidade = idEntidade;
    }
    
    @Override
    public ProcSeletivoUnidadeEnsinoVO inicializarDadosImportarCandidatoInscricaoProcSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoVO procSeletivoVO, Map<String, ProcSeletivoUnidadeEnsinoVO> mapProcSeletivoUnidadeEnsinoVOs,  UsuarioVO usuario) throws Exception {
    		
    		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeTurno(importarCandidatoVO.getCursoVO().getCodigo(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), importarCandidatoVO.getTurnoVO().getCodigo(), usuario);
    		if (!Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getCodigo())) {
    			throw new Exception("Unidade de Ensino Curso não encontrada. Favor realizar o cadastro do Curso "+importarCandidatoVO.getNomeCurso()+" na Unidade de Ensino "+importarCandidatoVO.getIdPolo()+". ");
    		} 
    		
    		if (mapProcSeletivoUnidadeEnsinoVOs.containsKey(procSeletivoVO.getDescricao().toString() +"."+ importarCandidatoVO.getUnidadeEnsinoVO().getCodigo().toString())) {
    			ProcSeletivoUnidadeEnsinoVO procUnidadeVO = mapProcSeletivoUnidadeEnsinoVOs.get(procSeletivoVO.getDescricao().toString() +"."+ importarCandidatoVO.getUnidadeEnsinoVO().getCodigo().toString());
    			if (!procUnidadeVO.getContemProcSeletivoCurso(procSeletivoVO.getCodigo(), importarCandidatoVO.getCursoVO().getCodigo(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), importarCandidatoVO.getTurnoVO().getCodigo())) {
    				procUnidadeVO.getProcSeletivoCursoVOs().add(getFacadeFactory().getProcSeletivoCursoFacade().inicializarDadosImportarCandidatoInscricaoProcSeletivo(importarCandidatoVO, procUnidadeVO, unidadeEnsinoCursoVO, usuario));
    			}
    			return null;
    		}
    		if (Uteis.isAtributoPreenchido(procSeletivoVO.getCodigo())) {
    			ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoExistenteVO = consultarPorProcSeletivoUnidadeEnsino(procSeletivoVO.getCodigo(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        		if (Uteis.isAtributoPreenchido(procSeletivoUnidadeEnsinoExistenteVO.getCodigo())) {
        			if (!procSeletivoUnidadeEnsinoExistenteVO.getContemProcSeletivoCurso(procSeletivoVO.getCodigo(), importarCandidatoVO.getCursoVO().getCodigo(), importarCandidatoVO.getUnidadeEnsinoVO().getCodigo(), importarCandidatoVO.getTurnoVO().getCodigo())) {
        				procSeletivoUnidadeEnsinoExistenteVO.getProcSeletivoCursoVOs().add(getFacadeFactory().getProcSeletivoCursoFacade().inicializarDadosImportarCandidatoInscricaoProcSeletivo(importarCandidatoVO, procSeletivoUnidadeEnsinoExistenteVO, unidadeEnsinoCursoVO, usuario));
        			}
        			mapProcSeletivoUnidadeEnsinoVOs.put(procSeletivoVO.getDescricao().toString() +"."+ importarCandidatoVO.getUnidadeEnsinoVO().getCodigo().toString(), procSeletivoUnidadeEnsinoExistenteVO);
        			return procSeletivoUnidadeEnsinoExistenteVO;
        		}
    		}
    		
    	
    	ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO = new ProcSeletivoUnidadeEnsinoVO();
    	procSeletivoUnidadeEnsinoVO.setProcSeletivo(procSeletivoVO);
    	
    	procSeletivoUnidadeEnsinoVO.getUnidadeEnsino().setCodigo(unidadeEnsinoCursoVO.getUnidadeEnsino());
		procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs().add(getFacadeFactory().getProcSeletivoCursoFacade().inicializarDadosImportarCandidatoInscricaoProcSeletivo(importarCandidatoVO, procSeletivoUnidadeEnsinoVO, unidadeEnsinoCursoVO, usuario));
		mapProcSeletivoUnidadeEnsinoVOs.put(procSeletivoVO.getDescricao().toString() +"."+ importarCandidatoVO.getUnidadeEnsinoVO().getCodigo().toString(), procSeletivoUnidadeEnsinoVO);
		return procSeletivoUnidadeEnsinoVO;
    }
    
    @Override
    public ProcSeletivoUnidadeEnsinoVO consultarPorProcSeletivoUnidadeEnsino(Integer procSeletivo, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select * from procseletivounidadeensino ");
    	sb.append(" where procseletivo = ").append(procSeletivo);    	
    	sb.append(" and unidadeensino = ").append(unidadeEnsino);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (tabelaResultado.next()) {
    		return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
    	}
    	return new ProcSeletivoUnidadeEnsinoVO();
    }
    
    @Override
    public void adicionarProcSeletivoUnidadeEnsinoEixoCursoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO) throws Exception{
    	if (!Uteis.isAtributoPreenchido(procSeletivoUnidadeEnsinoEixoCursoVO.getEixoCurso())) {
    		throw new Exception("Eixo de Curso não informado!"); 
    	}
//    	if (!Uteis.isAtributoPreenchido(procSeletivoUnidadeEnsinoEixoCursoVO.getNrVagasEixoCurso())) {
//    		throw new Exception("Número de vagas não informado!");
//    	}
    	procSeletivoUnidadeEnsinoEixoCursoVO.setProcSeletivoUnidadeEnsino(procSeletivoUnidadeEnsinoVO);
    	procSeletivoUnidadeEnsinoEixoCursoVO.setEixoCurso(getFacadeFactory().getEixoCursoFacade().consultarPorChavePrimaria(procSeletivoUnidadeEnsinoEixoCursoVO.getEixoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA));
    	if (!procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs().stream().anyMatch(p -> p.getEixoCurso().getCodigo().equals(procSeletivoUnidadeEnsinoEixoCursoVO.getEixoCurso().getCodigo()))) {
    		procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs().add(procSeletivoUnidadeEnsinoEixoCursoVO);
    	} else {
    		procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs().stream().filter(p -> p.getEixoCurso().getCodigo().equals(procSeletivoUnidadeEnsinoEixoCursoVO.getEixoCurso().getCodigo())).findFirst().get().setNrVagasEixoCurso(procSeletivoUnidadeEnsinoEixoCursoVO.getNrVagasEixoCurso());
    	}

    }
    
    public void removerProcSeletivoUnidadeEnsinoEixoCursoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO) {
    	try {
    		ProcSeletivoUnidadeEnsinoEixoCursoVO obj = (ProcSeletivoUnidadeEnsinoEixoCursoVO) context().getExternalContext().getRequestMap()
					.get("procSeletivoEixoCursoItens");
			procSeletivoUnidadeEnsinoVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs().remove(obj);
    	} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    public static boolean isNumeric (String s) {
	    try {
	        Long.parseLong (s); 
	        return true;
	    } catch (NumberFormatException ex) {
	        return false;
	    }
	}
    
    public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}
    @Override
    public void realizarProcessamentoPlanilha(FileUploadEvent uploadEvent, ProcSeletivoVO procSeletivoVO, UsuarioVO usuario) throws Exception {
		String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
		
		int rowMax = 0;
		XSSFSheet mySheetXlsx = null;
		HSSFSheet mySheetXls = null;
		if (extensao.equals("xlsx")) {
			XSSFWorkbook workbook = new XSSFWorkbook(uploadEvent.getUploadedFile().getInputStream());
			mySheetXlsx = workbook.getSheetAt(0);
			rowMax = mySheetXlsx.getLastRowNum();

		} else {
			HSSFWorkbook workbook = new HSSFWorkbook(uploadEvent.getUploadedFile().getInputStream());
			mySheetXls = workbook.getSheetAt(0);
			rowMax = mySheetXls.getLastRowNum();
		}

		int qtdeLinhaEmBranco = 0;
		int linha = 0;
		
		Row row = null;
		List<ProcSeletivoUnidadeEnsinoEixoCursoVO> procSeletivoUnidadeEnsinoEixoCursoVOs = new ArrayList<ProcSeletivoUnidadeEnsinoEixoCursoVO>(0);
		while (linha <= rowMax) {
			if (extensao.equals("xlsx")) {
				row = mySheetXlsx.getRow(linha);
			} else {
				row = mySheetXls.getRow(linha);
			}
			if (linha == 0) {
				linha++;
				continue;
			}
			if (qtdeLinhaEmBranco == 2 ) {
				break;
			}
			if (getValorCelula(0, row, true) == null || getValorCelula(0, row, true).toString().equals("")) {
				qtdeLinhaEmBranco++;
				continue;
			}
			String unidadeEnsino = getValorCelula(0, row, true) != null ? String.valueOf(getValorCelula(0, row, true)) : "";
			if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			 throw new Exception("Unidade de ensino não preenchida no documento "+ uploadEvent.getUploadedFile().getName() + ", linha "+ (linha+1) + ".");
			}
			
			ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO = null;
			if (procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs().stream().anyMatch(t -> Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(t.getUnidadeEnsino().getNome())).trim().equalsIgnoreCase(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(unidadeEnsino)).trim()))) {
				procSeletivoUnidadeEnsinoVO = procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs().stream().filter(t -> Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(t.getUnidadeEnsino().getNome())).trim().equalsIgnoreCase(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(unidadeEnsino)).trim())).findFirst().get();
			}
			if (procSeletivoUnidadeEnsinoVO == null) {
				procSeletivoUnidadeEnsinoVO = new ProcSeletivoUnidadeEnsinoVO();
				UnidadeEnsinoVO unidade = new UnidadeEnsinoVO();
				unidade = getFacadeFactory().getUnidadeEnsinoFacade().consultarCodigoUnicaUnidadeEnsinoPorNome(unidadeEnsino, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (unidade == null) {
					throw new Exception("Unidade de ensino ("+unidadeEnsino +") na linha "+(linha+1)+" não cadastrada.");
				} else {
				procSeletivoUnidadeEnsinoVO.setUnidadeEnsino(unidade);
				procSeletivoVO.adicionarObjProcSeletivoUnidadeEnsinoVOs(procSeletivoUnidadeEnsinoVO);
				}
			}
			
			String eixo = getValorCelula(1, row, true) != null ? String.valueOf(getValorCelula(1, row, true)) : "";
			if (!Uteis.isAtributoPreenchido(eixo)) {
				throw new Exception("Eixo de Curso não preenchido no documento "+uploadEvent.getUploadedFile().getName()+ " na linha "+ (linha+1) + ".");
			}
						
			ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO = new ProcSeletivoUnidadeEnsinoEixoCursoVO();			
 			procSeletivoUnidadeEnsinoEixoCursoVO.setEixoCurso(getFacadeFactory().getEixoCursoFacade().consultarNomePorCodigo(eixo, Uteis.NIVELMONTARDADOS_DADOSMINIMOS)) ;
			procSeletivoUnidadeEnsinoEixoCursoVO.setProcSeletivoUnidadeEnsino(procSeletivoUnidadeEnsinoVO);
			
			Integer vagas = getValorCelula(2, row, false) != null && !getValorCelula(2, row, false).toString().equals("") ? Integer.valueOf(String.valueOf(getValorCelula(2, row, true))) : 0;
//			if (!Uteis.isAtributoPreenchido(vagas)) {
//				throw new Exception("Número de vagas não preenchido no arquivo "+uploadEvent.getUploadedFile().getName()+" na linha "+ (linha+1) +".");
//			} 
			procSeletivoUnidadeEnsinoEixoCursoVO.setNrVagasEixoCurso(vagas);
			adicionarProcSeletivoUnidadeEnsinoEixoCursoVO(procSeletivoUnidadeEnsinoVO, procSeletivoUnidadeEnsinoEixoCursoVO);
			
			linha++;
		}
		
    }
    
}
