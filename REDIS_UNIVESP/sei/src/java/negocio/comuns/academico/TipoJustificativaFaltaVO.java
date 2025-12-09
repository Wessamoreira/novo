package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade TipoJustificativaFalta. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoJustificativaFaltaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private String descricao;
	private String sigla;
	/**
	 * Legenda
	 * 
	 * AB - Ambos
	 * M - Masculino
	 * F - Feminino
	 */
	private String tipoSexoJustificativa;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		if(descricao == null){
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getTipoSexoJustificativa() {
		if(tipoSexoJustificativa == null){
			tipoSexoJustificativa = "AB";
		}
		return tipoSexoJustificativa;
	}
	
	public void setTipoSexoJustificativa(String tipoSexoJustificativa) {
		this.tipoSexoJustificativa = tipoSexoJustificativa;
	}
	
	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	
	
}
