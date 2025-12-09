package negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

public class CieloCodigoRetornoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String codigoResposta;
	private String definicao;
	private String significado;
	private String acao;
	private String permiteRetentativa;
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getCodigoResposta() {
		if (codigoResposta == null) {
			codigoResposta = "";
		}
		return codigoResposta;
	}
	public void setCodigoResposta(String codigoResposta) {
		this.codigoResposta = codigoResposta;
	}
	public String getDefinicao() {
		if (definicao == null) {
			definicao = "";
		}
		return definicao;
	}
	public void setDefinicao(String definicao) {
		this.definicao = definicao;
	}
	public String getSignificado() {
		if (significado == null) {
			significado = "";
		}
		return significado;
	}
	public void setSignificado(String significado) {
		this.significado = significado;
	}
	public String getAcao() {
		if (acao == null) {
			acao = "";
		}
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public String getPermiteRetentativa() {
		if (permiteRetentativa == null) {
			permiteRetentativa = "";
		}
		return permiteRetentativa;
	}
	public void setPermiteRetentativa(String permiteRetentativa) {
		this.permiteRetentativa = permiteRetentativa;
	}
	

}
