/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.MapaSituacaoAlunoRelVO;
import relatorio.negocio.interfaces.financeiro.MapaSituacaoAlunoInadimplenteRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MapaSituacaoAlunoInadimplenteRel extends SuperRelatorio implements MapaSituacaoAlunoInadimplenteRelInterfaceFacade {

    public MapaSituacaoAlunoInadimplenteRel() {
    }

    public void validarDados(MatriculaVO matricula, TurmaVO turma, CursoVO curso, Integer unidadeEnsino, String ano, String semestre) throws ConsistirException {
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            throw new ConsistirException("A campo UNIDADE DE ENSINO deve ser informado para a geração do relatório.");
        }

        if ((curso.getCodigo().equals(0)) && (turma.getCodigo().equals(0)) && (matricula.getMatricula().equals(""))) {
            throw new ConsistirException("Pelo menos um dos três campos deve ser informado(CURSO, TURMA ou MATRÍCULA).");
        }

        if ((!curso.getCodigo().equals(0) && !curso.getNivelEducacional().equals("PO")
                && !curso.getNivelEducacional().equals("EX")) || (!turma.getCodigo().equals(0)
                && !turma.getCurso().getNivelEducacional().equals("PO") && !turma.getCurso().getNivelEducacional().equals("EX"))) {

//        if (!nivelEducacional.equals("PO") && !nivelEducacional.equals("EX")) {
            if (ano.equals("")) {
                throw new ConsistirException("O campo ANO deve ser informado.");
            }
            if (semestre.equals("")) {
                throw new ConsistirException("O campo SEMESTRE deve ser informado.");
            }
        }
//        if (curso == null || curso == 0) {
//            throw new ConsistirException("A campo CURSO deve ser informado para a geração do relatório.");
//        }
//        if (campoFiltro.equals("aluno")) {
//            if (matricula.equals("")) {
//                throw new ConsistirException("O campo MATRÍCULA deve ser informado para a geração do relatório.");
//            }
//        }
//        if (campoFiltro.equals("turma")) {
//            if (turma == 0) {
//                throw new ConsistirException("O campo TURMA deve ser informado para a geração do relatório.");
//            }
//        }
//        if (campoFiltro.equals("")) {
//            throw new ConsistirException("O campo Filtro deve ser informado.");
//        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.SituacaoFinanceiraAlunoRelInterfaceFacade#criarObjeto(negocio.comuns.academico
     * .MatriculaVO)
     */
    public List<MapaSituacaoAlunoRelVO> executarGeracaoRelatorio(Integer unidadeEnsino, Integer curso, Integer turma, MatriculaVO matriculaVO, String ano, String semestre) throws Exception {
        List<MapaSituacaoAlunoRelVO> listaResultado = new ArrayList<MapaSituacaoAlunoRelVO>(0);

        listaResultado = consultarMatricula(unidadeEnsino, curso, turma, matriculaVO.getMatricula(), ano, semestre);
        if (listaResultado.isEmpty()) {
            throw new Exception("Não foi encontrado nenhum aluno com os parâmetros passados.");
        }

        List<ContaReceberVO> contaReceberVOs = consultarContaReceberAluno(listaResultado);
        listaResultado = realizarSeparacaoContaReceberMatricula(listaResultado, contaReceberVOs);
        contaReceberVOs.clear();

        List<HistoricoVO> historicoVOs = consultarHistoricoAluno(listaResultado, ano, semestre);
        listaResultado = realizarSeparacaoHistoricoMatricula(listaResultado, historicoVOs);
        historicoVOs.clear();

        List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = consultarDocumentacaoMatriculaAluno(listaResultado);
        listaResultado = realizarSeparacaoDocumentacaoMatricula(listaResultado, documetacaoMatriculaVOs);
        documetacaoMatriculaVOs.clear();

        return listaResultado;
    }

    public List<MapaSituacaoAlunoRelVO> consultarMatricula(Integer unidadeEnsino, Integer curso, Integer turma, String matricula, String ano, String semestre) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select matricula.matricula, pessoa.nome AS \"aluno\",  curso.nome AS \"curso.nome\", ");
        sql.append(" (select SUM(gradedisciplina.cargaHoraria) from historico inner join disciplina on disciplina.codigo = historico.disciplina ");
        sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
        sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo and gradedisciplina.disciplina = disciplina.codigo ");
        sql.append(" where historico.matricula = matricula.matricula) AS \"cargaHorariaCurso\", ");
        sql.append(" (select SUM(gradedisciplina.cargaHoraria) from disciplina inner join historico on historico.disciplina = disciplina.codigo ");
        sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
        sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
        sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo and gradedisciplina.disciplina = disciplina.codigo ");
        sql.append(" where (historico.situacao = 'AP' or historico.situacao = 'AA') and historico.matricula = matricula.matricula) as \"cargaHorariaCumprida\", ");
        sql.append(" turma2.identificadorturma AS \"turma2.identificadorTurma\" ");
        sql.append(" from matricula ");
        sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = (select codigo from matriculaPeriodo where matriculaPeriodo.matricula = matricula.matricula order by codigo desc limit 1) ");
        sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sql.append(" inner join curso on curso.codigo = matricula.curso ");
        sql.append(" inner join gradeCurricular on gradeCurricular.codigo = matriculaPeriodo.gradeCurricular ");
        sql.append(" inner join periodoLetivo on periodoLetivo.gradeCurricular = gradeCurricular.codigo ");
        sql.append(" inner join gradeDisciplina on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
        sql.append(" inner join disciplina on disciplina.codigo = gradeDisciplina.disciplina ");
        if (turma.intValue() != 0) {
            sql.append(" inner join matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaPeriodo.codigo ");
            sql.append(" inner join turma on turma.codigo = mptd.turma ");
            sql.append(" left join turma turma2 on turma2.codigo = matriculaPeriodo.turma ");
        } else {
            sql.append(" left join turma turma2 on turma2.codigo = matriculaPeriodo.turma ");
        }
        sql.append(" where matricula.unidadeEnsino = ");
        sql.append(unidadeEnsino.intValue());
        if (!curso.equals(0)) {
            sql.append(" and curso.codigo = ").append(curso.intValue());
        }
        if (turma.intValue() != 0) {
            sql.append(" and turma.codigo = ").append(turma.intValue());
        }
        if (!matricula.equals("")) {
            sql.append(" and matricula.matricula = '").append(matricula).append("'");
        }
        if (!ano.equals("")) {
            sql.append(" and matriculaperiodo.ano = '").append(ano.toString()).append("'");
        }
        if (!semestre.equals("")) {
            sql.append(" and matriculaperiodo.semestre = '").append(semestre.toString()).append("'");
        }
//        if (turma.intValue() != 0) {
        sql.append(" Group by curso.nome, turma2.identificadorTurma, pessoa.nome, matricula.matricula ");
        sql.append(" order by curso.nome, turma2.identificadorTurma, pessoa.nome, matricula.matricula ");
//        } else {
//            sql.append(" Group by matricula.matricula, matricula.curso, curso.nome, turma.identificadorTurma, pessoa.nome, curso.codigo ");
//            sql.append(" order by matricula.matricula, matricula.curso, curso.nome, turma.identificadorTurma, pessoa.nome");
//        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarListaObjetoPrincipal(tabelaResultado);
    }

    public List<MapaSituacaoAlunoRelVO> realizarSeparacaoContaReceberMatricula(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, List<ContaReceberVO> contaReceberVOs) {
        for (ContaReceberVO contaReceberVO : contaReceberVOs) {
            for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
                if (mapaSituacaoAlunoRelVO.getMatricula().equals(contaReceberVO.getMatriculaAluno().getMatricula())) {
                    mapaSituacaoAlunoRelVO.getListaContaReceberVOs().add(contaReceberVO);
                    mapaSituacaoAlunoRelVO.setPendenciaFinanceira(Boolean.TRUE);
                    break;
                }
            }
        }

        return mapaSituacaoAlunoRelVOs;
    }

    public List<MapaSituacaoAlunoRelVO> realizarSeparacaoDocumentacaoMatricula(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs) {
        for (DocumetacaoMatriculaVO documetacaoMatriculaVO : documetacaoMatriculaVOs) {
            for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
                if (mapaSituacaoAlunoRelVO.getMatricula().equals(documetacaoMatriculaVO.getMatricula())) {
                    mapaSituacaoAlunoRelVO.getListaDocumentacaoVOs().add(documetacaoMatriculaVO);
                    mapaSituacaoAlunoRelVO.setPendenciaDocumentacao(Boolean.TRUE);
                    break;
                }
            }
        }

        return mapaSituacaoAlunoRelVOs;
    }

    public List<MapaSituacaoAlunoRelVO> realizarSeparacaoHistoricoMatricula(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, List<HistoricoVO> historicoVOs) {
        for (HistoricoVO historicoVO : historicoVOs) {
            for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
                if (mapaSituacaoAlunoRelVO.getMatricula().equals(historicoVO.getMatricula().getMatricula())) {
                    mapaSituacaoAlunoRelVO.getListaHistoricoVOs().add(historicoVO);
                    break;
                }
            }
        }

        return mapaSituacaoAlunoRelVOs;
    }

    private List<ContaReceberVO> consultarContaReceberAluno(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select matricula.matricula, contaReceber.parcela, contaReceber.valor, contaReceber.dataVencimento, contaReceber.multa,  contaReceber.juro ");
        sql.append(" from contaReceber ");
        sql.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
        sql.append(" where matricula.matricula in (");
        int x = 0;
        for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
            if (x > 0) {
                sql.append(", ");
            }
            sql.append("'");
            sql.append(mapaSituacaoAlunoRelVO.getMatricula());
            sql.append("'");
            x++;
        }
        sql.append(") and contareceber.situacao = 'AR' ");
        sql.append(" Group by matricula.matricula, contaReceber.parcela, contaReceber.valor, contaReceber.dataVencimento, contaReceber.multa,  contaReceber.juro ");
        sql.append(" order by matricula.matricula, contareceber.parcela");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarListaFinanceiroAluno(tabelaResultado);
    }

    private List<DocumetacaoMatriculaVO> consultarDocumentacaoMatriculaAluno(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select documetacaomatricula.situacao, tipoDocumento.nome AS \"tipoDocumento.nome\", matricula.matricula  from matricula ");
        sql.append(" inner join documetacaoMatricula on documetacaoMatricula.matricula = matricula.matricula ");
        sql.append(" inner join tipoDocumento on documetacaoMatricula.tipodeDocumento = tipoDocumento.codigo ");
        sql.append(" where matricula.matricula in(");
        int x = 0;
        for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
            if (x > 0) {
                sql.append(", ");
            }
            sql.append("'");
            sql.append(mapaSituacaoAlunoRelVO.getMatricula());
            sql.append("'");
            x++;
        }
        sql.append(") and documetacaomatricula.situacao = 'PE'");
        sql.append(" Group by documetacaomatricula.situacao, tipoDocumento.nome, matricula.matricula  ");
        sql.append(" order by  matricula.matricula, documetacaomatricula.situacao ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarListaDocumentacaoMatricula(tabelaResultado);
    }

    private List<HistoricoVO> consultarHistoricoAluno(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, String ano, String semestre) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT historico.matricula, disciplina.nome, gradeDisciplina.cargaHoraria, historico.freguencia, ");
        sql.append("historico.mediaFinal, historico.situacao, matricula.situacao AS \"matricula.situacao\" FROM historico ");
        sql.append("INNER JOIN disciplina ON disciplina.codigo = historico.disciplina ");
        sql.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = historico.matriculaperiodo ");
        sql.append("INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
        sql.append("inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
        sql.append("inner join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sql.append("WHERE historico.matricula IN(");
        int x = 0;
        for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
            if (x > 0) {
                sql.append(", ");
            }
            sql.append("'");
            sql.append(mapaSituacaoAlunoRelVO.getMatricula());
            sql.append("'");
            x++;
        }
        sql.append(") ");
        if (!ano.equals("")) {
            sql.append("AND matriculaperiodo.ano = '");
            sql.append(ano.toString());
            sql.append("' ");
        }
        if (!semestre.equals("")) {
            sql.append("AND matriculaperiodo.semestre = '");
            sql.append(semestre.toString());
            sql.append("' ");
        }
        sql.append("GROUP BY disciplina.nome, gradeDisciplina.cargaHoraria, historico.freguencia, historico.mediaFinal, historico.situacao, historico.matricula, matricula.situacao ");
        sql.append("ORDER BY historico.matricula, disciplina.nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarListaHistoricoAluno(tabelaResultado);
    }

    private List<MapaSituacaoAlunoRelVO> montarListaObjetoPrincipal(SqlRowSet tabelaResultado) throws Exception {
        List<MapaSituacaoAlunoRelVO> dadosSituacaoContaReceberVOs = new ArrayList<MapaSituacaoAlunoRelVO>(0);
        while (tabelaResultado.next()) {
            dadosSituacaoContaReceberVOs.add(montarDados(tabelaResultado));
        }
        return dadosSituacaoContaReceberVOs;
    }

    private List<HistoricoVO> montarListaHistoricoAluno(SqlRowSet tabelaResultado) throws Exception {
        List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>(0);
        while (tabelaResultado.next()) {
            historicoVOs.add(montarDadosHistoricoAluno(tabelaResultado));
        }
        return historicoVOs;
    }

    private List<DocumetacaoMatriculaVO> montarListaDocumentacaoMatricula(SqlRowSet tabelaResultado) throws Exception {
        List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
        while (tabelaResultado.next()) {
            documentacaoMatriculaVOs.add(montarDadosDocumentacaoMatricula(tabelaResultado));
        }
        return documentacaoMatriculaVOs;
    }

    private List<ContaReceberVO> montarListaFinanceiroAluno(SqlRowSet tabelaResultado) throws Exception {
        List<ContaReceberVO> dadosSituacaoContaReceberVOs = new ArrayList<ContaReceberVO>(0);
        while (tabelaResultado.next()) {
            dadosSituacaoContaReceberVOs.add(montarDadosFinanceiroAluno(tabelaResultado));
        }
        return dadosSituacaoContaReceberVOs;
    }

    private MapaSituacaoAlunoRelVO montarDados(SqlRowSet tabelaResultado) throws Exception {
        MapaSituacaoAlunoRelVO obj = new MapaSituacaoAlunoRelVO();
        obj.setMatricula(tabelaResultado.getString("matricula"));
        obj.setAluno(tabelaResultado.getString("aluno"));
        obj.setCurso(tabelaResultado.getString("curso.nome"));
        obj.setCargaHorariaCurso(tabelaResultado.getInt("cargaHorariaCurso"));
        obj.setCargaHorariaCumprida(tabelaResultado.getInt("cargaHorariaCumprida"));
        obj.setTurma(tabelaResultado.getString("turma2.identificadorTurma"));
        return obj;
    }

    private ContaReceberVO montarDadosFinanceiroAluno(SqlRowSet tabelaResultado) throws Exception {
        ContaReceberVO obj = new ContaReceberVO();
        obj.getMatriculaAluno().setMatricula(tabelaResultado.getString("matricula"));
        obj.setParcela(tabelaResultado.getString("parcela"));
        obj.setValor(tabelaResultado.getDouble("valor"));
        obj.setDataVencimento(tabelaResultado.getDate("dataVencimento"));
        obj.setMulta(tabelaResultado.getDouble("multa"));
        obj.setJuro(tabelaResultado.getDouble("juro"));
        return obj;
    }

    private DocumetacaoMatriculaVO montarDadosDocumentacaoMatricula(SqlRowSet tabelaResultado) throws Exception {
        DocumetacaoMatriculaVO documentacaoMatricula = new DocumetacaoMatriculaVO();
        documentacaoMatricula.setSituacao(tabelaResultado.getString("situacao"));
        documentacaoMatricula.setMatricula(tabelaResultado.getString("matricula"));
        documentacaoMatricula.getTipoDeDocumentoVO().setNome(tabelaResultado.getString("tipoDocumento.nome"));
        return documentacaoMatricula;
    }

    private HistoricoVO montarDadosHistoricoAluno(SqlRowSet tabelaResultado) throws Exception {
        HistoricoVO historicoVO = new HistoricoVO();
        historicoVO.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
        historicoVO.getMatricula().setSituacao(tabelaResultado.getString("matricula.situacao"));
        historicoVO.getDisciplina().setNome(tabelaResultado.getString("nome"));
        historicoVO.getGradeDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("cargaHoraria"));
        historicoVO.setFreguencia(tabelaResultado.getDouble("freguencia"));
        if (historicoVO.getMatricula().getSituacao().equals("CA")) {
            historicoVO.setSituacao("CA");
        } else {
            historicoVO.setSituacao(tabelaResultado.getString("situacao"));
        }
        historicoVO.setMediaFinal(tabelaResultado.getDouble("mediafinal"));
        return historicoVO;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("MapaSituacaoAlunoInadimplenteRel");
    }
}
