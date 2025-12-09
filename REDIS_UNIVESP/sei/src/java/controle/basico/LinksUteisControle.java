package controle.basico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cidadeForm.jsp cidadeCons.jsp) com as funcionalidades da classe <code>Cidade</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cidade
 * @see CidadeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.UsuarioLinksUteisVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;

@Controller("LinksUteisControle")
@Scope("viewScope")
@Lazy
@Path("/aplicativoSEISV/linkUtil")
public class LinksUteisControle extends SuperControle implements Serializable {
	private static final long serialVersionUID = 1L;

	private LinksUteisVO linksUteisVO;
	private String campoConsulta;
	private String valorConsulta;
	private String valorConsultaUsuario;
	private String campoConsultaUsuario;
	private List<UsuarioVO> listaConsultaUsuarios;
	private List<String> icones;
	private FileUploadEvent fileUploadEvent;
	private Boolean importarPorCPF;
	private Boolean importarPorEmailInstitucional;
	private FileUploadEvent uploadEvent;
	private Boolean excluirTodosUsuarios;
	private XSSFWorkbook xssfWorkbook; 
	private HSSFWorkbook hssfWorkbook;

	public LinksUteisControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsultaOtimizado().setPage(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
		setMensagemID("msg_entre_prmconsulta");
	}

	public List<SelectItem> tipoConsultaLinks;

	public List<SelectItem> getTipoConsultaLinks() {
		if (tipoConsultaLinks == null) {
			tipoConsultaLinks = new ArrayList<SelectItem>(0);
			tipoConsultaLinks.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaLinks.add(new SelectItem("link", "Link"));
		}
		return tipoConsultaLinks;
	}

	public void consultarLinksUteis() {
		try {
			super.consultar();
			if (getCampoConsulta().equals("descricao")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getLinksUteisFacade().consultarPorDescricao(getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getCampoConsulta().equals("link")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getLinksUteisFacade().consultarPorLink(getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravar() {
		try {
			if (getLinksUteisVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getLinksUteisFacade().incluir(getLinksUteisVO(), true, getUsuarioLogado());
			} else {
				getFacadeFactory().getLinksUteisFacade().alterar(getLinksUteisVO(), true, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String novo() {
		setLinksUteisVO(new LinksUteisVO());
		removerObjetoMemoria(this);
		removerObjetoMemoria(getLinksUteisVO());
		setLinksUteisVO(new LinksUteisVO());
		getLinksUteisVO().setNovoObj(true);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("linksUteisForm.xhtml");
	}

	public void excluir() {
		try {
			getFacadeFactory().getLinksUteisFacade().excluir(linksUteisVO, true, getUsuarioLogado());
			removerObjetoMemoria(getLinksUteisVO());
			setLinksUteisVO(new LinksUteisVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("linksUteisCons.xhtml");
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "descricao";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public LinksUteisVO getLinksUteisVO() {
		if (linksUteisVO == null) {
			linksUteisVO = new LinksUteisVO();
		}
		return linksUteisVO;
	}

	public void setLinksUteisVO(LinksUteisVO linksUteisVO) {
		this.linksUteisVO = linksUteisVO;
	}

	public String editar() {
		try {
			LinksUteisVO obj = (LinksUteisVO) context().getExternalContext().getRequestMap().get("linksUteisItem");
			obj.setUsuarioLinksUteisVOs(getFacadeFactory().getUsuarioLinksUteisFacade().consultarPorUsuarioLinksUteis(obj.getCodigo(), getUsuarioLogado()));
			obj.setNovoObj(Boolean.FALSE);
			setLinksUteisVO(obj);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("linksUteisForm");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("linksUteisForm.xhtml");
		}

	}

	public String getCampoConsultaUsuario() {
		if (campoConsultaUsuario == null) {
			campoConsultaUsuario = "";
		}
		return campoConsultaUsuario;
	}

	public void setCampoConsultaUsuario(String campoConsultaUsuario) {
		this.campoConsultaUsuario = campoConsultaUsuario;
	}

	public String getValorConsultaUsuario() {
		if (valorConsultaUsuario == null) {
			valorConsultaUsuario = "";
		}
		return valorConsultaUsuario;
	}

	public void setValorConsultaUsuario(String valorConsultaUsuario) {
		this.valorConsultaUsuario = valorConsultaUsuario;
	}

	public List<SelectItem> getTipoConsultaUsuario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		itens.add(new SelectItem("emailInstitucional", "E-mail Institucional"));
		return itens;
	}

	public List<UsuarioVO> getListaConsultaUsuarios() {
		if (listaConsultaUsuarios == null) {
			listaConsultaUsuarios = new ArrayList<UsuarioVO>(0);
		}
		return listaConsultaUsuarios;
	}

	public void setListaConsultaUsuarios(List<UsuarioVO> listaConsultaUsuarios) {
		this.listaConsultaUsuarios = listaConsultaUsuarios;
	}

	public void consultarUsuario() {
	    try {
	        getControleConsultaOtimizado().setPage(1);
	        getControleConsultaOtimizado().setPaginaAtual(1);
	        realizarConsultaUsuario();
	    } catch (Exception e) {
	         getControleConsultaOtimizado().getListaConsulta().clear();
	         getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
	         setMensagemDetalhada("msg_erro", e.getMessage());
	    }

	}
	
    private void realizarConsultaUsuario() throws Exception {
		super.consultar();
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setLimitePorPagina(8);
		listaConsultaUsuarios = new ArrayList<UsuarioVO>(0);
		if (getControleConsulta().getCampoConsulta().equals("nome")) {
			if (getControleConsultaOtimizado().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			listaConsultaUsuarios = getFacadeFactory().getUsuarioFacade().consultarPorNomeTipoEspecificoUsuarioAlunoProfessorCoordenador(getControleConsultaOtimizado().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getControleConsultaOtimizado(), getUsuarioLogado());
		}
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			if (getControleConsultaOtimizado().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			listaConsultaUsuarios = getFacadeFactory().getUsuarioFacade().consultarPorCPF(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false,  getControleConsultaOtimizado(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		if (getControleConsulta().getCampoConsulta().equals("emailInstitucional")) {
			if (getControleConsultaOtimizado().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			listaConsultaUsuarios = getFacadeFactory().getUsuarioFacade().consultarPorEmailInstitucional(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),false, getControleConsultaOtimizado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		getControleConsultaOtimizado().setListaConsulta(listaConsultaUsuarios);
	}
		
	
	public void anularDataModelo(){
        setControleConsultaOtimizado(null);
    }
	
	public void adicionarUsuarioLinkUteisVO() {
		try {
			UsuarioVO usuarioVO = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioConsultaItens");
			getFacadeFactory().getLinksUteisFacade().adicionarUsuarioLinkUteisVO(linksUteisVO, usuarioVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public FileUploadEvent getFileUploadEvent() {
		return fileUploadEvent;
	}

	public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
		this.fileUploadEvent = fileUploadEvent;
	}

	public List<String> getIcones() {
		if (icones == null) {
			icones = new ArrayList<String>(0);
			icones.add("fas fa-file-word");
			icones.add("fas fa-file-pdf");
			icones.add("fas fa-file-excel");
			icones.add("fas fa-file-powerpoint");
			icones.add("far fa-file-alt");
			icones.add("fas fa-book");
			icones.add("far fa-calendar-alt");
			icones.add("fas fa-calendar-alt");
			icones.add("fas fa-envelope-open-text");
			icones.add("fas fa-chalkboard-teacher");
			icones.add("fas fa-laptop");
			icones.add("fas fa-mobile");
			icones.add("fas fa-wifi");
			icones.add("fas fa-photo-video");
			icones.add("fas fa-download");
			icones.add("fas fa-globe-americas");
			icones.add("fas fa-map");
			icones.add("fas fa-shopping-cart");
			icones.add("fas fa-anchor");
			icones.add("fas fa-user-graduate");
			icones.add("fas fa-headphones-alt");

		}
		return icones;
	}

	public void realizarProcessamentoArquivoExcel(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null) {
				getAplicacaoControle().getProgressBarLinksUteisVO().resetar();
				getAplicacaoControle().getProgressBarLinksUteisVO().setUsuarioVO(getUsuarioLogado());
				getAplicacaoControle().getProgressBarLinksUteisVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
				if (extensao.equals("xlsx")) {
					setXssfWorkbook(new XSSFWorkbook(uploadEvent.getUploadedFile().getInputStream()));
				} else {
					setHssfWorkbook(new HSSFWorkbook(uploadEvent.getUploadedFile().getInputStream()));
				}
				setUploadEvent(uploadEvent);
				getAplicacaoControle().getProgressBarLinksUteisVO().iniciar(0l, 100000, "Carregando o arquivo....", true, this, "realizarImportacaoUsuario");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarImportacaoUsuario() {
		try {			
			getFacadeFactory().getLinksUteisFacade().realizarProcessamentoExcelPlanilha(getUploadEvent(), getXssfWorkbook(), getHssfWorkbook(), getImportarPorCPF(), getImportarPorEmailInstitucional(), getLinksUteisVO(), getAplicacaoControle().getProgressBarLinksUteisVO(), getUsuarioLogado());			
			getAplicacaoControle().getProgressBarLinksUteisVO().getSuperControle().setMensagemID("msg_dados_importados");			
			getAplicacaoControle().getProgressBarLinksUteisVO().setForcarEncerramento(true);
		} catch (Exception e) {
			getAplicacaoControle().getProgressBarLinksUteisVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
			getAplicacaoControle().getProgressBarLinksUteisVO().setForcarEncerramento(true);
		}
	}

	public void setIcones(List<String> icones) {
		this.icones = icones;
	}

	public Boolean getImportarPorCPF() {
		if (importarPorCPF == null) {
			importarPorCPF = false;
		}
		return importarPorCPF;
	}

	public void setImportarPorCPF(Boolean importarPorCPF) {
		this.importarPorCPF = importarPorCPF;
	}

	public Boolean getImportarPorEmailInstitucional() {
		if (importarPorEmailInstitucional == null) {
			importarPorEmailInstitucional = Boolean.TRUE;
		}
		return importarPorEmailInstitucional;
	}

	public void setImportarPorEmailInstitucional(Boolean importarPorEmailInstitucional) {
		this.importarPorEmailInstitucional = importarPorEmailInstitucional;
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		try {
			realizarConsultaUsuario();
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
	        getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void importarCPFEmail() {
		if (getImportarPorCPF()) {
			setImportarPorEmailInstitucional(Boolean.FALSE);
		} else {
		if (getImportarPorEmailInstitucional()) 
			setImportarPorCPF(Boolean.FALSE);
		}
	}
	
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'formModal:valorConsulta','999.999.999-99',event)";
		}
		return "";
	}
	
	public FileUploadEvent getUploadEvent() {
		return uploadEvent;
	}
	
	public void setUploadEvent(FileUploadEvent uploadEvent) {
		this.uploadEvent = uploadEvent;
	}
	
	public void marcarDesmarcarTodos() {
		getLinksUteisVO().getUsuarioLinksUteisVOs().forEach(u -> u.setExcluir(getExcluirTodosUsuarios()));
	}
	
	@SuppressWarnings("rawtypes")
	public void removerUsuarioLinkUteis() {
		try {
			List<UsuarioLinksUteisVO> listaExcluir = getLinksUteisVO().getUsuarioLinksUteisVOs().stream().filter(u -> u.getExcluir()).collect(Collectors.toList());
			if (listaExcluir != null && Uteis.isAtributoPreenchido(listaExcluir)) {
				Iterator i = listaExcluir.iterator();
				while (i.hasNext()) {
					UsuarioLinksUteisVO obj = (UsuarioLinksUteisVO) i.next();
					if (Uteis.isAtributoPreenchido(obj)) {
						getFacadeFactory().getUsuarioLinksUteisFacade().excluir(obj, getUsuarioLogado());
					}
					getFacadeFactory().getLinksUteisFacade().removerUsuarioLinkUteisVO(linksUteisVO, obj);
				}
				setMensagemID("msg_dados_excluidos");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getExcluirTodosUsuarios() {
		if (excluirTodosUsuarios == null) {
			excluirTodosUsuarios = false;
		}
		return excluirTodosUsuarios;
	}
	
	public void setExcluirTodosUsuarios(Boolean excluirTodosUsuarios) {
		this.excluirTodosUsuarios = excluirTodosUsuarios;
	}
	
	public XSSFWorkbook getXssfWorkbook() {
		return xssfWorkbook;
	}
	
	public void setXssfWorkbook(XSSFWorkbook xssfWorkbook) {
		this.xssfWorkbook = xssfWorkbook;
	}
	
	public HSSFWorkbook getHssfWorkbook() {
		return hssfWorkbook;
	}
	
	public void setHssfWorkbook(HSSFWorkbook hssfWorkbook) {
		this.hssfWorkbook = hssfWorkbook;
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/consultarLinksUteis/{visaoUsuario}/{matricula}")
    public Response consultarLinksUteis(@PathParam("visaoUsuario") final String visaoUsuario, @PathParam("matricula") final String matricula) {
    	try {
    		if(!visaoUsuario.equals("aluno") && !visaoUsuario.equals("pais")) {
    			throw new Exception("O parâmetro visaoUsuario deve ser informado apenas as opções aluno ou pais de acordo com o usuário.");
    		}
    		UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar(visaoUsuario);
			List<LinksUteisVO> linksUteisVOs = (getFacadeFactory().getLinksUteisFacade().consultarLinksUteisUsuario(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();    		
    		String json =	gson.toJson(linksUteisVOs);
    		return Response.status(Status.OK).entity(json).build();
    	}catch (Exception e) {
    		ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
    }
	
}