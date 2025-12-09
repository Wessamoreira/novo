package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

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

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.RegimeCurso;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcessoMatriculaCalendarioVO</code>. Responsável por implementar operações como incluir, alterar,
 * excluir e consultar pertinentes a classe <code>ProcessoMatriculaCalendarioVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProcessoMatriculaCalendarioVO
 * @see ControleAcesso
 * @see ProcessoMatricula
 */
@Repository
@Scope("singleton")
@Lazy
public class ProcessoMatriculaCalendario extends ControleAcesso  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

    public ProcessoMatriculaCalendario() throws Exception {
        super();
        setIdEntidade("ProcessoMatricula");
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#novo()
     */
    public ProcessoMatriculaCalendarioVO novo() throws Exception {
        ProcessoMatriculaCalendario.incluir(getIdEntidade());
        ProcessoMatriculaCalendarioVO obj = new ProcessoMatriculaCalendarioVO();
        return obj;
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#incluir(negocio.comuns.academico.ProcessoMatriculaVO, negocio.comuns.academico.ProcessoMatriculaCalendarioVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcessoMatriculaVO processoMatriculaVO, final ProcessoMatriculaCalendarioVO obj, UsuarioVO usuario) throws Exception {
//        ProcessoMatriculaCalendarioVO.validarDados(processoMatriculaVO, obj);
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCursoVO(obj.getCursoVO());
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTurnoVO(obj.getTurnoVO());
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCodigo(incluirPeriodoAtivo(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO(), processoMatriculaVO.getNivelProcessoMatricula(), usuario));
        final String sql = "INSERT INTO ProcessoMatriculaCalendario( processoMatricula, curso, dataInicioMatricula, dataFinalMatricula, "
                + "dataInicioInclusaoDisciplina, dataFinalInclusaoDisciplina, dataInicioMatForaPrazo, dataFinalMatForaPrazo, dataVencimentoMatricula, "
                + "mesVencimentoPrimeiraMensalidades, diaVencimentoPrimeiraMensalidades, anoVencimentoPrimeiraMensalidades, "
                + "periodoLetivoAtivoUnidadeEnsinoCurso, usarDataVencimentoDataMatricula, mesSubsequenteMatricula,"
                + "mesDataBaseGeracaoParcelas, zerarValorDescontoPlanoFinanceiroAluno, utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual, "
                + "qtdeDiasAvancarDataVencimentoMatricula, utilizaControleGeracaoParcelaTurma, controleGeracaoParcelaTurma, consideraCompetenciaVctoMatricula, dataCompetenciaMatricula, consideraCompetenciaVctoMensalidade, dataCompetenciaMensalidade, utilizarOrdemDescontoConfiguracaoFinanceira, politicaDivulgacaoMatriculaOnline, "
                + "acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno, turno, diaSemanaAula, turnoAula ) "
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getProcessoMatricula().intValue());
                sqlInserir.setInt(2, obj.getCursoVO().getCodigo().intValue());
                sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicioMatricula()));
                sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFinalMatricula()));
                sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataInicioInclusaoDisciplina()));
                sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataFinalInclusaoDisciplina()));
                sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataInicioMatForaPrazo()));
                sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataFinalMatForaPrazo()));
                if (obj.getDataVencimentoMatricula() != null) {
                    sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataVencimentoMatricula()));
                } else {
                    sqlInserir.setNull(9, 0);
                }
                if (obj.getMesVencimentoPrimeiraMensalidade() != null && obj.getMesVencimentoPrimeiraMensalidade() != 0) {
                    sqlInserir.setInt(10, obj.getMesVencimentoPrimeiraMensalidade().intValue());
                } else {
                    sqlInserir.setNull(10, 0);
                }
                if (obj.getDiaVencimentoPrimeiraMensalidade() != null && obj.getDiaVencimentoPrimeiraMensalidade() != 0) {
                    sqlInserir.setInt(11, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
                } else {
                    sqlInserir.setNull(11, 0);
                }
//				sqlInserir.setInt(11, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
                if (obj.getAnoVencimentoPrimeiraMensalidade() != null && obj.getAnoVencimentoPrimeiraMensalidade() != 0) {
                    sqlInserir.setInt(12, obj.getAnoVencimentoPrimeiraMensalidade().intValue());
                } else {
                    sqlInserir.setNull(12, 0);
                }
                sqlInserir.setInt(13, obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo());
                sqlInserir.setBoolean(14, obj.getUsarDataVencimentoDataMatricula());
                sqlInserir.setBoolean(15, obj.getMesSubsequenteMatricula());
                sqlInserir.setBoolean(16, obj.getMesDataBaseGeracaoParcelas());
                sqlInserir.setBoolean(17, obj.getZerarValorDescontoPlanoFinanceiroAluno());
                sqlInserir.setBoolean(18, obj.getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual());
                sqlInserir.setInt(19, obj.getQtdeDiasAvancarDataVencimentoMatricula());
                sqlInserir.setBoolean(20, obj.getUtilizaControleGeracaoParcelaTurma());
//                if (!obj.getControleGeracaoParcelaTurma().getCodigo().equals(0)) {
//                    sqlInserir.setInt(21, obj.getControleGeracaoParcelaTurma().getCodigo());
//                } else {
//                    sqlInserir.setNull(21, 0);
//                }
                sqlInserir.setBoolean(22, obj.getConsiderarCompetenciaVctoMatricula());
                sqlInserir.setDate(23, Uteis.getDataJDBC(obj.getDataCompetenciaMatricula()));
                sqlInserir.setBoolean(24, obj.getConsiderarCompetenciaVctoMensalidade());
                sqlInserir.setDate(25, Uteis.getDataJDBC(obj.getDataCompetenciaMensalidade()));
                sqlInserir.setBoolean(26, obj.getUtilizarOrdemDescontoConfiguracaoFinanceira());
                if(!obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
                	sqlInserir.setInt(27, obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo());
                } else {
                    sqlInserir.setNull(27, 0);
                }
                sqlInserir.setBoolean(28, obj.getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno());                
                sqlInserir.setInt(29, obj.getTurnoVO().getCodigo().intValue());
                sqlInserir.setString(30, obj.getDiaSemanaAula().name());
                sqlInserir.setString(31, obj.getTurnoAula().name());
                return sqlInserir;
            }
        });
//        getFacadeFactory().getProcessoMatriculaCalendarioLogFacade().preencherIncluirProcessoMatriculaCalendarioLog(processoMatriculaVO, obj, "inclusão", usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#incluirPeriodoAtivo(negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO)
     */
    public Integer incluirPeriodoAtivo(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoAtivo, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
        PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPeriodoAtivo(periodoAtivo.getCursoVO().getCodigo(), periodoAtivo.getTurnoVO().getCodigo(), null,
                periodoAtivo.getTipoPeriodoLetivo(), periodoAtivo.getSemestreReferenciaPeriodoLetivo(), periodoAtivo.getAnoReferenciaPeriodoLetivo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().incluir(periodoAtivo, nivelProcessoMatricula, usuario);
        return periodoAtivo.getCodigo();

    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#alterar(negocio.comuns.academico.ProcessoMatriculaVO, negocio.comuns.academico.ProcessoMatriculaCalendarioVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProcessoMatriculaVO processoMatriculaVO, final ProcessoMatriculaCalendarioVO obj, UsuarioVO usuario) throws Exception {
//        ProcessoMatriculaCalendarioVO.validarDados(processoMatriculaVO, obj);
        obj.getDataVencimentoMatricula();
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCursoVO(obj.getCursoVO());
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTurnoVO(obj.getTurnoVO());
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getProcessoMatriculaVO().setCodigo(processoMatriculaVO.getCodigo());
        getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().alterar(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO(), processoMatriculaVO.getNivelProcessoMatricula(), usuario);
        final String sql = "UPDATE ProcessoMatriculaCalendario set dataInicioMatricula=?, dataFinalMatricula=?, dataInicioInclusaoDisciplina=?, "
                + "dataFinalInclusaoDisciplina=?, dataInicioMatForaPrazo=?, dataFinalMatForaPrazo=?, dataVencimentoMatricula = ?, "
                + "mesVencimentoPrimeiraMensalidades = ?, diaVencimentoPrimeiraMensalidades = ?, anoVencimentoPrimeiraMensalidades = ?, "
                + "periodoLetivoAtivoUnidadeEnsinoCurso = ?, usarDataVencimentoDataMatricula=?, mesSubsequenteMatricula=?,"
                + "mesDataBaseGeracaoParcelas=?, zerarValorDescontoPlanoFinanceiroAluno=?, utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual=?, qtdeDiasAvancarDataVencimentoMatricula = ?,"
                + "utilizaControleGeracaoParcelaTurma=?, controleGeracaoParcelaTurma=?, consideraCompetenciaVctoMatricula=?, dataCompetenciaMatricula=?, consideraCompetenciaVctoMensalidade=?, dataCompetenciaMensalidade=?, utilizarOrdemDescontoConfiguracaoFinanceira=?, politicaDivulgacaoMatriculaOnline=?, acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno=?, "
                + "diaSemanaAula = ?, turnoAula = ? "
                + "WHERE ((processoMatricula = ?) and (curso = ?) and (turno = ?)) returning processoMatricula "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        if (!(Boolean) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataInicioMatricula()));
                sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataFinalMatricula()));
                sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataInicioInclusaoDisciplina()));
                sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataFinalInclusaoDisciplina()));
                sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataInicioMatForaPrazo()));
                sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataFinalMatForaPrazo()));
                if (obj.getDataVencimentoMatricula() != null) {
                    sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataVencimentoMatricula()));
                } else {
                    sqlAlterar.setNull(7, 0);
                }
                if (obj.getMesVencimentoPrimeiraMensalidade() != null && obj.getMesVencimentoPrimeiraMensalidade() != 0) {
                    sqlAlterar.setInt(8, obj.getMesVencimentoPrimeiraMensalidade().intValue());
                } else {
                    sqlAlterar.setNull(8, 0);
                }
                if (obj.getDiaVencimentoPrimeiraMensalidade() != null && obj.getDiaVencimentoPrimeiraMensalidade() != 0) {
                    sqlAlterar.setInt(9, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
                } else {
                    sqlAlterar.setNull(9, 0);
                }
//				sqlAlterar.setInt(9, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
                if (obj.getAnoVencimentoPrimeiraMensalidade() != null && obj.getAnoVencimentoPrimeiraMensalidade() != 0) {
                    sqlAlterar.setInt(10, obj.getAnoVencimentoPrimeiraMensalidade().intValue());
                } else {
                    sqlAlterar.setNull(10, 0);
                }
                sqlAlterar.setInt(11, obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo().intValue());
                sqlAlterar.setBoolean(12, obj.getUsarDataVencimentoDataMatricula());
                sqlAlterar.setBoolean(13, obj.getMesSubsequenteMatricula());
                sqlAlterar.setBoolean(14, obj.getMesDataBaseGeracaoParcelas());

                sqlAlterar.setBoolean(15, obj.getZerarValorDescontoPlanoFinanceiroAluno());
                sqlAlterar.setBoolean(16, obj.getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual());
                sqlAlterar.setInt(17, obj.getQtdeDiasAvancarDataVencimentoMatricula());
                sqlAlterar.setBoolean(18, obj.getUtilizaControleGeracaoParcelaTurma());
//                if (!obj.getControleGeracaoParcelaTurma().getCodigo().equals(0)) {
//                    sqlAlterar.setInt(19, obj.getControleGeracaoParcelaTurma().getCodigo());
//                } else {
//                    sqlAlterar.setNull(19, 0);
//                }
                sqlAlterar.setBoolean(20, obj.getConsiderarCompetenciaVctoMatricula());
                sqlAlterar.setDate(21, Uteis.getDataJDBC(obj.getDataCompetenciaMatricula()));
                sqlAlterar.setBoolean(22, obj.getConsiderarCompetenciaVctoMensalidade());
                sqlAlterar.setDate(23, Uteis.getDataJDBC(obj.getDataCompetenciaMensalidade()));
                sqlAlterar.setBoolean(24, obj.getUtilizarOrdemDescontoConfiguracaoFinanceira());

                if(!obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
                	sqlAlterar.setInt(25, obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo());
                } else {
                	sqlAlterar.setNull(25, 0);
                }
                sqlAlterar.setBoolean(26, obj.getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno());
                sqlAlterar.setString(27, obj.getDiaSemanaAula().name());
                sqlAlterar.setString(28, obj.getTurnoAula().name());
                sqlAlterar.setInt(29, obj.getProcessoMatricula().intValue());
                sqlAlterar.setInt(30, obj.getCursoVO().getCodigo().intValue());
                sqlAlterar.setInt(31, obj.getTurnoVO().getCodigo().intValue());
                
                return sqlAlterar;
            }
        }, new ResultSetExtractor<Boolean>() {

            public Boolean extractData(ResultSet arg0) throws SQLException, DataAccessException {
                return arg0.next();
            }
        })) {
            incluir(processoMatriculaVO, obj, usuario);
            return;
        }
//        getFacadeFactory().getProcessoMatriculaCalendarioLogFacade().preencherIncluirProcessoMatriculaCalendarioLog(processoMatriculaVO, obj, "alteração", usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#excluir(negocio.comuns.academico.ProcessoMatriculaCalendarioVO)
     */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProcessoMatriculaCalendarioVO obj, UsuarioVO usuarioVO) throws Exception {
		ProcessoMatriculaCalendario.excluir(getIdEntidade());
		String sql = "DELETE FROM ProcessoMatriculaCalendario WHERE ((processoMatricula = ?) and (curso = ?) and (turno = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getProcessoMatricula(), obj.getCursoVO().getCodigo() , obj.getTurnoVO().getCodigo() });
		getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().excluir(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO(), "", usuarioVO);
	}

   
    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarPorMatriculaPeriodoUnidadeEnsinoCurso(java.lang.Integer, java.lang.Integer, boolean, int)
     */
    public ProcessoMatriculaCalendarioVO consultarPorMatriculaPeriodoUnidadeEnsinoCurso(Integer codigoMatriculaPeriodo, Integer curso, Integer turno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        
        StringBuilder sqlStr = new StringBuilder("SELECT ProcessoMatriculaCalendario.* FROM ProcessoMatriculaCalendario ");
        sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.processomatricula = processomatriculacalendario.processomatricula ");
        sqlStr.append(" INNER JOIN unidadeensinocurso ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo and unidadeensinocurso.curso = ProcessoMatriculaCalendario.curso and unidadeensinocurso.turno = ProcessoMatriculaCalendario.turno ");
        sqlStr.append(" WHERE  ");
        sqlStr.append(" matriculaperiodo.codigo = ").append(codigoMatriculaPeriodo);   //.append(" and matriculaperiodo")     
        if(Uteis.isAtributoPreenchido(curso)) {
        	sqlStr.append(" and ProcessoMatriculaCalendario.curso = ").append(curso);
        }
        if(Uteis.isAtributoPreenchido(turno)) {
        	sqlStr.append(" and ProcessoMatriculaCalendario.turno = ").append(turno);        	
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and exists (");
        	sqlStr.append(" 	select from processomatriculaunidadeensino where processomatriculaunidadeensino.processomatricula = ProcessoMatriculaCalendario.processomatricula ");
        	sqlStr.append(" and processomatriculaunidadeensino.unidadeEnsino = ").append(unidadeEnsino);
        	sqlStr.append(" ) ");
        }
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new ProcessoMatriculaCalendarioVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

 



    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarProcessoMatriculaUnidadeEnsinoCursoDentroPrazo(java.lang.Integer, java.util.Date, boolean, int)
     */
    public List<ProcessoMatriculaCalendarioVO> consultarProcessoMatriculaUnidadeEnsinoCursoDentroPrazo(Integer curso, Integer turno, Integer unidadeEnsino, Date inicio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT ProcessoMatriculaCalendario.* FROM ProcessoMatriculaCalendario ");        
        sqlStr.append(" WHERE ProcessoMatriculaCalendario.curso = ").append(curso);
        sqlStr.append(" and ProcessoMatriculaCalendario.curso = ").append(turno);
        sqlStr.append(" and ((dataInicioMatricula <= '" + Uteis.getDataJDBC(inicio) + "') and (dataFinalMatricula >= '" + Uteis.getDataJDBC(inicio) + "'))");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        sqlStr.append(" and exists (");
        sqlStr.append(" 	select from processomatriculaunidadeensino where processomatriculaunidadeensino.processomatricula = ProcessoMatriculaCalendario.processomatricula ");
        sqlStr.append(" and processomatriculaunidadeensino.unidadeEnsino = ").append(unidadeEnsino);
        sqlStr.append(" ) ");
        }
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarProcessoMatriculaUnidadeEnsinoCursoForaPrazo(java.lang.Integer, java.util.Date, boolean, int)
     */
    public List<ProcessoMatriculaCalendarioVO> consultarProcessoMatriculaUnidadeEnsinoCursoForaPrazo(Integer curso, Integer turno, Integer unidadeEnsino, Date inicio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        
        StringBuilder sqlStr = new StringBuilder("SELECT ProcessoMatriculaCalendario.* FROM ProcessoMatriculaCalendario ");        
        sqlStr.append(" WHERE ProcessoMatriculaCalendario.curso = ").append(curso);
        sqlStr.append(" and ProcessoMatriculaCalendario.curso = ").append(turno);
        sqlStr.append(" and ((dataInicioMatForaPrazo <= '" + Uteis.getDataJDBC(inicio) + "') and (dataFinalMatForaPrazo >= '" + Uteis.getDataJDBC(inicio) + "'))");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        sqlStr.append(" and exists (");
        sqlStr.append(" 	select from processomatriculaunidadeensino where processomatriculaunidadeensino.processomatricula = ProcessoMatriculaCalendario.processomatricula ");
        sqlStr.append(" and processomatriculaunidadeensino.unidadeEnsino = ").append(unidadeEnsino);
        sqlStr.append(" ) ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarProcessoMatriculaUnidadeEnsinoCursoPrazoInclusaoExclusaoDisciplina(java.lang.Integer, java.util.Date,
     *      boolean, int)
     */
    public List<ProcessoMatriculaCalendarioVO> consultarProcessoMatriculaUnidadeEnsinoCursoPrazoInclusaoExclusaoDisciplina(Integer curso, Integer turno, Integer unidadeEnsino, Date inicio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT ProcessoMatriculaCalendario.* FROM ProcessoMatriculaCalendario ");        
        sqlStr.append(" WHERE ProcessoMatriculaCalendario.curso = ").append(curso);
        sqlStr.append(" and ProcessoMatriculaCalendario.curso = ").append(turno);
        sqlStr.append(" and ((dataInicioInclusaoDisciplina <= '" + Uteis.getDataJDBC(inicio) + "') and (dataFinalInclusaoDisciplina >= '" + Uteis.getDataJDBC(inicio) + "'))");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        sqlStr.append(" and exists (");
        sqlStr.append(" 	select from processomatriculaunidadeensino where processomatriculaunidadeensino.processomatricula = ProcessoMatriculaCalendario.processomatricula ");
        sqlStr.append(" and processomatriculaunidadeensino.unidadeEnsino = ").append(unidadeEnsino);
        sqlStr.append(" ) ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarPorDescricaoProcessoMatricula(java.lang.String, boolean, int)
     */
    public List consultarPorDescricaoProcessoMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProcessoMatriculaCalendario.* FROM ProcessoMatriculaCalendario, ProcessoMatricula WHERE ProcessoMatriculaCalendario.processoMatricula = ProcessoMatricula.codigo and ProcessoMatricula.descricao like('"
                + valorConsulta + "%') ORDER BY ProcessoMatricula.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarPorDescricaoProcessoMatricula(java.lang.String, boolean, int)
     */
    public ProcessoMatriculaCalendarioVO consultarPorPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from processomatriculacalendario where periodoletivoativounidadeensinocurso = " + valorConsulta;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new ProcessoMatriculaCalendarioVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    public Boolean consultarSeAindaExisteProcessoMatriculaCalendarioPorPeriodoLetivoAtivoDistintosComMesmoProcessoMatricula(Integer periodoLetivoAtivoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT pmc2.periodoletivoativounidadeensinocurso FROM processomatriculacalendario pmc ");
        sqlStr.append(" INNER JOIN processomatriculacalendario pmc2 ON pmc2.periodoletivoativounidadeensinocurso != pmc.periodoletivoativounidadeensinocurso AND pmc.processomatricula = pmc2.processomatricula ");
        sqlStr.append(" WHERE pmc.periodoletivoativounidadeensinocurso = ").append(periodoLetivoAtivoUnidadeEnsinoCurso);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado.next();
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>ProcessoMatriculaCalendarioVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ProcessoMatriculaCalendarioVO</code>.
     * 
     * @return O objeto da classe <code>ProcessoMatriculaCalendarioVO</code> com os dados devidamente montados.
     */
    public  ProcessoMatriculaCalendarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProcessoMatriculaCalendarioVO obj = new ProcessoMatriculaCalendarioVO();
        obj.setProcessoMatricula(dadosSQL.getInt("processoMatricula"));
        obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
        obj.getTurnoVO().setCodigo(dadosSQL.getInt("turno"));
        obj.setDataInicioMatricula(dadosSQL.getDate("dataInicioMatricula"));
        obj.setDataFinalMatricula(dadosSQL.getDate("dataFinalMatricula"));
        obj.setDataInicioInclusaoDisciplina(dadosSQL.getDate("dataInicioInclusaoDisciplina"));
        obj.setDataFinalInclusaoDisciplina(dadosSQL.getDate("dataFinalInclusaoDisciplina"));
        obj.setDataInicioMatForaPrazo(dadosSQL.getDate("dataInicioMatForaPrazo"));
        obj.setDataFinalMatForaPrazo(dadosSQL.getDate("dataFinalMatForaPrazo"));
        obj.setConsiderarCompetenciaVctoMatricula(dadosSQL.getBoolean("consideraCompetenciaVctoMatricula"));
        obj.setDataCompetenciaMatricula(dadosSQL.getDate("dataCompetenciaMatricula"));
        obj.setConsiderarCompetenciaVctoMensalidade(dadosSQL.getBoolean("consideraCompetenciaVctoMensalidade"));
        obj.setDataCompetenciaMensalidade(dadosSQL.getDate("dataCompetenciaMensalidade"));
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCodigo(new Integer(dadosSQL.getInt("periodoLetivoAtivoUnidadeEnsinoCurso")));        
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("diaSemanaAula"))) {
        	obj.setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("diaSemanaAula")));
        }else {
        	obj.setDiaSemanaAula(DiaSemana.NENHUM);
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("turnoAula"))) {
        	obj.setTurnoAula(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("turnoAula")));
        }else {
        	obj.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getInt("politicaDivulgacaoMatriculaOnline"))) {
        	obj.getPoliticaDivulgacaoMatriculaOnlineVO().setCodigo(dadosSQL.getInt("politicaDivulgacaoMatriculaOnline"));
        	obj.setPoliticaDivulgacaoMatriculaOnlineVO(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorChavePrimaria(obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null));            	
        	Boolean aux = false;
			for (SelectItem selectItem : obj.getListaSelectItemPoliticaDivulgacaoMatriculaVOs()) {
				if(selectItem.getValue().equals(obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo())) {
					aux = true;
				}
			}
			if(aux == false) {
				obj.getListaSelectItemPoliticaDivulgacaoMatriculaVOs().add(new SelectItem(obj.getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo(), obj.getPoliticaDivulgacaoMatriculaOnlineVO().getNome()));											
			}
        }  
        obj.setUtilizaControleGeracaoParcelaTurma(dadosSQL.getBoolean("utilizaControleGeracaoParcelaTurma"));
//        if (obj.getUtilizaControleGeracaoParcelaTurma()) {
//            // Caso esteja sendo utilizado um ControleGeracaoParcelaTurmaVO já gravado, então ao inves
//            // de montar os da entidade ProcessoMatriculaCalendarioVO, vamos montar os dados da entidade
//            // ControleGeracaoParcelaTurmaVO informada durante a criacado do calendário
//            if (dadosSQL.getInt("controleGeracaoParcelaTurma") != 0) {
//                obj.setControleGeracaoParcelaTurma(
//                        getFacadeFactory().getControleGeracaoParcelaTurmaFacade().consultarPorChavePrimaria(dadosSQL.getInt("controleGeracaoParcelaTurma"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
//            }
//            obj.inicializarDadosComBaseControleGeracaoParcelaTurmaVO(obj.getControleGeracaoParcelaTurma());
//        } else {
//            obj.setMesVencimentoPrimeiraMensalidade(dadosSQL.getInt("mesVencimentoPrimeiraMensalidades"));
//            obj.setDiaVencimentoPrimeiraMensalidade(dadosSQL.getInt("diaVencimentoPrimeiraMensalidades"));
//            obj.setAnoVencimentoPrimeiraMensalidade(dadosSQL.getInt("anoVencimentoPrimeiraMensalidades"));
//            obj.setQtdeDiasAvancarDataVencimentoMatricula(dadosSQL.getInt("qtdeDiasAvancarDataVencimentoMatricula"));
//            obj.setDataVencimentoMatricula(dadosSQL.getDate("dataVencimentoMatricula"));
//            obj.setUsarDataVencimentoDataMatricula(dadosSQL.getBoolean("usarDataVencimentoDataMatricula"));
//            obj.setMesSubsequenteMatricula(dadosSQL.getBoolean("mesSubsequenteMatricula"));
//            obj.setMesDataBaseGeracaoParcelas(dadosSQL.getBoolean("mesDataBaseGeracaoParcelas"));
//            obj.setZerarValorDescontoPlanoFinanceiroAluno(dadosSQL.getBoolean("zerarValorDescontoPlanoFinanceiroAluno"));
//            obj.setUtilizarOrdemDescontoConfiguracaoFinanceira(dadosSQL.getBoolean("utilizarOrdemDescontoConfiguracaoFinanceira"));
//            obj.setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(dadosSQL.getBoolean("utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual"));
//            obj.setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(dadosSQL.getBoolean("acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno"));
//        }


        montarDadosPeriodoLetivoAtivoUnidadeEnsinoCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosUnidadeEnsinoCurso(obj, nivelMontarDados, usuario);

        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>ProcessoMatriculaCalendarioVO</code>. Faz uso da chave primária da classe
     * <code>CursoVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosUnidadeEnsinoCurso(ProcessoMatriculaCalendarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCursoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
        if (obj.getTurnoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setTurnoVO(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurnoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  usuario));
    }

    public  void montarDadosPeriodoLetivoAtivoUnidadeEnsinoCurso(ProcessoMatriculaCalendarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO() == null || obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(
                obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#excluirProcessoMatriculaCalendarios(negocio.comuns.academico.ProcessoMatriculaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuario) throws Exception {
        for (ProcessoMatriculaCalendarioVO obj : processoMatriculaVO.getProcessoMatriculaCalendarioVOs()) {
            getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().excluir(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO(), processoMatriculaVO.getNivelProcessoMatricula(), usuario);
//            getFacadeFactory().getProcessoMatriculaCalendarioLogFacade().preencherIncluirProcessoMatriculaCalendarioLog(processoMatriculaVO, obj, "exclusão", usuario);
        }
        ProcessoMatriculaCalendario.excluir(getIdEntidade());
        String sql = "DELETE FROM ProcessoMatriculaCalendario WHERE (processoMatricula = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{processoMatriculaVO.getCodigo()});
    }

    public List consultarProcessoMatriculaExcluirPeriodoLetivoAtivoUnidadeEnsinoCurso(List objetos, Integer codigoProcessoMatricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String str = "SELECT * FROM ProcessoMatriculaCalendario WHERE (processoMatricula = " + codigoProcessoMatricula + ")";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ProcessoMatriculaCalendarioVO objeto = (ProcessoMatriculaCalendarioVO) i.next();
            str += " AND periodoLetivoAtivoUnidadeEnsinoCurso <> " + objeto.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo().intValue();
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public void excluirPeriodoLetivoAtivo(List<ProcessoMatriculaCalendarioVO> objetos, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
        for (ProcessoMatriculaCalendarioVO obj : objetos) {
            getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().excluirPorCodigoEUnidadeEnsinoCurso(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo(), nivelProcessoMatricula, usuario);
        }
    }
   
    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#alterarProcessoMatriculaCalendarios(negocio.comuns.academico.ProcessoMatriculaVO, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, List<ProcessoMatriculaCalendarioVO> objetos, List<ProcessoMatriculaCalendarioVO> listaProcessoMatriculaExcluirPeriodoLetivo, UsuarioVO usuario) throws Exception {
    	String str = "DELETE FROM ProcessoMatriculaCalendario WHERE (processoMatricula = " + processoMatriculaVO.getCodigo() + ")";
        Iterator<ProcessoMatriculaCalendarioVO> i = objetos.iterator();
        while (i.hasNext()) {
            ProcessoMatriculaCalendarioVO objeto = (ProcessoMatriculaCalendarioVO) i.next();
            str += " AND periodoLetivoAtivoUnidadeEnsinoCurso <> " + objeto.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        excluirPeriodoLetivoAtivo(listaProcessoMatriculaExcluirPeriodoLetivo, processoMatriculaVO.getNivelProcessoMatricula(), usuario);
        Iterator<ProcessoMatriculaCalendarioVO> e = objetos.iterator();
        while (e.hasNext()) {
            ProcessoMatriculaCalendarioVO objeto = (ProcessoMatriculaCalendarioVO) e.next();
            if (objeto.getPossuiMatriculaVinculada()) {
	            if (objeto.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo().equals(0)) {
	                objeto.setProcessoMatricula(processoMatriculaVO.getCodigo());
	                incluir(processoMatriculaVO, objeto, usuario);
	            } else {
	                alterar(processoMatriculaVO, objeto, usuario);
	            }
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ProcessoMatriculaCalendarioVO objeto = (ProcessoMatriculaCalendarioVO) e.next();
            if (objeto.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo().equals(0)) {
                objeto.setProcessoMatricula(processoMatriculaVO.getCodigo());
                incluir(processoMatriculaVO, objeto, usuario);
            } else {
                alterar(processoMatriculaVO, objeto, usuario);
            }
        }
    }

    public void excluirObjProcessoMatriculaCalendarioVOs(Integer curso, Integer turno, List<ProcessoMatriculaCalendarioVO> processoMatriculaCalendarioVOs) throws Exception {
        int index = 0;
        Iterator i = processoMatriculaCalendarioVOs.iterator();
        while (i.hasNext()) {
            ProcessoMatriculaCalendarioVO objExistente = (ProcessoMatriculaCalendarioVO) i.next();
            if (objExistente.getCursoVO().getCodigo().equals(curso) && objExistente.getTurnoVO().getCodigo().equals(turno)) {
                processoMatriculaCalendarioVOs.remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#incluirProcessoMatriculaCalendarios(negocio.comuns.academico.ProcessoMatriculaVO, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) e.next();
            obj.setProcessoMatricula(processoMatriculaVO.getCodigo());
            incluir(processoMatriculaVO, obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ProcessoMatriculaCalendarioVO</code> relacionados a um objeto da classe <code>academico.ProcessoMatricula</code>.
     * 
     * @param processoMatricula
     *            Atributo de <code>academico.ProcessoMatricula</code> a ser utilizado para localizar os objetos da classe <code>ProcessoMatriculaCalendarioVO</code>.
     * @return List Contendo todos os objetos da classe <code>ProcessoMatriculaCalendarioVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public  List consultarProcessoMatriculaCalendarios(Integer processoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ProcessoMatriculaCalendario WHERE processoMatricula = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{processoMatricula});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, nivelMontarDados, usuario));
        }
        return objetos;
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, java.lang.Integer, int)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ProcessoMatriculaCalendarioVO consultarPorChavePrimaria(Integer cursoPrm, Integer turno, Integer processoMatriculaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProcessoMatriculaCalendario WHERE (curso = ? and turno = ? AND processoMatricula = ?)";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{cursoPrm, turno, processoMatriculaPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Não foi encontrado nenhum Calendário Processo de Matrícula definido para o Curso.(Curso = " + cursoPrm + ", turno = "+turno+", processoMatricula = " + processoMatriculaPrm + ")");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public void inicializarDadosCalendario(ProcessoMatriculaVO processoMatricula, ProcessoMatriculaCalendarioVO obj) {
        obj.setDataInicioMatricula(processoMatricula.getDataInicio());
        obj.setDataFinalMatricula(processoMatricula.getDataFinal());
        obj.setDataInicioInclusaoDisciplina(processoMatricula.getDataInicio());
        obj.setDataFinalInclusaoDisciplina(processoMatricula.getDataFinal());
        obj.setDataVencimentoMatricula(processoMatricula.getDataFinal());
        gerarMesAnoVencimentoPrimeiraMensalidade(processoMatricula.getDataFinal(), obj);
        obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivo(processoMatricula.getDataFinal());
		if (processoMatricula.getNivelProcessoMatricula().equals(TipoNivelEducacional.BASICO.getValor()) 
				|| processoMatricula.getNivelProcessoMatricula().equals(TipoNivelEducacional.MEDIO.getValor())
				|| processoMatricula.getNivelProcessoMatricula().equals(TipoNivelEducacional.INFANTIL.getValor())) {
			try {
				getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().inicializarDadosCalendarioBimestre(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO(), processoMatricula);
				getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().calcularDadosDiaSemanaLetiva(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO());
			} catch (Exception e) {
			}
		}
		
        if (obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.ANUAL.getValor())) {
            obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivo(Uteis.obterDataFutura(processoMatricula.getDataFinal(), 365));
            obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setAnoReferenciaPeriodoLetivo(
                    String.valueOf(Uteis.getAnoData(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo())));
        }
        if (obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.SEMESTRAL.getValor())) {
            obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivo(Uteis.obterDataFutura(processoMatricula.getDataFinal(), 180));
            obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setAnoReferenciaPeriodoLetivo(
                    String.valueOf(Uteis.getAnoData(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo())));
            int x = Uteis.getMesData(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
            String semestre = "";
            if (x <= 6) {
                semestre = "1";
            } else {
                semestre = "2";
            }
            obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setSemestreReferenciaPeriodoLetivo(semestre);
        }
        if (obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.INTEGRAL.getValor())) {
            obj.setDataFinalInclusaoDisciplina(Uteis.obterDataFutura(processoMatricula.getDataFinal(), 365));
        }
        obj.setDataInicioMatForaPrazo(processoMatricula.getDataInicio());
        obj.setDataFinalMatForaPrazo(processoMatricula.getDataFinal());

    }

    public void gerarMesAnoVencimentoPrimeiraMensalidade(Date data, ProcessoMatriculaCalendarioVO obj) {
        int mes = Uteis.getMesData(data) + 1;
        int ano = Uteis.getAnoData(data);
        if (mes == 13) {
            mes = 1;
            obj.setMesVencimentoPrimeiraMensalidade(mes);
            obj.setAnoVencimentoPrimeiraMensalidade(ano + 1);
        } else {
            obj.setMesVencimentoPrimeiraMensalidade(mes);
            obj.setAnoVencimentoPrimeiraMensalidade(ano);
        }
    }

    public void validarDadosCursoExisteGradeCurricularAtiva(Integer curso, UsuarioVO usuario) throws Exception {
        GradeCurricularVO gradeCurricular = getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCurso(curso, "AT", false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (gradeCurricular.getCodigo().intValue() == 0) {
            throw new Exception("Não existe uma GRADE CURRICULAR ATIVA para este curso.");
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProcessoMatriculaCalendario.idEntidade;
    }

    /**
     * @see negocio.facade.jdbc.academico.ProcessoMatriculaCalendarioInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        ProcessoMatriculaCalendario.idEntidade = idEntidade;
    }
    
	public boolean executarVerificarProcessoMatriculaCalendarioTransferenciaTurno(Integer cursoPrm, Integer turno, Integer processoMatriculaPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT processoMatricula FROM ProcessoMatriculaCalendario WHERE (curso = ? and turno = ? AND processoMatricula = ?)";
		return getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { cursoPrm, turno, processoMatriculaPrm }).next();
	}
	
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoPeriodoCalendario(ProcessoMatriculaVO processoMatriculaVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuarioVO) throws Exception {
//		ProcessoMatriculaCalendarioVO.validarDados(processoMatriculaVO, processoMatriculaCalendarioVO);
//		getFacadeFactory().getProcessoMatriculaFacade().alterarPeriodoProcessoMatricula(processoMatriculaVO, usuarioVO);
//		boolean possuiPeriodoParaAlterar = possuiPeriodoParaAtualizar(processoMatriculaCalendarioVO);
//		if (possuiPeriodoParaAlterar) {
//			alterarPeriodoProcessoMatriculaCalendarioPorProcessoMatricula(processoMatriculaCalendarioVO, usuarioVO);
//		}		
//		if (!processoMatriculaCalendarioVO.getIntegral()) {
//			processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCursoVO(processoMatriculaCalendarioVO.getCursoVO());
//			processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTurnoVO(processoMatriculaCalendarioVO.getTurnoVO());
//			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().alterar(processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO(), processoMatriculaVO.getNivelProcessoMatricula(), usuarioVO);
//		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPeriodoProcessoMatriculaCalendarioPorProcessoMatricula(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuario) throws Exception {
//        String sql = "UPDATE processoMatriculaCalendario set dataInicioMatricula=?, dataFinalMatricula=?, dataInicioInclusaoDisciplina=?, dataFinalInclusaoDisciplina=?, dataInicioMatForaPrazo=?, dataFinalMatForaPrazo=? WHERE ((processoMatricula = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE processoMatriculaCalendario set ");
        
        if (processoMatriculaCalendarioVO.getDataInicioMatricula() != null) {
        	sb.append(" dataInicioMatricula = '").append(Uteis.getDataJDBC(processoMatriculaCalendarioVO.getDataInicioMatricula())).append("',");
        }
        if (processoMatriculaCalendarioVO.getDataFinalMatricula() != null) {
        	sb.append(" dataFinalMatricula = '").append(Uteis.getDataJDBC(processoMatriculaCalendarioVO.getDataFinalMatricula())).append("',");
        }
        if (processoMatriculaCalendarioVO.getDataInicioInclusaoDisciplina() != null) {
        	sb.append(" dataInicioInclusaoDisciplina = '").append(Uteis.getDataJDBC(processoMatriculaCalendarioVO.getDataInicioInclusaoDisciplina())).append("',");
        }
        if (processoMatriculaCalendarioVO.getDataFinalInclusaoDisciplina() != null) {
        	sb.append(" dataFinalInclusaoDisciplina = '").append(Uteis.getDataJDBC(processoMatriculaCalendarioVO.getDataFinalInclusaoDisciplina())).append("',");
        }
        if (processoMatriculaCalendarioVO.getDataInicioMatForaPrazo() != null) {
        	sb.append(" dataInicioMatForaPrazo = '").append(Uteis.getDataJDBC(processoMatriculaCalendarioVO.getDataInicioMatForaPrazo())).append("',");
        } else {
        	sb.append(" dataInicioMatForaPrazo = null,");
        }
        if (processoMatriculaCalendarioVO.getDataFinalMatForaPrazo() != null) {
        	sb.append(" dataFinalMatForaPrazo = '").append(Uteis.getDataJDBC(processoMatriculaCalendarioVO.getDataFinalMatForaPrazo())).append("',");
        } else {
        	sb.append(" dataFinalMatForaPrazo = null,");
        }
        
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append(" WHERE ((processoMatricula = ?))");
        if (processoMatriculaCalendarioVO.getAtualizacaoIndividual()) {
        	sb.append(" AND ((curso = ").append(processoMatriculaCalendarioVO.getCursoVO().getCodigo()).append("))  ");
        	sb.append(" AND ((turno = ").append(processoMatriculaCalendarioVO.getTurnoVO().getCodigo()).append("))  ");
        }
        sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        
        getConexao().getJdbcTemplate().update(sb.toString(), new Object[]{processoMatriculaCalendarioVO.getProcessoMatricula()});
    }
	
	public boolean possuiPeriodoParaAtualizar(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
		if (processoMatriculaCalendarioVO.getDataInicioMatricula() != null || processoMatriculaCalendarioVO.getDataFinalMatricula() != null 
				|| processoMatriculaCalendarioVO.getDataInicioInclusaoDisciplina() != null || processoMatriculaCalendarioVO.getDataFinalInclusaoDisciplina() != null 
				|| processoMatriculaCalendarioVO.getDataInicioMatForaPrazo() != null || processoMatriculaCalendarioVO.getDataFinalMatForaPrazo() != null) {
			return true;
		}
		return false;
	}
    
	
	
	public ProcessoMatriculaCalendarioVO consultarPorUnidadeEnsinoCursoAnoSemestre(Integer unidadeEnsinoCurso, String ano, String semestre, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct processomatriculacalendario.* ");
		sqlStr.append(" from processomatricula ");
		sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo ");
		sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo ");
		sqlStr.append(" inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
		sqlStr.append(" where processomatriculaunidadeensino.unidadeensino =  unidadeensinocurso.unidadeensino ");
		sqlStr.append(" and processomatriculacalendario.curso =  unidadeensinocurso.curso ");
		sqlStr.append(" and processomatriculacalendario.turno =  unidadeensinocurso.turno ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND anoReferenciaPeriodoLetivo = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND semestreReferenciaPeriodoLetivo = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(tipoAluno)) {
			sqlStr.append(" AND (processomatricula.tipoaluno = 'AMBOS' OR processomatricula.tipoaluno = '").append(tipoAluno).append("') ");
			sqlStr.append(" and processomatricula.situacao in ('AT', 'PR') ");
        }
		sqlStr.append(" limit 1");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return montarDados(dadosSQL, nivelMontarDados, usuarioVO);
		}
		return new ProcessoMatriculaCalendarioVO();
	}

	
	public void desvincularPoliticaDivulgacaoMatriculaOnline(final Integer politicaDivulgacaoMatriculaOnline, UsuarioVO usuario) throws Exception {
			final StringBuilder sql = new StringBuilder("UPDATE processomatriculacalendario set politicadivulgacaomatriculaonline = null where politicadivulgacaomatriculaonline = ?");		
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, politicaDivulgacaoMatriculaOnline);
					return sqlAlterar;
				}
			});
		}
	
	
	public Boolean verificarProcessoMatriculaCalendarioExistenteUnidadeEnsinoCurso(Integer unidadeEnsinoCodigo,Integer turnoCodigo,Integer cursoCodigo) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT COUNT(processomatricula.codigo) AS qtd  FROM processomatricula  "); 
			sql.append(" INNER JOIN processomatriculacalendario  ON processomatricula.codigo  = processomatriculacalendario.processomatricula  "); 			
			sql.append(" INNER JOIN processomatriculaunidadeensino  ON processomatricula.codigo = processomatriculaunidadeensino.processomatricula  ");
			sql.append(" WHERE processomatriculacalendario.curso = ").append(cursoCodigo);			
			sql.append(" AND processomatriculaunidadeensino.unidadeensino = ").append(unidadeEnsinoCodigo);
			sql.append(" AND processomatriculacalendario.turno = ").append(turnoCodigo);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw e;
		}
	}
}
