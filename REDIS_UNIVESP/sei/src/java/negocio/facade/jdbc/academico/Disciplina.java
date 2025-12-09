package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import negocio.comuns.academico.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DisciplinaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>DisciplinaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>DisciplinaVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see DisciplinaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class Disciplina extends ControleAcesso implements DisciplinaInterfaceFacade {

    private static final long serialVersionUID = -7297266409995284803L;

    protected static String idEntidade;

    public Disciplina() throws Exception {
        super();
        setIdEntidade("Disciplina");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>DisciplinaVO</code>.
     */
    public DisciplinaVO novo() throws Exception {
        Disciplina.incluir(getIdEntidade());
        DisciplinaVO obj = new DisciplinaVO();
        return obj;
    }

    /**
     * @param atividadeDiscursivaVO
     * @param verificarAcesso
     * @param usuarioVO
     * @throws Exception
     * @author Victor Hugo 25/02/2015
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(DisciplinaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
        validarCargaHoraria(obj);
        if (obj.isNovoObj()) {
            incluir(obj, usuarioVO);
        } else {
            alterar(obj, usuarioVO);
        }
        getAplicacaoControle().removerDisciplina(obj.getCodigo());
    }

    /**
     * @param disciplinaVO
     * @throws ConsistirException
     * @author Victor Hugo 25/02/2015
     * <p>
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DisciplinaVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     */
    private void validarCargaHoraria(DisciplinaVO disciplinaVO) throws ConsistirException {
        disciplinaVO.setCont(0);
        Iterator<ConteudoPlanejamentoVO> i = disciplinaVO.getConteudoPlanejamentoVOs().iterator();
        while (i.hasNext()) {
            ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
            disciplinaVO.setCont(disciplinaVO.getCont() + objExistente.getCargahoraria().intValue());
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>DisciplinaVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe <code>DisciplinaVO</code> que será gravado no
     *            banco de dados.
     * @throws Exception Caso haja problemas de conexão, restrição de acesso ou
     *                   validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            DisciplinaVO.validarDados(obj);
            Disciplina.incluir(getIdEntidade(), usuario);
            incluir(obj, "disciplina", new AtributoPersistencia().add("nome", obj.getNome())
                            .add("nivelEducacional", obj.getNivelEducacional())
                            .add("abreviatura", obj.getAbreviatura())
                            .add("descricaoComplementar", obj.getDescricaoComplementar())
                            .add("classificacaoDisciplina", obj.getClassificacaoDisciplina())
//					.add("modeloGeracaoSalaBlackboard", obj.getModeloGeracaoSalaBlackboard())
                            .add("percentualMinimoCargaHorariaAproveitamento", obj.getPercentualMinimoCargaHorariaAproveitamento())
                            .add("qtdeMinimaDeAnosAproveitamento", obj.getQtdeMinimaDeAnosAproveitamento())
                            .add("dividirSalaEmGrupo", obj.getDividirSalaEmGrupo())
                            .add("nrMaximoAulosPorGrupo", obj.getDividirSalaEmGrupo() ? obj.getNrMaximoAulosPorGrupo() : 0)
                            .add("nrMinimoAlunosPorGrupo", obj.getDividirSalaEmGrupo() ? obj.getNrMinimoAlunosPorGrupo() : 0)
                            .add("nrMaximoAulosPorSala", obj.getNrMaximoAulosPorSala())
                            .add("nrMinimoAlunosPorSala", obj.getNrMinimoAlunosPorSala())
                            .add("nrMaximoAlunosPorAmbientacao", obj.getNrMaximoAlunosPorAmbientacao())
                            .add("fonteDeDadosBlackboard", obj.getFonteDeDadosBlackboard())
                            .add("idConteudoMasterBlackboard", obj.getIdConteudoMasterBlackboard())
                            .add("grupoPessoa", obj.getGrupoPessoaVO()),
                    usuario);
            getFacadeFactory().getDisciplinaEquivalenteFacade().incluirDisciplinaEquivalentes(obj.getCodigo(), obj.getDisciplinaEquivalenteVOs(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>DisciplinaVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj Objeto da classe <code>DisciplinaVO</code> que será alterada
     *            no banco de dados.
     * @throws Execption Caso haja problemas de conexão, restrição de acesso ou
     *                   validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {

            DisciplinaVO.validarDados(obj);
            Disciplina.alterar(getIdEntidade(), usuario);

            alterar(obj, "disciplina", new AtributoPersistencia().add("nome", obj.getNome())
                            .add("nivelEducacional", obj.getNivelEducacional())
                            .add("abreviatura", obj.getAbreviatura())
                            .add("descricaoComplementar", obj.getDescricaoComplementar())
                            .add("classificacaoDisciplina", obj.getClassificacaoDisciplina())
//					.add("modeloGeracaoSalaBlackboard", obj.getModeloGeracaoSalaBlackboard())
                            .add("percentualMinimoCargaHorariaAproveitamento", obj.getPercentualMinimoCargaHorariaAproveitamento())
                            .add("qtdeMinimaDeAnosAproveitamento", obj.getQtdeMinimaDeAnosAproveitamento())
                            .add("dividirSalaEmGrupo", obj.getDividirSalaEmGrupo())
                            .add("nrMaximoAulosPorGrupo", obj.getDividirSalaEmGrupo() ? obj.getNrMaximoAulosPorGrupo() : 0)
                            .add("nrMinimoAlunosPorGrupo", obj.getDividirSalaEmGrupo() ? obj.getNrMinimoAlunosPorGrupo() : 0)
                            .add("nrMaximoAulosPorSala", obj.getNrMaximoAulosPorSala())
                            .add("nrMinimoAlunosPorSala", obj.getNrMinimoAlunosPorSala())
                            .add("nrMaximoAlunosPorAmbientacao", obj.getNrMaximoAlunosPorAmbientacao())
                            .add("fonteDeDadosBlackboard", obj.getFonteDeDadosBlackboard())
                            .add("idConteudoMasterBlackboard", obj.getIdConteudoMasterBlackboard())
                            .add("grupoPessoa", obj.getGrupoPessoaVO()),
                    new AtributoPersistencia().add("codigo", obj.getCodigo()),
                    usuario);

            getFacadeFactory().getDisciplinaEquivalenteFacade().alterarDisciplinaEquivalentes(obj.getCodigo(), obj.getDisciplinaEquivalenteVOs(), usuario);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>DisciplinaVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj Objeto da classe <code>DisciplinaVO</code> que será removido
     *            no banco de dados.
     * @throws Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            Disciplina.excluir(getIdEntidade(), usuario);
            if (obj.getCodigo() != null && obj.getCodigo() > 0) {
                SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select distinct turma.identificadorturma, horarioturma.anovigente, horarioturma.semestrevigente from horarioturmadetalhado(null, null, " + obj.getCodigo() + ", null) ht inner join turma on turma.codigo = ht.turma inner join horarioturma on horarioturma.codigo = ht.horarioturma limit 1");
                if (rs.next()) {
                    String turma = rs.getString("identificadorturma");
                    if (!rs.getString("anovigente").trim().isEmpty()) {
                        turma += " - " + rs.getString("anovigente");
                        if (!rs.getString("semestrevigente").trim().isEmpty()) {
                            turma += "/" + rs.getString("semestrevigente");
                        }
                    }
                    throw new Exception(UteisJSF.internacionalizar("msg_Disciplina_naoFoiPossivelExclusaoDiciplina") + " (" + turma + ").");
                }
            }

            getFacadeFactory().getDisciplinaEquivalenteFacade().excluirDisciplinaEquivalentes(obj.getCodigo(), usuario);
            getFacadeFactory().getDisciplinaCompostaFacade().excluirDisciplinaComposta(obj.getCodigo(), usuario);
            String sql = "DELETE FROM Disciplina WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDisciplinaUnificacaoDisciplina(DisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            Disciplina.excluir(getIdEntidade());
            getFacadeFactory().getDisciplinaEquivalenteFacade().excluirDisciplinaEquivalentes(obj.getCodigo(), usuario);
            getFacadeFactory().getDisciplinaCompostaFacade().excluirDisciplinaComposta(obj.getCodigo(), usuario);
            String sql = "DELETE FROM Disciplina WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            // //System.out.print(" ===> " + e.getMessage());
        }
    }

    public List<DisciplinaVO> consultarDisciplinasOptativasCumpridasDaGrade(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if (Uteis.isAtributoPreenchido(gradeCurricular) && Uteis.isAtributoPreenchido(matricula)) {
            String sqlStr = "select disciplina.* " + "from gradedisciplina " + "inner join disciplina on disciplina.codigo = gradedisciplina.disciplina " + "inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo " + "where periodoletivo.gradecurricular = ? and disciplina.codigo in ( " + "select disciplina.codigo from historico " + "inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo " + "inner join matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo " + "inner join gradecurricular on matriculaperiodo.gradecurricular = gradecurricular.codigo " + "inner join disciplina on (disciplina.codigo = historico.disciplina or (case when (mptd.disciplinaequivale = true)then disciplina.codigo = mptd.disciplinaequivalente end)) " + "where historico.matricula = ? and " + "(historico.situacao = ? or historico.situacao = ?)) and gradedisciplina.tipoDisciplina = 'OP'";
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{gradeCurricular, matricula, SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), SituacaoHistorico.APROVADO.getValor()});
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } else {
            throw new ConsistirException("É necessário informar a Matricula e a Grade Curricular do aluno para listar as Disciplinas Pendentes.");
        }
    }

    public List<DisciplinaVO> consultarDisciplinasNaoCumpridasDaGrade(String matricula, Integer gradeCurricular, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<Object> listaFiltros = new ArrayList<>();
        listaFiltros.add(gradeCurricular);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select disciplina.* from gradedisciplina ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
        sqlStr.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" where periodoletivo.gradecurricular = ?   ");
        if (Uteis.isAtributoPreenchido(periodoLetivo)) {
            sqlStr.append(" and periodoletivo.codigo = ?  ");
            listaFiltros.add(periodoLetivo);
        }
        sqlStr.append(" and (disciplina.codigo not in ( ");
        sqlStr.append(" select disciplina.codigo from historico ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
        sqlStr.append(" where historico.matricula = ? and (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) ");
        sqlStr.append(" and disciplina.codigo not in( ");
        sqlStr.append(" select disciplinaequivalente.equivalente from historico ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sqlStr.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina");
        sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))))");
        listaFiltros.add(matricula);
        listaFiltros.add(matricula);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

    }

    public List<DisciplinaVO> consultarDisciplinasCumpridasDaGrade(String matricula, Integer gradeCurricular, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if (Uteis.isAtributoPreenchido(gradeCurricular) && Uteis.isAtributoPreenchido(matricula) && Uteis.isAtributoPreenchido(periodoLetivo)) {
            StringBuilder sqlStr = new StringBuilder();
            sqlStr.append(" select disciplina.* from gradedisciplina ");
            sqlStr.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
            sqlStr.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo ");
            sqlStr.append(" where periodoletivo.gradecurricular = ? and periodoletivo.codigo = ? and (disciplina.codigo in ( ");
            sqlStr.append(" select disciplina.codigo from historico ");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
            sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) or disciplina.codigo in(");
            sqlStr.append(" select disciplinaequivalente.equivalente from historico ");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            sqlStr.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina");
            sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))))");
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{gradeCurricular, periodoLetivo, matricula, matricula});
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } else {
            throw new ConsistirException("É necessário informar o Período Letivo, a Matricula, e a Grade Curricular do aluno para listar as Disciplinas Pendentes.");
        }
    }

    public List<DisciplinaVO> consultarDisciplinasPreRequisitoNaoCumpridasDaGrade(String matricula, Integer disciplina, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if (Uteis.isAtributoPreenchido(disciplina) && Uteis.isAtributoPreenchido(gradeCurricular)) {
            StringBuilder sqlStr = new StringBuilder();
            sqlStr.append(" select disciplinapre.* from disciplinaprerequisito");
            sqlStr.append(" inner join disciplina as disciplinapre on disciplinaprerequisito.disciplina = disciplinapre.codigo");
            sqlStr.append(" INNER JOIN gradedisciplina ON gradedisciplina.codigo = disciplinaprerequisito.gradedisciplina");
            sqlStr.append(" INNER JOIN periodoletivo ON periodoletivo.codigo = gradedisciplina.periodoletivo");
            sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = gradedisciplina.disciplina");
            sqlStr.append(" where periodoletivo.gradecurricular = ? AND gradedisciplina.disciplina = ? and disciplinapre.codigo not in (");
            sqlStr.append(" select disciplina.codigo from historico");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo");
            sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina");
            sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) and disciplina.codigo not in(");
            sqlStr.append(" select disciplinaequivalente.equivalente from historico");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo");
            sqlStr.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina");
            sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')))");
            sqlStr.append(" union ");
            sqlStr.append(" select disciplinapre.* from disciplinaprerequisito");
            sqlStr.append(" inner join disciplina as disciplinapre on disciplinaprerequisito.disciplina = disciplinapre.codigo");
            sqlStr.append(" INNER JOIN gradecurriculargrupooptativadisciplina ON gradecurriculargrupooptativadisciplina.codigo = disciplinaprerequisito.gradecurriculargrupooptativadisciplina");
            sqlStr.append(" INNER JOIN gradecurriculargrupooptativa ON gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa");
            sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina");
            sqlStr.append(" where gradecurriculargrupooptativa.gradecurricular = ? AND gradecurriculargrupooptativadisciplina.disciplina = ? and disciplinapre.codigo not in (");
            sqlStr.append(" select disciplina.codigo from historico");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo");
            sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina");
            sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) and disciplina.codigo not in(");
            sqlStr.append(" select disciplinaequivalente.equivalente from historico");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo");
            sqlStr.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina");
            sqlStr.append(" where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')))");
            sqlStr.append("  union  ");
            sqlStr.append(" select disciplinapre.* from disciplinaprerequisito ");
            sqlStr.append("  inner join disciplina as disciplinapre on disciplinaprerequisito.disciplina = disciplinapre.codigo ");
            sqlStr.append("  INNER JOIN gradedisciplinacomposta ON disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo ");
            sqlStr.append("  INNER JOIN gradedisciplina ON gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
            sqlStr.append("  INNER JOIN periodoletivo ON periodoletivo.codigo = gradedisciplina.periodoletivo ");
            sqlStr.append("  INNER JOIN disciplina ON disciplina.codigo = gradedisciplinacomposta.disciplina ");
            sqlStr.append("  where periodoletivo.gradecurricular = ? ");
            sqlStr.append("  AND gradedisciplinacomposta.disciplina = ? ");
            sqlStr.append("  and disciplinapre.codigo not in ");
            sqlStr.append("  (select disciplina.codigo from historico inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            sqlStr.append("  inner join disciplina on disciplina.codigo = historico.disciplina where historico.matricula = ? ");
            sqlStr.append("  and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))");
            sqlStr.append("  ) ");
            sqlStr.append("  and disciplina.codigo not in");
            sqlStr.append("  (select disciplinaequivalente.equivalente from historico inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            sqlStr.append("  inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina ");
            sqlStr.append("  where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))");
            sqlStr.append("  ) ");
            sqlStr.append("  union  ");
            sqlStr.append("");
            sqlStr.append("  select disciplinapre.* from disciplinaprerequisito ");
            sqlStr.append("  inner join disciplina as disciplinapre on disciplinaprerequisito.disciplina = disciplinapre.codigo ");
            sqlStr.append("  INNER JOIN gradedisciplinacomposta ON disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo ");
            sqlStr.append("  INNER JOIN gradecurriculargrupooptativadisciplina ON gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
            sqlStr.append("  INNER JOIN gradecurriculargrupooptativa ON gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
            sqlStr.append("  INNER JOIN periodoletivo ON periodoletivo.codigo = gradedisciplinacomposta.periodoletivo ");
            sqlStr.append("  INNER JOIN disciplina ON disciplina.codigo = gradedisciplinacomposta.disciplina ");
            sqlStr.append("  where periodoletivo.gradecurricular = ? ");
            sqlStr.append("  AND gradedisciplinacomposta.disciplina = ? ");
            sqlStr.append("  and disciplinapre.codigo not in ");
            sqlStr.append("  (select disciplina.codigo from historico inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            sqlStr.append("  inner join disciplina on disciplina.codigo = historico.disciplina where historico.matricula = ? ");
            sqlStr.append("  and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))");
            sqlStr.append("  ) ");
            sqlStr.append("  and disciplina.codigo not in");
            sqlStr.append("  (select disciplinaequivalente.equivalente from historico inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            sqlStr.append("  inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina ");
            sqlStr.append("  where historico.matricula = ? and  (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))");
            sqlStr.append("  ) ");

            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{gradeCurricular, disciplina, matricula, matricula, gradeCurricular, disciplina, matricula, matricula, gradeCurricular, disciplina, matricula, matricula, gradeCurricular, disciplina, matricula, matricula});
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } else {
            throw new ConsistirException("É necessário informar o Período Letivo e a Grade Curricular do aluno para listar as Disciplinas Pendentes.");
        }
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinasAptasAproveitamentoDisciplina(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoAcademicoVO configuracaoAcademicoVO, TipoRequerimentoVO tipoRequerimentoVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<Object> listaFiltros = new ArrayList<>();
        listaFiltros.add(gradeCurricular);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select disciplina.* from gradedisciplina ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
        sqlStr.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" where periodoletivo.gradecurricular = ?   ");
        sqlStr.append(" and disciplina.classificacaoDisciplina not in ('").append(ClassificacaoDisciplinaEnum.ESTAGIO).append("','").append(ClassificacaoDisciplinaEnum.TCC).append("')");
        sqlStr.append(" and (disciplina.codigo not in ( ");
        sqlStr.append(" select disciplina.codigo from historico ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
        sqlStr.append(" where historico.matricula = ? ");
        if (tipoRequerimentoVO.getIsTipoAproveitamentoDisciplina() && !tipoRequerimentoVO.getPermitirAproveitarDisciplinaCursando()) {
            sqlStr.append(" and (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE', 'CS', 'CE'))) ");
        } else {
            sqlStr.append(" and (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) ");
        }
        sqlStr.append(" and disciplina.codigo not in( ");
        sqlStr.append(" select disciplinaequivalente.equivalente from historico ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sqlStr.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = historico.disciplina");
        sqlStr.append(" where historico.matricula = ? ");
        if (tipoRequerimentoVO.getIsTipoAproveitamentoDisciplina() && !tipoRequerimentoVO.getPermitirAproveitarDisciplinaCursando()) {
            sqlStr.append(" and (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE', 'CS', 'CE'))) )");
        } else {
            sqlStr.append(" and (historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')))) ");
        }
        if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO) && !configuracaoAcademicoVO.getPermitirAproveitamentoDisciplinasOptativas()) {
            sqlStr.append(" and gradedisciplina.tipodisciplina <> 'OP'  ");
        }
        listaFiltros.add(matricula);
        listaFiltros.add(matricula);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltros.toArray());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

    }

    /**
     * Responsável por realizar uma consulta de <code>Disciplina</code> através
     * do valor do atributo <code>String nome</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui
     *                        permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DisciplinaVO</code>
     * resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DisciplinaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE lower (sem_acentos(nome)) like lower(sem_acentos(?)) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT + valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Consuta as disciplinas utilizando {@link DataModelo} com limit e offset
     *
     * @param dataModelo
     * @throws Exception
     */
    @Override
    public void consultar(DataModelo dataModelo) throws Exception {
        dataModelo.setListaConsulta(consultarDisciplinas(dataModelo));
        dataModelo.setTotalRegistrosEncontrados(consultarTotalDisciplinas(dataModelo));
    }

    private List<DisciplinaVO> consultarDisciplinas(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM Disciplina ");
        sql.append(" WHERE lower (sem_acentos(nome)) like lower(sem_acentos(?)) ORDER BY nome");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), PERCENT + dataModelo.getValorConsulta() + PERCENT);

        return montarDadosLista(tabelaResultado);
    }

    private Integer consultarTotalDisciplinas(DataModelo dataModelo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(codigo) as qtde FROM Disciplina ");
        sql.append(" WHERE lower (sem_acentos(nome)) like lower(sem_acentos(?))");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), PERCENT + dataModelo.getValorConsulta() + PERCENT);
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

    @Override
    public Double consultarPercentualMinimoCargaHorariaAproveitamento(Integer disciplina) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT percentualMinimoCargaHorariaAproveitamento FROM Disciplina  where codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), disciplina);
        return (Double) Uteis.getSqlRowSetTotalizador(tabelaResultado, "percentualMinimoCargaHorariaAproveitamento", TipoCampoEnum.DOUBLE);
    }

    @Override
    public Integer consultarQtdeMinimaDeAnosAproveitamento(Integer disciplina) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT qtdeMinimaDeAnosAproveitamento FROM Disciplina  where codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), disciplina);
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtdeMinimaDeAnosAproveitamento", TipoCampoEnum.INTEIRO);
    }

    /**
     * Monta a lista de {@link DisciplinaVO}.
     *
     * @param tabelaResultado
     * @return
     * @throws Exception
     */
    private List<DisciplinaVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
        List<DisciplinaVO> disciplinas = new ArrayList<>();

        while (tabelaResultado.next()) {
            disciplinas.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
        }
        return disciplinas;
    }

    public List<DisciplinaVO> consultarPorNome_Matricula(String nome, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorNome_Matricula_DisciplinaEquivalente(nome, matricula, controlarAcesso, nivelMontarDados, usuario);
    }

    public List<DisciplinaVO> consultarPorNome_Matricula_DisciplinaEquivalente(String nome, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append(" select distinct disciplina.* from curso");
        sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
        sb.append(" inner join matricula on matricula.gradecurricularatual = gradeCurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
        sb.append(" where gradedisciplina.disciplinacomposta = false ");
        sb.append(" and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?))");
        sb.append(" union");
        sb.append(" select distinct disciplina.* from curso");
        sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
        sb.append(" inner join matricula on matricula.gradecurricularatual = gradeCurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sb.append(" inner join disciplinaequivalente on gradedisciplina.disciplina = disciplinaequivalente.disciplina");
        sb.append(" inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente ");
        sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?)) ");
        sb.append(" union");
        sb.append(" select distinct disciplina.* from curso");
        sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
        sb.append(" inner join matricula on matricula.gradecurricularatual = gradeCurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradedisciplina = gradedisciplina.codigo");
        sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
        sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?))");
        sb.append(" union");
        sb.append(" select distinct disciplina.* from curso");
        sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
        sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sb.append(" inner join matricula on matricula.gradecurricularatual = gradeCurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
        sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta =  false and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?))");
        sb.append(" union ");
        sb.append(" select distinct disciplina.* from curso");
        sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
        sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sb.append(" inner join matricula on matricula.gradecurricularatual = gradeCurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina = gradecurriculargrupooptativadisciplina.codigo");
        sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
        sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?))");
        sb.append(" union ");
        sb.append(" select distinct disciplina.* from curso");
        sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
        sb.append(" inner join matricula on matricula.gradecurricularatual = gradeCurricular.codigo and matricula.matricula = '").append(matricula).append("' ");
        sb.append(" inner join mapaequivalenciamatrizcurricular on mapaequivalenciamatrizcurricular.gradecurricular = gradecurricular.codigo");
        sb.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
        sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
        sb.append(" inner join disciplina on disciplina.codigo = mapaequivalenciadisciplinacursada.disciplina");
        sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?))");
        sb.append(" order by nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), nome + "%", nome + "%", nome + "%", nome + "%", nome + "%", nome + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

    }

    @Override
    public List<DisciplinaVO> consultarPorNome_Matricula_DisciplinaEquivalenteEDisciplinaComposta(String nome, String matricula, Integer gradeCurricular, Integer periodoTurma, boolean trazerDisciplinaCompostaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder("select distinct disciplina.*, historico.anoHistorico, historico.semestreHistorico from historico ");
        sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
        sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
        sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
        sql.append(" where matricula.matricula = '").append(matricula).append("' ");
        sql.append(" and historico.situacao not in ('AA', 'CH', 'CC', 'IS') ");
        /**
         * Adicionada regra para resolver impactos relacionados a alunos que
         * estão Cursando por Correspondência e que disciplinas saiam duplicadas
         * no Boletim Acadêmico
         */
        if (!Uteis.isAtributoPreenchido(gradeCurricular)) {
            sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
        } else {
            sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularEspecifico(" and ", gradeCurricular));
        }
        sql.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        if (!trazerDisciplinaCompostaPrincipal) {
            sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
        }
        sql.append(" and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?)) ORDER BY disciplina.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nome + "%");
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO disciplinaVO = montarDados(tabelaResultado, nivelMontarDados, usuario);
            disciplinaVO.setAno(tabelaResultado.getString("anoHistorico"));
            disciplinaVO.setSemestre(tabelaResultado.getString("semestreHistorico"));
            vetResultado.add(disciplinaVO);
        }
        return vetResultado;
    }

    public List<DisciplinaVO> consultarPorNomeDisciplinaCurso(String nome, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct disciplina.* from disciplina inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo inner join curso on gradeCurricular.curso = curso.codigo where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos(?)) ";
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr += "and curso.codigo = " + curso;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, nome + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorNome_CursoComGrade(String nome, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "inner join curso on gradeCurricular.curso = curso.codigo " + "where lower(sem_acentos(disciplina.nome)) like (sem_acentos('" + nome.toLowerCase() + "%')) ";
        if ((curso != null && curso > 0)) {
            sqlStr += "and curso.codigo = " + curso;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorNome_CursoDisciplinaComposta(String nome, Integer curso, Integer periodoTurma, boolean trazerDisciplinaComposta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        if ((curso != null && curso > 0) || (periodoTurma != null && periodoTurma > 0)) {
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
            sb.append(" where gradedisciplina.disciplinacomposta = false ");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join disciplinaequivalente on gradedisciplina.disciplina = disciplinaequivalente.disciplina");
            sb.append(" inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente ");
            sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%')) ");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradedisciplina = gradedisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
            sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }

            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo and gradecurriculargrupooptativa.codigo = periodoletivo.gradecurriculargrupooptativa ");
            }
            sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
            sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta =  false and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" union ");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo and gradecurriculargrupooptativa.codigo = periodoletivo.gradecurriculargrupooptativa ");
            }
            sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina = gradecurriculargrupooptativadisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
            sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" union ");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join mapaequivalenciamatrizcurricular on mapaequivalenciamatrizcurricular.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
            sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
                sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
                sb.append(" inner join mapaequivalenciadisciplinamatrizcurricular on mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo and mapaequivalenciadisciplinamatrizcurricular.disciplina = gradedisciplina.disciplina ");
            }
            sb.append(" inner join disciplina on disciplina.codigo = mapaequivalenciadisciplinacursada.disciplina");
            sb.append(" where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }

            if (trazerDisciplinaComposta) {
                sb.append(" union ");
                sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
                sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
                sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
                sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
                sb.append(" where gradedisciplina.disciplinacomposta  and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
                if ((curso != null && curso > 0)) {
                    sb.append(" and curso.codigo = ").append(curso);
                }
                if ((periodoTurma != null && periodoTurma > 0)) {
                    sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
                }
                sb.append(" union ");
                sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
                sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
                sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
                sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
                sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina");
                sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta and lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%'))");
                if ((curso != null && curso > 0)) {
                    sb.append(" and curso.codigo = ").append(curso);
                }
                if ((periodoTurma != null && periodoTurma > 0)) {
                    sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
                }
            }
        } else {
            sb.append("select *, '' as gradenome from disciplina where lower(sem_acentos(disciplina.nome)) like lower(sem_acentos('").append(nome).append("%')) ");
        }
        sb.append(" order by nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorCodigo_CursoDisciplinaComposta(Integer disciplina, Integer curso, boolean trazerDisciplinaComposta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        if (curso != null && curso > 0) {
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
            sb.append(" where gradedisciplina.disciplinacomposta = false and curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradedisciplina = gradedisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
            sb.append(" where curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join disciplinaequivalente on gradedisciplina.disciplina = disciplinaequivalente.disciplina");
            sb.append(" inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente ");
            sb.append(" where disciplina.codigo = ").append(disciplina).append(" ");
            sb.append(" and curso.codigo = ").append(curso);
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina");
            sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta =  false and curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
            sb.append(" union ");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina = gradecurriculargrupooptativadisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
            sb.append(" where curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
            sb.append(" union ");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join mapaequivalenciamatrizcurricular on mapaequivalenciamatrizcurricular.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
            sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = mapaequivalenciadisciplinacursada.disciplina");
            sb.append(" where curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
            if (trazerDisciplinaComposta) {
                sb.append(" union ");
                sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
                sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
                sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
                sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
                sb.append(" where gradedisciplina.disciplinacomposta and curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
                sb.append(" union ");
                sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
                sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
                sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
                sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
                sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina");
                sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta and curso.codigo = ").append(curso).append(" and disciplina.codigo = ").append(disciplina);
            }
        } else {
            sb.append("select *, '' as gradenome from disciplina where codigo = ").append(disciplina);
        }
        sb.append(" order by codigo ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    @Deprecated
    public List<DisciplinaVO> consultarPorNomeDisciplina(String nome, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "inner join matriculaPeriodoTurmaDisciplina as mptd on mptd.disciplina = disciplina.codigo " + "inner join matricula on mptd.matricula = matricula.matricula " + "inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo " + "where lower(sem_acentos(disciplina.nome)) like (sem_acentos('" + nome.toLowerCase() + "%')) ";
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
        }
        sqlStr += "order by disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    @Deprecated
    public List<DisciplinaVO> consultarPorCodigoDisciplina(Integer codigo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "inner join matriculaPeriodoTurmaDisciplina as mptd on mptd.disciplina = disciplina.codigo " + "inner join matricula on mptd.matricula = matricula.matricula " + "inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo " + "where disciplina.codigo = " + codigo.intValue() + " ";
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
        }
        sqlStr += " order by disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    @Deprecated
    public List<DisciplinaVO> consultarPorNome(String valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE lower (sem_acentos(nome)) like(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct disciplina.* from turma as t1, turmaAgrupada, turma as t2, PeriodoLetivo, GradeDisciplina, Disciplina");
        sqlStr.append(" where t1.codigo = turmaAgrupada.turmaOrigem and t2.codigo = turmaAgrupada.turma and t2.PeriodoLetivo = PeriodoLetivo.codigo ");
        sqlStr.append(" and GradeDisciplina.periodoLetivo = periodoletivo.codigo and  GradeDisciplina.Disciplina =  Disciplina.codigo ");
        sqlStr.append(" and t1.codigo = ").append(codigo);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarDisciplinaProfessorTurma(Integer professor, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarDisciplinaProfessorTurmaAgrupada(professor, turma, null, null, controlarAcesso, nivelMontarDados, usuario, true);
        // ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        // String sqlStr =
        // "SELECT distinct Disciplina.* FROM Disciplina, HorarioTurmaProfessorDisciplina, Pessoa, Turma WHERE HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo "
        // + " and Pessoa.codigo  = " + professor.intValue() +
        // " and HorarioTurmaProfessorDisciplina.turma = Turma.codigo and HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo"
        // + " and (Turma.codigo = " + turma.intValue() +
        // " OR turma.codigo in(select ta.turmaorigem from turmaagrupada ta " +
        // " inner join turmadisciplina td on td.turma = ta.turmaorigem " +
        // " inner join disciplina dis1 on td.disciplina = dis1.codigo " +
        // " left join disciplinacomposta on disciplinacomposta.disciplina = dis1.codigo "
        // + " where ta.turma = " + turma.intValue() +
        // " and (dis1.codigo =  disciplina.codigo or disciplinacomposta.composta = disciplina.codigo) )) "
        // + "ORDER BY Disciplina.nome";
        // SqlRowSet tabelaResultado =
        // getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        // return (montarDadosConsulta(tabelaResultado, nivelMontarDados,
        // usuario));
    }

	/*public List consultarDisciplinaProfessorTurmaAgrupada(Integer professor, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		return consultarDisciplinaProfessorTurmaAgrupada(professor, turma, null, null, controlarAcesso, nivelMontarDados, usuario);
		// String sqlStr =
		// "SELECT distinct Disciplina.* FROM Disciplina, HorarioTurmaProfessorDisciplina, Pessoa, Turma "
		// + " WHERE Pessoa.codigo = " + professor.intValue() +
		// " and Turma.codigo = " + turma.intValue() +
		// " and HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo and HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo"
		// +
		// " and (HorarioTurmaProfessorDisciplina.turma = Turma.codigo OR HorarioTurmaProfessorDisciplina.turma"
		// +
		// " IN(SELECT ta.turmaorigem FROM turmaagrupada ta INNER JOIN turmadisciplina td ON td.turma = ta.turmaorigem "
		// + " inner join disciplina dis1 on td.disciplina = dis1.codigo " +
		// " left join disciplinacomposta on disciplinacomposta.disciplina = dis1.codigo "
		// + " WHERE ta.turma = " + turma.intValue() +
		// " and (dis1.codigo =  disciplina.codigo or disciplinacomposta.composta = disciplina.codigo)  ))"
		// + " ORDER BY Disciplina.nome";
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		// return (montarDadosConsulta(tabelaResultado, nivelMontarDados,
		// usuario));
	}*/

    public List<DisciplinaVO> consultarDisciplinaProfessorTurmaAgrupada(Integer professor, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Boolean trazerTurmaBaseTurmaAgrupada) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" select distinct disciplina.* from horarioturma ");
        sqlStr.append(" inner join turma on horarioturma.turma = turma.codigo ");
        sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
        sqlStr.append(" where horarioturmadiaitem.professor = ").append(professor);
        sqlStr.append(" and (((turma.anual or turma.semestral) and horarioturma.anovigente = '").append(ano).append("') ");
        sqlStr.append(" or (turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("') ");
        sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
        sqlStr.append(" and (turma.codigo = ").append(turma);
        /*		A regra abaixo foi adicionado pois na tela de registro aula/nota estava permitindo registrar aula nota para a turma agrupada e a turma base,
         * 		gerando registros órfãos na base. Chamado número 16427*/

        if (trazerTurmaBaseTurmaAgrupada) {
            sqlStr.append(" or turma.codigo in ( select turmaorigem from turmaagrupada where turma = " + turma + " )) ");
        } else {
            sqlStr.append(" )");
        }
        sqlStr.append(" ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Este método retorna as disciplina que um determinado professor da aula
     * considerando as seguintes regras: 1 - Busca na turma normal as
     * disciplinas que o professor dá aula de acordo com a periodicidade do
     * curso validando na programação de aula 2 - Busca na turma agrupada as
     * disciplinas que o professor dá aula de acordo com a periodicidade do
     * curso validando na programação de aula se a disciplina programa é a mesma
     * disciplina da turma normal 3 - Busca a disciplina da turma de origem com
     * base na turma agrupada as disciplinas que o professor dá aula de acordo
     * com a periodicidade do curso validando na programação de aula se a
     * disciplina programa está na lista de equivalencia (disciplina
     * equivalencia ) na qual a disciplina da turma normal está vinculada
     */
    public List<DisciplinaVO> consultaRapidaPorDisciplinaProfessorTurmaAgrupada(Integer professor, Integer turma, String semestre, String ano, Boolean liberarRegistroAulaEntrePeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" select distinct disciplina.codigo, disciplina.nome, false  as disciplinacomposta from horarioturma ");
        sqlStr.append(" inner join turma on horarioturma.turma = turma.codigo ");
        sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
        sqlStr.append(" where horarioturmadiaitem.professor = ").append(professor);
        sqlStr.append(" and ((turma.anual and horarioturma.anovigente = '").append(ano).append("') ");
        sqlStr.append(" or (turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("') ");
        sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
        sqlStr.append(" and (turma.codigo = ").append(turma).append(" or turma.codigo in ( select turmaorigem from turmaagrupada where turma = " + turma + " )) ");
        /**
         * Este union é para resolver casos antigo que não possuem programação de aula mais ou de importação, porém existe registro de aula
         */
        sqlStr.append(" union ");
        sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, false  as disciplinacomposta from registroaula ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = registroaula.disciplina ");
        sqlStr.append(" inner join turma on turma.codigo = registroaula.turma ");
        sqlStr.append(" where registroaula.turma = ").append(turma);
        sqlStr.append(" and professor = ").append(professor);
        sqlStr.append(" and ((turma.anual and registroaula.ano = '").append(ano).append("') ");
        sqlStr.append(" or (turma.semestral and registroaula.ano = '").append(ano).append("' and registroaula.semestre = '").append(semestre).append("') ");
        sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
        sqlStr.append(" ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaBasica(tabelaResultado, usuario);
    }

    public List<DisciplinaVO> consultaRapidaPorProfessorTurma(Integer professor, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct Disciplina.codigo, Disciplina.nome, disciplina.disciplinacomposta FROM Disciplina, HorarioTurmaProfessorDisciplina, Pessoa, Turma WHERE HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo " + " and Pessoa.codigo  = " + professor.intValue() + " and HorarioTurmaProfessorDisciplina.turma = Turma.codigo and HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo" + " and (Turma.codigo = " + turma.intValue() + " OR turma.codigo in(select ta.turmaorigem from turmaagrupada ta " + " inner join turmadisciplina td on td.turma = ta.turmaorigem and td.disciplina = disciplina.codigo " + " where ta.turma = " + turma.intValue() + " )) " + "ORDER BY Disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaBasica(tabelaResultado, usuario));
    }

    /***
     * Método responsável por trazer a disciplina baseado na disciplina com
     * horarioturma
     *
     * @param professor
     * @param turma
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return
     * @throws Exception
     */
    public List<DisciplinaVO> consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(Integer professor, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(professor, turma, null, null, controlarAcesso, nivelMontarDados, usuario);
    }


    public List<DisciplinaVO> consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(Integer professor, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" select distinct disciplina.* from horarioturma ");
        sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
        sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
        sqlStr.append(" where turma.codigo = ").append(turma);
        if (Uteis.isAtributoPreenchido(professor)) {
            sqlStr.append(" and pessoa.codigo = ").append(professor);
        }
        if ((ano != null && !ano.isEmpty()) && (semestre != null && !semestre.isEmpty())) {
            sqlStr.append(" and ((turma.anual and horarioturma.anovigente = '").append(ano).append("') ");
            sqlStr.append(" or (turma.semestral and horarioturma.anovigente = '").append(ano).append("'  and horarioturma.semestrevigente = '").append(semestre).append("') ");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false )) ");
        }
        sqlStr.append(" order by disciplina.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }


    /***
     * Método traz as disciplinas baseado no professor, turma, ano e semestre
     *
     * @param professor
     * @param turma
     * @param ano
     * @param semestre
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return
     * @throws Exception
     */
    public List<DisciplinaVO> consultarDisciplinaProfessorTurma(Integer professor, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(professor, turma, ano, semestre, controlarAcesso, nivelMontarDados, usuario);
    }

    /***
     * Método traz as disciplinas baseado no professor, turma, ano e semestre
     *
     * @param professor
     * @param turma
     * @param ano
     * @param semestre
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return
     * @throws Exception
     */
    public List<DisciplinaVO> consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(Integer professor, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(professor, turma, ano, semestre, controlarAcesso, nivelMontarDados, usuario);
    }

    public List<DisciplinaVO> consultarDisciplinaPorTurmaHorarioTurmaProfessorDisciplina(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct(Disciplina.*) from HorarioTurmaProfessorDisciplina inner join disciplina on HorarioTurmaProfessorDisciplina.disciplina = disciplina.codigo where HorarioTurmaProfessorDisciplina.turma = " + turma + " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeDisciplinasTurma(String valorConsulta, TurmaVO turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Disciplina.* FROM Disciplina, GradeDisciplina WHERE Disciplina.codigo = gradeDisciplina.disciplina and GradeDisciplina.gradeCurricular = " + turma.getCurso().getCodigo() + " and lower (nome) like('" + valorConsulta.toLowerCase() + "%')  ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorTurmaOuTurmaAgrupada(String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct disciplina.* " + "from turma " + "left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo " + "left join turmadisciplina on turmadisciplina.turma = turmaagrupada.turma or turmadisciplina.turma = turma.codigo " + "left join disciplina on disciplina.codigo = turmadisciplina.disciplina " + "where turma.identificadorturma = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{identificadorTurma});
        // sqlConsultar.setString(1, identificadorTurma);
        // ResultSet tabelaResultado = sqlConsultar.executeQuery();
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Disciplina.* FROM Disciplina, AreaConhecimento WHERE Disciplina.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like('" + valorConsulta.trim().toLowerCase() + "%')  ORDER BY AreaConhecimento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeAreaConhecimento_Matricula(String nomeAreaConhecimento, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct(Disciplina.*) FROM Disciplina, AreaConhecimento, gradeDisciplina , periodoLetivo, gradeCurricular, curso, matricula where gradeDisciplina.disciplina = disciplina.codigo and Disciplina.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like('" + nomeAreaConhecimento.toLowerCase() + "%')  and gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo and curso.codigo = matricula.curso and matricula.matricula = '" + matricula + "'" + "ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeAreaConhecimento_Curso(String nomeAreaConhecimento, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct(Disciplina.*) FROM Disciplina, areaConhecimento, gradeDisciplina , periodoLetivo, gradeCurricular, curso where gradeDisciplina.disciplina = disciplina.codigo and Disciplina.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like(?) and gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo ";
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr += " and curso.codigo = " + curso + "";
        }
        sqlStr += " ORDER BY disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, nomeAreaConhecimento.toLowerCase() + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeAreaConhecimento_Turma(String nomeAreaConhecimento, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct(Disciplina.*) FROM Disciplina, areaConhecimento, gradeDisciplina , periodoLetivo, gradeCurricular, curso " + " INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.disciplina = disciplina.codigo " + " where gradeDisciplina.disciplina = disciplina.codigo and Disciplina.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like('" + nomeAreaConhecimento.toLowerCase() + "%') and gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo and mptd.turma = " + turma + "ORDER BY disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorGradeCurricular(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT distinct(Disciplina.*) ");
        sqlStr.append(" from periodoletivo ");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where periodoletivo.gradecurricular = ? ");
        sqlStr.append(" ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        // String sqlStr =
        // "SELECT distinct(Disciplina.*) FROM Disciplina, gradeDisciplina , periodoLetivo, gradeCurricular, curso, matricula where gradeDisciplina.disciplina = disciplina.codigo and gradeDisciplina.periodoLetivo = periodoLetivo.codigo "
        // +
        // "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo and curso.codigo = matricula.curso and matricula.matricula = '"
        // + valorConsulta + "'" + "ORDER BY disciplina.nome";
        StringBuilder sql = new StringBuilder("select distinct disciplina.* from historico ");
        sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
        sql.append(" inner join matricula on matricula.matricula = historico.matricula and matricula.gradecurricularatual = historico.matrizcurricular ");
        sql.append(" where matricula.matricula = '").append(valorConsulta).append("' ");
        sql.append(" and historico.situacao not in ('AA', 'CH', 'CC', 'IS') ");
        sql.append(" and (historico.historicoEquivalente is null or historico.historicoEquivalente = false) ");
        sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaPorMatriculaComConteudoOnline(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("");
        sb.append(" select distinct(disciplina.*) from matriculaperiodoturmadisciplina");
        sb.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
        sb.append(" inner join conteudo on disciplina.codigo = conteudo.disciplina and conteudo.situacaoconteudo= 'ATIVO'");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
        sb.append(" inner join matricula on matriculaperiodo.matricula= matricula.matricula");
        sb.append(" where matricula.matricula = '").append(matricula).append("' order by disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaPorMatriculaComListaExercicio(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("");
        sb.append(" select distinct(disciplina.*) from matriculaperiodoturmadisciplina");
        sb.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
        sb.append(" inner join listaExercicio on disciplina.codigo = listaExercicio.disciplina and listaExercicio.situacaoListaExercicio= 'ATIVA'");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
        sb.append(" inner join matricula on matriculaperiodo.matricula= matricula.matricula");
        sb.append(" where matricula.matricula = '").append(matricula).append("' order by disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorMatriculaPeriodoLetivo(String valorConsulta, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct(Disciplina.*) FROM Disciplina, gradeDisciplina , periodoLetivo, gradeCurricular, curso, matricula where gradeDisciplina.disciplina = disciplina.codigo and gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo and curso.codigo = matricula.curso and matricula.matricula = '" + valorConsulta + "' " + "AND periodoletivo.codigo = '" + periodoLetivo + "' ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorPeriodoLetivo(Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct(Disciplina.*) FROM periodoletivo ");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("where periodoletivo.codigo = ").append(periodoLetivo);
        sqlStr.append(" ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        // sql.append("select disciplina.* from matriculaPeriodo ");
        // sql.append(" inner join historico on historico.matriculaPeriodo = matriculaPeriodo.codigo and historico.matrizcurricular = matriculaPeriodo.gradecurricular");
        // sql.append(" inner join disciplina on historico.disciplina = disciplina.codigo ");
        // sql.append(" where  matriculaPeriodo.codigo = ");
        // sql.append(valorConsulta.intValue());
        // sql.append(" union all ");
        // sql.append(" select disciplina.* from matriculaPeriodo ");
        // sql.append(" inner join matriculaPeriodo as mpa on mpa.codigo <> matriculaPeriodo.codigo and matriculaPeriodo.periodoLetivoMatricula= mpa.periodoLetivoMatricula");
        // sql.append(" and mpa.matricula = matriculaPeriodo.matricula ");
        // sql.append(" inner join historico on (historico.matriculaPeriodo = mpa.codigo and (historico.situacao = 'AP' or historico.situacao = 'AA')) and historico.matrizcurricular = matriculaPeriodo.gradecurricular");
        // sql.append(" inner join disciplina on historico.disciplina = disciplina.codigo ");
        // sql.append(" where  matriculaPeriodo.codigo = ");
        sql.append("select distinct disciplina.* from matriculaPeriodo ");
        sql.append("inner join historico on historico.matriculaPeriodo = matriculaPeriodo.codigo and historico.matrizcurricular = matriculaPeriodo.gradecurricular ");
        sql.append("inner join disciplina on historico.disciplina = disciplina.codigo ");
        sql.append("inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sql.append("where  matriculaPeriodo.codigo = ").append(valorConsulta);
        sql.append(" order by disciplina.nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarDisciplinaPorCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false");
        sqlStr.append(" and gradecurricular.curso = ").append(valorConsulta);
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" and gradecurricular.curso = ").append(valorConsulta);
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradecurricular.curso = ").append(valorConsulta).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false");
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" and gradecurricular.curso = ").append(valorConsulta);

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinasPorCodigoCursoCodigoDisciplina(Integer codigoCurso, Integer codigoDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct(Disciplina.*) FROM Disciplina, gradeDisciplina , periodoLetivo, gradeCurricular, curso where gradeDisciplina.disciplina = disciplina.codigo and gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo and curso.codigo = " + codigoCurso + " and disciplina.codigo = " + codigoDisciplina + " ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinasPorCodigoCursoNomeDisciplina(Integer codigoCurso, String nomeDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct(Disciplina.*) FROM Disciplina, gradeDisciplina , periodoLetivo, gradeCurricular, curso where gradeDisciplina.disciplina = disciplina.codigo and gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "and periodoLetivo.gradecurricular = gradeCurricular.codigo and gradeCurricular.curso = curso.codigo and curso.codigo = " + codigoCurso + " and lower(disciplina.nome) like '" + nomeDisciplina.toLowerCase() + "%'  ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorGradeCurricularEPeriodoLetivo(Integer valorConsulta, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
            StringBuilder sqlStr = new StringBuilder(" SELECT distinct(Disciplina.*) ");
            sqlStr.append(" from periodoletivo ");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where periodoletivo.codigo = ? ");
            sqlStr.append(" ORDER BY disciplina.nome");
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), periodoLetivo);
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } catch (Exception e) {
            throw e;
        }
    }

    public List consultarDisciplinasDaGradeCurricularCursoPorMatriculaTipoDisciplina(String matricula, String tipoDisciplina1, String tipoDisciplina2, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT DISTINCT disciplina.*");
        sqlStr.append(" FROM matricula");
        sqlStr.append(" INNER JOIN gradecurricular ON gradecurricular.codigo = matricula.gradecurricularatual");
        sqlStr.append(" INNER JOIN periodoletivo ON periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" INNER JOIN gradedisciplina ON gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = gradedisciplina.disciplina");
        sqlStr.append(" WHERE matricula.matricula = '" + matricula + "' AND (gradedisciplina.tipodisciplina = '" + tipoDisciplina1 + "' OR gradedisciplina.tipodisciplina = '" + tipoDisciplina2 + "')");
        sqlStr.append(" ORDER BY disciplina.nome;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Disciplina</code> através
     * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui
     *                        permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DisciplinaVO</code>
     * resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigo_Matricula(Integer codigo, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct Disciplina.*, gradecurricular.nome as gradenome " + "FROM Disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoLetivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradeCurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "inner join curso on gradeCurricular.curso = curso.codigo " + "inner join matricula on curso.codigo = matricula.curso " + "inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.gradecurricular = gradecurricular.codigo " + "where disciplina.codigo = " + codigo + "and matricula.matricula = '" + matricula + "' " + "ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigo_Matricula_DisciplinaEquivalente(Integer codigo, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct Disciplina.*, gradecurricular.nome as gradenome " + "FROM Disciplina " + "left join disciplinaEquivalente de on de.equivalente = disciplina.codigo " + "inner join gradeDisciplina on (gradeDisciplina.disciplina = disciplina.codigo or (case when (de.equivalente is not null) then gradedisciplina.disciplina = de.disciplina end)) " + "inner join periodoLetivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradeCurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "inner join curso on gradeCurricular.curso = curso.codigo " + "inner join matricula on curso.codigo = matricula.curso " + "inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.gradecurricular = gradecurricular.codigo " + "where (disciplina.codigo = " + codigo + ") and matricula.matricula = '" + matricula + "' " + "ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarPorCodigo_Matricula_DisciplinaEquivalenteEDisciplinaComposta(Integer disciplina, String matricula, Integer gradeCurricular, boolean trazerDisciplinaCompostaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        // StringBuilder sb = new StringBuilder();
        StringBuilder sql = new StringBuilder("select distinct disciplina.*, historico.anoHistorico, historico.semestreHistorico from historico ");
        sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
        sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
        sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
        sql.append(" where matricula.matricula = '").append(matricula).append("' ");
        sql.append(" and historico.situacao not in ('AA', 'CH', 'CC', 'IS') ");
        /**
         * Adicionada regra para resolver impactos relacionados a alunos que
         * estão Cursando por Correspondência e que disciplinas saiam duplicadas
         * no Boletim Acadêmico
         */
        if (!Uteis.isAtributoPreenchido(gradeCurricular)) {
            sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
        } else {
            sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularEspecifico(" and ", gradeCurricular));
        }
        sql.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        if (!trazerDisciplinaCompostaPrincipal) {
            sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
        }
        sql.append(" and disciplina.codigo = ").append(disciplina).append(" ORDER BY disciplina.codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO disciplinaVO = montarDados(tabelaResultado, nivelMontarDados, usuario);
            disciplinaVO.setAno(tabelaResultado.getString("anoHistorico"));
            disciplinaVO.setSemestre(tabelaResultado.getString("semestreHistorico"));
            vetResultado.add(disciplinaVO);
        }
        return vetResultado;
    }

    public List consultarPorCodigo_Curso(Integer codigo, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "inner join curso on gradeCurricular.curso = curso.codigo " + "where disciplina.codigo = " + codigo + "and curso.codigo = " + curso;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorCodigoCursoTurma(Integer disciplina, Integer curso, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct disciplina.* from disciplina ");
        sqlStr.append("left join disciplinaEquivalente de on de.equivalente = disciplina.codigo ");
        sqlStr.append("inner join gradeDisciplina on (gradeDisciplina.disciplina = disciplina.codigo or (case when (de.equivalente is not null) then gradedisciplina.disciplina = de.disciplina end)) ");
        sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.disciplina = disciplina.codigo ");
        }
        sqlStr.append("where disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }


        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradeDisciplina on gradeDisciplina.codigo = gradeDisciplinacomposta.gradeDisciplina ");
        sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradeDisciplina = gradeDisciplina.codigo ");
        }
        sqlStr.append("where disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append("where disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradeDisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append("where disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" order by nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorNomeCursoTurma(String nomeDisciplina, Integer curso, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct disciplina.* from disciplina ");
        sqlStr.append("left join disciplinaEquivalente de on de.equivalente = disciplina.codigo ");
        sqlStr.append("inner join gradeDisciplina on (gradeDisciplina.disciplina = disciplina.codigo or (case when (de.equivalente is not null) then gradedisciplina.disciplina = de.disciplina end))");
        sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.turma = t.codigo and td.disciplina = disciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradeDisciplina on gradeDisciplina.codigo = gradeDisciplinacomposta.gradeDisciplina ");
        sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradeDisciplina = gradeDisciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradeDisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" order by nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeDisciplina + PERCENT, nomeDisciplina + PERCENT, nomeDisciplina + PERCENT, nomeDisciplina + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorListaCursoTurma(String nomeDisciplina, Integer curso, Integer turma, Integer unidadeEnsino, List<CursoVO> cursos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct disciplina.* from disciplina ");
        sqlStr.append("left join disciplinaEquivalente de on de.equivalente = disciplina.codigo ");
        sqlStr.append("inner join gradeDisciplina on (gradeDisciplina.disciplina = disciplina.codigo or (case when (de.equivalente is not null) then gradedisciplina.disciplina = de.disciplina end))");
        sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.turma = t.codigo and td.disciplina = disciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        if (cursos != null && !cursos.isEmpty()) {
            //sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsinos);
            sqlStr.append(" AND curso.codigo IN (").append(cursos.stream().map(c -> c.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradeDisciplina on gradeDisciplina.codigo = gradeDisciplinacomposta.gradeDisciplina ");
        sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradeDisciplina = gradeDisciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        if (cursos != null && !cursos.isEmpty()) {
            //sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsinos);
            sqlStr.append(" AND curso.codigo IN (").append(cursos.stream().map(c -> c.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        if (cursos != null && !cursos.isEmpty()) {
            //sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsinos);
            sqlStr.append(" AND curso.codigo IN (").append(cursos.stream().map(c -> c.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradeDisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append("inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("inner join turma t on t.curso = curso.codigo ");
            sqlStr.append("inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append("where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))");
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsino);
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        if (cursos != null && !cursos.isEmpty()) {
            //sqlStr.append(" and uec.unidadeensino = ").append(unidadeEnsinos);
            sqlStr.append(" AND curso.codigo IN (").append(cursos.stream().map(c -> c.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
        }
        sqlStr.append(" order by nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeDisciplina + PERCENT, nomeDisciplina + PERCENT, nomeDisciplina + PERCENT, nomeDisciplina + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorTurmaCodigo(Integer valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT disciplina.* FROM Disciplina, turmaDisciplina WHERE disciplina.codigo = " + valorConsulta.intValue() + "" + " AND disciplina.codigo = turmadisciplina.disciplina" + " AND turmadisciplina.turma = " + turma.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorTurmaNome(String valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT disciplina.* FROM Disciplina, turmaDisciplina WHERE lower (disciplina.nome) like ('" + valorConsulta.toLowerCase() + "%')" + " AND disciplina.codigo = turmadisciplina.disciplina" + " AND turmadisciplina.turma = " + turma.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    // public void consultarUnicidadeNomeCargaHoraria(String valorConsulta,
    // Integer cargaHoraria, UsuarioVO usuario) throws Exception {
    // String sqlStr =
    // "SELECT codigo FROM Disciplina WHERE upper( nome ) like('" +
    // valorConsulta.toUpperCase() + "') AND cargahoraria = " + cargaHoraria +
    // "";
    // SqlRowSet tabelaResultado =
    // getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    // if (tabelaResultado.next()) {
    // throw new
    // ConsistirException("Já existe esse NOME cadastrado com a mesma CARGA HORÁRIA (Disciplina).");
    // }
    // }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>DisciplinaVO</code>
     * resultantes da consulta.
     */
    public static List<DisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
            while (tabelaResultado.next()) {
                vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
            }
            return vetResultado;
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<DisciplinaVO> montarDadosConsultaUnificacaoDisciplina(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        try {
            List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
            while (tabelaResultado.next()) {
                vetResultado.add(montarDadosUnificacaoDisciplina(tabelaResultado, usuario));
            }
            return vetResultado;
        } catch (Exception e) {
            throw e;
        }
    }

    public static DisciplinaVO montarDadosUnificacaoDisciplina(SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
        DisciplinaVO obj = new DisciplinaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        return obj;
    }

    public static List<DisciplinaVO> montarDadosConsultaComNomeGrade(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        DisciplinaVO disciplinaVO;
        while (tabelaResultado.next()) {
            disciplinaVO = montarDados(tabelaResultado, nivelMontarDados, usuario);
            disciplinaVO.setNomeGrade(tabelaResultado.getString("gradenome"));
            vetResultado.add(disciplinaVO);
        }
        return vetResultado;
    }

    // public DisciplinaVO
    // consultarPorChavePrimariaDadosConfiguracaoAcademica(Integer disciplina,
    // UsuarioVO usuarioVO) throws ConsistirException, Exception {
    // StringBuilder sb = new StringBuilder();
    // sb.append("SELECT disciplina.codigo, configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\" FROM disciplina ");
    // sb.append(" INNER JOIN configuracaoAcademico ON configuracaoAcademico.codigo = disciplina.configuracaoAcademico ");
    // sb.append(" WHERE disciplina.codigo = ").append(disciplina);
    // SqlRowSet tabelaResultado =
    // getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    // if (!tabelaResultado.next()) {
    // throw new ConsistirException("Dados Não Encontrados (Disciplina).");
    // }
    // DisciplinaVO obj = new DisciplinaVO();
    // obj.setCodigo(tabelaResultado.getInt("codigo"));
    // return obj;
    // }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>DisciplinaVO</code>.
     *
     * @return O objeto da classe <code>DisciplinaVO</code> com os dados
     * devidamente montados.
     */
    public static DisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            DisciplinaVO obj = new DisciplinaVO();
            obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
            obj.setNome(dadosSQL.getString("nome"));
            obj.setAbreviatura(dadosSQL.getString("abreviatura"));
            obj.setDescricaoComplementar(dadosSQL.getString("descricaoComplementar"));
            obj.setPercentualMinimoCargaHorariaAproveitamento(dadosSQL.getDouble("percentualMinimoCargaHorariaAproveitamento"));
            obj.setQtdeMinimaDeAnosAproveitamento(dadosSQL.getInt("qtdeMinimaDeAnosAproveitamento"));
            obj.setDividirSalaEmGrupo(dadosSQL.getBoolean("dividirSalaEmGrupo"));
            obj.setNrMaximoAulosPorSala(dadosSQL.getInt("nrMaximoAulosPorSala"));
            obj.setNrMaximoAulosPorGrupo(dadosSQL.getInt("nrMaximoAulosPorGrupo"));
            obj.setNrMinimoAlunosPorSala(dadosSQL.getInt("nrMinimoAlunosPorSala"));
            obj.setNrMinimoAlunosPorGrupo(dadosSQL.getInt("nrMinimoAlunosPorGrupo"));
            obj.setNrMaximoAlunosPorAmbientacao(dadosSQL.getInt("nrMaximoAlunosPorAmbientacao"));
            obj.setFonteDeDadosBlackboard(dadosSQL.getString("fonteDeDadosBlackboard"));
            obj.setIdConteudoMasterBlackboard(dadosSQL.getString("idConteudoMasterBlackboard"));
            obj.getGrupoPessoaVO().setCodigo(dadosSQL.getInt("grupoPessoa"));
            if (dadosSQL.getString("classificacaoDisciplina") == null) {
                obj.setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.NENHUMA);
            } else {
                obj.setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(dadosSQL.getString("classificacaoDisciplina")));
            }
//			if(dadosSQL.getString("modeloGeracaoSalaBlackboard") == null) {
//				obj.setModeloGeracaoSalaBlackboard(ModeloGeracaoSalaBlackboardEnum.DISCIPLINA);
//			}else {
//				obj.setModeloGeracaoSalaBlackboard(ModeloGeracaoSalaBlackboardEnum.valueOf(dadosSQL.getString("modeloGeracaoSalaBlackboard")));
//			}
            if (Uteis.isAtributoPreenchido(dadosSQL.getString("niveleducacional"))) {
                obj.setNivelEducacional(dadosSQL.getString("niveleducacional"));
            }
            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
                return obj;
            }
            // obj.setDisciplinaComposta(dadosSQL.getBoolean("disciplinaComposta"));
            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
                return obj;
            }
            obj.getAreaConhecimento().setCodigo(new Integer(dadosSQL.getInt("areaConhecimento")));
            montarDadosAreaConhecimento(obj, nivelMontarDados, usuario);
            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
                return obj;
            }

            obj.getPlanoCursoVO().setCodigo(dadosSQL.getInt("planoCurso"));
            obj.setNovoObj(Boolean.FALSE);

            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
                return obj;
            }

            obj.setDisciplinaEquivalenteVOs(getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentes(obj.getCodigo(), false, usuario));
            // obj.setDisciplinaCompostaVOs(getFacadeFactory().getDisciplinaCompostaFacade().consultarDisciplinaComposta(obj.getCodigo(),
            // false, usuario));
            return obj;
        } catch (Exception e) {
            throw e;
        }
    }

    public static void montarDadosAreaConhecimento(DisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }

        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
    }

    public static void montarDadosPlanoCurso(DisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoCursoVO().getCodigo().intValue() == 0) {
            obj.setPlanoCursoVO(new PlanoCursoVO());
            return;
        }
        obj.setPlanoCursoVO(new PlanoCurso().consultarPorChavePrimaria(obj.getPlanoCursoVO().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>DisciplinaVO</code> através de sua chave primária.
     *
     * @throws Exception Caso haja problemas de conexão ou localização do objeto
     *                   procurado.
     */
    public DisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return getAplicacaoControle().getDisciplinaVO(codigoPrm, usuario);
    }

    public DisciplinaVO consultarPorChavePrimariaSemExcecao(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            return getAplicacaoControle().getDisciplinaVO(codigoPrm, usuario);
        } catch (Exception e) {
            return new DisciplinaVO();
        }
    }

    public DisciplinaVO consultarPorChavePrimariaVisaoCoordenador(Integer disciplina, Integer coordenador, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN horarioturmaprofessordisciplina htpd ON htpd.disciplina = disciplina.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = htpd.turma ");
        sqlStr.append("WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append("WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
        sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append("WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))) ");
        sqlStr.append("AND htpd.disciplina = ").append(disciplina).append(" ");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new DisciplinaVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public DisciplinaVO consultarPorChavePrimariaCursoTurmaVisaoCoordenador(Integer disciplina, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN horarioturmaprofessordisciplina htpd ON htpd.disciplina = disciplina.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = htpd.turma ");
        sqlStr.append("WHERE htpd.disciplina = ").append(disciplina).append(" ");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("AND turma.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append("AND turma.codigo = ").append(turma).append(" ");
        }
        if (professor != null && professor != 0) {
            sqlStr.append("AND htpd.professor = ").append(professor).append(" ");
        }
        sqlStr.append("ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new DisciplinaVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeVisaoCoordenador(String nomeDisciplina, Integer coordenador, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN horarioturmaprofessordisciplina htpd ON htpd.disciplina = disciplina.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = htpd.turma ");
        sqlStr.append("WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append("WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
        sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append("WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))) ");
        sqlStr.append("AND sem_acentos(lower(disciplina.nome)) like(sem_acentos('");
        sqlStr.append(nomeDisciplina.toLowerCase());
        sqlStr.append("%')) ");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        return listaResultado;
    }

    public List consultarPorNomeCursoTurmaVisaoCoordenador(String nomeDisciplina, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN horarioturmaprofessordisciplina htpd ON htpd.disciplina = disciplina.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = htpd.turma ");
        sqlStr.append("WHERE sem_acentos(lower(disciplina.nome)) like(sem_acentos('");
        sqlStr.append(nomeDisciplina.toLowerCase());
        sqlStr.append("%')) ");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("AND turma.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append("AND turma.codigo = ").append(turma).append(" ");
        }
        if (professor != null && professor != 0) {
            sqlStr.append("AND htpd.professor = ").append(professor).append(" ");
        }
        sqlStr.append("ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        return listaResultado;
    }

    public List consultarPorNomeAreaConhecimentoVisaoCoordenador(String areaConhecimento, Integer coordenador, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN areaConhecimento ac ON ac.codigo = disciplina.areaConhecimento ");
        sqlStr.append("INNER JOIN horarioturmaprofessordisciplina htpd ON htpd.disciplina = disciplina.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = htpd.turma ");
        sqlStr.append("WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append("WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
        sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append("WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))) ");
        sqlStr.append("AND sem_acentos(lower(AreaConhecimento.nome)) like(sem_acentos('");
        sqlStr.append(areaConhecimento.toLowerCase());
        sqlStr.append("%')) ");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeAreaConhecimentoCursoTurmaVisaoCoordenador(String areaConhecimento, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN areaConhecimento ac ON ac.codigo = disciplina.areaConhecimento ");
        sqlStr.append("INNER JOIN horarioturmaprofessordisciplina htpd ON htpd.disciplina = disciplina.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = htpd.turma ");
        sqlStr.append("WHERE sem_acentos(lower(AreaConhecimento.nome)) like(sem_acentos('");
        sqlStr.append(areaConhecimento.toLowerCase());
        sqlStr.append("%')) ");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (curso != null && curso != 0) {
            sqlStr.append("AND turma.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append("AND turma.codigo = ").append(turma).append(" ");
        }
        if (professor != null && professor != 0) {
            sqlStr.append("AND htpd.professor = ").append(professor).append(" ");
        }
        sqlStr.append("ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(Integer areaConhecimento, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" (SELECT distinct disciplina.* FROM turma ");
        sqlStr.append("INNER JOIN HorarioTurma on (Turma.codigo = horarioTurma.Turma) ");
        sqlStr.append("INNER JOIN HorarioTurmadia on (HorarioTurmadia.HorarioTurma = horarioTurma.codigo) ");
        sqlStr.append("INNER JOIN HorarioTurmadiaitem on (HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo) ");
        sqlStr.append("INNER JOIN disciplina on (HorarioTurmadiaitem.disciplina = disciplina.codigo) ");
        getFacadeFactory().getTurmaFacade().getSQLPadraoJoinCursoTurma("LEFT", "turma", "curso", sqlStr);
        sqlStr.append("WHERE 1=1 ");

        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (curso != null && curso != 0) {
            sqlStr.append(" AND curso.codigo = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" AND turma.codigo in (select distinct t.codigo from turma t where t.turmaprincipal =").append(turma).append(" or t.codigo =").append(turma).append(")");
        }
        if (professor != null && professor != 0) {
            sqlStr.append(" AND HorarioTurmadiaitem.professor = ").append(professor).append(" ");
        }
        if (Uteis.isAtributoPreenchido(ano)) {
            if (Uteis.isAtributoPreenchido(semestre)) {
                sqlStr.append(" and (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("') ");
            } else {
                sqlStr.append(" and (turma.anual and HorarioTurma.anovigente = '").append(ano).append("' ) ");
            }
        }

        sqlStr.append(" )");
        sqlStr.append(" union ");
        sqlStr.append(" (select distinct disciplina.* from programacaotutoriaonline");
        sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
        sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma");
        sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina");
        getFacadeFactory().getTurmaFacade().getSQLPadraoJoinCursoTurma("LEFT", "turma", "curso", sqlStr);
        sqlStr.append(" where 1=1 ");
        if (Uteis.isAtributoPreenchido(turma)) {
            sqlStr.append(" AND (turma.codigo in (select distinct t.codigo from turma t where t.turmaprincipal = ").append(turma).append(" or t.codigo = ").append(turma).append(") ");
            sqlStr.append(" or disciplina in (select disciplina from turmadisciplina where turma = ").append(turma).append(") )");
        }
        if (Uteis.isAtributoPreenchido(professor)) {
            sqlStr.append(" AND programacaotutoriaonlineprofessor.professor = ").append(professor).append(" ");
        }
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" AND ((turma.codigo IS NOT NULL AND curso.codigo = ").append(curso).append(" ) OR (turma.codigo IS NULL and EXISTS ( ");
            sqlStr.append(" select from gradecurricular");
            sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sqlStr.append(" inner join disciplina disc on gradedisciplina.disciplina = disc.codigo");
            sqlStr.append(" where gradedisciplina.disciplinacomposta = false");
            sqlStr.append(" and gradecurricular.curso = ").append(curso);
            sqlStr.append(" and disciplina.codigo = disc.codigo ");
            sqlStr.append(" union");
            sqlStr.append(" select from gradecurricular");
            sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
            sqlStr.append(" inner join disciplina disc on gradedisciplinacomposta.disciplina = disc.codigo");
            sqlStr.append(" and gradecurricular.curso = ").append(curso);
            sqlStr.append(" and disciplina.codigo = disc.codigo ");
            sqlStr.append(" union");
            sqlStr.append(" select from gradecurricular");
            sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sqlStr.append(" inner join disciplina disc on gradecurriculargrupooptativadisciplina.disciplina = disc.codigo");
            sqlStr.append(" where gradecurricular.curso = ").append(curso).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false");
            sqlStr.append(" and disciplina.codigo = disc.codigo ");
            sqlStr.append(" union");
            sqlStr.append(" select from gradecurricular");
            sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
            sqlStr.append(" inner join disciplina disc on gradedisciplinacomposta.disciplina = disc.codigo");
            sqlStr.append(" and gradecurricular.curso = ").append(curso);
            sqlStr.append(" and disciplina.codigo = disc.codigo )))");
        }

        if (Uteis.isAtributoPreenchido(ano)) {
            sqlStr.append(" AND (programacaotutoriaonline.ano = '").append(ano).append("' or programacaotutoriaonline.ano is null) ");
        }
        if (Uteis.isAtributoPreenchido(semestre)) {
            sqlStr.append(" AND (programacaotutoriaonline.semestre = '").append(semestre).append("' or programacaotutoriaonline.semestre is null) ");
        }
        if (Uteis.isAtributoPreenchido(ano)) {
            if (Uteis.isAtributoPreenchido(semestre)) {
                sqlStr.append(" and ((turma.semestral or turma.codigo is null) and (programacaotutoriaonline.ano = '").append(ano).append("' or programacaotutoriaonline.ano is null)");
                sqlStr.append(" and (programacaotutoriaonline.semestre = '").append(semestre).append("' or programacaotutoriaonline.semestre is null) )");
            } else {
                sqlStr.append(" and ((turma.anual or turma.codigo is null) and (programacaotutoriaonline.ano = '").append(ano).append("' ");
                sqlStr.append(" or programacaotutoriaonline.ano is null)) ");
            }
        }

        sqlStr.append(" )  ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }


    public DisciplinaVO consultarPorCodigoGradeDisciplina(Integer codigoGradeDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT DISTINCT Disciplina.* FROM Disciplina INNER JOIN GradeDisciplina ON (GradeDisciplina.disciplina = disciplina.codigo) WHERE gradeDisciplina.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoGradeDisciplina});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarDisciplinaAluno(Integer unidadeEnsino, String periodicidade, String matricula, int nivelMontarDados, UsuarioVO usuario, String ano, String semestre) throws SQLException, Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct disciplina.* from matricula ");
        sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
        sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
        sqlStr.append("inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sqlStr.append("inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
        sqlStr.append("where matricula.matricula = '").append(matricula).append("' ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append(" and matricula.unidadeensino = ").append(unidadeEnsino);
        }
        if (Uteis.isAtributoPreenchido(ano) && periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor()) || periodicidade.equals(PeriodicidadeEnum.ANUAL.getValor())) {
            sqlStr.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
        }
        if (Uteis.isAtributoPreenchido(semestre) && periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
            sqlStr.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
        }
        if (Uteis.isAtributoPreenchido(periodicidade)) {
            sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("' ");
        }
        sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
        sqlStr.append("order by disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public DisciplinaVO consultarDisciplinaEquivalenteAproveitada(Integer curso, Integer gradeCurricular, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select disc.*,disciplinaequivalente.disciplina as codigoDisciplinaAproveitada, disc.nome as nomeDisciplinaAproveitada, ");
        sqlStr.append(" disciplinaequivalente.equivalente as codigoDisciplinaEquivalente, disc2.nome as nomeDisciplinaEquivalente from disciplinaequivalente ");
        sqlStr.append(" inner join disciplina disc on disc.codigo = disciplinaequivalente.disciplina ");
        sqlStr.append(" inner join disciplina disc2 on disc2.codigo = disciplinaequivalente.equivalente ");
        sqlStr.append(" where disciplina in ( ");
        sqlStr.append(" select gradedisciplina.disciplina from Curso ");
        sqlStr.append(" inner join GradeCurricular on GradeCurricular.curso = curso.codigo ");
        sqlStr.append(" inner join periodoLetivo on PeriodoLetivo.GradeCurricular = GradeCurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on periodoLetivo.codigo = gradedisciplina.periodoLetivo ");
        sqlStr.append(" where gradeCurricular.situacao = 'AT' and curso.codigo = ").append(curso).append(" and gradecurricular.codigo = ").append(gradeCurricular);
        sqlStr.append(" group by gradedisciplina.disciplina  ) ");
        sqlStr.append(" and (disciplinaequivalente.equivalente = ").append(disciplina).append(") ");
        sqlStr.append(" union all ");
        sqlStr.append(" select disciplina.*,gradedisciplina.disciplina as codigoDisciplinaAproveitada, disciplina.nome as nomeDisciplinaAproveitada, ");
        sqlStr.append(" gradedisciplina.disciplina as codigoDisciplinaEquivalente, disciplina.nome as nomeDisciplinaEquivalente from Curso ");
        sqlStr.append(" inner join GradeCurricular on GradeCurricular.curso = curso.codigo ");
        sqlStr.append(" inner join periodoLetivo on PeriodoLetivo.GradeCurricular = GradeCurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on periodoLetivo.codigo = gradedisciplina.periodoLetivo ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
        sqlStr.append(" where gradeCurricular.situacao = 'AT' and curso.codigo = ").append(curso).append(" and gradecurricular.codigo = ").append(gradeCurricular);
        sqlStr.append(" and (gradedisciplina.disciplina = ").append(disciplina).append(") ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public DisciplinaVO consultarDisciplinaPelaDisciplinaEquivalente(Integer curso, Integer gradeCurricular, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select * from disciplina where disciplina.codigo in ( ");
        sqlStr.append(" select disciplinaequivalente.disciplina from disciplinaequivalente where disciplina in ( ");
        sqlStr.append(" select gradedisciplina.disciplina from Curso ");
        sqlStr.append(" inner join GradeCurricular on GradeCurricular.curso = curso.codigo ");
        sqlStr.append(" inner join periodoLetivo on PeriodoLetivo.GradeCurricular = GradeCurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on periodoLetivo.codigo = gradedisciplina.periodoLetivo ");
        sqlStr.append(" where gradeCurricular.situacao = 'AT' and curso.codigo = ").append(curso).append(" and gradecurricular.codigo = ").append(gradeCurricular);
        sqlStr.append(" group by gradedisciplina.disciplina  ) and (disciplinaequivalente.equivalente = ").append(disciplina).append(")) ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new DisciplinaVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("select distinct disciplina.codigo, disciplina.nome, disciplina.disciplinacomposta from disciplina ");
        str.append("inner join matriculaPeriodoTurmaDisciplina mptd on mptd.disciplina = disciplina.codigo ");
        str.append("inner join MatriculaPeriodo on mptd.matriculaPeriodo = MatriculaPeriodo.codigo ");
        str.append("inner join Matricula on Matricula.matricula = MatriculaPeriodo.matricula ");
        str.append("inner join PeriodoLetivo on MatriculaPeriodo.periodoLetivoMatricula = PeriodoLetivo.codigo ");
        return str;
    }

    @Override
    public List<DisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodo(String matricula, Integer unidadeEnsino, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        return consultaRapidaPorMatriculaEMatriculaPeriodo(matricula, null, unidadeEnsino, matriculaPeriodo, controlarAcesso, usuario);
    }

    public List<DisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodo(String matricula, Integer disciplina, Integer unidadeEnsino, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" SELECT distinct disciplina.codigo, disciplina.nome, disciplina.disciplinacomposta FROM Historico ");
        sqlStr.append(" inner join matricula on matricula.matricula = historico.matricula  inner join curso on curso.codigo = matricula.curso  ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico  ");
        sqlStr.append(" left join matriculaperiodo ON historico.matriculaperiodo = matriculaperiodo.codigo left join periodoletivo ON case when matriculaperiodo.codigo is not null 	then matriculaperiodo.periodoletivomatricula else case when historico.periodoletivocursada is not null 	then historico.periodoletivocursada else historico.periodoletivomatrizcurricular end end  = periodoletivo.codigo  ");
        sqlStr.append(" left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina left join turma on turma.codigo = matriculaperiodoturmadisciplina.turma  ");
        sqlStr.append(" where Matricula.matricula = '");
        sqlStr.append(matricula).append("' ");
        sqlStr.append(" and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
        sqlStr.append(" and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append(" and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
        sqlStr.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
        sqlStr.append(" and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append(" and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
        sqlStr.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
        sqlStr.append(" AND historico.matriculaperiodo = ").append(matriculaPeriodo).append(" ");
        sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null  ");
        sqlStr.append(" or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)  ");
        sqlStr.append(" ORDER BY disciplina.nome ");
//		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
//		sqlStr.append(" where Matricula.matricula = '");
//		sqlStr.append(matricula).append("' ");
//		if (matriculaPeriodo != null && matriculaPeriodo > 0) {
//			sqlStr.append(" and MatriculaPeriodo.codigo = ");
//			sqlStr.append(matriculaPeriodo);
//		}
//		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
//			sqlStr.append(" AND (matricula.unidadeEnsino = ");
//			sqlStr.append(unidadeEnsino.intValue());
//			sqlStr.append(") ");
//		}
//		if (disciplina != null && disciplina.intValue() != 0) {
//			sqlStr.append(" AND (mptd.disciplina = ");
//			sqlStr.append(disciplina.intValue());
//			sqlStr.append(") ");
//		}
//		sqlStr.append(" ORDER BY disciplina.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaBasica(tabelaResultado, usuario);
    }

    public List<DisciplinaVO> consultaRapidaPorMatriculasSituacaoHistorico(List<MatriculaVO> listaMatricula, String situacaoHistorico, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT disciplina.codigo, disciplina.nome, disciplina.disciplinacomposta from disciplina");
        sqlStr.append(" INNER JOIN historico h ON h.disciplina = disciplina.codigo");
        sqlStr.append(" INNER JOIN matricula ON matricula.matricula = h.matricula");
        sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.codigo = h.matriculaperiodoturmadisciplina ");
        sqlStr.append(" INNER JOIN MatriculaPeriodo ON mptd.matriculaPeriodo = MatriculaPeriodo.codigo ");
        sqlStr.append(" WHERE h.matricula in (");
        for (MatriculaVO matricula : listaMatricula) {
            sqlStr.append("'").append(matricula.getMatricula()).append("'");
            if (!matricula.getMatricula().equals(listaMatricula.get(listaMatricula.size() - 1).getMatricula())) {
                sqlStr.append(", ");
            }
        }
        sqlStr.append(") ");
        if (!situacaoHistorico.equals("")) {
            sqlStr.append(" AND h.situacao = '").append(situacaoHistorico).append("' ");
        }
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND (matricula.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
            sqlStr.append(") ");
        }
        sqlStr.append(" GROUP BY disciplina.codigo, disciplina.nome, disciplina.disciplinacomposta ");
        // sqlStr.append(" HAVING COUNT(disciplina.codigo) = ").append(listaMatricula.size());
        sqlStr.append(" ORDER BY disciplina.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaBasica(tabelaResultado, usuario);
    }

    public List consultaRapidaPorMatriculaEMatriculaPeriodoAtiva(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" where Matricula.matricula = '");
        sqlStr.append(matricula);
        sqlStr.append("' and MatriculaPeriodo.situacaoMatriculaPeriodo = 'AT'");

        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND (matricula.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
            sqlStr.append(") ");
        }
        sqlStr.append(" ORDER BY disciplina.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaBasica(tabelaResultado, usuario);
    }

    public List<DisciplinaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO obj = new DisciplinaVO();
            montarDadosBasico(obj, tabelaResultado, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    private void montarDadosBasico(DisciplinaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        // Dados da Disciplina
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDisciplinaComposta(dadosSQL.getBoolean("disciplinacomposta"));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return Disciplina.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Disciplina.idEntidade = idEntidade;
    }

    public DisciplinaVO consultarPeriodoLetivoDisciplinaPreRequisito(Integer gradeCurricular, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT periodoletivo.periodoletivo AS periodoletivo,periodoletivo.descricao AS descricaoperiodoletivo,disciplina.* as codigoDisciplina FROM periodoletivo");
        sqlStr.append(" INNER JOIN gradedisciplina ON gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = gradedisciplina.disciplina");
        sqlStr.append(" WHERE periodoletivo.gradecurricular = ? AND disciplina.codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{gradeCurricular, disciplina});
        if (!tabelaResultado.next()) {
            return null;
        }
        return montarDadosPreRequisito(tabelaResultado, nivelMontarDados, usuario);
    }

    public static DisciplinaVO montarDadosPreRequisito(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DisciplinaVO obj = new DisciplinaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setAbreviatura(dadosSQL.getString("abreviatura"));
        obj.getAreaConhecimento().setCodigo(new Integer(dadosSQL.getInt("areaConhecimento")));
        obj.setPeriodoLetivo(dadosSQL.getInt("periodoletivo"));
        obj.setDescricaoPeriodoLetivo(dadosSQL.getString("descricaoperiodoletivo"));
        montarDadosAreaConhecimento(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosAreaConhecimento(obj, nivelMontarDados, usuario);
            return obj;
        }
        obj.getPlanoCursoVO().setCodigo(dadosSQL.getInt("planoCurso"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosPlanoCurso(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setDisciplinaEquivalenteVOs(getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentes(obj.getCodigo(), false, usuario));

        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaDisciplinasRemovidasImportacao(final DisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            Disciplina.alterar(getIdEntidade());
            final String sql = "UPDATE Disciplina set listaDisciplinaRemovidasImportacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    String listaGravar = obj.getListaDisciplinaRemovidasImportacao();
                    if (listaGravar.length() >= 250) {
                        listaGravar = listaGravar.substring(0, 249);
                    }
                    sqlAlterar.setString(1, listaGravar);
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<DisciplinaVO> obterListaDisciplinaIdealParaManterNaBaseDeDados(UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "select max(codigo) as codigo, nome from disciplina group by nome order by nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaUnificacaoDisciplina(tabelaResultado, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<DisciplinaVO> obterListaDisciplinaSimilarParaEliminacaoDaBD(DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "select codigo, nome, disciplinaComposta from disciplina where (codigo != " + disciplinaVO.getCodigo() + ") and (nome = '" + disciplinaVO.getNome().replace("'", "") + "') order by nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaDisciplinaInteresse(final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuarioVO) {
        Boolean jaPossuiRegistro = false;
        try {
            List<DisciplinasInteresseVO> disciplinasInteresses = getFacadeFactory().getDisciplinasInteresseFacade().consultarDisciplinasInteressesPorCodigoDisciplina(codigoExcluir, false, usuarioVO);
            for (final DisciplinasInteresseVO obj : disciplinasInteresses) {
                jaPossuiRegistro = getFacadeFactory().getDisciplinasInteresseFacade().consultarExistenciaDisciplinaComProfessor(codigoPermanecer, obj.getProfessor());

                if (!jaPossuiRegistro) {
                    final String sql = "UPDATE DisciplinasInteresse set disciplina=? where disciplina = ? and professor = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
                    getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                        public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                            PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                            sqlAlterar.setInt(1, codigoPermanecer);
                            sqlAlterar.setInt(2, codigoExcluir);
                            sqlAlterar.setInt(3, obj.getProfessor());
                            return sqlAlterar;
                        }
                    });
                } else {
                    final String sql = "DELETE from DisciplinasInteresse where disciplina=?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
                    getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                        public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                            PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                            // sqlAlterar.setInt(1, codigoPermanecer);
                            sqlAlterar.setInt(1, codigoExcluir);
                            return sqlAlterar;
                        }
                    });
                }
            }
        } catch (Exception e) {
            // System.out.println("Disciplina Erro:" + e.getMessage());
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaGradeDisciplina(final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuario) throws Exception {
        Boolean existeDisciplinaGrade = false;
        List<Integer> listaGrade = consultarGradeDisciplinaExcluirUnificacaoDisciplina(codigoExcluir);

        for (Integer periodoLetivo : listaGrade) {
            existeDisciplinaGrade = realizarVerificacaoExistenciaDisciplinaPermanecerGrade(codigoPermanecer, periodoLetivo);
            if (!existeDisciplinaGrade) {
                final String sql = "UPDATE gradedisciplina set disciplina=? where disciplina = ?";
                getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                        sqlAlterar.setInt(1, codigoPermanecer);
                        sqlAlterar.setInt(2, codigoExcluir);
                        return sqlAlterar;
                    }
                });
            }
            getFacadeFactory().getGradeDisciplinaFacade().excluirPorCodigoDisciplinaPeriodoLetivo(periodoLetivo, codigoExcluir, usuario);
            existeDisciplinaGrade = false;
        }
    }

    public List<Integer> consultarTurmaDisciplinaExcluirUnificacaoDisciplina(Integer codigoExcluir) {
        List<Integer> listaTurma = new ArrayList<Integer>();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select * from turmadisciplina where disciplina = ");
        sqlStr.append(codigoExcluir);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            listaTurma.add(tabelaResultado.getInt("turma"));
        }
        return listaTurma;
    }

    public Boolean realizarVerificacaoExistenciaDisciplina(Integer codigoPermanecer, Integer turma) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM turmaDisciplina WHERE disciplina = ");
        sqlStr.append(codigoPermanecer);
        sqlStr.append(" AND turma = ");
        sqlStr.append(turma);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    public List<Integer> consultarGradeDisciplinaExcluirUnificacaoDisciplina(Integer codigoExcluir) {
        List<Integer> listaGrade = new ArrayList<Integer>();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select * from gradedisciplina where disciplina = ");
        sqlStr.append(codigoExcluir);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            listaGrade.add(tabelaResultado.getInt("periodoletivo"));
        }
        return listaGrade;
    }

    public Boolean realizarVerificacaoExistenciaDisciplinaPermanecerGrade(Integer codigoPermanecer, Integer periodoLetivo) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM gradeDisciplina WHERE disciplina = ");
        sqlStr.append(codigoPermanecer);
        sqlStr.append(" AND periodoLetivo = ");
        sqlStr.append(periodoLetivo);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplina(final Integer codigoExcluir, final Integer codigoPermanecer, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoDisciplinaExcluirDisciplinaPermanecerUnificacaoDisciplina(codigoExcluir, codigoPermanecer, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);

        try {

            if (!listaMatriculaPeriodoTurmaDisciplina.isEmpty()) {
                for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaMatriculaPeriodoTurmaDisciplina) {
                    excluirMatriculaPeriodoTurmaDisciplinaPorDisciplina(mptd, mptd.getMatriculaPeriodo(), codigoPermanecer, codigoExcluir, configuracaoFinanceiroVO, usuarioVO);
                }
            }
            List<Integer> listaMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoDisciplina(codigoExcluir);
            if (!listaMatriculaPeriodo.isEmpty()) {
                for (final Integer matriculaPeriodo : listaMatriculaPeriodo) {
                    final String sql = "UPDATE matriculaperiodoturmadisciplina set disciplina=? where disciplina = ? and matriculaPeriodo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
                    getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                        public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                            PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                            sqlAlterar.setInt(1, codigoPermanecer);
                            sqlAlterar.setInt(2, codigoExcluir);
                            sqlAlterar.setInt(3, matriculaPeriodo);
                            return sqlAlterar;
                        }
                    });

                }
                listaMatriculaPeriodo.clear();
            }
        } finally {
            listaMatriculaPeriodoTurmaDisciplina = null;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplinaParaDisciplinaEquivalente(final Integer codigoExcluir, final Integer codigoPermanecer, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoDisciplinaExcluirMatriculaPeriodoTurmaDisciplinaComAMesmaEquivalente(codigoExcluir, codigoPermanecer, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);

        try {
            if (!listaMatriculaPeriodoTurmaDisciplina.isEmpty()) {
                // for (MatriculaPeriodoTurmaDisciplinaVO
                // matriculaPeriodoTurmaDisciplina :
                // listaMatriculaPeriodoTurmaDisciplina) {
                final String sql = "UPDATE matriculaperiodoturmadisciplina set disciplinaEquivalente=? where disciplinaEquivalente = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
                getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                        sqlAlterar.setInt(1, codigoPermanecer);
                        sqlAlterar.setInt(2, codigoExcluir);
                        return sqlAlterar;
                    }
                });
                // }
                listaMatriculaPeriodoTurmaDisciplina.clear();
            }
        } finally {
            listaMatriculaPeriodoTurmaDisciplina = null;
        }
    }

    public void excluirMatriculaPeriodoTurmaDisciplinaPorDisciplina(MatriculaPeriodoTurmaDisciplinaVO mptdExcluir, Integer matriculaPeriodo, Integer codigoPermanecer, Integer codigoExcluir, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO mptdPermanecer = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaPeriodoDisciplina(matriculaPeriodo, codigoPermanecer, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().inicializarDadosMatriculaPeriodoTurmaDisciplinaUnificacaoDisciplina(mptdPermanecer, mptdExcluir);
        getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterar(mptdPermanecer, configuracaoFinanceiroVO, usuarioVO);
        getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluir(mptdExcluir, false, usuarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHistorico(final Integer codigoExcluir, final Integer codigoPermanecer, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        List<HistoricoVO> listaHistorico = getFacadeFactory().getHistoricoFacade().consultarPorCodigoDisciplinaExcluirDisciplinaPermanecerUnificacaoDisciplina(codigoExcluir, codigoPermanecer, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);

        try {

            if (!listaHistorico.isEmpty()) {
                for (HistoricoVO obj : listaHistorico) {
                    excluirHistoricoPorDisciplina(obj, obj.getMatriculaPeriodo().getCodigo(), codigoPermanecer, codigoExcluir, configuracaoFinanceiroVO, usuario);
                }
            }

            List<Integer> listaMatriculaPeriodo = getFacadeFactory().getHistoricoFacade().consultarPorCodigoDisciplina(codigoExcluir);
            if (!listaMatriculaPeriodo.isEmpty()) {
                for (final Integer matriculaPeriodo : listaMatriculaPeriodo) {
                    final String sql = "UPDATE historico set disciplina=? where disciplina = ? and matriculaperiodo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
                    getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                        public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                            PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                            sqlAlterar.setInt(1, codigoPermanecer);
                            sqlAlterar.setInt(2, codigoExcluir);
                            sqlAlterar.setInt(3, matriculaPeriodo);
                            return sqlAlterar;
                        }
                    });
                }
                listaMatriculaPeriodo.clear();
            }

        } finally {
            listaHistorico = null;
        }
    }

    public void excluirHistoricoPorDisciplina(HistoricoVO historicoExcluir, Integer matriculaPeriodo, Integer codigoPermanecer, final Integer codigoExcluir, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        HistoricoVO historicoPermanecer = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoDisciplina(matriculaPeriodo, codigoPermanecer, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO);
        getFacadeFactory().getHistoricoFacade().inicializarDadosHistoricoUnificacaoDisciplina(historicoPermanecer, historicoExcluir);
        getFacadeFactory().getHistoricoFacade().alterar(historicoPermanecer, usuarioVO);
        getFacadeFactory().getHistoricoFacade().excluir(historicoExcluir, true, usuarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHorarioTurmaProfessorDisciplina(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE horarioturmaprofessordisciplina set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaProfessorMinistrouAulaTurma(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE ProfessorMinistrouAulaTurma set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaProfessorTitularDisciplinaTurma(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE ProfessorTitularDisciplinaTurma set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaRegistroAula(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE registroaula set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaTransferenciaEntradaDisciplinasAproveitadas(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE transferenciaentradadisciplinasaproveitadas set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaTurmaDisciplina(final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuario) throws Exception {

        Boolean existeDisciplinaTurma = false;
        List<Integer> listaTurma = consultarTurmaDisciplinaExcluirUnificacaoDisciplina(codigoExcluir);

        for (Integer turma : listaTurma) {
            existeDisciplinaTurma = realizarVerificacaoExistenciaDisciplina(codigoPermanecer, turma);
            if (!existeDisciplinaTurma) {
                final String sql = "UPDATE turmadisciplina set disciplina=? where disciplina = ?";
                getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                        sqlAlterar.setInt(1, codigoPermanecer);
                        sqlAlterar.setInt(2, codigoExcluir);
                        return sqlAlterar;
                    }
                });
            }
            getFacadeFactory().getTurmaDisciplinaFacade().excluirPorCodigoDisciplinaTurma(turma, codigoExcluir, usuario);
            existeDisciplinaTurma = false;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaArquivo(final Integer codigoExcluir, final Integer codigoPermanecer, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

        executarCriacaoNovoDiretorioUnificacaoDisciplina(codigoExcluir, codigoPermanecer, configuracaoGeralSistemaVO);

        final String sql = "UPDATE arquivo set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarCriacaoNovoDiretorioUnificacaoDisciplina(Integer codigoExcluir, Integer codigoPermanecer, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        List<String> listaNomeArquivo = new ArrayList<String>(0);
        try {
            listaNomeArquivo = getFacadeFactory().getArquivoFacade().consultarNomeArquivoPorCodigoDisciplina(codigoExcluir);
            if (!listaNomeArquivo.isEmpty()) {
                getFacadeFactory().getArquivoHelper().executarCriacaoNovoDiretorioUnificacaoDisciplina(codigoExcluir, codigoPermanecer, listaNomeArquivo, configuracaoGeralSistemaVO, PastaBaseArquivoEnum.ARQUIVO);
            }
        } finally {
            listaNomeArquivo = null;
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaPreMatriculaPeriodoTurmaDisciplina(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE prematriculaperiodoturmadisciplina set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaInclusaoDisciplinaHistoricoForaPrazo(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE inclusaodisciplinashistoricoforaprazo set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHistoricoGradeMigradaEquivalente(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE historicogrademigradaequivalente set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHistoricoGrade(final Integer codigoExcluir, final Integer codigoPermanecer) throws Exception {
        final String sql = "UPDATE historicoGrade set disciplina=? where disciplina = ?";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHorarioProfessor(final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuario) throws Exception {
        if ((codigoExcluir.equals(14291)) || (codigoExcluir.equals(14289))) {
            int i = 0;
        }
        if ((codigoPermanecer.equals(14291)) || (codigoPermanecer.equals(14289))) {
            int i = 0;
        }
        if ((codigoExcluir.equals(10856)) || (codigoPermanecer.equals(10856))) {
            int i = 0;
        }

        List<HorarioProfessorVO> lista = getFacadeFactory().getHorarioProfessorFacade().consultarPorHorarioProfessorContendoDisciplina(codigoExcluir, null, null, usuario);
        for (HorarioProfessorVO horario : lista) {
            horario.setSegunda(Uteis.substituirPadraoString(horario.getSegunda(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            horario.setTerca(Uteis.substituirPadraoString(horario.getTerca(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            horario.setQuarta(Uteis.substituirPadraoString(horario.getQuarta(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            horario.setQuinta(Uteis.substituirPadraoString(horario.getQuinta(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            horario.setSexta(Uteis.substituirPadraoString(horario.getSexta(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            horario.setSabado(Uteis.substituirPadraoString(horario.getSabado(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            horario.setDomingo(Uteis.substituirPadraoString(horario.getDomingo(), "[" + codigoExcluir + "]", "[" + codigoPermanecer + "]"));
            getFacadeFactory().getHorarioProfessorFacade().alterarSometeHorariosSemana(horario, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHorarioProfessorDia(final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuarioVO) throws Exception {
        List<HorarioProfessorDiaVO> lista = getFacadeFactory().getHorarioProfessorDiaFacade().consultarPorHorarioProfessorDiaContendoDisciplina(codigoExcluir);
        for (HorarioProfessorDiaVO horario : lista) {
            for (HorarioProfessorDiaItemVO diaItem : horario.getHorarioProfessorDiaItemVOs()) {
                if (diaItem.getDisciplinaVO().getCodigo().equals(codigoExcluir)) {
                    diaItem.getDisciplinaVO().getCodigo().equals(codigoPermanecer);
                }
            }
            getFacadeFactory().getHorarioProfessorDiaFacade().alterar(horario, usuarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciaTabelaHorarioTurmaDia(final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuarioVO) throws Exception {
        List<HorarioTurmaDiaVO> lista = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorHorarioTurmaDiaContendoDisciplina(codigoExcluir);
        for (HorarioTurmaDiaVO horario : lista) {
            for (HorarioTurmaDiaItemVO item : horario.getHorarioTurmaDiaItemVOs()) {
                if (Uteis.isAtributoPreenchido(item.getDisciplinaVO().getCodigo()) && item.getDisciplinaVO().getCodigo().equals(codigoExcluir)) {
                    item.getDisciplinaVO().setCodigo(codigoPermanecer);
                }
            }
            getFacadeFactory().getHorarioTurmaDiaFacade().alterar(horario, usuarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterarReferenciasDisciplinasRemoverParaDisciplinaFinal(Integer codigoExcluir, Integer codigoPermanecer, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            alterarReferenciaTabelaDisciplinaInteresse(codigoExcluir, codigoPermanecer, usuario);
            alterarReferenciaTabelaGradeDisciplina(codigoExcluir, codigoPermanecer, usuario);
            alterarReferenciaTabelaHistorico(codigoExcluir, codigoPermanecer, configuracaoFinanceiroVO, usuario);
            alterarReferenciaTabelaHorarioProfessor(codigoExcluir, codigoPermanecer, usuario);
            alterarReferenciaTabelaHorarioProfessorDia(codigoExcluir, codigoPermanecer, usuario);
            alterarReferenciaTabelaTurmaDisciplina(codigoExcluir, codigoPermanecer, usuario);
            alterarReferenciaTabelaHorarioTurmaDia(codigoExcluir, codigoPermanecer, usuario);
            alterarReferenciaTabelaHorarioTurmaProfessorDisciplina(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplina(codigoExcluir, codigoPermanecer, configuracaoFinanceiroVO, usuario);
            alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplinaParaDisciplinaEquivalente(codigoExcluir, codigoPermanecer, configuracaoFinanceiroVO, usuario);
            alterarReferenciaTabelaProfessorMinistrouAulaTurma(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaProfessorTitularDisciplinaTurma(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaRegistroAula(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaTransferenciaEntradaDisciplinasAproveitadas(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaArquivo(codigoExcluir, codigoPermanecer, configuracaoGeralSistemaVO);
            alterarReferenciaTabelaPreMatriculaPeriodoTurmaDisciplina(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaInclusaoDisciplinaHistoricoForaPrazo(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaHistoricoGradeMigradaEquivalente(codigoExcluir, codigoPermanecer);
            alterarReferenciaTabelaHistoricoGrade(codigoExcluir, codigoPermanecer);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void processarDisicpliaEspecificaEliminandoDisciplinasRedundantesDoBD(DisciplinaVO disciplinaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            // //System.out.print("DISCIPLINA => " +
            // disciplinaVO.getCodigo().intValue());
            if (disciplinaVO.getCodigo().equals(98)) {
                Integer teste = 0;
            }
            List<DisciplinaVO> listaDisciplinaRemoverBD = obterListaDisciplinaSimilarParaEliminacaoDaBD(disciplinaVO, usuario);
            String listaComCodigosRemovidos = "";
            String separador = "";
            for (DisciplinaVO disciplinaRemover : listaDisciplinaRemoverBD) {
                // System.out.println("   >> Remover - " +
                // disciplinaRemover.toString());
                // alterar todas as referencias a disciplina.
                alterarReferenciasDisciplinasRemoverParaDisciplinaFinal(disciplinaRemover.getCodigo(), disciplinaVO.getCodigo(), configuracaoFinanceiroVO, usuario, configuracaoGeralSistemaVO);
                // registrar no historico da disciplina que ficou o codigo da
                // disciplina que foi eliminada.
                listaComCodigosRemovidos += separador + disciplinaRemover.getCodigo();
                separador = ";";
                // remover disciplina da base.
                getFacadeFactory().getDisciplinaFacade().excluirDisciplinaUnificacaoDisciplina(disciplinaRemover, usuario);
                // System.out.println("   >>   removida com sucesso - " +
                // disciplinaRemover.toString());
            }
            // registrar no historico da disciplina que ficou o codigo da
            // disciplina que foi eliminada.
            // if (!listaComCodigosRemovidos.equals("")) {
            // disciplinaVO.setListaDisciplinaRemovidasImportacao(listaComCodigosRemovidos);
            // alterarListaDisciplinasRemovidasImportacao(disciplinaVO,
            // usuario);
            // }
        } catch (Exception e) {
            // //System.out.print(" +++ >" + e.getMessage());
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarUnificacaoDeDisciplinasIdenticadas(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer disciplinaDefazada, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        List<DisciplinaVO> listaDisciplinasParaProcessamento = new ArrayList();
        if (disciplinaDefazada != null) {
            if (disciplinaDefazada != 0) {
                listaDisciplinasParaProcessamento.add(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplinaDefazada, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
            }
        }
        if (listaDisciplinasParaProcessamento.isEmpty()) {
            listaDisciplinasParaProcessamento = obterListaDisciplinaIdealParaManterNaBaseDeDados(usuario);
        }
        for (DisciplinaVO obj : listaDisciplinasParaProcessamento) {
            // System.out.println(obj.toString());
            try {
                processarDisicpliaEspecificaEliminandoDisciplinasRedundantesDoBD(obj, configuracaoFinanceiroVO, usuario, configuracaoGeralSistemaVO);
            } catch (Exception e) {
                // //System.out.print(" +++ >" + e.getMessage());
            }
            // System.out.println("-------------------------------");
        }
    }

    public List<DisciplinaVO> obterListaDisciplinaUtilizadaEmHistoricoComProblemas(Integer codigoGrade, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "select distinct disciplina.* from historico " + "inner join disciplina on disciplina.codigo = historico.disciplina " + "inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo " + "inner join gradecurricular on matriculaperiodo.gradecurricular = gradecurricular.codigo " + "inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo  " + "where gradecurricular.codigo = " + codigoGrade + " and " + " disciplina.codigo not in (" + "     select distinct disciplina.codigo from disciplina " + "         inner join gradedisciplina on (gradedisciplina.disciplina = disciplina.codigo)" + "         inner join periodoletivo on (gradedisciplina.periodoletivo = periodoletivo.codigo)" + "         inner join gradecurricular on (gradecurricular.codigo = periodoletivo.gradecurricular)" + "             WHERE gradecurricular.codigo = " + codigoGrade + " );";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuario));
    }

    public DisciplinaVO obterCodigoDisciplinaCorretaGradeAtual(Integer gradeCurricular, DisciplinaVO disciplinaReferencia, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "select distinct disciplina.* from disciplina " + " inner join gradedisciplina on (gradedisciplina.disciplina = disciplina.codigo)" + " inner join periodoletivo on (gradedisciplina.periodoletivo = periodoletivo.codigo)" + " inner join gradecurricular on (gradecurricular.codigo = periodoletivo.gradecurricular)" + " WHERE gradecurricular.codigo = " + gradeCurricular + " and" + " disciplina.nome = '" + disciplinaReferencia.getNome() + "'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    private void alterarReferenciaTabelaHistorico(final Integer codigoGrade, final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE historico set disciplina=? where disciplina = ? and " + " codigo in ( " + "  select historico.codigo from historico" + "    inner join disciplina on disciplina.codigo = historico.disciplina   " + "    inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo " + "    inner join gradecurricular on matriculaperiodo.gradecurricular = gradecurricular.codigo  " + "    where gradecurricular.codigo = " + codigoGrade + " )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    private void alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplina(final Integer codigoGrade, final Integer codigoExcluir, final Integer codigoPermanecer, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE matriculaperiodoturmadisciplina set disciplina=? where disciplina = ? and " + " codigo in ( " + "  select matriculaperiodoturmadisciplina.codigo from matriculaperiodoturmadisciplina" + "    inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo " + "    inner join gradecurricular on matriculaperiodo.gradecurricular = gradecurricular.codigo  " + "    where gradecurricular.codigo = " + codigoGrade + " )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, codigoPermanecer);
                sqlAlterar.setInt(2, codigoExcluir);
                return sqlAlterar;
            }
        });
    }

    public void processarTrocaDisciplinaHistoricoDiretamente(Integer codigoGrade, Integer disciplinaDefasada, Integer disciplinaCorreta, UsuarioVO usuario) {
        try {
            // private Integer disciplinaDefasada;
            // private Integer disciplinaCorreta;
            // DisciplinaVO disciplinaCorreta =
            // obterCodigoDisciplinaCorretaGradeAtual(gradeCurricular,
            // disciplinaVO);
            // System.out.println(" --> OK - DISCIPLINA ALTERADA");
            alterarReferenciaTabelaHistorico(codigoGrade, disciplinaDefasada, disciplinaCorreta, usuario);
            alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplina(codigoGrade, disciplinaDefasada, disciplinaCorreta, usuario);
        } catch (Exception e) {
            // System.out.println("" + e.getMessage());
        }
    }

    private void processarDisicpliaGradeRealizandoAjusteHistoricoMatriculaPeriodoTurmaDisciplina(Integer gradeCurricular, DisciplinaVO disciplinaVO, UsuarioVO usuario) {
        try {
            // private Integer disciplinaDefasada;
            // private Integer disciplinaCorreta;
            DisciplinaVO disciplinaCorreta = obterCodigoDisciplinaCorretaGradeAtual(gradeCurricular, disciplinaVO, usuario);
            // System.out.println(" --> OK - DISCIPLINA ALTERADA");
            alterarReferenciaTabelaHistorico(gradeCurricular, disciplinaVO.getCodigo(), disciplinaCorreta.getCodigo(), usuario);
            alterarReferenciaTabelaMatriculaPeriodoTurmaDisciplina(gradeCurricular, disciplinaVO.getCodigo(), disciplinaCorreta.getCodigo(), usuario);
        } catch (Exception e) {
            // System.out.println("" + e.getMessage());
        }
    }

    public void executarTransferenciaDisciplinasHistoricoAltigoGradeCorreta_Direito(Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception {
        List<DisciplinaVO> listaDisciplinasParaProcessamento = obterListaDisciplinaUtilizadaEmHistoricoComProblemas(codigoGradeCurricular, usuario);
        int contador = 1;
        for (DisciplinaVO obj : listaDisciplinasParaProcessamento) {
            processarDisicpliaGradeRealizandoAjusteHistoricoMatriculaPeriodoTurmaDisciplina(codigoGradeCurricular, obj, usuario);
            contador++;
        }
    }

    // public Integer consultarCargaHorariaCumpridaNoHistorico(String matricula,
    // UsuarioVO usuario) throws Exception {
    // String sqlStr =
    // "SELECT SUM(CARGAHORARIA) FROM DISCIPLINA D INNER JOIN HISTORICO H ON H.DISCIPLINA = D.CODIGO ";
    // sqlStr +=
    // "WHERE H.MATRICULA = ? AND (H.SITUACAO = 'AP' OR H.SITUACAO = 'AA')";
    // try {
    // return (int) getConexao().getJdbcTemplate().queryForInt(sqlStr, new
    // Object[]{matricula});
    // } finally {
    // sqlStr = null;
    // }
    // }

    // public Integer consultarCargaHorariaDisciplinaPorCodigo(Integer codigo,
    // UsuarioVO usuarioVO) {
    // StringBuilder sb = new StringBuilder();
    // sb.append("select cargaHoraria from disciplina where codigo = ").append(codigo);
    // try {
    // return (int) getConexao().getJdbcTemplate().queryForInt(sb.toString());
    // } finally {
    // sb = null;
    // }
    // }
    //
    // public Integer
    // consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricular(String
    // matricula, Integer codigoGradeCurricular, UsuarioVO usuario) throws
    // Exception {
    // String sqlStr =
    // "SELECT SUM(CARGAHORARIA) FROM DISCIPLINA D INNER JOIN HISTORICO H ON H.DISCIPLINA = D.CODIGO ";
    // sqlStr +=
    // "WHERE H.MATRICULA = ? AND (H.SITUACAO = 'AP' OR H.SITUACAO = 'AA') ";
    // sqlStr += "AND H.disciplina in ( ";
    // sqlStr += "select disciplina.codigo from disciplina ";
    // sqlStr +=
    // "inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ";
    // sqlStr +=
    // "inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ";
    // sqlStr += "where periodoletivo.gradecurricular = ? ";
    // sqlStr += "order by disciplina.nome)";
    // try {
    // return (int) getConexao().getJdbcTemplate().queryForInt(sqlStr, new
    // Object[]{matricula, codigoGradeCurricular});
    // } finally {
    // sqlStr = null;
    // }
    // }

    public Integer consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(String matricula, Integer codigoGradeCurricular, Boolean trazerDisciplinaForaGrade, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select SUM(cargahorariadisciplina) as CARGAHORARIA from ( ");
        sb.append(" select case when (historico.situacao = 'AA' and (historico.cargahorariaaproveitamentodisciplina is not null and historico.cargahorariaaproveitamentodisciplina > 0)) then historico.cargahorariaaproveitamentodisciplina");
        sb.append(" when (historico.cargahorariadisciplina is not null and historico.cargahorariadisciplina > 0) then historico.cargahorariadisciplina");
        sb.append(" else gradedisciplina.cargahoraria end as cargahorariadisciplina ");
        sb.append(" from historico");
        sb.append(" left join gradedisciplina on gradedisciplina.codigo = historico.gradeDisciplina");
        sb.append(" WHERE historico.MATRICULA = '").append(matricula).append("' ");
        sb.append(" AND historico.SITUACAO in ('AP', 'AA', 'IS', 'CC', 'CH', 'AE') ");
        sb.append(" AND historico.matrizcurricular = ").append(codigoGradeCurricular);
        sb.append(" and (historico.gradecurriculargrupooptativadisciplina is not null or historico.gradedisciplina is not null or historicodisciplinaforagrade) ");
        sb.append(" and (historicoequivalente = false or historicoequivalente is null) ");
        sb.append(" and (historicodisciplinafazpartecomposicao = false or historicodisciplinafazpartecomposicao is null) ");
        if (trazerDisciplinaForaGrade != null && !trazerDisciplinaForaGrade) {
            sb.append(" and (historicodisciplinaforagrade is null or historicodisciplinaforagrade = false) ");
        }
        sb.append(" ) as t");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (rs.next()) {
            return rs.getInt("CARGAHORARIA");
        }
        return 0;

        // COMENTADO PELO RODRIGO
        // 23/09/2014
        // String sqlStr = "select sum(CARGAHORARIA) FROM(";
        // sqlStr +=
        // "SELECT SUM(gradeDisciplina.CARGAHORARIA) AS CARGAHORARIA FROM DISCIPLINA D INNER JOIN HISTORICO H ON H.DISCIPLINA = D.CODIGO INNER JOIN MATRICULAPERIODO ON MATRICULAPERIODO.CODIGO = H.MATRICULAPERIODO ";
        // sqlStr +=
        // "INNER JOIN periodoletivo on periodoletivo.gradecurricular = matriculaperiodo.gradecurricular ";
        // sqlStr +=
        // "INNER JOIN gradeDisciplina ON gradeDisciplina.disciplina = D.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ";
        // sqlStr +=
        // "WHERE H.MATRICULA = ? AND H.SITUACAO in ('AP', 'AA', 'IS') AND MATRICULAPERIODO.GRADECURRICULAR = ? ";
        // sqlStr += "AND (H.disciplina in ( ";
        // sqlStr += "select disciplina.codigo from disciplina ";
        // sqlStr +=
        // "inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ";
        // sqlStr +=
        // "inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ";
        // sqlStr += "where periodoletivo.gradecurricular = ? )";
        // sqlStr += "or H.disciplina in ( ";
        // sqlStr +=
        // "select disciplinaequivalente.equivalente from disciplinaequivalente ";
        // sqlStr +=
        // "inner join gradedisciplina on gradedisciplina.disciplina = disciplinaequivalente.disciplina ";
        // sqlStr +=
        // "inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ";
        // sqlStr += "where periodoletivo.gradecurricular = ? )";
        // sqlStr +=
        // " or H.disciplina in ( select matriculaperiodoturmadisciplina.disciplina from matriculaperiodoturmadisciplina inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ";
        // sqlStr +=
        // " where matriculaperiodo.gradecurricular = ? and disciplinaequivalente is not null and matriculaperiodo.matricula = ? and matriculaperiodoturmadisciplina.disciplina not in ( ";
        // sqlStr +=
        // " select disciplinaequivalente.equivalente from disciplinaequivalente inner join gradedisciplina on gradedisciplina.disciplina = disciplinaequivalente.disciplina  ";
        // sqlStr +=
        // " inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo where periodoletivo.gradecurricular = ?))) ";
        // sqlStr += " union all ";
        // sqlStr +=
        // " select SUM(cargaHoraria) AS CARGAHORARIA from inclusaodisciplinaForaGrade ";
        // sqlStr +=
        // " inner join disciplinaForaGrade on disciplinaForaGrade.inclusaodisciplinaForaGrade = inclusaodisciplinaForaGrade.codigo ";
        // sqlStr += " where matricula = '";
        // sqlStr += matricula.toString();
        // sqlStr += "' and situacao in ('AP', 'AA', 'IS') ";
        // sqlStr += " union all ";
        // sqlStr +=
        // " select SUM(gradeDisciplina.cargaHoraria) AS cargahoraria from historicogrademigradaequivalente ";
        // sqlStr +=
        // " inner join transferenciamatrizcurricular on transferenciamatrizcurricular.codigo = historicogrademigradaequivalente.transferenciamatrizcurricular ";
        // sqlStr +=
        // " inner join disciplina on disciplina.codigo = historicogrademigradaequivalente.disciplina ";
        // sqlStr +=
        // " INNER JOIN periodoletivo on periodoletivo.gradecurricular = " +
        // codigoGradeCurricular + " ";
        // sqlStr +=
        // " INNER JOIN gradeDisciplina ON gradeDisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ";
        // sqlStr += " where matricula = '";
        // sqlStr += matricula.toString();
        // sqlStr += "' and situacao in ('AP', 'AA', 'IS') ";
        // sqlStr += " and transferenciamatrizcurricular.gradeMigrar = " +
        // codigoGradeCurricular + " ";
        // sqlStr += " union all ";
        // sqlStr +=
        // " select SUM(gradeDisciplina.cargaHoraria) AS CARGAHORARIA from aproveitamentodisciplina  ";
        // sqlStr +=
        // " inner join concessaoCreditoDisciplina on concessaoCreditoDisciplina.aproveitamentodisciplina = aproveitamentodisciplina.codigo ";
        // sqlStr +=
        // " inner join disciplina on disciplina.codigo = concessaoCreditoDisciplina.disciplina ";
        // sqlStr +=
        // " inner join historico on historico.disciplina = disciplina.codigo and historico.matricula = aproveitamentodisciplina.matricula  and historico.matriculaperiodo = aproveitamentodisciplina.matriculaperiodo ";
        // sqlStr +=
        // " inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ";
        // sqlStr +=
        // " INNER JOIN periodoletivo on periodoletivo.gradecurricular = matriculaperiodo.gradecurricular ";
        // sqlStr +=
        // " INNER JOIN gradeDisciplina ON gradeDisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ";
        // sqlStr += " where aproveitamentodisciplina.matricula = '";
        // sqlStr += matricula.toString();
        // sqlStr += "' and historico.situacao = 'CC' ";
        // sqlStr += " union all ";
        // sqlStr += " select sum (j.cargahoraria) from ( ";
        // sqlStr +=
        // " select distinct historico.disciplina, gradedisciplina.cargahoraria from historico   ";
        // sqlStr +=
        // " inner join disciplina on disciplina.codigo = historico.disciplina ";
        // sqlStr +=
        // " inner join disciplinaequivalente on disciplinaequivalente.equivalente = disciplina.codigo ";
        // sqlStr +=
        // " inner join gradedisciplina on gradedisciplina.disciplina = disciplinaequivalente.disciplina ";
        // sqlStr +=
        // " inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ";
        // sqlStr += " where periodoletivo.gradecurricular = " +
        // codigoGradeCurricular + " ";
        // sqlStr += " and historico.matricula = '";
        // sqlStr += matricula.toString();
        // sqlStr += "' and historico.situacao in ('AP', 'AA', 'IS')";
        // sqlStr +=
        // " and gradedisciplina.disciplina in (select historico.disciplina from historico ";
        // sqlStr +=
        // " where historico.situacao in ('RF', 'RE') and historico.matricula = '"
        // + matricula.toString() + "')) as j";
        // sqlStr += " union all ";
        // sqlStr +=
        // " select distinct sum(gradedisciplina.cargahoraria) from historico ";
        // sqlStr +=
        // " inner join disciplina on disciplina.codigo = historico.disciplina ";
        // sqlStr +=
        // " inner join disciplinaequivalente on disciplinaequivalente.equivalente = disciplina.codigo ";
        // sqlStr +=
        // " inner join gradedisciplina on gradedisciplina.disciplina = disciplinaequivalente.disciplina ";
        // sqlStr +=
        // " inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ";
        // sqlStr += " where periodoletivo.gradecurricular = " +
        // codigoGradeCurricular + " ";
        // sqlStr += " and historico.matricula = '";
        // sqlStr += matricula.toString();
        // sqlStr += "' ";
        // sqlStr +=
        // " and gradedisciplina.disciplina in (select historico.disciplina from historico ";
        // sqlStr +=
        // " where historico.situacao in ('AP', 'AA', 'IS') and historico.matricula = '"
        // + matricula.toString() + "')";
        // sqlStr +=
        // " and disciplina.codigo not in (select historico.disciplina from historico  where historico.situacao in ('AP', 'AA', 'IS') and historico.matricula = '"
        // + matricula.toString() + "')";
        // sqlStr += ") as t";
        // try {
        // return (int) getConexao().getJdbcTemplate().queryForInt(sqlStr, new
        // Object[] { matricula, codigoGradeCurricular, codigoGradeCurricular,
        // codigoGradeCurricular, codigoGradeCurricular, matricula,
        // codigoGradeCurricular });
        // } finally {
        // sqlStr = null;
        // }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalenteEDisciplinaPorCorrespondecia(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario, String ano) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(historico.cargahorariadisciplina) AS CARGAHORARIA FROM historico ");
        sb.append(" WHERE historico.MATRICULA = '").append(matricula).append("' ");
        sb.append(" AND historico.SITUACAO in ('AP', 'AA', 'IS', 'CC', 'CH', 'AE') ");
        sb.append(" AND historico.matrizcurricular = ").append(codigoGradeCurricular);
        sb.append(" and (historico.gradecurriculargrupooptativadisciplina is not null or historico.gradedisciplina is not null) ");
        sb.append(" and (historicodisciplinaforagrade = false or historicodisciplinaforagrade is null)");
        sb.append(" and (historicoequivalente = false or historicoequivalente is null) ");
        sb.append(" and (historicodisciplinafazpartecomposicao = false or historicodisciplinafazpartecomposicao is null) ");
        if (Uteis.isAtributoPreenchido(ano)) {
            sb.append(" and historico.anohistorico <= '").append(ano).append("'");
        }
        return (int) getConexao().getJdbcTemplate().queryForInt(sb.toString());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer consultarCargaHorariaCumpridaNoHistoricoGradePorGradeCurricularPorMatricula(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(gradedisciplina.cargahoraria) from historicograde ");
        sb.append(" inner join disciplina on disciplina.codigo = historicograde.disciplina ");
        sb.append(" inner join transferenciamatrizcurricular tr on tr.codigo = historicograde.transferenciamatrizcurricular ");
        sb.append(" left join disciplina disciplinaequivalente on disciplinaequivalente.codigo = historicograde.disciplinaequivalente ");
        sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = ").append(codigoGradeCurricular);
        sb.append(" inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sb.append(" where (situacao = 'AP' or situacao = 'AA') ");
        sb.append(" and matricula = '").append(matricula).append("' ");
        sb.append(" and tr.gradeorigem = ").append(codigoGradeCurricular);
        try {
            return (int) getConexao().getJdbcTemplate().queryForInt(sb.toString());
        } finally {
            sb = null;
        }
    }

    public List<DisciplinaVO> consultarPorMatriculaPeriodoAnoSemestre(String matricula, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT distinct disciplina.* FROM historico ");
        sqlStr.append(" INNER JOIN matricula ON historico.matricula = matricula.matricula and matricula.gradecurricularatual = historico.matrizcurricular ");
        sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
        sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = historico.disciplina ");
        sqlStr.append(" WHERE matricula.matricula = '" + matricula + "'  ");
        sqlStr.append(" AND (historico.historicodisciplinacomposta is null or historicodisciplinacomposta = false) ");
        sqlStr.append(" AND (historico.historicoporequivalencia is null or historicoporequivalencia = false) ");
        if (ano != null && !ano.trim().isEmpty()) {
            sqlStr.append(" AND ((historico.anohistorico = '" + ano + "' and curso.periodicidade != 'IN') or (curso.periodicidade = 'IN')) ");
        }
        if (semestre != null && !semestre.trim().isEmpty()) {
            sqlStr.append(" AND ((historico.semestrehistorico = '" + semestre + "' and curso.periodicidade = 'SE') or (curso.periodicidade = 'IN'))");
        }
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }
    }

    public List<DisciplinaVO> consultarDisciplinasDoHistoricoPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM disciplina d ");
        sqlStr.append("INNER JOIN historico h ON d.codigo = h.disciplina ");
        sqlStr.append("INNER JOIN matriculaperiodo mp ON h.matriculaperiodo = mp.codigo ");
        sqlStr.append("WHERE mp.codigo = " + matriculaPeriodo + " ");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }
    }

    /**
     * Consulta as disciplinas de uma ou mais grades curriculares de acordo com
     * um código de disciplina ou uma parte do nome.
     *
     * @param codigoGrade      Código da grade curricular
     * @param codigoDisciplina Código da disciplina a procurar
     * @param nomeDisciplina   Nome completo ou parte do nome da disciplina a procurar
     * @param nivelMontarDados Nível de montar dados
     * @throws Se ocorrer algum erro no SQL.
     */
    public List<DisciplinaVO> consultarDisciplinasDaGradePorCodigoOuNome(Integer codigoDisciplina, String nomeDisciplina, int nivelMontarDados, UsuarioVO usuario, Integer... grades) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT d.*, gc.nome AS \"gradenome\"  FROM gradeDisciplina gd ");
        sqlStr.append("inner join disciplina d on gd.disciplina = d.codigo ");
        sqlStr.append("inner join periodoletivo pl on gd.periodoletivo = pl.codigo ");
        sqlStr.append("inner join gradecurricular gc on pl.gradecurricular = gc.codigo ");
        sqlStr.append("where 1 = 1 AND ( ");

        int contador = grades.length;
        for (Integer codigoGrade : grades) {
            contador = contador - 1;
            sqlStr.append("gc.codigo = ").append(codigoGrade).append(" ");
            if (contador != 0) {
                sqlStr.append("OR ");
            }
        }
        sqlStr.append(") ");
        if (codigoDisciplina != 0) {
            sqlStr.append("and d.codigo = " + codigoDisciplina + " ");
        }
        if (!nomeDisciplina.equals("")) {
            sqlStr.append("and lower(d.nome) like '" + nomeDisciplina.toLowerCase() + "%' ");
        }
        sqlStr.append(" union ");

        sqlStr.append("SELECT composta.*, gc.nome AS \"gradenome\"  FROM gradeDisciplina gd ");
        sqlStr.append(" inner join disciplina d on gd.disciplina = d.codigo ");
        sqlStr.append(" inner join disciplinacomposta on disciplinacomposta.disciplina = d.codigo");
        sqlStr.append(" inner join disciplina as composta on disciplinacomposta.composta = composta.codigo");
        sqlStr.append(" inner join periodoletivo pl on gd.periodoletivo = pl.codigo ");
        sqlStr.append(" inner join gradecurricular gc on pl.gradecurricular = gc.codigo ");
        sqlStr.append(" where 1 = 1 AND ( ");

        contador = grades.length;
        for (Integer codigoGrade : grades) {
            contador = contador - 1;
            sqlStr.append("gc.codigo = ").append(codigoGrade).append(" ");
            if (contador != 0) {
                sqlStr.append("OR ");
            }
        }
        sqlStr.append(") ");
        if (codigoDisciplina != 0) {
            sqlStr.append("and composta.codigo = " + codigoDisciplina + " ");
        }
        if (!nomeDisciplina.equals("")) {
            sqlStr.append("and lower(composta.nome) like '" + nomeDisciplina.toLowerCase() + "%' ");
        }

        sqlStr.append("order by nome ");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }
    }

    public List consultarDisciplinaHorarioProfessorPorCodigo(Integer pessoa, Integer disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct disciplina.* from horarioTurma ");
        sqlStr.append(" inner join turma on turma.codigo = horarioTurma.turma ");
        sqlStr.append(" inner join horarioTurmadia on horarioTurmadia.horarioTurma = horarioTurma.codigo ");
        sqlStr.append(" inner join horarioTurmadiaitem on horarioTurmadiaitem.horarioTurmadia = horarioTurmadia.codigo ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = horarioTurmadiaitem.professor ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = horarioTurmadiaitem.disciplina ");
        sqlStr.append(" where pessoa.codigo = ");
        sqlStr.append(pessoa.intValue());
        sqlStr.append(" and disciplina.codigo = ");
        sqlStr.append(disciplina.intValue());
        sqlStr.append(" and (( turma.semestral and horarioTurma.anoVigente = '");
        sqlStr.append(ano);
        sqlStr.append("' and horarioturma.semestreVigente = '");
        sqlStr.append(semestre);
        sqlStr.append("') or ( turma.anual and horarioTurma.anoVigente = '");
        sqlStr.append(ano).append("') or (turma.anual = false and turma.semestral = false and horarioTurma.anoVigente = '' and horarioTurma.semestreVigente = '')) ");
        sqlStr.append(" order by disciplina.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaMinistrouHorarioProfessorPorCodigo(Integer pessoa, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct(disciplina.codigo), disciplina.* from disciplina ");
        sqlStr.append(" inner join horarioTurmaProfessorDisciplina htpd on htpd.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = htpd.professor ");
        sqlStr.append(" inner join horarioTurma on horarioturma.codigo = htpd.horarioTurma ");
        sqlStr.append(" inner join horarioTurmadia on horarioturma.codigo = horarioturmadia.horarioTurma  ");
        sqlStr.append(" where pessoa.codigo = ");
        sqlStr.append(pessoa.intValue());
        sqlStr.append(" and ((horarioTurma.anoVigente = '");
        sqlStr.append(Uteis.getAnoDataAtual4Digitos());
        sqlStr.append("' and horarioturma.semestreVigente = '");
        sqlStr.append(Uteis.getSemestreAtual());
        sqlStr.append("') or (horarioTurma.anoVigente = '' and horarioTurma.semestreVigente = '')) ");
        sqlStr.append(" and horarioturmadia.data <= '");
        sqlStr.append(Uteis.getDataJDBCTimestamp(new Date()));
        sqlStr.append("'");
        // sqlStr.append(" and horarioTurma.turma = ");
        // sqlStr.append(turma);
        sqlStr.append(" order by disciplina.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaCursadasAlunoPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct(disciplina.codigo), disciplina.* from historico ");
        sqlStr.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.codigo = historico.matriculaperiodoturmadisciplina ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = mptd.disciplina ");
        sqlStr.append(" where historico.matricula = '");
        sqlStr.append(matricula);
        sqlStr.append("' and historico.situacao <> 'DP' ");
        sqlStr.append(" and historico.situacao <> 'IS' ");
        sqlStr.append(" and historico.situacao <> 'NC' ");
        sqlStr.append(" and historico.situacao <> 'TR' ");
        sqlStr.append(" and historico.situacao <> 'AC' ");
        sqlStr.append(" and historico.situacao <> 'CA' ");
        sqlStr.append(" and historico.situacao <> 'TF' ");
        sqlStr.append(" and historico.situacao <> 'CS' ");
        sqlStr.append(" order by disciplina.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaHorarioProfessorPorNome(Integer pessoa, String disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select distinct disciplina.* from horarioTurma ");
        sqlStr.append(" inner join turma on turma.codigo = horarioTurma.turma ");
        sqlStr.append(" inner join horarioTurmadia on horarioTurmadia.horarioTurma = horarioTurma.codigo ");
        sqlStr.append(" inner join horarioTurmadiaitem on horarioTurmadiaitem.horarioTurmadia = horarioTurmadia.codigo ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = horarioTurmadiaitem.professor ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = horarioTurmadiaitem.disciplina ");
        sqlStr.append(" where pessoa.codigo = ");
        sqlStr.append(pessoa.intValue());
        sqlStr.append(" and lower(sem_acentos(disciplina.nome)) like(sem_acentos('");
        sqlStr.append(disciplina.toLowerCase().toString());
        sqlStr.append("%')) ");
        sqlStr.append(" and (( turma.semestral and horarioTurma.anoVigente = '");
        sqlStr.append(ano);
        sqlStr.append("' and horarioturma.semestreVigente = '");
        sqlStr.append(semestre);
        sqlStr.append("') or ( turma.anual and horarioTurma.anoVigente = '");
        sqlStr.append(ano).append("') or (turma.anual = false and turma.semestral = false and horarioTurma.anoVigente = '' and horarioTurma.semestreVigente = '')) ");
        sqlStr.append(" order by disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaHorarioProfessorPorAreaConhecimento(Integer pessoa, String areaConhecimento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select disciplina.* from horarioTurma ");
        sqlStr.append(" inner join turma on turma.codigo = horarioTurma.turma ");
        sqlStr.append(" inner join horarioTurmadia on horarioTurmadia.horarioTurma = horarioTurma.codigo ");
        sqlStr.append(" inner join horarioTurmadiaitem on horarioTurmadiaitem.horarioTurmadia = horarioTurmadia.codigo ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = horarioTurmadiaitem.professor ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = horarioTurmadiaitem.disciplina ");
        sqlStr.append(" inner join areaconhecimento on areaconhecimento.codigo = disciplina.areaconhecimento ");
        sqlStr.append(" where pessoa.codigo = ");
        sqlStr.append(pessoa.intValue());
        sqlStr.append(" and lower(areaconhecimento.nome) like('");
        sqlStr.append(areaConhecimento.toString());
        sqlStr.append("%') ");
        sqlStr.append(" and (( turma.semestral and horarioTurma.anoVigente = '");
        sqlStr.append(ano);
        sqlStr.append("' and horarioturma.semestreVigente = '");
        sqlStr.append(semestre);
        sqlStr.append("') or ( turma.anual and horarioTurma.anoVigente = '");
        sqlStr.append(ano).append("') or (turma.anual = false and turma.semestral = false and horarioTurma.anoVigente = '' and horarioTurma.semestreVigente = '')) ");
        sqlStr.append(" order by disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaCoordenadorPorCodigo(Integer coordenador, Integer disciplina, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        //traz a disciplina da grade disciplina
        sqlStr.append(" (select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on  gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" inner join disciplina on  gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma  and turma.gradecurricular = gradecurricular.codigo  ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa =  ").append(coordenador);
        sqlStr.append(" AND disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }

        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        //traz a disciplina filha da composição da grade disciplina
        sqlStr.append(" limit 1) union (");
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on  gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" inner join gradedisciplinacomposta on  gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
        sqlStr.append(" inner join disciplina on  gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma  and turma.gradecurricular = gradecurricular.codigo  ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa = ").append(coordenador);
        sqlStr.append(" AND disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }

        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        //traz a disciplina do grupo de disciplina optativa
        sqlStr.append("limit 1) union (");
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativa on  periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on  gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join disciplina on  gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma and turma.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa = ").append(coordenador);
        sqlStr.append(" AND disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }

        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }

        //traz a disciplina filha da composição do grupo de disciplina optativa
        sqlStr.append(" limit 1) union (");
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativa on  periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on  gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradedisciplinacomposta on  gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sqlStr.append(" inner join disciplina on  gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma and turma.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa = ").append(coordenador);
        sqlStr.append(" AND disciplina.codigo = ").append(disciplina);
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        sqlStr.append(" limit 1) ORDER BY nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaCoordenadorPorNome(Integer coordenador, String nomeDisciplina, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        //traz a disciplina da grade disciplina
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on  gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" inner join disciplina on  gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma  and turma.gradecurricular = gradecurricular.codigo  ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa =  ").append(coordenador);
        sqlStr.append(" AND UPPER(disciplina.nome) like('").append(nomeDisciplina.toUpperCase()).append("%')");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        //traz a disciplina filha da composição da grade disciplina
        sqlStr.append(" union ");
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradedisciplina on  gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" inner join gradedisciplinacomposta on  gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
        sqlStr.append(" inner join disciplina on  gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma  and turma.gradecurricular = gradecurricular.codigo  ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa = ").append(coordenador);
        sqlStr.append(" AND UPPER(disciplina.nome) like('").append(nomeDisciplina.toUpperCase()).append("%')");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        //traz a disciplina do grupo de disciplina optativa
        sqlStr.append(" union ");
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativa on  periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on  gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join disciplina on  gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma and turma.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa = ").append(coordenador);
        sqlStr.append(" AND UPPER(disciplina.nome) like('").append(nomeDisciplina.toUpperCase()).append("%')");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }

        //traz a disciplina filha da composição do grupo de disciplina optativa
        sqlStr.append(" union ");
        sqlStr.append(" select distinct disciplina.* from gradecurricular ");
        sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativa on  periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on  gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradedisciplinacomposta on  gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sqlStr.append(" inner join disciplina on  gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
        sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
        sqlStr.append(" left join turma on turma.codigo = cursoCoordenador.turma and turma.gradecurricular = gradecurricular.codigo ");
        sqlStr.append(" left join turmadisciplina on turmadisciplina.turma = turma.codigo ");
        sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" where (cursoCoordenador.turma is null or (cursoCoordenador.turma is not null and gradecurricular.codigo = turma.gradecurricular ");
        sqlStr.append(" and periodoletivo.codigo = turma.periodoletivo and turmadisciplina.codigo is not null ");
        sqlStr.append(" )) ");
        sqlStr.append(" and funcionario.pessoa = ").append(coordenador);
        sqlStr.append(" AND UPPER(disciplina.nome) like('").append(nomeDisciplina.toUpperCase()).append("%')");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (!ano.equals("") && semestre.trim().isEmpty()) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and (((turma.anual or turma.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and (((t.anual or t.semestral) and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        if (!ano.trim().isEmpty() && !semestre.equals("")) {
            sqlStr.append(" and ((cursoCoordenador.turma is not null and exists (");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" where horarioturma.turma = turma.codigo ");
            sqlStr.append(" and ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false)) limit 1 ");
            sqlStr.append(" )) or (cursoCoordenador.turma is null and exists ( ");
            sqlStr.append(" select horarioturma.codigo from horarioturma  ");
            sqlStr.append(" inner join turma as t on horarioturma.turma = t.codigo ");
            sqlStr.append(" where t.curso = cursocoordenador.curso ");
            sqlStr.append(" and ((t.anual and HorarioTurma.anovigente = '").append(ano).append("' )");
            sqlStr.append(" or (t.semestral and HorarioTurma.anovigente = '").append(ano).append("' ").append(" and HorarioTurma.semestrevigente = '").append(semestre).append("' )");
            sqlStr.append(" or (t.anual = false and t.semestral = false)) limit 1 ");
            sqlStr.append(" )))");
        }
        sqlStr.append(" ORDER BY nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaCoordenadorPorAreaConhecimento(Integer coordenador, String areaConhecimento, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct(disciplina.codigo), disciplina.* FROM disciplina");
        sqlStr.append(" INNER JOIN turmaDisciplina ON turmaDisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" INNER JOIN turma ON turma.codigo = turmaDisciplina.turma");
        sqlStr.append(" LEFT JOIN HorarioTurma on HorarioTurma.turma = turma.codigo");
        sqlStr.append(" LEFT JOIN unidadeEnsino ON turma.unidadeEnsino = unidadeEnsino.codigo");
        sqlStr.append(" LEFT JOIN areaconhecimento ON areaconhecimento.codigo = disciplina.areaconhecimento ");
        sqlStr.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
        sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc");
        sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
        sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL)))");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        sqlStr.append(" AND UPPER(areaConhecimento.nome) like('").append(areaConhecimento.toUpperCase()).append("%')");
        if (!ano.equals("")) {
            sqlStr.append(" AND (HorarioTurma.anovigente = '").append(ano).append("' OR HorarioTurma.anovigente='')");
        }
        if (!semestre.equals("")) {
            sqlStr.append(" AND (HorarioTurma.semestrevigente = '").append(semestre).append("' OR HorarioTurma.semestrevigente='')");
        }
        sqlStr.append(" ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaCoordenadorPorTurma(Integer coordenador, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct(disciplina.codigo), disciplina.* FROM disciplina");
        sqlStr.append(" INNER JOIN turmaDisciplina ON turmaDisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" INNER JOIN turma ON turma.codigo = turmaDisciplina.turma");
        sqlStr.append(" LEFT JOIN unidadeEnsino ON turma.unidadeEnsino = unidadeEnsino.codigo");
        sqlStr.append(" LEFT JOIN areaconhecimento ON areaconhecimento.codigo = disciplina.areaconhecimento");
        sqlStr.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
        sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc");
        sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
        sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))");
        // Esse OR ele vai procurar por turma agrupada
        sqlStr.append(" OR (turma.codigo IN (");
        sqlStr.append(" SELECT distinct turmaagrupada.turmaorigem FROM turmaagrupada ");
        sqlStr.append(" INNER JOIN turma ON turma.codigo = turmaagrupada.turma ");
        sqlStr.append(" WHERE turma.codigo in(SELECT cc.turma FROM cursoCoordenador cc ");
        sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
        sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador).append(") ");
        sqlStr.append(" or turma.curso in(");
        sqlStr.append(" SELECT DISTINCT cc.curso FROM cursoCoordenador cc  ");
        sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
        sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL ))))");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" AND turma.codigo = ").append(turma);
        }
        sqlStr.append(" ORDER BY disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigo_UnidadeEnsino(Integer codigo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = " select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + " inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + " inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + " inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + " inner join curso on gradeCurricular.curso = curso.codigo " + " inner join unidadeEnsinoCurso on unidadeEnsinoCurso.curso = curso.codigo " + " inner join unidadeEnsino on unidadeEnsinoCurso.unidadeEnsino = unidadeEnsino.codigo " + " where disciplina.codigo = " + codigo + " and unidadeEnsino.codigo = " + unidadeEnsino;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuarioVO));
    }

    public List consultarPorCodigo_GradeCurricular(Integer codigo, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "where disciplina.codigo = " + codigo + "and curso.codigo = " + gradeCurricular;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuarioVO));
    }

    public List consultarPorNome_UnidadeEnsino(String nome, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = " select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + " inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + " inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + " inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + " inner join curso on gradeCurricular.curso = curso.codigo " + " inner join unidadeEnsinoCurso on unidadeEnsinoCurso.curso = curso.codigo " + " inner join unidadeEnsino on unidadeEnsinoCurso.unidadeEnsino = unidadeEnsino.codigo " + " where lower(disciplina.nome) like ('" + nome.toLowerCase() + "%') " + " and unidadeEnsino.codigo = " + unidadeEnsino;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuarioVO));
    }

    public List consultarPorNome_GradeCurricular(String nome, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "where lower(disciplina.nome) like ('" + nome.toLowerCase() + "%') ";
        if (Uteis.isAtributoPreenchido(gradeCurricular)) {
            sqlStr += " and gradecurricular.codigo = " + gradeCurricular;
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuarioVO));
    }

    public List consultarPorCodigoDisciplina_GradeCurricular(Integer disciplina, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = "select distinct disciplina.*, gradecurricular.nome as gradenome from disciplina " + "inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo " + "inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo " + "where disciplina.codigo = " + disciplina;
        if (Uteis.isAtributoPreenchido(gradeCurricular)) {
            sqlStr += " and gradecurricular.codigo = " + gradeCurricular;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuarioVO));
    }

    public List consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(Integer codigo, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplina gd on d.codigo = gd.disciplina ");
        sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
//		sqlStr.append("left join turma t on t.curso = c.codigo and t.periodoletivo = pl.codigo ");
        sqlStr.append("where d.codigo = ").append(codigo).append(" ");
        if (unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }
        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplinacomposta gdc on d.codigo = gdc.disciplina ");
        sqlStr.append("inner join gradedisciplina gd on gd.codigo = gdc.gradedisciplina ");
        sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
//		sqlStr.append("left join turma t on t.curso = c.codigo and t.periodoletivo = pl.codigo ");
        sqlStr.append("where d.codigo = ").append(codigo).append(" ");
        if (unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplinacomposta gdc on d.codigo = gdc.disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gdc.gradecurriculargrupooptativadisciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
        sqlStr.append("inner join periodoletivo pl on pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
//		sqlStr.append("left join turma t on t.curso = c.codigo and t.periodoletivo = pl.codigo ");
        sqlStr.append("where d.codigo = ").append(codigo).append(" ");
        if (unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on d.codigo = gradecurriculargrupooptativadisciplina.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
        sqlStr.append("inner join periodoletivo pl on pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
//		sqlStr.append("left join turma t on t.curso = c.codigo and t.periodoletivo = pl.codigo ");
        sqlStr.append("where d.codigo = ").append(codigo).append(" ");
        if (unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append("order by nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigoDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(Integer codigo, Integer unidadeEnsino, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct d.* from disciplina d ");
        sqlStr.append("inner join turmadisciplina on d.codigo = turmadisciplina.disciplina ");
        sqlStr.append("inner join turma t on t.codigo = turmadisciplina.turma ");
        sqlStr.append("where d.codigo = ").append(codigo).append(" ");
        if (unidadeEnsino != 0) {
            sqlStr.append("and t.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (turma != 0) {
            sqlStr.append("and t.codigo = ").append(turma).append(" ");
        }
        sqlStr.append("order by d.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(String nome, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplina gd on d.codigo = gd.disciplina ");
        sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
        //sqlStr.append("left join turma t on t.curso = c.codigo and t.periodoletivo = pl.codigo ");
        sqlStr.append("where lower(sem_acentos(d.nome)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplinacomposta gdc on d.codigo = gdc.disciplina ");
        sqlStr.append("inner join gradedisciplina gd on gd.codigo = gdc.gradedisciplina ");
        sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
        sqlStr.append("where lower(sem_acentos(d.nome)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplinacomposta gdc on d.codigo = gdc.disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gdc.gradecurriculargrupooptativadisciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
        sqlStr.append("inner join periodoletivo pl on pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");

        sqlStr.append("where lower(sem_acentos(d.nome)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on d.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
        sqlStr.append("inner join periodoletivo pl on pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");

        sqlStr.append("where lower(sem_acentos(d.nome)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append("order by nome");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nome + PERCENT, nome + PERCENT, nome + PERCENT, nome + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(String nome, Integer unidadeEnsino, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct d.* from disciplina d ");
        sqlStr.append("inner join turmadisciplina on d.codigo = turmadisciplina.disciplina ");
        sqlStr.append("inner join turma t on t.codigo = turmadisciplina.turma ");
        sqlStr.append("where lower(d.nome) like '").append(nome.toLowerCase()).append("%' ");
        if (unidadeEnsino != 0) {
            sqlStr.append("and t.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (turma != 0) {
            sqlStr.append("and t.codigo = ").append(turma).append(" ");
        }
        sqlStr.append("order by d.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarDisciplinaPorMatriculaPeriodoParaVisualizarNota(String matricula, Integer periodoLetivoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("select disciplina.* from matriculaPeriodo ");
        sql.append(" inner join historico on historico.matriculaPeriodo = matriculaPeriodo.codigo ");
        sql.append(" inner join disciplina on historico.disciplina = disciplina.codigo ");
        sql.append(" where  matriculaPeriodo.matricula = '").append(matricula).append("' and matriculaPeriodo.periodoLetivoMatricula = ").append(periodoLetivoMatricula.intValue());
        sql.append(" union all ");
        sql.append(" select disciplina.* from matriculaPeriodo ");
        sql.append(" inner join matriculaPeriodo as mpa on mpa.codigo <> matriculaPeriodo.codigo and matriculaPeriodo.periodoLetivoMatricula= mpa.periodoLetivoMatricula");
        sql.append(" and mpa.matricula = matriculaPeriodo.matricula ");
        sql.append(" inner join historico on (historico.matriculaPeriodo = mpa.codigo and (historico.situacao = 'AP' or historico.situacao = 'AA')) ");
        sql.append(" inner join disciplina on historico.disciplina = disciplina.codigo ");
        sql.append(" where  matriculaPeriodo.matricula = '").append(matricula).append("' and matriculaPeriodo.periodoLetivoMatricula = ").append(periodoLetivoMatricula.intValue());
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        while (dadosSQL.next()) {
            DisciplinaVO obj = new DisciplinaVO();
            obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
            obj.setNome(dadosSQL.getString("nome"));
            obj.getAreaConhecimento().setCodigo(new Integer(dadosSQL.getInt("areaConhecimento")));
            obj.setDisciplinaComposta(dadosSQL.getBoolean("disciplinaComposta"));
            vetResultado.add(obj);
        }
        return vetResultado;

    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaDoProfessorEAD(Integer codigoPessoa, Integer unidadeEnsino, Integer curso, Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        getSQLPadraoConsultaDisciplinasProfessorEad(codigoPessoa, unidadeEnsino, curso, turma, sqlStr);
        sqlStr.append(" order by nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinasDoProfessor(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, String semestre, String ano, int nivelMontarDados, Boolean disciplinasEAD, Boolean trazerDisciplinaMaeComposicao, UsuarioVO usuario) throws Exception {
        return consultarDisciplinasDoProfessor(professor, unidadeEnsino, curso, turma, semestre, ano, nivelMontarDados, disciplinasEAD, trazerDisciplinaMaeComposicao, usuario, false);
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinasDoProfessor(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, String semestre, String ano, int nivelMontarDados, Boolean disciplinasEAD, Boolean trazerDisciplinaMaeComposicao, UsuarioVO usuario, boolean professorExclusivo) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        getSQLPadraoConsultaDisciplinasProfessorProgramacaoAula(professor, unidadeEnsino, curso, turma, sqlStr);
        if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
            sqlStr.append(" and ((turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("') ");
            sqlStr.append(" or (turma.anual and HorarioTurma.anovigente = '").append(ano).append("' ) ");
            sqlStr.append(" or (turma.anual = false and turma.semestral = false )) ");
        }

        if (trazerDisciplinaMaeComposicao) {
            sqlStr.append(" union ");
            sqlStr.append(" select distinct disciplina.* ");
            sqlStr.append(" from horarioturma ");
            sqlStr.append(" INNER JOIN turma on (turma.codigo = horarioTurma.turma) ");
            sqlStr.append(" INNER JOIN HorarioTurmadia on (HorarioTurmadia.HorarioTurma = horarioTurma.codigo) ");
            sqlStr.append(" INNER JOIN HorarioTurmadiaitem on (HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo) ");
            sqlStr.append(" INNER JOIN turmadisciplina on ((turma.turmaagrupada = false and turma.codigo = turmadisciplina.turma) or  ");
            sqlStr.append(" (turma.turmaagrupada  and turmadisciplina.codigo in (select td.codigo from turmadisciplina td ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turma = td.turma ");
            sqlStr.append(" and turmaagrupada.turmaorigem = turma.codigo))) ");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo is not null and gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina  ");
            sqlStr.append(" and gradedisciplinacomposta.disciplina = HorarioTurmadiaitem.disciplina ");
            sqlStr.append(" inner join disciplina on gradedisciplina.codigo is not null and disciplina.codigo = gradedisciplina.disciplina  ");
            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo  )))   ");
            }
//			if(usuario.getIsApresentarVisaoProfessor()){
//				sqlStr.append(" and ").append(getSqlPadraoValidacaoExistenciaPeriodoLetivoAtivoUnidadeEnsinoCurso());
//			}
            sqlStr.append(" where  HorarioTurmadiaitem.professor =  ").append(professor);
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sqlStr.append(" and   turma.unidadeensino =  ").append(unidadeEnsino);
            }

            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" and   curso.codigo =  ").append(curso);
            }
            if (Uteis.isAtributoPreenchido(turma)) {
                sqlStr.append(" and   turma.codigo =  ").append(turma);
            }
            if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
                sqlStr.append(" and ((turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("') ");
                sqlStr.append(" or (turma.anual and HorarioTurma.anovigente = '").append(ano).append("' ) ");
                sqlStr.append(" or (turma.anual = false and turma.semestral = false )) ");
            }

            sqlStr.append(" union ");
            sqlStr.append(" select distinct disciplina.* ");
            sqlStr.append(" from horarioturma ");
            sqlStr.append(" INNER JOIN turma on (turma.codigo = horarioTurma.turma) ");
            sqlStr.append(" INNER JOIN HorarioTurmadia on (HorarioTurmadia.HorarioTurma = horarioTurma.codigo) ");
            sqlStr.append(" INNER JOIN HorarioTurmadiaitem on (HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo) ");
            sqlStr.append(" INNER JOIN turmadisciplina on ((turma.turmaagrupada = false and turma.codigo = turmadisciplina.turma) or  ");
            sqlStr.append(" (turma.turmaagrupada  and turmadisciplina.codigo in (select td.codigo from turmadisciplina td ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turma = td.turma ");
            sqlStr.append(" and turmaagrupada.turmaorigem = turma.codigo))) ");
            sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
            sqlStr.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo is not null and gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
            sqlStr.append(" and gradedisciplinacomposta.disciplina = HorarioTurmadiaitem.disciplina ");
            sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.codigo is not null and disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo  )))   ");
            }
//			if(usuario.getIsApresentarVisaoProfessor()){
//				sqlStr.append(" and ").append(getSqlPadraoValidacaoExistenciaPeriodoLetivoAtivoUnidadeEnsinoCurso());
//			}
            sqlStr.append(" where  HorarioTurmadiaitem.professor =  ").append(professor);
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sqlStr.append(" and   turma.unidadeensino =  ").append(unidadeEnsino);
            }

            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" and   curso.codigo =  ").append(curso);
            }
            if (Uteis.isAtributoPreenchido(turma)) {
                sqlStr.append(" and   turma.codigo =  ").append(turma);
            }
            if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
                sqlStr.append(" and ((turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("') ");
                sqlStr.append(" or (turma.anual and HorarioTurma.anovigente = '").append(ano).append("' ) ");
                sqlStr.append(" or (turma.anual = false and turma.semestral = false )) ");
            }
        }

        if (disciplinasEAD) {
            sqlStr.append(" union");
            getSQLPadraoConsultaDisciplinasProfessorEad(professor, unidadeEnsino, curso, turma, sqlStr);
        }

        if (professorExclusivo) {
            sqlStr.append(" union");
            sqlStr.append(" select disciplina.* from disciplina");
            sqlStr.append(" inner join calendariolancamentonota on calendariolancamentonota.disciplina = disciplina.codigo");
            sqlStr.append(" where 1 = 1");
            if (Uteis.isAtributoPreenchido(ano)) {
                sqlStr.append(" and calendariolancamentonota.ano =  '").append(ano).append("'");
            }
            if (Uteis.isAtributoPreenchido(semestre)) {
                sqlStr.append(" and calendariolancamentonota.semestre ='").append(semestre).append("'");
            }

            if (Uteis.isAtributoPreenchido(turma)) {
                sqlStr.append(" and calendariolancamentonota.turma =").append(turma);
            }

            if (Uteis.isAtributoPreenchido(professor)) {
                sqlStr.append(" and calendariolancamentonota.professorexclusivolancamentodenota =").append(professor);
            }
        }

        sqlStr.append(" order by nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

    }

    public void getSQLPadraoConsultaDisciplinasProfessorProgramacaoAula(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, StringBuilder sqlStr) {
        sqlStr.append(" select distinct disciplina.* ");
        sqlStr.append(" from horarioturma ");
        sqlStr.append(" INNER JOIN turma on (turma.codigo = horarioTurma.turma) ");
        sqlStr.append(" INNER JOIN HorarioTurmadia on (HorarioTurmadia.HorarioTurma = horarioTurma.codigo) ");
        sqlStr.append(" INNER JOIN HorarioTurmadiaitem on (HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo) ");
        sqlStr.append(" INNER JOIN disciplina on (HorarioTurmadiaitem.disciplina = disciplina.codigo)  ");
        sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
        sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
        sqlStr.append(" Inner join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
        // Comentado pelo Rodrigo pois o professor poderá visualizar as disciplinas baseado no perfil de acesso que permite ou não verificar turma anteriores, então a regra abaixo estava conflitando com outras regras do sistema
//		if(usuario.getIsApresentarVisaoProfessor()){
//			sqlStr.append(" and ").append(getSqlPadraoValidacaoExistenciaPeriodoLetivoAtivoUnidadeEnsinoCurso());
//		}
        sqlStr.append(" where  HorarioTurmadiaitem.professor =  ").append(professor);
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append(" and   turma.unidadeensino =  ").append(unidadeEnsino);
        }

        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" and   curso.codigo =  ").append(curso);
        }
        if (Uteis.isAtributoPreenchido(turma)) {
            sqlStr.append(" and   turma.codigo =  ").append(turma);
        }
    }

    public void getSQLPadraoConsultaDisciplinasProfessorEad(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, StringBuilder sqlStr) {
        if (!Uteis.isAtributoPreenchido(turma) && !Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" select distinct disciplina.* from programacaotutoriaonline");
            sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina");
            sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
            sqlStr.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
            sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
            }


        } else {
            sqlStr.append(" select distinct disciplina.* from programacaotutoriaonline ");
            sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.disciplina = programacaotutoriaonline.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA' ");
            sqlStr.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
            sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
            sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
            sqlStr.append(" Inner join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
            //sqlStr.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo  )))   ");
            sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
            sqlStr.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
            sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sqlStr.append(" and turma.unidadeensino = ").append(unidadeEnsino);
                sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
            }

            sqlStr.append(" and programacaotutoriaonline.turma is null ");
            if (Uteis.isAtributoPreenchido(turma)) {
                sqlStr.append(" and turma.codigo = ").append(turma);
            }
            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" and programacaotutoriaonline.curso is null and curso.codigo = ").append(curso);
            }

            sqlStr.append(" union");
            sqlStr.append(" select distinct disciplina.* from programacaotutoriaonline");
            sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina ");
            sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
            sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
            sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
            sqlStr.append(" Inner join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
            //sqlStr.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo  )))   ");
            sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
            sqlStr.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
            sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sqlStr.append(" and turma.unidadeensino = ").append(unidadeEnsino);
                sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
            }

            if (Uteis.isAtributoPreenchido(turma)) {
                sqlStr.append(" and turma.codigo =  ").append(turma);
            }
            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" and programacaotutoriaonline.curso is null and curso.codigo = ").append(curso);
            }

            if (Uteis.isAtributoPreenchido(curso)) {
                sqlStr.append(" union");
                sqlStr.append(" select distinct disciplina.* from programacaotutoriaonline");
                sqlStr.append(" inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina");
                sqlStr.append(" inner join curso on curso.codigo = programacaotutoriaonline.curso");
                sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
                sqlStr.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
                sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
                if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                    sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
                }
                sqlStr.append(" and curso.codigo =  ").append(curso);
            }
        }
    }

	/*@Override
	public List<DisciplinaVO> consultarDisciplinasPorMatriculaPeriodoTurmaDisciplina(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, String semestre, String ano,
			int nivelMontarDados, Boolean disciplinasEAD, List<ModalidadeDisciplinaEnum> modalidadeDisciplinaEnums, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct disciplina.*  from matriculaperiodoturmadisciplina ");
		sqlStr.append(" inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sqlStr.append(" where 1 = 1 ");
		if (Uteis.isAtributoPreenchido(modalidadeDisciplinaEnums)) {
			sqlStr.append(modalidadeDisciplinaEnums.stream()
					.map(ModalidadeDisciplinaEnum::name)
					.collect(Collectors.joining("', '", " AND matriculaperiodoturmadisciplina.modalidadedisciplina in ('", "') ")));
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sqlStr.append(" and  matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(curso)){
			sqlStr.append(" and  matricula.curso = ").append(curso);
		}
		if(Uteis.isAtributoPreenchido(turma)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.turma = ").append(turma);
		}
		if(Uteis.isAtributoPreenchido(ano)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(matricula)){
			sqlStr.append(" and  matricula.matricula = '").append(matricula).append("' ");
		}
		sqlStr.append(" order by disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

	}*/

    public DisciplinaVO consultarDisciplinaPorDisciplinaCompostaMatriculaPeriodo(Integer subDisciplina, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select distinct d1Composta.codigo, d1Composta.nome from disciplina ");
        sqlStr.append(" inner join disciplinacomposta on disciplinaComposta.composta = disciplina.codigo ");
        sqlStr.append(" inner join disciplina d1Composta on d1Composta.codigo = disciplinaComposta.disciplina ");
        if (matriculaPeriodo != null && matriculaPeriodo > 0) {
            sqlStr.append(" inner join historico on historico.disciplina = d1Composta.codigo ");
        }
        sqlStr.append("	where disciplina.codigo = ").append(subDisciplina);
        if (matriculaPeriodo != null && matriculaPeriodo > 0) {
            sqlStr.append(" and historico.matriculaPeriodo = ").append(matriculaPeriodo);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new DisciplinaVO();
        }
        DisciplinaVO obj = new DisciplinaVO();
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.setNome(tabelaResultado.getString("nome"));
        return obj;

    }

    public void validarDadosDisciplinaComposta(DisciplinaCompostaVO obj) throws Exception {
        if (obj.getCompostaVO().getCodigo().equals(0)) {
            throw new Exception("O campo DISCIPLINA (Disciplinas Compostas) deve ser informado.");
        }
    }

    public void adicionarObjDisciplinaCompostaVOs(DisciplinaCompostaVO disciplinaCompostaVO, DisciplinaVO obj) throws Exception {
        if (disciplinaCompostaVO.getCompostaVO().getCodigo().equals(obj.getCodigo()) && !obj.getCodigo().equals(0)) {
            throw new Exception();
        }
        validarDadosDisciplinaComposta(disciplinaCompostaVO);
        disciplinaCompostaVO.setOrdem(obj.getDisciplinaCompostaVOs().size());
        int index = 0;
        Iterator i = obj.getDisciplinaCompostaVOs().iterator();
        while (i.hasNext()) {
            DisciplinaCompostaVO objExistente = (DisciplinaCompostaVO) i.next();
            if (objExistente.getCompostaVO().getCodigo().equals(disciplinaCompostaVO.getCompostaVO().getCodigo())) {
                obj.getDisciplinaCompostaVOs().set(index, disciplinaCompostaVO);
                return;
            }
            index++;
        }
        obj.getDisciplinaCompostaVOs().add(disciplinaCompostaVO);
    }

    public void excluirObjDisciplinaCompostaVOs(Integer composta, DisciplinaVO obj) throws Exception {
        int index = 0;
        Iterator i = obj.getDisciplinaCompostaVOs().iterator();
        while (i.hasNext()) {
            DisciplinaCompostaVO objExistente = (DisciplinaCompostaVO) i.next();
            if (objExistente.getCompostaVO().getCodigo().equals(composta)) {
                obj.getDisciplinaCompostaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public void moverParaBaixo(DisciplinaCompostaVO obj, List listaDiscipinaComposta) {
        DisciplinaCompostaVO aux = new DisciplinaCompostaVO();
        Iterator i = listaDiscipinaComposta.iterator();
        while (i.hasNext()) {
            DisciplinaCompostaVO dre = (DisciplinaCompostaVO) i.next();
            if (dre.getOrdem() == obj.getOrdem() + 1) {
                aux = obj;
                obj = dre;
                dre = aux;
                listaDiscipinaComposta.set(obj.getOrdem().intValue(), dre);
                listaDiscipinaComposta.set(dre.getOrdem().intValue(), obj);
                break;
            }
        }
        enumeraQuestionario(listaDiscipinaComposta);
    }

    public void enumeraQuestionario(List listaDRE) {
        Iterator i = listaDRE.iterator();
        int cont = 0;
        while (i.hasNext()) {
            DisciplinaCompostaVO obj = (DisciplinaCompostaVO) i.next();
            obj.setOrdem(cont);
            cont++;
        }
    }

    public void moverParaCima(DisciplinaCompostaVO obj, List listaDiscipinaComposta) {
        DisciplinaCompostaVO aux = new DisciplinaCompostaVO();
        Iterator i = listaDiscipinaComposta.iterator();
        while (i.hasNext()) {
            DisciplinaCompostaVO dre = (DisciplinaCompostaVO) i.next();
            if (dre.getOrdem() == obj.getOrdem() - 1) {
                aux = obj;
                obj = dre;
                dre = aux;
                listaDiscipinaComposta.set(obj.getOrdem().intValue(), dre);
                listaDiscipinaComposta.set(dre.getOrdem().intValue(), obj);
                break;
            }
        }
        enumeraQuestionario(listaDiscipinaComposta);
    }

    public void validarDadosRemoverDisciplinaComposta(Integer disciplinaComposta, UsuarioVO usuarioVO) throws Exception {
        Boolean existeGradeAtivaUsandoDisciplina = consultarDisciplinaCompostaGradeCurricularAtiva(disciplinaComposta, usuarioVO);
        if (existeGradeAtivaUsandoDisciplina) {
            throw new Exception("Não é possível remover a disciplina porquê a mesma se encontra em uso com a grade curricular ativa.");
        }
    }

    public Boolean consultarDisciplinaCompostaGradeCurricularAtiva(Integer disciplinaComposta, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select gradecurricular.codigo from gradecurricular ");
        sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
        sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sb.append(" where gradecurricular.situacao = 'AT' ");
        sb.append(" and gradedisciplina.disciplina = ").append(disciplinaComposta);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    @Override
    public List consultarPorCodigoEspecifico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr;
        if (valorConsulta == null || valorConsulta == 0) {
            sqlStr = "SELECT * FROM disciplina WHERE codigo >= 0 ORDER BY codigo";
        } else {
            sqlStr = "SELECT * FROM disciplina WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List consultarPorNomeEpecifico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr;
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if (valorConsulta != null && !valorConsulta.equals("")) {
            sqlStr = "SELECT * FROM disciplina WHERE lower (sem_acentos(nome)) like lower(sem_acentos(?)) ORDER BY nome";
        } else {
            sqlStr = "SELECT * FROM disciplina ORDER BY codigo";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consutlarDisciplinaPorCursoEPeriodoLetivo(Integer codigoPeriodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr;
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        try {
            sqlStr = "SELECT disciplina.* from disciplina inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo where gradedisciplina.periodoletivo = " + codigoPeriodoLetivo + " order by disciplina.nome";
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

        } catch (Exception e) {
            throw e;
        }

    }

    public List<DisciplinaVO> consutlarDisciplinaPorCurso(Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr;
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        try {
            sqlStr = "SELECT distinct disciplina.codigo,disciplina.nome, disciplina.abreviatura, descricaoComplementar ,disciplina.niveleducacional from disciplina " + "inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo " + "inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo " + "inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular " + "where gradecurricular.curso = " + codigoCurso + " order by disciplina.nome";
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));

        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public List<DisciplinaVO> consutlarDisciplinaPorCodigoTurma(Integer codigoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        try {
            sqlStr.append(" SELECT distinct disciplina.*");
            sqlStr.append(" from disciplina");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.disciplina=disciplina.codigo");
            sqlStr.append(" inner join turma on turmadisciplina.turma= turma.codigo");
            sqlStr.append(" where turma.codigo=").append(codigoTurma);
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

        } catch (Exception e) {
            throw e;
        }

    }

    public DisciplinaVO consultarDisciplinaEquivalenteTurmaAgrupadaPorTurma(Integer turma, Integer turmaOrigem, Integer disciplina, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select disciplina.codigo, disciplina.nome, 1 as ordem from turmadisciplina ");
        sb.append("inner join disciplina  on disciplina.codigo = turmadisciplina.disciplinaEquivalenteTurmaAgrupada ");
        sb.append("inner join turmaagrupada on turmaagrupada.turmaorigem = turmadisciplina.turma ");
        sb.append("inner join turmadisciplina as turmadisciplinaturmabase  on turmadisciplinaturmabase.turma = turmaagrupada.turma ");
        sb.append("and turmadisciplinaturmabase.disciplina  = turmadisciplina.disciplinaEquivalenteTurmaAgrupada ");
        sb.append("where turmaagrupada.turma = ").append(turma);
        sb.append(" and  turmaagrupada.turmaorigem = ").append(turmaOrigem);
        if (disciplina != null && disciplina > 0) {
            sb.append(" and turmadisciplina.disciplina = ").append(disciplina);
        }
        sb.append(" union ");
        sb.append("select disciplina.codigo, disciplina.nome, 2 as ordem from turmadisciplina ");
        sb.append("inner join disciplinaequivalente  on disciplinaequivalente.disciplina = turmadisciplina.disciplina  ");
        sb.append("inner join disciplina  on disciplina.codigo = disciplinaequivalente.equivalente ");
        sb.append("inner join turmaagrupada on turmaagrupada.turmaorigem = turmadisciplina.turma ");
        sb.append("inner join turmadisciplina as turmadisciplinaturmabase  on turmadisciplinaturmabase.turma = turmaagrupada.turma ");
        sb.append("and turmadisciplinaturmabase.disciplina  = disciplina.codigo ");
        sb.append("where turmaagrupada.turma = ").append(turma);
        sb.append(" and  turmaagrupada.turmaorigem = ").append(turmaOrigem);
        if (disciplina != null && disciplina > 0) {
            sb.append(" and turmadisciplina.disciplina = ").append(disciplina);
        }
        sb.append(" union ");
        sb.append("select disciplina.codigo, disciplina.nome, 3 as ordem from turmadisciplina ");
        sb.append("inner join disciplinaequivalente  on disciplinaequivalente.equivalente = turmadisciplina.disciplina  ");
        sb.append("inner join disciplina  on disciplina.codigo = disciplinaequivalente.disciplina ");
        sb.append("inner join turmaagrupada on turmaagrupada.turmaorigem = turmadisciplina.turma ");
        sb.append("inner join turmadisciplina as turmadisciplinaturmabase  on turmadisciplinaturmabase.turma = turmaagrupada.turma ");
        sb.append("and turmadisciplinaturmabase.disciplina  = disciplina.codigo ");
        sb.append("where turmaagrupada.turma = ").append(turma);
        sb.append(" and  turmaagrupada.turmaorigem = ").append(turmaOrigem);
        if (disciplina != null && disciplina > 0) {
            sb.append(" and turmadisciplina.disciplina = ").append(disciplina);
        }
        sb.append(" order by ordem limit 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        DisciplinaVO obj = new DisciplinaVO();
        if (tabelaResultado.next()) {
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
        }
        return obj;
    }

    @Override
    public Integer consultarTotalCargaHorariaCursadaInclusaoForaGradeTransferenciaMatrizCurricularConcessaoCredito(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("select sum(CARGAHORARIA) FROM(");
        sqlStr.append(" select SUM(cargaHoraria) AS CARGAHORARIA from inclusaodisciplinaForaGrade ");
        sqlStr.append(" inner join disciplinaForaGrade on disciplinaForaGrade.inclusaodisciplinaForaGrade = inclusaodisciplinaForaGrade.codigo ");
        sqlStr.append(" where matricula = '");
        sqlStr.append(matricula.toString());
        sqlStr.append("' and situacao = 'AP' or situacao = 'AA' ");

        // sqlStr.append(" union all ");
        // sqlStr.append(" select SUM(cargaHoraria) AS CARGAHORARIA from inclusaodisciplinaForaGrade ");
        // sqlStr.append(" inner join disciplinaForaGrade on disciplinaForaGrade.inclusaodisciplinaForaGrade = inclusaodisciplinaForaGrade.codigo ");
        // sqlStr.append(" where matricula = '").append(matricula).append("' ");
        // sqlStr.append(" and situacao = 'AP' or situacao = 'AA'");

        sqlStr.append(" union all ");
        sqlStr.append(" select SUM(gradeDisciplina.cargaHoraria) AS cargahoraria from historicogrademigradaequivalente ");
        sqlStr.append(" inner join transferenciamatrizcurricular on transferenciamatrizcurricular.codigo = historicogrademigradaequivalente.transferenciamatrizcurricular ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = historicogrademigradaequivalente.disciplina ");
        sqlStr.append(" INNER JOIN periodoletivo on periodoletivo.gradecurricular = ").append(codigoGradeCurricular);
        sqlStr.append(" INNER JOIN gradeDisciplina ON gradeDisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" where matricula = '");
        sqlStr.append(matricula.toString());
        sqlStr.append("' and (situacao = 'AP' or situacao = 'AA') ");
        sqlStr.append(" and transferenciamatrizcurricular.gradeMigrar = ").append(codigoGradeCurricular);
        sqlStr.append(" union all ");
        sqlStr.append(" select SUM(gradeDisciplina.cargaHoraria) AS CARGAHORARIA from aproveitamentodisciplina  ");
        sqlStr.append(" inner join concessaoCreditoDisciplina on concessaoCreditoDisciplina.aproveitamentodisciplina = aproveitamentodisciplina.codigo ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = concessaoCreditoDisciplina.disciplina ");
        sqlStr.append(" inner join historico on historico.disciplina = disciplina.codigo and historico.matricula = aproveitamentodisciplina.matricula  and historico.matriculaperiodo = aproveitamentodisciplina.matriculaperiodo ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sqlStr.append(" INNER JOIN periodoletivo on periodoletivo.gradecurricular = matriculaperiodo.gradecurricular ");
        sqlStr.append(" INNER JOIN gradeDisciplina ON gradeDisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append(" where aproveitamentodisciplina.matricula = '");
        sqlStr.append(matricula.toString());
        sqlStr.append("' and historico.situacao = 'CC' ");
        sqlStr.append(") as t");
        try {
            return (int) getConexao().getJdbcTemplate().queryForInt(sqlStr.toString());
        } finally {
            sqlStr = null;
        }
    }

    @Override
    public boolean consultarDisciplinaSeClassificadaComoTcc(Integer codigo, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT  count(Disciplina.codigo) as QTDE  FROM Disciplina ");
        sqlStr.append("WHERE Disciplina.codigo = ? ").append(" and Disciplina.classificacaoDisciplina = 'TCC' ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
        return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
    }

    @Override
    public boolean consultarSeExisteDisciplinaPadraoTccPorGradeCurricular(Integer gradeCurricular, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT  count(distinct (Disciplina.codigo)) as QTDE  FROM Disciplina ");
        sqlStr.append("inner join gradecurricular on gradecurricular.disciplinapadraotcc = disciplina.codigo ");
        sqlStr.append("WHERE gradecurricular.codigo = ? ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), gradeCurricular);
        return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
    }

    @Override
    public DisciplinaVO consultarPorGradeCurricularDisciplinaPadraoTcc(Integer gradeCurricular, boolean retornarExcecao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT disciplina.* FROM Disciplina ");
        sqlStr.append("inner join gradecurricular on gradecurricular.disciplinapadraotcc = disciplina.codigo ");
        sqlStr.append("WHERE gradecurricular.codigo = ? ");
        sqlStr.append("ORDER BY Disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), gradeCurricular);
        if (!tabelaResultado.next()) {
            if (retornarExcecao) {
                throw new ConsistirException("Dados Não Encontrados (Disciplina).");
            } else {
                return null;
            }
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarPorGradeCurricularDisciplinaPadraoTcc(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT disciplina.* FROM Disciplina ");
        sqlStr.append("inner join gradecurricular on gradecurricular.disciplinapadraotcc = disciplina.codigo and disciplina.classificacaoDisciplina = 'TCC' ");
        sqlStr.append("WHERE 1=1 ");
        sqlStr.append("ORDER BY Disciplina.abreviatura, Disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarPorDisciplinaClassificadaTccPorDivisaoGrupo(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT disciplina.* FROM Disciplina ");
        sqlStr.append("WHERE classificacaoDisciplina = '").append(ClassificacaoDisciplinaEnum.TCC.name()).append("' ");
        sqlStr.append("and dividirSalaEmGrupo ");
        sqlStr.append("ORDER BY Disciplina.abreviatura, Disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaPorClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT disciplina.* FROM Disciplina ");
        sqlStr.append("WHERE classificacaoDisciplina = ? ");
        sqlStr.append("ORDER BY Disciplina.abreviatura, Disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), classificacaoDisciplinaEnum.name());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarPorTurmaAnoSemestre(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT (disciplina.codigo), Disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN TurmaDisciplina on TurmaDisciplina.disciplina = Disciplina.codigo ");
        sqlStr.append("INNER JOIN Turma on Turma.codigo = TurmaDisciplina.turma ");
        sqlStr.append("WHERE TurmaDisciplina.turma = ").append(turma);
        sqlStr.append("ORDER BY Disciplina.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarHorarioTurmaDisciplinaProgramadaPorTurma(Integer turma, boolean trazerDisciplinaCompostaPrincipal, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        StringBuilder sqlStr = new StringBuilder("");
        // Tras as disciplina compostas da grade disciplina de uma turma normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina =  gradedisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union ");
        if (trazerDisciplinaCompostaPrincipal) {
            sqlStr.append(" select distinct disciplina.* from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina =  gradedisciplina.codigo");
            sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union ");
        }

        // Tras as disciplina da grade disciplina de uma turma normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union ");
        // Tras as disciplina compostas da grade disciplina de uma turma
        // composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina =  gradedisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union");
        if (trazerDisciplinaCompostaPrincipal) {
            sqlStr.append(" select distinct disciplina.* from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
            sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
            sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina =  gradedisciplina.codigo");
            sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union");
        }
        // Tras as disciplina da grade disciplina de uma turma composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union ");
        // Tras as disciplina compostas de um grupo de optativas de uma turma
        // normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina =  gradeCurricularGrupoOptativaDisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union ");
        if (trazerDisciplinaCompostaPrincipal) {
            sqlStr.append(" select distinct disciplina.* from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina =  gradeCurricularGrupoOptativaDisciplina.codigo");
            sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union ");
        }
        // Tras as disciplina de um grupo de optativas de uma turma normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union ");
        // Tras as disciplina compostas de um grupo de optativas de uma turma
        // composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina =  gradeCurricularGrupoOptativaDisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" union ");
        if (trazerDisciplinaCompostaPrincipal) {
            sqlStr.append(" select distinct disciplina.* from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
            sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
            sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
            sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina =  gradeCurricularGrupoOptativaDisciplina.codigo");
            sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union ");
        }
        // Tras as disciplina de um grupo de optativas de uma turma composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        sqlStr.append(" order by nome ");

        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(rs, nivelMontarDados, usuario));

    }

    @Override
    public List<DisciplinaVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, Integer unidadeEnsino, int limit, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("select distinct codigo, nome, abreviatura ");
        sqlStr.append("from disciplina ");
        sqlStr.append("WHERE (sem_acentos(nome) ilike sem_acentos(?) or sem_acentos(abreviatura) ilike sem_acentos(?)) ");
        sqlStr.append("ORDER BY nome ");
        sqlStr.append("limit ").append(limit);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%" + valorConsulta + "%", "%" + valorConsulta + "%");
        List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO obj = new DisciplinaVO();
            obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
            obj.setNome(tabelaResultado.getString("nome"));
            obj.setAbreviatura(tabelaResultado.getString("abreviatura"));
            disciplinaVOs.add(obj);
        }
        return disciplinaVOs;
    }


    @Override
    public List consultarDisciplinasConteudoUsoExclusivoProfessor(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

        StringBuilder sqlStr = new StringBuilder(" ");
        sqlStr.append(" select distinct disciplina.nome as nome, disciplina.codigo as codigo from disciplina");
        sqlStr.append(" inner join conteudo on conteudo.disciplina = disciplina.codigo");
        sqlStr.append(" where conteudo.professor = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" and conteudo.usoexclusivoprofessor = 't'");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        DisciplinaVO disciplinaVO = null;
        List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>();
        while (tabelaResultado.next()) {
            disciplinaVO = new DisciplinaVO();
            disciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
            disciplinaVO.setNome(tabelaResultado.getString("nome"));
            disciplinaVOs.add(disciplinaVO);
        }
        return disciplinaVOs;
    }

    /**
     * @author Victor Hugo 23/02/2015
     */
    @Override
    public List<DisciplinaVO> consultarPorNomeAreaConhecimentoUnidadeEnsino(String valorConsulta, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT Disciplina.* FROM Disciplina inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append(" inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append(" inner join curso on gradeCurricular.curso = curso.codigo ");
        sqlStr.append(" inner join unidadeEnsinoCurso on unidadeEnsinoCurso.curso = curso.codigo ");
        sqlStr.append(" inner join unidadeEnsino on unidadeEnsinoCurso.unidadeEnsino = unidadeEnsino.codigo ");
        sqlStr.append(" ,AreaConhecimento WHERE Disciplina.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like('").append(valorConsulta.trim().toLowerCase()).append("%')");
        sqlStr.append(" and unidadeEnsino.codigo = ").append(codigoUnidadeEnsino);
        sqlStr.append(" ORDER BY AreaConhecimento.nome");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public Boolean verificarDisciplinaIncluidaGradeDisciplinaGradeCompostaGradeCurricularGrupoOptativaDisciplinaEquivalente(Integer codigoDisciplina, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();

        sqlStr.append(" (select disciplina.codigo as codigo from disciplina");
        sqlStr.append("  inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append("  where disciplina.codigo = ").append(codigoDisciplina);
        sqlStr.append("  union");
        sqlStr.append("  select disciplina.codigo as codigo from disciplina");
        sqlStr.append("  inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append("  where disciplina.codigo = ").append(codigoDisciplina);
        sqlStr.append("  union");
        sqlStr.append("  select disciplina.codigo as codigo from disciplina");
        sqlStr.append("  inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
        sqlStr.append("  where disciplina.codigo = ").append(codigoDisciplina);
        sqlStr.append("  union");
        sqlStr.append("  select disciplina.codigo as codigo from disciplina");
        sqlStr.append("  inner join disciplinaequivalente on disciplinaequivalente.disciplina = disciplina.codigo");
        sqlStr.append("  where disciplina.codigo = ").append(codigoDisciplina);
        sqlStr.append(" ) limit 1");

        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        if (rs.next()) {
            return true;
        }
        return false;
    }

    /**
     * @author Victor Hugo 26/02/2015
     */
    @Override
    public DisciplinaVO consultarPorChavePrimariaNivelEducacional(Integer codigoPrm, String nivelEducacional, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE codigo = ? and nivelEducacional = '" + nivelEducacional + "'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Disciplina).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * @author Victor Hugo 26/02/2015
     */
    @Override
    public List<DisciplinaVO> consultarPorNomeNivelEducional(String valorConsulta, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE lower (sem_acentos(nome)) like lower(sem_acentos(?)) and nivelEducacional = ? ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT + valorConsulta + PERCENT, nivelEducacional);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinasPorGradeCurricularPeriodoLetivo(Integer gradeCurricular, Integer periodoLetivo, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder(" ");
        sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, periodoletivo.descricao from disciplina ");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join periodoLetivo on periodoLetivo.codigo = gradedisciplina.periodoletivo ");
        sqlStr.append(" where gradeCurricular = ").append(gradeCurricular);
        if (!periodoLetivo.equals(0)) {
            sqlStr.append(" and periodoletivo.codigo = ").append(periodoLetivo);
        }
        sqlStr.append(" order by periodoletivo.descricao, disciplina.nome");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        DisciplinaVO disciplinaVO = null;
        List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>();
        while (tabelaResultado.next()) {
            disciplinaVO = new DisciplinaVO();
            disciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
            disciplinaVO.setNome(tabelaResultado.getString("nome"));
            disciplinaVO.setDescricaoPeriodoLetivo(tabelaResultado.getString("descricao"));
            disciplinaVOs.add(disciplinaVO);
        }
        return disciplinaVOs;
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinasPorGradeCurricularPeriodoLetivoAreaConhecimento(Integer gradeCurricular, Integer periodoLetivo, Integer areaConhecimento, String matricula, String ano, String semestre, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder(" ");
        sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, periodoletivo.descricao from disciplina ");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join periodoLetivo on periodoLetivo.codigo = gradedisciplina.periodoletivo ");
        sqlStr.append(" inner join gradeCurricular on gradeCurricular.codigo = periodoLetivo.gradeCurricular ");
        sqlStr.append(" inner join curso on curso.codigo = gradeCurricular.curso ");
        sqlStr.append(" where gradeCurricular.codigo = ").append(gradeCurricular);
        if (!periodoLetivo.equals(0)) {
            sqlStr.append(" and periodoletivo.codigo = ").append(periodoLetivo);
        }
        if (!areaConhecimento.equals(0)) {
            sqlStr.append(" and ((gradedisciplina.areaConhecimento is not null and gradedisciplina.areaConhecimento = ").append(areaConhecimento).append(") ");
            sqlStr.append(" or (gradedisciplina.areaConhecimento is null and curso.areaConhecimento = ").append(areaConhecimento).append(")) ");
        }
        sqlStr.append(" and exists (select h.codigo from historico h where h.gradedisciplina = gradedisciplina.codigo and h.matricula = ? and h.anohistorico = ? and h.semestrehistorico =? ) ");
        sqlStr.append(" order by periodoletivo.descricao, disciplina.nome");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula, ano, semestre);

        DisciplinaVO disciplinaVO = null;
        List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>();
        while (tabelaResultado.next()) {
            disciplinaVO = new DisciplinaVO();
            disciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
            disciplinaVO.setNome(tabelaResultado.getString("nome"));
            disciplinaVO.setDescricaoPeriodoLetivo(tabelaResultado.getString("descricao"));
            disciplinaVOs.add(disciplinaVO);
        }
        return disciplinaVOs;
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaGradeEOptativaPorPeriodoLetivoFazParteComposicao(Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false");
        sqlStr.append(" and periodoletivo.codigo = ").append(periodoLetivo);
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" and periodoletivo.codigo = ").append(periodoLetivo);
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where periodoletivo.codigo = ").append(periodoLetivo).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false");
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" and periodoletivo.codigo = ").append(periodoLetivo);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaGradeEOptativaPorGradeCurricularFazParteComposicao(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false");
        sqlStr.append(" and gradecurricular.codigo = ").append(gradeCurricular);
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" and gradecurricular.codigo = ").append(gradeCurricular);
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradecurricular.codigo = ").append(gradeCurricular).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false");
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" and gradecurricular.codigo = ").append(gradeCurricular);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaGradeEOptativaPorTurmaFazParteComposicao(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct disciplina.* from gradecurricular ");
        sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append("inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("where gradedisciplina.disciplinacomposta = false ");
        sqlStr.append("and turmadisciplina.turma = ").append(turma);
        sqlStr.append(" union ");
        sqlStr.append("select distinct disciplina.* from gradecurricular ");
        sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append("inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
        sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("and turmadisciplina.turma = ").append(turma);
        sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
        sqlStr.append(" and case when turmadisciplinacomposta.codigo is not null then turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo end ");
        sqlStr.append(" union ");
        sqlStr.append("select distinct disciplina.* from gradecurricular ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("where turmadisciplina.turma = ").append(turma).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false ");
        sqlStr.append(" union ");
        sqlStr.append("select distinct disciplina.* from gradecurricular ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append("and turmadisciplina.turma = ").append(turma);
        sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
        sqlStr.append(" and case when turmadisciplinacomposta.codigo is not null then turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo end ");
        //Union adicionado para filtrar as disciplinas filhas da composição.
        sqlStr.append("UNION ");
        sqlStr.append("SELECT distinct disciplina.* ");
        sqlStr.append("FROM gradecurricular ");
        sqlStr.append(" INNER JOIN periodoletivo           ON periodoletivo.gradecurricular      = gradecurricular.codigo ");
        sqlStr.append(" INNER JOIN gradedisciplina         ON gradedisciplina.periodoletivo      = periodoletivo.codigo ");
        sqlStr.append(" INNER JOIN turmadisciplina         ON turmadisciplina.gradedisciplina    = gradedisciplina.codigo ");
        sqlStr.append("left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
        sqlStr.append(" INNER JOIN gradedisciplinacomposta ON (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina end) ");
        sqlStr.append(" INNER JOIN disciplina              ON gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append("WHERE turmadisciplina.turma  = ").append(turma).append(";");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaPorMatriculaAptoVincularRequerimento(String matricula, String ano, String semestre, TipoRequerimentoVO tipoRequerimentoVO, ConfiguracaoAcademicoVO configuracaoAcademico) throws Exception {
        List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>(0);
        if (Uteis.isAtributoPreenchido(tipoRequerimentoVO) && Uteis.isAtributoPreenchido(matricula) && tipoRequerimentoVO.getIsPermiteInformarDisciplina()) {
            if (tipoRequerimentoVO.getIsTipoInclusaoDisciplina()) {
                return consultarDisciplinaAptaParaInclusaoAluno(matricula, false, false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
            }
            StringBuilder sqlStr = new StringBuilder("");
            sqlStr.append(" select distinct disciplina.nome as \"disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\",  disciplina.codigo as \"disciplina.codigo\"");
            sqlStr.append("from historico ");
            sqlStr.append("inner join matricula on matricula.matricula = historico.matricula ");
            sqlStr.append("inner join disciplina on disciplina.codigo = historico.disciplina ");
            if (Uteis.isAtributoPreenchido(tipoRequerimentoVO) && tipoRequerimentoVO.getTipo().equals("SEGUNDA_CHAMADA")) {
                sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo and matriculaperiodo.codigo in ");
                sqlStr.append(" (");
                sqlStr.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = historico.matricula order by mp.codigo desc, mp.ano desc, mp.semestre desc limit 1 ");
                sqlStr.append(" ) ");
            } else {
                sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
            }
            sqlStr.append("left join gradedisciplina on historico.gradedisciplina = gradedisciplina.codigo ");
            if (tipoRequerimentoVO.getIsAtividadeDiscursiva()) {
                sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
            }
            if (tipoRequerimentoVO.getIsTipoSegundaChamada()) {
                montarFiltroBimestreSemestre(sqlStr, tipoRequerimentoVO);
            }
            sqlStr.append("WHERE historico.matricula = '").append(matricula).append("'");
            if (!ano.trim().isEmpty()) {
                sqlStr.append(" and matriculaperiodo.ano = '").append(ano).append("'");
            }
            if (!semestre.trim().isEmpty()) {
                sqlStr.append(" and matriculaperiodo.semestre = '").append(semestre).append("'");
            }
            /**
             * Adicionada regra para resolver impactos relacionados a alunos que
             * estão Cursando por Correspondência e que disciplinas saiam duplicadas
             * no Boletim Acadêmico
             */
            sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
            sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PC') ");
            sqlStr.append(" and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) ");

            if (tipoRequerimentoVO.getIsTipoReposicao()) {
                sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
                sqlStr.append(" and (historico.situacao in ('RE', 'RF', 'RP') or matriculaPeriodo.situacaoMatriculaPeriodo in ('TR', 'AC') ) ");
                sqlStr.append(" and historico.disciplina not in ( ");
                sqlStr.append(" select his.disciplina from historico his where his.matricula = matricula.matricula  ");
                sqlStr.append(" and his.matricula = matricula.matricula  ");
                sqlStr.append(" and matricula.gradecurricularatual = his.matrizcurricular ");
                sqlStr.append(" and his.situacao in ('AA', 'CH', 'CC', 'IS', 'AP', 'CS', 'AE', 'CE') ");
                sqlStr.append(") ");
            } else if (tipoRequerimentoVO.getIsAtividadeDiscursiva()) {
                sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
                sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
                sqlStr.append(" and (historico.situacao in ('CS', 'CE', 'RE', 'RF', 'RP', 'CO') and matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI', 'PR') ) ");
            } else if (tipoRequerimentoVO.getIsCertificadoModular()) {
                sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
                sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
                sqlStr.append(" and (historico.situacao in ('AP', 'AA', 'CC', 'CH', 'IS', 'AB', 'AE')) ");
            } else if (Uteis.isAtributoPreenchido(tipoRequerimentoVO) && tipoRequerimentoVO.getIsTipoSegundaChamada()) {
                sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
                sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
                sqlStr.append(" and (historico.situacao in ('CS','RE', 'RF') and matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI', 'PR') ) ");
                sqlStr.append(" and coalesce(GradeDisciplina.disciplinaestagio, false) is false");
                sqlStr.append(" and coalesce(GradeDisciplina.disciplinatcc, false) is false");
            }
            if (Uteis.isAtributoPreenchido(configuracaoAcademico) && tipoRequerimentoVO.getIsTipoAproveitamentoDisciplina() && !configuracaoAcademico.getPermitirAproveitamentoDisciplinasOptativas()) {
                sqlStr.append(" and gradedisciplina.tipodisciplina <> 'OP' ");
            }
            if (tipoRequerimentoVO.getIsTipoAproveitamentoDisciplina() && tipoRequerimentoVO.getPermitirAproveitarDisciplinaCursando()) {
                sqlStr.append(" and historico.situacao not in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE', 'CS', 'CE') ");
            }
            sqlStr.append(" order by disciplina.nome");
            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            DisciplinaVO disciplinaVO = null;
            while (rs.next()) {
                disciplinaVO = new DisciplinaVO();
                disciplinaVO.setCodigo(rs.getInt("disciplina.codigo"));
                disciplinaVO.setNome(rs.getString("disciplina.nome"));
                disciplinaVO.setAbreviatura(rs.getString("disciplina.abreviatura"));
                disciplinaVOs.add(disciplinaVO);
            }
        }
        return disciplinaVOs;
    }


    /**
     * @param usuarioVO
     * @param semestre
     * @param ano
     * @param buscarTurmasAnteriores
     * @param unidadeEnsino
     * @return
     * @throws Exception
     * @author Rodrigo Wind - 17/10/2015
     */
    @Override
    public List<DisciplinaVO> consultarDisciplinaComAtividadeDiscursivaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(UsuarioVO usuarioVO, String semestre, String ano, Boolean buscarTurmasAnteriores, Integer turma, Integer unidadeEnsino) throws Exception {
        StringBuilder sql = new StringBuilder("select distinct disciplina.codigo, disciplina.nome from atividadeDiscursiva ");
        sql.append(" inner join turma on  atividadeDiscursiva.turma = turma.codigo");
        sql.append(" inner join disciplina on  disciplina.codigo = atividadeDiscursiva.disciplina");
        sql.append(" where atividadeDiscursiva.responsavelCadastro = ").append(usuarioVO.getCodigo()).append(" ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sql.append(" and turma.unidadeensino = ").append(unidadeEnsino);
        }
        if (Uteis.isAtributoPreenchido(turma)) {
            sql.append(" and turma.codigo = ").append(turma);
        }
        sql.append(" and turma.situacao = 'AB' ");
        if (!buscarTurmasAnteriores) {
            sql.append(" and ((turma.semestral and  atividadeDiscursiva.ano = '").append(ano).append("' and  atividadeDiscursiva.semestre = '").append(semestre).append("') or ");
            sql.append("  (turma.anual and  atividadeDiscursiva.ano = '").append(ano).append("') or (turma.semestral = false and turma.anual = false))");
        } else {
//    		sql.append(" and ((turma.semestral and  atividadeDiscursiva.ano = '").append(ano).append("' and  atividadeDiscursiva.semestre = '").append(semestre).append("') or ");
//    		sql.append("  (turma.anual and  atividadeDiscursiva.ano = '").append(ano).append("') or (turma.integral))");
        }
        sql.append(" order by nome ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>(0);
        DisciplinaVO disciplinaVO = null;
        while (rs.next()) {
            disciplinaVO = new DisciplinaVO();
            disciplinaVO.setNome(rs.getString("nome"));
            disciplinaVO.setCodigo(rs.getInt("codigo"));
            disciplinaVOs.add(disciplinaVO);
        }
        return disciplinaVOs;
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaMatrizCurricularAluno(
            String matricula, String nomeDisciplina, Integer codigoDisciplina,
            boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        // CONSULTAR GERAL RESULTADO PARA ORDENACAO
        sqlStr.append("SELECT * FROM (");

        // DISCIPLINAS DA GRADE
        sqlStr.append("select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
        if ((nomeDisciplina != null) && (!nomeDisciplina.equals(""))) {
            sqlStr.append(" and disciplina.nome ilike ('").append(nomeDisciplina).append("%')");
        }
        if (codigoDisciplina != null) {
            sqlStr.append(" and disciplina.codigo = ").append(codigoDisciplina);
        }

        // DISCIPLINAS DA COMPOSICAO
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
        sqlStr.append(" and gradedisciplina.disciplinacomposta = true ");
        if ((nomeDisciplina != null) && (!nomeDisciplina.equals(""))) {
            sqlStr.append(" and disciplina.nome ilike ('").append(nomeDisciplina).append("%')");
        }
        if (codigoDisciplina != null) {
            sqlStr.append(" and disciplina.codigo = ").append(codigoDisciplina);
        }

        // DISCIPLINAS DO GRUPO DE OPTATIVA
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
        if ((nomeDisciplina != null) && (!nomeDisciplina.equals(""))) {
            sqlStr.append(" and disciplina.nome ilike ('").append(nomeDisciplina).append("%')");
        }
        if (codigoDisciplina != null) {
            sqlStr.append(" and disciplina.codigo = ").append(codigoDisciplina);
        }

        // DISCIPLINAS DO GRUPO OPTATIVA COMPOSTAS
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
        sqlStr.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = true ");
        if ((nomeDisciplina != null) && (!nomeDisciplina.equals(""))) {
            sqlStr.append(" and disciplina.nome ilike ('").append(nomeDisciplina).append("%')");
        }
        if (codigoDisciplina != null) {
            sqlStr.append(" and disciplina.codigo = ").append(codigoDisciplina);
        }

        // FECHANDO SQL GERAL
        sqlStr.append(") AS resultadoUnion ORDER BY ");
        if (codigoDisciplina != null) {
            sqlStr.append("resultadoUnion.codigo");
        } else {
            sqlStr.append("resultadoUnion.nome");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaPorTurmaParaLancamentoNota(Integer turma, String campoConsulta, String valorConsulta, boolean trazerDisciplinaComposta, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        // Tras as disciplina compostas da grade disciplina de uma turma normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina =  gradedisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union ");
        // Tras as disciplina da grade disciplina de uma turma normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union ");
        // Tras as disciplina compostas da grade disciplina de uma turma
        // composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina =  gradedisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union");
        // Tras as disciplina da grade disciplina de uma turma composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union ");
        // Tras as disciplina compostas de um grupo de optativas de uma turma
        // normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina =  gradeCurricularGrupoOptativaDisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union ");
        // Tras as disciplina de um grupo de optativas de uma turma normal
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union ");
        // Tras as disciplina compostas de um grupo de optativas de uma turma
        // composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina =  gradeCurricularGrupoOptativaDisciplina.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sqlStr.append(" union ");
        // Tras as disciplina de um grupo de optativas de uma turma composta
        sqlStr.append(" select distinct disciplina.* from turma ");
        sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
        sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
        sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
        sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
        sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada");
        sqlStr.append(" and turma.codigo = ").append(turma);
        if (campoConsulta.equals("nome")) {
            sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("%").append("'))");
        } else if (campoConsulta.equals("codigo")) {
            sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        if (trazerDisciplinaComposta) {
            sqlStr.append(" union ");
            sqlStr.append(" select distinct disciplina.* from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
            sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
            sqlStr.append(" and turma.codigo = ").append(turma);
            if (campoConsulta.equals("nome")) {
                sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("'))");
            } else if (campoConsulta.equals("codigo")) {
                sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
            }
            sqlStr.append(" union ");
            // Tras as disciplina compostas de um grupo de optativas de uma turma
            // normal
            sqlStr.append(" select distinct disciplina.* from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
            sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
            sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
            sqlStr.append(" and turma.codigo = ").append(turma);
            if (campoConsulta.equals("nome")) {
                sqlStr.append(" and sem_acentos(disciplina.nome) ilike (sem_acentos('").append(valorConsulta).append("'))");
            } else if (campoConsulta.equals("codigo")) {
                sqlStr.append(" and disciplina.codigo = ").append(valorConsulta);
            }
        }
        sqlStr.append(" order by nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarDisciplinaAptaParaInclusaoAluno(String matricula, boolean trazerDisciplinaFilhaComposicao, boolean naoTrazerDisciplinaCursando, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        // CONSULTAR GERAL RESULTADO PARA ORDENACAO
        sqlStr.append("SELECT * FROM (");

        // DISCIPLINAS DA GRADE
        sqlStr.append("select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
        sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
        if (trazerDisciplinaFilhaComposicao) {
            // DISCIPLINAS DA COMPOSICAO
            sqlStr.append(" union");
            sqlStr.append(" select distinct disciplina.* from gradecurricular");
            sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
            sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina");
            sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
            sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
            sqlStr.append(" and gradedisciplina.disciplinacomposta = true ");
        }
        // DISCIPLINAS DO GRUPO DE OPTATIVA
        sqlStr.append(" union");
        sqlStr.append(" select distinct disciplina.* from gradecurricular");
        sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
        sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
        if (trazerDisciplinaFilhaComposicao) {
            // DISCIPLINAS DO GRUPO OPTATIVA COMPOSTAS
            sqlStr.append(" union");
            sqlStr.append(" select distinct disciplina.* from gradecurricular");
            sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo");
            sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
            sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
            sqlStr.append(" where matricula.matricula = '").append(matricula).append("'");
            sqlStr.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = true ");
        }
        // FECHANDO SQL GERAL
        sqlStr.append(") AS resultadoUnion ");
        sqlStr.append(" WHERE resultadoUnion.codigo not in (select disciplina from historico inner join matricula m on m.matricula = historico.matricula ");
        sqlStr.append(" where m.gradecurricularatual = historico.matrizcurricular and historico.situacao in ('AP', 'CC', 'CH', 'IS', 'AA', 'AE' ");
        if (naoTrazerDisciplinaCursando) {
            sqlStr.append(", 'CS', 'CO', 'CE'");
        }
        sqlStr.append(") and m.matricula =  '").append(matricula).append("' ");
        sqlStr.append(" ) ");
        sqlStr.append(" ORDER BY resultadoUnion.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarPorCodigoCurso(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT Disciplina.* FROM Disciplina ");
        sqlStr.append("INNER JOIN GradeDisciplina on Disciplina.codigo = GradeDisciplina.disciplina ");
        sqlStr.append("INNER JOIN PeriodoLetivo on GradeDisciplina.periodoLetivo = PeriodoLetivo.codigo ");
        sqlStr.append("INNER JOIN GradeCurricular on PeriodoLetivo.gradeCurricular = GradeCurricular.codigo ");
        sqlStr.append("INNER JOIN Curso on GradeCurricular.curso = Curso.codigo ");
        sqlStr.append("WHERE Curso.codigo = ").append(curso);
        sqlStr.append(" GROUP BY Disciplina.codigo, Disciplina.nome");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuario);
    }

    @Override
    public List<DisciplinaVO> consultarApenasDisciplinaEstagioPorMatriculaAnoSemestreNivelComboBox(String matricula, String ano, String semestre, Integer estagio, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        //TRAZ DISCIPLINAS QUE NÃO SÃO COMPOSTAS
        sb.append("select distinct disciplina.codigo, disciplina.nome from historico ");
        sb.append(" inner join matricula on historico.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo ");
        sb.append(" inner join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina  ");
        sb.append(" inner join disciplina on historico.disciplina = disciplina.codigo  ");
        sb.append(" where matricula.matricula = '").append(matricula).append("' ");
        if (!ano.equals("")) {
            sb.append(" and historico.anohistorico = '").append(ano).append("' ");
        }
        if (!semestre.equals("")) {
            sb.append(" and historico.semestrehistorico = '").append(semestre).append("' ");
        }
        sb.append(" and gradedisciplina.disciplinaestagio ");
        sb.append(" and gradedisciplina.disciplinacomposta = false ");
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));

        sb.append(" union ");

        //TRAZ AS DISCIPLINAS FILHAS DA COMPOSIÇÃO
        sb.append(" select distinct disciplina.codigo, disciplina.nome ");
        sb.append(" from historico ");
        sb.append(" inner join matricula on historico.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo ");
        sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = historico.gradedisciplinacomposta ");
        sb.append(" inner join gradedisciplina on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
        sb.append(" inner join disciplina on historico.disciplina = disciplina.codigo  ");
        sb.append(" where matricula.matricula = '").append(matricula).append("' ");
        if (!ano.equals("")) {
            sb.append(" and historico.anohistorico = '").append(ano).append("' ");
        }
        if (!semestre.equals("")) {
            sb.append(" and historico.semestrehistorico = '").append(semestre).append("' ");
        }
        sb.append(" and gradedisciplina.disciplinaestagio ");
        sb.append(" and gradedisciplina.disciplinacomposta = true ");
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));

        sb.append(" union ");

        //TRAZ AS DISCIPLINAS DE ESTÁGIO DO GRUPO DE OPTATIVA QUE NÃO SÃO COMPOSTAS
        sb.append(" select distinct disciplina.codigo, disciplina.nome ");
        sb.append(" from historico ");
        sb.append(" inner join matricula on historico.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo ");
        sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
        sb.append(" inner join disciplina on historico.disciplina = disciplina.codigo ");
        sb.append(" where matricula.matricula = '").append(matricula).append("' ");
        if (!ano.equals("")) {
            sb.append(" and historico.anohistorico = '").append(ano).append("' ");
        }
        if (!semestre.equals("")) {
            sb.append(" and historico.semestrehistorico = '").append(semestre).append("' ");
        }
        sb.append(" and gradecurriculargrupooptativadisciplina.disciplinaestagio ");
        sb.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false ");
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));

        sb.append(" union ");

        //TRAZ AS FILHAS DA DISCIPLINA COMPOSTA DE ESTÁGIO DO GRUPO DE OPTATIVA
        sb.append(" select distinct disciplina.codigo, disciplina.nome ");
        sb.append(" from historico ");
        sb.append(" inner join matricula on historico.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo ");
        sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = historico.gradedisciplinacomposta ");
        sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sb.append(" inner join disciplina on historico.disciplina = disciplina.codigo ");
        sb.append(" where matricula.matricula = '").append(matricula).append("' ");
        if (!ano.equals("")) {
            sb.append(" and historico.anohistorico = '").append(ano).append("' ");
        }
        if (!semestre.equals("")) {
            sb.append(" and historico.semestrehistorico = '").append(semestre).append("' ");
        }
        sb.append(" and gradecurriculargrupooptativadisciplina.disciplinaestagio ");
        sb.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = true ");
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));

        sb.append(" union ");

        //TRAZ TODAS AS DISCIPLINAS DO ESTÁGIO
        //ESSA CONSULTA SERVE PARA RESOLVER O PROBLEMA DOS ALUNOS QUE POSSUEM ESTÁGIO
        //PORÉM NÃO ESTÁ CURSANDO A DISCIPLINA
        sb.append(" select disciplina.codigo, disciplina.nome || ' - (DISCIPLINA NÃO CURSADA PELO ALUNO)' from estagio ");
        sb.append(" inner join disciplina on disciplina.codigo = estagio.disciplina ");
        sb.append(" where estagio.codigo = ").append(estagio);
        sb.append(" and estagio.disciplina not in(");
        sb.append(" select historico.disciplina from historico  ");
        sb.append(" inner join matricula on matricula.matricula = historico.matricula and matricula.gradecurricularatual = historico.matrizcurricular ");
        sb.append(" where historico.matricula = estagio.matricula ");
        sb.append(" and historico.anohistorico = estagio.ano ");
        sb.append(" and historico.semestrehistorico = estagio.semestre) ");

        sb.append(" order by nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List<DisciplinaVO> listaDisciplinaVOs = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO obj = new DisciplinaVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            listaDisciplinaVOs.add(obj);
        }
        return listaDisciplinaVOs;
    }


    public static List<DisciplinaVO> montarDadosConsultaCHPrevista(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
            while (tabelaResultado.next()) {
                vetResultado.add(montarDadosComCHPrevista(tabelaResultado, nivelMontarDados, usuario));
            }
            return vetResultado;
        } catch (Exception e) {
            throw e;
        }
    }

    public static DisciplinaVO montarDadosComCHPrevista(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            DisciplinaVO obj = montarDados(dadosSQL, nivelMontarDados, usuario);
            obj.setCargaHorariaPrevista(dadosSQL.getInt("cargaHorariaPrevista"));
            return obj;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<DisciplinaVO> consultarPorNome(String valorConsulta, Integer codigoCurso, Integer codigoGradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        if ((codigoGradeCurricular != null) && (codigoGradeCurricular.intValue() != 0)) {
            // se esta filtrando por grade, automaticamnte ja esta filtrando por curso
            // monta a carga horaria prevista para ajudar o usuario, quando o mesmo informa a grade.
            sql.append(" SELECT * FROM (");
            sql.append("SELECT distinct disciplina.*, gradedisciplina.cargaHoraria as cargaHorariaPrevista FROM GradeDisciplina ");
            sql.append(" inner join Disciplina on Disciplina.codigo = GradeDisciplina.Disciplina ");
            sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
            sql.append(" where periodoletivo.gradecurricular = ").append(codigoGradeCurricular);
            // trazendo disciplinas grupo optativas
            sql.append(" union ");
            sql.append(" select distinct disciplina.*, gradecurriculargrupooptativadisciplina.cargaHoraria as cargaHorariaPrevista from gradecurriculargrupooptativa ");
            sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
            sql.append(" inner join Disciplina on Disciplina.codigo = gradecurriculargrupooptativadisciplina.Disciplina ");
            sql.append(" where gradecurriculargrupooptativa.gradecurricular = ").append(codigoGradeCurricular);
            // trazendo disciplinas filhas de uma composição
            sql.append(" union ");
            sql.append(" select distinct disciplina.*, gradedisciplinacomposta.cargaHoraria as cargaHorariaPrevista from gradedisciplinacomposta ");
            sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplinacomposta.periodoletivo ");
            sql.append(" inner join Disciplina on Disciplina.codigo = gradedisciplinacomposta.Disciplina ");
            sql.append(" where periodoletivo.gradecurricular = ").append(codigoGradeCurricular);
            // trazendo disciplinas filhas de uma composição de um grupo optativa
            sql.append(" union ");
            sql.append(" select distinct Disciplina.*, gradedisciplinacomposta.cargahoraria as cargaHorariaPrevista from gradedisciplinacomposta ");
            sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
            sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
            sql.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
            sql.append(" where gradecurriculargrupooptativa.gradecurricular = ").append(codigoGradeCurricular);
            // fechando SQL
            sql.append(" ) as disciplina where ");
        } else {
            // se nao esta filtrando por grade entamos temos q verificar o curso
            if ((codigoCurso != null) && (codigoCurso.intValue() != 0)) {
                sql.append("SELECT *, 0 as cargaHorariaPrevista FROM Disciplina WHERE ");
                sql.append(" codigo in ( ");
                sql.append(" select distinct disciplina.codigo from gradedisciplina ");
                sql.append(" inner join Disciplina on Disciplina.codigo = GradeDisciplina.Disciplina ");
                sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // trazendo disciplinas grupo optativas
                sql.append(" union ");
                sql.append(" select distinct disciplina.codigo from gradecurriculargrupooptativa ");
                sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
                sql.append(" inner join Disciplina on Disciplina.codigo = gradecurriculargrupooptativadisciplina.Disciplina ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // trazendo disciplinas filhas de uma composição
                sql.append(" union ");
                sql.append(" select distinct disciplina.codigo from gradedisciplinacomposta ");
                sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplinacomposta.periodoletivo ");
                sql.append(" inner join Disciplina on Disciplina.codigo = gradedisciplinacomposta.Disciplina ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // trazendo disciplinas filhas de uma composição de um grupo optativa
                sql.append(" union ");
                sql.append(" select distinct Disciplina.codigo from gradedisciplinacomposta ");
                sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
                sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
                sql.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // fechando SQL
                sql.append(" ) and ");
            } else {
                sql.append("SELECT *, 0 as cargaHorariaPrevista FROM Disciplina WHERE ");
            }
        }
        sql.append(" lower (sem_acentos(nome)) like(sem_acentos('%").append(valorConsulta.toLowerCase()).append("%')) ORDER BY nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsultaCHPrevista(tabelaResultado, nivelMontarDados, usuario));
    }


    @Override
    public List<DisciplinaVO> consultarDisciplinasEstagio(String consultarPor, String valorConsulta) {
        StringBuilder sb = new StringBuilder();
        //TRAZ DISCIPLINAS QUE NÃO SÃO COMPOSTAS
        sb.append("select distinct disciplina.codigo, disciplina.nome from disciplina ");
        sb.append(" inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ");
        sb.append(" where gradedisciplina.disciplinaestagio ");
        sb.append(" and gradedisciplina.disciplinacomposta = false ");
        if (consultarPor.equals("nome")) {
            sb.append(" and sem_acentos(disciplina.nome) ilike 	sem_acentos('").append(valorConsulta).append("%') ");
        } else if (consultarPor.equals("codigo")) {
            sb.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sb.append(" union ");
        //TRAZ AS DISCIPLINAS FILHAS DA COMPOSIÇÃO
        sb.append(" select distinct disciplina.codigo, disciplina.nome ");
        sb.append(" from disciplina ");
        sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sb.append(" inner join gradedisciplina on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
        sb.append(" where gradedisciplina.disciplinaestagio ");
        sb.append(" and gradedisciplina.disciplinacomposta = true ");
        if (consultarPor.equals("nome")) {
            sb.append(" and sem_acentos(disciplina.nome) ilike 	sem_acentos('").append(valorConsulta).append("%') ");
        } else if (consultarPor.equals("codigo")) {
            sb.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sb.append(" union ");

        //TRAZ AS DISCIPLINAS DE ESTÁGIO DO GRUPO DE OPTATIVA QUE NÃO SÃO COMPOSTAS
        sb.append(" select distinct disciplina.codigo, disciplina.nome ");
        sb.append(" from disciplina ");
        sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sb.append(" where gradecurriculargrupooptativadisciplina.disciplinaestagio ");
        sb.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false ");
        if (consultarPor.equals("nome")) {
            sb.append(" and sem_acentos(disciplina.nome) ilike 	sem_acentos('").append(valorConsulta).append("%') ");
        } else if (consultarPor.equals("codigo")) {
            sb.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sb.append(" union ");

        //TRAZ AS FILHAS DA DISCIPLINA COMPOSTA DE ESTÁGIO DO GRUPO DE OPTATIVA
        sb.append(" select distinct disciplina.codigo, disciplina.nome ");
        sb.append(" from disciplina ");
        sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = disciplina.codigo ");
        sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sb.append(" where gradecurriculargrupooptativadisciplina.disciplinaestagio ");
        sb.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = true ");
        if (consultarPor.equals("nome")) {
            sb.append(" and sem_acentos(disciplina.nome) ilike 	sem_acentos('").append(valorConsulta).append("%') ");
        } else if (consultarPor.equals("codigo")) {
            sb.append(" and disciplina.codigo = ").append(valorConsulta);
        }
        sb.append(" order by nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List<DisciplinaVO> listaDisciplinaVOs = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO obj = new DisciplinaVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            listaDisciplinaVOs.add(obj);
        }
        return listaDisciplinaVOs;
    }


    @Override
    public Boolean realizarVerificacaoDisciplinaECompostaTurma(Integer turma, Integer disciplina) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" select distinct disciplina.codigo ");
        sqlStr.append(" from turma ");
        sqlStr.append(" INNER JOIN turmadisciplina on ((turma.turmaagrupada = false and turma.codigo = turmadisciplina.turma) or  ");
        sqlStr.append(" (turma.turmaagrupada  and turmadisciplina.codigo in (select td.codigo from turmadisciplina td ");
        sqlStr.append(" inner join turmaagrupada on turmaagrupada.turma = td.turma ");
        sqlStr.append(" and turmaagrupada.turmaorigem = turma.codigo))) ");
        sqlStr.append(" left join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
        sqlStr.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
        sqlStr.append(" inner join gradedisciplinacomposta on ((gradedisciplina.codigo is not null and gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina)  ");
        sqlStr.append(" or (gradecurriculargrupooptativadisciplina.codigo is not null and gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina)) ");
        sqlStr.append(" inner join disciplina on ((gradedisciplina.codigo is not null and disciplina.codigo = gradedisciplina.disciplina)  ");
        sqlStr.append(" or (gradecurriculargrupooptativadisciplina.codigo is not null and disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina)) ");
        sqlStr.append(" where turma.codigo =  ").append(turma);
        sqlStr.append(" and disciplina.codigo =  ").append(disciplina);
        return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
    }

    public StringBuilder getSqlPadraoValidacaoExistenciaPeriodoLetivoAtivoUnidadeEnsinoCurso() {
        StringBuilder sql = new StringBuilder("");
        sql.append(" (exists (");
        sql.append("  select periodoletivoativounidadeensinocurso.codigo   ");
        sql.append("  from periodoletivoativounidadeensinocurso  ");
        sql.append("  inner join unidadeensinocurso on unidadeensinocurso.codigo = periodoletivoativounidadeensinocurso.unidadeensinocurso  ");
        sql.append("  where periodoletivoativounidadeensinocurso.situacao = 'AT'  ");
        sql.append("  and unidadeensinocurso.unidadeensino = turma.unidadeensino and unidadeensinocurso.curso = curso.codigo  ");
        sql.append("  and ((turma.semestral and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = HorarioTurma.anovigente    ");
        sql.append("  and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = HorarioTurma.semestrevigente)    ");
        sql.append("  or (turma.anual and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = HorarioTurma.anovigente)  ");
        sql.append("  or (turma.anual = false and turma.semestral = false)) limit 1  ");
        sql.append("  )  or not exists (");
        sql.append("");
        sql.append(" select periodoletivoativounidadeensinocurso.codigo   ");
        sql.append("  from periodoletivoativounidadeensinocurso  ");
        sql.append("  inner join unidadeensinocurso on unidadeensinocurso.codigo = periodoletivoativounidadeensinocurso.unidadeensinocurso  ");
        sql.append("  where unidadeensinocurso.unidadeensino = turma.unidadeensino and unidadeensinocurso.curso = curso.codigo  ");
        sql.append("  and ((turma.semestral and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = HorarioTurma.anovigente    ");
        sql.append("  and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = HorarioTurma.semestrevigente)    ");
        sql.append("  or (turma.anual and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = HorarioTurma.anovigente)  ");
        sql.append("  or (turma.anual = false and turma.semestral = false)) limit 1 ");
        sql.append("  ) )");
        return sql;

    }

    public List<DisciplinaVO> consultarPorArtefatoEntregaAluno(Integer artefato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct (disciplina.*) FROM disciplina INNER JOIN nivelcontroleartefato on disciplina.codigo=nivelcontroleartefato.disciplina WHERE nivelcontroleartefato.artefato = " + artefato;

        sqlStr += " ORDER BY disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarDisciplinasPreRequisitoNaoCumpridasConsiderandoMapaEquivalenciaDisciplina(DisciplinaVO disciplinaVO, String matricula, Integer gradeCurricular, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
        List<DisciplinaVO> listaDisciplinasPreRequisitosVOs = new ArrayList<>();
        if (Uteis.isAtributoPreenchido(mapaEquivalenciaDisciplinaVO)) {
            for (Integer disciplina : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaCodigoDisciplinaMatrizCurricularVOs()) {
                listaDisciplinasPreRequisitosVOs.addAll(consultarDisciplinasPreRequisitoNaoCumpridasDaGrade(matricula, disciplina, gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
            }
        } else {
            listaDisciplinasPreRequisitosVOs.addAll(consultarDisciplinasPreRequisitoNaoCumpridasDaGrade(matricula, disciplinaVO.getCodigo(), gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
        }
        return listaDisciplinasPreRequisitosVOs;
    }

    public Map<String, Integer> consultarVinculosDisciplinaAlteracaoNome(int codigoDisciplina, String nomeDisciplina, boolean validarAlteracaoNome) throws Exception {
        StringBuilder str = new StringBuilder().append(" SELECT VINCULO.* FROM disciplina d INNER JOIN LATERAL ( ")
                .append(" SELECT COUNT(codigo) AS quantidade, 'Histórico' AS chave ")
                .append(" FROM historico WHERE disciplina = d.codigo")
                .append(" UNION SELECT COUNT(DISTINCT gc.codigo) AS quantidade, 'Grade Curricular' AS chave ")
                .append(" FROM gradecurricular gc ")
                .append(" INNER JOIN periodoletivo pl ON pl.gradecurricular = gc.codigo ")
                .append(" LEFT JOIN gradedisciplina gd ON gd.periodoletivo = pl.codigo ")
                .append(" LEFT JOIN gradedisciplinacomposta gdc ON gdc.periodoletivo = pl.codigo ")
                .append(" LEFT JOIN gradecurriculargrupooptativa gcgo ON gcgo.gradecurricular = gc.codigo ")
                .append(" LEFT JOIN gradecurriculargrupooptativadisciplina gcgod ON gcgod.gradecurriculargrupooptativa = gcgo.codigo ")
                .append(" WHERE ")
                .append(" (gd.disciplina = d.codigo OR gdc.disciplina = d.codigo OR gcgod.disciplina = d.codigo ) ")
                .append(" UNION SELECT COUNT(DISTINCT t.codigo) AS quantidade, 'Turma' AS chave ")
                .append(" FROM turmadisciplina td ")
                .append(" INNER JOIN turma t ON td.turma = t.codigo ")
                .append(" WHERE td.disciplina = d.codigo ")
                .append(" ) AS VINCULO ON 1 = 1 ")
                .append(" WHERE d.codigo = ")
                .append(codigoDisciplina)
                .append(" AND VINCULO.quantidade > 0 ");
        SqlRowSet queryForRowSet = null;
        if (validarAlteracaoNome) {
            str.append(" AND d.nome <> ?");
            queryForRowSet = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), nomeDisciplina);
        } else {
            queryForRowSet = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        }
        Map<String, Integer> mapVinculoQuantidade = new HashMap<>();
        while (queryForRowSet.next()) {
            mapVinculoQuantidade.put(queryForRowSet.getString("chave"), queryForRowSet.getInt("quantidade"));
        }
        return mapVinculoQuantidade;
    }

    @Override
    public DisciplinaVO consultarPorChavePrimariaUnica(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Disciplina).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<DisciplinaVO> consultarPorUnidadeEnsinoCursoTurma(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turma, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
        StringBuilder sqlStr = new StringBuilder(" select distinct disciplina.* from disciplina ");
        sqlStr.append(" left join disciplinaEquivalente de on de.equivalente = disciplina.codigo ");
        sqlStr.append(" inner join gradeDisciplina on (gradeDisciplina.disciplina = disciplina.codigo or (case when (de.equivalente is not null) then gradedisciplina.disciplina = de.disciplina end))");
        sqlStr.append(" inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append(" inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append(" inner join curso on gradeCurricular.curso = curso.codigo ");
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" inner join turma t on t.curso = curso.codigo ");
            sqlStr.append(" inner join turmadisciplina td on td.turma = t.codigo and td.disciplina = disciplina.codigo ");
        }
        sqlStr.append(" WHERE 1=1 ");

        if (campoConsulta.equals("nome")) {
            valorConsulta += "%";
            sqlStr.append(" AND (sem_acentos(disciplina.nome)) ilike (sem_acentos(?)) ");
        } else if (campoConsulta.equals("abreviatura")) {
            valorConsulta += "%";
            sqlStr.append(" AND (sem_acentos(disciplina.abreviatura)) ilike (sem_acentos(?))");

        } else if (campoConsulta.equals("codigo")) {
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            sqlStr.append(" AND disciplina.codigo = ? ");
        }

        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" and uec.unidadeensino in (");
            int x = 0;
            for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
                    if (x > 0) {
                        sqlStr.append(", ");
                    }
                    sqlStr.append(unidadeEnsinoVO.getCodigo());
                    x++;
                }

            }
            sqlStr.append(" ) ");
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append(" inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join gradeDisciplina on gradeDisciplina.codigo = gradeDisciplinacomposta.gradeDisciplina ");
        sqlStr.append(" inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sqlStr.append(" inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append(" inner join curso on gradeCurricular.curso = curso.codigo ");
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" inner join turma t on t.curso = curso.codigo ");
            sqlStr.append(" inner join turmadisciplina td on td.gradeDisciplina = gradeDisciplina.codigo ");
        }
        sqlStr.append(" WHERE 1=1 ");
        if (campoConsulta.equals("nome")) {

            sqlStr.append(" AND (sem_acentos(disciplina.nome)) ilike (sem_acentos(?)) ");
        } else if (campoConsulta.equals("abreviatura")) {

            sqlStr.append(" AND (sem_acentos(disciplina.abreviatura)) ilike (sem_acentos(?))");

        } else if (campoConsulta.equals("codigo")) {
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            sqlStr.append(" AND disciplina.codigo = ? ");
        }
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" and uec.unidadeensino in (");
            int x = 0;
            for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
                    if (x > 0) {
                        sqlStr.append(", ");
                    }
                    sqlStr.append(unidadeEnsinoVO.getCodigo());
                    x++;
                }

            }
            sqlStr.append(" ) ");
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append(" inner join curso on gradeCurricular.curso = curso.codigo ");
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" inner join turma t on t.curso = curso.codigo ");
            sqlStr.append(" inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append(" WHERE 1=1 ");
        if (campoConsulta.equals("nome")) {

            sqlStr.append(" AND (sem_acentos(disciplina.nome)) ilike (sem_acentos(?)) ");
        } else if (campoConsulta.equals("abreviatura")) {

            sqlStr.append(" AND (sem_acentos(disciplina.abreviatura)) ilike (sem_acentos(?))");

        } else if (campoConsulta.equals("codigo")) {
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            sqlStr.append(" AND disciplina.codigo = ? ");
        }
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" and uec.unidadeensino in (");
            int x = 0;
            for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
                    if (x > 0) {
                        sqlStr.append(", ");
                    }
                    sqlStr.append(unidadeEnsinoVO.getCodigo());
                    x++;
                }

            }
            sqlStr.append(" ) ");
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }
        sqlStr.append(" union select distinct disciplina.* from disciplina ");
        sqlStr.append(" inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
        sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradeDisciplinacomposta.gradecurriculargrupooptativadisciplina ");
        sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append(" inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
        sqlStr.append(" inner join curso on gradeCurricular.curso = curso.codigo ");
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" inner join unidadeensinocurso uec on uec.curso = curso.codigo ");
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" inner join turma t on t.curso = curso.codigo ");
            sqlStr.append(" inner join turmadisciplina td on td.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
        }
        sqlStr.append(" WHERE 1=1 ");
        if (campoConsulta.equals("nome")) {

            sqlStr.append(" AND (sem_acentos(disciplina.nome)) ilike (sem_acentos(?)) ");
        } else if (campoConsulta.equals("abreviatura")) {

            sqlStr.append(" AND (sem_acentos(disciplina.abreviatura)) ilike (sem_acentos(?))");

        } else if (campoConsulta.equals("codigo")) {
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            sqlStr.append(" AND disciplina.codigo = ? ");
        }
        if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
            sqlStr.append(" and uec.unidadeensino in (");
            int x = 0;
            for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
                    if (x > 0) {
                        sqlStr.append(", ");
                    }
                    sqlStr.append(unidadeEnsinoVO.getCodigo());
                    x++;
                }

            }
            sqlStr.append(" ) ");
        }
        if (curso != null && !curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (turma != null && !turma.equals(0)) {
            sqlStr.append(" and t.codigo = ").append(turma);
        }

        if ((unidadeEnsinoVOs.isEmpty() || unidadeEnsinoVOs.stream().noneMatch(u -> u.getFiltrarUnidadeEnsino()))
                && (curso == null || curso.equals(0))
                && (turma == null || turma.equals(0))) {
            sqlStr.append(" union select distinct disciplina.* from disciplina ");
            sqlStr.append(" WHERE 1=1 ");
            if (campoConsulta.equals("nome")) {
                sqlStr.append(" AND (sem_acentos(disciplina.nome)) ilike (sem_acentos('").append(valorConsulta).append("')) ");
            } else if (campoConsulta.equals("abreviatura")) {
                sqlStr.append(" AND (sem_acentos(disciplina.abreviatura)) ilike (sem_acentos('").append(valorConsulta).append("'))");
            } else if (campoConsulta.equals("codigo")) {
                if (valorConsulta.equals("")) {
                    valorConsulta = "0";
                }
                sqlStr.append(" AND disciplina.codigo =  ").append(Integer.valueOf(valorConsulta));
            }
        }
        sqlStr.append(" order by nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), campoConsulta.equals("codigo") ? Integer.valueOf(valorConsulta) : valorConsulta, campoConsulta.equals("codigo") ? Integer.valueOf(valorConsulta) : valorConsulta, campoConsulta.equals("codigo") ? Integer.valueOf(valorConsulta) : valorConsulta, campoConsulta.equals("codigo") ? Integer.valueOf(valorConsulta) : valorConsulta);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
    }

    @Override
    public List<DisciplinaVO> consultarPorAbreviatura(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE lower (sem_acentos(abreviatura)) like lower(sem_acentos(?)) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT + valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<Integer> consultarBimestresPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("  select distinct gradedisciplina.bimestre from gradedisciplina ");
        if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCursoVO())) {
            sql.append("  inner join periodoletivo on periodoletivo.codigo  = gradedisciplina.periodoletivo");
            sql.append("  inner join gradecurricular on periodoletivo.gradecurricular  = gradecurricular.codigo ");
        }
        if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO()) || Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO())) {
            sql.append("  inner join turmadisciplina on turmadisciplina.gradedisciplina  = gradedisciplina.codigo ");
            sql.append("  inner join turma on turmadisciplina.turma  = turma.codigo ");
        }
        sql.append("  where gradedisciplina.disciplina = ").append(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());
        if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCursoVO())) {
            sql.append(" and gradecurricular.curso = ").append(programacaoTutoriaOnlineVO.getCursoVO().getCodigo());
        }
        if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO())) {
            sql.append(" and turmadisciplina.turma = ").append(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());
        }
        if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO())) {
            sql.append(" and turma.unidadeEnsino = ").append(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo());
        }
        sql.append(" ");
        List<Integer> bimestres = new ArrayList<Integer>(0);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (rs.next()) {
            bimestres.add(rs.getInt("bimestre"));
        }
        return bimestres;
    }

    @Override
    public List<DisciplinaVO> consultarPorAbreviatura_Matricula_DisciplinaEquivalenteEDisciplinaComposta(String abreviatura, String matricula, Integer gradeCurricular, Integer periodoTurma, boolean trazerDisciplinaCompostaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder("select distinct disciplina.*, historico.anoHistorico, historico.semestreHistorico from historico ");
        sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
        sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
        sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
        sql.append(" where matricula.matricula = '").append(matricula).append("' ");
        sql.append(" and historico.situacao not in ('AA', 'CH', 'CC', 'IS') ");
        /**
         * Adicionada regra para resolver impactos relacionados a alunos que
         * estão Cursando por Correspondência e que disciplinas saiam duplicadas
         * no Boletim Acadêmico
         */
        if (!Uteis.isAtributoPreenchido(gradeCurricular)) {
            sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
        } else {
            sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularEspecifico(" and ", gradeCurricular));
        }
        sql.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        if (!trazerDisciplinaCompostaPrincipal) {
            sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
        }
        sql.append(" and lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos(?)) ORDER BY disciplina.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), abreviatura + "%");
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        while (tabelaResultado.next()) {
            DisciplinaVO disciplinaVO = montarDados(tabelaResultado, nivelMontarDados, usuario);
            disciplinaVO.setAno(tabelaResultado.getString("anoHistorico"));
            disciplinaVO.setSemestre(tabelaResultado.getString("semestreHistorico"));
            vetResultado.add(disciplinaVO);
        }
        return vetResultado;
    }

    @Override
    public List<DisciplinaVO> consultarPorAbreviatura_CursoDisciplinaComposta(String abreviatura, Integer curso, Integer periodoTurma, boolean trazerDisciplinaComposta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        if ((curso != null && curso > 0) || (periodoTurma != null && periodoTurma > 0)) {
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
            sb.append(" where gradedisciplina.disciplinacomposta = false ");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" and lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join disciplinaequivalente on gradedisciplina.disciplina = disciplinaequivalente.disciplina");
            sb.append(" inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente ");
            sb.append(" where lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%')) ");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
            sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradedisciplina = gradedisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
            sb.append(" where lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }

            sb.append(" union");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo and gradecurriculargrupooptativa.codigo = periodoletivo.gradecurriculargrupooptativa ");
            }
            sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
            sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta =  false and lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" union ");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo and gradecurriculargrupooptativa.codigo = periodoletivo.gradecurriculargrupooptativa ");
            }
            sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
            sb.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina = gradecurriculargrupooptativadisciplina.codigo");
            sb.append(" inner join disciplina on disciplina.codigo = gradeDisciplinaComposta.disciplina");
            sb.append(" where lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }
            sb.append(" union ");
            sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
            sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
            sb.append(" inner join mapaequivalenciamatrizcurricular on mapaequivalenciamatrizcurricular.gradecurricular = gradecurricular.codigo");
            sb.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
            sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
                sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
                sb.append(" inner join mapaequivalenciadisciplinamatrizcurricular on mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo and mapaequivalenciadisciplinamatrizcurricular.disciplina = gradedisciplina.disciplina ");
            }
            sb.append(" inner join disciplina on disciplina.codigo = mapaequivalenciadisciplinacursada.disciplina");
            sb.append(" where lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
            if ((curso != null && curso > 0)) {
                sb.append(" and curso.codigo = ").append(curso);
            }
            if ((periodoTurma != null && periodoTurma > 0)) {
                sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
            }

            if (trazerDisciplinaComposta) {
                sb.append(" union ");
                sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
                sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
                sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
                sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
                sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
                sb.append(" where gradedisciplina.disciplinacomposta  and lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
                if ((curso != null && curso > 0)) {
                    sb.append(" and curso.codigo = ").append(curso);
                }
                if ((periodoTurma != null && periodoTurma > 0)) {
                    sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
                }
                sb.append(" union ");
                sb.append(" select distinct disciplina.*, gradecurricular.nome as gradenome from curso");
                sb.append(" inner join gradecurricular on curso.codigo = gradeCurricular.curso");
                sb.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo");
                sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
                sb.append(" inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina");
                sb.append(" where gradecurriculargrupooptativadisciplina.disciplinacomposta and lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%'))");
                if ((curso != null && curso > 0)) {
                    sb.append(" and curso.codigo = ").append(curso);
                }
                if ((periodoTurma != null && periodoTurma > 0)) {
                    sb.append(" and periodoletivo.codigo = ").append(periodoTurma);
                }
            }
        } else {
            sb.append("select *, '' as gradenome from disciplina where lower(sem_acentos(disciplina.abreviatura)) like lower(sem_acentos('").append(abreviatura).append("%')) ");
        }
        sb.append(" order by nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsultaComNomeGrade(tabelaResultado, nivelMontarDados, usuario));
    }


    @Override
    public List<DisciplinaVO> consultaRapidaDisciplinaPorGradeCurricular(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT distinct(Disciplina.*) ");
        sqlStr.append(" from periodoletivo ");
        sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
        sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
        sqlStr.append(" where periodoletivo.gradecurricular = ? ");
        sqlStr.append(" ORDER BY disciplina.nome");
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
        List<DisciplinaVO> vetResultado = new ArrayList<DisciplinaVO>(0);
        while (dadosSQL.next()) {
            DisciplinaVO obj = new DisciplinaVO();
            obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
            obj.setNome(dadosSQL.getString("nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    @Override
    public List<DisciplinaVO> consultarPorAbreviaturaDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(String abreviatura, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplina gd on d.codigo = gd.disciplina ");
        sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
        //sqlStr.append("left join turma t on t.curso = c.codigo and t.periodoletivo = pl.codigo ");
        sqlStr.append("where lower(sem_acentos(d.abreviatura)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplinacomposta gdc on d.codigo = gdc.disciplina ");
        sqlStr.append("inner join gradedisciplina gd on gd.codigo = gdc.gradedisciplina ");
        sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");
        sqlStr.append("where lower(sem_acentos(d.abreviatura)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradedisciplinacomposta gdc on d.codigo = gdc.disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gdc.gradecurriculargrupooptativadisciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
        sqlStr.append("inner join periodoletivo pl on pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");

        sqlStr.append("where lower(sem_acentos(d.abreviatura)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append(" union ");
        sqlStr.append(" select distinct d.* from disciplina d ");
        sqlStr.append("inner join gradecurriculargrupooptativadisciplina on d.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
        sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
        sqlStr.append("inner join periodoletivo pl on pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
        sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular ");
        sqlStr.append("inner join curso c on c.codigo = gc.curso ");
        sqlStr.append("inner join unidadeensinocurso uec on uec.curso = c.codigo ");

        sqlStr.append("where lower(sem_acentos(d.abreviatura)) like lower(sem_acentos(?)) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("and uec.curso = ").append(curso).append(" ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append(" and exists (");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
            sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.turmaagrupada");
            sqlStr.append(" and c.codigo = t.curso");
            sqlStr.append(" and d.codigo = turmadisciplina.disciplina");
            sqlStr.append(" and turma.codigo = ").append(turma);
            sqlStr.append(" union all");
            sqlStr.append(" select turma.codigo from turma ");
            sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
            sqlStr.append(" where turma.curso = c.codigo ");
            sqlStr.append(" and turmadisciplina.disciplina = d.codigo ");
            sqlStr.append(" and turma.codigo = ").append(turma);
            ;
            sqlStr.append(" ) ");
        }

        sqlStr.append("order by nome");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), abreviatura + PERCENT, abreviatura + PERCENT, abreviatura + PERCENT, abreviatura + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DisciplinaVO> consultarDisciplinaPorAbreviatura(String valorConsulta, Integer codigoCurso, Integer codigoGradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        if ((codigoGradeCurricular != null) && (codigoGradeCurricular.intValue() != 0)) {
            // se esta filtrando por grade, automaticamnte ja esta filtrando por curso
            // monta a carga horaria prevista para ajudar o usuario, quando o mesmo informa a grade.
            sql.append(" SELECT * FROM (");
            sql.append("SELECT distinct disciplina.*, gradedisciplina.cargaHoraria as cargaHorariaPrevista FROM GradeDisciplina ");
            sql.append(" inner join Disciplina on Disciplina.codigo = GradeDisciplina.Disciplina ");
            sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
            sql.append(" where periodoletivo.gradecurricular = ").append(codigoGradeCurricular);
            // trazendo disciplinas grupo optativas
            sql.append(" union ");
            sql.append(" select distinct disciplina.*, gradecurriculargrupooptativadisciplina.cargaHoraria as cargaHorariaPrevista from gradecurriculargrupooptativa ");
            sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
            sql.append(" inner join Disciplina on Disciplina.codigo = gradecurriculargrupooptativadisciplina.Disciplina ");
            sql.append(" where gradecurriculargrupooptativa.gradecurricular = ").append(codigoGradeCurricular);
            // trazendo disciplinas filhas de uma composição
            sql.append(" union ");
            sql.append(" select distinct disciplina.*, gradedisciplinacomposta.cargaHoraria as cargaHorariaPrevista from gradedisciplinacomposta ");
            sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplinacomposta.periodoletivo ");
            sql.append(" inner join Disciplina on Disciplina.codigo = gradedisciplinacomposta.Disciplina ");
            sql.append(" where periodoletivo.gradecurricular = ").append(codigoGradeCurricular);
            // trazendo disciplinas filhas de uma composição de um grupo optativa
            sql.append(" union ");
            sql.append(" select distinct Disciplina.*, gradedisciplinacomposta.cargahoraria as cargaHorariaPrevista from gradedisciplinacomposta ");
            sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
            sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
            sql.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
            sql.append(" where gradecurriculargrupooptativa.gradecurricular = ").append(codigoGradeCurricular);
            // fechando SQL
            sql.append(" ) as disciplina where ");
        } else {
            // se nao esta filtrando por grade entamos temos q verificar o curso
            if ((codigoCurso != null) && (codigoCurso.intValue() != 0)) {
                sql.append("SELECT *, 0 as cargaHorariaPrevista FROM Disciplina WHERE ");
                sql.append(" codigo in ( ");
                sql.append(" select distinct disciplina.codigo from gradedisciplina ");
                sql.append(" inner join Disciplina on Disciplina.codigo = GradeDisciplina.Disciplina ");
                sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // trazendo disciplinas grupo optativas
                sql.append(" union ");
                sql.append(" select distinct disciplina.codigo from gradecurriculargrupooptativa ");
                sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
                sql.append(" inner join Disciplina on Disciplina.codigo = gradecurriculargrupooptativadisciplina.Disciplina ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // trazendo disciplinas filhas de uma composição
                sql.append(" union ");
                sql.append(" select distinct disciplina.codigo from gradedisciplinacomposta ");
                sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplinacomposta.periodoletivo ");
                sql.append(" inner join Disciplina on Disciplina.codigo = gradedisciplinacomposta.Disciplina ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // trazendo disciplinas filhas de uma composição de um grupo optativa
                sql.append(" union ");
                sql.append(" select distinct Disciplina.codigo from gradedisciplinacomposta ");
                sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
                sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
                sql.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
                sql.append(" inner join gradecurricular on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
                sql.append(" where gradecurricular.curso = ").append(codigoCurso);
                // fechando SQL
                sql.append(" ) and ");
            } else {
                sql.append("SELECT *, 0 as cargaHorariaPrevista FROM Disciplina WHERE ");
            }
        }
        sql.append(" lower (sem_acentos(abreviatura)) like(sem_acentos('%").append(valorConsulta.toLowerCase()).append("%')) ORDER BY nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsultaCHPrevista(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public DisciplinaVO consultarPorAbreviaturaUnica(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Disciplina WHERE lower (sem_acentos(abreviatura)) ilike lower(sem_acentos(?)) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.trim());
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        }
        return null;
    }

    public void montarFiltroBimestreSemestre(StringBuilder sqlStr, TipoRequerimentoVO tipoRequerimentoVO) {
        sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo and mptd.disciplina = disciplina.codigo ");
        switch (tipoRequerimentoVO.getBimestre()) {
            case "1":
                sqlStr.append(" AND mptd.bimestre = '").append("1").append("' ");
                sqlStr.append(" AND mptd.semestre = '").append("1").append("' ");
                break;
            case "2":
                sqlStr.append(" AND mptd.bimestre = '").append("2").append("' ");
                sqlStr.append(" AND mptd.semestre = '").append("1").append("' ");
                break;
            case "3":
                sqlStr.append(" AND mptd.bimestre = '").append("1").append("' ");
                sqlStr.append(" AND mptd.semestre = '").append("2").append("' ");
                break;
            case "4":
                sqlStr.append(" AND mptd.bimestre = '").append("2").append("' ");
                sqlStr.append(" AND mptd.semestre = '").append("2").append("' ");
                break;
            case "semestre":
                sqlStr.append(" AND mptd.bimestre = '").append("2").append("' ");
                sqlStr.append(" AND mptd.semestre = '").append("2").append("' ");
                break;
        }
    }

    @Override
    public List<DisciplinaVO> consultarPorNome_CalendarioRelatorioFacilitador(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
        sql.append(" * FROM Disciplina ");
        sql.append(" WHERE sem_acentos(disciplina.nome) ilike sem_acentos(?) ");
        sql.append(" and exists ( ");
        sql.append(" select from calendariorelatoriofinalfacilitador ");
        sql.append(" where calendariorelatoriofinalfacilitador.disciplina = disciplina.codigo ) ");
        sql.append(" ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), PERCENT + valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List consultarPorCodigo_CalendarioRelatorioFacilitador(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
        sql.append(" * FROM Disciplina ");
        sql.append(" WHERE disciplina.codigo = ").append(valorConsulta.intValue());
        sql.append(" and exists ( ");
        sql.append(" select from calendariorelatoriofinalfacilitador ");
        sql.append(" where calendariorelatoriofinalfacilitador.disciplina = disciplina.codigo ) ");
        sql.append(" ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

}
