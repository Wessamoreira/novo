package webservice.boletoonline.sicredi.comuns;

import java.util.Date;


public class ResponseSicrediVO {
	
	// campos usados para retorno de emissão / envio 
	private String linhaDigitavel ;
	private String codigoBanco ;
	private String nomeBeneficiario ;
	private String enderecoBeneficiario ;
	private String cpfCnpjBeneficiario ;
	private String cooperativaBeneficiario ;
	private String postoBeneficiario ;
	private String codigoBeneficiario ;
	private String dataDocumento ;
	private String seuNumero ;
	private String especieDocumento ;
	private String aceite ;
	private String nossoNumero ;
	private String especie ;
	private String valorDocumento ;
	private String dataVencimento ;
	private String nomePagador ;
	private String cpfCnpjPagador ;
	private String enderecoPagador ;
	private String dataLimiteDesconto ;
	private String valorDesconto ;
	private String instrucao ;
	private String informativo ;
	private String codigoBarra ;
   
    
	//campos  responsavel pela autenticação
	private String chaveTransacao ;	
	private Date dataExpiracao ;
	
	//campos  responsavel pela  consulta
	private String valor ;
	private String valorLiquidado;
	private String dataEmissao;
	private String situacao;
	
	
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	public String getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}
	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}
	public String getEnderecoBeneficiario() {
		return enderecoBeneficiario;
	}
	public void setEnderecoBeneficiario(String enderecoBeneficiario) {
		this.enderecoBeneficiario = enderecoBeneficiario;
	}
	public String getCpfCnpjBeneficiario() {
		return cpfCnpjBeneficiario;
	}
	public void setCpfCnpjBeneficiario(String cpfCnpjBeneficiario) {
		this.cpfCnpjBeneficiario = cpfCnpjBeneficiario;
	}
	public String getCooperativaBeneficiario() {
		return cooperativaBeneficiario;
	}
	public void setCooperativaBeneficiario(String cooperativaBeneficiario) {
		this.cooperativaBeneficiario = cooperativaBeneficiario;
	}
	public String getPostoBeneficiario() {
		return postoBeneficiario;
	}
	public void setPostoBeneficiario(String postoBeneficiario) {
		this.postoBeneficiario = postoBeneficiario;
	}
	public String getCodigoBeneficiario() {
		return codigoBeneficiario;
	}
	public void setCodigoBeneficiario(String codigoBeneficiario) {
		this.codigoBeneficiario = codigoBeneficiario;
	}
	public String getDataDocumento() {
		return dataDocumento;
	}
	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}
	public String getSeuNumero() {
		return seuNumero;
	}
	public void setSeuNumero(String seuNumero) {
		this.seuNumero = seuNumero;
	}
	public String getEspecieDocumento() {
		return especieDocumento;
	}
	public void setEspecieDocumento(String especieDocumento) {
		this.especieDocumento = especieDocumento;
	}
	public String getAceite() {
		return aceite;
	}
	public void setAceite(String aceite) {
		this.aceite = aceite;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public String getEspecie() {
		return especie;
	}
	public void setEspecie(String especie) {
		this.especie = especie;
	}
	public String getValorDocumento() {
		return valorDocumento;
	}
	public void setValorDocumento(String valorDocumento) {
		this.valorDocumento = valorDocumento;
	}
	public String getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getNomePagador() {
		return nomePagador;
	}
	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}
	public String getCpfCnpjPagador() {
		return cpfCnpjPagador;
	}
	public void setCpfCnpjPagador(String cpfCnpjPagador) {
		this.cpfCnpjPagador = cpfCnpjPagador;
	}
	public String getEnderecoPagador() {
		return enderecoPagador;
	}
	public void setEnderecoPagador(String enderecoPagador) {
		this.enderecoPagador = enderecoPagador;
	}
	public String getDataLimiteDesconto() {
		return dataLimiteDesconto;
	}
	public void setDataLimiteDesconto(String dataLimiteDesconto) {
		this.dataLimiteDesconto = dataLimiteDesconto;
	}
	public String getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	public String getInstrucao() {
		return instrucao;
	}
	public void setInstrucao(String instrucao) {
		this.instrucao = instrucao;
	}
	public String getInformativo() {
		return informativo;
	}
	public void setInformativo(String informativo) {
		this.informativo = informativo;
	}
	public String getCodigoBarra() {
		return codigoBarra;
	}
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	public String getChaveTransacao() {
		return chaveTransacao;
	}
	public void setChaveTransacao(String chaveTransacao) {
		this.chaveTransacao = chaveTransacao;
	}
	
	
	public Date getDataExpiracao() {
		if(dataExpiracao == null ) {
			dataExpiracao = new Date();
		}
		return dataExpiracao;
	}
	public void setDataExpiracao(Date dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getValorLiquidado() {
		return valorLiquidado;
	}
	public void setValorLiquidado(String valorLiquidado) {
		this.valorLiquidado = valorLiquidado;
	}
	public String getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	 
	
	
	

}
