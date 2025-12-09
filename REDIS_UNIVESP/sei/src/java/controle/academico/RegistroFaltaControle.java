package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroFaltaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Leonardo Riciolle
 */
@Controller("RegistroFaltaControle")
@Scope("viewScope")
@Lazy
public class RegistroFaltaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 4809071738858728221L;

	private RegistroFaltaVO registroFaltaVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List listaConsultaAluno;
	private Date dataInicio;
	private Date dataFim;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List listaConsultaCurso;

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaCons.xhtml");
	}

	public String novo() {
		try {
			setRegistroFaltaVO(new RegistroFaltaVO());
			removerObjetoMemoria(this);
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		}
	}

	public String consultarRegistroFalta() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRegistrarFaltaInterfaceFacade().consultar(getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getRegistroFaltaVO().getMatriculaVO().getMatricula(), getRegistroFaltaVO().getMatriculaVO().getCurso().getCodigo(), getDataInicio(), getDataFim(), false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS));
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaCons.xhtml");
		}
	}

	public String editar() {
		try {
			RegistroFaltaVO obj = ((RegistroFaltaVO) context().getExternalContext().getRequestMap().get("registrarFaltaItens"));
			setRegistroFaltaVO(getFacadeFactory().getRegistrarFaltaInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		}
	}

	public String excluir() throws Exception {
		try {
			getFacadeFactory().getRegistrarFaltaInterfaceFacade().excluir(getRegistroFaltaVO(), false, getUsuarioLogado());
			setRegistroFaltaVO(getRegistroFaltaVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		}
	}

	public String gravar() {
		try {
			getFacadeFactory().getRegistrarFaltaInterfaceFacade().persistir(getRegistroFaltaVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarFaltaForm.xhtml");
		}
	}

	public RegistroFaltaControle() throws Exception {
		setRegistroFaltaVO(new RegistroFaltaVO());
		montarListaSelectItemUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getRegistroFaltaVO().getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getRegistroFaltaVO().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getRegistroFaltaVO().setMatriculaVO(objAluno);
			getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			if (!getRegistroFaltaVO().getMatriculaVO().getCurso().getNivelEducacional().equals("IN") && !getRegistroFaltaVO().getMatriculaVO().getCurso().getNivelEducacional().equals("BA")) {
				setRegistroFaltaVO(new RegistroFaltaVO());
				throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFalta_EducaoInfantil"));
			}
			getRegistroFaltaVO().setAno(Uteis.getAno(objAluno.getData()));
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRegistroFaltaVO(new RegistroFaltaVO());
		}
	}

	public void consultarCursoPorNome() {
		try {
			if (this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo() != 0) {
				if (!Uteis.isAtributoPreenchido(this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo())) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				CursoVO obj = (CursoVO) getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getRegistroFaltaVO().getMatriculaVO().setCurso(obj);
			} else {
				throw new Exception("Informe uma unidade de ensino valida. ");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRegistroFaltaVO(new RegistroFaltaVO());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("registrarFaltaItens");
			getRegistroFaltaVO().setMatriculaVO(obj);
			if (!getRegistroFaltaVO().getMatriculaVO().getCurso().getNivelEducacional().equals("IN") && !getRegistroFaltaVO().getMatriculaVO().getCurso().getNivelEducacional().equals("BA")) {
				setRegistroFaltaVO(new RegistroFaltaVO());
				throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFalta_EducaoInfantil"));
			}
			getRegistroFaltaVO().setAno(Uteis.getAno(obj.getData()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoUnidadeEnsino() {
		try {
			List objs = new ArrayList(0);
			if (this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Unidade Ensino</code>.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemUnidadeEnsino(String valorConsulta) throws Exception {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(valorConsulta, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public void limparDadosAluno() throws Exception {
		getRegistroFaltaVO().setMatriculaVO(null);
		setListaConsultaAluno(new ArrayList<SelectItem>(0));
	}

	public void setRegistroFaltaVO(RegistroFaltaVO registroFaltaVO) {
		this.registroFaltaVO = registroFaltaVO;
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

	public List<SelectItem> getListaSelectItemBimestre() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(BimestreEnum.BIMESTRE_01, BimestreEnum.BIMESTRE_01.getValorApresentar()));
		objs.add(new SelectItem(BimestreEnum.BIMESTRE_02, BimestreEnum.BIMESTRE_02.getValorApresentar()));
		objs.add(new SelectItem(BimestreEnum.BIMESTRE_03, BimestreEnum.BIMESTRE_03.getValorApresentar()));
		objs.add(new SelectItem(BimestreEnum.BIMESTRE_04, BimestreEnum.BIMESTRE_04.getValorApresentar()));
		return objs;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));
		return objs;
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

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public RegistroFaltaVO getRegistroFaltaVO() {
		if (registroFaltaVO == null) {
			registroFaltaVO = new RegistroFaltaVO();
		}
		return registroFaltaVO;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public boolean getIsApresentarAno() {
		if (getRegistroFaltaVO().getMatriculaVO().getCurso().getSemestral() || getRegistroFaltaVO().getMatriculaVO().getCurso().getAnual()) {
			// getRegistroFaltaVO().setAno(Uteis.getAno(getRegistroFaltaVO().getDataFalta()));
			return true;
		} else {
			getRegistroFaltaVO().setAno("");
			getRegistroFaltaVO().setSemestre("");
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getRegistroFaltaVO().getMatriculaVO().getCurso().getSemestral()) {
			getRegistroFaltaVO().setSemestre(Uteis.getSemestreAtual());
			return true;
		} else {
			getRegistroFaltaVO().setSemestre("");
		}
		return false;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo() != 0) {
				if (getCampoConsultaCurso().equals("codigo")) {
					if (getValorConsultaCurso().equals("")) {
						setValorConsultaCurso("0");
					}
					int valorInt = Integer.parseInt(getValorConsultaCurso());
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(new Integer(valorInt), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				if (getCampoConsultaCurso().equals("nome")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), this.getRegistroFaltaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				setListaConsultaCurso(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getRegistroFaltaVO().getMatriculaVO().setCurso(obj);
			if (!getRegistroFaltaVO().getMatriculaVO().getCurso().getNivelEducacional().equals("IN") && !getRegistroFaltaVO().getMatriculaVO().getCurso().getNivelEducacional().equals("BA")) {
				setRegistroFaltaVO(new RegistroFaltaVO());
				throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFalta_EducaoInfantil"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		try {
			getRegistroFaltaVO().getMatriculaVO().setCurso(null);
		} catch (Exception e) {
		}
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List litaConsultaCurso) {
		this.listaConsultaCurso = litaConsultaCurso;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
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

	public void executarMudancaData() {
		getRegistroFaltaVO().setAno(Uteis.getAno(getRegistroFaltaVO().getDataFalta()));
	}
}
