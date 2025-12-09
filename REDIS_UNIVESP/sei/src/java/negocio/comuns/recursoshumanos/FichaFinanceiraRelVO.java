package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade ContraCheque. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FichaFinanceiraRelVO extends SuperVO {

	private static final long serialVersionUID = 2002356819398010636L;

	private String unidadeEnsino;
	private String periodo;
	private Date competencia;
	private String matriculaCargo;
	private String nomeFuncionario;
	private String nomeCargo;
	private String situacao;
	private String dataAdmissao;
	private String dataDemissao;
	private BigDecimal salario;
	private String secaoFolhaPagamento;
	private BigDecimal provento;
	private BigDecimal desconto;
	private BigDecimal areceber;

	private BigDecimal baseCalculoIrrf;
	private BigDecimal baseCalculoInss;

	// Campos do final do relatorio
	private BigDecimal inssSegurado;
	private BigDecimal baseInss13;
	private BigDecimal baseIrrf13;
	private BigDecimal baseIrrfFerias;

	private BigDecimal previdenciaPropria;

	private List<EventoContraChequeRelVO> listaEventosProvento;
	private List<EventoContraChequeRelVO> listaEventosDesconto;

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null)
			unidadeEnsino = "";
		return unidadeEnsino;
	}

	public String getPeriodo() {
		if (periodo == null)
			periodo = "";
		return periodo;
	}

	public Date getCompetencia() {
		return competencia;
	}

	public String getMatriculaCargo() {
		if (matriculaCargo == null)
			matriculaCargo = "";
		return matriculaCargo;
	}

	public String getNomeFuncionario() {
		if (nomeFuncionario == null)
			nomeFuncionario = "";
		return nomeFuncionario;
	}

	public String getNomeCargo() {
		if (nomeCargo == null)
			nomeCargo = "";
		return nomeCargo;
	}

	public String getSituacao() {
		if (situacao == null)
			situacao = "";
		return situacao;
	}

	public String getDataAdmissao() {
		if (dataAdmissao == null)
			dataAdmissao = "";
		return dataAdmissao;
	}

	public String getDataDemissao() {
		if (dataDemissao == null)
			dataDemissao = "";
		return dataDemissao;
	}

	public BigDecimal getSalario() {
		if (salario == null)
			salario = BigDecimal.ZERO;
		return salario;
	}

	public String getSecaoFolhaPagamento() {
		if (secaoFolhaPagamento == null)
			secaoFolhaPagamento = "";
		return secaoFolhaPagamento;
	}

	public BigDecimal getProvento() {
		if (provento == null)
			provento = BigDecimal.ZERO;
		return provento;
	}

	public BigDecimal getDesconto() {
		if (desconto == null)
			desconto = BigDecimal.ZERO;
		return desconto;
	}

	public BigDecimal getAreceber() {
		if (areceber == null)
			areceber = BigDecimal.ZERO;
		return areceber;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public void setCompetencia(Date competencia) {
		this.competencia = competencia;
	}

	public void setMatriculaCargo(String matriculaCargo) {
		this.matriculaCargo = matriculaCargo;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public void setNomeCargo(String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public void setDataAdmissao(String dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public void setDataDemissao(String dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public void setSecaoFolhaPagamento(String secaoFolhaPagamento) {
		this.secaoFolhaPagamento = secaoFolhaPagamento;
	}

	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public void setAreceber(BigDecimal areceber) {
		this.areceber = areceber;
	}

	public List<EventoContraChequeRelVO> getListaEventosProvento() {
		if (listaEventosProvento == null)
			listaEventosProvento = new ArrayList<>();
		return listaEventosProvento;
	}

	public List<EventoContraChequeRelVO> getListaEventosDesconto() {
		if (listaEventosDesconto == null)
			listaEventosDesconto = new ArrayList<>();
		return listaEventosDesconto;
	}

	public void setListaEventosProvento(List<EventoContraChequeRelVO> listaEventosProvento) {
		this.listaEventosProvento = listaEventosProvento;
	}

	public void setListaEventosDesconto(List<EventoContraChequeRelVO> listaEventosDesconto) {
		this.listaEventosDesconto = listaEventosDesconto;
	}

	public JRDataSource getListaEventosProventoJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaEventosProvento().toArray());
		return jr;
	}

	public JRDataSource getListaEventosDescontoJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaEventosDesconto().toArray());
		return jr;
	}

	public BigDecimal getBaseCalculoIrrf() {
		if (baseCalculoIrrf == null)
			baseCalculoIrrf = BigDecimal.ZERO;
		return baseCalculoIrrf;
	}

	public BigDecimal getBaseCalculoInss() {
		if (baseCalculoInss == null)
			baseCalculoInss = BigDecimal.ZERO;
		return baseCalculoInss;
	}

	public void setBaseCalculoIrrf(BigDecimal baseCalculoIrrf) {
		this.baseCalculoIrrf = baseCalculoIrrf;
	}

	public void setBaseCalculoInss(BigDecimal baseCalculoInss) {
		this.baseCalculoInss = baseCalculoInss;
	}

	public BigDecimal getInssSegurado() {
		if (inssSegurado == null)
			inssSegurado = BigDecimal.ZERO;
		return inssSegurado;
	}

	public BigDecimal getBaseInss13() {
		if (baseInss13 == null)
			baseInss13 = BigDecimal.ZERO;
		return baseInss13;
	}

	public BigDecimal getBaseIrrf13() {
		if (baseIrrf13 == null)
			baseIrrf13 = BigDecimal.ZERO;
		return baseIrrf13;
	}

	public BigDecimal getBaseIrrfFerias() {
		if (baseIrrfFerias == null)
			baseIrrfFerias = BigDecimal.ZERO;
		return baseIrrfFerias;
	}

	public void setInssSegurado(BigDecimal inssSegurado) {
		this.inssSegurado = inssSegurado;
	}

	public void setBaseInss13(BigDecimal baseInss13) {
		this.baseInss13 = baseInss13;
	}

	public void setBaseIrrf13(BigDecimal baseIrrf13) {
		this.baseIrrf13 = baseIrrf13;
	}

	public void setBaseIrrfFerias(BigDecimal baseIrrfFerias) {
		this.baseIrrfFerias = baseIrrfFerias;
	}

	public BigDecimal getPrevidenciaPropria() {
		if (previdenciaPropria == null)
			previdenciaPropria = BigDecimal.ZERO;
		return previdenciaPropria;
	}

	public void setPrevidenciaPropria(BigDecimal previdenciaPropria) {
		this.previdenciaPropria = previdenciaPropria;
	}

}