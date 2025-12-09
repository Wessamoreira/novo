package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas artefatoEntregaAlunoForm.jsf artefatoEntregaAlunoCons.jsf) com as
 * funcionalidades da classe <code>ArtefatoEntregaAluno</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ArtefatoEntregaAluno
 * @see artefatoEntregaAlunoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.NivelControleArtefatoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("ArtefatoEntregaAlunoControle")
@Scope("viewScope")
@Lazy
public class ArtefatoEntregaAlunoControle extends SuperControle implements Serializable {

	private ArtefatoEntregaAlunoVO artefatoEntregaAlunoVO;
	private List listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List listaSelectItemCurso;
	private CursoVO curso;
	private String valorConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboBoxDisciplina;
	private String campoConsultaDisciplina;
	private List<DisciplinaVO> disciplinaVOs;
	private DisciplinaVO disciplinaVO;
	private String valorConsultaFuncionario;
	private List<SelectItem> tipoConsultaComboBoxFuncionario;
	private String campoConsultaFuncionario;
	private List<FuncionarioVO> funcionarioVOs;
	private FuncionarioVO funcionarioVO;
	private String nivelControle;
	private String periodicidadeCurso;
	private String situacao;
	private Boolean isApresentarListagemDisciplina;
	private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	private Boolean liberarCriarScriptArtefatoEntregaAluno;

	public static final long serialVersionUID = 1L;

	public ArtefatoEntregaAlunoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		listaConsulta = new ArrayList(0);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmconsulta");
	}

	public ArtefatoEntregaAlunoVO getArtefatoEntregaAlunoVO() {
		if (artefatoEntregaAlunoVO == null) {
			artefatoEntregaAlunoVO = new ArtefatoEntregaAlunoVO();
		}
		return artefatoEntregaAlunoVO;
	}

	public void setArtefatoEntregaAlunoVO(ArtefatoEntregaAlunoVO artefatoEntregaAlunoVO) {
		this.artefatoEntregaAlunoVO = artefatoEntregaAlunoVO;
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ArtefatoEntregaAluno</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoForm.xhtml");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemCurso();
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ArtefatoEntregaAluno</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSF correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		ArtefatoEntregaAlunoVO obj = (ArtefatoEntregaAlunoVO) context().getExternalContext().getRequestMap()
				.get("artefatoEntregaAlunoControleItens");
		try {
			obj = getFacadeFactory().getArtefatoEntregaAlunoFacade().consultarPorChavePrimaria(obj.getCodigo(),
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			setArtefatoEntregaAlunoVO(obj);
			apresentarListagemDisciplina();
			realizarLimparCampos();

			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoForm.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		} finally {
			obj = null;
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>ArtefatoEntregaAluno</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto
	 * não é gravado, sendo re-apresentado para o usuário juntamente com uma
	 * mensagem de erro.
	 */
	public void gravar() {
		try {
			getFacadeFactory().getArtefatoEntregaAlunoFacade().validarDados(artefatoEntregaAlunoVO);
			if (artefatoEntregaAlunoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getArtefatoEntregaAlunoFacade().incluir(artefatoEntregaAlunoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getArtefatoEntregaAlunoFacade().alterar(artefatoEntregaAlunoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ArtefatoEntregaAluno</code> Após a exclusão ela automaticamente aciona
	 * a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getArtefatoEntregaAlunoFacade().excluir(artefatoEntregaAlunoVO, getUsuarioLogado());
			setArtefatoEntregaAlunoVO(null);
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoForm.xhtml");
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoCons.xhtml");
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm,
					super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public void montarListaSelectItemCurso(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorNome(prm);
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().clear();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CursoVO obj = (CursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemCurso() {
		try {
			montarListaSelectItemCurso("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List consultarCursoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCursoFacade().consultarPorNome(nomePrm, false,
				Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap()
					.get("disciplinaLista");
			setDisciplinaVO(disciplina);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDisciplina() {
		try {
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = new DisciplinaVO();
				disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt),
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setDisciplinaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setDisciplinaVOs(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public List<SelectItem> getTipoConsultaComboBoxDisciplina() {
		if (tipoConsultaComboBoxDisciplina == null) {
			tipoConsultaComboBoxDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboBoxDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboBoxDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboBoxDisciplina;
	}

	public void setTipoConsultaComboBoxDisciplina(List<SelectItem> tipoConsultaComboBoxDisciplina) {
		this.tipoConsultaComboBoxDisciplina = tipoConsultaComboBoxDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public List<DisciplinaVO> getDisciplinaVOs() {
		if (disciplinaVOs == null) {
			disciplinaVOs = new ArrayList<DisciplinaVO>();
		}
		return disciplinaVOs;
	}

	public void setDisciplinaVOs(List<DisciplinaVO> disciplinaVOs) {
		this.disciplinaVOs = disciplinaVOs;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void selecionarFuncionario() throws Exception {
		try {
			FuncionarioVO funcionario = (FuncionarioVO) context().getExternalContext().getRequestMap()
					.get("funcionarioLista");
			setFuncionarioVO(funcionario);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarFuncionario() {
		try {
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaFuncionario().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaFuncionario());
				FuncionarioVO funcionario = new FuncionarioVO();
				funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(
						new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!funcionario.equals(new FuncionarioVO()) || funcionario != null) {
					objs.add(funcionario);
				}
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), 0,
						false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setFuncionarioVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setFuncionarioVOs(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaComboBoxFuncionario() {
		if (tipoConsultaComboBoxFuncionario == null) {
			tipoConsultaComboBoxFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboBoxFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboBoxFuncionario.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboBoxFuncionario;
	}

	public void setTipoConsultaComboBoxFuncionario(List<SelectItem> tipoConsultaComboBoxFuncionario) {
		this.tipoConsultaComboBoxFuncionario = tipoConsultaComboBoxFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<FuncionarioVO> getFuncionarioVOs() {
		if (funcionarioVOs == null) {
			funcionarioVOs = new ArrayList<FuncionarioVO>();
		}
		return funcionarioVOs;
	}

	public void setFuncionarioVOs(List<FuncionarioVO> funcionarioVOs) {
		this.funcionarioVOs = funcionarioVOs;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public String getNivelControle() {
		if (nivelControle == null) {
			nivelControle = "CUR";
		}
		return nivelControle;
	}

	public void setNivelControle(String nivelControle) {
		this.nivelControle = nivelControle;
	}

	public String getPeriodicidadeCurso() {
		if (periodicidadeCurso == null) {
			periodicidadeCurso = "AN";
		}
		return periodicidadeCurso;
	}

	public void setPeriodicidadeCurso(String periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AT";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List<SelectItem> getListaSelectItemNivelControle() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("CUR", "Curso"));
		itens.add(new SelectItem("DIS", "Disciplina"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemPeriodicidadeCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AN", "Anual"));
		itens.add(new SelectItem("SE", "Semestral"));
		itens.add(new SelectItem("IN", "Integral"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("IN", "Inativo"));
		return itens;
	}

	public void adicionarUnidadeEnsino() {
		try {
			UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
					getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			NivelControleArtefatoVO nivelControleArtefato = new NivelControleArtefatoVO();
			nivelControleArtefato.setUnidadeEnsino(obj);
			nivelControleArtefato.setTipo("UNE");
			getArtefatoEntregaAlunoVO().adicionarObjNivelControleArtefatoUnidadeEnsinoVOs(nivelControleArtefato);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerUnidadeEnsino() throws Exception {
		NivelControleArtefatoVO obj = (NivelControleArtefatoVO) context().getExternalContext().getRequestMap()
				.get("unidadeEnsinoItens");
		getArtefatoEntregaAlunoVO().excluirObjNivelControleArtefatoUnidadeEnsinoVOs(obj.getUnidadeEnsino().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarCurso() {
		try {
			CursoVO obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(
					getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado());
			NivelControleArtefatoVO nivelControleArtefato = new NivelControleArtefatoVO();
			nivelControleArtefato.setCurso(obj);
			nivelControleArtefato.setTipo("CUR");
			getArtefatoEntregaAlunoVO().adicionarObjNivelControleArtefatoCursoVOs(nivelControleArtefato);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerCurso() throws Exception {
		NivelControleArtefatoVO obj = (NivelControleArtefatoVO) context().getExternalContext().getRequestMap()
				.get("cursoItens");
		getArtefatoEntregaAlunoVO().excluirObjNivelControleArtefatoCursoVOs(obj.getCurso().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarDisciplina() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			NivelControleArtefatoVO nivelControleArtefato = new NivelControleArtefatoVO();
			nivelControleArtefato.setDisciplina(obj);
			nivelControleArtefato.setTipo("DIS");
			getArtefatoEntregaAlunoVO().adicionarObjNivelControleArtefatoDisciplinaVOs(nivelControleArtefato);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCampoConsultaDisciplina(null);
			setValorConsultaDisciplina(null);
		}
	}

	public void removerDisciplina() throws Exception {
		NivelControleArtefatoVO obj = (NivelControleArtefatoVO) context().getExternalContext().getRequestMap()
				.get("disciplinaItens");
		getArtefatoEntregaAlunoVO().excluirObjNivelControleArtefatoDisciplinaVOs(obj.getDisciplina().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			NivelControleArtefatoVO nivelControleArtefato = new NivelControleArtefatoVO();
			nivelControleArtefato.setFuncionario(obj);
			nivelControleArtefato.setTipo("FUN");
			getArtefatoEntregaAlunoVO().adicionarObjNivelControleArtefatoFuncionarioVOs(nivelControleArtefato);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCampoConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
		}
	}

	public void removerFuncionario() throws Exception {
		NivelControleArtefatoVO obj = (NivelControleArtefatoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioItens");
		getArtefatoEntregaAlunoVO().excluirObjNivelControleArtefatoFuncionarioVOs(obj.getFuncionario().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSF
	 * ArtefatoEntregaAlunoCons.jsf. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSF. Como
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
				objs = getFacadeFactory().getArtefatoEntregaAlunoFacade().consultarPorCodigo(new Integer(valorInt),
						true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getArtefatoEntregaAlunoFacade().consultarPorNome(
						getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoEntregaAlunoCons.xhtml");
		}
	}

	/**
	 * limpa os campos da tela os campos serão limpos
	 * 
	 * @return
	 */
	public void realizarLimparCampos() {
		getControleConsulta().setValorConsulta("");
	}

	public void apresentarListagemDisciplina() {
		if (artefatoEntregaAlunoVO.getNivelControle().equals("DIS")) {
			setIsApresentarListagemDisciplina(true);
		} else {
			setIsApresentarListagemDisciplina(false);
		}

	}

	public Boolean getIsApresentarListagemDisciplina() {
		return isApresentarListagemDisciplina;
	}

	public void setIsApresentarListagemDisciplina(Boolean isApresentarListagemDisciplina) {
		this.isApresentarListagemDisciplina = isApresentarListagemDisciplina;
	}

	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null) {
			usernameLiberarOperacaoFuncionalidade = "";
		}
		return usernameLiberarOperacaoFuncionalidade;
	}

	public void setUsernameLiberarOperacaoFuncionalidade(String usernameLiberarOperacaoFuncionalidade) {
		this.usernameLiberarOperacaoFuncionalidade = usernameLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}
	public Boolean getLiberarCriarScriptArtefatoEntregaAluno() {
		return liberarCriarScriptArtefatoEntregaAluno;
	}

	public void setLiberarCriarScriptArtefatoEntregaAluno(Boolean liberarCriarScriptArtefatoEntregaAluno) {
		this.liberarCriarScriptArtefatoEntregaAluno = liberarCriarScriptArtefatoEntregaAluno;
	}

	public void validarLiberarOperacaoFuncionalidade() {		
		boolean usuarioValido = false;
		UsuarioVO usuarioVerif = null;
		try {
			usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			usuarioValido = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirCriarScriptArtefatoEntregaAluno", usuarioVerif);
			usuarioTemPermissaoLiberar = true;
		} catch (Exception e) {
		}
		try {
			if (!usuarioValido) {
				throw new Exception("Usuário/Senha Inválidos");
			}
			if (!usuarioTemPermissaoLiberar) {
				throw new Exception("Você não tem permissão para Criar Script para o Artefato.");
			} else {
				setLiberarCriarScriptArtefatoEntregaAluno(Boolean.TRUE);
			}
			this.setUsernameLiberarOperacaoFuncionalidade("");

			this.setSenhaLiberarOperacaoFuncionalidade("");
			setMensagemID("msg_funcionalidadeLiberadaComSucesso");
		} catch (Exception e) {
			this.setUsernameLiberarOperacaoFuncionalidade("");

			this.setSenhaLiberarOperacaoFuncionalidade("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
}
