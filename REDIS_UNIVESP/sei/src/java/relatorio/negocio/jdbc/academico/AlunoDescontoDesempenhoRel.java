
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.AlunoDescontoDesempenhoRelVO;
import relatorio.negocio.interfaces.academico.AlunoDescontoDesempenhoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class AlunoDescontoDesempenhoRel extends SuperRelatorio implements AlunoDescontoDesempenhoRelInterfaceFacade {

    public AlunoDescontoDesempenhoRel() {
    }

    public void validarDadosGeracaoRelatorio(String tipoRelatorio) throws ConsistirException {
        if (tipoRelatorio.equals("")) {
            throw new ConsistirException("Por Favor Informe o tipo de consulta(Curso/Turma).");
        }
    }

    public List realizarMontagemListaSelectItemCurso(Integer unidadeEnsino) throws Exception {
        List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino("", unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getCurso().getNome()));
        }
        return objs;
    }

    public List realizarMontagemListaSelectItemTurma(Integer unidadeEnsino) throws Exception {
        List resultadoConsulta = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma("", unidadeEnsino, false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            TurmaVO obj = (TurmaVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
        }
        return objs;
    }

    public List executarCriacaoObjetos(AlunoDescontoDesempenhoRelVO alunoDescontoDesempenhoRelVO, String tipoConsulta, Integer unidadeEnsino, Integer curso, Integer turma) throws Exception {
        List listaAlunoDescontoDesempenho = new ArrayList(0);
        if (listaAlunoDescontoDesempenho.isEmpty()) {
            listaAlunoDescontoDesempenho = consultarPorTipoPessoaUnidadeEnsinoCursoTurnoTurma(tipoConsulta, unidadeEnsino, curso, turma);
        }
        return listaAlunoDescontoDesempenho;
    }

    public List consultarPorTipoPessoaUnidadeEnsinoCursoTurnoTurma(String tipoConsulta, Integer unidadeEnsino, Integer curso, Integer turma) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        if (tipoConsulta.equals("curso")) {
            sqlStr.append(" SELECT tabela1.*, (select distinct count(disciplina.*) from disciplina ");
            sqlStr.append("inner join matriculaPeriodoTurmaDisciplina as mptd on mptd.disciplina = disciplina.codigo ");
            sqlStr.append("inner join turma on turma.codigo = mptd.turma ");
            sqlStr.append("inner join matricula on matricula.matricula = mptd.matricula ");
            sqlStr.append("inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo ");
            sqlStr.append("inner join curso on curso.codigo = turma.curso ");
            sqlStr.append("where matricula.matricula = tabela1.matricula and curso.codigo = ");
            sqlStr.append(curso);
            sqlStr.append(" AND unidadeEnsino.codigo = ");
            sqlStr.append(unidadeEnsino);
            sqlStr.append(" )AS totalDisciplinas, ");

            sqlStr.append("(select distinct count(disciplina.*) from disciplina ");
            sqlStr.append("inner join matriculaPeriodoTurmaDisciplina as mptd on mptd.disciplina = disciplina.codigo ");
            sqlStr.append("inner join turma on turma.codigo = mptd.turma ");
            sqlStr.append("inner join matricula on matricula.matricula = mptd.matricula ");
            sqlStr.append("inner join matriculaPeriodo on matriculaPeriodo.codigo = mptd.matriculaPeriodo ");
            sqlStr.append("inner join historico on historico.matriculaPeriodo = matriculaPeriodo.codigo ");
            sqlStr.append("inner join unidadeEnsino on turma.unidadeEnsino = unidadeEnsino.codigo ");
            sqlStr.append("inner join curso on curso.codigo = turma.curso ");
            sqlStr.append("where matricula.matricula = tabela1.matricula and curso.codigo = ");
            sqlStr.append(curso);
            sqlStr.append(" AND unidadeEnsino.codigo = ");
            sqlStr.append(unidadeEnsino);
            sqlStr.append(" and historico.situacao = 'RE') AS disciplinasReprovadas");

            sqlStr.append(" FROM (select distinct matricula.matricula, pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\",");
            sqlStr.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\" ");
            sqlStr.append(" from matricula ");
            sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
            sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina as mptd on mptd.matricula = matricula.matricula ");
            sqlStr.append(" inner join turma on turma.codigo = mptd.turma ");
            sqlStr.append(" inner join unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo ");
            sqlStr.append(" inner join curso on curso.codigo = turma.curso ");
            sqlStr.append(" where curso.codigo = ");
            sqlStr.append(curso);
            sqlStr.append(" AND unidadeEnsino.codigo = ");
            sqlStr.append(unidadeEnsino);
            sqlStr.append(" ) AS tabela1 ");
        }
        sqlStr.append(" ORDER BY tabela1.matricula");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado));
    }

    public List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    public static AlunoDescontoDesempenhoRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
        AlunoDescontoDesempenhoRelVO obj = new AlunoDescontoDesempenhoRelVO();
        obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatriculaVO().getAluno().setCPF(dadosSQL.getString("pessoa.cpf"));
        obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
        obj.setTotalDisciplinas(new Integer(dadosSQL.getInt("totalDisciplinas")));
        obj.setTotalDisciplinasReprovadas(new Integer(dadosSQL.getInt("disciplinasReprovadas")));
        return obj;
    }

    public String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getIdEntidade() {
        return ("AlunoDescontoDesempenhoRel");
    }
}
