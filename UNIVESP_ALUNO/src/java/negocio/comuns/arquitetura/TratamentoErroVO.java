package negocio.comuns.arquitetura;

import java.io.Serializable;

public class TratamentoErroVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String erro;
	private String mensagemApresentar;
	
	public TratamentoErroVO() {
		
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
	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getMensagemApresentar() {
		if (mensagemApresentar == null) {
			mensagemApresentar = "";
		}
		return mensagemApresentar;
	}

	public void setMensagemApresentar(String mensagemApresentar) {
		this.mensagemApresentar = mensagemApresentar;
	}

}
