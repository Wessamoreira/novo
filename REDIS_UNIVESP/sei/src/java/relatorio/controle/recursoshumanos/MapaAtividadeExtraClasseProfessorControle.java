package relatorio.controle.recursoshumanos;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorPostadoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("MapaAtividadeExtraClasseProfessorControle")
@Scope("viewScope")
@Lazy
public class MapaAtividadeExtraClasseProfessorControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -8297813585975013759L;

	private AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoVO;
	private AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoFiltroVO;

	private String situacao;

	private DataModelo dataModelofuncionarioCargo;

	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemRelatorios;

	private SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum;
	private SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnumInicial;
	private List<AtividadeExtraClasseProfessorPostadoVO> listaAtividadeExtraClasseProfessorPostadoVO;

	public MapaAtividadeExtraClasseProfessorControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().setLimitePorPagina(50);
		
		montarListaSelectItemCurso();
	}

	@Override
	public void consultarDados() throws Exception {
		try {
			getControleConsultaOtimizado().setListaConsulta( getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarAtividadeExtraClasseProfessorPostadoPorFuncionario(
				getControleConsultaOtimizado(), getAtividadeExtraClasseProfessorPostadoFiltroVO().getFuncionarioCargo().getCodigo(), false, getSituacao(), getAtividadeExtraClasseProfessorPostadoFiltroVO().getCursoVO().getCodigo(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(
					getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarTotalAtividadeExtraClasseProfessorPostadoPorFuncionario(
							getControleConsultaOtimizado(), getAtividadeExtraClasseProfessorPostadoFiltroVO().getFuncionarioCargo().getCodigo(), getSituacao(), getAtividadeExtraClasseProfessorPostadoFiltroVO().getCursoVO().getCodigo(), getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao) {
		AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemAtividadeExtraClasseProfessorPostado");
		setSituacaoHoraAtividadeExtraClasseEnum(situacao);
		try {
			setListaAtividadeExtraClasseProfessorPostadoVO(
					getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarAtivadeExtraClassePorSituacaoData(situacao, obj.getDataAtividade(), obj.getFuncionarioCargo().getCodigo(), obj.getCursoVO()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String selecionarArquivoDocumentoParaDownload() {
		try {
			AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemsAtividadeExtraCurricular");
			ArquivoVO arquivoVO = obj.getArquivo();
			arquivoVO.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +arquivoVO.getPastaBaseArquivo());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("arquivoVO", arquivoVO);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			obj.setRealizadoDownloadArquivo(Boolean.TRUE);
			getListaAtividadeExtraClasseProfessorPostadoVO().stream().forEach(p -> {
				if (p.getCodigo().equals(obj.getCodigo()) ) {
					p = obj;
				}
			});
		} catch (Exception e) {
			getAtividadeExtraClasseProfessorPostadoVO().setRealizadoDownloadArquivo(Boolean.FALSE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {

			if (getDataModelofuncionarioCargo().getCampoConsulta().equals("nome")) {
				getDataModelofuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioAtivo(getDataModelofuncionarioCargo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getDataModelofuncionarioCargo().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorNomeFuncionarioAtivo(getDataModelofuncionarioCargo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}

			if (getDataModelofuncionarioCargo().getCampoConsulta().equals("matricula")) {
				getDataModelofuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getDataModelofuncionarioCargo().getValorConsulta(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				if (Uteis.isAtributoPreenchido(getDataModelofuncionarioCargo().getListaConsulta())) {
					getDataModelofuncionarioCargo().setTotalRegistrosEncontrados(getDataModelofuncionarioCargo().getListaConsulta().size());
				}
			}

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getAtividadeExtraClasseProfessorPostadoVO().getFuncionarioCargo().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getAtividadeExtraClasseProfessorPostadoVO().getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getAtividadeExtraClasseProfessorPostadoVO().setFuncionarioCargo(funcionarioCargo);

				setMensagemID("msg_entre_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerFuncionarioCargo(DataScrollEvent dataScrollerEvent) {
		getDataModelofuncionarioCargo().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModelofuncionarioCargo().setPage(dataScrollerEvent.getPage());
		this.consultarFuncionario();
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarDados();
	}

	/**
	 * Seleciona o funcionario cargo pesquisado.
	 */
	public void selecionarFuncionarioCargo() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
			getAtividadeExtraClasseProfessorPostadoFiltroVO().setFuncionarioCargo(obj);
			
			setDataModelofuncionarioCargo(new DataModelo());
			dataModelofuncionarioCargo.setLimitePorPagina(10);
			//montarListaSelectItemCurso();
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosFuncionario () {
		try {
			getAtividadeExtraClasseProfessorPostadoFiltroVO().setFuncionarioCargo(new FuncionarioCargoVO());
			getAtividadeExtraClasseProfessorPostadoFiltroVO().setCursoVO(new CursoVO());
			setDataModelofuncionarioCargo(new DataModelo());
			dataModelofuncionarioCargo.setLimitePorPagina(10);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public Map<String, String> getSituacoes() {
		Map<String, String> situacoes = new LinkedHashMap<String, String>();
		for (SituacaoHoraAtividadeExtraClasseEnum situacao : SituacaoHoraAtividadeExtraClasseEnum.values()) {
			situacoes.put(situacao.getDescricao(), situacao.getValor());
		}
		return situacoes;
	}

	public boolean apresentarAprovado(AtividadeExtraClasseProfessorPostadoVO obj) {
		return obj.getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO) 
				|| obj.getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO)? true : false;
	}

	public boolean apresentarIndeferido(AtividadeExtraClasseProfessorPostadoVO obj) {
		return obj.getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.APROVADO) 
				|| obj.getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO) ? true : false;
	}

	public void montarDadosAprovar() {
		setAtividadeExtraClasseProfessorPostadoVO((AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemsAtividadeExtraCurricular"));
		setSituacaoHoraAtividadeExtraClasseEnumInicial((SituacaoHoraAtividadeExtraClasseEnum) SerializationUtils.clone(getAtividadeExtraClasseProfessorPostadoVO().getSituacaoHoraAtividadeExtraClasseEnum()));
		getAtividadeExtraClasseProfessorPostadoVO().setSituacaoHoraAtividadeExtraClasseEnum(SituacaoHoraAtividadeExtraClasseEnum.APROVADO);
		getAtividadeExtraClasseProfessorPostadoVO().setDataAprovacao(new Date());
		getAtividadeExtraClasseProfessorPostadoVO().setDataIndeferimento(null);
		getAtividadeExtraClasseProfessorPostadoVO().setMotivoIndeferimento(null);
		getAtividadeExtraClasseProfessorPostadoVO().setUsuarioResponsavel(getUsuarioLogado());
		setSituacaoHoraAtividadeExtraClasseEnum(SituacaoHoraAtividadeExtraClasseEnum.APROVADO);
	}

	public void montarDadosIndeferir() {
		setAtividadeExtraClasseProfessorPostadoVO((AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemsAtividadeExtraCurricular"));
		setSituacaoHoraAtividadeExtraClasseEnumInicial((SituacaoHoraAtividadeExtraClasseEnum) SerializationUtils.clone(getAtividadeExtraClasseProfessorPostadoVO().getSituacaoHoraAtividadeExtraClasseEnum()));
		getAtividadeExtraClasseProfessorPostadoVO().setSituacaoHoraAtividadeExtraClasseEnum(SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO);
		getAtividadeExtraClasseProfessorPostadoVO().setDataAprovacao(null);
		getAtividadeExtraClasseProfessorPostadoVO().setUsuarioResponsavel(getUsuarioLogado());
		setSituacaoHoraAtividadeExtraClasseEnum(SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO);
	}

	public void selecionarAtividadeExtraClasseProfessorPostado() {
		setAtividadeExtraClasseProfessorPostadoVO((AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemsAtividadeExtraCurricular"));
	}

	public void montarListaSelectItemCurso() {
		setListaSelectItemCurso(new ArrayList<>());
		List<CursoVO> cursoVOs = new ArrayList<>(0);
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoPorCoordenador(getUsuarioLogado().getPessoa().getCodigo());
		}  else {
			cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoAtividadeExtraClasseProfessor(0);
		}
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			getListaSelectItemCurso().add(new SelectItem("", ""));
			for (CursoVO cursoVO : cursoVOs) {
				getListaSelectItemCurso().add(new SelectItem(cursoVO.getCodigo(), cursoVO.getNome()));
			}
		}
	}

	public void aprovarIndeferirAtividadeExtraClasseProfessor() {
		String situacao = "";
		StringBuilder texto = new StringBuilder();
		try {
			situacao = getAtividadeExtraClasseProfessorPostadoVO().getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.APROVADO) ? "Aprovou" : "Indeferiu";
			texto.append("<br />").append("Usuário: ").append(getUsuarioLogado().getNome()).append(" no dia ")
					.append(UteisData.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy HH:mm:ss")).append(" ").append(situacao).append(".");
			getAtividadeExtraClasseProfessorPostadoVO().setLog(getAtividadeExtraClasseProfessorPostadoVO().getLog() + texto.toString());

			getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade()
					.aprovarAtividadeExtraClasseProfessor(getAtividadeExtraClasseProfessorPostadoVO(), getUsuarioLogado());
			
			consultarDados();

		} catch (Exception e) {
			for (AtividadeExtraClasseProfessorPostadoVO obj : listaAtividadeExtraClasseProfessorPostadoVO) {
				if (getAtividadeExtraClasseProfessorPostadoVO().getCodigo() == obj.getCodigo()) {
					obj.setRealizadoDownloadArquivo(false);
					obj.setSituacaoHoraAtividadeExtraClasseEnum(getSituacaoHoraAtividadeExtraClasseEnumInicial());
				}
			}
			getAtividadeExtraClasseProfessorPostadoVO().setLog(getAtividadeExtraClasseProfessorPostadoVO().getLog().replace(texto.toString(), ""));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void chamarRelatorioSEIDecidir() {
		context().getExternalContext().getSessionMap().put("modulo", "RECURSOS_HUMANOS");
	}

	// GETTER AND SETTER
	public DataModelo getDataModelofuncionarioCargo() {
		if (dataModelofuncionarioCargo == null) {
			dataModelofuncionarioCargo = new DataModelo();
			dataModelofuncionarioCargo.setLimitePorPagina(10);
		}
		return dataModelofuncionarioCargo;
	}

	public void setDataModelofuncionarioCargo(DataModelo dataModelofuncionarioCargo) {
		this.dataModelofuncionarioCargo = dataModelofuncionarioCargo;
	}

	public List<SelectItem> getListaSelectItemRelatorios() {
		if (listaSelectItemRelatorios == null) {
			listaSelectItemRelatorios = new ArrayList<>();
		}
		return listaSelectItemRelatorios;
	}

	public void setListaSelectItemRelatorios(List<SelectItem> listaSelectItemRelatorios) {
		this.listaSelectItemRelatorios = listaSelectItemRelatorios;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List<AtividadeExtraClasseProfessorPostadoVO> getListaAtividadeExtraClasseProfessorPostadoVO() {
		if (listaAtividadeExtraClasseProfessorPostadoVO == null) {
			listaAtividadeExtraClasseProfessorPostadoVO = new ArrayList<>();
		}
		return listaAtividadeExtraClasseProfessorPostadoVO;
	}

	public void setListaAtividadeExtraClasseProfessorPostadoVO(
			List<AtividadeExtraClasseProfessorPostadoVO> listaAtividadeExtraClasseProfessorPostadoVO) {
		this.listaAtividadeExtraClasseProfessorPostadoVO = listaAtividadeExtraClasseProfessorPostadoVO;
	}

	public SituacaoHoraAtividadeExtraClasseEnum getSituacaoHoraAtividadeExtraClasseEnum() {
		if (situacaoHoraAtividadeExtraClasseEnum == null) {
			situacaoHoraAtividadeExtraClasseEnum = SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO;
		}
		return situacaoHoraAtividadeExtraClasseEnum;
	}

	public void setSituacaoHoraAtividadeExtraClasseEnum(
			SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum) {
		this.situacaoHoraAtividadeExtraClasseEnum = situacaoHoraAtividadeExtraClasseEnum;
	}

	public AtividadeExtraClasseProfessorPostadoVO getAtividadeExtraClasseProfessorPostadoVO() {
		if (atividadeExtraClasseProfessorPostadoVO == null) {
			atividadeExtraClasseProfessorPostadoVO = new AtividadeExtraClasseProfessorPostadoVO();
		}
		return atividadeExtraClasseProfessorPostadoVO;
	}

	public void setAtividadeExtraClasseProfessorPostadoVO(
			AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoVO) {
		this.atividadeExtraClasseProfessorPostadoVO = atividadeExtraClasseProfessorPostadoVO;
	}

	public SituacaoHoraAtividadeExtraClasseEnum getSituacaoHoraAtividadeExtraClasseEnumInicial() {
		if (situacaoHoraAtividadeExtraClasseEnumInicial == null) {
			situacaoHoraAtividadeExtraClasseEnumInicial = SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO;
		}
		return situacaoHoraAtividadeExtraClasseEnumInicial;
	}

	public void setSituacaoHoraAtividadeExtraClasseEnumInicial(
			SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnumInicial) {
		this.situacaoHoraAtividadeExtraClasseEnumInicial = situacaoHoraAtividadeExtraClasseEnumInicial;
	}
	
	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<>();
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public AtividadeExtraClasseProfessorPostadoVO getAtividadeExtraClasseProfessorPostadoFiltroVO() {
		if (atividadeExtraClasseProfessorPostadoFiltroVO == null) {
			atividadeExtraClasseProfessorPostadoFiltroVO = new AtividadeExtraClasseProfessorPostadoVO();
		}
		return atividadeExtraClasseProfessorPostadoFiltroVO;
	}

	public void setAtividadeExtraClasseProfessorPostadoFiltroVO(
			AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoFiltroVO) {
		this.atividadeExtraClasseProfessorPostadoFiltroVO = atividadeExtraClasseProfessorPostadoFiltroVO;
	}
}