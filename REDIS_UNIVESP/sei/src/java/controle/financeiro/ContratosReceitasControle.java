package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas contratosDespesasForm.jsp
 * contratosDespesasCons.jsp) com as funcionalidades da classe <code>ContratosDespesas</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ContratosDespesas
 * @see ContratosDespesasVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContratoReceitaEspecificoVO;
import negocio.comuns.financeiro.ContratosReceitasVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.ContratoReceitaSituacaoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("ContratosReceitasControle")
@Scope("viewScope")
@Lazy
public class ContratosReceitasControle extends SuperControle implements Serializable {

	protected ContratosReceitasVO contratosReceitasVO;
	protected List<ContaCorrenteVO> listaSelectItemContaCorrente;
	protected List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino;
	protected List<CentroReceitaVO> listaConsultaCentroReceita;
	protected String valorConsultaCentroReceita;
	protected String campoConsultaCentroReceita;
	protected String valorConsultaFuncionario;
	protected String campoConsultaFuncionario;
	protected List<FuncionarioVO> listaConsultaFuncionario;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	protected List<PessoaVO> listaConsultaAluno;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	protected List<ParceiroVO> listaConsultaParceiro;
	protected String campoConsultaTipoSacado;
	protected int numParcela = 0;
	private String aprovarData = "";
	private Boolean permitirAlterarDataTermino;
	private Double novoValorParcelas;
	private ContratoReceitaEspecificoVO contratoReceitaEspecificoVO;
	protected boolean desabilitarComboSituacao = false;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	private Boolean permitirEditarCampoUnidadeEnsino;
	private String motivoAlteracao;
	protected List<SelectItem> listaSelectItemDepartamento;

	public ContratosReceitasControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ContratosDespesas</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setContratosReceitasVO(new ContratosReceitasVO());
		permitirAlterarDataTermino = !this.getIsApresentarCampos();
		contratosReceitasVO.inicializarUnidadeEnsinoLogado(getUnidadeEnsinoLogadoClone());
		setContratoReceitaEspecificoVO(new ContratoReceitaEspecificoVO());
		inicializarListasSelectItemTodosComboBox();
		setMotivoAlteracao("");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ContratosDespesas</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		ContratosReceitasVO obj = (ContratosReceitasVO) context().getExternalContext().getRequestMap().get("contratosReceitasItens");
		try {
			obj = getFacadeFactory().getContratosReceitasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setContratosReceitasVO(obj);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasCons.xhtml");
		}
		setMotivoAlteracao("");
		setContratoReceitaEspecificoVO(new ContratoReceitaEspecificoVO());
		inicializarListasSelectItemTodosComboBox();
		if (getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO)) {
			setMensagemDetalhada("Os dados deste CONTRATO DE RECEITA NÃO PODEM MAIS SEREM ALTERADOS. O mesmo só pode ser FINALIZADO.");
			setDesabilitarComboSituacao(false);
		} else {
			if (getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.FINALIZADO)) {
				setMensagemDetalhada("Os dados deste CONTRATO DE DESPESA NÃO PODEM MAIS SEREM ALTERADOS, pois o mesmo já está FINALIZADO.");
				setDesabilitarComboSituacao(true);
			} else {
				setMensagemID("msg_dados_editar");
			}
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ContratosDespesas</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (contratosReceitasVO.isNovoObj().booleanValue()) {
				contratosReceitasVO.setSituacao(ContratoReceitaSituacaoEnum.ATIVO);
				getFacadeFactory().getContratosReceitasFacade().incluir(contratosReceitasVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			} else {
				getFacadeFactory().getContratosReceitasFacade().alterar(contratosReceitasVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			getContratosReceitasVO().reiniciarControleBloqueioCompetencia();
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
		} catch (Exception e) {
			if (contratosReceitasVO.getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO)) {
				contratosReceitasVO.setSituacao(ContratoReceitaSituacaoEnum.EM_CONSTRUCAO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
		}
	}

	public String finalizar() {
		try {
			getContratosReceitasVO().setSituacao(ContratoReceitaSituacaoEnum.FINALIZADO);
			getFacadeFactory().getContratosReceitasFacade().alterarFinalizacaoContratoReceitasPorCodigoReceita(getContratosReceitasVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			getContratosReceitasVO().reiniciarControleBloqueioCompetencia();
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
		} catch (Exception e) {
			if (getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO)) {
				getContratosReceitasVO().setSituacao(ContratoReceitaSituacaoEnum.EM_CONSTRUCAO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
		}
	}

	public String alterarValorDasParcelasPendentes() {
		try {
			getFacadeFactory().getContratosReceitasFacade().alterarValorDasParcelasPendentes(getContratosReceitasVO(), getNovoValorParcelas(), getMotivoAlteracao(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			getContratosReceitasVO().reiniciarControleBloqueioCompetencia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ContratosDespesasCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List<ContratosReceitasVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
					throw new Exception("Informe apenas valores numéricos.");
	            }
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getContratosReceitasFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoContrato")) {
				objs = getFacadeFactory().getContratosReceitasFacade().consultarPorTipoContrato(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("contratante")) {
				objs = getFacadeFactory().getContratosReceitasFacade().consultarPorNomeSacado(getControleConsulta().getValorConsulta(), "", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("consultarPorIdentificadorCentroReceita")) {
				objs = getFacadeFactory().getContratosReceitasFacade().consultarPorIdentificadorCentroReceitaCentroReceita(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getContratosReceitasFacade().consultarPorMatriculaAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				if (getControleConsulta().getValorConsulta().equals("Ativo")) {
					getControleConsulta().setValorConsulta(ContratoReceitaSituacaoEnum.ATIVO.getValor());
				} else {
					getControleConsulta().setValorConsulta(ContratoReceitaSituacaoEnum.FINALIZADO.getValor());
				}
				objs = getFacadeFactory().getContratosReceitasFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ContratosDespesasVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getContratosReceitasFacade().excluir(contratosReceitasVO, getUsuarioLogado());
			setContratosReceitasVO(new ContratosReceitasVO());
			setContratoReceitaEspecificoVO(new ContratoReceitaEspecificoVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
		}
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					if (obj.getContaCaixa()) {
						objs.add(new SelectItem(obj.getCodigo(), "CC:" + obj.getNumero() + "-" + obj.getDigito()));
					} else {
						objs.add(new SelectItem(obj.getCodigo(), obj.getBancoAgenciaContaCorrente()));
					}
				}

			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarContaCorrentePorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false, false, getContratosReceitasVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
		return lista;
	}

	public void consultarCentroReceita() {
		try {
			List<CentroReceitaVO> objs = new ArrayList<CentroReceitaVO>(0);
			if (getCampoConsultaCentroReceita().equals("descricao")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroReceita(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroReceita(new ArrayList<CentroReceitaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		return itens;
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			this.getContratosReceitasVO().setParceiro(obj);
			getContratosReceitasVO().setSacado(obj.getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			getContratosReceitasVO().setFornecedor(obj);
			getContratosReceitasVO().setSacado(obj.getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboFornecedor;

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("codigo")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99999999999999', event);";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
			tipoConsultaComboFornecedor.add(new SelectItem("codigo", "codigo"));
		}
		return tipoConsultaComboFornecedor;
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("codigo")) {
				if (getValorConsultaFornecedor().equals("")) {
					setValorConsultaFornecedor("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFornecedor());
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			getContratosReceitasVO().setPessoa(getFacadeFactory().getPessoaFacade().consultarFuncionarioPorMatricula(obj.getMatricula(), "FUNCIONARIO", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getContratosReceitasVO().setSacado(obj.getPessoa().getCodigo());
			getContratosReceitasVO().setParceiro(null);
			montarListaSelectItemDepartamento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			getContratosReceitasVO().setPessoa(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getContratosReceitasVO().setMatriculaVO(obj);
			getContratosReceitasVO().setSacado(obj.getAluno().getCodigo());
			getContratosReceitasVO().setParceiro(null);
			getContratosReceitasVO().setUnidadeEnsino(obj.getUnidadeEnsino());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCentroReceita() {
		try {
			CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
			this.getContratosReceitasVO().setCentroReceitaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {

			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());

			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "","", getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaAluno());
				objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("situacao")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeResponsavel")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);

		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);

		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));

		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>ContratoDespesaEspecifico</code> para o objeto <code>contratoDespesaVO</code> da classe <code>ContratoDespesa</code>
	 */
	public void adicionarContratoReceitaEspecifico() throws Exception {

		try {
			if (!getContratosReceitasVO().getCodigo().equals(0)) {
				contratoReceitaEspecificoVO.setContratoReceita(getContratosReceitasVO().getCodigo());
			}
			getContratosReceitasVO().adicionarObjContratoReceitaEspecificoVOs(getContratoReceitaEspecificoVO());
			this.setContratoReceitaEspecificoVO(new ContratoReceitaEspecificoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ContratoDespesaEspecifico</code> para edição pelo usuário.
	 */
	public String editarContratoReceitaEspecifico() throws Exception {
		ContratoReceitaEspecificoVO obj = (ContratoReceitaEspecificoVO) context().getExternalContext().getRequestMap().get("contratoReceitaEspecificoItens");
		setContratoReceitaEspecificoVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ContratoDespesaEspecifico</code> do objeto <code>contratoDespesaVO</code> da classe <code>ContratoDespesa</code>
	 */
	public String removerContratoReceitaEspecifico() throws Exception {
		ContratoReceitaEspecificoVO obj = (ContratoReceitaEspecificoVO) context().getExternalContext().getRequestMap().get("contratoReceitaEspecificoItens");
		getContratosReceitasVO().excluirObjContratoReceitaEspecificoVOs(obj.getNrParcela());
		setMensagemID("msg_dados_excluidos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasForm.xhtml");
	}

	public void irPaginaInicial() throws Exception {
		// controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>tipoContrato</code>
	 */
	public List<SelectItem> getListaSelectItemTipoContratoContratosReceitas() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("", ""));
		Hashtable tipoContratoDespesass = (Hashtable) Dominios.getTipoContratoDespesas();
		Enumeration keys = tipoContratoDespesass.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoContratoDespesass.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) objs, ordenador);
		return objs;
	}

	public List<SelectItem> getListaSelectItemSituacaoContratosReceitas() throws Exception {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(ContratoReceitaSituacaoEnum.ATIVO.getDescricao()));
		itens.add(new SelectItem(ContratoReceitaSituacaoEnum.FINALIZADO.getDescricao()));
		return itens;
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<ContaCorrenteVO> consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		List<ContaCorrenteVO> lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getContratosReceitasVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNumero(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarUnidadeEnsinoPorNumero(String numeroPrm) throws Exception {
		List lista = new ArrayList();
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			lista.add(getUnidadeEnsinoLogadoClone());
		} else {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(numeroPrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemContaCorrente();
		if(Uteis.isAtributoPreenchido(getContratosReceitasVO()) && getContratosReceitasVO().isTipoSacadoFuncionario()){
			montarListaSelectItemDepartamento();
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("contratante", "Contratante"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("tipoContrato", "Tipo Contrato"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public boolean isCampoSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

	public boolean isCampoTipoContrato() {
		if (getControleConsulta().getCampoConsulta().equals("tipoContrato")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosReceitasCons.xhtml");
	}

	public List<SelectItem> getListaSelectItemTipoSacado() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("AL", "Aluno"));
		objs.add(new SelectItem("FU", "Funcionário"));
		objs.add(new SelectItem("PA", "Parceiro"));
		objs.add(new SelectItem("FO", "Fornecedor"));
		return objs;
	}

	public void limparDadosSacado() {
		getContratosReceitasVO().setParceiro(null);
		getContratosReceitasVO().setPessoa(null);
		getContratosReceitasVO().setFornecedor(null);
		if (getContratosReceitasVO().getTipoSacado().equals("AL")) {
			setPermitirEditarCampoUnidadeEnsino(false);
		} else {
			setPermitirEditarCampoUnidadeEnsino(true);
		}
	}

	public boolean getSomenteLeitura() {
		return this.getContratosReceitasVO().getDadosBasicosDisponiveisSomenteConsulta();
	}

	public boolean getApresentarContratoIndeterminado() {
		try {
			return getContratosReceitasVO().getApresentarContratoIndeterminado();
		} catch (Exception e) {
			return false;
		}
	}

	public List consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	private void montarListaSelectItemDepartamento() throws Exception {
		getListaSelectItemDepartamento().clear();
		List<DepartamentoVO> listaDepartamento = new ArrayList<>();
		listaDepartamento.addAll(getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPessoaFuncionario(getContratosReceitasVO().getPessoa().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		listaDepartamento.stream().forEach(p -> getListaSelectItemDepartamento().add(new SelectItem(p.getCodigo(), p.getNome())));
		if(listaDepartamento.size() == 1){
			getContratosReceitasVO().getDepartamentoVO().setCodigo(listaDepartamento.get(0).getCodigo());
		}

	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		listaSelectItemDepartamento = Optional.ofNullable(listaSelectItemDepartamento).orElse(new ArrayList<>());
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public ContratosReceitasVO getContratosReceitasVO() {
		if (contratosReceitasVO == null) {
			contratosReceitasVO = new ContratosReceitasVO();
		}
		return contratosReceitasVO;
	}

	public void setContratosReceitasVO(ContratosReceitasVO contratosReceitasVO) {
		this.contratosReceitasVO = contratosReceitasVO;
	}

	public List<ContaCorrenteVO> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<ContaCorrenteVO>(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<ContaCorrenteVO> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<UnidadeEnsinoVO> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<CentroReceitaVO> getListaConsultaCentroReceita() {
		if (listaConsultaCentroReceita == null) {
			listaConsultaCentroReceita = new ArrayList<CentroReceitaVO>(0);
		}
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List<CentroReceitaVO> listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public String getValorConsultaCentroReceita() {
		if (valorConsultaCentroReceita == null) {
			valorConsultaCentroReceita = "";
		}
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public String getCampoConsultaCentroReceita() {
		if (campoConsultaCentroReceita == null) {
			campoConsultaCentroReceita = "";
		}
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public String getCampoConsultaTipoSacado() {
		if (campoConsultaTipoSacado == null) {
			campoConsultaTipoSacado = "";
		}
		return campoConsultaTipoSacado;
	}

	public void setCampoConsultaTipoSacado(String campoConsultaTipoSacado) {
		this.campoConsultaTipoSacado = campoConsultaTipoSacado;
	}

	public int getNumParcela() {
		return numParcela;
	}

	public void setNumParcela(int numParcela) {
		this.numParcela = numParcela;
	}

	public String getAprovarData() {
		return aprovarData;
	}

	public void setAprovarData(String aprovarData) {
		this.aprovarData = aprovarData;
	}

	public Boolean getPermitirAlterarDataTermino() {
		return permitirAlterarDataTermino;
	}

	public void setPermitirAlterarDataTermino(Boolean permitirAlterarDataTermino) {
		this.permitirAlterarDataTermino = permitirAlterarDataTermino;
	}

	public Double getNovoValorParcelas() {
		return novoValorParcelas;
	}

	public void setNovoValorParcelas(Double novoValorParcelas) {
		this.novoValorParcelas = novoValorParcelas;
	}

	public ContratoReceitaEspecificoVO getContratoReceitaEspecificoVO() {
		return contratoReceitaEspecificoVO;
	}

	public void setContratoReceitaEspecificoVO(ContratoReceitaEspecificoVO contratoReceitaEspecificoVO) {
		this.contratoReceitaEspecificoVO = contratoReceitaEspecificoVO;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<PessoaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<PessoaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getValorConsultaParceiro() {
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public boolean isDesabilitarComboSituacao() {
		return desabilitarComboSituacao;
	}

	public void setDesabilitarComboSituacao(boolean desabilitarComboSituacao) {
		this.desabilitarComboSituacao = desabilitarComboSituacao;
	}

	public Boolean getIsPermitirEditarCampoUnidadeEnsino() {
		if (permitirEditarCampoUnidadeEnsino == null) {
			permitirEditarCampoUnidadeEnsino = true;
		}
		return permitirEditarCampoUnidadeEnsino;
	}

	public void setPermitirEditarCampoUnidadeEnsino(Boolean permitirEditarCampoUnidadeEnsino) {
		this.permitirEditarCampoUnidadeEnsino = permitirEditarCampoUnidadeEnsino;
	}

	public boolean getIsApresentarBotaoGravar() {
		return (!getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.FINALIZADO) && !getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO));
	}

	public boolean getIsApresentarCampos() {
		return ((!getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.FINALIZADO)
				|| !getContratosReceitasVO().getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO))
				&& !getContratosReceitasVO().isNovoObj());
	}

	public String getMotivoAlteracao() {
		if (motivoAlteracao == null) {
			motivoAlteracao = "";
		}
		return motivoAlteracao;
	}

	public void setMotivoAlteracao(String motivoAlteracao) {
		this.motivoAlteracao = motivoAlteracao;
	}
	
	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getContratosReceitasVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);		
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getContratosReceitasVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.ARECEBER, this.getContratosReceitasVO().getDescricaoBloqueio(), this.getContratosReceitasVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getContratosReceitasVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioIncluirAlterarContaReceber", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 	}	
	
    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getContratosReceitasVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }		

}
