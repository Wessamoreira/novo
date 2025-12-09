package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ContaReceberRelVO;
import relatorio.negocio.interfaces.financeiro.ContaReceberPorTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ContaReceberPorTurmaRel extends SuperRelatorio implements ContaReceberPorTurmaRelInterfaceFacade {

    public ContaReceberPorTurmaRel() {
        inicializarOrdenacoesRelatorio();
    }

    public List<ContaReceberRelVO> criarObjeto(Boolean dataCompetencia, TurmaVO turmaVO, Date dataInicio, Date dataFim, String situacaoContaReceber, UsuarioVO usuarioLogado, ConfiguracaoFinanceiroVO confFinanVO) throws Exception {
        List<ContaReceberRelVO> contaReceberRelVOs = new ArrayList<ContaReceberRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(dataCompetencia, turmaVO, dataInicio, dataFim, situacaoContaReceber);
        while (dadosSQL.next()) {
            contaReceberRelVOs.add(montarDados(dadosSQL, usuarioLogado, confFinanVO));
        }
        return contaReceberRelVOs;
    }

    private ContaReceberRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioLogado, ConfiguracaoFinanceiroVO confFinanVO) throws Exception {
        ContaReceberRelVO contaReceberRelVO = new ContaReceberRelVO();
        contaReceberRelVO.setMatricula(dadosSQL.getString("matricula"));
        contaReceberRelVO.setTipoPessoa(dadosSQL.getString("contareceber_tipopessoa"));
        contaReceberRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_datavencimento")));
        contaReceberRelVO.setCodigo(dadosSQL.getInt("contareceber_codigo"));
        contaReceberRelVO.setTipoOrigem(TipoOrigemContaReceber.getDescricao(dadosSQL.getString("contareceber_tipoorigem")));
        contaReceberRelVO.setNrDocumento(dadosSQL.getString("contareceber_nrdocumento"));
        contaReceberRelVO.setValor(dadosSQL.getDouble("contareceber_valor"));
        contaReceberRelVO.setSituacao(dadosSQL.getString("contareceber_situacao"));
        if(contaReceberRelVO.getSituacao().equals("AR")){
            contaReceberRelVO.setValorDesconto(0.0);
        }else{
            contaReceberRelVO.setValorDesconto(dadosSQL.getDouble("contareceber_desconto"));
        }
		if (contaReceberRelVO.getTipoOrigem().equals("BCC")) {
			contaReceberRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("contareceber_valordescontocalculadoprimeirafaixadescontos"));
		} else {
			contaReceberRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("valordescontocalculadoprimeirafaixadescontos"));
		}
        contaReceberRelVO.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
        contaReceberRelVO.setJuro(dadosSQL.getDouble("contareceber_juro"));
        contaReceberRelVO.setMulta(dadosSQL.getDouble("contareceber_multa"));
        contaReceberRelVO.setAcrescimo(dadosSQL.getDouble("contareceber_acrescimo"));
        contaReceberRelVO.setParcela(dadosSQL.getString("contareceber_parcela"));
        contaReceberRelVO.setNumeroContaCorrente(dadosSQL.getString("contacorrente_numero"));
        contaReceberRelVO.setDigito(dadosSQL.getString("contacorrente_digito"));
        contaReceberRelVO.setNomeTurma(dadosSQL.getString("turma_identificadorturma"));
        contaReceberRelVO.setNomePessoa(dadosSQL.getString("pessoa_parceiro"));

        contaReceberRelVO.setValorAcrescimoJuroMulta(contaReceberRelVO.getJuro() + contaReceberRelVO.getMulta() + contaReceberRelVO.getAcrescimo());
        calcularJuroMulta(contaReceberRelVO.getDataVencimento(), contaReceberRelVO, dadosSQL, usuarioLogado, confFinanVO);
        
        return contaReceberRelVO;
    }

    public SqlRowSet executarConsultaParametrizada(Boolean dataCompetencia, TurmaVO turmaVO, Date dataInicio, Date dataFim, String situacaoContaReceber) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" SELECT matriculaperiodo.matricula as matricula, trunc(matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos::NUMERIC, 2) AS valordescontocalculadoprimeirafaixadescontos, contareceber.tipopessoa as contareceber_tipopessoa, contareceber.datavencimento as contareceber_datavencimento,");
        selectStr.append(" contareceber.codigo as contareceber_codigo,  contareceber.tipoorigem as contareceber_tipoorigem, contareceber.situacao as contareceber_situacao,");
        selectStr.append(" contareceber.nrdocumento as contareceber_nrdocumento,  trunc(contareceber.valor::NUMERIC,2) as contareceber_valor,");
        selectStr.append(" trunc(contareceber.valorrecebido::NUMERIC,2) as contareceber_valorrecebido,  trunc(contareceber.multa::NUMERIC,2) as contareceber_multa,");
        selectStr.append(" trunc(contareceber.juro::NUMERIC,2) as contareceber_juro, trunc(contareceber.acrescimo::NUMERIC,2) as contareceber_acrescimo, ");
        selectStr.append(" case when (valordescontoalunojacalculado is null) then 0.0 else trunc(valordescontoalunojacalculado::NUMERIC,2) end +");
        selectStr.append(" case when (descontoconvenio is null) then 0.0 else trunc(descontoconvenio::NUMERIC,2) end +");
        selectStr.append(" case when (descontoinstituicao is null) then 0.0 else trunc(descontoinstituicao::NUMERIC,2) end +");
        selectStr.append(" case when (valordescontoprogressivo is null) then 0.0 else trunc(valordescontoprogressivo::NUMERIC,2) end +");
        selectStr.append(" case when (valorCalculadoDescontoLancadoRecebimento is null) then 0.0 else trunc(valorCalculadoDescontoLancadoRecebimento::NUMERIC,2) end as contareceber_desconto, ");
        selectStr.append(" contareceber.parcela as contareceber_parcela,  contacorrente.numero as contacorrente_numero,");
        selectStr.append(" contacorrente.digito as contacorrente_digito,");
        selectStr.append(" turma.identificadorturma as turma_identificadorturma,");
        selectStr.append(" (coalesce((pessoa.nome), '') || coalesce((parceiro.nome), '')) as pessoa_parceiro, contareceber.valordescontocalculadoprimeirafaixadescontos as contareceber_valordescontocalculadoprimeirafaixadescontos ");
        selectStr.append(" FROM contareceber");
        selectStr.append(" LEFT JOIN pessoa ON (contareceber.pessoa = pessoa.codigo)");
        selectStr.append(" LEFT JOIN convenio ON (contareceber.convenio = convenio.codigo)");
        selectStr.append(" LEFT JOIN contarecebernegociacaorecebimento ON (contareceber.codigo = contarecebernegociacaorecebimento.contareceber)");
        selectStr.append(" LEFT JOIN negociacaorecebimento ON (negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento)");
        selectStr.append(" LEFT JOIN parceiro ON ( convenio.parceiro = parceiro.codigo)");
        selectStr.append(" LEFT JOIN contacorrente ON (contareceber.contacorrente = contacorrente.codigo)");
        selectStr.append(" INNER JOIN matriculaperiodo ON  matriculaperiodo.codigo = contareceber.matriculaperiodo ");
        selectStr.append(" LEFT JOIN matriculaperiodovencimento ON matriculaperiodovencimento.contareceber = contareceber.codigo ");
        selectStr.append(" INNER JOIN turma ON (case when (matriculaperiodo.turma is null) then turma.codigo = (select matriculaperiodoturmadisciplina.turma from matriculaperiodoturmadisciplina ");
        selectStr.append(" where matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and turma.codigo = matriculaperiodoturmadisciplina.turma");
        selectStr.append(" ) else   matriculaperiodo.turma = turma.codigo end)  ");
//		selectStr.append(" LEFT JOIN matriculaperiodo ON (cast (contareceber.codorigem as integer) = matriculaperiodo.codigo)");
//		selectStr.append(" LEFT JOIN matriculaperiodoturmadisciplina ON (matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo)");
//		selectStr.append(" LEFT JOIN turma ON (matriculaperiodoturmadisciplina.turma = turma.codigo)");
        selectStr.append(montarFiltrosRelatorio(dataCompetencia, turmaVO, dataInicio, dataFim, situacaoContaReceber));
        selectStr.append(" GROUP BY ");
        selectStr.append(" contareceber_tipopessoa,");
        selectStr.append(" contareceber_datavencimento,");
        selectStr.append(" contareceber_codigo,");
        selectStr.append(" contareceber_tipoorigem,");
        selectStr.append(" contareceber_situacao,");
        selectStr.append(" contareceber_nrdocumento,");
        selectStr.append(" contareceber_valor,");
        selectStr.append(" contareceber_valorrecebido,");
        selectStr.append(" contareceber_multa,");
        selectStr.append(" contareceber_juro,");
        selectStr.append(" contareceber_acrescimo,");
        selectStr.append(" contareceber_desconto,");
        selectStr.append(" contareceber_parcela,");
        selectStr.append(" contacorrente_numero,");
        selectStr.append(" contacorrente_digito,");
        selectStr.append(" pessoa_parceiro,");
        selectStr.append(" turma_identificadorturma, ");
        selectStr.append(" matriculaperiodo.matricula, ");
        selectStr.append(" matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos ");
        selectStr.append(montarOrdenacaoRelatorio());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    private String montarFiltrosRelatorio(Boolean dataCompetencia, TurmaVO turmaVO, Date dataInicio, Date dataFim, String situacaoContaReceber) {
        String condicao = "";
        String where = " where ";
        situacaoContaReceber = "receber";
        condicao += where + "((contareceber.tipoorigem = 'BCC') or (contareceber.tipoorigem = 'MAT') or (contareceber.tipoorigem = 'MEN') or (contareceber.tipoorigem = 'NCR') or (contareceber.tipoorigem = 'OUT'))";
        where = " and ";
        if (turmaVO != null && turmaVO.getCodigo() != 0) {
            condicao += where + "( turma.codigo = " + turmaVO.getCodigo() + ")";
            where = " and ";
        }
        if (situacaoContaReceber.equals("receber")) {
        	if (!dataCompetencia) {
	            if (dataInicio != null) {
	                condicao += where + "( contareceber.datavencimento >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
	                where = " and ";
	            }
	            if (dataFim != null) {
	                condicao += where + "( contareceber.datavencimento <= '" + Uteis.getDataJDBC(dataFim) + " ')";
	                where = " and ";
	            }
        	} else {
	            if (dataInicio != null) {
	                condicao += where + "( contareceber.datacompetencia >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
	                where = " and ";
	            }
	            if (dataFim != null) {
	                condicao += where + "( contareceber.datacompetencia <= '" + Uteis.getDataJDBC(dataFim) + " ')";
	                where = " and ";
	            }
        	}
        } else if (situacaoContaReceber.equals("recebidos")) {
        	if (!dataCompetencia) {
	            if (dataInicio != null) {
	                condicao += where + "( negociacaorecebimento.data::Date >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
	                where = " and ";
	            }
	            if (dataFim != null) {
	                condicao += where + "( negociacaorecebimento.data::Date <= '" + Uteis.getDataJDBC(dataFim) + " ')";
	                where = " and ";
	            }
        	} else {
                if (dataInicio != null) {
                    condicao += where + "( contareceber.datacompetencia::Date >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
                    where = " and ";
                }
                if (dataFim != null) {
                    condicao += where + "( contareceber.datacompetencia::Date <= '" + Uteis.getDataJDBC(dataFim) + " ')";
                    where = " and ";
                }
        	}
        } else {
            where = " and ( ( ";
            if (!dataCompetencia) {
	            if (dataInicio != null) {
	                condicao += where + "( contareceber.datavencimento >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
	                where = " and ";
	            }
	            if (dataFim != null) {
	                condicao += where + "( contareceber.datavencimento <= '" + Uteis.getDataJDBC(dataFim) + " ')";                
	            }
            } else {
                if (dataInicio != null) {
                    condicao += where + "( contareceber.datacompetencia >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
                    where = " and ";
                }
                if (dataFim != null) {
                    condicao += where + "( contareceber.datacompetencia <= '" + Uteis.getDataJDBC(dataFim) + " ')";                
                }
            }
            where = ") or (";
            if (!dataCompetencia) {
	            if (dataInicio != null) {
	                condicao += where + "( negociacaorecebimento.data::Date >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
	                where = " and ";
	            }
	            if (dataFim != null) {
	                condicao += where + "( negociacaorecebimento.data::Date <= '" + Uteis.getDataJDBC(dataFim) + " ')";
	                where = " and ";
	            }
            } else {
	            if (dataInicio != null) {
	                condicao += where + "( contareceber.datacompetencia::Date >= '" + Uteis.getDataJDBC(dataInicio) + " ')";
	                where = " and ";
	            }
	            if (dataFim != null) {
	                condicao += where + "( contareceber.datacompetencia::Date <= '" + Uteis.getDataJDBC(dataFim) + " ')";
	                where = " and ";
	            }
            }
            if(where.trim().startsWith("and")){
                condicao += "))";
            }
        }
        if (situacaoContaReceber.equals("recebidos")) {
            condicao += where + "( contareceber.situacao = 'RE')";
            where = " and ";
        } else if (situacaoContaReceber.equals("receber")) {
            condicao += where + "( contareceber.situacao = 'AR')";
            where = " and ";
        }
        return condicao;
    }

    private String montarVinculoEntreTabelas(String selectStr) {
        String vinculos = "";
        vinculos = adicionarCondicionalWhere(vinculos, "(negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento)", false);

        if (!vinculos.equals("")) {
            if (selectStr.indexOf("WHERE") == -1) {
                selectStr = selectStr + " WHERE " + vinculos;
            } else {
                selectStr = selectStr + " WHERE " + vinculos;
            }
        }
        return selectStr;
    }

    protected String montarOrdenacaoRelatorio() {
        String selectStr = "";
        String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());

        if (ordenacao.equals("Data")) {
            ordenacao = "contareceber.datavencimento";
        }
        if (ordenacao.equals("Nome")) {
            ordenacao = "pessoa_parceiro";
        }
        if (!ordenacao.equals("")) {
            selectStr = selectStr + " ORDER BY " + ordenacao;
        }
        return selectStr;

    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignIReportRelatorioExcel() {    
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }


    public static String getIdEntidade() {
        return ("ContaReceberPorTurmaRel");
    }

    public static String getIdEntidadeExcel() {    
        return ("ContaReceberPorTurmaExcelRel");
    }

    public void inicializarOrdenacoesRelatorio() {
        Vector ordenacao = this.getOrdenacoesRelatorio();
        ordenacao.add("Data");
        ordenacao.add("Nome");
    }
    
    public void calcularJuroMulta(Date dataVencimento, ContaReceberRelVO contaReceberRelVO, SqlRowSet tabelaResultado, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO confFinanVO) throws Exception {
        Double valorFinal = 0.0;
        valorFinal = contaReceberRelVO.getValor();
        Long diasAtraso = 0L;
        diasAtraso = Uteis.nrDiasEntreDatas(new Date(), dataVencimento);
        if (diasAtraso > 0) {
            if (contaReceberRelVO.getMultaPorcentagem() == 0) {
            	contaReceberRelVO.setMultaPorcentagem(confFinanVO.getPercentualMultaPadrao());
            }
            double valorComMulta = (valorFinal * (contaReceberRelVO.getMultaPorcentagem()) / 100);
            contaReceberRelVO.setMulta(valorComMulta);
                if (contaReceberRelVO.getJuroPorcentagem() == 0) {
                	contaReceberRelVO.setJuroPorcentagem(confFinanVO.getPercentualJuroPadrao());
                }
                double atraso = (diasAtraso.doubleValue() / 30);
                double valorComJuro = (valorFinal * Math.pow(((contaReceberRelVO.getJuroPorcentagem() / 100) + 1), Uteis.arrendondarForcando2CadasDecimais(atraso))) - valorFinal;
                contaReceberRelVO.setJuro(Uteis.arrendondarForcando2CadasDecimais(valorComJuro));
        } else {
        	contaReceberRelVO.setJuro(tabelaResultado.getDouble("contareceber_juro"));
        	contaReceberRelVO.setMulta(tabelaResultado.getDouble("contareceber_multa"));
        }

    }
    
}
