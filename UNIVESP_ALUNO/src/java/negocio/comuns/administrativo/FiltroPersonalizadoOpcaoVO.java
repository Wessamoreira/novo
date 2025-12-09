package negocio.comuns.administrativo;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

public class FiltroPersonalizadoOpcaoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private FiltroPersonalizadoVO filtroPersonalizadoVO;
	private String descricaoOpcao;
	private String keyOpcao;
	private Integer ordem;
	private Boolean selecionado;
	private String campoQuery;

	
	public FiltroPersonalizadoOpcaoVO() {
		super();
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

	public String getDescricaoOpcao() {
		if (descricaoOpcao == null) {
			descricaoOpcao = "";
		}
		return descricaoOpcao;
	}

	public void setDescricaoOpcao(String descricaoOpcao) {
		this.descricaoOpcao = descricaoOpcao;
	}

	public FiltroPersonalizadoVO getFiltroPersonalizadoVO() {
		if (filtroPersonalizadoVO == null) {
			filtroPersonalizadoVO = new FiltroPersonalizadoVO();
		}
		return filtroPersonalizadoVO;
	}

	public void setFiltroPersonalizadoVO(FiltroPersonalizadoVO filtroPersonalizadoVO) {
		this.filtroPersonalizadoVO = filtroPersonalizadoVO;
	}

	public String getKeyOpcao() {
		if (keyOpcao == null) {
			keyOpcao = "";
		}
		return keyOpcao;
	}

	public void setKeyOpcao(String keyOpcao) {
		this.keyOpcao = keyOpcao;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getCampoQuery() {
		if (campoQuery == null) {
			campoQuery = "";
		}
		return campoQuery;
	}

	public void setCampoQuery(String campoQuery) {
		this.campoQuery = campoQuery;
	}

	

}
