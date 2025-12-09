package negocio.comuns.arquitetura.enumeradores;

import java.util.List;

/**
 * @author Otimize-TI
 *
 */
public interface PerfilAcessoPermissaoEnumInterface {
	/**
	 * @author Otimize-TI - 28 de set de 2016 
	 */
	
	/**
	 * @return the tipoPerfilAcesso
	 */
	public TipoPerfilAcessoPermissaoEnum getTipoPerfilAcesso();
	/**
	 * @param tipoPerfilAcesso the tipoPerfilAcesso to set
	 */
	public void setTipoPerfilAcesso(TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso) ;
	
	/**
	 * @return the perfilAcessoSubModulo
	 */
	public PerfilAcessoSubModuloEnum getPerfilAcessoSubModulo();
	/**
	 * @param perfilAcessoSubModulo the perfilAcessoSubModulo to set
	 */
	public void setPerfilAcessoSubModulo(PerfilAcessoSubModuloEnum perfilAcessoSubModulo) ;
		
	/**
	 * @return the valor
	 */
	public String getValor() ;

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) ;
	
	/**
	 * @return the permissaoSuperiorEnum
	 */
	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissaoSuperiorEnum() ;

	/**
	 * @param permissaoSuperiorEnum the permissaoSuperiorEnum to set
	 */
	public void setPermissaoSuperiorEnum(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum) ;
	
	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum);	
	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum);
	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum);
	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum);
	public Boolean getIsApresentarApenasPermissaoTotal();	
	public Boolean getUtilizaVisao(TipoVisaoEnum tipoVisaoEnum) ;
	public PermissaoVisao[] getPermissaoVisao() ;
	public void setPermissaoVisao(PermissaoVisao[] PermissaoVisao) ;
	public String getDescricaoModulo();
	public String getDescricaoSubModulo();
	public String getDescricaoModuloSubModulo();
}
