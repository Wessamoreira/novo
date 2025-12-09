package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

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

import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoUsoProcessoMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ProcessoMatriculaInterfaceFacade;
import webservice.servicos.ProcessoMatriculaRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcessoMatriculaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ProcessoMatriculaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProcessoMatriculaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ProcessoMatricula extends ControleAcesso implements ProcessoMatriculaInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

    public ProcessoMatricula() throws Exception {
        super();
        setIdEntidade("ProcessoMatricula");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProcessoMatriculaVO</code>.
     */
    public ProcessoMatriculaVO novo() throws Exception {
        ProcessoMatricula.incluir(getIdEntidade());
        ProcessoMatriculaVO obj = new ProcessoMatriculaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcessoMatriculaVO</code>. Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProcessoMatriculaVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProcessoMatriculaVO.validarDados(obj);
            consultarUnicidadeDescricao(obj.getDescricao());
            ProcessoMatricula.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO ProcessoMatricula( descricao, data, dataInicio, dataFinal, validoPelaInternet, exigeConfirmacaoPresencial, "
                    + "situacao, nivelProcessoMatricula, apresentarProcessoVisaoAluno, dataInicioMatriculaOnline, dataFimMatriculaOnline, " +
                    "  mensagemApresentarVisaoAluno, permiteIncluirExcluirDisciplinaVisaoAluno, qtdeMininaDisciplinaCursar, termoAceite, apresentarTermoAceite, textoPadraoContratoRenovacaoOnline, bloquearAlunosPossuemConvenioRenovacaoOnline, mensagemConfirmacaoRenovacaoAluno, tipoaluno, tipoUsoProcessoMatriculaEnum ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlInserir.setBoolean(5, obj.isValidoPelaInternet().booleanValue());
                    sqlInserir.setBoolean(6, obj.isExigeConfirmacaoPresencial().booleanValue());
                    sqlInserir.setString(7, obj.getSituacao());
                    sqlInserir.setString(8, obj.getNivelProcessoMatricula());
                    sqlInserir.setBoolean(9, obj.getApresentarProcessoVisaoAluno());
                    sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataInicioMatriculaOnline()));
                    sqlInserir.setDate(11, Uteis.getDataJDBC(obj.getDataFimMatriculaOnline()));
                    sqlInserir.setString(12, obj.getMensagemApresentarVisaoAluno());
                    sqlInserir.setBoolean(13, obj.getPermiteIncluirExcluirDisciplinaVisaoAluno());
                    sqlInserir.setInt(14, obj.getQtdeMininaDisciplinaCursar());
                    sqlInserir.setString(15, obj.getTermoAceite());
                    sqlInserir.setBoolean(16, obj.getApresentarTermoAceite());
                    if (obj.getTextoPadraoContratoRenovacaoOnline().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(17, obj.getTextoPadraoContratoRenovacaoOnline().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(17, 0);
                    }
                    sqlInserir.setBoolean(18, obj.getBloquearAlunosPossuemConvenioRenovacaoOnline());
                    sqlInserir.setString(19, obj.getMensagemConfirmacaoRenovacaoAluno());
                    sqlInserir.setString(20, obj.getTipoAluno().name());
                    sqlInserir.setString(21, obj.getTipoUsoProcessoMatriculaEnum().name());
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
            getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().persistir(obj, usuario);            
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getProcessoMatriculaCalendarioFacade().incluirProcessoMatriculaCalendarios(obj, obj.getProcessoMatriculaCalendarioVOs(), usuario);
        } catch (Exception e) {
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcessoMatriculaVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a
     * ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProcessoMatriculaVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProcessoMatriculaVO.validarDados(obj);
            ProcessoMatricula.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE ProcessoMatricula set descricao=?, data=?, dataInicio=?, dataFinal=?, validoPelaInternet=?, "
                    + "exigeConfirmacaoPresencial=?, situacao=?, nivelProcessoMatricula=?, apresentarProcessoVisaoAluno=?, "
                    + "dataInicioMatriculaOnline=?, dataFimMatriculaOnline=?, mensagemApresentarVisaoAluno=?, permiteIncluirExcluirDisciplinaVisaoAluno=?, " 
                    + "qtdeMininaDisciplinaCursar=?, termoAceite=?, apresentarTermoAceite=?, textoPadraoContratoRenovacaoOnline=?, bloquearAlunosPossuemConvenioRenovacaoOnline=?, mensagemConfirmacaoRenovacaoAluno=?, tipoaluno=?, tipoUsoProcessoMatriculaEnum=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlAlterar.setBoolean(5, obj.isValidoPelaInternet().booleanValue());
                    sqlAlterar.setBoolean(6, obj.isExigeConfirmacaoPresencial().booleanValue());
                    
                    sqlAlterar.setString(7, obj.getSituacao());
                    sqlAlterar.setString(8, obj.getNivelProcessoMatricula());
                    sqlAlterar.setBoolean(9, obj.getApresentarProcessoVisaoAluno());
                    sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataInicioMatriculaOnline()));
                    sqlAlterar.setDate(11, Uteis.getDataJDBC(obj.getDataFimMatriculaOnline()));
                    sqlAlterar.setString(12,obj.getMensagemApresentarVisaoAluno());
                    sqlAlterar.setBoolean(13, obj.getPermiteIncluirExcluirDisciplinaVisaoAluno());
                    sqlAlterar.setInt(14, obj.getQtdeMininaDisciplinaCursar());
                    sqlAlterar.setString(15, obj.getTermoAceite());
                    sqlAlterar.setBoolean(16, obj.getApresentarTermoAceite());
                    if (obj.getTextoPadraoContratoRenovacaoOnline().getCodigo().intValue() != 0) {
                    	sqlAlterar.setInt(17, obj.getTextoPadraoContratoRenovacaoOnline().getCodigo().intValue());
                    } else {
                    	
                    	sqlAlterar.setNull(17, 0);
                    }
                    sqlAlterar.setBoolean(18, obj.getBloquearAlunosPossuemConvenioRenovacaoOnline());
                    sqlAlterar.setString(19,obj.getMensagemConfirmacaoRenovacaoAluno());
                    sqlAlterar.setString(20, obj.getTipoAluno().name());
                    sqlAlterar.setString(21, obj.getTipoUsoProcessoMatriculaEnum().name());
                    sqlAlterar.setInt(22, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().persistir(obj, usuario);
            getFacadeFactory().getProcessoMatriculaCalendarioFacade().alterarProcessoMatriculaCalendarios(obj, obj.getProcessoMatriculaCalendarioVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProcessoMatriculaVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente
     * verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProcessoMatriculaVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProcessoMatricula.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM ProcessoMatricula WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getProcessoMatriculaCalendarioFacade().excluirProcessoMatriculaCalendarios(obj, usuario);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcessoMatricula</code> através do valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ProcessoMatriculaVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ProcessoMatriculaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProcessoMatricula WHERE ";
        sqlStr += " exists (select ProcessoMatriculaunidadeEnsino.unidadeEnsino from ProcessoMatriculaunidadeEnsino inner join unidadeensino on unidadeensino.codigo = ProcessoMatriculaunidadeEnsino.unidadeensino where ProcessoMatriculaunidadeEnsino.ProcessoMatricula = ProcessoMatricula.codigo and   sem_acentos(unidadeensino.nome) ilike sem_acentos(?)  ";
    	if (unidadeEnsino.intValue() != 0) {
    		sqlStr += " and   unidadeEnsino.codigo = " + unidadeEnsino.intValue() + " ) ";
    	}
    	sqlStr += " ) ";
    	sqlStr += " ORDER BY descricao";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%"+valorConsulta+"%");
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<ProcessoMatriculaVO> consultarPorNomeUnidadeEnsino_Ativo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "";
        SqlRowSet tabelaResultado = null;
        if (unidadeEnsino.intValue() != 0) {        	
        	sqlStr = "SELECT ProcessoMatricula.* FROM ProcessoMatricula inner join ProcessoMatriculaUnidadeEnsino on ProcessoMatricula.codigo = ProcessoMatriculaUnidadeEnsino.ProcessoMatricula  WHERE (ProcessoMatricula.situacao = 'AT' or ProcessoMatricula.situacao = 'PR') and ProcessoMatriculaUnidadeEnsino.unidadeEnsino = "
        			+ unidadeEnsino.intValue() + " ORDER BY ProcessoMatricula.descricao ";       
        	tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        }else {
        	sqlStr = "SELECT ProcessoMatricula.* FROM ProcessoMatricula  WHERE (ProcessoMatricula.situacao = 'AT' or ProcessoMatricula.situacao = 'PR') ORDER BY ProcessoMatricula.descricao";
        	tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        }        
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public void consultarUnicidadeDescricao(String valorConsulta) throws Exception {
        String sqlStr = "SELECT codigo FROM processoMatricula WHERE sem_acentos( descricao ) ilike(sem_acentos(?))";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
        if (tabelaResultado.next()) {
            throw new ConsistirException("Descrição já cadastrada (Processo Matrícula).");
        }
    }

   
    /**
     * Responsável por realizar uma consulta de <code>ProcessoMatricula</code> através do valor do atributo <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por
     * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProcessoMatriculaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ProcessoMatriculaVO> consultarSomentePelaDataFimProcessoMatricula(Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct ProcessoMatricula.* FROM ProcessoMatricula ";
        sqlStr += " inner join ProcessoMatriculaunidadeensino on ProcessoMatriculaunidadeensino.ProcessoMatricula = ProcessoMatricula.codigo ";
        sqlStr += " inner join unidadeensino on unidadeensino.codigo = ProcessoMatriculaunidadeensino.unidadeensino ";
        sqlStr += " WHERE unidadeensino.apresentartelaprocessoseletivo = true and (ProcessoMatricula.dataFinal >= '" + Uteis.getDataJDBC(prmIni) + "')  ORDER BY ProcessoMatricula.datainicio";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcessoMatricula</code> através do valor do atributo <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico
     * ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProcessoMatriculaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ProcessoMatriculaVO> consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        	String sqlStr = "SELECT * FROM ProcessoMatricula WHERE sem_acentos(descricao) ilike sem_acentos(?) ";
        	if (unidadeEnsino.intValue() != 0) {
        		sqlStr += " and exists (select ProcessoMatriculaunidadeEnsino.unidadeEnsino from ProcessoMatriculaunidadeEnsino where ProcessoMatriculaunidadeEnsino.ProcessoMatricula = ProcessoMatricula.codigo and   ProcessoMatriculaunidadeEnsino.unidadeEnsino = " + unidadeEnsino.intValue() + " ) ";
        	}
        	sqlStr += " ORDER BY descricao";
        	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%"+valorConsulta+"%");
        	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcessoMatricula</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao
     * parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProcessoMatriculaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ProcessoMatriculaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProcessoMatricula WHERE codigo >= ? ";
    	if (unidadeEnsino.intValue() != 0) {
    		sqlStr += " and exists (select ProcessoMatriculaunidadeEnsino.unidadeEnsino from ProcessoMatriculaunidadeEnsino where ProcessoMatriculaunidadeEnsino.ProcessoMatricula = ProcessoMatricula.codigo and   ProcessoMatriculaunidadeEnsino.unidadeEnsino = " + unidadeEnsino.intValue() + " ) ";
    	}
    	sqlStr += " ORDER BY descricao";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>ProcessoMatriculaVO</code> resultantes da consulta.
     */
    public static List<ProcessoMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ProcessoMatriculaVO> vetResultado = new ArrayList<ProcessoMatriculaVO>(0);
        while (tabelaResultado.next()) {
            ProcessoMatriculaVO obj = new ProcessoMatriculaVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ProcessoMatriculaVO</code>.
     *
     * @return O objeto da classe <code>ProcessoMatriculaVO</code> com os dados devidamente montados.
     */
    public static ProcessoMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcessoMatriculaVO obj = new ProcessoMatriculaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setData(dadosSQL.getDate("data"));        
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setNivelProcessoMatricula(dadosSQL.getString("nivelProcessoMatricula"));
        if (dadosSQL.getString("tipoUsoProcessoMatriculaEnum") != null) {
        	obj.setTipoUsoProcessoMatriculaEnum(TipoUsoProcessoMatriculaEnum.valueOf(dadosSQL.getString("tipoUsoProcessoMatriculaEnum")));
        }
        if (dadosSQL.getString("tipoAluno") != null) {
        	obj.setTipoAluno(TipoAlunoCalendarioMatriculaEnum.valueOf(dadosSQL.getString("tipoAluno")));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {           
            return obj;
        }
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
        obj.setValidoPelaInternet(new Boolean(dadosSQL.getBoolean("validoPelaInternet")));
        obj.setApresentarProcessoVisaoAluno((dadosSQL.getBoolean("apresentarProcessoVisaoAluno")));
        obj.setPermiteIncluirExcluirDisciplinaVisaoAluno((dadosSQL.getBoolean("permiteIncluirExcluirDisciplinaVisaoAluno")));
        obj.setMensagemApresentarVisaoAluno(dadosSQL.getString("mensagemApresentarVisaoAluno"));
        obj.setMensagemConfirmacaoRenovacaoAluno(dadosSQL.getString("mensagemConfirmacaoRenovacaoAluno"));
        obj.setDataInicioMatriculaOnline(dadosSQL.getDate("dataInicioMatriculaOnline"));
        obj.setDataFimMatriculaOnline(dadosSQL.getDate("dataFimMatriculaOnline"));
        obj.setExigeConfirmacaoPresencial(new Boolean(dadosSQL.getBoolean("exigeConfirmacaoPresencial")));
        obj.setQtdeMininaDisciplinaCursar(dadosSQL.getInt("qtdeMininaDisciplinaCursar"));
        obj.setTermoAceite(dadosSQL.getString("termoAceite"));
        obj.getTextoPadraoContratoRenovacaoOnline().setCodigo(dadosSQL.getInt("textoPadraoContratoRenovacaoOnline"));
        obj.setApresentarTermoAceite(dadosSQL.getBoolean("apresentarTermoAceite"));
        obj.setBloquearAlunosPossuemConvenioRenovacaoOnline(dadosSQL.getBoolean("bloquearAlunosPossuemConvenioRenovacaoOnline"));

        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {            
            return obj;
        }
        obj.setProcessoMatriculaUnidadeEnsinoVOs(getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().consultarPorProcessoMatricula(obj.getCodigo()));
        obj.setProcessoMatriculaCalendarioVOs(ProcessoMatriculaCalendario.consultarProcessoMatriculaCalendarios(obj.getCodigo(), false, nivelMontarDados, usuario));
       
        
        return obj;
    }   

    /**
     * Operação responsável por localizar um objeto da classe <code>ProcessoMatriculaVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProcessoMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM ProcessoMatricula WHERE codigo = ?";
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProcessoMatricula.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ProcessoMatricula.idEntidade = idEntidade;
    }

    public void carregarDados(ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((ProcessoMatriculaVO) obj, NivelMontarDados.TODOS, usuario);
    }

    public void carregarDados(ProcessoMatriculaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if (obj.getIsDadosBasicosDevemSerCarregados(nivelMontarDados)) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((ProcessoMatriculaVO) obj, resultado);
        }
        if (obj.getIsDadosCompletosDevemSerCarregados(nivelMontarDados)) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((ProcessoMatriculaVO) obj, resultado);
        }
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT Distinct ProcessoMatricula.*, ");
        sql.append(" textoPadrao.descricao AS \"textoPadrao.descricao\", textoPadrao.texto AS \"textoPadrao.texto\" ");
        sql.append(" FROM ProcessoMatricula");        
        sql.append(" LEFT JOIN textoPadrao ON textoPadrao.codigo = processoMatricula.textoPadraoContratoRenovacaoOnline ");
        sql.append(" ");
        return sql;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ProcessoMatricula.*, ProcessoMatriculaCalendario.*, ");
        sql.append(" curso.codigo as \"curso.codigo\"  , Curso.nome as \"Curso.nome\"  ,curso.periodicidade as \"Curso.periodicidade\", Turno.Nome as \"Turno.nome\" , Turno.Codigo as \"Turno.codigo\", ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.codigo,PeriodoLetivoAtivoUnidadeEnsinoCurso.datainicioperiodoletivo, ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.datafimperiodoletivo ,PeriodoLetivoAtivoUnidadeEnsinoCurso.tipoperiodoletivo, ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.semestrereferenciaperiodoletivo , PeriodoLetivoAtivoUnidadeEnsinoCurso.anoreferenciaperiodoletivo, ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.dataInicioPeriodoLetivoPrimeiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.dataFimPeriodoLetivoPrimeiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.dataInicioPeriodoLetivoSegundoBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.dataFimPeriodoLetivoSegundoBimestre, ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.dataInicioPeriodoLetivoTerceiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.dataFimPeriodoLetivoTerceiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.dataInicioPeriodoLetivoQuartoBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.dataFimPeriodoLetivoQuartoBimestre, ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeDiaLetivoPrimeiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeSemanaLetivaPrimeiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeDiaLetivoSegundoBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeSemanaLetivaSegundoBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeDiaLetivoTerceiroBimestre, ");
        sql.append(" PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeSemanaLetivaTerceiroBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeDiaLetivoQuartoBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.qtdeSemanaLetivaQuartoBimestre, PeriodoLetivoAtivoUnidadeEnsinoCurso.totalDiaLetivoAno, PeriodoLetivoAtivoUnidadeEnsinoCurso.totalSemanaLetivaAno, ");
        sql.append(" textoPadrao.descricao AS \"textoPadrao.descricao\", textoPadrao.texto AS \"textoPadrao.texto\", ");
        sql.append(" EXISTS (SELECT FROM matriculaperiodo ");
        sql.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.codigo = matriculaperiodo.unidadeensinocurso ");
        sql.append(" WHERE matriculaperiodo.processomatricula = processomatricula.codigo ");
        sql.append(" AND unidadeensinocurso.curso = curso.codigo ");
        sql.append(" AND unidadeensinocurso.turno = turno.codigo ) possuimatriculavinculada ");
        sql.append(" FROM ProcessoMatricula ");        
        sql.append(" LEFT JOIN ProcessoMatriculaCalendario ON ProcessoMatriculaCalendario.ProcessoMatricula = ProcessoMatricula.codigo");        
        sql.append(" LEFT JOIN Curso ON ProcessoMatriculaCalendario.Curso = Curso.Codigo  ");
        sql.append(" LEFT JOIN Turno ON ProcessoMatriculaCalendario.Turno = Turno.Codigo ");
        sql.append(" LEFT JOIN PeriodoLetivoAtivoUnidadeEnsinoCurso ON ProcessoMatriculaCalendario.periodoletivoativounidadeensinocurso = PeriodoLetivoAtivoUnidadeEnsinoCurso.codigo ");
        sql.append(" LEFT JOIN textoPadrao ON textoPadrao.codigo = processoMatricula.textoPadraoContratoRenovacaoOnline ");
        sql.append("");
        return sql;
    }

    public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE ProcessoMatricula.codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    public SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sql = getSQLPadraoConsultaCompleta();
        sql.append(" WHERE ProcessoMatricula.codigo = ? order by Curso.nome, turno.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private void montarDadosBasico(ProcessoMatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados Processo Matricula
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setData(dadosSQL.getTimestamp("data"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setMensagemApresentarVisaoAluno(dadosSQL.getString("mensagemApresentarVisaoAluno"));
        obj.setMensagemConfirmacaoRenovacaoAluno(dadosSQL.getString("mensagemConfirmacaoRenovacaoAluno"));
        obj.setApresentarProcessoVisaoAluno(dadosSQL.getBoolean("apresentarProcessoVisaoAluno"));
        obj.setDataInicioMatriculaOnline(dadosSQL.getDate("dataInicioMatriculaOnline"));
        obj.setDataFimMatriculaOnline(dadosSQL.getDate("dataFimMatriculaOnline"));
        obj.setPermiteIncluirExcluirDisciplinaVisaoAluno((dadosSQL.getBoolean("permiteIncluirExcluirDisciplinaVisaoAluno")));
        obj.setQtdeMininaDisciplinaCursar(dadosSQL.getInt("qtdeMininaDisciplinaCursar"));
        obj.setApresentarTermoAceite(dadosSQL.getBoolean("apresentarTermoAceite"));
        obj.getTextoPadraoContratoRenovacaoOnline().setCodigo(dadosSQL.getInt("textoPadraoContratoRenovacaoOnline"));
        obj.getTextoPadraoContratoRenovacaoOnline().setDescricao(dadosSQL.getString("textoPadrao.descricao"));
        obj.getTextoPadraoContratoRenovacaoOnline().setTexto(dadosSQL.getString("textoPadrao.texto"));
        obj.setBloquearAlunosPossuemConvenioRenovacaoOnline(dadosSQL.getBoolean("bloquearAlunosPossuemConvenioRenovacaoOnline"));
        obj.setTermoAceite(dadosSQL.getString("termoAceite"));
    }

    private void montarDadosCompleto(ProcessoMatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setData(dadosSQL.getTimestamp("data"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setNivelProcessoMatricula(dadosSQL.getString("nivelProcessoMatricula"));
        obj.setExigeConfirmacaoPresencial(dadosSQL.getBoolean("exigeconfirmacaopresencial"));
        obj.setApresentarProcessoVisaoAluno(dadosSQL.getBoolean("apresentarProcessoVisaoAluno"));
        obj.setDataInicioMatriculaOnline(dadosSQL.getDate("dataInicioMatriculaOnline"));
        obj.setDataFimMatriculaOnline(dadosSQL.getDate("dataFimMatriculaOnline"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
        obj.setValidoPelaInternet(new Boolean(dadosSQL.getBoolean("validoPelaInternet")));
        obj.setMensagemApresentarVisaoAluno(dadosSQL.getString("mensagemApresentarVisaoAluno"));
        obj.setMensagemConfirmacaoRenovacaoAluno(dadosSQL.getString("mensagemConfirmacaoRenovacaoAluno"));
        obj.setPermiteIncluirExcluirDisciplinaVisaoAluno((dadosSQL.getBoolean("permiteIncluirExcluirDisciplinaVisaoAluno")));
        obj.setQtdeMininaDisciplinaCursar(dadosSQL.getInt("qtdeMininaDisciplinaCursar"));
        obj.setApresentarTermoAceite(dadosSQL.getBoolean("apresentarTermoAceite"));
        obj.setTermoAceite(dadosSQL.getString("termoAceite"));
        obj.getTextoPadraoContratoRenovacaoOnline().setCodigo(dadosSQL.getInt("textoPadraoContratoRenovacaoOnline"));
        obj.getTextoPadraoContratoRenovacaoOnline().setDescricao(dadosSQL.getString("textoPadrao.descricao"));
        obj.getTextoPadraoContratoRenovacaoOnline().setTexto(dadosSQL.getString("textoPadrao.texto"));
        obj.setBloquearAlunosPossuemConvenioRenovacaoOnline(dadosSQL.getBoolean("bloquearAlunosPossuemConvenioRenovacaoOnline"));
        obj.setTipoAluno(TipoAlunoCalendarioMatriculaEnum.valueOf(dadosSQL.getString("tipoAluno")));
        obj.setNivelMontarDados(NivelMontarDados.TODOS);
        obj.setProcessoMatriculaUnidadeEnsinoVOs(getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().consultarPorProcessoMatricula(obj.getCodigo()));        

        ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO = null;
        do {
            processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
            processoMatriculaCalendarioVO.setProcessoMatricula(obj.getCodigo());
            processoMatriculaCalendarioVO.setDataFinalInclusaoDisciplina(dadosSQL.getDate("datafinalinclusaodisciplina"));
            processoMatriculaCalendarioVO.setDataFinalMatForaPrazo(dadosSQL.getDate("datafinalmatforaprazo"));
            processoMatriculaCalendarioVO.setDataFinalMatricula(dadosSQL.getDate("datafinalmatricula"));
            processoMatriculaCalendarioVO.setDataInicioInclusaoDisciplina(dadosSQL.getDate("datainicioinclusaodisciplina"));
            processoMatriculaCalendarioVO.setDataInicioMatForaPrazo(dadosSQL.getDate("datainiciomatforaprazo"));
            processoMatriculaCalendarioVO.setDataInicioMatricula(dadosSQL.getDate("datainiciomatricula"));
            processoMatriculaCalendarioVO.setUtilizaControleGeracaoParcelaTurma(dadosSQL.getBoolean("utilizaControleGeracaoParcelaTurma"));
            
            if (processoMatriculaCalendarioVO.getUtilizaControleGeracaoParcelaTurma()) {
                // Caso esteja sendo utilizado um ControleGeracaoParcelaTurmaVO já gravado, então ao inves
                // de montar os da entidade ProcessoMatriculaCalendarioVO, vamos montar os dados da entidade
                // ControleGeracaoParcelaTurmaVO informada durante a criacado do calendário            
                if (dadosSQL.getInt("controleGeracaoParcelaTurma") != 0) {
                    processoMatriculaCalendarioVO.setControleGeracaoParcelaTurma(
                    getFacadeFactory().getControleGeracaoParcelaTurmaFacade().consultarPorChavePrimaria(dadosSQL.getInt("controleGeracaoParcelaTurma"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
                }
                processoMatriculaCalendarioVO.inicializarDadosComBaseControleGeracaoParcelaTurmaVO(processoMatriculaCalendarioVO.getControleGeracaoParcelaTurma());
            } else {
                processoMatriculaCalendarioVO.setAnoVencimentoPrimeiraMensalidade(dadosSQL.getInt("anovencimentoprimeiramensalidades"));
                processoMatriculaCalendarioVO.setDiaVencimentoPrimeiraMensalidade(dadosSQL.getInt("diavencimentoprimeiramensalidades"));
                processoMatriculaCalendarioVO.setMesVencimentoPrimeiraMensalidade(dadosSQL.getInt("mesvencimentoprimeiramensalidades"));
                processoMatriculaCalendarioVO.setQtdeDiasAvancarDataVencimentoMatricula(dadosSQL.getInt("qtdeDiasAvancarDataVencimentoMatricula"));
                processoMatriculaCalendarioVO.setDataVencimentoMatricula(dadosSQL.getDate("datavencimentomatricula"));                
                processoMatriculaCalendarioVO.setUsarDataVencimentoDataMatricula(dadosSQL.getBoolean("usardatavencimentodatamatricula"));
                processoMatriculaCalendarioVO.setMesSubsequenteMatricula(dadosSQL.getBoolean("messubsequentematricula"));
                processoMatriculaCalendarioVO.setMesDataBaseGeracaoParcelas(dadosSQL.getBoolean("mesdatabasegeracaoparcelas"));   
                processoMatriculaCalendarioVO.setZerarValorDescontoPlanoFinanceiroAluno(dadosSQL.getBoolean("zerarValorDescontoPlanoFinanceiroAluno"));
                processoMatriculaCalendarioVO.setUtilizarOrdemDescontoConfiguracaoFinanceira(dadosSQL.getBoolean("utilizarOrdemDescontoConfiguracaoFinanceira"));
                processoMatriculaCalendarioVO.setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(dadosSQL.getBoolean("utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual"));
                processoMatriculaCalendarioVO.setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(dadosSQL.getBoolean("acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno"));
            }
                    
            
            processoMatriculaCalendarioVO.getTurnoVO().setCodigo(dadosSQL.getInt("Turno.codigo"));
            processoMatriculaCalendarioVO.getTurnoVO().setNome(dadosSQL.getString("Turno.nome"));
            processoMatriculaCalendarioVO.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
            processoMatriculaCalendarioVO.getCursoVO().setNome(dadosSQL.getString("Curso.nome"));
            processoMatriculaCalendarioVO.getCursoVO().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("periodoletivoativounidadeensinocurso"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivo(dadosSQL.getDate("datainicioperiodoletivo"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivo(dadosSQL.getDate("datafimperiodoletivo"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo(dadosSQL.getString("tipoperiodoletivo"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setAnoReferenciaPeriodoLetivo(dadosSQL.getString("anoreferenciaperiodoletivo"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setSemestreReferenciaPeriodoLetivo(dadosSQL.getString("semestrereferenciaperiodoletivo"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoPrimeiroBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoPrimeiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoPrimeiroBimestre(dadosSQL.getDate("dataFimPeriodoLetivoPrimeiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoSegundoBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoSegundoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoSegundoBimestre(dadosSQL.getDate("dataFimPeriodoLetivoSegundoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoTerceiroBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoTerceiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoTerceiroBimestre(dadosSQL.getDate("dataFimPeriodoLetivoTerceiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoQuartoBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoQuartoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoQuartoBimestre(dadosSQL.getDate("dataFimPeriodoLetivoQuartoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoPrimeiroBimestre(dadosSQL.getInt("qtdeDiaLetivoPrimeiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoSegundoBimestre(dadosSQL.getInt("qtdeDiaLetivoSegundoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoTerceiroBimestre(dadosSQL.getInt("qtdeDiaLetivoTerceiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoQuartoBimestre(dadosSQL.getInt("qtdeDiaLetivoQuartoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaPrimeiroBimestre(dadosSQL.getInt("qtdeSemanaLetivaPrimeiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaSegundoBimestre(dadosSQL.getInt("qtdeSemanaLetivaSegundoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaTerceiroBimestre(dadosSQL.getInt("qtdeSemanaLetivaTerceiroBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaQuartoBimestre(dadosSQL.getInt("qtdeSemanaLetivaQuartoBimestre"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTotalDiaLetivoAno(dadosSQL.getInt("totalDiaLetivoAno"));
            processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTotalSemanaLetivaAno(dadosSQL.getInt("totalSemanaLetivaAno"));
            if(Uteis.isAtributoPreenchido(dadosSQL.getString("diaSemanaAula"))) {
            	processoMatriculaCalendarioVO.setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("diaSemanaAula")));
            }else {
            	processoMatriculaCalendarioVO.setDiaSemanaAula(DiaSemana.NENHUM);
            }
            if(Uteis.isAtributoPreenchido(dadosSQL.getString("turnoAula"))) {
            	processoMatriculaCalendarioVO.setTurnoAula(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("turnoAula")));
            }else {
            	processoMatriculaCalendarioVO.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
            }
            processoMatriculaCalendarioVO.montarListaSelectItemPoliticaDivulgacaoMatriculaVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorCodigoCurso(processoMatriculaCalendarioVO.getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			if (Uteis.isAtributoPreenchido(dadosSQL.getInt("politicaDivulgacaoMatriculaOnline"))) {
				processoMatriculaCalendarioVO.getPoliticaDivulgacaoMatriculaOnlineVO().setCodigo(dadosSQL.getInt("politicaDivulgacaoMatriculaOnline"));
				processoMatriculaCalendarioVO.setPoliticaDivulgacaoMatriculaOnlineVO(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorChavePrimaria(processoMatriculaCalendarioVO.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null));
				Boolean aux = false;
				for (SelectItem selectItem : processoMatriculaCalendarioVO.getListaSelectItemPoliticaDivulgacaoMatriculaVOs()) {
					if(selectItem.getValue().equals(processoMatriculaCalendarioVO.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo())) {
						aux = true;
					}
				}
				if(aux == false) {
					processoMatriculaCalendarioVO.getListaSelectItemPoliticaDivulgacaoMatriculaVOs().add(new SelectItem(processoMatriculaCalendarioVO.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo(), processoMatriculaCalendarioVO.getPoliticaDivulgacaoMatriculaOnlineVO().getNome()));											
				}
			}
            processoMatriculaCalendarioVO.setPossuiMatriculaVinculada(!dadosSQL.getBoolean("possuimatriculavinculada"));

            obj.getProcessoMatriculaCalendarioVOs().add(processoMatriculaCalendarioVO);

        } while (dadosSQL.next());
    }
    
        public List<ProcessoMatriculaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
        List<ProcessoMatriculaVO> vetResultado = new ArrayList<ProcessoMatriculaVO>(0);
        while (tabelaResultado.next()) {
            ProcessoMatriculaVO obj = new ProcessoMatriculaVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<ProcessoMatriculaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
        List<ProcessoMatriculaVO> vetResultado = new ArrayList<ProcessoMatriculaVO>(0);
        while (tabelaResultado.next()) {
            ProcessoMatriculaVO obj = new ProcessoMatriculaVO();
            montarDadosCompleto(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE UPPER(sem_acentos(processoMatricula.descricao)) ilike(sem_acentos('%");
        sql.append(valorConsulta.toUpperCase());
        sql.append("%'))");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
        }
        sql.append(" ORDER BY processoMatricula.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE ((data >= '");
        sql.append(prmFim);
        sql.append("')) ");
        sql.append(" AND (data <= '");
        sql.append(prmFim);
        sql.append("') ");
        if (unidadeEnsino.intValue() != 0) {
            sql.append("and unidadeEnsino.codigo = ");
            sql.append(unidadeEnsino);
            sql.append(" ");
        }
        sql.append(" ORDER BY data");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorDataInicio(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE ((dataInicio >= '");
        sql.append(prmIni);
        sql.append("')");
        sql.append(" and (dataInicio <= '");
        sql.append(prmFim);
        sql.append("')) ");
        if (unidadeEnsino.intValue() != 0) {
            sql.append("AND unidadeEnsino.codigo =");
            sql.append(unidadeEnsino);
            sql.append(" ");
        }
        sql.append(" ORDER BY dataInicio");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorDataFinal(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE ((dataFinal >=  '");
        sql.append(prmIni);
        sql.append("') ");
        sql.append(" AND  (dataFinal <= '");
        sql.append(prmFim);
        sql.append("'))");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" ");
            sql.append(" and unidadeEnsino.codigo = ");
            sql.append(unidadeEnsino);
        }
        sql.append(" ORDER BY dataFinal");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE exists (select processomatriculaunidadeensino.codigo from processomatriculaunidadeensino ");
        sql.append(" inner join unidadeensino on unidadeensino.codigo  = processomatriculaunidadeensino.unidadeensino ");
        if (unidadeEnsino.intValue() != 0) {
        	sql.append(" AND  unidadeensino.codigo = ");
        	sql.append(unidadeEnsino);
        	sql.append(" ");
        }
        sql.append(" where processomatriculaunidadeensino.ProcessoMatricula = ProcessoMatricula.codigo and  sem_acentos(UnidadeEnsino.nome) ilike (sem_acentos(?)) limit 1)");
        sql.append("ORDER BY ProcessoMatricula.descricao ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%");
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" INNER JOIN ProcessoMatriculaCalendario pmc ON pmc.processomatricula = processomatricula.codigo ");        
        sql.append(" INNER JOIN ProcessoMatriculaUnidadeEnsino ON ProcessoMatriculaUnidadeEnsino.processomatricula = processomatricula.codigo ");
        sql.append(" INNER jOIN periodoLetivoAtivoUnidadeEnsinoCurso plauec ON plauec.codigo = pmc.periodoLetivoAtivoUnidadeEnsinoCurso ");
        sql.append(" WHERE pmc.turno = ").append(turno).append(" AND pmc.curso = ").append(curso).append(" AND ProcessoMatriculaUnidadeEnsino.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        sql.append(" AND plauec.situacao = '").append(situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso).append("' ");
        sql.append(" AND (ProcessoMatricula.situacao = 'AT' or ProcessoMatricula.situacao = 'PR')");
        sql.append(" ORDER BY ProcessoMatricula.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorNomeUnidadeEnsinoESituacao(String valorConsulta, Integer unidadeEnsino, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE exists (select processomatriculaunidadeensino.codigo from processomatriculaunidadeensino ");
        sql.append(" inner join unidadeensino on unidadeensino.codigo  = processomatriculaunidadeensino.unidadeensino ");
        if (unidadeEnsino.intValue() != 0) {
        	sql.append(" AND  unidadeensino.codigo = ");
        	sql.append(unidadeEnsino);
        	sql.append(" ");
        }
        sql.append(" where processomatriculaunidadeensino.ProcessoMatricula = ProcessoMatricula.codigo and  sem_acentos(UnidadeEnsino.nome) ilike (sem_acentos(?)) limit 1)");                
        sql.append(" and situacao = '").append(situacao).append("' ");
        sql.append(" ORDER BY UnidadeEnsino.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%");
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public List<ProcessoMatriculaVO> consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, String situacao, boolean visaoAluno, boolean controlarAcesso, int nivelMontarDados, String matricula, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        StringBuffer sql = getSQLPadraoConsultaBasica();  
        sql.append(" INNER JOIN ProcessoMatriculaCalendario pmc ON pmc.processomatricula = processomatricula.codigo ");
        sql.append(" INNER JOIN ProcessoMatriculaUnidadeEnsino ON ProcessoMatriculaUnidadeEnsino.processomatricula = processomatricula.codigo ");
        sql.append(" INNER jOIN periodoLetivoAtivoUnidadeEnsinoCurso plauec ON plauec.codigo = pmc.periodoLetivoAtivoUnidadeEnsinoCurso ");
        if(visaoAluno) {
        	sql.append(" inner join curso on pmc.curso = curso.codigo ")	;
        	sql.append(" inner join matricula on matricula.matricula = '").append(matricula).append("' and matricula.situacao not in ('PC' , 'CA', 'TI', 'TS', 'FI', 'FO') ");	
        	sql.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ")	;
        	sql.append(" and matriculaperiodo.codigo =  ( ")	;
        	sql.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
        	sql.append(" and (");
        	sql.append(" select matriculaperiodo.situacaomatriculaperiodo from matriculaperiodo where matriculaperiodo.matricula = mp.matricula order by matriculaperiodo.ano || matriculaperiodo.semestre desc, matriculaperiodo.codigo desc limit 1 ");
        	sql.append(" ) not in ('PC' , 'CA', 'TI', 'TS')");
        	sql.append(" order by mp.ano desc, mp.semestre desc, mp.codigo desc limit 1 ) ")	;
        }
        sql.append(" WHERE pmc.turno = ").append(turno).append(" AND pmc.curso = ").append(curso).append(" AND ProcessoMatriculaUnidadeEnsino.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        sql.append(" AND plauec.situacao = '").append(situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso).append("' ");        
        if(visaoAluno){
            sql.append(" AND ProcessoMatricula.apresentarProcessoVisaoAluno = true  ");
            sql.append(" AND ((dataInicioMatriculaOnline <= current_date AND dataFimMatriculaOnline >= current_date) ");
            sql.append(" OR (pmc.datainicioinclusaodisciplina <= current_date and pmc.datafinalinclusaodisciplina >= current_date ");
            sql.append(" AND exists(select mp.codigo from matriculaperiodo as mp where mp.ano = plauec.anoreferenciaperiodoletivo and  mp.semestre = plauec.semestrereferenciaperiodoletivo and mp.matricula = matricula.matricula and mp.processomatricula = processomatricula.codigo )))");
            sql.append(" AND CASE WHEN ProcessoMatricula.bloquearAlunosPossuemConvenioRenovacaoOnline = true then  ");
            sql.append(" CASE WHEN (select planofinanceiroaluno.codigo from planofinanceiroaluno ");
            sql.append(" inner join itemplanofinanceiroaluno on itemplanofinanceiroaluno.planofinanceiroaluno = planofinanceiroaluno.codigo ");
            sql.append(" where itemplanofinanceiroaluno.tipoitemplanofinanceiro = 'CO' ");
            sql.append(" and matriculaperiodo in(select matriculaperiodo.codigo from matriculaperiodo ");
            sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
            sql.append(" where matricula = '").append(matricula).append("' ");
            sql.append(" order by (MatriculaPeriodo.ano||'/'||MatriculaPeriodo.semestre) desc, periodoletivo.periodoletivo desc, MatriculaPeriodo.codigo desc limit 1 ");
            sql.append(" ) limit 1) > 0 THEN false else true END else true END ");
            try {
                Matricula.verificarPermissaoUsuarioFuncionalidade(usuarioVO, "PermitirRealizarEdicaoMatriculaRenovada");
            } catch (Exception e) {
            	sql.append(" and not exists ( ");
            	sql.append("   select mp.codigo from matriculaperiodo mp where mp.matricula = '").append(matricula).append("' ");
            	sql.append("   and situacaomatriculaperiodo != 'PC' and mp.ano = plauec.anoreferenciaperiodoletivo ");
            	sql.append("   and mp.semestre = plauec.semestrereferenciaperiodoletivo ");
            	sql.append(" ) ");
            }
        }
        if (situacao.equals("PR_AT")) { // Neste caso, temos que montar os processos de matrícula ativos para matrícula e para pré-matrícula
            sql.append(" AND ((ProcessoMatricula.situacao = 'PR') OR (ProcessoMatricula.situacao = 'AT'))");
        } else {
            sql.append(" AND ProcessoMatricula.situacao = '").append(situacao).append("' ");
        }
        if(visaoAluno) {
        	sql.append(" and ( ");
        	sql.append(" ( ");
        	sql.append(" (select count(codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC')  = 1 ");
        	sql.append(" and case when curso.periodicidade = 'AN' or (curso.periodicidade = 'SE' and matriculaperiodo.semestre = '2' ) ");
        	sql.append(" then (matriculaperiodo.ano::int + 1)::varchar else matriculaperiodo.ano end  =  plauec.anoreferenciaperiodoletivo ");
        	sql.append(" and case when curso.periodicidade = 'SE' ");
        	sql.append(" then case when matriculaperiodo.semestre = '1' then '2' else '1' end  else '' end  =  plauec.semestrereferenciaperiodoletivo) ");
        	sql.append(" or ( ");
        	sql.append(" (select count(codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo != 'PC')  >= 1	 ");
        	sql.append(" and (matriculaperiodo.ano||matriculaperiodo.semestre) <= (plauec.anoreferenciaperiodoletivo||plauec.semestrereferenciaperiodoletivo) ");
        	sql.append(" ) ) ");
        }
        if (Uteis.isAtributoPreenchido(tipoAluno) && tipoAluno != TipoAlunoCalendarioMatriculaEnum.AMBOS) {
        	sql.append(" AND (processomatricula.tipoaluno = 'AMBOS' OR processomatricula.tipoaluno = '").append(tipoAluno).append("') ");
        }
      
        sql.append(" ORDER BY ProcessoMatricula.codigo desc");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }

    public void adicionarProcessoMatriculaCalendario(ProcessoMatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CursoTurnoVO cursoTurnoVO, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getProcessoMatriculaCalendarioFacade().validarDadosCursoExisteGradeCurricularAtiva(cursoTurnoVO.getCurso(), usuario);
        inicializarDadosProcessoMatriculaCalendario(processoMatriculaCalendario, cursoTurnoVO, usuario);
        adicionarObjProcessoMatriculaCalendarioVOs(obj, processoMatriculaCalendario, false);
    }

    public void inicializarDadosProcessoMatriculaCalendario(ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CursoTurnoVO cursoTurnoVO, UsuarioVO usuario) throws Exception {
        processoMatriculaCalendario.setCursoVO(cursoTurnoVO.getCursoVO());
        processoMatriculaCalendario.setTurnoVO(cursoTurnoVO.getTurno());
        processoMatriculaCalendario.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataAbertura(new Date());
        processoMatriculaCalendario.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setResponsavelFechamento(usuario);
        processoMatriculaCalendario.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCursoVO(cursoTurnoVO.getCursoVO());
        processoMatriculaCalendario.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTurnoVO(cursoTurnoVO.getTurno());        
        processoMatriculaCalendario.setDiaSemanaAula(cursoTurnoVO.getDiaSemanaAula());
        processoMatriculaCalendario.setTurnoAula(cursoTurnoVO.getTurnoAula());
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>ProcessoMatriculaCalendarioVO</code> ao List <code>processoMatriculaCalendarioVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>ProcessoMatriculaCalendario</code> - getCurso().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ProcessoMatriculaCalendarioVO</code> que será adiocionado ao Hashtable correspondente.
     */
	public void adicionarObjProcessoMatriculaCalendarioVOs(ProcessoMatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, boolean editarProcessoMatriculaCalendarioVO) throws ConsistirException {
		if (!obj.getNivelProcessoMatricula().equals(TipoNivelEducacional.POS_GRADUACAO.getValor()) && !obj.getNivelProcessoMatricula().equals(TipoNivelEducacional.EXTENSAO.getValor())) {
			ProcessoMatriculaCalendarioVO.validarDados(obj, processoMatriculaCalendario);
			ProcessoMatriculaCalendarioVO.validarPeriodicidadeCursoTipoPeriodoLetivo (obj, processoMatriculaCalendario);
		}
		int index = 0;
		int achou = 0;
		Iterator<ProcessoMatriculaCalendarioVO> i = obj.getProcessoMatriculaCalendarioVOs().iterator();
		while (i.hasNext()) {
			ProcessoMatriculaCalendarioVO objExistente = (ProcessoMatriculaCalendarioVO) i.next();
			if (objExistente.getCursoVO().getCodigo().equals(processoMatriculaCalendario.getCursoVO().getCodigo()) && objExistente.getTurnoVO().getCodigo().equals(processoMatriculaCalendario.getTurnoVO().getCodigo()) && !editarProcessoMatriculaCalendarioVO) {
				throw new ConsistirException("Não é possível substituir um Calendário de Matrícula Curso já adicionado! Realize a edição do mesmo caso seja permitido!");
			}
			if (objExistente.getCursoVO().getCodigo().equals(processoMatriculaCalendario.getCursoVO().getCodigo()) && objExistente.getTurnoVO().getCodigo().equals(processoMatriculaCalendario.getTurnoVO().getCodigo())) {
				obj.getProcessoMatriculaCalendarioVOs().set(index, processoMatriculaCalendario);
				achou = 1;
				break;
			} else {
				obj.getProcessoMatriculaCalendarioVOs().set(index, objExistente);
			}
			index++;
		}
		if (achou == 0) {
			obj.getProcessoMatriculaCalendarioVOs().add(processoMatriculaCalendario);
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoProcessoMatricula(ProcessoMatriculaVO processoMatricula, String situacao, UsuarioVO usuario) throws Exception {
        String sql = "UPDATE processoMatricula set situacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{situacao, processoMatricula.getCodigo()});        
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoProcessoMatricula(ProcessoMatriculaVO processoMatriculaVO, Integer unidadeEnsino, String situacaoProcessoMatriculaBanco, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        alterarSituacaoProcessoMatricula(processoMatriculaVO, processoMatriculaVO.getSituacao(), usuario);
        
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void ativarPreMatriculaPeriodo(ProcessoMatriculaVO processoMatriculaVO, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        List<MatriculaPeriodoVO> listaMatriculaPeriodo = null;
        try {
//            boolean existeOutraMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorProcessoMatriculaSituacao(processoMatriculaVO.getCodigo(), "AT");
//            if (existeOutraMatriculaPeriodoAtiva) {
//                throw new Exception(UteisJSF.internacionalizar("msg_ConfirmacaoPreMatricula_outraAtiva"));
//            }
            listaMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorProcessoMatricula(processoMatriculaVO.getCodigo(), false, usuario);
            if (!listaMatriculaPeriodo.isEmpty()) {
                for (MatriculaPeriodoVO matriculaPeriodoVO : listaMatriculaPeriodo) {
                    matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
                    matriculaPeriodoVO.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getMatricula(), unidadeEnsino, NivelMontarDados.TODOS, usuario));
                    getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosParaProcessarGeracaoContasReceberReferentesParcelasMatriculaPeriodo(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), usuario, configuracaoFinanceiroVO);
                    getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerAtivada(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, configuracaoFinanceiroVO, usuario);
                    getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaVOParaAtivada(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, usuario, configuracaoFinanceiroVO);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    public ProcessoMatriculaVO consultaRapidaPorMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception {
    	StringBuffer sb = new StringBuffer();
    	sb.append(getSQLPadraoConsultaBasica());
    	sb.append(" inner join matriculaperiodo on matriculaperiodo.processomatricula = processomatricula.codigo ");
    	sb.append(" where matriculaperiodo.codigo = ").append(matriculaPeriodo);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	ProcessoMatriculaVO obj = new ProcessoMatriculaVO();
    	if (tabelaResultado.next()) {
    		montarDadosBasico(obj, tabelaResultado);
    	}
    	return obj;
    }
    
    @Override
    public List<ProcessoMatriculaVO> consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, String situacao, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" INNER JOIN ProcessoMatriculaCalendario pmc ON pmc.processomatricula = processomatricula.codigo ");       
        sql.append(" INNER JOIN ProcessoMatriculaUnidadeEnsino  ON ProcessoMatriculaUnidadeEnsino.processomatricula = processomatricula.codigo ");
        sql.append(" INNER jOIN periodoLetivoAtivoUnidadeEnsinoCurso plauec ON plauec.codigo = pmc.periodoLetivoAtivoUnidadeEnsinoCurso ");
        sql.append(" WHERE 1=1 ");
        sql.append(" and pmc.datainiciomatricula  <= current_date  and pmc.datafinalmatricula >= current_date ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sql.append(" AND ProcessoMatriculaUnidadeEnsino.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        }
        if(Uteis.isAtributoPreenchido(curso)) {
        	sql.append(" AND pmc.curso = ").append(curso);
        }
        if(Uteis.isAtributoPreenchido(turno)) {
        	sql.append(" AND pmc.turno = ").append(turno);
        }
        sql.append(" AND plauec.situacao = '").append(situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso).append("' ");        
        if (situacao.equals("PR_AT")) { // Neste caso, temos que montar os processos de matrícula ativos para matrícula e para pré-matrícula
            sql.append(" AND ((ProcessoMatricula.situacao = 'PR') OR (ProcessoMatricula.situacao = 'AT'))");
        } else {
            sql.append(" AND ProcessoMatricula.situacao = '").append(situacao).append("' ");
        }
        if(ano != null && !ano.trim().isEmpty()){
        	sql.append(" and plauec.anoReferenciaPeriodoLetivo  = '").append(ano).append("' ");
        	if(semestre != null && !semestre.trim().isEmpty()){
            	sql.append(" and plauec.semestreReferenciaPeriodoLetivo = '").append(semestre).append("' ");
            }
        }
        if (Uteis.isAtributoPreenchido(tipoAluno) && tipoAluno != TipoAlunoCalendarioMatriculaEnum.AMBOS) {
        	sql.append(" AND (processomatricula.tipoaluno = 'AMBOS' OR processomatricula.tipoaluno = '").append(tipoAluno).append("') ");
        }
      
        sql.append(" ORDER BY ProcessoMatricula.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);
    }
    
    @Override
    public Boolean verificarPossibilidadeMatriculaOnline(Integer codigoBanner, Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoProcessoMatricula, Integer codigoGradeCurricular, Integer codigoTurma, Map<String, Integer> curso, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" select unidadeensinocurso.unidadeensino, unidadeensinocurso.curso, unidadeensinocurso.turno, processomatricula.codigo as processomatricula, ");
    	sqlStr.append(" (select gradecurricular.codigo from gradecurricular  where gradecurricular.curso = unidadeensinocurso.curso and gradecurricular.situacao = 'AT' limit 1) as gradecurricular");
    	sqlStr.append(" from processomatricula  ");
    	sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo");
    	sqlStr.append(" and (processomatriculacalendario.datainiciomatricula <= now() or processomatriculacalendario.datainiciomatricula is null) ");
    	sqlStr.append("	and (processomatriculacalendario.datafinalmatricula >= now() or processomatriculacalendario.datafinalmatricula is null) ");
    	sqlStr.append(" and (processomatriculacalendario.datainiciomatforaprazo <= now() or processomatriculacalendario.datainiciomatforaprazo is null) ");
    	sqlStr.append("	and (processomatriculacalendario.datafinalmatforaprazo >= now() or processomatriculacalendario.datafinalmatforaprazo is null) ");
    	sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino and unidadeensinocurso.curso = processomatriculacalendario.curso  and unidadeensinocurso.turno = processomatriculacalendario.turno ");
    	sqlStr.append(" where processomatriculacalendario.politicadivulgacaomatriculaonline = ").append(codigoBanner);
    	sqlStr.append(" and processomatricula.situacao in ('AT', 'PR') ");
    	sqlStr.append(" and processomatricula.apresentarprocessovisaoaluno  ");
    	sqlStr.append(" and processomatricula.datainiciomatriculaonline <= now()");
    	sqlStr.append(" and processomatricula.datafimmatriculaonline >= now()");
    	sqlStr.append(" and (select turma.codigo from turma  where turma.curso = unidadeensinocurso.curso and turma.turno = unidadeensinocurso.turno ");
    	sqlStr.append(" and turma.unidadeensino = unidadeensinocurso.unidadeensino and turma.situacao = 'AB' ");
    	sqlStr.append(" and turma.periodoletivo in (");
    	sqlStr.append(" select periodoletivo.codigo from gradecurricular ");
    	sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo  where periodoletivo.periodoletivo = 1 ");
    	sqlStr.append(" and gradecurricular.curso = unidadeensinocurso.curso and gradecurricular.situacao = 'AT') ");
    	sqlStr.append(" and  (select planofinanceirocurso from condicaopagamentoplanofinanceirocurso ");
    	sqlStr.append("	where condicaopagamentoplanofinanceirocurso.tipousocondicaopagamento = 'MATRICULA_REGULAR' ");
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
    	if ((Uteis.isAtributoPreenchido(usuarioVO)) && (usuarioVO.getIsApresentarVisaoAluno())) {
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarcondicaovisaoaluno  ");
    	}
    	if ((Uteis.isAtributoPreenchido(usuarioVO)) && (usuarioVO.getIsApresentarVisaoProfessor())) {
    		sqlStr.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineProfessor ");
    	}
    	if ((Uteis.isAtributoPreenchido(usuarioVO)) && (usuarioVO.getIsApresentarVisaoCoordenador())) {
    		sqlStr.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineCoordenador ");
    	}
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.planofinanceirocurso = case when turma.planofinanceirocurso is not null then turma.planofinanceirocurso else unidadeensinocurso.planofinanceirocurso end ");
    	sqlStr.append("	limit 1");
    	sqlStr.append("	) is not null limit 1) is not null");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if(rs.next()) {
        	curso.put("codigoCurso", rs.getInt("curso"));
        	return true;
        } 
        return false;
    }
    
    @Override
    public List<ProcessoMatriculaVO> consultarProcessosMatriculasPorCodigoBanner(Integer codigoBanner, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" select distinct processomatricula.codigo as processomatricula, processomatricula.*");
    	sqlStr.append(" from processomatricula  ");
    	sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino and unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno ");
    	sqlStr.append(" where processomatriculacalendario.politicadivulgacaomatriculaonline = ").append(codigoBanner);
    	sqlStr.append(" and processomatricula.situacao in ('AT', 'PR') ");
    	sqlStr.append(" and processomatricula.apresentarprocessovisaoaluno  ");
    	sqlStr.append(" and processomatricula.datainiciomatriculaonline <= now()");
    	sqlStr.append(" and processomatricula.datafimmatriculaonline >= now()");
    	sqlStr.append(" and processomatricula.tipoaluno in ('AMBOS', 'CALOURO') ");
    	if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(unidadeEnsino);
    	}
//    	sqlStr.append(" and processomatriculacalendario.datainiciomatricula <= now() and processomatriculacalendario.datafinalmatricula >= now()");
//    	sqlStr.append(" and processomatriculacalendario.datainiciomatforaprazo <= now() and processomatriculacalendario.datafinalmatforaprazo >= now()");    	
    	sqlStr.append(" and (select turma.codigo from turma  where turma.curso = unidadeensinocurso.curso and turma.turno = unidadeensinocurso.turno ");
    	sqlStr.append(" and turma.unidadeensino = unidadeensinocurso.unidadeensino and turma.situacao = 'AB' ");
    	sqlStr.append(" and turma.periodoletivo in (");
    	sqlStr.append(" select periodoletivo.codigo from gradecurricular ");
    	sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo  where periodoletivo.periodoletivo = 1 ");
    	sqlStr.append(" and gradecurricular.curso = unidadeensinocurso.curso and gradecurricular.situacao = 'AT') ");
    	sqlStr.append(" and  (select planofinanceirocurso from condicaopagamentoplanofinanceirocurso ");
    	sqlStr.append("	where condicaopagamentoplanofinanceirocurso.tipousocondicaopagamento = 'MATRICULA_REGULAR' ");
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
    	if(!Uteis.isAtributoPreenchido(usuarioVO) || !Uteis.isAtributoPreenchido(usuarioVO.getVisaoLogar())){
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineExterna  ");
    	}else if(Uteis.isAtributoPreenchido(usuarioVO) && usuarioVO.getIsApresentarVisaoCoordenador()){
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineCoordenador  ");
    	}else if(Uteis.isAtributoPreenchido(usuarioVO) && usuarioVO.getIsApresentarVisaoProfessor()){
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineProfessor  ");
    	}else {
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarCondicaoVisaoAluno  ");
    	}
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.planofinanceirocurso = case when turma.planofinanceirocurso is not null then turma.planofinanceirocurso else unidadeensinocurso.planofinanceirocurso end ");
    	sqlStr.append("	limit 1");
    	sqlStr.append("	) is not null limit 1) is not null");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        ProcessoMatriculaVO processoMatriculaVO = null;
        List<ProcessoMatriculaVO> processoMatriculaVOs = new ArrayList<ProcessoMatriculaVO>();
        while(rs.next()) {
			processoMatriculaVO = new ProcessoMatriculaVO();
			processoMatriculaVO = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			processoMatriculaVO.setProcessoMatriculaUnidadeEnsinoVOs(getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().consultarPorProcessoMatricula(processoMatriculaVO.getCodigo()));
			processoMatriculaVOs.add(processoMatriculaVO);	
        }
        return processoMatriculaVOs;
    }
	
    public Integer consultaRapidaPorCodigoTurmaAtivoEntrePeriodo(Integer turma, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception {    	
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select processomatricula.codigo from processomatricula ");
    	sql.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo ");
    	sql.append(" inner join turma on turma.unidadeensino = processomatriculaunidadeensino.unidadeensino ");
    	sql.append(" inner join ProcessoMatriculaCalendario on ProcessoMatriculaCalendario.processomatricula = processomatricula.codigo ");    	
    	sql.append(" where turma.codigo = ").append(turma).append(" and ProcessoMatriculaCalendario.curso = turma.curso and ProcessoMatriculaCalendario.turno = turma.turno and processomatricula.datainicio <= current_date and processomatricula.datafinal >= current_date ");
    	 if (Uteis.isAtributoPreenchido(tipoAluno) && tipoAluno != TipoAlunoCalendarioMatriculaEnum.AMBOS) {
         	sql.append(" AND (processomatricula.tipoaluno = 'AMBOS' OR processomatricula.tipoaluno = '").append(tipoAluno).append("') ");
         }
    	sql.append(" order by processomatricula.codigo desc limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	
    	if (tabelaResultado.next()) {    		
    		return tabelaResultado.getInt("codigo");
    	} else {
    		return 0;
    	}
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPeriodoProcessoMatricula(ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuario) throws Exception {
        String sql = "UPDATE processoMatricula set dataInicio=?, dataFinal=?, dataInicioMatriculaOnline=?, dataFimMatriculaOnline=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{processoMatriculaVO.getDataInicio(), processoMatriculaVO.getDataFinal(), processoMatriculaVO.getDataInicioMatriculaOnline(), processoMatriculaVO.getDataFimMatriculaOnline(), processoMatriculaVO.getCodigo()});

    }
	

	@Override
	public String consultarAnoSemestrePorProcessoMatriculaUnidadeEnsinoCurso(Integer processoMatricula, Integer unidadeEnsinoCurso) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select anoreferenciaperiodoletivo ||'/'||semestrereferenciaperiodoletivo as anosemestre from processomatricula"); 
		sql.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo"); 
		sql.append(" inner join periodoletivoativounidadeensinocurso on processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo");
		sql.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
		sql.append(" where  processomatricula.codigo = ").append(processoMatricula);
		sql.append(" and periodoletivoativounidadeensinocurso.curso = unidadeensinocurso.curso "); 
		sql.append(" and periodoletivoativounidadeensinocurso.turno = unidadeensinocurso.turno ");
		sql.append(" and processomatriculacalendario.curso = unidadeensinocurso.curso "); 
		sql.append(" and processomatriculacalendario.turno = unidadeensinocurso.turno ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()) {
			return rs.getString("anosemestre");
		}
		return null;
	}
	
	@Override
    public List<ProcessoMatriculaVO> consultarProcessosMatriculaOnline(Boolean apresentarVisaoAluno, Boolean matriculaOnline , Integer unidadeEnsino,String ano,String semestre,Integer curso,Integer turno, UsuarioVO usuarioVO, Integer limit, Boolean trazerUltimoCalendarioAtivo ,int nivelMontarDados,  Boolean validarTurmaMatriculaOnline, Boolean transferenciaInterna) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" select distinct processomatricula.codigo as processomatricula, periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo as ano, periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo as semestre, processomatricula.*");
    	sqlStr.append(" from processomatricula  ");
    	sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo ");
    	sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo ");    	
    	sqlStr.append(" inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
    	sqlStr.append(" inner join unidadeEnsinoCurso on unidadeEnsinoCurso.unidadeEnsino = processomatriculaunidadeensino.unidadeEnsino and unidadeEnsinoCurso.curso = processomatriculacalendario.curso and unidadeEnsinoCurso.turno = processomatriculacalendario.turno ");
    	sqlStr.append(" where processomatricula.situacao in ('AT', 'PR') ");
    	if(apresentarVisaoAluno) {    		
    		sqlStr.append(" and processomatricula.apresentarprocessovisaoaluno  ");
    	}
    	if(matriculaOnline) {   	
    		sqlStr.append(" and processomatricula.datainiciomatriculaonline <= current_date  ");
    		sqlStr.append(" and processomatricula.datafimmatriculaonline >= current_date ");
    	}else {
    		sqlStr.append(" and processomatricula.datainicio <= current_date ");
    		sqlStr.append(" and processomatricula.datafinal >= current_date ");
    	}
    	if(transferenciaInterna) {
    		sqlStr.append(" and processomatricula.tipoaluno in ('AMBOS', 'VETERANO') ");
    	} else {
    		sqlStr.append(" and processomatricula.tipoaluno in ('AMBOS', 'CALOURO') ");
    	}
    	sqlStr.append(" and processomatriculaunidadeensino.unidadeensino = ").append(unidadeEnsino);
    	sqlStr.append(" and processomatriculacalendario.curso = ").append(curso);
    	sqlStr.append(" and processomatriculacalendario.turno = ").append(turno);
    	if(trazerUltimoCalendarioAtivo == null || !trazerUltimoCalendarioAtivo) {
    		sqlStr.append(" and case when tipoperiodoletivo = 'SE' then ").append("(periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("'").append(" and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = '").append(semestre).append("')");
    		sqlStr.append(" when tipoperiodoletivo = 'AN' then (periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("')");
    		sqlStr.append(" else true end");
    	}  	
    	
    	if(validarTurmaMatriculaOnline) {    	
    		sqlStr.append(getSqlConsiderarTurmaMatricula(matriculaOnline));
    	}    
    	sqlStr.append("	 order by periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo desc, periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo desc");
    	if(Uteis.isAtributoPreenchido(limit)) {
    		sqlStr.append(" limit ").append(limit);	
    	}
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        ProcessoMatriculaVO processoMatriculaVO = null;
        List<ProcessoMatriculaVO> processoMatriculaVOs = new ArrayList<ProcessoMatriculaVO>();
        while(rs.next()) {
			processoMatriculaVO = new ProcessoMatriculaVO();
			processoMatriculaVO = montarDados(rs, nivelMontarDados, usuarioVO);
			processoMatriculaVO.setAno(rs.getString("ano"));
			processoMatriculaVO.setSemestre(rs.getString("semestre"));
			processoMatriculaVOs.add(processoMatriculaVO);	
        }
        return processoMatriculaVOs;
    }

	private StringBuffer getSqlConsiderarTurmaMatricula(Boolean matriculaOnline) {
		StringBuffer sqlStr = new StringBuffer();		
		sqlStr.append(" and (select turma.codigo from turma  where turma.curso = processomatriculacalendario.curso and turma.turno = processomatriculacalendario.turno ");
    	sqlStr.append(" and turma.unidadeensino = processomatriculaunidadeensino.unidadeensino and turma.situacao = 'AB' ");   	
    	sqlStr.append(" and  (select planofinanceirocurso from condicaopagamentoplanofinanceirocurso ");
    	sqlStr.append("	where condicaopagamentoplanofinanceirocurso.tipousocondicaopagamento = 'MATRICULA_REGULAR' ");
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
	    if(matriculaOnline) {
	    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineExterna  ");
	    }	   
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.planofinanceirocurso = case when turma.planofinanceirocurso is not null then turma.planofinanceirocurso else unidadeensinocurso.planofinanceirocurso end ");
    	sqlStr.append("	limit 1");
    	sqlStr.append("	) is not null limit 1) is not null ");
        
		return sqlStr;
	}

	
	@Override
	public List<ProcessoMatriculaRSVO> consultarProcessosMatriculaOnline(Integer unidadeEnsino,String ano,String semestre,Integer curso,Integer turno,Boolean validarTurmaMatriculaOnline, UsuarioVO usuarioVO) {
		List<ProcessoMatriculaRSVO> processoMatriculaRSVOs = new ArrayList<ProcessoMatriculaRSVO>();
		try {
			List<ProcessoMatriculaVO> processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculaOnline(true ,true ,unidadeEnsino, ano, semestre, curso, turno, usuarioVO, null, false,Uteis.NIVELMONTARDADOS_TODOS,validarTurmaMatriculaOnline, false);
			processoMatriculaVOs.stream().forEach(processoMatriculaVO -> {
				ProcessoMatriculaRSVO processoMatriculaRSVO = new ProcessoMatriculaRSVO();
				processoMatriculaRSVO.setCodigo(processoMatriculaVO.getCodigo());
				processoMatriculaRSVO.setNome(processoMatriculaVO.getDescricao());
				try {
					processoMatriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(processoMatriculaVO.getPermiteIncluirExcluirDisciplinaVisaoAluno() && processoMatriculaVO.getProcessoMatriculaCalendarioVOs().get(0).verificarDataEstaDentroPeriodoInclusaoExclusaoValido(new Date()));
				}catch (Exception e) {
					processoMatriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(false);	
				}
				processoMatriculaRSVOs.add(processoMatriculaRSVO);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processoMatriculaRSVOs;
	}
	
	
	
	@Override
	public void realizarInclusaoProcessoMatriculaDeAcordoComMatriculaPeriodo(MatriculaPeriodoVO obj, List<ProcessoMatriculaVO> listaProcessoMatriculaVOs, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo().equals(0)) {
			return;
		}
		if (!listaProcessoMatriculaVOs.isEmpty()) {
			boolean existeProcessoMatriculaLista = false;
			for (ProcessoMatriculaVO processoMatriculaVO : listaProcessoMatriculaVOs) {
				if (processoMatriculaVO.getCodigo().equals(obj.getProcessoMatricula())) {
					existeProcessoMatriculaLista = true;
				}
			}
			if (!existeProcessoMatriculaLista) {
				listaProcessoMatriculaVOs.add(adicionarProcessoMatriculaVinculadoMatriculaPeriodo(obj,usuarioVO));
			}
		} else {
			listaProcessoMatriculaVOs.add(adicionarProcessoMatriculaVinculadoMatriculaPeriodo(obj,usuarioVO));
		}
	}
	
	private ProcessoMatriculaVO adicionarProcessoMatriculaVinculadoMatriculaPeriodo(MatriculaPeriodoVO obj, UsuarioVO usuarioVO) throws Exception {
		ProcessoMatriculaVO processoMatriculaVO = new ProcessoMatriculaVO();
		if (!obj.getProcessoMatricula().equals(0)) {
			processoMatriculaVO.setCodigo(obj.getProcessoMatricula());
			getFacadeFactory().getProcessoMatriculaFacade().carregarDados(processoMatriculaVO, NivelMontarDados.BASICO, usuarioVO);
			return processoMatriculaVO;
		}
		processoMatriculaVO = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorMatriculaPeriodo(obj.getCodigo(), usuarioVO);
		return processoMatriculaVO;
	}
	
	@Override
	public List consultarProcessoMatriculaPorUnidadeEnsino(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Boolean renovandoMatricula, Boolean existeRegraRenovacaoPorProcessoMatriculaTurma, UsuarioVO usuarioVO, Boolean transferenciaInterna) throws Exception {
		List<ProcessoMatriculaVO> listaProcessoMatriculaVOs = new ArrayList<ProcessoMatriculaVO>(0);
		boolean existeOutraMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoAtivaPorSituacao(matriculaVO.getMatricula(), matriculaPeriodoVO.getCodigo(), "AT");
		Boolean exiteTransferenciaInterna  = getFacadeFactory().getTransferenciaEntradaFacade().consultarExisteTransferencia(matriculaVO.getAluno().getCodigo(),  TipoTransferenciaEntrada.INTERNA, usuarioVO);
		if (renovandoMatricula == null) {
			renovandoMatricula = Boolean.FALSE;
		}
		if ((existeOutraMatriculaPeriodoAtiva && renovandoMatricula)) {
			listaProcessoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR", usuarioVO.getVisaoLogar().equals("aluno"), false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), usuarioVO, TipoAlunoCalendarioMatriculaEnum.VETERANO);
		} else if ((!existeOutraMatriculaPeriodoAtiva && (renovandoMatricula|| transferenciaInterna))) {
			listaProcessoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR_AT", usuarioVO.getVisaoLogar().equals("aluno"), false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), usuarioVO, TipoAlunoCalendarioMatriculaEnum.VETERANO);
		}
		if (listaProcessoMatriculaVOs.isEmpty() && !renovandoMatricula && !exiteTransferenciaInterna) {
			listaProcessoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR_AT", existeRegraRenovacaoPorProcessoMatriculaTurma ? false : usuarioVO.getVisaoLogar().equals("aluno"), false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), usuarioVO, TipoAlunoCalendarioMatriculaEnum.CALOURO);
		}
		realizarInclusaoProcessoMatriculaDeAcordoComMatriculaPeriodo(matriculaPeriodoVO, listaProcessoMatriculaVOs, usuarioVO);
		return listaProcessoMatriculaVOs;
	}
	
	
	
}
