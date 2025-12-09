package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutComprovanteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ProcSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcSeletivoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ProcSeletivoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProcSeletivoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivo extends ControleAcesso implements ProcSeletivoInterfaceFacade {

    protected static String idEntidade;

    public ProcSeletivo() throws Exception {
        super();
        setIdEntidade("ProcSeletivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoVO</code>.
     */
    public ProcSeletivoVO novo() throws Exception {
        ProcSeletivo.incluir(getIdEntidade());
        ProcSeletivoVO obj = new ProcSeletivoVO();
        return obj;
    }

    public void inicializarProcSeletivoSemAgendamento(ProcSeletivoVO procSeletivoVO) {
        procSeletivoVO.setDataProva(null);
        procSeletivoVO.setHorarioProva(null);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
     * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProcSeletivoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProcSeletivoVO.validarDados(obj);
            validarDadosMediaMinimaAprovacao(obj);
            incluir(getIdEntidade(), true, usuarioVO);
            final String sql = "INSERT INTO ProcSeletivo( descricao, dataInicio, dataFim, dataInicioInternet, dataFimInternet, valorInscricao, dataProva, horarioProva, responsavel, "
                    + "documentaoObrigatoria, nrOpcoesCurso, requisitosGerais, nivelEducacional, mediaMinimaAprovacao, questionario, informarQtdeDiasVencimentoAposInscricao, "
                    + "qtdeDiasVencimentoAposInscricao, regimeAprovacao, quantidadeAcertosMinimosAprovacao, tipoLayoutComprovante, notaMinimaRedacao, ano, semestre, valorporacerto, campanhaGerarAgendaInscritos, tipoAvaliacaoProcessoSeletivo ,tipoProcessoSeletivo,"
                    +"tipoEnem ,tipoPortadorDiploma,uploadComprovanteEnem ,uploadComprovantePortadorDiploma,orientacaoUploadEnem,orientacaoUploadPortadorDiploma, tipoTransferencia, obrigarUploadComprovanteTransferencia, orientacaoTransferencia, realizarProvaOnline, tempoRealizacaoProvaOnline, termoAceiteProva, utilizarAutenticacaoEmail, utilizarAutenticacaoSMS , permitirAlunosMatriculadosInscreverMesmoCurso, informarDadosBancarios ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataFim()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataInicioInternet()));
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataFimInternet()));
                    sqlInserir.setDouble(6, obj.getValorInscricao().doubleValue());
                    if (obj.getDataProva() != null) {
                        sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataProva()));
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getHorarioProva() != null) {
                        sqlInserir.setString(8, obj.getHorarioProva());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    sqlInserir.setInt(9, obj.getResponsavel().getCodigo().intValue());
                    sqlInserir.setString(10, obj.getDocumentaoObrigatoria());
                    sqlInserir.setString(11, obj.getNrOpcoesCurso());
                    sqlInserir.setString(12, obj.getRequisitosGerais());
                    sqlInserir.setString(13, obj.getNivelEducacional());
                    sqlInserir.setDouble(14, obj.getMediaMinimaAprovacao().doubleValue());
                    sqlInserir.setInt(15, obj.getQuestionario().getCodigo().intValue());
                    sqlInserir.setBoolean(16, obj.getInformarQtdeDiasVencimentoAposInscricao());
                    if (obj.getQtdeDiasVencimentoAposInscricao() != null) {
                        sqlInserir.setInt(17, obj.getQtdeDiasVencimentoAposInscricao());
                    } else {
                        sqlInserir.setNull(17, 0);
                    }
                    sqlInserir.setString(18, obj.getRegimeAprovacao());
                    sqlInserir.setInt(19, obj.getQuantidadeAcertosMinimosAprovacao());
                    sqlInserir.setString(20, obj.getTipoLayoutComprovante().name());
                    sqlInserir.setDouble(21, obj.getNotaMinimaRedacao());
                    sqlInserir.setString(22, obj.getAno());
                    sqlInserir.setString(23, obj.getSemestre());
                    sqlInserir.setDouble(24, obj.getValorPorAcerto());
                    if (!obj.getCampanhaGerarAgendaInscritos().getCodigo().equals(0)) {
                        sqlInserir.setInt(25, obj.getCampanhaGerarAgendaInscritos().getCodigo());
                    } else {
                        sqlInserir.setNull(25, 0);
                    }
                    sqlInserir.setString(26, obj.getTipoAvaliacaoProcessoSeletivo().name().toString());
                    sqlInserir.setBoolean(27, obj.getTipoProcessoSeletivo());
                    sqlInserir.setBoolean(28, obj.getTipoEnem());
                    sqlInserir.setBoolean(29, obj.getTipoPortadorDiploma());
                    sqlInserir.setBoolean(30, obj.getUploadComprovanteEnem());
                    sqlInserir.setBoolean(31, obj.getUploadComprovantePortadorDiploma());
                    sqlInserir.setString(32, obj.getOrientacaoUploadEnem());
                    sqlInserir.setString(33, obj.getOrientacaoUploadPortadorDiploma()); 
                    sqlInserir.setBoolean(34, obj.getTipoTransferencia());
                    sqlInserir.setBoolean(35, obj.getObrigarUploadComprovanteTransferencia());
                    sqlInserir.setString(36, obj.getOrientacaoTransferencia());
                    sqlInserir.setBoolean(37, obj.getRealizarProvaOnline());
                    sqlInserir.setInt(38, obj.getTempoRealizacaoProvaOnline());
                    sqlInserir.setString(39, obj.getTermoAceiteProva());
                    sqlInserir.setBoolean(40, obj.getUtilizarAutenticacaoEmail());
                    sqlInserir.setBoolean(41, obj.getUtilizarAutenticacaoSMS());
                    sqlInserir.setBoolean(42, obj.getPermitirAlunosMatriculadosInscreverMesmoCurso());
                    sqlInserir.setBoolean(43, obj.getInformarDadosBancarios());
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
//            getFacadeFactory().getProcSeletivoDisciplinasProcSeletivoFacade().incluirProcSeletivoDisciplinasProcSeletivos(obj.getCodigo(), obj.getProcSeletivoDisciplinasProcSeletivoVOs(), usuarioVO);
//            getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().incluirProcSeletivoUnidadeEnsinos(obj, usuarioVO);
//            getFacadeFactory().getItemProcSeletivoDataProvaFacade().incluirItemProcSeletivoDataProva(obj.getCodigo(), obj.getItemProcSeletivoDataProvaVOs(), usuarioVO);
//            getFacadeFactory().getPeriodoChamadaProcSeletivoFacade().incluirPeriodoChamadaProcSeletivo(obj.getCodigo(), obj.getPeriodoChamadaProcSeletivoVOs(), usuarioVO);
        } catch (Exception e) {
            obj.setNovoObj(true);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser
     * alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
     * através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProcSeletivoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProcSeletivoVO.validarDados(obj);
            validarDadosMediaMinimaAprovacao(obj);
            alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE ProcSeletivo set descricao=?, dataInicio=?, dataFim=?, dataInicioInternet=?, dataFimInternet=?, valorInscricao=?, dataProva=?, "
                    + "horarioProva=?, responsavel=?, documentaoObrigatoria=?, nrOpcoesCurso=?, requisitosGerais=?, nivelEducacional=?, mediaMinimaAprovacao=?, "
                    + "questionario=?, informarQtdeDiasVencimentoAposInscricao=?, qtdeDiasVencimentoAposInscricao=?, regimeAprovacao=?, quantidadeAcertosMinimosAprovacao=?, tipoLayoutComprovante=?, notaMinimaRedacao=?, ano = ?, semestre = ?, valorporacerto = ?, campanhaGerarAgendaInscritos=?, tipoAvaliacaoProcessoSeletivo=? ,tipoProcessoSeletivo=? ,tipoEnem=? ,tipoPortadorDiploma=?,uploadComprovanteEnem=? ,uploadComprovantePortadorDiploma=?,orientacaoUploadEnem=?,orientacaoUploadPortadorDiploma=?, tipoTransferencia=?, obrigarUploadComprovanteTransferencia=?, orientacaoTransferencia=?, realizarProvaOnline=?, tempoRealizacaoProvaOnline=?, termoAceiteProva=?, utilizarAutenticacaoEmail=?, utilizarAutenticacaoSMS=? , permitirAlunosMatriculadosInscreverMesmoCurso=?, informarDadosBancarios=?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataFim()));
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataInicioInternet()));
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataFimInternet()));
                    sqlAlterar.setDouble(6, obj.getValorInscricao().doubleValue());
                    sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataProva()));
                    sqlAlterar.setString(8, obj.getHorarioProva());
                    sqlAlterar.setInt(9, obj.getResponsavel().getCodigo().intValue());
                    sqlAlterar.setString(10, obj.getDocumentaoObrigatoria());
                    sqlAlterar.setString(11, obj.getNrOpcoesCurso());
                    sqlAlterar.setString(12, obj.getRequisitosGerais());
                    sqlAlterar.setString(13, obj.getNivelEducacional());
                    sqlAlterar.setDouble(14, obj.getMediaMinimaAprovacao().doubleValue());
                    sqlAlterar.setInt(15, obj.getQuestionario().getCodigo().intValue());
                    sqlAlterar.setBoolean(16, obj.getInformarQtdeDiasVencimentoAposInscricao());
                    if (obj.getQtdeDiasVencimentoAposInscricao() != null) {
                        sqlAlterar.setInt(17, obj.getQtdeDiasVencimentoAposInscricao());
                    } else {
                        sqlAlterar.setNull(17, 0);
                    }
                    sqlAlterar.setString(18, obj.getRegimeAprovacao());
                    sqlAlterar.setInt(19, obj.getQuantidadeAcertosMinimosAprovacao());
                    sqlAlterar.setString(20, obj.getTipoLayoutComprovante().name());
                    sqlAlterar.setDouble(21, obj.getNotaMinimaRedacao());
                    sqlAlterar.setString(22, obj.getAno());
                    sqlAlterar.setString(23, obj.getSemestre());
                    sqlAlterar.setDouble(24, obj.getValorPorAcerto());
                    if (!obj.getCampanhaGerarAgendaInscritos().getCodigo().equals(0)) {
                        sqlAlterar.setInt(25, obj.getCampanhaGerarAgendaInscritos().getCodigo());
                    } else {
                        sqlAlterar.setNull(25, 0);
                    }
                    sqlAlterar.setString(26, obj.getTipoAvaliacaoProcessoSeletivo().name().toString());
                   
                    sqlAlterar.setBoolean(27, obj.getTipoProcessoSeletivo());
                    sqlAlterar.setBoolean(28, obj.getTipoEnem());
                    sqlAlterar.setBoolean(29, obj.getTipoPortadorDiploma());
                    sqlAlterar.setBoolean(30, obj.getUploadComprovanteEnem());
                    sqlAlterar.setBoolean(31, obj.getUploadComprovantePortadorDiploma());
                    sqlAlterar.setString(32, obj.getOrientacaoUploadEnem());
                    sqlAlterar.setString(33, obj.getOrientacaoUploadPortadorDiploma()); 
                    sqlAlterar.setBoolean(34, obj.getTipoTransferencia());
                    sqlAlterar.setBoolean(35, obj.getObrigarUploadComprovanteTransferencia());
                    sqlAlterar.setString(36, obj.getOrientacaoTransferencia()); 
                    sqlAlterar.setBoolean(37, obj.getRealizarProvaOnline());
                    sqlAlterar.setInt(38, obj.getTempoRealizacaoProvaOnline());
                    sqlAlterar.setString(39, obj.getTermoAceiteProva());
                    sqlAlterar.setBoolean(40, obj.getUtilizarAutenticacaoEmail());
                    sqlAlterar.setBoolean(41, obj.getUtilizarAutenticacaoSMS());
                    sqlAlterar.setBoolean(42, obj.getPermitirAlunosMatriculadosInscreverMesmoCurso());
                    sqlAlterar.setBoolean(43, obj.getInformarDadosBancarios());
                    sqlAlterar.setInt(44, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
//            getFacadeFactory().getProcSeletivoDisciplinasProcSeletivoFacade().alterarProcSeletivoDisciplinasProcSeletivos(obj.getCodigo(), obj.getProcSeletivoDisciplinasProcSeletivoVOs(), usuarioVO);
//            getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().alterarProcSeletivoUnidadeEnsinos(obj, usuarioVO);
//            getFacadeFactory().getItemProcSeletivoDataProvaFacade().alterarItemProcSeletivoDataProva(obj.getCodigo(), obj.getItemProcSeletivoDataProvaVOs(), usuarioVO);
//            getFacadeFactory().getPeriodoChamadaProcSeletivoFacade().alterarPeriodoChamadaProcSeletivo(obj.getCodigo(), obj.getPeriodoChamadaProcSeletivoVOs(), usuarioVO);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProcSeletivoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void excluir(ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
//        try {
//            ProcSeletivo.excluir(getIdEntidade(), true, usuarioVO);
//            String sql = "DELETE FROM ProcSeletivo WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
//            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
//            getFacadeFactory().getProcSeletivoDisciplinasProcSeletivoFacade().excluirProcSeletivoDisciplinasProcSeletivos(obj.getCodigo());
//            getFacadeFactory().getItemProcSeletivoDataProvaFacade().excluirItemProcSeletivoDataProva(obj.getCodigo(), usuarioVO);
//            Iterator<ProcSeletivoUnidadeEnsinoVO> i = obj.getProcSeletivoUnidadeEnsinoVOs().iterator();
//            while (i.hasNext()) {
//                ProcSeletivoUnidadeEnsinoVO objs = (ProcSeletivoUnidadeEnsinoVO) i.next();
//                getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().excluir(objs, usuarioVO);
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//    }
    
    public ProcSeletivoVO consultarUltimoProcessoSeletivo(int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT codigo, descricao FROM ProcSeletivo ORDER BY codigo DESC LIMIT 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        ProcSeletivoVO obj = new ProcSeletivoVO();
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.setDescricao(tabelaResultado.getString("descricao"));
        return obj;
    }
    
    public List<ProcSeletivoVO> consultarUltimosProcessosSeletivos(int quantidade, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT codigo, descricao, datainicio, datafim FROM ProcSeletivo ORDER BY datainicio desc, codigo DESC LIMIT " + quantidade;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
           return new ArrayList<ProcSeletivoVO>();
        }
        List<ProcSeletivoVO> vetResultado = new ArrayList<ProcSeletivoVO>();
        while (tabelaResultado.next()) {
        	ProcSeletivoVO obj = new ProcSeletivoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setDescricao(tabelaResultado.getString("descricao"));
            obj.setDataInicio(tabelaResultado.getDate("datainicio"));
            obj.setDataFim(tabelaResultado.getDate("datafim"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivo</code> através do valor do atributo <code>Date dataProva</code>. Retorna os objetos com valores pertecentes ao período informado por
     * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProcSeletivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ProcSeletivoVO> consultarPorUnidadeEnsinoUnidadeEnsinoCurso(Integer unidadeEnsino, Integer unidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivo.*  From ProcSeletivo, ProcSeletivoCurso, ProcSeletivoUnidadeEnsino " + "where ProcSeletivoCurso.unidadeEnsinoCurso = " + unidadeEnsinoCurso.intValue()
                + " and ProcSeletivoUnidadeEnsino.unidadeEnsino = " + unidadeEnsino.intValue() + " " + "and ProcSeletivoCurso.procSeletivoUnidadeEnsino = procSeletivoUnidadeEnsino.codigo "
                + "and ProcSeletivoUnidadeEnsino.procSeletivo = procSeletivo.codigo ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<ProcSeletivoVO> consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcSeletivo.*  From ProcSeletivo, ProcSeletivoUnidadeEnsino " + "where "
                + " ProcSeletivoUnidadeEnsino.unidadeEnsino = " + unidadeEnsino.intValue()
                + "and ProcSeletivoUnidadeEnsino.procSeletivo = procSeletivo.codigo order by codigo desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<ProcSeletivoVO> consultarPorUnidadeEnsinoAptoInscricao(Integer unidadeEnsino, boolean visaoCandidato,  boolean controlarAcesso,Integer codigoCurso, Integer codigoTurno , String tipoProcSeletivo , int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sql = new StringBuilder("SELECT distinct procseletivo.* from    procseletivo ");  
    	sql.append(" inner join ProcSeletivoUnidadeEnsino on  ProcSeletivoUnidadeEnsino.procSeletivo = procSeletivo.codigo ");
    	sql.append(" inner join itemprocseletivodataprova on  itemprocseletivodataprova.procSeletivo = procSeletivo.codigo ");
    	sql.append(" inner join UnidadeEnsinoCurso  on  UnidadeEnsinoCurso.unidadeensino = ProcSeletivoUnidadeEnsino.unidadeEnsino");
    	sql.append(" inner join curso c on c.codigo = UnidadeEnsinoCurso.curso");
    	sql.append(" inner join turno t on t.codigo = UnidadeEnsinoCurso.turno");
    	sql.append(" where ProcSeletivoUnidadeEnsino.unidadeEnsino  =  ").append(unidadeEnsino);
    	sql.append(" and itemprocseletivodataprova.dataprova >= current_timestamp ");    	
    	sql.append(" and ((itemprocseletivodataprova.datainicioinscricao <= current_timestamp ");
    	sql.append(" and itemprocseletivodataprova.dataterminoinscricao >= current_timestamp) ");    	
    	if(!visaoCandidato){
    		sql.append(" or (procseletivo.datainicio <= current_timestamp ");
    		sql.append(" and procseletivo.datafim >= current_timestamp )");
    	}
    	sql.append(" )");    	
    	 if(Uteis.isAtributoPreenchido(codigoCurso)) {        	
    		 sql.append(" and c.codigo = " ).append(codigoCurso);
         } 
         if(Uteis.isAtributoPreenchido(codigoTurno)) {        	
        	 sql.append(" and t.codigo = " ).append(codigoTurno);
         } 
         if(Uteis.isAtributoPreenchido(tipoProcSeletivo)) { 
         	if(tipoProcSeletivo.equals("TODOS")) {
         		sql.append(" and (procseletivo.tipoenem = true");
         		sql.append(" or procseletivo.tipoprocessoseletivo =true");
         		sql.append(" or procseletivo.tipoportadordiploma = true");
                  sql.append(" or procseletivo.tipotransferencia = true )");      
         	}else if(FormaIngresso.valueOf(tipoProcSeletivo).equals(FormaIngresso.ENEM)) {
         		
         		sql.append(" and procseletivo.tipoenem");
         	}else if(FormaIngresso.valueOf(tipoProcSeletivo).equals(FormaIngresso.PROCESSO_SELETIVO)) {
         		sql.append(" and procseletivo.tipoprocessoseletivo");
         		
         	}else if(FormaIngresso.valueOf(tipoProcSeletivo).equals(FormaIngresso.PORTADOR_DE_DIPLOMA)) {
         		
         		sql.append(" and procseletivo.tipoportadordiploma");
         	}else if(tipoProcSeletivo.equals("TRANSFERENCIA")) {        		
         		sql.append(" and procseletivo.tipotransferencia ");      
         	}
        }
    	sql.append(" order by procseletivo.dataInicio ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<ProcSeletivoVO> consultarPorUnidadeEnsinoLogado(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct ProcSeletivo.* FROM ProcSeletivo "
                + "inner join procSeletivoUnidadeEnsino as psue on psue.procSeletivo = procSeletivo.codigo ";
        if (unidadeEnsino != 0) {
            sqlStr = sqlStr.concat("WHERE psue.unidadeEnsino = " + unidadeEnsino.intValue() + " ");
        }
        sqlStr = sqlStr.concat("order by procSeletivo.descricao ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<ProcSeletivoVO> consultarPorDescricaoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sb = new StringBuilder();
    	if (valorConsulta.contains("'")) {
    		valorConsulta = valorConsulta.replaceAll("'", "''");
		}    	
    	sb.append("SELECT distinct ProcSeletivo.* FROM ProcSeletivo ");
    	sb.append(" left join procSeletivoUnidadeEnsino as psue on psue.procSeletivo = procSeletivo.codigo ");
    	sb.append(" where ProcSeletivo.descricao ilike('" + valorConsulta + "%') ");
    	if (unidadeEnsino != 0) {
    		sb.append(" and psue.unidadeEnsino = " + unidadeEnsino.intValue() + " ");
    	}
    	sb.append("order by procSeletivo.descricao ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<ProcSeletivoVO> consultarProcessoSeletivoAntesDataProvaPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct ProcSeletivo.* FROM ProcSeletivo ");
        sqlStr.append(" where procSeletivo.codigo in (select distinct data.procSeletivo from ItemProcSeletivoDataProva data where data.procSeletivo = procSeletivo.codigo and data.dataProva::DATE >= current_date )");        
        if (unidadeEnsino != 0) {
            sqlStr.append(" and procSeletivo.codigo in (select distinct  psue.procSeletivo from procSeletivoUnidadeEnsino as psue where psue.procSeletivo = procSeletivo.codigo  and psue.unidadeEnsino  = " + unidadeEnsino.intValue() + " ) ");
        }
        sqlStr.append(" order by procSeletivo.codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<ProcSeletivoVO> consultarProcessoSeletivoAposDataProvaPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct ProcSeletivo.* FROM ProcSeletivo ");
        sqlStr.append(" where procSeletivo.codigo in (select distinct data.procSeletivo from ItemProcSeletivoDataProva data where data.procSeletivo = procSeletivo.codigo and data.dataProva::DATE <= (current_date+30) )");
        if (unidadeEnsino != 0) {
            sqlStr.append(" and procSeletivo.codigo in (select distinct  psue.procSeletivo from procSeletivoUnidadeEnsino as psue where psue.procSeletivo = procSeletivo.codigo  and psue.unidadeEnsino  = " + unidadeEnsino.intValue() + " ) ");
            
        }
        sqlStr.append(" order by procSeletivo.codigo desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

   
    
    public List<ProcSeletivoVO> consultarPorDataProvaUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        //String sqlStr = "SELECT * FROM ProcSeletivo WHERE ((dataProva >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataProva <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataProva";
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" SELECT distinct ProcSeletivo.* FROM itemProcSeletivoDataProva ");
        sqlStr.append(" inner join procseletivo on procseletivo.codigo = itemProcSeletivoDataProva.procseletivo ");
        sqlStr.append(" left join procSeletivoUnidadeEnsino as psue on psue.procSeletivo = procSeletivo.codigo ");
        sqlStr.append(" WHERE (itemProcSeletivoDataProva.dataProva::DATE >= '").append(Uteis.getDataJDBC(prmIni)).append("' and itemProcSeletivoDataProva.dataProva::DATE <= '").append(Uteis.getDataJDBC(prmFim)).append("') ");
        if (unidadeEnsino != 0) {
        	sqlStr.append(" and psue.unidadeEnsino = " + unidadeEnsino.intValue() + " ");
    	}
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

  
    
    @Override
    public List<ProcSeletivoVO> consultarPorDataFimInternetUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" SELECT * FROM ProcSeletivo ");
    	sqlStr.append(" LEFT JOIN procSeletivoUnidadeEnsino AS psue ON psue.procSeletivo = procSeletivo.codigo ");
    	sqlStr.append(" WHERE ((dataFimInternet >= '");
    	sqlStr.append(Uteis.getDataJDBC(prmIni));
    	sqlStr.append("') and (dataFimInternet <= '");
    	sqlStr.append(Uteis.getDataJDBC(prmFim)).append("')) ");
    	if (!unidadeEnsino.equals(0)) {
    		sqlStr.append(" AND psue.unidadeEnsino = ").append(unidadeEnsino);
    	}
    	sqlStr.append(" ORDER BY dataInicioInternet");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    
    
    @Override
    public List<ProcSeletivoVO> consultarPorDataInicioInternetUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" SELECT * FROM ProcSeletivo ");
    	sqlStr.append(" LEFT JOIN procSeletivoUnidadeEnsino AS psue ON psue.procSeletivo = procSeletivo.codigo ");
    	sqlStr.append(" WHERE ((dataInicioInternet >= '");
    	sqlStr.append(Uteis.getDataJDBC(prmIni));
    	sqlStr.append("') and (dataInicioInternet <= '");
    	sqlStr.append(Uteis.getDataJDBC(prmFim)).append("')) ");
    	if (!unidadeEnsino.equals(0)) {
    		sqlStr.append(" AND psue.unidadeEnsino = ").append(unidadeEnsino);
    	}
    	sqlStr.append(" ORDER BY dataInicioInternet");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

   

    public List<ProcSeletivoVO> consultarPorDataFimUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT ProcSeletivo.* FROM ProcSeletivo ";
    	sqlStr = sqlStr.concat(" left join procSeletivoUnidadeEnsino as psue on psue.procSeletivo = procSeletivo.codigo ");
    	sqlStr = sqlStr.concat(" WHERE dataFim BETWEEN '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(prmIni)) + "' and '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(prmFim)) + "' ");
    	if (!unidadeEnsino.equals(0)) {
    		sqlStr = sqlStr.concat(" AND psue.unidadeEnsino = "+ unidadeEnsino +" ");
    	}
    	sqlStr = sqlStr.concat(" ORDER BY dataFim");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
   
    public List<ProcSeletivoVO> consultarPorDataInicioUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT ProcSeletivo.* FROM ProcSeletivo ";
    	sqlStr = sqlStr.concat(" left join procSeletivoUnidadeEnsino as psue on psue.procSeletivo = procSeletivo.codigo ");
    	sqlStr = sqlStr.concat(" WHERE dataInicio BETWEEN '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(prmIni)) + "' and '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(prmFim)) + "' ");
    	if (!unidadeEnsino.equals(0)) {
    		sqlStr = sqlStr.concat(" AND psue.unidadeEnsino = "+ unidadeEnsino +" ");
    	}
    	sqlStr = sqlStr.concat(" ORDER BY dataInicio");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
   

    public ProcSeletivoVO consultarCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProcSeletivo WHERE codigo = " + codigo.intValue() + " ORDER BY codigo";
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!dadosSQL.next()) {
            return new ProcSeletivoVO();
        }
        return montarDados(dadosSQL, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivo</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProcSeletivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ProcSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProcSeletivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>ProcSeletivoVO</code> resultantes da consulta.
     */
    public  List<ProcSeletivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ProcSeletivoVO> vetResultado = new ArrayList<ProcSeletivoVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ProcSeletivoVO</code>.
     *
     * @return O objeto da classe <code>ProcSeletivoVO</code> com os dados devidamente montados.
     */
    public  ProcSeletivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcSeletivoVO obj = new ProcSeletivoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setAno(dadosSQL.getString("ano"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFim(dadosSQL.getDate("dataFim"));
        obj.setDataProva(dadosSQL.getDate("dataProva"));
        obj.setInformarQtdeDiasVencimentoAposInscricao(dadosSQL.getBoolean("informarQtdeDiasVencimentoAposInscricao"));
        obj.setPermitirAlunosMatriculadosInscreverMesmoCurso(dadosSQL.getBoolean("permitirAlunosMatriculadosInscreverMesmoCurso"));
        obj.setQtdeDiasVencimentoAposInscricao(dadosSQL.getInt("qtdeDiasVencimentoAposInscricao"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAvaliacaoProcessoSeletivo"))) {
			obj.setTipoAvaliacaoProcessoSeletivo(TipoAvaliacaoProcessoSeletivoEnum.valueOf(dadosSQL.getString("tipoAvaliacaoProcessoSeletivo")));
		}
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        
        obj.getCampanhaGerarAgendaInscritos().setCodigo(dadosSQL.getInt("campanhaGerarAgendaInscritos"));
        obj.setDataInicioInternet(dadosSQL.getDate("dataInicioInternet"));
        obj.setDataFimInternet(dadosSQL.getDate("dataFimInternet"));
        obj.setValorInscricao(new Double(dadosSQL.getDouble("valorInscricao")));

        obj.setHorarioProva(dadosSQL.getString("horarioProva"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setDocumentaoObrigatoria(dadosSQL.getString("documentaoObrigatoria"));
        obj.setNrOpcoesCurso(dadosSQL.getString("nrOpcoesCurso"));
        obj.setRequisitosGerais(dadosSQL.getString("requisitosGerais"));
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setMediaMinimaAprovacao(new Double(dadosSQL.getDouble("mediaMinimaAprovacao")));
        obj.getQuestionario().setCodigo(new Integer(dadosSQL.getInt("questionario")));
        obj.setRegimeAprovacao(dadosSQL.getString("regimeAprovacao"));
        obj.setQuantidadeAcertosMinimosAprovacao(dadosSQL.getInt("quantidadeAcertosMinimosAprovacao"));
        if(dadosSQL.getString("tipoLayoutComprovante") == null || dadosSQL.getString("tipoLayoutComprovante").trim().isEmpty()){
        	obj.setTipoLayoutComprovante(TipoLayoutComprovanteEnum.LAYOUT_1);
        }else{
        	obj.setTipoLayoutComprovante(TipoLayoutComprovanteEnum.valueOf(dadosSQL.getString("tipoLayoutComprovante")));
        }
        obj.setNotaMinimaRedacao(dadosSQL.getDouble("notaMinimaRedacao"));
        obj.setValorPorAcerto(dadosSQL.getDouble("valorporacerto"));
        obj.setTipoProcessoSeletivo(dadosSQL.getBoolean("tipoProcessoSeletivo"));
        obj.setTipoEnem(dadosSQL.getBoolean("tipoEnem"));
        obj.setTipoPortadorDiploma(dadosSQL.getBoolean("tipoPortadorDiploma"));
        obj.setUploadComprovanteEnem(dadosSQL.getBoolean("uploadComprovanteEnem"));
        obj.setUploadComprovantePortadorDiploma(dadosSQL.getBoolean("uploadComprovantePortadorDiploma"));
        obj.setOrientacaoUploadEnem(dadosSQL.getString("orientacaoUploadEnem"));
        obj.setOrientacaoUploadPortadorDiploma(dadosSQL.getString("orientacaoUploadPortadorDiploma"));
        obj.setTipoTransferencia(dadosSQL.getBoolean("tipoTransferencia"));
        obj.setObrigarUploadComprovanteTransferencia(dadosSQL.getBoolean("obrigarUploadComprovanteTransferencia"));
        obj.setOrientacaoTransferencia(dadosSQL.getString("orientacaoTransferencia"));
        obj.setRealizarProvaOnline(dadosSQL.getBoolean("realizarProvaOnline"));
        obj.setTempoRealizacaoProvaOnline(dadosSQL.getInt("tempoRealizacaoProvaOnline"));
        obj.setTermoAceiteProva(dadosSQL.getString("termoAceiteProva"));
        obj.setUtilizarAutenticacaoEmail(dadosSQL.getBoolean("utilizarAutenticacaoEmail"));
        obj.setUtilizarAutenticacaoSMS(dadosSQL.getBoolean("utilizarAutenticacaoSMS"));
        obj.setInformarDadosBancarios(dadosSQL.getBoolean("informarDadosBancarios"));
        
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
//        obj.setProcSeletivoDisciplinasProcSeletivoVOs(ProcSeletivoDisciplinasProcSeletivo.consultarProcSeletivoDisciplinasProcSeletivos(obj.getCodigo(), usuario));
//        obj.setProcSeletivoUnidadeEnsinoVOs(ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(obj.getCodigo(), nivelMontarDados, usuario));
//        Ordenacao.ordenarLista(obj.getProcSeletivoUnidadeEnsinoVOs(), "ordenacao");
//        obj.setItemProcSeletivoDataProvaVOs(ItemProcSeletivoDataProva.consultarItemProcSeletivoDataProva(obj.getCodigo(), nivelMontarDados));
//        obj.setPeriodoChamadaProcSeletivoVOs(PeriodoChamadaProcSeletivo.consultarPeriodoChamadaProcSeletivoPorCodigoProcessoSeletivo(obj.getCodigo(), false, nivelMontarDados,usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosQuestionario(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ProcSeletivoVO</code>. Faz uso da chave primária da classe
     * <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosResponsavel(ProcSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    public  void montarDadosQuestionario(ProcSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getQuestionario().getCodigo().intValue() == 0) {
            obj.setQuestionario(new QuestionarioVO());
            return;
        }
        obj.setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getQuestionario().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProcSeletivoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProcSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProcSeletivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProcSeletivo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ProcSeletivo.idEntidade = idEntidade;
    }

//    public void inicializarDadosDisciplinaProcSeletivo(ProcSeletivoVO procSeletivoVO) {
//        try {
//            procSeletivoVO.setProcSeletivoDisciplinasProcSeletivoVOs(new ArrayList());
//
//            List disciplinasPadrao = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorNome("", false, null);
//            Iterator i = disciplinasPadrao.iterator();
//            while (i.hasNext()) {
//                DisciplinasProcSeletivoVO disciplinasProcSeletivoVO = (DisciplinasProcSeletivoVO) i.next();
//                ProcSeletivoDisciplinasProcSeletivoVO procSeletivoDisciplinasProcSeletivoVO = new ProcSeletivoDisciplinasProcSeletivoVO();
//                procSeletivoDisciplinasProcSeletivoVO.setDisciplinasProcSeletivo(disciplinasProcSeletivoVO);
//                procSeletivoVO.adicionarObjProcSeletivoDisciplinasProcSeletivoVOs(procSeletivoDisciplinasProcSeletivoVO);
//            }
//        } catch (Exception e) {
//            procSeletivoVO.setProcSeletivoDisciplinasProcSeletivoVOs(new ArrayList());
//        }
//    }
//
//    public String consultarDisciplinasProcSeletivoString(ProcSeletivoVO procSeletivoVO) {
//        if (procSeletivoVO.getProcSeletivoDisciplinasProcSeletivoVOs() == null || procSeletivoVO.getProcSeletivoDisciplinasProcSeletivoVOs().isEmpty()) {
//            inicializarDadosDisciplinaProcSeletivo(procSeletivoVO);
//        }
//        String disciplinas = null;
//        for (ProcSeletivoDisciplinasProcSeletivoVO proc : procSeletivoVO.getProcSeletivoDisciplinasProcSeletivoVOs()) {
//            if (Uteis.isAtributoPreenchido(disciplinas)) {
//                disciplinas += ", " + proc.getDisciplinasProcSeletivo().getNome();
//            } else {
//                disciplinas = proc.getDisciplinasProcSeletivo().getNome();
//            }
//        }
//        return disciplinas;
//    }

    public void validarDadosMediaMinimaAprovacao(ProcSeletivoVO procSeletivoVO) throws Exception {
//        if (procSeletivoVO.getMediaMinimaAprovacao() > 10.00) {
//            throw new Exception("A média mínima de aprovação não pode ser maior que 10. ");
//        }
        
    }
    
 
    public StringBuilder SQLrealizarClassificacaoCandidatoProcessoSeletivo(Integer processoSeletivo, Integer unidadeEnsinoCurso) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT ROW_NUMBER () OVER ( PARTITION BY case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, procseletivo.codigo, unidadeensinocurso.unidadeensino, curso.codigo, turno.codigo "); 
		sqlStr.append(" ORDER BY CASE WHEN (procseletivo.regimeaprovacao = 'quantidadeAcertosRedacao') THEN (medianotasprocseletivo + notaredacao) ELSE medianotasprocseletivo END DESC,");
		sqlStr.append("  notaredacao DESC,  ARRAY( ");
		sqlStr.append("  SELECT rank FROM (SELECT RANK() OVER( PARTITION BY case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, ins.procseletivo, uec.unidadeEnsino, uec.curso, uec.turno,  ");
		sqlStr.append("  dgdps.ordemcriteriodesempate ORDER BY  uec.unidadeensino, uec.curso, uec.turno ,  dgdps.ordemcriteriodesempate, rdps.nota DESC ),  ");
		sqlStr.append("  ins.codigo as inscricao, uec.unidadeEnsino, uec.curso, uec.turno,  rdps.disciplinaprocseletivo,  dgdps.ordemcriteriodesempate,  rdps.nota ");
		sqlStr.append(" FROM resultadoprocessoseletivo   as rps INNER JOIN inscricao as ins ON ins.codigo = rps.inscricao  ");
		sqlStr.append("  INNER JOIN unidadeensinocurso uec ON case when rps.resultadoprimeiraopcao = 'AP' then  ins.cursoopcao1 else   ");
		sqlStr.append("  CASE WHEN rps.resultadosegundaopcao = 'AP' then ins.cursoopcao2 else ins.cursoopcao3 end end  = uec.codigo  and uec.codigo =  ").append(unidadeEnsinoCurso);
		sqlStr.append("  INNER JOIN procseletivounidadeensino psue ON psue.procseletivo = ins.procseletivo   AND uec.unidadeensino = psue.unidadeEnsino    ");
		sqlStr.append("  INNER JOIN procseletivocurso psc ON psc.unidadeensinocurso = uec.codigo   and psc.procseletivounidadeensino = psue.codigo    ");
		sqlStr.append("  INNER JOIN grupodisciplinaprocseletivo gdps on gdps.codigo = psc.grupodisciplinaprocseletivo ");
		sqlStr.append("  INNER JOIN disciplinasgrupodisciplinaprocseletivo dgdps ON dgdps.grupodisciplinaprocseletivo = gdps.codigo ");
		sqlStr.append("  INNER JOIN resultadodisciplinaprocseletivo rdps ON rdps.resultadoprocessoseletivo = rps.codigo ");
		sqlStr.append("  AND rdps.disciplinaprocseletivo = dgdps.disciplinasprocseletivo ");
		sqlStr.append(" WHERE ins.procseletivo =  ").append(processoSeletivo);
		sqlStr.append(" AND (rps.resultadoprimeiraopcao = 'AP' OR rps.resultadosegundaopcao = 'AP' OR rps.resultadoterceiraopcao = 'AP')  ");
		sqlStr.append(" AND CASE WHEN (dgdps.ordemcriteriodesempate IS NULL OR dgdps.ordemcriteriodesempate = '') THEN 0 ELSE dgdps.ordemcriteriodesempate::INT END > 0) AS t WHERE t.inscricao = inscricao.codigo  ");
		sqlStr.append(" ORDER BY ordemcriteriodesempate ,RANK), datanasc ) AS classificacao, ");
		return sqlStr;
	}
	
	public List<ProcSeletivoVO> consultaRapidaComboBoxPorDescricaoUnidadeEnsino(String descricao, Integer unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select procSeletivo.codigo, procSeletivo.descricao from procSeletivo ");
		sb.append(" inner join procSeletivoUnidadeEnsino on procSeletivoUnidadeEnsino.procSeletivo = procSeletivo.codigo ");
		sb.append(" where 1=1 ");
		if (!descricao.equals("")) {
			sb.append(" and ProcSeletivo.descricao ilike('" + descricao + "%') ");
		}
		if (!unidadeEnsino.equals(0)) {
			sb.append(" and procSeletivoUnidadeEnsino.unidadeEnsino = ").append(unidadeEnsino);
		}
		sb.append("order by codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ProcSeletivoVO> listaProcSeletivoVos = null;
		while (tabelaResultado.next()) {
			if (listaProcSeletivoVos == null) {
				listaProcSeletivoVos = new ArrayList<ProcSeletivoVO>(0);
			}
			ProcSeletivoVO obj = new ProcSeletivoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			listaProcSeletivoVos.add(obj);
			
		}
		return listaProcSeletivoVos;
	}
	
	public List<ProcSeletivoVO> consultaRapidaComboBoxProcessoSeletivoFaltandoLista(List<ProcSeletivoVO> procSeletivoVOs, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao from procSeletivo ");
		sb.append(" where 1=1 ");
		if (!procSeletivoVOs.isEmpty()) {
			sb.append("and procSeletivo.codigo in (");
			for (ProcSeletivoVO procSeletivoVO : procSeletivoVOs) {
				sb.append(procSeletivoVO.getCodigo()).append(", ");
			}
			sb.append("0) ");
		}
		sb.append("ORDER BY procSeletivo.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ProcSeletivoVO> listaProcSeletivoVos = null;
		while (tabelaResultado.next()) {
			if (listaProcSeletivoVos == null) {
				listaProcSeletivoVos = new ArrayList<ProcSeletivoVO>(0);
			}
			ProcSeletivoVO obj = new ProcSeletivoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			listaProcSeletivoVos.add(obj);

		}
		return listaProcSeletivoVos;
	}
    
	@Override
	public List<ProcSeletivoVO> consultarPorUnidadeEnsinoUltimosProcessosSeletivos(Integer unidadeEnsino, Integer quantidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ProcSeletivo.* From ProcSeletivo, ProcSeletivoUnidadeEnsino where ProcSeletivoUnidadeEnsino.unidadeEnsino = ").append(unidadeEnsino);
        sb.append(" and ProcSeletivoUnidadeEnsino.procSeletivo = procSeletivo.codigo order by codigo desc limit ").append(quantidade);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
	
	@Override
	public List<ProcSeletivoVO> consultarPorUnidadeEnsinoCursoTurnoTipoProcessoSeletivo(Integer unidadeEnsino , Integer curso , Integer turno ,String tipoProcessoSeletivo , boolean controlarAcesso  ,int nivelMontarDados, UsuarioVO usuario)  throws Exception{
		consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        
        sb.append("select  procs.* , quest.* from procseletivo procs" );
        sb.append(" inner join procseletivounidadeensino procsu  on procsu.procseletivo= procs.codigo ");
        sb.append(" inner join unidadeensino  uni on uni.codigo = procsu.unidadeensino");
        sb.append(" inner join unidadeensinocurso unic on  unic.unidadeensino = uni.codigo");
        sb.append(" inner join curso c on c.codigo = unic.curso");
        sb.append(" inner join turno t on t.codigo = unic.turno");
        sb.append(" left join questionario quest on quest.codigo = procs.questionario");
        sb.append(" where 1=1 ");
        
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {        	
        	sb.append(" and uni.codigo = " ).append(unidadeEnsino);
        } 
        if(Uteis.isAtributoPreenchido(curso)) {        	
        	sb.append(" and c.codigo = " ).append(curso);
        } 
        if(Uteis.isAtributoPreenchido(turno)) {        	
        	sb.append(" and t.codigo = " ).append(turno);
        } 
        if(Uteis.isAtributoPreenchido(tipoProcessoSeletivo)) { 
        	if(tipoProcessoSeletivo.equals("TODOS")) {
        		 sb.append(" and (procs.tipoenem = true");
                 sb.append(" or procs.tipoprocessoseletivo =true");
                 sb.append(" or procs.tipoportadordiploma = true");
                 sb.append(" or procs.tipotransferencia = true )");      
        	}else if(FormaIngresso.valueOf(tipoProcessoSeletivo).equals(FormaIngresso.ENEM)) {
        		
        		sb.append(" and procs.tipoenem");
        	}else if(FormaIngresso.valueOf(tipoProcessoSeletivo).equals(FormaIngresso.PROCESSO_SELETIVO)) {
        		   sb.append(" and procs.tipoprocessoseletivo");
        		
        	}else if(FormaIngresso.valueOf(tipoProcessoSeletivo).equals(FormaIngresso.PORTADOR_DE_DIPLOMA)) {
        		
        		sb.append(" and procs.tipoportadordiploma");
        	}else if(tipoProcessoSeletivo.equals("TRANSFERENCIA")) {        		
        		 sb.append(" and procs.tipotransferencia ");      
        	}
       }      
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
     return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
//	@Override
//	public ProcSeletivoVO consultarPorDescricaoExata(String descricao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT * FROM ProcSeletivo ");
//		sb.append(" WHERE upper(sem_acentos(ProcSeletivo.descricao)) like (upper(sem_acentos(?))) ");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), descricao);
//		if (!tabelaResultado.next()) {
//			return new ProcSeletivoVO();
//		}
//		return montarDados(tabelaResultado, nivelMontarDados, usuario);
//	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcSeletivoVO inicializarDadosProcSeletivoImportacaoCandidatoInscricao( UsuarioVO usuario) throws Exception {
		ProcSeletivoVO procVO = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoExata( false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		if (Uteis.isAtributoPreenchido(procVO.getCodigo())) {
			return procVO;
		}
		ProcSeletivoVO procSeletivoVO = new ProcSeletivoVO();
//		procSeletivoVO.setDescricao(importarCandidatoVO.getDescricaoProcessoSeletivo());
//		procSeletivoVO.setAno(importarCandidatoVO.getAno());
//		procSeletivoVO.setSemestre(importarCandidatoVO.getSemestre());
		procSeletivoVO.getResponsavel().setCodigo(usuario.getCodigo());
		procSeletivoVO.setDocumentaoObrigatoria(".");
//		procSeletivoVO.setDataInicio(importarCandidatoVO.getDataInicioInscricao());
//		procSeletivoVO.setDataFim(importarCandidatoVO.getDataTerminoInscricao());
//		procSeletivoVO.setDataFimInternet(importarCandidatoVO.getDataTerminoInscricao());
		return procSeletivoVO;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer verificarQuantidadeAlunosMatriculadosPorProcessoSeletivoUnidadeEnsino(Integer procSeletivo, Integer unidadeEnsino, Integer eixoCurso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select count (m.matricula) as quantidade from matricula m ");
		sb.append(" inner join curso c on c.codigo = m.curso ");
		sb.append(" inner join inscricao  i on i.codigo = m.inscricao ");
		sb.append(" inner join procseletivo ps on ps.codigo = i.procseletivo ");
		sb.append(" inner join procseletivounidadeensino psu on psu.procseletivo = ps.codigo  ");		
		sb.append(" where m.situacao in ('AT','PR')  ");
		sb.append(" and m.unidadeensino = ? ");
		sb.append(" and psu.unidadeensino = ?  ");
		sb.append(" and ps.codigo = ?  ");
		sb.append(" and c.eixocurso = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { unidadeEnsino, unidadeEnsino, procSeletivo, eixoCurso });
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("quantidade"));
		}else {
			return 0;
		}
	}

	@Override
	public void removerProcSeletivoUnidadeEnsinoTodas(ProcSeletivoVO procSeletivoVO, UsuarioVO usuarioVO) throws Exception {
			
//			for (Iterator<ProcSeletivoUnidadeEnsinoVO> iterator = procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs().iterator(); iterator.hasNext();) {
//				ProcSeletivoUnidadeEnsinoVO obj = iterator.next();
//				for (ProcSeletivoCursoVO psc : obj.getProcSeletivoCursoVOs()) {
//					if (getFacadeFactory().getInscricaoFacade().verificarExisteInscricaoVinculadaProcSeletivoUnidadeEnsinoCurso(
//							procSeletivoVO.getCodigo(), psc.getUnidadeEnsinoCurso().getCodigo(), false,
//							usuarioVO)) {
//						continue q;
//					}
//				}
//				iterator.remove();
//			}
		}

	@Override
	public void adicionarCursoTurnoGeral(ProcSeletivoVO procSeletivoVO, CursoTurnoVO cursoTurnoVO, UsuarioVO usuarioVO) throws Exception{
	
//		for(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO: procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()) {
//			if(procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs().stream().noneMatch(
//						c -> 
//						c.getUnidadeEnsinoCurso().getCurso().getCodigo().equals(cursoTurnoVO.getCursoVO().getCodigo())
//						&& c.getUnidadeEnsinoCurso().getTurno().getCodigo().equals(cursoTurnoVO.getTurno().getCodigo()))) {
//				UnidadeEnsinoCursoVO unidadeEnsinoCursoVO =  getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeTurno(cursoTurnoVO.getCursoVO().getCodigo(), procSeletivoUnidadeEnsinoVO.getUnidadeEnsino().getCodigo(), cursoTurnoVO.getTurno().getCodigo(), usuarioVO);
//				if(Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO)) {
//					ProcSeletivoCursoVO procSeletivoCursoVO = new ProcSeletivoCursoVO();
//					procSeletivoCursoVO.setProcSeletivoUnidadeEnsino(procSeletivoUnidadeEnsinoVO);
//					procSeletivoCursoVO.setUnidadeEnsinoCurso(unidadeEnsinoCursoVO);
//					procSeletivoCursoVO.setNumeroVaga(cursoTurnoVO.getNrVagas());
//					procSeletivoCursoVO.getGrupoDisciplinaProcSeletivo().setCodigo(cursoTurnoVO.getGrupoDisciplinaProcSeletivo().getCodigo());
//					procSeletivoCursoVO.setDataInicioProcSeletivoCurso(cursoTurnoVO.getDataInicioProcSeletivoCurso());
//					procSeletivoCursoVO.setDataFimProcSeletivoCurso(cursoTurnoVO.getDataFimProcSeletivoCurso());
//					procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs().add(procSeletivoCursoVO);
//					Ordenacao.ordenarLista(procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs(), "ordenacao");
//				}
//			}
//		}
		
	}

	@Override
	public void excluir(ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String consultarDisciplinasProcSeletivoString(ProcSeletivoVO procSeletivoVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcSeletivoVO consultarPorDescricaoExata(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
