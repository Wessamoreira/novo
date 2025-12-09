package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF da
 * páginas registroEntregaArtefatoAlunoForm.jsf) com as
 * funcionalidades da classe <code>RegistroEntregaArtefatoAluno</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControleRelatorio
 * @see RegistroEntregaArtefatoAluno
 * @see RegistroEntregaArtefatoAlunoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.NivelControleArtefatoVO;
import negocio.comuns.academico.RegistroEntregaArtefatoAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.RegistroEntregaArtefatoAluno;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("RegistroEntregaArtefatoAlunoControle")
@Scope("viewScope")
@Lazy
public class RegistroEntregaArtefatoAlunoControle extends SuperControleRelatorio implements Serializable {

	private List listaSelectItemArtefatoEntregaAluno;
	private ArtefatoEntregaAlunoVO artefatoEntregaAlunoVO;
	private List listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String campoConsultaUnidadeEnsino;
	private String valorConsultaUnidadeEnsino;
	private List listaConsultaUnidadeEnsino;
	private List listaSelectItemCurso;
	private CursoVO curso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private TurmaVO turma;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private MatriculaVO matricula;
	private List listaSelectItemDisciplina;
	private DisciplinaVO disciplinaVO;
	private String valorConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboBoxDisciplina;
	private String campoConsultaDisciplina;
	private List<DisciplinaVO> disciplinaVOs;
	private Date dataInicio;
	private Date dataFim;
	private String ano;
	private String semestre;
	private Boolean isApresentarAno;
	private Boolean isApresentarSemestre;
	private String situacaoEntrega;
	private List<RegistroEntregaArtefatoAlunoVO> listaAlunos;
	private RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAlunoVO;
	private Boolean checkEntregueTodosAlunos;
	private String layout;

	public static final long serialVersionUID = 1L;

	public RegistroEntregaArtefatoAlunoControle() throws Exception {
		inicializarListasSelectItemComboBoxArtefato();
		setMensagemID("msg_entre_prmconsulta");
	}

	public RegistroEntregaArtefatoAlunoVO getRegistroEntregaArtefatoAlunoVO() {
		if (registroEntregaArtefatoAlunoVO == null) {
			registroEntregaArtefatoAlunoVO = new RegistroEntregaArtefatoAlunoVO();
		}
		return registroEntregaArtefatoAlunoVO;
	}

	public void setRegistroEntregaArtefatoAlunoVO(RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAlunoVO) {
		this.registroEntregaArtefatoAlunoVO = registroEntregaArtefatoAlunoVO;
	}

	public List getListaSelectItemArtefatoEntregaAluno() {
		return listaSelectItemArtefatoEntregaAluno;
	}

	public void setListaSelectItemArtefatoEntregaAluno(List listaSelectItemArtefatoEntregaAluno) {
		this.listaSelectItemArtefatoEntregaAluno = listaSelectItemArtefatoEntregaAluno;
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

	public void montarListaSelectItemArtefatoEntregaAluno() {
		try {
			montarListaSelectItemArtefatoEntregaAluno("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemArtefatoEntregaAluno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		Integer codigoFuncionario = 0;
		try {
			if (getUsuarioLogado().getPessoa().getCodigo() > 0) {
				codigoFuncionario = getFacadeFactory().getFuncionarioFacade()
						.consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(),
								getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado())
						.getCodigo();
			}
			if (super.getUnidadeEnsinoLogado().getCodigo() > 0) {
				resultadoConsulta = getFacadeFactory().getArtefatoEntregaAlunoFacade()
						.consultarArtefatosAtivosPorUnidadeEnsino(codigoFuncionario, getUnidadeEnsinoLogado(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			} else {
				resultadoConsulta = getFacadeFactory().getArtefatoEntregaAlunoFacade().consultarArtefatosAtivos(
						codigoFuncionario, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}

			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (!resultadoConsulta.isEmpty()) {
				objs.add(new SelectItem(0, ""));
			}

			while (i.hasNext()) {
				ArtefatoEntregaAlunoVO obj = (ArtefatoEntregaAlunoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));

			}
			setListaSelectItemArtefatoEntregaAluno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void inicializarListasSelectItemComboBoxArtefato() {		
		montarListaSelectItemArtefatoEntregaAluno();		
	}

	public void inicializarListasSelectItemTodosComboBoxLigacaoArtefatoEntregaAluno() {
		try {
			if (artefatoEntregaAlunoVO.getCodigo() == null || artefatoEntregaAlunoVO.getCodigo() == 0) {
				throw new Exception("O artefato deve ser informado!");
			}
			setArtefatoEntregaAlunoVO(getFacadeFactory().getArtefatoEntregaAlunoFacade().consultarPorChavePrimaria(
					artefatoEntregaAlunoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));

			apresentarAno(getArtefatoEntregaAlunoVO().getPeriodicidadeCurso());
			apresentarSemestre(getArtefatoEntregaAlunoVO().getPeriodicidadeCurso());
			limparUnidadeEnsino();
			limparCurso();
			limparDisciplina();
			limparTurma();
			limparDadosAluno();
			setSituacaoEntrega(null);
			setLayout(null);
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemCurso();
			montarListaSelectItemDisciplina();
			limparGridAlunos();
			setDataInicio(null);
			setDataFim(null);
			setAno(null);
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDF() {

		List<RegistroEntregaArtefatoAlunoVO> listaObjetos = null;
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "RegistroEntregaArtefatoAluno",
					"Inicializando Geração de Registro Entrega Artefato Aluno" + this.getUnidadeEnsinoVO().getNome()
							+ " - " + getUsuarioLogado().getCodigo() + " - "
							+ getUsuarioLogado().getPerfilAcesso().getCodigo(),
					"Emitindo Relatório");

			getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().validarDados(artefatoEntregaAlunoVO, getAno(),
					getSemestre(), getDisciplinaVO());

			listaObjetos = getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().consultarAlunos(
					getArtefatoEntregaAlunoVO(), getDataInicio(), getDataFim(), getAno(), getSemestre(),
					getUnidadeEnsinoVO(), getCurso(), getTurma(), getDisciplinaVO(), getMatricula(), getSituacaoEntrega(),
					getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(
						getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().designIReportRelatorio(getLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(RegistroEntregaArtefatoAluno.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Registro de Entrega de Artefato Aluno");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(
						getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");

				StringBuilder unidadeEnsino = new StringBuilder();
				StringBuilder curso = new StringBuilder();
				StringBuilder disciplina = new StringBuilder();

				if (super.getUnidadeEnsinoLogado().getCodigo() > 0) {
					unidadeEnsino.append(super.getUnidadeEnsinoLogado().getNome());
				} else if (getUnidadeEnsinoVO().getCodigo() != null && getUnidadeEnsinoVO().getCodigo() > 0) {
					if(getUnidadeEnsinoVO().getNome() != null && !getUnidadeEnsinoVO().getNome().equals("")) {
						unidadeEnsino.append(getUnidadeEnsinoVO().getNome());
					}else {
						unidadeEnsino.append(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
					}
					
				} else if (!getArtefatoEntregaAlunoVO().getNivelControleArtefatoUnidadeEnsinoVOs().isEmpty()) {

					for (NivelControleArtefatoVO nivelControleArtefatoUnidadeEnsino : getArtefatoEntregaAlunoVO()
							.getNivelControleArtefatoUnidadeEnsinoVOs()) {

						unidadeEnsino.append(nivelControleArtefatoUnidadeEnsino.getUnidadeEnsino().getNome())
								.append("; ");
					}

				}

				if (getCurso().getCodigo() != null && getCurso().getCodigo() > 0) {
					if(getCurso().getNome() != null && !getCurso().getNome().equals("")) {
						curso.append(getCurso().getNome());
					}else {
						curso.append(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()).getNome());
					}
				} else if (!getArtefatoEntregaAlunoVO().getNivelControleArtefatoCursoVOs().isEmpty()) {

					for (NivelControleArtefatoVO nivelControleArtefatoCurso : getArtefatoEntregaAlunoVO()
							.getNivelControleArtefatoCursoVOs()) {

						curso.append(nivelControleArtefatoCurso.getCurso().getNome()).append("; ");
					}

				}

				if (getDisciplinaVO().getCodigo() != null && getDisciplinaVO().getCodigo() > 0) {
					if(getDisciplinaVO().getNome() != null & !getDisciplinaVO().getNome().equals("")) {
						disciplina.append(getDisciplinaVO().getNome());
					}else {
						disciplina.append(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
					}
					
				} else if (!getArtefatoEntregaAlunoVO().getNivelControleArtefatoDisciplinaVOs().isEmpty()) {

					for (NivelControleArtefatoVO nivelControleArtefatoDisciplina : getArtefatoEntregaAlunoVO()
							.getNivelControleArtefatoDisciplinaVOs()) {

						disciplina.append(nivelControleArtefatoDisciplina.getDisciplina().getNome()).append("; ");
					}

				}

				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.toString());

				getSuperParametroRelVO().adicionarParametro("artefato", getArtefatoEntregaAlunoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("ano", getAno());
				getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
				getSuperParametroRelVO().adicionarParametro("curso", curso.toString());
				getSuperParametroRelVO().adicionarParametro("turma", getTurma().getIdentificadorTurma());
				getSuperParametroRelVO().adicionarParametro("disciplina", disciplina.toString());
				getSuperParametroRelVO().adicionarParametro("situacaoEntrega", getSituacaoEntrega());

				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");

				registrarAtividadeUsuario(getUsuarioLogado(), "RegistroEntregaArtefatoAluno",
						"Finalizando Geração de Registro Entrega Artefato Aluno", "Emitindo Relatório");

			} else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>RegistroEntregaArtefatoAluno</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário
	 * juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if(!listaAlunos.isEmpty()) {
				getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().validarDados(artefatoEntregaAlunoVO, getAno(),
						getSemestre(), getDisciplinaVO());
				for (RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAlunoVO : getListaAlunos()) {
					RegistroEntregaArtefatoAlunoVO obj = new RegistroEntregaArtefatoAlunoVO();
					obj = getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().verificarArtefatoEntregueAluno(
							artefatoEntregaAlunoVO, registroEntregaArtefatoAlunoVO, false, Uteis.NIVELMONTARDADOS_COMBOBOX,
							getUsuarioLogado());
					if (obj.getCodigo() == null) {
						getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().incluir(artefatoEntregaAlunoVO,
								registroEntregaArtefatoAlunoVO, getDisciplinaVO(), getUsuarioLogado());
					} else {
						registroEntregaArtefatoAlunoVO.setCodigo(obj.getCodigo());
						getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().alterar(artefatoEntregaAlunoVO,
								registroEntregaArtefatoAlunoVO, getDisciplinaVO(), getUsuarioLogado());
					}
				}
	
				setMensagemID("msg_dados_gravados");				
			}
			else {
				setMensagemDetalhada("Deve ser ter ao menos um aluno listado para gravar!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public String getCampoConsultaUnidadeEnsino() {
		return campoConsultaUnidadeEnsino;
	}

	public void setCampoConsultaUnidadeEnsino(String campoConsultaUnidadeEnsino) {
		this.campoConsultaUnidadeEnsino = campoConsultaUnidadeEnsino;
	}

	public String getValorConsultaUnidadeEnsino() {
		return valorConsultaUnidadeEnsino;
	}

	public void setValorConsultaUnidadeEnsino(String valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
	}

	public List getListaConsultaUnidadeEnsino() {
		return listaConsultaUnidadeEnsino;
	}

	public void setListaConsultaUnidadeEnsino(List listaConsultaUnidadeEnsino) {
		this.listaConsultaUnidadeEnsino = listaConsultaUnidadeEnsino;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaUnidadeEnsino() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		return itens;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * UnidadeEnsinoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultarUnidadeEnsino() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaUnidadeEnsino().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(
						getValorConsultaUnidadeEnsino(), super.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaUnidadeEnsino().equals("razaoSocial")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorRazaoSocial(
						getValorConsultaUnidadeEnsino(), super.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaUnidadeEnsino().equals("nomeCidade")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNomeCidade(
						getValorConsultaUnidadeEnsino(), super.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaUnidadeEnsino().equals("CNPJ")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCnpj(
						getValorConsultaUnidadeEnsino(), super.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaUnidadeEnsino(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
			return "";
		} catch (Exception e) {
			setListaConsultaUnidadeEnsino(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}

	}

	/**
	 * Método responsável por selecionar o objeto UnidadeEnsinoVO
	 * <code>UnidadeEnsino/code>.
	 */
	public void selecionarUnidadeEnsino() {
		try {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap()
					.get("unidadeEnsinoItens");
			setUnidadeEnsinoVO(obj);
			getListaConsultaUnidadeEnsino().clear();
			this.setValorConsultaUnidadeEnsino("");
			this.setCampoConsultaUnidadeEnsino("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(null);
		} catch (Exception e) {
		}
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

		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorArtefatoEntregaAluno(
					artefatoEntregaAlunoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			getListaSelectItemUnidadeEnsino().clear();
			if (!resultadoConsulta.isEmpty()) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public void montarListaSelectItemCurso() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getCursoFacade().consultarPorArtefatoEntregaAluno(
					artefatoEntregaAlunoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().clear();
			if (!resultadoConsulta.isEmpty()) {
				getListaSelectItemCurso().add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				CursoVO obj = (CursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
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

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CursoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultarCurso() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEPeriodicidade(
						getArtefatoEntregaAlunoVO().getPeriodicidadeCurso(), getValorConsultaCurso(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Método responsável por selecionar o objeto CursoVO <code>Curso/code>.
	 */
	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCurso(obj);
			getListaConsultaCurso().clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparCurso() {
		try {
			setCurso(null);
		} catch (Exception e) {
		}
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de Turma
	 * <code>Turma/code>.
	 */

	public List<SelectItem> getTipoConsultaTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador Turma"));
		return itens;
	}

	public String consultarTurma() {
		try {
			getCurso().getCodigo();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(),
						super.getUnidadeEnsinoLogado().getCodigo(), false, false, true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "";

		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Método responsável por selecionar o objeto TurmaVO <code>Turma/code>.
	 */
	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurma(obj);
			getListaConsultaTurma().clear();
			this.setValorConsultaTurma("");
			this.setCampoConsultaTurma("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparTurma() {
		try {
			setTurma(null);
		} catch (Exception e) {
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

	/**
	 * Método responsável por selecionar o objeto DisciplinaVO
	 * <code>Disciplina/code>.
	 */
	public void selecionarDisciplina() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
			getDisciplinaVOs().clear();
			this.setValorConsultaDisciplina("");
			this.setCampoConsultaDisciplina("");
			limparGridAlunos();
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparDisciplina() {
		try {
			setDisciplinaVO(null);
			limparGridAlunos();
		} catch (Exception e) {
		}
	}
	
	public void limparGridAlunos() {
		try {
			listaAlunos.clear();
		} catch (Exception e) {
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

	public List getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public void montarListaSelectItemDisciplina() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getDisciplinaFacade().consultarPorArtefatoEntregaAluno(
					artefatoEntregaAlunoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			getListaSelectItemDisciplina().clear();
			if (!resultadoConsulta.isEmpty()) {
				getListaSelectItemDisciplina().add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " +  obj.getNome()));
				removerObjetoMemoria(obj);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
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

	public List<SelectItem> getListaSelectItemSituacaoEntrega() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("entregue", "Entregue"));
		itens.add(new SelectItem("naoEntregue", "Não Entregue"));
		return itens;
	}

	public String getSituacaoEntrega() {
		return situacaoEntrega;
	}

	public void setSituacaoEntrega(String situacaoEntrega) {
		this.situacaoEntrega = situacaoEntrega;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("1", "1"));
		itens.add(new SelectItem("2", "2"));
		return itens;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Boolean getIsApresentarAno() {
		return isApresentarAno;
	}

	public void setIsApresentarAno(Boolean isApresentarAno) {
		this.isApresentarAno = isApresentarAno;
	}

	public Boolean getIsApresentarSemestre() {
		return isApresentarSemestre;
	}

	public void setIsApresentarSemestre(Boolean isApresentarSemestre) {
		this.isApresentarSemestre = isApresentarSemestre;
	}

	public void apresentarAno(String periodicidadeCurso) {

		if (periodicidadeCurso.equals("AN") || periodicidadeCurso.equals("SE")) {
			setIsApresentarAno(true);
		} else {
			setIsApresentarAno(false);
			setAno(null);
		}

	}

	public void apresentarSemestre(String periodicidadeCurso) {
		if (periodicidadeCurso.equals("SE")) {
			setIsApresentarSemestre(true);
		} else {
			setIsApresentarSemestre(false);
			setSemestre(null);
		}

	}

	public List<RegistroEntregaArtefatoAlunoVO> getListaAlunos() {
		if (listaAlunos == null) {
			listaAlunos = new ArrayList(0);
		}
		return listaAlunos;
	}

	public void setListaAlunos(List<RegistroEntregaArtefatoAlunoVO> listaAlunos) {
		this.listaAlunos = listaAlunos;
	}

	public void consultarAlunos() {
		try {

			getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().validarDados(artefatoEntregaAlunoVO, getAno(),
					getSemestre(), getDisciplinaVO());
			List<RegistroEntregaArtefatoAlunoVO> objs = new ArrayList<RegistroEntregaArtefatoAlunoVO>(0);
			objs = getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade().consultarAlunos(
					getArtefatoEntregaAlunoVO(), getDataInicio(), getDataFim(), getAno(), getSemestre(),
					getUnidadeEnsinoVO(), getCurso(), getTurma(), getDisciplinaVO(), getMatricula(), getSituacaoEntrega(),
					getUsuarioLogado());
			setListaAlunos(objs);
			if (!verificaRegistroEntregaArtefatoAluno().isEmpty()) {
				for (RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluno : verificaRegistroEntregaArtefatoAluno()) {
					for (RegistroEntregaArtefatoAlunoVO aluno : getListaAlunos()) {
						if (registroEntregaArtefatoAluno.getMatriculaPeriodo().getCodigo()
								.equals(aluno.getMatriculaPeriodo().getCodigo())) {
							if (registroEntregaArtefatoAluno.getSituacao().equals("ENT")) {
								aluno.setEntregue(true);
							} else {
								aluno.setEntregue(false);
							}
						}
					}
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getCheckEntregueTodosAlunos() {
		return checkEntregueTodosAlunos;
	}

	public void setCheckEntregueTodosAlunos(Boolean checkEntregueTodosAlunos) {
		this.checkEntregueTodosAlunos = checkEntregueTodosAlunos;
	}

	public void checarEntregueTodosAlunos() {
		if (getCheckEntregueTodosAlunos()) {
			for (RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluno : getListaAlunos()) {
				registroEntregaArtefatoAluno.setEntregue(true);
			}
		} else {
			for (RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluno : getListaAlunos()) {
				registroEntregaArtefatoAluno.setEntregue(false);
			}
		}
	}

	public List<RegistroEntregaArtefatoAlunoVO> verificaRegistroEntregaArtefatoAluno() {
		try {
			List<RegistroEntregaArtefatoAlunoVO> listaRegistroEntregaArtefatoAluno = new ArrayList<RegistroEntregaArtefatoAlunoVO>(
					0);
			listaRegistroEntregaArtefatoAluno = getFacadeFactory().getRegistroEntregaArtefatoAlunoFacade()
					.consultarRegistroEntregaArtefatoAluno(getArtefatoEntregaAlunoVO(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
			return listaRegistroEntregaArtefatoAluno;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return new ArrayList<RegistroEntregaArtefatoAlunoVO>(0);
		}

	}
	
	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}
	
	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
	
	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatricula(objAluno);

			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatricula(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);

				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatricula(obj);
	
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatricula());
	}
	
	public String getLayout() {
		if (layout == null) {
			layout = "relAssinaturaAluno";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public List<SelectItem> getListaSelectItemLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("relAssinaturaAluno", "Relatório com Assinatura do Aluno"));
		itens.add(new SelectItem("relAssinaturaAlunoEResponsavelEntrega", "Relatório com Assinatura do Aluno/Responsável Entrega"));
		return itens;
	}

}
