package relatorio.negocio.jdbc.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;
import negocio.comuns.crm.RankingTurmaVO;
import negocio.comuns.crm.RankingVO;
import negocio.comuns.utilitarias.Extenso;

import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


import relatorio.negocio.comuns.crm.ReciboComissoesRelVO;
import relatorio.negocio.comuns.crm.ReciboComissoesTurmaRelVO;
import relatorio.negocio.interfaces.crm.ReciboComissoesRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ReciboComissoesRel extends SuperRelatorio implements ReciboComissoesRelInterfaceFacade {

    public ReciboComissoesRel() {
        inicializarOrdenacoesRelatorio();
    }

    public List<ReciboComissoesRelVO> criarObjeto(String valorConsultaMes, Integer funcionario, Integer cargo, UsuarioVO usuarioLogado) throws Exception {

        List<ReciboComissoesRelVO> reciboComissoesRelVOs = new ArrayList<ReciboComissoesRelVO>(0);
        List<RankingTurmaVO> listaRankingTurmaVO = consultarRankingTurma(valorConsultaMes, funcionario, cargo, usuarioLogado);
        for (RankingTurmaVO rankingTurmaVO : listaRankingTurmaVO) {
            List<RankingVO> listaRankingVO = consultarRanking(rankingTurmaVO, valorConsultaMes, usuarioLogado);
            for (RankingVO rankingVO : listaRankingVO) {
                ReciboComissoesRelVO obj = new ReciboComissoesRelVO();
                if (rankingVO.getValor() != 0) {
                    if (!reciboComissoesRelVOs.isEmpty()) {
                        for (int i = 0; i < reciboComissoesRelVOs.size(); i++) {
                            ReciboComissoesRelVO objExistente = reciboComissoesRelVOs.get(i);
                            if (rankingVO.getConsultor().getPessoa().getCodigo().equals(objExistente.getCodigo())) {
                                obj = montarDadosReciboComissoes(rankingVO);
                                reciboComissoesRelVOs.remove(objExistente);
                                obj.getReciboComissoesTurmaRelVOs().addAll(objExistente.getReciboComissoesTurmaRelVOs());
                                break;
                            }
                            if (i == reciboComissoesRelVOs.size() - 1) {
                                obj = montarDadosReciboComissoes(rankingVO);
                            }
                        }
                    } else {
                        obj = montarDadosReciboComissoes(rankingVO);
                    }
                    if (obj.getCodigo() != 0) {
                        reciboComissoesRelVOs.add(obj);
                    }
                }

            }
        }
        return reciboComissoesRelVOs;
    }

    public List<RankingTurmaVO> consultarRankingTurma(String valorConsultaMes, Integer funcionario, Integer cargo, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT turma.codigo, turma.identificadorTurma, ");
        sb.append(" (SELECT count(matriculaperiodo.matricula) from matriculaperiodo  ");
        sb.append(" INNER JOIN matricula on matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" INNER JOIN comissionamentoTurma on comissionamentoTurma.turma = matriculaperiodo.turma ");
        sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
        sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
        sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sb.append(" where matriculaperiodo.turma = turma.codigo ");
        sb.append(" AND matriculaperiodo.situacaoMatriculaperiodo = 'AT' and matriculaperiodo.bolsista = false ");
        sb.append(" AND EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
        sb.append(" AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ");
        if (!funcionario.equals(0)) {
            sb.append(" AND pessoa.codigo = ").append(funcionario).append(" ");
        }
        if (!cargo.equals(0)) {
            sb.append(" AND funcionariocargo.cargo = ").append(cargo).append(" ");
        }
        sb.append(" ) AS qtdeAlunoMatriculado FROM turma ");
        sb.append(" WHERE (SELECT count(matriculaperiodo.matricula) ");
        sb.append("from matriculaperiodo   ");
        sb.append("INNER JOIN matricula on matricula.matricula = matriculaperiodo.matricula ");
        sb.append("INNER JOIN comissionamentoTurma on comissionamentoTurma.turma = matriculaperiodo.turma ");
        sb.append("INNER JOIN funcionario ON funcionario.codigo = matricula.consultor  ");
        sb.append("INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo  ");
        sb.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
        sb.append("WHERE matriculaperiodo.turma = turma.codigo  ");
        sb.append("AND matriculaperiodo.situacaoMatriculaperiodo = 'AT' ");
        sb.append("AND matriculaperiodo.bolsista = false  ");
        sb.append("AND EXTRACT(MONTH FROM matricula.data) = 5 AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO')) <> 0 ");
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

    public List<RankingVO> consultarRanking(RankingTurmaVO obj, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception {
        Double valorComissao = 0.0;
        Integer qtdeConsultor = 0;
        List<RankingVO> listaRankingVO = new ArrayList(0);
        List<PercentualConfiguracaoRankingVO> listaPercentualRankingVOs = new ArrayList(0);
        listaPercentualRankingVOs = getFacadeFactory().getPercentualConfiguracaoRankingFacade().consultarPorTurmaComissionamentoTurma(obj.getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        listaRankingVO = consultarRankingConsultorPorTurmaMes(obj.getTurmaVO().getCodigo(), valorConsultaMes, usuarioVO);
        valorComissao = getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().consultarValorComissionamentoPorTurma(obj.getTurmaVO().getCodigo(), obj.getQtdeAluno(), usuarioVO);
        qtdeConsultor = consultarQtdeConsultorPorTurmaMes(obj.getTurmaVO().getCodigo(), valorConsultaMes, usuarioVO);
        qtdeConsultor = qtdeConsultor + 1;
        executarCalculoPosicaoValorRanking(valorComissao, qtdeConsultor, listaRankingVO, listaPercentualRankingVOs);
        return listaRankingVO;
    }

    public void executarCalculoPosicaoValorRanking(Double valorComissao, Integer qtdeConsultor, List<RankingVO> listaRankingVOs, List<PercentualConfiguracaoRankingVO> listaPercenturalRankingVOs) throws Exception {
        Integer qtdeMatriculaTemp = 0;
        Integer posicaoTemp = 0;
        for (PercentualConfiguracaoRankingVO percentualConfiguracaoRankingVO : listaPercenturalRankingVOs) {
            Integer cont = 1;
            Integer qtdePosicao = percentualConfiguracaoRankingVO.getQtdePosicao();
            if (qtdePosicao.equals(0)) {
                for (RankingVO rankingVO : listaRankingVOs) {
                    if (!qtdeMatriculaTemp.equals(rankingVO.getQtdeMatriculado())) {
                        if (rankingVO.getPosicao().equals("")) {
                            rankingVO.setPosicao(percentualConfiguracaoRankingVO.getPosicao().toString() + "º");
                            rankingVO.setValor(executarCalculoValorComissao(valorComissao, qtdeConsultor, percentualConfiguracaoRankingVO.getPercentual(), rankingVO.getConfiguracaoRankingVO().getPercentualGerente(), rankingVO.getGerente()));
                            qtdeMatriculaTemp = rankingVO.getQtdeMatriculado();
                            posicaoTemp = percentualConfiguracaoRankingVO.getPosicao();
                            break;
                        }
                    } else {
                        if (rankingVO.getPosicao().equals("")) {
                            if (qtdeMatriculaTemp.equals(rankingVO.getQtdeMatriculado()) && !posicaoTemp.equals(percentualConfiguracaoRankingVO.getPosicao())) {
                                throw new Exception("Existem mais consultores para a mesma posição no Ranking, favor alterar a configuração do Ranking(" + rankingVO.getConfiguracaoRankingVO().getNome().toUpperCase() + ").");
                            }
                            rankingVO.setPosicao(percentualConfiguracaoRankingVO.getPosicao().toString() + "º");
                            rankingVO.setValor(executarCalculoValorComissao(valorComissao, qtdeConsultor, percentualConfiguracaoRankingVO.getPercentual(), rankingVO.getConfiguracaoRankingVO().getPercentualGerente(), rankingVO.getGerente()));
                            qtdeMatriculaTemp = rankingVO.getQtdeMatriculado();
                            posicaoTemp = percentualConfiguracaoRankingVO.getPosicao();
                            break;
                        }
                    }
                }
            } else {
                while (cont <= qtdePosicao) {
                    for (RankingVO rankingVO : listaRankingVOs) {
                        if (!qtdeMatriculaTemp.equals(rankingVO.getQtdeMatriculado())) {
                            if (rankingVO.getPosicao().equals("")) {
                                rankingVO.setPosicao(percentualConfiguracaoRankingVO.getPosicao().toString() + "º");
                                rankingVO.setValor(executarCalculoValorComissao(valorComissao, qtdeConsultor, percentualConfiguracaoRankingVO.getPercentual(), rankingVO.getConfiguracaoRankingVO().getPercentualGerente(), rankingVO.getGerente()));
                                qtdeMatriculaTemp = rankingVO.getQtdeMatriculado();
                                posicaoTemp = percentualConfiguracaoRankingVO.getPosicao();
                                break;
                            }
                        } else {
                            if (rankingVO.getPosicao().equals("")) {
                                if (qtdeMatriculaTemp.equals(rankingVO.getQtdeMatriculado()) && !posicaoTemp.equals(percentualConfiguracaoRankingVO.getPosicao())) {
                                    throw new Exception("Existem mais consultores para a mesma posição no Ranking, favor alterar a configuração do Ranking(" + rankingVO.getConfiguracaoRankingVO().getNome().toUpperCase() + ").");
                                }
                                rankingVO.setPosicao(percentualConfiguracaoRankingVO.getPosicao().toString() + "º");
                                rankingVO.setValor(executarCalculoValorComissao(valorComissao, qtdeConsultor, percentualConfiguracaoRankingVO.getPercentual(), rankingVO.getConfiguracaoRankingVO().getPercentualGerente(), rankingVO.getGerente()));
                                qtdeMatriculaTemp = rankingVO.getQtdeMatriculado();
                                posicaoTemp = percentualConfiguracaoRankingVO.getPosicao();
                                break;
                            }
                        }
                    }
                    cont++;
                }
            }
        }
    }

    public Double executarCalculoValorComissao(Double valorComissao, Integer qtdeConsultor, Double percentual, Double percentualGerente, Boolean gerente) throws Exception {
        Double valor = 0.0;
        Double valorTemp = 0.0;
        if (!valorComissao.equals(0.0)) {
            if (gerente) {
                if (percentualGerente.equals(0.0)) {
                    throw new Exception("Problema com a Configuração do Ranking, o campo PERCENTUAL GERENTE está zerado.");
                }
                String perc = percentual.toString().substring(percentual.toString().indexOf(".") + 1);
                String percGerente = percentualGerente.toString().substring(percentualGerente.toString().indexOf(".") + 1);
                Integer somaPerc = Integer.parseInt(perc) + Integer.parseInt(percGerente);
                Double percGerenteFinal = Double.parseDouble("1." + somaPerc);

                valorTemp = valorComissao / qtdeConsultor;
                valor = valorTemp * percGerenteFinal;
                return valor;
            } else {
                valorTemp = valorComissao / qtdeConsultor;
                valor = valorTemp * percentual;
                return valor;
            }
        }
        return 0.0;
    }

    public List<RankingVO> consultarRankingConsultorPorTurmaMes(Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT turma.identificadorTurma, turma.codigo AS \"turma.codigo\", pessoa.nome, pessoa.codigo AS \"pessoa.codigo\",  ");
        sb.append(" configuracaoRanking.codigo AS \"configuracaoRanking.codigo\", unidadeensino.razaosocial AS \"unidadeensino.razaosocial\", configuracaoRanking.nome AS \"configuracaoRanking.nome\", count(pessoa.codigo) AS qtdeMatriculado,  ");
        sb.append(" configuracaoRanking.percentualGerente AS \"configuracaoRanking.percentualGerente\", funcionariocargo.gerente ");
        sb.append(" FROM comissionamentoTurma  ");
        sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
        sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
        sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
        sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
        sb.append(" INNER JOIN unidadeensino ON configuracaoRanking.unidadeensino = unidadeensino.codigo ");
        sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sb.append(" WHERE turma.codigo = ").append(turma);
        sb.append(" AND EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
        sb.append(" AND matricula.data >= comissionamentoTurma.dataPrimeiroPagamento and matricula.data <= comissionamentoTurma.dataUltimoPagamento ");
        sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
        sb.append(" GROUP BY turma.identificadorTurma, pessoa.nome, pessoa.codigo, turma.codigo, configuracaoRanking.codigo, configuracaoRanking.nome, funcionariocargo.gerente, configuracaoRanking.percentualGerente,unidadeensino.razaosocial ");
        sb.append(" ORDER BY qtdeMatriculado DESC ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, usuarioVO);
    }

    public Integer consultarQtdeConsultorPorTurmaMes(Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(DISTINCT codigo) AS qtdeConsultor FROM (");
        sb.append(" SELECT DISTINCT (pessoa.codigo) ");
        sb.append(" FROM comissionamentoTurma  ");
        sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
        sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
        sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
        sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
        sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
        sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
        sb.append(" WHERE turma.codigo = ").append(turma);
        sb.append(" AND EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
        sb.append(" AND matricula.data >= comissionamentoTurma.dataPrimeiroPagamento and matricula.data <= comissionamentoTurma.dataUltimoPagamento ");
        sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
        sb.append(") AS t");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getInt("qtdeConsultor");
        }
        return 0;
    }

    public List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuarioVO));
        }
        return vetResultado;
    }

    public RankingVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
        RankingVO obj = new RankingVO();
        obj.getRankingTurmaVO().getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getRankingTurmaVO().getTurmaVO().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
        obj.getConsultor().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getConsultor().getPessoa().setNome(dadosSQL.getString("nome"));
        obj.getConfiguracaoRankingVO().setCodigo(dadosSQL.getInt("configuracaoRanking.codigo"));
        obj.getConfiguracaoRankingVO().setNome(dadosSQL.getString("configuracaoRanking.nome"));
        obj.getConfiguracaoRankingVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaosocial"));
        obj.getConfiguracaoRankingVO().setPercentualGerente(dadosSQL.getDouble("configuracaoRanking.percentualGerente"));
        obj.setGerente(dadosSQL.getBoolean("gerente"));
        obj.setQtdeMatriculado(dadosSQL.getInt("qtdeMatriculado"));
        return obj;
    }

    public ReciboComissoesRelVO montarDadosReciboComissoes(RankingVO rankingVO) {
        ReciboComissoesRelVO obj = new ReciboComissoesRelVO();
        obj.setCodigo(rankingVO.getConsultor().getPessoa().getCodigo());
        obj.setFuncionario(rankingVO.getConsultor().getPessoa().getNome());
        obj.setUnidadeEnsino(rankingVO.getConfiguracaoRankingVO().getUnidadeEnsino().getRazaoSocial());
        montarDadosReciboComissoesTurma(rankingVO, obj);
        return obj;
    }

    public ReciboComissoesTurmaRelVO montarDadosReciboComissoesTurma(RankingVO rankingVO, ReciboComissoesRelVO reciboComissoesRelVO) {
        ReciboComissoesTurmaRelVO obj = new ReciboComissoesTurmaRelVO();
        Extenso ext = new Extenso();
        obj.setNomeTurma(rankingVO.getRankingTurmaVO().getTurmaVO().getIdentificadorTurma());
        obj.setValorTurma(rankingVO.getValor());
        reciboComissoesRelVO.getReciboComissoesTurmaRelVOs().add(obj);
        return obj;
    }

    @Override
    public String designIReportRelatorio() {
        return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
    }

    @Override
    public String designIReportRelatorioExcel() {
        return (caminhoBaseRelatorio() + getIdEntidadeExcel() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator);
    }

    public static String getIdEntidade() {
        return "ReciboComissoesRel";
    }

    public static String getIdEntidadeExcel() {
        return "ReciboComissoesExcelRel";
    }

    public void inicializarOrdenacoesRelatorio() {
        Vector ordenacao = this.getOrdenacoesRelatorio();
        ordenacao.add("Nome");
        ordenacao.add("Data");
    }
}
