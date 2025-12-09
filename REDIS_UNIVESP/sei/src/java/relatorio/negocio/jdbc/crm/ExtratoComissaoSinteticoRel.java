package relatorio.negocio.jdbc.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.RankingTurmaVO;
import negocio.comuns.crm.RankingVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.crm.ExtratoComissaoRelVO;
import relatorio.negocio.comuns.crm.ExtratoComissaoSinteticoRelVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import relatorio.negocio.interfaces.crm.ExtratoComissaoSinteticoRelInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class ExtratoComissaoSinteticoRel extends SuperRelatorio implements ExtratoComissaoSinteticoRelInterfaceFacade {

    public ExtratoComissaoSinteticoRel() {
    }

    public void validarDados(Integer unidadeEnsino) throws Exception {
        if (unidadeEnsino.equals(0)) {
            throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
        }
    }

    public List<ExtratoComissaoRelVO> criarObjeto(Integer unidadeEnsino, Integer consultorPessoa, Integer turma, String tipoRelatorio, String valorConsultaMes, String valorOrdenarPor, UsuarioVO usuarioVO) throws Exception {
        validarDados(unidadeEnsino);
        if (tipoRelatorio.equals("sintetico")) {
            return executarCriacaoRelatorioSintetico(unidadeEnsino, consultorPessoa, turma, valorConsultaMes, valorOrdenarPor, usuarioVO);
        } else {
            return executarCriacaoRelatorioAnalitico(unidadeEnsino, consultorPessoa, turma, valorConsultaMes, valorOrdenarPor, usuarioVO);
        }
    }

    public List<ExtratoComissaoRelVO> executarCriacaoRelatorioSintetico(Integer unidadeEnsino, Integer consultorPessoa, Integer turma, String valorConsultaMes, String valorOrdenarPor, UsuarioVO usuarioVO) throws Exception {
        List<RankingTurmaVO> listaRankingTurmaVO = new ArrayList(0);
        List<RankingVO> listaRankingVOs = null;
        List<ExtratoComissaoSinteticoRelVO> listaExtratoRelVOs = consultarConsultorPorCodigo(unidadeEnsino, consultorPessoa, turma, valorConsultaMes, usuarioVO);
        HashMap<String, List<RankingVO>> mapRanking = new HashMap<String, List<RankingVO>>(0);
        listaRankingTurmaVO = consultarRankingTurmaPorConsultorTurmaUnidadeEnsino(unidadeEnsino, consultorPessoa, turma, valorConsultaMes, usuarioVO);
        if (listaRankingTurmaVO.isEmpty()) {
            return new ArrayList(0);
        }
        for (RankingTurmaVO rankingTurmaVO : listaRankingTurmaVO) {
            listaRankingVOs = getFacadeFactory().getRankingFacade().consultarRanking(rankingTurmaVO, valorConsultaMes, usuarioVO);
            mapRanking.put(rankingTurmaVO.getTurmaVO().getIdentificadorTurma(), listaRankingVOs);
        }
        return executarCalculoValorComissaoConsultorSintetico(listaExtratoRelVOs, mapRanking, valorOrdenarPor);
    }

    public List<ExtratoComissaoRelVO> executarCalculoValorComissaoConsultorSintetico(List<ExtratoComissaoSinteticoRelVO> listaExtratoRelVOs, HashMap<String, List<RankingVO>> mapRanking, String valorOrdenarPor) {
        List<RankingVO> listaRanking = new ArrayList<RankingVO>(0);
        List<ExtratoComissaoRelVO> listaExtratoComissaoVOs = new ArrayList(0);
        HashMap<Integer, ExtratoComissaoRelVO> mapFunc = new HashMap<Integer, ExtratoComissaoRelVO>();
        for (ExtratoComissaoSinteticoRelVO extratoComissaoVO : listaExtratoRelVOs) {
            listaRanking = mapRanking.get(extratoComissaoVO.getTurmaVO().getIdentificadorTurma());
            for (RankingVO rankingVO : listaRanking) {
                if (extratoComissaoVO.getFuncionarioVO().getPessoa().getCodigo().equals(rankingVO.getConsultor().getPessoa().getCodigo())) {
                    if (mapFunc.containsKey(rankingVO.getConsultor().getPessoa().getCodigo())) {
                        ExtratoComissaoRelVO obj = mapFunc.get(rankingVO.getConsultor().getPessoa().getCodigo());
                        obj.setValorComissao(obj.getValorComissao() + rankingVO.getValor());
                        obj.setNomeConsultor(rankingVO.getConsultor().getPessoa().getNome());
                        mapFunc.put(rankingVO.getConsultor().getPessoa().getCodigo(), obj);
                    } else {
                        ExtratoComissaoRelVO obj = new ExtratoComissaoRelVO();
                        obj.setFuncionarioVO(rankingVO.getConsultor());
                        obj.setValorComissao(rankingVO.getValor());
                        obj.setNomeConsultor(rankingVO.getConsultor().getPessoa().getNome());
                        mapFunc.put(rankingVO.getConsultor().getPessoa().getCodigo(), obj);
                    }
                }
            }
        }
        for (Map.Entry<Integer, ExtratoComissaoRelVO> entry : mapFunc.entrySet()) {
            listaExtratoComissaoVOs.add(entry.getValue());
        }
        if (valorOrdenarPor.equals("nome")) {
            Ordenacao.ordenarLista(listaExtratoComissaoVOs, "nomeConsultor");
        } else {
            Ordenacao.ordenarLista(listaExtratoComissaoVOs, "valorComissao");
        }
        return listaExtratoComissaoVOs;
    }

    public List<ExtratoComissaoRelVO> executarCriacaoRelatorioAnalitico(Integer unidadeEnsino, Integer consultorPessoa, Integer turma, String valorConsultaMes, String valorOrdenarPor, UsuarioVO usuarioVO) throws Exception {
        List<RankingTurmaVO> listaRankingTurmaVO = new ArrayList(0);
        List<RankingVO> listaRankingVOs = null;
        List<ExtratoComissaoRelVO> listaExtratoComissaoVOs = new ArrayList(0);
        listaRankingTurmaVO = consultarRankingTurmaPorConsultorTurmaUnidadeEnsino(unidadeEnsino, consultorPessoa, turma, valorConsultaMes, usuarioVO);
        if (listaRankingTurmaVO.isEmpty()) {
            return new ArrayList(0);
        }
        ExtratoComissaoRelVO obj = new ExtratoComissaoRelVO();
        for (RankingTurmaVO rankingTurmaVO : listaRankingTurmaVO) {
            listaRankingVOs = getFacadeFactory().getRankingFacade().consultarRanking(rankingTurmaVO, valorConsultaMes, usuarioVO);
            obj.getListaRankingVOs().addAll(listaRankingVOs);
        }
        listaExtratoComissaoVOs.add(obj);
        return listaExtratoComissaoVOs;
    }

    public List<ExtratoComissaoSinteticoRelVO> consultarConsultorPorCodigo(Integer unidadeEnsino, Integer consultorPessoa, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT funcionario.codigo AS \"funcionario.codigo\",  pessoa.nome, pessoa.codigo AS \"pessoa.codigo\", ");
        sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\" ");
        sb.append(" FROM comissionamentoTurma ");
        sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking  ");
        sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma  ");
        sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
        sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula  ");
        sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor  ");
        sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
        sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sb.append(" WHERE EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
        if (!consultorPessoa.equals(0)) {
            sb.append(" AND pessoa.codigo = ").append(consultorPessoa);
        }
        if (!turma.equals(0)) {
            sb.append(" AND turma.codigo = ").append(turma.intValue());
        }
        sb.append(" AND matricula.data >= comissionamentoTurma.dataPrimeiroPagamento and matricula.data <= comissionamentoTurma.dataUltimoPagamento ");
        sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
        sb.append(" ORDER BY pessoa.nome, pessoa.codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ExtratoComissaoSinteticoRelVO obj = new ExtratoComissaoSinteticoRelVO();
            obj.getFuncionarioVO().setCodigo(tabelaResultado.getInt("funcionario.codigo"));
            obj.getFuncionarioVO().getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
            obj.getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("nome"));
            obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
            obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorTurma"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<RankingTurmaVO> consultarRankingTurmaPorConsultorTurmaUnidadeEnsino(Integer unidadeEnsino, Integer consultorPessoa, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT turma.codigo, turma.identificadorTurma, ");
        sb.append(" (SELECT count(matriculaperiodo.matricula) from matriculaperiodo  ");
        sb.append(" INNER JOIN matricula on matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" INNER JOIN comissionamentoTurma on comissionamentoTurma.turma = matriculaperiodo.turma ");
        sb.append(" where matriculaperiodo.turma = turma.codigo ");
        sb.append(" AND matriculaperiodo.situacaoMatriculaperiodo = 'AT' and matriculaperiodo.bolsista = false ");
        sb.append(" AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ");
        sb.append(" AND EXTRACT(MONTH FROM matricula.data) <= ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
        sb.append(" ) AS qtdeAlunoMatriculado FROM turma ");
        sb.append(" INNER JOIN comissionamentoTurma ON comissionamentoTurma.turma = turma.codigo ");
        sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
        sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
        sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
        sb.append(" WHERE 1=1 ");
        if (!unidadeEnsino.equals(0)) {
            sb.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
        }
        if (!turma.equals(0)) {
            sb.append(" AND turma.codigo = ").append(turma);
        }
        if (!consultorPessoa.equals(0)) {
            sb.append(" AND funcionario.pessoa = ").append(consultorPessoa);
        }
        sb.append(" AND EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
        sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
        sb.append(" ORDER BY turma.identificadorTurma ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List vetResultado = new ArrayList(0);
        RankingTurmaVO obj = null;
        while (tabelaResultado.next()) {
            obj = new RankingTurmaVO();
            obj.getTurmaVO().setCodigo(tabelaResultado.getInt("codigo"));
            obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
            obj.setQtdeAluno(tabelaResultado.getInt("qtdeAlunoMatriculado"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
        if (campoConsulta.equals("identificadorTurma")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(valorConsulta, false, usuarioVO);
        }
        return new ArrayList(0);
    }

    public static String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator);
    }

    public static String getDesignIReportRelatorio(String tipoRelatorio) {
        if (tipoRelatorio.equals("sintetico")) {
            return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
        } else {
            return (caminhoBaseRelatorio() + getIdEntidadeAnalitico() + ".jrxml");
        }
    }

    public static String getIdEntidade() {
        return ("ExtratoComissaoSinteticoRel");
    }

    public static String getIdEntidadeAnalitico() {
        return ("ExtratoComissaoAnaliticoRel");
    }
    public static String getDesignIReportRelatorioExcel(String tipoRelatorio) {
        if (tipoRelatorio.equals("sintetico")) {
            return (caminhoBaseRelatorio() + getIdEntidadeExcelSintetico() + ".jrxml");
        } else {
            return (caminhoBaseRelatorio() + getIdEntidadeExcelAnalitico() + ".jrxml");
        }
    }

    public static String getIdEntidadeExcelSintetico() {
        return ("ExtratoComissaoSinteticoRelExcel");
    }

    public static String getIdEntidadeExcelAnalitico() {
        return ("ExtratoComissaoAnaliticoRelExcel");
    }
}
