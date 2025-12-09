package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PortadorDiplomaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("PortadorDiplomaControle")
@Scope("viewScope")
public class PortadorDiplomaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private PortadorDiplomaVO portadorDiplomaVO;
	private List<SelectItem> listaSelectItemTipoMidiaCaptacao;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<PessoaVO> listaConsultaAluno;
	private String valorConsultaPortadorDiploma;
	private String campoConsultaPortadorDiploma;
	private List<MatriculaVO> matriculaVOs;
	private String onCompleteSelecionarMatricula;
    private String valorConsultaCidade;
    private String campoConsultaCidade;
    private List<CidadeVO> listaConsultaCidade;

	public PortadorDiplomaControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PortadorDiploma</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		try {
			setPortadorDiplomaVO(new PortadorDiplomaVO());
			inicializarListasSelectItemTodosComboBox();
			getPortadorDiplomaVO().setResponsavelAutorizacao(getUsuarioLogado().getPessoa());
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaCons.xhtml");
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PortadorDiploma</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		PortadorDiplomaVO obj = (PortadorDiplomaVO) context().getExternalContext().getRequestMap().get("portadorDiplomaItens");
		inicializarListasSelectItemTodosComboBox();
		obj.setNovoObj(Boolean.FALSE);
		setPortadorDiplomaVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PortadorDiploma</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			getFacadeFactory().getPortadorDiplomaFacade().persistir(getPortadorDiplomaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaForm.xhtml");
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PortadorDiplomaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<PortadorDiplomaVO> objs = new ArrayList<PortadorDiplomaVO>(0);
			if (getCampoConsultaPortadorDiploma().equals("codigo")) {
				if (getValorConsultaPortadorDiploma().equals("")) {
					setValorConsultaPortadorDiploma("0");
				}
				if (getValorConsultaPortadorDiploma().trim() != null || !getValorConsultaPortadorDiploma().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaPortadorDiploma().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaPortadorDiploma());
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaPortadorDiploma().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaPortadorDiploma());
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaPortadorDiploma().equals("descricao")) {
				if (getValorConsultaPortadorDiploma().trim().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorDescricao(getValorConsultaPortadorDiploma(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			// if (getCampoConsultaPortadorDiploma().equals("situacao")) {
			// objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true,
			// Uteis.NIVELMONTARDADOS_TODOS);
			// }
			if (getCampoConsultaPortadorDiploma().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorMatriculaMatricula(getValorConsultaPortadorDiploma(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaPortadorDiploma().equals("nomeMatricula")) {
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorNomeMatricula(getValorConsultaPortadorDiploma(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}			
			if (getCampoConsultaPortadorDiploma().equals("codigoRequerimento")) {
				if (getValorConsultaPortadorDiploma().equals("")) {
					setValorConsultaPortadorDiploma("0");
				}
				if (getValorConsultaPortadorDiploma().trim() != null || !getValorConsultaPortadorDiploma().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaPortadorDiploma().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaPortadorDiploma());
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorCodigoRequerimento(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaPortadorDiploma().equals("nomePessoa")) {
				objs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorNomePessoa(getValorConsultaPortadorDiploma(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<Object>(0));
			if (e.getMessage().contains("Unparseable date:")) {
				setMensagemDetalhada("msg_erro", "Digite uma data válida.");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaCons.xhtml");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PortadorDiplomaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPortadorDiplomaFacade().excluir(portadorDiplomaVO, getUsuarioLogado());
			setPortadorDiplomaVO(new PortadorDiplomaVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaForm.xhtml");
	}

	public void consultarRequerimento() {
		try {
			List<RequerimentoVO> objs = new ArrayList<RequerimentoVO>(0);
			if (getCampoConsultaRequerimento().equals("codigo")) {
				int valorInt = Uteis.getValorInteiro(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(new Integer(valorInt), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomeTipoRequerimento")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeTipoRequerimento(getValorConsultaRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacao")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacao(getValorConsultaRequerimento(), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacaoFinanceira(getValorConsultaRequerimento(), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomePessoa")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomePessoa(getValorConsultaRequerimento(), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("cpfPessoa")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeCPFPessoa(getValorConsultaRequerimento(), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaMatricula(getValorConsultaRequerimento(), TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			setListaConsultaRequerimento(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRequerimento(new ArrayList<RequerimentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			getFacadeFactory().getPortadorDiplomaFacade().validarSituacaoRequerimento(obj);
			getPortadorDiplomaVO().setCodigoRequerimento(obj);
			this.getPortadorDiplomaVO().setMatricula(obj.getMatricula());
			if (obj.getMatricula().getMatricula().equals("")) {
				this.getPortadorDiplomaVO().getMatricula().setAluno(obj.getPessoa());				
			} else {
				this.getPortadorDiplomaVO().setMatricula(obj.getMatricula());
			}
			getPortadorDiplomaVO().setCurso(obj.getCurso().getNome());			
			boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(getPortadorDiplomaVO().getMatricula().getMatricula(), getUsuarioLogado());
			String mensagem = "";
			if (existePendenciaFinanceira) {
				mensagem = getPortadorDiplomaVO().getMatricula().validaMatriculaLiberadaFinanceiroCancelamentoTrancamento(true);
			}
			if (!mensagem.equals("")) {
				getPortadorDiplomaVO().setCodigoRequerimento(new RequerimentoVO());
				getPortadorDiplomaVO().setMatricula(new MatriculaVO());
				throw new Exception("O registro de Portador de Diploma não pode ser realizado. Realize a liberação financeira da matrícula e tente novamente!");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getPortadorDiplomaVO().setCodigoRequerimento(new RequerimentoVO());
			getPortadorDiplomaVO().setMatricula(new MatriculaVO());
		}
	}

	public void consultarAluno() {
		try {
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNome(getValorConsultaAluno(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorCPF(getValorConsultaAluno(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorRG(getValorConsultaAluno(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAluno() throws Exception {
		getPortadorDiplomaVO().setMatricula(new MatriculaVO());
	}

	public void selecionarAluno() throws Exception {
		try {
			getPortadorDiplomaVO().getMatricula().setAluno((PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens"));
			getFacadeFactory().getPessoaFacade().carregarDados(getPortadorDiplomaVO().getMatricula().getAluno(), getUsuarioLogado());
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			setListaConsultaAluno(new ArrayList<PessoaVO>(0));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			getPortadorDiplomaVO().setResponsavelAutorizacao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getPortadorDiplomaVO().getResponsavelAutorizacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave primária. Esta rotina é
	 * utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			getPortadorDiplomaVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getPortadorDiplomaVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemTipoMidiaCaptacao();
	}

	public String getMascaraConsulta() {
		if (getCampoConsultaPortadorDiploma().equals("data")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
		itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
		itens.add(new SelectItem("nomeMatricula", "Aluno"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("nomePessoa", "Responsavel Autorização"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("portadorDiplomaCons.xhtml");
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>TipoMidiaCaptacao</code>.
	 */
	public void montarListaSelectItemTipoMidiaCaptacao(String prm) throws Exception {
		List<TipoMidiaCaptacaoVO> resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia(prm);
		setListaSelectItemTipoMidiaCaptacao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nomeMidia"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>TipoMidiaCaptacao</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos
	 * dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTipoMidiaCaptacao() {
		try {
			montarListaSelectItemTipoMidiaCaptacao("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Requerimento</code> por meio de sua respectiva chave primária. Esta rotina é
	 * utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			RequerimentoVO requerimento = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(getPortadorDiplomaVO().getCodigoRequerimento().getCodigo(), "PO", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			getPortadorDiplomaVO().getCodigoRequerimento().setCodigo(requerimento.getCodigo());
			getPortadorDiplomaVO().getCodigoRequerimento().setSituacao(requerimento.getSituacao());
			getFacadeFactory().getPortadorDiplomaFacade().validarSituacaoRequerimento(requerimento);
			if (requerimento.getMatricula().getMatricula().equals("")) {
				getPortadorDiplomaVO().getMatricula().setAluno(requerimento.getPessoa());
			} else {
				getPortadorDiplomaVO().setMatricula(requerimento.getMatricula());
			}
			getPortadorDiplomaVO().setCurso(requerimento.getCurso().getNome());			
			boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(getPortadorDiplomaVO().getMatricula().getMatricula(), getUsuarioLogado());
			String mensagem = "";
			if (existePendenciaFinanceira) {
				mensagem = getPortadorDiplomaVO().getMatricula().validaMatriculaLiberadaFinanceiroCancelamentoTrancamento(true);
			}
			if (!mensagem.equals("")) {
				getPortadorDiplomaVO().setCodigoRequerimento(new RequerimentoVO());
				getPortadorDiplomaVO().setMatricula(new MatriculaVO());
				throw new Exception("O registro de Portador de Diploma não pode ser realizado. Realize a liberação financeira da matrícula e tente novamente!");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getPortadorDiplomaVO().getCodigoRequerimento().setCodigo(0);
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeMidia</code> Este atributo é uma lista (
	 * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<TipoMidiaCaptacaoVO> consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
		return getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
	}

	public String getMascaraConsultaAluno() {
		if (getCampoConsultaAluno().equals("CPF")) {
			return "return mascara(this.form,'formAluno:valorConsultaAluno','999.999.999-99',event)";
		}
		return "";
	}

	public String iniciarMatricula() throws Exception {
		try {
			MatriculaVO matricula = new MatriculaVO();
			matricula = getPortadorDiplomaVO().getMatricula();
			matricula.setFormaIngresso("PD");
			matricula.setGuiaAba("DadosBasicos");
			context().getExternalContext().getSessionMap().put("alunoPortadorDiploma", matricula.getAluno());
			context().getExternalContext().getSessionMap().put("codigoPortadorDiploma", getPortadorDiplomaVO());
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			return "";
		}
	}

	public PortadorDiplomaVO getPortadorDiplomaVO() {
		if (portadorDiplomaVO == null) {
			portadorDiplomaVO = new PortadorDiplomaVO();
		}
		return portadorDiplomaVO;
	}

	public void setPortadorDiplomaVO(PortadorDiplomaVO portadorDiplomaVO) {
		this.portadorDiplomaVO = portadorDiplomaVO;
	}

	public List<SelectItem> getListaSelectItemTipoMidiaCaptacao() {
		if (listaSelectItemTipoMidiaCaptacao == null) {
			listaSelectItemTipoMidiaCaptacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoMidiaCaptacao;
	}

	public void setListaSelectItemTipoMidiaCaptacao(List<SelectItem> listaSelectItemTipoMidiaCaptacao) {
		this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
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

	public String getCampoConsultaPortadorDiploma() {
		if (campoConsultaPortadorDiploma == null) {
			campoConsultaPortadorDiploma = "";
		}
		return campoConsultaPortadorDiploma;
	}

	public void setCampoConsultaPortadorDiploma(String campoConsultaPortadorDiploma) {
		this.campoConsultaPortadorDiploma = campoConsultaPortadorDiploma;
	}

	public String getValorConsultaPortadorDiploma() {
		if (valorConsultaPortadorDiploma == null) {
			valorConsultaPortadorDiploma = "";
		}
		return valorConsultaPortadorDiploma;
	}

	public void setValorConsultaPortadorDiploma(String valorConsultaPortadorDiploma) {
		this.valorConsultaPortadorDiploma = valorConsultaPortadorDiploma;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}

	public void consultarAlunoMatricula() {
		try {
			setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoa(getPortadorDiplomaVO().getMatricula().getAluno().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaVOs(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAlunoMatricula() {
		try {
			MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			List<PortadorDiplomaVO> portadorDiplomaVOs = getFacadeFactory().getPortadorDiplomaFacade().consultarPorMatriculaMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(portadorDiplomaVOs)) {
				throw new Exception(UteisJSF.internacionalizar("msg_PortadorDiploma_matriculaVinculadaRegistroDiploma").replace("{0}", matriculaVO.getMatricula()));
			}
			getPortadorDiplomaVO().setMatricula(matriculaVO);
			getFacadeFactory().getMatriculaFacade().carregarDados(getPortadorDiplomaVO().getMatricula(), getUsuarioLogado());
			setMatriculaVOs(new ArrayList<MatriculaVO>(0));
			setOnCompleteSelecionarMatricula("RichFaces.$('panelAlunoMatricula').hide()");
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setOnCompleteSelecionarMatricula("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getOnCompleteSelecionarMatricula() {
		if (onCompleteSelecionarMatricula == null) {
			onCompleteSelecionarMatricula = "";
		}
		return onCompleteSelecionarMatricula;
	}

	public void setOnCompleteSelecionarMatricula(String onCompleteSelecionarMatricula) {
		this.onCompleteSelecionarMatricula = onCompleteSelecionarMatricula;
	}

	public boolean getIsApresentarIniciarMatriculaEVincularMatriculaExistente() {
		return !getPortadorDiplomaVO().getCodigo().equals(0) && !getPortadorDiplomaVO().getNovoObj() && getPortadorDiplomaVO().getMatricula().getMatricula().equals("");
	}
	
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getPortadorDiplomaVO().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    public String getCampoConsultaCidade() {
        if (campoConsultaCidade == null) {
            campoConsultaCidade = "";
        }
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public List<CidadeVO> getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList<CidadeVO>(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("estado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparCidade() {
        getPortadorDiplomaVO().setCidade(null);
    }
    
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("estado", "Estado"));
        return itens;
    }

    
}


