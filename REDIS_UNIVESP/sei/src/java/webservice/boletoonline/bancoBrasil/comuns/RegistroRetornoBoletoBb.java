package webservice.boletoonline.bancoBrasil.comuns;

public class RegistroRetornoBoletoBb {

    private String numero;
    private String numeroCarteira;
    private String numeroVariacaoCarteira;
    private String codigoCliente;
    private String linhaDigitavel;
    private String codigoBarraNumerico;
    private String numeroContratoCobranca;
    private BeneficiarioVO beneficiario;
    private QrCodeVO qrCode;
    private Integer statusCode;
    
    
    
	public String getNumero() {
		if(numero == null ) {
			numero ="";
		}
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNumeroCarteira() {
		if(numeroCarteira == null ) {
			numeroCarteira ="";
		}
		return numeroCarteira;
	}
	public void setNumeroCarteira(String numeroCarteira) {
		this.numeroCarteira = numeroCarteira;
	}
	public String getNumeroVariacaoCarteira() {
		if(numeroVariacaoCarteira == null ) {
			numeroVariacaoCarteira ="";
		}
		return numeroVariacaoCarteira;
	}
	public void setNumeroVariacaoCarteira(String numeroVariacaoCarteira) {
		this.numeroVariacaoCarteira = numeroVariacaoCarteira;
	}
	public String getCodigoCliente() {
		if(codigoCliente == null ) {
			codigoCliente ="";
		}
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getLinhaDigitavel() {
		if(linhaDigitavel == null ) {
			linhaDigitavel ="";
		}
		return linhaDigitavel;
	}
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	public String getCodigoBarraNumerico() {
		if(codigoBarraNumerico == null ) {
			codigoBarraNumerico ="";
		}
		return codigoBarraNumerico;
	}
	public void setCodigoBarraNumerico(String codigoBarraNumerico) {
		this.codigoBarraNumerico = codigoBarraNumerico;
	}
	public String getNumeroContratoCobranca() {
		if(numeroContratoCobranca == null ) {
			numeroContratoCobranca ="";
		}
		return numeroContratoCobranca;
	}
	public void setNumeroContratoCobranca(String numeroContratoCobranca) {
		this.numeroContratoCobranca = numeroContratoCobranca;
	}
	public BeneficiarioVO getBeneficiario() {
		if(beneficiario == null ) {
			beneficiario = new BeneficiarioVO();
		}
		return beneficiario;
	}
	public void setBeneficiario(BeneficiarioVO beneficiario) {
		this.beneficiario = beneficiario;
	}
	public QrCodeVO getQrCode() {
		if(qrCode == null ) {
			qrCode = new QrCodeVO();
		}
		return qrCode;
	}
	public void setQrCode(QrCodeVO qrCode) {
		this.qrCode = qrCode;
	}
	public Integer getStatusCode() {
		if(statusCode == null ) {
			statusCode = 0;
		}
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
    

    

    
}
