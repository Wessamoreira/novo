package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import jakarta.faces.model.SelectItem;

import org.primefaces.event.DragDropEvent;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.QuestionarioRespostaControle;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ReferenciaBibliograficaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoPlanoEnsinoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.Turno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.academico.PlanoDisciplinaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.PlanoDisciplinaRel;

@Controller("PlanoEnsinoControle")
@Lazy
@Scope("viewScope")
public class PlanoEnsinoControle extends QuestionarioRespostaControle {

	private static final long serialVersionUID = 2478210884926001356L;

	private PlanoEnsinoVO planoEnsinoVO;
	private ReferenciaBibliograficaVO referenciaBibliograficaVO;
	private ConteudoPlanejamentoVO conteudoPlanejamentoVO;
	private PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO;
	private TurmaVO turmaVO;

	private Integer indiceHorarioAula;
	private Integer periodoLetivo;

	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaProfessor;
	private String campoConsultaProfessor;
	private String valorConsultaTurma;
	private String campoConsultaTurma;

	private List<CursoVO> listaConsultaCurso;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<FuncionarioVO> listaConsultaProfessor;
	private List<TurmaVO> listaConsultaTurma;

	private DataModelo controleConsultaOtimizadoCatalogo;

	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> tipoConsultaComboCatalogo;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> listaSelectItemClassificacaoConteudoPlanejamento;
	private List<SelectItem> tipoConsultaComboTurma;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemTipoReferenciaReferenciaBibliografica;
	private List<SelectItem> listaSelectItemTipoPublicacaoReferenciaBibliografica;
	private List<SelectItem> listaSelectItemSituacao;
	private List<SelectItem> listaSelectItemSituacaoCons;
	private List<SelectItem> listaSelectItemDiaSemana;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private DataModelo listaDocumentosAssinados;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum;


	
	private double numerico;
	
	private Boolean habilitarAbas;
	
	private Boolean abrirModalRevisao;

	public double getNumerico() {
		return numerico;
	}

	public void setNumerico(double numerico) {
		this.numerico = numerico;
	}

	public PlanoEnsinoControle() {
		setControleConsulta(new ControleConsulta());
		setControleConsultaOtimizado(new DataModelo());
		montarListaSelectItemPeriodicidade();
	}

	public void persistir() throws Exception {
		try {
			if (getHabilitarControlePorCalendarioLancamentoPlanoEnsino()) {
				liberarCalendarioLancamentoPlanoEnsino();
			}
			getFacadeFactory().getPlanoEnsinoFacade().persistir(getPlanoEnsinoVO(), getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
				Integer codigoQuestionario = 0;
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())) {
					codigoQuestionario = getPlanoEnsinoVO().getCurso().getQuestionarioVO().getCodigo();
				}else {
					codigoQuestionario = getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo();
				}
				getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				 
				for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
					getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), getUsuarioLogado());
				}		
			}
			setHabilitarAbas(true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String excluir() throws Exception {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().excluir(getPlanoEnsinoVO(), getUsuarioLogado());
			setPlanoEnsinoVO(new PlanoEnsinoVO());
//			limparDados();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoEnsinoForm.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoEnsinoForm.xhtml");
		}
		return null;
	}

	public void limparDados() {
		setPlanoEnsinoVO(null);
		setConteudoPlanejamentoVO(null);
		setReferenciaBibliograficaVO(null);
		getControleConsultaOtimizado().getListaConsulta().clear();
	}

	public void clonar() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().realizarClonagem(getPlanoEnsinoVO(), false, true, getUsuarioLogado());
			setHabilitarAbas(!getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String novo() {
		setPlanoEnsinoVO(null);
		setConteudoPlanejamentoVO(null);
		setReferenciaBibliograficaVO(null);
		getPlanoEnsinoVO().setSituacao("PE");
		getPlanoEnsinoHorarioAulaVO().setTurmaVO(new TurmaVO());
		setHabilitarAbas(!getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
		montarListaSelectItemPeriodicidade();
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoEnsinoForm");
	}

	public String editar() {
		try {
			setPlanoEnsinoVO((PlanoEnsinoVO) getRequestMap().get("planoEnsinoItem"));
			montarListaSelectItemTurno();
			montarListaSelectItemPeriodicidade();
			setPlanoEnsinoVO(getFacadeFactory().getPlanoEnsinoFacade().consultarPorChavePrimaria(getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
			getFacadeFactory().getPlanoEnsinoFacade().preencherDadosPlanoEnsinoQuestionarioRespostaOrigem(planoEnsinoVO, getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo(), getUsuarioLogado());
			
			setHabilitarAbas(Boolean.TRUE);
			setMensagemID("msg_informe_dados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoEnsinoForm");
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPlanoEnsinoFacade().consultar(getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(), getPlanoEnsinoVO().getAno(), getPlanoEnsinoVO().getSemestre(), getPlanoEnsinoVO().getDescricao(), getPlanoEnsinoVO().getSituacao(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getPeriodoLetivo(), getTurmaVO().getCodigo(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPlanoEnsinoFacade().consultarTotalRegistro(getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(), getPlanoEnsinoVO().getAno(), getPlanoEnsinoVO().getSemestre(), getPlanoEnsinoVO().getDescricao(), getPlanoEnsinoVO().getSituacao(), getPeriodoLetivo(), getTurmaVO().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void liberarCalendarioLancamentoPlanoEnsino() throws ConsistirException {
		try {
//			getFacadeFactory().getPlanoEnsinoFacade().validarCalendarioLancamentoPlanoEnsino(getPlanoEnsinoVO(), getUsuarioLogado().getIsApresentarVisaoProfessor());
			setHabilitarAbas(!getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
			limparMensagem();
		} catch (Exception e) {
			setHabilitarAbas(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemApresentacaoConteudoPlanejado(DragDropEvent<?> dropEvent) {
		try {
			if (dropEvent.getData() instanceof ConteudoPlanejamentoVO && dropEvent.getData() instanceof ConteudoPlanejamentoVO) {
				getFacadeFactory().getPlanoEnsinoFacade().alterarOrdenacaoConteudoPlanejamentoVO(getPlanoEnsinoVO(), (ConteudoPlanejamentoVO) dropEvent.getData(), (ConteudoPlanejamentoVO) dropEvent.getData());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirConteudoPlanejado() {
		try {
			ConteudoPlanejamentoVO opc1 = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamentoItens");
			if (opc1.getOrdem() > 0) {
				ConteudoPlanejamentoVO opc2 = getPlanoEnsinoVO().getConteudoPlanejamentoVOs().get(opc1.getOrdem() - 2);
				getFacadeFactory().getPlanoEnsinoFacade().alterarOrdenacaoConteudoPlanejamentoVO(getPlanoEnsinoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerConteudoPlanejado() {
		try {
			ConteudoPlanejamentoVO opc1 = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamentoItens");
			if (getPlanoEnsinoVO().getConteudoPlanejamentoVOs().size() >= opc1.getOrdem()) {
				ConteudoPlanejamentoVO opc2 = getPlanoEnsinoVO().getConteudoPlanejamentoVOs().get(opc1.getOrdem());
				getFacadeFactory().getPlanoEnsinoFacade().alterarOrdenacaoConteudoPlanejamentoVO(getPlanoEnsinoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void alterarAguardandoAprovacao() {
		try {
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor());
			getPlanoEnsinoVO().setMotivo("");
			getPlanoEnsinoVO().setResponsavelAutorizacao((getUsuarioLogado()));
			getFacadeFactory().getPlanoEnsinoFacade().alterarSituacao(getPlanoEnsinoVO().getCodigo(), 
					SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor(), getPlanoEnsinoVO().getMotivo(),  getUsuarioLogado());
			setMensagemID("msg_PlanoEnsinoAguardandoAprovacao");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarAutorizado() {
		try {
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor());
			getPlanoEnsinoVO().setMotivo("");
			getFacadeFactory().getPlanoEnsinoFacade().alterarSituacao(getPlanoEnsinoVO().getCodigo(), 
					SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor(), getPlanoEnsinoVO().getMotivo(),  getUsuarioLogado());
			setMensagemID("msg_PlanoEnsinoAutorizado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void alterarEmRevisao() {
		try {
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor());
			getFacadeFactory().getPlanoEnsinoFacade().alterarSituacao(getPlanoEnsinoVO().getCodigo(), SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor(), 
					getPlanoEnsinoVO().getMotivo(), getUsuarioLogado());
			setMensagemID("msg_PlanoEnsinoEmRevisao");
			setAbrirModalRevisao(Boolean.FALSE);
		} catch (Exception e) {
			setAbrirModalRevisao(Boolean.TRUE);
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean apresentarSolicitarAprovacao() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				(getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor()) ||
				 getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.PENDENTE.getValor()));
	}

	public boolean apresentarAutorizar() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				(getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor()));
	}

	public boolean apresentarVoltarRevisao() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				(getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor()) || 
						getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor()));
	}
	
	public boolean desabilitarCampos() {
		return ( getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor()) || 
				getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor()) && getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
	}
	
	public boolean getHabilitarControlePorCalendarioLancamentoPlanoEnsino() {
    	try {
    		return getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("HabilitarControlePorCalendarioLancamentoPlanoEnsino", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return false;
		}
    }
	
	public String abrirModalRevisao() {
		if (getAbrirModalRevisao()) {
			return "PF('panelVoltarRevisao').show();";			
		} else {
			return "PF('panelVoltarRevisao').hide();";
		}
	}

	public void paginarConsulta( ) {
		
	}

	public String inicializarConsultar() {
		setPlanoEnsinoVO(null);	
		removerObjetoMemoria(this);
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoEnsinoCons");
	}

	public void adicionarReferenciaBibliografiaVOs() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().adicionarReferenciaBibliografiaVOs(getPlanoEnsinoVO(), getReferenciaBibliograficaVO());
			setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerReferenciaBibliografiaVOs() {
		try {
			ReferenciaBibliograficaVO obj = (ReferenciaBibliograficaVO) getRequestMap().get("referenciaBibliograficaItem");
			getFacadeFactory().getPlanoEnsinoFacade().removerReferenciaBibliografiaVOs(getPlanoEnsinoVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarReferenciaBibliografiaVOs() {
		try {
			ReferenciaBibliograficaVO obj = (ReferenciaBibliograficaVO) getRequestMap().get("referenciaBibliograficaItem");
			setReferenciaBibliograficaVO(obj);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConteudoPlanejamentoVOs() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().adicionarConteudoPlanejamentoVOs(getPlanoEnsinoVO(), getConteudoPlanejamentoVO());
			setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void atualizarTotalCargaHoraria() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerConteudoPlanejamentoVOs() {
		try {
			ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) getRequestMap().get("planejamentoItens");
			getFacadeFactory().getPlanoEnsinoFacade().removerConteudoPlanejamentoVOs(getPlanoEnsinoVO(), obj);
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarConteudoPlanejamentoVOs() {
		try {
			setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
			setConteudoPlanejamentoVO((ConteudoPlanejamentoVO) getRequestMap().get("planejamentoItens"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getPlanoEnsinoVO().setCurso(obj);
			
			if(!getTurmaVO().getCurso().getCodigo().equals(obj.getCodigo())) {
				limparDadosTurma();
			}
			getListaConsultaDisciplina().clear();
			montarListaSelectItemTurno();
			getPlanoEnsinoVO().setPeriodicidade(getPlanoEnsinoVO().getCurso().getPeriodicidade());
			getFacadeFactory().getPlanoEnsinoFacade().realizarCriacaoQuestionarioRespostaOrigem(getPlanoEnsinoVO(), obj.getQuestionarioVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(valorInt, getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviaturaDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getPlanoEnsinoVO().setDisciplina(obj);
			setValorConsultaDisciplina("");
			setCampoConsultaDisciplina("");
			getListaConsultaDisciplina().clear();
			if (getHabilitarControlePorCalendarioLancamentoPlanoEnsino()) {
				liberarCalendarioLancamentoPlanoEnsino();
			}
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())) {
				if(Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino())) {
					getFacadeFactory().getPlanoEnsinoFacade().realizarCriacaoQuestionarioRespostaOrigem(getPlanoEnsinoVO(), getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino(), getUsuarioLogado());
				}
			}
			setHabilitarAbas(Boolean.TRUE);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	

	public void selecionarDisciplinaConsulta() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getPlanoEnsinoVO().setDisciplina(obj);
			setValorConsultaDisciplina("");
			setCampoConsultaDisciplina("");
			getListaConsultaDisciplina().clear();	
		} catch (Exception e) {			
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void scrollerListener( ) throws Exception {
     
    }

	public void selecionarCatalogo() {
		try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItem");
			verificaPublicacaoExemplar(obj);
			getReferenciaBibliograficaVO().setCodigo(obj.getCodigo());
//			getReferenciaBibliograficaVO().setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, getUsuarioLogado()));
			setControleConsultaOtimizadoCatalogo(new DataModelo());
			getControleConsulta().setCampoConsulta("");
			getControleConsulta().setValorConsulta("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Monta os dados do {@link SelectItem} do {@link Turno}.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurno() {
		try {
			getListaSelectItemTurno().clear();
			List<TurnoVO> turnos = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCurso(
					getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemTurno().add(new SelectItem("",""));
			for (TurnoVO turnoVO : turnos) {
				SelectItem selectItem = new SelectItem();
				selectItem.setValue(turnoVO.getCodigo());
				selectItem.setLabel(turnoVO.getNome());
				getListaSelectItemTurno().add(selectItem);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificaPublicacaoExemplar(CatalogoVO obj) throws Exception {
//		if (!getFacadeFactory().getExemplarFacade().realizarVerifacaoCatalogoPossuiExemplar(obj.getCodigo())) {
//			throw new Exception("Nenhum exemplar foi encontrado para esse catálogo na Biblioteca");
//		}
	}

	public void consultarCatalogo() {
//		try {
//			super.consultar();
//			List<CatalogoVO> objs = new ArrayList<CatalogoVO>(0);
//			getControleConsultaOtimizadoCatalogo().setLimitePorPagina(5);
//			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCodigo(new Integer(valorInt), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorCodigoCatalogo(new Integer(valorInt), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("tituloTitulo")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTituloCatalogo(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("nomeEditora")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeEditora(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorNomeEditora(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("autor")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeAutor(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorNomeAutor(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("tombo")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTombo(getControleConsulta().getValorConsulta(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTombo(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("assunto")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorAssunto(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorAssunto(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("classificacao")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorClassificacao(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorClassificacao(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("tipoCatalogo")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTipoCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTipoCatalogo(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			if (getControleConsulta().getCampoConsulta().equals("cutterPha")) {
//				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCutterPha(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizadoCatalogo().getLimitePorPagina(), getControleConsultaOtimizadoCatalogo().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//				getControleConsultaOtimizadoCatalogo().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorCutterPha(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
//			}
//			getControleConsultaOtimizadoCatalogo().setListaConsulta(objs);
//			setMensagemID("msg_dados_consultados");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public void inicializarImpressaoPlanoEnsino() {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCursoCodigoDisciplina(getPlanoEnsinoVO().getCurso().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemGradeCurricular().clear();
			boolean gradeEncontrada = false;
			for (GradeCurricularVO gradeCurricularVO : gradeCurricularVOs) {
				if (!gradeEncontrada) {
					getPlanoEnsinoVO().getGradeCurricular().setCodigo(gradeCurricularVO.getCodigo());
					gradeEncontrada = true;
				}
				getListaSelectItemGradeCurricular().add(new SelectItem(gradeCurricularVO.getCodigo(), gradeCurricularVO.getNome() + "( " + gradeCurricularVO.getSituacao_Apresentar() + " )"));
			}
			consultarDocumentosPlanoDeEnsino();
			selecionarDocumentoAssinado();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparReferenciaBibliograficaVO() {
		getReferenciaBibliograficaVO().setCatalogo(null);
		getReferenciaBibliograficaVO().setTipoReferencia(null);
		getReferenciaBibliograficaVO().setCatalogo(new CatalogoVO());
	}

	public void realizarImpressaoPlanoEnsino() {
		List<PlanoDisciplinaRelVO> planoEnsinoVOs = null;

		try {
			if (getPlanoEnsinoVO().isNovoObj()) {
				throw new Exception("Só é possível emitir o PLANO DE ENSINO após ter sido gravado.");
			}

			planoEnsinoVOs = getFacadeFactory().getPlanoDisciplinaRelFacade().realizarGeracaoRelatorioPlanoEnsino(getPlanoEnsinoVO(), getUsuarioLogado());
			String caminho = PlanoDisciplinaRel.getCaminhoBaseRelatorio();
			String design = "";
			if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
				design = PlanoDisciplinaRel.getDesignIReportRelatorio("PlanoDisciplinaFormularioQuestionarioRel");
			}else {
				design = PlanoDisciplinaRel.getDesignIReportRelatorio();
			}		
										
			getSuperParametroRelVO().setUnidadeEnsino(getPlanoEnsinoVO().getUnidadeEnsino().getNome());
			getSuperParametroRelVO().setGradeCurricular(getPlanoEnsinoVO().getGradeCurricular().getNome());
			getSuperParametroRelVO().setCurso(getPlanoEnsinoVO().getCurso().getNome());
			getSuperParametroRelVO().setTurma("");
			getSuperParametroRelVO().setDisciplina(getPlanoEnsinoVO().getDisciplina().getNome());

			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Plano de Ensino");
			getSuperParametroRelVO().setListaObjetos(planoEnsinoVOs);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros("");
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok", Uteis.SUCESSO);
			if(isAssinarDigitalmente() && getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor())) {
				setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorPlanoDeEnsino(getCaminhoRelatorio(), getPlanoEnsinoVO(), getPlanoEnsinoVO().getAno(), 
						getPlanoEnsinoVO().getSemestre(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				getListaDocumentoAsssinados().clear();
			}
		} catch (Exception e) {
			getListaDocumentoAsssinados().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public boolean getIsPermitirEditarSituacao() {
		return getLoginControle().getPermissaoAcessoMenuVO().getAutorizarPublicarPlanoEnsino() || getPlanoEnsinoVO().getNovoObj();
	}
	
	public void scrollerListenerConsultaCatalogo( ) throws Exception {
//		getControleConsultaOtimizadoCatalogo().setPaginaAtual(dataScrollerEvent.getPage());
//		getControleConsultaOtimizadoCatalogo().setPage(dataScrollerEvent.getPage());
//		consultarCatalogo();
	}
	
	public DataModelo getControleConsultaOtimizadoCatalogo() {
		if (controleConsultaOtimizadoCatalogo == null) {
			controleConsultaOtimizadoCatalogo = new DataModelo();
		}
		return controleConsultaOtimizadoCatalogo;
	}

	public void setControleConsultaOtimizadoCatalogo(DataModelo controleConsultaOtimizadoCatalogo) {
		this.controleConsultaOtimizadoCatalogo = controleConsultaOtimizadoCatalogo;
	}
	
	public void adicionarPlanoEnsinoHorarioAulaVO(){
		try{
			getFacadeFactory().getPlanoEnsinoFacade().adicionarPlanoEnsinoHorarioAula(getPlanoEnsinoVO(), getPlanoEnsinoHorarioAulaVO());
			setPlanoEnsinoHorarioAulaVO(new PlanoEnsinoHorarioAulaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerPlanoEnsinoHorarioAulaVO(){
		try{
			getFacadeFactory().getPlanoEnsinoFacade().removerPlanoEnsinoHorarioAula(getPlanoEnsinoVO(), (PlanoEnsinoHorarioAulaVO) getRequestMap().get("planoEnsinoHorarioAulaItem"));			
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				super.consultarTurma(getValorConsultaTurma(), getPlanoEnsinoVO().getCurso().getCodigo(),  getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);				
			}			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
		
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			if (getIndiceHorarioAula().equals(0)){
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getPlanoEnsinoHorarioAulaVO().setTurmaVO(getTurmaVO());
				selecionarCursoPelaTurma();
				selecionarUnidadeEnsinoPelaTurma();
			} else if(!getPlanoEnsinoVO().getPlanoEnsinoHorarioAulaVOs().isEmpty() && getPlanoEnsinoVO().getPlanoEnsinoHorarioAulaVOs().size() >= getIndiceHorarioAula()){
				getPlanoEnsinoVO().getPlanoEnsinoHorarioAulaVOs().get(getIndiceHorarioAula()-1).setTurmaVO(obj.clone());				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarTurma() {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				if (Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getUnidadeEnsino())) {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				} else {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				}
				selecionarCursoPelaTurma();
				selecionarUnidadeEnsinoPelaTurma();
			} else {
				throw new Exception("Informe a Turma.");
			}
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void selecionarCursoPelaTurma() {
		if(getTurmaVO() != null && !getTurmaVO().getTurmaAgrupada() && getTurmaVO().getCurso() != null) {
			getPlanoEnsinoVO().setCurso(getTurmaVO().getCurso());
		}
	}
	
	private void selecionarUnidadeEnsinoPelaTurma() {
		if(getTurmaVO() != null && !getTurmaVO().getTurmaAgrupada() && getTurmaVO().getCurso() != null) {
			getPlanoEnsinoVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino());
		}
	}
	
	public boolean getIsHabilitarCampoUnidadeEnsino() {
		if (getTurmaVO() != null && getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			return true;
		}else if (getPlanoEnsinoVO().getCurso() != null && getPlanoEnsinoVO().getCurso().getCodigo() != null && getPlanoEnsinoVO().getCurso().getCodigo() != 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public void consultarTurmaPorChavePrimaria() {
		try {
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getUnidadeEnsino())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_unidadeEnsino"));
			}
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_curso"));
			}
			String campoConsulta = getPlanoEnsinoHorarioAulaVO().getTurmaVO().getIdentificadorTurma();
			if (campoConsulta != null && !campoConsulta.trim().equals("")) {
				getPlanoEnsinoHorarioAulaVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnicoCursoTurno( campoConsulta, getPlanoEnsinoVO().getCurso().getCodigo(), 0, getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));				
				setMensagemID("msg_dados_consultados");
			}else{
				limparMensagem();
			}			
		} catch (Exception e) {
			getPlanoEnsinoHorarioAulaVO().setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimariaListaHorario() {
		PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO =  (PlanoEnsinoHorarioAulaVO) getRequestMap().get("planoEnsinoHorarioAulaItem");
		try {
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getUnidadeEnsino())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_unidadeEnsino"));
			}
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_curso"));
			}
			String campoConsulta = planoEnsinoHorarioAulaVO.getTurmaVO().getIdentificadorTurma();
			if (campoConsulta != null && !campoConsulta.trim().equals("")) {				
				planoEnsinoHorarioAulaVO.setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnicoCursoTurno( campoConsulta, getPlanoEnsinoVO().getCurso().getCodigo(), 0, getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));			
				setMensagemID("msg_dados_consultados");
			}else{
				planoEnsinoHorarioAulaVO.setTurmaVO(new TurmaVO());
			}			
		} catch (Exception e) {
			planoEnsinoHorarioAulaVO.setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarProfessor() {
		try {
			super.consultar();
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), 
						TipoPessoa.PROFESSOR.getValor(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), 
						false, 0, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaProfessor(), 
						TipoPessoa.PROFESSOR.getValor(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), 
						false, 0, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() {
		try {
			FuncionarioVO professorSelecionado = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorVOItens");
			if (Uteis.isAtributoPreenchido(professorSelecionado)) {
				getPlanoEnsinoVO().setProfessorResponsavel(professorSelecionado.getPessoa());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setValorConsultaProfessor("");
		}
	}

	public void limparDadosTurma(){
		getPlanoEnsinoHorarioAulaVO().setTurmaVO(new TurmaVO());
		limparDadosDisciplina();
		setTurmaVO(new TurmaVO());
	}

	public void limparDadosTurmaListaHorario(){
		PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO =  (PlanoEnsinoHorarioAulaVO) getRequestMap().get("planoEnsinoHorarioAulaItem");
		planoEnsinoHorarioAulaVO.setTurmaVO(new TurmaVO());
	}

	public void limparDadosProfessor() {
		getPlanoEnsinoVO().setProfessorResponsavel(new PessoaVO());
	}
	
	public void limparDadosCurso() {
		limparDadosTurma();
		getPlanoEnsinoVO().setCurso(new CursoVO());
		getPlanoEnsinoVO().setUnidadeEnsino(new UnidadeEnsinoVO());
	}
	
	public void limparDadosDisciplina() {
		getPlanoEnsinoVO().setDisciplina(new DisciplinaVO());
	}

	//TODO GETTER AND SETTER
	public PlanoEnsinoVO getPlanoEnsinoVO() {
		if (planoEnsinoVO == null) {
			planoEnsinoVO = new PlanoEnsinoVO();
		}
		return planoEnsinoVO;
	}

	public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
		this.planoEnsinoVO = planoEnsinoVO;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
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

	public ReferenciaBibliograficaVO getReferenciaBibliograficaVO() {
		if (referenciaBibliograficaVO == null) {
			referenciaBibliograficaVO = new ReferenciaBibliograficaVO();
		}
		return referenciaBibliograficaVO;
	}

	public void setReferenciaBibliograficaVO(ReferenciaBibliograficaVO referenciaBibliograficaVO) {
		this.referenciaBibliograficaVO = referenciaBibliograficaVO;
	}

	public ConteudoPlanejamentoVO getConteudoPlanejamentoVO() {
		if (conteudoPlanejamentoVO == null) {
			conteudoPlanejamentoVO = new ConteudoPlanejamentoVO();
		}
		return conteudoPlanejamentoVO;
	}

	public void setConteudoPlanejamentoVO(ConteudoPlanejamentoVO conteudoPlanejamentoVO) {
		this.conteudoPlanejamentoVO = conteudoPlanejamentoVO;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}
	
	public Integer getIndiceHorarioAula() {
		if(indiceHorarioAula == null){
			indiceHorarioAula = 0;
		}
		return indiceHorarioAula;
	}

	public void setIndiceHorarioAula(Integer indiceHorarioAula) {
		this.indiceHorarioAula = indiceHorarioAula;
	}
	
	public Integer getPeriodoLetivo() {
		if(periodoLetivo == null) {
			periodoLetivo = 0;
		}
		
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}
	
	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}
	
	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}
	
	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	
	public PlanoEnsinoHorarioAulaVO getPlanoEnsinoHorarioAulaVO() {
		if(planoEnsinoHorarioAulaVO == null){
			planoEnsinoHorarioAulaVO = new PlanoEnsinoHorarioAulaVO();
		}
		return planoEnsinoHorarioAulaVO;
	}

	public void setPlanoEnsinoHorarioAulaVO(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO) {
		this.planoEnsinoHorarioAulaVO = planoEnsinoHorarioAulaVO;
	}
	
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	//TODO SELECTITEM
	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<>();
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem(" ", " "));
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}
	
	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodoLetivo.add(new SelectItem(" ", " "));
			listaSelectItemPeriodoLetivo.add(new SelectItem("1", "1º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("2", "2º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("3", "3º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("4", "4º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("5", "5º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("6", "6º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("7", "7º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("8", "8º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("9", "9º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("10", "10º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("11", "11º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("12", "12º"));
		}
		
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SelectItem> getListaSelectItemClassificacaoConteudoPlanejamento() throws Exception {
		if (listaSelectItemClassificacaoConteudoPlanejamento == null) {
			listaSelectItemClassificacaoConteudoPlanejamento = new ArrayList<SelectItem>(0);
			listaSelectItemClassificacaoConteudoPlanejamento.add(new SelectItem("", ""));
			Hashtable conteudoPlanejamento = (Hashtable) Dominios.getConteudoPlanejamento();
			Enumeration keys = conteudoPlanejamento.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) conteudoPlanejamento.get(value);
				listaSelectItemClassificacaoConteudoPlanejamento.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) listaSelectItemClassificacaoConteudoPlanejamento, ordenador);
		}
		return listaSelectItemClassificacaoConteudoPlanejamento;
	}

	public List<SelectItem> getTipoConsultaComboProfessorBusca() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null){
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemTipoReferenciaReferenciaBibliografica() throws Exception {
		if (listaSelectItemTipoReferenciaReferenciaBibliografica == null) {
			listaSelectItemTipoReferenciaReferenciaBibliografica = new ArrayList<SelectItem>(0);
			listaSelectItemTipoReferenciaReferenciaBibliografica.add(new SelectItem("", ""));
			Hashtable<String, String> tipoReferenciareferenciaBibliograficas = (Hashtable<String, String>) Dominios.getTipoReferenciareferenciaBibliografica();
			Enumeration<String> keys = tipoReferenciareferenciaBibliograficas.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) tipoReferenciareferenciaBibliograficas.get(value);
				listaSelectItemTipoReferenciaReferenciaBibliografica.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) listaSelectItemTipoReferenciaReferenciaBibliografica, ordenador);
		}
		return listaSelectItemTipoReferenciaReferenciaBibliografica;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemTipoPublicacaoReferenciaBibliografica() throws Exception {
		if (listaSelectItemTipoPublicacaoReferenciaBibliografica == null) {
			listaSelectItemTipoPublicacaoReferenciaBibliografica = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPublicacaoReferenciaBibliografica.add(new SelectItem("", ""));
			Hashtable tipoPublicacaoReferenciaBibliograficas = (Hashtable) Dominios.getTipoPublicacaoReferenciaBibliografica();
			Enumeration keys = tipoPublicacaoReferenciaBibliograficas.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) tipoPublicacaoReferenciaBibliograficas.get(value);
				listaSelectItemTipoPublicacaoReferenciaBibliografica.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) listaSelectItemTipoPublicacaoReferenciaBibliografica, ordenador);
		}
		return listaSelectItemTipoPublicacaoReferenciaBibliografica;
	}
	
	public List<SelectItem> getListaSelectItemSituacao() throws Exception {
		if(listaSelectItemSituacao == null){
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacao.add(new SelectItem(SituacaoPlanoEnsinoEnum.PENDENTE.getValor(), SituacaoPlanoEnsinoEnum.PENDENTE.getDescricao()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor(), SituacaoPlanoEnsinoEnum.AUTORIZADO.getDescricao()));
		}
		return listaSelectItemSituacao;
	}
	
	public List<SelectItem> getTipoOrdenarPorCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("edicao", "Edição"));
		itens.add(new SelectItem("titulo", "Título"));
		itens.add(new SelectItem("ano", "Ano Publicação"));
		itens.add(new SelectItem("crescente", "Ordem Crescente"));
		itens.add(new SelectItem("decrescente", "Ordem Decrescente"));
		return itens;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoCons() throws Exception {
		if(listaSelectItemSituacaoCons == null){
			listaSelectItemSituacaoCons = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.TODOS.getValor(), SituacaoPlanoEnsinoEnum.TODOS.getDescricao()));			
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.PENDENTE.getValor(), SituacaoPlanoEnsinoEnum.PENDENTE.getDescricao()));
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor(), SituacaoPlanoEnsinoEnum.AUTORIZADO.getDescricao()));
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor(), SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getDescricao()));
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor(), SituacaoPlanoEnsinoEnum.EM_REVISAO.getDescricao()));
		}
		return listaSelectItemSituacaoCons;
	}
	
	public List<SelectItem> getListaSelectItemDiaSemana() {
		if(listaSelectItemDiaSemana == null){
			listaSelectItemDiaSemana =  new ArrayList<SelectItem>(0);
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.DOMINGO, DiaSemana.DOMINGO.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.SEGUNGA, DiaSemana.SEGUNGA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.TERCA, DiaSemana.TERCA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.QUARTA, DiaSemana.QUARTA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.QUINTA, DiaSemana.QUINTA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.SEXTA, DiaSemana.SEXTA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.SABADO, DiaSemana.SABADO.getDescricao()));
		}
		return listaSelectItemDiaSemana;
	}

	public void setListaSelectItemDiaSemana(List<SelectItem> listaSelectItemDiaSemana) {
		this.listaSelectItemDiaSemana = listaSelectItemDiaSemana;
	}
	
	public List<SelectItem> getTipoConsultaComboCatalogo() {
		if (tipoConsultaComboCatalogo == null) {
			tipoConsultaComboCatalogo = new ArrayList<SelectItem>(0);
			tipoConsultaComboCatalogo.add(new SelectItem("tituloTitulo", "Título"));
			tipoConsultaComboCatalogo.add(new SelectItem("nomeEditora", "Editora"));
			tipoConsultaComboCatalogo.add(new SelectItem("autor", "Autor"));
			tipoConsultaComboCatalogo.add(new SelectItem("tombo", "Tombo"));
			tipoConsultaComboCatalogo.add(new SelectItem("assunto", "Assunto"));
			tipoConsultaComboCatalogo.add(new SelectItem("classificacao", "Classificação"));
			tipoConsultaComboCatalogo.add(new SelectItem("tipoCatalogo", "Tipo Catálogo"));
			tipoConsultaComboCatalogo.add(new SelectItem("cutterPha", "Cutter/PHA"));
		}
		return tipoConsultaComboCatalogo;
	}
	public void scrollerListenerTurma( ) throws Exception {
      
    }

	
	public void adicionarListaPerguntaItemRespostaOrigemVO() {		
		super.adicionarListaPerguntaItemRespostaOrigemVO();
	}

	public void inicializarControleConsultaTurma() {
		setControleConsultaTurma(null);
	}

	public Boolean getHabilitarAbas() {
		if (habilitarAbas == null) {
			habilitarAbas = Boolean.FALSE;
		}
		return habilitarAbas;
	}

	public void setHabilitarAbas(Boolean habilitarAbas) {
		this.habilitarAbas = habilitarAbas;
	}

	public Boolean getAbrirModalRevisao() {
		if (abrirModalRevisao == null) {
			abrirModalRevisao = Boolean.FALSE;
		}
		return abrirModalRevisao;
	}

	public void setAbrirModalRevisao(Boolean abrirModalRevisao) {
		this.abrirModalRevisao = abrirModalRevisao;
	}
	
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}
	
	public void montarListaSelectItemPeriodicidade() {
		getListaSelectItemPeriodicidade().clear();
		getListaSelectItemPeriodicidade().add(new SelectItem("IN", "Integral"));	
		getListaSelectItemPeriodicidade().add(new SelectItem("SE", "Semestral"));		
		getListaSelectItemPeriodicidade().add(new SelectItem("AN", "Anual"));
		
	}
	
	public DataModelo getListaDocumentosAssinados() {
		if(listaDocumentosAssinados == null) {
			listaDocumentosAssinados =  new DataModelo();
		}
		return listaDocumentosAssinados;
	}

	public void setListaDocumentosAssinados(DataModelo listaDocumentosAssinados) {
		this.listaDocumentosAssinados = listaDocumentosAssinados;
	}
	
	public void consultarDocumentoAssinado() {
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPlanoEnsino(getListaDocumentosAssinados(), getPlanoEnsinoVO(),  getPlanoEnsinoVO().getDisciplina(), TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getPlanoEnsinoVO().getGradeCurricular(),  getUsuarioLogado(), getListaDocumentosAssinados().getLimitePorPagina(), getListaDocumentosAssinados().getOffset());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void paginarDocumentosAssinados( ) {
		
	}
	
	public void consultarDocumentosPlanoDeEnsino() {
		getListaDocumentosAssinados().setOffset(0);
		getListaDocumentosAssinados().setLimitePorPagina(10);
		getListaDocumentosAssinados().setPage(0);
		getListaDocumentosAssinados().setPaginaAtual(0);
		consultarDocumentoAssinado();
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if(documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
	
	public SituacaoDocumentoAssinadoPessoaEnum getSituacaoDocumentoAssinadoPessoaEnum() {
		if(situacaoDocumentoAssinadoPessoaEnum == null) {
			situacaoDocumentoAssinadoPessoaEnum = SituacaoDocumentoAssinadoPessoaEnum.ASSINADO;
		}
		return situacaoDocumentoAssinadoPessoaEnum;
	}

	public void setSituacaoDocumentoAssinadoPessoaEnum(
			SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum) {
		this.situacaoDocumentoAssinadoPessoaEnum = situacaoDocumentoAssinadoPessoaEnum;
	}
	
	public void selecionarDocumentoAssinado() {
		setDocumentoAssinadoVO((DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem"));
		try {
			//setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(getDocumentoAssinadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
			for(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO: getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa()) {
				if(documentoAssinadoPessoaVO.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())) {
					setSituacaoDocumentoAssinadoPessoaEnum(documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
}
