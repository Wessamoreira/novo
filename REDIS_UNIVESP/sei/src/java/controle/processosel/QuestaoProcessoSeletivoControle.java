package controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("QuestaoProcessoSeletivoControle")
@Scope("viewScope")
public class QuestaoProcessoSeletivoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO;
	private OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO;
	private List<SelectItem> listaSelectItemSituacaoQuestaoConsulta;
	private List<SelectItem> listaSelectItemComplexidadeQuestao;
	private List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta;
	protected List<SelectItem> listaSelectItemDisciplinaProcessoSeletivo;
	private ArquivoVO arquivoVO;
	private List<ArquivoVO> arquivoVOs;	
	private DisciplinaVO disciplinaImagem;

	public QuestaoProcessoSeletivoControle() {
	}

	public String editar() {
		try {
			setQuestaoProcessoSeletivoVO(getFacadeFactory().getQuestaoProcessoSeletivoFacade().consultarPorChavePrimaria(((QuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("questaoItens")).getCodigo()));
			if (!getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs().isEmpty()) {
				Ordenacao.ordenarLista(getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs(), "ordemApresentacao");
			}
			montarListaArquivosImagem();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProcessoSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProcessoSeletivoCons.xhtml");
		}
	}

	public String novo() {
		try {
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().novo(getQuestaoProcessoSeletivoVO());
			getArquivoVOs().clear();
			setListaSelectItemDisciplinaProcessoSeletivo(null);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProcessoSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProcessoSeletivoCons.xhtml");
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().excluir(getQuestaoProcessoSeletivoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().persistir(getQuestaoProcessoSeletivoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void clonarQuestao() {
		try {
			setQuestaoProcessoSeletivoVO(getFacadeFactory().getQuestaoProcessoSeletivoFacade().clonarQuestao(getQuestaoProcessoSeletivoVO()));
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativarQuestao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtivarQuestaoProcessoSeletivo", getUsuarioLogado());
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().ativarQuestao(getQuestaoProcessoSeletivoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarQuestao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InativarQuestaoProcessoSeletivo", getUsuarioLogado());
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().inativarQuestao(getQuestaoProcessoSeletivoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void cancelarQuestao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("CancelarQuestaoProcessoSeletivo", getUsuarioLogado());
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().cancelarQuestao(getQuestaoProcessoSeletivoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getQuestaoProcessoSeletivoFacade().consultar(getQuestaoProcessoSeletivoVO().getEnunciado(), getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo(), getQuestaoProcessoSeletivoVO().getSituacaoQuestaoEnum(), getQuestaoProcessoSeletivoVO().getNivelComplexidadeQuestao(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoProcessoSeletivoFacade().consultarTotalResgistro(getQuestaoProcessoSeletivoVO().getEnunciado(), getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo(), getQuestaoProcessoSeletivoVO().getSituacaoQuestaoEnum(), getQuestaoProcessoSeletivoVO().getNivelComplexidadeQuestao()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		 return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProcessoSeletivoCons.xhtml");
	}

	public void paginarConsulta(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultar();
	}

	public void adicionarOpcaoRespostaQuestao() {
		try {
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().adicionarOpcaoRespostaQuestao(getQuestaoProcessoSeletivoVO(), false, new OpcaoRespostaQuestaoProcessoSeletivoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarOpcaoRespostaQuestao() {
		try {
			for (OpcaoRespostaQuestaoProcessoSeletivoVO opc : getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
				opc.setEditar(false);
			}
			OpcaoRespostaQuestaoProcessoSeletivoVO opc = (OpcaoRespostaQuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (opc == null && getOpcaoRespostaQuestaoProcessoSeletivoVO().getOrdemApresentacao() != null && getOpcaoRespostaQuestaoProcessoSeletivoVO().getOrdemApresentacao() > 0) {
				for (OpcaoRespostaQuestaoProcessoSeletivoVO opc1 : getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
					if (opc1.getOrdemApresentacao().equals(getOpcaoRespostaQuestaoProcessoSeletivoVO().getOrdemApresentacao())) {
						opc1.setEditar(true);
						getOpcaoRespostaQuestaoProcessoSeletivoVO().setOrdemApresentacao(0);
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
			OpcaoRespostaQuestaoProcessoSeletivoVO opc = (OpcaoRespostaQuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (opc == null && getOpcaoRespostaQuestaoProcessoSeletivoVO().getOrdemApresentacao() != null && getOpcaoRespostaQuestaoProcessoSeletivoVO().getOrdemApresentacao() > 0) {
				for (OpcaoRespostaQuestaoProcessoSeletivoVO opc1 : getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
					if (opc1.getOrdemApresentacao().equals(getOpcaoRespostaQuestaoProcessoSeletivoVO().getOrdemApresentacao())) {
						opc1.setEditar(false);
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
			OpcaoRespostaQuestaoProcessoSeletivoVO opc = (OpcaoRespostaQuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().removerOpcaoRespostaQuestao(getQuestaoProcessoSeletivoVO(), opc);
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
				getFacadeFactory().getQuestaoProcessoSeletivoFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoProcessoSeletivoVO(), (OpcaoRespostaQuestaoProcessoSeletivoVO) dropEvent.getDragValue(), (OpcaoRespostaQuestaoProcessoSeletivoVO) dropEvent.getDropValue());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirOpcaoRespostaQuestao() {
		try {
			OpcaoRespostaQuestaoProcessoSeletivoVO opc1 = (OpcaoRespostaQuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (opc1.getOrdemApresentacao() > 1) {
				OpcaoRespostaQuestaoProcessoSeletivoVO opc2 = getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs().get(opc1.getOrdemApresentacao() - 2);
				getFacadeFactory().getQuestaoProcessoSeletivoFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoProcessoSeletivoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerOpcaoRespostaQuestao() {
		try {
			OpcaoRespostaQuestaoProcessoSeletivoVO opc1 = (OpcaoRespostaQuestaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs().size() >= opc1.getOrdemApresentacao()) {
				OpcaoRespostaQuestaoProcessoSeletivoVO opc2 = getQuestaoProcessoSeletivoVO().getOpcaoRespostaQuestaoProcessoSeletivoVOs().get(opc1.getOrdemApresentacao());
				getFacadeFactory().getQuestaoProcessoSeletivoFacade().alterarOrdemOpcaoRespostaQuestao(getQuestaoProcessoSeletivoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoQuestaoConsulta() {
		if (listaSelectItemSituacaoQuestaoConsulta == null) {
			listaSelectItemSituacaoQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoQuestaoConsulta.add(new SelectItem(null, ""));
			listaSelectItemSituacaoQuestaoConsulta.add(new SelectItem(SituacaoQuestaoEnum.EM_ELABORACAO, SituacaoQuestaoEnum.EM_ELABORACAO.getValorApresentar()));
			listaSelectItemSituacaoQuestaoConsulta.add(new SelectItem(SituacaoQuestaoEnum.ATIVA, SituacaoQuestaoEnum.ATIVA.getValorApresentar()));
			listaSelectItemSituacaoQuestaoConsulta.add(new SelectItem(SituacaoQuestaoEnum.INATIVA, SituacaoQuestaoEnum.INATIVA.getValorApresentar()));
			listaSelectItemSituacaoQuestaoConsulta.add(new SelectItem(SituacaoQuestaoEnum.CANCELADA, SituacaoQuestaoEnum.CANCELADA.getValorApresentar()));
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
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(null, ""));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.FACIL, NivelComplexidadeQuestaoEnum.FACIL.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.MEDIO, NivelComplexidadeQuestaoEnum.MEDIO.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.DIFICIL, NivelComplexidadeQuestaoEnum.DIFICIL.getValorApresentar()));
		}
		return listaSelectItemComplexidadeQuestaoConsulta;
	}

	public void setListaSelectItemComplexidadeQuestaoConsulta(List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta) {
		this.listaSelectItemComplexidadeQuestaoConsulta = listaSelectItemComplexidadeQuestaoConsulta;
	}

	public String inicializarConsultar() {
		setQuestaoProcessoSeletivoVO(new QuestaoProcessoSeletivoVO());
		setControleConsultaOtimizado(new DataModelo());
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("questaoProcessoSeletivoCons.xhtml");
	}

	private LoginControle loginControle = (LoginControle) getControlador("LoginControle");

	/**
	 * Verifica se a questao pode ser alterada pelo usuario observando as seguintes regras:
	 * 
	 * 1 - A questao tem que estar em ativa e, 2 - A questao deve ter sido criado pelo usuario logado ou possua permissao para alterar questao de
	 * outro usuario e, 3 - O usuario deve ter permissao de inativar uma questao observando o seu tipo
	 * 
	 * @return true || false
	 */
	public Boolean getPossuiPermissaoInativar() {

		return getQuestaoProcessoSeletivoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) && loginControle.getPermissaoAcessoMenuVO().getInativarQuestaoProcessoSeletivo();
	}

	/**
	 * Verifica se a questao pode ser alterada pelo usuario observando as seguintes regras:
	 * 
	 * 1 - A questao tem que estar em ativa e, 2 - A questao deve ter sido criado pelo usuario logado ou possua permissao para alterar questao de
	 * outro usuario e, 3 - O usuario deve ter permissao de cancelar uma questao observando o seu tipo e, 4 - A questao deve ser do tipo online ou
	 * presencial
	 * 
	 * @return true || false
	 */
	public Boolean getPossuiPermissaoCancelarQuestao() {
		return getQuestaoProcessoSeletivoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.ATIVA) && loginControle.getPermissaoAcessoMenuVO().getCancelarQuestaoProcessoSeletivo();
	}

	/**
	 * Verifica se a questao pode ser alterada pelo usuario observando as seguintes regras: 1 - Questão pode ser nova ou, 2 - A questao tem que estar
	 * em elaboracao e, 3 - A questao deve ter sido criado pelo usuario logado ou possua permissao para alterar questao de outro usuario
	 * 
	 * @return true || false
	 */
	public Boolean getPermiteAlterarQuestao() {
		return getQuestaoProcessoSeletivoVO().isNovoObj() || getQuestaoProcessoSeletivoVO().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.EM_ELABORACAO);
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

	public List<SelectItem> getListaSelectItemDisciplinaProcessoSeletivo() {
		if (listaSelectItemDisciplinaProcessoSeletivo == null) {
			listaSelectItemDisciplinaProcessoSeletivo = new ArrayList<SelectItem>(0);
			try {
				List<DisciplinasProcSeletivoVO> disciplinasProcSeletivoVOs = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorNome("", false, getUsuarioLogado());
				listaSelectItemDisciplinaProcessoSeletivo.add(new SelectItem(0, ""));
				for (DisciplinasProcSeletivoVO disciplinasProcSeletivoVO : disciplinasProcSeletivoVOs) {
					listaSelectItemDisciplinaProcessoSeletivo.add(new SelectItem(disciplinasProcSeletivoVO.getCodigo(), disciplinasProcSeletivoVO.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemDisciplinaProcessoSeletivo;
	}

	public void setListaSelectItemDisciplinaProcessoSeletivo(List<SelectItem> listaSelectItemDisciplinaProcessoSeletivo) {
		this.listaSelectItemDisciplinaProcessoSeletivo = listaSelectItemDisciplinaProcessoSeletivo;
	}

	public OpcaoRespostaQuestaoProcessoSeletivoVO getOpcaoRespostaQuestaoProcessoSeletivoVO() {
		if (opcaoRespostaQuestaoProcessoSeletivoVO == null) {
			opcaoRespostaQuestaoProcessoSeletivoVO = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		}
		return opcaoRespostaQuestaoProcessoSeletivoVO;
	}

	public void setOpcaoRespostaQuestaoProcessoSeletivoVO(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) {
		this.opcaoRespostaQuestaoProcessoSeletivoVO = opcaoRespostaQuestaoProcessoSeletivoVO;
	}
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			
			arquivoVO = new ArquivoVO();
			
			if (!Uteis.isAtributoPreenchido(getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo())) {
				throw new Exception("A Disciplina é Obrigatória Para Realizar Upload de Imagens.");
			}
			getArquivoVO().setCodOrigem(getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo());
			getArquivoVO().setOrigem(OrigemArquivo.PROCESSO_SELETIVO_QUESTOES.getValor());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES, getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			uploadEvent = null;
			if (!getArquivoVO().getNome().equals("")) {
				try {
					getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
					getArquivoVO().setDescricao(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo()+"/"+getArquivoVO().getPastaBaseArquivo()+"/"+getArquivoVO().getNome());
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
    		getArquivoVOs().clear();
    		if (Uteis.isAtributoPreenchido(getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo())) {
    			getArquivoVOs().addAll(getFacadeFactory().getArquivoFacade().consultarArquivosQuestaoProcessoSeletivoPorDisciplina(PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES.getValue()+"/"+getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo(), getQuestaoProcessoSeletivoVO().getDisciplinaProcSeletivo().getCodigo()));    			
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
			montarListaArquivosImagem();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
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

}
