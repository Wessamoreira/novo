/**
 * 
 */
package negocio.comuns.arquitetura.enumeradores;


/**
 * @author Rodrigo Wind
 *
 */
public class PermissaoVisao {
	/**
	 * @author Rodrigo Wind - 10/02/2016
	 */
	private String descricao;
	private String ajuda;
	private String[] paginaAcesso;
	private TipoVisaoEnum tipoVisaoEnum;

	/**
	 * @param descricao
	 * @param tipoVisaoEnum
	 */
	public PermissaoVisao(TipoVisaoEnum tipoVisaoEnum, String descricao, String ajuda, String[] paginaAcesso) {
		super();
		this.descricao = descricao;
		this.tipoVisaoEnum = tipoVisaoEnum;
		this.ajuda = ajuda;
		this.paginaAcesso = paginaAcesso;
	}

	/**
	 * @param descricao
	 * @param tipoVisaoEnum
	 */
	public PermissaoVisao(TipoVisaoEnum tipoVisaoEnum, String descricao, String ajuda) {
		super();
		this.descricao = descricao;
		this.tipoVisaoEnum = tipoVisaoEnum;
		this.ajuda = ajuda;
	}

	/**
	 * @param descricao
	 * @param tipoVisaoEnum
	 */
	public PermissaoVisao(TipoVisaoEnum tipoVisaoEnum, String descricao) {
		super();
		this.descricao = descricao;
		this.tipoVisaoEnum = tipoVisaoEnum;		
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
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the tipoVisaoEnum
	 */
	public TipoVisaoEnum getTipoVisaoEnum() {
		if (tipoVisaoEnum == null) {
			tipoVisaoEnum = TipoVisaoEnum.ADMINISTRATIVA;
		}
		return tipoVisaoEnum;
	}

	/**
	 * @param tipoVisaoEnum
	 *            the tipoVisaoEnum to set
	 */
	public void setTipoVisaoEnum(TipoVisaoEnum tipoVisaoEnum) {
		this.tipoVisaoEnum = tipoVisaoEnum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipoVisaoEnum == null) ? 0 : tipoVisaoEnum.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || (getClass() != obj.getClass() && !(obj instanceof TipoVisaoEnum)))
			return false;
		if ((obj instanceof TipoVisaoEnum)) {
			return tipoVisaoEnum.equals(obj);
		}
		return tipoVisaoEnum.equals(((PermissaoVisao) obj).tipoVisaoEnum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DescricaoPermissaoVisao [tipoVisaoEnum=" + getTipoVisaoEnum() + ", descricao=" + descricao + ", ajuda=" + ajuda + ", paginaAcesso=" + getPaginaAcesso().toString() + "]";
	}

	/**
	 * @return the ajuda
	 */
	public String getAjuda() {
		if (ajuda == null) {
			ajuda = "";
		}
		return ajuda;
	}

	/**
	 * @param ajuda
	 *            the ajuda to set
	 */
	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	/**
	 * @return the paginaAcesso
	 */
	public String[] getPaginaAcesso() {
		if (paginaAcesso == null) {
			paginaAcesso = new String[0];
		}
		return paginaAcesso;
	}

	/**
	 * @param paginaAcesso
	 *            the paginaAcesso to set
	 */
	public void setPaginaAcesso(String[] paginaAcesso) {
		this.paginaAcesso = paginaAcesso;
	}

}
