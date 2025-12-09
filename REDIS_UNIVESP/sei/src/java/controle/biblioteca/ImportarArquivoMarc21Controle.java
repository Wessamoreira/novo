package controle.biblioteca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("ImportarArquivoMarc21Controle")
@Scope(value = "request")
@Lazy
public class ImportarArquivoMarc21Controle extends SuperControle implements Serializable {

	private ArquivoMarc21VO arquivoMarc21VO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemBiblioteca;

	public ImportarArquivoMarc21Controle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("dataProcessamento");
		getControleConsulta().setDataIni(Uteis.obterDataAntiga(new Date(), 10));
		getControleConsulta().setDataFim(new Date());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		removerObjetoMemoria(this);
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public void processarArquivo() {
		try {
			getFacadeFactory().getArquivoMarc21Facade().executarProcessarArquivoMarc21(getArquivoMarc21VO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setMensagemID("msg_arquivoProcessado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preencherTodosListaArquivoMarcCatalogo() {
		for (ArquivoMarc21CatalogoVO marcCatalogoVO : getArquivoMarc21VO().getArquivoMarc21CatalogoVOs()) {
			if (marcCatalogoVO.getCatalogoVO().getCodigo().equals(0)) {
				marcCatalogoVO.setSelecionado(true);
			}
		}
	}

	public void desmarcarTodosListaArquivoMarcCatalogo() {
		for (ArquivoMarc21CatalogoVO marcCatalogoVO : getArquivoMarc21VO().getArquivoMarc21CatalogoVOs()) {
			marcCatalogoVO.setSelecionado(false);
		}
	}

	public String gravar() {
		try {
			if (getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_ArquivoMarc21_mensagemErroGravar"));
			}
			getArquivoMarc21VO().setResponsavel(getUsuarioLogadoClone());
			if (getArquivoMarc21VO().getCodigo().equals(0)) {
				getFacadeFactory().getArquivoMarc21Facade().incluir(getArquivoMarc21VO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getArquivoMarc21Facade().alterar(getArquivoMarc21VO(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<ArquivoMarc21VO>(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}
	
	public void limparCampoConsulta() {
		getControleConsulta().setValorConsulta("");
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
	}

	public String consultar() {
		try {
			super.consultar();
			List<ArquivoMarc21VO> arquivoMarcVOs = new ArrayList<ArquivoMarc21VO>(0);
			if (getControleConsulta().getCampoConsulta().equals("responsavel")) {
				arquivoMarcVOs = getFacadeFactory().getArquivoMarc21Facade().consultarPorResponsavel(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
				arquivoMarcVOs = getFacadeFactory().getArquivoMarc21Facade().consultarPorDataProcessamento(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsulta(arquivoMarcVOs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<ArquivoMarc21VO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public String editar() {
		try {
			ArquivoMarc21VO obj = (ArquivoMarc21VO) context().getExternalContext().getRequestMap().get("arquivoMarc21");
			obj.setArquivoMarcCatalogoVOs(getFacadeFactory().getArquivoMarc21CatalogoFacade().consultarPorCodigoArquivoMarc21(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			obj.setNovoObj(Boolean.FALSE);
			setArquivoMarc21VO(obj);
			setMensagemID("msg_dados_editar");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void upLoadArquivo(FileUploadEvent upload) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(upload, getArquivoMarc21VO().getArquivoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			upload = null;
		}
	}

	public boolean getIsExibirGravar() {
		if ((!getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().isEmpty() && getArquivoMarc21VO().getCodigo().equals(0)) || (!getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().isEmpty() && !getArquivoMarc21VO().getCodigo().equals(0))) {
			return true;
		}
		return false;
	}

	public boolean getIsArquivoSelecionado() {
		if ((getArquivoMarc21VO().getArquivoVO().getNome() != null && !getArquivoMarc21VO().getArquivoVO().getNome().equals(""))) {
			return true;
		}
		return false;
	}

	public boolean getIsConsultaPorDataProcessamentoSelecionado() {
		if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
			return true;
		}
		return false;
	}

	public boolean getIsConsultaPorResponsavelSelecionado() {
		if (getControleConsulta().getCampoConsulta().equals("responsavel")) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("dataProcessamento", UteisJSF.internacionalizar("prt_ImportarArquivoMarc21_dataProcessamento")));
		itens.add(new SelectItem("responsavel", UteisJSF.internacionalizar("prt_ImportarArquivoMarc21_responsavel")));
		return itens;
	}

	public ArquivoMarc21VO getArquivoMarc21VO() {
		if (arquivoMarc21VO == null) {
			arquivoMarc21VO = new ArquivoMarc21VO();
		}
		return arquivoMarc21VO;
	}

	public void setArquivoMarc21VO(ArquivoMarc21VO arquivoMarc21VO) {
		this.arquivoMarc21VO = arquivoMarc21VO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemBiblioteca() {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemBiblioteca;
	}

	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

}
