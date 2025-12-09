package controle.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.faces. model.SelectItem;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SolicitarAlterarSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoUsuario;

@Controller("SolicitarAlterarSenhaControle")
@Scope("viewScope")
@Lazy
public class SolicitarAlterarSenhaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private SolicitarAlterarSenhaVO solicitarAlterarSenhaVO;
	private String valorConsultaTipoUsuario;
	private String campoConsultarCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDepartamento;
	private Date dataInicial;
	private Date dataFinal;
	private Integer qtdeTotalUsuarioSolicitarNovaSenha;
	ProgressBarVO progressBarVO =  new ProgressBarVO();

	public SolicitarAlterarSenhaControle() throws Exception {
		setSolicitarAlterarSenhaVO(new SolicitarAlterarSenhaVO());
	}

	public String novo() {
		setSolicitarAlterarSenhaVO(new SolicitarAlterarSenhaVO());
		getSolicitarAlterarSenhaVO().setResponsavel(getUsuarioLogadoClone());
		montarListaSelectItemUnidadeEnsino();
		if(Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado())){
			getSolicitarAlterarSenhaVO().setUnidadeEnsinoVO(unidadeEnsinoVO);
		}
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAlterarSenhaForm");
	}

	public String inicializarConsulta() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAlterarSenhaCons");
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().consultar(getDataInicial(), getDataFinal(), getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado(), getSolicitarAlterarSenhaVO(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().consultarTotal(getDataInicial(), getDataFinal(), getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true, getUsuarioLogado(), getSolicitarAlterarSenhaVO()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAlterarSenhaCons");
	}

	public String editar() {
		try {
			SolicitarAlterarSenhaVO obj = ((SolicitarAlterarSenhaVO) context().getExternalContext().getRequestMap().get("solicitarAlterarSenhaControleItens"));
			setSolicitarAlterarSenhaVO(getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().consultarSolicitarAlterarSenhaPorCodigo(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaSelectItemUnidadeEnsino();
			if(obj.getTipoUsuario().getValor().equals("FU")){
				montarListaSelectItemDepartamento();
			}
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAlterarSenhaForm");
	}

	public void gravar() {
		setProgressBarVO(new ProgressBarVO());	
		try {
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().iniciar(0l, getQtdeTotalUsuarioSolicitarNovaSenha(), "Iniciando....", false, null, "");			

			getSolicitarAlterarSenhaVO().setSolicitarNovaSenha(true);
			getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().realizarSolicitacaoNovaSenhaUsuario(getProgressBarVO(), getSolicitarAlterarSenhaVO(), getQtdeTotalUsuarioSolicitarNovaSenha(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluir() {
		setProgressBarVO(new ProgressBarVO());	
		try {
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().iniciar(0l, getQtdeTotalUsuarioSolicitarNovaSenha(), "Iniciando....", false, null, "");		
			
			getSolicitarAlterarSenhaVO().setSolicitarNovaSenha(false);
			getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().realizarExclusaoSolicitacaoNovaSenhaUsuario(getProgressBarVO(), getSolicitarAlterarSenhaVO(), getQtdeTotalUsuarioSolicitarNovaSenha(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO());

			getProgressBarVO().setForcarEncerramento(true);
			setProgressBarVO(new ProgressBarVO());	
			novo();
			setMensagemID("msg_dados_excluidos");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			//System.out.print("nome");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemDepartamento() throws Exception {
		List<DepartamentoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarDepartamentoPorNome("");
			setListaSelectItemDepartamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	@SuppressWarnings("unchecked")
	private List<DepartamentoVO> consultarDepartamentoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("responsavel", "Responsável Solicitação"));
		objs.add(new SelectItem("data", "Data Solicitação"));
		objs.add(new SelectItem("tipoUsuario", "Tipo Usuário"));
		return objs;
	}

	/**
	 * Verifica se o campo selecionado para pesquisa é do tipo data.
	 *
	 * @return boolean
	 */
	public boolean getIsApresentarCampoData() {
		return getControleConsulta().getCampoConsulta().equals("data");
	}

	/**
	 * Verifica se o campo selecionado para pesquisa é do tipo tipoUsuario.
	 *
	 * @return boolean
	 */
	public Boolean getApresentarComboTipoUsuario() {
		return getControleConsulta().getCampoConsulta().equals("tipoUsuario");
	}

	public Boolean getIsApresentarCurso() {
		return getSolicitarAlterarSenhaVO().getTipoUsuario().getValor().equals("AL") || getSolicitarAlterarSenhaVO().getTipoUsuario().getValor().equals("PR") || getSolicitarAlterarSenhaVO().getTipoUsuario().getValor().equals("CO");
	}

	public Boolean getIsApresentarDepartamento() {
		return getSolicitarAlterarSenhaVO().getTipoUsuario().getValor().equals("FU");
	}

	public Boolean getIsEnviarEmail() {
		return getSolicitarAlterarSenhaVO().getEnviarEmail();
	}

	public SolicitarAlterarSenhaVO getSolicitarAlterarSenhaVO() {
		if (solicitarAlterarSenhaVO == null) {
			solicitarAlterarSenhaVO = new SolicitarAlterarSenhaVO();
		}
		return solicitarAlterarSenhaVO;
	}

	public void setSolicitarAlterarSenhaVO(SolicitarAlterarSenhaVO solicitarAlterarSenhaVO) {
		this.solicitarAlterarSenhaVO = solicitarAlterarSenhaVO;
	}

	public Date getDataInicial() {
		if (dataInicial == null) {
			dataInicial = new Date();
		}
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		if (dataFinal == null) {
			dataFinal = new Date();
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<SelectItem> getListaSelectItemTipoUsuario() {
		List<SelectItem> listaSelectItemTipoUsuario = new ArrayList<SelectItem>(0);
		listaSelectItemTipoUsuario.add(new SelectItem("", ""));
		listaSelectItemTipoUsuario.add(new SelectItem(TipoUsuario.ALUNO, TipoUsuario.ALUNO.getDescricao()));
		listaSelectItemTipoUsuario.add(new SelectItem(TipoUsuario.PROFESSOR, TipoUsuario.PROFESSOR.getDescricao()));
		listaSelectItemTipoUsuario.add(new SelectItem(TipoUsuario.COORDENADOR, TipoUsuario.COORDENADOR.getDescricao()));
		listaSelectItemTipoUsuario.add(new SelectItem(TipoUsuario.FUNCIONARIO, TipoUsuario.FUNCIONARIO.getDescricao()));
		return listaSelectItemTipoUsuario;
	}

	public void consultarCurso() {
		try {
			if (getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo().equals(null) || getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_unidadeEnsino"));
			}
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultarCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaSelectItemCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSelectItemCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void limparCurso() throws Exception {
		try {
			getSolicitarAlterarSenhaVO().setCursoVO(new CursoVO());
			setCampoConsultarCurso(null);
			setValorConsultaCurso(null);
			setListaSelectItemCurso(new ArrayList<CursoVO>(0));
		} catch (Exception e) {
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getSolicitarAlterarSenhaVO().setCursoVO(obj);
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
		tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		return tipoConsultaComboCurso;
	}

	public String getValorConsultaTipoUsuario() {
		if (valorConsultaTipoUsuario == null) {
			valorConsultaTipoUsuario = "AL";
		}
		return valorConsultaTipoUsuario;
	}

	public void setValorConsultaTipoUsuario(String valorConsultaTipoUsuario) {
		this.valorConsultaTipoUsuario = valorConsultaTipoUsuario;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultarCurso() {
		if (campoConsultarCurso == null) {
			campoConsultarCurso = "";
		}
		return campoConsultarCurso;
	}

	public void setCampoConsultarCurso(String campoConsultarCurso) {
		this.campoConsultarCurso = campoConsultarCurso;
	}

	public List<CursoVO> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<CursoVO>(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<CursoVO> objs) {
		this.listaSelectItemCurso = objs;
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

	public List<SelectItem> getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public void montarDadosListaSelectItemDepartamento() {
		try {
			if (getSolicitarAlterarSenhaVO().getTipoUsuario().getValor().equals("FU")) {
				montarListaSelectItemDepartamento();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener() throws Exception {
		
		
		consultar();
	}
	
	public Boolean getIsApresentarBotaoGravar(){
		return getSolicitarAlterarSenhaVO().isNovoObj();
	}
	
	public Integer getQtdeTotalUsuarioSolicitarNovaSenha() {
		if(qtdeTotalUsuarioSolicitarNovaSenha == null) {
			qtdeTotalUsuarioSolicitarNovaSenha = 0;
		}
		return qtdeTotalUsuarioSolicitarNovaSenha;
	}

	public void setQtdeTotalUsuarioSolicitarNovaSenha(Integer qtdeTotalUsuarioSolicitarNovaSenha) {
		this.qtdeTotalUsuarioSolicitarNovaSenha = qtdeTotalUsuarioSolicitarNovaSenha;
	}

	public void consultarTotalUsuarioParaSolicitarNovaSenha() throws Exception{
		Integer qtdeUsuarioAlterarSenha = 0;

			if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.ALUNO)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioAlunoPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getCursoVO().getCodigo(), false);
			}
			else if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioProfessorPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getCursoVO().getCodigo(), false);
			}
			else if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.COORDENADOR)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioCoordenadorPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getCursoVO().getCodigo(), false);
			}
			else if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.FUNCIONARIO)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioFuncionarioPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getDepartamentoVO().getCodigo(), false);
			}
			setQtdeTotalUsuarioSolicitarNovaSenha(qtdeUsuarioAlterarSenha); 			
		
	}
	
	public void consultarTotalUsuarioParaExcluirSolicitarNovaSenha() throws Exception{
		Integer qtdeUsuarioAlterarSenha = 0;

			if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.ALUNO)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioAlunoPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getCursoVO().getCodigo(), true);
			}
			else if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioProfessorPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getCursoVO().getCodigo(), true);
			}
			else if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.COORDENADOR)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioCoordenadorPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getCursoVO().getCodigo(), true);
			}
			else if(getSolicitarAlterarSenhaVO().getTipoUsuario().equals(TipoUsuario.FUNCIONARIO)) {
				qtdeUsuarioAlterarSenha = getFacadeFactory().getUsuarioFacade().consultarTotalUsuarioFuncionarioPorUnidadeEnsinoCurso(getSolicitarAlterarSenhaVO().getUnidadeEnsinoVO().getCodigo(), getSolicitarAlterarSenhaVO().getDepartamentoVO().getCodigo(), true);
			}
			setQtdeTotalUsuarioSolicitarNovaSenha(qtdeUsuarioAlterarSenha); 			
		
	}
	
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO =  new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public void atualizarStatusProcessamento() {
		setProgressBarVO(getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().consultarProgressBarAtivo());
	}
	
	/*public void realizarInicioProgressBar(){
		setProgressBarVO(new ProgressBarVO());		
		try {
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().iniciar(0l, getQtdeTotalUsuarioSolicitarNovaSenha(), "Iniciando Processamento do(s) resultado(s).", true, this, "gravar");			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	
	}*/
}
