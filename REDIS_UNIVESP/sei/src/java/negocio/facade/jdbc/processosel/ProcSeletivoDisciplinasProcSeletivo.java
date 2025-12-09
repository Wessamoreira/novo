package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoDisciplinasProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ProcSeletivoDisciplinasProcSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ProcSeletivoDisciplinasProcSeletivoVO
 * @see ControleAcesso
 * @see ProcSeletivo
 */
@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoDisciplinasProcSeletivo extends ControleAcesso implements ProcSeletivoDisciplinasProcSeletivoInterfaceFacade {

    protected static String idEntidade;

    public ProcSeletivoDisciplinasProcSeletivo() throws Exception {
        super();
        setIdEntidade("ProcSeletivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     */
    public ProcSeletivoDisciplinasProcSeletivoVO novo() throws Exception {
        ProcSeletivoDisciplinasProcSeletivo.incluir(getIdEntidade());
        ProcSeletivoDisciplinasProcSeletivoVO obj = new ProcSeletivoDisciplinasProcSeletivoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcSeletivoDisciplinasProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProcSeletivoDisciplinasProcSeletivoVO.validarDados(obj);
            final String sql = "INSERT INTO ProcSeletivoDisciplinasProcSeletivo( procSeletivo, disciplinasProcSeletivo ) VALUES ( ?, ? ) ";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getProcSeletivo().intValue());
                    sqlInserir.setInt(2, obj.getDisciplinasProcSeletivo().getCodigo().intValue());
                    return sqlInserir;
                }
            });
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProcSeletivoDisciplinasProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
        ProcSeletivoDisciplinasProcSeletivoVO.validarDados(obj);
        final String sql = "UPDATE ProcSeletivoDisciplinasProcSeletivo set procSeletivo = ?, disciplinasProcSeletivo = ?  WHERE ((procSeletivo = ?) and (disciplinasProcSeletivo = ?))";
        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getProcSeletivo().intValue());
                sqlAlterar.setInt(2, obj.getDisciplinasProcSeletivo().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getProcSeletivo().intValue());
                sqlAlterar.setInt(4, obj.getDisciplinasProcSeletivo().getCodigo().intValue());
                return sqlAlterar;
            }
        })==0){
        	incluir(obj, usuarioVO);
        	return;
        };
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProcSeletivoDisciplinasProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
        ProcSeletivoDisciplinasProcSeletivo.excluir(getIdEntidade());
        String sql = "DELETE FROM ProcSeletivoDisciplinasProcSeletivo WHERE ((procSeletivo = ?) and (disciplinasProcSeletivo = ?))";
        getConexao().getJdbcTemplate().update(sql, obj.getProcSeletivo().intValue(), obj.getDisciplinasProcSeletivo().getCodigo().intValue());
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivoDisciplinasProcSeletivo</code> através do valor do atributo 
     * <code>nome</code> da classe <code>DisciplinasProcSeletivo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeDisciplinasProcSeletivo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivoDisciplinasProcSeletivo.* FROM ProcSeletivoDisciplinasProcSeletivo, DisciplinasProcSeletivo WHERE ProcSeletivoDisciplinasProcSeletivo.disciplinasProcSeletivo = DisciplinasProcSeletivo.codigo and DisciplinasProcSeletivo.nome like('" + valorConsulta + "%') ORDER BY DisciplinasProcSeletivo.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarDisciplinasProcSeletivoPorCodigoProcSeletivo(Integer inscricao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct ProcSeletivoDisciplinasProcSeletivo.* FROM ProcSeletivoDisciplinasProcSeletivo "
                + " inner join inscricao on inscricao.procseletivo = ProcSeletivoDisciplinasProcSeletivo.procseletivo "
                + " inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = ProcSeletivoDisciplinasProcSeletivo.disciplinasprocseletivo "
                + " WHERE inscricao.codigo = " + inscricao.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * @return  O objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> com os dados devidamente montados.
     */
    public static ProcSeletivoDisciplinasProcSeletivoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        ProcSeletivoDisciplinasProcSeletivoVO obj = new ProcSeletivoDisciplinasProcSeletivoVO();
        obj.setProcSeletivo(new Integer(dadosSQL.getInt("procSeletivo")));
        obj.getDisciplinasProcSeletivo().setCodigo(new Integer(dadosSQL.getInt("disciplinasProcSeletivo")));
        obj.setNovoObj(Boolean.FALSE);

        montarDadosDisciplinasProcSeletivo(obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DisciplinasProcSeletivoVO</code> relacionado ao objeto <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Faz uso da chave primária da classe <code>DisciplinasProcSeletivoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDisciplinasProcSeletivo(ProcSeletivoDisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplinasProcSeletivo().getCodigo().intValue() == 0) {
            obj.setDisciplinasProcSeletivo(new DisciplinasProcSeletivoVO());
            return;
        }
        obj.setDisciplinasProcSeletivo(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(obj.getDisciplinasProcSeletivo().getCodigo(), usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
     * @param <code>procSeletivo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcSeletivoDisciplinasProcSeletivos(Integer procSeletivo) throws Exception {
        String sql = "DELETE FROM ProcSeletivoDisciplinasProcSeletivo WHERE (procSeletivo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{procSeletivo});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirProcSeletivoDisciplinasProcSeletivos</code> e <code>incluirProcSeletivoDisciplinasProcSeletivos</code> disponíveis na classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarProcSeletivoDisciplinasProcSeletivos(Integer procSeletivo, List<ProcSeletivoDisciplinasProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sql  = new StringBuilder("DELETE FROM procseletivodisciplinasprocseletivo WHERE procSeletivo = ").append(procSeletivo).append(" and disciplinasProcSeletivo not in (0 ");    	
    	for(ProcSeletivoDisciplinasProcSeletivoVO obj :objetos){            
            sql.append(", ").append(obj.getDisciplinasProcSeletivo().getCodigo());            
        }
    	sql.append(" ) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    	getConexao().getJdbcTemplate().update(sql.toString());
    	for(ProcSeletivoDisciplinasProcSeletivoVO obj :objetos){
    		obj.setProcSeletivo(procSeletivo);
            alterar(obj, usuarioVO);            
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirProcSeletivoDisciplinasProcSeletivos(Integer procSeletivoPrm, List<ProcSeletivoDisciplinasProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception {
        try {
            for (ProcSeletivoDisciplinasProcSeletivoVO obj : objetos) {
                obj.setProcSeletivo(procSeletivoPrm);
                incluir(obj, usuarioVO);
            }
        } catch (Exception e) {
            for (ProcSeletivoDisciplinasProcSeletivoVO obj : objetos) {
                obj.setNovoObj(true);
            }
            throw e;
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ProcSeletivoDisciplinasProcSeletivoVO</code> relacionados a um objeto da classe <code>processosel.ProcSeletivo</code>.
     * @param procSeletivo  Atributo de <code>processosel.ProcSeletivo</code> a ser utilizado para localizar os objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
	public static List consultarProcSeletivoDisciplinasProcSeletivos(Integer procSeletivo, UsuarioVO usuario)
			throws Exception {
		ProcSeletivoDisciplinasProcSeletivo.consultar(getIdEntidade());
		List objetos = new ArrayList();
		StringBuilder sql = new StringBuilder(
				"SELECT ProcSeletivoDisciplinasProcSeletivo.procSeletivo, ProcSeletivoDisciplinasProcSeletivo.disciplinasProcSeletivo, ");
		sql.append(" DisciplinasProcSeletivo.codigo, ");
		sql.append(" DisciplinasProcSeletivo.nome, DisciplinasProcSeletivo.tipoDisciplina, ");
		sql.append(" DisciplinasProcSeletivo.descricao, DisciplinasProcSeletivo.requisitos, ");
		sql.append(" DisciplinasProcSeletivo.bibliografia, DisciplinasProcSeletivo.disciplinaIdioma ");
		sql.append(" FROM ProcSeletivoDisciplinasProcSeletivo ");
		sql.append(
				" inner join  DisciplinasProcSeletivo on DisciplinasProcSeletivo.codigo = ProcSeletivoDisciplinasProcSeletivo.disciplinasProcSeletivo ");
		sql.append(" WHERE ProcSeletivoDisciplinasProcSeletivo.procSeletivo = ? ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), procSeletivo.intValue());
		while (resultado.next()) {
			ProcSeletivoDisciplinasProcSeletivoVO obj = new ProcSeletivoDisciplinasProcSeletivoVO();
			obj.setProcSeletivo(new Integer(resultado.getInt("procSeletivo")));
			obj.getDisciplinasProcSeletivo().setCodigo(new Integer(resultado.getInt("disciplinasProcSeletivo")));
			obj.getDisciplinasProcSeletivo().setCodigo(new Integer(resultado.getInt("codigo")));
			obj.getDisciplinasProcSeletivo().setNome(resultado.getString("nome"));
			obj.getDisciplinasProcSeletivo().setTipoDisciplina(resultado.getString("tipoDisciplina"));
			obj.getDisciplinasProcSeletivo().setDescricao(resultado.getString("descricao"));
			obj.getDisciplinasProcSeletivo().setRequisitos(resultado.getString("requisitos"));
			obj.getDisciplinasProcSeletivo().setBibliografia(resultado.getString("bibliografia"));
			obj.getDisciplinasProcSeletivo().setDisciplinaIdioma(new Boolean(resultado.getBoolean("disciplinaIdioma")));
			obj.getDisciplinasProcSeletivo().setNovoObj(Boolean.FALSE);
			obj.setNovoObj(Boolean.FALSE);

			objetos.add(obj);
		}
		return objetos;
	}

    public static List consultarProcSeletivoDisciplinasProcSeletivosLinguaExtrangeira(Integer procSeletivo, UsuarioVO usuario) throws Exception {
        ProcSeletivoDisciplinasProcSeletivo.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT ProcSeletivoDisciplinasProcSeletivo.* FROM ProcSeletivoDisciplinasProcSeletivo, disciplinasprocseletivo WHERE procSeletivo = ? and disciplinasprocseletivo.codigo = ProcSeletivoDisciplinasProcSeletivo.disciplinasprocseletivo and disciplinasprocseletivo.disciplinaidioma = true";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, procSeletivo.intValue());
        while (resultado.next()) {
            objetos.add(ProcSeletivoDisciplinasProcSeletivo.montarDados(resultado, usuario));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProcSeletivoDisciplinasProcSeletivoVO consultarPorChavePrimaria(Integer disciplinasProcSeletivoPrm, Integer procSeletivoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProcSeletivoDisciplinasProcSeletivo WHERE disciplinasProcSeletivo = ? and procSeletivo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, disciplinasProcSeletivoPrm.intValue(), procSeletivoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProcSeletivoDisciplinasProcSeletivo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ProcSeletivoDisciplinasProcSeletivo.idEntidade = idEntidade;
    }
}
