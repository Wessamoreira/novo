package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.NumeroMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.NumeroMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>NumeroMatriculaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>NumeroMatriculaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see NumeroMatriculaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class NumeroMatricula extends ControleAcesso implements NumeroMatriculaInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2865654251268840113L;
	protected static String idEntidade;

    public NumeroMatricula() throws Exception {
        super();
        setIdEntidade("NumeroMatricula");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>NumeroMatriculaVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>NumeroMatriculaVO</code> que será
     *            gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final NumeroMatriculaVO obj) throws Exception {
        try {
            NumeroMatriculaVO.validarDados(obj);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO NumeroMatricula( ano, semestre, unidadeEnsino, curso, incremental, mascara, cursoabreviatura, formaingresso ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if(obj.getMascara().contains("A")) {
                    	sqlInserir.setInt(1, obj.getAno().intValue());
                    } else {
                    	sqlInserir.setNull(1, 0);
                    }
                    if(obj.getMascara().contains("S")) {
                    	sqlInserir.setInt(2, obj.getSemestre().intValue());
                    } else {
                    	sqlInserir.setNull(2, 0);
                    }
                    if(obj.getMascara().contains("U")) {
                    	sqlInserir.setInt(3, obj.getUnidadeEnsino().intValue());
                    } else {
                    	sqlInserir.setNull(3, 0);
                    }
                    if(obj.getMascara().contains("C")) {
                    	sqlInserir.setInt(4, obj.getCurso().intValue());
                    } else {
                    	sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setInt(5, obj.getIncremental().intValue());
                    sqlInserir.setString(6, obj.getMascara());
                    if(obj.getMascara().contains("B")) {
                    	sqlInserir.setString(7, obj.getCursoAbreviatura());
                    } else {
                    	sqlInserir.setNull(7, 0);
                    }
                    if(obj.getMascara().contains("F")) {
                    	sqlInserir.setString(8, obj.getFormaIngresso());
                    } else {
                    	sqlInserir.setNull(8, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>NumeroMatriculaVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>NumeroMatriculaVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final NumeroMatriculaVO obj) throws Exception {
        try {
            NumeroMatriculaVO.validarDados(obj);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE NumeroMatricula set ano=?, semestre=?, unidadeEnsino=?, curso=?, incremental=?, mascara=?, , cursoabreviatura=?, formaingresso=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getMascara().contains("A")) {
                    	sqlAlterar.setInt(1, obj.getAno().intValue());
                    } else {
                    	sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getMascara().contains("S")) {
                    	sqlAlterar.setInt(2, obj.getSemestre().intValue());
                    } else {
                    	sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getMascara().contains("U")) {
                    	sqlAlterar.setInt(3, obj.getUnidadeEnsino().intValue());
                    } else {
                    	sqlAlterar.setNull(3, 0);
                    }
                    if (obj.getMascara().contains("C")) {
                    	sqlAlterar.setInt(4, obj.getCurso().intValue());
                    } else {
                    	sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setInt(5, obj.getIncremental().intValue());
                    sqlAlterar.setString(6, obj.getMascara());
                    if (obj.getMascara().contains("B")) {
                    	sqlAlterar.setString(7, obj.getCursoAbreviatura());
                    } else {
                    	sqlAlterar.setNull(7, 0);
                    }
                    if (obj.getMascara().contains("F")) {
                    	sqlAlterar.setString(8, obj.getFormaIngresso());
                    } else {
                    	sqlAlterar.setNull(8, 0);
                    }
                    sqlAlterar.setInt(9, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarIncremental(final NumeroMatriculaVO obj, Integer incremental, UsuarioVO usuario) {
		try {
			alterar(obj, "NumeroMatricula", new AtributoPersistencia().add("incremental", incremental), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>NumeroMatriculaVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>NumeroMatriculaVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void excluir(NumeroMatriculaVO obj) throws Exception {
        try {
            String sql = "DELETE FROM NumeroMatricula WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public  String gerarNumeroMatricula(MatriculaVO matriculaVO, int anoData) throws Exception {
        String mascaraPadrao = matriculaVO.getCurso().getConfiguracaoAcademico().getMascaraPadraoGeracaoMatricula();
        Integer ano = null;
        Integer semestre = null;
        SqlRowSet sqlRowSet = consultarAnoSemestreGeracaoNumeroMatricula(matriculaVO.getMatriculaPeriodoVOs().get(0).getUnidadeEnsinoCurso(), matriculaVO.getMatriculaPeriodoVOs().get(0).getProcessoMatricula());
        if (sqlRowSet != null) {
            ano = Integer.valueOf(sqlRowSet.getString("anoreferenciaperiodoletivo"));
            if (ano != null && ano > 0) {
                anoData = ano;
            }
            semestre = Integer.valueOf(sqlRowSet.getString("semestrereferenciaperiodoletivo"));
        }
        NumeroMatriculaVO numeroMatriculaVO = null;
        if (ano == null || ano == 0) {
            numeroMatriculaVO = new NumeroMatriculaVO(matriculaVO, anoData, semestre);
        } else {
            numeroMatriculaVO = new NumeroMatriculaVO(matriculaVO, ano, semestre);
        }
        NumeroMatricula numeroMatricula = new NumeroMatricula();
        numeroMatriculaVO = numeroMatricula.consultarPorTodosParametros(numeroMatriculaVO, mascaraPadrao);
        numeroMatricula.excluir(numeroMatriculaVO);
        numeroMatriculaVO.setMascara(mascaraPadrao);
        Integer incremental = numeroMatriculaVO.getIncremental(); 		
        String matricula = numeroMatriculaVO.gerarNumeroMatricula(numeroMatriculaVO.getAno());
//        String matricula = numeroMatriculaVO.gerarNumeroMatricula(anoData);
        if (matriculaVO.getCurso().getConfiguracaoAcademico().getCriarDigitoMascaraMatricula()) {
        	String formulaDigito = matriculaVO.getCurso().getConfiguracaoAcademico().getFormulaCriarDigitoMascaraMatricula();
        	numeroMatriculaVO.setMascara(formulaDigito);
    		numeroMatriculaVO.setIncremental(incremental);
    		ScriptEngineManager factory = new ScriptEngineManager();
    		// create a JavaScript engine
    		ScriptEngine engine = factory.getEngineByName("JavaScript");
    		Object result;
            String novaFormula = numeroMatriculaVO.gerarNumeroMatricula(numeroMatriculaVO.getAno());
            String formulaConc = novaFormula.substring(0, novaFormula.indexOf("/"));
            String formulaFinal = "";
            for (int i = 0; i < formulaConc.length(); i++) {
            	if (i == 0) {
            		formulaFinal += formulaConc.charAt(i);
            	} else if(i != (formulaConc.length()-1) && "0123456789".contains(String.valueOf(formulaConc.charAt(i)))){
            		formulaFinal += "+" + formulaConc.charAt(i);
                } else{
                	formulaFinal += formulaConc.charAt(i);
                }
			}
            formulaFinal += novaFormula.substring(novaFormula.indexOf("/"));
            result = engine.eval(formulaFinal);
            String resultado = result.toString();
            String resFinal = null;
            if(resultado.contains(".")){
            	resFinal = resultado.substring(0, resultado.indexOf("."));	
            }else{
            	resFinal = resultado;
            }
        	matricula += resFinal; 
        	numeroMatriculaVO.setMascara(mascaraPadrao);        	
        }		
		numeroMatricula.incluir(numeroMatriculaVO);		
        matriculaVO.setCodigoNumeroMatricula(numeroMatriculaVO.getCodigo());
        matriculaVO.setNumeroMatriculaIncrimental(numeroMatriculaVO.getIncremental());
        return matricula;
    }

    public  SqlRowSet consultarAnoSemestreGeracaoNumeroMatricula(Integer unidadeEnsinoCurso, Integer processoMatricula) {
        StringBuilder sb = new StringBuilder("select case when (anoreferenciaperiodoletivo is null or anoreferenciaperiodoletivo = '') then '0' else anoreferenciaperiodoletivo end as anoreferenciaperiodoletivo,");
        sb.append(" case when (semestrereferenciaperiodoletivo is null or semestrereferenciaperiodoletivo = '') then '0' else semestrereferenciaperiodoletivo end as semestrereferenciaperiodoletivo from processomatriculacalendario");
        sb.append(" inner join processomatriculaunidadeensino on processomatriculacalendario.processomatricula = processomatriculaunidadeensino.processomatricula ");
        sb.append(" inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino and unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno ");
        sb.append(" inner join periodoletivoativounidadeensinocurso on processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo");
        sb.append(" where unidadeensinocurso.codigo = ? and processomatriculacalendario.processomatricula = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), unidadeEnsinoCurso, processoMatricula);
        if (tabelaResultado.next()) {
            return tabelaResultado;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.NumeroMatriculaInterfaceFacade#consultarPorTodosParametros(negocio.comuns.academico.NumeroMatriculaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public NumeroMatriculaVO consultarPorTodosParametros(NumeroMatriculaVO numeroMatriculaVO, String mascaraPadrao) throws Exception {
        String sqlStr = "SELECT * from NumeroMatricula where ";
        String and = "";
        if (numeroMatriculaVO.getAno() != null && numeroMatriculaVO.getAno().intValue() != 0 && mascaraPadrao.contains("A")) {
            sqlStr += " ano=" + numeroMatriculaVO.getAno().intValue();
            and = " and ";
        }
        if (numeroMatriculaVO.getSemestre() != null && numeroMatriculaVO.getSemestre().intValue() != 0 && mascaraPadrao.contains("S")) {
            sqlStr += and + " semestre=" + numeroMatriculaVO.getSemestre().intValue();
            and = " and ";
        }
        if (numeroMatriculaVO.getUnidadeEnsino() != null && numeroMatriculaVO.getUnidadeEnsino().intValue() != 0 && mascaraPadrao.contains("U")) {
            sqlStr += and + " unidadeensino=" + numeroMatriculaVO.getUnidadeEnsino().intValue();
            and = " and ";
        }
        if (numeroMatriculaVO.getCurso() != null && numeroMatriculaVO.getCurso().intValue() != 0 && mascaraPadrao.contains("C")) {
            sqlStr += and + "curso=" + numeroMatriculaVO.getCurso().intValue();
            and = " and ";
        }
        if (mascaraPadrao.contains("B")) {
            sqlStr += and + "cursoabreviatura= '" + numeroMatriculaVO.getCursoAbreviatura() + "'";
            and = " and ";
        }
        if (mascaraPadrao.contains("F")) {
            sqlStr += and + "formaingresso= '" + numeroMatriculaVO.getFormaIngresso() + "'";
            and = " and ";
        }
        if (mascaraPadrao != null && !mascaraPadrao.equals("")) {
            sqlStr += and + "mascara= '" + mascaraPadrao + "'";
            and = " and ";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            numeroMatriculaVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
        }
        return numeroMatriculaVO;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.NumeroMatriculaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
     */
    public List<NumeroMatriculaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception {
        String sqlStr = "SELECT * FROM NumeroMatricula WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>NumeroMatriculaVO</code> resultantes da consulta.
     */
    public static List<NumeroMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<NumeroMatriculaVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            NumeroMatriculaVO obj = new NumeroMatriculaVO();
            obj = montarDados(tabelaResultado, nivelMontarDados);
            vetResultado.add(obj);
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>NumeroMatriculaVO</code>.
     *
     * @return O objeto da classe <code>NumeroMatriculaVO</code> com os dados
     *         devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static NumeroMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        NumeroMatriculaVO obj = new NumeroMatriculaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setAno(new Integer(dadosSQL.getInt("ano")));
        obj.setSemestre(new Integer(dadosSQL.getInt("semestre")));
        obj.setUnidadeEnsino(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setCurso(new Integer(dadosSQL.getInt("curso")));
        obj.setIncremental(new Integer(dadosSQL.getInt("incremental")));
        obj.setMascara(dadosSQL.getString("mascara"));
        obj.setFormaIngresso(dadosSQL.getString("formaingresso"));
        obj.setCursoAbreviatura(dadosSQL.getString("cursoabreviatura"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.NumeroMatriculaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public NumeroMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM NumeroMatricula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( NumeroMatricula ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return NumeroMatricula.idEntidade;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.NumeroMatriculaInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        NumeroMatricula.idEntidade = idEntidade;
    }
}
