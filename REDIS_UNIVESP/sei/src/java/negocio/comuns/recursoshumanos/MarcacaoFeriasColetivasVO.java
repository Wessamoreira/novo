package negocio.comuns.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;

/**
 * Reponsavel por manter os dados da entidade TemplateLancamentoFolhaPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MarcacaoFeriasColetivasVO extends SuperVO {

	private static final long serialVersionUID = 2329248243766743050L;

	private Integer codigo;
	private Date dataFechamento;
	private Date dataInicioGozo;
	private Date dataFinalGozo;
	private Date dataPagamento;
	private Date dataInicioAviso;
	private Integer quantidadeDiasAbono;
	private Integer quantidadeDias;
	private Boolean abono;
	private Boolean pagarPrimeiraParcela13;
	private Boolean encerrarPeriodoAquisitivo;
	private String informacoesAdicionais;
	private SituacaoMarcacaoFeriasEnum situacao;
	private String descricao;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO;
	
	//Transient
	private List<ControleMarcacaoFeriasVO> listaHistoricoMarcacaoFeriasColetivaVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Boolean getEncerrarPeriodoAquisitivo() {
		if (encerrarPeriodoAquisitivo == null) {
			encerrarPeriodoAquisitivo = Boolean.FALSE;
		}
		return encerrarPeriodoAquisitivo;
	}

	public void setEncerrarPeriodoAquisitivo(Boolean encerrarPeriodoAquisitivo) {
		this.encerrarPeriodoAquisitivo = encerrarPeriodoAquisitivo;
	}

	public Date getDataInicioGozo() {
		return dataInicioGozo;
	}

	public void setDataInicioGozo(Date dataInicioGozo) {
		this.dataInicioGozo = dataInicioGozo;
	}

	public Date getDataFinalGozo() {
		return dataFinalGozo;
	}

	public void setDataFinalGozo(Date dataFinalGozo) {
		this.dataFinalGozo = dataFinalGozo;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataInicioAviso() {
		return dataInicioAviso;
	}

	public void setDataInicioAviso(Date dataInicioAviso) {
		this.dataInicioAviso = dataInicioAviso;
	}

	public Integer getQuantidadeDiasAbono() {
		if (quantidadeDiasAbono == null) {
			quantidadeDiasAbono = 0;
		}
		return quantidadeDiasAbono;
	}

	public void setQuantidadeDiasAbono(Integer quantidadeDiasAbono) {
		this.quantidadeDiasAbono = quantidadeDiasAbono;
	}

	public Integer getQuantidadeDias() {
		if (quantidadeDias == null) {
			quantidadeDias = 30;
		}
		return quantidadeDias;
	}

	public void setQuantidadeDias(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	public Boolean getAbono() {
		if (abono == null) {
			abono = Boolean.FALSE;
		}
		return abono;
	}

	public void setAbono(Boolean abono) {
		this.abono = abono;
	}

	public Boolean getPagarPrimeiraParcela13() {
		if (pagarPrimeiraParcela13 == null) {
			pagarPrimeiraParcela13 = Boolean.FALSE;
		}
		return pagarPrimeiraParcela13;
	}

	public void setPagarPrimeiraParcela13(Boolean pagarPrimeiraParcela13) {
		this.pagarPrimeiraParcela13 = pagarPrimeiraParcela13;
	}

	public String getInformacoesAdicionais() {
		if (informacoesAdicionais == null) {
			informacoesAdicionais = "";
		}
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamentoVO() {
		if (templateLancamentoFolhaPagamentoVO == null) {
			templateLancamentoFolhaPagamentoVO = new TemplateLancamentoFolhaPagamentoVO();
		}
		return templateLancamentoFolhaPagamentoVO;
	}

	public void setTemplateLancamentoFolhaPagamentoVO(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO) {
		this.templateLancamentoFolhaPagamentoVO = templateLancamentoFolhaPagamentoVO;
	}

	public List<ControleMarcacaoFeriasVO> getListaHistoricoMarcacaoFeriasColetivaVO() {
		if (listaHistoricoMarcacaoFeriasColetivaVO == null) {
			listaHistoricoMarcacaoFeriasColetivaVO = new ArrayList<>();
		}
		return listaHistoricoMarcacaoFeriasColetivaVO;
	}

	public void setListaHistoricoMarcacaoFeriasColetivaVO(List<ControleMarcacaoFeriasVO> listaHistoricoMarcacaoFeriasColetivaVO) {
		this.listaHistoricoMarcacaoFeriasColetivaVO = listaHistoricoMarcacaoFeriasColetivaVO;
	}

	public SituacaoMarcacaoFeriasEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoMarcacaoFeriasEnum situacao) {
		this.situacao = situacao;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}		
}