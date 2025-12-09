package negocio.comuns.biblioteca;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.biblioteca.enumeradores.TipoOrigemTimeLineEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;

public class TimeLineVO extends SuperVO implements Serializable{
	
	private String situacao;
	private String biblioteca;
	private Date dataInicial;
	private Date dataFinal;
	private Integer codigoCatalogo;
	private String catalogo;
	private String exemplar;
	private String tipoEmprestimo;
	private String situacaoEmprestimo;
	private String dataEmprestimo;
	private String dataPrevDevolucao;
	private Integer diasAtraso;
	private Double valorMulta;
	private Double totalMultaEmprestimo;
	private Boolean multaPaga;
	private String tipoCatalogo;
	private String tipoItemEmprestimo;
	private String dataDevolucao;
	private Boolean multaIsenta;
	private Double valorMultaIsenta;
	private Boolean emprestarSomenteFinalDeSemana; 
	private Boolean reserva;
	private String dataReserva;
	private String dataTerminoReserva;
	private String situacaoReserva;
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaVO;
	private String tipoPessoaEmprestimo;
	private CidadeVO cidadeBibliotecaVO;
	private Integer codigoBiblioteca;
	private String dataVencimentoContaReceber;
	private String dataRecebimentoContaReceber;
	private TipoOrigemTimeLineEnum  tipoOrigemTimeLine;
	private Date dataOcorrenciaApresentar;
	private String codigoOrigem;
	private Double juro;
	private Double multa;
	private Double acrescimo;
	private Double desconto;
	private Double valorRecebido;
	
	
	
	public String getSituacao() {
		if(situacao == null) {
			situacao = "";
		}
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
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
	public Boolean getReserva() {
		if (reserva == null) {
			reserva = false;
		}
		return reserva;
	}
	public void setReserva(Boolean reserva) {
		this.reserva = reserva;
	}
	
	public String getDataReserva() {
		if(dataReserva == null) {
			dataReserva = "";
		}
		return dataReserva;
	}
	public void setDataReserva(String dataReserva) {
		this.dataReserva = dataReserva;
	}
	public String getDataTerminoReserva() {
		if(dataTerminoReserva == null) {
			dataTerminoReserva = "";
		}
		return dataTerminoReserva;
	}
	public void setDataTerminoReserva(String dataTerminoReserva) {
		this.dataTerminoReserva = dataTerminoReserva;
	}
	public String getSituacaoReserva() {
		if (situacaoReserva == null) {
			situacaoReserva = "";
		}
		return situacaoReserva;
	}
	public void setSituacaoReserva(String situacaoReserva) {
		this.situacaoReserva = situacaoReserva;
	}
	public Boolean getEmprestarSomenteFinalDeSemana() {
		if (emprestarSomenteFinalDeSemana == null) {
			emprestarSomenteFinalDeSemana = false;
		}
		return emprestarSomenteFinalDeSemana;
	}
	public void setEmprestarSomenteFinalDeSemana(Boolean emprestarSomenteFinalDeSemana) {
		this.emprestarSomenteFinalDeSemana = emprestarSomenteFinalDeSemana;
	}
	public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaVO() {
		if (configuracaoBibliotecaVO == null) {
			configuracaoBibliotecaVO = new ConfiguracaoBibliotecaVO();
		}
		return configuracaoBibliotecaVO;
	}
	public void setConfiguracaoBibliotecaVO(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) {
		this.configuracaoBibliotecaVO = configuracaoBibliotecaVO;
	}
	public String getTipoPessoaEmprestimo() {
		if (tipoPessoaEmprestimo == null) {
			tipoPessoaEmprestimo = "";
		}
		return tipoPessoaEmprestimo;
	}
	public void setTipoPessoaEmprestimo(String tipoPessoaEmprestimo) {
		this.tipoPessoaEmprestimo = tipoPessoaEmprestimo;
	}
	public CidadeVO getCidadeBibliotecaVO() {
		if (cidadeBibliotecaVO == null) {
			cidadeBibliotecaVO = new CidadeVO();
		}
		return cidadeBibliotecaVO;
	}
	public void setCidadeBibliotecaVO(CidadeVO cidadeBibliotecaVO) {
		this.cidadeBibliotecaVO = cidadeBibliotecaVO;
	}
	public Integer getCodigoBiblioteca() {
		if (codigoBiblioteca == null) {
			codigoBiblioteca = 0;
		}
		return codigoBiblioteca;
	}
	public void setCodigoBiblioteca(Integer codigoBiblioteca) {
		this.codigoBiblioteca = codigoBiblioteca;
	}
	public String getDataVencimentoContaReceber() {
		if (dataVencimentoContaReceber == null) {
			dataVencimentoContaReceber = "";
		}
		return dataVencimentoContaReceber;
	}
	public void setDataVencimentoContaReceber(String dataVencimentoContaReceber) {
		this.dataVencimentoContaReceber = dataVencimentoContaReceber;
	}
	
	public TipoOrigemTimeLineEnum getTipoOrigemTimeLine() {
		return tipoOrigemTimeLine;
	}
	public void setTipoOrigemTimeLine(TipoOrigemTimeLineEnum tipoOrigemTimeLine) {
		this.tipoOrigemTimeLine = tipoOrigemTimeLine;
	}
	public Date getDataOcorrenciaApresentar() {
		if(dataOcorrenciaApresentar == null) {
			dataOcorrenciaApresentar = new Date();
		}
		return dataOcorrenciaApresentar;
	}
	public void setDataOcorrenciaApresentar(Date dataOcorrenciaApresentar) {
		this.dataOcorrenciaApresentar = dataOcorrenciaApresentar;
	}
	public String getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = "";
		}
		return codigoOrigem;
	}
	public void setCodigoOrigem(String codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
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
	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}
	public void setMulta(Double multa) {
		this.multa = multa;
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
	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}
	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}
	
	private String mesAno;
	
	public String getMesAno() {
		if(mesAno == null) {
			mesAno = (Uteis.getMesData(getDataOcorrenciaApresentar()) < 10 ?"0":"")+ Uteis.getMesData(getDataOcorrenciaApresentar())+"/"+Uteis.getAnoData(getDataOcorrenciaApresentar());
		}
		return mesAno;
	}
	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}
	public Boolean filter(String mesAno, TipoOrigemTimeLineEnum operacao) {
		return (mesAno.isEmpty() || mesAno.equals(getMesAno())) && (operacao == null || getTipoOrigemTimeLine().equals(operacao)); 
	}
	
	public String getDataRecebimentoContaReceber() {
		return dataRecebimentoContaReceber;
	}
	
	public void setDataRecebimentoContaReceber(String dataRecebimentoContaReceber) {
		this.dataRecebimentoContaReceber = dataRecebimentoContaReceber;
	}
	
	
	
}
