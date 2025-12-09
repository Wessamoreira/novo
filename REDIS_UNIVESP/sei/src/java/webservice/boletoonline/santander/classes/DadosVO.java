package webservice.boletoonline.santander.classes;

import java.util.ArrayList;
import java.util.List;

public class DadosVO {

	public List<EntryVO> listaEntry;

	public DadosVO () {
		setListaEntry(new ArrayList<EntryVO>());
	}
	
	public List<EntryVO> getListaEntry() {
		return listaEntry;
	}

	public void setListaEntry(List<EntryVO> listaEntry) {
		this.listaEntry = listaEntry;
	}

}
