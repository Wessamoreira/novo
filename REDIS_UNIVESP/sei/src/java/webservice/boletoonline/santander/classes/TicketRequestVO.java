package webservice.boletoonline.santander.classes;

import java.util.ArrayList;
import java.util.List;

public class TicketRequestVO {

	//public DadosVO dados;
	public List<EntryVO> listaEntry;
	public String expiracao;
	public String sistema;
	
	public TicketRequestVO () {
		//setDados(new DadosVO());
		setListaEntry(new ArrayList<EntryVO>());
		setExpiracao("100");
		setSistema("YMB");
	}
	
	public List<EntryVO> getListaEntry() {
		return listaEntry;
	}

	public void setListaEntry(List<EntryVO> listaEntry) {
		this.listaEntry = listaEntry;
	}
	
	public String getExpiracao() {
		return expiracao;
	}
	public void setExpiracao(String expiracao) {
		this.expiracao = expiracao;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

}
