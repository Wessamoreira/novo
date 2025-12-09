package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;


/**
 * Reponsável por manter os dados da entidade Catalogo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EmprestimoRelVO extends SuperVO implements Serializable {

	private String unidadeEnsino;
	private String situacao;
	private String tipoPessoa;
	private String biblioteca;
	private Date dataInicial;
	private Date dataFinal;
	private Integer codigoCatalogo;
	private String catalogo;
	private String exemplar;
	private String pessoaEmprestimo;
	private String tipoEmprestimo;
	private String situacaoEmprestimo;
	private String dataEmprestimo;
	private String dataPrevDevolucao;
	private String responsavelEmprestimo;
	private Integer diasAtraso;
	private Double valorMulta;
	private Double totalMultaEmprestimo;
	private Boolean multaPaga;
	private String tipoCatalogo;
	private String tipoItemEmprestimo;	
	private String dataDevolucao;
	private Boolean multaIsenta;
	private Double valorMultaIsenta;
	private String motivoIsencao;
	
	
	
	
	public String getUnidadeEnsino() {
		if(unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	public String getSituacao() {
		if(situacao == null) {
			situacao = "";
		}
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getTipoPessoa() {
		if(tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	public String getBiblioteca() {
		if(biblioteca == null) {
			biblioteca = "";
		}
		return biblioteca;
	}
	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}
	public Date getDataInicial() {
		if(dataInicial == null) {
			dataInicial = new Date();
		}
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getDataFinal() {
		if(dataFinal == null) {
			dataFinal = new Date();
		}
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getCatalogo() {
		if(catalogo == null) {
			catalogo = "";
		}
		return catalogo;
	}
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	public String getExemplar() {
		if(exemplar == null) {
			exemplar = "";
		}
		return exemplar;
	}
	public void setExemplar(String exemplar) {
		this.exemplar = exemplar;
	}
	public String getPessoaEmprestimo() {
		if(pessoaEmprestimo == null) {
			pessoaEmprestimo = "";
		}
		return pessoaEmprestimo;
	}
	public void setPessoaEmprestimo(String pessoaEmprestimo) {
		this.pessoaEmprestimo = pessoaEmprestimo;
	}
	public String getTipoEmprestimo() {
		if(tipoEmprestimo == null) {
			tipoEmprestimo = "";
		}
		return tipoEmprestimo;
	}
	public void setTipoEmprestimo(String tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}
	public String getSituacaoEmprestimo() {
		if(situacaoEmprestimo == null) {
			situacaoEmprestimo = "";
		}
		return situacaoEmprestimo;
	}
	public void setSituacaoEmprestimo(String situacaoEmpretimo) {
		this.situacaoEmprestimo = situacaoEmpretimo;
	}
	public String getDataEmprestimo() {
		if(dataEmprestimo == null) {
			dataEmprestimo = "";
		}
		return dataEmprestimo;
	}
	public void setDataEmprestimo(String dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	public String getDataPrevDevolucao() {
		if(dataPrevDevolucao == null) {
			dataPrevDevolucao = "";
		}
		return dataPrevDevolucao;
	}
	public void setDataPrevDevolucao(String dataPrevDevolucao) {
		this.dataPrevDevolucao = dataPrevDevolucao;
	}
	public String getResponsavelEmprestimo() {
		if(responsavelEmprestimo == null) {
			responsavelEmprestimo = "";
		}
		return responsavelEmprestimo;
	}
	public void setResponsavelEmprestimo(String responsvelEmprestimo) {
		this.responsavelEmprestimo = responsvelEmprestimo;
	}
	public Integer getDiasAtraso() {
		if (diasAtraso == null) {
			diasAtraso = 0;
		}
		return diasAtraso;
	}
	public void setDiasAtraso(Integer diasAtraso) {
		this.diasAtraso = diasAtraso;
	}
	public Double getValorMulta() {
		if (valorMulta == null) {
			valorMulta = 0.0;
		}
		return valorMulta;
	}
	public void setValorMulta(Double valorMulta) {
		this.valorMulta = valorMulta;
	}
	public Integer getCodigoCatalogo() {
		if (codigoCatalogo == null) {
			codigoCatalogo = 0;
		}
		return codigoCatalogo;
	}
	public void setCodigoCatalogo(Integer codigoCatalogo) {
		this.codigoCatalogo = codigoCatalogo;
	}
	public Double getTotalMultaEmprestimo() {
		if (totalMultaEmprestimo == null) {
			totalMultaEmprestimo = 0.0;
		}
		return totalMultaEmprestimo;
	}
	public void setTotalMultaEmprestimo(Double totalMultaEmprestimo) {
		this.totalMultaEmprestimo = totalMultaEmprestimo;
	}
	public Boolean getMultaPaga() {
		if (multaPaga == null) {
			multaPaga = false;
		}
		return multaPaga;
	}
	public void setMultaPaga(Boolean multaPaga) {
		this.multaPaga = multaPaga;
	}
	
	public Boolean getIsPossuiMulta(){
		return getValorMulta() > 0 || getValorMultaIsenta() > 0;
	}
	public String getTipoCatalogo() {
		if (tipoCatalogo == null) {
			tipoCatalogo = "";
		}
		return tipoCatalogo;
	}
	public void setTipoCatalogo(String tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}
	public String getDataDevolucao() {
		if (dataDevolucao == null) {
			dataDevolucao = "";
		}
		return dataDevolucao;
	}
	public void setDataDevolucao(String dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	public Boolean getMultaIsenta() {
		if (multaIsenta == null) {
			multaIsenta = Boolean.FALSE;
		}
		return multaIsenta;
	}
	public void setMultaIsenta(Boolean multaIsenta) {
		this.multaIsenta = multaIsenta;
	}
	
	public String getSituacaoEmprestimo_Apresentar() {
		return SituacaoItemEmprestimo.getDescricao(situacaoEmprestimo);
	}
	public Double getValorMultaIsenta() {
		if (valorMultaIsenta == null) {
			valorMultaIsenta = 0.0;
		}
		return valorMultaIsenta;
	}
	public void setValorMultaIsenta(Double valorMultaIsenta) {
		this.valorMultaIsenta = valorMultaIsenta;
	}
	public String getTipoItemEmprestimo() {
		if (tipoItemEmprestimo == null) {
			tipoItemEmprestimo = "";
		}
		return tipoItemEmprestimo;
	}
	public void setTipoItemEmprestimo(String tipoItemEmprestimo) {
		this.tipoItemEmprestimo = tipoItemEmprestimo;
	}
	public String getMotivoIsencao() {
		if (motivoIsencao == null) {
			motivoIsencao = "";
		}
		return motivoIsencao;
	}
	public void setMotivoIsencao(String motivoIsencao) {
		this.motivoIsencao = motivoIsencao;
	}
	
}