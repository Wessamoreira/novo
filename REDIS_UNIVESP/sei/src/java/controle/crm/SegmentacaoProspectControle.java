package controle.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoLayoutApresentacaoResultadoSegmentacaoEnum;
import negocio.comuns.segmentacao.SegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas segmentacaoProspectForm.jsp segmentacaoProspectCons.jsp).
 * 
 * @see SuperControle
 */

@Controller("SegmentacaoProspectControle")
@Scope("viewScope")
@Lazy
public class SegmentacaoProspectControle extends SuperControle implements Serializable {

	private SegmentacaoProspectVO segmentacaoProspectVO;
	private SegmentacaoOpcaoVO segmentacaoOpcaoVO;
	private String tipoGrafico;
	private List<SelectItem> listaSelectItemTipoLayoutApresentacaoResultadoSegmentacaoEnum;

	public SegmentacaoProspectControle() {
		inicializarObjetoSegmentacaoPropectVO();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_segmentacao_inicio");
		setMensagemDetalhada("");
	}

	public List getTipoConsultaCombo() {
		return TipoGraficoEnum.getCombobox();
	}

	public String adicionarItemSegmetacaoOpcao() {
		try {
			getFacadeFactory().getSegmentacaOpcaoFacade().validarDadosSegmentacaoOpcao(segmentacaoOpcaoVO);
			if (!getSegmentacaoProspectVO().getCodigo().equals(0)) {
				segmentacaoOpcaoVO.setSegmentacaoProspectVO(getSegmentacaoProspectVO());
			}
			getFacadeFactory().getSegmentacaoProspectFacade().adicionarObjSegmentacaoVOs(segmentacaoProspectVO, segmentacaoOpcaoVO);
			setMensagemID("msg_segmentacao_adicionada");
			setSegmentacaoOpcaoVO(new SegmentacaoOpcaoVO());
		} catch (Exception e) {
			setMensagemID("msg_segmentacao_vazio");
			setMensagemDetalhada(e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
	}

	public String gravar() {
		try {
			getFacadeFactory().getSegmentacaoProspectFacade().persistir(getSegmentacaoProspectVO(), getConfiguracaoGeralPadraoSistema(), false, getUsuarioLogado());
			setMensagemID("msg_segmentacao_gravada");
		} catch (Exception e) {
			setMensagemID("msg_segmentacao_vazio");
			setMensagemDetalhada(e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
	}

	public String novo() {
		setSegmentacaoProspectVO(new SegmentacaoProspectVO());
		setSegmentacaoOpcaoVO(new SegmentacaoOpcaoVO());
		setMensagemID("msg_segmentacao_inicio");
		setMensagemDetalhada("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
	}

	public String consultarSegmentacao() {
		setMensagemID("msg_segmentacao_inicio");
		setMensagemDetalhada("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectCons.xhtml");
	}

	public String removerSegmentacaoOpcaoVOs() {

		try {
			SegmentacaoOpcaoVO obj = (SegmentacaoOpcaoVO) context().getExternalContext().getRequestMap().get("segmentacaoItens");
			setSegmentacaoOpcaoVO(obj);
			getFacadeFactory().getSegmentacaOpcaoFacade().removerSegmentacaoOpcaoVOs(segmentacaoProspectVO, segmentacaoOpcaoVO);
			setSegmentacaoOpcaoVO(new SegmentacaoOpcaoVO());
			setMensagemID("msg_segmentacao_removida");
		} catch (Exception e) {
			setMensagemID("msg_segmentacao_vazio");
			setMensagemDetalhada(e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
	}

	public void inicializarObjetoSegmentacaoPropectVO() {
		SegmentacaoProspectVO segmentacao;
		segmentacao = (SegmentacaoProspectVO) context().getExternalContext().getSessionMap().get(SegmentacaoProspectVO.class.getSimpleName());
		if (segmentacao != null) {
			setSegmentacaoProspectVO(segmentacao);
		}
	}

	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "SegmentacaoProspectControle", "Inicializando Consultar Segmento", "Consultando");
			super.consultar();
			List objs = new ArrayList(0);
			objs = getFacadeFactory().getSegmentacaoProspectFacade().consultarSegmento(getControleConsulta().getCampoConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "SegmentacaoProspectControle", "Finalizando Consultar Segmento", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectCons.xhtml");
		}
	}

	public String editarSegmentacaoProspect() throws Exception {
		SegmentacaoProspectVO obj = (SegmentacaoProspectVO) context().getExternalContext().getRequestMap().get("segmentoItens");
		setSegmentacaoProspectVO(getFacadeFactory().getSegmentacaoProspectFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getSegmentacaoProspectVO().setNovoObj(Boolean.FALSE);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
	}

	public void editarSegmentacaoOpcao() throws Exception {
		SegmentacaoOpcaoVO obj = (SegmentacaoOpcaoVO) context().getExternalContext().getRequestMap().get("segmentacaoItens");
		setSegmentacaoOpcaoVO(obj);
	}

	public String excluirSegmentacaoProspectVO() {
		try {
			getFacadeFactory().getSegmentacaoProspectFacade().excluirSegmentacaoProspect(segmentacaoProspectVO, getUsuarioLogado());
			setSegmentacaoProspectVO(new SegmentacaoProspectVO());
			this.consultar();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("segmentacaoProspectForm.xhtml");
		}
	}

	public List getAtivarSegmentacao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("ATIVAR", "Ativa"));
		itens.add(new SelectItem("INATIVAR", "Inativa"));
		return itens;
	}

	public String getApresentarMensagemInativacaoSegmentacao() {
		if (getSegmentacaoProspectVO().getAtivaSegmentacao().equals("INATIVAR")) {
			setMensagemID("");
			setMensagemDetalhada("");
			return "alert('Ao inativar uma segmentação, a mesma não estará disponível no cadastro de Prospects');";
		} else {
			return "";
		}
	}

	// Consulta se existe um prospect vinculado a uma segmentaçãoopção, baseando
	// no código do segmentoprospect.
	public void consultaProspectVinculado() {
		try {
			if (getSegmentacaoProspectVO().getAtivaSegmentacao().equals("INATIVAR")) {
				getFacadeFactory().getSegmentacaoProspectFacade().consultaProspectVinculado(segmentacaoProspectVO);
				setMensagemID("msg_segmentacao_vazio");
				setMensagemDetalhada("Ao inativar uma segmentação, a mesma não estará disponível no cadastro de Prospects");
			}

		} catch (Exception e) {
			getSegmentacaoProspectVO().setAtivaSegmentacao("Ativa");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizaValidacaoSegmentacao() {
		segmentacaoProspectVO.setVerificaMudancaDescricaoSegmentacao(Boolean.TRUE);
		
	}

	public SegmentacaoProspectVO getSegmentacaoProspectVO() {

		if (segmentacaoProspectVO == null) {
			segmentacaoProspectVO = new SegmentacaoProspectVO();
		}

		return segmentacaoProspectVO;
	}

	public void setSegmentacaoProspectVO(SegmentacaoProspectVO segmentacaoProspectVO) {
		this.segmentacaoProspectVO = segmentacaoProspectVO;
	}

	public String getTipoGrafico() {
		if (tipoGrafico == null) {
			tipoGrafico = "";
		}
		return tipoGrafico;
	}

	public void setTipoGrafico(String tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}

	public SegmentacaoOpcaoVO getSegmentacaoOpcaoVO() {
		if (segmentacaoOpcaoVO == null) {
			segmentacaoOpcaoVO = new SegmentacaoOpcaoVO();
		}

		return segmentacaoOpcaoVO;
	}

	public void setSegmentacaoOpcaoVO(SegmentacaoOpcaoVO segmentacaoOpcaoVO) {
		this.segmentacaoOpcaoVO = segmentacaoOpcaoVO;
	}
	
	public List<SelectItem> getListaSelectItemTipoLayoutApresentacaoResultadoSegmentacaoEnum() {
		if(listaSelectItemTipoLayoutApresentacaoResultadoSegmentacaoEnum == null) {
			listaSelectItemTipoLayoutApresentacaoResultadoSegmentacaoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoLayoutApresentacaoResultadoSegmentacaoEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectItemTipoLayoutApresentacaoResultadoSegmentacaoEnum;
	}

}
