package controle.biblioteca;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.TimeLineVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.TipoFiltroConsulta;
import negocio.comuns.utilitarias.TipoFiltroConsultaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.biblioteca.Exemplar;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("MinhaBibliotecaControle")
@Scope("session")
@Lazy
public class MinhaBibliotecaControle extends EmprestimoControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8104712775227324739L;
	private CatalogoVO catalogoVO;
	private ExemplarVO exemplarVO;
	private BibliotecaVO bibliotecaVO;
	private List<TimeLineVO> listaTimeLineVOs;
	private List<TimeLineVO> listaReservaTimeLineVOs;
	private List<TimeLineVO> dadosTimeLineUsuario;
	private String valorPesquisaCatalogoVO;
	private String valorFiltrarCatalogoVOs;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaVO;
	private String dataLogado;
	private List<CatalogoVO> ListaCatalogoVOs;
	private List<SelectItem> listaBibliotecas;
	private Boolean usuarioLogadoBiblioteca;
	private String caminhoFotoUsuario;
	private Integer quantidadeEmprestimoAberto;
	private Integer quantidadeEmprestimoAtrasado;
	private Integer quantidadeReservaPendente;
	private Double valorTotalMulta;
	private PessoaVO pessoaVO;
	private List<TipoFiltroConsulta> areaConhecimentoVOs;
	private List<TipoFiltroConsulta> tipoCatalogoVOs;
	private List<TipoFiltroConsulta> listaMenuCursoVOs;
	private List<TipoFiltroConsulta> tipoFiltroConsultas;
	private List<TipoFiltroConsulta> listaBibliotecaExterna;
	private List<TipoFiltroConsulta> disciplinaPeriodo;
	private List<ExemplarVO> listaAdicionadosGuia;
	private List<ExemplarVO> listaExemplarCatalogoSelecionado;
	private List<CatalogoVO> listaCatalogosAdicionadosNaGuiaReserva;
	private Boolean apresentarModalAluno;
	private Boolean abrirModalTipoUsuario;
	private List<ReservaVO> reservaVOs;
	private String mensagemCatalogosReserva;
	private Boolean mostrarModalReserva;
	private String ajudaCriacaoSenha;
	private Boolean abrirModalRegistrar;
	private Boolean visualizarSenha;
	private String senhaUsuarioMembroComunidade;
	private String usernameUsuarioMembroComunidade;
	private Integer testetamanhoTela;
	private PessoaVO pessoaExistente;
	private UsuarioVO usuarioExistente;
	private String mensagemAlertaCriacaoUsuario;
	private String mensagemAlertaCriacaoUsername;
	private String mensagemAlertaCriacaoSenha;
	private String mensagemAlertaEmail;
	private Boolean mostrarAlertaCpf;
	private Boolean mostrarAlertaUsuario;
	private Boolean mostrarAlertaSenha;
	private Boolean mostrarAlertaEmail;
	private List<ArquivoVO> listaArquivosBibliotecaExterna;
	private Boolean logadoVisaoAdministrativa;
	private String tipoPessoaReserva;
	private String valorConsultaPessoaReserva;
	private PessoaVO pessoaReservarCatalogo;
	private List listaConsultaPessoaReservaCatalogo;
	private String matriculaReserva;
	private Boolean mostrarMensagemConfirmacaoReserva;
	private String tituloBuscaAvancada;
	private String assuntoBuscaAvancada;
	private String palavrasChaveBuscaAvancada;
	private String autoresBuscaAvancada;
	private String classificacaoBuscaAvancada;
	private String isbnBuscaAvancada;
	private String issnBuscaAvancada;
	private String cutterPhaBuscaAvancada;
	private String tomboBuscaAvancada;
	private Boolean unificarConsultaBuscaAvancada;
	private String areaConhecimentoBuscaAvancada;
	private String subtituloBuscaAvancada;
	private List<SelectItem> listaAreaConhecimentoBuscaAvancada;
	private String codigoCatalogoServelet;
	private String codigoBibliotecaServelet;
	private String oncomplete;

	public int getTestetamanhoTela() {
		if (testetamanhoTela == null) {
			testetamanhoTela = 0;
		}
		return testetamanhoTela;
	}

	public void setTestetamanhoTela(int testetamanhoTela) {
		this.testetamanhoTela = testetamanhoTela;
	}

	public MinhaBibliotecaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public CatalogoVO getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}

	public void verificarChamadaBibliotecaExterna() {
		try {
			if (Uteis.isAtributoPreenchido(getCodigoCatalogoServelet())) {
				setBibliotecaVO(new BibliotecaVO());
				getControleConsultaOtimizado().setPaginaAtual(getControleConsultaOtimizado().getPaginaAtual() == 0 ? 1 : getControleConsultaOtimizado().getPaginaAtual());
				consultarCatalogo();
				BibliotecaVO bibliotecaVO = getFacadeFactory().getBibliotecaFacade().consultarPorCodigoCatalogo(
						new Integer(getCodigoCatalogoServelet()), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(bibliotecaVO)) {
					setBibliotecaVO(bibliotecaVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}

	public ExemplarVO getExemplarVO() {
		if (exemplarVO == null) {
			exemplarVO = new ExemplarVO();
		}
		return exemplarVO;
	}

	public void setExemplarVO(ExemplarVO exemplarVO) {
		this.exemplarVO = exemplarVO;
	}

	public BibliotecaVO getBibliotecaVO() {
		if (bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}

	public String getValorPesquisaCatalogoVO() {
		if (valorPesquisaCatalogoVO == null) {
			valorPesquisaCatalogoVO = "";
		}
		return valorPesquisaCatalogoVO;
	}

	public void setValorPesquisaCatalogoVO(String valorPesquisaCatalogoVO) {
		this.valorPesquisaCatalogoVO = valorPesquisaCatalogoVO;
	}

	public String getValorFiltrarCatalogoVOs() {
		if (valorFiltrarCatalogoVOs == null) {
			valorFiltrarCatalogoVOs = "";
		}
		return valorFiltrarCatalogoVOs;
	}

	public void setValorFiltrarCatalogoVOs(String valorFiltrarCatalogoVOs) {
		this.valorFiltrarCatalogoVOs = valorFiltrarCatalogoVOs;
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

	public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaVO() {
		if (configuracaoBibliotecaVO == null) {
			configuracaoBibliotecaVO = new ConfiguracaoBibliotecaVO();
		}
		return configuracaoBibliotecaVO;
	}

	public void setConfiguracaoBibliotecaVO(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) {
		this.configuracaoBibliotecaVO = configuracaoBibliotecaVO;
	}

	public List<TimeLineVO> getListaTimeLineVOs() {
		if (listaTimeLineVOs == null) {
			listaTimeLineVOs = new ArrayList<TimeLineVO>(0);
		}
		return listaTimeLineVOs;
	}

	public void setListaTimeLineVOs(List<TimeLineVO> listaTimeLineVOs) {
		this.listaTimeLineVOs = listaTimeLineVOs;
	}

	public List<TimeLineVO> getListaReservaTimeLineVOs() {
		if (listaReservaTimeLineVOs == null) {
			listaReservaTimeLineVOs = new ArrayList<TimeLineVO>(0);
		}
		return listaReservaTimeLineVOs;
	}

	public void setListaReservaTimeLineVOs(List<TimeLineVO> listaReservaTimeLineVOs) {
		this.listaReservaTimeLineVOs = listaReservaTimeLineVOs;
	}

	public List<TimeLineVO> getDadosTimeLineUsuario() {
		if (dadosTimeLineUsuario == null) {
			dadosTimeLineUsuario = new ArrayList<TimeLineVO>(0);
		}
		return dadosTimeLineUsuario;
	}

	public void setDadosTimeLineUsuario(List<TimeLineVO> dadosTimeLineUsuario) {
		this.dadosTimeLineUsuario = dadosTimeLineUsuario;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public List<CatalogoVO> getListaCatalogoVOs() {
		if (ListaCatalogoVOs == null) {
			ListaCatalogoVOs = new ArrayList<CatalogoVO>(0);
		}
		return ListaCatalogoVOs;
	}

	public void setListaCatalogoVOs(List<CatalogoVO> listaCatalogoVOs) {
		ListaCatalogoVOs = listaCatalogoVOs;
	}

	public List<ExemplarVO> getListaAdicionadosGuia() {
		if (listaAdicionadosGuia == null) {
			listaAdicionadosGuia = new ArrayList<ExemplarVO>(0);
		}
		return listaAdicionadosGuia;
	}

	public void setListaAdicionadosGuia(List<ExemplarVO> listaAdicionadosGuia) {
		this.listaAdicionadosGuia = listaAdicionadosGuia;
	}

	public List<CatalogoVO> getListaCatalogosAdicionadosNaGuiaReserva() {
		if (listaCatalogosAdicionadosNaGuiaReserva == null) {
			listaCatalogosAdicionadosNaGuiaReserva = new ArrayList<CatalogoVO>(0);
		}
		return listaCatalogosAdicionadosNaGuiaReserva;
	}

	public void setListaCatalogosAdicionadosNaGuiaReserva(List<CatalogoVO> listaCatalogosAdicionadosNaGuiaReserva) {
		this.listaCatalogosAdicionadosNaGuiaReserva = listaCatalogosAdicionadosNaGuiaReserva;
	}

	public Boolean getApresentarModalAluno() {
		if (apresentarModalAluno == null) {
			apresentarModalAluno = false;
		}
		return apresentarModalAluno;
	}

	public void setApresentarModalAluno(Boolean apresentarModalAluno) {
		this.apresentarModalAluno = apresentarModalAluno;
	}

	public Boolean getAbrirModalTipoUsuario() {
		if (abrirModalTipoUsuario == null) {
			abrirModalTipoUsuario = false;
		}
		return abrirModalTipoUsuario;
	}

	public void setAbrirModalTipoUsuario(Boolean abrirModalTipoUsuario) {
		this.abrirModalTipoUsuario = abrirModalTipoUsuario;
	}

	public void setMensagemCatalogosReserva(String mensagemCatalogosReserva) {
		this.mensagemCatalogosReserva = mensagemCatalogosReserva;
	}

	public String getMensagemCatalogosReserva() {
		if (mensagemCatalogosReserva == null) {
			mensagemCatalogosReserva = "";
		}
		return mensagemCatalogosReserva;
	}

	public Boolean getMostrarModalReserva() {
		if (mostrarModalReserva == null) {
			mostrarModalReserva = false;
		}
		return mostrarModalReserva;
	}

	public void setMostrarModalReserva(Boolean mostrarModalReserva) {
		this.mostrarModalReserva = mostrarModalReserva;
	}

	public List<ExemplarVO> getListaExemplarCatalogoSelecionado() {
		if (listaExemplarCatalogoSelecionado == null) {
			listaExemplarCatalogoSelecionado = new ArrayList<ExemplarVO>(0);
		}
		return listaExemplarCatalogoSelecionado;
	}

	public void setListaExemplarCatalogoSelecionado(List<ExemplarVO> listaExemplarCatalogoSelecionado) {
		this.listaExemplarCatalogoSelecionado = listaExemplarCatalogoSelecionado;
	}

	public String getAjudaCriacaoSenha() {
		if (ajudaCriacaoSenha == null) {
			ajudaCriacaoSenha = "";
		}
		return ajudaCriacaoSenha;
	}

	public void setAjudaCriacaoSenha(String ajudaCriacaoSenha) {
		this.ajudaCriacaoSenha = ajudaCriacaoSenha;
	}

	public Boolean getAbrirModalRegistrar() {
		if (abrirModalRegistrar == null) {
			abrirModalRegistrar = Boolean.FALSE;
		}
		return abrirModalRegistrar;
	}

	public void setAbrirModalRegistrar(Boolean abrirModalRegistrar) {
		this.abrirModalRegistrar = abrirModalRegistrar;
	}

	public PessoaVO getPessoaExistente() {
		if (pessoaExistente == null) {
			pessoaExistente = new PessoaVO();
		}
		return pessoaExistente;
	}

	public void setPessoaExistente(PessoaVO pessoaExistente) {
		this.pessoaExistente = pessoaExistente;
	}

	public UsuarioVO getUsuarioExistente() {
		if (usuarioExistente == null) {
			usuarioExistente = new UsuarioVO();
		}
		return usuarioExistente;
	}

	public void setUsuarioExistente(UsuarioVO usuarioExistente) {
		this.usuarioExistente = usuarioExistente;
	}

	@PostConstruct
	public void inicializarDados(){
		try {
			setOncomplete("");
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
			setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(getBibliotecaVO().getCodigo(),getUsuarioLogado().getPessoa().getTipoPessoa(), "", getUsuarioLogado()));
			montarDadosBiblioteca();
			carregarDadosBibliotecaAposLogin();
			if (getNomeTelaAtual().contains("homeAdministrador.xhtml")) {
				setOncomplete("abrirPopupMaximizada('../../minhaBiblioteca/homeBibliotecaExterna.xhtml', 'buscaBiblioteca');");
			} else if (getNomeTelaAtual().contains("visaoAluno")) {
				setOncomplete("window.open('../minhaBiblioteca/homeBibliotecaExterna.xhtml', '_blank')");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			if (getNomeTelaAtual().contains("homeAdministrador.xhtml") || getNomeTelaAtual().contains("visaoAluno")) {
				setOncomplete("");
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getDataLogado() {
		if (dataLogado == null) {
			dataLogado = Uteis.getDiaSemana_Apresentar().concat(",").concat(Uteis.getData(new Date()));
		}
		return dataLogado;
	}

	public void setDataLogado(String dataLogado) {
		this.dataLogado = dataLogado;
	}

	public List<SelectItem> getListaBibliotecas() {
		if (listaBibliotecas == null) {
			listaBibliotecas = new ArrayList<SelectItem>(0);
		}
		return listaBibliotecas;
	}

	public void setListaBibliotecas(List<SelectItem> listaBibliotecas) {
		this.listaBibliotecas = listaBibliotecas;
	}

	public String getCaminhoFotoUsuario() {
		if (caminhoFotoUsuario == null) {
			caminhoFotoUsuario = "/resources/imagens/biblioteca/perfilPadrao.png";
		}
		return caminhoFotoUsuario;
	}

	public void setCaminhoFotoUsuario(String caminhoFotoUsuario) {
		this.caminhoFotoUsuario = caminhoFotoUsuario;
	}

	public Integer getQuantidadeEmprestimoAberto() {
		if (quantidadeEmprestimoAberto == null) {
			quantidadeEmprestimoAberto = 0;
		}
		return quantidadeEmprestimoAberto;
	}

	public void setQuantidadeEmprestimoAberto(Integer quantidadeEmprestimoAberto) {
		this.quantidadeEmprestimoAberto = quantidadeEmprestimoAberto;
	}

	public Integer getQuantidadeEmprestimoAtrasado() {
		if (quantidadeEmprestimoAtrasado == null) {
			quantidadeEmprestimoAtrasado = 0;
		}
		return quantidadeEmprestimoAtrasado;
	}

	public void setQuantidadeEmprestimoAtrasado(Integer quantidadeEmprestimoAtrasado) {
		this.quantidadeEmprestimoAtrasado = quantidadeEmprestimoAtrasado;
	}

	public Integer getQuantidadeReservaPendente() {
		if (quantidadeReservaPendente == null) {
			quantidadeReservaPendente = 0;
		}
		return quantidadeReservaPendente;
	}

	public void setQuantidadeReservaPendente(Integer quantidadeReservaPendente) {
		this.quantidadeReservaPendente = quantidadeReservaPendente;
	}

	public Double getValorTotalMulta() {
		if (valorTotalMulta == null) {
			valorTotalMulta = 0.0;
		}
		return valorTotalMulta;
	}

	public void setValorTotalMulta(Double valorTotalMulta) {
		this.valorTotalMulta = valorTotalMulta;
	}

	public List<ReservaVO> getReservaVOs() {
		if (reservaVOs == null) {
			reservaVOs = new ArrayList<ReservaVO>(0);
		}
		return reservaVOs;
	}

	public void setReservaVOs(List<ReservaVO> reservaVOs) {
		this.reservaVOs = reservaVOs;
	}

	public String getSenhaUsuarioMembroComunidade() {
		if (senhaUsuarioMembroComunidade == null) {
			senhaUsuarioMembroComunidade = "";
		}
		return senhaUsuarioMembroComunidade;
	}

	public void setSenhaUsuarioMembroComunidade(String senhaUsuarioMembroComunidade) {
		this.senhaUsuarioMembroComunidade = senhaUsuarioMembroComunidade;
	}

	public String getUsernameUsuarioMembroComunidade() {
		if (usernameUsuarioMembroComunidade == null) {
			usernameUsuarioMembroComunidade = "";
		}
		return usernameUsuarioMembroComunidade;
	}

	public void setUsernameUsuarioMembroComunidade(String usernameUsuarioMembroComunidade) {
		this.usernameUsuarioMembroComunidade = usernameUsuarioMembroComunidade;
	}

	public String getMensagemAlertaCriacaoUsuario() {
		if (mensagemAlertaCriacaoUsuario == null) {
			mensagemAlertaCriacaoUsuario = "";
		}
		return mensagemAlertaCriacaoUsuario;
	}

	public void setMensagemAlertaCriacaoUsuario(String mensagemAlertaCriacaoUsuario) {
		this.mensagemAlertaCriacaoUsuario = mensagemAlertaCriacaoUsuario;
	}

	public String getMensagemAlertaCriacaoUsername() {
		if (mensagemAlertaCriacaoUsername == null) {
			mensagemAlertaCriacaoUsername = "";
		}
		return mensagemAlertaCriacaoUsername;
	}

	public void setMensagemAlertaCriacaoUsername(String mensagemAlertaCriacaoUsername) {
		this.mensagemAlertaCriacaoUsername = mensagemAlertaCriacaoUsername;
	}

	public Boolean getMostrarAlertaCpf() {
		if (mostrarAlertaCpf == null) {
			mostrarAlertaCpf = false;
		}
		return mostrarAlertaCpf;
	}

	public void setMostrarAlertaCpf(Boolean mostrarAlertaCpf) {
		this.mostrarAlertaCpf = mostrarAlertaCpf;
	}

	public Boolean getMostrarAlertaUsuario() {
		if (mostrarAlertaUsuario == null) {
			mostrarAlertaUsuario = false;
		}
		return mostrarAlertaUsuario;
	}

	public void setMostrarAlertaUsuario(Boolean mostrarAlertaUsuario) {
		this.mostrarAlertaUsuario = mostrarAlertaUsuario;
	}

	public String getMensagemAlertaCriacaoSenha() {
		if (mensagemAlertaCriacaoSenha == null) {
			mensagemAlertaCriacaoSenha = "";
		}
		return mensagemAlertaCriacaoSenha;
	}

	public void setMensagemAlertaCriacaoSenha(String mensagemAlertaCriacaoSenha) {
		this.mensagemAlertaCriacaoSenha = mensagemAlertaCriacaoSenha;
	}

	public Boolean getMostrarAlertaSenha() {
		if (mostrarAlertaSenha == null) {
			mostrarAlertaSenha = false;
		}
		return mostrarAlertaSenha;
	}

	public void setMostrarAlertaSenha(Boolean mostrarAlertaSenha) {
		this.mostrarAlertaSenha = mostrarAlertaSenha;
	}

	public String getMensagemAlertaEmail() {
		if (mensagemAlertaEmail == null) {
			mensagemAlertaEmail = "";
		}
		return mensagemAlertaEmail;
	}

	public void setMensagemAlertaEmail(String mensagemAlertaEmail) {
		this.mensagemAlertaEmail = mensagemAlertaEmail;
	}

	public Boolean getMostrarAlertaEmail() {
		if (mostrarAlertaEmail == null) {
			mostrarAlertaEmail = false;
		}
		return mostrarAlertaEmail;
	}

	public void setMostrarAlertaEmail(Boolean mostrarAlertaEmail) {
		this.mostrarAlertaEmail = mostrarAlertaEmail;
	}

	public List<ArquivoVO> getListaArquivosBibliotecaExterna() {
		if (listaArquivosBibliotecaExterna == null) {
			listaArquivosBibliotecaExterna = new ArrayList<ArquivoVO>(0);
		}
		return listaArquivosBibliotecaExterna;
	}

	public void setListaArquivosBibliotecaExterna(List<ArquivoVO> listaArquivosBibliotecaExterna) {
		this.listaArquivosBibliotecaExterna = listaArquivosBibliotecaExterna;
	}

	public Boolean getLogadoVisaoAdministrativa() {
		if (logadoVisaoAdministrativa == null) {
			logadoVisaoAdministrativa = false;
		}
		return logadoVisaoAdministrativa;
	}

	public void setLogadoVisaoAdministrativa(Boolean logadoVisaoAdministrativa) {
		this.logadoVisaoAdministrativa = logadoVisaoAdministrativa;
	}

	public String getTipoPessoaReserva() {
		if (tipoPessoaReserva == null) {
			tipoPessoaReserva = "";
		}
		return tipoPessoaReserva;
	}

	public void setTipoPessoaReserva(String tipoPessoaReserva) {
		this.tipoPessoaReserva = tipoPessoaReserva;
	}

	public String getValorConsultaPessoaReserva() {
		if (valorConsultaPessoaReserva == null) {
			valorConsultaPessoaReserva = "";
		}
		return valorConsultaPessoaReserva;
	}

	public void setValorConsultaPessoaReserva(String valorConsultaPessoaReserva) {
		this.valorConsultaPessoaReserva = valorConsultaPessoaReserva;
	}

	public PessoaVO getPessoaReservarCatalogo() {
		if (pessoaReservarCatalogo == null) {
			pessoaReservarCatalogo = new PessoaVO();
		}
		return pessoaReservarCatalogo;
	}

	public void setPessoaReservarCatalogo(PessoaVO pessoaReservarCatalogo) {
		this.pessoaReservarCatalogo = pessoaReservarCatalogo;
	}

	@SuppressWarnings("rawtypes")
	public List getListaConsultaPessoaReservaCatalogo() {
		if (listaConsultaPessoaReservaCatalogo == null) {
			listaConsultaPessoaReservaCatalogo = new ArrayList(0);
		}
		return listaConsultaPessoaReservaCatalogo;
	}

	@SuppressWarnings("rawtypes")
	public void setListaConsultaPessoaReservaCatalogo(List listaConsultaPessoaReservaCatalogo) {
		this.listaConsultaPessoaReservaCatalogo = listaConsultaPessoaReservaCatalogo;
	}

	public String getMatriculaReserva() {
		if (matriculaReserva == null) {
			matriculaReserva = "";
		}
		return matriculaReserva;
	}

	public void setMatriculaReserva(String matriculaReserva) {
		this.matriculaReserva = matriculaReserva;
	}

	public Boolean getMostrarMensagemConfirmacaoReserva() {
		if (mostrarMensagemConfirmacaoReserva == null) {
			mostrarMensagemConfirmacaoReserva = false;
		}
		return mostrarMensagemConfirmacaoReserva;
	}

	public void setMostrarMensagemConfirmacaoReserva(Boolean mostrarMensagemConfirmacaoReserva) {
		this.mostrarMensagemConfirmacaoReserva = mostrarMensagemConfirmacaoReserva;
	}

	public String getSubtituloBuscaAvancada() {
		if (subtituloBuscaAvancada == null) {
			subtituloBuscaAvancada = "";
		}
		return subtituloBuscaAvancada;
	}

	public void setSubtituloBuscaAvancada(String subtituloBuscaAvancada) {
		this.subtituloBuscaAvancada = subtituloBuscaAvancada;
	}

	public List<SelectItem> getListaAreaConhecimentoBuscaAvancada() {
		if (listaAreaConhecimentoBuscaAvancada == null) {
			listaAreaConhecimentoBuscaAvancada = new ArrayList<SelectItem>();
		}
		return listaAreaConhecimentoBuscaAvancada;
	}

	public void setListaAreaConhecimentoBuscaAvancada(List<SelectItem> listaAreaConhecimentoBuscaAvancada) {
		this.listaAreaConhecimentoBuscaAvancada = listaAreaConhecimentoBuscaAvancada;
	}

	public void montarDadosBiblioteca() throws Exception {

		try {
			List<BibliotecaVO> bibliotecaVOs = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsinoNivelComboBox(
					getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());			
			if (Uteis.isAtributoPreenchido(bibliotecaVOs)) {
				bibliotecaVOs.add(new BibliotecaVO());
				setListaBibliotecas(null);
				getListaBibliotecas().addAll(UtilSelectItem.getListaSelectItem(bibliotecaVOs, "codigo", "nome", false));
				getBibliotecaVO().setCodigo(Integer.valueOf(getListaBibliotecas().get(1).getValue().toString()));
				getBibliotecaVO().setNome(getListaBibliotecas().get(1).getLabel());

			}
			selecionarBiblioteca();
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarDadosAreaConhecimento() throws Exception {

		try {

			if (Uteis.isAtributoPreenchido(getAreaConhecimentoVOs())) {
				setListaAreaConhecimentoBuscaAvancada(null);
				getListaAreaConhecimentoBuscaAvancada().add(new SelectItem(0, ""));
				getAreaConhecimentoVOs().stream()
						.map(p -> new SelectItem(p.getKeyConsulta().toString(), p.getLabelConsulta()))
						.forEach(getListaAreaConhecimentoBuscaAvancada()::add);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean getUsuarioLogadoBiblioteca() {
		if (usuarioLogadoBiblioteca == null) {
			usuarioLogadoBiblioteca = false;
		}
		return usuarioLogadoBiblioteca;
	}

	public void setUsuarioLogadoBiblioteca(Boolean usuarioLogadoBiblioteca) {
		this.usuarioLogadoBiblioteca = usuarioLogadoBiblioteca;
	}

	public void carregarDadosBibliotecaAposLogin() {
		setOncompleteModal("");
		setOncomplete("");
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			try {

				if (Uteis.isAtributoPreenchido(context().getExternalContext().getSessionMap().get("ListaTelaAcesso"))
						&& context().getExternalContext().getSessionMap().get("ListaTelaAcesso").toString()
								.contains("biblioteca.xhtml")) {
					setLogadoVisaoAdministrativa(true);
				}
				inicializarDadosFotoUsuario();
				inicializarDadosMenuTopo();
				setUsuarioLogadoBiblioteca(true);
				setAbrirModalRegistrar(false);
				
				setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade()
						.executarObterConfiguracaoBibliotecaComBaseTipoPessoa(getBibliotecaVO().getCodigo(),
								getUsuarioLogado().getPessoa().getTipoPessoa(), "", getUsuarioLogado()));
				montarDadosBiblioteca();
				if (getNomeTelaAtual().contains("homeAdministrador.xhtml")) {
					setOncomplete("abrirPopupMaximizada('../../minhaBiblioteca/homeBibliotecaExterna.xhtml', 'buscaBiblioteca');");
				} else if (getNomeTelaAtual().contains("visaoAluno")) {
					setOncomplete("window.open('../minhaBiblioteca/homeBibliotecaExterna.xhtml', '_blank')");
				}
				setOncompleteModal("RichFaces.$('modalLogin').hide();RichFaces.$('panelRegistrar').hide();");
			} catch (Exception e) {
				if (getNomeTelaAtual().contains("homeAdministrador.xhtml") || getNomeTelaAtual().contains("visaoAluno")) {
					setOncomplete("");
				}
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	public String deslogarUsuarioBiblioteca() throws Exception {
		HttpSession session = (HttpSession) context().getExternalContext().getSession(false);
		session.invalidate();
		removerObjetoMemoria(this);
		setUsuario(new UsuarioVO());
		context().getExternalContext().getSessionMap().remove("usuarioLogado");
		setListaTimeLineVOs(new ArrayList<TimeLineVO>(0));
		setUsuarioLogadoBiblioteca(false);
		setMensagemID("msg_entre_prmlogout");
		montarDadosBiblioteca();
		consultarFiltrosBibliotecaExterna();
		return Uteis.getCaminhoRedirecionamentoNavegacao("homeBibliotecaExterna.xhtml");
	}

	public void inicializarDadosFotoUsuario() throws Exception {
		if (!getUsuarioLogado().getPessoa().getArquivoImagem().getNome().equals("")) {
			setCaminhoFotoUsuario(getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + getUsuarioLogado().getPessoa().getCPF()
					+ File.separator + getUsuarioLogado().getPessoa().getArquivoImagem().getNome());
		} else {
			setCaminhoFotoUsuario("/resources/imagens/biblioteca/perfilPadrao.png");
		}
	}

	public void inicializarDadosMenuTopo() throws Exception {
		setValorTotalMulta(0.0);
		setQuantidadeReservaPendente(0);
		setQuantidadeEmprestimoAberto(0);
		setQuantidadeEmprestimoAtrasado(0);
		
		setListaTimeLineVOs(getFacadeFactory().getMinhaBibliotecaInterfaceFacade()
				.listarDadosTimeLine(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado()));

		for (TimeLineVO obj : getListaTimeLineVOs()) {

			if (obj.getTipoEmprestimo().equals("ATRASADO") && !obj.getSituacaoEmprestimo().equals("DE")) {
				setQuantidadeEmprestimoAtrasado(getQuantidadeEmprestimoAtrasado() + 1);
				obj.setValorMulta(getFacadeFactory().getMinhaBibliotecaInterfaceFacade().realizarCalculoMultaParcial(
						obj, TipoPessoa.getEnum(obj.getTipoPessoaEmprestimo()), obj.getConfiguracaoBibliotecaVO(),
						obj.getCidadeBibliotecaVO()));
			}

			if (obj.getSituacaoEmprestimo().equals("AT") || obj.getSituacaoEmprestimo().equals("EX")) {
				setQuantidadeEmprestimoAberto(getQuantidadeEmprestimoAberto() + 1);
				
			}

			if (obj.getReserva() == true
					&& (obj.getSituacaoReserva().equals("EX") || obj.getSituacaoReserva().equals("DI"))) {
				setQuantidadeReservaPendente(getQuantidadeReservaPendente() + 1);
			}

			if (obj.getMultaPaga() == false) {
				setValorTotalMulta(getValorTotalMulta() + obj.getValorMulta());
			}
		}

		DadosMenuTopoReserva();
		if(getQuantidadeEmprestimoAberto() > 0) {
			setListaConsulta(getFacadeFactory().getItemEmprestimoFacade().consultarItensEmprestadosPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getBibliotecaVO().getCodigo(), "", Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoBibliotecaVO(), getUsuarioLogado()));
		}

	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralSistemaVO(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(),
					getConfiguracaoGeralSistemaVO(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralSistemaVO(), getLargura(), getAltura(), getX(), getY());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralSistemaVO(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			inicializarBooleanoFoto();
			setOncompleteModal("RichFaces.$('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public String getShowFotoCrop() {
		try {
			if (getPessoaVO().getArquivoImagem().getNome() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario() + "?UID=" + new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}

	public void cancelar() {
		try {
			if (getPessoaVO().getArquivoImagem() != null && getPessoaVO().getArquivoImagem().getDescricao() != "") {
				getFacadeFactory().getArquivoHelper().realizarGravacaoFotoSemRecorte(getPessoaVO().getArquivoImagem(),
						getConfiguracaoGeralPadraoSistema());
				setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
						getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
						getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
				inicializarBooleanoFoto();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void inicializarBooleanoFoto() {
		setRemoverFoto((Boolean) false);
		setExibirUpload(true);
		setExibirBotao(false);
	}

	public String gravar() {
		try {
			getPessoaVO().setMembroComunidade(true);
			if (!Uteis.isAtributoPreenchido(getPessoaVO().getCPF())
					&& !Uteis.isAtributoPreenchido(getPessoaVO().getCertidaoNascimento())) {
				throw new Exception("É Obrigatório Preencher o Campo CPF)");
			} else {
				getFacadeFactory().getMinhaBibliotecaInterfaceFacade().criarCadastroVisitante(getPessoaVO(),
						getConfiguracaoGeralPadraoSistema(), getUsernameUsuarioMembroComunidade(),
						getSenhaUsuarioMembroComunidade());
				executarLogin(getUsernameUsuarioMembroComunidade(), getSenhaUsuarioMembroComunidade());
				return "homeBibliotecaExterna";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setAbrirModalRegistrar(false);
			return "";
		}

	}

	public String iniciarPessoa() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Novo Aluno", "Novo");
			removerObjetoMemoria(this);
			setPessoaVO(new PessoaVO());
			setAbrirModalRegistrar(Boolean.TRUE);
			regrasCriacaoSenha();
			setMensagemID("msg_entre_dados");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("login");

	}

	public List<TipoFiltroConsulta> getAreaConhecimentoVOs() {
		if (areaConhecimentoVOs == null) {
			areaConhecimentoVOs = new ArrayList<TipoFiltroConsulta>();
		}
		return areaConhecimentoVOs;
	}

	public void setAreaConhecimentoVOs(List<TipoFiltroConsulta> areaConhecimentoVOs) {
		this.areaConhecimentoVOs = areaConhecimentoVOs;
	}

	public List<TipoFiltroConsulta> getTipoCatalogoVOs() {
		if (tipoCatalogoVOs == null) {
			tipoCatalogoVOs = new ArrayList<TipoFiltroConsulta>();
		}
		return tipoCatalogoVOs;
	}

	public void setTipoCatalogoVOs(List<TipoFiltroConsulta> tipoCatalogoVOs) {
		this.tipoCatalogoVOs = tipoCatalogoVOs;
	}

	public List<TipoFiltroConsulta> getListaMenuCursoVOs() {
		if (listaMenuCursoVOs == null) {
			listaMenuCursoVOs = new ArrayList<TipoFiltroConsulta>();
		}
		return listaMenuCursoVOs;
	}

	public void setListaMenuCursoVOs(List<TipoFiltroConsulta> listaMenuCursoVOs) {
		this.listaMenuCursoVOs = listaMenuCursoVOs;
	}

	public List<TipoFiltroConsulta> getTipoFiltroConsultas() {
		if (tipoFiltroConsultas == null) {
			tipoFiltroConsultas = new ArrayList<TipoFiltroConsulta>();
		}
		return tipoFiltroConsultas;
	}

	public void setTipoFiltroConsultas(List<TipoFiltroConsulta> tipoFiltroConsultas) {
		this.tipoFiltroConsultas = tipoFiltroConsultas;
	}

	public List<TipoFiltroConsulta> getDisciplinaPeriodo() {
		if (disciplinaPeriodo == null) {
			disciplinaPeriodo = new ArrayList<TipoFiltroConsulta>();
		}
		return disciplinaPeriodo;
	}

	public void setDisciplinaPeriodo(List<TipoFiltroConsulta> disciplinaPeriodo) {
		this.disciplinaPeriodo = disciplinaPeriodo;
	}

	public Boolean getVisualizarSenha() {
		if (visualizarSenha == null) {
			visualizarSenha = false;
		}
		return visualizarSenha;
	}

	public void setVisualizarSenha(Boolean visualizarSenha) {
		this.visualizarSenha = visualizarSenha;
	}

	public String getTituloBuscaAvancada() {
		if (tituloBuscaAvancada == null) {
			tituloBuscaAvancada = "";
		}
		return tituloBuscaAvancada;
	}

	public void setTituloBuscaAvancada(String tituloBuscaAvancada) {
		this.tituloBuscaAvancada = tituloBuscaAvancada;
	}

	public String getAssuntoBuscaAvancada() {
		if (assuntoBuscaAvancada == null) {
			assuntoBuscaAvancada = "";
		}
		return assuntoBuscaAvancada;
	}

	public void setAssuntoBuscaAvancada(String assuntoBuscaAvancada) {
		this.assuntoBuscaAvancada = assuntoBuscaAvancada;
	}

	public String getPalavrasChaveBuscaAvancada() {
		if (palavrasChaveBuscaAvancada == null) {
			palavrasChaveBuscaAvancada = "";
		}
		return palavrasChaveBuscaAvancada;
	}

	public void setPalavrasChaveBuscaAvancada(String palavrasChaveBuscaAvancada) {
		this.palavrasChaveBuscaAvancada = palavrasChaveBuscaAvancada;
	}

	public String getAutoresBuscaAvancada() {
		if (autoresBuscaAvancada == null) {
			autoresBuscaAvancada = "";
		}
		return autoresBuscaAvancada;
	}

	public void setAutoresBuscaAvancada(String autoresBuscaAvancada) {
		this.autoresBuscaAvancada = autoresBuscaAvancada;
	}

	public String getClassificacaoBuscaAvancada() {
		if (classificacaoBuscaAvancada == null) {
			classificacaoBuscaAvancada = "";
		}
		return classificacaoBuscaAvancada;
	}

	public void setClassificacaoBuscaAvancada(String classificacaoBuscaAvancada) {
		this.classificacaoBuscaAvancada = classificacaoBuscaAvancada;
	}

	public String getIsbnBuscaAvancada() {
		if (isbnBuscaAvancada == null) {
			isbnBuscaAvancada = "";
		}
		return isbnBuscaAvancada;
	}

	public void setIsbnBuscaAvancada(String isbnBuscaAvancada) {
		this.isbnBuscaAvancada = isbnBuscaAvancada;
	}

	public String getIssnBuscaAvancada() {
		if (issnBuscaAvancada == null) {
			issnBuscaAvancada = "";
		}
		return issnBuscaAvancada;
	}

	public void setIssnBuscaAvancada(String issnBuscaAvancada) {
		this.issnBuscaAvancada = issnBuscaAvancada;
	}

	public String getCutterPhaBuscaAvancada() {
		if (cutterPhaBuscaAvancada == null) {
			cutterPhaBuscaAvancada = "";
		}
		return cutterPhaBuscaAvancada;
	}

	public void setCutterPhaBuscaAvancada(String cutterPhaBuscaAvancada) {
		this.cutterPhaBuscaAvancada = cutterPhaBuscaAvancada;
	}

	public String getTomboBuscaAvancada() {
		if (tomboBuscaAvancada == null) {
			tomboBuscaAvancada = "";
		}
		return tomboBuscaAvancada;
	}

	public void setTomboBuscaAvancada(String tomboBuscaAvancada) {
		this.tomboBuscaAvancada = tomboBuscaAvancada;
	}

	public Boolean getUnificarConsultaBuscaAvancada() {
		if (unificarConsultaBuscaAvancada == null) {
			unificarConsultaBuscaAvancada = false;
		}
		return unificarConsultaBuscaAvancada;
	}

	public void setUnificarConsultaBuscaAvancada(Boolean unificarConsultaBuscaAvancada) {
		this.unificarConsultaBuscaAvancada = unificarConsultaBuscaAvancada;
	}

	public String getAreaConhecimentoBuscaAvancada() {
		if (areaConhecimentoBuscaAvancada == null) {
			areaConhecimentoBuscaAvancada = "";
		}
		return areaConhecimentoBuscaAvancada;
	}

	public void setAreaConhecimentoBuscaAvancada(String areaConhecimentoBuscaAvancada) {
		this.areaConhecimentoBuscaAvancada = areaConhecimentoBuscaAvancada;
	}

	public void selecionarBiblioteca() {

		consultarFiltrosBibliotecaExterna();
		irPaginaInicial();
	}

	public void consultarCatalogo() {
		try {
			limparMensagem();
			getControleConsultaOtimizado().setLimitePorPagina(15);
			getFacadeFactory().getMinhaBibliotecaInterfaceFacade().consultarCatalogos(getCodigoCatalogoServelet(),
					getControleConsultaOtimizado(), getBibliotecaVO(), getTipoFiltroConsultas(),
					getValorPesquisaCatalogoVO(), "titulo",
					getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo(), getTomboBuscaAvancada(),
					getTituloBuscaAvancada(), getAssuntoBuscaAvancada(), getPalavrasChaveBuscaAvancada(),
					getAutoresBuscaAvancada(), getClassificacaoBuscaAvancada(), getIsbnBuscaAvancada(),
					getIssnBuscaAvancada(), getUsuarioLogado());

			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarFiltroConsulta() {
		TipoFiltroConsulta tipoFiltroConsulta = (TipoFiltroConsulta) getRequestMap().get("filtro");
//		if(tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.TIPO_CATALOGO)) {
//			for (Iterator<TipoFiltroConsulta> iterator = getTipoFiltroConsultas().iterator(); iterator.hasNext();) {
//				TipoFiltroConsulta tipoFiltroConsulta2 = iterator.next();
//				if(tipoFiltroConsulta2.getCampoConsulta().equals(TipoFiltroConsultaEnum.TIPO_CATALOGO)) {
//					iterator.remove();
//				}
//			}
//		}
		getTipoFiltroConsultas().add(tipoFiltroConsulta);
		tipoFiltroConsulta.setSelecionado(true);
		irPaginaInicial();
	}

	public void removerFiltroConsulta() {
		TipoFiltroConsulta tipoFiltroConsulta = (TipoFiltroConsulta) getRequestMap().get("filtro");
		getTipoFiltroConsultas().remove(tipoFiltroConsulta);
		tipoFiltroConsulta.setSelecionado(false);
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.TITULO)) {
			setTituloBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.SUBTITULO)) {
			setSubtituloBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.ASSUNTO)) {
			setAssuntoBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.AUTOR)) {
			setAutoresBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.TOMBO)) {
			setTomboBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.CLASSIFICACAO)) {
			setClassificacaoBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.PALAVRA_CHAVE)) {
			setPalavrasChaveBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.CUTTER_PHA)) {
			setCutterPhaBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.ISBN)) {
			setIsbnBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.ISSN)) {
			setIssnBuscaAvancada(null);
		}
		if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.AREA_CONHECIMENTO)) {
			setAreaConhecimentoBuscaAvancada(null);
		}
		irPaginaInicial();
	}

	public void consultarFiltrosBibliotecaExterna() {
		try {
			Map<TipoFiltroConsultaEnum, List<TipoFiltroConsulta>> mapConsulta = getFacadeFactory().getCatalogoFacade()
					.consultarFiltrosBibliotecaExterna(getBibliotecaVO().getCodigo(), getUsuarioLogado());
			setTipoCatalogoVOs(mapConsulta.get(TipoFiltroConsultaEnum.TIPO_CATALOGO));
			setAreaConhecimentoVOs(mapConsulta.get(TipoFiltroConsultaEnum.AREA_CONHECIMENTO));
			setListaMenuCursoVOs(mapConsulta.get(TipoFiltroConsultaEnum.CURSO));
			setDisciplinaPeriodo(mapConsulta.get(TipoFiltroConsultaEnum.DISCIPLINA));
			for (Iterator<TipoFiltroConsulta> iterator = getTipoFiltroConsultas().iterator(); iterator.hasNext();) {
				TipoFiltroConsulta tipoFiltroConsulta = iterator.next();
				if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.AREA_CONHECIMENTO)
						&& !getAreaConhecimentoVOs().contains(tipoFiltroConsulta)) {
					iterator.remove();
				} else if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.TIPO_CATALOGO)
						&& !getTipoCatalogoVOs().contains(tipoFiltroConsulta)) {
					iterator.remove();
				} else if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.CURSO)
						&& !getTipoCatalogoVOs().contains(tipoFiltroConsulta)) {
					iterator.remove();
				} else if (tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.DISCIPLINA)
						&& !getTipoCatalogoVOs().contains(tipoFiltroConsulta)) {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarCatalogo();

	}

	public void irPaginaInicial() {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		consultarCatalogo();
	}

	public Boolean getApresentarImagemTopo() {
		if (getTam() <= 4) {
			return false;
		}
		return true;

	}

	public Boolean getApresentarColunaModal() {
		if (getTam() <= 2) {
			return false;
		}
		return true;

	}

	public void selecionarCatalogo() throws Exception {
		catalogoVO = (CatalogoVO) context().getExternalContext().getRequestMap().get("itensCatalogos");
		setListaArquivosBibliotecaExterna(
				getFacadeFactory().getArquivoFacade().consultarArquivosBibliotecaExterna(catalogoVO.getCodigo()));
	}

	public void adicionarCatalogoNaGuiaDeReserva() {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("itensCatalogos");
		try {
			setCatalogoVO(obj);
			getListaCatalogosAdicionadosNaGuiaReserva().add(getCatalogoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void removerCatalogoDaGuiaDeReserva() {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoReservaItens");
		try {
			getListaCatalogosAdicionadosNaGuiaReserva().remove(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void adicionarExemplarNaGuia() {
		ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("itensExemplarGuia");
		try {
			getListaAdicionadosGuia().add(obj);
			limparExemplareslistaSelecionada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void removerExemplarNaGuia() {
		ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarGuiaItens");
		try {
			getListaAdicionadosGuia().remove(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void buscaExemplaresCatalogo() {
		setListaExemplarCatalogoSelecionado(new ArrayList<ExemplarVO>(0));
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("itensCatalogos");
		setCatalogoVO(obj);
		try {
			getListaExemplarCatalogoSelecionado().addAll(getFacadeFactory().getExemplarFacade()
					.consultarPorCatalogoBiblioteca(obj.getCodigo(), getBibliotecaVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void limparExemplareslistaSelecionada() {
		setListaExemplarCatalogoSelecionado(null);
	}

	public void validarVisaoReservaCatalogo() {

		if (!getLogadoVisaoAdministrativa()) {
//			setPessoaVO(getUsuario().getPessoa());
			setPessoaVO(getUsuarioLogado().getPessoa());
			setTipoPessoaReserva(getUsuarioLogado().getTipoPessoa());
			executarReservaCatalogos();
		}
	}

	public void executarReservaCatalogos() {
		try {
			if (getPessoaVO().getCodigo() == 0) {
				throw new Exception("O campo PESSOA deve ser informado.");
			} else {
				getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(getPessoaVO().getCodigo(),
						getBibliotecaVO().getCodigo(), getTipoPessoaReserva(), getUsuarioLogado());
				setReservaVOs(getFacadeFactory().getCatalogoFacade().executarReservaCatalogos(
						getListaCatalogosAdicionadosNaGuiaReserva(), getBibliotecaVO(), getPessoaVO(),
						getTipoPessoaReserva(), getMatriculaReserva(), getConfiguracaoBibliotecaVO(),
						getUsuarioLogado()));
				setMensagemCatalogosReserva(getReservaVOs().get(0).getMensagemListaCatalogoReservado());
				if (getReservaVOs().get(0).getCodigo() == 0) {
					setReservaVOs(null);
				}
				DadosMenuTopoReserva();
				setListaCatalogosAdicionadosNaGuiaReserva(null);
				setMostrarModalReserva(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMostrarModalReserva(Boolean.TRUE);
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMensagemCatalogosReserva(e.getMessage());
		}
	}

	public void DadosMenuTopoReserva() throws Exception {

		setQuantidadeReservaPendente(null);
		setListaReservaTimeLineVOs(getFacadeFactory().getMinhaBibliotecaInterfaceFacade()
				.listarDadosTimeLineReservas(getUsuarioLogado().getPessoa().getCodigo(), getUsuario()));
		for (TimeLineVO obj : getListaReservaTimeLineVOs()) {

			if (obj.getReserva() == true
					&& (obj.getSituacaoReserva().equals("EX") || obj.getSituacaoReserva().equals("DI"))) {
				setQuantidadeReservaPendente(getQuantidadeReservaPendente() + 1);
			}
		}

	}

	public void imprimirTicketReservaPDF() {

		try {
			if (getReservaVOs() != null && !getReservaVOs().isEmpty()) {
				String design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca"
						+ File.separator + "comprovanteReserva.jrxml");
				getSuperParametroRelVO().setTituloRelatorio("COMPROVANTE DE RESERVA");
				getSuperParametroRelVO().adicionarParametro("nomeBiblioteca",
						getReservaVOs().get(0).getBibliotecaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("solicitante",
						getReservaVOs().get(0).getPessoa().getNome());
				getSuperParametroRelVO().adicionarParametro("matricula", getReservaVOs().get(0).getMatricula());
				getSuperParametroRelVO().adicionarParametro("textoReserva",
						configuracaoBibliotecaVO.getTextoPadraoReservaCatalogo());

				getSuperParametroRelVO().setListaObjetos(getReservaVOs());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				realizarImpressaoRelatorio();

				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirGuia() {
		try {
			if (!getListaAdicionadosGuia().isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(Exemplar.designerGuiaReservaExemplar());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(Exemplar.caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("GUIA DE EMPRÉSTIMO");
				getSuperParametroRelVO().setListaObjetos(getListaAdicionadosGuia());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(Exemplar.caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (getUnidadeEnsinoLogado().getCodigo().equals(0)) {
					getSuperParametroRelVO().setUnidadeEnsino("");
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				}
				if (getBibliotecaVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().setBiblioteca("");
				} else {
					getSuperParametroRelVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade()
							.consultarPorChavePrimaria(getBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,
									getUsuarioLogado())
							.getNome());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void regrasCriacaoSenha() {

		try {
			setConfiguracaoGeralSistemaVO(
					getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
			setAjudaCriacaoSenha("Sua senha deve ter no mínimo "
					+ getConfiguracaoGeralSistemaVO().getQuantidadeCaracteresMinimoSenhaUsuario() + " caracteres");

			if (getConfiguracaoGeralSistemaVO().getNivelSegurancaCaracterEspecial()) {
				setAjudaCriacaoSenha(getAjudaCriacaoSenha().concat(" ,possuir caracteres especiais(*/%#_&$|@!?)"));
			}
			if (getConfiguracaoGeralSistemaVO().getNivelSegurancaLetraMaiuscula()) {
				setAjudaCriacaoSenha(getAjudaCriacaoSenha().concat(",Letra Maiuscula"));
			}
			if (getConfiguracaoGeralSistemaVO().getNivelSegurancaLetra()) {
				setAjudaCriacaoSenha(getAjudaCriacaoSenha().concat(",letra"));
			}
			if (getConfiguracaoGeralSistemaVO().getNivelSegurancaNumero()) {
				setAjudaCriacaoSenha(getAjudaCriacaoSenha().concat(",numero"));
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public String executarLogin(String userName, String senha) throws Exception {		
		getLoginControle().setUsuario(new UsuarioVO());
		getLoginControle().getUsuario().setPerfilAcesso(new PerfilAcessoVO());
		getLoginControle().setUsername(userName);
		getLoginControle().setSenha(senha);
		return getLoginControle().loginSistemaBiblioteca();
	}

	public String fecharModalRegistrar() {
		setAbrirModalRegistrar(false);
		return Uteis.getCaminhoRedirecionamentoNavegacao("homeBibliotecaExterna.xhtml");
	}

	public void apresentarSenha() {
		setVisualizarSenha(true);
//		  setSenhaUsuarioMembroComunidade(getSenhaUsuarioMembroComunidade());
	}

	public void montarDadosTimeLine() {
		try {
			setDadosTimeLineUsuario(null);
			getDadosTimeLineUsuario().addAll(
					getFacadeFactory().getMinhaBibliotecaInterfaceFacade().montarDadosTimeLine(getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarPessoaCadastradaPorCPF() throws Exception {
		ProspectsVO obj = null;
		String cpf = "";
		setMensagemAlertaCriacaoUsuario("");
		setMostrarAlertaCpf(false);
		if (getPessoaVO().getCPF().length() == 14) {
			cpf = getPessoaVO().getCPF();
			setPessoaExistente(getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(cpf, 0, "", false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (pessoaExistente.getCodigo().intValue() == 0) {
				obj = new ProspectsVO();
				obj.setCpf(cpf);
				obj = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(obj, false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
		} else {
			throw new Exception("CPF inválido.");
		}
		if (obj != null) {
			if (obj.getCodigo().intValue() != 0) {
				setMensagemAlertaCriacaoUsuario("Já existe um Prospect cadastrado");
				setMostrarAlertaCpf(true);
			}
		}
		if (pessoaExistente.getCodigo().intValue() != 0) {
			setMensagemAlertaCriacaoUsuario("Já existe um usuario cadastrado com este CPF : " + cpf);
			setMostrarAlertaCpf(true);

		}

	}

	public void validarUsuarioCadastrada() throws Exception {

		String Username = "";
		setMensagemAlertaCriacaoUsername("");
		setMostrarAlertaUsuario(false);

		Username = getUsernameUsuarioMembroComunidade();
		setUsuarioExistente(getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(Username, false,
				Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

		if (getUsuarioExistente().getCodigo().intValue() != 0) {
			setMensagemAlertaCriacaoUsername("Já Existe um Usuario cadastrado com este Username : " + Username);
			setMostrarAlertaUsuario(true);
		}

	}

	public void validarSenhaUsuarioCadastrada() {

		try {
			setMostrarAlertaSenha(false);
			setMensagemAlertaCriacaoSenha("");
			Uteis.validarSenha(getConfiguracaoGeralSistemaVO(), getSenhaUsuarioMembroComunidade());
		} catch (ConsistirException e) {
			for (String mensagemAlertaSenha : e.getListaMensagemErro()) {
				setMensagemAlertaCriacaoSenha(getMensagemAlertaCriacaoSenha().concat(mensagemAlertaSenha));
			}
			setMostrarAlertaSenha(true);
		}

	}

	public void validarEmail() {

		setMensagemAlertaEmail("");
		if (Uteis.getValidaEmail(getPessoaVO().getEmail())) {
			setMostrarAlertaEmail(false);
		} else {
			setMostrarAlertaEmail(true);
			setMensagemAlertaEmail("Email Invalido !");
		}

	}

	public Boolean getApresentarCadastrarUsuarioBiblioteca() {
		return (Uteis.isAtributoPreenchido(getPessoaVO().getCPF()) && getMostrarAlertaCpf() == false)
				&& (Uteis.isAtributoPreenchido(getUsernameUsuarioMembroComunidade())
						&& getMostrarAlertaUsuario() == false)
				&& (Uteis.isAtributoPreenchido(getSenhaUsuarioMembroComunidade()) && getMostrarAlertaSenha() == false)
				&& (Uteis.isAtributoPreenchido(getPessoaVO().getEmail()) && getMostrarAlertaEmail() == false);
	}

	public void realizarDownloadArquivo() throws CloneNotSupportedException {
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
		ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
		cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator
				+ cloneArquivo.getPastaBaseArquivo() + File.separator + getCatalogoVO().getCodigo());
		context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);

	}

	public List<SelectItem> tipoConsultaComboTipoPessoa;

	public List<SelectItem> getTipoConsultaComboTipoPessoa() {
		if (tipoConsultaComboTipoPessoa == null) {
			tipoConsultaComboTipoPessoa = new ArrayList<SelectItem>(0);
			tipoConsultaComboTipoPessoa.add(new SelectItem("", ""));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.ALUNO.getValor(), "Aluno"));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.PROFESSOR.getValor(), "Professor"));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), "Funcionário"));
		}
		return tipoConsultaComboTipoPessoa;
	}

	@SuppressWarnings("rawtypes")
	public void consultarPessoaReserva() {
		List objs = new ArrayList(0);
		setListaConsultaPessoaReservaCatalogo(null);
		try {
			if (getTipoPessoaReserva().equals(TipoPessoa.ALUNO.getValor())) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(
						getValorConsultaPessoaReserva(), getUnidadeEnsinoLogado().getCodigo(), false,
						getUsuarioLogado());

			} else if (getTipoPessoaReserva().equals(TipoPessoa.PROFESSOR.getValor())) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaPessoaReserva(),
						TipoPessoa.PROFESSOR.getValor(), getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			} else {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaPessoaReserva(),
						TipoPessoa.FUNCIONARIO.getValor(), getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaPessoaReservaCatalogo(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCamposConsultaPessoaReserva() {
		setListaConsultaPessoaReservaCatalogo(null);
		setValorConsultaPessoaReserva(null);
	}

	public void selecionarPessoaReserva() throws Exception {

		setMensagemCatalogosReserva("");

		if (getTipoPessoaReserva().equals(TipoPessoa.ALUNO.getValor())) {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			setPessoaVO(obj.getAluno());
			setMatriculaReserva(obj.getMatricula());
			setMensagemCatalogosReserva("Confirmar Reservar para " + obj.getAluno().getNome());
		} else {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			setPessoaVO(obj.getPessoa());
			setMensagemCatalogosReserva("Confirmar Reservar para " + obj.getPessoa().getNome());
		}

		limparCamposConsultaPessoaReserva();
		setMostrarMensagemConfirmacaoReserva(true);

	}

	public void consultarAvancada() {
		try {

			TipoFiltroConsulta tipoFiltroConsulta;

			if (Uteis.isAtributoPreenchido(getTituloBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.TITULO, "Titulo", "Titulo");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getSubtituloBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.SUBTITULO, "SubTitulo", "SubTitulo");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getAssuntoBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.ASSUNTO, "Assunto", "Assunto");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getAutoresBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.AUTOR, "Autor", "Autor");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getTomboBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.TOMBO, "Tombo", "Tombo");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getClassificacaoBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.CLASSIFICACAO, "Classificação",
						"Classificação");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getPalavrasChaveBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.PALAVRA_CHAVE, "Palavras-Chave",
						"Palavras-Chave");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getCutterPhaBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.CUTTER_PHA, "Cutter Pha",
						"Cutter Pha");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getIsbnBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.ISBN, "ISBN", "ISBN");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getIssnBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.ISSN, "ISSN", "ISSN");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}
			if (Uteis.isAtributoPreenchido(getAreaConhecimentoBuscaAvancada())) {
				tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.AREA_CONHECIMENTO,
						"Área de Conhecimento", "Área de Conhecimento");
				getTipoFiltroConsultas().add(tipoFiltroConsulta);
				tipoFiltroConsulta.setSelecionado(true);
			}

			irPaginaInicial();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarCatalogoServlet(String catalogo) {
		this.catalogoVO = new CatalogoVO();

	}

	public String getCodigoCatalogoServelet() {
		this.codigoCatalogoServelet = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("catalogo");
		if (codigoCatalogoServelet == null) {
			codigoCatalogoServelet = "";
		}
		return codigoCatalogoServelet;
	}

	public void setCodigoCatalogoServelet(String codigoCatalogoServelet) {
		this.codigoCatalogoServelet = codigoCatalogoServelet;
	}

	public String getCodigoBibliotecaServelet() {
		this.codigoBibliotecaServelet = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("biblioteca");
		if (codigoBibliotecaServelet == null) {
			codigoBibliotecaServelet = "";
		}
		return codigoBibliotecaServelet;
	}

	public void setCodigoBibliotecaServelet(String codigoBibliotecaServelet) {
		this.codigoBibliotecaServelet = codigoBibliotecaServelet;
	}

	
	public String getOncomplete() {
		if (oncomplete == null) {
			oncomplete = Constantes.EMPTY;
		}
		return oncomplete;
	}
	
	public void setOncomplete(String oncomplete) {
		this.oncomplete = oncomplete;
	}
}
