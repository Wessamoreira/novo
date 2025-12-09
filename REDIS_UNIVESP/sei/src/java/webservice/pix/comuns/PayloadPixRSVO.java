package webservice.pix.comuns;

import java.util.List;

import webservice.arquitetura.InfoWSVO;

public class PayloadPixRSVO {

	private CalendarioPixRSVO calendario;
	private PessoaPixRSVO devedor;
	private PessoaPixRSVO recebedor;
	private LocationDoPayloadPixRSVO loc;
	private ValorCobrancaComVencimentoPixRSVO valor;
	private String chave;
	private String solicitacaoPagador;

	private String status;
	private String location;
	private String txid;	
	private Integer revisao;
	// Banco do Brasil uso a atributo abaixo para QRcode
	private String textoImagemQRcode;
	private List<PixRSVO> pix;
	private List<InfoWSVO> erros;

	
	public CalendarioPixRSVO getCalendario() {
		return calendario;
	}

	public void setCalendario(CalendarioPixRSVO calendario) {
		this.calendario = calendario;
	}

	public PessoaPixRSVO getDevedor() {
		return devedor;
	}

	public void setDevedor(PessoaPixRSVO devedor) {
		this.devedor = devedor;
	}

	public LocationDoPayloadPixRSVO getLoc() {
		return loc;
	}

	public void setLoc(LocationDoPayloadPixRSVO loc) {
		this.loc = loc;
	}

	public ValorCobrancaComVencimentoPixRSVO getValor() {
		return valor;
	}

	public void setValor(ValorCobrancaComVencimentoPixRSVO valor) {
		this.valor = valor;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getSolicitacaoPagador() {
		return solicitacaoPagador;
	}

	public void setSolicitacaoPagador(String solicitacaoPagador) {
		this.solicitacaoPagador = solicitacaoPagador;
	}

	public PessoaPixRSVO getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(PessoaPixRSVO recebedor) {
		this.recebedor = recebedor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Integer getRevisao() {
		return revisao;
	}

	public void setRevisao(Integer revisao) {
		this.revisao = revisao;
	}

	public String getTextoImagemQRcode() {		
		return textoImagemQRcode;
	}

	public void setTextoImagemQRcode(String textoImagemQRcode) {
		this.textoImagemQRcode = textoImagemQRcode;
	}
	
	public List<PixRSVO> getPix() {
		return pix;
	}

	public void setPix(List<PixRSVO> pix) {
		this.pix = pix;
	}

	public List<InfoWSVO> getErros() {
		return erros;
	}

	public void setErros(List<InfoWSVO> erros) {
		this.erros = erros;
	}
	
	
	
	

}
