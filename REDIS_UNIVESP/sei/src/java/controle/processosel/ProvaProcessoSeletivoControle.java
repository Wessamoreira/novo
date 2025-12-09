package controle.processosel;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoProvaProcessoSeletivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ProvaProcessoSeletivoControle")
@Scope("viewScope")
public class ProvaProcessoSeletivoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private ProvaProcessoSeletivoVO provaProcessoSeletivoVO;
	private List<SelectItem> listaSelectItemDisciplinaProcessoSeletivo;
	protected List<SelectItem> listaSelectItemSituacaoProvaProcessoSeletivo;
	private List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta;
	private Boolean mostarGabarito;
	private QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO;
	private DataModelo controleConsultaQuestao;
	private List<SelectItem> listaSelectItemGrupoDisciplinaProcSeletivo;

    public void scrollListener(DataScrollEvent DataScrollEvent) {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();        
    }

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getProvaProcessoSeletivoFacade().consultar(getProvaProcessoSeletivoVO().getDescricao(), getProvaProcessoSeletivoVO().getSituacaoProvaProcessoSeletivo(), true, "ProvaProcessoSeletivo", getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getProvaProcessoSeletivoFacade().consultarTotalRegistro(getProvaProcessoSeletivoVO().getDescricao(), getProvaProcessoSeletivoVO().getSituacaoProvaProcessoSeletivo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("provaProcessoSeletivoCons.xhtml");
	}

	public void inativar() {
		try {
			getFacadeFactory().getProvaProcessoSeletivoFacade().inativar(getProvaProcessoSeletivoVO(), true, getUsuarioLogado().getIsApresentarVisaoProfessor() ? "InativarListaExercicioProfessor" : "InativarListaExercicio", getUsuarioLogado());
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getProvaProcessoSeletivoFacade().excluir(getProvaProcessoSeletivoVO(), true, "ProvaProcessoSeletivo", getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void clonar() {
		try {
			setProvaProcessoSeletivoVO(getFacadeFactory().getProvaProcessoSeletivoFacade().clonarProvaProcessoSeletivo(getProvaProcessoSeletivoVO()));
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativar() {
		try {
			getFacadeFactory().getProvaProcessoSeletivoFacade().ativar(getProvaProcessoSeletivoVO(), true, "AtivarProvaProcessoSeletivo", getUsuarioLogado());
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getProvaProcessoSeletivoFacade().persistir(getProvaProcessoSeletivoVO(), true, "ProvaProcessoSeletivo", getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			setProvaProcessoSeletivoVO((ProvaProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("provaProcessoSeletivoItens"));
			getProvaProcessoSeletivoVO().setQuestaoProvaProcessoSeletivoVOs(getFacadeFactory().getQuestaoProvaProcessoSeletivoFacade().consultarPorProvaProcessoSeletivo(getProvaProcessoSeletivoVO().getCodigo(), NivelMontarDados.BASICO));
			montarListaSelectItemGrupoDisciplinaProcSeletivo();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("provaProcessoSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("provaProcessoSeletivoCons.xhtml");
		}
	}

	public String novo() {
		try {
			setProvaProcessoSeletivoVO(getFacadeFactory().getProvaProcessoSeletivoFacade().novo());
			getProvaProcessoSeletivoVO().getResponsavelCriacao().setCodigo(getUsuarioLogado().getCodigo());
			getProvaProcessoSeletivoVO().getResponsavelCriacao().setNome(getUsuarioLogado().getNome());
			montarListaSelectItemGrupoDisciplinaProcSeletivo();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("provaProcessoSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("provaProcessoSeletivoCons.xhtml");
		}
	}

	public void alterarOrdemApresentacaoQuestaoProvaProcessoSeletivo(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof QuestaoProvaProcessoSeletivoVO && dropEvent.getDropValue() instanceof QuestaoProvaProcessoSeletivoVO) {
				getFacadeFactory().getProvaProcessoSeletivoFacade().alterarOrdemApresentacaoQuestaoProvaProcessoSeletivo(getProvaProcessoSeletivoVO(), (QuestaoProvaProcessoSeletivoVO) dropEvent.getDragValue(), (QuestaoProvaProcessoSeletivoVO) dropEvent.getDropValue());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirQuestaoProvaProcessoSeletivo() {
		try {
			QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO = (QuestaoProvaProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("questaoProvaProcessoSeletivoItens");
			if (questaoProvaProcessoSeletivoVO.getOrdemApresentacao() > 1) {
				QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO2 = getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs().get(questaoProvaProcessoSeletivoVO.getOrdemApresentacao() - 2);
				getFacadeFactory().getProvaProcessoSeletivoFacade().alterarOrdemApresentacaoQuestaoProvaProcessoSeletivo(getProvaProcessoSeletivoVO(), questaoProvaProcessoSeletivoVO, questaoProvaProcessoSeletivoVO2);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerQuestaoProvaProcessoSeletivo() {
		try {
			QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO = (QuestaoProvaProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("questaoProvaProcessoSeletivoItens");
			if (getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs().size() >= questaoProvaProcessoSeletivoVO.getOrdemApresentacao()) {
				QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO2 = getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs().get(questaoProvaProcessoSeletivoVO.getOrdemApresentacao());
				getFacadeFactory().getProvaProcessoSeletivoFacade().alterarOrdemApresentacaoQuestaoProvaProcessoSeletivo(getProvaProcessoSeletivoVO(), questaoProvaProcessoSeletivoVO, questaoProvaProcessoSeletivoVO2);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarQuestao() {
		try {
			QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO = new QuestaoProvaProcessoSeletivoVO();
			QuestaoProcessoSeletivoVO questaoVO = (QuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("questaoItens");
			questaoProvaProcessoSeletivoVO.setQuestaoProcessoSeletivo(questaoVO);
			getFacadeFactory().getProvaProcessoSeletivoFacade().adicionarQuestaoProvaProcessoSeletivo(getProvaProcessoSeletivoVO(), questaoProvaProcessoSeletivoVO);
			questaoVO.setSelecionado(true);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestao() {
		try {
			QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO = new QuestaoProvaProcessoSeletivoVO();
			QuestaoProcessoSeletivoVO questaoVO = (QuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("questaoItens");
			questaoProvaProcessoSeletivoVO.setQuestaoProcessoSeletivo(questaoVO);
			getFacadeFactory().getProvaProcessoSeletivoFacade().removerQuestaoProvaProcessoSeletivo(getProvaProcessoSeletivoVO(), questaoProvaProcessoSeletivoVO);
			questaoVO.setSelecionado(false);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestaoListaExercicio() {
		try {
			getFacadeFactory().getProvaProcessoSeletivoFacade().removerQuestaoProvaProcessoSeletivo(getProvaProcessoSeletivoVO(), (QuestaoProvaProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("questaoProvaProcessoSeletivoItens"));
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarConsultaQuestao() {
		montarListaSelectItemDisciplinaProcessoSeletivo();
		setControleConsultaQuestao(null);
		limparMensagem();
	}

	public void consultarQuestao() {
		try {
			if (!Uteis.isAtributoPreenchido(getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ProvaProcessoSeletivo_grupoDisciplinaProcessoSeletivo"));
			}
			getControleConsultaQuestao().setLimitePorPagina(5);
			getControleConsultaQuestao().setListaConsulta(getFacadeFactory().getQuestaoProcessoSeletivoFacade().consultarPoGrupoDisciplinaProcSeletivo(getQuestaoProcessoSeletivoVO().getEnunciado(), getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo(), getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), SituacaoQuestaoEnum.ATIVA, getQuestaoProcessoSeletivoVO().getNivelComplexidadeQuestao(), false, getUsuarioLogado(), getControleConsultaQuestao().getLimitePorPagina(), getControleConsultaQuestao().getOffset()));
			getControleConsultaQuestao().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoProcessoSeletivoFacade().consultarTotalResgistroPoGrupoDisciplinaProcSeletivo(getQuestaoProcessoSeletivoVO().getEnunciado(), getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo(), getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), SituacaoQuestaoEnum.ATIVA, getQuestaoProcessoSeletivoVO().getNivelComplexidadeQuestao()));
			marcarQuestaoJaSelecionada();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void marcarQuestaoJaSelecionada() {
		q: for (QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO : (List<QuestaoProcessoSeletivoVO>) getControleConsultaQuestao().getListaConsulta()) {
			for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
				if (questaoProcessoSeletivoVO.getCodigo().intValue() == questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo().intValue()) {
					questaoProcessoSeletivoVO.setSelecionado(true);
					continue q;
				}
			}
		}
	}

	public void paginarQuestao(DataScrollEvent dataScrollerEvent) {
		getControleConsultaQuestao().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaQuestao().setPage(dataScrollerEvent.getPage());
		consultarQuestao();
	}

	public String inicializarConsultar() {
		setProvaProcessoSeletivoVO(new ProvaProcessoSeletivoVO());
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("provaProcessoSeletivoCons.xhtml");
	}

	public List<SelectItem> getListaSelectItemSituacaoProvaProcessoSeletivo() {
		if (listaSelectItemSituacaoProvaProcessoSeletivo == null) {
			listaSelectItemSituacaoProvaProcessoSeletivo = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoProvaProcessoSeletivoEnum.class, "name", "valorApresentar", true);
		}
		return listaSelectItemSituacaoProvaProcessoSeletivo;
	}

	public DataModelo getControleConsultaQuestao() {
		if (controleConsultaQuestao == null) {
			controleConsultaQuestao = new DataModelo();
			controleConsultaQuestao.setLimitePorPagina(10);
		}
		return controleConsultaQuestao;
	}

	public void setControleConsultaQuestao(DataModelo controleConsultaQuestao) {
		this.controleConsultaQuestao = controleConsultaQuestao;
	}

	public List<SelectItem> getListaSelectItemComplexidadeQuestaoConsulta() {
		if (listaSelectItemComplexidadeQuestaoConsulta == null) {
			listaSelectItemComplexidadeQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.FACIL, NivelComplexidadeQuestaoEnum.FACIL.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.MEDIO, NivelComplexidadeQuestaoEnum.MEDIO.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.DIFICIL, NivelComplexidadeQuestaoEnum.DIFICIL.getValorApresentar()));
		}
		return listaSelectItemComplexidadeQuestaoConsulta;
	}

	public Boolean getPossuiPermissaoInativar() {
		return getProvaProcessoSeletivoVO().getSituacaoProvaProcessoSeletivo() != null && getProvaProcessoSeletivoVO().getSituacaoProvaProcessoSeletivo().equals(SituacaoProvaProcessoSeletivoEnum.ATIVA);
	}

	public void realizarCorrecaoExercicio() {
		try {
			getFacadeFactory().getProvaProcessoSeletivoFacade().realizarGeracaoGabarito(getProvaProcessoSeletivoVO());
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			setMostarGabarito(true);
		} catch (ConsistirException e) {
			setMostarGabarito(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMostarGabarito(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getMostarGabarito() {
		if (mostarGabarito == null) {
			mostarGabarito = false;
		}
		return mostarGabarito;
	}

	public boolean getPermiteAlterarProvaProcessoSeletivo() {
		return getProvaProcessoSeletivoVO().isNovoObj() || getProvaProcessoSeletivoVO().getSituacaoProvaProcessoSeletivo().equals(SituacaoProvaProcessoSeletivoEnum.EM_ELABORACAO);
	}

	public void setMostarGabarito(Boolean mostarGabarito) {
		this.mostarGabarito = mostarGabarito;
	}

	public void realizarVerificacaoQuestaoUnicaEscolha() {
		OpcaoRespostaQuestaoProcessoSeletivoVO orq = (OpcaoRespostaQuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestao");
		if (orq.getMarcada()) {
			orq.setMarcada(false);
		} else {
			orq.setMarcada(true);
			getFacadeFactory().getProvaProcessoSeletivoFacade().realizarVerificacaoQuestaoUnicaEscolha(getProvaProcessoSeletivoVO(), orq);
		}
	}

	public ProvaProcessoSeletivoVO getProvaProcessoSeletivoVO() {
		if (provaProcessoSeletivoVO == null) {
			provaProcessoSeletivoVO = new ProvaProcessoSeletivoVO();
		}
		return provaProcessoSeletivoVO;
	}

	public void setProvaProcessoSeletivoVO(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) {
		this.provaProcessoSeletivoVO = provaProcessoSeletivoVO;
	}

	public QuestaoProcessoSeletivoVO getQuestaoProcessoSeletivoVO() {
		if (questaoProcessoSeletivoVO == null) {
			questaoProcessoSeletivoVO = new QuestaoProcessoSeletivoVO();
		}
		return questaoProcessoSeletivoVO;
	}

	public void setQuestaoProcessoSeletivoVO(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) {
		this.questaoProcessoSeletivoVO = questaoProcessoSeletivoVO;
	}

	public List<SelectItem> getListaSelectItemGrupoDisciplinaProcSeletivo() {
		if (listaSelectItemGrupoDisciplinaProcSeletivo == null) {
			listaSelectItemGrupoDisciplinaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGrupoDisciplinaProcSeletivo;
	}

	public void setListaSelectItemGrupoDisciplinaProcSeletivo(List<SelectItem> listaSelectItemGrupoDisciplinaProcSeletivo) {
		this.listaSelectItemGrupoDisciplinaProcSeletivo = listaSelectItemGrupoDisciplinaProcSeletivo;
	}

	public void montarListaSelectItemGrupoDisciplinaProcSeletivo() throws Exception {
		List<GrupoDisciplinaProcSeletivoVO> grupoDisciplinaProcSeletivoVOs = getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorDescricaoSituacaoAtivo("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemGrupoDisciplinaProcSeletivo(UtilSelectItem.getListaSelectItem(grupoDisciplinaProcSeletivoVOs, "codigo", "descricao"));
	}

	public List<SelectItem> getListaSelectItemDisciplinaProcessoSeletivo() {
		if (listaSelectItemDisciplinaProcessoSeletivo == null) {
			listaSelectItemDisciplinaProcessoSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaProcessoSeletivo;
	}

	public void setListaSelectItemDisciplinaProcessoSeletivo(List<SelectItem> listaSelectItemDisciplinaProcessoSeletivo) {
		this.listaSelectItemDisciplinaProcessoSeletivo = listaSelectItemDisciplinaProcessoSeletivo;
	}

	public void montarListaSelectItemDisciplinaProcessoSeletivo() {
		try {
			List<DisciplinasProcSeletivoVO> disciplinasProcSeletivoVOs = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorGrupoDisciplinaProcSeletivo(getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), false, getUsuarioLogado());
			setListaSelectItemDisciplinaProcessoSeletivo(UtilSelectItem.getListaSelectItem(disciplinasProcSeletivoVOs, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

}
