package webservice.pix.comuns;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pix")
public class PixRSVO {
	
	private String endToEndId;
	
	private String txid;
	
	private String valor;
	
	private String horario;
	// 140 maxLength	
	private String infoPagador;
	
	private List<DevolucaoPixRSVO> devolucoes;
	
	private String link;
	
	private String qrCode;
	
	private PessoaPixRSVO pagador;

	@XmlElement(name = "endToEndId")
	public String getEndToEndId() {
		return endToEndId;
	}

	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}

	@XmlElement(name = "txid")
	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}
	
	@XmlElement(name = "valor")
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	@XmlElement(name = "horario")
	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	@XmlElement(name = "infoPagador")
	public String getInfoPagador() {
		return infoPagador;
	}

	public void setInfoPagador(String infoPagador) {
		this.infoPagador = infoPagador;
	}

	@XmlElement(name = "devolucoes")
	public List<DevolucaoPixRSVO> getDevolucoes() {		
		return devolucoes;
	}

	public void setDevolucoes(List<DevolucaoPixRSVO> devolucoes) {
		this.devolucoes = devolucoes;
	}

	@XmlElement(name = "link")
	public String getLink() {		
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@XmlElement(name = "qrCode")
	public String getQrCode() {		
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public PessoaPixRSVO getPagador() {
		return pagador;
	}

	public void setPagador(PessoaPixRSVO pagador) {
		this.pagador = pagador;
	}
	
	
	
	
	
	

}
