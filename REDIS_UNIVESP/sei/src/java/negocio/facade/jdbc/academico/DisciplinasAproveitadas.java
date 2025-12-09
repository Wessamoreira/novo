package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
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

import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.DisciplinasAproveitadasInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe
 * <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see AproveitamentoDisciplinaDisciplinasAproveitadasVO
 * @see ControleAcesso
 * @see AproveitamentoDisciplina
 */
@Repository
@Scope("singleton")
@Lazy
public class DisciplinasAproveitadas extends ControleAcesso implements DisciplinasAproveitadasInterfaceFacade {

    private static final long serialVersionUID = -1125108885136825593L;
    protected static String idEntidade;

    public DisciplinasAproveitadas() throws Exception {
        super();
        setIdEntidade("AproveitamentoDisciplina");
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade#novo()
     */
    public DisciplinasAproveitadasVO novo() throws Exception {
        DisciplinasAproveitadas.incluir(getIdEntidade());
        DisciplinasAproveitadasVO obj = new DisciplinasAproveitadasVO();
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #incluir(negocio
     * .comuns.academico.AproveitamentoDisciplinaDisciplinasAproveitadasVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DisciplinasAproveitadasVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception {
        try {
            DisciplinasAproveitadasVO.validarDados(obj, periodicidadeCurso);
            obj.setDataAproveitamentoEntreMatriculas(new Date());
            obj.getResponsavelAproveitamentoEntreMatriculas().setCodigo(usuario.getCodigo());
            obj.getResponsavelAproveitamentoEntreMatriculas().setNome(usuario.getNome());
            final String sql = "INSERT INTO DisciplinasAproveitadas( disciplina, nota, frequencia, "
                    + "AproveitamentoDisciplina, ano, semestre, disciplinaForaGrade, periodoLetivoOrigemDisciplina, "
                    + "instituicao, mediaFinalConceito, utilizaNotaConceito, cidade, cargaHoraria, cargaHorariaCursada, "
                    + "aproveitamentoPorIsencao, tipo, descricaoComplementacaoCH, qtdeCreditoConcedido, "
                    + "qtdeCargaHorariaConcedido, periodoletivoGrupoOptativa, mapaEquivalenciaDisciplinaCursada, nomeDisciplinaCursada, situacaoHistorico,"
                    + "matriculaOrigemAproveitamentoEntreMatriculas, responsavelAproveitamentoEntreMatriculas, dataAproveitamentoEntreMatriculas, "
                    + "observacaoAproveitamentoEntreMatriculas, apresentarAprovadoHistorico, nomeprofessor, titulacaoprofessor, sexoprofessor, dataInicioAula, dataFimAula ) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getDisciplina().getCodigo().intValue());
                    sqlInserir.setDouble(2, obj.getNota().doubleValue());
                    sqlInserir.setDouble(3, obj.getFrequencia().doubleValue());
                    if (obj.getAproveitamentoDisciplina().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getAproveitamentoDisciplina().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getAno());
                    sqlInserir.setString(6, obj.getSemestre());
                    sqlInserir.setBoolean(7, obj.getDisciplinaForaGrade());
                    sqlInserir.setInt(8, obj.getPeriodoLetivoOrigemDisciplina());
                    sqlInserir.setString(9, obj.getInstituicao());
                    sqlInserir.setString(10, obj.getMediaFinalConceito());
                    sqlInserir.setBoolean(11, obj.getUtilizaNotaConceito());
                    if (obj.getCidade().getCodigo() > 0) {
                        sqlInserir.setInt(12, obj.getCidade().getCodigo());
                    } else {
                        sqlInserir.setNull(12, 0);
                    }
                    sqlInserir.setInt(13, obj.getCargaHoraria());
                    sqlInserir.setInt(14, obj.getCargaHorariaCursada());
                    sqlInserir.setBoolean(15, obj.getAproveitamentoPorIsencao());
                    sqlInserir.setString(16, obj.getTipo());
                    sqlInserir.setString(17, obj.getDescricaoComplementacaoCH());
                    sqlInserir.setInt(18, obj.getQtdeCreditoConcedido());
                    sqlInserir.setInt(19, obj.getQtdeCargaHorariaConcedido());
                    if (obj.getPeriodoletivoGrupoOptativaVO().getCodigo() > 0) {
                        sqlInserir.setInt(20, obj.getPeriodoletivoGrupoOptativaVO().getCodigo());
                    } else {
                        sqlInserir.setNull(20, 0);
                    }
                    if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo() > 0) {
                        sqlInserir.setInt(21, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo());
                    } else {
                        sqlInserir.setNull(21, 0);
                    }
                    sqlInserir.setString(22, obj.getNomeDisciplinaCursada());
                    if(Uteis.isAtributoPreenchido(obj.getSituacaoHistorico())) {
						sqlInserir.setString(23, obj.getSituacaoHistorico().getValor());
					} else {
						sqlInserir.setNull(23, 0);
					}
                    
                    sqlInserir.setString(24, obj.getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula());
                    if (!obj.getResponsavelAproveitamentoEntreMatriculas().getCodigo().equals(0)) {
                    	sqlInserir.setInt(25, obj.getResponsavelAproveitamentoEntreMatriculas().getCodigo());
                    } else {
                    	sqlInserir.setNull(25, 0);
                    }
                    sqlInserir.setDate(26, Uteis.getDataJDBC(obj.getDataAproveitamentoEntreMatriculas()));
                    sqlInserir.setString(27, obj.getObservacaoAproveitamentoEntreMatriculas());                    
                    sqlInserir.setBoolean(28, obj.getApresentarAprovadoHistorico());
                    sqlInserir.setString(29, obj.getNomeProfessor());                    
                    sqlInserir.setString(30, obj.getTitulacaoProfessor());
                    sqlInserir.setString(31, obj.getSexoProfessor());
                    sqlInserir.setDate(32, Uteis.getDataJDBC(obj.getDataInicioAula()));
                    sqlInserir.setDate(33, Uteis.getDataJDBC(obj.getDataFimAula()));
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

            if (!obj.getAproveitamentoDisciplinaVO().getAproveitamentoPrevisto()) {
                if (obj.getHistoricoAtual().getCursando()) {
                    //CASO EXISTA UM HISTÓRICO PARA O ALUNO INDICANDO QUE O MESMO ESTÁ CURSANDO A DISCIPLINA
                    //(GERADO GERALMENTE QUANDO A DISCIPLINA É INCLUÍDA PARA O ALUNO NA RENOVAÇÃO DE MATRÍCULA
                    //ENTÃO ESTE HISTÓRICO É REMOVIDO, PARA QUE O HISTÓRICO DE APROVEITAMENTO PREVALEÇA COMO
                    //SITUACAO FINAL DO ALUNO. O ATRIBUTO isExcluirHistoricoDisciplinaCursada É SETADO TRUE
                    //QUANDO O APROVEITAMENTO DA DISCIPLINA É ADICIONADO PARA SER PERSISTIDO.
                	if(Uteis.isAtributoPreenchido(obj.getHistoricoAtual().getMatriculaPeriodoTurmaDisciplina())) {                		
                		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
                		getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().verificarAproveitamentoDisciplinaAlunoDelete(obj.getHistoricoAtual().getMatriculaPeriodoTurmaDisciplina(), usuario, configuracaoGeralSistemaVO);
                	}
                    getFacadeFactory().getHistoricoFacade().excluir(obj.getHistoricoAtual(), false, usuario);
                }
                HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().criarHistoricoAPartirDisciplinaAproveitada(obj);
                if (obj.getDisciplinaComposta()) {
                	for (DisciplinasAproveitadasVO disciplinaFazParteComposicao : obj.getDisciplinasAproveitadasFazemParteComposicao()) {
                        HistoricoVO historicoVOParteComposicao = getFacadeFactory().getHistoricoFacade().criarHistoricoAPartirDisciplinaAproveitada(disciplinaFazParteComposicao);
                        historicoVOParteComposicao.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);
                        historicoVOParteComposicao.setConfiguracaoAcademico(disciplinaFazParteComposicao.getConfiguracaoAcademicoVO());
                        historicoVOParteComposicao.setGradeDisciplinaComposta(disciplinaFazParteComposicao.getGradeDisciplinaCompostaVO());
                        getFacadeFactory().getHistoricoFacade().incluir(historicoVOParteComposicao, usuario);
                	}
                	historicoVO.setHistoricoDisciplinaComposta(Boolean.TRUE);
                }
                getFacadeFactory().getHistoricoFacade().incluir(historicoVO, usuario);
            }
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
        obj.setCriarNovoHistorico(true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarAproveitamentoPrevistoParaEfetivo(final DisciplinasAproveitadasVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception {
        try {
            DisciplinasAproveitadasVO.validarDados(obj, periodicidadeCurso);
            HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().criarHistoricoAPartirDisciplinaAproveitada(obj);
            getFacadeFactory().getHistoricoFacade().incluir(historicoVO, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #alterar(negocio
     * .comuns.academico.AproveitamentoDisciplinaDisciplinasAproveitadasVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DisciplinasAproveitadasVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception {
        try {
            DisciplinasAproveitadasVO.validarDados(obj, periodicidadeCurso);

            final String sql = "UPDATE DisciplinasAproveitadas set disciplina=?, nota=?, frequencia=?, AproveitamentoDisciplina=?, "
                    + "ano=?, semestre=?, disciplinaForaGrade=?, periodoLetivoOrigemDisciplina=?, instituicao=?, mediaFinalConceito=?, utilizaNotaConceito=?, cidade = ?, cargaHoraria = ?, cargaHorariaCursada = ?, aproveitamentoPorIsencao=?, "
                    + " tipo=?, descricaoComplementacaoCH=?, qtdeCreditoConcedido=?, qtdeCargaHorariaConcedido=?, "
                    + "periodoletivoGrupoOptativa=?, mapaEquivalenciaDisciplinaCursada=?, nomeDisciplinaCursada=?, situacaoHistorico=?,"
                    + "matriculaOrigemAproveitamentoEntreMatriculas=?, responsavelAproveitamentoEntreMatriculas=?, dataAproveitamentoEntreMatriculas=?,"
                    + " observacaoAproveitamentoEntreMatriculas=?, apresentarAprovadoHistorico = ?, nomeprofessor=?, titulacaoprofessor=?, sexoprofessor=?, dataInicioAula=?, dataFimAula=? "
                    + " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getDisciplina().getCodigo().intValue());
                    sqlAlterar.setDouble(2, obj.getNota().doubleValue());
                    sqlAlterar.setDouble(3, obj.getFrequencia().doubleValue());
                    if (obj.getAproveitamentoDisciplina().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getAproveitamentoDisciplina().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setString(5, obj.getAno());
                    sqlAlterar.setString(6, obj.getSemestre());
                    sqlAlterar.setBoolean(7, obj.getDisciplinaForaGrade());
                    sqlAlterar.setInt(8, obj.getPeriodoLetivoOrigemDisciplina());
                    sqlAlterar.setString(9, obj.getInstituicao());
                    sqlAlterar.setString(10, obj.getMediaFinalConceito());
                    sqlAlterar.setBoolean(11, obj.getUtilizaNotaConceito());
                    if (obj.getCidade().getCodigo() > 0) {
                        sqlAlterar.setInt(12, obj.getCidade().getCodigo());
                    } else {
                        sqlAlterar.setNull(12, 0);
                    }
                    sqlAlterar.setInt(13, obj.getCargaHoraria().intValue());
                    sqlAlterar.setInt(14, obj.getCargaHorariaCursada().intValue());
                    sqlAlterar.setBoolean(15, obj.getAproveitamentoPorIsencao());
                    sqlAlterar.setString(16, obj.getTipo());
                    sqlAlterar.setString(17, obj.getDescricaoComplementacaoCH());
                    sqlAlterar.setInt(18, obj.getQtdeCreditoConcedido());
                    sqlAlterar.setInt(19, obj.getQtdeCargaHorariaConcedido());
                    if (obj.getPeriodoletivoGrupoOptativaVO().getCodigo() > 0) {
                        sqlAlterar.setInt(20, obj.getPeriodoletivoGrupoOptativaVO().getCodigo());
                    } else {
                        sqlAlterar.setNull(20, 0);
                    }
                    if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo() > 0) {
                        sqlAlterar.setInt(21, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo());
                    } else {
                        sqlAlterar.setNull(21, 0);
                    }
                    sqlAlterar.setString(22, obj.getNomeDisciplinaCursada());
					if (Uteis.isAtributoPreenchido(obj.getSituacaoHistorico())) {
						sqlAlterar.setString(23, obj.getSituacaoHistorico().getValor());
					} else {
						sqlAlterar.setNull(23, 0);
					}
					
					sqlAlterar.setString(24, obj.getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula());
                    if (!obj.getResponsavelAproveitamentoEntreMatriculas().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(25, obj.getResponsavelAproveitamentoEntreMatriculas().getCodigo());
                    } else {
                    	sqlAlterar.setNull(25, 0);
                    }
                    sqlAlterar.setDate(26, Uteis.getDataJDBC(obj.getDataAproveitamentoEntreMatriculas()));
                    sqlAlterar.setString(27, obj.getObservacaoAproveitamentoEntreMatriculas());
                    sqlAlterar.setBoolean(28, obj.getApresentarAprovadoHistorico());
                    sqlAlterar.setString(29, obj.getNomeProfessor());                    
                    sqlAlterar.setString(30, obj.getTitulacaoProfessor());
                    sqlAlterar.setString(31, obj.getSexoProfessor());
                    sqlAlterar.setDate(32, Uteis.getDataJDBC(obj.getDataInicioAula()));
                    sqlAlterar.setDate(33, Uteis.getDataJDBC(obj.getDataFimAula()));
                    sqlAlterar.setInt(34, obj.getCodigo().intValue());

                    return sqlAlterar;
                }
            });

            if (!obj.getAproveitamentoDisciplinaVO().getAproveitamentoPrevisto()) {
                if (obj.getHistoricoAtual().getCursando()) {
                    //CASO EXISTA UM HISTÓRICO PARA O ALUNO INDICANDO QUE O MESMO ESTÁ CURSANDO A DISCIPLINA
                    //(GERADO GERALMENTE QUANDO A DISCIPLINA É INCLUÍDA PARA O ALUNO NA RENOVAÇÃO DE MATRÍCULA
                    //ENTÃO ESTE HISTÓRICO É REMOVIDO, PARA QUE O HISTÓRICO DE APROVEITAMENTO PREVALEÇA COMO
                    //SITUACAO FINAL DO ALUNO. O ATRIBUTO isExcluirHistoricoDisciplinaCursada É SETADO TRUE
                    //QUANDO O APROVEITAMENTO DA DISCIPLINA É ADICIONADO PARA SER PERSISTIDO.
                    getFacadeFactory().getHistoricoFacade().excluir(obj.getHistoricoAtual(), false, usuario);
                }
                //Dados são carrregados para garantir que o histórico seja persistido, sem  perder nenhum dado importante do mesmo
                getFacadeFactory().getHistoricoFacade().carregarDados(obj.getHistoricoAtual(), NivelMontarDados.TODOS, usuario);
                getFacadeFactory().getHistoricoFacade().atualizarDadosHistoricoComBaseDisciplinaAproveitada(obj.getHistoricoAtual(), obj);
                getFacadeFactory().getHistoricoFacade().alterar(obj.getHistoricoAtual(), usuario);
            }

        } catch (Exception e) {
            throw e;
        }

    }

    /*
     * HOMOLOGADO VERSAO 5.0
     * Método responsável por excluir a disciplinaAproveitada e o histórico de aproveitamento assoaciado 
     * ao mesmo.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(String matricula, DisciplinasAproveitadasVO obj, UsuarioVO usuario) throws Exception {
        try {
            DisciplinasAproveitadas.excluir(getIdEntidade());
            String sql = "DELETE FROM DisciplinasAproveitadas WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getHistoricoFacade().excluirHistoricoPorMatriculaCodigoDisciplinaAproveitamento(
                    matricula,
                    obj.getDisciplina().getCodigo(),
                    usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<DisciplinasAproveitadasVO> consultarDisciplinasAproveitadasRemovidasAproveitamento(AproveitamentoDisciplinaVO aproveitamento, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DisciplinasAproveitadas.*, historico.codigo as \"historico.codigo\",  ");
        sql.append(" historico.disciplina as \"historico.disciplina\", historico.cargaHorariaDisciplina as \"historico.cargaHorariaDisciplina\", ");
        sql.append(" historico.gradeDisciplina as \"historico.gradeDisciplina\", historico.gradeCurricularGrupoOptativaDisciplina as \"historico.gradeCurricularGrupoOptativaDisciplina\", ");
        sql.append(" historico.disciplinaReferenteAUmGrupoOptativa as \"historico.disciplinaReferenteAUmGrupoOptativa\", historico.historicoDisciplinaComposta as \"historico.historicoDisciplinaComposta\" ");
        sql.append(" FROM DisciplinasAproveitadas ");
        sql.append(" INNER JOIN AproveitamentoDisciplina ON (DisciplinasAproveitadas.AproveitamentoDisciplina = AproveitamentoDisciplina.codigo)");
        sql.append(" LEFT JOIN Historico ON (DisciplinasAproveitadas.codigo = Historico.disciplinasAproveitadas)");
        sql.append(" WHERE (AproveitamentoDisciplina.codigo = ").append(aproveitamento.getCodigo()).append(") ");
        if (!aproveitamento.getDisciplinasAproveitadasVOs().isEmpty()) {
            // se nao está vazia, temos que retornar todos as disciplinasAproveitas que não estão
            // nesta lista, pois estes serão mantidos. Queremos justamente obter os que não estão
            // contidos nesta lista. Caso esteja vazia, significa que nenhum disciplinaAproveitada será mantida
            sql.append("   and (DisciplinasAproveitadas.codigo not in (");
            String virgula = "";
            for (DisciplinasAproveitadasVO disciplinasAproveitadasVOManter : aproveitamento.getDisciplinasAproveitadasVOs()) {
                sql.append(virgula);
                sql.append(disciplinasAproveitadasVOManter.getCodigo());
                virgula = ", ";
            }
            sql.append("))");
        }
        sql.append("   ORDER BY DisciplinasAproveitadas.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaComResumoHistorico(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorDescricaoAproveitamentoDisciplina(java.lang.String, int)
     */
    public List<DisciplinasAproveitadasVO> consultarPorDescricaoAproveitamentoDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT DisciplinasAproveitadas.* FROM AproveitamentoDisciplinaDisciplinasAproveitadas, AproveitamentoDisciplina WHERE AproveitamentoDisciplinaDisciplinasAproveitadas.AproveitamentoDisciplina = AproveitamentoDisciplina.codigo and upper( AproveitamentoDisciplina.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY AproveitamentoDisciplina.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorFrequencia(java.lang.Double, boolean, int)
     */
    public List<DisciplinasAproveitadasVO> consultarPorFrequencia(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DisciplinasAproveitadas WHERE frequencia >= " + valorConsulta.doubleValue() + " ORDER BY frequencia";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorNota(java.lang.Double, boolean, int)
     */
    public List<DisciplinasAproveitadasVO> consultarPorNota(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DisciplinasAproveitadas WHERE nota >= " + valorConsulta.doubleValue() + " ORDER BY nota";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorDisciplinaETransferencia(java.lang.Integer,
     * java.lang.Integer, boolean, int)
     */
    public List<DisciplinasAproveitadasVO> consultarPorDisciplinaETransferencia(Integer valorConsulta, Integer transferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DisciplinasAproveitadas WHERE disciplina = " + valorConsulta.intValue() + " and AproveitamentoDisciplina = " + transferencia + " ORDER BY disciplina";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorCodigo(java.lang.Integer, boolean, int)
     */
    public List<DisciplinasAproveitadasVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DisciplinasAproveitadas WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public Boolean consultarExistenciaRegistroEmDisciplinaAproveitada(String matricula, Integer codigoDisciplina, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT da.codigo FROM disciplinasAproveitadas da ");
        sqlStr.append("INNER JOIN aproveitamentoDisciplina ad ON da.aproveitamentoDisciplina = ad.codigo ");
        sqlStr.append("WHERE ad.matricula = '" + matricula + "' AND da.disciplina = " + codigoDisciplina + " ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return false;
        } else {
            return true;
        }
    }

    public String consultarAnoSemestreDisciplinaAproveitada(String matricula, Integer codigoDisciplina, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT ano, semestre FROM disciplinasAproveitadas da ");
        sqlStr.append("INNER JOIN aproveitamentoDisciplina ad ON da.aproveitamentoDisciplina = ad.codigo ");
        sqlStr.append("WHERE ad.matricula = '" + matricula + "' AND da.disciplina = " + codigoDisciplina + " ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        tabelaResultado.next();
        String resultado = tabelaResultado.getString("ano") + "/" + tabelaResultado.getString("semestre");
        return resultado;

    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe
     *         <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>
     *         resultantes da consulta.
     */
    public static List<DisciplinasAproveitadasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DisciplinasAproveitadasVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            DisciplinasAproveitadasVO obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public static List<DisciplinasAproveitadasVO> montarDadosConsultaComResumoHistorico(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DisciplinasAproveitadasVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            DisciplinasAproveitadasVO obj = montarDadosComResumoHistorico(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public static DisciplinasAproveitadasVO montarDadosComResumoHistorico(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DisciplinasAproveitadasVO obj = montarDados(dadosSQL, nivelMontarDados, usuario);
        obj.getHistoricoAtual().setCodigo(new Integer(dadosSQL.getInt("historico.codigo")));
        obj.getHistoricoAtual().getDisciplina().setCodigo(new Integer(dadosSQL.getInt("historico.disciplina")));
        obj.getHistoricoAtual().setCargaHorariaDisciplina(new Integer(dadosSQL.getInt("historico.cargaHorariaDisciplina")));
        obj.getHistoricoAtual().getGradeDisciplinaVO().setCodigo(new Integer(dadosSQL.getInt("historico.gradeDisciplina")));
        obj.getHistoricoAtual().getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(new Integer(dadosSQL.getInt("historico.gradeCurricularGrupoOptativaDisciplina")));
        obj.getHistoricoAtual().setDisciplinaReferenteAUmGrupoOptativa(dadosSQL.getBoolean("historico.disciplinaReferenteAUmGrupoOptativa"));
        obj.getHistoricoAtual().setHistoricoDisciplinaComposta(dadosSQL.getBoolean("historico.historicoDisciplinaComposta"));
        return obj;
    }    
	
    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>.
     * 
     * @return O objeto da classe
     *         <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code> com os
     *         dados devidamente montados.
     */
    public static DisciplinasAproveitadasVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DisciplinasAproveitadasVO obj = new DisciplinasAproveitadasVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.setNomeDisciplinaCursada(dadosSQL.getString("nomeDisciplinaCursada"));
        obj.setNota(new Double(dadosSQL.getDouble("nota")));
        obj.setFrequencia(new Double(dadosSQL.getDouble("frequencia")));
        obj.setAproveitamentoDisciplina(new Integer(dadosSQL.getInt("AproveitamentoDisciplina")));
        obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
        obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
        obj.setCargaHorariaCursada(dadosSQL.getInt("cargaHorariaCursada"));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setInstituicao(dadosSQL.getString("instituicao"));
        obj.setDisciplinaForaGrade(dadosSQL.getBoolean("disciplinaForaGrade"));
        obj.setPeriodoLetivoOrigemDisciplina(dadosSQL.getInt("periodoLetivoOrigemDisciplina"));
        obj.setUtilizaNotaConceito(dadosSQL.getBoolean("utilizaNotaConceito"));
        obj.setMediaFinalConceito(dadosSQL.getString("mediaFinalConceito"));
        obj.setAproveitamentoPorIsencao(dadosSQL.getBoolean("aproveitamentoPorIsencao"));
        obj.getPeriodoletivoGrupoOptativaVO().setCodigo(dadosSQL.getInt("periodoletivoGrupoOptativa"));

        obj.getMapaEquivalenciaDisciplinaCursada().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));

        obj.setTipo(dadosSQL.getString("tipo"));
        obj.setDescricaoComplementacaoCH(dadosSQL.getString("descricaoComplementacaoCH"));
        obj.setQtdeCreditoConcedido(dadosSQL.getInt("qtdeCreditoConcedido"));
        obj.setQtdeCargaHorariaConcedido(dadosSQL.getInt("qtdeCargaHorariaConcedido"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("situacaoHistorico"))) {
			obj.setSituacaoHistorico(SituacaoHistorico.getEnum(dadosSQL.getString("situacaoHistorico")));
		}
        obj.getMatriculaOrigemAproveitamentoEntreMatriculas().setMatricula(dadosSQL.getString("matriculaOrigemAproveitamentoEntreMatriculas"));
        obj.getResponsavelAproveitamentoEntreMatriculas().setCodigo(dadosSQL.getInt("responsavelAproveitamentoEntreMatriculas"));
        obj.setDataAproveitamentoEntreMatriculas(dadosSQL.getDate("dataAproveitamentoEntreMatriculas"));
        obj.setObservacaoAproveitamentoEntreMatriculas(dadosSQL.getString("observacaoAproveitamentoEntreMatriculas"));
		obj.setApresentarAprovadoHistorico(dadosSQL.getBoolean("apresentarAprovadoHistorico"));
		obj.setNomeProfessor(dadosSQL.getString("nomeprofessor"));
        obj.setTitulacaoProfessor(dadosSQL.getString("titulacaoprofessor"));
        obj.setSexoProfessor(dadosSQL.getString("sexoprofessor"));
        obj.setDataInicioAula(dadosSQL.getDate("dataInicioAula"));
        obj.setDataFimAula(dadosSQL.getDate("dataFimAula"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosDisciplina(obj, nivelMontarDados, usuario);
        montarDadosCidade(obj, nivelMontarDados, usuario);
        montarDadosPeriodoLetivoGrupoOptativa(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosPeriodoLetivoGrupoOptativa(DisciplinasAproveitadasVO obj, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        if (obj.getPeriodoletivoGrupoOptativaVO().getCodigo() == null || obj.getPeriodoletivoGrupoOptativaVO().getCodigo() == 0) {
            return;
        }
        obj.setPeriodoletivoGrupoOptativaVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeriodoletivoGrupoOptativaVO().getCodigo(), nivelMontarDados, usuarioVO));
    }

    public static void montarDadosDisciplina(DisciplinasAproveitadasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            obj.setDisciplina(new DisciplinaVO());
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosCidade(DisciplinasAproveitadasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #excluirDisciplinasAproveitadass(java.lang.Integer)
     */
    public void excluirDisciplinasAproveitadass(AproveitamentoDisciplinaVO aproveitamentoDisciplina, UsuarioVO usuario) throws Exception {
        try {
            DisciplinasAproveitadas.excluir(getIdEntidade());
            // primeiro limpamos as disciplinas aproveitadas do aproveitamento. Assim, o método de consulta
            // abaixo irá retornar todas as disciplinasAproveitadas do aproveitamento, permitindo a exclusao
            // de cada um deles logo abaixo.
            aproveitamentoDisciplina.getDisciplinasAproveitadasVOs().clear();
            List<DisciplinasAproveitadasVO> listaDisciplinasAproveitadasVORemover = consultarDisciplinasAproveitadasRemovidasAproveitamento(aproveitamentoDisciplina, usuario);
            
            // Primeiramente, vamos tratar a exclusao de todas as disciplinas que foram aproveitas fora da grade
            // para que por meio de um mapa de equivalencia, outra disciplina da matricula fosse paga (aprovada para o aluno).
            // Quando se tratar de uma mapa de equivalencia, teremos que:
            //   a) Excluir somente o historico da disciplina cursada (fora da grade) nao será suficiente. Teremos que avaliar
            //      se outras aproveitamentos também serão excluídos para este mapa. 
            //   b) Ao final da exclusao de todos os historicos fora da grade que impactam no mapa, teremos que avaliar a situacao 
            //      final do mapa. Caso todas as disciplinas a serem cursadas sejam removidas, logo o aproveitamento gerado pelo
            //      mapa para a disciplina da matriz tambem deverá ser removido (ou seja, o historico da disciplina da matriz tambem
            //      terá que ser removido.
            //   c) Caso nem todas as disciplinas do mapa de equivalencia tenham sido removidas, entao teremos na verdade que alterar
            //      a situacao do historico do alnuo que estava aprovado por equivalencia para cursando por equivalencia. Pois o mapa
            //      que estava resolvido (caso o mesmo esteja resolvido mesmo) passe a ficar como parcialmente comprido.
            //  Após tratar estes casos especiais (tanto para disciplinas comuns quanto para grupo de optativas, seguimos com a exclusao
            //     abaixo dos demais aproveitamentos.
            
            // Como um mapa de equivalencia por envolver varias disciplinas, entao vamos utilizar esta lista para controlar, disciplinasaproveitadas
            // que já forem processadas por meio do mapa, evitando uma nova interacao seja executada de forma desnecessária.
            List<DisciplinasAproveitadasVO> listaDisciplinasAproveitadasJaProcessadas = new ArrayList<DisciplinasAproveitadasVO>(0);
            for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : listaDisciplinasAproveitadasVORemover) {
            	if (!disciplinasAproveitadasVO.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(0)) {
            		
            		boolean disciplinasAproveitadaJaProcessada = false;
            		for (DisciplinasAproveitadasVO disciplinaAproveitaJaProcessadas : listaDisciplinasAproveitadasJaProcessadas) {
            			if ((disciplinasAproveitadasVO.getDisciplina().getCodigo().equals(disciplinaAproveitaJaProcessadas.getDisciplina().getCodigo())) &&
            			    (disciplinasAproveitadasVO.getCargaHoraria().equals(disciplinaAproveitaJaProcessadas.getCargaHoraria()))) {
            				disciplinasAproveitadaJaProcessada = true;
            			}
            		}
            		
            		if (!disciplinasAproveitadaJaProcessada) {
	            		listaDisciplinasAproveitadasJaProcessadas.add(disciplinasAproveitadasVO);
	            		// encontramos uma disciplina que foi aproveitada somente para cumprir um mapa, logo vamos processar
	            		// esse e mapa.
	            		MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada = getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMapaEquivalenciaDisciplinaCursada().getCodigo());
	            		disciplinasAproveitadasVO.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursada);
	            		MapaEquivalenciaDisciplinaVO mapaEquivalencia = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(mapaEquivalenciaDisciplinaCursada.getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS);
	
	            		boolean todasDisciplinasASeremCursadasMapaSeraoRemovidas = true;
	            		
	            		if (mapaEquivalencia.getMapaEquivalenciaDisciplinaCursadaVOs().size() > 1) {
	            			// se o mapa for somente de uma disciplina cursada, como o localizamos por meio desta disciplina cursada,
	            			// nao temos nada o que fazer. Já podemos seguir a logica para excluir o historico da disciplina da matriz.
	            			for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalencia.getMapaEquivalenciaDisciplinaCursadaVOs()) {
	            				boolean encontrouDisciplinaCursadaDoMapaNoAproveitamento = false;
	            				for (DisciplinasAproveitadasVO disciplinasAproveitadasBuscaVO : listaDisciplinasAproveitadasVORemover) {
	            					if ((mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().equals(disciplinasAproveitadasBuscaVO.getDisciplina().getCodigo())) &&
	            					    (mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria().equals(disciplinasAproveitadasBuscaVO.getCargaHoraria()))) {
	            						listaDisciplinasAproveitadasJaProcessadas.add(disciplinasAproveitadasBuscaVO);
	            						encontrouDisciplinaCursadaDoMapaNoAproveitamento = true;
	            					}
	            				}
	            				if (!encontrouDisciplinaCursadaDoMapaNoAproveitamento) {
	            					todasDisciplinasASeremCursadasMapaSeraoRemovidas = false;
	            					break;
	            				}
	            			}
	            		}
	            		
	            		if (todasDisciplinasASeremCursadasMapaSeraoRemovidas) {
	            			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaEquivalencia.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
	            				// como todas as disciplinas a serem estudas do mapa estao sendo removidas, entao vamos excluir os historicos do mapa referentes as disciplinas
	            				// da matriz. Já os históricos das disciplinas cursadas, serao removidas pela rotina abaixo, assim que o disciplinaaproveitamento for excluido.
	            				getFacadeFactory().getHistoricoFacade().excluirHistoricoPorMatriculaCodigoDisciplinaAproveitamentoEquivalencia(aproveitamentoDisciplina.getMatricula().getMatricula(), mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo(), usuario);
	            			}
	            		} else {
	                		getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().carregarHistoricosMapaEquivalenciaParaAvaliacaoEResolucao(aproveitamentoDisciplina.getMatricula().getMatricula(), aproveitamentoDisciplina.getGradeCurricular().getCodigo(), mapaEquivalencia, disciplinasAproveitadasVO.getHistoricoAtual().getNumeroAgrupamentoEquivalenciaDisciplina(), 0, usuario);
	            			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaEquivalencia.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
	            				// como NEM todas as disciplinas a serem estudas do mapa estao sendo removidas, entao vamos ter que somente alterar a situacao das disciplinas 
	            				// da matriz, voltando as mesmas para cursando por equivalencia. Assim o aluno poderá em uma segunda oportunidade pagar as disciplinas
	            				// a ser cursada do mapa e finalizar sua execucao.
	            				if (mapaEquivalenciaDisciplinaMatrizCurricularVO.getHistorico().getAprovado()) {
	            					getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(mapaEquivalenciaDisciplinaMatrizCurricularVO.getHistorico().getCodigo(), SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor(), usuario);
	            				}
	            			}
	            		}
            		}
            	}
            }
            
            for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : listaDisciplinasAproveitadasVORemover) {
            	if (disciplinasAproveitadasVO.getHistoricoAtual().getHistoricoDisciplinaComposta()) {
            		// caso a disciplina seja composta, temos que garantir que os historicos das disciplinas que fazem parte da composicao,
            		// tambem sejam excluídos (caso os mesmos existam). Uma vez que o aproveitamento da disciplina mae, agora, pode ter sido criado por meio
            		// do aproveitamento das filhas da composicao. Recurso implementado na versao 5.0.6.x (25/08/2016).
            		List<GradeDisciplinaCompostaVO> disciplinasFazParteComposicao = null; 
            		if (disciplinasAproveitadasVO.getHistoricoAtual().getDisciplinaReferenteAUmGrupoOptativa()) {
            			disciplinasFazParteComposicao = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGrupoOptativaDisciplina(disciplinasAproveitadasVO.getHistoricoAtual().getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
            		} else {
            			disciplinasFazParteComposicao = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGradeDisciplina(disciplinasAproveitadasVO.getHistoricoAtual().getGradeDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
            		}
            		for (GradeDisciplinaCompostaVO disciplinaFazParteComposicao: disciplinasFazParteComposicao) {
        				getFacadeFactory().getHistoricoFacade().excluirHistoricoPorMatriculaCodigoDisciplinaAproveitamentoEquivalencia(aproveitamentoDisciplina.getMatricula().getMatricula(), disciplinaFazParteComposicao.getDisciplina().getCodigo(), usuario);
            		}
            	}
                this.excluir(aproveitamentoDisciplina.getMatricula().getMatricula(), disciplinasAproveitadasVO, usuario);
            }

        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * HOMOLOGADO VERSAO 5.0
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisciplinasAproveitadasPrevistasParaEfetiva(AproveitamentoDisciplinaVO aproveitamentoDisciplina, String periodicidadeCurso, List<DisciplinasAproveitadasVO> objetos, UsuarioVO usuario) throws Exception {
        try {
            for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : aproveitamentoDisciplina.getDisciplinasAproveitadasVOs()) {
                alterarAproveitamentoPrevistoParaEfetivo(disciplinasAproveitadasVO, periodicidadeCurso, usuario);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * HOMOLOGADO VERSAO 5.0
     * Método responsável por alterar as DisciplinasAproveitasVO de um AproveitamentoDisciplina,
     * removendo as disciplinasAproveitadas (com seus históricos) que foram removidos pelo usuário
     * e persistindo os demais objetos de disciplinasAproveitadas mantidas pelo usuário.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisciplinasAproveitadass(AproveitamentoDisciplinaVO aproveitamentoDisciplina, String periodicidadeCurso, List<DisciplinasAproveitadasVO> objetos, UsuarioVO usuario) throws Exception {
        try {
            List<DisciplinasAproveitadasVO> listaDisciplinasAproveitadasVORemovidas = consultarDisciplinasAproveitadasRemovidasAproveitamento(aproveitamentoDisciplina, usuario);
            for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : listaDisciplinasAproveitadasVORemovidas) {
                this.excluir(aproveitamentoDisciplina.getMatricula().getMatricula(), disciplinasAproveitadasVO, usuario);
            }
            for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : aproveitamentoDisciplina.getDisciplinasAproveitadasVOs()) {
            	disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplina.getCodigo());
            	disciplinasAproveitadasVO.setAproveitamentoDisciplinaVO(aproveitamentoDisciplina);
                if (disciplinasAproveitadasVO.getCodigo().equals(0)) {
                    incluir(disciplinasAproveitadasVO, periodicidadeCurso, usuario);
                } else {
                    alterar(disciplinasAproveitadasVO, periodicidadeCurso, usuario);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #incluirDisciplinasAproveitadass(java.lang.Integer,
     * java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDisciplinasAproveitadass(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, String periodicidadeCurso, List<DisciplinasAproveitadasVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<DisciplinasAproveitadasVO> e = objetos.iterator();
        while (e.hasNext()) {
            DisciplinasAproveitadasVO obj = (DisciplinasAproveitadasVO) e.next();
            obj.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
            obj.setAproveitamentoDisciplinaVO(aproveitamentoDisciplinaVO);
            incluir(obj, periodicidadeCurso, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os
     * <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code> relacionados a
     * um objeto da classe <code>academico.AproveitamentoDisciplina</code>.
     * 
     * @param AproveitamentoDisciplina
     *            Atributo de <code>academico.AproveitamentoDisciplina</code> a ser
     *            utilizado para localizar os objetos da classe
     *            <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>.
     * @return List Contendo todos os objetos da classe
     *         <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta
     *                operação.
     */
    public List<DisciplinasAproveitadasVO> consultarDisciplinasAproveitadass(Integer AproveitamentoDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DisciplinasAproveitadas.consultar(getIdEntidade());
        List<DisciplinasAproveitadasVO> objetos = new ArrayList<DisciplinasAproveitadasVO>(0);
        String sql = "SELECT * FROM DisciplinasAproveitadas WHERE AproveitamentoDisciplina = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{AproveitamentoDisciplina});
        while (tabelaResultado.next()) {
            DisciplinasAproveitadasVO novoObj = new DisciplinasAproveitadasVO();
            novoObj = DisciplinasAproveitadas.montarDados(tabelaResultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public DisciplinasAproveitadasVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DisciplinasAproveitadas WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( DisciplinasAproveitadas ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return DisciplinasAproveitadas.idEntidade;
    }

    /*
     * (non-Javadoc)
     * 
     * @seenegocio.facade.jdbc.academico.
     * AproveitamentoDisciplinaDisciplinasAproveitadasInterfaceFacade
     * #setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        DisciplinasAproveitadas.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(final Integer codigo, final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE DisciplinasAproveitadas set nota=?, frequencia=?, cargaHoraria=?, ano=?, semestre=?,"
            		+ " instituicao=?, cidade=?, cargaHorariaCursada=?, aproveitamentoPorIsencao=?, mediaFinalConceito=?,"
            		+ " utilizaNotaConceito=?, apresentarAprovadoHistorico=?, nomeprofessor=?, titulacaoprofessor=?, sexoprofessor=? "
            		+ " WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlAlterar.setDouble(++i, obj.getMedia());
                    sqlAlterar.setDouble(++i, obj.getFrequencia());
                    sqlAlterar.setInt(++i, obj.getCargaHoraria());
                    sqlAlterar.setString(++i, obj.getAno());
                    sqlAlterar.setString(++i, obj.getSemestre());
                    sqlAlterar.setString(++i, obj.getInstituicao());
                    sqlAlterar.setInt(++i, obj.getCidadeVO().getCodigo().intValue());
                    sqlAlterar.setInt(++i, obj.getCargaHorariaCursada());
                    sqlAlterar.setBoolean(++i, obj.getIsentarMediaFinal());
                    sqlAlterar.setString(++i, obj.getMediaFinalConceito());
                    sqlAlterar.setBoolean(++i, obj.getUtilizaNotaConceito());
                    sqlAlterar.setBoolean(++i, obj.getApresentarAprovadoHistorico());
                    sqlAlterar.setString(++i, obj.getNomeProfessor());
                    sqlAlterar.setString(++i, obj.getTitulacaoProfessor());
                    sqlAlterar.setString(++i, obj.getSexoProfessor());
                    sqlAlterar.setInt(++i, obj.getCodigoOrigem());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	public DisciplinasAproveitadasVO consultarAproveitamentoPorMatriculaDisciplina(String matricula,
			Integer codigoDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select DisciplinasAproveitadas.codigo, DisciplinasAproveitadas.nomeprofessor, DisciplinasAproveitadas.titulacaoprofessor, DisciplinasAproveitadas.sexoprofessor from DisciplinasAproveitadas ");
		sql.append(" inner join aproveitamentodisciplina on aproveitamentodisciplina.codigo = aproveitamentodisciplina");
		sql.append(" where aproveitamentodisciplina.matricula = ? and disciplina = ? order by DisciplinasAproveitadas.codigo desc limit 1");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, codigoDisciplina);
		DisciplinasAproveitadasVO obj = new DisciplinasAproveitadasVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNomeProfessor(tabelaResultado.getString("nomeprofessor"));
			obj.setTitulacaoProfessor(tabelaResultado.getString("titulacaoprofessor"));
			obj.setSexoProfessor(tabelaResultado.getString("sexoprofessor"));
		}

		return obj;
	}
}