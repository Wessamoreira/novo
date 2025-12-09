/**
 * 
 */
package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum TipoPerfilAcessoPermissaoEnum {
	
	ENTIDADE(1, UteisJSF.internacionalizar("enum_TipoPerfilAcessoPermissaoEnum_ENTIDADE")), 
	RELATORIO(1,  UteisJSF.internacionalizar("enum_TipoPerfilAcessoPermissaoEnum_RELATORIO")),
	FUNCIONALIDADE(2,  UteisJSF.internacionalizar("enum_TipoPerfilAcessoPermissaoEnum_FUNCIONALIDADE"));   

	/**
	 * @param valor
	 * @param descricao
	 */
	private TipoPerfilAcessoPermissaoEnum(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	private Integer valor;
	private String descricao;

	/**
	 * @return the valor
	 */
	public Integer getValor() {
		if (valor == null) {
			valor = 0;
		}
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Integer valor) {
		this.valor = valor;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getIsEntidade(){
		return this.name().equals(TipoPerfilAcessoPermissaoEnum.ENTIDADE.name());
	}

	public Boolean getIsRelatorio(){
		return this.name().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO.name());
	} 

	public Boolean getIsFuncionalidade(){
		return this.name().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE.name());
	} 
}
