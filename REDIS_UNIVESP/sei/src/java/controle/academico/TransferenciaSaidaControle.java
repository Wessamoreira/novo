package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaSaidaVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.TransferenciaSaida;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas transferenciaSaidaForm.jsp transferenciaSaidaCons.jsp) com as
 * funcionalidades da classe <code>TransferenciaSaida</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see TransferenciaSaida
 * @see TransferenciaSaidaVO
 */
@Lazy
@Scope("viewScope")
@Controller("TransferenciaSaidaControle")
public class TransferenciaSaidaControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private TransferenciaSaidaVO transferenciaSaidaVO;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private Boolean imprimirContrato;
	private Integer textoPadraoDeclaracao;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private String abrirModalPanelHistorico;
	private Boolean realizarMarcacaoDesmarcacaoTodos;
	private Boolean permiteAlterarData;
	private UsuarioVO usuarioLiberarData;
	private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;

	public TransferenciaSaidaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TransferenciaSaida</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setTransferenciaSaidaVO(null);
		inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		verificaPermissaoAlterarData(getUsuarioLogadoClone());				
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaSaidaForm.xhtml");
	}


	public void verificaPermissaoAlterarData(UsuarioVO usuario) {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("TransSaida_permiteAlterarData", usuario);			
			setPermiteAlterarData(!getTransferenciaSaidaVO().getEstornado());			
		} catch (Exception e) {
			setPermiteAlterarData(Boolean.FALSE);
		}
	}
	
	public void executaDesbloqueioAlterarData() {
		setUsuarioLiberarData(null);
	}
	
    public void autenticarUsuario() {
        try {
            UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsuarioLiberarData().getUsername(), this.getUsuarioLiberarData().getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
            verificaPermissaoAlterarData(usuarioVerif);
            if (!getPermiteAlterarData()) {
            	throw new Exception("Usuário informado não possui permissão para realizar essa funcionalidade!");
            }
            getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.TRANSFERENCIA_SAIDA, getTransferenciaSaidaVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.ALTERAR_DATA_TRANSFERENCIA_SAIDA, usuarioVerif, ""));
            setMensagemID("msg_PermissaoUsuarioRealizarMatriculaDisciplinaPreRequisito");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }            
    }
    	
	public void inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado() {
		try {
			getTransferenciaSaidaVO().setResponsavelAutorizacao(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomeAluno")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAluno() throws Exception {
		setTransferenciaSaidaVO(null);
		setMensagemID("msg_entre_prmconsulta");
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			getTransferenciaSaidaVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			getFacadeFactory().getTransferenciaSaidaFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTransferenciaSaidaVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setTransferenciaSaidaVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>TransferenciaSaida</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			setTransferenciaSaidaVO((TransferenciaSaidaVO) context().getExternalContext().getRequestMap().get("transferenciaSaidaItens"));
			setTransferenciaSaidaVO(getFacadeFactory().getTransferenciaSaidaFacade().consultarPorChavePrimaria(getTransferenciaSaidaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			consultarListaSelectItemTipoTextoPadrao(getTransferenciaSaidaVO().getMatricula().getUnidadeEnsino().getCodigo());
			verificaPermissaoAlterarData(getUsuarioLogadoClone());			
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaSaidaForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TransferenciaSaida</code> . Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getTransferenciaSaidaVO().setResponsavelAutorizacao(getUsuarioLogadoClone());
			getFacadeFactory().getTransferenciaSaidaFacade().persistir(getTransferenciaSaidaVO(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			consultarListaSelectItemTipoTextoPadrao(getTransferenciaSaidaVO().getMatricula().getUnidadeEnsino().getCodigo());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TransferenciaSaidaCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String consultar() {
		try {
			super.consultar();
			if (!Uteis.isAtributoPreenchido(getControleConsulta().getValorConsulta()) && !getControleConsulta().getCampoConsulta().equals("codigo")) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			List<TransferenciaSaidaVO> objs = new ArrayList<TransferenciaSaidaVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoRequerimento")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorCodigoRequerimento(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("instituicaoDestino")) {
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorInstituicaoDestino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoJustificativa")) {
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorTipoJustificativa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getTransferenciaSaidaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaSaidaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaSaidaCons.xhtml");
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoJustificativa</code>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemTipoJustificativaTransferenciaSaida() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable tipoJustificativaAlteracaoMatriculas = (Hashtable) Dominios.getTipoJustificativaAlteracaoMatricula();
		Enumeration keys = tipoJustificativaAlteracaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoJustificativaAlteracaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemSituacaoTransferenciaSaida() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoTrancamentos = (Hashtable) Dominios.getSituacaoTrancamento();
		Enumeration keys = situacaoTrancamentos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoTrancamentos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Requerimento</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			getTransferenciaSaidaVO().setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(getTransferenciaSaidaVO().getCodigoRequerimento().getCodigo(), "TS", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			getTransferenciaSaidaVO().setMatricula(getTransferenciaSaidaVO().getCodigoRequerimento().getMatricula());
			getFacadeFactory().getTransferenciaSaidaFacade().validarTransferenciaSaida(getTransferenciaSaidaVO());
			getFacadeFactory().getTransferenciaSaidaFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTransferenciaSaidaVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTransferenciaSaidaVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			getTransferenciaSaidaVO().setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			getTransferenciaSaidaVO().setMatricula(getTransferenciaSaidaVO().getCodigoRequerimento().getMatricula());
			getFacadeFactory().getTransferenciaSaidaFacade().validarTransferenciaSaida(getTransferenciaSaidaVO());
			getFacadeFactory().getTransferenciaSaidaFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTransferenciaSaidaVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			getListaConsultaRequerimento().clear();
			setCampoConsultaRequerimento("");
			setValorConsultaRequerimento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTransferenciaSaidaVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			getTransferenciaSaidaVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getTransferenciaSaidaVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado()));
			getFacadeFactory().getTransferenciaSaidaFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTransferenciaSaidaVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTransferenciaSaidaVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
			tipoConsultaCombo.add(new SelectItem("matriculaMatricula", "Matrícula"));
			tipoConsultaCombo.add(new SelectItem("aluno", "Aluno"));
			tipoConsultaCombo.add(new SelectItem("nomePessoa", "Responsável Autorização"));
			tipoConsultaCombo.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
			tipoConsultaCombo.add(new SelectItem("instituicaoDestino", "Instituição Destino"));
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaSaidaCons.xhtml");
	}

	public TransferenciaSaidaVO getTransferenciaSaidaVO() {
		if (transferenciaSaidaVO == null) {
			transferenciaSaidaVO = new TransferenciaSaidaVO();
		}
		return transferenciaSaidaVO;
	}

	public void setTransferenciaSaidaVO(TransferenciaSaidaVO transferenciaSaidaVO) {
		this.transferenciaSaidaVO = transferenciaSaidaVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		setTransferenciaSaidaVO(null);
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the imprimirContrato
	 */
	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = false;
		}
		return imprimirContrato;
	}

	/**
	 * @param imprimirContrato
	 *            the imprimirContrato to set
	 */
	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}
	
	public List<ImpressaoDeclaracaoVO> getListaImpressaoDeclaracaoVOs() {
		if (listaImpressaoDeclaracaoVOs == null) {
			listaImpressaoDeclaracaoVOs = new ArrayList<ImpressaoDeclaracaoVO>(0);
		}
		return listaImpressaoDeclaracaoVOs;
	}

	public void setListaImpressaoDeclaracaoVOs(List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs) {
		this.listaImpressaoDeclaracaoVOs = listaImpressaoDeclaracaoVOs;
	}

	/**
	 * @return the textoPadraoDeclaracao
	 */
	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	/**
	 * @param textoPadraoDeclaracao
	 *            the textoPadraoDeclaracao to set
	 */
	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	/**
	 * @return the listaSelectItemTipoTextoPadrao
	 */
	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	/**
	 * @param listaSelectItemTipoTextoPadrao
	 *            the listaSelectItemTipoTextoPadrao to set
	 */
	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	/**
	 * @return the abrirModalPanelHistorico
	 */
	public String getAbrirModalPanelHistorico() {
		if (abrirModalPanelHistorico == null) {
			abrirModalPanelHistorico = "";
		}
		return abrirModalPanelHistorico;
	}

	/**
	 * @param abrirModalPanelHistorico
	 *            the abrirModalPanelHistorico to set
	 */
	public void setAbrirModalPanelHistorico(String abrirModalPanelHistorico) {
		this.abrirModalPanelHistorico = abrirModalPanelHistorico;
	}

	/**
	 * @return the realizarMarcacaoDesmarcacaoTodos
	 */
	public Boolean getRealizarMarcacaoDesmarcacaoTodos() {
		if (realizarMarcacaoDesmarcacaoTodos == null) {
			realizarMarcacaoDesmarcacaoTodos = false;
		}
		return realizarMarcacaoDesmarcacaoTodos;
	}

	/**
	 * @param realizarMarcacaoDesmarcacaoTodos
	 *            the realizarMarcacaoDesmarcacaoTodos to set
	 */
	public void setRealizarMarcacaoDesmarcacaoTodos(Boolean realizarMarcacaoDesmarcacaoTodos) {
		this.realizarMarcacaoDesmarcacaoTodos = realizarMarcacaoDesmarcacaoTodos;
	}

	/**
	 * Responsável por executar a marcação e a desmarcação dos históricos que
	 * serão realizados a Transferência de Saída.
	 * 
	 * @author Wellington Rodrigues - 02/04/2015
	 */
	public void executarMarcacaoDesmarcacaoTodos() {
		for (HistoricoVO obj : getTransferenciaSaidaVO().getHistoricoVOs()) {
			if (obj.getEditavel()) {
				obj.setRealizarAlteracaoSituacaoHistorico(getRealizarMarcacaoDesmarcacaoTodos());
			}
		}
	}

	public void iniciarlizarDadosParaImpressao(){
		try {
			getListaImpressaoDeclaracaoVOs().clear();
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void visualizarImpressaoDeclaracaoAluno() {
		try {
			getFacadeFactory().getTransferenciaSaidaFacade().validarDadosAntesImpressao(getTransferenciaSaidaVO(), getTextoPadraoDeclaracao());
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaPorTextoPadrao(getTransferenciaSaidaVO().getMatricula().getMatricula(), getTextoPadraoDeclaracao(), TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void impressaoDeclaracaoContratoJaGerada() {
		ImpressaoDeclaracaoVO obj = (ImpressaoDeclaracaoVO) context().getExternalContext().getRequestMap().get("impressaoDeclaracaoItens");
		try {
			limparMensagem();
			this.setCaminhoRelatorio("");
			ImpressaoContratoVO impressaoContrato = new ImpressaoContratoVO();
			impressaoContrato.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContrato.setGerarNovoArquivoAssinado(false);
			impressaoContrato.setMatriculaVO(obj.getMatricula());
			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContrato, obj.getTextoPadraoDeclaracao(), "", true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setImprimirContrato(false);
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setImprimirContrato(false);
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDF2() throws Exception {
		try {
			getFacadeFactory().getTransferenciaSaidaFacade().validarDadosAntesImpressao(getTransferenciaSaidaVO(), getTextoPadraoDeclaracao());
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getTransferenciaSaidaFacade().imprimirDeclaracaoTransferenciaSaida(getTransferenciaSaidaVO(), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			if (getCaminhoRelatorio().isEmpty()) {
				setImprimirContrato(true);
				setFazerDownload(false);
			} else {
				setImprimirContrato(false);
				setFazerDownload(true);
			}
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setImprimirContrato(false);
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545); RichFaces.$('panelTextoPadraoDeclaracao').hide()";
		} else if (getFazerDownload()) {
			return getDownload();
		}
		return "";
	}	

	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
		try {
			getListaSelectItemTipoTextoPadrao().clear();
			List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("TS", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(textoPadraoDeclaracaoVOs, "codigo", "descricao"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Responsável por executar a montagem dos históricos para realizar
	 * alteração da situação de acordo com a ultima matrícula período cuja
	 * situação seja AT ou PR.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	public void executarMontagemHistoricosParaRealizarAlteracaoSituacao() {
		try {
			setAbrirModalPanelHistorico("");
			getTransferenciaSaidaVO().setHistoricoVOs(getFacadeFactory().getTrancamentoFacade().executarMontagemHistoricosParaRealizarAlteracaoSituacao(getTransferenciaSaidaVO().getMatricula(), null, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
			if (Uteis.isAtributoPreenchido(getTransferenciaSaidaVO().getHistoricoVOs())) {
				setAbrirModalPanelHistorico("RichFaces.$('panelHistorico').show()");
			} else {
				getFacadeFactory().getTransferenciaSaidaFacade().persistir(getTransferenciaSaidaVO(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				consultarListaSelectItemTipoTextoPadrao(getTransferenciaSaidaVO().getMatricula().getUnidadeEnsino().getCodigo());
				setMensagemID("msg_dados_gravados");
			}
		} catch (Exception e) {
			setAbrirModalPanelHistorico("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPermiteAlterarData() {
		if (permiteAlterarData == null) {
			permiteAlterarData = Boolean.FALSE;
		}
		return permiteAlterarData;
	}

	public void setPermiteAlterarData(Boolean permiteAlterarData) {
		this.permiteAlterarData = permiteAlterarData;
	}

	public UsuarioVO getUsuarioLiberarData() {
		if (usuarioLiberarData == null) {
			usuarioLiberarData = new UsuarioVO();
		}
		return usuarioLiberarData;
	}

	public void setUsuarioLiberarData(UsuarioVO usuarioLiberarData) {
		this.usuarioLiberarData = usuarioLiberarData;
	}

	public boolean getApresentarBotaoEstornar(){
		return Uteis.isAtributoPreenchido(getTransferenciaSaidaVO().getCodigo()) && !getTransferenciaSaidaVO().getEstornado() && getLoginControle().getPermissaoAcessoMenuVO().getPermiteEstornarTransferenciaSaida();
	} 
	
	public void executarEstorno(){
		try{
			getFacadeFactory().getTransferenciaSaidaFacade().realizarEstornoTransferenciaSaida(getTransferenciaSaidaVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTransferenciaSaidaVO().getMatricula().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setPermiteAlterarData(false);
			setMensagemID("msg_estornoRealizado", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
}
