package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO.EnumCampoConsultaHistoricoDependentes;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas historicoFuncionarioForm.xhtml e historicoFuncionarioCons.xhtl com as
 * funcionalidades da classe <code>historicoFuncionario</code>. Implemtacao da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 */
@Controller("AfastamentoFuncionarioControle")
@Scope("viewScope")
public class AfastamentoFuncionarioControle extends SuperControle {

	private static final long serialVersionUID = 1934177815264056741L;

	private static final String TELA_CONS = "afastamentoFuncionarioCons";
	private static final String TELA_FORM = "afastamentoFuncionarioForm";
	private static final String CONTEXT_PARA_EDICAO = "itemAfastamentoFuncionario";

	private AfastamentoFuncionarioVO afastamentoFuncionarioVO;
	private List<AfastamentoFuncionarioVO> listaAfastamentosFuncionario;
	
	private FuncionarioCargoVO funcionarioCargo;
	private String situacaoFuncionario;
	
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

	private Boolean habilitarGravarImagem;
	private String onCompletePanelUpload;
	
	public AfastamentoFuncionarioControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
		try {
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getHistoricoFuncionarioInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), situacaoFuncionario);

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarAfastamentoPorFuncionarioCargo() {
		try {
			setListaAfastamentosFuncionario(getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().consultarAfastamentoPorCodigoFuncionarioCargo(getFuncionarioCargo().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String editar() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setFuncionarioCargo(obj);
		consultarAfastamentoPorFuncionarioCargo();
		getAfastamentoFuncionarioVO().setFuncionarioCargo(obj);

		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_dados_editar.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	@Override
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0 ;		
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}


	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public void persistir() {
		try {
			getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().persistirTodos(getListaAfastamentosFuncionario(), getFuncionarioCargo(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
			setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorCodigo(getFuncionarioCargo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().excluir(getAfastamentoFuncionarioVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluirPorFuncionarioCargo() {
		try {
			getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().excluirPorFuncionarioCargo(getListaAfastamentosFuncionario(), getFuncionarioCargo(), true, getUsuarioLogado());
			setListaAfastamentosFuncionario(new ArrayList<>());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaHistoricoDependentes.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public void removerAfastamento(AfastamentoFuncionarioVO obj) {
		getListaAfastamentosFuncionario().removeIf(p -> p.getCodigo().equals(obj.getCodigo()));
	}

	/**
	 * Evento de consulta paginada da tela pesquisa do Funcionario Cargo da tela de
	 * historicoFuncionarioCons
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
			consultarAfastamentoPorFuncionarioCargo();
			
			getAfastamentoFuncionarioVO().setFuncionarioCargo(getFuncionarioCargo());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void adicionarAfastamento() {
		try {
			getAfastamentoFuncionarioVO().setFuncionarioCargo(getFuncionarioCargo());
			getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().validarDados(getAfastamentoFuncionarioVO());
			getListaAfastamentosFuncionario().add(getAfastamentoFuncionarioVO());
			setAfastamentoFuncionarioVO(new AfastamentoFuncionarioVO());
			
			getAfastamentoFuncionarioVO().setFuncionarioCargo(getFuncionarioCargo());
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		} catch (ConsistirException e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void cancelarAfastamento() {
		setAfastamentoFuncionarioVO(new AfastamentoFuncionarioVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}

	public void carregarDadosPanelUpload() {
		try {
			setHabilitarGravarImagem(true);
			getAfastamentoFuncionarioVO().setArquivo(new ArquivoVO());
			setMensagemID(MSG_TELA.msg_entre_dados.name());
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').show()");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
			setOnCompletePanelUpload("");
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
				setMensagemDetalhada("Prezado Usuário, seu arquivo excede o tamanho máximo para upload de 15Mb, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
				setHabilitarGravarImagem(false);
			} else {
				getFacadeFactory().getArquivoHelper().uploadArquivosComuns(uploadEvent, getAfastamentoFuncionarioVO().getArquivo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
				setMensagemID(MSG_TELA.msg_entre_dados.name());
				setHabilitarGravarImagem(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void adicionarArquivoLista() {
		try {
			getAfastamentoFuncionarioVO().getArquivo().setValidarDisciplina(Boolean.FALSE);
			getAfastamentoFuncionarioVO().getArquivo().setResponsavelUpload(getUsuarioLogado());
			getAfastamentoFuncionarioVO().getArquivo().setDataUpload(new Date());
			getAfastamentoFuncionarioVO().getArquivo().setDataDisponibilizacao(new Date());
			getAfastamentoFuncionarioVO().getArquivo().setManterDisponibilizacao(false);
			getAfastamentoFuncionarioVO().getArquivo().setOrigem(OrigemArquivo.AFASTAMENTO_FUNCIONARIO.getValor());
			getAfastamentoFuncionarioVO().getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getAfastamentoFuncionarioVO().getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.ARQUIVO_TMP.getValue());
			getAfastamentoFuncionarioVO().getArquivo().setDescricaoArquivo(OrigemArquivo.AFASTAMENTO_FUNCIONARIO.getDescricao());
			
			if (!Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
			}

			getFacadeFactory().getArquivoFacade().persistir(getAfastamentoFuncionarioVO().getArquivo(), false, getUsuarioLogado(), configuracaoGeralSistemaVO);
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		} finally {
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').hide()");
		}
	}

	public void selecionarArquivoParaDownload() {
		try {
			AfastamentoFuncionarioVO obj = (AfastamentoFuncionarioVO) context().getExternalContext().getRequestMap().get("afastamento"); 
			context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivo());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean validarUsuarioAtivo() {
		return getFuncionarioCargo().getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.ATIVO.name());
	}

	public void calcularDataFinal() {
		if (Uteis.isAtributoPreenchido(getAfastamentoFuncionarioVO().getDataInicio()) &&
				Uteis.isAtributoPreenchido(getAfastamentoFuncionarioVO().getQuantidadeDiasAfastado())) {

			Date dataFinal = UteisData.getAdicionarDataAtualSomandoOuSubtraindo(getAfastamentoFuncionarioVO().getDataInicio(), getAfastamentoFuncionarioVO().getQuantidadeDiasAfastado());
	        getAfastamentoFuncionarioVO().setDataFinal(dataFinal);
		}
	}
	
	public void dataFinalSelecionada() {
		if (Uteis.isAtributoPreenchido(getAfastamentoFuncionarioVO().getDataInicio())) {
			Long diferencaDias = UteisData.getCalculaDiferencaEmDias(getAfastamentoFuncionarioVO().getDataInicio(), getAfastamentoFuncionarioVO().getDataFinal()) + 1;
			getAfastamentoFuncionarioVO().setQuantidadeDiasAfastado(diferencaDias.intValue());
			
		}
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

	public String getSituacaoFuncionario() {
		if (situacaoFuncionario == null) {
			situacaoFuncionario = "TODOS";
		}
		return situacaoFuncionario;
	}

	public void setSituacaoFuncionario(String situacaoFuncionario) {
		this.situacaoFuncionario = situacaoFuncionario;
	}

	public AfastamentoFuncionarioVO getAfastamentoFuncionarioVO() {
		if (afastamentoFuncionarioVO == null) {
			afastamentoFuncionarioVO = new AfastamentoFuncionarioVO();
		}
		return afastamentoFuncionarioVO;
	}

	public void setAfastamentoFuncionarioVO(AfastamentoFuncionarioVO afastamentoFuncionarioVO) {
		this.afastamentoFuncionarioVO = afastamentoFuncionarioVO;
	}

	public List<AfastamentoFuncionarioVO> getListaAfastamentosFuncionario() {
		if (listaAfastamentosFuncionario == null) {
			listaAfastamentosFuncionario = new ArrayList<>();
		}
		return listaAfastamentosFuncionario;
	}

	public void setListaAfastamentosFuncionario(List<AfastamentoFuncionarioVO> listaAfastamentosFuncionario) {
		this.listaAfastamentosFuncionario = listaAfastamentosFuncionario;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public Boolean getHabilitarGravarImagem() {
		if (habilitarGravarImagem == null) {
			habilitarGravarImagem = Boolean.TRUE;
		}
		return habilitarGravarImagem;
	}

	public void setHabilitarGravarImagem(Boolean habilitarGravarImagem) {
		this.habilitarGravarImagem = habilitarGravarImagem;
	}

	public String getOnCompletePanelUpload() {
		if (onCompletePanelUpload == null) {
			onCompletePanelUpload = "";
		}
		return onCompletePanelUpload;
	}

	public void setOnCompletePanelUpload(String onCompletePanelUpload) {
		this.onCompletePanelUpload = onCompletePanelUpload;
	}
}
