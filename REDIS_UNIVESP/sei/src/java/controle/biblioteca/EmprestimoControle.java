package controle.biblioteca;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.SituacaoReservaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoImpressaoComprovanteBibliotecaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoMidiaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoExemplar;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.TicketRelVO;;

@Controller("EmprestimoControle")
@Scope("viewScope")
@Lazy
public class EmprestimoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = -4960829667430225125L;

	private EmprestimoVO emprestimoVO;
	private Boolean tipoPessoaAluno;

	private Boolean tipoPessoaProfessor;
	private Boolean tipoPessoaFuncionario;
	
	private String matricula;
	private String matriculaFuncionario;
	private ExemplarVO exemplarVO;
	private ReservaVO reservaVO;
	private List<ReservaVO> listaCatalogosReservados;
	private List<ItemEmprestimoVO> listaItensEmprestimos;
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaVO;
	private List<SelectItem> listaSelectItemBiblioteca;
	private List<SelectItem> listaSelectItemHoraEmprestimo;
	private Boolean mostrarComboboxBiblioteca;
	private Boolean mostrarModalCodigoBarrasReserva;
	private String mensagemEmprestimoRealizadoComSucesso;
	private Boolean mostrarModalEmprestimoDevolucaoRealizadoComSucesso;
	private boolean apenasEmprestimosEmAberto = true;
	private List<String> listaMensagemSucesso;
	private Double valorMultaBiblioteca;
	private Integer qtdeEmprestimosAtrasados;
	private Double valorMultaACobrarBiblioteca;
	private Double valorIsencaoDevolucaoBiblioteca;
	private ExemplarVO exemplarConsulta;
	private String senhaSolicitante;
	private Boolean solicitarSenhaSolicitante;
	private UnidadeEnsinoVO unidadeEnsino;

	private String textoComprovante;
	private Boolean tipoPessoaMembroComunidade;
	private Boolean apresentarTipoEmprestimo;	
	private List listaConsultaMembroComunidade;
	private String valorConsultaMembroComunidade;
	private String campoConsultaMembroComunidade;
	private CidadeVO cidadeBibliotecaVO;
	private Boolean possuiBloqueioBiblioteca;
	private List<SelectItem> listaSelectItemImpressora;
	private String codigoFinanceiroMatricula;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Boolean apresentarMotivoIsencao;
	private ItemEmprestimoVO itemEmprestimoIsencaoVO;

	private DataModelo dataModeloAluno;
	private DataModelo dataModeloProfessor;
	private DataModelo dataModeloFuncionario;
	
	private String tipoConsulta;

	public EmprestimoControle() {
		try {
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@PostConstruct
	public String novoEmprestimo() {
		try {
			EmprestimoVO obj = (EmprestimoVO) context().getExternalContext().getSessionMap().get("EmprestimoVO");
			if (obj != null && !obj.getBiblioteca().getCodigo().equals(0)) {
				novo();
				setListaConsulta(
						getFacadeFactory().getEmprestimoFacade().consultarAtrasadosPorBibliotecaSituacaoEmExecucao(
								obj.getBiblioteca().getCodigo(), getUnidadeEnsinoLogado().getCodigo(),
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				context().getExternalContext().getSessionMap().remove("EmprestimoVO");
			} else if (context().getExternalContext().getSessionMap().get("matriculaEmprestimoFichaAluno") != null) {
				MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap()
						.get("matriculaEmprestimoFichaAluno");
				if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
					try {
						novo();
						getEmprestimoVO().setPessoa(matriculaVO.getAluno());
						getEmprestimoVO().getMatricula().setMatricula(matriculaVO.getMatricula());
						getEmprestimoVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
						setMatricula(matriculaVO.getMatricula());
						montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
					} catch (Exception e) {
						setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
					} finally {
						context().getExternalContext().getSessionMap().remove("matriculaEmprestimoFichaAluno");
					}
				}
			}else if(!getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
					setEmprestimoVO(new EmprestimoVO());
					montarListaSelectItemBiblioteca();
					getEmprestimoVO().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
					if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()){
						getEmprestimoVO().setMatricula(getVisaoAlunoControle().getMatricula());
						setMatricula(getVisaoAlunoControle().getMatricula().getMatricula());
						getEmprestimoVO().setTipoPessoa("AL");
					}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()){
						getEmprestimoVO().setTipoPessoa("FU");
					}else if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
						getEmprestimoVO().setTipoPessoa("PR");
					}
					montarConfiguracaoBibliotecaComBaseTipoPessoa();
					executarConsultaTelaBuscaEmprestimoEmAberto();
			} else {
				removerObjetoMemoria(this);
				setEmprestimoVO(null);
				setExemplarVO(null);
				montarListaSelectItemBiblioteca();
				getFacadeFactory().getEmprestimoFacade().inicializarDadosEmprestimoNovo(getEmprestimoVO(),getUsuarioLogado());
				setarFalseNosFiltros();
				limparListas();
				setTipoPessoaAluno(Boolean.TRUE);
				setMensagemID("msg_entre_dados");
			}
			inicializarDadosFotoUsuario();
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}
	
	/**
	 * Rotina responsÃ¯Â¿Â½vel por disponibilizar um novo objeto da classe
	 * <code>Emprestimo</code> para ediÃ¯Â¿Â½Ã¯Â¿Â½o pelo
	 * usuÃ¯Â¿Â½rio da aplicaÃ¯Â¿Â½Ã¯Â¿Â½o.
	 */
	public String novo() {
		Integer codBiblioteca = this.getEmprestimoVO().getBiblioteca().getCodigo();
		removerObjetoMemoria(this);
		try {
			setPossuiBloqueioBiblioteca(false);
			setEmprestimoVO(null);
			setExemplarVO(null);
			montarListaSelectItemBiblioteca();
			getFacadeFactory().getEmprestimoFacade().inicializarDadosEmprestimoNovo(getEmprestimoVO(),
					getUsuarioLogado());
			setarFalseNosFiltros();
			limparListas();
			setTipoPessoaAluno(Boolean.TRUE);
			getEmprestimoVO().getBiblioteca().setCodigo(codBiblioteca);
			alterarBiblioteca();
			getListaConsultaMembroComunidade().clear();
			inicializarDadosFotoUsuario();
			setMensagemID("msg_entre_dados");		
			return Uteis.getCaminhoRedirecionamentoNavegacao("emprestimoForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}

	/**
	 * MÃ¯Â¿Â½todo que realiza a finalizaÃ¯Â¿Â½Ã¯Â¿Â½o do
	 * emprÃ¯Â¿Â½stimo/devoluÃ¯Â¿Â½Ã¯Â¿Â½o. Esse mÃ¯Â¿Â½todo
	 * comtempla os seguintes aspectos: Como temos uma lista de exemplares para ser
	 * manipulada, devemos transformar esses exemplares em registros de
	 * ItemEmprestimo, para poder gravar no banco.
	 */
	public void finalizar() {
		try {
			UnidadeEnsinoVO unidadeEnsinoBibliotecaVO = null;
			setSolicitarSenhaSolicitante(getConfiguracaoBibliotecaVO().getSolicitarSenhaRealizarEmprestimo());
			getEmprestimoVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(
					getEmprestimoVO().getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado()));
			if (!getSolicitarSenhaSolicitante()
					|| getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {

				if (!getConfiguracaoBibliotecaVO().getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo()) {
					for (ItemEmprestimoVO itemEmprestimoVO : getListaItensEmprestimos()) {
						itemEmprestimoVO.setBibliotecaDevolvida(getEmprestimoVO().getBiblioteca().getCodigo());
						itemEmprestimoVO.getExemplar()
								.setBibliotecaAtual(getEmprestimoVO().getBiblioteca().getCodigo());
					}
				}

				if (getEmprestimoVO().getBiblioteca().getCodigo().equals(0)) {
					throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroSelecioneBiblioteca"));
				} else {
					if (getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
						getEmprestimoVO().getBiblioteca()
								.setUnidadeEnsinoBibliotecaVOs(getFacadeFactory().getUnidadeEnsinoBibliotecaFacade()
										.consultarPorBiblioteca(getEmprestimoVO().getBiblioteca().getCodigo(), false,
												Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					}
					if(Uteis.isAtributoPreenchido(getEmprestimoVO().getUnidadeEnsinoVO().getCodigo())) {
						unidadeEnsinoBibliotecaVO = getEmprestimoVO().getUnidadeEnsinoVO();
					}else {
						unidadeEnsinoBibliotecaVO = getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs()
								.get(0).getUnidadeEnsino();
					}
				}
				if (getEmprestimoVO().isTipoPessoaAluno()) {
					ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory()
							.getConfiguracaoFinanceiroFacade()
							.consultarPorMatriculaAluno(getMatricula(), getUsuarioLogado());
					verificarExisteContaCorrenteBibliotecaConfiguracaoFinanceiro(configuracaoFinanceiroVO,
							getValorMultaCobrarBiblioteca());
					setListaMensagemSucesso(
							getFacadeFactory().getEmprestimoFacade().finalizarEmprestimoDevolucao(getEmprestimoVO(),
									getMatricula(), getListaItensEmprestimos(), getConfiguracaoBibliotecaVO(),
									configuracaoFinanceiroVO, unidadeEnsinoBibliotecaVO, getUsuarioLogado()));
				} else {
					verificarExisteContaCorrenteBibliotecaConfiguracaoFinanceiro(
							getConfiguracaoFinanceiroPadraoSistema(), getValorMultaCobrarBiblioteca());
					setListaMensagemSucesso(getFacadeFactory().getEmprestimoFacade().finalizarEmprestimoDevolucao(
							getEmprestimoVO(), getMatricula(), getListaItensEmprestimos(),
							getConfiguracaoBibliotecaVO(), getConfiguracaoFinanceiroPadraoSistema(),
							unidadeEnsinoBibliotecaVO, getUsuarioLogado()));
				}
				setPossuiBloqueioBiblioteca(false);
				montarListaSelectItemImpressora();
				consultarImpressoraPadraoUsuarioBblioteca();
				setMensagemID("msg_dados_gravados");
			} else {
				montarListaSelectItemImpressora();
				consultarImpressoraPadraoUsuarioBblioteca();
				setSenhaSolicitante("");
				limparMensagem();
				setMensagemID("");
				setMensagem("");
				setMensagemDetalhada("", "");
			}
			setMostrarModalEmprestimoDevolucaoRealizadoComSucesso(Boolean.TRUE);
			limparMotivoIsencao();

		} catch (Exception e) {
			getEmprestimoVO().setItemEmprestimoVOs(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarConfirmacaoSenhaSolicitante() throws Exception{
		try {
			getFacadeFactory().getUsuarioFacade()
					.realizarValidacaoSenhaPessoa(getEmprestimoVO().getPessoa().getCodigo(), getSenhaSolicitante());
			UnidadeEnsinoVO unidadeEnsinoBibliotecaVO = null;
			if (!getConfiguracaoBibliotecaVO().getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo()) {
				for (ItemEmprestimoVO itemEmprestimoVO : getListaItensEmprestimos()) {
					itemEmprestimoVO.setBibliotecaDevolvida(getEmprestimoVO().getBiblioteca().getCodigo());
					itemEmprestimoVO.getExemplar().setBibliotecaAtual(getEmprestimoVO().getBiblioteca().getCodigo());
				}
			}

			if (getEmprestimoVO().getBiblioteca().getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroSelecioneBiblioteca"));
			} else {
				if (getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
					getEmprestimoVO().getBiblioteca()
							.setUnidadeEnsinoBibliotecaVOs(getFacadeFactory().getUnidadeEnsinoBibliotecaFacade()
									.consultarPorBiblioteca(getEmprestimoVO().getBiblioteca().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				unidadeEnsinoBibliotecaVO = getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs().get(0)
						.getUnidadeEnsino();
			}

			setListaMensagemSucesso(getFacadeFactory().getEmprestimoFacade().finalizarEmprestimoDevolucao(
					getEmprestimoVO(), getMatricula(), getListaItensEmprestimos(), getConfiguracaoBibliotecaVO(),
					getConfiguracaoFinanceiroPadraoSistema(), unidadeEnsinoBibliotecaVO, getUsuarioLogado()));
			setSolicitarSenhaSolicitante(false);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setSolicitarSenhaSolicitante(true);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/*
	 * public boolean isWorkingDay(Date date, List<FeriadoVO> listaFeriadoVOs) { if
	 * (!Uteis.isWorkingWeekDay(date)) { return false; }
	 * 
	 * for (FeriadoVO holiday : listaFeriadoVOs) { if (holiday.isHoliday(date)) {
	 * return false; } }
	 * 
	 * return true; }
	 */

	/**
	 * MÃ¯Â¿Â½todo que com o cÃ¯Â¿Â½digo de barras informado pelo
	 * usuÃ¯Â¿Â½rio, consulta na base a situaÃ¯Â¿Â½Ã¯Â¿Â½o do
	 * exemplar, tomando aÃ¯Â¿Â½Ã¯Â¿Â½es baseadas nessa
	 * situaÃ¯Â¿Â½Ã¯Â¿Â½o. Se o exemplar estiver DISPONÃ¯Â¿Â½VEL,
	 * entÃ¯Â¿Â½o verificamos se existe alguma reserva para o
	 * catÃ¯Â¿Â½logo. Se o nÃ¯Â¿Â½mero de reservas for maior ou igual ao
	 * nÃ¯Â¿Â½mero de exemplares do mesmo catÃ¯Â¿Â½logo, e uma dessas
	 * reservas nÃ¯Â¿Â½o Ã¯Â¿Â½ do estudante em questÃ¯Â¿Â½o,
	 * entÃ¯Â¿Â½o o sistema avisa ao usuÃ¯Â¿Â½rio que os exemplares
	 * desse catÃ¯Â¿Â½logo estÃ¯Â¿Â½o reservados. Por outro lado, se o
	 * nÃ¯Â¿Â½mero de reservas for menor que o nÃ¯Â¿Â½mero de exemplares
	 * do catÃ¯Â¿Â½logo ou se essa reserva estiver no nome do estudante,
	 * entÃ¯Â¿Â½o o exemplar Ã¯Â¿Â½ adicionado para a lista de
	 * emprÃ¯Â¿Â½stimo/devoluÃ¯Â¿Â½Ã¯Â¿Â½o. E se nÃ¯Â¿Â½o
	 * existirem reservas para o catÃ¯Â¿Â½logo, entÃ¯Â¿Â½o o exemplar
	 * tambÃ¯Â¿Â½m Ã¯Â¿Â½ adicionado para a lista de
	 * emprÃ¯Â¿Â½stimo/devoluÃ¯Â¿Â½Ã¯Â¿Â½o. Agora se o exemplar
	 * estiver EMPRESTADO, entÃ¯Â¿Â½o se trata de uma
	 * devoluÃ¯Â¿Â½Ã¯Â¿Â½o ou renovaÃ¯Â¿Â½Ã¯Â¿Â½o, logo
	 * serÃ¯Â¿Â½ automaticamente adicionado na lista de
	 * emprÃ¯Â¿Â½stimo/devoluÃ¯Â¿Â½Ã¯Â¿Â½o.
	 * 
	 * @author Murillo Parreira
	 */
	public void consultarExemplarPeloCodigoBarras() {
		Integer nrReservasPorCatalogo = 0;
		try {
			if (getExemplarVO().getCodigoBarra() != null && !getExemplarVO().getCodigoBarra().trim().isEmpty()) {
				Integer codBiblioteca = getEmprestimoVO().getBiblioteca().getCodigo();
				if (getMostrarComboboxBiblioteca()
						&& getConfiguracaoBibliotecaVO().getLiberaDevolucaoExemplarOutraBiblioteca()) {
					codBiblioteca = 0;
				}
				if (Uteis.isAtributoPreenchido(getListaItensEmprestimos())) {
					setExemplarVO(getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnicoBiblioteca(
							getExemplarVO().getCodigoBarra(), codBiblioteca, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getUsuarioLogado()));
				} else {
					setExemplarVO(getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnico(
							getExemplarVO().getCodigoBarra(), codBiblioteca, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getUsuarioLogado()));
				}
								
				if(!Uteis.isAtributoPreenchido(getExemplarVO())){
					throw new Exception("C�digo de barra � inv�lido, ou n�o existente nesta biblioteca");
				}
				
				if (Uteis.isAtributoPreenchido(getListaCatalogosReservados())) {
					List<ExemplarVO> lista = getFacadeFactory().getExemplarFacade().consultarPorCatalogoDisponivel(
							getExemplarVO().getCatalogo().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
							SituacaoReservaEnum.DISPONIVEL.getKey(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getUsuarioLogado());
					Iterator<ExemplarVO> i = lista.iterator();
					while (i.hasNext()) {
						ExemplarVO ex = (ExemplarVO)i.next();
						if (ex.getCodigoBarra().equals(getExemplarVO().getCodigoBarra())) {
							Iterator o = getListaCatalogosReservados().iterator();
							while (o.hasNext()) {
								ReservaVO r = (ReservaVO)o.next();
								if(ex.getCatalogo().getCodigo().intValue() == r.getCatalogo().getCodigo()) {
									setReservaVO(r);
								}
							}
							getListaCatalogosReservados().remove(getReservaVO());			
							getExemplarVO().setExemplarSelecionadoDeUmaReserva(Boolean.TRUE);
							consultarExemplarPeloCodigoBarrasModalReserva();
							if (getExemplarVO().getCodigo() == 0) {
								throw new Exception(getMensagemDetalhada());
							}
						}
					}
				} else {				
					if (!getExemplarVO().getSituacaoAtual().equals(SituacaoExemplar.DISPONIVEL.getValor())
							&& !getExemplarVO().getSituacaoAtual().equals(SituacaoExemplar.EMPRESTADO.getValor())) {
						String situacao = getFacadeFactory().getItemRegistroSaidaAcervoFacade()
								.consultarUltimoTipoSaidaExemplar(getExemplarVO().getCodigo());
						if(situacao != null && !situacao.trim().isEmpty()){
							throw new Exception(
									"Este exemplar n�o pode ser emprestado pois o mesmo est� com a situa��o "
											+ situacao);
						}
						throw new Exception("Este exemplar n�o pode ser emprestado pois o mesmo est� com a situa��o "
								+ getExemplarVO().getSituacaoAtualApresentar());
					}
					
					if (!getExemplarVO().getTipoMidia().equals(TipoMidiaEnum.NAO_POSSUI.getKey())) {
						getExemplarVO().getCatalogo().setTitulo(UteisJSF
								.internacionalizar("msg_Biblioteca_MidiaAdicional")
								.replace("{0}", getExemplarVO().getCatalogo().getTitulo()).replace("{1}",
										TipoMidiaEnum.getEnumPorValor(getExemplarVO().getTipoMidia()).getValue()));
					}

					if (getExemplarVO().getParaConsulta() && !getExemplarVO().getEmprestarSomenteFinalDeSemana()) {
						throw new ConsistirException(
								UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarSomenteParaConsulta")
										.replace("{0}", getExemplarVO().getCodigoBarra()));
					}

					if (getExemplarVO().getEmprestarSomenteFinalDeSemana()
							&& !getExemplarVO().getSituacaoAtual().equals(SituacaoExemplar.EMPRESTADO.getValor())) {
						Integer diaDaSemana = Uteis.getDiaSemana(new Date());
						if (diaDaSemana != 6 && diaDaSemana != 7 && diaDaSemana != 1
								&& !getFacadeFactory().getFeriadoFacade().validarDataSeVesperaFimDeSemana(new Date(),
										getEmprestimoVO().getBiblioteca().getCidade().getCodigo(),
										getConfiguracaoBibliotecaVO().getConsiderarSabadoDiaUtil(),
										getConfiguracaoBibliotecaVO().getConsiderarDomingoDiaUtil(),
										ConsiderarFeriadoEnum.BIBLIOTECA)) {
							throw new ConsistirException(UteisJSF
									.internacionalizar(
											"msg_Biblioteca_ErroExemplarEmprestadoSomenteVesperaFeriadoOuFinalSemana")
									.replace("{0}", getExemplarVO().getCodigoBarra())
									.replace("{1}", getExemplarVO().getCatalogo().getTitulo()));
						}
					}

					if (getExemplarVO().getSituacaoAtual().equals(SituacaoExemplar.EMPRESTADO.getValor())) {
						PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade()
								.consultarPessoaDoEmprestimoPeloExemplar(getEmprestimoVO().getPessoa(),
										getExemplarVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
										getUsuarioLogado());
						if (getEmprestimoVO().getPessoa().getCodigo() != 0
								&& !getEmprestimoVO().getPessoa().getCodigo().equals(pessoaVO.getCodigo())) {
							throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarEmprestado"));
						}
						
						getEmprestimoVO().setPessoa(pessoaVO);
						getEmprestimoVO().setUnidadeEnsinoVO(pessoaVO.getUnidadeEnsinoVO());
						getEmprestimoVO().setBiblioteca(getExemplarVO().getBiblioteca());
						if (getEmprestimoVO().getPessoa().getTipoPessoa().equals("AL")) {
							getEmprestimoVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
							setarFalseNosFiltros();
							setTipoPessoaAluno(Boolean.TRUE);
							setMatricula(
									getFacadeFactory().getEmprestimoFacade().consultarMatriculaAlunoPorCodigoExemplar(
											getExemplarVO().getCodigo(), false, getUsuarioLogado()));
						} else if (getEmprestimoVO().getPessoa().getTipoPessoa().equals("MC")) {
							getEmprestimoVO().setTipoPessoa(TipoPessoa.MEMBRO_COMUNIDADE.getValor());
							setarFalseNosFiltros();
							setTipoPessoaMembroComunidade(Boolean.TRUE);
						} else {
							getEmprestimoVO().setTipoPessoa(TipoPessoa.PROFESSOR.getValor());
							setarFalseNosFiltros();
							setTipoPessoaProfessor(Boolean.TRUE);
							setMatriculaFuncionario(getFacadeFactory().getEmprestimoFacade()
									.consultarMatriculaFuncionarioProfessorPorCodigoExemplar(
											getExemplarVO().getCodigo(), false, getUsuarioLogado()));
						}
						montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
						montarListaSelectItemUnidadeEnsino();
					} else if (getExemplarVO().getSituacaoAtual().equals(SituacaoExemplar.DISPONIVEL.getValor())) {
						if (getEmprestimoVO().getPessoa().getCodigo() == 0) {
							throw new Exception(
									UteisJSF.internacionalizar("msg_Biblioteca_ErroInformePessoaNovoEmprestimo"));
						}				
						if (!getFacadeFactory().getEmprestimoFacade()
								.consultaExistenciaVinculoPessoComUnidadeEnsinoBiblioteca(getEmprestimoVO().getPessoa(),
										getEmprestimoVO().getTipoPessoa(),
										getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), false,
										getUsuarioLogado())) {
							String nome = getEmprestimoVO().getPessoa().getNome();
							getEmprestimoVO().setPessoa(new PessoaVO());
							setMatricula("");
							setMatriculaFuncionario("");
							throw new Exception(getEmprestimoVO().getTituloPessoaEmprestimo() + " " + nome
									+ " n�o tem vinculo com as unidades de ensino da biblioteca.");
						}
						getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(
								getEmprestimoVO().getPessoa().getCodigo(),
								getEmprestimoVO().getBiblioteca().getCodigo(), getEmprestimoVO().getTipoPessoa(),
								getUsuarioLogado());
						if(getConfiguracaoBibliotecaVO().getControlaBloqueio(getEmprestimoVO().getTipoPessoa())){
							for(ItemEmprestimoVO itemEmprestimoVO: getListaItensEmprestimos()){
								if (Uteis.isAtributoPreenchido(itemEmprestimoVO)
										&& itemEmprestimoVO.getIsEmprestimoAtrasado()) {
										TipoPessoa tipoPessoa2 = TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa());
									throw new ConsistirException("O(a)  " + tipoPessoa2.getDescricao()
											+ "(a) possui exemplar(es) em atraso, com isto o mesmo n�o poder� rea realizar um novo empr�stimos.");
									}						
							}
							}
						// nrReservasPorCatalogo =
						// getFacadeFactory().getReservaFacade().consultarReservasPorCodigoCatalogo(getExemplarVO().getCatalogo().getCodigo());
						// if (nrReservasPorCatalogo > 0) {
						// getFacadeFactory().getReservaFacade().verificarPossibilidadeEmprestimoCatalogoComBaseNoNumeroDeReservas(getExemplarVO().getCatalogo().getCodigo(),
						// getExemplarVO().getCodigo());
						// }
						getExemplarVO().setOperacao("Emprestar");
						if (!getConfiguracaoBibliotecaVO().getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo()) {
							for (ItemEmprestimoVO itemEmprestimoVO : getListaItensEmprestimos()) {
								if (itemEmprestimoVO.getExemplar().getCatalogo().getCodigo()
										.intValue() == getExemplarVO().getCatalogo().getCodigo().intValue()) {
									throw new Exception(UteisJSF.internacionalizar(
											"msg_Biblioteca_ErroAdicionarMaisExemplarMesmoCatalogo"));
								}
							}
						}
						getEmprestimoVO().setBiblioteca(getExemplarVO().getBiblioteca());
						ItemEmprestimoVO ie = new ItemEmprestimoVO();
						ie.setEmprestar(true);
						ie.setExemplar(getExemplarVO());
						inicializarDadosCidadeBiblioteca();
						getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplar(ie,
								TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
								getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), false,
								getCidadeBibliotecaVO(), getUsuarioLogado());
						getListaItensEmprestimos().add(ie);
					}
				}
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setExemplarVO(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			nrReservasPorCatalogo = null;
			setExemplarVO(new ExemplarVO());
		}
	}

	public void consultarExemplarPeloCodigoBarrasModalReserva() {
		try {
			if (!getExemplarVO().getTipoMidia().equals(TipoMidiaEnum.NAO_POSSUI.getKey())) {
				getExemplarVO().getCatalogo()
						.setTitulo(UteisJSF.internacionalizar("msg_Biblioteca_MidiaAdicional")
								.replace("{0}", getExemplarVO().getCatalogo().getTitulo()).replace("{1}",
										TipoMidiaEnum.getEnumPorValor(getExemplarVO().getTipoMidia()).getValue()));
			}

			if (getExemplarVO().getParaConsulta() && !getExemplarVO().getEmprestarSomenteFinalDeSemana()) {
				throw new ConsistirException(
						UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarSomenteParaConsulta").replace("{0}",
								getExemplarVO().getCodigoBarra()));
			}

			if (getExemplarVO().getEmprestarSomenteFinalDeSemana()) {
				Integer diaDaSemana = Uteis.getDiaSemana(new Date());
				if (diaDaSemana != 6 && diaDaSemana != 7 && diaDaSemana != 1
						&& !getFacadeFactory().getFeriadoFacade().validarDataSeVesperaFimDeSemana(new Date(),
								getEmprestimoVO().getBiblioteca().getCidade().getCodigo(),
								getConfiguracaoBibliotecaVO().getConsiderarSabadoDiaUtil(),
								getConfiguracaoBibliotecaVO().getConsiderarDomingoDiaUtil(),
								ConsiderarFeriadoEnum.BIBLIOTECA)) {
					throw new ConsistirException(UteisJSF
							.internacionalizar(
									"msg_Biblioteca_ErroExemplarEmprestadoSomenteVesperaFeriadoOuFinalSemana")
							.replace("{0}", getExemplarVO().getCodigoBarra())
							.replace("{1}", getExemplarVO().getCatalogo().getTitulo()));
				}
			}

			if (getExemplarVO().getSituacaoAtual().equals(SituacaoExemplar.EMPRESTADO.getValor())) {
				setMostrarModalCodigoBarrasReserva(Boolean.FALSE);
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarEmprestado"));
			}
			if (getExemplarVO().getCodigo() == 0) {
				setMostrarModalCodigoBarrasReserva(Boolean.FALSE);
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroCodigoBarrasExemplarInvalido"));
			}
			if (!getFacadeFactory().getEmprestimoFacade().consultaExistenciaVinculoPessoComUnidadeEnsinoBiblioteca(
					getEmprestimoVO().getPessoa(), getEmprestimoVO().getTipoPessoa(),
					getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), false, getUsuarioLogado())) {
				String nome = getEmprestimoVO().getPessoa().getNome();
				getEmprestimoVO().setPessoa(new PessoaVO());
				setMatricula("");
				setMatriculaFuncionario("");
				throw new Exception(getEmprestimoVO().getTituloPessoaEmprestimo() + " " + nome
						+ " n�o tem vinculo com as unidades de ensino da biblioteca.");
			}
			getExemplarVO().setOperacao("Emprestar");

			if (!getConfiguracaoBibliotecaVO().getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo()) {
				for (ItemEmprestimoVO itemEmprestimoVO : getListaItensEmprestimos()) {
					if (itemEmprestimoVO.getExemplar().getCatalogo().getCodigo().intValue() == getExemplarVO()
							.getCatalogo().getCodigo().intValue()) {
						throw new Exception(
								UteisJSF.internacionalizar("msg_Biblioteca_ErroAdicionarMaisExemplarMesmoCatalogo"));
					}
				}
			}
			if(Uteis.isAtributoPreenchido(getEmprestimoVO().getPessoa().getCodigo())){
				getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(
						getEmprestimoVO().getPessoa().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
						getEmprestimoVO().getTipoPessoa(), getUsuarioLogado());
				if(getConfiguracaoBibliotecaVO().getControlaBloqueio(getEmprestimoVO().getTipoPessoa())){
					for(ItemEmprestimoVO itemEmprestimoVO: getListaItensEmprestimos()){
						if (Uteis.isAtributoPreenchido(itemEmprestimoVO)
								&& itemEmprestimoVO.getIsEmprestimoAtrasado()) {
								TipoPessoa tipoPessoa2 = TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa());
							throw new ConsistirException("O(a)  " + tipoPessoa2.getDescricao()
									+ "(a) possui exemplar(es) em atraso, com isto o mesmo n�o poder� rea realizar um novo empr�stimos.");
							}						
					}
					}
			}
			ItemEmprestimoVO ie = new ItemEmprestimoVO();
			ie.setEmprestar(true);
			ie.setExemplar(getExemplarVO());
			inicializarDadosCidadeBiblioteca();
			getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplar(ie,
					TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()), getEmprestimoVO().getBiblioteca(),
					getConfiguracaoBibliotecaVO(), false, getCidadeBibliotecaVO(), getUsuarioLogado());
			getListaItensEmprestimos().add(ie);
			setMensagemID("msg_dados_consultados");
			setMensagemDetalhada("");
		} catch (Exception e) {
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			setMostrarModalCodigoBarrasReserva(Boolean.FALSE);
			setExemplarVO(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			setMostrarModalCodigoBarrasReserva(Boolean.FALSE);
			setExemplarVO(null);
		}
	}

	public void fecharModalCodigoBarrasReserva() {
		setMostrarModalCodigoBarrasReserva(Boolean.FALSE);
		setExemplarVO(null);
	}

	public void fecharModalEmprestimoRealizadoComSucesso() {
		try {
			setMostrarModalEmprestimoDevolucaoRealizadoComSucesso(Boolean.FALSE);
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada("", "");
			if (!getSolicitarSenhaSolicitante()
					|| getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {
				limparListas();
				novo();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void alterarBiblioteca() {
		try {
			if (!getEmprestimoVO().getBiblioteca().getCodigo().equals(0)) {
				getEmprestimoVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(
						getEmprestimoVO().getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado()));
			}
			montarListaSelectItemUnidadeEnsino();
			if (!getEmprestimoVO().getPessoa().isNovoObj()) {
				if (!getFacadeFactory().getEmprestimoFacade().consultaExistenciaVinculoPessoComUnidadeEnsinoBiblioteca(
						getEmprestimoVO().getPessoa(), getEmprestimoVO().getTipoPessoa(),
						getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), false, getUsuarioLogado())) {
					String nome = getEmprestimoVO().getPessoa().getNome();
					getEmprestimoVO().setPessoa(new PessoaVO());
					setMatricula("");
					setMatriculaFuncionario("");
					novo();
					throw new Exception(getEmprestimoVO().getTituloPessoaEmprestimo() + " " + nome
							+ " n�o tem vinculo com as unidades de ensino da biblioteca.");
				}
				List<ItemEmprestimoVO> itemEmprestimoVOs = new ArrayList<ItemEmprestimoVO>(0);
				for (ItemEmprestimoVO ie : getListaItensEmprestimos()) {
					if (ie.isNovoObj()) {
						itemEmprestimoVOs.add(ie);
					}
				}
				getListaItensEmprestimos().clear();
				getListaItensEmprestimos().addAll(itemEmprestimoVOs);
				getExemplarConsulta().getBiblioteca().setCodigo(getEmprestimoVO().getBiblioteca().getCodigo());
				montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			}
			inicializarDadosCidadeBiblioteca();
			limparMensagem();
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarConfiguracaoBibliotecaComBaseTipoPessoa() throws Exception {
		setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade()
				.executarObterConfiguracaoBibliotecaComBaseTipoPessoa(getEmprestimoVO().getBiblioteca().getCodigo(),
						getEmprestimoVO().getTipoPessoa(), getMatricula(), getUsuarioLogado()));
		if (getEmprestimoVO().getTipoPessoa().equals("PR")) {			
			setApresentarTipoEmprestimo(getConfiguracaoBibliotecaVO().getPermiteRealizarEmprestimoporHoraProfessor());
			if (getApresentarTipoEmprestimo()) {
				montarListaSelectItemHora(getConfiguracaoBibliotecaVO().getLimiteMaximoHorasEmprestimoProfessor());
			}
		} else if(getEmprestimoVO().getTipoPessoa().equals("FU")) {
			setApresentarTipoEmprestimo(getConfiguracaoBibliotecaVO().getPermiteRealizarEmprestimoporHoraFuncionario());
			if (getApresentarTipoEmprestimo()) {
				montarListaSelectItemHora(getConfiguracaoBibliotecaVO().getLimiteMaximoHorasEmprestimoFuncionario());
			}
		} else if(getEmprestimoVO().getTipoPessoa().equals("MC")) {
			setApresentarTipoEmprestimo(getConfiguracaoBibliotecaVO().getPermiteRealizarEmprestimoporHoraVisitante());
			if (getApresentarTipoEmprestimo()) {
				montarListaSelectItemHora(getConfiguracaoBibliotecaVO().getLimiteMaximoHorasEmprestimoVisitante());
			}
		} else {
			setApresentarTipoEmprestimo(getConfiguracaoBibliotecaVO().getPermiteRealizarEmprestimoporHoraAluno());
			if (getApresentarTipoEmprestimo()) {
				montarListaSelectItemHora(getConfiguracaoBibliotecaVO().getLimiteMaximoHorasEmprestimoAluno());
			}
		} 
	}

	public void montarListaSelectItemHora(Integer limiteHora) {
		if (limiteHora > 0) {
			getListaSelectItemHoraEmprestimo().clear();
			for (int x = 1; x <= limiteHora; x++) {
				if (x == 1) {
					getListaSelectItemHoraEmprestimo().add(new SelectItem(x, x + ""));
				} else {
					getListaSelectItemHoraEmprestimo().add(new SelectItem(x, x + ""));
				}
			}			
		}
	}
	
	/**
	 * MÃ¯Â¿Â½todo que quando o usuÃ¯Â¿Â½rio selecionar uma pessoa na
	 * tela, monta a lista de catÃ¯Â¿Â½logos reservados e a lista de
	 * exemplares emprestados para aquela pessoa.
	 * 
	 * @author Murillo Parreira
	 */
	public void montarListaCatalogosReservadosExemplaresReservadosComBasePessoa() {
		try {
			getListaCatalogosReservados().clear();
			getListaItensEmprestimos().clear();
			getEmprestimoVO().getPessoa().setNovoObj(false);
			montarConfiguracaoBibliotecaComBaseTipoPessoa();
			setListaCatalogosReservados(getFacadeFactory().getReservaFacade().consultarReservasPorCodigoPessoa(
					getEmprestimoVO().getPessoa().getCodigo(), getEmprestimoVO().getBiblioteca(),
					getConfiguracaoBibliotecaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getListaItensEmprestimos().addAll(getFacadeFactory().getItemEmprestimoFacade()
					.consultarItensEmprestadosPorCodigoPessoa(getEmprestimoVO().getPessoa().getCodigo(),
							getEmprestimoVO().getBiblioteca().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getConfiguracaoBibliotecaVO(), getUsuarioLogado()));
			setQtdeEmprestimosAtrasados(
					getFacadeFactory().getEmprestimoFacade().realizarCalculoQtdeEmprestimosAtrasados(
							getListaItensEmprestimos(), getEmprestimoVO().getPessoa().getCodigo(),
							getConfiguracaoBibliotecaVO(), getUsuarioLogado()));
			setValorMultaBiblioteca(getFacadeFactory().getContaReceberFacade()
					.consultarValorDevedorBibliotecaPorPessoa(getEmprestimoVO().getPessoa().getCodigo()));
			realizarCalculoMultaDevolucaoEmprestimo();
			realizarCalculoIsencaoDevolucaoEmprestimo();
			if(!getListaItensEmprestimos().isEmpty()) {
				getEmprestimoVO().getUnidadeEnsinoVO()
						.setCodigo(getListaItensEmprestimos().get(0).getEmprestimo().getUnidadeEnsinoVO().getCodigo());
				getEmprestimoVO().getUnidadeEnsinoVO()
						.setNome(getListaItensEmprestimos().get(0).getEmprestimo().getUnidadeEnsinoVO().getNome());
			}
			verificaUsuarioPossuiBloqueioBiblioteca();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarCobrancaoMulta() {
		ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
				.get("itemEmprestadoItens");
		itemEmprestimoVO.setIsentarCobrancaMulta(false);
		realizarCalculoMultaDevolucaoEmprestimo();
		realizarCalculoIsencaoDevolucaoEmprestimo();
	}

	public void realizarIsencaoMulta() {
		ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap().get("itemEmprestadoItens");
		itemEmprestimoVO.setIsentarCobrancaMulta(true);
		realizarCalculoMultaDevolucaoEmprestimo();
		realizarCalculoIsencaoDevolucaoEmprestimo();
		setItemEmprestimoIsencaoVO(itemEmprestimoVO);

	}

	public void realizarCalculoMultaDevolucaoEmprestimo() {
		try {
			setValorMultaACobrarBiblioteca(
					getFacadeFactory().getEmprestimoFacade().realizarCalculoMultaDevolucaoEmprestimo(
							getListaItensEmprestimos(), emprestimoVO.getPessoa().getCodigo(),
							TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()), false, getConfiguracaoBibliotecaVO(),
							getEmprestimoVO().getBiblioteca().getCidade(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarCalculoIsencaoDevolucaoEmprestimo() {
		try {
			setValorIsencaoDevolucaoBiblioteca(
					getFacadeFactory().getEmprestimoFacade().realizarCalculoIsencaoDevolucaoEmprestimo(
							getListaItensEmprestimos(), emprestimoVO.getPessoa().getCodigo(),
							TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()), getConfiguracaoBibliotecaVO(),
							getEmprestimoVO().getBiblioteca().getCidade(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFiltro() throws Exception {
		setPossuiBloqueioBiblioteca(false);
		setMatriculaFuncionario(null);
		setMatricula(null);
		getEmprestimoVO().setPessoa(null);
		getEmprestimoVO().setCodigo(0);
		getListaCatalogosReservados().clear();
		getListaItensEmprestimos().clear();
		setValorMultaACobrarBiblioteca(0.0);
		setValorMultaBiblioteca(0.0);
		setQtdeEmprestimosAtrasados(0);
		setarFalseNosFiltros();
		getListaConsultaMembroComunidade().clear();
		if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			setTipoPessoaAluno(true);
		} else if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
			setTipoPessoaProfessor(true);
		} else if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			setTipoPessoaFuncionario(true);
		} else if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {
			setTipoPessoaMembroComunidade(true);
		}
		inicializarDadosFotoUsuario();		
	}

	private void setarFalseNosFiltros() {
		setTipoPessoaAluno(false);
		setTipoPessoaProfessor(false);
		setTipoPessoaFuncionario(false);
		setTipoPessoaMembroComunidade(false);
	}

	public List<SelectItem> getTipoConsultaComboTipoPessoa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(TipoPessoa.ALUNO.getValor(), "Aluno"));
		itens.add(new SelectItem(TipoPessoa.PROFESSOR.getValor(), "Professor"));
		itens.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), "Funcion�rio"));
		itens.add(new SelectItem(TipoPessoa.MEMBRO_COMUNIDADE.getValor(), "Visitante"));
		return itens;
	}

	public void registarDevolucaoEmprestimo() {
		try {
			ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
					.get("itemEmprestadoItens");
			itemEmprestimoVO.setDevolverEmprestimo(true);
			itemEmprestimoVO.setRenovarEmprestimo(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarCatalogoReservadoListaEmprestimo() throws Exception {
		try {
			ReservaVO reserva = (ReservaVO) context().getExternalContext().getRequestMap().get("reservaCatalogoItens");
			if(Uteis.isAtributoPreenchido(getEmprestimoVO().getPessoa().getCodigo())){
				getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(
						getEmprestimoVO().getPessoa().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
						getEmprestimoVO().getTipoPessoa(), getUsuarioLogado());
			}
			if (reserva.getDataTerminoReserva() != null) {
				reserva.setSituacao(SituacaoReservaEnum.EMPRESTADO.getKey());
				setReservaVO(reserva);
				getReservaVO().getCatalogo()
						.setExemplarVOs(getFacadeFactory().getExemplarFacade().consultarPorCatalogoDisponivel(
								reserva.getCatalogo().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
								SituacaoReservaEnum.DISPONIVEL.getKey(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
								getUsuarioLogado()));
				setMostrarModalCodigoBarrasReserva(Boolean.TRUE);
			} else {
				Integer numeroReservasValidas = getFacadeFactory().getReservaFacade()
						.consultarQuantidadeDeReservasValidasPorCatalogo(reserva.getCatalogo(),
								reserva.getBibliotecaVO());
				Integer quantidadeReservaEmAberto = getFacadeFactory().getReservaFacade()
						.consultarQuantidadeReservaEmAbertoPorCatalogoPessoa(reserva.getCatalogo().getCodigo(),
								reserva.getPessoa().getCodigo(), reserva.getCodigo());
				String quantidadeReservaEmAbertoStr = quantidadeReservaEmAberto.toString();
				if (numeroReservasValidas > 0 && quantidadeReservaEmAberto == 0) {
					reserva.setSituacao(SituacaoReservaEnum.EMPRESTADO.getKey());
					setReservaVO(reserva);
					getReservaVO().getCatalogo()
							.setExemplarVOs(getFacadeFactory().getExemplarFacade().consultarPorCatalogoDisponivel(
									reserva.getCatalogo().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
									SituacaoReservaEnum.DISPONIVEL.getKey(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
									getUsuarioLogado()));
					setMostrarModalCodigoBarrasReserva(Boolean.TRUE);					
				} else if (numeroReservasValidas > 0 && quantidadeReservaEmAberto < numeroReservasValidas) {
					reserva.setSituacao(SituacaoReservaEnum.EMPRESTADO.getKey());
					setReservaVO(reserva);
					getReservaVO().getCatalogo()
							.setExemplarVOs(getFacadeFactory().getExemplarFacade().consultarPorCatalogoDisponivel(
									reserva.getCatalogo().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
									SituacaoReservaEnum.DISPONIVEL.getKey(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
									getUsuarioLogado()));
					setMostrarModalCodigoBarrasReserva(Boolean.TRUE);					
				} else {
					if (quantidadeReservaEmAbertoStr.equals("0")) {
						quantidadeReservaEmAbertoStr = "";
					}
					throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroAdicionarReservaNaoDisponivel")
							.replace("{0}", reserva.getCatalogo().getTitulo())
							.replace("{1}", quantidadeReservaEmAbertoStr));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarDadosCancelamentoReserva() {
		try {
			ReservaVO reserva = (ReservaVO) context().getExternalContext().getRequestMap().get("reservaCatalogoItens");
			reserva.setSituacao(SituacaoReservaEnum.CANCELADO.getKey());
			setReservaVO(reserva);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerCatalogoReservadoListaEmprestimo() {
		try {
			ReservaVO reserva = (ReservaVO) context().getExternalContext().getRequestMap().get("reservaCatalogoItens");
			ConfiguracaoBibliotecaVO conBibliotecaVO = null;
			getFacadeFactory().getReservaFacade().alterarSituacaoReserva(reserva,
					SituacaoReservaEnum.CANCELADO.getKey(), getUsuarioLogado());
			if (getFacadeFactory().getReservaFacade().consultarNumeroDeExemplaresDisponiveisPorCatalogo(
					reserva.getCatalogo(), reserva.getBibliotecaVO(), getConfiguracaoBibliotecaVO(), false) > 0) {
				conBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade()
						.executarObterConfiguracaoBibliotecaComBaseTipoPessoa(reserva.getBibliotecaVO().getCodigo(),
								reserva.getTipoPessoa(), reserva.getMatricula(), getUsuarioLogado());
				getFacadeFactory().getReservaFacade()
						.executarAlterarDataTerminoReservaDataReservaMaisAntigaPorCatalogoEEnviaMensagemReservaDisponivel(
								reserva.getCatalogo(), reserva, conBibliotecaVO, getUsuarioLogado());
			}
			getListaCatalogosReservados().remove(reserva);
			//getReservaVO().getCatalogo().getExemplarVOs().remove(reserva);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarExemplarPeloCodigoBarrasModalReserva() throws Exception {
		ExemplarVO exemplar = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
		setExemplarVO(getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnicoCodigoCatalogo(
				exemplar.getCodigoBarra(), exemplar.getCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				getUsuarioLogado()));
		getExemplarVO().setExemplarSelecionadoDeUmaReserva(Boolean.TRUE);
		getListaCatalogosReservados().remove(getReservaVO());
		consultarExemplarPeloCodigoBarrasModalReserva();
	}
	
	public void adicionarExemplarPeloCodigoBarrasModalExemplar() throws Exception {
		try {
			ExemplarVO exemplar = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
			setExemplarVO(getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnicoCodigoCatalogo(
					exemplar.getCodigoBarra(), exemplar.getCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado()));
			Integer numeroReservasValidas = getFacadeFactory().getReservaFacade()
					.consultarQuantidadeDeReservasValidasPorCatalogo(exemplar.getCatalogo(), exemplar.getBiblioteca());
//			Integer quantidadeReservaEmAberto = getFacadeFactory().getReservaFacade().consultarQuantidadeReservaEmAbertoPorCatalogoPessoa(exemplar.getCatalogo().getCodigo(), getEmprestimoVO().getPessoa().getCodigo());			
			if (numeroReservasValidas > 0) {
				throw new Exception(
						"Existe uma reserva para o catalogo selecionado! Selecione o mesmo utilizando o recurso de adicionar catalogo a partir do quadro de reserva!");
			}
			consultarExemplarPeloCodigoBarrasModalReserva();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void cancelarDevolucaoRenovacao() {
		try {
			ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
					.get("itemEmprestadoItens");
			itemEmprestimoVO.setDevolverEmprestimo(false);
			itemEmprestimoVO.setIsentarCobrancaMulta(false);
			itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getEmprestimo().getDataTemp());			
			realizarCobrancaoMulta();
			if (!itemEmprestimoVO.getTipoEmprestimo().equals("NO")) {
				itemEmprestimoVO.setRenovarEmprestimo(true);
				itemEmprestimoVO.setDataPrevisaoDevolucao(itemEmprestimoVO.getEmprestimo().getDataTemp());
			} else {
				itemEmprestimoVO.setRenovarEmprestimo(false);
			}
			if (itemEmprestimoVO.getRenovarEmprestimo()) {
				if (!itemEmprestimoVO.getTipoEmprestimo().equals("NO")) {
					itemEmprestimoVO.getEmprestimo().setDataTemp(itemEmprestimoVO.getEmprestimo().getData());
					itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getDataPrevisaoDevolucao());
					getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplarHora(
							itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true,
							getCidadeBibliotecaVO(), getUsuarioLogado());
					itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getEmprestimo().getDataTemp());
					itemEmprestimoVO.setRenovarEmprestimo(false);
				} else {
					getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplar(
							itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true,
							getCidadeBibliotecaVO(), getUsuarioLogado());
				}
			} else {
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(itemEmprestimoVO.getDataPrevisaoDevolucao());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerExemplarListaParaEmprestimo() {
		try {
			ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
					.get("itemEmprestadoItens");
			int index = 0;
			for (ItemEmprestimoVO ie : getListaItensEmprestimos()) {
				if (itemEmprestimoVO.getExemplar().getCatalogo().getCodigo().intValue() == ie.getExemplar()
						.getCatalogo().getCodigo().intValue()) {
					getListaItensEmprestimos().remove(index);
					break;
				}
				index++;
			}
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void registarRenovacaoEmprestimo() {
		try {
			ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
					.get("itemEmprestadoItens");
			itemEmprestimoVO.setRenovadoPeloSolicitante(false);
			getFacadeFactory().getItemEmprestimoFacade().registarRenovacaoEmprestimo(getEmprestimoVO().getTipoPessoa(),
					itemEmprestimoVO, getConfiguracaoBibliotecaVO(), false, getUsuarioLogado());
			if (itemEmprestimoVO.getRenovarEmprestimo()) {
				if (!itemEmprestimoVO.getTipoEmprestimo().equals("NO")) {
					itemEmprestimoVO.getEmprestimo().setDataTemp(itemEmprestimoVO.getEmprestimo().getData());
					itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getDataPrevisaoDevolucao());
					getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplarHora(
							itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true,
							getCidadeBibliotecaVO(), getUsuarioLogado());
					itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getEmprestimo().getDataTemp());

				} else {
					getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplar(
							itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true,
							getCidadeBibliotecaVO(), getUsuarioLogado());
				}
			} else {
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(itemEmprestimoVO.getDataPrevisaoDevolucao());
			}

//			apresentarDataPrevistaDevolucaoTemp();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void registarRenovacaoEmprestimoVisaoAlunoProfessorFuncionario() {
		try {
			ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
					.get("itemEmprestimoItens");
			itemEmprestimoVO.getEmprestimo()
					.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(
							itemEmprestimoVO.getEmprestimo().getBiblioteca().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade()
					.executarObterConfiguracaoBibliotecaComBaseTipoPessoa(
							itemEmprestimoVO.getEmprestimo().getBiblioteca().getCodigo(),
							itemEmprestimoVO.getEmprestimo().getTipoPessoa(),
							itemEmprestimoVO.getEmprestimo().getMatricula().getMatricula(), getUsuarioLogado()));

			if (itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRenovarExemplarEmprestimoEspecial")
						.replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo())
						.replace("{1}", itemEmprestimoVO.getExemplar().getCodigoBarra()));
			}

			if (getUsuarioLogado().getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
				if (getConfiguracaoBibliotecaVO().getQuantidadeRenovacaoPermitidaVisaoAluno().equals(0)) {
					throw new Exception(
							"N�o � poss�vel renovar o exemplar. Por favor, entre em contato com a Biblioteca.");
				}
				if (itemEmprestimoVO.getDataPrevisaoDevolucao().before(Uteis.getDateSemHora(new Date()))) {
					if (!getPermiteRenovarExemplarEmAtrasoVisaoAluno()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRenovarExemplarEmAtraso"));
					}
				}

				if (!getConfiguracaoBibliotecaVO().getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno()
						.equals(0)) {
					if (Uteis.nrDiasEntreDatas(itemEmprestimoVO.getDataPrevisaoDevolucao(),
							new Date()) > getConfiguracaoBibliotecaVO()
									.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno()) {
						throw new Exception(UteisJSF
								.internacionalizar("msg_Biblioteca_ErroQuantidadeDiasFaltandoRenovarExemplar")
								.replace("{0}", getConfiguracaoBibliotecaVO()
										.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno().toString())
								.replace("{1}", Uteis.getDataAplicandoFormatacao(
										itemEmprestimoVO.getDataPrevisaoDevolucao(), "dd/MM/yyyy")));
					}
				}

			} else if (getUsuarioLogado().getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
				if (getConfiguracaoBibliotecaVO().getQuantidadeRenovacaoPermitidaVisaoProfessor().equals(0)) {
					throw new Exception(
							"N�o � poss�vel renovar o exemplar. Por favor, entre em contato com a Biblioteca.");
				}
				if (itemEmprestimoVO.getDataPrevisaoDevolucao().before(Uteis.getDateSemHora(new Date()))) {
					if (!getPermiteRenovarExemplarEmAtrasoVisaoProfessor()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRenovarExemplarEmAtraso"));
					}
				}

				if (!getConfiguracaoBibliotecaVO()
						.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor().equals(0)) {
					if (Uteis.nrDiasEntreDatas(itemEmprestimoVO.getDataPrevisaoDevolucao(),
							new Date()) > getConfiguracaoBibliotecaVO()
									.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor()) {
						throw new Exception(
								UteisJSF.internacionalizar("msg_Biblioteca_ErroQuantidadeDiasFaltandoRenovarExemplar")
										.replace("{0}", getConfiguracaoBibliotecaVO()
												.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor()
												.toString())
										.replace("{1}", Uteis.getDataAplicandoFormatacao(
												itemEmprestimoVO.getDataPrevisaoDevolucao(), "dd/MM/yyyy")));
					}
				}
			} else if (getUsuarioLogado().getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
				if (getConfiguracaoBibliotecaVO().getQuantidadeRenovacaoPermitidaVisaoCoordenador().equals(0)) {
					throw new Exception(
							"N�o � poss�vel renovar o exemplar. Por favor, entre em contato com a Biblioteca.");
				}
				if (itemEmprestimoVO.getDataPrevisaoDevolucao().before(Uteis.getDateSemHora(new Date()))) {
					if (!getPermiteRenovarExemplarEmAtrasoVisaoCoordenador()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRenovarExemplarEmAtraso"));
					}
				}

				if (!getConfiguracaoBibliotecaVO()
						.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador().equals(0)) {
					if (Uteis.nrDiasEntreDatas(itemEmprestimoVO.getDataPrevisaoDevolucao(),
							new Date()) > getConfiguracaoBibliotecaVO()
									.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador()) {
						throw new Exception(UteisJSF
								.internacionalizar("msg_Biblioteca_ErroQuantidadeDiasFaltandoRenovarExemplar")
								.replace("{0}", getConfiguracaoBibliotecaVO()
										.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador()
										.toString())
								.replace("{1}", Uteis.getDataAplicandoFormatacao(
										itemEmprestimoVO.getDataPrevisaoDevolucao(), "dd/MM/yyyy")));
					}
				}
			}
			setEmprestimoVO(new EmprestimoVO());
			itemEmprestimoVO.setRenovadoPeloSolicitante(true);
			getListaItensEmprestimos().clear();
			String tipoPessoa = getUsuarioLogado().getIsApresentarVisaoAluno()
					|| getUsuarioLogado().getIsApresentarVisaoPais() ? TipoPessoa.ALUNO.getValor()
							: TipoPessoa.PROFESSOR.getValor();
			getFacadeFactory().getItemEmprestimoFacade().registarRenovacaoEmprestimo(tipoPessoa, itemEmprestimoVO,
					getConfiguracaoBibliotecaVO(), false, getUsuarioLogado());
//			getFacadeFactory().getItemEmprestimoFacade().registarRenovacaoEmprestimo(getEmprestimoVO().getTipoPessoa(), itemEmprestimoVO, getConfiguracaoBibliotecaVO(), false, getUsuarioLogado());
			getListaItensEmprestimos().add(itemEmprestimoVO);
			setEmprestimoVO((EmprestimoVO)itemEmprestimoVO.getEmprestimo().clone());
			getEmprestimoVO().setCodigo(0);
			getEmprestimoVO().setNovoObj(true);
			getEmprestimoVO().setData(new Date());
			apresentarDataPrevistaDevolucaoTemp();			
			finalizarRenovacaoEmprestimoVisaoAlunoProfessorFuncionario(getEmprestimoVO());
		} catch (Exception e) {		
			executarConsultaTelaBuscaEmprestimoEmAberto();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void finalizarRenovacaoEmprestimoVisaoAlunoProfessorFuncionario(EmprestimoVO emprestimoVO) throws Exception {

		setMostrarModalEmprestimoDevolucaoRealizadoComSucesso(Boolean.FALSE);
		UnidadeEnsinoVO unidadeEnsinoBibliotecaVO = null;

		if (getUsuarioLogado().getUnidadeEnsinoLogado() != null
				&& !getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo().equals(0)) {
			unidadeEnsinoBibliotecaVO = getUsuarioLogado().getUnidadeEnsinoLogado();
		} else {
			if (emprestimoVO.getBiblioteca().getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
				emprestimoVO.getBiblioteca()
						.setUnidadeEnsinoBibliotecaVOs(getFacadeFactory().getUnidadeEnsinoBibliotecaFacade()
								.consultarPorBiblioteca(emprestimoVO.getBiblioteca().getCodigo(), false,
										Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			unidadeEnsinoBibliotecaVO = emprestimoVO.getBiblioteca().getUnidadeEnsinoBibliotecaVOs().get(0)
					.getUnidadeEnsino();
		}
		setListaMensagemSucesso(getFacadeFactory().getEmprestimoFacade().finalizarEmprestimoDevolucao(emprestimoVO,
				emprestimoVO.getMatricula().getMatricula(), getListaItensEmprestimos(), getConfiguracaoBibliotecaVO(),
				getConfiguracaoFinanceiroPadraoSistema(), unidadeEnsinoBibliotecaVO, getUsuarioLogado()));
		setMensagemID("msg_exemplar_renovado");

		setMostrarModalEmprestimoDevolucaoRealizadoComSucesso(Boolean.TRUE);
		
		limparMensagem();
		setMensagemID("");
		setMensagem("");
		setMensagemDetalhada("", "");
		setListaItensEmprestimos(null);
		setListaConsulta(null);
		executarConsultaTelaBuscaEmprestimoEmAberto();
		
	}

	public String getApresentarPanelUltimaRenovacao() {
		try {
			if (getFacadeFactory().getEmprestimoFacade().isNumeroMaximoRenovacoesPessoa(getListaItensEmprestimos(),
					getConfiguracaoBibliotecaVO(), getUsuarioLogado())) {
				if (getMostrarModalEmprestimoDevolucaoRealizadoComSucesso()) {
					return "RichFaces.$('panelUltimaRenovacao').show(); RichFaces.$('panelEmprestimoRealizadoComSucesso').show()";
				} else {
					return "RichFaces.$('panelUltimaRenovacao').show()";
				}
			} else {
				if (getMostrarModalEmprestimoDevolucaoRealizadoComSucesso()) {
					return "RichFaces.$('panelEmprestimoRealizadoComSucesso').show()";
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}

	public void consultarAluno() {
		try {
			getDataModeloAluno().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaPorDataModeloUnidadeEnsinoBiblioteca(
					getDataModeloAluno(), getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), "", getUsuarioLogado()));
			getDataModeloAluno().setTotalRegistrosEncontrados(getFacadeFactory().getMatriculaFacade().consultaTotalRapidaPorDataModeloUnidadeEnsinoBiblioteca(
					getDataModeloAluno(), getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), "", getUsuarioLogado()));
			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade()
					.consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(getMatricula(),
							getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), false, "",
							getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroMatriculaNaoEncontrado")
						.replace("{0}", TipoPessoa.ALUNO.getDescricao()).replace("{1}", getMatricula()));
			}
			if(objAluno.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			getEmprestimoVO().setPessoa(objAluno.getAluno());
			getEmprestimoVO().setUnidadeEnsinoVO(objAluno.getUnidadeEnsino());
			setUnidadeEnsino(objAluno.getUnidadeEnsino());
			setCodigoFinanceiroMatricula(objAluno.getCodigoFinanceiroMatricula());
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatricula(null);
			getEmprestimoVO().setPessoa(null);
			setListaItensEmprestimos(null);
		}
	}

	public void consultarAlunoPorCPF() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade()
					.consultaRapidaPorCPFUnidadeEnsinoBiblioteca(getMatricula(),
							getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), false, "",
							getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroMatriculaNaoEncontrado")
						.replace("{0}", TipoPessoa.ALUNO.getDescricao()).replace("{1}", getMatricula()));
			}
			if(objAluno.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			getEmprestimoVO().setPessoa(objAluno.getAluno());
			getEmprestimoVO().setUnidadeEnsinoVO(objAluno.getUnidadeEnsino());
			setUnidadeEnsino(objAluno.getUnidadeEnsino());
			setCodigoFinanceiroMatricula(objAluno.getCodigoFinanceiroMatricula());
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatricula(null);
			getEmprestimoVO().setPessoa(null);
			setListaItensEmprestimos(null);
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			if(obj.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			if (getMensagemDetalhada().equals("")) {
				setMatricula(obj.getMatricula());
				getEmprestimoVO().setPessoa(obj.getAluno());
				getEmprestimoVO().setUnidadeEnsinoVO(obj.getUnidadeEnsino());
				setUnidadeEnsino(obj.getUnidadeEnsino());
				setCodigoFinanceiroMatricula(obj.getCodigoFinanceiroMatricula());
			}
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();					
			inicializarDadosFotoUsuario();			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatricula(null);
			getEmprestimoVO().setPessoa(null);
			setListaItensEmprestimos(null);
		}
	}

	public void limparMotivoIsencao() {
		
		setItemEmprestimoIsencaoVO(null);
//		itemEmprestimoIsencaoVO.setMotivoIsencao("");
	}

	public void limparCampoAluno() {
		setMatricula(null);
		getEmprestimoVO().setPessoa(null);
		limparListas();
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public void consultarMembroComunidade() {
		try {
			List objs = new ArrayList<>(0);
			if (getValorConsultaMembroComunidade().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaMembroComunidade().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorNome(
						getValorConsultaMembroComunidade(), 0, "MC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}

			if (getCampoConsultaMembroComunidade().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorCPF(
						getValorConsultaMembroComunidade(), 0, "MC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaMembroComunidade().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorRG(
						getValorConsultaMembroComunidade(), 0, "MC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaMembroComunidade(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			getListaConsultaMembroComunidade().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}

	public void cadastrarNovoAluno() throws Exception {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			session.setAttribute("iniciarPessoaEmprestimoMembroComunidade", true);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarVisitantePorCPF() {
		try {
			String campoConsulta = getEmprestimoVO().getPessoa().getCPF();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getEmprestimoVO().setPessoa(pessoa);
			getEmprestimoVO().getMatricula().setMatricula("");
			if (pessoa.getCodigo().equals(0)) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			getEmprestimoVO().getPessoa().setNome("");
			getEmprestimoVO().getPessoa().setCodigo(0);
		}
	}

	public void limparCampoMembroComunidade() {
		getEmprestimoVO().setPessoa(new PessoaVO());
		setValorConsultaMembroComunidade("");
		limparListas();
	}

	public void selecionarMembroComunidade() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("membroComunidadeItens");
			obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getEmprestimoVO().setPessoa(obj);
			getEmprestimoVO().getMatricula().setMatricula("");
			getListaConsultaMembroComunidade().clear();
			setValorConsultaMembroComunidade(null);
			setCampoConsultaMembroComunidade(null);
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			montarListaSelectItemUnidadeEnsino();
			inicializarDadosFotoUsuario();			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarProfessor() {
		try {

			getDataModeloProfessor().setListaConsulta(getFacadeFactory().getFuncionarioFacade().consultaPorDataModeloUnidadeEnsinoBiblioteca(
					getDataModeloProfessor(), TipoPessoa.FUNCIONARIO.getValor(), getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), 
					false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			getDataModeloProfessor().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalPorDataModeloUnidadeEnsinoBiblioteca(
					getDataModeloProfessor(), TipoPessoa.FUNCIONARIO.getValor(), getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), 
					false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarProfessorPorMatricula() throws Exception {
		try {
			FuncionarioVO objProfessor = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(getMatriculaFuncionario(),
							getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(),
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objProfessor.getMatricula().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroMatriculaNaoEncontrado")
						.replace("{0}", TipoPessoa.PROFESSOR.getDescricao()).replace("{1}", getMatriculaFuncionario()));
			}
			getEmprestimoVO().setPessoa(objProfessor.getPessoa());
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			inicializarDadosFotoUsuario();						
			montarListaSelectItemUnidadeEnsino();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatriculaFuncionario(null);
			getEmprestimoVO().setPessoa(null);
		}
	}

	public void consultarProfessorPorCPF() throws Exception {
		try {
			FuncionarioVO objProfessor = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorCPFUnicaUnidadeEnsinoBiblioteca(getMatriculaFuncionario(),
							getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(),
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objProfessor.getMatricula().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroMatriculaNaoEncontrado")
						.replace("{0}", TipoPessoa.PROFESSOR.getDescricao()).replace("{1}", getMatriculaFuncionario()));
			}
			getEmprestimoVO().setPessoa(objProfessor.getPessoa());
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			inicializarDadosFotoUsuario();						
			montarListaSelectItemUnidadeEnsino();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatriculaFuncionario(null);
			getEmprestimoVO().setPessoa(null);
		}
	}

	public void selecionarProfessor() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
		if (getMensagemDetalhada().equals("")) {
			setMatriculaFuncionario(obj.getMatricula());
			getEmprestimoVO().setPessoa(obj.getPessoa());

		}
		montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
		inicializarDadosFotoUsuario();		
		montarListaSelectItemUnidadeEnsino();
	}

	public void limparCampoProfessor() {
		setMatriculaFuncionario(null);
		getEmprestimoVO().setPessoa(null);
		limparListas();
		novo();
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public void consultarFuncionario() {
		try {
			
			getDataModeloFuncionario().setListaConsulta(getFacadeFactory().getFuncionarioFacade().consultaPorDataModeloUnidadeEnsinoBiblioteca(
					getDataModeloFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), 
					false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			getDataModeloFuncionario().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalPorDataModeloUnidadeEnsinoBiblioteca(
					getDataModeloFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(), 
					false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPorMatricula() throws Exception {
		try {
			FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(getMatriculaFuncionario(),
							getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(),
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objFuncionario.getMatricula().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroMatriculaNaoEncontrado")
						.replace("{0}", TipoPessoa.FUNCIONARIO.getDescricao())
						.replace("{1}", getMatriculaFuncionario()));
			}
			getEmprestimoVO().setPessoa(objFuncionario.getPessoa());
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			montarListaSelectItemUnidadeEnsino();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatriculaFuncionario(null);
			getEmprestimoVO().setPessoa(null);
		}
	}

	public void consultarFuncionarioPorCpf() throws Exception {
		try {
			FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorCPFUnicaUnidadeEnsinoBiblioteca(getMatriculaFuncionario(),
							getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs(),
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objFuncionario.getMatricula().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroMatriculaNaoEncontrado")
						.replace("{0}", TipoPessoa.FUNCIONARIO.getDescricao())
						.replace("{1}", getMatriculaFuncionario()));
			}
			getEmprestimoVO().setPessoa(objFuncionario.getPessoa());
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			montarListaSelectItemUnidadeEnsino();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setMatriculaFuncionario(null);
			getEmprestimoVO().setPessoa(null);
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		if (getMensagemDetalhada().equals("")) {
			setMatriculaFuncionario(obj.getMatricula());
			getEmprestimoVO().setPessoa(obj.getPessoa());
		}
		montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
		inicializarDadosFotoUsuario();		
		montarListaSelectItemUnidadeEnsino();
	}

	public void limparCampoFuncionario() {
		setMatriculaFuncionario(null);
		getEmprestimoVO().setPessoa(null);
		limparListas();
		novo();
	}

	public void limparListas() {
		setPossuiBloqueioBiblioteca(false);
		getListaCatalogosReservados().clear();
		getListaItensEmprestimos().clear();
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoConsulta() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("matricula", "Matr�cula"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoEmprestimo() {
		List<SelectItem> lista = new ArrayList<SelectItem>();		
		lista.add(new SelectItem("NO", "Normal"));
		lista.add(new SelectItem("HR", "Hora"));
		return lista;
	}

	public void montarListaSelectItemBiblioteca() throws Exception {
		List<BibliotecaVO> bibliotecaVOs = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(
				getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if (bibliotecaVOs.size() == 1) {
			getEmprestimoVO().setBiblioteca((BibliotecaVO)bibliotecaVOs.get(0).clone());
			getExemplarConsulta().getBiblioteca().setCodigo(bibliotecaVOs.get(0).getCodigo());
			getListaSelectItemBiblioteca()
					.addAll(UtilSelectItem.getListaSelectItem(bibliotecaVOs, "codigo", "nome", false));
			getEmprestimoVO().getBiblioteca().setCodigo((Integer) getListaSelectItemBiblioteca().get(0).getValue());
			setMostrarComboboxBiblioteca(Boolean.FALSE);
		} else if (bibliotecaVOs.size() > 1) {
			setMostrarComboboxBiblioteca(Boolean.TRUE);
			// getListaSelectItemBiblioteca().add(new SelectItem(0, "TODAS"));
			getListaSelectItemBiblioteca()
					.addAll(UtilSelectItem.getListaSelectItem(bibliotecaVOs, "codigo", "nome", false));
			getEmprestimoVO().getBiblioteca().setCodigo((Integer) getListaSelectItemBiblioteca().get(0).getValue());
			alterarBiblioteca();
		} else {
			setMostrarComboboxBiblioteca(Boolean.FALSE);
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroNaoExisteBiblioteca"));
		}
	}
	}

	/**
	 * Rotina responsÃ¯Â¿Â½vel por organizar a paginaÃ¯Â¿Â½Ã¯Â¿Â½o
	 * entre as pÃ¯Â¿Â½ginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("emprestimoCons.xhtml");
	}

	public boolean getTipoPessoaAluno() {
		if (tipoPessoaAluno == null) {
			tipoPessoaAluno = Boolean.FALSE;
		}
		return tipoPessoaAluno;
	}

	public void setTipoPessoaAluno(boolean tipoPessoaAluno) {
		this.tipoPessoaAluno = tipoPessoaAluno;
	}

	public boolean getTipoPessoaProfessor() {
		if (tipoPessoaProfessor == null) {
			tipoPessoaProfessor = Boolean.FALSE;
		}
		return tipoPessoaProfessor;
	}

	public void setTipoPessoaProfessor(boolean tipoPessoaProfessor) {
		this.tipoPessoaProfessor = tipoPessoaProfessor;
	}

	public boolean getTipoPessoaFuncionario() {
		if (tipoPessoaFuncionario == null) {
			tipoPessoaFuncionario = Boolean.FALSE;
		}
		return tipoPessoaFuncionario;
	}

	public void setTipoPessoaFuncionario(boolean tipoPessoaFuncionario) {
		this.tipoPessoaFuncionario = tipoPessoaFuncionario;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatriculaFuncionario(String matriculaFuncionario) {
		this.matriculaFuncionario = matriculaFuncionario;
	}

	public String getMatriculaFuncionario() {
		if (matriculaFuncionario == null) {
			matriculaFuncionario = "";
		}
		return matriculaFuncionario;
	}

	public EmprestimoVO getEmprestimoVO() {
		if (emprestimoVO == null) {
			emprestimoVO = new EmprestimoVO();
		}
		return emprestimoVO;
	}

	public void setEmprestimoVO(EmprestimoVO emprestimoVO) {
		this.emprestimoVO = emprestimoVO;
	}

	public List<ReservaVO> getListaCatalogosReservados() {
		if (listaCatalogosReservados == null) {
			listaCatalogosReservados = new ArrayList<ReservaVO>(0);
		}
		return listaCatalogosReservados;
	}

	public void setListaCatalogosReservados(List<ReservaVO> listaCatalogosReservados) {
		this.listaCatalogosReservados = listaCatalogosReservados;
	}

	// public List<ItemEmprestimoVO> getListaItensEmprestados() {
	// if (listaItensEmprestados == null) {
	// listaItensEmprestados = new ArrayList<ItemEmprestimoVO>(0);
	// }
	// return listaItensEmprestados;
	// }
	//
	// public void setListaItensEmprestados(List<ItemEmprestimoVO>
	// listaItensEmprestados) {
	// this.listaItensEmprestados = listaItensEmprestados;
	// }
	public void setExemplarVO(ExemplarVO exemplarVO) {
		this.exemplarVO = exemplarVO;
	}

	public ExemplarVO getExemplarVO() {
		if (exemplarVO == null) {
			exemplarVO = new ExemplarVO();
		}
		return exemplarVO;
	}

	public Boolean getMostrarCheckboxRenovacao() {
		ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
				.get("itemEmprestimoParaDevolucaoRenovacao");
		if (itemEmprestimoVO.getExemplar().getOperacao().equals("Devolver")) {
			return true;
		} else {
			return false;
		}
	}

	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

	public List<SelectItem> getListaSelectItemBiblioteca() {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList<>(0);
		}
		return listaSelectItemBiblioteca;
	}

	public Boolean getMostrarComboboxBiblioteca() {
		if (mostrarComboboxBiblioteca == null) {
			mostrarComboboxBiblioteca = Boolean.FALSE;
		}
		return mostrarComboboxBiblioteca;
	}

	public void setMostrarComboboxBiblioteca(Boolean mostrarComboboxBiblioteca) {
		this.mostrarComboboxBiblioteca = mostrarComboboxBiblioteca;
	}

	public ReservaVO getReservaVO() {
		if (reservaVO == null) {
			reservaVO = new ReservaVO();
		}
		return reservaVO;
	}

	public void setReservaVO(ReservaVO reservaVO) {
		this.reservaVO = reservaVO;
	}

	public Boolean getMostrarModalCodigoBarrasReserva() {
		if (mostrarModalCodigoBarrasReserva == null) {
			mostrarModalCodigoBarrasReserva = Boolean.FALSE;
		}
		return mostrarModalCodigoBarrasReserva;
	}

	public void setMostrarModalCodigoBarrasReserva(Boolean mostrarModalCodigoBarrasReserva) {
		this.mostrarModalCodigoBarrasReserva = mostrarModalCodigoBarrasReserva;
	}

	public void setMensagemEmprestimoRealizadoComSucesso(String mensagemEmprestimoRealizadoComSucesso) {
		this.mensagemEmprestimoRealizadoComSucesso = mensagemEmprestimoRealizadoComSucesso;
	}

	public String getMensagemEmprestimoRealizadoComSucesso() {
		if (mensagemEmprestimoRealizadoComSucesso == null) {
			mensagemEmprestimoRealizadoComSucesso = "";
		}
		return mensagemEmprestimoRealizadoComSucesso;
	}

	public void setMostrarModalEmprestimoDevolucaoRealizadoComSucesso(
			Boolean mostrarModalEmprestimoDevolucaoRealizadoComSucesso) {
		this.mostrarModalEmprestimoDevolucaoRealizadoComSucesso = mostrarModalEmprestimoDevolucaoRealizadoComSucesso;
	}

	public Boolean getMostrarModalEmprestimoDevolucaoRealizadoComSucesso() {
		if (mostrarModalEmprestimoDevolucaoRealizadoComSucesso == null) {
			mostrarModalEmprestimoDevolucaoRealizadoComSucesso = Boolean.FALSE;
		}
		return mostrarModalEmprestimoDevolucaoRealizadoComSucesso;
	}

	public String executarConsultaTelaBuscaEmprestimoEmAberto() {
		try {
			if(!getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				setListaConsulta(getFacadeFactory().getItemEmprestimoFacade()
						.consultarItemEmprestimoVisaoAlunoProfessor(getControleConsulta().getValorConsulta(),
								isApenasEmprestimosEmAberto(), false, getUsuarioLogado()));
				setListaCatalogosReservados(getFacadeFactory().getReservaFacade().consultarReservasPorCodigoPessoa(
						getUsuarioLogado().getPessoa().getCodigo(), null, getConfiguracaoBibliotecaVO(),
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				if (!getListaConsulta().isEmpty()) {
					getFacadeFactory().getEmprestimoFacade().realizarCalculoMultaDevolucaoEmprestimo(getListaConsulta(),
							emprestimoVO.getPessoa().getCodigo(), TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							false, getConfiguracaoBibliotecaVO(), getEmprestimoVO().getBiblioteca().getCidade(),
							getUsuarioLogado());
				}
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public boolean isApenasEmprestimosEmAberto() {
		return apenasEmprestimosEmAberto;
	}

	public void setApenasEmprestimosEmAberto(boolean apenasEmprestimosEmAberto) {
		this.apenasEmprestimosEmAberto = apenasEmprestimosEmAberto;
	}

	public boolean getIsApresentarColunaDataDevolucao() {
		return !isApenasEmprestimosEmAberto();
	}

	public String getDataDevolucao_Apresentar() {
		ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
				.get("itemEmprestimoItens");
		if (itemEmprestimoVO != null && itemEmprestimoVO.getSituacao().equals("EX")) {
			return "";
		}
		return Uteis.isAtributoPreenchido(itemEmprestimoVO) ? itemEmprestimoVO.getDataDevolucao_Apresentar() : "";
	}
	
	private Boolean impressaoPDF;
	
	public Boolean getImpressaoPDF() {
		if(impressaoPDF == null){
			impressaoPDF = false;
		}
		return impressaoPDF;
	}

	public void setImpressaoPDF(Boolean impressaoPDF) {
		this.impressaoPDF = impressaoPDF;
	}

	public void imprimirTicketPDF() {
		try {
			getEmprestimoVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(
					getEmprestimoVO().getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado()));
			List<TicketRelVO> listaEmprestimo = getFacadeFactory().getTicketRel().criarObjeto(getEmprestimoVO(),
					getListaItensEmprestimos(), getConfiguracaoBibliotecaVO().getTextoPadraoEmprestimo(),
					getConfiguracaoBibliotecaVO().getTextoPadraoDevolucao(), getConfiguracaoBibliotecaVO().getTextoPadraoUltimaRenovacao(),
					getMatricula(), getMatriculaFuncionario());
			if (getEmprestimoVO().getUnidadeEnsinoVO().getCodigo().equals(0)) {
				getSuperParametroRelVO().setUnidadeEnsino("");
			} else {
				getSuperParametroRelVO().setUnidadeEnsino(getEmprestimoVO().getUnidadeEnsinoVO().getNome());
			}
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			if(getImpressaoPDF()){
				getSuperParametroRelVO()
						.setNomeDesignIreport(getFacadeFactory().getTicketRel().designerTicketEmprestimo());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTicketRel().caminhoBaseRelatorio());
	
				if (!listaEmprestimo.get(0).getItemEmprestimoVOs().isEmpty()) {
					getSuperParametroRelVO().setTituloRelatorio("TICKET EMPRESTIMO");
				} else if (!listaEmprestimo.get(0).getItemEmprestimoVOsDevolucao().isEmpty()) {
					getSuperParametroRelVO().setTituloRelatorio("TICKET DEVOLUCAO");
				} else {
					getSuperParametroRelVO().setTituloRelatorio("TICKET RENOVACAO");
				}
				getSuperParametroRelVO().setListaObjetos(listaEmprestimo);
				getSuperParametroRelVO()
						.setCaminhoBaseRelatorio(getFacadeFactory().getTicketRel().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
	
				if (!listaEmprestimo.get(0).getItemEmprestimoVOs().isEmpty() || !listaEmprestimo.get(0).getItemEmprestimoVOsRenovacao().isEmpty()) {
					getSuperParametroRelVO().setEmprestimoRenovacao(true);
					if (!listaEmprestimo.get(0).getItemEmprestimoVOsRenovacao().isEmpty()) {
						getSuperParametroRelVO().adicionarParametro("renovacao", getFacadeFactory().getEmprestimoFacade().isNumeroMaximoRenovacoesPessoa(listaEmprestimo.get(0).getItemEmprestimoVOsRenovacao(), getConfiguracaoBibliotecaVO(), getUsuarioLogado()));						
					}
				} else {
					getSuperParametroRelVO().setEmprestimoRenovacao(false);
				}
				if (!listaEmprestimo.get(0).getItemEmprestimoVOsDevolucao().isEmpty()) {
					getSuperParametroRelVO().setDevolucao(true);
				} else {
					getSuperParametroRelVO().setDevolucao(false);
				}
				getSuperParametroRelVO().setCurso("TODOS");
				getSuperParametroRelVO().setTipoRelatorio("");
				realizarImpressaoRelatorio();
			}else{
				setTextoComprovante(
						getFacadeFactory().getEmprestimoFacade().realizarCriacaoComprovanteEmprestimo(listaEmprestimo,
								getSuperParametroRelVO().getUnidadeEnsino(), getEmprestimoVO(), getUsuarioLogado()));
				if (getEmprestimoVO().getBiblioteca().getTipoImpressaoComprovanteBiblioteca()
						.equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)) {
					registrarImpressoraPadraoUsuarioBblioteca();
				}
			}

			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String editar() {
		try {
			EmprestimoVO emprestimo = (EmprestimoVO) context().getExternalContext().getRequestMap()
					.get("emprestimoItens");
			setEmprestimoVO(emprestimo);
			if (!getEmprestimoVO().getBiblioteca().getConfiguracaoBiblioteca().getCodigo().equals(0)) {
				setConfiguracaoBibliotecaVO(
						getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorChavePrimaria(
								getEmprestimoVO().getBiblioteca().getConfiguracaoBiblioteca().getCodigo(),
								Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
				setTipoPessoaAluno(true);
				setMatricula(getEmprestimoVO().getMatricula().getMatricula());
				setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade()
						.consultarConfiguracaoBibliotecaPorBibliotecaUnidadeEnsinoENivelEducacional(
								getEmprestimoVO().getBiblioteca().getCodigo(),
								getEmprestimoVO().getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
								getUsuarioLogado()));
			} else if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
				setTipoPessoaProfessor(true);
				setMatriculaFuncionario(
						getFacadeFactory().getEmprestimoFacade().consultarMatriculaFuncionarioProfessorPorCodigoPessoa(
								getEmprestimoVO().getPessoa().getCodigo(), false, getUsuarioLogado()));
			} else if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
				setTipoPessoaFuncionario(true);
				setMatriculaFuncionario(
						getFacadeFactory().getEmprestimoFacade().consultarMatriculaFuncionarioProfessorPorCodigoPessoa(
								getEmprestimoVO().getPessoa().getCodigo(), false, getUsuarioLogado()));
			}
			montarListaCatalogosReservadosExemplaresReservadosComBasePessoa();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("emprestimoForm.xhtml");
	}

	public Boolean getPermiteRenovarExemplarEmAtrasoVisaoAluno() {
		return getConfiguracaoBibliotecaVO().getPermiteRenovarExemplarEmAtrasoVisaoAluno();
	}

	public Boolean getPermiteRenovarExemplarEmAtrasoVisaoProfessor() {
		return getConfiguracaoBibliotecaVO().getPermiteRenovarExemplarEmAtrasoVisaoProfessor();
	}

	public Boolean getPermiteRenovarExemplarEmAtrasoVisaoCoordenador() {
		return getConfiguracaoBibliotecaVO().getPermiteRenovarExemplarEmAtrasoVisaoCoordenador();
	}

	public List<String> getListaMensagemSucesso() {
		if (listaMensagemSucesso == null) {
			listaMensagemSucesso = new ArrayList<String>(0);
		}
		return listaMensagemSucesso;
	}

	public void setListaMensagemSucesso(List<String> listaMensagemSucesso) {
		this.listaMensagemSucesso = listaMensagemSucesso;
	}

	public List<ItemEmprestimoVO> getListaItensEmprestimos() {
		if (listaItensEmprestimos == null) {
			listaItensEmprestimos = new ArrayList<ItemEmprestimoVO>(0);
		}
		return listaItensEmprestimos;
	}

	public void setListaItensEmprestimos(List<ItemEmprestimoVO> listaItensEmprestimos) {
		this.listaItensEmprestimos = listaItensEmprestimos;
	}

	public Integer getQtdeEmprestimosAtrasados() {
		if (qtdeEmprestimosAtrasados == null) {
			qtdeEmprestimosAtrasados = 0;
		}
		return qtdeEmprestimosAtrasados;
	}

	public void setQtdeEmprestimosAtrasados(Integer qtdeEmprestimosAtrasados) {
		this.qtdeEmprestimosAtrasados = qtdeEmprestimosAtrasados;
	}

	public Double getValorMultaBiblioteca() {
		if (valorMultaBiblioteca == null) {
			valorMultaBiblioteca = 0.0;
		}
		return valorMultaBiblioteca;
	}

	public void setValorMultaBiblioteca(Double valorMultaBiblioteca) {
		this.valorMultaBiblioteca = valorMultaBiblioteca;
	}

	public ExemplarVO getExemplarConsulta() {
		if (exemplarConsulta == null) {
			exemplarConsulta = new ExemplarVO();
		}
		return exemplarConsulta;
	}

	public void setExemplarConsulta(ExemplarVO exemplarConsulta) {
		this.exemplarConsulta = exemplarConsulta;
	}

	public List<SelectItem> getListaSelectItemTipoExemplar() {
		return TipoExemplar.getListaSelectItemTipoExemplar();
	}

	public void consultarExemplar() {
		try {
			getExemplarConsulta().setBiblioteca(getEmprestimoVO().getBiblioteca());
			Integer codBiblioteca = getEmprestimoVO().getBiblioteca().getCodigo();
			getControleConsultaOtimizado().setLimitePorPagina(5);
			if (getMostrarComboboxBiblioteca()
					&& getConfiguracaoBibliotecaVO().getLiberaDevolucaoExemplarOutraBiblioteca()) {
				codBiblioteca = 0;
			}
			getControleConsultaOtimizado().setListaConsulta(
					getFacadeFactory().getExemplarFacade().consultar("DI",
							null, getExemplarConsulta().getCatalogo().getTitulo(), codBiblioteca,
							getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(),
							getControleConsultaOtimizado().getOffset(), false, getConfiguracaoBibliotecaVO(),
							getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade()
					.consultarTotalRegistro("DI", null,
							getExemplarConsulta().getCatalogo().getTitulo(), codBiblioteca,
							getUnidadeEnsinoLogado().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}

	}

	public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultarExemplar();
	}

	public void scrollListenerExemplar(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultarExemplar();
	}

	public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) {
		getDataModeloAluno().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloAluno().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

	public void scrollerListenerProfessor(DataScrollEvent dataScrollEvent) {
		getDataModeloProfessor().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloProfessor().setPage(dataScrollEvent.getPage());
		consultarProfessor();
	}

	public void scrollerListenerFuncionario(DataScrollEvent dataScrollEvent) {
		getDataModeloFuncionario().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloFuncionario().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

	public Double getValorMultaACobrarBiblioteca() {
		if (valorMultaACobrarBiblioteca == null) {
			valorMultaACobrarBiblioteca = 0.0;
		}
		return valorMultaACobrarBiblioteca;
	}

	public Double getValorMultaCobrarBiblioteca() {
		Double valor = 0.0;
		for (ItemEmprestimoVO itemEmprestimoVO : getListaItensEmprestimos()) {
			if (itemEmprestimoVO.getDevolverEmprestimo() || itemEmprestimoVO.getRenovarEmprestimo()) {
				valor += itemEmprestimoVO.getValorMulta();
			}
		}
		return valor;
	}

	public Boolean getApresentarPanelCobranca() {
		return getValorMultaACobrarBiblioteca() > 0.0 || getValorIsencaoDevolucaoBiblioteca() > 0.0;
	}

	public void setValorMultaACobrarBiblioteca(Double valorMultaACobrarBiblioteca) {
		this.valorMultaACobrarBiblioteca = valorMultaACobrarBiblioteca;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getTextoComprovante() {
		if (textoComprovante == null) {
			textoComprovante = "";
		}
		return textoComprovante;
	}

	public void setTextoComprovante(String textoComprovante) {
		this.textoComprovante = textoComprovante;
	}

	public String getSenhaSolicitante() {
		if (senhaSolicitante == null) {
			senhaSolicitante = "";
		}
		return senhaSolicitante;
	}

	public void setSenhaSolicitante(String senhaSolicitante) {
		this.senhaSolicitante = senhaSolicitante;
	}

	public Boolean getSolicitarSenhaSolicitante() {
		if (solicitarSenhaSolicitante == null) {
			solicitarSenhaSolicitante = false;
		}
		return solicitarSenhaSolicitante;
	}

	public void setSolicitarSenhaSolicitante(Boolean solicitarSenhaSolicitante) {
		this.solicitarSenhaSolicitante = solicitarSenhaSolicitante;
	}

	public Double getValorIsencaoDevolucaoBiblioteca() {
		if (valorIsencaoDevolucaoBiblioteca == null) {
			valorIsencaoDevolucaoBiblioteca = 0.0;
		}
		return valorIsencaoDevolucaoBiblioteca;
	}

	public void setValorIsencaoDevolucaoBiblioteca(Double valorIsencaoDevolucaoBiblioteca) {
		this.valorIsencaoDevolucaoBiblioteca = valorIsencaoDevolucaoBiblioteca;
	}

	/**
	 * Rotina responsavel por apresentar na lista emprestados/ novo emprestimo,
	 * emprestimoForm.jsp a data prevista de devolucao
	 * 
	 * @throws Exception
	 */
	public void apresentarDataPrevistaDevolucaoTemp() throws Exception {
		inicializarDadosCidadeBiblioteca();
		for (ItemEmprestimoVO itemEmprestimoVO : getListaItensEmprestimos()) {
			if (itemEmprestimoVO.getRenovarEmprestimo()) {
				if (!itemEmprestimoVO.getTipoEmprestimo().equals("NO")) {
					itemEmprestimoVO.getEmprestimo().setDataTemp(itemEmprestimoVO.getEmprestimo().getData());
					itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getDataPrevisaoDevolucao());				
					getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplarHora(
							itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true,
							getCidadeBibliotecaVO(), getUsuarioLogado());
					itemEmprestimoVO.getEmprestimo().setData(itemEmprestimoVO.getEmprestimo().getDataTemp());					
				} else {
					getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplar(
							itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
							getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true,
							getCidadeBibliotecaVO(), getUsuarioLogado());
				}
			} else {
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(itemEmprestimoVO.getDataPrevisaoDevolucao());
			}
		}
	}

	public Boolean getTipoPessoaMembroComunidade() {
		if (tipoPessoaMembroComunidade == null) {
			tipoPessoaMembroComunidade = false;
		}
		return tipoPessoaMembroComunidade;
	}

	public void setTipoPessoaMembroComunidade(Boolean tipoPessoaMembroComunidade) {
		this.tipoPessoaMembroComunidade = tipoPessoaMembroComunidade;
	}

	public List getListaConsultaMembroComunidade() {
		if (listaConsultaMembroComunidade == null) {
			listaConsultaMembroComunidade = new ArrayList<>(0);
		}
		return listaConsultaMembroComunidade;
	}

	public void setListaConsultaMembroComunidade(List listaConsultaMembroComunidade) {
		this.listaConsultaMembroComunidade = listaConsultaMembroComunidade;
	}

	public String getValorConsultaMembroComunidade() {
		if (valorConsultaMembroComunidade == null) {
			valorConsultaMembroComunidade = "";
		}
		return valorConsultaMembroComunidade;
	}

	public void setValorConsultaMembroComunidade(String valorConsultaMembroComunidade) {
		this.valorConsultaMembroComunidade = valorConsultaMembroComunidade;
	}

	public String getCampoConsultaMembroComunidade() {
		if (campoConsultaMembroComunidade == null) {
			campoConsultaMembroComunidade = "";
		}
		return campoConsultaMembroComunidade;
	}

	public void setCampoConsultaMembroComunidade(String campoConsultaMembroComunidade) {
		this.campoConsultaMembroComunidade = campoConsultaMembroComunidade;
	}

	public List<SelectItem> getTipoConsultaComboMembroComunidade() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	public String getMascaraConsultaMembroComunidade() {
		if (getCampoConsultaMembroComunidade().equals("CPF")) {
			return "return mascara(this.form,'formRequerente:valorConsultaRequerente','999.999.999-99',event);";
		}
		return "";
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
	
	public void changeAlterarModuloEtiqueta(ValueChangeEvent event) {
		alterarBiblioteca();
	}
	
	public String getShowFotoCrop() {
		try {
			if (getEmprestimoVO().getPessoa().getArquivoImagem().getNome() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario()+"?UID="+new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}
	
	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getEmprestimoVO().getPessoa().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			getEmprestimoVO().getPessoa().getArquivoImagem().setCpfRequerimento(getEmprestimoVO().getPessoa().getCPF());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getEmprestimoVO().getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public void cancelar() {
		try {
			getFacadeFactory().getPessoaFacade().carregarDados(getEmprestimoVO().getPessoa(), getUsuarioLogado());
			inicializarDadosFotoUsuario();
		} catch(Exception e) {			
		}
		setExibirUpload(true);
		setExibirBotao(false);
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getEmprestimoVO().getPessoa().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
			getEmprestimoVO().getPessoa().getArquivoImagem().setCpfRequerimento(getEmprestimoVO().getPessoa().getCPF());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getEmprestimoVO().getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			setRemoverFoto((Boolean) false);
			setExibirUpload(true);
			setExibirBotao(false);
			getFacadeFactory().getPessoaFacade().alterarFoto(getEmprestimoVO().getPessoa(), getUsuarioLogado(),
					getConfiguracaoGeralPadraoSistema());
			removerImagensUploadArquivoTemp();
			setOncompleteModal("RichFaces.$('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage(), Uteis.ERRO);
		}
	}	
	
	public void removerImagensUploadArquivoTemp() throws Exception {
		try {
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator
					+ getEmprestimoVO().getPessoa().getCPF();
			File arquivo = new File(arquivoExterno);
			getArquivoHelper().deleteRecursivo(arquivo);
		} catch (Exception e) {
			throw e;
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in",
				getEmprestimoVO().getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out",
				getEmprestimoVO().getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getEmprestimoVO().getPessoa().getArquivoImagem().setCpfRequerimento(getEmprestimoVO().getPessoa().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(
					getEmprestimoVO().getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(),
					PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator
					+ getEmprestimoVO().getPessoa().getCPF() + File.separator
					+ getEmprestimoVO().getPessoa().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(
					getEmprestimoVO().getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(
					getEmprestimoVO().getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getEmprestimoVO().getPessoa().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public String getUrlWebCam() {
		try {
			String url = request().getRequestURL().toString().substring(0,
					request().getRequestURL().toString().indexOf(request().getContextPath()))
					+ request().getContextPath();
			return "webcam.freeze();webcam.upload('"+ url +"/UploadServlet')";
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}

	public void inicializarDadosFotoUsuario() throws Exception {
		getEmprestimoVO().getPessoa().getArquivoImagem().setCpfRequerimento(getEmprestimoVO().getPessoa().getCPF());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
				getEmprestimoVO().getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
				getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
	}
	
	public CidadeVO getCidadeBibliotecaVO() {
		if (cidadeBibliotecaVO == null) {
			cidadeBibliotecaVO = new CidadeVO();
		}
		return cidadeBibliotecaVO;
	}

	public void setCidadeBibliotecaVO(CidadeVO cidadeBibliotecaVO) {
		this.cidadeBibliotecaVO = cidadeBibliotecaVO;
	}
	
	public void inicializarDadosCidadeBiblioteca() {
		CidadeVO obj = getFacadeFactory().getCidadeFacade()
				.consultarDadosComboBoxPorBiblioteca(getEmprestimoVO().getBiblioteca().getCodigo(), getUsuarioLogado());
		if (obj != null && !obj.getCodigo().equals(0)) {
			setCidadeBibliotecaVO(obj);
		}
	}

	public Boolean getApresentarTipoEmprestimo() {
		if (apresentarTipoEmprestimo == null) {
			apresentarTipoEmprestimo = Boolean.FALSE;
		}
		return apresentarTipoEmprestimo;
	}

	public void setApresentarTipoEmprestimo(Boolean apresentarTipoEmprestimo) {
		this.apresentarTipoEmprestimo = apresentarTipoEmprestimo;
	}

	public List<SelectItem> getListaSelectItemHoraEmprestimo() {
		if (listaSelectItemHoraEmprestimo == null) {
			listaSelectItemHoraEmprestimo = new ArrayList<>();
		}
		return listaSelectItemHoraEmprestimo;
	}

	public void setListaSelectItemHoraEmprestimo(List<SelectItem> listaSelectItemHoraEmprestimo) {
		this.listaSelectItemHoraEmprestimo = listaSelectItemHoraEmprestimo;
	}
	
	public void limparDadosConsultaExemplar(){
		getControleConsultaOtimizado().setLimitePorPagina(0);
		getControleConsultaOtimizado().setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
	}
	
	public void imprimirTicketReservaPDF() {
		try {
			if (getListaCatalogosReservados() != null && !getListaCatalogosReservados().isEmpty()) {
				String design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca"
						+ File.separator + "comprovanteReserva.jrxml");
				ConfiguracaoBibliotecaVO confBibVO = getFacadeFactory().getConfiguracaoBibliotecaFacade()
						.consultarConfiguracaoPorBiblioteca(
								getListaCatalogosReservados().get(0).getBibliotecaVO().getCodigo(), 1,
								getUsuarioLogado());
				getSuperParametroRelVO().setTituloRelatorio("COMPROVANTE DE RESERVA");
				getSuperParametroRelVO().adicionarParametro("nomeBiblioteca",
						getListaCatalogosReservados().get(0).getBibliotecaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("solicitante",
						getListaCatalogosReservados().get(0).getPessoa().getNome());
				getSuperParametroRelVO().adicionarParametro("matricula",
						getListaCatalogosReservados().get(0).getMatricula());
				getSuperParametroRelVO().adicionarParametro("textoReserva", confBibVO.getTextoPadraoReservaCatalogo());
				if (getUsuarioLogado().getVisaoLogar().equals("aluno")
						|| getUsuarioLogado().getVisaoLogar().equals("professor")) {
					getSuperParametroRelVO().adicionarParametro("assinaturaFuncionario", null);
					getSuperParametroRelVO().adicionarParametro("assinaturaSolicitante", null);
					getSuperParametroRelVO().adicionarParametro("apresentaAssinaturas", false);
				}else{
					getSuperParametroRelVO().adicionarParametro("assinaturaFuncionario",
							getUsuarioLogado().getNome_Apresentar());
					getSuperParametroRelVO().adicionarParametro("assinaturaSolicitante",
							getListaCatalogosReservados().get(0).getPessoa().getNome());
					getSuperParametroRelVO().adicionarParametro("apresentaAssinaturas", true);
				}
				getSuperParametroRelVO().setListaObjetos(getListaCatalogosReservados());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				realizarImpressaoRelatorio();

				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void imprimirTicket() {
		try {
			ConfiguracaoBibliotecaVO confBibVO = getFacadeFactory().getConfiguracaoBibliotecaFacade()
					.executarObterConfiguracaoBibliotecaComBaseTipoPessoa(
							getListaCatalogosReservados().get(0).getBibliotecaVO().getCodigo(),
							getListaCatalogosReservados().get(0).getPessoa().getTipoPessoa(), getMatricula(),
							getUsuarioLogado());
			setTextoComprovante(getFacadeFactory().getReservaFacade().gerarStringParaTicket(
					getListaCatalogosReservados(), getEmprestimoVO().getBiblioteca(),
					getEmprestimoVO().getImpressoraVO(), confBibVO, getUsuarioLogado()));
			if (getEmprestimoVO().getBiblioteca().getTipoImpressaoComprovanteBiblioteca()
					.equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)) {
				registrarImpressoraPadraoUsuarioBblioteca();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
	}
	
	public void calcularDataPrevDevolucaoHora() {
		ItemEmprestimoVO itemEmprestimoVO = (ItemEmprestimoVO) context().getExternalContext().getRequestMap()
				.get("itemEmprestadoItens");
		try {			
			if (!itemEmprestimoVO.getTipoEmprestimo().equals("NO")) {
				getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplarHora(
						itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
						getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), true, getCidadeBibliotecaVO(),
						getUsuarioLogado());
			} else {
				getFacadeFactory().getItemEmprestimoFacade().realizarCalculoDataPrevisaoDevolucaoExemplar(
						itemEmprestimoVO, TipoPessoa.getEnum(getEmprestimoVO().getTipoPessoa()),
						getEmprestimoVO().getBiblioteca(), getConfiguracaoBibliotecaVO(), false,
						getCidadeBibliotecaVO(), getUsuarioLogado());
			}
			limparMensagem();
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			itemEmprestimoVO.setTipoEmprestimo("NO");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void verificaUsuarioPossuiBloqueioBiblioteca(){
		if (Uteis.isAtributoPreenchido(getEmprestimoVO().getBiblioteca())
				&& Uteis.isAtributoPreenchido(getEmprestimoVO().getPessoa())) {
			try{
				getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(
						getEmprestimoVO().getPessoa().getCodigo(), getEmprestimoVO().getBiblioteca().getCodigo(),
						getEmprestimoVO().getTipoPessoa(), getUsuarioLogado());
				setPossuiBloqueioBiblioteca(false);
			}catch(Exception e){
				setPossuiBloqueioBiblioteca(true);
			}
		}else{
			setPossuiBloqueioBiblioteca(false);
		}			
	}

	public Boolean getPossuiBloqueioBiblioteca() {
		if(possuiBloqueioBiblioteca == null){
			possuiBloqueioBiblioteca = false;
		}	
		return possuiBloqueioBiblioteca;
	}

	public void setPossuiBloqueioBiblioteca(Boolean possuiBloqueioBiblioteca) {
		this.possuiBloqueioBiblioteca = possuiBloqueioBiblioteca;
	}

	public void montarListaSelectItemImpressora(){
		try{
			if (Uteis.isAtributoPreenchido(getEmprestimoVO().getBiblioteca()) && getEmprestimoVO().getBiblioteca()
					.getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)) {
				List<ImpressoraVO> impressoraVOs = getFacadeFactory().getImpressoraFacade().consultar(
						"codigoBiblioteca", getEmprestimoVO().getBiblioteca().getCodigo().toString(),
						getUnidadeEnsinoLogado(), false, getUsuarioLogado());
				setListaSelectItemImpressora(
						UtilSelectItem.getListaSelectItem(impressoraVOs, "codigo", "nomeImpressora", false));
				limparMensagem();
			}
		}catch(Exception e){
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void registrarImpressoraPadraoUsuarioBblioteca(){
		try {
			if (Uteis.isAtributoPreenchido(getEmprestimoVO().getBiblioteca()) && getEmprestimoVO().getBiblioteca()
					.getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
						getEmprestimoVO().getImpressoraVO().getCodigo().toString(), "Emprestimo", "ImpressoraU"
								+ getUsuarioLogado().getCodigo() + "B" + getEmprestimoVO().getBiblioteca().getCodigo(),
						getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarImpressoraPadraoUsuarioBblioteca(){
		try {
			if (Uteis.isAtributoPreenchido(getEmprestimoVO().getBiblioteca()) && getEmprestimoVO().getBiblioteca()
					.getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL)) {
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade()
						.consultarPorEntidadeCampo("Emprestimo", "ImpressoraU" + getUsuarioLogado().getCodigo() + "B"
								+ getEmprestimoVO().getBiblioteca().getCodigo(), false, getUsuarioLogado());
				if (layoutPadraoVO != null && !layoutPadraoVO.getValor().trim().isEmpty()
						&& Uteis.getIsValorNumerico(layoutPadraoVO.getValor())) {
					getEmprestimoVO().getImpressoraVO().setCodigo(Integer.valueOf(layoutPadraoVO.getValor()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemImpressora() {
		if(listaSelectItemImpressora == null){
			listaSelectItemImpressora =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemImpressora;
	}

	public void setListaSelectItemImpressora(List<SelectItem> listaSelectItemImpressora) {
		this.listaSelectItemImpressora = listaSelectItemImpressora;
	}

	public String getCodigoFinanceiroMatricula() {
		if (codigoFinanceiroMatricula == null) {
			codigoFinanceiroMatricula = "";
		}
		return codigoFinanceiroMatricula;
	}

	public void setCodigoFinanceiroMatricula(String codigoFinanceiroMatricula) {
		this.codigoFinanceiroMatricula = codigoFinanceiroMatricula;
	}
	
	private void verificarExisteContaCorrenteBibliotecaConfiguracaoFinanceiro(
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Double valorMultaASerCobrado) throws ConsistirException {
		if (valorMultaASerCobrado.intValue() == 0.0){
			return;
		}
		if (configuracaoFinanceiroVO.getContaCorrentePadraoBiblioteca() == null
				|| configuracaoFinanceiroVO.getContaCorrentePadraoBiblioteca().intValue() == 0) {
			throw new ConsistirException(
					"A configura��o financeiro n�o possui conta corrente da biblioteca cadastrada necess�ria para esta a��o!");
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		getListaSelectItemUnidadeEnsino().clear();
		if (getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {			
			if (getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
				try {
					getEmprestimoVO().getBiblioteca()
							.setUnidadeEnsinoBibliotecaVOs(getFacadeFactory().getUnidadeEnsinoBibliotecaFacade()
									.consultarPorBiblioteca(getEmprestimoVO().getBiblioteca().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			boolean existeUnidade = false;
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : getEmprestimoVO().getBiblioteca()
					.getUnidadeEnsinoBibliotecaVOs()) {
				getListaSelectItemUnidadeEnsino()
						.add(new SelectItem(unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getCodigo(),
								unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getNome()));
				if(!existeUnidade) {
					existeUnidade = getEmprestimoVO().getUnidadeEnsinoVO().getCodigo()
							.equals(unidadeEnsinoVO.getCodigo());
				}
			}
			if(!existeUnidade && ! getEmprestimoVO().getBiblioteca().getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
				getEmprestimoVO().getUnidadeEnsinoVO().setCodigo(getEmprestimoVO().getBiblioteca()
						.getUnidadeEnsinoBibliotecaVOs().get(0).getUnidadeEnsino().getCodigo());
			}
		} else if (!getEmprestimoVO().getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade()
						.consultarUnidadeEnsinoPessoaEBiblioteca(getEmprestimoVO().getPessoa().getCodigo(),
								getEmprestimoVO().getBiblioteca().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
								getUsuarioLogado());
				boolean existeUnidade = false;
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					getListaSelectItemUnidadeEnsino()
							.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
					if(!existeUnidade) {
						existeUnidade = getEmprestimoVO().getUnidadeEnsinoVO().getCodigo()
								.equals(unidadeEnsinoVO.getCodigo());
					}
				}
				if(!existeUnidade && !unidadeEnsinoVOs.isEmpty()) {
					getEmprestimoVO().getUnidadeEnsinoVO().setCodigo(unidadeEnsinoVOs.get(0).getCodigo());
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

	}

	public Boolean getApresentarMotivoIsencao() {
		if (apresentarMotivoIsencao == null) {
			apresentarMotivoIsencao = false;
		}
		return apresentarMotivoIsencao;
	}

	public void setApresentarMotivoIsencao(Boolean apresentarMotivoIsencao) {
		this.apresentarMotivoIsencao = apresentarMotivoIsencao;
	}

	public ItemEmprestimoVO getItemEmprestimoIsencaoVO() {
		if (itemEmprestimoIsencaoVO == null) {
			itemEmprestimoIsencaoVO = new ItemEmprestimoVO();
		}
		return itemEmprestimoIsencaoVO;
	}

	public void setItemEmprestimoIsencaoVO(ItemEmprestimoVO itemEmprestimoIsencaoVO) {
		this.itemEmprestimoIsencaoVO = itemEmprestimoIsencaoVO;
	}

	public DataModelo getDataModeloAluno() {
		if (dataModeloAluno == null) {
			dataModeloAluno = new DataModelo();
			dataModeloAluno.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloAluno;
	}

	public void setDataModeloAluno(DataModelo dataModeloAluno) {
		this.dataModeloAluno = dataModeloAluno;
	}

	public DataModelo getDataModeloProfessor() {
		if (dataModeloProfessor == null) {
			dataModeloProfessor = new DataModelo();
			dataModeloProfessor.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloProfessor;
	}

	public DataModelo getDataModeloFuncionario() {
		if (dataModeloFuncionario == null) {
			dataModeloFuncionario = new DataModelo();
			dataModeloFuncionario.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloFuncionario;
	}

	public void setDataModeloProfessor(DataModelo dataModeloProfessor) {
		this.dataModeloProfessor = dataModeloProfessor;
	}

	public void setDataModeloFuncionario(DataModelo dataModeloFuncionario) {
		this.dataModeloFuncionario = dataModeloFuncionario;
	}

	public String getTipoConsulta() {
		if (tipoConsulta == null) {
			tipoConsulta = "matricula";
		}
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
}
