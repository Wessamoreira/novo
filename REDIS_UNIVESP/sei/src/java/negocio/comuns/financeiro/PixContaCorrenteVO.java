package negocio.comuns.financeiro;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoPixEnum;
import negocio.comuns.financeiro.enumerador.StatusPixEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import webservice.pix.comuns.PayloadPixRSVO;

public class PixContaCorrenteVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4849760021700471648L;
	private Integer codigo;
	private Date dataGeracao;
	private Integer tempoExpiracao;
	private String txid;
	private String qrCode;
	private String jsonEnvio;
	private String jsonRetorno;
	private SituacaoPixEnum situacaoPixEnum;
	private StatusPixEnum statusPixEnum;
	private ContaReceberVO contaReceberVO;
	private Double valorContaReceberEnvio;
	private String nossoNumero;
	private UsuarioVO usuarioVO;
	private String endToEndId;
	private Double valor;
	private String infoPagador;
	private Date horario;
	private ContaCorrenteVO contaCorrenteVO;
	private String documentoPagador;
	private String nomePagador;
	private String motivoCancelamento;
	/**
	 * Transient
	 */
	private PayloadPixRSVO payloadPixRSVO;
	private static String ID_PAYLOAD_FORMAT_INDICATOR = "00";
	private static String ID_POINT_OF_INITIATION_METHOD = "01";
	private static String ID_MERCHANT_ACCOUNT_INFORMATION = "26";
	private static String ID_MERCHANT_ACCOUNT_INFORMATION_GUI = "00";
	private static String ID_MERCHANT_ACCOUNT_INFORMATION_KEY = "01";
	private static String ID_MERCHANT_ACCOUNT_INFORMATION_DESCRIPTION = "02";
	private static String ID_MERCHANT_ACCOUNT_INFORMATION_URL = "25";
	private static String ID_MERCHANT_CATEGORY_CODE = "52";
	private static String ID_TRANSACTION_CURRENCY = "53";
	private static String ID_TRANSACTION_AMOUNT = "54";
	private static String ID_COUNTRY_CODE = "58";
	private static String ID_MERCHANT_NAME = "59";
	private static String ID_MERCHANT_CITY = "60";
	private static String ID_ADDITIONAL_DATA_FIELD_TEMPLATE = "62";
	private static String ID_ADDITIONAL_DATA_FIELD_TEMPLATE_TXID = "05";
	private static String ID_CRC16 = "63";
	
	public String getUrlEnvioCobranca() {
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoBrasil()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_COBVQRC + getTxid();				
		}
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoItau()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_COB + getTxid();				
		}
		throw new StreamSeiException("Não foi definido a URL para o envio do Pix de Cobrança.");
	}
	
	public String getUrlRevisaoCobranca() {
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoBrasil()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_COBV + getTxid();				
		}
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoItau()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_COB + getTxid();				
		}
		throw new StreamSeiException("Não foi definido a URL para revisão do Pix de Cobrança.");
	}
	
	public String getUrlConsultaCobranca() {
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoBrasil()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_COBV + getTxid();				
		}
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoItau()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_COB + getTxid();				
		}
		throw new StreamSeiException("Não foi definido a URL para consulta do Pix de Cobrança.");
	}
	
	public String getUrlConsultaQrCode() {
		if (getContaCorrenteVO().getAgencia().getBanco().isBancoItau()) {
			return getContaCorrenteVO().getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_COB + getTxid() + "/qrcode";				
		}
		throw new StreamSeiException("Não foi definido a URL para consulta do Pix de QrCode.");
	}
	
	

	public String getPayloadQrCodeDimanico() {
		StringBuilder sb = new StringBuilder();
		sb.append(getFormatarQrCode(ID_PAYLOAD_FORMAT_INDICATOR, "01"));
		sb.append(getFormatarQrCode(ID_POINT_OF_INITIATION_METHOD, "12"));
		sb.append(getMerchantAccountInformation());
		sb.append(getFormatarQrCode(ID_MERCHANT_CATEGORY_CODE, "0000"));
		sb.append(getFormatarQrCode(ID_TRANSACTION_CURRENCY, "986"));
		sb.append(getFormatarQrCode(ID_TRANSACTION_AMOUNT, getPayloadPixRSVO().getValor().getOriginal()));
		sb.append(getFormatarQrCode(ID_COUNTRY_CODE, "BR"));
		sb.append(getFormatarQrCode(ID_MERCHANT_NAME, getPayloadPixRSVO().getDevedor().getNome()));
		sb.append(getFormatarQrCode(ID_MERCHANT_CITY, getPayloadPixRSVO().getDevedor().getCidade()));
		// sb.append(getAdditionalDataFieldTemplate());
		sb.append(ID_CRC16).append("04").append(getCRC16(sb.toString()));
		return sb.toString();
	}

	private String getMerchantAccountInformation() {
		StringBuilder sb = new StringBuilder();
		// DOMÍNIO DO BANCO
		sb.append(getFormatarQrCode(ID_MERCHANT_ACCOUNT_INFORMATION_GUI, "br.gov.bcb.pix"));

		// CHAVE PIX
		sb.append(getFormatarQrCode(ID_MERCHANT_ACCOUNT_INFORMATION_KEY, getPayloadPixRSVO().getChave()));

		// DESCRIÇÃO DO PAGAMENTO
		// sb.append(getFormatarQrCode(ID_MERCHANT_ACCOUNT_INFORMATION_DESCRIPTION,getPayloadPixRSVO().getSolicitacaoPagador()));

		// URL DO QR CODE DINÂMICO
		// sb.append(getFormatarQrCode(ID_MERCHANT_ACCOUNT_INFORMATION_URL,getLink().replace("https://", "").replace("http://", "")));

		// VALOR COMPLETO DA CONTA
		return getFormatarQrCode(ID_MERCHANT_ACCOUNT_INFORMATION, sb.toString());
	}

	private String getAdditionalDataFieldTemplate() {
		return getFormatarQrCode(ID_ADDITIONAL_DATA_FIELD_TEMPLATE, getFormatarQrCode(ID_ADDITIONAL_DATA_FIELD_TEMPLATE_TXID, getTxid()));
	}

	private String getCRC16(String data) {
		data = data + ID_CRC16 + "04";
		int len = data.length();
		if (!(len % 2 == 0)) {
			return "0000";
		}
		int num = len / 2;
		byte[] para = new byte[num];
		for (int i = 0; i < num; i++) {
			int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
			para[i] = (byte) value;
		}
		return getCRC16(para);
	}

	private String getCRC16(byte[] bytes) {
		// CRC registers are all 1
		int CRC = 0x0000ffff;
		// Polynomial check value
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		// Result converted to hex
		String result = Integer.toHexString(CRC).toUpperCase();
		if (result.length() != 4) {
			StringBuffer sb = new StringBuffer("0000");
			result = sb.replace(4 - result.length(), 4, result).toString();
		}
		// High position in the front position in the back
		// return result.substring(2, 4) + " " + result.substring(0, 2);
		// Exchange high low, low in front, high in back
		return result.substring(2, 4) + " " + result.substring(0, 2);
	}	

	private String getFormatarQrCode(String id, String valor) {
		String length = StringUtils.leftPad(String.valueOf(valor.length()), 2, "0");
		return id + length + valor;
	}
	

	public String getQrCode() {
		if (qrCode == null) {
			qrCode = "";
		}
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getJsonEnvio() {
		if (jsonEnvio == null) {
			jsonEnvio = "";
		}
		return jsonEnvio;
	}

	public void setJsonEnvio(String jsonEnvio) {
		this.jsonEnvio = jsonEnvio;
	}

	public String getJsonRetorno() {
		if (jsonRetorno == null) {
			jsonRetorno = "";
		}
		return jsonRetorno;
	}

	public void setJsonRetorno(String jsonRetorno) {
		this.jsonRetorno = jsonRetorno;
	}

	public SituacaoPixEnum getSituacaoPixEnum() {
		if (situacaoPixEnum == null) {
			situacaoPixEnum = SituacaoPixEnum.EM_PROCESSAMENTO;
		}
		return situacaoPixEnum;
	}

	public void setSituacaoPixEnum(SituacaoPixEnum situacaoPixEnum) {
		this.situacaoPixEnum = situacaoPixEnum;
	}

	public StatusPixEnum getStatusPixEnum() {
		if (statusPixEnum == null) {
			statusPixEnum = StatusPixEnum.ATIVA;
		}
		return statusPixEnum;
	}

	public void setStatusPixEnum(StatusPixEnum statusPixEnum) {
		this.statusPixEnum = statusPixEnum;
	}

	public String getEndToEndId() {
		return endToEndId;
	}

	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getDataGeracao_Apresentar() {
		return Uteis.getDataAplicandoFormatacao(getDataGeracao(), "dd/MM/yyyy HH:mm:ss");
	}

	public Date getDataGeracao() {
		if (dataGeracao == null) {
			dataGeracao = new Date();
		}
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}	

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public PayloadPixRSVO getPayloadPixRSVO() {
		if (payloadPixRSVO == null) {
			payloadPixRSVO = new PayloadPixRSVO();
		}
		return payloadPixRSVO;
	}

	public void setPayloadPixRSVO(PayloadPixRSVO payloadPixRSVO) {
		this.payloadPixRSVO = payloadPixRSVO;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorContaReceberEnvio() {
		return valorContaReceberEnvio;
	}

	public void setValorContaReceberEnvio(Double valorContaReceberEnvio) {
		this.valorContaReceberEnvio = valorContaReceberEnvio;
	}

	public String getInfoPagador() {
		return infoPagador;
	}

	public void setInfoPagador(String infoPagador) {
		this.infoPagador = infoPagador;
	}

	public Date getHorario() {
		return horario;
	}

	public void setHorario(Date horario) {
		this.horario = horario;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public String getTxid() {
		if (txid == null) {
			txid = "";
		}
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getDocumentoPagador() {
		if (documentoPagador == null) {
			documentoPagador = "";
		}
		return documentoPagador;
	}

	public void setDocumentoPagador(String documentoPagador) {
		this.documentoPagador = documentoPagador;
	}

	public String getNomePagador() {
		if (nomePagador == null) {
			nomePagador = "";
		}
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}

	public String getMotivoCancelamento() {
		if (motivoCancelamento == null) {
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public Integer getTempoExpiracao() {
		if (tempoExpiracao == null) {
			tempoExpiracao = 0;
		}
		return tempoExpiracao;
	}

	public void setTempoExpiracao(Integer tempoExpiracao) {
		this.tempoExpiracao = tempoExpiracao;
	}	
	
	
	
	
	
	
	
	

}
