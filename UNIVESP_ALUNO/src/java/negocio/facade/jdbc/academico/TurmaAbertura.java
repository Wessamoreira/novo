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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaAberturaInterfaceFacade;

import negocio.comuns.academico.TurmaAgrupadaVO;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TurmaAberturaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TurmaAberturaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see TurmaAberturaVO
 * @see ControleAcesso
 * @see Turma
 */
@Repository
@Scope("singleton")
public class TurmaAbertura extends ControleAcesso implements TurmaAberturaInterfaceFacade {

    protected static String idEntidade;

    public TurmaAbertura() throws Exception {
        super();
        setIdEntidade("Turma");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>TurmaAberturaVO</code>.
     */
    public TurmaAberturaVO novo() throws Exception {
        TurmaAbertura.incluir(getIdEntidade());
        TurmaAberturaVO obj = new TurmaAberturaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>TurmaAberturaVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TurmaAberturaVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TurmaAberturaVO obj) throws Exception {
        try {
            TurmaAberturaVO.validarDados(obj);
            final String sql = "INSERT INTO TurmaAbertura( turma, situacao, data, usuario, dataAdiada ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getTurma().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setString(2, obj.getSituacao());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setInt(4, obj.getUsuario().getCodigo());
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataAdiada()));
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
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>TurmaAberturaVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TurmaAberturaVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TurmaAberturaVO obj) throws Exception {
        try {
            TurmaAberturaVO.validarDados(obj);
            final String sql = "UPDATE TurmaAbertura set turma=?, situacao=?, data=?, usuario=?, dataAdiada=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getTurma().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getSituacao());
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setInt(4, obj.getUsuario().getCodigo());
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataAdiada()));
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>TurmaAberturaVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TurmaAberturaVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(TurmaAberturaVO obj) throws Exception {
        try {
            TurmaAbertura.excluir(getIdEntidade());
            String sql = "DELETE FROM TurmaAbertura WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacao(Integer codigo, String situacao) throws Exception {
        try {            
            String sql = "UPDATE TurmaAbertura set situacao = ? WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{situacao, codigo});
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * Responsável por realizar uma consulta de <code>TurmaAberturaVO</code>
     * através do valor do atributo <code>Integer valorConsulta</code>. Retorna
     * os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     *
     * @return List Contendo vários objetos da classe
     * <code>TurmaAberturaVO</code> resultantes da consulta.
     *
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List<TurmaAberturaVO> consultarPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TurmaAbertura WHERE turma = " + valorConsulta.intValue() + " ORDER BY turma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public TurmaAberturaVO consultarUltimaTurmaAberturaPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM TurmaAbertura WHERE turma = " + valorConsulta.intValue() + " ORDER BY codigo desc limit 1";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	if(tabelaResultado.next()){
    		return montarDados(tabelaResultado, nivelMontarDados, usuario);
    	}
    	return null;
    }

    /*
     * Responsável por realizar uma consulta de <code>TurmaAberturaVO</code>
     * através do valor do atributo <code>Integer turma, Integer
     * turmaOrigem</code>. Retorna os objetos, com início do valor do atributo
     * idêntico ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     *
     * @return TurmaAberturaVO.
     *
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
//    public TurmaAberturaVO consultarPorCodigoTurmaCodigoTurmaOrigem(Integer turma, Integer turmaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        consultar(getIdEntidade(), controlarAcesso, usuario);
//        String sqlStr = "SELECT * FROM TurmaAbertura WHERE turma = " + turma.intValue() + " and turmaOrigem = " + turmaOrigem.intValue() + " ORDER BY turma";
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//        if (!tabelaResultado.next()) {
//            return new TurmaAberturaVO();
//        }
//        return montarDados(tabelaResultado, nivelMontarDados, usuario);
//    }

    /*
     * Responsável por realizar uma consulta de <code>TurmaAberturaVO</code>
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
     * <code>TurmaAberturaVO</code> resultantes da consulta.
     *
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List<TurmaAberturaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TurmaAbertura WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
    public List<Integer> consultarPorSituacaoDataJob(String situacao, Date data) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT codigo FROM TurmaAbertura ");
        sqlStr.append("WHERE situacao = '").append(situacao).append("' AND data < '").append(Uteis.getDataJDBC(data)).append("' ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            Integer codigoTurmaAbertura = tabelaResultado.getInt("codigo");            
            vetResultado.add(codigoTurmaAbertura);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>TurmaAberturaVO</code> resultantes da consulta.
     */
    public static List<TurmaAberturaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            TurmaAberturaVO obj = new TurmaAberturaVO();
//            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>TurmaAberturaVO</code>.
     *
     * @return O objeto da classe <code>TurmaAberturaVO</code> com os dados
     *         devidamente montados.
     */
    public  TurmaAberturaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TurmaAberturaVO obj = new TurmaAberturaVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.getTurma().setCodigo(new Integer(tabelaResultado.getInt("turma")));
        obj.setSituacao(tabelaResultado.getString("situacao"));
        obj.setData(tabelaResultado.getDate("data"));
        obj.setDataAdiada(tabelaResultado.getDate("dataAdiada"));
        obj.getUsuario().setCodigo(tabelaResultado.getInt("usuario"));
        montarDadosUsuario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public  void montarDadosUsuario(TurmaAberturaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuario().getCodigo().intValue() == 0) {
            obj.setUsuario(new UsuarioVO());
            return;
        }
        obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), nivelMontarDados, usuario));
    }

    public void carregarDadosTurmaAbertura(TurmaVO turmaVO, UsuarioVO usuario) throws Exception {
        List<TurmaAberturaVO> listaTurmaAbertura = consultarTurmaAberturas(turmaVO.getCodigo(), false, 0, usuario);
//        turmaVO.setTurmaAberturaVOs(listaTurmaAbertura);
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAberturaInterfaceFacade#
     * excluirTurmaAberturas(java.lang.Integer)
     */
    public void excluirTurmaAberturas(Integer turma) throws Exception {
        try {
            String sql = "DELETE FROM TurmaAbertura WHERE (turma = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{turma});
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAberturaInterfaceFacade#
     * alterarTurmaAberturas(negocio.comuns.academico.TurmaVO, java.util.List)
     */
    public void alterarTurmaAberturas(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception {
        excluirTurmaAberturas(turma.getCodigo());
        incluirTurmaAberturas(turma, objetos, usuario);
    }

    public void incluirTurmaAberturas(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception {
//        if (objetos.isEmpty()) {
//            objetos.add(inicializarDadosTurmaAbertura(turma.getCodigo(), usuario));
//        }
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            TurmaAberturaVO obj = (TurmaAberturaVO) e.next();
            obj.setTurma(turma);
            incluir(obj);
        }
    }

    public TurmaAberturaVO inicializarDadosTurmaAbertura(Integer turma, UsuarioVO usuario) {
        TurmaAberturaVO obj = new TurmaAberturaVO();
        obj.setData(new Date());
        obj.getTurma().setCodigo(turma);
        obj.setSituacao("AC");
        obj.setUsuario(usuario);
        return obj;
    }

    /**
     * Operação responsável por consultar todos os <code>TurmaAberturaVO</code>
     * relacionados a um objeto da classe <code>academico.Turma</code>.
     *
     * @param turma
     *            Atributo de <code>academico.Turma</code> a ser utilizado para
     *            localizar os objetos da classe <code>TurmaAberturaVO</code>.
     * @return List Contendo todos os objetos da classe
     *         <code>TurmaAberturaVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta
     *                operação.
     */
    public  List<TurmaAberturaVO> consultarTurmaAberturas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM TurmaAbertura WHERE turma = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{turma});
        while (tabelaResultado.next()) {
            TurmaAberturaVO novoObj = new TurmaAberturaVO();
            novoObj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    public void carregarDados(TurmaVO obj, UsuarioVO usuario) throws Exception {
//        obj.setTurmaAberturaVOs(this.consultarPorTurma(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
    }

    /*
     * (non-Javadoc)
     *
     * @seenegocio.facade.jdbc.academico.TurmaAberturaInterfaceFacade#
     * consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public TurmaAberturaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TurmaAbertura WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( TurmaAbertura ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataAberturaTurmaBaseAgrupada(List<TurmaAgrupadaVO> listaTurmaAgrupada, TurmaAberturaVO turmaAberturaVO, UsuarioVO usuario) throws Exception {
    	
    	if(!Uteis.isAtributoPreenchido(listaTurmaAgrupada)) {
    		throw new ConsistirException("Não existe turma para ser alterada.");
    	}
    	
    	if(turmaAberturaVO.getSituacao().equals("")) {
    		throw new ConsistirException("Campo situação deve ser selecionado.");
    	}
    	
    	if(turmaAberturaVO.getAcaoTurmaBase().equals("")) {
    		throw new ConsistirException("Campo Ação Turma Base deve ser selecionada.");
    	}
    	for(TurmaAgrupadaVO turmaAgrupadaVO : listaTurmaAgrupada) {
    		if(turmaAberturaVO.getAcaoTurmaBase().equals("EI")) {
    			excluirTurmaAberturas(turmaAgrupadaVO.getTurma().getCodigo());
    		}
    		TurmaAberturaVO tuAberturaVO = montarTurmaAbertura(turmaAgrupadaVO.getTurma(), turmaAberturaVO, usuario);
    		incluir(tuAberturaVO);
    	}
    }
    
    private TurmaAberturaVO montarTurmaAbertura(TurmaVO turma, TurmaAberturaVO aberturaVO, UsuarioVO usuarioVO) {
    	TurmaAberturaVO turmaAbertura = new TurmaAberturaVO();
    	turmaAbertura.setTurma(turma);
    	turmaAbertura.setUsuario(usuarioVO);
    	turmaAbertura.setData(aberturaVO.getData());
    	turmaAbertura.setSituacao(aberturaVO.getSituacao());
    	return turmaAbertura;
    }
    
    
    public  List<TurmaAberturaVO> consultarTurmaBaseAberturas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List<TurmaAberturaVO> objetos = new ArrayList<TurmaAberturaVO>();
        String sql = "SELECT turma.codigo as turma, turma.identificadorturma, TurmaAbertura.* FROM TurmaAbertura inner join turma on TurmaAbertura.turma = turma.codigo where turma = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{turma});
        while (tabelaResultado.next()) {
            TurmaAberturaVO novoObj = new TurmaAberturaVO();
            novoObj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
            novoObj.getTurma().setCodigo(new Integer(tabelaResultado.getInt("turma")));
            novoObj.getTurma().setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
            novoObj.setSituacao(tabelaResultado.getString("situacao"));
            novoObj.setData(tabelaResultado.getDate("data"));
            novoObj.setDataAdiada(tabelaResultado.getDate("dataAdiada"));
            novoObj.setNovoObj(Boolean.FALSE);
            objetos.add(novoObj);
        }
        return objetos;
    }
    
    public void carregarDadosTurmaAberturaTurmaBase(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception {
    	for(TurmaAgrupadaVO turmaAgrupadaVO : turmaVO.getTurmaAgrupadaVOs()) {
    		turmaVO.getListaSituacaoAberturaTurmaBase().addAll(consultarTurmaBaseAberturas(turmaAgrupadaVO.getTurma().getCodigo(), false, 0, usuarioLogado));
    	}
    
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return TurmaAbertura.idEntidade;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.academico.TurmaAberturaInterfaceFacade#setIdEntidade
     * (java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        TurmaAbertura.idEntidade = idEntidade;
    }
}
