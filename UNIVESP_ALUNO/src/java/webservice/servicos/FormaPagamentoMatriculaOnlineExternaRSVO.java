package webservice.servicos;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@XmlRootElement(name = "formaPagamentoMatriculaOnlineExterna")
public class FormaPagamentoMatriculaOnlineExternaRSVO {

	private String $$hashKey;
	private String bandeira;
	private String numeroCartao;
	private String nomeNoCartao;
	private Integer mesValidade;
	private Integer anoValidade;
	private String codigoDeVerificacao;
	private Double valor;
	private String valorApresentar;
	private Integer codigoConfiguracaoFinanceiroCartao;
	private Integer parcela;
	private BandeiraRSVO bandeiraRSVO;
	private Boolean utilizarCartaoComoPagamentoRecorrenteProximaParcela;
	private FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoPagamentoAutomaticoParcelaRecorrencia;
	private Integer diaPadraoPagamentoRecorrencia;
	private String orientacaoRecorrenciaAluno;

	@XmlElement(name = "bandeira")
	public String getBandeira() {
		if(bandeira == null) {
			bandeira = "";
		}
		return bandeira;
	}

	public void setBandeira(String bandeira) {
		this.bandeira = bandeira;
	}

	@XmlElement(name = "numeroCartao")
	public String getNumeroCartao() {
		if(numeroCartao == null) {
			numeroCartao = "";
		}
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	@XmlElement(name = "nomeNoCartao")
	public String getNomeNoCartao() {
		if(nomeNoCartao == null) {
			nomeNoCartao = "";
		}
		return nomeNoCartao;
	}

	public void setNomeNoCartao(String nomeNoCartao) {
		this.nomeNoCartao = nomeNoCartao;
	}

	@XmlElement(name = "mesValidade")
	public Integer getMesValidade() {
		if(mesValidade == null) {
			mesValidade = 0;
		}
		return mesValidade;
	}

	public void setMesValidade(Integer mesValidade) {
		this.mesValidade = mesValidade;
	}

	@XmlElement(name = "anoValidade")
	public Integer getAnoValidade() {
		if(anoValidade == null) {
			anoValidade = 0;
		}
		return anoValidade;
	}

	public void setAnoValidade(Integer anoValidade) {
		this.anoValidade = anoValidade;
	}

	@XmlElement(name = "codigoDeVerificacao")
	public String getCodigoDeVerificacao() {
		if(codigoDeVerificacao == null) {
			codigoDeVerificacao = "";
		}
		return codigoDeVerificacao;
	}

	public void setCodigoDeVerificacao(String codigoDeVerificacao) {
		this.codigoDeVerificacao = codigoDeVerificacao;
	}

	@XmlElement(name = "valor")
	public Double getValor() {
		if(valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@XmlElement(name = "codigoConfiguracaoFinanceiroCartao")
	public Integer getCodigoConfiguracaoFinanceiroCartao() {
		if(codigoConfiguracaoFinanceiroCartao == null) {
			codigoConfiguracaoFinanceiroCartao = 0;
		}
		return codigoConfiguracaoFinanceiroCartao;
	}

	public void setCodigoConfiguracaoFinanceiroCartao(Integer codigoConfiguracaoFinanceiroCartao) {
		this.codigoConfiguracaoFinanceiroCartao = codigoConfiguracaoFinanceiroCartao;
	}

	@XmlElement(name = "valorApresentar")
	public String getValorApresentar() {
		if (valorApresentar == null) {
			valorApresentar = "";
		}
		return valorApresentar;
	}

	public void setValorApresentar(String valorApresentar) {
		this.valorApresentar = valorApresentar;
	}

	@XmlElement(name = "$$hashKey")
	public String get$$hashKey() {
		if ($$hashKey == null) {
			$$hashKey = "";
		}
		return $$hashKey;
	}

	public void set$$hashKey(String $$hashKey) {
		this.$$hashKey = $$hashKey;
	}

	@XmlElement(name = "parcela")
	public Integer getParcela() {
		if (parcela == null) {
			parcela = 1;
		}
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	@XmlElement(name = "bandeiraRSVO")
	public BandeiraRSVO getBandeiraRSVO() {
		if (bandeiraRSVO == null) {
			bandeiraRSVO = new BandeiraRSVO();
		}
		return bandeiraRSVO;
	}
	

	public void setBandeiraRSVO(BandeiraRSVO bandeiraRSVO) {
		this.bandeiraRSVO = bandeiraRSVO;
	}

	@XmlElement(name = "utilizarCartaoComoPagamentoRecorrenteProximaParcela")
	public Boolean getUtilizarCartaoComoPagamentoRecorrenteProximaParcela() {
		if (utilizarCartaoComoPagamentoRecorrenteProximaParcela == null) {
			utilizarCartaoComoPagamentoRecorrenteProximaParcela = false;
		}
		return utilizarCartaoComoPagamentoRecorrenteProximaParcela;
	}

	public void setUtilizarCartaoComoPagamentoRecorrenteProximaParcela(Boolean utilizarCartaoComoPagamentoRecorrenteProximaParcela) {
		this.utilizarCartaoComoPagamentoRecorrenteProximaParcela = utilizarCartaoComoPagamentoRecorrenteProximaParcela;
	}

	@XmlElement(name = "formaPadraoPagamentoAutomaticoParcelaRecorrencia")
	public FormaPadraoDataBaseCartaoRecorrenteEnum getFormaPadraoPagamentoAutomaticoParcelaRecorrencia() {
		if (formaPadraoPagamentoAutomaticoParcelaRecorrencia == null) {
			formaPadraoPagamentoAutomaticoParcelaRecorrencia = FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO;
		}
		return formaPadraoPagamentoAutomaticoParcelaRecorrencia;
	}

	public void setFormaPadraoPagamentoAutomaticoParcelaRecorrencia(
			FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoPagamentoAutomaticoParcelaRecorrencia) {
		this.formaPadraoPagamentoAutomaticoParcelaRecorrencia = formaPadraoPagamentoAutomaticoParcelaRecorrencia;
	}

	@XmlElement(name = "diaPadraoPagamentoRecorrencia")
	public Integer getDiaPadraoPagamentoRecorrencia() {
		if (diaPadraoPagamentoRecorrencia == null) {
			diaPadraoPagamentoRecorrencia = UteisData.getDiaMesData(new Date());
		}
		return diaPadraoPagamentoRecorrencia;
	}

	public void setDiaPadraoPagamentoRecorrencia(Integer diaPadraoPagamentoRecorrencia) {
		this.diaPadraoPagamentoRecorrencia = diaPadraoPagamentoRecorrencia;
	}

	@XmlElement(name = "orientacaoRecorrenciaAluno")
	public String getOrientacaoRecorrenciaAluno() {
		if (orientacaoRecorrenciaAluno == null) {
			orientacaoRecorrenciaAluno = "";
		}
		return orientacaoRecorrenciaAluno;
	}

	public void setOrientacaoRecorrenciaAluno(String orientacaoRecorrenciaAluno) {
		this.orientacaoRecorrenciaAluno = orientacaoRecorrenciaAluno;
	}
	
	
	
	
}
