package negocio.comuns.estagio;

import negocio.comuns.arquitetura.SuperVO;

public class TipoConcedenteVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7437173514398026280L;
	private Integer codigo;
	private String nome;
	private boolean cnpjObrigatorio = false;	
	private Boolean codigoMECObrigatorio;
	private Boolean permitirCadastroConcedente;
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isCnpjObrigatorio() {
		return cnpjObrigatorio;
	}

	public void setCnpjObrigatorio(boolean cnpjObrigatorio) {
		this.cnpjObrigatorio = cnpjObrigatorio;
	}

	public Boolean getCodigoMECObrigatorio() {
		if (codigoMECObrigatorio == null) {
			codigoMECObrigatorio = Boolean.FALSE;
		}
		return codigoMECObrigatorio;
	}

	public void setCodigoMECObrigatorio(Boolean codigoMECObrigatorio) {
		this.codigoMECObrigatorio = codigoMECObrigatorio;
	}

	public Boolean getPermitirCadastroConcedente() {
		if (permitirCadastroConcedente == null) {
			permitirCadastroConcedente = Boolean.TRUE;
		}
		return permitirCadastroConcedente;
	}

	public void setPermitirCadastroConcedente(Boolean permitirCadastroConcedente) {
		this.permitirCadastroConcedente = permitirCadastroConcedente;
	}

}
