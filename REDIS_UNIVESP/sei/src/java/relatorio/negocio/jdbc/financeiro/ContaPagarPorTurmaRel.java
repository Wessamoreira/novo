package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoSacado;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ContaPagarCategoriaDespesaRelVO;
import relatorio.negocio.comuns.financeiro.ContaPagarPorTurmaRelVO;
import relatorio.negocio.interfaces.financeiro.ContaPagarPorTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import relatorio.negocio.jdbc.financeiro.enumeradores.ContaPagarFiltrosEnum;

@Repository
@Scope("singleton")
@Lazy
public class ContaPagarPorTurmaRel extends SuperRelatorio implements ContaPagarPorTurmaRelInterfaceFacade {

    private static String idEntidade;

    public ContaPagarPorTurmaRel() {
        inicializarParametros();
    }

    @Override
    public List<ContaPagarPorTurmaRelVO> criarObjeto(ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaPagaCancelado) throws Exception {
    	
    	if (ContaPagarPorTurmaRelVO.getDataInicio() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataInicio"));
		}
		if (ContaPagarPorTurmaRelVO.getDataFim() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTermino"));
		}
		if (ContaPagarPorTurmaRelVO.getDataFim().before(ContaPagarPorTurmaRelVO.getDataInicio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTerminoMaiorDataInicio"));
		}
		
		if (filtroContaAPagar.equals("naoFiltrar") && filtroContaPaga.equals("naoFiltrar") && filtroContaPagaParcialmente.equals("naoFiltrar") && filtroContaPagaCancelado.equals("naoFiltrar")) {
			throw new ConsistirException(UteisJSF.internacionalizar("Deve ser informado pelo menos um filtro de CONTA A PAGAR/CONTA PAGA/CONTA PAGA PARCIALMENTE/CONTA CANCELADA diferente de NÃO FILTRAR."));
		}
			
    	
        Double valorTotal = 0.0;
        List<ContaPagarPorTurmaRelVO> ContaPagarPorTurmaRelVOs = new ArrayList<ContaPagarPorTurmaRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(ContaPagarPorTurmaRelVO, codigoTurma, filtroContaAPagar, filtroContaPaga, filtroContaPagaParcialmente, filtroContaPagaCancelado);
        while (dadosSQL.next()) {
            ContaPagarPorTurmaRelVOs.add(montarDados(dadosSQL));
            valorTotal += dadosSQL.getDouble("contapagar_valorpago");
        }

        if (!ContaPagarPorTurmaRelVOs.isEmpty()) {
            Map<String, ContaPagarCategoriaDespesaRelVO> mapaTemp = realizarCriacaoMapContaPagarCategoriaDespesaVO();

            for (ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO2 : ContaPagarPorTurmaRelVOs) {
                if (mapaTemp.containsKey(ContaPagarPorTurmaRelVO2.getIdentificadorCategoriaDespesa())) {
                    ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = mapaTemp.get(ContaPagarPorTurmaRelVO2.getIdentificadorCategoriaDespesa());
                    categoriaDespesaRelVO.setValor(categoriaDespesaRelVO.getValor() + ContaPagarPorTurmaRelVO2.getValorPago());
                    categoriaDespesaRelVO.setValorTotal(categoriaDespesaRelVO.getValorTotal() + ContaPagarPorTurmaRelVO2.getValorContaPagar());
                }
            }

            List<ContaPagarCategoriaDespesaRelVO> listaOrdenada = realizarOrdenacaoCategoriaDespesaVO(mapaTemp);
            listaOrdenada = realizarCalculoValoresContaPagarCategoriaDespesaRelVO(listaOrdenada);
            listaOrdenada = realizarCalculoValoresTotalContaPagarCategoriaDespesaRelVO(listaOrdenada);
            listaOrdenada = realizarCalculoPercentualContaPagarCategoriaDespesaRelVO(listaOrdenada, valorTotal);
            ContaPagarPorTurmaRelVO obj = ContaPagarPorTurmaRelVOs.get(ContaPagarPorTurmaRelVOs.size() - 1);
            obj.setListaContaPagarCategoriaDespesaRelVO(listaOrdenada);
        }

        return ContaPagarPorTurmaRelVOs;
    }
    
    public List<ContaPagarCategoriaDespesaRelVO> realizarCalculoValoresTotalContaPagarCategoriaDespesaRelVO(List<ContaPagarCategoriaDespesaRelVO> listaOrdenada) {
		int index = 0;
		for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO : listaOrdenada) {
			if (!contaPagarCategoriaDespesaRelVO.getValorTotalJaCalculado()) {
				contaPagarCategoriaDespesaRelVO.setValorTotal(contaPagarCategoriaDespesaRelVO.getValorTotal() + 
						executarCalculoValorTotalCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO));
				
				if (contaPagarCategoriaDespesaRelVO.getValorTotal() == 0.0) {
					listaOrdenada.remove(index);
					return realizarCalculoValoresTotalContaPagarCategoriaDespesaRelVO(listaOrdenada);
				}
				contaPagarCategoriaDespesaRelVO.setValorTotalJaCalculado(true);
			}
			index++;
		}
		return listaOrdenada;
	}
    
    public Double executarCalculoValorTotalCategoriaDespesaPrincipal(ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO) {
		Double valorTotal = 0.0;
		int index = 0;
		for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO2 : contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs()) {
			if (!contaPagarCategoriaDespesaRelVO2.getValorTotalJaCalculado()) {
				contaPagarCategoriaDespesaRelVO2.setValorTotal(contaPagarCategoriaDespesaRelVO2.getValorTotal() + executarCalculoValorTotalCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO2));
				valorTotal += contaPagarCategoriaDespesaRelVO2.getValorTotal();
				if (contaPagarCategoriaDespesaRelVO2.getValorTotal() == 0.0) {
					contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().remove(index);
					return valorTotal + executarCalculoValorTotalCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO);
				}
				contaPagarCategoriaDespesaRelVO2.setValorTotalJaCalculado(true);
			}
			index++;
		}
		return valorTotal;
	}


    public List<ContaPagarCategoriaDespesaRelVO> realizarCalculoPercentualContaPagarCategoriaDespesaRelVO(List<ContaPagarCategoriaDespesaRelVO> listaOrdenada, Double valorTotal) {
        Ordenacao.ordenarLista(listaOrdenada, "identificadorCategoriaDespesa");
        for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO : listaOrdenada) {
            contaPagarCategoriaDespesaRelVO.setPorcentagemValorTotal(contaPagarCategoriaDespesaRelVO.getValor() * 100 / valorTotal);
            executarCalculoPercentualCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO, contaPagarCategoriaDespesaRelVO.getValor(), 1, valorTotal);
        }
        return listaOrdenada;
    }

    public void executarCalculoPercentualCategoriaDespesaPrincipal(ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO, Double valor, Integer nivel, Double valorTotal) {
        Ordenacao.ordenarLista(contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs(), "identificadorCategoriaDespesa");
        for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO2 : contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs()) {
            contaPagarCategoriaDespesaRelVO2.setPorcentagemValorTotal(contaPagarCategoriaDespesaRelVO2.getValor() * 100 / valorTotal);
            for (int x = 1; x <= nivel; x++) {
                contaPagarCategoriaDespesaRelVO2.setDescricaoCategoriaDespesa("     " + contaPagarCategoriaDespesaRelVO2.getDescricaoCategoriaDespesa());
            }
            executarCalculoPercentualCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO2, contaPagarCategoriaDespesaRelVO2.getValor(),
                    nivel + 1, valorTotal);
        }
    }

    public List<ContaPagarCategoriaDespesaRelVO> realizarCalculoValoresContaPagarCategoriaDespesaRelVO(List<ContaPagarCategoriaDespesaRelVO> listaOrdenada) {
        int index = 0;
        for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO : listaOrdenada) {
            if (!contaPagarCategoriaDespesaRelVO.getValorJaCalculado()) {
                contaPagarCategoriaDespesaRelVO.setValor(contaPagarCategoriaDespesaRelVO.getValor() + executarCalculoValorCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO));

                if (contaPagarCategoriaDespesaRelVO.getValor() == 0.0) {
                    listaOrdenada.remove(index);
                    return realizarCalculoValoresContaPagarCategoriaDespesaRelVO(listaOrdenada);
                }
                contaPagarCategoriaDespesaRelVO.setValorJaCalculado(true);
            }
            index++;
        }
        return listaOrdenada;
    }

    public Double executarCalculoValorCategoriaDespesaPrincipal(ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO) {
        Double valorTotal = 0.0;
        int index = 0;
        for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO2 : contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs()) {
            if (!contaPagarCategoriaDespesaRelVO2.getValorJaCalculado()) {
                contaPagarCategoriaDespesaRelVO2.setValor(contaPagarCategoriaDespesaRelVO2.getValor()
                        + executarCalculoValorCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO2));
                valorTotal += contaPagarCategoriaDespesaRelVO2.getValor();
                if (contaPagarCategoriaDespesaRelVO2.getValor() == 0.0) {
                    contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().remove(index);
                    return valorTotal + executarCalculoValorCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO);
                }
                contaPagarCategoriaDespesaRelVO2.setValorJaCalculado(true);
            }
            index++;
        }
        return valorTotal;
    }

    public List<ContaPagarCategoriaDespesaRelVO> realizarOrdenacaoCategoriaDespesaVO(Map<String, ContaPagarCategoriaDespesaRelVO> mapa) throws Exception {

        List<ContaPagarCategoriaDespesaRelVO> listaOrdenada = new ArrayList<ContaPagarCategoriaDespesaRelVO>(0);

        for (ContaPagarCategoriaDespesaRelVO obj : mapa.values()) {
            if (obj.getCodigoCategoriaDespesaPrincipal() > 0) {
                listaOrdenada = adicionarObjContaPagarCategoriaDespesaRelVOs(obj, listaOrdenada);
            } else {
                listaOrdenada.add(obj);
            }

        }
        return listaOrdenada;
    }

    public List<ContaPagarCategoriaDespesaRelVO> adicionarObjContaPagarCategoriaDespesaRelVOs(ContaPagarCategoriaDespesaRelVO obj, List<ContaPagarCategoriaDespesaRelVO> listaOrdenada) {

        for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO : listaOrdenada) {
            if (contaPagarCategoriaDespesaRelVO.getCodigoCategoriaDespesa().equals(obj.getCodigoCategoriaDespesaPrincipal())) {
                contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().add(obj);
                return listaOrdenada;
            } else if (contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs() != null
                    && contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().size() > 0) {
                adicionarObjContaPagarCategoriaDespesaRelVOs(obj, contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs());
            }
        }
        return listaOrdenada;
    }

    public Map<String, ContaPagarCategoriaDespesaRelVO> realizarCriacaoMapContaPagarCategoriaDespesaVO() throws Exception {
        Map<String, ContaPagarCategoriaDespesaRelVO> lista = new LinkedHashMap<String, ContaPagarCategoriaDespesaRelVO>(0);

        StringBuilder sql = new StringBuilder();
        sql.append("select identificadorcategoriadespesa, descricao, codigo, categoriadespesaprincipal ");
        sql.append("from categoriadespesa order by categoriadespesaprincipal ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

        while (tabelaResultado.next()) {
            ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = montarDadosCategoriaDespesaVO(tabelaResultado);
            lista.put(categoriaDespesaRelVO.getIdentificadorCategoriaDespesa(), categoriaDespesaRelVO);
        }
        return lista;
    }

    public ContaPagarCategoriaDespesaRelVO montarDadosCategoriaDespesaVO(SqlRowSet dadosSQL) {
        ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = new ContaPagarCategoriaDespesaRelVO();

        categoriaDespesaRelVO.setIdentificadorCategoriaDespesa(dadosSQL.getString("identificadorcategoriadespesa"));
        categoriaDespesaRelVO.setDescricaoCategoriaDespesa(dadosSQL.getString("descricao"));
        categoriaDespesaRelVO.setCodigoCategoriaDespesa(dadosSQL.getInt("codigo"));
        categoriaDespesaRelVO.setCodigoCategoriaDespesaPrincipal(dadosSQL.getInt("categoriadespesaprincipal"));

        return categoriaDespesaRelVO;
    }

    private ContaPagarPorTurmaRelVO montarDados(SqlRowSet dadosSQL) {
        ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO = new ContaPagarPorTurmaRelVO();
        ContaPagarPorTurmaRelVO.setCodigoCategoriaDespesa(dadosSQL.getInt("codigocategoriadespesa"));
        ContaPagarPorTurmaRelVO.setIdentificadorCategoriaDespesa(dadosSQL.getString("identificadorcategoriadespesa"));
        ContaPagarPorTurmaRelVO.setCategoriaDespesaPrincipal(dadosSQL.getInt("categoriadespesaprincipal"));
        ContaPagarPorTurmaRelVO.setBanco(dadosSQL.getString("banco_nome"));
        ContaPagarPorTurmaRelVO.setCodigoContaPagar(dadosSQL.getInt("contapagar_codigo"));
        ContaPagarPorTurmaRelVO.setDataContaPagar(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_data")));
        ContaPagarPorTurmaRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_datavencimento")));
        ContaPagarPorTurmaRelVO.setDesconto(dadosSQL.getDouble("contapagar_desconto"));
        ContaPagarPorTurmaRelVO.setDescricaoCentroDespesa(dadosSQL.getString("centrodespesa_descricao"));
        ContaPagarPorTurmaRelVO.setNomeFornecedor(dadosSQL.getString("fornecedor_nome"));
        ContaPagarPorTurmaRelVO.setNomeFuncionario(dadosSQL.getString("funcionario_nome"));
        ContaPagarPorTurmaRelVO.setNomeBanco(dadosSQL.getString("banco_nome"));
        ContaPagarPorTurmaRelVO.setNomeResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
        ContaPagarPorTurmaRelVO.setNomeParceiro(dadosSQL.getString("parceiro_nome"));
        ContaPagarPorTurmaRelVO.setNomeAluno(dadosSQL.getString("aluno_nome"));
        
        ContaPagarPorTurmaRelVO.setFornecedor(dadosSQL.getString("fornecedor_nome"));
        ContaPagarPorTurmaRelVO.setFuncionario(dadosSQL.getString("funcionario_nome"));
        ContaPagarPorTurmaRelVO.setBanco(dadosSQL.getString("banco_nome"));
        ContaPagarPorTurmaRelVO.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
        ContaPagarPorTurmaRelVO.setParceiro(dadosSQL.getString("parceiro_nome"));
        ContaPagarPorTurmaRelVO.setAluno(dadosSQL.getString("aluno_nome"));
        ContaPagarPorTurmaRelVO.setJuro(dadosSQL.getDouble("contapagar_juro"));
        ContaPagarPorTurmaRelVO.setMulta(dadosSQL.getDouble("contapagar_multa"));
        ContaPagarPorTurmaRelVO.setNomeTurma(dadosSQL.getString("identificador_turma"));
        ContaPagarPorTurmaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
        ContaPagarPorTurmaRelVO.setNumeroDocumento(dadosSQL.getString("contapagar_nrdocumento"));
        ContaPagarPorTurmaRelVO.setSituacao(dadosSQL.getString("contapagar_situacao"));
        ContaPagarPorTurmaRelVO.setTipoSacado(dadosSQL.getString("contapagar_tiposacado"));
        ContaPagarPorTurmaRelVO.setUnidadeEnsino(dadosSQL.getInt("contapagar_unidadeensino"));
        ContaPagarPorTurmaRelVO.setValorContaPagar(dadosSQL.getDouble("contapagar_valor"));
        ContaPagarPorTurmaRelVO.setValorPago(dadosSQL.getDouble("contapagar_valorpago"));
        ContaPagarPorTurmaRelVO.setQuebra(dadosSQL.getString("contapagar_quebra"));
        ContaPagarPorTurmaRelVO.setListaContaPagarCategoriaDespesaRelVO(new ArrayList<ContaPagarCategoriaDespesaRelVO>(0));

        if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())){
            ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeFornecedor());
        }else if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())){
            ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeFuncionario());
        }else if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.ALUNO.getValor())){
            ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeAluno());
        }else if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.BANCO.getValor())){
            ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeBanco());
        }else if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())){
            ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeFornecedor());
        }else if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())){
            ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeParceiro());
        }else if(ContaPagarPorTurmaRelVO.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())){
            if(ContaPagarPorTurmaRelVO.getNomeAluno() != null && !ContaPagarPorTurmaRelVO.getNomeAluno().trim().isEmpty()){
                ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeAluno());
            }else{
                ContaPagarPorTurmaRelVO.setFavorecido(ContaPagarPorTurmaRelVO.getNomeResponsavelFinanceiro());
            }            
        }

        return ContaPagarPorTurmaRelVO;
    }

    public SqlRowSet executarConsultaParametrizada(ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaPagaCancelado) throws Exception {
        String selectStr = "SELECT distinct categoriadespesa.codigo as codigocategoriadespesa, categoriadespesa.identificadorcategoriadespesa as identificadorcategoriadespesa, "
                + "categoriadespesa.categoriadespesaprincipal as categoriadespesaprincipal, contapagar.codigo AS contapagar_codigo, contapagar.data AS contapagar_data, contapagar.nrdocumento AS contapagar_nrdocumento, "
                + "contapagar.situacao AS contapagar_situacao, contapagar.datavencimento AS contapagar_datavencimento, trunc(contapagar.valor::numeric,2) AS contapagar_valor, "
                +" trunc(contapagar.juro::numeric,2) AS contapagar_juro, trunc(contapagar.multa::numeric,2) AS contapagar_multa, trunc(contapagar.desconto::numeric,2) AS contapagar_desconto, trunc(contapagar.valorpago::numeric,2) AS contapagar_valorpago, "
                + "contapagar.tiposacado AS contapagar_tiposacado, categoriadespesa.descricao AS centrodespesa_descricao, contapagar.unidadeensino AS contapagar_unidadeensino, "
                + "contapagar.turma AS contapagar_turma, contapagar.funcionario AS contapagar_funcionario, contapagar.fornecedor AS contapagar_fornecedor, "
                + "contapagar.banco AS contapagar_banco, unidadeensino.nome AS unidadeensino_nome, turma.identificadorturma AS identificador_turma, pessoa.nome AS funcionario_nome, "

                + "fornecedor.nome AS fornecedor_nome, banco.nome AS banco_nome, responsavelFinanceiro.nome as responsavelFinanceiro_nome, aluno.nome as aluno_nome, parceiro.nome as parceiro_nome,  (CASE WHEN (contapagar.tiposacado = 'FO') THEN contapagar.tiposacado || contapagar.fornecedor ELSE "
                + "CASE WHEN (contapagar.tiposacado = 'FU') THEN contapagar.tiposacado || contapagar.funcionario ELSE "
                + "CASE WHEN (contapagar.tiposacado = 'AL') THEN contapagar.tiposacado || contapagar.pessoa ELSE "
                + "CASE WHEN (contapagar.tiposacado = 'RF') THEN contapagar.tiposacado || contapagar.responsavelFinanceiro ELSE "
                + "CASE WHEN (contapagar.tiposacado = 'PA') THEN contapagar.tiposacado || contapagar.parceiro ELSE "
                + "CASE WHEN (contapagar.tiposacado = 'BA') THEN contapagar.tiposacado || contapagar.banco ELSE contapagar.tiposacado || contapagar.fornecedor END END END END END END) AS contapagar_quebra "
                + "FROM ContaPagar AS contapagar LEFT JOIN categoriadespesa ON (contapagar.centrodespesa = categoriadespesa.codigo) "
                + "LEFT JOIN fornecedor ON (contapagar.fornecedor = fornecedor.codigo) LEFT JOIN funcionario ON (contapagar.funcionario = funcionario.codigo) "
                + "LEFT JOIN pessoa ON (funcionario.pessoa = pessoa.codigo) LEFT JOIN unidadeensino ON (contapagar.unidadeensino = unidadeensino.codigo) "
                + "LEFT JOIN pessoa as aluno ON (contapagar.pessoa = aluno.codigo)"
                + "LEFT JOIN parceiro ON (contapagar.parceiro = parceiro.codigo)"
                + "LEFT JOIN Pessoa as responsavelFinanceiro ON (contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo)"                
                + "LEFT JOIN banco ON (contapagar.banco = banco.codigo) "
                + "INNER JOIN centroresultadoorigem ON centroresultadoorigem.codOrigem = contapagar.codigo::TEXT  AND centroresultadoorigem.tipoCentroResultadoOrigem = '"+TipoCentroResultadoOrigemEnum.CONTA_PAGAR+"'"
		        + "INNER JOIN turma ON (centroresultadoorigem.turma = turma.codigo)  ";
        if (ContaPagarPorTurmaRelVO.getFiltroData().equals("dataPagamento")) {
            selectStr += " INNER JOIN contapagarnegociacaopagamento ON (contapagarnegociacaopagamento.contapagar = contapagar.codigo) ";
            selectStr += " INNER JOIN negociacaopagamento ON (contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo) ";
        } else if (filtroContaPaga.equals("negociacaopagamento.data")) {
            selectStr += " INNER JOIN contapagarnegociacaopagamento ON (contapagarnegociacaopagamento.contapagar = contapagar.codigo) ";
            selectStr += " INNER JOIN negociacaopagamento ON (contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo) ";
        } else if (filtroContaPagaParcialmente.equals("negociacaopagamento.data")) {
            selectStr += " INNER JOIN contapagarnegociacaopagamento ON (contapagarnegociacaopagamento.contapagar = contapagar.codigo) ";
            selectStr += " INNER JOIN negociacaopagamento ON (contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo) ";
        }
        selectStr = montarFiltrosRelatorio(selectStr, ContaPagarPorTurmaRelVO, codigoTurma, filtroContaAPagar, filtroContaPaga, filtroContaPagaParcialmente, filtroContaPagaCancelado);
        selectStr += " GROUP BY contapagar_codigo, contapagar_data, contapagar_nrdocumento, contapagar_situacao, contapagar_datavencimento, contapagar_valor, "
                + "contapagar_juro, contapagar_multa, contapagar_desconto, contapagar_valorpago, contapagar_tiposacado, centrodespesa_descricao, contapagar_quebra, "
                + "unidadeensino_nome, identificador_turma, contapagar_unidadeensino, contapagar_turma, contapagar_funcionario, contapagar_fornecedor, "
                + "contapagar_banco, funcionario_nome, fornecedor_nome, banco_nome, categoriadespesa.codigo, parceiro.nome, responsavelFinanceiro.nome, aluno.nome, categoriadespesa.categoriadespesaprincipal, categoriadespesa.identificadorcategoriadespesa ";

        

        if (ContaPagarPorTurmaRelVO.getOrdernarPor().equals("turma")) {
            selectStr += " order by identificador_turma, contapagar.data, contapagar.fornecedor, categoriadespesa.codigo ";

        } else {
            selectStr += " order by contapagar.data, fornecedor.nome, aluno.nome, responsavelFinanceiro.nome, pessoa.nome, banco.nome, parceiro.nome, categoriadespesa.codigo ";
        }
        // selectStr = montarOrdenacaoRelatorio(selectStr);

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    private String montarFiltrosRelatorio(String selectStr, ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaPagaCancelado) {
        String filtros = "";
        boolean adicionarAND = true;
        if ((ContaPagarPorTurmaRelVO.getCodigoCategoriaDespesa() != null) && (ContaPagarPorTurmaRelVO.getCodigoCategoriaDespesa().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.centrodespesa = " + ContaPagarPorTurmaRelVO.getCodigoCategoriaDespesa() + ")", adicionarAND);
            adicionarAND = true;
        }
        if ((ContaPagarPorTurmaRelVO.getUnidadeEnsino() != null) && (ContaPagarPorTurmaRelVO.getUnidadeEnsino().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.unidadeensino = " + ContaPagarPorTurmaRelVO.getUnidadeEnsino() + ")", adicionarAND);
            adicionarAND = true;
        }
        if ((codigoTurma != null) && (codigoTurma.intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( turma.codigo = " + codigoTurma.intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (ContaPagarPorTurmaRelVO.getFornecedor().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'FO' )", adicionarAND);
            adicionarAND = true;
        } else if ((ContaPagarPorTurmaRelVO.getFornecedor().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (ContaPagarPorTurmaRelVO.getCodigoFornecedor() != null)
                && (ContaPagarPorTurmaRelVO.getCodigoFornecedor().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.fornecedor = " + ContaPagarPorTurmaRelVO.getCodigoFornecedor().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (ContaPagarPorTurmaRelVO.getFuncionario().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'FU' )", adicionarAND);
        } else if ((ContaPagarPorTurmaRelVO.getFuncionario().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (ContaPagarPorTurmaRelVO.getCodigoFuncionario() != null)
                && (ContaPagarPorTurmaRelVO.getCodigoFuncionario().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.funcionario = " + ContaPagarPorTurmaRelVO.getCodigoFuncionario().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        
        if (ContaPagarPorTurmaRelVO.getAluno().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'AL' )", adicionarAND);
            adicionarAND = true;
        } else if ((ContaPagarPorTurmaRelVO.getAluno().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (ContaPagarPorTurmaRelVO.getCodigoAluno() != null)
                && (ContaPagarPorTurmaRelVO.getCodigoAluno().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.pessoa = " + ContaPagarPorTurmaRelVO.getCodigoAluno().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (ContaPagarPorTurmaRelVO.getParceiro().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'PA' )", adicionarAND);
            adicionarAND = true;
        } else if ((ContaPagarPorTurmaRelVO.getParceiro().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (ContaPagarPorTurmaRelVO.getCodigoParceiro() != null)
                && (ContaPagarPorTurmaRelVO.getCodigoParceiro().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.parceiro = " + ContaPagarPorTurmaRelVO.getCodigoParceiro().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (ContaPagarPorTurmaRelVO.getResponsavelFinanceiro().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'RF' )", adicionarAND);
            adicionarAND = true;
        } else if ((ContaPagarPorTurmaRelVO.getResponsavelFinanceiro().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (ContaPagarPorTurmaRelVO.getCodigoResponsavelFinanceiro() != null)
                && (ContaPagarPorTurmaRelVO.getCodigoResponsavelFinanceiro().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.responsavelFinanceiro = " + ContaPagarPorTurmaRelVO.getCodigoResponsavelFinanceiro().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        
        if (ContaPagarPorTurmaRelVO.getBanco().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'BA' )", adicionarAND);
        } else if ((ContaPagarPorTurmaRelVO.getBanco().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (ContaPagarPorTurmaRelVO.getCodigoBanco() != null)
                && (ContaPagarPorTurmaRelVO.getCodigoBanco().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( contapagar.banco = " + ContaPagarPorTurmaRelVO.getCodigoBanco().intValue() + ")", adicionarAND);
            adicionarAND = true;
        }
        if (filtroContaAPagar.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'AP')", adicionarAND);
			adicionarAND = true;
		}
		if (filtroContaPaga.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'PA')", adicionarAND);
			adicionarAND = true;
		}
		if (filtroContaPagaParcialmente.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'PP')", adicionarAND);
			adicionarAND = true;
		}
		if (filtroContaPagaCancelado.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'CF')", adicionarAND);
			adicionarAND = true;
		}
		StringBuilder filtroData = new StringBuilder("");

		if (ContaPagarPorTurmaRelVO.getDataInicio() != null && ContaPagarPorTurmaRelVO.getDataFim() != null) {
			if (!filtroContaAPagar.equals("naoFiltrar")) {
				filtroData.append("( ( contaPagar.situacao = 'AP' and ").append(filtroContaAPagar).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroContaAPagar).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPaga.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroContaPaga).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPagaParcialmente.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PP' and ").append(filtroContaPagaParcialmente).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroContaPagaParcialmente).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPagaCancelado.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'CF' and ").append(filtroContaPagaCancelado).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroContaPagaCancelado).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			filtroData.append(" ) ");

		} else if (ContaPagarPorTurmaRelVO.getDataInicio() != null) {
			if (!filtroContaAPagar.equals("naoFiltrar")) {
				filtroData.append("( ( contaPagar.situacao = 'AP' and ").append(filtroContaAPagar).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("') ");

			}
			if (!filtroContaPaga.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("') ");

			}
			if (!filtroContaPagaParcialmente.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PP' and ").append(filtroContaPagaParcialmente).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("') ");

			}
			if (!filtroContaPagaCancelado.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'CF' and ").append(filtroContaPagaCancelado).append("::DATE >= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataInicio())).append("') ");
				
			}
			filtroData.append(" ) ");

		

		} else if (ContaPagarPorTurmaRelVO.getDataFim() != null) {
			

			if (!filtroContaAPagar.equals("naoFiltrar")) {
				filtroData.append("( ( contaPagar.situacao = 'AP'  and ").append(filtroContaAPagar).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPaga.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPagaParcialmente.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PP' and ").append(filtroContaPagaParcialmente).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPagaCancelado.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'CF' and ").append(filtroContaPagaCancelado).append("::DATE <= '").append(Uteis.getDataJDBC(ContaPagarPorTurmaRelVO.getDataFim())).append("') ");
			}
			filtroData.append(" ) ");

		}
		if (!filtroData.toString().trim().isEmpty()) {
			filtros = adicionarCondicionalWhere(filtros, filtroData.toString(), adicionarAND);
		}
        filtros = filtros.replaceFirst("AND", "WHERE");
        selectStr += filtros;
        return selectStr;
    }

    protected String montarOrdenacaoRelatorio(String selectStr) {
        String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());

        if (ordenacao.equals("Nome")) {
            ordenacao = "fornecedor.nome";
        }
        if (ordenacao.equals("Data")) {
            ordenacao = "contapagar.data";
        }
        if (ordenacao.equals("Situação")) {
            ordenacao = "contapagar.situacao";
        }
        if (ordenacao.equals("Categoria de Despesa")) {
            ordenacao = "contapagar.centrodespesa";
        }
        if (ordenacao.equals("Unidade de Ensino")) {
            ordenacao = "contapagar.unidadeensino";
        }
        if (ordenacao.equals("Funcionário")) {
            ordenacao = "contapagar.funcionario";
        }
        if (ordenacao.equals("Fornecedor")) {
            ordenacao = "contapagar.fornecedor";
        }
        if (ordenacao.equals("Responsável Financeiro")) {
            ordenacao = "responsavelFinanceiro.nome";
        }
        if (ordenacao.equals("Parceiro")) {
            ordenacao = "parceiro.nome";
        }
        if (ordenacao.equals("Aluno")) {
            ordenacao = "aluno.nome";
        }
        if (ordenacao.equals("Banco")) {
            ordenacao = "contapagar.banco";
        }
        if (!ordenacao.equals("")) {
            if ("ContaPagarResumidaPorDataRel".equals(this.getIdEntidade())) {
                selectStr += " ORDER BY contapagar_datavencimento, contapagar_quebra, " + ordenacao;
            } else if ("ContaPagarResumidaPorFornecedorRel".equals(this.getIdEntidade())) {
                selectStr += " ORDER BY contapagar_quebra, contapagar_datavencimento, " + ordenacao;
            }
        }
        return selectStr;

    }

    @Override
    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return ("ContaPagarPorTurmaRel");
    }

    public String getNomeRelatorio() {
        return idEntidade;
    }

    public void setNomeRelatorio(String nome) {
        this.idEntidade = nome;
    }

    public void inicializarParametros() {
    }

    @Override
    public void validarDados(ContaPagarPorTurmaRelVO ContaPagarPorTurmaRelVO) throws ConsistirException {
        if (ContaPagarPorTurmaRelVO.getUnidadeEnsino() == null || ContaPagarPorTurmaRelVO.getUnidadeEnsino() == 0) {
            throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
        }
    }
}
