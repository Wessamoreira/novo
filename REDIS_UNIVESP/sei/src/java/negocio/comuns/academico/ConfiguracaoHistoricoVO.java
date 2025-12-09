package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.negocio.comuns.academico.enumeradores.TipoObservacaoHistoricoEnum;

public class ConfiguracaoHistoricoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8010391724936845267L;
	private Integer codigo;
	private TipoNivelEducacional nivelEducacional;	
	private List<ConfiguracaoLayoutHistoricoVO> configuracaoLayoutHistoricoVOs;
	private List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoVOs;
	

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoNivelEducacional getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional =  TipoNivelEducacional.SUPERIOR;
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(TipoNivelEducacional nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public List<ConfiguracaoLayoutHistoricoVO> getConfiguracaoLayoutHistoricoVOs() {
		if(configuracaoLayoutHistoricoVOs == null) {
			configuracaoLayoutHistoricoVOs =  new ArrayList<ConfiguracaoLayoutHistoricoVO>(0);
		}
		return configuracaoLayoutHistoricoVOs;
	}

	public void setConfiguracaoLayoutHistoricoVOs(List<ConfiguracaoLayoutHistoricoVO> configuracaoLayoutHistoricoVOs) {
		this.configuracaoLayoutHistoricoVOs = configuracaoLayoutHistoricoVOs;
	}

	public List<ConfiguracaoObservacaoHistoricoVO> getConfiguracaoObservacaoHistoricoVOs() {
		if(configuracaoObservacaoHistoricoVOs == null) {
			configuracaoObservacaoHistoricoVOs =  new ArrayList<ConfiguracaoObservacaoHistoricoVO>(0);
		}
		return configuracaoObservacaoHistoricoVOs;
	}

	public void setConfiguracaoObservacaoHistoricoVOs(
			List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoVOs) {
		this.configuracaoObservacaoHistoricoVOs = configuracaoObservacaoHistoricoVOs;
	}

	public String observacaoPadraoIntegralizacao;
	public String getObservacaoPadraoIntegralizacao() {
		if(observacaoPadraoIntegralizacao == null) {
			if(getConfiguracaoObservacaoHistoricoVOs().stream().anyMatch(o -> o.getPadrao() && !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.INTEGRALIZACAO))) {
				observacaoPadraoIntegralizacao = getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> o.getPadrao() && !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.INTEGRALIZACAO)).findFirst().get().getObservacao();
			}else {
				observacaoPadraoIntegralizacao =  "";
			}
		}
		return observacaoPadraoIntegralizacao;
	}
	
	public String observacaoPadraoComplementar;
	public String getObservacaoPadraoComplementar() {
		if(observacaoPadraoComplementar == null) {
			if(getConfiguracaoObservacaoHistoricoVOs().stream().anyMatch(o -> o.getPadrao() && !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.COMPLEMENTAR))) {
				observacaoPadraoComplementar = getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> o.getPadrao() && !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.COMPLEMENTAR)).findFirst().get().getObservacao();
			}else {
				observacaoPadraoComplementar =  "";
			}
		}
		return observacaoPadraoComplementar;
	}
	
	public String observacaoPadraoCertificadoEstudos;
	public String getObservacaoPadraoCertificadoEstudos() {
		if(observacaoPadraoCertificadoEstudos == null) {
			if(getConfiguracaoObservacaoHistoricoVOs().stream().anyMatch(o -> o.getPadrao() && !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.CERTIFICADO_ESTUDO))) {
				observacaoPadraoCertificadoEstudos = getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> o.getPadrao() && !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.CERTIFICADO_ESTUDO)).findFirst().get().getObservacao();
			}else {
				observacaoPadraoCertificadoEstudos =  "";
			}
		}
		return observacaoPadraoCertificadoEstudos;
	}
	
	public List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoCertificadoEstudosVOs;
	public List<ConfiguracaoObservacaoHistoricoVO> getConfiguracaoObservacaoHistoricoCertificadoEstudosVOs() {
		if(configuracaoObservacaoHistoricoCertificadoEstudosVOs == null) {
			configuracaoObservacaoHistoricoCertificadoEstudosVOs = getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.CERTIFICADO_ESTUDO)).collect(Collectors.toList());
			if(configuracaoObservacaoHistoricoCertificadoEstudosVOs == null) {
				configuracaoObservacaoHistoricoCertificadoEstudosVOs = new ArrayList<ConfiguracaoObservacaoHistoricoVO>(0);
			}
		}
		return configuracaoObservacaoHistoricoCertificadoEstudosVOs;
	}
	
	public List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoIntegralizacaoVOs;
	public List<ConfiguracaoObservacaoHistoricoVO> getConfiguracaoObservacaoHistoricoIntegralizacaoVOs() {
		if(configuracaoObservacaoHistoricoIntegralizacaoVOs == null) {
			configuracaoObservacaoHistoricoIntegralizacaoVOs = getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.INTEGRALIZACAO)).collect(Collectors.toList());
			if(configuracaoObservacaoHistoricoIntegralizacaoVOs == null) {
				configuracaoObservacaoHistoricoIntegralizacaoVOs = new ArrayList<ConfiguracaoObservacaoHistoricoVO>(0);
			}
		}
		return configuracaoObservacaoHistoricoIntegralizacaoVOs;
	}
	
	public List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoComplementarVOs;
	public List<ConfiguracaoObservacaoHistoricoVO> getConfiguracaoObservacaoHistoricoComplementarVOs() {
		if(configuracaoObservacaoHistoricoComplementarVOs == null) {
			configuracaoObservacaoHistoricoComplementarVOs = getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> !o.getOcultar() && o.getTipoObservacaoHistorico().equals(TipoObservacaoHistoricoEnum.COMPLEMENTAR)).collect(Collectors.toList());
			if(configuracaoObservacaoHistoricoComplementarVOs == null) {
				configuracaoObservacaoHistoricoComplementarVOs = new ArrayList<ConfiguracaoObservacaoHistoricoVO>(0);
			}
		}
		return configuracaoObservacaoHistoricoComplementarVOs;
	}

	public void setObservacaoPadraoIntegralizacao(String observacaoPadraoIntegralizacao) {
		this.observacaoPadraoIntegralizacao = observacaoPadraoIntegralizacao;
	}

	public void setObservacaoPadraoComplementar(String observacaoPadraoComplementar) {
		this.observacaoPadraoComplementar = observacaoPadraoComplementar;
	}

	public void setObservacaoPadraoCertificadoEstudos(String observacaoPadraoCertificadoEstudos) {
		this.observacaoPadraoCertificadoEstudos = observacaoPadraoCertificadoEstudos;
	}

	public void setConfiguracaoObservacaoHistoricoCertificadoEstudosVOs(
			List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoCertificadoEstudosVOs) {
		this.configuracaoObservacaoHistoricoCertificadoEstudosVOs = configuracaoObservacaoHistoricoCertificadoEstudosVOs;
	}

	public void setConfiguracaoObservacaoHistoricoIntegralizacaoVOs(
			List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoIntegralizacaoVOs) {
		this.configuracaoObservacaoHistoricoIntegralizacaoVOs = configuracaoObservacaoHistoricoIntegralizacaoVOs;
	}

	public void setConfiguracaoObservacaoHistoricoComplementarVOs(
			List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoComplementarVOs) {
		this.configuracaoObservacaoHistoricoComplementarVOs = configuracaoObservacaoHistoricoComplementarVOs;
	}
	
	private List<SelectItem> listaSelectItemLayoutPadrao;


	public List<SelectItem> getListaSelectItemLayoutPadrao() {
		if(listaSelectItemLayoutPadrao == null && !getConfiguracaoLayoutHistoricoVOs().isEmpty()) {
			try {
				listaSelectItemLayoutPadrao = new ArrayList<SelectItem>(0);
				listaSelectItemLayoutPadrao.add(new SelectItem("", ""));
				for(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO: getConfiguracaoLayoutHistoricoVOs().stream().filter(l -> l.getLayoutFixoSistema()).collect(Collectors.toList())) {
					listaSelectItemLayoutPadrao.add(new SelectItem(configuracaoLayoutHistoricoVO.getChave(), configuracaoLayoutHistoricoVO.getDescricao()));
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
