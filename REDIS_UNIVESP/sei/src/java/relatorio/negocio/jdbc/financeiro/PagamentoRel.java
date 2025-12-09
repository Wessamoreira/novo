package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.PagamentoRelVO;
import relatorio.negocio.interfaces.financeiro.PagamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PagamentoRel extends SuperRelatorio implements PagamentoRelInterfaceFacade {

    public PagamentoRel() {
    }

    @Override
    public List<PagamentoRelVO> criarObjeto(Date dataInicio, Date dataFim, Boolean filtrarDataFatoGerador, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, Integer aluno,
            Integer filtroAluno, String ordenacao, String tipo, Integer filtroTipo, String tipoRelatorio, Integer filtroResponsavelFinanceiro, Integer responsavelFinanceiro, String nomeResponsavelFinanceiro, Integer filtroParceiro, Integer parceiro, String nomeParceiro, String tipoConta,Integer codigoContaCorrenteCaixa, Integer filtroOperadoraCartao, Integer operadoraCartao) throws Exception {
        List<PagamentoRelVO> pagamentoRelVOs = new ArrayList<PagamentoRelVO>(0);

        Map<Integer, PagamentoRelVO> mapa = new HashMap<Integer, PagamentoRelVO>(0);
        Map<Integer, Integer> mapaContaPagar = new HashMap<Integer, Integer>(0);

        SqlRowSet dadosSQL = executarConsultaParametrizada(dataInicio, dataFim, filtrarDataFatoGerador, unidadeEnsino, fornecedor, fornecedorNome, fornecedorCpfCnpj,
                funcionarioNome, funcionario, banco, bancoNome, filtroFornecedor, filtroFuncionario, filtroBanco, aluno, filtroAluno, ordenacao, tipo, filtroTipo, filtroResponsavelFinanceiro, responsavelFinanceiro, filtroParceiro, parceiro, tipoConta,codigoContaCorrenteCaixa, filtroOperadoraCartao, operadoraCartao);
        while (dadosSQL.next()) {
            if (tipoRelatorio != null && tipoRelatorio.equals("SI")) {
                PagamentoRelVO obj = montarDadosSintetico(dadosSQL, mapa, mapaContaPagar);
                mapa.put(obj.getCodigoPagamento(), obj);
            } else {
                PagamentoRelVO obj = montarDados(dadosSQL, mapa);
                mapa.put(obj.getCodigoContaPagar(), obj);
            }
        }
        for (PagamentoRelVO pagamentoRelVO : mapa.values()) {
            pagamentoRelVOs.add(pagamentoRelVO);
        }
//        Ordenacao.ordenarLista(pagamentoRelVOs, "tipoSacado");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "funcionario");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "fornecedor");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "banco");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "aluno");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "parceiro");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "responsavelFinanceiro");
//        Ordenacao.ordenarLista(pagamentoRelVOs, "formaPagamento");
        
        if (ordenacao.equals("dataVencimento")) {
            Ordenacao.ordenarLista(pagamentoRelVOs, "dataContaPagar");
        } else if (ordenacao.equals("dataPagamento")) {
            Ordenacao.ordenarLista(pagamentoRelVOs, "dataPagamento");
        } else {
        	Ordenacao.ordenarLista(pagamentoRelVOs, "nomeUnidadeEnsino");        	
        } 

        return pagamentoRelVOs;
    }

    public PagamentoRelVO montarDados(SqlRowSet dadosSQL, Map<Integer, PagamentoRelVO> mapaPagamentos) throws Exception {
        PagamentoRelVO pagamentoRelVO;
        try {
            if (!mapaPagamentos.containsKey(dadosSQL.getInt("contapagar_codigo"))) {
                pagamentoRelVO = new PagamentoRelVO();
                if(Uteis.isAtributoPreenchido(dadosSQL.getString("cc2_nomeApresentacaoSistema"))){
                	pagamentoRelVO.setContaCorrente(dadosSQL.getString("cc2_nomeApresentacaoSistema"));
                }else{
                	pagamentoRelVO.setContaCorrente(dadosSQL.getString("contacorrente"));
                }
                pagamentoRelVO.setBanco(dadosSQL.getString("banco_nome"));
                pagamentoRelVO.setAluno(dadosSQL.getString("aluno_nome"));
                pagamentoRelVO.setCategoriaDespesa(dadosSQL.getString("categoriadespesa_descricao"));
                pagamentoRelVO.setCodigoContaPagar(dadosSQL.getInt("contapagar_codigo"));
                pagamentoRelVO.setParceiro(dadosSQL.getString("parceiro_nome"));
                pagamentoRelVO.setOperadoraCartao(dadosSQL.getString("operadoracartao_nome"));
                pagamentoRelVO.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
                pagamentoRelVO.setDataContaPagar(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_data")));
                pagamentoRelVO.setDataPagamento(Uteis.getDataJDBC(dadosSQL.getDate("pagamento_data")));
                pagamentoRelVO.setDesconto(dadosSQL.getDouble("contapagar_desconto"));
                pagamentoRelVO.setDataFatoGerador(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_datafatogerador")));
                pagamentoRelVO.setFormaPagamentoCreditoDebito(dadosSQL.getString("fpnp_valor_cd"));
                pagamentoRelVO.setFormaPagamentoDeposito(dadosSQL.getString("fpnp_valor_dc"));
                pagamentoRelVO.setFormaPagamentoBoleto(dadosSQL.getString("fpnp_valor_bo"));
                pagamentoRelVO.setFormaPagamentoCartao(dadosSQL.getString("fpnp_valor_ca"));
                pagamentoRelVO.setFormaPagamentoCheque(dadosSQL.getString("fpnp_valor_ch"));
                pagamentoRelVO.setFormaPagamentoDebito(dadosSQL.getString("fpnp_valor_de"));
                pagamentoRelVO.setFormaPagamentoDinheiro(dadosSQL.getString("fpnp_valor_di"));
                pagamentoRelVO.setFormaPagamentoIsencao(dadosSQL.getString("fpnp_valor_is"));
                pagamentoRelVO.setFormaPagamentoPermuta(dadosSQL.getString("fpnp_valor_pe"));
                pagamentoRelVO.setFornecedor(dadosSQL.getString("fornecedor_nome"));
                pagamentoRelVO.setFuncionario(dadosSQL.getString("funcionario_nome"));
                pagamentoRelVO.setJuro(dadosSQL.getDouble("contapagar_juro"));
                pagamentoRelVO.setMulta(dadosSQL.getDouble("contapagar_multa"));
                pagamentoRelVO.setNegociacao(dadosSQL.getInt("pagamento_negociacao"));
                pagamentoRelVO.setNumeroDocumento(dadosSQL.getString("contapagar_numero"));
                pagamentoRelVO.setTipoSacado(dadosSQL.getString("pagamento_tiposacado"));
                pagamentoRelVO.setUnidadeEnsino(dadosSQL.getInt("pagamento_unidadeensino"));
                pagamentoRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
                pagamentoRelVO.setValor(dadosSQL.getDouble("contapagar_valor"));
                pagamentoRelVO.setValorPagamento(dadosSQL.getDouble("pagamento_valorpagamento"));
                pagamentoRelVO.setValorPago((dadosSQL.getDouble("contapagar_valor") + dadosSQL.getDouble("contapagar_juro") + dadosSQL.getDouble("contapagar_multa")) - dadosSQL.getDouble("contapagar_desconto"));
                pagamentoRelVO.setValorTroco(dadosSQL.getDouble("pagamento_troco"));
            } else {
                pagamentoRelVO = mapaPagamentos.get(dadosSQL.getInt("contapagar_codigo"));
                if (dadosSQL.getString("fpnp_valor_cd") != null) {
                    pagamentoRelVO.setFormaPagamentoCreditoDebito(dadosSQL.getString("fpnp_valor_cd"));
                }
                if (dadosSQL.getString("fpnp_valor_dc") != null) {
                	pagamentoRelVO.setFormaPagamentoDeposito(dadosSQL.getString("fpnp_valor_dc"));
                }
                if (dadosSQL.getString("fpnp_valor_bo") != null) {
                	pagamentoRelVO.setFormaPagamentoBoleto(dadosSQL.getString("fpnp_valor_bo"));
                }
                if (dadosSQL.getString("fpnp_valor_ca") != null) {
                    pagamentoRelVO.setFormaPagamentoCartao(dadosSQL.getString("fpnp_valor_ca"));
                }
                if (dadosSQL.getString("fpnp_valor_ch") != null) {
                    pagamentoRelVO.setFormaPagamentoCheque(dadosSQL.getString("fpnp_valor_ch"));
                }
                if (dadosSQL.getString("fpnp_valor_de") != null) {
                    pagamentoRelVO.setFormaPagamentoDebito(dadosSQL.getString("fpnp_valor_de"));
                }
                if (dadosSQL.getString("fpnp_valor_di") != null) {
                    pagamentoRelVO.setFormaPagamentoDinheiro(dadosSQL.getString("fpnp_valor_di"));
                }
                if (dadosSQL.getString("fpnp_valor_pe") != null) {
                	pagamentoRelVO.setFormaPagamentoPermuta(dadosSQL.getString("fpnp_valor_pe"));
                }
                if (dadosSQL.getString("fpnp_valor_is") != null) {
                	pagamentoRelVO.setFormaPagamentoIsencao(dadosSQL.getString("fpnp_valor_is"));
                }
            }
            pagamentoRelVO.setTipoContaCorrente(dadosSQL.getBoolean("contacaixa"));
            validarFormaPagamentoApresentar(pagamentoRelVO);
            validarContaCorrenteApresentar(pagamentoRelVO);
        } catch (Exception e) {
            throw e;
        }
        return pagamentoRelVO;
    }

    public PagamentoRelVO montarDadosSintetico(SqlRowSet dadosSQL, Map<Integer, PagamentoRelVO> mapaPagamentos, Map<Integer, Integer> mapaContaPagar) throws Exception {
        PagamentoRelVO pagamentoRelVO;
        Double valorPagoPorTipo;
        try {
            if (!mapaPagamentos.containsKey(dadosSQL.getInt("pagamento_codigo"))) {
                pagamentoRelVO = new PagamentoRelVO();
                if(Uteis.isAtributoPreenchido(dadosSQL.getString("cc2_nomeApresentacaoSistema"))){
                	pagamentoRelVO.setContaCorrente(dadosSQL.getString("cc2_nomeApresentacaoSistema"));
                }else{
                	pagamentoRelVO.setContaCorrente(dadosSQL.getString("contacorrente"));
                }
                pagamentoRelVO.setBanco(dadosSQL.getString("banco_nome"));
                pagamentoRelVO.setAluno(dadosSQL.getString("aluno_nome"));
                pagamentoRelVO.setParceiro(dadosSQL.getString("parceiro_nome"));
                pagamentoRelVO.setOperadoraCartao(dadosSQL.getString("operadoracartao_nome"));
                pagamentoRelVO.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
                pagamentoRelVO.setCategoriaDespesa(dadosSQL.getString("categoriadespesa_descricao"));
                pagamentoRelVO.setCodigoContaPagar(dadosSQL.getInt("contapagar_codigo"));
                pagamentoRelVO.setDataContaPagar(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_data")));
                pagamentoRelVO.setCodigoPagamento(dadosSQL.getInt("pagamento_codigo"));
                pagamentoRelVO.setDataPagamento(Uteis.getDataJDBC(dadosSQL.getDate("pagamento_data")));
                pagamentoRelVO.setDesconto(dadosSQL.getDouble("contapagar_desconto"));
                pagamentoRelVO.setDataFatoGerador(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_datafatogerador")));
                pagamentoRelVO.setFormaPagamentoCreditoDebito(dadosSQL.getString("fpnp_valor_cd"));
                pagamentoRelVO.setFormaPagamentoDeposito(dadosSQL.getString("fpnp_valor_dc"));
                pagamentoRelVO.setFormaPagamentoBoleto(dadosSQL.getString("fpnp_valor_bo"));
                pagamentoRelVO.setFormaPagamentoCartao(dadosSQL.getString("fpnp_valor_ca"));
                pagamentoRelVO.setFormaPagamentoCheque(dadosSQL.getString("fpnp_valor_ch"));
                pagamentoRelVO.setFormaPagamentoDebito(dadosSQL.getString("fpnp_valor_de"));
                pagamentoRelVO.setFormaPagamentoDinheiro(dadosSQL.getString("fpnp_valor_di"));
                pagamentoRelVO.setFormaPagamentoIsencao(dadosSQL.getString("fpnp_valor_is"));
                pagamentoRelVO.setFormaPagamentoPermuta(dadosSQL.getString("fpnp_valor_pe"));
                pagamentoRelVO.setFornecedor(dadosSQL.getString("fornecedor_nome"));
                pagamentoRelVO.setFuncionario(dadosSQL.getString("funcionario_nome"));
                pagamentoRelVO.setJuro(dadosSQL.getDouble("contapagar_juro"));
                pagamentoRelVO.setMulta(dadosSQL.getDouble("contapagar_multa"));
                pagamentoRelVO.setNegociacao(dadosSQL.getInt("pagamento_negociacao"));
                pagamentoRelVO.setNumeroDocumento(dadosSQL.getString("contapagar_numero"));
                pagamentoRelVO.setTipoSacado(dadosSQL.getString("pagamento_tiposacado"));
                pagamentoRelVO.setUnidadeEnsino(dadosSQL.getInt("pagamento_unidadeensino"));
                pagamentoRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
                pagamentoRelVO.setValor(dadosSQL.getDouble("contapagar_valor"));
                pagamentoRelVO.setValorPagamento(dadosSQL.getDouble("pagamento_valorpagamento"));
                pagamentoRelVO.setValorPago((dadosSQL.getDouble("contapagar_valor") + dadosSQL.getDouble("contapagar_juro") + dadosSQL.getDouble("contapagar_multa")) - dadosSQL.getDouble("contapagar_desconto"));
                pagamentoRelVO.setValorTroco(dadosSQL.getDouble("pagamento_troco"));
                if (!mapaContaPagar.containsKey(dadosSQL.getInt("contapagar_codigo"))) {
                    mapaContaPagar.put(dadosSQL.getInt("contapagar_codigo"), dadosSQL.getInt("contapagar_codigo"));
                }
            } else {
                pagamentoRelVO = mapaPagamentos.get(dadosSQL.getInt("pagamento_codigo"));
                if (pagamentoRelVO.getValor() != null) {
                    if (pagamentoRelVO.getCodigoContaPagar() != null && dadosSQL.getInt("contapagar_codigo") != 0 && !mapaContaPagar.containsKey(dadosSQL.getInt("contapagar_codigo"))) {
                        pagamentoRelVO.setValor(pagamentoRelVO.getValor() + (dadosSQL.getDouble("contapagar_valor")));
                        mapaContaPagar.put(dadosSQL.getInt("contapagar_codigo"), dadosSQL.getInt("contapagar_codigo"));
                    }
                } else {
                    pagamentoRelVO.setValor(dadosSQL.getDouble("contapagar_valor"));
                }
                if (dadosSQL.getString("fpnp_valor_bo") != null) {
                    valorPagoPorTipo = 0.0;
                    if (pagamentoRelVO.getFormaPagamentoBoleto() != null) {
                        valorPagoPorTipo = Double.parseDouble(pagamentoRelVO.getFormaPagamentoBoleto()) + dadosSQL.getDouble("fpnp_valor_bo");
                        pagamentoRelVO.setFormaPagamentoBoleto(valorPagoPorTipo.toString());
                    } else {
                        pagamentoRelVO.setFormaPagamentoBoleto(dadosSQL.getString("fpnp_valor_bo"));
                    }
                }
                if (dadosSQL.getString("fpnp_valor_ca") != null) {
                    valorPagoPorTipo = 0.0;
                    if (pagamentoRelVO.getFormaPagamentoCartao() != null) {
                        valorPagoPorTipo = Double.parseDouble(pagamentoRelVO.getFormaPagamentoCartao()) + dadosSQL.getDouble("fpnp_valor_ca");
                        pagamentoRelVO.setFormaPagamentoCartao(valorPagoPorTipo.toString());
                    } else {
                        pagamentoRelVO.setFormaPagamentoCartao(dadosSQL.getString("fpnp_valor_ca"));
                    }
                }
                if (dadosSQL.getString("fpnp_valor_ch") != null) {
                    valorPagoPorTipo = 0.0;
                    if (pagamentoRelVO.getFormaPagamentoCheque() != null) {
                        valorPagoPorTipo = Double.parseDouble(pagamentoRelVO.getFormaPagamentoCheque()) + dadosSQL.getDouble("fpnp_valor_ch");
                        pagamentoRelVO.setFormaPagamentoCheque(valorPagoPorTipo.toString());
                    } else {
                        pagamentoRelVO.setFormaPagamentoCheque(dadosSQL.getString("fpnp_valor_ch"));
                    }
                }
                if (dadosSQL.getString("fpnp_valor_de") != null) {
                    valorPagoPorTipo = 0.0;
                    if (pagamentoRelVO.getFormaPagamentoDebito() != null) {
                        valorPagoPorTipo = Double.parseDouble(pagamentoRelVO.getFormaPagamentoDebito()) + dadosSQL.getDouble("fpnp_valor_de");
                        pagamentoRelVO.setFormaPagamentoDebito(valorPagoPorTipo.toString());
                    } else {
                        pagamentoRelVO.setFormaPagamentoDebito(dadosSQL.getString("fpnp_valor_de"));
                    }
                }
                if (dadosSQL.getString("fpnp_valor_di") != null) {
                    valorPagoPorTipo = 0.0;
                    if (pagamentoRelVO.getFormaPagamentoDinheiro() != null) {
                        valorPagoPorTipo = Double.parseDouble(pagamentoRelVO.getFormaPagamentoDinheiro()) + dadosSQL.getDouble("fpnp_valor_di");
                        pagamentoRelVO.setFormaPagamentoDinheiro(valorPagoPorTipo.toString());
                    } else {
                        pagamentoRelVO.setFormaPagamentoDinheiro(dadosSQL.getString("fpnp_valor_di"));
                    }
                }
            }
            pagamentoRelVO.setTipoContaCorrente(dadosSQL.getBoolean("contacaixa"));
            validarFormaPagamentoApresentar(pagamentoRelVO);
            validarContaCorrenteApresentar(pagamentoRelVO);
        } catch (Exception e) {
            throw e;
        }
        return pagamentoRelVO;
    }

    @Override
    public SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, Boolean filtrarDataFatoGerador, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, Integer aluno, Integer filtroAluno, String ordenacao, String tipo, Integer filtroTipo, Integer filtroResponsavelFinanceiro, Integer responsavelFinanceiro, Integer filtroParceiro, Integer parceiro, String tipoConta,Integer codigoContaCorrenteCaixa, Integer filtroOperadoraCartao, Integer operadoraCartao) throws Exception {
        String selectStr = "SELECT cc2.contacaixa,cc2.nomeApresentacaoSistema AS cc2_nomeApresentacaoSistema, cc2.numero as contacorrente, pagamento.data AS pagamento_data, pagamento.codigo AS pagamento_negociacao, pagamento.valortotal::NUMERIC(20,2) AS pagamento_valor, "
                + "pagamento.valortotalpagamento AS pagamento_valorpagamento, pagamento.valortroco::NUMERIC(20,2) AS pagamento_troco, pagamento.tiposacado AS pagamento_tiposacado, "
                + "pagamento.unidadeensino AS pagamento_unidadeensino, pagamento.funcionario AS pagamento_funcionario, pagamento.fornecedor as pagamento_fornecedor, "
                + "pagamento.banco AS pagamento_banco, unidadeensino.nome as unidadeensino_nome, pessoa.nome as funcionario_nome, fornecedor.nome as fornecedor_nome, "
                + "banco.nome AS banco_nome, contapagar.nrdocumento as contapagar_numero, contapagar.datavencimento as contapagar_data, contapagar.valor::NUMERIC(20,2) as contapagar_valor, "
                + "contapagar.codigo AS contapagar_codigo, contapagar.juro::NUMERIC(20,2) as contapagar_juro, contapagar.multa::NUMERIC(20,2) as contapagar_multa, contapagar.desconto::NUMERIC(20,2) as contapagar_desconto, contapagar.datafatogerador as contapagar_datafatogerador, "
                + "array_to_string(array(select distinct categoriadespesa.descricao from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa "
                +"  where centroresultadoorigem.tipoCentroResultadoOrigem = '"+TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()+"' and centroresultadoorigem.codOrigem = contapagar.codigo::VARCHAR and tipoMovimentacaoCentroResultadoOrigemEnum != '"+TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()+"'"
    			+"  order by categoriadespesa.descricao), ', ') as categoriadespesa_descricao, "
                + " aluno.nome AS aluno_nome, responsavelFinanceiro.nome as responsavelFinanceiro_nome, parceiro.nome as parceiro_nome, fp.tipo, operadoracartao.nome as operadoracartao_nome, "
                + "case when (fp.tipo = 'DE' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_cd, "
                + "case when (fp.tipo = 'DC' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_dc, "
                + "case when (fp.tipo = 'DI' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_di, "
                + "case when (fp.tipo in ('CD', 'DE') ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_de, "
                + "case when (fp.tipo = 'CA' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_ca, "
                + "case when (fp.tipo = 'BO' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_bo, "
                + "case when (fp.tipo = 'CH' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_ch, "
                + "case when (fp.tipo = 'IS' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_is, "
                + "case when (fp.tipo = 'PE' ) then sum(cpp.valorTotalPagamento::NUMERIC(20,2)) end fpnp_valor_pe, "
                + "pagamento.codigo AS pagamento_codigo "
                + "FROM ContaPagar as contapagar "
                + "LEFT JOIN contapagarnegociacaopagamento as cpnp ON (cpnp.contapagar = contapagar.codigo) "
                + "LEFT JOIN negociacaopagamento as pagamento ON (pagamento.codigo = cpnp.negociacaocontapagar) LEFT JOIN fornecedor ON (pagamento.fornecedor = fornecedor.codigo) "
                + "LEFT JOIN funcionario ON (pagamento.funcionario = funcionario.codigo) LEFT JOIN pessoa ON (funcionario.pessoa = pessoa.codigo) "
                + "LEFT JOIN pessoa aluno ON (aluno.codigo = pagamento.aluno) "
                + "LEFT JOIN unidadeensino ON (pagamento.unidadeensino = unidadeensino.codigo) LEFT JOIN banco ON (pagamento.banco = banco.codigo) "
                //+ "LEFT JOIN categoriadespesa ON categoriadespesa.codigo = contapagar.centroDespesa "
                + "LEFT JOIN Pessoa as responsavelFinanceiro ON responsavelFinanceiro.codigo = pagamento.responsavelFinanceiro "
                + "LEFT JOIN Parceiro ON parceiro.codigo = pagamento.parceiro "
                + "LEFT JOIN operadoracartao ON operadoracartao.codigo = pagamento.operadoracartao "
                + "INNER JOIN formapagamentonegociacaopagamento as fpnp2 on fpnp2.negociacaocontapagar = pagamento.codigo "
                + "LEFT JOIN contacorrente as cc2 on cc2.codigo = fpnp2.contacorrente "
                + "INNER JOIN contapagarpagamento as cpp on cpp.contapagar = contapagar.codigo  and cpp.formapagamentonegociacaopagamento = fpnp2.codigo "
                + "INNER JOIN formapagamento as fp on fp.codigo = cpp.formapagamento";
        selectStr = montarFiltrosRelatorio(selectStr, dataInicio, dataFim, filtrarDataFatoGerador, unidadeEnsino, fornecedor, fornecedorNome, fornecedorCpfCnpj,
                funcionarioNome, funcionario, banco, bancoNome, filtroFornecedor, filtroFuncionario, filtroBanco, aluno, filtroAluno, tipo, filtroTipo, filtroResponsavelFinanceiro, responsavelFinanceiro, filtroParceiro, parceiro, tipoConta,codigoContaCorrenteCaixa, filtroOperadoraCartao, operadoraCartao);
        selectStr += " Group By pagamento_data, fp.tipo, aluno.nome, unidadeensino_nome, pagamento_valor, pagamento_valorpagamento, pagamento_troco, pagamento_tiposacado, "
                + "pagamento_negociacao, unidadeensino_nome, pagamento_unidadeensino, pagamento_funcionario, pagamento_fornecedor, pagamento_banco, contapagar_numero, contapagar_valor, "
                + "contapagar_data, contapagar_codigo, contapagar_juro, contapagar_multa, contapagar_desconto, contapagar_datafatogerador, funcionario_nome, fornecedor_nome, banco_nome, "
                + "cc2.numero,cc2.nomeApresentacaoSistema, cc2.contacaixa, pagamento.codigo, responsavelFinanceiro.nome, parceiro.nome, operadoracartao.nome ";
        selectStr += montarOrdenacao(ordenacao);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    public String montarOrdenacao(String ordenacao) {
        if (ordenacao.equals("nome")) {
            return "ORDER BY funcionario_nome, fornecedor_nome, banco_nome, aluno_nome, parceiro.nome, responsavelFinanceiro.nome, operadoracartao.nome, pagamento.unidadeensino";
        } else if (ordenacao.equals("dataVencimento")) {
            return "ORDER BY contapagar_data, pagamento.unidadeensino, funcionario_nome, fornecedor_nome, banco_nome, aluno_nome, parceiro.nome, responsavelFinanceiro.nome, operadoracartao.nome ";
        } else if (ordenacao.equals("dataPagamento")) {
            return "ORDER BY pagamento.data, pagamento.unidadeensino, funcionario_nome, fornecedor_nome, banco_nome, aluno_nome, parceiro.nome, responsavelFinanceiro.nome, operadoracartao.nome ";
        }
        return "";
    }

    private String montarFiltrosRelatorio(String selectStr, Date dataInicio, Date dataFim, Boolean filtraDataFatoGerador, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, Integer aluno, Integer filtroAluno, String tipo, Integer filtroTipo, Integer filtroResponsavelFinanceiro, Integer responsavelFinanceiro, Integer filtroParceiro, Integer parceiro, String tipoConta,Integer codigoContaCorrenteCaixa, Integer filtroOperadoraCartao, Integer operadoraCartao) {
        String filtros = "";
        boolean adicionarAND = true;
        if ((unidadeEnsino != null) && (unidadeEnsino.getCodigo().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.unidadeensino = " + unidadeEnsino.getCodigo().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if ((tipo != null) && (!tipo.equals(""))) {
            filtros = adicionarCondicionalWhere(filtros, "( upper(fp.tipo) = ('" + tipo.toUpperCase() + "'))", adicionarAND);
            adicionarAND = true;
        }
        if (filtroTipo.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( upper(fp.tipo) = 'NENHUM')", adicionarAND);
            adicionarAND = true;
        } else if ((filtroTipo.intValue() == 2) && (tipo != null) && (!tipo.equals(""))) {
            filtros = adicionarCondicionalWhere(filtros, "( upper(fp.tipo) = ('" + tipo.toUpperCase() + "'))", adicionarAND);
            adicionarAND = true;
        }
        if (filtroFornecedor.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'FO' )", adicionarAND);
            adicionarAND = true;
        } else if ((filtroFornecedor.intValue() == 2) && (fornecedor != null) && (fornecedor.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.fornecedor = " + fornecedor.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (tipoConta.equals("corrente")) {
            filtros = adicionarCondicionalWhere(filtros, "( cc2.contaCaixa = false )", adicionarAND);
            if(codigoContaCorrenteCaixa != null && codigoContaCorrenteCaixa !=0){
            	filtros = adicionarCondicionalWhere(filtros, "( cc2.codigo = "+codigoContaCorrenteCaixa+" )", adicionarAND);
            }
            adicionarAND = true;
        } else if (tipoConta.equals("caixa")) {
            filtros = adicionarCondicionalWhere(filtros, "( cc2.contaCaixa = true )", adicionarAND);
            if(codigoContaCorrenteCaixa != null && codigoContaCorrenteCaixa != 0){
            	  filtros = adicionarCondicionalWhere(filtros, "( cc2.codigo = "+codigoContaCorrenteCaixa+" )", adicionarAND);
            }
            adicionarAND = true;
        }
        if (filtroFuncionario.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'FU' )", adicionarAND);
        } else if ((filtroFuncionario.intValue() == 2) && (funcionario != null) && (funcionario.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.funcionario = " + funcionario.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }

        if (filtroResponsavelFinanceiro != null && filtroResponsavelFinanceiro.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'RF' )", adicionarAND);
        } else if ((filtroResponsavelFinanceiro != null && filtroResponsavelFinanceiro.intValue() == 2) && (responsavelFinanceiro != null) && (responsavelFinanceiro.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.responsavelFinanceiro = " + responsavelFinanceiro.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }

        if (filtroParceiro != null && filtroParceiro.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'PA' )", adicionarAND);
        } else if ((filtroParceiro != null && filtroParceiro.intValue() == 2) && (parceiro != null) && (parceiro.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.parceiro = " + parceiro.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }

        if (filtroBanco.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'BA' )", adicionarAND);
        } else if ((filtroBanco.intValue() == 2) && (banco != null) && (banco.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.banco = " + banco.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (filtroOperadoraCartao.intValue() == 1) {
        	filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'OC' )", adicionarAND);
        } else if ((filtroOperadoraCartao.intValue() == 2) && (operadoraCartao != null) && (operadoraCartao.intValue() != 0)) {
        	filtros = adicionarCondicionalWhere(filtros, "( pagamento.operadoraCartao = " + operadoraCartao.intValue() + ")", adicionarAND);
        	adicionarAND = true;
        }
        if (filtroAluno.intValue() == 1) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'AL' )", adicionarAND);
        } else if ((filtroAluno.intValue() == 2) && (aluno != null) && (aluno.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( pagamento.aluno = " + aluno.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (dataInicio != null) {
            if (!filtraDataFatoGerador) {
                filtros = adicionarCondicionalWhere(filtros, "(pagamento.data >= '" + Uteis.getDataJDBC(Uteis.getDateTime(dataInicio, 0, 0, 0)) + " ')", adicionarAND);
                adicionarAND = true;
            } else {
                filtros = adicionarCondicionalWhere(filtros, "(contapagar.datafatogerador >= '" + Uteis.getDataJDBC(Uteis.getDateTime(dataInicio, 0, 0, 0)) + " ')", adicionarAND);
                adicionarAND = true;
            }

        }
        if (dataFim != null) {
            if (!filtraDataFatoGerador) {
                filtros = adicionarCondicionalWhere(filtros, "(pagamento.data <= '" + Uteis.getDataJDBC(Uteis.getDateTime(dataFim, 23, 59, 59)) + " ')", adicionarAND);
                adicionarAND = true;
            } else {
                filtros = adicionarCondicionalWhere(filtros, "(contapagar.datafatogerador <= '" + Uteis.getDataJDBC(Uteis.getDateTime(dataFim, 23, 59, 59)) + " ')", adicionarAND);
                adicionarAND = true;
            }
        }
        filtros = filtros.replaceFirst("AND", "WHERE");
        selectStr += filtros;
        return selectStr;
    }

    @Override
    public String designIReportRelatorio(String tipoRelatorio, String layout) {
        if (tipoRelatorio.equals("AN")) {
            if (layout.equals("layout1")) {
                return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
            } else {
                return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeLayout2() + ".jrxml");
            }
        } else {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeSintetico() + ".jrxml");
        }
    }

    @Override
    public String caminhoBaseIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro");
    }

    public static String getIdEntidade() {
        return ("PagamentoRel");
    }

    public static String getIdEntidadeLayout2() {
        return ("PagamentoLayout2Rel");
    }

    public static String getIdEntidadeSintetico() {
        return ("PagamentoResumidoRel");
    }
    
	private void validarFormaPagamentoApresentar(PagamentoRelVO pagamentoRelVO) {
		StringBuilder formaPagamentoApresentar = new StringBuilder("");
		if (pagamentoRelVO.getFormaPagamentoDinheiro() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Dinheiro");
		}
		if (pagamentoRelVO.getFormaPagamentoDebito() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Débito");
		}
		if (pagamentoRelVO.getFormaPagamentoCartao() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Cartão");
		}
		if (pagamentoRelVO.getFormaPagamentoCheque() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Cheque");
		}
		if (pagamentoRelVO.getFormaPagamentoBoleto() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Boleto");
		}
		if (pagamentoRelVO.getFormaPagamentoIsencao() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Isenção");
		}
		if (pagamentoRelVO.getFormaPagamentoPermuta() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Permuta");
		}
		if (pagamentoRelVO.getFormaPagamentoCreditoDebito() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Crédito/Débito");
		}
		if (pagamentoRelVO.getFormaPagamentoDeposito() != null) {
			adicionarVirgulaQuebraLinha(formaPagamentoApresentar);
			formaPagamentoApresentar.append("Deposito");
		}
		pagamentoRelVO.setFormaPagamentoApresentar(formaPagamentoApresentar.toString());
	}

	private void adicionarVirgulaQuebraLinha(StringBuilder str) {
		if (str.toString().length() > 1) {
			str.append(",\n");
		}
	}
	
	private void validarContaCorrenteApresentar(PagamentoRelVO pagamentoRelVO) {
		String contaCorrenteApresentar = "";
		if (pagamentoRelVO.getFormaPagamentoPermuta() != null) {
			contaCorrenteApresentar = "";
		} else {
			if (pagamentoRelVO.getTipoContaCorrente()) {
				contaCorrenteApresentar = "CX - ";
			} else {
				contaCorrenteApresentar = "CC - ";
			}
			contaCorrenteApresentar += pagamentoRelVO.getContaCorrente() != null ? pagamentoRelVO.getContaCorrente() : "";
		}
		pagamentoRelVO.setContaCorrenteApresentar(contaCorrenteApresentar);
	}
}