package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.FluxoCaixaRelVO;
import relatorio.negocio.interfaces.financeiro.FluxoCaixaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class FluxoCaixaRel extends SuperRelatorio implements FluxoCaixaRelInterfaceFacade {

    public FluxoCaixaRel() {
    }

    public List<FluxoCaixaRelVO> criarObjeto(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer contaCaixa, Integer usuario, String modeloRel, String tipoLayout) throws Exception {
        List<FluxoCaixaRelVO> fluxoCaixaRelVOs = new ArrayList<FluxoCaixaRelVO>(0);
        HashMap<Integer, Double> hashFluxoJaConsultados = new HashMap<Integer, Double>();
        SqlRowSet dadosSQL = executarConsultaParametrizada(dataInicio, dataFim, unidadeEnsino, contaCaixa, usuario, modeloRel, tipoLayout);
        while (dadosSQL.next()) {
            fluxoCaixaRelVOs.add(montarDados(dadosSQL, hashFluxoJaConsultados, dataInicio, dataFim, unidadeEnsino, usuario));
        }
        return fluxoCaixaRelVOs;
    }

    public List<FluxoCaixaRelVO> criarObjetoListaVazia(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer contaCaixa, Integer usuario) throws Exception {
        List<FluxoCaixaRelVO> fluxoCaixaRelVOs = new ArrayList<FluxoCaixaRelVO>(0);
        HashMap<Integer, Double> hashFluxoJaConsultados = new HashMap<Integer, Double>();
        SqlRowSet dadosSQL = executarConsultaParametrizadaListaVazia(dataInicio, dataFim, unidadeEnsino, contaCaixa, usuario);
        while (dadosSQL.next()) {
            fluxoCaixaRelVOs.add(montarDadosListaVazia(dadosSQL, hashFluxoJaConsultados, dataInicio, dataFim, unidadeEnsino, usuario));
        }
        return fluxoCaixaRelVOs;
    }

    private FluxoCaixaRelVO montarDados(SqlRowSet dadosSQL, HashMap<Integer, Double> hashFluxoJaConsultados, Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer usuario) throws Exception {
        FluxoCaixaRelVO fluxoCaixaRelVO = new FluxoCaixaRelVO();
        fluxoCaixaRelVO.setTipoMovimentacao(TipoMovimentacaoFinanceira.getDescricao(dadosSQL.getString("tipo")));
        fluxoCaixaRelVO.setTipoOrigem(TipoOrigemMovimentacaoCaixa.getDescricao(dadosSQL.getString("movimentacao")));
        if (fluxoCaixaRelVO.getTipoMovimentacao().equals("Saída") && fluxoCaixaRelVO.getTipoOrigem().equals("Recebimento")) {
            fluxoCaixaRelVO.setTipoOrigem("Recebimento");
            fluxoCaixaRelVO.setTipoMovimentacao("Estorno");
        }
        try {
            if (dadosSQL.getString("movimentacao").equals("RE")) {
                fluxoCaixaRelVO.setDescricao(getFacadeFactory().getNegociacaoRecebimentoFacade().consultarDescricaoPorCodigo(new Integer(dadosSQL.getInt("codigoOrigem"))));
            } else if (dadosSQL.getString("movimentacao").equals("MF")) {
                fluxoCaixaRelVO.setDescricao(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarDescricaoPorCodigo(new Integer(dadosSQL.getInt("codigoOrigem"))));
            }
        } catch (Exception e) {
            fluxoCaixaRelVO.setDescricao("");
        }
        fluxoCaixaRelVO.setNomeFavorecido(dadosSQL.getString("sacado"));
        if (fluxoCaixaRelVO.getNomeFavorecido() == null || fluxoCaixaRelVO.getNomeFavorecido().equals("")) {
            fluxoCaixaRelVO.setNomeFavorecido(dadosSQL.getString("parceiro"));
            if (fluxoCaixaRelVO.getNomeFavorecido() == null || fluxoCaixaRelVO.getNomeFavorecido().equals("")) {
                fluxoCaixaRelVO.setNomeFavorecido(dadosSQL.getString("fornecedor"));
            }else if (fluxoCaixaRelVO.getNomeFavorecido() == null || fluxoCaixaRelVO.getNomeFavorecido().equals("")) {
            	fluxoCaixaRelVO.setNomeFavorecido(dadosSQL.getString("sacadobanco"));
            }else if (fluxoCaixaRelVO.getNomeFavorecido() == null || fluxoCaixaRelVO.getNomeFavorecido().equals("")) {
            	fluxoCaixaRelVO.setNomeFavorecido(dadosSQL.getString("operadoracartao"));
            }
        }
        fluxoCaixaRelVO.setFormaPagamento(dadosSQL.getString("formapagamento"));
        fluxoCaixaRelVO.setValorMovimentacao(dadosSQL.getDouble("valor"));
        fluxoCaixaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("nomeApresentacaoSistema"))){
        	fluxoCaixaRelVO.setContaCaixa(dadosSQL.getString("nomeApresentacaoSistema"));
        }else{
        	fluxoCaixaRelVO.setContaCaixa(dadosSQL.getString("contacaixa") + " - " + dadosSQL.getString("digito"));
        }
        fluxoCaixaRelVO.setDataAbertura(dadosSQL.getDate("dataabertura"));
        fluxoCaixaRelVO.setDataFechamento(dadosSQL.getDate("datafechamento"));
        fluxoCaixaRelVO.setResponsavelAbertura(dadosSQL.getString("responsavelabertura"));
        fluxoCaixaRelVO.setDataMovimentacao(dadosSQL.getDate("data"));
        fluxoCaixaRelVO.setValorInicial(dadosSQL.getDouble("valorinicial"));
        fluxoCaixaRelVO.setValorFinal(dadosSQL.getDouble("valorfinal"));
        fluxoCaixaRelVO.setTipoPagamento(dadosSQL.getString("tipopagamento"));
        fluxoCaixaRelVO.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
        fluxoCaixaRelVO.setBanco(dadosSQL.getString("banco"));
        fluxoCaixaRelVO.setNumeroCheque(dadosSQL.getString("numeroCheque"));
        fluxoCaixaRelVO.setSaldoFinalCaixaDinheiro(executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(dadosSQL.getInt("codigoContaCaixa"), dadosSQL.getDate("dataabertura"), dadosSQL.getDate("datafechamento"), dadosSQL.getInt("codigoFluxoCaixa"), usuario));
        fluxoCaixaRelVO.setSaldoFinalCaixaCheque(executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(dadosSQL.getInt("codigoContaCaixa"), dadosSQL.getDate("dataabertura"), dadosSQL.getDate("datafechamento"), dadosSQL.getInt("codigoFluxoCaixa"), usuario));
        if (!hashFluxoJaConsultados.containsKey(dadosSQL.getInt("fcCodigo"))) {
            fluxoCaixaRelVO.setValorRecebidoBoletoBancario(executarConsultaValorRecebidoBoletoBancario(dataInicio, dataFim, unidadeEnsino, dadosSQL.getInt("fcCodigo"), usuario));
            hashFluxoJaConsultados.put(dadosSQL.getInt("fcCodigo"), fluxoCaixaRelVO.getValorRecebidoBoletoBancario());
        } else {
            fluxoCaixaRelVO.setValorRecebidoBoletoBancario(hashFluxoJaConsultados.get(dadosSQL.getInt("fcCodigo")));
        }
        return fluxoCaixaRelVO;
    }

    private FluxoCaixaRelVO montarDadosListaVazia(SqlRowSet dadosSQL, HashMap<Integer, Double> hashFluxoJaConsultados, Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer usuario) throws Exception {
        FluxoCaixaRelVO fluxoCaixaRelVO = new FluxoCaixaRelVO();
        fluxoCaixaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino"));
        if(Uteis.isAtributoPreenchido("nomeApresentacaoSistema")){
        	fluxoCaixaRelVO.setContaCaixa(dadosSQL.getString("nomeApresentacaoSistema"));
        }else{
        	fluxoCaixaRelVO.setContaCaixa(dadosSQL.getString("contacaixa") + " - " + dadosSQL.getString("digito"));
        }
        fluxoCaixaRelVO.setDataAbertura(dadosSQL.getDate("dataabertura"));
        fluxoCaixaRelVO.setDataFechamento(dadosSQL.getDate("datafechamento"));
        fluxoCaixaRelVO.setResponsavelAbertura(dadosSQL.getString("responsavelabertura"));
        return fluxoCaixaRelVO;
    }

    private SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer contaCorrente, Integer usuario, String modeloRel, String tipoLayout) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct cc.nomeApresentacaoSistema,mc.banco, mc.numeroCheque, turma.identificadorturma, fc.codigo as fcCodigo, mc.tipomovimentacao as tipo, mc.tipoorigem as movimentacao, p2.nome as sacado, mc.codigoorigem, ");
        sql.append("fp.nome as formapagamento, fp.tipo as tipopagamento, mc.valor as valor, ue.nome as unidadeensino, ");
        sql.append("cc.numero as contacaixa, cc.digito as digito, fc.dataabertura as dataabertura, fc.datafechamento as datafechamento, ");
        sql.append("u.nome as responsavelabertura, mc.data as data, fc.saldoinicial as valorinicial, fc.saldofinal as valorfinal, parc.nome as parceiro, fornecedor.nome AS fornecedor, cc.codigo AS codigoContaCaixa, fc.codigo AS codigoFluxoCaixa, ");
        sql.append(" sacadobanco.nome as sacadobanco, operadoracartao.nome as operadoracartao ");
        sql.append("from movimentacaocaixa mc ");
        sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
        sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
        sql.append("left join parceiro parc on parc.codigo = mc.parceiro ");
        sql.append("left join pessoa p2 on p2.codigo = mc.pessoa ");
        sql.append("left join fornecedor  on fornecedor.codigo = mc.fornecedor ");
        sql.append("left join banco as sacadobanco on sacadobanco.codigo = mc.sacadobanco ");
        sql.append("left join operadoracartao on operadoracartao.codigo = mc.operadoracartao ");
        sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo ");
        sql.append("inner join unidadeensino ue on ue.codigo = fc.unidadeensino ");
        sql.append("inner join usuario u on u.codigo = fc.responsavelabertura ");
        sql.append("left join negociacaorecebimento on negociacaorecebimento.codigo = mc.codigoorigem and mc.tipoOrigem = 'RE' ");
        sql.append("left join matricula on matricula.matricula = negociacaorecebimento.matricula ");
        sql.append("left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo =  ");
        sql.append("(select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula order by matriculaperiodo.codigo desc limit 1) ");
        sql.append("left join turma on turma.codigo = matriculaperiodo.turma ");
        sql.append("where fc.dataabertura between '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' and '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataFim, 23, 59, 59))).append("' ");
        sql.append(" AND fp.tipo IN ('DI','CH')  ");
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sql.append(" and ue.codigo = ");
            sql.append(unidadeEnsino);
            sql.append(" ");
        }
        if (contaCorrente != null && contaCorrente > 0) {
            sql.append(" and cc.codigo = ");
            sql.append(contaCorrente);
            sql.append(" ");
        }
        if (usuario != null && usuario > 0) {
            sql.append(" and u.codigo = ");
            sql.append(usuario);
            sql.append(" ");
        }
        sql.append("and fp.tipo in ('DI', 'CH') ");
        sql.append("group by cc.nomeApresentacaoSistema,mc.banco, sacadobanco.nome, operadoracartao.nome, mc.numeroCheque, turma.identificadorturma, ue.nome, cc.numero, fc.dataabertura, mc.data, p2.nome, mc.tipomovimentacao, mc.tipoorigem, mc.codigoorigem, ");
        sql.append("fp.nome, fp.tipo, mc.valor, ue.nome, cc.numero, cc.digito,fc.dataabertura, fc.datafechamento, u.nome, fc.saldoinicial, fc.saldofinal, parc.nome, fc.codigo, fornecedor.nome, cc.codigo ");
        if (modeloRel.equals("retrato") && tipoLayout.equals("layout2")) {
            sql.append("order by ue.nome, cc.numero, fc.dataabertura, mc.data, fp.nome, p2.nome, mc.tipomovimentacao,  sacadobanco.nome, operadoracartao.nome, fornecedor.nome, parc.nome  ");
        } else {
            sql.append("order by ue.nome, cc.numero, fc.dataabertura, mc.data, fp.nome, p2.nome, sacadobanco.nome, operadoracartao.nome, fornecedor.nome, parc.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return tabelaResultado;
    }

    private SqlRowSet executarConsultaParametrizadaListaVazia(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer contaCorrente, Integer usuario) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select ue.nome as unidadeensino, cc.numero as contacaixa,cc.nomeApresentacaoSistema, cc.digito as digito, fc.dataabertura as dataabertura, fc.datafechamento as datafechamento, u.nome as responsavelabertura ");
        sql.append("from fluxocaixa fc inner join contacorrente cc on fc.contacaixa = cc.codigo inner join unidadeensino ue on ue.codigo = fc.unidadeensino inner join usuario u on u.codigo = fc.responsavelabertura ");
        sql.append("where fc.dataabertura between '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' and '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataFim, 23, 59, 59))).append("' ");
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sql.append(" and ue.codigo = ");
            sql.append(unidadeEnsino);
            sql.append(" ");
        }
        if (contaCorrente != null && contaCorrente > 0) {
            sql.append(" and cc.codigo = ");
            sql.append(contaCorrente);
            sql.append(" ");
        }
        if (usuario != null && usuario > 0) {
            sql.append(" and u.codigo = ");
            sql.append(usuario);
            sql.append(" ");
        }
        sql.append("group by cc.nomeApresentacaoSistema,cc.numero, ue.nome, fc.dataabertura, fc.datafechamento, u.nome, cc.digito ");
        sql.append("order by ue.nome, cc.numero, fc.dataabertura, cc.digito");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return tabelaResultado;
    }

    private Double executarConsultaValorRecebidoBoletoBancario(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer fluxoCaixa, Integer usuario) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.codigo, sum(fpnr.valorrecebimento) as valorRecebidoBoletoBancario from fluxocaixa fc ");
        sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
        sql.append("inner join negociacaorecebimento nr ON nr.contacorrentecaixa = fc.contacaixa ");
        sql.append("inner join formapagamentonegociacaorecebimento fpnr on fpnr.negociacaorecebimento = nr.codigo ");
        sql.append("inner join formapagamento fp on fpnr.formapagamento = fp.codigo  ");
        sql.append("inner join unidadeensino ue on ue.codigo = fc.unidadeensino ");
        sql.append("inner join usuario u on u.codigo = fc.responsavelabertura ");
        sql.append("WHERE fp.tipo = 'BO' AND fc.codigo = ").append(fluxoCaixa);
        sql.append(" AND fc.dataabertura between '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' AND '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 23, 59, 59))).append("' ");
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sql.append(" AND ue.codigo = ").append(unidadeEnsino).append(" ");
        }
        if (usuario != null && usuario > 0) {
            sql.append(" AND u.codigo = ").append(usuario).append(" ");
        }
        sql.append(" and nr.data between fc.dataabertura and (case when (fc.responsavelfechamento is null) then now() else fc.datafechamento end) ");
        sql.append(" group by fc.codigo; ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valorRecebidoBoletoBancario");
        } else {
            return 0.0;
        }
    }
    
    
    
    private Double executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select (select sum(valor) from movimentacaocaixa mc ");
        sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo "); 
        sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
        sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
        if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
        	sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "') as t ");
        } else {
        	sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "') as t ");
        }
         
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        sql = new StringBuilder();
        sql.append("select (select sum(valor) from movimentacaocaixa mc ");
        sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo "); 
        sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
        sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
        if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
        	sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "') as t ");
        } else {
        	sql.append("where fp.tipo = 'DI' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "') as t ");
        }
        SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        Double entrada = 0.0;
        Double saida = 0.0;
        if (tabelaResultado.next()) {
        	entrada = tabelaResultado.getDouble("t");
        }
        if (tabelaResultado2.next()) {
        	saida = tabelaResultado2.getDouble("t");
        }
        return entrada - saida;
    }
    
    private Double executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select (select sum(valor) from movimentacaocaixa mc ");
        sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo "); 
        sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
        sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
        if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
        	sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + "  and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "') as t ");
        } else {
        	sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'EN' and fc.codigo <= " + codigoFluxoCaixa + "  and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "') as t ");
        }
         
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        sql = new StringBuilder();
        sql.append("select (select sum(valor) from movimentacaocaixa mc ");
        sql.append("inner join formapagamento fp on mc.formapagamento = fp.codigo "); 
        sql.append("inner join fluxocaixa fc on mc.fluxocaixa = fc.codigo ");
        sql.append("inner join contacorrente cc on fc.contacaixa = cc.codigo ");
        if (Uteis.getDataBD0000(dataFechamento).equals(Uteis.getDataBD0000(dataAbertura))) {
        	sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD2359(dataFechamento) + "') as t ");
        } else {
        	sql.append("where fp.tipo = 'CH' and mc.tipomovimentacao = 'SA' and fc.codigo <= " + codigoFluxoCaixa + " and cc.codigo = " + codigoCaixa + " and data <= '" + Uteis.getDataBD0000(dataFechamento) + "') as t ");
        }
         
        SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        Double entrada = 0.0;
        Double saida = 0.0;
        if (tabelaResultado.next()) {
        	entrada = tabelaResultado.getDouble("t");
        }
        if (tabelaResultado2.next()) {
        	saida = tabelaResultado2.getDouble("t");
        }
        return entrada - saida;
    }
    

    @Override
    public void validarDados(Date dataInicio, Date dataFim) throws ConsistirException {
        if (dataInicio == null || dataFim == null) {
            throw new ConsistirException("O Período deve ser informado para a geração do relatório.");
        }
    }

    public String designIReportRelatorio(String modeloRel, String tipoLayout) {
        if (modeloRel.equals("retrato") && tipoLayout.equals("layout1")) {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
        } else if (modeloRel.equals("retrato") && tipoLayout.equals("layout2")) {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeLayout2() + ".jrxml");
        } else {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeModeloPaisagem() + ".jrxml");
        }
    }

    @Override
    public String caminhoBaseIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro");
    }

    public static String getIdEntidade() {
        return ("FluxoCaixaRel");
    }

    public static String getIdEntidadeLayout2() {
        return ("FluxoCaixaLayout2Rel");
    }

    public static String getIdEntidadeModeloPaisagem() {
        return ("FluxoCaixaPaisagemRel");
    }
}
