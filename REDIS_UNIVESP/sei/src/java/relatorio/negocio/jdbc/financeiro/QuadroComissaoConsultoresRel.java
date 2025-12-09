package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.FuncionarioVO;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.PrevisaoFaturamentoRelVO;
import relatorio.negocio.comuns.financeiro.QuadroComissaoConsultoresRelVO;
import relatorio.negocio.interfaces.financeiro.QuadroComissaoConsultoresRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class QuadroComissaoConsultoresRel extends SuperRelatorio implements QuadroComissaoConsultoresRelInterfaceFacade {

    public void validarDados(Integer unidadeEsnino) throws Exception {
        if (unidadeEsnino == null || unidadeEsnino == 0) {
            throw new Exception("O campo Unidade de Ensino deve ser informado.");
        }

    }

    public List<QuadroComissaoConsultoresRelVO> criarObjeto(Integer codigoUnidadeEnsino, Integer curso, Integer turma, Boolean situacaoPreMatricula, Boolean matRecebida, Boolean matAReceber, UsuarioVO usuarioLogado) throws Exception {

        List<QuadroComissaoConsultoresRelVO> quadroComissaoConsultoresRelVOs = new ArrayList<QuadroComissaoConsultoresRelVO>(0);
        try {
            SqlRowSet tabelaResultado = executarConsultaParametrizada(codigoUnidadeEnsino, curso, turma, situacaoPreMatricula, matRecebida, matAReceber, usuarioLogado);
            while (tabelaResultado.next()) {
                quadroComissaoConsultoresRelVOs.add(montarDados(tabelaResultado, codigoUnidadeEnsino, situacaoPreMatricula, matRecebida, matAReceber, usuarioLogado));
            }
            return quadroComissaoConsultoresRelVOs;
        } finally {
        }
    }

    public SqlRowSet executarConsultaParametrizada(Integer codigoUnidadeEnsino, Integer curso, Integer turma, Boolean situacaoPreMatricula, Boolean matRecebida, Boolean matAReceber, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder selectStr = new StringBuilder("select true as tipoConsultor, '' as consultor,identificadorturma, SUM(totalMatriculados) AS totalMatriculados, SUM(totalNaoAtivos) AS totalNaoAtivos, SUM(totalBolsista) AS totalBolsista, ");
        selectStr.append(" ( SUM(totalMatriculados)  - SUM(totalNaoAtivos) - SUM(totalBolsista)) AS totalPagantes ");
        selectStr.append(" FROM( ");
        selectStr.append(" select turma.identificadorturma, count(matricula.matricula) AS totalMatriculados, ");
        selectStr.append(" (CASE WHEN(matricula.situacao <> 'AT') then count(matricula.matricula) else 0 end ) AS totalNaoAtivos, ");
        selectStr.append(" (CASE WHEN(matriculaPeriodo.bolsista = true and matricula.situacao = 'AT') then count(matricula.matricula) else 0 end ) AS totalBolsista  ");
        selectStr.append(" from matricula ");
        selectStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        selectStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
        if (matRecebida || matAReceber) {
        	selectStr.append(" inner join matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
        }
        selectStr.append(" where matricula.unidadeensino = ");
        selectStr.append(codigoUnidadeEnsino);
        if (matRecebida || matAReceber) {
        	//AND (contareceber.parcela like ('1/%') or contareceber.parcela like ('01%') 
        	//selectStr.append(" and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') and matriculaperiodovencimento.situacao in (''");
        	selectStr.append(" and (matriculaperiodovencimento.parcela like '1/%' or matriculaperiodovencimento.parcela like '01%') and matriculaperiodovencimento.situacao in (''");
        }
        if (matRecebida) {
        	selectStr.append(" ,'GP'");
        }
        if (matAReceber) {
        	selectStr.append(" ,'GE'");
        }
        if (matRecebida || matAReceber) {
        	selectStr.append(" )");
        }
        
        if (curso != null && curso != 0) {
            selectStr.append(" AND matricula.curso = ");
            selectStr.append(curso);
        }
        if (turma != null && turma != 0) {
            selectStr.append(" AND turma.codigo = ");
            selectStr.append(turma);
        }        
        if (!situacaoPreMatricula) {
        	selectStr.append(" and matriculaperiodo.situacaomatriculaperiodo <> 'PR' ");
        }               
        selectStr.append(" GROUP BY turma.identificadorturma, matricula.situacao, matriculaPeriodo.bolsista");
        selectStr.append(" ORDER BY turma.identificadorTurma ) AS t GROUP BY identificadorturma");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        ////System.out.print(selectStr.toString());
        return tabelaResultado;
    }

    private QuadroComissaoConsultoresRelVO montarDados(SqlRowSet dadosSQL, Integer unidadeEnsino, Boolean situacaoPreMatricula, Boolean matRecebida, Boolean matAReceber, UsuarioVO usuarioLogado) throws Exception {
        QuadroComissaoConsultoresRelVO quadroComissaoConsultoresRelVO = new QuadroComissaoConsultoresRelVO();
        quadroComissaoConsultoresRelVO.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
        quadroComissaoConsultoresRelVO.setTipoConsultor(dadosSQL.getBoolean("tipoconsultor"));
        quadroComissaoConsultoresRelVO.setNomeConsultor(dadosSQL.getString("consultor"));
        quadroComissaoConsultoresRelVO.setTotalMatriculados(dadosSQL.getInt("totalMatriculados"));
        quadroComissaoConsultoresRelVO.setTotalNaoAtivos(dadosSQL.getInt("totalNaoAtivos"));
        quadroComissaoConsultoresRelVO.setTotalBolsista(dadosSQL.getInt("totalBolsista"));
        quadroComissaoConsultoresRelVO.setTotalPagantes(dadosSQL.getInt("totalPagantes"));

        quadroComissaoConsultoresRelVO.setListaConsultor(consultarConsultoresTurma(unidadeEnsino, quadroComissaoConsultoresRelVO.getTurmaVO().getIdentificadorTurma(), situacaoPreMatricula, matRecebida, matAReceber));
        return quadroComissaoConsultoresRelVO;
    }

    public List<FuncionarioVO> consultarConsultoresTurma(Integer unidadeEsnino, String identificadorTurma, Boolean situacaoPreMatricula, Boolean matRecebida, Boolean matAReceber) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append("select pessoa.nome, count(matricula.matricula)as qtdeAlunoVinculadoConsultor from matricula ");
        selectStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        selectStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
        selectStr.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
        selectStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
        if (matRecebida || matAReceber) {
        	selectStr.append(" inner join matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
        }
        selectStr.append(" where matricula.situacao = 'AT' and matriculaPeriodo.bolsista <> true AND matricula.unidadeensino = ");
        selectStr.append(unidadeEsnino);
        selectStr.append(" and turma.identificadorturma = '");
        selectStr.append(identificadorTurma);        
        selectStr.append("' ");
        if (matRecebida || matAReceber) {
        	selectStr.append(" and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') and matriculaperiodovencimento.situacao in (''");        	
        }
        if (matRecebida) {
        	selectStr.append(" ,'GP'");
        }
        if (matAReceber) {
        	selectStr.append(" ,'GE'");
        }
        if (matRecebida || matAReceber) {
        	selectStr.append(" )");
        }        
        if (!situacaoPreMatricula) {
        	selectStr.append(" and matriculaperiodo.situacaomatriculaperiodo <> 'PR' ");
        }               
        selectStr.append("GROUP BY pessoa.nome ORDER BY qtdeAlunoVinculadoConsultor, pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        ////System.out.print(selectStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<FuncionarioVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<FuncionarioVO> vetResultado = new ArrayList<FuncionarioVO>(0);
        while (tabelaResultado.next()) {
            FuncionarioVO obj = new FuncionarioVO();
            montarDadosBasicoConsultor(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosBasicoConsultor(FuncionarioVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do Funcionário
        obj.getPessoa().setNome(dadosSQL.getString("nome"));
        obj.setQtdeAlunoVinculadosConsultor(dadosSQL.getInt("qtdeAlunoVinculadoConsultor"));
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public static String getCaminhoBaseDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return ("QuadroComissaoConsultoresRel");
    }

    public static String getIdEntidadeExcel() {
        return ("QuadroComissaoConsultoresExcelRel");
    }
}
