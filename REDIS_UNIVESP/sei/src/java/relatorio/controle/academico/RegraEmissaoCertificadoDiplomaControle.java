package relatorio.controle.academico;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.RegraEmissaoUnidadeEnsinoVO;
import negocio.comuns.academico.RegraEmissaoVO;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.financeiro.CondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;


@SuppressWarnings("unchecked")
@Controller("RegraEmissaoCertificadoDiplomaControle")
@Scope("viewScope")
@Lazy
public class RegraEmissaoCertificadoDiplomaControle extends SuperControle implements Serializable {
	
	private RegraEmissaoVO regraEmissaoVO;
	private Boolean exibirTipoContrato= false;
	private List<SelectItem> listaSelectItemContrato;
	private List<RegraEmissaoVO> regraEmissaoVOs;
	private RegraEmissaoVO regraEmissaoSelecionada;
	private boolean permitirExibirRegraEmissao = false;
	
	
	public RegraEmissaoCertificadoDiplomaControle() {
		carregarNivelEducacional();
	}
	
	@PostConstruct
	public void consultarTodasRegrasEmissao() {
		try {
			setRegraEmissaoVOs(getFacadeFactory().getRegraEmissaoInterfaceFacade().consultarTodasRegrasEmissao(getUsuarioLogado()));
			montarUnidadeEnsinoRegraEmissao();
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
			verificarPermissaoRegraEmissao();
			montarApresentarTipoNivelEducacional(getRegraEmissaoVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	private void montarApresentarTipoNivelEducacional(List<RegraEmissaoVO> regraEmissaoVOs) {
		for(RegraEmissaoVO regraEmissaoVO :regraEmissaoVOs) {
			regraEmissaoVO.setTipoNivelEducacionalApresentar(TipoNivelEducacional.getDescricao(regraEmissaoVO.getNivelEducacional()));
		}
	}

	private void montarUnidadeEnsinoRegraEmissao() {
		for(RegraEmissaoVO regraEmissaoVO : getRegraEmissaoVOs()) {
			StringBuilder unidadeEnsino = new StringBuilder();
			for(RegraEmissaoUnidadeEnsinoVO regraEmissaoUnidadeEnsinoVO : regraEmissaoVO.getRegraEmissaoUnidadeEnsinoVOs()) {
				if(Uteis.isAtributoPreenchido(regraEmissaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					unidadeEnsino.append(regraEmissaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getNome().trim()).append("; ");
				}
			}
			regraEmissaoVO.setNomeUnidadeEnsisnoSelecionadas(unidadeEnsino.toString());
		}
	}
		
	public void carregarNivelEducacional() {
			setListaSelectItemContrato(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoContratoMatriculaEnum.class));
	}
	
	@Override
	public void marcarTodasUnidadesEnsinoAction() {
		try {
			for (UnidadeEnsinoVO unidadeEnsino : getUnidadeEnsinoVOs()) {
				if (getMarcarTodasUnidadeEnsino()) {
					unidadeEnsino.setFiltrarUnidadeEnsino(Boolean.TRUE);
				} else {
					unidadeEnsino.setFiltrarUnidadeEnsino(Boolean.FALSE);
				}
			}
			verificarTodasUnidadesSelecionadas();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}
	
	public void adicionarRegraEmissao() {
		try {
			getRegraEmissaoVO().setTipoNivelEducacionalApresentar(TipoNivelEducacional.getDescricao(getRegraEmissaoVO().getNivelEducacional()));
			getFacadeFactory().getRegraEmissaoInterfaceFacade().adicionarRegraEmissaoVOs(getRegraEmissaoVOs(), getRegraEmissaoVO());
			setRegraEmissaoVO(inicializarDadosRegraEmissao());
			consultarUnidadeEnsinoFiltroRelatorio("");
			montarUnidadeEnsinoRegraEmissao();
			verificarTodasUnidadesSelecionadas();
			verificarPermissaoRegraEmissao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}	
	}
	
	public void removerRegraEmissao() {
		try {
			//RegraEmissaoVO obj = (RegraEmissaoVO) context().getExternalContext().getRequestMap().get("regraEmissaoItem");
			getRegraEmissaoVO().getListaRegraEmissaoRemovidos().add(getRegraEmissaoSelecionada());
			getRegraEmissaoVOs().remove(getRegraEmissaoSelecionada());
			getFacadeFactory().getRegraEmissaoInterfaceFacade().excluir(getRegraEmissaoSelecionada(), false, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private RegraEmissaoVO inicializarDadosRegraEmissao() {
		RegraEmissaoVO obj = new RegraEmissaoVO();
		obj.setValidarDocumentosEntregues(false);
		return obj;
	}
	
	public void persistir() {
		try {
			getFacadeFactory().getRegraEmissaoInterfaceFacade().persistir(getRegraEmissaoVOs(), false, getUsuarioLogado());
			setRegraEmissaoVOs(new ArrayList<RegraEmissaoVO>());
			consultarTodasRegrasEmissao();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editar() {
		RegraEmissaoVO obj = (RegraEmissaoVO) context().getExternalContext().getRequestMap().get("regraEmissaoItem");
		setRegraEmissaoVO(obj);
		carregrarUnidadeEnsino(obj.getRegraEmissaoUnidadeEnsinoVOs());
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
	}
	
	private void carregrarUnidadeEnsino(List<RegraEmissaoUnidadeEnsinoVO> unidadeEnsinoVO) {
		for(RegraEmissaoUnidadeEnsinoVO emissaoUnidadeEnsinoVO : unidadeEnsinoVO) {
			for(UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if(obj.getCodigo().equals(emissaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					obj.setFiltrarUnidadeEnsino(true);
				}else {
					obj.setFiltrarUnidadeEnsino(false);
				}
			}
		}
		verificarTodasUnidadeEnsinoSelecionados();
	}

	public void verificarTodasUnidadesSelecionadas() {
		try {
			for (UnidadeEnsinoVO unidadeEnsino : getUnidadeEnsinoVOs()) {
				RegraEmissaoUnidadeEnsinoVO rgue = new RegraEmissaoUnidadeEnsinoVO();
				rgue.setUnidadeEnsinoVO(unidadeEnsino);
				rgue.setRegraEmissaoVO(getRegraEmissaoVO());
				
				if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
					getFacadeFactory().getRegraEmissaoInterfaceFacade().adicionarUnidadeEnsino(rgue,getRegraEmissaoVO());
				} else {
					getFacadeFactory().getRegraEmissaoInterfaceFacade().removerRegraEmissao(rgue, getRegraEmissaoVO());
				}
			}
			preencherUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void preencherUnidadeEnsino() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			getRegraEmissaoVO().setNomeUnidadeEnsisnoSelecionadas(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getRegraEmissaoVO().setNomeUnidadeEnsisnoSelecionadas(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
	}
	
	   
    private boolean verificarPermissaoRegraEmissao() {
    	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_DEFINIR_REGRA_EMISSAO_CERTIFICADO, getUsuarioLogado());
    		setPermitirExibirRegraEmissao(true);
		} catch (Exception e) {
			setPermitirExibirRegraEmissao(false);
		}
    	return isPermitirExibirRegraEmissao();
    }
	
	/* Getters and Setter */
	public RegraEmissaoVO getRegraEmissaoVO() {
		if (regraEmissaoVO == null) {
			regraEmissaoVO = new RegraEmissaoVO();
		}
		return regraEmissaoVO;
	}
	
	public void setRegraEmissaoVO(RegraEmissaoVO regraEmissaoVO) {
		this.regraEmissaoVO = regraEmissaoVO;
	}
	
	public List<SelectItem> getListaSelectItemTipoNivelEducacional() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class);
	}

	public Boolean getExibirTipoContrato() {
		if (exibirTipoContrato == null) {
			exibirTipoContrato = false;
		}
		return exibirTipoContrato;
	}

	public void setExibirTipoContrato(Boolean exibirTipoContrato) {
		this.exibirTipoContrato = exibirTipoContrato;
	}

	public List<SelectItem> getListaSelectItemContrato() {
		if (listaSelectItemContrato == null) {
			listaSelectItemContrato = new ArrayList<SelectItem>();
		}
		return listaSelectItemContrato;
	}

	public void setListaSelectItemContrato(List<SelectItem> listaSelectItemContrato) {
		this.listaSelectItemContrato = listaSelectItemContrato;
	}

	public List<RegraEmissaoVO> getRegraEmissaoVOs() {
		if (regraEmissaoVOs == null) {
			regraEmissaoVOs = new ArrayList<RegraEmissaoVO>();
		}
		return regraEmissaoVOs;
	}

	public void setRegraEmissaoVOs(List<RegraEmissaoVO> regraEmissaoVOs) {
		this.regraEmissaoVOs = regraEmissaoVOs;
	}

	public RegraEmissaoVO getRegraEmissaoSelecionada() {
		if (regraEmissaoSelecionada == null) {
			regraEmissaoSelecionada = new RegraEmissaoVO();
		}
		return regraEmissaoSelecionada;
	}

	public void setRegraEmissaoSelecionada(RegraEmissaoVO regraEmissaoSelecionada) {
		this.regraEmissaoSelecionada = regraEmissaoSelecionada;
	}
	
	public boolean isPermitirExibirRegraEmissao() {
		return permitirExibirRegraEmissao;
	}

	public void setPermitirExibirRegraEmissao(boolean permitirExibirRegraEmissao) {
		this.permitirExibirRegraEmissao = permitirExibirRegraEmissao;
	}
}