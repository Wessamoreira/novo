package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.CursoCoordenadorInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoCoordenadorVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>CursoCoordenadorVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CursoCoordenadorVO
 * @see ControleAcesso
 * @see UnidadeEnsino
 */
@Repository
@Scope("singleton")
@Lazy
public class CursoCoordenador extends ControleAcesso implements CursoCoordenadorInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 498034497766682659L;
	protected static String idEntidade;

    public CursoCoordenador() throws Exception {
        super();
        setIdEntidade("Curso");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CursoCoordenadorVO</code>.
     */
    public CursoCoordenadorVO novo() throws Exception {
        CursoCoordenador.incluir(getIdEntidade());
        CursoCoordenadorVO obj = new CursoCoordenadorVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CursoCoordenadorVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public void validarDados(CursoCoordenadorVO obj, UsuarioVO usuario) throws ConsistirException {
        if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo FUNCIONARIO (CursoCoordenador) deve ser informado.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CursoCoordenadorVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CursoCoordenadorVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj, usuario);
        incluir(obj, "CursoCoordenador", new AtributoPersistencia()
        		.add("funcionario", obj.getFuncionario().getCodigo()) 
        		.add("curso", obj.getCurso().getCodigo()) 
        		.add("turma", obj.getTurma()) 
        		.add("porcentagemcomissao", obj.getPorcentagemComissao()) 
        		.add("valorfixocomissao", obj.getValorFixoComissao()) 
        		.add("valorporaluno", obj.getValorPorAluno()) 
        		.add("unidadeEnsino",  Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getCodigo()) ? obj.getUnidadeEnsino().getCodigo() : null) 
        		.add("datacriacao", Uteis.getDataJDBCTimestamp(new Date())) 
        		.add("tipoCoordenadorCurso", obj.getTipoCoordenadorCurso()) 
        		, usuario);       
        obj.setNovoObj(Boolean.FALSE);
        getFacadeFactory().getPessoaFacade().alterarCoordenador(obj.getFuncionario().getPessoa().getCodigo(), Boolean.TRUE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursoCoordenadorVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a
     * ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CursoCoordenadorVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj, usuario);        
        alterar(obj, "CursoCoordenador", new AtributoPersistencia()
        		.add("funcionario", obj.getFuncionario().getCodigo()) 
        		.add("curso", obj.getCurso().getCodigo()) 
        		.add("turma", obj.getTurma()) 
        		.add("porcentagemcomissao", obj.getPorcentagemComissao()) 
        		.add("valorfixocomissao", obj.getValorFixoComissao()) 
        		.add("valorporaluno", obj.getValorPorAluno()) 
        		.add("unidadeEnsino", Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getCodigo()) ? obj.getUnidadeEnsino().getCodigo() : null) 
        		.add("datacriacao", Uteis.getDataJDBCTimestamp(new Date())) 
        		.add("tipoCoordenadorCurso", obj.getTipoCoordenadorCurso()) 
        		, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);       
        getFacadeFactory().getPessoaFacade().alterarCoordenador(obj.getFuncionario().getPessoa().getCodigo(), Boolean.TRUE);    
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarCursoCoordenadorVOs(CursoVO obj, UsuarioVO usuario) throws Exception {
        for (CursoCoordenadorVO cursoCoordenadorVO : obj.getCursoCoordenadorVOs()) {
            cursoCoordenadorVO.getCurso().setCodigo(obj.getCodigo());
            if (cursoCoordenadorVO.getCodigo().equals(0)) {
                incluir(cursoCoordenadorVO, usuario);
            } else {
                alterar(cursoCoordenadorVO, usuario);
            }
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CursoCoordenadorVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente
     * verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CursoCoordenadorVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception {
        CursoCoordenador.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoCoordenador WHERE ((curso = ?) and (funcionario = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCurso().getCodigo().intValue(), obj.getFuncionario().getCodigo().intValue()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(Integer codigoCursoCoordenador, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM CursoCoordenador WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{codigoCursoCoordenador.intValue()});
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoCoordenador</code> através do valor do atributo <code>nome</code> da classe <code>Turno</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>CursoCoordenadorVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CursoCoordenadorVO> consultarPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CursoCoordenador.* FROM CursoCoordenador, Turma WHERE CursoCoordenador.turma = Turma.codigo and Turma.identificadorturma like('" + valorConsulta + "%') ORDER BY Turma.identificadorturma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoCoordenador</code> através do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>CursoCoordenadorVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CursoCoordenadorVO> consultarPorNomeCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT CursoCoordenador.* FROM CursoCoordenador, Curso WHERE CursoCoordenador.curso = Curso.codigo and LOWER(Curso.nome) like('" + valorConsulta.toLowerCase()
                + "%') ORDER BY Curso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public CursoCoordenadorVO consultarPorMatriculaAluno(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT uec.* FROM cursocoordenador uec ");
        sqlStr.append("INNER JOIN matricula m ON uec.curso = m.curso ");
        sqlStr.append("WHERE m.matricula = '" + valorConsulta + "' ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        CursoCoordenadorVO unidadeEnsinoCursoVO = new CursoCoordenadorVO();
        if (!tabelaResultado.next()) {
            throw new Exception();
        }
        return montarDados(tabelaResultado, unidadeEnsinoCursoVO, nivelMontarDados, usuario);
    }

    public CursoCoordenadorVO consultarPorCursoTurma(Integer curso, Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT cursocoordenador.* FROM cursocoordenador ");
        sqlStr.append("WHERE curso = " + curso + " AND turma = " + turma + " ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        CursoCoordenadorVO unidadeEnsinoCursoVO = new CursoCoordenadorVO();
        if (!tabelaResultado.next()) {
            throw new Exception();
        }
        return montarDados(tabelaResultado, unidadeEnsinoCursoVO, nivelMontarDados, usuario);
    }

    public List<CursoCoordenadorVO> consultarPorCodigoMatriculaPeriodoTurmaDisciplina(Integer codMatPerTurDisc, int nivelMontarDados, UsuarioVO usuario) throws Exception {        
        StringBuilder sqlStr = new StringBuilder("select cursocoordenador.* from matriculaperiodoturmadisciplina ");
        sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma inner join curso on curso.codigo = turma.curso inner join cursocoordenador on cursocoordenador.curso = curso.codigo ");
        sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(codMatPerTurDisc).append(" ORDER BY cursocoordenador.funcionario ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    }

    public List<CursoCoordenadorVO> consultarPorCodigoCurso(Integer valorConsulta, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT CursoCoordenador.* FROM CursoCoordenador ");
        sqlStr.append("INNER JOIN curso ON CursoCoordenador.curso = Curso.codigo ");
        sqlStr.append("WHERE CursoCoordenador.curso = ").append(valorConsulta).append(" ORDER BY Curso.Nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoCoordenador</code> através do valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>CursoCoordenadorVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CursoCoordenadorVO> consultarPorCodigoCursoUnidadeEnsino(Integer valorConsulta, Integer funcionarioCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT cursocoordenador.* FROM  cursocoordenador WHERE curso = " + valorConsulta.intValue() + " and funcionario = " + funcionarioCodigo.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CursoCoordenadorVO> consultarPorCodigoCursoFuncionarioNivelEducacional(Integer valorConsulta, Integer funcionarioCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT cursocoordenador.* FROM  cursocoordenador ");
        sqlStr.append(" INNER JOIN curso ON curso.codigo = cursocoordenador.curso");
        sqlStr.append(" WHERE cursocoordenador.curso >= ").append(valorConsulta).append(" and curso.niveleducacional = '").append(nivelEducacional).append("'");
        if (funcionarioCodigo != null && funcionarioCodigo != 0) {
            sqlStr.append(" AND cursocoordenador.funcionario = ").append(funcionarioCodigo);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CursoCoordenadorVO> consultarPorCodigoCursoUnidadeEnsinoPeriodicidade(Integer valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT cursocoordenador.* FROM  cursocoordenador inner join curso on (cursocoordenador.curso = curso.codigo) WHERE curso >= " + valorConsulta.intValue();
        if (!nivelEducacional.equals("")) {
            if (nivelEducacional.equals("SU")) {
                // neste caso temos que filtrar os diferentes tipos de cursos superior existentes,
                // a citar: superior, graduação tecnologica ou sequencial
                sqlStr = sqlStr.concat(" and ((curso.nivelEducacional = 'SU') OR " + "(curso.nivelEducacional = 'GT') OR " + "(curso.nivelEducacional = 'SE'))");
            } else {
                sqlStr = sqlStr.concat(" and (curso.nivelEducacional = '" + nivelEducacional.toUpperCase() + "')");
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CursoCoordenadorVO> consultarPorNomeCursoFuncionario(String valorConsulta, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), b, usuario);
        String sqlStr = "SELECT cursocoordenador.* FROM   cursocoordenador ,curso where cursocoordenador.curso=curso.codigo " + "and  lower (curso.nome) like('" + valorConsulta.toLowerCase()
                + "%') and cursocoordenador.funcionario = " + unidadeEnsinoCodigo.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CursoCoordenadorVO> consultarPorNomeCursoUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), b, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT cursocoordenador.* FROM  cursocoordenador");
        sqlStr.append(" INNER JOIN curso ON curso.codigo = cursocoordenador.curso");
        sqlStr.append(" WHERE Upper(curso.nome) like ('").append(valorConsulta.toUpperCase()).append("%') and curso.niveleducacional = '").append(nivelEducacional).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public  List<CursoCoordenadorVO> consultarCursoCoordenadors(Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        List<CursoCoordenadorVO> objetos = new ArrayList<CursoCoordenadorVO>(0);
        String sql = "SELECT * FROM cursocoordenador WHERE curso = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{curso});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, usuario));
        }
        return objetos;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>CursoCoordenadorVO</code> resultantes da consulta.
     */
    public  List<CursoCoordenadorVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<CursoCoordenadorVO> vetResultado = new ArrayList<CursoCoordenadorVO>(0);
        CursoCoordenadorVO obj = null;
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>CursoCoordenadorVO</code>.
     *
     * @return O objeto da classe <code>CursoCoordenadorVO</code> com os dados devidamente montados.
     */
    public  CursoCoordenadorVO montarDados(SqlRowSet dadosSQL, CursoCoordenadorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        // //System.out.println(">> Montar dados(CursoCoordenador) - " + new Date());
        obj = new CursoCoordenadorVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
        obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
        obj.setPorcentagemComissao(dadosSQL.getDouble("porcentagemcomissao"));
        obj.setValorFixoComissao(dadosSQL.getDouble("valorfixocomissao"));
        obj.setValorPorAluno(dadosSQL.getBoolean("valorporaluno"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setTipoCoordenadorCurso(TipoCoordenadorCursoEnum.valueOf(dadosSQL.getString("tipoCoordenadorCurso")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        return obj;
    }

    public  CursoCoordenadorVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        CursoCoordenadorVO obj = new CursoCoordenadorVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
        obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
        obj.setPorcentagemComissao(dadosSQL.getDouble("porcentagemcomissao"));
        obj.setValorFixoComissao(dadosSQL.getDouble("valorfixocomissao"));
        obj.setValorPorAluno(dadosSQL.getBoolean("valorporaluno"));
        obj.setTipoCoordenadorCurso(TipoCoordenadorCursoEnum.valueOf(dadosSQL.getString("tipoCoordenadorCurso")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto <code>CursoCoordenadorVO</code>. Faz uso da chave primária da classe
     * <code>TurnoVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosTurma(CursoCoordenadorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), NivelMontarDados.BASICO, usuario);
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>CursoCoordenadorVO</code>. Faz uso da chave primária da classe
     * <code>CursoVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosCurso(CursoCoordenadorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        getFacadeFactory().getCursoFacade().carregarDados(obj.getCurso(), NivelMontarDados.BASICO, usuario);
    }

    public  void montarDadosUnidadeEnsino(CursoCoordenadorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(obj.getUnidadeEnsino(), NivelMontarDados.BASICO, usuario);
    }

    public  void montarDadosFuncionario(CursoCoordenadorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionario().getCodigo().intValue() == 0) {
            obj.setFuncionario(new FuncionarioVO());
            return;
        }
        getFacadeFactory().getFuncionarioFacade().carregarDados(obj.getFuncionario(), NivelMontarDados.BASICO, usuario);
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>CursoCoordenadorVO</code> no BD. Faz uso da operação <code>excluir</code> disponível na classe <code>CursoCoordenador</code>.
     *
     * @param <code>unidadeEnsino</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCursoCoordenador(Integer curso, UsuarioVO usuario) throws Exception {
        CursoCoordenador.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoCoordenador WHERE (curso = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{curso.intValue()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCursoCoordenador(Integer curso, List<CursoCoordenadorVO> objetos, UsuarioVO usuario) throws Exception {
        CursoCoordenador.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoCoordenador WHERE (curso = ?)";
        Iterator<CursoCoordenadorVO> i = objetos.iterator();
        while (i.hasNext()) {
            CursoCoordenadorVO obj = (CursoCoordenadorVO) i.next();
            if (obj.getCodigo().intValue() != 0) {
                sql += " and codigo != " + obj.getCodigo().intValue();
            }
        }
        getConexao().getJdbcTemplate().update(sql+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[]{curso.intValue()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>CursoCoordenadorVO</code> contidos em um Hashtable no BD. Faz uso da operação <code>excluirCursoCoordenadors</code> e
     * <code>incluirCursoCoordenadors</code> disponíveis na classe <code>CursoCoordenador</code>.
     *
     * @param objetos
     *            List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarCursoCoordenador(Integer curso, List<CursoCoordenadorVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<CursoCoordenadorVO> e = objetos.iterator();
        while (e.hasNext()) {
            CursoCoordenadorVO obj = (CursoCoordenadorVO) e.next();
            obj.getFuncionario().setCodigo(curso);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, usuario);
            } else {
                alterar(obj, usuario);
            }
        }
        excluirCursoCoordenador(curso, objetos, usuario);
    }

    /**
     * Operação responsável por incluir objetos da <code>CursoCoordenadorVO</code> no BD. Garantindo o relacionamento com a entidade principal <code>administrativo.UnidadeEnsino</code> através do
     * atributo de vínculo.
     *
     * @param objetos
     *            List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirCursoCoordenador(Integer curso, List<CursoCoordenadorVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<CursoCoordenadorVO> e = objetos.iterator();
        while (e.hasNext()) {
            CursoCoordenadorVO obj = (CursoCoordenadorVO) e.next();
            obj.getCurso().setCodigo(curso);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, usuario);
            } else {
                alterar(obj, usuario);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>CursoCoordenadorVO</code> relacionados a um objeto da classe <code>administrativo.UnidadeEnsino</code>.
     *
     * @param unidadeEnsino
     *            Atributo de <code>administrativo.UnidadeEnsino</code> a ser utilizado para localizar os objetos da classe <code>CursoCoordenadorVO</code>.
     * @return List Contendo todos os objetos da classe <code>CursoCoordenadorVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List<CursoCoordenadorVO> consultarCursoCoordenadors(Integer curso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CursoCoordenador.consultar(getIdEntidade());
        List<CursoCoordenadorVO> objetos = new ArrayList<CursoCoordenadorVO>(0);
        String sql = "SELECT * FROM CursoCoordenador WHERE curso = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{curso.intValue()});
        CursoCoordenadorVO obj = null;
        while (resultado.next()) {
            objetos.add(montarDados(resultado, obj, nivelMontarDados, usuario));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CursoCoordenadorVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CursoCoordenadorVO consultarPorChavePrimaria(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CursoCoordenador WHERE curso = ? and funcionario = ? and turma = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{cursoPrm.intValue(), unidadeEnsinoPrm.intValue(), turnoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        CursoCoordenadorVO obj = null;
        return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
    }

    public CursoCoordenadorVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CursoCoordenador WHERE codigo = ? ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        CursoCoordenadorVO obj = null;
        return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CursoCoordenador.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CursoCoordenador.idEntidade = idEntidade;
    }

    public void carregarDados(CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception {
        carregarDados(obj, NivelMontarDados.BASICO, usuario);
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT CursoCoordenador.*, ");
        str.append("Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", ");
        str.append("Turma.identificadorturma as \"Turma.identificadorturma\", Turma.codigo as \"Turma.codigo\", ");
        str.append("Funcionario.codigo as \"Funcionario.codigo\", ");
        str.append("Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", ");
        str.append("UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\" ");
        str.append(" FROM CursoCoordenador  ");
        str.append("      LEFT JOIN Curso ON (CursoCoordenador.curso = Curso.codigo) ");
        str.append("      LEFT JOIN Turma ON (CursoCoordenador.turma = Turma.codigo) ");
        str.append("      LEFT JOIN Funcionario ON (CursoCoordenador.funcionario = Funcionario.codigo) ");
        str.append("      LEFT JOIN Pessoa ON (Funcionario.pessoa = Pessoa.codigo) ");
        str.append("      LEFT JOIN UnidadeEnsino ON (UnidadeEnsino.codigo = cursoCoordenador.unidadeEnsino) ");
        return str;
    }

    private void montarDadosBasico(CursoCoordenadorVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        // Dados do CursoCoordenadorVO
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setPorcentagemComissao(dadosSQL.getDouble("porcentagemcomissao"));
        obj.setValorFixoComissao(dadosSQL.getDouble("valorfixocomissao"));
        obj.setValorPorAluno(dadosSQL.getBoolean("valorporaluno"));
        obj.setNovoObj(Boolean.FALSE);
        obj.setNivelMontarDados(NivelMontarDados.BASICO);
        // Dados do Curso
        obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
        obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
        obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
        obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
        // Dados da Turma
        obj.getTurma().setCodigo(dadosSQL.getInt("Turma.codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("Turma.identificadorturma"));
        obj.getTurma().setNivelMontarDados(NivelMontarDados.BASICO);
        // Dados do Funcionario
        obj.getFuncionario().setCodigo(dadosSQL.getInt("Funcionario.codigo"));
        obj.getFuncionario().setNivelMontarDados(NivelMontarDados.BASICO);
        obj.getFuncionario().getPessoa().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
        obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("Pessoa.nome"));
        obj.getFuncionario().getPessoa().setNivelMontarDados(NivelMontarDados.BASICO);
        // Dados do UnidadeEnsino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
        obj.setTipoCoordenadorCurso(TipoCoordenadorCursoEnum.valueOf(dadosSQL.getString("tipoCoordenadorCurso")));
        obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
    }

    public List<CursoCoordenadorVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<CursoCoordenadorVO> vetResultado = new ArrayList<CursoCoordenadorVO>(0);
        while (tabelaResultado.next()) {
            CursoCoordenadorVO obj = new CursoCoordenadorVO();
            montarDadosBasico(obj, tabelaResultado, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public void carregarDados(CursoCoordenadorVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico(obj, resultado, usuario);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico(obj, resultado, usuario);
        }
    }

    public CursoCoordenadorVO consultaRapidaPorCursoFuncionarioTurma(Integer cursoPrm, Integer funcionarioPrm, Integer turmaPrm, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (Curso.codigo = " + cursoPrm + ")");
        sqlStr.append("   AND (Turma.codigo = " + turmaPrm + ")");
        sqlStr.append("   AND (Funcionario.codigo = " + funcionarioPrm + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        CursoCoordenadorVO obj = new CursoCoordenadorVO();
        montarDadosBasico(obj, tabelaResultado, usuario);
        return obj;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (CursoCoordenador.codigo = " + codigo + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    public boolean validarExisteCoordenadorMaisDeUmCurso(Integer codigoPessoa, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT CASE WHEN(count(cursoCoordenador.codigo) > 0) THEN true ELSE false END as coordenador from cursoCoordenador ");
        sqlStr.append(" INNER JOIN funcionario on funcionario.codigo = cursoCoordenador.funcionario ");
        sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = funcionario.pessoa ");
        sqlStr.append(" WHERE pessoa.codigo = ").append(codigoPessoa);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getBoolean("coordenador");
        }
        return false;
    }

    @Override
    public List<TipoNivelEducacional> consultarNivelEducacionalCursosCoordenador(Integer codigoPessoa, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT curso.niveleducacional ");
        sqlStr.append(" FROM cursocoordenador cc");
        sqlStr.append(" INNER JOIN curso ON curso.codigo = cc.curso");
        sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append(" WHERE pessoa.codigo = ").append(codigoPessoa);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List<TipoNivelEducacional> objetos = new ArrayList<TipoNivelEducacional>(0);
        while(tabelaResultado.next()) {
            objetos.add(TipoNivelEducacional.getEnum(tabelaResultado.getString("niveleducacional")));
        }
        return objetos;
    }

    @Override
	public boolean consultarSeExisteCoordenadorPorUsuario(UsuarioVO usuario, CursoVO curso, TurmaVO turma) throws Exception {
		 StringBuilder sqlStr = new StringBuilder(" SELECT count(1) as qtd FROM cursocoordenador ");
		 sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		 sqlStr.append(" WHERE funcionario.pessoa  = ? ");
		 if(Uteis.isAtributoPreenchido(curso)){
			 sqlStr.append(" and cursocoordenador.curso =  ").append(curso.getCodigo());  
		 }
		 if(Uteis.isAtributoPreenchido(turma)){
			 sqlStr.append(" and ( cursocoordenador.curso in ( select curso from turma where codigo = ").append(turma.getCodigo()).append(" ) or cursocoordenador.turma =  ").append(turma.getCodigo()).append(" )");  
		 }
		 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), usuario.getPessoa().getCodigo());
		 return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}
    
    @Override
    public boolean consultarPorFuncionarioUnidadeEnsinoTurma(UsuarioVO usuario, MatriculaPeriodoVO matriculaPeriodo) throws Exception {
//    	matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorCodigo(matriculaPeriodo.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, usuario);
    	MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaPeriodo.getMatricula(), null, NivelMontarDados.BASICO, usuario);
    	
    	StringBuilder sqlStr = new StringBuilder(" SELECT cursocoordenador.* ");
    	sqlStr.append(" FROM cursocoordenador ");
    	sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
    	sqlStr.append(" WHERE funcionario.pessoa  = ").append(usuario.getPessoa().getCodigo());
    	sqlStr.append(" AND (( unidadeensino = ").append(matricula.getUnidadeEnsino().getCodigo()).append(" AND curso = ").append(matricula.getCurso().getCodigo()).append(" AND turma = ").append(matriculaPeriodo.getTurma().getCodigo()).append(" ) "); 
    	sqlStr.append(" OR ( unidadeensino =  ").append(matricula.getUnidadeEnsino().getCodigo()).append(" AND curso = ").append(matricula.getCurso().getCodigo()).append(" AND turma is null ) ");
    	sqlStr.append(" OR ( unidadeensino is null ").append(" AND curso = ").append(matricula.getCurso().getCodigo()).append(" AND turma is null ))");
    	sqlStr.append(" LIMIT 1 ");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	
    	if (tabelaResultado.next()) {
    		return true;
    	}
    	return false;
    }
    
	
    @Override
    public List<CursoCoordenadorVO> consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(Integer pessoa, Integer unidadeEnsino, String nivelEducacional, Integer curso, Integer turma, Boolean coordenadorGealAmbos, UsuarioVO usuario) throws Exception{
    	return consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(pessoa, unidadeEnsino, nivelEducacional, curso, turma, coordenadorGealAmbos, Boolean.FALSE, usuario);
    }

	@Override
	public List<CursoCoordenadorVO> consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(Integer pessoa, Integer unidadeEnsino, String nivelEducacional, Integer curso, Integer turma, Boolean coordenadorGealAmbos, Boolean apresentarCoordenadorSemUnidadeEnsino, UsuarioVO usuario) throws Exception{
		 StringBuilder sqlStr = new StringBuilder(getSQLPadraoConsultaBasica());
		 sqlStr.append(" WHERE pessoa.ativo ");
		 if(Uteis.isAtributoPreenchido(pessoa)){
			 sqlStr.append(" AND funcionario.pessoa = ").append(pessoa);
		 }
		 if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			if (apresentarCoordenadorSemUnidadeEnsino) {
				sqlStr.append(" AND  ((cursocoordenador.unidadeensino = ").append(unidadeEnsino).append(" ) OR (cursocoordenador.unidadeensino IS NULL))");
			} else {
				sqlStr.append(" AND  cursocoordenador.unidadeensino = ").append(unidadeEnsino);
			}
		 }
		 if(Uteis.isAtributoPreenchido(curso)){
			 sqlStr.append(" AND  cursocoordenador.curso = ").append(curso);
		 }
		 if(Uteis.isAtributoPreenchido(turma)){
			 sqlStr.append(" AND  (cursocoordenador.turma = ").append(turma);
			 sqlStr.append(" or curso.codigo = (select curso from turma where codigo = ").append(turma).append(") )");
		 }
		 if(Uteis.isAtributoPreenchido(nivelEducacional)){
			 sqlStr.append(" AND  curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		 }
		 if (coordenadorGealAmbos) {
			 sqlStr.append(" AND cursocoordenador.tipocoordenadorcurso IN ('AMBOS', 'GERAL') ");
		 }
		 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		 return montarDadosConsultaBasica(tabelaResultado, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFuncionarioCursoCoordenadorUnificacaoFuncionario(Integer funcionarioAntigo, Integer funcionarioNovo) throws Exception {
		String sqlStr = "UPDATE cursocoordenador set funcionario=? WHERE ((funcionario = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { funcionarioNovo, funcionarioAntigo });
	}
	
	@Override
	public List<CursoCoordenadorVO> consultarPorCodigoCursoECodigoUnidadeEnsino(int nivelMontarDados, UsuarioVO usuario) throws Exception {
      
		StringBuilder sqlStr = new StringBuilder();	
		
		sqlStr.append("SELECT cursocoordenador.codigo,funcionario,curso,turma,porcentagemcomissao,valorfixocomissao,valorporaluno,unidadeEnsino,tipoCoordenadorCurso FROM  cursocoordenador");
		sqlStr.append(" INNER JOIN funcionario ON cursocoordenador.funcionario = funcionario.codigo");
		sqlStr.append(" WHERE funcionario.pessoa ='").append(usuario.getPessoa().getCodigo()).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
}
