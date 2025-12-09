package controle.ead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.enumeradores.OrdemEstudoDisciplinasOnlineEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoProvaPresencialEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberarAcessoProximaDisciplinaEnum;
import negocio.comuns.ead.enumeradores.TipoControleTempoLimiteConclusaoDisciplinaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("ConfiguracaoEADControle")
@Scope("viewScope")
public class ConfiguracaoEADControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private ConfiguracaoEADVO configuracaoEADVO;
	private Boolean editandoAPartirFormConfiguracores;
	private List<SelectItem> listaSelectItemGrupoDestinatario;
	private List<SelectItem> listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	private List<SelectItem> listaSelectItemVariavelNotaCfaVOs;
	private List<SelectItem> listaSelectItemVariavelNotaAtividadeDiscursivaVOs;
	
	@PostConstruct
	public void init() {
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoEADFacade().persistir(configuracaoEADVO, true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCamposParaClone() {
		getConfiguracaoEADVO().setCodigo(0);
		getConfiguracaoEADVO().setNovoObj(true);
	}

	public String novo() {
		try {
			setConfiguracaoEADVO(new ConfiguracaoEADVO());
			removerObjetoMemoria(this);
			montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline();
			montarListaDeNotasDaConfiguracaoAcademico();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoEADForm");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoEADForm");
		}
	}

	public void gravar() {
		try {
			getFacadeFactory().getConfiguracaoEADFacade().persistir(configuracaoEADVO, false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() throws Exception {
		try {
			getFacadeFactory().getConfiguracaoEADFacade().excluir(getConfiguracaoEADVO(), false, getUsuarioLogado());
			setConfiguracaoEADVO(getConfiguracaoEADVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			ConfiguracaoEADVO obj = ((ConfiguracaoEADVO) context().getExternalContext().getRequestMap().get("configuracaoEADItem"));
			setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(obj.getCodigo()));
			montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline();
			montarListaDeNotasDaConfiguracaoAcademico();
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoEADForm");
	}

	public void realizarAtivacaoSituacaoConfiguracaoEAD() {
		try {
			getFacadeFactory().getConfiguracaoEADFacade().alterarSituacaoConfiguracaoEAD(getConfiguracaoEADVO().getCodigo(), "ATIVO", getUsuarioLogado());
			getConfiguracaoEADVO().setSituacao(SituacaoEnum.ATIVO);
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarFinalizacaoSituacaoConfiguracaoEAD() {
		try {
			getFacadeFactory().getConfiguracaoEADFacade().alterarSituacaoConfiguracaoEAD(getConfiguracaoEADVO().getCodigo(), "FINALIZADO", getUsuarioLogado());
			getConfiguracaoEADVO().setSituacao(SituacaoEnum.FINALIZADO);
			setMensagemID("msg_dados_finalizado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getEditandoAPartirFormConfiguracores() {
		if (editandoAPartirFormConfiguracores == null) {
			return Boolean.FALSE;
		}
		return editandoAPartirFormConfiguracores;
	}

	public void setEditandoAPartirFormConfiguracores(Boolean editandoAPartirFormConfiguracores) {
		this.editandoAPartirFormConfiguracores = editandoAPartirFormConfiguracores;
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getConfiguracaoEADFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS));
			if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoEADCons");
	}
	
	public void limparCamposTipoControleTempoLimiteConclusaoDisciplina() {
		try {
			if (getConfiguracaoEADVO().getTipoControleTempoLimiteConclusaoDisciplina().isPeriodoCalendarioLetivo()) {
				getConfiguracaoEADVO().setTempoLimiteConclusaoDisciplina(0.0);
				getConfiguracaoEADVO().setTempoLimiteConclusaoTodasDisciplinas(0);
				getConfiguracaoEADVO().setTempoLimiteConclusaoCursoIncluindoTCC(0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getlistaSelectItemSituacao() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoEnum.class, "name", "valorApresentar", false);
	}

	public List<SelectItem> getListaSelectItemTipoControleTempoLimiteConclusaoDisciplina() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoControleTempoLimiteConclusaoDisciplinaEnum.class, "name", "valorApresentar", false);
	}

	public List<SelectItem> getListaSelectItemTipoControleLiberacaoAvaliacaoOnline() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoControleLiberacaoAvaliacaoOnlineEnum.class, "name", "valorApresentar", false);
	}

	public List<SelectItem> getListaSelectItemOrdemEstudoDisciplinasOnline() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(OrdemEstudoDisciplinasOnlineEnum.class, "name", "valorApresentar", false);
	}

	public List<SelectItem> getListaSelectItemTipoControleLiberarAcessoProximaDisciplina() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoControleLiberarAcessoProximaDisciplinaEnum enumerador : TipoControleLiberarAcessoProximaDisciplinaEnum.values()) {
			if (
//					(enumerador.isAprovadoProvaPresencial() && getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().isNaoControlarProvaPresencial())|| 
				 (enumerador.isAprovadoAvaliacaoOnline() && getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().isNaoAplicarAvaliacaoOnline())) {
				continue;
			}
			lista.add(new SelectItem(enumerador, UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString())));
		}
		return lista;
	}

	public List<SelectItem> getListaSelectItemTempoLimiteRealizacaoProvaPresencial() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoControleLiberacaoProvaPresencialEnum enumerador : TipoControleLiberacaoProvaPresencialEnum.values()) {
			if ((enumerador.isAprovadoAvaliacaoOnline() && getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().isNaoAplicarAvaliacaoOnline())) {
				continue;
			}
			lista.add(new SelectItem(enumerador, UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString())));
		}
		return lista;
	}

	public Boolean getApresentarBotaoAtivar() {
		return getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.EM_CONSTRUCAO) && !getConfiguracaoEADVO().isNovoObj();
	}

	public Boolean getApresentarBotaoFinalizar() {
		return getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.ATIVO);
	}

	public boolean getIsApresentarTempoLimiteRealizacaoProvaPresencial() {
		return getConfiguracaoEADVO().getControlarTempoLimiteRealizarProvaPresencial();
	}

	public boolean getIsApresentarNumeroDiasFixo() {
		return getConfiguracaoEADVO().getTipoControleTempoLimiteConclusaoDisciplina().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_FIXO);
	}

	public boolean getIsApresentarNumeroDiasPorCredito() {
		return getConfiguracaoEADVO().getTipoControleTempoLimiteConclusaoDisciplina().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_POR_CREDITO);
	}

	public boolean getIsApresentarNumeroDiasPorHoraDaDisciplina() {
		return getConfiguracaoEADVO().getTipoControleTempoLimiteConclusaoDisciplina().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_POR_HORA_DA_DISCIPLINA_CH);
	}

	public boolean getIsApresentarOrdemDeEstudosDisciplinas() {
		return getConfiguracaoEADVO().getOrdemEstudoDisciplinasOnline().equals(OrdemEstudoDisciplinasOnlineEnum.SIMULTANEAS);
	}

	public boolean getIsApresentarPontuacaoAlcancadaEstudos() {
		return getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.PONTUACAO_ALCANCADA_ESTUDOS);
	}
	
	public boolean getIsApresentarNDiasEntreAvaliacaoOnlineEVezesPodeRepetir() {
		return !getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE);
	}

	public boolean getIsApresentarPontuacaoAlcancadaEstudosProvaPresencial() {
		return getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().equals(TipoControleLiberacaoProvaPresencialEnum.PONTUACAO_ALCANCADA_ESTUDOS);
	}

	public boolean getIsApresentarConteudoLidoProvaPresencial() {
		return getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().equals(TipoControleLiberacaoProvaPresencialEnum.CONTEUDO_LIDO);
	}

	public boolean getIsApresentarNumeroDiasIncioDisciplinaProvaPresencial() {
		return getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().equals(TipoControleLiberacaoProvaPresencialEnum.NR_DIAS_APOS_O_INICIO_DA_DISCIPLINA);
	}

	public boolean getIsApresentarConteudoLido() {
		return getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.CONTEUDO_LIDO);
	}

	public boolean getIsApresentarNumeroDiasAposInicioCurso() {
		return getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NR_DIAS_APOS_O_INICIO_DO_CURSO);
	}

	public boolean getIsApresentarPercentualPontuacaoLiberacaoProximaDisciplinas() {
		return getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.PONTUACAO_ALCANCADA_ESTUDOS);
	}

	public boolean getIsApresentarConteudoLidoProximaDisciplina() {
		return getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.CONTEUDO_LIDO);
	}

	public boolean getIsApresentarValorControleLiberarAcessoProximaDisciplina() {
		return !getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_AVALIACAO_ONLINE) 
//				&& !getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_PROVA_PRESENCIAL) 
				&& !getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APOS_APROVACAO_REPROVACAO_DISCIPLINA);
	}

	public boolean getIsApresentarValorControleLiberacaoAvalicaoOnline() {
		return !getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE) && !getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.IMEDIATO_APOS_O_INICIO_DO_CURSO);
	}

	public boolean getIsApresentarValorControleLiberacaoProvaPresencial() {
		return !getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().equals(TipoControleLiberacaoProvaPresencialEnum.APROVADO_AVALIACAO_ONLINE) && !getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().equals(TipoControleLiberacaoProvaPresencialEnum.NAO_CONTROLAR_PROVA_PRESENCIAL);
	}
	
	public void montarListaDeNotasDaConfiguracaoAcademico() {
		try {
			getListaSelectItemVariavelNotaCfaVOs().clear();
			getListaSelectItemVariavelNotaAtividadeDiscursivaVOs().clear();
			getListaSelectItemVariavelNotaCfaVOs().addAll(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarVariavelTituloConfiguracaoAcademicoPorTipoUsoNota(TipoUsoNotaEnum.AVALIACAO_ONLINE, getUsuarioLogado()));
			getListaSelectItemVariavelNotaAtividadeDiscursivaVOs().addAll(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarVariavelTituloConfiguracaoAcademicoPorTipoUsoNota(TipoUsoNotaEnum.ATIVIDADE_DISCURSIVA, getUsuarioLogado()));			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemVariavelNotaCfaVOs() {
		if (listaSelectItemVariavelNotaCfaVOs == null) {
			listaSelectItemVariavelNotaCfaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNotaCfaVOs;
	}

	public void setListaSelectItemVariavelNotaCfaVOs(List<SelectItem> listaSelectItemVariavelNotaCfaVOs) {
		this.listaSelectItemVariavelNotaCfaVOs = listaSelectItemVariavelNotaCfaVOs;
	}
	

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("descricao", "Descrição"));
		return objs;
	}

	public ConfiguracaoEADVO getConfiguracaoEADVO() {
		if (configuracaoEADVO == null) {
			configuracaoEADVO = new ConfiguracaoEADVO();
		}
		return configuracaoEADVO;
	}

	public void setConfiguracaoEADVO(ConfiguracaoEADVO configuracaoEADVO) {
		this.configuracaoEADVO = configuracaoEADVO;
	}
	
	public boolean getIsApresentarCampoVariavelNotaCfgPadraoProvaPresencialEcalcularMediaFinalAposRealizacaoProvaPresencial() {
		if(!(getConfiguracaoEADVO().getTipoControleLiberacaoProvaPresencial().equals(TipoControleLiberacaoProvaPresencialEnum.NAO_CONTROLAR_PROVA_PRESENCIAL))) {
			return true;
		}
		return false;
	}
	
	public boolean getIsApresentarTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao() {
		if(getConfiguracaoEADVO().getPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso()) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaSelectItemGrupoDestinatario() {
		if (listaSelectItemGrupoDestinatario == null) {
			listaSelectItemGrupoDestinatario = new ArrayList<SelectItem>();
		}
		return listaSelectItemGrupoDestinatario;
	}

	public void setListaSelectItemGrupoDestinatario(List<SelectItem> listaSelectItemGrupoDestinatario) {
		this.listaSelectItemGrupoDestinatario = listaSelectItemGrupoDestinatario;
	}
	
	public void montarListaSelectItemGrupoDestinatarios() {
		setListaSelectItemGrupoDestinatario(getFacadeFactory().getGrupoDestinatariosFacade().consultarDadosListaSelectItem(Obrigatorio.NAO));
	}
	
	public List<SelectItem> getListaSelectItemParametrosMonitoramentoAvaliacaoOnline() {
		if(listaSelectItemParametrosMonitoramentoAvaliacaoOnline == null) {
			listaSelectItemParametrosMonitoramentoAvaliacaoOnline = new ArrayList<SelectItem>();
		}
		return listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	}

	public void setListaSelectItemParametrosMonitoramentoAvaliacaoOnline(List<SelectItem> listaSelectItemParametrosMonitoramentoAvaliacaoOnline) {
		this.listaSelectItemParametrosMonitoramentoAvaliacaoOnline = listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	}
	
	public void montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline() {
		try {
			setListaSelectItemParametrosMonitoramentoAvaliacaoOnline(UtilSelectItem.getListaSelectItem(getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().consultarTodosParametros(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), "codigo", "descricao", true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public boolean getIsDesativarCamposSeAtivo() {
		return getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.ATIVO) || getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.INATIVO);
	}
	
	public void clonar() {
		try {
			setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().clonarConfiguracaoEADVO(getConfiguracaoEADVO()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SelectItem> getListaSelectItemVariavelNotaAtividadeDiscursivaVOs() {
		if(listaSelectItemVariavelNotaAtividadeDiscursivaVOs == null) {
			listaSelectItemVariavelNotaAtividadeDiscursivaVOs =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNotaAtividadeDiscursivaVOs;
	}

	public void setListaSelectItemVariavelNotaAtividadeDiscursivaVOs(
			List<SelectItem> listaSelectItemVariavelNotaAtividadeDiscursivaVOs) {
		this.listaSelectItemVariavelNotaAtividadeDiscursivaVOs = listaSelectItemVariavelNotaAtividadeDiscursivaVOs;
	}
	
	
}
