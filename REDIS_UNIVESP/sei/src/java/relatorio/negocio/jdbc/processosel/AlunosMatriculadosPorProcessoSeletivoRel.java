package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.AlunosMatriculadosPorProcessoSeletivoRelVO;
import relatorio.negocio.interfaces.processosel.AlunosMatriculadosPorProcessoSeletivoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AlunosMatriculadosPorProcessoSeletivoRel extends SuperRelatorio implements AlunosMatriculadosPorProcessoSeletivoRelInterfaceFacade {

    @Override
    public List<AlunosMatriculadosPorProcessoSeletivoRelVO> criarObjeto(String tipoRelatorio, Integer codigoProcessoSeletivo, Integer codigoCurso) throws Exception {
        List<AlunosMatriculadosPorProcessoSeletivoRelVO> alunosMatriculadosAnaliticoRelVOs = new ArrayList<AlunosMatriculadosPorProcessoSeletivoRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(tipoRelatorio, codigoProcessoSeletivo, codigoCurso);
        while (dadosSQL.next()) {
            alunosMatriculadosAnaliticoRelVOs.add(montarDados(dadosSQL, tipoRelatorio, codigoCurso));
        }
        return alunosMatriculadosAnaliticoRelVOs;
    }

    private AlunosMatriculadosPorProcessoSeletivoRelVO montarDados(SqlRowSet dadosSQL, String tipoRelatorio, Integer codigoCurso) {
        AlunosMatriculadosPorProcessoSeletivoRelVO alunosMatriculadosAnaliticoRelVO = new AlunosMatriculadosPorProcessoSeletivoRelVO();
        alunosMatriculadosAnaliticoRelVO.setTipoRelatorio(tipoRelatorio);
        if (tipoRelatorio.equals("aprovados")) {
            alunosMatriculadosAnaliticoRelVO.setTipoRelatorioExtenso("Relatório de Vestibulandos Aprovados");
        } else if (tipoRelatorio.equals("reprovados")) {
            alunosMatriculadosAnaliticoRelVO.setTipoRelatorioExtenso("Relatório de Vestibulandos Reprovados");
        } else if (tipoRelatorio.equals("aprovadosMatriculados")) {
            alunosMatriculadosAnaliticoRelVO.setTipoRelatorioExtenso("Relatório de Vestibulandos Aprovados e Matriculados");
        } else if (tipoRelatorio.equals("aprovadosNaoMatriculados")) {
            alunosMatriculadosAnaliticoRelVO.setTipoRelatorioExtenso("Relatório de Vestibulandos Aprovados e Não Matriculados");
        }
        alunosMatriculadosAnaliticoRelVO.setCodigoInscricao(dadosSQL.getInt("codigoInscricao"));
        if (tipoRelatorio.equals("aprovadosMatriculados") || tipoRelatorio.equals("aprovadosNaoMatriculados")) {
            alunosMatriculadosAnaliticoRelVO.setDataMatricula(Uteis.getDataJDBC(dadosSQL.getDate("dataMatricula")));
        } else {
            alunosMatriculadosAnaliticoRelVO.setDataProva(Uteis.getDataJDBC(dadosSQL.getDate("dataProva")));
        }
        alunosMatriculadosAnaliticoRelVO.setDescricaoProcSeletivo(dadosSQL.getString("descricaoProcSeletivo"));
        alunosMatriculadosAnaliticoRelVO.setEmail(dadosSQL.getString("email"));
        // if (codigoCurso != 0) {
        alunosMatriculadosAnaliticoRelVO.setNomeCurso(dadosSQL.getString("nomeCurso") + " - " + dadosSQL.getString("nomeTurno"));
        alunosMatriculadosAnaliticoRelVO.setNomeTurno(dadosSQL.getString("nomeTurno"));
        // }
        alunosMatriculadosAnaliticoRelVO.setNomePessoa(dadosSQL.getString("nomePessoa"));
        alunosMatriculadosAnaliticoRelVO.setNomeUnidadeEnsino(dadosSQL.getString("nomeUnidadeEnsino"));
        alunosMatriculadosAnaliticoRelVO.setTelefone(dadosSQL.getString("telefone"));
        String matricula = dadosSQL.getString("matricula");

        if (matricula != null && !matricula.equals("")) {
            alunosMatriculadosAnaliticoRelVO.setMatricula(matricula);
            alunosMatriculadosAnaliticoRelVO.setMatriculado(true);
        } else {
            alunosMatriculadosAnaliticoRelVO.setMatricula("");
            alunosMatriculadosAnaliticoRelVO.setMatriculado(false);
        }
        return alunosMatriculadosAnaliticoRelVO;
    }

    public SqlRowSet executarConsultaParametrizada(String tipoRelatorio, Integer codigoProcessoSeletivo, Integer codigoCurso) {
        StringBuilder sqlStr = new StringBuilder("");

        sqlStr.append("SELECT I.CODIGO AS codigoInscricao, P.NOME AS nomePessoa, P.RG AS rg, P.CPF AS cpf, P.TELEFONERES AS telefone, P.EMAIL AS email, ");
        sqlStr.append("PS.DESCRICAO AS descricaoProcSeletivo, UE.NOME AS nomeUnidadeEnsino, m.matricula, ");
        if (tipoRelatorio.equals("aprovadosMatriculados") || tipoRelatorio.equals("aprovadosNaoMatriculados")) {
            sqlStr.append("M.DATA AS dataMatricula, ");
        } else {
            sqlStr.append("I.DATA AS dataProva, ");
        }
        if (!tipoRelatorio.equals("reprovados")) {
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'AP' THEN C1.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'AP') THEN C2.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'AP') THEN C3.NOME END) AS nomeCurso, ");
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'AP' THEN TN1.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'AP') THEN TN2.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'AP') THEN TN3.NOME END) AS nomeTurno, ");
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'AP' THEN C1.codigo WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'AP') ");
            sqlStr.append("THEN C2.codigo WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'AP') THEN C3.codigo END) AS codigoCurso, ");
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'AP' THEN TN1.codigo WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'AP') ");
            sqlStr.append("THEN TN2.codigo WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'AP') THEN TN3.codigo END) AS codigoTurno ");

        } else {
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'RE' THEN C1.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'RE') THEN C2.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'RE') THEN C3.NOME END) AS nomeCurso, ");
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'RE' THEN TN1.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'RE') THEN TN2.NOME ");
            sqlStr.append("WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'RE') THEN TN3.NOME END) AS nomeTurno, ");
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'RE' THEN C1.codigo WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'RE') ");
            sqlStr.append("THEN C2.codigo WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'RE') THEN C3.codigo END) AS codigoCurso, ");
            sqlStr.append("(CASE WHEN RPS.RESULTADOPRIMEIRAOPCAO = 'RE' THEN TN1.codigo WHEN (RPS.RESULTADOSEGUNDAOPCAO = 'RE') ");
            sqlStr.append("THEN TN2.codigo WHEN (RPS.RESULTADOTERCEIRAOPCAO = 'RE') THEN TN3.codigo END) AS codigoTurno ");
        }
        sqlStr.append("FROM PESSOA P ");
        sqlStr.append("INNER JOIN INSCRICAO I ON P.CODIGO = I.CANDIDATO ");
        sqlStr.append("INNER JOIN PROCSELETIVO PS ON I.PROCSELETIVO = PS.CODIGO ");
        sqlStr.append("LEFT JOIN MATRICULA M ON P.CODIGO = M.ALUNO ");
        sqlStr.append("INNER JOIN RESULTADOPROCESSOSELETIVO RPS ON I.CODIGO = RPS.INSCRICAO ");
        sqlStr.append("INNER JOIN UNIDADEENSINO UE ON I.UNIDADEENSINO = UE.CODIGO ");
        sqlStr.append("LEFT JOIN UNIDADEENSINOCURSO UEC1 ON I.CURSOOPCAO1 = UEC1.CODIGO ");
        sqlStr.append("LEFT JOIN UNIDADEENSINOCURSO UEC2 ON I.CURSOOPCAO2 = UEC2.CODIGO ");
        sqlStr.append("LEFT JOIN UNIDADEENSINOCURSO UEC3 ON I.CURSOOPCAO3 = UEC3.CODIGO ");
        sqlStr.append("LEFT JOIN CURSO C1 ON UEC1.CURSO = C1.CODIGO ");
        sqlStr.append("LEFT JOIN CURSO C2 ON UEC2.CURSO = C2.CODIGO ");
        sqlStr.append("LEFT JOIN CURSO C3 ON UEC3.CURSO = C3.CODIGO ");
        sqlStr.append("LEFT JOIN TURNO TN1 ON UEC1.TURNO = TN1.CODIGO ");
        sqlStr.append("LEFT JOIN TURNO TN2 ON UEC2.TURNO = TN2.CODIGO ");
        sqlStr.append("LEFT JOIN TURNO TN3 ON UEC3.TURNO = TN3.CODIGO ");
        sqlStr.append("WHERE PS.CODIGO = " + codigoProcessoSeletivo + " ");
        if (codigoCurso != 0) {
            sqlStr.append("AND (C1.CODIGO = " + codigoCurso + " OR C2.CODIGO = " + codigoCurso + " OR C3.CODIGO = " + codigoCurso + ") ");
        }
        if (tipoRelatorio.equals("aprovadosMatriculados")) {
            sqlStr.append("AND M.SITUACAOFINANCEIRA = 'QU' ");
        }
        if (tipoRelatorio.equals("aprovadosNaoMatriculados")) {
            sqlStr.append("AND M.SITUACAOFINANCEIRA = 'PF' ");
        }
        if (!tipoRelatorio.equals("reprovados")) {
            sqlStr.append("AND (RPS.RESULTADOTERCEIRAOPCAO = 'AP' OR RPS.RESULTADOSEGUNDAOPCAO = 'AP' OR RPS.RESULTADOPRIMEIRAOPCAO = 'AP') ");
        } else {
            sqlStr.append("AND (RPS.RESULTADOTERCEIRAOPCAO = 'RE' AND RPS.RESULTADOSEGUNDAOPCAO = 'RE' AND RPS.RESULTADOPRIMEIRAOPCAO = 'RE') ");
        }
        sqlStr.append(" group by ps.codigo, codigoCurso, codigoTurno, nomeCurso, nomeTurno, ");
        sqlStr.append("I.CODIGO, P.NOME, P.RG, P.CPF, P.TELEFONERES, P.EMAIL, PS.DESCRICAO, UE.NOME, I.DATA, m.matricula, m.data ");
        sqlStr.append("order by ps.codigo, codigoCurso, codigoTurno, nomeCurso, nomeTurno ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado;
    }

    public void validarDados(UnidadeEnsinoCursoVO unidadeEnsinoCurso, String tipoRelatorio) throws Exception {
        if ((unidadeEnsinoCurso == null) ||
                    (unidadeEnsinoCurso.getUnidadeEnsino() == 0)) {
            throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosAnalistoRel_unidadeEnsino"));
        }
        if (tipoRelatorio.equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosAnalistoRel_tipoRelatorio"));
        }
    }

    @Override
    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
    }

    @Override
    public String caminhoBaseIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel");
    }

    public static String getIdEntidade() {
        return ("AlunosMatriculadosPorProcessoSeletivoRel");
    }

}