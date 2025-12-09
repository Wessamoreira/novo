package controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("ConfiguracaoRecebimentoCartaoOnlineControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoRecebimentoCartaoOnlineControle extends SuperControle {

	private static final long serialVersionUID = 1677955933644205354L;

    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List<TurmaVO> turmaVOs;

    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private CursoVO cursoVO;

    private SituacaoEnum situacao;
    private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;
    private ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO;
    private List<SelectItem> comboboxSituacaoEnum;
    private List<SelectItem> listaSelectItemCampoConsulta;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemNivelConfiguracao;
    private List<SelectItem> listaSelectItemConfiguracaoFinanceira;
    

    public ConfiguracaoRecebimentoCartaoOnlineControle() {
    	getControleConsulta().setCampoConsulta("descricao");
    }

    public void gravar() {
		try {
		    getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().persistir(getConfiguracaoRecebimentoCartaoOnlineVO(), true, getUsuarioLogado());
		    setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void excluir() {
		try {
		    getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().excluir(getConfiguracaoRecebimentoCartaoOnlineVO(), true, getUsuarioLogado());
		    novo();
		    setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void montarListaSelectItemUnidadeEnsino() {
		try {
		    getControleConsulta().setValorConsulta("");
		    if (getControleConsulta().getCampoConsulta().equals("unidadeensino")) {
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(0, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemUnidadeEnsino().clear();
			getListaSelectItemUnidadeEnsino().addAll(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		    }
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void montarListaSelectItemUnidadeEnsinoEditar(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		try {
		    if (unidadeEnsinoVOs.isEmpty() || unidadeEnsinoVOs.size() == 1) {
			if (unidadeEnsinoVOs.isEmpty() && getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO().getConfiguracoesVO().getCodigo() != 0) {
			    unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorConfiguracoes(getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO().getConfiguracoesVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			getListaSelectItemUnidadeEnsino().clear();
			getListaSelectItemUnidadeEnsino().addAll(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome"));
		    }
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    @SuppressWarnings("unchecked")
    public String consultar() {
		try {
		    getControleConsulta().getListaConsulta().clear();
		    getControleConsulta().getListaConsulta().addAll(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getCampoConsulta().equals("curso") ? getCursoVO().getCodigo().toString() : getControleConsulta().getValorConsulta(), getSituacao(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		    setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRecebimentoCartaoOnlineCons.xhtml");
    }


    public String novo() {
		setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>();
		montarListaSelectItemUnidadeEnsinoEditar(unidadeEnsinoVOs);
		montarListaSelectItemConfiguracaoFinanceiro();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRecebimentoCartaoOnlineForm.xhtml");
    }

    public String editar() {
		try {
		    List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>();
		    setConfiguracaoRecebimentoCartaoOnlineVO((ConfiguracaoRecebimentoCartaoOnlineVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoRecebimentoCartaoOnline"));
		    setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarPorChavePrimariaUnica(getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		    montarListaSelectItemUnidadeEnsinoEditar(unidadeEnsinoVOs);
		    montarListaSelectItemConfiguracaoFinanceiro();
		    setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRecebimentoCartaoOnlineForm.xhtml");
    }

    public void ativar() {
    	SituacaoEnum situacaoAntesAtivacao = getConfiguracaoRecebimentoCartaoOnlineVO().getSituacao();
    	UsuarioVO reposnsavelAtivacao = getConfiguracaoRecebimentoCartaoOnlineVO().getReposnsavelAtivacao();
		try {
		    getConfiguracaoRecebimentoCartaoOnlineVO().setSituacao(SituacaoEnum.ATIVO);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setReposnsavelAtivacao(getUsuarioLogadoClone());
		    getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().alterarSituacaoConfiguracaoRecebimentoCartaoOnlineVOAtivo(getConfiguracaoRecebimentoCartaoOnlineVO(), true, getUsuarioLogado());
			getAplicacaoControle().removerConfiguracaoRecebimentoCartaoOnline(getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
		    setMensagemID("msg_ativar_dados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setSituacao(situacaoAntesAtivacao);
			getConfiguracaoRecebimentoCartaoOnlineVO().setReposnsavelAtivacao(reposnsavelAtivacao);
		} catch (Exception e) {
		    setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setSituacao(situacaoAntesAtivacao);
			getConfiguracaoRecebimentoCartaoOnlineVO().setReposnsavelAtivacao(reposnsavelAtivacao);
		}
    }

    public void inativar() {
    	SituacaoEnum situacaoAntesInativacao = getConfiguracaoRecebimentoCartaoOnlineVO().getSituacao();
    	UsuarioVO reposnsavelInativacao = getConfiguracaoRecebimentoCartaoOnlineVO().getReposnsavelInativacao();
		try {
		    getConfiguracaoRecebimentoCartaoOnlineVO().setSituacao(SituacaoEnum.INATIVO);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setReposnsavelInativacao(getUsuarioLogadoClone());
		    getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().alterarSituacaoConfiguracaoRecebimentoCartaoOnlineVOInativo(getConfiguracaoRecebimentoCartaoOnlineVO(), true, getUsuarioLogado());
			getAplicacaoControle().removerConfiguracaoRecebimentoCartaoOnline(getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
		    setMensagemID("msg_dados_inativado");
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setSituacao(situacaoAntesInativacao);
			getConfiguracaoRecebimentoCartaoOnlineVO().setReposnsavelInativacao(reposnsavelInativacao);
		} catch (Exception e) {
		    setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setSituacao(situacaoAntesInativacao);
			getConfiguracaoRecebimentoCartaoOnlineVO().setReposnsavelInativacao(reposnsavelInativacao);
		}
    }

    public void clonar() {
		try {
		    setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().clonar(getConfiguracaoRecebimentoCartaoOnlineVO()));
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void adicionarRecebimentoAdministrativo() {
		try {
			
			getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().adicionarConfiguracaoFinanceiroCartaoRecebimento(getConfiguracaoFinanceiroCartaoRecebimentoVO(), getConfiguracaoRecebimentoCartaoOnlineVO(), getUsuarioLogado());
			if (getConfiguracaoFinanceiroCartaoRecebimentoVO().getItemEmEdicao()) {
				
				Iterator<ConfiguracaoFinanceiroCartaoRecebimentoVO> i = getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroCartaoRecebimentoVOs().iterator();
				int index = 0;
				int aux = -1;
				ConfiguracaoFinanceiroCartaoRecebimentoVO objAux = new ConfiguracaoFinanceiroCartaoRecebimentoVO();
				while(i.hasNext()) {
					ConfiguracaoFinanceiroCartaoRecebimentoVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getConfiguracaoFinanceiroCartaoRecebimentoVO().getCodigo()) && objExistente.getItemEmEdicao()){
						getConfiguracaoFinanceiroCartaoRecebimentoVO().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getConfiguracaoFinanceiroCartaoRecebimentoVO();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroCartaoRecebimentoVOs().set(aux, objAux);
				}
			} else {		
				getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroCartaoRecebimentoVOs().add(getConfiguracaoFinanceiroCartaoRecebimentoVO());
			}

		    setConfiguracaoFinanceiroCartaoRecebimentoVO(new ConfiguracaoFinanceiroCartaoRecebimentoVO());
		    setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void cancelarRecebimentoAdministrativo() {
    	setConfiguracaoFinanceiroCartaoRecebimentoVO(new ConfiguracaoFinanceiroCartaoRecebimentoVO());
    	setMensagemID(MSG_TELA.msg_dados_consultados.name());
    }

    public void removerRecebimentoAdministrativo() {
		try {
		    ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO = (ConfiguracaoFinanceiroCartaoRecebimentoVO) context().getExternalContext().getRequestMap().get("configuracaoFinanceiroCartaoRecebimentoItem");
		    getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().removerConfiguracaoFinanceiroCartaoRecebimento(recebimentoAdministrativoVO, getConfiguracaoRecebimentoCartaoOnlineVO(), getUsuarioLogado());
		    setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    public void editarRecebimentoAdministrativo() {
    	try {
    		ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO = (ConfiguracaoFinanceiroCartaoRecebimentoVO) context().getExternalContext().getRequestMap().get("configuracaoFinanceiroCartaoRecebimentoItem");

    		recebimentoAdministrativoVO.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setConfiguracaoFinanceiroCartaoRecebimentoVO((ConfiguracaoFinanceiroCartaoRecebimentoVO) SerializationUtils.clone(recebimentoAdministrativoVO));   
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public String voltarTelaConsultar() {
		getControleConsulta().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRecebimentoCartaoOnlineCons.xhtml");
    }

    public void consultarTurma() {
		try {
		    getTurmaVOs().clear();
		    if (getCampoConsultaTurma().equals("identificadorTurma")) {
			getTurmaVOs().addAll(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getConfiguracaoRecebimentoCartaoOnlineVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		    }
		    setMensagemID("msg_dados_consultados");
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void montarTurma() throws Exception {
		try {
		    getControleConsulta().setValorConsulta(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()).getIdentificadorTurma());
		    setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void montarTurmaEditar() throws Exception {
		try {
		    getConfiguracaoRecebimentoCartaoOnlineVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getConfiguracaoRecebimentoCartaoOnlineVO().getTurmaVO().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		    getConfiguracaoRecebimentoCartaoOnlineVO().setCursoVO(getConfiguracaoRecebimentoCartaoOnlineVO().getTurmaVO().getCurso());
		    getConfiguracaoRecebimentoCartaoOnlineVO().setUnidadeEnsinoVO(getConfiguracaoRecebimentoCartaoOnlineVO().getTurmaVO().getUnidadeEnsino());
		    montarListaSelectItemConfiguracaoFinanceiro();
		    setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void selecionarTurma() {
		try {
		    TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
		    getControleConsulta().setValorConsulta(obj.getIdentificadorTurma());
		} catch (Exception e) {
		    setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void selecionarTurmaEditar() {
		try {
		    TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
		    getConfiguracaoRecebimentoCartaoOnlineVO().setTurmaVO(obj);
		    getConfiguracaoRecebimentoCartaoOnlineVO().setCursoVO(obj.getCurso());
		    getConfiguracaoRecebimentoCartaoOnlineVO().setUnidadeEnsinoVO(getConfiguracaoRecebimentoCartaoOnlineVO().getTurmaVO().getUnidadeEnsino());
		    montarListaSelectItemConfiguracaoFinanceiro();
		} catch (Exception e) {
		    setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    @SuppressWarnings("unchecked")
    public void consultarDadosCurso() {
		try {
		    getCursoVOs().clear();
		    if (getCampoConsultaCurso().equals("nome")) {
		    	getCursoVOs().addAll(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
		    }

		    if (getCampoConsultaCurso().equals("nrRegistroInterno")) {
		    	getCursoVOs().addAll(getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getValorConsultaCurso(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		    }

		    if (getCampoConsultaCurso().equals("nomeAreaConhecimento")) {
		    	getCursoVOs().addAll(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getValorConsultaCurso(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		    }

		    if (getCampoConsultaCurso().equals("nivelEducacional")) {
		    	getCursoVOs().addAll(getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getValorConsultaCurso(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		    }

		    if (getCampoConsultaCurso().equals("unidadeEnsino")) {
		    	getCursoVOs().addAll(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getValorConsultaCurso(), 0, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		    }
		    setMensagemDetalhada("");
		    setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void limparDadosCurso() {
		removerObjetoMemoria(getCursoVO());
		getCursoVOs().clear();
    }

    public void limparDadosTurma() {
		getControleConsulta().setValorConsulta("");
		getTurmaVOs().clear();
    }

    public void limparDadosCursoEditar() {
		getConfiguracaoRecebimentoCartaoOnlineVO().setCursoVO(null);
		getCursoVOs().clear();
    }

    public void limparDadosTurmaCursoQuandoUnidadeEnsinoForAlterada() {
		try {
		    if (getConfiguracaoRecebimentoCartaoOnlineVO().getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO) || getConfiguracaoRecebimentoCartaoOnlineVO().getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA)) {
				getConfiguracaoRecebimentoCartaoOnlineVO().setCursoVO(new CursoVO());
				getConfiguracaoRecebimentoCartaoOnlineVO().setTurmaVO(new TurmaVO());
		    }
		    if (Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO().getUnidadeEnsinoVO().getCodigo())) {
		    	getConfiguracaoRecebimentoCartaoOnlineVO().setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getConfiguracaoRecebimentoCartaoOnlineVO().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
		    }
		    montarListaSelectItemConfiguracaoFinanceiro();
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void limparDadosTurmaEditar() {
		getConfiguracaoRecebimentoCartaoOnlineVO().setTurmaVO(null);
		getTurmaVOs().clear();
		getConfiguracaoRecebimentoCartaoOnlineVO().setCursoVO(new CursoVO());
		getConfiguracaoRecebimentoCartaoOnlineVO().setUnidadeEnsinoVO(null);
    }

    public void selecionarCurso() throws Exception {
		try {
		    CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		    setCursoVO(obj);
		    setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void selecionarCursoEditar() throws Exception {
		try {
		    CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		    getConfiguracaoRecebimentoCartaoOnlineVO().setCursoVO(obj);
		    List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>();
		    unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigoCurso(getConfiguracaoRecebimentoCartaoOnlineVO().getCursoVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		    montarListaSelectItemUnidadeEnsinoEditar(unidadeEnsinoVOs);
		    setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    @SuppressWarnings("unchecked")
	public void montarListaSelectItemConfiguracaoFinanceiro() {
		try {
		    List<ConfiguracaoFinanceiroVO> resultadoConsulta = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigo(0, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		    getListaSelectItemConfiguracaoFinanceira().clear();
		    List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
		    SelectItemOrdemValor ordenador = new SelectItemOrdemValor();

		    if (resultadoConsulta != null && !resultadoConsulta.isEmpty()) {
				listaDeSelectItem.add(new SelectItem(0, ""));
				for (ConfiguracaoFinanceiroVO obj : resultadoConsulta) {
				    listaDeSelectItem.add(new SelectItem(obj.getCodigo(), obj.getConfiguracoesVO().getNome()));
				}
				Collections.sort(listaDeSelectItem, ordenador);
		    }
		    getListaSelectItemConfiguracaoFinanceira().addAll(listaDeSelectItem);
	
		    Uteis.liberarListaMemoria(resultadoConsulta);
		} catch (ConsistirException e) {
		    setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void selecionarConfiguracaoFinanceira() {
		try {
		    getListaSelectItemUnidadeEnsino().clear();
		    if (Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO().getCodigo())) {
				getConfiguracaoRecebimentoCartaoOnlineVO().setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorChavePrimaria(getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>();
				montarListaSelectItemUnidadeEnsinoEditar(unidadeEnsinoVOs);
		    } else {
		    	getConfiguracaoRecebimentoCartaoOnlineVO().setConfiguracaoFinanceiroVO(new ConfiguracaoFinanceiroVO());
		    }
		} catch (Exception e) {
		    setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    // Getters and Setters
    public List<SelectItem> getListaSelectItemCampoConsulta() {
		if (listaSelectItemCampoConsulta == null) {
		    listaSelectItemCampoConsulta = new ArrayList<SelectItem>();
		    listaSelectItemCampoConsulta.add(new SelectItem("descricao", "Descrição"));
		    listaSelectItemCampoConsulta.add(new SelectItem("unidadeensino", "Unidade de Ensino"));
		    listaSelectItemCampoConsulta.add(new SelectItem("nivelEducacional", "Nível Educacional"));
		    listaSelectItemCampoConsulta.add(new SelectItem("curso", "Curso"));
		    listaSelectItemCampoConsulta.add(new SelectItem("turma", "Turma"));
		}
		return listaSelectItemCampoConsulta;
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrRegistroInterno", "Número (Registro Interno)"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
		itens.add(new SelectItem("nivelEducacional", "Nível Educacional"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null)
		    listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		return listaSelectItemUnidadeEnsino;
    }

    public List<SelectItem> getComboBoxConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
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

    public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
		    valorConsultaTurma = "";
		}
		return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
    	this.valorConsultaTurma = valorConsultaTurma;
    }

    public List<TurmaVO> getTurmaVOs() {
		if (turmaVOs == null) {
		    turmaVOs = new ArrayList<TurmaVO>(0);
		}
		return turmaVOs;
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

    public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
		    valorConsultaCurso = "";
		}
		return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
    	this.valorConsultaCurso = valorConsultaCurso;
    }

    public CursoVO getCursoVO() {
		if (cursoVO == null) {
		    cursoVO = new CursoVO();
		}
		return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
    	this.cursoVO = cursoVO;
    }

    public SituacaoEnum getSituacao() {
		if (situacao == null)
		    situacao = SituacaoEnum.EM_CONSTRUCAO;
		return situacao;
    }

    public void setSituacao(SituacaoEnum situacao) {
    	this.situacao = situacao;
    }

    public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if (configuracaoRecebimentoCartaoOnlineVO == null)
		    configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		return configuracaoRecebimentoCartaoOnlineVO;
    }

    public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
    	this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
    }

    public ConfiguracaoFinanceiroCartaoRecebimentoVO getConfiguracaoFinanceiroCartaoRecebimentoVO() {
		if (configuracaoFinanceiroCartaoRecebimentoVO == null)
		    configuracaoFinanceiroCartaoRecebimentoVO = new ConfiguracaoFinanceiroCartaoRecebimentoVO();
		return configuracaoFinanceiroCartaoRecebimentoVO;
    }

    public void setConfiguracaoFinanceiroCartaoRecebimentoVO(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO) {
    	this.configuracaoFinanceiroCartaoRecebimentoVO = configuracaoFinanceiroCartaoRecebimentoVO;
    }

    public List<SelectItem> getListaSelectItemNivelConfiguracao() {
		if (listaSelectItemNivelConfiguracao == null) {
		    listaSelectItemNivelConfiguracao = new ArrayList<SelectItem>();
		    listaSelectItemNivelConfiguracao = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectItemNivelConfiguracao;
    }

    public boolean getIsNaoPermitirAlterarQuandoAtivado() {
    	return getConfiguracaoRecebimentoCartaoOnlineVO().getSituacao().equals(SituacaoEnum.ATIVO) || getConfiguracaoRecebimentoCartaoOnlineVO().getSituacao().equals(SituacaoEnum.INATIVO);
    }    

    public boolean getIsApresentarBooleanos() {
    	return getConfiguracaoRecebimentoCartaoOnlineVO().getUsarConfiguracaoVisaoAdministrativa() || getConfiguracaoRecebimentoCartaoOnlineVO().getUsarConfiguracaoVisaoAlunoPais();
    }

    public List<SelectItem> getComboboxSituacaoEnum() {
		if (comboboxSituacaoEnum == null) {
		    comboboxSituacaoEnum = new ArrayList<SelectItem>();
		    comboboxSituacaoEnum.add(new SelectItem(SituacaoEnum.ATIVO.getName(), SituacaoEnum.ATIVO.getValorApresentar()));
		    comboboxSituacaoEnum.add(new SelectItem(SituacaoEnum.INATIVO.getName(), SituacaoEnum.INATIVO.getValorApresentar()));
		    comboboxSituacaoEnum.add(new SelectItem(SituacaoEnum.EM_CONSTRUCAO.getName(), SituacaoEnum.EM_CONSTRUCAO.getValorApresentar()));
		}
		return comboboxSituacaoEnum;
    }

    public List<SelectItem> getListaSelectItemConfiguracaoFinanceira() {
		if (listaSelectItemConfiguracaoFinanceira == null) {
		    listaSelectItemConfiguracaoFinanceira = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoFinanceira;
    }

    public void setListaSelectItemConfiguracaoFinanceira(List<SelectItem> listaSelectItemConfiguracaoFinanceira) {
    	this.listaSelectItemConfiguracaoFinanceira = listaSelectItemConfiguracaoFinanceira;
    }
    
    public List<SelectItem> getComboBoxConsultaComboFormaPadraoDataBaseCartaoRecorrente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO, FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO.getValorApresentar()));
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO, FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO.getValorApresentar()));
		return itens;
    }
    
    public void inicializarDadosPersonalizarMensagemRejeicao() {
    	context().getExternalContext().getSessionMap().put("TEMPLATE", TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PAGAMENTO_RECORRENCIA_DCC_NAO_REALIZADO);
    }
    
    public void inicializarDadosPersonalizarMensagemAutorizacao() {
    	context().getExternalContext().getSessionMap().put("TEMPLATE", TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PAGAMENTO_RECORRENCIA_DCC_REALIZADO_COM_SUCESSO);
    }
}
