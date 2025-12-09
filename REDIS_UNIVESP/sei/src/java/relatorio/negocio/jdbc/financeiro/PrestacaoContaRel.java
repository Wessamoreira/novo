package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ContasRecebimentoRelVO;
import relatorio.negocio.comuns.financeiro.PrestacaoContaRelVO;
import relatorio.negocio.interfaces.financeiro.PrestacaoContaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PrestacaoContaRel extends SuperRelatorio implements PrestacaoContaRelInterfaceFacade {

    public PrestacaoContaRel() {
        inicializarOrdenacoesRelatorio();
    }

    @Override
    public List<PrestacaoContaRelVO> criarObjeto(Date dataInicio, Date dataFim, TurmaVO turma, boolean trazerContasConvenio, Integer ordenacao, String descricaoFiltros) throws Exception {
    	
        List<PrestacaoContaRelVO> PrestacaoContaRelVOs = new java.util.ArrayList<PrestacaoContaRelVO>(0);
        SqlRowSet tabelaResultado = executarConsultaParametrizada(dataInicio, dataFim, turma, trazerContasConvenio, ordenacao, descricaoFiltros);
        while (tabelaResultado.next()) {
            PrestacaoContaRelVOs.add(montarDados(tabelaResultado));
        }
        for (PrestacaoContaRelVO PrestacaoContaRelVO : PrestacaoContaRelVOs) {
            Double soma = 0.0;
			for (ContasRecebimentoRelVO contasRecebimentoRelVO : PrestacaoContaRelVO.getContasRecebimentoRelVOs()) {
				soma = soma + contasRecebimentoRelVO.getValorRecebido();
			}
			PrestacaoContaRelVO.setValorTotalRecebido(soma);
		}
        return PrestacaoContaRelVOs;
    }

    private PrestacaoContaRelVO montarDados(SqlRowSet dadosSQL) {
        PrestacaoContaRelVO PrestacaoContaRelVO = new PrestacaoContaRelVO();
        Double valorDescontoAlunoJaCalculado = 0.0;
        Double descontoInstituicao = 0.0;
        Double descontoConvenio = 0.0;
        Double valorDescontoProgressivo = 0.0;
        Double valorCalculadoDescontoLancadoRecebimento = 0.0;
        PrestacaoContaRelVO.setDescontoChancela(dadosSQL.getBoolean("descontochancela"));
        PrestacaoContaRelVO.setData(Uteis.getDataJDBC(dadosSQL.getDate("datavencimento")));
        PrestacaoContaRelVO.setDesconto(dadosSQL.getDouble("desconto"));
        PrestacaoContaRelVO.setDigito(dadosSQL.getString("digito"));
        PrestacaoContaRelVO.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
        PrestacaoContaRelVO.setNossoNumero(dadosSQL.getString("nossonumero"));
        PrestacaoContaRelVO.setNomeCurso(dadosSQL.getString("nomecurso"));
        PrestacaoContaRelVO.setNomeParceiro(dadosSQL.getString("nomeparceiro"));
        if(PrestacaoContaRelVO.getNomeParceiro() != null && !PrestacaoContaRelVO.getNomeParceiro().equals("")){
            PrestacaoContaRelVO.setNomeParceiro(dadosSQL.getString("pessoaParceiro") + " / " + PrestacaoContaRelVO.getNomeParceiro());
        }
        PrestacaoContaRelVO.setNomePessoa(dadosSQL.getString("nomepessoa"));
        PrestacaoContaRelVO.setNrDocumento(dadosSQL.getString("nrdocumento"));
        PrestacaoContaRelVO.setNumero(dadosSQL.getString("numero"));
        PrestacaoContaRelVO.setParcela(dadosSQL.getString("parcela"));
        PrestacaoContaRelVO.setTipoOrigem(TipoOrigemContaReceber.getDescricao(dadosSQL.getString("tipoorigem")));
        PrestacaoContaRelVO.setTipoPessoa(dadosSQL.getString("tipopessoa"));
        PrestacaoContaRelVO.setValor(dadosSQL.getDouble("valor"));
        PrestacaoContaRelVO.setValorRecebido(dadosSQL.getDouble("valorrecebido"));
        PrestacaoContaRelVO.setDataPagamento(Uteis.getDataJDBC(dadosSQL.getDate("datapagamento")));
        PrestacaoContaRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos"));
        PrestacaoContaRelVO.setObservacao(dadosSQL.getString("observacao"));
        PrestacaoContaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("nomeUnidadeEnsino"));
        PrestacaoContaRelVO.setInstituicaoChanceladora(dadosSQL.getString("instituicaoChanceladora"));

        String formaPagamento = dadosSQL.getString("formapgto");

        if (formaPagamento != null) {
            PrestacaoContaRelVO.setFormaPagamento(montarFormaPagamentos(formaPagamento));
        }

        if (dadosSQL.getString("tipochancela") != null && !dadosSQL.getString("tipochancela").equals("")) {
            Double porcentagem = dadosSQL.getDouble("porcentageminstituicaochancelado");
            Double valorInst = dadosSQL.getDouble("valorinstituicaochancelado");
            Boolean valorPorAluno = dadosSQL.getBoolean("valorporaluno");
            if (porcentagem != null && porcentagem > 0) {
                PrestacaoContaRelVO.setPorcentagemInstChancela(Integer.valueOf(Uteis.formatarDecimal(dadosSQL.getDouble("porcentageminstituicaochancelado"), "0")));
            }
            if (valorInst != null && valorInst > 0) {
                PrestacaoContaRelVO.setValorInstChancela(dadosSQL.getDouble("valorinstituicaochancelado"));
            }
            if (valorPorAluno != null) {
                PrestacaoContaRelVO.setValorPorAluno(valorPorAluno);
            }
        }
        valorDescontoAlunoJaCalculado = dadosSQL.getDouble("valorDescontoAlunoJaCalculado");
        descontoInstituicao = dadosSQL.getDouble("descontoInstituicao");
        descontoConvenio = dadosSQL.getDouble("descontoConvenio");
        valorDescontoProgressivo = dadosSQL.getDouble("valorDescontoProgressivo");
        valorCalculadoDescontoLancadoRecebimento = dadosSQL.getDouble("valorcalculadodescontolancadorecebimento");
        calcularDescontoRecebido(PrestacaoContaRelVO, valorDescontoAlunoJaCalculado, descontoInstituicao, descontoConvenio, valorDescontoProgressivo, valorCalculadoDescontoLancadoRecebimento);

        ContasRecebimentoRelVO contasRecebimentoRelVO = null;
        Map<Integer, Integer> hashMapCodigoConta = new HashMap<Integer, Integer>();
        hashMapCodigoConta.put(dadosSQL.getInt("contareceber_codigo"), null);
        do {
        	PrestacaoContaRelVO.setValorTotalRecebido(PrestacaoContaRelVO.getValorTotalRecebido() + dadosSQL.getDouble("valorrecebido"));
        	contasRecebimentoRelVO = new ContasRecebimentoRelVO();
        	contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("numero") + " - " + dadosSQL.getString("digito"));
        	contasRecebimentoRelVO.setFormaPagamento(montarFormaPagamentos(dadosSQL.getString("formapgto")));
        	contasRecebimentoRelVO.setValorRecebido(dadosSQL.getDouble("valorrecebido"));
        	if (hashMapCodigoConta.containsKey(dadosSQL.getInt("contareceber_codigo"))) {
        		PrestacaoContaRelVO.getContasRecebimentoRelVOs().add(contasRecebimentoRelVO);
			} else {
				break;
			}
        } while (dadosSQL.next());
        dadosSQL.previous();
        
        return PrestacaoContaRelVO;
    }

    public void calcularDescontoRecebido(PrestacaoContaRelVO PrestacaoContaRelVO, Double valorDescontoAlunoJaCalculado, Double descontoInstituicao, Double descontoConvenio, Double valorDescontoProgressivo, Double valorCalculadoDescontoLancadoRecebimento) {
        if (valorDescontoAlunoJaCalculado != null) {
            PrestacaoContaRelVO.setDescontoRecebido(PrestacaoContaRelVO.getDescontoRecebido() + valorDescontoAlunoJaCalculado);
        }
        if (descontoInstituicao != null) {
            PrestacaoContaRelVO.setDescontoRecebido(PrestacaoContaRelVO.getDescontoRecebido() + descontoInstituicao);
        }
        if (descontoConvenio != null) {
            PrestacaoContaRelVO.setDescontoRecebido(PrestacaoContaRelVO.getDescontoRecebido() + descontoConvenio);
        }
        if (valorDescontoProgressivo != null) {
            PrestacaoContaRelVO.setDescontoRecebido(PrestacaoContaRelVO.getDescontoRecebido() + valorDescontoProgressivo);
        }
        if (valorCalculadoDescontoLancadoRecebimento != null) {
            PrestacaoContaRelVO.setDescontoRecebido(PrestacaoContaRelVO.getDescontoRecebido() + valorCalculadoDescontoLancadoRecebimento);
        }
        PrestacaoContaRelVO.setDescontoRecebido(Uteis.arredondar(PrestacaoContaRelVO.getDescontoRecebido(), 2, 0));
    }



    public SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, TurmaVO turma, boolean trazerContasConvenio, Integer ordenacao, String descricaoFiltros) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" SELECT matricula.descontochancela, negociacaorecebimento.tipopessoa as tipopessoa,  negociacaorecebimento.data as data, negociacaorecebimento.observacao, contareceber.tipoorigem as tipoorigem, ");
        selectStr.append(" contareceber.nrdocumento as nrdocumento, contareceber.datavencimento, contareceber.valor as valor, crr.valorrecebimento as valorrecebido,  contareceber.valordesconto, ");
        selectStr.append(" contareceber.valorcalculadodescontolancadorecebimento, contareceber.descontoinstituicao, ");
        selectStr.append(" contareceber.descontoconvenio, contareceber.valordescontoprogressivo,  contareceber.valordesconto as desconto, contareceber.valorDescontoAlunoJaCalculado, ");
        selectStr.append(" contareceber.nossonumero, curso.nome as nomecurso, unidadeEnsino.nome as nomeUnidadeEnsino,  contareceber.parcela as parcela, ");
        selectStr.append(" contareceber.valorDescontoCalculadoPrimeiraFaixaDescontos, contacorrente.numero as numero, contacorrente.digito as digito, ");
        selectStr.append(" pessoa.nome as nomepessoa, parceiro.nome as nomeparceiro, turma.identificadorturma as identificadorturma, ");
        selectStr.append(" negociacaorecebimento.data as datapagamento, formapagamento.tipo AS formapgto, turma.tipochancela as tipochancela, crr.contareceber as contareceber_codigo, ");
        selectStr.append(" turma.valorfixochancela as valorinstituicaochancelado, turma.porcentagemchancela as porcentageminstituicaochancelado, turma.valorporaluno as valorporaluno, chancela.instituicaoChanceladora as instituicaoChanceladora, p2.nome as pessoaParceiro ");
        selectStr.append(" FROM negociacaorecebimento ");
        selectStr.append(" INNER JOIN contarecebernegociacaorecebimento ON (negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento) ");
        selectStr.append(" INNER JOIN contareceber ON (contarecebernegociacaorecebimento.contareceber = contareceber.codigo) ");
        selectStr.append(" INNER JOIN matricula ON contareceber.matriculaaluno = matricula.matricula ");
        selectStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
        selectStr.append(" INNER JOIN curso ON matricula.curso = curso.codigo ");
        selectStr.append(" LEFT JOIN parceiro ON (negociacaorecebimento.parceiro = parceiro.codigo) ");
        selectStr.append(" LEFT JOIN contacorrente ON (negociacaorecebimento.contacorrentecaixa = contacorrente.codigo) ");
        
//        selectStr.append(" LEFT JOIN matriculaperiodo on (contareceber.matriculaperiodo = matriculaperiodo.codigo or matriculaperiodo.codigo =  (select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula limit 1) ) ");
        
        selectStr.append(" LEFT JOIN matriculaperiodo on matriculaperiodo.codigo = ( case when contareceber.matriculaperiodo is not null then (contareceber.matriculaperiodo) ");
		selectStr.append(" else ((select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula limit 1) ) end) ");
        
        selectStr.append(" INNER JOIN turma ON (case when (matriculaperiodo.turma is null) then turma.codigo = (select matriculaperiodoturmadisciplina.turma from matriculaperiodoturmadisciplina ");
        selectStr.append(" where matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and turma.codigo = matriculaperiodoturmadisciplina.turma");
        selectStr.append(" ) else   matriculaperiodo.turma = turma.codigo end)  ");
        selectStr.append(" LEFT JOIN chancela ON chancela.codigo = turma.chancela ");
        selectStr.append(" INNER JOIN formapagamentonegociacaorecebimento ON (formapagamentonegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) ");
        selectStr.append(" INNER JOIN formapagamento ON (formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento) ");
        if (trazerContasConvenio) {
            selectStr.append(" LEFT JOIN pessoa ON (negociacaorecebimento.pessoa = pessoa.codigo) ");
        } else {
            selectStr.append(" INNER JOIN pessoa ON (negociacaorecebimento.pessoa = pessoa.codigo) ");
        }
        selectStr.append(" LEFT JOIN pessoa p2 ON p2.codigo = matricula.aluno ");
        selectStr.append(" INNER JOIN contareceberrecebimento crr ON formapagamentonegociacaorecebimento.codigo = crr.formapagamentonegociacaorecebimento AND contareceber.codigo = crr.contareceber ");
        //Filtros
        selectStr.append(" where contareceber.tipoorigem in ('BCC', 'MAT', 'MEN', 'NCR','OUT')  ");
        if (turma.getCodigo() != null && turma.getCodigo() != 0) {
            selectStr.append( " and ( turma.codigo = ");
            selectStr.append(turma.getCodigo());
            selectStr.append(")");
        }
        if (dataInicio != null) {
            selectStr.append(" and ( negociacaorecebimento.data::Date >= '");
            selectStr.append(Uteis.getDataJDBC(dataInicio));
            selectStr.append("')");
        }
        if (dataFim != null) {
            selectStr.append(" and ( negociacaorecebimento.data::Date <= '");
            selectStr.append(Uteis.getDataJDBC(dataFim));
            selectStr.append("')");
        }
        selectStr.append(" GROUP BY nomecurso,  identificadorturma, nomepessoa,  nomeparceiro, negociacaorecebimento.tipopessoa, negociacaorecebimento.data, negociacaorecebimento.observacao, tipoorigem, contareceber.nrdocumento, valor,  valorrecebido, ");
        selectStr.append("desconto,  parcela, valorDescontoCalculadoPrimeiraFaixaDescontos, contacorrente.numero, digito, datapagamento, formapgto, nossonumero, contareceber.datavencimento, nomeUnidadeEnsino, contareceber_codigo, ");
        selectStr.append(" contareceber.valordesconto, contareceber.valorDescontoAlunoJaCalculado, contareceber.valorcalculadodescontolancadorecebimento, contareceber.descontoinstituicao, contareceber.descontoconvenio, contareceber.valordescontoprogressivo, ");
        selectStr.append(" turma.tipochancela, turma.valorfixochancela, turma.porcentagemchancela, turma.valorporaluno, matricula.descontochancela, chancela.instituicaoChanceladora, ");
        selectStr.append(" crr.valorrecebimento, pessoaParceiro ");
        //Ordenação
        selectStr.append(" ORDER BY ");
        if(ordenacao == 1){
            selectStr.append(" negociacaorecebimento.data ");
        }else{
            if (trazerContasConvenio) {
                selectStr.append(" pessoaParceiro, ");
            }
            selectStr.append(" pessoa.nome ");            
        }
        selectStr.append(" , contareceber_codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }



    private String montarFormaPagamentos(String enumFormaPgto) {
        if (enumFormaPgto.equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor())) {
            return "Boleto";
        }
        if (enumFormaPgto.equals(TipoFormaPagamento.CHEQUE.getValor())) {
            return "Cheque";
        }
        if (enumFormaPgto.equals(TipoFormaPagamento.DINHEIRO.getValor())) {
            return "Dinheiro";
        }
        if (enumFormaPgto.equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
            return "Cartão";
        }
        if (enumFormaPgto.equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor())) {
            return "Débito";
        }
        if (enumFormaPgto.equals(TipoFormaPagamento.DEPOSITO.getValor())) {
            return "Depósito";
        }
        return "";
    }

    public String getDesignIReportRelatorio(String layout) {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + layout + ".jrxml");
    }

    public String caminhoBaseIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return ("PrestacaoConta2Rel");
    }

    public void inicializarOrdenacoesRelatorio() {
        Vector ordenacao = this.getOrdenacoesRelatorio();
        ordenacao.add("Data");
        ordenacao.add("Aluno");
    }

    @Override
    public void validarDados(Integer codigoTurma) throws ConsistirException {
        if (codigoTurma == null || codigoTurma == 0) {
            throw new ConsistirException("A TURMA deve ser informada para a geração do relatório.");
        }
    }
}
