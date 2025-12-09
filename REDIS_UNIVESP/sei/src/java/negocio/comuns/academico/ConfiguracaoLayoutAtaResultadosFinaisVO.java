package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class ConfiguracaoLayoutAtaResultadosFinaisVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7515447197198525438L;
	private Integer codigo;
	private String titulo;
	private String chave;
	private Boolean layoutFixoSistema;
	private Boolean inativarLayout;
	private Boolean layoutPadrao;
	private List<ArquivoVO> arquivoPdfVOs;
	private ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo =  0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getTitulo() {
		if(chave == null) {
			chave =  "";
		}
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getChave() {
		if(chave == null) {
			chave =  "";
		}
		return chave;
	}
	public void setChave(String chave) {
		
		this.chave = chave;
	}
	public Boolean getLayoutFixoSistema() {
		if(layoutFixoSistema == null) {
			layoutFixoSistema =  false;
		}
		return layoutFixoSistema;
	}
	public void setLayoutFixoSistema(Boolean layoutFixoSistema) {
		this.layoutFixoSistema = layoutFixoSistema;
	}
	public Boolean getInativarLayout() {
		if(inativarLayout == null) {
			inativarLayout =  false;
		}
		return inativarLayout;
	}
	public void setInativarLayout(Boolean inativarLayout) {
		this.inativarLayout = inativarLayout;
	}
	public Boolean getLayoutPadrao() {
		if(layoutPadrao == null) {
			layoutPadrao =  false;
		}
		return layoutPadrao;
	}
	public void setLayoutPadrao(Boolean layoutPadrao) {
		this.layoutPadrao = layoutPadrao;
	}
	public List<ArquivoVO> getArquivoPdfVOs() {
		if(arquivoPdfVOs == null) {
			arquivoPdfVOs =  new ArrayList<ArquivoVO>(0);
		}
		return arquivoPdfVOs;
	}
	public void setArquivoPdfVOs(List<ArquivoVO> arquivoPdfVOs) {
		this.arquivoPdfVOs = arquivoPdfVOs;
	}
	public ConfiguracaoAtaResultadosFinaisVO getConfiguracaoAtaResultadosFinaisVO() {
		if(configuracaoAtaResultadosFinaisVO == null) {
			configuracaoAtaResultadosFinaisVO =  new ConfiguracaoAtaResultadosFinaisVO();
		}
		return configuracaoAtaResultadosFinaisVO;
	}
	public void setConfiguracaoAtaResultadosFinaisVO(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO) {
		this.configuracaoAtaResultadosFinaisVO = configuracaoAtaResultadosFinaisVO;
	}

	public String chaveUsar;
	public String getChaveUsar() {
		if(chaveUsar == null) {
		if(getLayoutFixoSistema()) {
			chaveUsar = getChave();
		}else {
			chaveUsar = getCodigo().toString();
		}
		}
		return chaveUsar;
	}
	
	public ArquivoVO getArquivoPdfPrincipal() {
		if(getArquivoPdfVOs().stream().anyMatch(a -> a.getArquivoIreportPrincipal())) {
			return getArquivoPdfVOs().stream().filter(a -> a.getArquivoIreportPrincipal()).findFirst().get();
		}
		return null;
	}
	
	public String getNomeArquivoPdfPrincipal() {
		if(getArquivoPdfVOs().stream().anyMatch(a -> a.getArquivoIreportPrincipal())) {
			return getArquivoPdfVOs().stream().filter(a -> a.getArquivoIreportPrincipal()).findFirst().get().getNome();
		}
		return null;
	}
	
	public String getPastaBaseArquivoPdfPrincipal() {
		if(getArquivoPdfVOs().stream().anyMatch(a -> a.getArquivoIreportPrincipal())) {
			return getArquivoPdfVOs().stream().filter(a -> a.getArquivoIreportPrincipal()).findFirst().get().getPastaBaseArquivo();
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chaveUsar == null) ? 0 : chaveUsar.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfiguracaoLayoutAtaResultadosFinaisVO other = (ConfiguracaoLayoutAtaResultadosFinaisVO) obj;
		if (chaveUsar == null) {
			if (other.chaveUsar != null)
				return false;
		} else if (!chaveUsar.equals(other.chaveUsar))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	
	
}
