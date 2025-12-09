package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.SetranspAlunoVO;
import negocio.comuns.academico.SetranspVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.SetranspInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>SetranspVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>SetranspVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see SetranspVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Setransp extends ControleAcesso implements SetranspInterfaceFacade {

    protected static String idEntidade;
    private ArquivoHelper arquivoHelper = new ArquivoHelper();
    private PrintWriter pwAluno;
    private Date dataPadrao = new GregorianCalendar(2009, 11, 30).getTime();
    private FuncionarioVO funcionarioVO;

    public Setransp() throws Exception {
        super();
        setIdEntidade("Setransp");
    }

    public SetranspVO gerarArquivo(SetranspVO obj, String caminhoPasta, UsuarioVO usuario) throws Exception {
        File arquivo = criarArquivosTexto(caminhoPasta, obj.getArquivo().getNome());
        criarCabecalhoArquivos(obj, usuario);
        criarRegistroAlunos(obj);
        getPwAluno().close();
//		obj.getArquivo().setArquivo(arquivoHelper.getArray(arquivo));
        obj.getArquivo().setPastaBaseArquivo(caminhoPasta);
        obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.SETRANSP_TMP);
        return obj;
    }

    private void criarRegistroAlunos(SetranspVO obj) throws Exception {
        StringBuilder linha = new StringBuilder();
//		MatriculaVO matriculaVO;
        PessoaVO alunoVO;
        for (SetranspAlunoVO setranspAlunoVO : obj.getSetranspAlunoVOs()) {
//			matriculaVO = getFacadeFactory().getMatriculaFacade().consultarAlunoPorMatricula(setranspAlunoVO.getMatriculaPeriodo().getMatricula(), null, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            alunoVO = setranspAlunoVO.getMatriculaPeriodo().getMatriculaVO().getAluno();
//			alunoVO.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
//			getFacadeFactory().getPessoaFacade().carregarDados(alunoVO);
//			getPwAluno().println(gerarRegistroAluno(linha, setranspAlunoVO, matriculaVO, alunoVO));
            getPwAluno().println(gerarRegistroAluno(linha, setranspAlunoVO, setranspAlunoVO.getMatriculaPeriodo().getMatriculaVO(), alunoVO));
            linha.setLength(0);
        }
    }

    private String gerarRegistroAluno(StringBuilder linha, SetranspAlunoVO setranspAlunoVO, MatriculaVO matriculaVO, PessoaVO alunoVO) {
        linha.append(alunoVO.getIdAlunoInep() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentos(alunoVO.getNome())) + SetranspVO.SEPARADOR_COLUNA);
        linha.append(Uteis.getData(alunoVO.getDataNasc(), "dd/MM/yyyy") + SetranspVO.SEPARADOR_COLUNA);
        linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentos(alunoVO.getNomeMae())) + SetranspVO.SEPARADOR_COLUNA);
        linha.append(matriculaVO.getMatricula() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(alunoVO.getEndereco() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(0 + SetranspVO.SEPARADOR_COLUNA);
        linha.append(0 + SetranspVO.SEPARADOR_COLUNA);
        linha.append(alunoVO.getNumero() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(alunoVO.getComplemento() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(alunoVO.getSetor() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(alunoVO.getCidade().getNome() + SetranspVO.SEPARADOR_COLUNA);
        linha.append(0 + SetranspVO.SEPARADOR_COLUNA);
        return linha.toString();
    }

    private void criarCabecalhoArquivos(SetranspVO setranspVO, UsuarioVO usuario) throws Exception {
        getPwAluno().println(
                usuario.getUnidadeEnsinoLogado().getNome() + SetranspVO.SEPARADOR_COLUNA + usuario.getUnidadeEnsinoLogado().getDiretorGeral().getPessoa().getNome() + SetranspVO.SEPARADOR_COLUNA
                + setranspVO.getUnidadeEnsino().getCodigoIES());
    }

    private File criarArquivosTexto(String caminhoPasta, String nomeArquivo) throws Exception {
        File dir = null;
        dir = new File(caminhoPasta);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        setPwAluno(getArquivoHelper().criarArquivoTexto(caminhoPasta, nomeArquivo, true));
        return new File(caminhoPasta + File.separator + nomeArquivo);
    }

    public SetranspVO criarObjetoSetranspVO(SetranspVO setranspVO, Integer unidadeEnsino, Integer curso, Integer turma, String aluno, Date dataInicio, Date dataFim, String ano, String semestre, String tipoFiltroPeriodicidade) throws Exception {
    	validarPeriodicidade(ano, semestre, tipoFiltroPeriodicidade);
    	List<MatriculaPeriodoVO> matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
        matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorUnidadeCursoTurmaAlunoEntreDatasSituacaoParaSetransp(
        		unidadeEnsino, curso, turma, aluno, dataInicio, dataFim, SituacaoMatriculaPeriodoEnum.ATIVA.getValor(), ano, semestre, tipoFiltroPeriodicidade);
        if (matriculaPeriodoVOs.isEmpty()) {
            throw new Exception("Não há alunos com esses parâmetros de consulta.");
        }
        SetranspAlunoVO setranspAlunoVO;
        for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
            setranspAlunoVO = new SetranspAlunoVO();
            setranspAlunoVO.setMatriculaPeriodo(matriculaPeriodoVO);
            setranspAlunoVO.setSetransp(setranspVO);
            setranspVO.getSetranspAlunoVOs().add(setranspAlunoVO);
        }
        return setranspVO;
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>SetranspVO</code>.
     */
    public SetranspVO novo() throws Exception {
        Setransp.incluir(getIdEntidade());
        SetranspVO obj = new SetranspVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>SetranspVO</code>. Primeiramente
     * valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>SetranspVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final SetranspVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            SetranspVO.validarDados(obj);
            Setransp.incluir(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Setransp( dataGeracao, responsavel, unidadeEnsino, arquivo ) VALUES ( ?, ?, ?, ? ) returning codigo";

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataGeracao()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getUnidadeEnsino() != null && obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getArquivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getArquivo().getCodigo().intValue());
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

        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>SetranspVO</code>. Sempre utiliza a
     * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
     * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>SetranspVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final SetranspVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            SetranspVO.validarDados(obj);
            Setransp.alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Setransp SET dataGeracao=?, responsavel=?, unidadeEnsino=?, arquivo=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataGeracao()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getUnidadeEnsino() != null && obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setInt(4, obj.getArquivo().getCodigo());
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getSetranspAlunoFacade().alterarSetranspAluno(obj.getCodigo(), obj.getSetranspAlunoVOs());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>SetranspVO</code>. Sempre localiza o registro a
     * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>SetranspVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(SetranspVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            Setransp.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Setransp WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Setransp</code> através do valor do atributo
     * <code>Integer responsavel</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>SetranspVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Setransp WHERE responsavel >= " + valorConsulta.intValue() + " ORDER BY responsavel";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Setransp</code> através do valor do atributo
     * <code>Date dataGeracao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>SetranspVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Setransp WHERE ((dataGeracao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataGeracao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataGeracao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Setransp</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>SetranspVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Setransp WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>SetranspVO</code> resultantes da consulta.
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
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>SetranspVO</code>.
     *
     * @return O objeto da classe <code>SetranspVO</code> com os dados devidamente montados.
     */
    public static SetranspVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SetranspVO obj = new SetranspVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDataGeracao(dadosSQL.getDate("dataGeracao"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
        obj.getArquivo().setCodigo(dadosSQL.getInt("arquivo"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
            return obj;
        }
        return obj;
    }

    public static void montarDadosArquivo(SetranspVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivo().getCodigo().intValue() == 0) {
            obj.setArquivo(new ArquivoVO());
            return;
        }
        obj.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivo().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosUnidadeEnsino(SetranspVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>CensoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(SetranspVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>SetranspVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public SetranspVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Setransp WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Setransp ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Setransp.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Setransp.idEntidade = idEntidade;
    }

    public PrintWriter getPwAluno() {
        return pwAluno;
    }

    public void setPwAluno(PrintWriter pwAluno) {
        this.pwAluno = pwAluno;
    }

    public ArquivoHelper getArquivoHelper() {
        return arquivoHelper;
    }

    public void setArquivoHelper(ArquivoHelper arquivoHelper) {
        this.arquivoHelper = arquivoHelper;
    }

    public FuncionarioVO getFuncionarioVO() {
        return funcionarioVO;
    }

    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }
    
    private void validarPeriodicidade(String ano, String semestre, String tipoFiltroPeriodicidade) throws Exception {
    	if (tipoFiltroPeriodicidade.equals("AN") || tipoFiltroPeriodicidade.equals("SE")) {
    		if (Uteis.isAtributoPreenchido(ano) && ano.length() < 4) {
    			throw new ConsistirException("Ano inválido! Se preenchido, o campo (ano) deve conter 4 dígitos.");
    		}
    	}
    	if (tipoFiltroPeriodicidade.equals("SE")) {
    		if (Uteis.isAtributoPreenchido(semestre) && !Uteis.isAtributoPreenchido(ano)) {
    			throw new ConsistirException("O campo (ano) deve ser informado, pois o semestre está selecionado.");
    		}
    	}
    }
}
