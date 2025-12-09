package controle.recursoshumanos;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorPostadoVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO.EnumCampoConsultaHistoricoDependentes;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas historicoFuncionarioForm.xhtml e historicoFuncionarioCons.xhtl com as
 * funcionalidades da classe <code>historicoFuncionario</code>. Implemtacao da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 */
@Controller("AtividadeExtraClasseProfessorPostadoControle")
@Scope("viewScope")
public class AtividadeExtraClasseProfessorPostadoControle extends SuperControle {

	private static final long serialVersionUID = -9145134888233724110L;

	private static final String CONTEXT_PARA_EDICAO = "itemsAtividadeExtraClasse";

	private AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoSelecionadoVO;
	
	private AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoVO;
	private List<AtividadeExtraClasseProfessorPostadoVO> listaAtividadeExtraClasseProfessorPostadoVO;

	private FuncionarioCargoVO funcionarioCargo;
	private List<SelectItem> listaSelectItemfuncionarioCargo;

	private List<SelectItem> listaSelectItemCurso;

	private String situacao;
	private Boolean apresentarTelaPesquisa;

	private SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum;

	public AtividadeExtraClasseProfessorPostadoControle() throws Exception {
		setApresentarTelaPesquisa(Boolean.TRUE);
		setControleConsultaOtimizado(new DataModelo());
		novo();
		inicializarConsultar();
		montarListaSelectItemFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorPessoaESituacaoFuncionario(
				getUsuarioLogado().getPessoa().getCodigo(), SituacaoFuncionarioEnum.ATIVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	}

	private void montarListaSelectItemFuncionarioCargo(List<FuncionarioCargoVO> listaFuncionarioCargo) {
		if (Uteis.isAtributoPreenchido(listaFuncionarioCargo)) {
			int contador = 1;
			for (FuncionarioCargoVO funcionarioCargoVO : listaFuncionarioCargo) {
				if (contador == 1) {
					setFuncionarioCargo(funcionarioCargoVO);
					consultarDados();
					montarListaSelectItemCurso();
					contador++;
				}
				getListaSelectItemfuncionarioCargo().add(new SelectItem(funcionarioCargoVO.getCodigo(), 
						funcionarioCargoVO.getFuncionarioVO().getPessoa().getNome() + " - " + funcionarioCargoVO.getMatriculaCargo() + " - " + 
						funcionarioCargoVO.getCargo().getDescricao()));
			}
		} else {
			setListaSelectItemfuncionarioCargo(new ArrayList<>());
		}
	}
	
	public void montarListaSelectItemCurso() {
		List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoAtividadeExtraClasseProfessor(getFuncionarioCargo().getCodigo());
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			getListaSelectItemCurso().add(new SelectItem("", ""));
			for (CursoVO cursoVO : cursoVOs) {
				getListaSelectItemCurso().add(new SelectItem(cursoVO.getCodigo(), cursoVO.getNome()));
			}
		} else {
			setListaSelectItemCurso(new ArrayList<>());
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getControleConsultaOtimizado().getListaFiltros().add(getFuncionarioCargo().getCodigo());
			
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarAtividadeExtraClasseProfessorPostadoPorFuncionario(getControleConsultaOtimizado(), getFuncionarioCargo().getCodigo(), false, getSituacao(), getAtividadeExtraClasseProfessorPostadoVO().getCursoVO().getCodigo(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarTotalAtividadeExtraClasseProfessorPostadoPorFuncionario(getControleConsultaOtimizado(), getFuncionarioCargo().getCodigo(), getSituacao(), getAtividadeExtraClasseProfessorPostadoVO().getCursoVO().getCodigo(), getUsuarioLogado()));

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade) {
		AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemHoraAtividadeExtraClasseProfessorPostado");
		setSituacaoHoraAtividadeExtraClasseEnum(situacao);
		try {
			setListaAtividadeExtraClasseProfessorPostadoVO(
					getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarAtivadeExtraClassePorSituacaoData(situacao, dataAtividade, getFuncionarioCargo().getCodigo(), obj.getCursoVO()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editar() {
		AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setAtividadeExtraClasseProfessorPostadoVO(obj);

		setControleConsultaOtimizado(new DataModelo());
		alterarApresentacaoTela();
		setMensagemID(MSG_TELA.msg_dados_editar.name());
	}

	@Override
	public boolean getApresentarResultadoConsulta() {
		return !getControleConsultaOtimizado().getListaConsulta().isEmpty() &&
				getControleConsultaOtimizado().getListaConsulta().size() > 0 ;		
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	public void novo() throws Exception {
		setAtividadeExtraClasseProfessorPostadoVO(new AtividadeExtraClasseProfessorPostadoVO());
		getAtividadeExtraClasseProfessorPostadoVO().setSituacaoHoraAtividadeExtraClasseEnum(SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO);
		getAtividadeExtraClasseProfessorPostadoVO().setFuncionarioCargo(getFuncionarioCargo());
		/*getAtividadeExtraClasseProfessorPostadoVO().setAtividadeExtraClasseProfessorVO(
				getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().consultarPorMesAnoDataAtividade(getFuncionarioCargo().getCodigo(), getAtividadeExtraClasseProfessorPostadoVO().getDataAtividade()));*/
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}

	public void persistir() {
		try {
			getAtividadeExtraClasseProfessorPostadoVO().setAtividadeExtraClasseProfessorVO(
					getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().consultarPorMesAnoDataAtividade(getFuncionarioCargo().getCodigo(), getAtividadeExtraClasseProfessorPostadoVO().getDataAtividade()));
			getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().persistir(getAtividadeExtraClasseProfessorPostadoVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().excluir(getAtividadeExtraClasseProfessorPostadoVO(), Boolean.TRUE, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public void inicializarConsultar() { 
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaHistoricoDependentes.FUNCIONARIO.name());
		getControleConsultaOtimizado().setDataIni(UteisData.getPrimeiroDataMes(new Date()));
		getControleConsultaOtimizado().setDataFim(UteisData.getUltimaDataMes(new Date()));
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	/**
	 * Evento de consulta paginada da tela pesquisa do Funcionario Cargo da tela de
	 * AtividadeExtraClasseProfessorPostadoCons.xhtml
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		this.consultarDados();
	}

	public void consultarFuncionarioPelaMatriculaCargo() {
		try {
			setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void consultarAtividadeExtraClasse() {
		try {
			novo();
			this.consultarDados();			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Altera a apresentação da tela de pesquisa para cadastro ou vice e versa.
	 */
	public void alterarApresentacaoTela() {
		getControleConsultaOtimizado().setDataIni(UteisData.getPrimeiroDataMes(new Date()));
		getControleConsultaOtimizado().setDataFim(UteisData.getUltimaDataMes(new Date()));
		apresentarTelaPesquisa = !getApresentarTelaPesquisa();
	}

	/**
	 * Upload de {@link ArquivoVO}.
	 * 
	 * @param upload
	 */
	public void uploadArquivo(FileUploadEvent upload) {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().uploadDocumento(upload, getAtividadeExtraClasseProfessorPostadoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String selecionarArquivoDocumentoParaDownload() {
		try {
			AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemsAtividadeExtraClasse");
			ArquivoVO arquivoVO = obj.getArquivo();
			arquivoVO.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +arquivoVO.getPastaBaseArquivo());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("arquivoVO", arquivoVO);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public void selecionarAtividadeExtraClasseProfessorPostado() {
		AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemsAtividadeExtraClasse");
		setAtividadeExtraClasseProfessorPostadoSelecionadoVO(obj);
	}
	
	public boolean apresentarAtividaExtraClasse() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 10;
	}

	// GETTER AND SETTER
	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
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

	public Boolean getApresentarTelaPesquisa() {
		if (apresentarTelaPesquisa == null) {
			apresentarTelaPesquisa = Boolean.TRUE;
		}
		return apresentarTelaPesquisa;
	}

	public void setApresentarTelaPesquisa(Boolean apresentarTelaPesquisa) {
		this.apresentarTelaPesquisa = apresentarTelaPesquisa;
	}

	public List<SelectItem> getListaSelectItemfuncionarioCargo() {
		if (listaSelectItemfuncionarioCargo == null) {
			listaSelectItemfuncionarioCargo = new ArrayList<>();
		}
		return listaSelectItemfuncionarioCargo;
	}

	public void setListaSelectItemfuncionarioCargo(List<SelectItem> listaSelectItemfuncionarioCargo) {
		this.listaSelectItemfuncionarioCargo = listaSelectItemfuncionarioCargo;
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

	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<>();
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public AtividadeExtraClasseProfessorPostadoVO getAtividadeExtraClasseProfessorPostadoSelecionadoVO() {
		if (atividadeExtraClasseProfessorPostadoSelecionadoVO == null) {
			atividadeExtraClasseProfessorPostadoSelecionadoVO = new AtividadeExtraClasseProfessorPostadoVO();
		}
		return atividadeExtraClasseProfessorPostadoSelecionadoVO;
	}

	public void setAtividadeExtraClasseProfessorPostadoSelecionadoVO(
			AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoSelecionadoVO) {
		this.atividadeExtraClasseProfessorPostadoSelecionadoVO = atividadeExtraClasseProfessorPostadoSelecionadoVO;
	}
}