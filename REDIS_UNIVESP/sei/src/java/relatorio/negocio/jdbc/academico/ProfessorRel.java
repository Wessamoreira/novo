package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.Escolaridade;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.AlunosPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.academico.ProfessoresPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.academico.ProfessoresPorUnidadeCursoTurmaRelVOs;
import relatorio.negocio.interfaces.academico.ProfessorRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ProfessorRel extends SuperRelatorio implements ProfessorRelInterfaceFacade {

    public ProfessorRel() {
    }

    public static void validarDados(UnidadeEnsinoVO unidadeEnsino, UnidadeEnsinoCursoVO unidadeEnsinoCurso, DisciplinaVO disciplina) throws ConsistirException {
//        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
//            throw new ConsistirException(UteisJSF.internacionalizar("msg_ProfessorRel_unidadeEnsino"));
//        }
//        if ((unidadeEnsinoCurso.getCurso().getCodigo() == 0 || unidadeEnsinoCurso.getCurso().getCodigo() == null || unidadeEnsinoCurso.getCurso() == null)) {
//            throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosPorUnidadeCursoTurnoTurmaRel_cursoDisciplina"));
//        }
//        if (unidadeEnsinoCurso.getCurso().getCodigo() == 0 || unidadeEnsinoCurso.getCurso().getCodigo() == null || unidadeEnsinoCurso.getCurso() == null) {
//            throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosPorUnidadeCursoTurnoTurmaRel_curso"));
//        }
    }

    /*
     * (non-Javadoc)
     *
     * @seerelatorio.negocio.jdbc.academico.ProfessorRelInterfaceFacade#criarObjeto(negocio.comuns.
     * administrativo.UnidadeEnsinoVO, negocio.comuns.academico.CursoVO, negocio.comuns.academico.TurmaVO,
     * java.lang.String, java.lang.String)
     */
    public List<ProfessoresPorUnidadeCursoTurmaRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String semestre, String ano, String situacaoProfessor, String escolaridade, UsuarioVO usuarioVO) throws Exception {
        List<ProfessoresPorUnidadeCursoTurmaRelVO> listaRelatorio = new ArrayList<ProfessoresPorUnidadeCursoTurmaRelVO>(0);
        ProfessoresPorUnidadeCursoTurmaRelVO professoresPorUnidadeCursoTurmaRelVO = new ProfessoresPorUnidadeCursoTurmaRelVO();
        professoresPorUnidadeCursoTurmaRelVO.setProfessoresVOs(consultarProfessores(unidadeEnsino, curso, turma, disciplina.getCodigo(), semestre, ano, situacaoProfessor, escolaridade, usuarioVO));
        professoresPorUnidadeCursoTurmaRelVO.setUnidadeEnsino(consultarNomeUnidadeEnsino(unidadeEnsino));
        professoresPorUnidadeCursoTurmaRelVO.setDisciplina(disciplina.getNome());
        professoresPorUnidadeCursoTurmaRelVO.setFiltroSituacao(situacaoProfessor);
        if (!curso.getCodigo().equals(0)) {
            professoresPorUnidadeCursoTurmaRelVO.setCurso(consultarNomeCurso(curso));
        } else {
            professoresPorUnidadeCursoTurmaRelVO.setCurso("Curso não Informado!");
        }
        if (turma.getCodigo() != 0) {
            TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
            professoresPorUnidadeCursoTurmaRelVO.setTurma(turmaVO.getIdentificadorTurma());
            professoresPorUnidadeCursoTurmaRelVO.setTurno(turmaVO.getTurno().getNome());
        } else {
            professoresPorUnidadeCursoTurmaRelVO.setTurma("Turma não Informada!");
            professoresPorUnidadeCursoTurmaRelVO.setTurno("Turno não Informado!");
        }
        professoresPorUnidadeCursoTurmaRelVO.setAno(ano);
        professoresPorUnidadeCursoTurmaRelVO.setSemestre(semestre);
        professoresPorUnidadeCursoTurmaRelVO.setQtdeProfessores(verificarQtdeProfessoresLista(professoresPorUnidadeCursoTurmaRelVO.getProfessoresVOs()));

        listaRelatorio.add(professoresPorUnidadeCursoTurmaRelVO);
        return listaRelatorio;
    }

    private Integer verificarQtdeProfessoresLista(List<ProfessoresPorUnidadeCursoTurmaRelVOs> professoresVOs) {
        String matricula = "";
        Integer qtdeAlunoTemp = 0;
        for (ProfessoresPorUnidadeCursoTurmaRelVOs professor : professoresVOs) {
            if (!matricula.equals(professor.getMatricula())) {
                matricula = professor.getMatricula();
                qtdeAlunoTemp = qtdeAlunoTemp + 1;
            }
        }
        return qtdeAlunoTemp;
    }

    private String consultarNomeUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) throws Exception {
        if (unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() != 0) {
            unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
            return unidadeEnsino.getNome();
        } else {
            return "Unidade de Ensino não Informada!";
        }
    }

    private String consultarNomeCurso(CursoVO curso) throws Exception {
        curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, null);
        return curso.getNome();
    }

    private List<ProfessoresPorUnidadeCursoTurmaRelVOs> consultarProfessores(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, Integer disciplina, String semestre, String ano, String situacaoProfessor, String escolaridade, UsuarioVO usuarioVO) throws Exception {
        List lista = new ArrayList(0);
        lista = consultarPorCursoTurmaDisciplinaAnoSemestreSituacao(unidadeEnsino.getCodigo(), curso.getCodigo(), turma.getCodigo(), disciplina, semestre, ano, situacaoProfessor, escolaridade, usuarioVO);
        if (lista.isEmpty()) {
            throw new Exception("Não há professores cadastrados nas condições propostas acima.");
        }
        return lista;
    }

    public List consultarPorCursoTurmaDisciplinaAnoSemestreSituacao(Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, String semestre, String ano, String situacaoProfessor, String escolaridade, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT pessoa.codigo AS \"pessoa.codigo\", funcionario.matricula AS \"funcionario.matricula\", ");
        sqlStr.append("pessoa.nome AS \"pessoa.nome\", CASE WHEN pessoa.ativo = true THEN 'Ativo' ELSE 'Inativo' END AS situacao, ");
        sqlStr.append("pessoa.telefoneRes AS \"pessoa.telefoneRes\", ");
        sqlStr.append("pessoa.celular AS \"pessoa.celular\", pessoa.email AS \"pessoa.email\", pessoa.email2 AS \"pessoa.email2\", ");
        sqlStr.append("pessoa.funcionario AS \"pessoa.funcionario\", pessoa.professor AS \"pessoa.professor\", pessoa.sexo AS \"pessoa.sexo\" ");
        sqlStr.append("FROM professortitulardisciplinaturma ptdt ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = ptdt.professor ");
        sqlStr.append("INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = ptdt.turma ");
        sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
        sqlStr.append("INNER JOIN unidadeensino ON unidadeensino.codigo = turma.unidadeensino ");
        sqlStr.append("INNER JOIN disciplina ON disciplina.codigo = ptdt.disciplina ");
        sqlStr.append("WHERE 1=1 ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append("AND unidadeEnsino.codigo = ").append(unidadeEnsino);
        }
        if (curso != 0) {
            sqlStr.append(" AND turma.curso = ").append(curso);
        }
        if (turma != 0) {
            sqlStr.append(" AND ptdt.turma = ").append(turma);
        }
        if (disciplina != null && !disciplina.equals(0)) {
            sqlStr.append(" AND ptdt.disciplina = ").append(disciplina);
        }
        if (!ano.equals("")) {
            sqlStr.append(" AND ptdt.ano = '").append(ano).append("'");
        }
        if (!semestre.equals("")) {
            sqlStr.append(" AND ptdt.semestre = '").append(semestre).append("'");
        }
        if (situacaoProfessor.equals("AT")) {
            sqlStr.append(" AND pessoa.ativo = true");
        } else if (situacaoProfessor.equals("IN")) {
            sqlStr.append(" AND pessoa.ativo = false");
        }
        if (!escolaridade.equals("")) {
            sqlStr.append(" AND pessoa.codigo IN (select distinct pessoa from formacaoacademica  where escolaridade = '").append(escolaridade).append("'").append(")");
        }
        sqlStr.append(" ORDER BY Pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public static List<ProfessoresPorUnidadeCursoTurmaRelVOs> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList<MatriculaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    public static ProfessoresPorUnidadeCursoTurmaRelVOs montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        ProfessoresPorUnidadeCursoTurmaRelVOs obj = new ProfessoresPorUnidadeCursoTurmaRelVOs();
        obj.setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.setMatricula(dadosSQL.getString("funcionario.matricula"));
        obj.setNomeProfessor(dadosSQL.getString("pessoa.nome"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTelefoneRes(dadosSQL.getString("pessoa.telefoneRes"));
        obj.setCelular(dadosSQL.getString("pessoa.celular"));
        obj.setEmail(dadosSQL.getString("pessoa.email"));
        obj.setEmail2(dadosSQL.getString("pessoa.email2"));
        obj.setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
        obj.setProfessor(dadosSQL.getBoolean("pessoa.professor"));
        obj.setSexo(dadosSQL.getString("pessoa.sexo"));
        obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, obj.getFuncionario(), usuario));
        obj.setTitulacao(obj.consultarMaiorNivelEscolaridade());
        return obj;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignIReportRelatorioSintetico() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeSintetico() + ".jrxml");
    }
    
    public static String getDesignIReportRelatorioNomeAssinatura() {
    	return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "NomeAssinatura.jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("ProfessorRel");
    }

    public static String getIdEntidadeSintetico() {
        return ("ProfessorPorUnidadeCursoTurmaSinteticoRel");
    }
}
