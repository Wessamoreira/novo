package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Carlos
 */
public class RenegociacaoRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String identificadorTurma;
	private String tipoPessoa;
	private Integer negociacaoContaReceber;
	private String matricula;
	private String nome;
	private String tipoRenegociacao;
	private String responsavel;
	private Double valorTotal;
	private Double valor;
	private Double desconto;
	private Double multa;
	private Double juro;
	private Double valorEntrada;
	private Date dataNegociacao;
	private Integer nrParcela;
	private Double acrescimo;
	private List<RenegociacaoContaNegociadaRelVO> listaRenegociacaoContaNegociadaRelVOs;
	private List<RenegociacaoContaGeradaRelVO> listaRenegociacaoContaGeradaRelVOs;
	private String nomeCurso;
	private Double valorPrimeiraParcela;
	private Double valorSegundaParcela;
	private Double valorMultaCalculado;
	private Double valorJuroCalculado;
	private Double valorAcrescimoCalculado;
	private Double valorDescontoCalculado;

	public JRDataSource getContaNegociadaVOs() {
		return new JRBeanArrayDataSource(getListaRenegociacaoContaNegociadaRelVOs().toArray());
	}

	public JRDataSource getContaGeradaVOs() {
		return new JRBeanArrayDataSource(getListaRenegociacaoContaGeradaRelVOs().toArray());
	}

	public List<RenegociacaoContaNegociadaRelVO> getListaRenegociacaoContaNegociadaRelVOs() {
		if (listaRenegociacaoContaNegociadaRelVOs == null) {
			listaRenegociacaoContaNegociadaRelVOs = new ArrayList<RenegociacaoContaNegociadaRelVO>(0);
		}
		return listaRenegociacaoContaNegociadaRelVOs;
	}

	public void setListaRenegociacaoContaNegociadaRelVOs(List<RenegociacaoContaNegociadaRelVO> listaRenegociacaoContaNegociadaRelVOs) {
		this.listaRenegociacaoContaNegociadaRelVOs = listaRenegociacaoContaNegociadaRelVOs;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getResponsavel() {
		if (responsavel == null) {
			responsavel = "";
		}
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Date getDataNegociacao() {
		return dataNegociacao;
	}

	public void setDataNegociacao(Date dataNegociacao) {
		this.dataNegociacao = dataNegociacao;
	}

	public Integer getNrParcela() {
		if (nrParcela == null) {
			nrParcela = 0;
		}
		return nrParcela;
	}

	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	public Integer getNegociacaoContaReceber() {
		if (negociacaoContaReceber == null) {
			negociacaoContaReceber = 0;
		}
		return negociacaoContaReceber;
	}

	public void setNegociacaoContaReceber(Integer negociacaoContaReceber) {
		this.negociacaoContaReceber = negociacaoContaReceber;
	}

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public Double getValorEntrada() {
		if (valorEntrada == null) {
			valorEntrada = 0.0;
		}
		return valorEntrada;
	}

	public void setValorEntrada(Double valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public List<RenegociacaoContaGeradaRelVO> getListaRenegociacaoContaGeradaRelVOs() {
		if (listaRenegociacaoContaGeradaRelVOs == null) {
			listaRenegociacaoContaGeradaRelVOs = new ArrayList<RenegociacaoContaGeradaRelVO>(0);
		}
		return listaRenegociacaoContaGeradaRelVOs;
	}

	public void setListaRenegociacaoContaGeradaRelVOs(List<RenegociacaoContaGeradaRelVO> listaRenegociacaoContaGeradaRelVOs) {
		this.listaRenegociacaoContaGeradaRelVOs = listaRenegociacaoContaGeradaRelVOs;
	}

	/**
	 * @return the tipoRenegociacao
	 */
	public String getTipoRenegociacao() {
		if (tipoRenegociacao == null) {
			tipoRenegociacao = "";
		}
		return tipoRenegociacao;
	}

	public String getTipoRenegociacao_Apresentar() {
		if (tipoRenegociacao == null) {
			return "AMBAS";
		}
		if (tipoRenegociacao.equals("AV")) {
			return "A VENCER";
		} else if (tipoRenegociacao.equals("VE")) {
			return "VENCIDA";
		} else {
			return "AMBAS";
		}

	}

	/**
	 * @param tipoRenegociacao
	 *            the tipoRenegociacao to set
	 */
	public void setTipoRenegociacao(String tipoRenegociacao) {
		this.tipoRenegociacao = tipoRenegociacao;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "AL";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public Double getValorPrimeiraParcela() {
		if (valorPrimeiraParcela == null) {
			valorPrimeiraParcela = 0.0;
		}
		return valorPrimeiraParcela;
	}

	public void setValorPrimeiraParcela(Double valorPrimeiraParcela) {
		this.valorPrimeiraParcela = valorPrimeiraParcela;
	}

	public Double getValorSegundaParcela() {
		if (valorSegundaParcela == null) {
			valorSegundaParcela = 0.0;
		}
		return valorSegundaParcela;
	}

	public void setValorSegundaParcela(Double valorSegundaParcela) {
		this.valorSegundaParcela = valorSegundaParcela;
	}

	public Double getValorMultaCalculado() {
		if (valorMultaCalculado == null) {
			valorMultaCalculado = 0.0;
		}
		return valorMultaCalculado;
	}

	public Double getValorJuroCalculado() {
		if (valorJuroCalculado == null) {
			valorJuroCalculado = 0.0;
		}
		return valorJuroCalculado;
	}

	public Double getValorAcrescimoCalculado() {
		if (valorAcrescimoCalculado == null) {
			valorAcrescimoCalculado = 0.0;
		}
		return valorAcrescimoCalculado;
	}

	public Double getValorDescontoCalculado() {
		if (valorDescontoCalculado == null) {
			valorDescontoCalculado = 0.0;
		}
		return valorDescontoCalculado;
	}

	public void calcularValorMultaCalculado() {
		valorMultaCalculado = getListaRenegociacaoContaNegociadaRelVOs().parallelStream().map(RenegociacaoContaNegociadaRelVO::getMulta).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public void calcularValorJuroCalculado() {
		valorJuroCalculado = getListaRenegociacaoContaNegociadaRelVOs().parallelStream().map(RenegociacaoContaNegociadaRelVO::getJuro).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public void calcularValorDescontoCalculado() {
		valorDescontoCalculado = getListaRenegociacaoContaNegociadaRelVOs().parallelStream().map(RenegociacaoContaNegociadaRelVO::getValorDesconto).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public void calcularValorAcrescimoCalculado() {
		valorAcrescimoCalculado = getListaRenegociacaoContaNegociadaRelVOs().parallelStream().map(RenegociacaoContaNegociadaRelVO::getAcrescimo).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public void calcularValor() {
		valor = getListaRenegociacaoContaNegociadaRelVOs().parallelStream().map(RenegociacaoContaNegociadaRelVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
}
