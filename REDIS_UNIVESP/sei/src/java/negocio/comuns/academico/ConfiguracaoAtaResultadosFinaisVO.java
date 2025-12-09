package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.negocio.comuns.academico.enumeradores.TipoObservacaoHistoricoEnum;

public class ConfiguracaoAtaResultadosFinaisVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8010391724936845267L;
	private Integer codigo;
	private List<ConfiguracaoLayoutAtaResultadosFinaisVO> configuracaoLayoutAtaResultadosFinaisVOs;
	

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<ConfiguracaoLayoutAtaResultadosFinaisVO> getConfiguracaoLayoutAtaResultadosFinaisVOs() {
		if(configuracaoLayoutAtaResultadosFinaisVOs == null) {
			configuracaoLayoutAtaResultadosFinaisVOs =  new ArrayList<ConfiguracaoLayoutAtaResultadosFinaisVO>(0);
		}
		return configuracaoLayoutAtaResultadosFinaisVOs;
	}

	public void setConfiguracaoLayoutAtaResultadosFinaisVOs(List<ConfiguracaoLayoutAtaResultadosFinaisVO> configuracaoLayoutAtaResultadosFinaisVOs) {
		this.configuracaoLayoutAtaResultadosFinaisVOs = configuracaoLayoutAtaResultadosFinaisVOs;
	}
	
	private List<SelectItem> listaSelectItemLayoutPadrao;


	public List<SelectItem> getListaSelectItemLayoutPadrao() {
		if(listaSelectItemLayoutPadrao == null && !getConfiguracaoLayoutAtaResultadosFinaisVOs().isEmpty()) {
			try {
				listaSelectItemLayoutPadrao = new ArrayList<SelectItem>(0);
				listaSelectItemLayoutPadrao.add(new SelectItem("", ""));
				for(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO: getConfiguracaoLayoutAtaResultadosFinaisVOs().stream().filter(l -> l.getLayoutFixoSistema()).collect(Collectors.toList())) {
					listaSelectItemLayoutPadrao.add(new SelectItem(configuracaoLayoutAtaResultadosFinaisVO.getChave(), configuracaoLayoutAtaResultadosFinaisVO.getTitulo()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaSelectItemLayoutPadrao;
	}

	public void setListaSelectItemLayoutPadrao(List<SelectItem> listaSelectItemLayoutPadrao) {
		this.listaSelectItemLayoutPadrao = listaSelectItemLayoutPadrao;
	}
	
	
	
}
