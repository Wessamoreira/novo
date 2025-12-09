package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas censoForm.jsp censoCons.jsp) com
 * as funcionalidades da classe <code>Censo</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Censo
 * @see CensoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AlunoCensoVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CensoUnidadeEnsinoVO;
import negocio.comuns.academico.CensoVO;
import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.academico.ProfessorCensoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.Censo;

@Controller("CensoControle")
@Scope("viewScope")
@Lazy
public class CensoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private CensoVO censoVO;
	private String campoConsultarResponsavel;
	private String valorConsultarResponsavel;
	private List<SelectItem> listaConsultarResponsavel;
	private ProfessorCensoVO professorCenso;
	private String campoConsultarProfessor;
	private String valorConsultarProfessor;
	private List<PessoaVO> listaConsultarProfessor;
	private AlunoCensoVO alunoCenso;
	private String campoConsultarMatricula;
	private String valorConsultarMatricula;
	private List<SelectItem> listaConsultarMatricula;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private boolean isMostrarDownloadArquivos;
	private String unidadeEnsinoApresentar;
	private ProgressBarVO progressBarVO;

	public CensoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		inicializarListasSelectItemTodosComboBox();
		novo();
		setIsMostrarDownloadArquivos(false);
		setMensagemID("msg_entre_prmconsulta");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Censo</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			removerObjetoMemoria(this);
			setCensoVO(new CensoVO());
			getCensoVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			getCensoVO().getResponsavel().setNome(getUsuarioLogado().getNome());
			inicializarListasSelectItemTodosComboBox();			
			carregarDadosUnidadeEnsino();
			setAlunoCensoVO(new AlunoCensoVO());
			setProfessorCenso(new ProfessorCensoVO());
			setIsMostrarDownloadArquivos(Boolean.FALSE);
			getFacadeFactory().getCensoUnidadeEnsinoFacade().carregarUnidadeEnsinoNaoSelecionado(getCensoVO(), getUnidadeEnsinoLogado().getCodigo());
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("censoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Censo</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
			obj = getFacadeFactory().getCensoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			setCensoVO(obj);
			setAlunoCensoVO(new AlunoCensoVO());
			setProfessorCenso(new ProfessorCensoVO());
			getFacadeFactory().getCensoUnidadeEnsinoFacade().carregarUnidadeEnsinoNaoSelecionado(getCensoVO(), getUnidadeEnsinoLogado().getCodigo());
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("censoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("censoCons.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Censo</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public void persistir() {
		getProgressBarVO().setUsuarioVO(getUsuarioLogado());
		getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		getProgressBarVO().resetar();
		getProgressBarVO().iniciar(0l, 4, "Carregando dados...", true, this, "gravar");
	}
	
	public void gravar() {
		try {
			if (getCensoVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getCensoFacade().incluir(censoVO, getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO());
				setIsMostrarDownloadArquivos(Boolean.TRUE);
			}
			getProgressBarVO().getSuperControle().setMensagemID("msg_dados_gravados");
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);
//			return Uteis.getCaminhoRedirecionamentoNavegacao("censoForm.xhtml");
		} catch (Exception e) {
			getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
			getProgressBarVO().setForcarEncerramento(true);
//			return Uteis.getCaminhoRedirecionamentoNavegacao("censoForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CensoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCensoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("ano")) {
				objs = getFacadeFactory().getCensoFacade().consultarPorAno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("censoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("censoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>CensoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			CensoVO censo = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
			getFacadeFactory().getCensoFacade().excluir(censo, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			// novo();
			consultar();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("censoCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("censoCons.xhtml");
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio dos parametros informados no richmodal.
	 * Esta rotina é utilizada fundamentalmente por requisições Ajax, que
	 * realizam busca pelos parâmentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarMatricula() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarMatricula().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorMatricula(getValorConsultarMatricula(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultarMatricula(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultarMatricula(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultarMatricula());
				objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("situacao")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getValorConsultarMatricula(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultarMatricula(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsultarMatricula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarMatricula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCaminhoServidorDownloadArquivoAluno() {
		try {
			fazerDownloadArquivo(getCensoVO().getArquivoAluno());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadArquivoAlunoExcel() {
		try {
			fazerDownloadArquivo(getCensoVO().getArquivoAlunoExcel());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadArquivoProfessor() {
		try {
			fazerDownloadArquivo(getCensoVO().getArquivoProfessor());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

//	public String getCaminhoServidorDownloadArquivoAlunoTelaConsulta() {
//		try {
//			CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
//			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoAluno(), PastaBaseArquivoEnum.CENSO, getConfiguracaoGeralPadraoSistema());
//		} catch (Exception ex) {
//			setMensagemDetalhada("msg_erro", ex.getMessage());
//		}
//		return "";
//	}
	
	public void realizarDownloadArquivoAlunoTelaConsulta() {
		CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
		try {
			realizarDownloadArquivo(obj.getArquivoAluno());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public String getCaminhoServidorDownloadArquivoAlunoExcelTelaConsulta() {
//		try {
//			CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
//			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoAlunoExcel(), PastaBaseArquivoEnum.CENSO, getConfiguracaoGeralPadraoSistema());
//		} catch (Exception ex) {
//			setMensagemDetalhada("msg_erro", ex.getMessage());
//		}
//		return "";
//	}
	
	public void realizarDownloadArquivoAlunoExcelTelaConsulta() {
		CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
		try {
			realizarDownloadArquivo(obj.getArquivoAlunoExcel());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public String getCaminhoServidorDownloadArquivoProfessorTelaConsulta() {
//		try {
//			CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
//			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoProfessor(), PastaBaseArquivoEnum.CENSO, getConfiguracaoGeralPadraoSistema());
//		} catch (Exception ex) {
//			setMensagemDetalhada("msg_erro", ex.getMessage());
//		}
//		return "";
//	}
	
	public void realizarDownloadArquivoProfessorTelaConsulta() {
		CensoVO obj = (CensoVO) context().getExternalContext().getRequestMap().get("censoItens");
		try {
			realizarDownloadArquivo(obj.getArquivoProfessor());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String realizarDownloadArquivoAluno() {
		try {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			// request.setAttribute("codigoArquivo",
			// getCensoVO().getArquivoAluno().getCodigo());
			// request.setAttribute("urlAcessoArquivo",
			// getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getCensoVO().getArquivoAluno(),
			// PastaBaseArquivoEnum.CENSO, getConfiguracaoGeralPadraoSistema(),
			// request.getRemoteAddr()));
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("Erro");
		}
		return "";
	}

	public String realizarDownloadArquivoProfessor() {
		try {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			// request.setAttribute("codigoArquivo",
			// getCensoVO().getArquivoProfessor().getCodigo());
			// request.setAttribute("urlAcessoArquivo",
			// getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getCensoVO().getArquivoProfessor(),
			// PastaBaseArquivoEnum.CENSO, getConfiguracaoGeralPadraoSistema(),
			// request.getRemoteAddr()));
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("Erro");
		}
		return "";
	}

	public String realizarDownloadArquivoAlunoExcel() {
		try {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			// request.setAttribute("codigoArquivo",
			// getCensoVO().getArquivoProfessor().getCodigo());
			// request.setAttribute("urlAcessoArquivo",
			// getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getCensoVO().getArquivoProfessor(),
			// PastaBaseArquivoEnum.CENSO, getConfiguracaoGeralPadraoSistema(),
			// request.getRemoteAddr()));
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> lista =  new ArrayList<UnidadeEnsinoVO>(0);
			if(getCensoVO().getLayout().equals(Censo.GRADUACAO) || getCensoVO().getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)) {
				lista.add(getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}else {
				lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(lista, "codigo", "nome", false));
		} catch (Exception e) {
			setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarDadosUnidadeEnsino() {
		try {
			if (getCensoVO().getUnidadeEnsino().getCodigo() != 0) {
				getCensoVO().setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(getCensoVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List<SelectItem> getTipoConsultarComboMatricula() {
		List<SelectItem>  itens = new ArrayList<SelectItem> (0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("nomePessoa", "Responsável Matrícula"));
		return itens;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio dos parametros informados no richmodal. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pelos parâmentros informados no richModal montando automaticamente
	 * o resultado da consulta para apresentação.
	 */
	public void consultarProfessor() {
		try {
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultarProfessor().equals("codigo")) {
				if (getValorConsultarProfessor().equals("")) {
					setValorConsultarProfessor("0");
				}
				Integer valorInt = Integer.parseInt(getValorConsultarProfessor());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(valorInt, TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarProfessor().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultarProfessor(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarProfessor().equals("nomeCidade")) {
				// objs =
				// getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultarProfessor(),
				// TipoPessoa.PROFESSOR.getValor(), false,
				// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarProfessor().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultarProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarProfessor().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultarProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarProfessor().equals("necessidadesEspeciais")) {
				// objs =
				// getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getValorConsultarProfessor(),
				// TipoPessoa.PROFESSOR.getValor(), false,
				// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultarProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarProfessor(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
		if (getMensagemDetalhada().equals("")) {
			this.getProfessorCensoVO().setProfessor(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarProfessor());
		this.setValorConsultarProfessor(null);
		this.setCampoConsultarProfessor(null);
	}

	public void limparCampoProfessor() {
		this.getProfessorCensoVO().setProfessor(new PessoaVO());
	}

	public String getTotalProfessores() {
		return "Professores: " + getCensoVO().getListaProfessorCenso().size() + 1;
	}

	public String getTotalAlunos() {
		return "Alunos: " + getCensoVO().getListaAlunoCenso().size() + 1;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List<SelectItem>  getTipoConsultarComboProfessor() {
		List<SelectItem>  itens = new ArrayList<SelectItem> (0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List<SelectItem>  getTipoConsultarComboResponsavel() {
		List<SelectItem>  itens = new ArrayList<SelectItem> (0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("necessidadesEspeciais", "Necessidades Especiais"));
		return itens;
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
			return "return mascara(this.form, 'form:valorConsulta', '99/99/9999', event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("dataAgendamento")) {
			return "return mascara(this.form, 'form:valorConsulta', '99/99/9999', event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
			return "return mascara(this.form, 'form:valorConsulta', '99/99/9999', event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem>  getTipoConsultaCombo() {
		List<SelectItem>  itens = new ArrayList<SelectItem> (0);
		itens.add(new SelectItem("ano", "Ano"));
		itens.add(new SelectItem("codigo", "Codigo"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<SelectItem>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("censoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		censoVO = null;
		professorCenso = null;
		alunoCenso = null;
	}

	/**
	 * Rotina responsável por preencher a combo layout.
	 */
	public List<SelectItem>  getTipoCensoComboLayout() {
		List<SelectItem>  itens = new ArrayList<SelectItem> (0);
		itens.add(new SelectItem("GRADUACAO", "Graduação"));
		itens.add(new SelectItem("GRADUACAO_TECNOLOGICA", "Graduação Tecnológica"));
		//itens.add(new SelectItem("TECNICO", "Técnico"));		
		itens.add(new SelectItem("EDUCACAO_BASICA_TECNICO", "Educação Básica ou Técnico"));
		return itens;
	}

	public String getCampoConsultarMatricula() {
		return campoConsultarMatricula;
	}

	public void setCampoConsultarMatricula(String campoConsultarMatricula) {
		this.campoConsultarMatricula = campoConsultarMatricula;
	}

	public String getValorConsultarMatricula() {
		return valorConsultarMatricula;
	}

	public void setValorConsultarMatricula(String valorConsultarMatricula) {
		this.valorConsultarMatricula = valorConsultarMatricula;
	}

	public List<SelectItem>  getListaConsultarMatricula() {
		if (listaConsultarMatricula == null) {
			listaConsultarMatricula = new ArrayList<SelectItem> (0);
		}
		return listaConsultarMatricula;
	}

	public void setListaConsultarMatricula(List<SelectItem>  listaConsultarMatricula) {
		this.listaConsultarMatricula = listaConsultarMatricula;
	}

	public AlunoCensoVO getAlunoCensoVO() {
		if (alunoCenso == null) {
			alunoCenso = new AlunoCensoVO();
		}
		return alunoCenso;
	}

	public void setAlunoCensoVO(AlunoCensoVO alunoCensoVO) {
		this.alunoCenso = alunoCensoVO;
	}

	public String getCampoConsultarProfessor() {
		return campoConsultarProfessor;
	}

	public void setCampoConsultarProfessor(String campoConsultarProfessor) {
		this.campoConsultarProfessor = campoConsultarProfessor;
	}

	public String getValorConsultarProfessor() {
		return valorConsultarProfessor;
	}

	public void setValorConsultarProfessor(String valorConsultarProfessor) {
		this.valorConsultarProfessor = valorConsultarProfessor;
	}

	public List<PessoaVO>  getListaConsultarProfessor() {
		if (listaConsultarProfessor == null) {
			listaConsultarProfessor = new ArrayList<PessoaVO> (0);
		}
		return listaConsultarProfessor;
	}

	public void setListaConsultarProfessor(List<PessoaVO>  objs) {
		this.listaConsultarProfessor = objs;
	}

	public ProfessorCensoVO getProfessorCensoVO() {
		if (professorCenso == null) {
			professorCenso = new ProfessorCensoVO();
		}
		return professorCenso;
	}

	public void setProfessorCenso(ProfessorCensoVO professorCenso) {
		this.professorCenso = professorCenso;
	}

	public String getCampoConsultarResponsavel() {
		return campoConsultarResponsavel;
	}

	public void setCampoConsultarResponsavel(String campoConsultarResponsavel) {
		this.campoConsultarResponsavel = campoConsultarResponsavel;
	}

	public String getValorConsultarResponsavel() {
		return valorConsultarResponsavel;
	}

	public void setValorConsultarResponsavel(String valorConsultarResponsavel) {
		this.valorConsultarResponsavel = valorConsultarResponsavel;
	}

	public List<SelectItem>  getListaConsultarResponsavel() {
		if (listaConsultarResponsavel == null) {
			listaConsultarResponsavel = new ArrayList<SelectItem> (0);
		}
		return listaConsultarResponsavel;
	}

	public void setListaConsultarResponsavel(List<SelectItem>  listaConsultarResponsavel) {
		this.listaConsultarResponsavel = listaConsultarResponsavel;
	}

	public CensoVO getCensoVO() {
		if (censoVO == null) {
			censoVO = new CensoVO();
		}
		return censoVO;
	}

	public void setCensoVO(CensoVO censoVO) {
		this.censoVO = censoVO;
	}

	public List<SelectItem>  getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem> (0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> list) {
		this.listaSelectItemUnidadeEnsino = list;
	}

	public AlunoCensoVO getAlunoCenso() {
		if (alunoCenso == null) {
			alunoCenso = new AlunoCensoVO();
		}
		return alunoCenso;
	}

	public void setAlunoCenso(AlunoCensoVO alunoCenso) {
		this.alunoCenso = alunoCenso;
	}

	public boolean isIsMostrarDownloadArquivos() {
		return isMostrarDownloadArquivos;
	}

	public void setIsMostrarDownloadArquivos(boolean isMostrarDownloadArquivos) {
		this.isMostrarDownloadArquivos = isMostrarDownloadArquivos;
	}
	
	private void fazerDownloadArquivo(ArquivoVO arquivoVO) throws Exception {
		context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		context().getExternalContext().dispatch("/DownloadSV");
		FacesContext.getCurrentInstance().responseComplete();
	}	

	public List<SelectItem>  tipoCensoComboTratarAbandonoCurso;
	public List<SelectItem>  getTipoCensoComboTratarAbandonoCurso() {
		if(tipoCensoComboTratarAbandonoCurso == null) {
		tipoCensoComboTratarAbandonoCurso = new ArrayList<SelectItem> (0);
		tipoCensoComboTratarAbandonoCurso.add(new SelectItem("TR", "Trancado"));
		tipoCensoComboTratarAbandonoCurso.add(new SelectItem("CA", "Cancelado"));
		}
		return tipoCensoComboTratarAbandonoCurso;
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (CensoUnidadeEnsinoVO unidade : getCensoVO().getCensoUnidadeEnsinoVOs()) {
			unidade.setSelecionado(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {		
		getCensoVO().setUnidadeEnsinoDescricao(null);
	}
	
	public String getUnidadeEnsinoApresentar() {
		if(unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}
	
	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
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
	
	
}
