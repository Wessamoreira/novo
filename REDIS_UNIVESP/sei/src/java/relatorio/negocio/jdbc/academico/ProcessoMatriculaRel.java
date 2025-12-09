package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.ProcessoMatriculaRelVO;
import relatorio.negocio.interfaces.academico.ProcessoMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ProcessoMatriculaRel extends SuperRelatorio implements ProcessoMatriculaRelInterfaceFacade {

    public ProcessoMatriculaRel() throws Exception {
        inicializarOrdenacoesRelatorio();
    }

    public List<ProcessoMatriculaRelVO> criarObjeto(String unidadeEnsino, String curso, String situacao) throws Exception {
        List<ProcessoMatriculaRelVO> processoMatriculaRelVOs = new ArrayList<ProcessoMatriculaRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(unidadeEnsino, curso, situacao);
        while (dadosSQL.next()) {
            processoMatriculaRelVOs.add(montarDados(dadosSQL));
        }
        return processoMatriculaRelVOs;
    }

    private ProcessoMatriculaRelVO montarDados(SqlRowSet dadosSQL) {
        ProcessoMatriculaRelVO processoMatriculaRelVO = new ProcessoMatriculaRelVO();
        processoMatriculaRelVO.setData(Uteis.getDataJDBC(dadosSQL.getDate("processomatricula_data")));
        processoMatriculaRelVO.setDataFinal(Uteis.getDataJDBC(dadosSQL.getDate("processomatricula_datafinal")));
        processoMatriculaRelVO.setDataFinalMatForaPrazo(Uteis.getDataJDBC(dadosSQL.getDate("processomatriculacalendario_datafinalmatforaprazo")));
        processoMatriculaRelVO.setDataFinalMatricula(Uteis.getDataJDBC(dadosSQL.getDate("processomatriculacalendario_datafinalmatricula")));
        processoMatriculaRelVO.setDataInicio(Uteis.getDataJDBC(dadosSQL.getDate("processomatricula_datainicio")));
        processoMatriculaRelVO.setDataInicioMatForaPrazo(Uteis.getDataJDBC(dadosSQL.getDate("processomatriculacalendario_datainiciomatforaprazo")));
        processoMatriculaRelVO.setDataInicioMatricula(Uteis.getDataJDBC(dadosSQL.getDate("processomatriculacalendario_datainiciomatricula")));
        processoMatriculaRelVO.setDescricao(dadosSQL.getString("processomatricula_descricao"));
        processoMatriculaRelVO.setExigeConfirmacaoPresencial(dadosSQL.getBoolean("processomatricula_exigeconfirmacaopresencial"));
        processoMatriculaRelVO.setNomeCurso(dadosSQL.getString("curso_nome"));
        processoMatriculaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
        processoMatriculaRelVO.setSituacao(dadosSQL.getString("processomatricula_situacao"));
        processoMatriculaRelVO.setUnidadeEnsino(dadosSQL.getInt("unidadeensino_codigo"));
        processoMatriculaRelVO.setValidoPelaInternet(dadosSQL.getBoolean("processomatricula_validopelainternet"));
        return processoMatriculaRelVO;
    }

    public SqlRowSet executarConsultaParametrizada(String unidadeEnsino, String curso, String situacao) throws Exception {
        String selectStr = "SELECT processomatricula.situacao AS processomatricula_situacao, "
                + "processomatricula.exigeconfirmacaopresencial  AS processomatricula_exigeconfirmacaopresencial, "
                + "processomatricula.validopelainternet AS processomatricula_validopelainternet,  processomatricula.datafinal AS processomatricula_datafinal, "
                + "processomatricula.datainicio AS processomatricula_datainicio,  processomatricula.data AS processomatricula_data, "
                + "processomatricula.descricao AS processomatricula_descricao,  processomatriculacalendario.datafinalmatforaprazo AS processomatriculacalendario_datafinalmatforaprazo, "
                + "processomatriculacalendario.datainiciomatforaprazo AS processomatriculacalendario_datainiciomatforaprazo,  "
                + "processomatriculacalendario.datafinalmatricula AS processomatriculacalendario_datafinalmatricula, "
                + "processomatriculacalendario.datainiciomatricula AS processomatriculacalendario_datainiciomatricula,  curso.nome AS curso_nome, unidadeensino.nome AS unidadeensino_nome, unidadeensino.codigo as unidadeensino_codigo from "
                + "processomatricula , curso, unidadeensinocurso, unidadeensino, processomatriculacalendario, processomatriculaunidadeensino";
        selectStr = montarVinculoEntreTabelas(selectStr);
        selectStr = montarFiltrosRelatorio(selectStr, unidadeEnsino, curso, situacao);
        selectStr += "GROUP BY processomatricula_situacao, "
                + "processomatricula_exigeconfirmacaopresencial, processomatricula_validopelainternet, processomatricula_datafinal, processomatricula_datainicio,"
                + "processomatricula_data, processomatricula_descricao, processomatriculacalendario_datafinalmatforaprazo, processomatriculacalendario_datainiciomatforaprazo,"
                + "processomatriculacalendario_datafinalmatricula, processomatriculacalendario_datainiciomatricula, curso_nome, unidadeensino_nome, unidadeensino_codigo";
        selectStr = montarOrdenacaoRelatorio(selectStr);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    private String montarFiltrosRelatorio(String selectStr, String unidadeEnsino, String curso, String situacao) {
        String filtros = "";
        if (!unidadeEnsino.equals("0")) {
            filtros = adicionarCondicionalWhere(filtros, "(unidadeensino.codigo =" + unidadeEnsino + " )", true);
            adicionarDescricaoFiltro("UnidadeEnsino = " + unidadeEnsino);
        }
        if (!curso.equals("0")) {
            filtros = adicionarCondicionalWhere(filtros, "( curso.codigo = " + curso + ")", true);
            adicionarDescricaoFiltro("Curso = " + curso);
        }
        if (situacao != null && !situacao.equals("")) {
            filtros = adicionarCondicionalWhere(filtros, "( processomatricula.situacao = '" + situacao + "')", true);
            adicionarDescricaoFiltro("Situacao = " + situacao);
        }

        selectStr += filtros;
        return selectStr;
    }

    private String montarVinculoEntreTabelas(String selectStr) {
        String vinculos = "";
        vinculos = adicionarCondicionalWhere(vinculos, "(processomatricula.codigo = processomatriculacalendario.processomatricula)", false);
        vinculos = adicionarCondicionalWhere(vinculos, "(processomatriculacalendario.processomatricula = processomatricula.codigo )", true);
        vinculos = adicionarCondicionalWhere(vinculos, "(unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno and unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino )", true);
        vinculos = adicionarCondicionalWhere(vinculos, "(curso.codigo = unidadeensinocurso.curso )", true);
        vinculos = adicionarCondicionalWhere(vinculos, "(unidadeensino.codigo = processomatriculaunidadeensino.unidadeensino)", true);

        if (!vinculos.equals("")) {
            if (selectStr.indexOf("WHERE") == -1) {
                selectStr = selectStr + " WHERE " + vinculos;
            } else {
                selectStr = selectStr + " WHERE " + vinculos;
            }
        }
        return selectStr;
    }

    protected String montarOrdenacaoRelatorio(String selectStr) {
        String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());
        if (ordenacao.equals("Unidade de Ensino")) {
            ordenacao = "unidadeensino.nome";
        }
        if (ordenacao.equals("Curso")) {
            ordenacao = "curso.nome";
        }
        if (ordenacao.equals("Situação")) {
            ordenacao = "processomatricula.situacao";
        }
        if (!ordenacao.equals("")) {
            selectStr = selectStr + " ORDER BY " + ordenacao;
        }
        return selectStr;
    }

    public void inicializarOrdenacoesRelatorio() {
        Vector ordenacao = this.getOrdenacoesRelatorio();
        ordenacao.add("Unidade de Ensino");
        ordenacao.add("Curso");
        ordenacao.add("Situação");
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }
    
    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("ProcessoMatriculaRel");
    }
}
