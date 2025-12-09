package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade TipoTransporte. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoTransporteVO extends SuperVO {

	private static final long serialVersionUID = -15576627305213335L;

	private Integer codigo;
	private String descricao;

	public enum EnumCampoConsultaTipoTransporte {
		DESCRICAO, CODIGO;
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
