package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoAssuntoVO;
import negocio.comuns.ead.QuestaoConteudoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("QuestaoControle")
@Scope("viewScope")
public class QuestaoControle extends SuperControle {

	/**
     * 
     */
	private static final long serialVersionUID = -5055707131665262710L;
	private QuestaoVO questaoVO;
	private QuestaoVO questaoConsultaVO;
	private OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO;
	private List<SelectItem> listaSelectItemTipoQuestao;
	private List<SelectItem> listaSelectItemSituacaoQuestaoConsulta;
	private List<SelectItem> listaSelectItemComplexidadeQuestao;
	private List<SelectItem> listaSelectItemTipoQuestaoConsulta;
	private List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta;
	protected List<SelectItem> listaSelectItemDisciplina;
	protected List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private String matriculaAluno;
	private Integer disciplinaAluno;
	private Boolean telaUsoTarefaDiscursiva;
	private Boolean telaUsoOnline;
	private Boolean telaUsoPresencial;
	private Boolean telaUsoExercicio;
	private String tipoCloneQuestao;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private List<SelectItem> listaSelectItemTurma;
	private Boolean buscarTurmasAnteriores;
	private TipoQuestaoEnum tipoQuestaoEnum;
	private NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum;
	private SituacaoQuestaoEnum situacaoQuestaoEnum;
	private TemaAssuntoVO temaAssuntoVO;
	private List<TemaAssuntoVO> listaConsultaTemaAssuntoVOs;
	private List<String> tiposCloneQuestao;
	private String assunto;
	private List<SelectItem> listaSelectItemAssunto;
	private ArquivoVO arquivoVO;
	private List<ArquivoVO> arquivoVOs;
	private DisciplinaVO disciplinaImagem; 
	private Boolean clonarComoAtiva;
	private DataModelo responsavelDataModelo;
	private List<MatriculaVO> listaMatriculaCorrigirQuestaoAnuladaVOs;
	private List<AvaliacaoOnlineVO> listaAvaliacaoOnlineQuestaoFixaVOs;
	private Boolean possuiPermissaoAnularQuestao;

	public QuestaoControle() {
		super();
	}

	@PostConstruct
	public void init() {
		String uso = "";
		if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("us") != null) {
			setTelaUsoTarefaDiscursiva(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("us").equals("ad"));
			setTelaUsoExercicio(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("us").equals("ex"));
			setTelaUsoOnline(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("us").equals("on"));
			setTelaUsoPresencial(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("us").equals("pr"));
			uso = ((HttpServletRequest)context().getExternalContext().getRequest()).getParameter("us");
			setListaSelectItemDisciplinasTurma(null);
			setListaSelectItemTurma(null);
		} else {
			setTelaUsoExercicio(true);
			setTelaUsoTarefaDiscursiva(false);
			setTelaUsoOnline(false);
			setTelaUsoPresencial(false);
			setListaSelectItemDisciplinasTurma(null);
			setListaSelectItemTurma(null);
		}
		getControleConsulta().setCampoConsulta("disciplina");
		setIdControlador(QuestaoControle.class.getSimpleName()+"TU"+uso);
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		montarListaSelectItemTurmaDisciplina();
		verificarFuncionalidadeCadastroQuestaoVisaoProfessor();
		if (!getTelaUsoExercicio()) {
			verificarPossuiPermissaoAnularQuestao();
		}
	}

	public String inicializarDadosTelaExercicio() {
		setControleConsultaOtimizado(new DataModelo());
		setQuestaoVO(null);
		setTelaUsoTarefaDiscursiva(false);
		setTelaUsoExercicio(true);
		setTelaUsoOnline(false);
		setTelaUsoPresencial(false);
		verificarFuncionalidadeCadastroQuestaoVisaoProfessor();
		return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorCons.xhtml");
	}

	public String inicializarDadosTelaOnline() {
		setControleConsultaOtimizado(new DataModelo());
		setQuestaoVO(null);
		setTelaUsoTarefaDiscursiva(false);
		setTelaUsoExercicio(false);
		setTelaUsoOnline(true);
		setTelaUsoPresencial(false);
		verificarFuncionalidadeCadastroQuestaoVisaoProfessor();
		return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorCons.xhtml");
	}

	public void verificarFuncionalidadeCadastroQuestaoVisaoProfessor() {
		try {
			if (getIsApresentarCampoDisciplina() && getUsuarioLogado().getIsApresentarVisaoProfessor())
				return;
			montarListaSelectItemDisciplinasConteudoExclusivoProfessor();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaConteudoDisciplinaSelecionado() {
		try {
			if (Uteis.isAtributoPreenchido(getQuestaoVO().getDisciplina().getCodigo())) {
				List<ConteudoVO> resultado = getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getQuestaoVO().getDisciplina().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					if ((getTelaUsoOnline() && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoSemInformarConteudoOnline()) || (getTelaUsoPresencial() && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial()) || (getTelaUsoExercicio() && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio() && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio())) {
						// if (resultado.isEmpty()) {
						// throw new
						// Exception(UteisJSF.internacionalizar("msg_Questao_cadastrePrimeiroUmConteudo"));
						// }
						for (Object object : resultado) {
							setQuestaoConteudoVO(new QuestaoConteudoVO());
							getQuestaoConteudoVO().setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(((ConteudoVO) object).getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado()));
							getQuestaoVO().getQuestaoConteudoVOs().add(getQuestaoConteudoVO());
						}
					}
				}
				setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String inicializarDadosTelaPresencial() {
		setControleConsultaOtimizado(new DataModelo());
		setQuestaoVO(null);
		setTelaUsoTarefaDiscursiva(false);
		setTelaUsoExercicio(false);
		setTelaUsoOnline(false);
		setTelaUsoPresencial(true);
		limparMensagem();
		verificarFuncionalidadeCadastroQuestaoVisaoProfessor();
		return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorCons");
	}

	public String inicializarDadosTelaAvaliacaoDiscursiva() {
		setControleConsultaOtimizado(new DataModelo());
		setQuestaoVO(null);
		setTelaUsoTarefaDiscursiva(true);
		setTelaUsoExercicio(false);
		setTelaUsoOnline(false);
		setTelaUsoPresencial(false);
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorCons");
	}

	public String editar() {
		try {
			setQuestaoVO(getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(((QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItem")).getCodigo()));
			//montarListaConteudoDisciplinaSelecionado();
			montarListaSelectItemConteudos();
			montarListaArquivosImagem();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm");	
			}
			
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoCons");
		}
	}

	public String novo() {
		try {
			setQuestaoVO(new QuestaoVO());
			setDisciplinaImagem(new DisciplinaVO());
			getListaSelectItemConteudo().clear();
//			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getQuestaoFacade().novo(getQuestaoVO(), getTelaUsoTarefaDiscursiva(), getTelaUsoExercicio(), getTelaUsoOnline(), getTelaUsoPresencial());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			montarListaArquivosImagem();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemTurmaDisciplina();
				return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm.xhtml");	
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoCons", getIdControlador());
		}
	}

	private String getIdEntidade() {
		String entidade = "Questao";
		if (getTelaUsoExercicio()) {
			entidade += "Exercicio";
		}
		if (getTelaUsoOnline()) {
			entidade += "Online";
		}
		if (getTelaUsoPresencial()) {
			entidade += "Presencial";
		}
		if (getTelaUsoTarefaDiscursiva()) {
			entidade += "AtividadeDiscursiva";
		}
		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			entidade += "Aluno";
		}
		return entidade;
	}

	public void excluir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getQuestaoFacade().excluir(getQuestaoVO(), true, getIdEntidade(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getQuestaoFacade().persistir(getQuestaoVO(), true, getIdEntidade(),  getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String clonarQuestao() {
		try {
			setQuestaoVO(getFacadeFactory().getQuestaoFacade().clonarQuestao(getQuestaoVO(), getTiposCloneQuestao(),  getTelaUsoExercicio(), getTelaUsoOnline(), getTelaUsoPresencial(), getIdEntidade(), getUsuarioLogado(), getClonarComoAtiva()));
			setAcaoModalClonar("RichFaces.$('panelClonar').hide()");
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
			if(getTelaUsoExercicio() && !getQuestaoVO().getUsoExercicio()) {
				if(getQuestaoVO().getUsoOnline()) {
					setTelaUsoOnline(true);
					setTelaUsoExercicio(false);
					if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
						return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm");	
					}
					return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
				}
				if(getQuestaoVO().getUsoPresencial()) {
					setTelaUsoPresencial(true);
					setTelaUsoExercicio(false);					
					return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
				}
			}
			if(getTelaUsoOnline() && !getQuestaoVO().getUsoOnline()) {
				if(getQuestaoVO().getUsoExercicio()) {
					setTelaUsoOnline(false);
					setTelaUsoExercicio(true);		
					if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
						return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm");	
					}
					return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
				}
				if(getQuestaoVO().getUsoPresencial()) {
					setTelaUsoOnline(false);
					setTelaUsoPresencial(true);		
					if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
						return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm");	
					}
					return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
				}
			}
			if(getTelaUsoPresencial() && !getQuestaoVO().getUsoPresencial()) {
				if(getQuestaoVO().getUsoExercicio()) {
					setTelaUsoExercicio(true);
					setTelaUsoPresencial(false);	
					if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
						return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm");	
					}
					return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
				}
				if(getQuestaoVO().getUsoOnline()) {					
					setTelaUsoOnline(true);
					setTelaUsoPresencial(false);
					if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
						return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorForm");	
					}
					return Uteis.getCaminhoRedirecionamentoNavegacao("questaoForm", getIdControlador());
				}
			}
			
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void inicializarClonagem() {
		setClonarComoAtiva(false);
		setAcaoModalClonar("");
		setMensagem("");
		setMensagemID("");
		setMensagemDetalhada("");
		setIconeMensagem("");
		setTiposCloneQuestao(new ArrayList<String>());
		setAcaoModalClonar("RichFaces.$('panelClonar').show()");
	}

	private String acaoModalClonar;

	public String getAcaoModalClonar() {
		if (acaoModalClonar == null) {
			acaoModalClonar = "";
		}
		return acaoModalClonar;
	}

	public void setAcaoModalClonar(String acaoModalClonar) {
		this.acaoModalClonar = acaoModalClonar;
	}

	private List<SelectItem> listaSelectItemUsoQuestao;

	public List<SelectItem> getListaSelectItemUsoQuestao() {
		if (listaSelectItemUsoQuestao == null) {
			listaSelectItemUsoQuestao = new ArrayList<SelectItem>(0);
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				if (loginControle.getPermissaoAcessoMenuVO().getQuestaoExercicio()) {
					listaSelectItemUsoQuestao.add(new SelectItem("ex", UteisJSF.internacionalizar("prt_Questao_usoExercicio")));
				}
				if (loginControle.getPermissaoAcessoMenuVO().getQuestaoOnline()) {
					listaSelectItemUsoQuestao.add(new SelectItem("on", UteisJSF.internacionalizar("prt_Questao_usoOnline")));
				}
//				if (loginControle.getPermissaoAcessoMenuVO().getQuestaoPresencial()) {
//					listaSelectItemUsoQuestao.add(new SelectItem("pr", UteisJSF.internacionalizar("prt_Questao_usoPresencial")));
//				}
			} else {
				if (loginControle.getPermissaoAcessoMenuVO().getQuestaoExercicio()) {
					listaSelectItemUsoQuestao.add(new SelectItem("ex", UteisJSF.internacionalizar("prt_Questao_usoExercicio")));
				}
				if (loginControle.getPermissaoAcessoMenuVO().getQuestaoOnline()) {
					listaSelectItemUsoQuestao.add(new SelectItem("on", UteisJSF.internacionalizar("prt_Questao_usoOnline")));
				}
//				if (loginControle.getPermissaoAcessoMenuVO().getQuestaoPresencial()) {
//					listaSelectItemUsoQuestao.add(new SelectItem("pr", UteisJSF.internacionalizar("prt_Questao_usoPresencial")));
//				}
				}
			}
		return listaSelectItemUsoQuestao;
	}
	
	public void setListaSelectItemUsoQuestao(List<SelectItem> listaSelectItemUsoQuestao) {
		this.listaSelectItemUsoQuestao = listaSelectItemUsoQuestao;
	}

	public void ativarQuestao() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getQuestaoFacade().ativarQuestao(getQuestaoVO(), true, getIdEntidade(),  getUsuarioLogado());
			for(QuestaoVO questaoVO : (List<QuestaoVO>)getControleConsultaOtimizado().getListaConsulta()) {
				if(questaoVO.getCodigo().equals(getQuestaoVO().getCodigo())) {
					questaoVO.setSituacaoQuestaoEnum(getQuestaoVO().getSituacaoQuestaoEnum());
					break;
				}
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarQuestao() {
		try {
			getFacadeFactory().getQuestaoFacade().inativarQuestao(getQuestaoVO(), true, getIdEntidade(),  getUsuarioLogado());
			for(QuestaoVO questaoVO : (List<QuestaoVO>)getControleConsultaOtimizado().getListaConsulta()) {
				if(questaoVO.getCodigo().equals(getQuestaoVO().getCodigo())) {
					questaoVO.setSituacaoQuestaoEnum(getQuestaoVO().getSituacaoQuestaoEnum());
					break;
				}
			}
			setMensagemID("msg_Questao_inativada", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void cancelarQuestao() {
		try {
			getFacadeFactory().getQuestaoFacade().cancelarQuestao(getQuestaoVO(), true, getIdEntidade(),  getUsuarioLogado());
			setMensagemID("msg_Questao_cancelada", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List objs = new ArrayList<>();
			if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") || getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("pais"))) {
				getQuestaoVO().setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);
				if (getQuestaoVO().getDisciplina().getCodigo() == 0) {
					throw new Exception("msg_Questao_disciplina");
				}
				if (getControleConsulta().getCampoConsulta().equals("codigo")) {
					if(getControleConsulta().getValorConsulta().isEmpty()) {
						getControleConsulta().setValorConsulta("0");
					}
					if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
						throw new Exception("Informe apenas valores numéricos para o filtro código.");
					}
					QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getControleConsulta().getValorConsulta()), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva());
					objs.add(questaoVO);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(1);
					getControleConsultaOtimizado().setListaConsulta(objs);
				} else {
					getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultar(getQuestaoConsultaVO().getEnunciado(), getTemaAssuntoVO(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), true, getIdEntidade(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), 0, null, null, null, null, false));
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro(getQuestaoConsultaVO().getEnunciado(), getTemaAssuntoVO(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), 0, null, null, null, null, getUsuarioLogado(), false));
				}
			} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline() || getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial() || getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio()) {
					if (getControleConsulta().getCampoConsulta().equals("codigo")) {
						if(getControleConsulta().getValorConsulta().isEmpty()) {
							getControleConsulta().setValorConsulta("0");
						}
						if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
							throw new Exception("Informe apenas valores numéricos para o filtro código.");
						}
						QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getControleConsulta().getValorConsulta()), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva());
						objs.add(questaoVO);
						getControleConsultaOtimizado().setTotalRegistrosEncontrados(1);
						getControleConsultaOtimizado().setListaConsulta(objs);
					} else {
						getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultar(getQuestaoConsultaVO().getEnunciado(), getTemaAssuntoVO(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), true, getIdEntidade(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), 0, null, null, null, null, false));
						getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro(getQuestaoConsultaVO().getEnunciado(), getTemaAssuntoVO(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), 0, null, null, null, null, getUsuarioLogado(), false));
					}
				} else {
					if (getControleConsulta().getCampoConsulta().equals("codigo")) {
						if(getControleConsulta().getValorConsulta().isEmpty()) {
							getControleConsulta().setValorConsulta("0");
						}
						if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
							throw new Exception("Informe apenas valores numéricos para o filtro código.");
						}
						QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimariaUsuario(Integer.parseInt(getControleConsulta().getValorConsulta()), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getUsuarioLogado());
						objs.add(questaoVO);
						getControleConsultaOtimizado().setTotalRegistrosEncontrados(1);
						getControleConsultaOtimizado().setListaConsulta(objs);
					} else {
						getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultarQuestoesPorUsuario(getQuestaoConsultaVO().getEnunciado(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), true, getIdEntidade(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), 0));
						getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistroPorUsuario(getQuestaoConsultaVO().getEnunciado(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), 0, getUsuarioLogado()));
					}
				}
			} else {
				if (getControleConsulta().getCampoConsulta().equals("codigo")) {
					if(getControleConsulta().getValorConsulta().isEmpty()) {
						getControleConsulta().setValorConsulta("0");
					}
					if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
						throw new Exception("Informe apenas valores numéricos para o filtro código.");
					}
					QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getControleConsulta().getValorConsulta()), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva());
					objs.add(questaoVO);
					getControleConsultaOtimizado().setListaConsulta(objs);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(1);
				} else {
					getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultar(getQuestaoConsultaVO().getEnunciado(), getTemaAssuntoVO(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), true, getIdEntidade(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getCodigoConteudo(), null, null, null, null, false));
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro(getQuestaoConsultaVO().getEnunciado(),getTemaAssuntoVO(), getQuestaoConsultaVO().getDisciplina().getCodigo(), getSituacaoQuestaoEnum(), getTelaUsoOnline(), getTelaUsoPresencial(), getTelaUsoExercicio(), getTelaUsoTarefaDiscursiva(), getTipoQuestaoEnum(), getNivelComplexidadeQuestaoEnum(), getCodigoConteudo(), null, null, null, null, null, false));
				}
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void paginarConsulta(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public void adicionarOpcaoRespostaQuestao() {
		try {
			getFacadeFactory().getQuestaoFacade().adicionarOpcaoRespostaQuestao(getQuestaoVO(), false, new OpcaoRespostaQuestaoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarOpcaoRespostaQuestao() {
		try {
			for (OpcaoRespostaQuestaoVO opc : getQuestaoVO().getOpcaoRespostaQuestaoVOs()) {
				opc.setEditar(false);
			}
			OpcaoRespostaQuestaoVO opc = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItem");
			if (opc == null && getOpcaoRespostaQuestaoVO().getOrdemApresentacao() != null && getOpcaoRespostaQuestaoVO().getOrdemApresentacao() > 0) {
				for (OpcaoRespostaQuestaoVO opc1 : getQuestaoVO().getOpcaoRespostaQuestaoVOs()) {
					if (opc1.getOrdemApresentacao().equals(getOpcaoRespostaQuestaoVO().getOrdemApresentacao())) {
						opc1.setEditar(true);
						getOpcaoRespostaQuestaoVO().setOrdemApresentacao(0);
						break;
					}
				}

			}
			if (opc != null) {
				opc.setEditar(true);
			}
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void finalizarEditacaoOpcaoRespostaQuestao() {
		try {
			OpcaoRespostaQuestaoVO opc = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItem");
			if (opc == null && getOpcaoRespostaQuestaoVO().getOrdemApresentacao() != null && getOpcaoRespostaQuestaoVO().getOrdemApresentacao() > 0) {
				for (OpcaoRespostaQuestaoVO opc1 : getQuestaoVO().getOpcaoRespostaQuestaoVOs()) {
					if (opc1.getOrdemApresentacao().equals(getOpcaoRespostaQuestaoVO().getOrdemApresentacao())) {
						opc1.setEditar(false);
						getOpcaoRespostaQuestaoVO().setOrdemApresentacao(0);
						break;
					}
				}

			}
			if (opc != null) {
				opc.setEditar(false);
			}
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerOpcaoRespostaQuestao() {
		try {
			OpcaoRespostaQuestaoVO opc = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItem");
			getFacadeFactory().getQuestaoFacade().removerOpcaoRespostaQuestao(getQuestaoVO(), opc);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemApresentacaoOpcaoRespostaQuestao(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof OpcaoRespostaQuestaoVO && dropEvent.getDropValue() instanceof OpcaoRespostaQuestaoVO) {
				getFacadeFactory().getQuestaoFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoVO(), (OpcaoRespostaQuestaoVO) dropEvent.getDragValue(), (OpcaoRespostaQuestaoVO) dropEvent.getDropValue());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirOpcaoRespostaQuestao() {
		try {
			OpcaoRespostaQuestaoVO opc1 = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItem");
			if (opc1.getOrdemApresentacao() > 1) {
				OpcaoRespostaQuestaoVO opc2 = getQuestaoVO().getOpcaoRespostaQuestaoVOs().get(opc1.getOrdemApresentacao() - 2);
				getFacadeFactory().getQuestaoFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerOpcaoRespostaQuestao() {
		try {
			OpcaoRespostaQuestaoVO opc1 = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItem");
			if (getQuestaoVO().getOpcaoRespostaQuestaoVOs().size() >= opc1.getOrdemApresentacao()) {
				OpcaoRespostaQuestaoVO opc2 = getQuestaoVO().getOpcaoRespostaQuestaoVOs().get(opc1.getOrdemApresentacao());
				getFacadeFactory().getQuestaoFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public QuestaoVO getQuestaoVO() {
		if (questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}

	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}

	public OpcaoRespostaQuestaoVO getOpcaoRespostaQuestaoVO() {
		if (opcaoRespostaQuestaoVO == null) {
			opcaoRespostaQuestaoVO = new OpcaoRespostaQuestaoVO();
		}
		return opcaoRespostaQuestaoVO;
	}

	public void setOpcaoRespostaQuestaoVO(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) {
		this.opcaoRespostaQuestaoVO = opcaoRespostaQuestaoVO;
	}

	public List<SelectItem> getListaSelectItemTipoQuestao() {
		if (listaSelectItemTipoQuestao == null) {
			listaSelectItemTipoQuestao = new ArrayList<SelectItem>(0);
			listaSelectItemTipoQuestao.add(new SelectItem(TipoQuestaoEnum.UNICA_ESCOLHA, TipoQuestaoEnum.UNICA_ESCOLHA.getValorApresentar()));
			listaSelectItemTipoQuestao.add(new SelectItem(TipoQuestaoEnum.MULTIPLA_ESCOLHA, TipoQuestaoEnum.MULTIPLA_ESCOLHA.getValorApresentar()));
		}
		return listaSelectItemTipoQuestao;
	}

	public void setListaSelectItemTipoQuestao(List<SelectItem> listaSelectItemTipoQuestao) {
		this.listaSelectItemTipoQuestao = listaSelectItemTipoQuestao;
	}

	public List<SelectItem> getListaSelectItemTipoQuestaoConsulta() {
		if (listaSelectItemTipoQuestaoConsulta == null) {
			listaSelectItemTipoQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem("", ""));
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(TipoQuestaoEnum.UNICA_ESCOLHA, TipoQuestaoEnum.UNICA_ESCOLHA.getValorApresentar()));
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(TipoQuestaoEnum.MULTIPLA_ESCOLHA, TipoQuestaoEnum.MULTIPLA_ESCOLHA.getValorApresentar()));
		}
		return listaSelectItemTipoQuestaoConsulta;
	}

	public void setListaSelectItemTipoQuestaoConsulta(List<SelectItem> listaSelectItemTipoQuestaoConsulta) {
		this.listaSelectItemTipoQuestaoConsulta = listaSelectItemTipoQuestaoConsulta;
	}

	public List<SelectItem> getListaSelectItemSituacaoQuestaoConsulta() {
		if (listaSelectItemSituacaoQuestaoConsulta == null) {
			listaSelectItemSituacaoQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoQuestaoConsulta = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoQuestaoEnum.class, "name", "valorApresentar", true);
			if (getTelaUsoExercicio()) {
				listaSelectItemSituacaoQuestaoConsulta.remove(4);
			}
		}
		return listaSelectItemSituacaoQuestaoConsulta;
	}

	public void setListaSelectItemSituacaoQuestaoConsulta(List<SelectItem> listaSelectItemSituacaoQuestaoConsulta) {
		this.listaSelectItemSituacaoQuestaoConsulta = listaSelectItemSituacaoQuestaoConsulta;
	}

	public List<SelectItem> getListaSelectItemComplexidadeQuestao() {
		if (listaSelectItemComplexidadeQuestao == null) {
			listaSelectItemComplexidadeQuestao = new ArrayList<SelectItem>(0);
			listaSelectItemComplexidadeQuestao.add(new SelectItem(NivelComplexidadeQuestaoEnum.FACIL, NivelComplexidadeQuestaoEnum.FACIL.getValorApresentar()));
			listaSelectItemComplexidadeQuestao.add(new SelectItem(NivelComplexidadeQuestaoEnum.MEDIO, NivelComplexidadeQuestaoEnum.MEDIO.getValorApresentar()));
			listaSelectItemComplexidadeQuestao.add(new SelectItem(NivelComplexidadeQuestaoEnum.DIFICIL, NivelComplexidadeQuestaoEnum.DIFICIL.getValorApresentar()));
		}
		return listaSelectItemComplexidadeQuestao;
	}

	public void setListaSelectItemComplexidadeQuestao(List<SelectItem> listaSelectItemComplexidadeQuestao) {
		this.listaSelectItemComplexidadeQuestao = listaSelectItemComplexidadeQuestao;
	}

	public List<SelectItem> getListaSelectItemComplexidadeQuestaoConsulta() {
		if (listaSelectItemComplexidadeQuestaoConsulta == null) {
			listaSelectItemComplexidadeQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemComplexidadeQuestaoConsulta = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelComplexidadeQuestaoEnum.class, "name", "valorApresentar", true);
		}
		return listaSelectItemComplexidadeQuestaoConsulta;
	}

	public void setListaSelectItemComplexidadeQuestaoConsulta(List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta) {
		this.listaSelectItemComplexidadeQuestaoConsulta = listaSelectItemComplexidadeQuestaoConsulta;
	}

	public void montarListaSelectItemTurmaDisciplina(){
		try {
			getListaSelectItemDisciplina().clear();
			List<DisciplinaVO> listaConsultas = null;
			if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor"))) {
				getListaSelectItemDisciplina().add(new SelectItem(0, ""));				
				if(verificarUsuarioPossuiPermissaoDisciplinasAnosAnteriores()) {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
				}else {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
				}
			} else if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
				if (getDisciplinaAluno() != null && getDisciplinaAluno() > 0) {
					listaConsultas = new ArrayList<DisciplinaVO>(0);
					listaConsultas.add(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(getDisciplinaAluno(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				} else {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultaRapidaPorMatriculaEMatriculaPeriodo(getMatriculaAluno(), getUnidadeEnsinoLogado().getCodigo(), null, false, getUsuarioLogado());
				}
			}
			if (listaConsultas != null && !listaConsultas.isEmpty()) {
				for (DisciplinaVO obj : listaConsultas) {
					if (getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor")) {
						getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getDescricaoParaCombobox()));
					} else {
						getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getNome()));
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			VisaoAlunoControle vac = (VisaoAlunoControle) getControlador("VisaoAlunoControle");
			if (vac != null) {
				matriculaAluno = vac.getMatricula().getMatricula();
			} else {
				matriculaAluno = "";
			}
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public Integer getDisciplinaAluno() {
		if (disciplinaAluno == null) {
			VisaoAlunoControle vac = (VisaoAlunoControle) getControlador("VisaoAlunoControle");
			if (vac != null) {
				disciplinaAluno = vac.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo();
			} else {
				disciplinaAluno = 0;
			}
			getQuestaoVO().getDisciplina().setCodigo(disciplinaAluno);
		}
		return disciplinaAluno;
	}

	public void setDisciplinaAluno(Integer disciplinaAluno) {
		this.disciplinaAluno = disciplinaAluno;
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(disciplina)) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDisciplina() {
		getQuestaoVO().setDisciplina(null);
		setDisciplinaImagem(null);
		montarListaArquivosImagem();
		setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
	}
	
	public void limparDisciplinaConsulta() {
		getQuestaoConsultaVO().setDisciplina(null);
		setCodigoConteudo(0);
		setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
	}

	public void selecionarDisciplina() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getQuestaoVO().setDisciplina(disciplina);
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaConteudoDisciplinaSelecionado();
			} else {
				setDisciplinaImagem(disciplina);
				montarListaSelectItemConteudos();
			}
			montarListaArquivosImagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarDisciplinaConsulta() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getQuestaoConsultaVO().setDisciplina(disciplina);			
			try {
				List<ConteudoVO> resultado = getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getQuestaoConsultaVO().getDisciplina().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
				setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
		} catch (Exception e) {
				e.printStackTrace();
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public String inicializarConsultar() {
		setQuestaoVO(new QuestaoVO());
		setControleConsultaOtimizado(new DataModelo());
		limparMensagem();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProfessorCons");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("questaoCons", getIdControlador());
	}

	public Boolean getTelaUsoTarefaDiscursiva() {
		if (telaUsoTarefaDiscursiva == null) {
			telaUsoTarefaDiscursiva = false;
		}
		return telaUsoTarefaDiscursiva;
	}

	public void setTelaUsoTarefaDiscursiva(Boolean telaUsoTarefaDiscursiva) {
		this.telaUsoTarefaDiscursiva = telaUsoTarefaDiscursiva;
	}

	public Boolean getTelaUsoOnline() {
		if (telaUsoOnline == null) {
			telaUsoOnline = false;
		}
		return telaUsoOnline;
	}

	public void setTelaUsoOnline(Boolean telaUsoOnline) {
		this.telaUsoOnline = telaUsoOnline;
	}

	public Boolean getTelaUsoPresencial() {
		if (telaUsoPresencial == null) {
			telaUsoPresencial = false;
		}
		return telaUsoPresencial;
	}

	public void setTelaUsoPresencial(Boolean telaUsoPresencial) {
		this.telaUsoPresencial = telaUsoPresencial;
	}

	public Boolean getTelaUsoExercicio() {
		if (telaUsoExercicio == null) {
			telaUsoExercicio = false;
		}
		return telaUsoExercicio;
	}

	public void setTelaUsoExercicio(Boolean telaUsoExercicio) {
		this.telaUsoExercicio = telaUsoExercicio;
	}

	public void setTipoConsultaComboDisciplina(List<SelectItem> tipoConsultaComboDisciplina) {
		this.tipoConsultaComboDisciplina = tipoConsultaComboDisciplina;
	}

	private LoginControle loginControle = (LoginControle) getControlador("LoginControle");

	/**
	 * Verifica se a questao pode ser alterada pelo usuario observando as
	 * seguintes regras: 1 - Questão pode ser nova ou, 2 - A questao tem que
	 * estar em elaboracao e, 3 - A questao deve ter sido criado pelo usuario
	 * logado ou possua permissao para alterar questao de outro usuario
	 * 
	 * @return true || false
	 */
	public Boolean getPermiteAlterarQuestao() {
		return permiteAlterarQuestaoConsulta(getQuestaoVO());
	}

	/**
	 * Verifica se a questao pode ser alterada pelo professor observando as
	 * seguintes regras: * 1 - Questão pode ser nova ou, 2 - A questao tem que
	 * estar em elaboracao e, 3 - A questao deve ter sido criado pelo usuario
	 * logado ou possua permissao para alterar questao de outro usuario 4 - Esta
	 * validacao e utilizada somente na visao do professor
	 * 
	 * @return true || false
	 */
	public Boolean getPermiteAlterarQuestaoProfessor() {
		return permiteAlterarQuestaoConsulta(getQuestaoVO());
	}

	/**
	 * Verifica se a questao pode ser alterada pelo usuario observando as
	 * seguintes regras:
	 * 
	 * 1 - A questao tem que estar em ativa e, 2 - A questao deve ter sido
	 * criado pelo usuario logado ou possua permissao para alterar questao de
	 * outro usuario e, 3 - O usuario deve ter permissao de inativar uma questao
	 * observando o seu tipo
	 * 
	 * @return true || false
	 */
	public Boolean getPossuiPermissaoInativar() {

		return getQuestaoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) && 
				( getQuestaoVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) 
				 || ((getQuestaoVO().getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoOnline()) || (getQuestaoVO().getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoPresencial()) || (getQuestaoVO().getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getInativarExercicio()) || (getQuestaoVO().getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getInativarAtividadeDiscursiva())) 
				) ;
	}

	/**
	 * Verifica se a questao pode ser alterada pelo professor observando as
	 * seguintes regras:
	 * 
	 * 1 - A questao tem que estar em ativa e, 2 - A questao deve ter sido
	 * criado pelo usuario logado ou possua permissao para alterar questao de
	 * outro usuario e, 3 - O usuario deve ter permissao de inativar uma questao
	 * observando o seu tipo e, 4 - Esta validacao e utilizada na visao do
	 * professor
	 * 
	 * @return true || false
	 */
	public Boolean getPossuiPermissaoInativarProfessor() {

		return getQuestaoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) 
				&& (getQuestaoVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || 
				((getQuestaoVO().getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoOnline()) 
						|| (getQuestaoVO().getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoPresencial()) 
						|| (getQuestaoVO().getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getInativarExercicio()) 
						|| (getQuestaoVO().getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getInativarAtividadeDiscursiva()))) ;
	}

	/**
	 * Verifica se a questao pode ser alterada pelo usuario observando as
	 * seguintes regras:
	 * 
	 * 1 - A questao tem que estar em ativa e, 2 - A questao deve ter sido
	 * criado pelo usuario logado ou possua permissao para alterar questao de
	 * outro usuario e, 3 - O usuario deve ter permissao de cancelar uma questao
	 * observando o seu tipo e, 4 - A questao deve ser do tipo online ou
	 * presencial
	 * 
	 * @return true || false
	 */
	public Boolean getPossuiPermissaoCancelarQuestao() {
		return getQuestaoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) 
				&& (getQuestaoVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) 
				|| ((getQuestaoVO().getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoOnlineOutroProfessor()) 
				|| (getQuestaoVO().getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoPresencialOutroProfessor()) 
				|| (getQuestaoVO().getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getAlterarExercicioOutroProfessor()) 
				|| (getQuestaoVO().getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getAlterarAtividadeDiscursivaOutroProfessor()))) 
				&& ((getQuestaoVO().getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getCancelarQuestaoOnline()) 
				|| (getQuestaoVO().getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getCancelarQuestaoPresencial()));
	}

	/**
	 * Verifica se a questao pode ser alterada pelo professor observando as
	 * seguintes regras:
	 * 
	 * 1 - A questao tem que estar em ativa e, 2 - A questao deve ter sido
	 * criado pelo usuario logado ou possua permissao para alterar questao de
	 * outro usuario e, 3 - O usuario deve ter permissao de cancelar uma questao
	 * observando o seu tipo e, 4 - A questao deve ser do tipo online ou
	 * presencial e, 5 - Esta validacao e utilizada na visao do professor
	 * 
	 * @return true || false
	 */
	public Boolean getPossuiPermissaoCancelarQuestaoProfessor() {
		return getQuestaoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) 
				&& (getQuestaoVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) 
				|| ((getQuestaoVO().getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoOnlineOutroProfessor()) 
				|| (getQuestaoVO().getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoPresencialOutroProfessor()) 
				|| (getQuestaoVO().getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getAlterarExercicioOutroProfessor()) 
				|| (getQuestaoVO().getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getAlterarAtividadeDiscursivaOutroProfessor()))) 
				&& ((getQuestaoVO().getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getCancelarQuestaoOnline()) 
				|| (getQuestaoVO().getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getCancelarQuestaoPresencial()));
	}

	public String getTipoCloneQuestao() {
		if (tipoCloneQuestao == null) {
			if (getTelaUsoExercicio()) {
				tipoCloneQuestao = "ex";
			}
			if (getTelaUsoOnline()) {
				tipoCloneQuestao = "on";
			}
			if (getTelaUsoPresencial()) {
				tipoCloneQuestao = "pr";
			}
			if (getTelaUsoTarefaDiscursiva()) {
				tipoCloneQuestao = "ad";
			}
		}
		return tipoCloneQuestao;
	}

	public void setTipoCloneQuestao(String tipoCloneQuestao) {
		this.tipoCloneQuestao = tipoCloneQuestao;
	}

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List<SelectItem> listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = Boolean.FALSE;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			if (getQuestaoVO().getTurmaVO().getCodigo() != 0) {
				List<SelectItem> objs = new ArrayList<SelectItem>(0);
				List<DisciplinaVO> resultado = consultarDisciplinaProfessorTurma();
				Iterator<DisciplinaVO> i = resultado.iterator();
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					DisciplinaVO obj = (DisciplinaVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
				setListaSelectItemDisciplinasTurma(objs);
			} else {
				montarListaSelectItemDisciplinasProfessor();
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getQuestaoVO().getTurmaVO().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogado());
	}

	public void montarListaSelectItemDisciplinasProfessor() throws Exception {
		setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "nome"));
	}

	public void montarListaSelectItemDisciplinasConteudoExclusivoProfessor() {
		try {
			setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade()
					.consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "descricaoParaCombobox", true, false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemTurmaProfessor() {
		List<TurmaVO> listaTurmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, "TODAS AS TURMAS"));
			for (TurmaVO turmaVO : listaTurmas) {
				if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					removerObjetoMemoria(turmaVO);
				}
			}

		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			mapAuxiliarSelectItem = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, false);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false, false);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false, false);
		}
	}

	public String novoVisaoProfessorQuestao() throws Exception {
		novo();
		montarListaSelectItemTurmaProfessor();
		montarListaSelectItemDisciplinasProfessor();
		return "editar";
	}

	/**
	 * @author Victor Hugo 08/01/2015
	 */
	private List<SelectItem> listaSelectItemConteudo;
	private QuestaoConteudoVO questaoConteudoVO;

	public List<SelectItem> getListaSelectItemConteudo() {
		if (listaSelectItemConteudo == null) {
			listaSelectItemConteudo = new ArrayList<SelectItem>();
		}
		return listaSelectItemConteudo;
	}

	public void setListaSelectItemConteudo(List<SelectItem> listaSelectItemConteudo) {
		this.listaSelectItemConteudo = listaSelectItemConteudo;
	}

	public void montarListaSelectItemConteudos() {
		try {
			List<ConteudoVO> resultado = getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getQuestaoVO().getDisciplina().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
			setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public QuestaoConteudoVO getQuestaoConteudoVO() {
		if (questaoConteudoVO == null) {
			questaoConteudoVO = new QuestaoConteudoVO();
		}
		return questaoConteudoVO;
	}

	public void setQuestaoConteudoVO(QuestaoConteudoVO questaoConteudoVO) {
		this.questaoConteudoVO = questaoConteudoVO;
	}

	public void adicionarConteudo() {
		try {
			getFacadeFactory().getQuestaoConteudoFacade().adicionarConteudo(getQuestaoVO(), getQuestaoConteudoVO(), getUsuarioLogado());
			setQuestaoConteudoVO(new QuestaoConteudoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerConteudo() {
		try {
			QuestaoConteudoVO obj = (QuestaoConteudoVO) context().getExternalContext().getRequestMap().get("conteudoItem");
			if (obj.getCodigo() != 0) {
				getFacadeFactory().getQuestaoConteudoFacade().excluir(obj, false, getUsuarioLogado());
			}
			getQuestaoVO().getQuestaoConteudoVOs().remove(obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public TipoQuestaoEnum getTipoQuestaoEnum() {
		return tipoQuestaoEnum;
	}

	public void setTipoQuestaoEnum(TipoQuestaoEnum tipoQuestaoEnum) {
		this.tipoQuestaoEnum = tipoQuestaoEnum;
	}

	public NivelComplexidadeQuestaoEnum getNivelComplexidadeQuestaoEnum() {
		return nivelComplexidadeQuestaoEnum;
	}

	public void setNivelComplexidadeQuestaoEnum(NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum) {
		this.nivelComplexidadeQuestaoEnum = nivelComplexidadeQuestaoEnum;
	}

	public SituacaoQuestaoEnum getSituacaoQuestaoEnum() {
		return situacaoQuestaoEnum;
	}

	public void setSituacaoQuestaoEnum(SituacaoQuestaoEnum situacaoQuestaoEnum) {
		this.situacaoQuestaoEnum = situacaoQuestaoEnum;
	}

	public Boolean getIsApresentarCampoDisciplina() {
		return (getTelaUsoOnline() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline()) || (getTelaUsoPresencial() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial()) || (getTelaUsoExercicio() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio());
	}

	public TemaAssuntoVO getTemaAssuntoVO() {
		if (temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}
		return temaAssuntoVO;
	}

	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}

	public List<TemaAssuntoVO> getListaConsultaTemaAssuntoVOs() {
		if (listaConsultaTemaAssuntoVOs == null) {
			listaConsultaTemaAssuntoVOs = new ArrayList<TemaAssuntoVO>();
		}
		return listaConsultaTemaAssuntoVOs;
	}

	public void setListaConsultaTemaAssuntoVOs(List<TemaAssuntoVO> listaConsultaTemaAssuntoVOs) {
		this.listaConsultaTemaAssuntoVOs = listaConsultaTemaAssuntoVOs;
	}

	public void consultarTemaAssunto() {
		try {
			limparTemaAssunto();
			getListaConsultaTemaAssuntoVOs().clear();
			setListaConsultaTemaAssuntoVOs(getFacadeFactory().getTemaAssuntoFacade().consultarTemaAssuntoPorCodigoDisciplina(getQuestaoVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparTemaAssunto() {
		limparMensagem();
		setTemaAssuntoVO(new TemaAssuntoVO());
	}

	public void adicionarTemaAssunto() {
		try {
			TemaAssuntoVO obj = (TemaAssuntoVO) context().getExternalContext().getRequestMap().get("assuntosIncluirItens");
			getFacadeFactory().getQuestaoAssuntoFacade().adicionarTemaAssunto(obj, getQuestaoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerTemaAssunto() {
		try {
			QuestaoAssuntoVO obj = (QuestaoAssuntoVO) context().getExternalContext().getRequestMap().get("assuntosItem");
			getFacadeFactory().getQuestaoAssuntoFacade().removerTemaAssunto(obj, getQuestaoVO().getQuestaoAssuntoVOs(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarResponsavel() {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("responsavelItem");
			getQuestaoVO().setResponsavelCriacao(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarResponsavel() {
		try {
			this.getResponsavelDataModelo().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarUsuarioPorUnidadeEnsino(getResponsavelDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), this.getResponsavelDataModelo().getLimitePorPagina(), this.getResponsavelDataModelo().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, this.getUsuarioLogado()));
			this.getResponsavelDataModelo().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarUsuarioPorUnidadeEnsinoContador(getResponsavelDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, this.getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerUsuario(DataScrollEvent dataScrollEvent) {
		getResponsavelDataModelo().setPaginaAtual(dataScrollEvent.getPage());
		getResponsavelDataModelo().setPage(dataScrollEvent.getPage());
		consultarResponsavel();
	}
	
	public void limparResponsavelCriacao() {
		try {
			getQuestaoVO().setResponsavelCriacao(new UsuarioVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboResponsavel() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public DataModelo getResponsavelDataModelo() {
		this.responsavelDataModelo = Optional.ofNullable(this.responsavelDataModelo).orElseGet(() -> {
			DataModelo dataModelo = new DataModelo();
			dataModelo.setOffset(0);
			dataModelo.setLimitePorPagina(10);
			dataModelo.setPaginaAtual(1);
			return dataModelo;
		});
		return responsavelDataModelo;
	}

	public void setResponsavelDataModelo(DataModelo responsavelDataModelo) {
		this.responsavelDataModelo = responsavelDataModelo;
	}
	
	

	public List<String> getTiposCloneQuestao() {
		if(tiposCloneQuestao == null) {
			tiposCloneQuestao = new ArrayList<String>();
		}
		return tiposCloneQuestao;
	}

	public void setTiposCloneQuestao(List<String> tiposCloneQuestao) {
		this.tiposCloneQuestao = tiposCloneQuestao;
	}
	
	
	public Boolean getPermiteReativarQuestao() {
		return permiteReativarQuestaoConsulta(getQuestaoVO());		
	}
	
	public Boolean permiteReativarQuestaoConsulta(QuestaoVO questaoVO) {
		return questaoVO.isNovoObj() 
				|| ((questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.EM_ELABORACAO) 
						|| questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.INATIVA) 
						|| questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.CANCELADA)
					) 
				&& (questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) 
				|| ((questaoVO.getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoOnlineOutroProfessor()) 
						|| (questaoVO.getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoPresencialOutroProfessor()) 
						|| (questaoVO.getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getAlterarExercicioOutroProfessor()) 
						|| (questaoVO.getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getAlterarAtividadeDiscursivaOutroProfessor()))
				));
	}
	
	public Boolean possuiPermissaoInativarConsulta(QuestaoVO questaoVO) {

		return questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) && 
				( questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) 
				 || ((questaoVO.getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoOnline()) 
						 || (questaoVO.getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoPresencial()) 
						 || (questaoVO.getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getInativarExercicio()) 
						 || (questaoVO.getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getInativarAtividadeDiscursiva())) 
				) ;
	}
	
	public Boolean permiteAlterarQuestaoConsulta(QuestaoVO questaoVO) {
		return questaoVO.isNovoObj() 
				|| ((questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.EM_ELABORACAO) 
						&& (questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo())) 
				|| (questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.EM_ELABORACAO) 
					&& ((questaoVO.getUsoOnline()&& loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoOnlineOutroProfessor()) 
					|| (questaoVO.getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoPresencialOutroProfessor()) 
				    || (questaoVO.getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getAlterarExercicioOutroProfessor()) 
				    || (getQuestaoVO().getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getAlterarAtividadeDiscursivaOutroProfessor()))))
						)
				
				|| ((questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) 
						|| questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.INATIVA) 
						|| questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.CANCELADA)
						|| questaoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ANULADA) 
						)
						//Valida regra de alteração de questão online
				&& ((questaoVO.getUsoOnline() && loginControle.getPermissaoAcessoMenuVO().getPermiteAlterarQuestoesOnlineAtivas() 
				&& (questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoOnlineOutroProfessor()))
				//Valida regra de alteração de questão presencial
				|| (questaoVO.getUsoPresencial() && loginControle.getPermissaoAcessoMenuVO().getPermiteAlterarQuestoesPresencialAtivas() // não tem isso na tela de permissoes - tá vindo false
				&& (questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarQuestaoPresencialOutroProfessor())) 
				//Valida regra de alteração de questão exercicio
				|| (questaoVO.getUsoExercicio() && loginControle.getPermissaoAcessoMenuVO().getPermiteAlterarQuestoesExercicioAtivas()
				&& (questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarExercicioOutroProfessor()))
				//Valida regra de alteração de questão atividade discursiva
				|| (questaoVO.getUsoAtividadeDiscursiva() && loginControle.getPermissaoAcessoMenuVO().getPermiteAlterarQuestoesAtividadeDiscursivaAtivas()
				|| (questaoVO.getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) 
						|| loginControle.getPermissaoAcessoMenuVO().getAlterarAtividadeDiscursivaOutroProfessor()))));
	}
	
	public void editarConsulta() {
		try {
			setQuestaoVO(getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(((QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItem")).getCodigo()));			
			setMensagemID("msg_entre_dados", Uteis.ALERTA);			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
	}
	
	public void ativarQuestaoConsulta() {		
			editarConsulta();
			ativarQuestao();
	}

	public QuestaoVO getQuestaoConsultaVO() {
		if (questaoConsultaVO == null) {
			questaoConsultaVO = new QuestaoVO();
		}
		return questaoConsultaVO;
	}

	public void setQuestaoConsultaVO(QuestaoVO questaoConsultaVO) {
		this.questaoConsultaVO = questaoConsultaVO;
	}
	
	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}
	
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	
	public List<SelectItem> getListaSelectItemAssunto() {
		if (listaSelectItemAssunto == null) {
			listaSelectItemAssunto = new ArrayList<>();
			montarListaSelectItemAssunto();
		}
		return listaSelectItemAssunto;
	}

	public void setListaSelectItemAssunto(List<SelectItem> listaSelectItemAssunto) {
		this.listaSelectItemAssunto = listaSelectItemAssunto;
	}
	
	public void montarListaSelectItemAssunto() {
		try {
			List<TemaAssuntoVO> resultado = getFacadeFactory().getTemaAssuntoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			setListaSelectItemAssunto(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			
			arquivoVO = new ArquivoVO();
			
			if (!Uteis.isAtributoPreenchido(getQuestaoVO().getDisciplina().getCodigo())) {
				throw new Exception("A Disciplina é Obrigatória Para Realizar Upload de Imagens.");
			}
			getArquivoVO().setDisciplina(getQuestaoVO().getDisciplina());
			getArquivoVO().setOrigem(OrigemArquivo.QUESTAO_EAD.getValor());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.EAD_QUESTOES, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			uploadEvent = null;
			if (!getArquivoVO().getNome().equals("")) {
				try {
					getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
					getArquivoVOs().add(getArquivoVO());
					
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		}
	}   
	
    public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}
	
    public void montarListaArquivosImagem() {
    	try {
    		if (Uteis.isAtributoPreenchido(getQuestaoVO().getDisciplina().getCodigo())) {
    			getArquivoVOs().clear();
    			getArquivoVOs().addAll(getFacadeFactory().getArquivoFacade().consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(PastaBaseArquivoEnum.EAD.getValue()+"/questoes/"+getQuestaoVO().getDisciplina().getCodigo(), getQuestaoVO().getDisciplina().getCodigo()));
    			setDisciplinaImagem(getQuestaoVO().getDisciplina());
    		} else {
    			setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(PastaBaseArquivoEnum.EAD.getValue()+"/questoes/"+getDisciplinaImagem().getCodigo(), getDisciplinaImagem().getCodigo()));
    		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
	public List<ArquivoVO> getArquivoVOs() {
		if (arquivoVOs == null) {
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}  
	
	public void excluirArquivo() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO)getRequestMap().get("imagemItens");
			arquivoVO.setDataIndisponibilizacao(new Date());
			arquivoVO.setManterDisponibilizacao(false);
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(arquivoVO, getUsuarioLogado());
			setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(PastaBaseArquivoEnum.EAD.getValue()+"/questoes/"+getDisciplinaImagem().getCodigo(), getDisciplinaImagem().getCodigo()));
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	} 
	
	public void selecionarDisciplinaImagens() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			setDisciplinaImagem(disciplina);
			setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(PastaBaseArquivoEnum.EAD.getValue()+"/questoes/"+getDisciplinaImagem().getCodigo(), getDisciplinaImagem().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the disciplinaImagem
	 */
	public DisciplinaVO getDisciplinaImagem() {
		if (disciplinaImagem == null) {
			disciplinaImagem = new DisciplinaVO();
		}
		return disciplinaImagem;
	}

	/**
	 * @param disciplinaImagem the disciplinaImagem to set
	 */
	public void setDisciplinaImagem(DisciplinaVO disciplinaImagem) {
		this.disciplinaImagem = disciplinaImagem;
	}


	public void prepararConsultaDisciplina() throws Exception {
		try {
			setValorConsultaDisciplina("");
			setListaConsultaDisciplina(new ArrayList<>());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public String clonarManterQuestao() {
		try {
			QuestaoVO questaoClonada = new QuestaoVO();
			questaoClonada  = getFacadeFactory().getQuestaoFacade().clonarQuestao(getQuestaoVO(), getTiposCloneQuestao(),  getTelaUsoExercicio(), getTelaUsoOnline(), getTelaUsoPresencial(), getIdEntidade(), getUsuarioLogado(), getClonarComoAtiva());
			getFacadeFactory().getQuestaoFacade().persistir(questaoClonada, true, getIdEntidade(),  getUsuarioLogado());
			novo();
			setQuestaoVO(getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(questaoClonada.getCodigo()));
			montarListaArquivosImagem();
			setAcaoModalClonar("RichFaces.$('panelClonar').hide()");
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO, true);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(getUsuarioLogado().getIsApresentarVisaoProfessor() ? "questaoProfessorForm.xhtml" : "");
	}

	public Boolean getClonarComoAtiva() {
		if (clonarComoAtiva == null) {
			clonarComoAtiva = false;
		}
		return clonarComoAtiva;
	}

	public void setClonarComoAtiva(Boolean clonarComoAtiva) {
		this.clonarComoAtiva = clonarComoAtiva;
	}
	
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("disciplina", "Disciplina"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public void limparCamposBusca() {
		getControleConsultaOtimizado().setListaConsulta(new ArrayList<>());
		getControleConsulta().setValorConsulta("");
		montarListaSelectItemAssunto();
	}
	

	public void montarDadosDisciplinaQuestao() {
		try {
			getQuestaoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getQuestaoVO().getDisciplina().getCodigo() , Uteis.NIVELMONTARDADOS_COMBOBOX , getUsuarioLogado()));
			setDisciplinaImagem(getQuestaoVO().getDisciplina());
			setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(PastaBaseArquivoEnum.EAD.getValue()+"/questoes/"+getDisciplinaImagem().getCodigo(), getDisciplinaImagem().getCodigo()));
			montarListaConteudoDisciplinaSelecionado();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public Boolean verificarUsuarioPossuiPermissaoDisciplinasAnosAnteriores() {
		Boolean liberar = Boolean.FALSE;
		String identificadorAcaoPermissao = "";
		try {
			
			if (getTelaUsoOnline()) {
				identificadorAcaoPermissao = "PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnteriores";	
			}else if(getTelaUsoExercicio()) {
				identificadorAcaoPermissao = "PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnterioresExercicio";	
			}else {
				identificadorAcaoPermissao = "PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnterioresPresencial";	
			}
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
			liberar = Boolean.TRUE;
		} catch (Exception e) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}
	
	private Integer codigoConteudo;

	public Integer getCodigoConteudo() {
		if(codigoConteudo == null) {
			codigoConteudo = 0;
}
		return codigoConteudo;
	}

	public void setCodigoConteudo(Integer codigoConteudo) {
		this.codigoConteudo = codigoConteudo;
	}
	
	public void inicializarDadosAnulacaoQuestao() {
		try {
			consultarAlunoQuestaoAnulada();
			consultarAvaliacaoOnlineQuestaoFixaoAnulacao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void consultarAvaliacaoOnlineQuestaoFixaoAnulacao() throws Exception {
		setListaAvaliacaoOnlineQuestaoFixaVOs(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorQuestaoFixa(getQuestaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
	}
	
	public void consultarAlunoQuestaoAnulada() {
		setListaMatriculaCorrigirQuestaoAnuladaVOs(getFacadeFactory().getMatriculaFacade().consultarAlunoParticipaQuestaoOnline(getQuestaoVO().getCodigo(), getUsuarioLogado()));
	}
	
	public void realizarAnulacaoQuestao() {
		try {
			getFacadeFactory().getQuestaoFacade().realizarAnulacaoQuestao(getQuestaoVO(), getListaMatriculaCorrigirQuestaoAnuladaVOs(), false, getUsuarioLogado());
			setMensagemID("msg_dados_anulados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarSimnulacaoAnulcaoQuestao() {
		try {
			getFacadeFactory().getQuestaoFacade().realizarAnulacaoQuestao(getQuestaoVO(), getListaMatriculaCorrigirQuestaoAnuladaVOs(), true, getUsuarioLogado());
			setMensagemID("msg_dados_anulados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	} 

	public List<MatriculaVO> getListaMatriculaCorrigirQuestaoAnuladaVOs() {
		if (listaMatriculaCorrigirQuestaoAnuladaVOs == null) {
			listaMatriculaCorrigirQuestaoAnuladaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaCorrigirQuestaoAnuladaVOs;
	}

	public void setListaMatriculaCorrigirQuestaoAnuladaVOs(List<MatriculaVO> listaMatriculaCorrigirQuestaoAnuladaVOs) {
		this.listaMatriculaCorrigirQuestaoAnuladaVOs = listaMatriculaCorrigirQuestaoAnuladaVOs;
	}

	public List<AvaliacaoOnlineVO> getListaAvaliacaoOnlineQuestaoFixaVOs() {
		if (listaAvaliacaoOnlineQuestaoFixaVOs == null) {
			listaAvaliacaoOnlineQuestaoFixaVOs = new ArrayList<AvaliacaoOnlineVO>(0);
		}
		return listaAvaliacaoOnlineQuestaoFixaVOs;
	}

	public void setListaAvaliacaoOnlineQuestaoFixaVOs(List<AvaliacaoOnlineVO> listaAvaliacaoOnlineQuestaoFixaVOs) {
		this.listaAvaliacaoOnlineQuestaoFixaVOs = listaAvaliacaoOnlineQuestaoFixaVOs;
	}
	
	public void verificarPossuiPermissaoAnularQuestao() {
		try {
			 if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("AnularQuestaoOnline", getUsuarioLogado())) {
				 setPossuiPermissaoAnularQuestao(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPossuiPermissaoAnularQuestao(Boolean.FALSE);
		}
	}

	public Boolean getPossuiPermissaoAnularQuestao() {
		if (possuiPermissaoAnularQuestao == null) {
			possuiPermissaoAnularQuestao = false;
		}
		return possuiPermissaoAnularQuestao;
	}

	public void setPossuiPermissaoAnularQuestao(Boolean possuiPermissaoAnularQuestao) {
		this.possuiPermissaoAnularQuestao = possuiPermissaoAnularQuestao;
	}

	public Boolean getApresentarBotaoAnular() {
		return getPossuiPermissaoAnularQuestao() && !getQuestaoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ANULADA) 
				&& !getQuestaoVO().isNovoObj() && !getTelaUsoExercicio();
	}
}
