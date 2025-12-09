package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;

@Controller("OfertaDisciplinaControle")
@Scope("viewScope")
public class OfertaDisciplinaControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5670212699994709611L;
	private List<OfertaDisciplinaVO> ofertaDisciplinaVOs;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private List<SelectItem> listaSelectItemPeriodo;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<OfertaDisciplinaVO> listaDisciplina;
	private List<OfertaDisciplinaVO> listaImportarDisciplinaErro;
	private List<SelectItem> listaSelectItemOpcaoConsultaDisciplina;
	
	public void validarDados() throws Exception{		
		if(!Uteis.isAtributoPreenchido(getAno())) {
			throw new Exception("O campo ANO deve ser informado.");
		}
		if(getAno().length() != 4) {
			throw new Exception("O campo ANO deve ser informado com 4 dígitos.");
		}
		if(!Uteis.getIsValorNumerico(getAno())) {
			throw new Exception("O campo ANO deve ser informado apenas com valores numéricos.");
		}
		if(!Uteis.isAtributoPreenchido(getSemestre())) {
			throw new Exception("O campo SEMESTRE deve ser informado.");
		}
	}
	
	public void novo() {
		limparMensagem();
		setAno("");
		setSemestre("1");
		getOfertaDisciplinaVOs().clear();
	}
	
	public void persistir() {
		try {
			validarDados();
			getFacadeFactory().getOfertaDisciplinaFacade().persistir(getOfertaDisciplinaVOs(), getUsuarioLogado());
			getAplicacaoControle().ofertaDisciplina(null, null, Uteis.EXCLUIR);
			getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(null, null, false, true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluir(OfertaDisciplinaVO ofertaDisciplinaVO) {
		try {			
			getFacadeFactory().getOfertaDisciplinaFacade().excluir(ofertaDisciplinaVO, ofertaDisciplinaVO.getQtdeAlunoMatriculados() > 0, getUsuarioLogado());
			getOfertaDisciplinaVOs().removeIf(d -> d.getDisciplinaVO().getCodigo().equals(ofertaDisciplinaVO.getDisciplinaVO().getCodigo()));
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarOfertaDisciplina() {
		try {
			setListaSelectItemPeriodo(null);
			validarDados();
			setOfertaDisciplinaVOs(getFacadeFactory().getOfertaDisciplinaFacade().consultarPorAnoSemestre(getAno(), getSemestre(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarDisciplina(OfertaDisciplinaVO ofertaDisciplinaVO) {
		try {					
			ofertaDisciplinaVO.setAno(getAno());
			ofertaDisciplinaVO.setSemestre(getSemestre());
			getFacadeFactory().getOfertaDisciplinaFacade().adicionarDisciplina(ofertaDisciplinaVO, getOfertaDisciplinaVOs());
			getListaDisciplina().removeIf(d -> d.getDisciplinaVO().getCodigo().equals(ofertaDisciplinaVO.getDisciplinaVO().getCodigo()));
			setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarDisciplina() {
		try {
			validarDados();
			setListaSelectItemPeriodo(null);
			setListaDisciplina(getFacadeFactory().getOfertaDisciplinaFacade().consultarDisciplina(getAno(), getSemestre(), getCampoConsultaDisciplina(), getValorConsultaDisciplina()));			
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<OfertaDisciplinaVO> getOfertaDisciplinaVOs() {
		if(ofertaDisciplinaVOs == null) {
			ofertaDisciplinaVOs =  new ArrayList<OfertaDisciplinaVO>();
		}
		return ofertaDisciplinaVOs;
	}
	public void setOfertaDisciplinaVOs(List<OfertaDisciplinaVO> ofertaDisciplinaVOs) {
		this.ofertaDisciplinaVOs = ofertaDisciplinaVOs;
	}
	
	
	public String getAno() {
		if(ano == null) {
			ano = "";
		}
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		if(semestre == null) {
			semestre = "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if(listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico =  new ArrayList<SelectItem>(0);
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs;
			try {
				configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarTodasConfiguracaoAcademica(Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
				for(ConfiguracaoAcademicoVO c: configuracaoAcademicoVOs) {
					listaSelectItemConfiguracaoAcademico.add(new SelectItem(c.getCodigo(), c.getNome())); 
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}finally {
				configuracaoAcademicoVOs =  null;
			}
		}
		return listaSelectItemConfiguracaoAcademico;
	}
	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}
	public String getCampoConsultaDisciplina() {
		if(campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "abreviatura";
		}
		return campoConsultaDisciplina;
	}
	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}
	public String getValorConsultaDisciplina() {
		if(valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}
	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
	public List<OfertaDisciplinaVO> getListaDisciplina() {
		if(listaDisciplina == null) {
			listaDisciplina =  new ArrayList<OfertaDisciplinaVO>(0);
		}
		return listaDisciplina;
	}
	public void setListaDisciplina(List<OfertaDisciplinaVO> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}
	public List<SelectItem> getListaSelectItemOpcaoConsultaDisciplina() {
		if(listaSelectItemOpcaoConsultaDisciplina == null) {
			listaSelectItemOpcaoConsultaDisciplina =  new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaDisciplina.add(new SelectItem("nome", "Nome"));
			listaSelectItemOpcaoConsultaDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
			listaSelectItemOpcaoConsultaDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return listaSelectItemOpcaoConsultaDisciplina;
	}
	public void setListaSelectItemOpcaoConsultaDisciplina(List<SelectItem> listaSelectItemOpcaoConsultaDisciplina) {
		this.listaSelectItemOpcaoConsultaDisciplina = listaSelectItemOpcaoConsultaDisciplina;
	}

	public List<SelectItem> getListaSelectItemPeriodo() {
		if(listaSelectItemPeriodo == null) {
			listaSelectItemPeriodo = new ArrayList<SelectItem>(0);
			if(getSemestre().equals("1")) {
				listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.BIMESTRE_01, BimestreEnum.BIMESTRE_01.getValorApresentar()));
				listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.BIMESTRE_02, BimestreEnum.BIMESTRE_02.getValorApresentar()));
				listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.SEMESTRE_1, BimestreEnum.SEMESTRE_1.getValorApresentar()));			
			}else 	if(getSemestre().equals("2")) {
				listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.BIMESTRE_03, BimestreEnum.BIMESTRE_03.getValorApresentar()));
				listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.BIMESTRE_04, BimestreEnum.BIMESTRE_04.getValorApresentar()));
				listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.SEMESTRE_2, BimestreEnum.SEMESTRE_2.getValorApresentar()));
			}
			listaSelectItemPeriodo.add(new SelectItem(BimestreEnum.NAO_CONTROLA, "Não Definido"));			
		}
		return listaSelectItemPeriodo;
	}

	public void setListaSelectItemPeriodo(List<SelectItem> listaSelectItemPeriodo) {
		this.listaSelectItemPeriodo = listaSelectItemPeriodo;
	}
	
	public void consultarConfiguracaoAcademico(OfertaDisciplinaVO ofertaDisciplinaVO) {
		try {
			limparMensagem();
			ofertaDisciplinaVO.setConfiguracaoAcademicoVO(getAplicacaoControle().carregarDadosConfiguracaoAcademica(ofertaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo()));	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<OfertaDisciplinaVO> getListaImportarDisciplinaErro() {
		if(listaImportarDisciplinaErro == null) {
			listaImportarDisciplinaErro =  new ArrayList<OfertaDisciplinaVO>(0);
		}
		return listaImportarDisciplinaErro;
	}

	public void setListaImportarDisciplinaErro(List<OfertaDisciplinaVO> listaImportarDisciplinaErro) {
		this.listaImportarDisciplinaErro = listaImportarDisciplinaErro;
	}

	public void inicializarUpload() throws Exception {
		try {
			setOncompleteModal("");			
			validarDados();
			getListaImportarDisciplinaErro().clear();
			limparMensagem();
			setOncompleteModal("RichFaces.$('panelImportarDisciplina').show()");
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void downloadLayoutPadraoExcel() throws Exception {
		try {
			String xmlModeloUtilizar = "ModeloImportacaoOfertaDisciplina.xlsx";			
			File arquivo = new File(UteisJSF.getCaminhoWeb() + "arquivos" +  File.separator + xmlModeloUtilizar);
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void upload(FileUploadEvent uploadEvent) {
		setOncompleteModal("");
		getListaImportarDisciplinaErro().clear();
		limparMensagem();
		if (uploadEvent.getUploadedFile() != null) {			
			try {
				validarDados();
				getFacadeFactory().getOfertaDisciplinaFacade().upload(uploadEvent, getOfertaDisciplinaVOs(), getListaImportarDisciplinaErro(), getAno(), getSemestre());
				if(getListaImportarDisciplinaErro().isEmpty()) {
					setOncompleteModal("RichFaces.$('panelImportarDisciplina').hide()");
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				e.printStackTrace();
			}
			
		} else {			
			setMensagemDetalhada("msg_erro", "Selecione um arquivo para a prosseguir com a importação.");
		}
	}
	
	public void incluirHistoricoDisciplinaAluno(OfertaDisciplinaVO ofertaDisciplinaVO) {
		try {					
			getFacadeFactory().getOfertaDisciplinaFacade().realizarInclusaoDisciplinaAluno(ofertaDisciplinaVO, getUsuarioLogado());			
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarCriacaoTurma() {
		try {
			validarDados();
			getFacadeFactory().getOfertaDisciplinaFacade().realizarCriacaoTurmaOfertaDisciplina(getAno(), getSemestre(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
}
