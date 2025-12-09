/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import negocio.comuns.academico.DisciplinaEquivalenteVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.TurmaAgrupadaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TurmaAgrupadaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TurmaAgrupadaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see TurmaAgrupadaVO
 * @see ControleAcesso
 * @see Turma
 */
@Repository
@Scope("singleton")
@Lazy
public class TurmaAgrupada extends ControleAcesso implements TurmaAgrupadaInterfaceFacade {

    protected static String idEntidade;

    public TurmaAgrupada() throws Exception {
        super();
        setIdEntidade("Turma");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>TurmaAgrupadaVO</code>.
     */
    public TurmaAgrupadaVO novo() throws Exception {
        TurmaAgrupada.incluir(getIdEntidade());
        TurmaAgrupadaVO obj = new TurmaAgrupadaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>TurmaAgrupadaVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TurmaAgrupadaVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TurmaAgrupadaVO obj) throws Exception {
        try {
            TurmaAgrupadaVO.validarDados(obj);
            final String sql = "INSERT INTO TurmaAgrupada( turmaOrigem, turma ) VALUES ( ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getTurmaOrigem().intValue());
                    sqlInserir.setInt(2, obj.getTurma().getCodigo().intValue());
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
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>TurmaAgrupadaVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TurmaAgrupadaVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TurmaAgrupadaVO obj) throws Exception {
        try {
            TurmaAgrupadaVO.validarDados(obj);
            final String sql = "UPDATE TurmaAgrupada set turmaOrigem=?, turma=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getTurmaOrigem().intValue());
                    sqlAlterar.setInt(2, obj.getTurma().getCodigo().intValue());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>TurmaAgrupadaVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TurmaAgrupadaVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(TurmaAgrupadaVO obj) throws Exception {
        try {
            TurmaAgrupada.excluir(getIdEntidade());
            String sql = "DELETE FROM TurmaAgrupada WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * Responsável por realizar uma consulta de <code>TurmaAgrupadaVO</code>
     * através do valor do atributo <code>Integer valorConsulta</code>. Retorna
     * os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     *
     * @return List Contendo vários objetos da classe
     * <code>TurmaAgrupadaVO</code> resultantes da consulta.
     *
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List<TurmaAgrupadaVO> consultarPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TurmaAgrupada WHERE turma = " + valorConsulta.intValue() + " ORDER BY turma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /*
     * Responsável por realizar uma consulta de <code>TurmaAgrupadaVO</code>
     * através do valor do atributo <code>Integer turma, Integer
     * turmaOrigem</code>. Retorna os objetos, com início do valor do atributo
     * idêntico ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     *
     * @return TurmaAgrupadaVO.
     *
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public TurmaAgrupadaVO consultarPorCodigoTurmaCodigoTurmaOrigem(Integer turma, Integer turmaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TurmaAgrupada WHERE turma = " + turma.intValue() + " and turmaOrigem = " + turmaOrigem.intValue() + " ORDER BY turma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new TurmaAgrupadaVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    /*
     * Responsável por realizar uma consulta de <code>TurmaAgrupadaVO</code>
     * através do valor do atributo <code>Integer turma, Integer
     * turmaOrigem</code>. Retorna os objetos, com início do valor do atributo
     * idêntico ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     *
     * @return List Contendo vários objetos da classe
     * <code>TurmaAgrupadaVO</code> resultantes da consulta.
     *
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List<TurmaAgrupadaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TurmaAgrupada WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    private void processarDisciplinasEquivalentesTurma(List<TurmaDisciplinaVO> turmaDisciplinaVOs, List<TurmaDisciplinaVO> disciplinasComuns, List<TurmaDisciplinaVO> disciplinasEquivalencia, UsuarioVO usuario) {
    	for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
        	DisciplinaEquivalenteVO disciplinaEquivalente1 = null;
        	DisciplinaEquivalenteVO disciplinaEquivalente2 = null;
        	for (TurmaDisciplinaVO objVerificar : turmaDisciplinaVOs) {
        		if (!turmaDisciplinaVO.getDisciplina().getCodigo().equals(objVerificar.getDisciplina().getCodigo())) {
        			disciplinaEquivalente1 = getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentePorDisciplinaEEquivalente(turmaDisciplinaVO.getDisciplina().getCodigo(), objVerificar.getDisciplina().getCodigo(), usuario);
        			if (disciplinaEquivalente1 != null && !disciplinaEquivalente1.getEquivalente().getCodigo().equals(0)) {
        				turmaDisciplinaVO.getDisciplina().getDisciplinaEquivalenteVOs().add(disciplinaEquivalente1);
        				turmaDisciplinaVO.setDisciplinaEquivalenteTurmaAgrupada(disciplinaEquivalente1.getEquivalente());
        				turmaDisciplinaVO.setMensagemDisciplinaEquivalenteTurmaAgrupada("Disciplina incluída por causa da Equivalência com a disciplina: Código = "+disciplinaEquivalente1.getEquivalente().getCodigo()+", Nome = "+disciplinaEquivalente1.getEquivalente().getNome().toUpperCase()+".");
        				if (disciplinasEquivalencia.stream().anyMatch(td -> td.getDisciplina().getCodigo().equals(turmaDisciplinaVO.getDisciplina().getCodigo()))) {
        					disciplinasEquivalencia.removeIf(td -> td.getDisciplina().getCodigo().equals(turmaDisciplinaVO.getDisciplina().getCodigo()));
        				}
        				disciplinasEquivalencia.add(turmaDisciplinaVO);
        				break;
        			} else {
        				disciplinaEquivalente2 = getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentePorDisciplinaEEquivalente(objVerificar.getDisciplina().getCodigo(), turmaDisciplinaVO.getDisciplina().getCodigo(), usuario);
        			}
        			if (disciplinaEquivalente2 != null && !disciplinaEquivalente2.getEquivalente().getCodigo().equals(0) && disciplinasEquivalencia.stream().noneMatch(td -> td.getDisciplina().getCodigo().equals(objVerificar.getDisciplina().getCodigo()))) {
        				objVerificar.getDisciplina().getDisciplinaEquivalenteVOs().add(disciplinaEquivalente2);
        				objVerificar.setDisciplinaEquivalenteTurmaAgrupada(disciplinaEquivalente2.getEquivalente());
        				objVerificar.setMensagemDisciplinaEquivalenteTurmaAgrupada("Disciplina incluída por causa da Equivalência com a disciplina: Código = "+disciplinaEquivalente2.getEquivalente().getCodigo()+", Nome = "+disciplinaEquivalente2.getEquivalente().getNome().toUpperCase()+".");
        				disciplinasEquivalencia.add(objVerificar);
        				break;
        			}
        		}
        	}
        }
        for (TurmaDisciplinaVO turmaDisciplinaEquivalenciaVO : disciplinasEquivalencia) {
        	if (disciplinasComuns.stream().anyMatch(td -> td.getDisciplina().getCodigo().equals(turmaDisciplinaEquivalenciaVO.getDisciplina().getCodigo()))) {
        		disciplinasComuns.removeIf(td -> td.getDisciplina().getCodigo().equals(turmaDisciplinaEquivalenciaVO.getDisciplina().getCodigo()));
        	}
        	disciplinasComuns.add(turmaDisciplinaEquivalenciaVO);
        }
    }

    public void executarBuscaPorDisciplinasComunsEntreTurmasAgrupadas(TurmaVO turmaAgrupada, UsuarioVO usuario) throws Exception {
        if (!turmaAgrupada.getTurmaAgrupada()) {
            throw new Exception("Esta turma não é agrupada, portanto não é possível mapear disciplinas comuns entre turmas!");
        }
        List<TurmaDisciplinaVO> disciplinasComuns = new ArrayList<TurmaDisciplinaVO>(0);
        List<TurmaDisciplinaVO> disciplinasIncomuns = new ArrayList<>();
        List<TurmaDisciplinaVO> disciplinasEquivalencia = new ArrayList<>();
        for (TurmaAgrupadaVO turmaBuscarComuns : turmaAgrupada.getTurmaAgrupadaVOs()) {
        	getFacadeFactory().getTurmaFacade().carregarDados(turmaBuscarComuns.getTurma(), NivelMontarDados.TODOS, usuario);
            turmaBuscarComuns.getTurma().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);//Adicionado pois e necessario consultar novamente a turma pra validacoes de disciplina equivalente Pedro Andrade
        }
        Stream<TurmaDisciplinaVO> flatMap = turmaAgrupada.getTurmaAgrupadaVOs().stream().map(TurmaAgrupadaVO::getTurma).map(TurmaVO::getTurmaDisciplinaVOs).flatMap(Collection::stream);
	    Map<Integer, List<TurmaDisciplinaVO>> collect = flatMap.collect(Collectors.groupingBy(td -> td.getDisciplina().getCodigo()));
	    for (Map.Entry<Integer, List<TurmaDisciplinaVO>> map : collect.entrySet()) {
	      	if (map.getValue().size() > 1) {
	      		disciplinasComuns.add(map.getValue().get(0));
	      	} else if (map.getValue().size() == 1) {
	      		disciplinasIncomuns.add(map.getValue().get(0));
	      	}
	    }
	    List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<>();
	    turmaDisciplinaVOs.addAll(disciplinasComuns);
	    turmaDisciplinaVOs.addAll(disciplinasIncomuns);
	    processarDisciplinasEquivalentesTurma(turmaDisciplinaVOs, disciplinasComuns, disciplinasEquivalencia, usuario);
        getFacadeFactory().getTurmaFacade().executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(turmaAgrupada, disciplinasComuns, true, usuario);
    }
    
    public void executarVerificacaoDisciplinaEquivalente(TurmaVO turmaAgrupada, UsuarioVO usuarioVO) {
    	List listaTurmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarDisciplinaDiferenteTurmaAgrupada(turmaAgrupada.getCodigo(), turmaAgrupada.getTurmaDisciplinaVOs(), usuarioVO);
    	
    	
    }
    
    private void vincularDisciplinasComunsATurmaAgrupada(TurmaVO turmaAgrupada, List<TurmaDisciplinaVO> disciplinasComuns) {
        turmaAgrupada.setTurmaDisciplinaVOs(new ArrayList<TurmaDisciplinaVO>(0));
        for (TurmaDisciplinaVO obj : disciplinasComuns) {
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            obj.setTurma(turmaAgrupada.getCodigo());
        }
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>TurmaAgrupadaVO</code> resultantes da consulta.
     */
    public static List<TurmaAgrupadaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            TurmaAgrupadaVO obj = new TurmaAgrupadaVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>TurmaAgrupadaVO</code>.
     *
     * @return O objeto da classe <code>TurmaAgrupadaVO</code> com os dados
     *         devidamente montados.
     */
    public static TurmaAgrupadaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TurmaAgrupadaVO obj = new TurmaAgrupadaVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.setTurmaOrigem(new Integer(tabelaResultado.getInt("turmaOrigem")));
        obj.getTurma().setCodigo(new Integer(tabelaResultado.getInt("turma")));
        montarDadosTurma(obj, nivelMontarDados, usuario);
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public static void montarDadosTurma(TurmaAgrupadaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), NivelMontarDados.BASICO, usuario);
    }

    public void carregarDadosTurmaAgrupada(TurmaVO turmaVO, UsuarioVO usuario) throws Exception {
        List<TurmaAgrupadaVO> listaTurmaAgrupada = consultarTurmaAgrupadas(turmaVO.getCodigo(), false, 0, usuario);
        turmaVO.setTurmaAgrupadaVOs(listaTurmaAgrupada);
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAgrupadaInterfaceFacade#
     * excluirTurmaAgrupadas(java.lang.Integer)
     */
    public void excluirTurmaAgrupadas(Integer turma) throws Exception {
        try {
            String sql = "DELETE FROM TurmaAgrupada WHERE (turmaorigem = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{turma});
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAgrupadaInterfaceFacade#
     * alterarTurmaAgrupadas(negocio.comuns.academico.TurmaVO, java.util.List)
     */
    public void alterarTurmaAgrupadas(TurmaVO turma, List objetos) throws Exception {
        excluirTurmaAgrupadas(turma.getCodigo());
        incluirTurmaAgrupadas(turma, objetos);
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAgrupadaInterfaceFacade#
     * incluirTurmaAgrupadas(negocio.comuns.academico.TurmaVO, java.util.List)
     */
    public void incluirTurmaAgrupadas(TurmaVO turma, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            TurmaAgrupadaVO obj = (TurmaAgrupadaVO) e.next();
            obj.setTurmaOrigem(turma.getCodigo());
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>TurmaAgrupadaVO</code>
     * relacionados a um objeto da classe <code>academico.Turma</code>.
     *
     * @param turma
     *            Atributo de <code>academico.Turma</code> a ser utilizado para
     *            localizar os objetos da classe <code>TurmaAgrupadaVO</code>.
     * @return List Contendo todos os objetos da classe
     *         <code>TurmaAgrupadaVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta
     *                operação.
     */
    public static List<TurmaAgrupadaVO> consultarTurmaAgrupadas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TurmaAgrupada.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM TurmaAgrupada WHERE turmaorigem = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{turma});
        while (tabelaResultado.next()) {
            TurmaAgrupadaVO novoObj = new TurmaAgrupadaVO();
            novoObj = TurmaAgrupada.montarDados(tabelaResultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAgrupadaInterfaceFacade#
     * consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public TurmaAgrupadaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TurmaAgrupada WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( TurmaAgrupada ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return TurmaAgrupada.idEntidade;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.academico.TurmaAgrupadaInterfaceFacade#setIdEntidade
     * (java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        TurmaAgrupada.idEntidade = idEntidade;
    }

    public Boolean consultarPossuiTurmaAgrupada(Integer turmaOrigem, Integer turma) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT codigo FROM turmaAgrupada ");
        sb.append(" where turmaorigem = ");
        sb.append(turmaOrigem);
        sb.append(" and turma = ");
        sb.append(turma);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }
    
    public TurmaAgrupadaVO consultarPorCodigoTurmaOrigem(Integer turmaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT turmaagrupada.codigo,turmaagrupada.turmaorigem,turmaagrupada.turma FROM TurmaAgrupada WHERE turmaOrigem = " + turmaOrigem.intValue() + " LIMIT 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new TurmaAgrupadaVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }
}
