package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DownloadVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DownloadInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DownloadVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>DownloadVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DownloadVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class Download extends ControleAcesso implements DownloadInterfaceFacade {

    protected static String idEntidade;

    public Download() throws Exception {
        super();
        setIdEntidade("Download");
    }

    public DownloadVO novo() throws Exception {
        Download.incluir(getIdEntidade());
        DownloadVO obj = new DownloadVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DownloadVO</code>. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DownloadVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DownloadVO obj, UsuarioVO usuario) throws Exception {
        try {
            DownloadVO.validarDados(obj);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Download( arquivo, pessoa, dataDownload, turma, disciplina, matriculaperiodo  ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getArquivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getArquivo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataDownload()));
                    if (obj.getTurma().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getTurma().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    if (obj.getDisciplina().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getDisciplina().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (obj.getMatriculaPeriodoVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getMatriculaPeriodoVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
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
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DownloadVO</code>. Sempre utiliza a
     * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
     * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DownloadVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DownloadVO obj, UsuarioVO usuario) throws Exception {
        try {
            DownloadVO.validarDados(obj);
            Download.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Download set arquivo=?, pesssoa=?, dataDownload=?, turma=?, disciplina=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getArquivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getArquivo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataDownload()));
                    if (obj.getTurma().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getTurma().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getDisciplina().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getDisciplina().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DownloadVO</code>. Sempre localiza o registro a
     * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DownloadVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DownloadVO obj, UsuarioVO usuario) throws Exception {
        try {
            Download.excluir(getIdEntidade());
            String sql = "DELETE FROM Download WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDownloadsArquivo(ArquivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM download WHERE arquivo = " + obj.getCodigo()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Download</code> através do valor do atributo
     * <code>Date dataDownload</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DownloadVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataDownload(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Download WHERE ((dataDownload >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataDownload <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataDownload";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Download</code> através do valor do atributo <code>nome</code> da
     * classe <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>DownloadVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT Download.* FROM Download, Pessoa WHERE Download.pesssoa = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Download</code> através do valor do atributo <code>nome</code> da
     * classe <code>Arquivo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>DownloadVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeArquivo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT Download.* FROM Download, Arquivo WHERE Download.arquivo = Arquivo.codigo and upper( Arquivo.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Arquivo.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Download</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DownloadVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Download WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List consultarPorCodigoArquivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer pessoa, String ano, String semestre) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Download");
        sb.append(" INNER JOIN matriculaperiodo on download.matriculaperiodo = matriculaperiodo.codigo");
        sb.append(" WHERE download.arquivo = " + valorConsulta.intValue());
        sb.append(" AND pessoa = ").append(pessoa);
//        sb.append(" AND matriculaperiodo.ano = '").append(ano).append("'");
//        sb.append(" AND matriculaperiodo.semestre = '").append(semestre).append("'");
        sb.append(" ORDER BY Download.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>DownloadVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DownloadVO</code>.
     *
     * @return O objeto da classe <code>DownloadVO</code> com os dados devidamente montados.
     */
    public static DownloadVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DownloadVO obj = new DownloadVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo")));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.setDataDownload(dadosSQL.getDate("dataDownload"));
        obj.setTurma(dadosSQL.getInt("turma"));
        obj.setDisciplina(dadosSQL.getInt("disciplina"));
        obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosArquivo(obj, nivelMontarDados,usuario);
        montarDadosPesssoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>DownloadVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPesssoa(DownloadVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ArquivoVO</code> relacionado ao objeto
     * <code>DownloadVO</code>. Faz uso da chave primária da classe <code>ArquivoVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosArquivo(DownloadVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivo().getCodigo().intValue() == 0) {
            obj.setArquivo(new ArquivoVO());
            return;
        }
        obj.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivo().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DownloadVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DownloadVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM Download WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Download ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Download.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Download.idEntidade = idEntidade;
    }
    
	public void registrarDownload(ArquivoVO arquivoVO, UsuarioVO usuarioVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ) throws Exception {
		if (arquivoVO != null && usuarioVO.getPessoa().getAluno() && !usuarioVO.getPermiteSimularNavegacaoAluno()) {
			DownloadVO download = new DownloadVO();
			download.setArquivo(arquivoVO);
			download.setPessoa(usuarioVO.getPessoa());
			download.setDataDownload(new Date());
			download.setTurma(arquivoVO.getTurma().getCodigo());
			download.setDisciplina(arquivoVO.getDisciplina().getCodigo());
			int codigoMatriculaPeriodo = matriculaPeriodoVO.getCodigo();
			if(!Uteis.isAtributoPreenchido(codigoMatriculaPeriodo)){
				codigoMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), false, usuarioVO);
			}
			download.getMatriculaPeriodoVO().setCodigo(codigoMatriculaPeriodo);
			if (Uteis.isAtributoPreenchido(download.getMatriculaPeriodoVO().getCodigo()) && Uteis.isAtributoPreenchido(download.getDisciplina()) && !Uteis.isAtributoPreenchido(download.getTurma())) {
				MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade()
						.consultarPorMatriculaPeriodoDisciplina(download.getMatriculaPeriodoVO().getCodigo(), download.getDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				download.setTurma(matriculaPeriodoTurmaDisciplinaVO == null ? 0 : matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
			}
			incluir(download, usuarioVO);
		}
	}
}
