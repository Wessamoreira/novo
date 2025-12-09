/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

/**
 * 
 * @author Otimize-TI
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.utilitarias.Uteis;

public class OpcaoPagamentoDividaVO implements Serializable {

    private Double entrada;
    private Double valorTotal;
    private Double desconto;
    private String parcelas;
    private List<ContaReceberVO> listaCondicaoPagamentoVOs;
    public static final long serialVersionUID = 1L;

    public OpcaoPagamentoDividaVO() {
        this.entrada = 0.0;
        this.parcelas = "";
        this.valorTotal = 0.0;
        this.desconto = 0.0;
    }

    public void montarListaCondicaoPagamento(Double valorTotal, Double desconto, List condicaoPagamentos, UnidadeEnsinoVO unidadeEnsino, String matricula, Integer pessoa, String tipoPessoa, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        setListaCondicaoPagamentoVOs(new ArrayList<ContaReceberVO>());
        setDesconto(desconto);
        setValorTotal(valorTotal);
        valorTotal = Uteis.arrendondarForcando2CadasDecimais(valorTotal - desconto);
        int tam = condicaoPagamentos.size();
        Iterator i = condicaoPagamentos.iterator();
        while (i.hasNext()) {
            ParcelaCondicaoPagamentoVO parcelaCondicaoPagamento = (ParcelaCondicaoPagamentoVO) i.next();
            String nrParcela = String.valueOf(parcelaCondicaoPagamento.getNumeroParcela() + "/" + tam);
            Date dataVencimento = Uteis.obterDataFutura(new Date(), parcelaCondicaoPagamento.getIntervalo());
            Double valorParcela = Uteis.arrendondarForcando2CadasDecimais(valorTotal
                    * (parcelaCondicaoPagamento.getPercentualValor() / 100));

            if (parcelaCondicaoPagamento.getIntervalo().intValue() == 0) {
                setEntrada(valorParcela);
                adiconarParcelas(valorParcela, true, parcelaCondicaoPagamento.getNumeroParcela());
            } else {
                adiconarParcelas(valorParcela, false, parcelaCondicaoPagamento.getNumeroParcela());
            }
            String descricao = "Negociação de dívida";
            String nrDoc = String.valueOf(Uteis.getDiaMesData(new Date())) + Uteis.getMesReferenciaData(new Date())
                    + nrParcela + pessoa + unidadeEnsino;
            getListaCondicaoPagamentoVOs().add(montarDadosContaReceber(configuracaoFinanceiroVO, matricula, pessoa, unidadeEnsino, nrDoc, descricao, nrParcela, valorParcela, dataVencimento, tipoPessoa));
        }
    }

    public void adiconarParcelas(Double valor, Boolean entrada, Integer nrParcela) {
        if (entrada) {
            parcelas += "Entrada " + valor;
            return;
        }
        if (!entrada && nrParcela.intValue() == 1) {
            parcelas += "Entrada 0,00";
        }
        if (parcelas.indexOf("x " + valor) != -1) {
            String subString = "";
            int fim = parcelas.indexOf("x " + valor);
            int cont = 1;
            while (subString.equals("")) {
                subString = parcelas.substring(fim - cont, fim);
                if (subString.startsWith(" ")) {
                    cont--;
                    break;
                } else {
                    subString = "";
                }
                cont++;
            }
            subString = parcelas.substring(fim - cont, fim);
            Integer nrparcela = Integer.parseInt(subString);
            nrparcela++;
            String primeiraParte = parcelas.substring(0, fim - cont);
            String segundaParte = parcelas.substring(fim, parcelas.length());
            parcelas = primeiraParte + nrparcela + segundaParte;
        } else {
            parcelas += " + 1x " + valor;
        }

    }

    public ContaReceberVO montarDadosContaReceber(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String matricula, Integer pessoa, UnidadeEnsinoVO unidadeEnsino, String nrDocumento, String descricao, String parcela, Double valor, Date dataVencimento, String tipoPessoa) {
        ContaReceberVO contaReceberVO = new ContaReceberVO();
        contaReceberVO.getCentroReceita().setCodigo(configuracaoFinanceiroVO.getCentroReceitaNegociacaoPadrao().getCodigo());
        contaReceberVO.setData(new Date());
        if (tipoPessoa.equals("AL")) {
            contaReceberVO.getMatriculaAluno().setMatricula(matricula);
            contaReceberVO.getMatriculaAluno().getAluno().setCodigo(pessoa);
        }
        if (tipoPessoa.equals("FU")) {
            contaReceberVO.getFuncionario().setMatricula(matricula);
            contaReceberVO.getFuncionario().getPessoa().setCodigo(pessoa);
        }
        contaReceberVO.setSituacao("AR");
        contaReceberVO.setTipoPessoa(tipoPessoa);
        contaReceberVO.setParcela(parcela);
        contaReceberVO.setCodOrigem(matricula);
        contaReceberVO.setTipoOrigem("AD");
        
        if (Uteis.isAtributoPreenchido(unidadeEnsino.getContaCorrentePadraoNegociacao())) {
			contaReceberVO.setContaCorrente(unidadeEnsino.getContaCorrentePadraoNegociacao());
		} else {
			contaReceberVO.setContaCorrente(configuracaoFinanceiroVO.getContaCorrentePadraoNegociacao());
		}
        
        contaReceberVO.getPessoa().setCodigo(pessoa);
        contaReceberVO.getUnidadeEnsino().setCodigo(unidadeEnsino.getCodigo());
        contaReceberVO.setDescricaoPagamento(descricao);
        contaReceberVO.setValor(valor);
        contaReceberVO.setDataVencimento(dataVencimento);
        contaReceberVO.setNrDocumento(nrDocumento);
        contaReceberVO.getDescontoProgressivo().setCodigo(0);
        return contaReceberVO;
    }

    public Double getValorSemDesconto() {
        return Uteis.arrendondarForcando2CadasDecimais(getValorTotal() + getDesconto());
    }

    public Double getEntrada() {
        return entrada;
    }

    public void setEntrada(Double entrada) {
        this.entrada = entrada;
    }

    public List<ContaReceberVO> getListaCondicaoPagamentoVOs() {
        if (listaCondicaoPagamentoVOs == null) {
            listaCondicaoPagamentoVOs = new ArrayList<ContaReceberVO>();
        }
        return listaCondicaoPagamentoVOs;
    }

    public void setListaCondicaoPagamentoVOs(List<ContaReceberVO> listaCondicaoPagamentoVOs) {
        this.listaCondicaoPagamentoVOs = listaCondicaoPagamentoVOs;
    }

    public String getParcelas() {
        return parcelas;
    }

    public void setParcelas(String parcelas) {
        this.parcelas = parcelas;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
