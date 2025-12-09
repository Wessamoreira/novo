package negocio.comuns.utilitarias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StreamSeiException extends RuntimeException implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2218944305905509436L;
	private List<String> listaMensagemErro;

	public StreamSeiException(Throwable t) {
		super(t);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage()
				.replaceAll("negocio.comuns.utilitarias.StreamSeiException:", "")
				.replaceAll("java.lang.Exception:", "");
	}

	public StreamSeiException(String msgErro) {
		super(msgErro);
	}

	public StreamSeiException() {

	}

	public boolean existeErroListaMensagemErro() {
		if (listaMensagemErro != null && getListaMensagemErro().size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public void adicionarListaMensagemErro(String erro) {
		if (!this.getListaMensagemErro().contains(erro)) {
			this.getListaMensagemErro().add(erro);
		}
	}

	public List<String> getListaMensagemErro() {
		if (listaMensagemErro == null) {
			this.listaMensagemErro = new ArrayList<String>();
		}
		return listaMensagemErro;
	}

	public void setListaMensagemErro(List<String> listaMensagemErro) {
		this.listaMensagemErro = listaMensagemErro;
	}

	public String getToStringMensagemErro() {
		if (getListaMensagemErro().isEmpty()) {
			return getMessage();
		}
		StringBuilder erros = new StringBuilder();
		for (String erro : getListaMensagemErro()) {
			if (erros.length() != 0) {
				erros.append("</br>");
			}
			erros.append(erro);
		}
		return erros.toString();
	}

}
