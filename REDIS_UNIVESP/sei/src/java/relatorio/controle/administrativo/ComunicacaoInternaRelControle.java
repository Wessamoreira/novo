package relatorio.controle.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.ComunicacaoInternaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.administrativo.ComunicacaoInternaRel;

@Controller("ComunicacaoInternaRelControle")
@Scope("viewScope")
@Lazy
public class ComunicacaoInternaRelControle extends SuperControleRelatorio {

	ComunicacaoInternaRelVO comunicacaoInternaRelVO;
	UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String tipoPessoa;
	private Date dataInicio;
	private Date dataFim;
	private Boolean aluno;
	private Boolean professor;
	private Boolean coordenador;
	private Boolean funcionario;
	private Boolean mostrarCamposDescritivos;
	private String campoDescritivo;
	private String valorDescritivo;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List listaConsultaProfessor;
	private String campoConsultaCoordenador;
	private String valorConsultaCoordenador;
	private List listaConsultaCoordenador;
	private Integer codigoPessoa;
	private Boolean filtroLeituraRegistrada;
	private String tipoOrdenacao;

	public ComunicacaoInternaRelControle() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		limparDados();
		setarFalseNosFiltros();
		setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
		setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		List listaObjetos = null;
		String titulo = "Relatório Comunicação Interna";
		try {
			listaObjetos = new ArrayList(0);
			listaObjetos = getFacadeFactory().getComunicacaoInternaRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getTipoPessoa(), getCodigoPessoa(), getDataInicio(), getDataFim(), getFiltroLeituraRegistrada(), getTipoOrdenacao(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				String design = ComunicacaoInternaRel.getDesignIReportRelatorio();
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ComunicacaoInternaRel.getCaminhoBaseDesignIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(ComunicacaoInternaRel.getCaminhoBaseDesignIReportRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");

				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
				if (getDataInicio() != null) {
					getSuperParametroRelVO().setDataInicio(Uteis.getData(getDataInicio()));
				} else {
					getSuperParametroRelVO().setDataInicio("");
				}
				if (getDataFim() != null) {
					getSuperParametroRelVO().setDataFim(Uteis.getData(getDataFim()));
				} else {
					getSuperParametroRelVO().setDataFim("");
				}
				if (getTipoPessoa().equals("")) {
					getSuperParametroRelVO().adicionarParametro("tipoPessoa", "TODAS");
				} else {
					if (getTipoPessoa().equals("aluno")) {
						getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Aluno");
					} else if (getTipoPessoa().equals("professor")) {
						getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Professor");
					} else if (getTipoPessoa().equals("coordenador")) {
						getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Coordenador");
					} else if (getTipoPessoa().equals("funcionario")) {
						getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Funcionário");
					}
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			listaObjetos.clear();
			listaObjetos = null;
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>(0));
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			setCampoDescritivo(obj.getMatricula());
			setValorDescritivo(obj.getAluno().getNome());
			setCodigoPessoa(obj.getAluno().getCodigo());
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			setCampoDescritivo(obj.getMatricula());
			setValorDescritivo(obj.getPessoa().getNome());
			setCodigoPessoa(obj.getPessoa().getCodigo());
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
			getListaConsultaFuncionario().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			setCampoDescritivo(getFacadeFactory().getFuncionarioFacade().consultarMatriculaFuncionarioPorCodigoPessoa(obj.getCodigo(), 0));
			setValorDescritivo(obj.getNome());
			setCodigoPessoa(obj.getCodigo());
			setCampoConsultaProfessor("");
			setValorConsultaProfessor("");
			getListaConsultaProfessor().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarCoordenador() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("coordenadorItens");
			setCampoDescritivo(obj.getMatricula());
			setValorDescritivo(obj.getPessoa().getNome());
			setCodigoPessoa(obj.getCodigo());
			setCampoConsultaCoordenador("");
			setValorConsultaCoordenador("");
			getListaConsultaCoordenador().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		List objs = new ArrayList(0);
		try {
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("CPF")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCPF(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void consultarFuncionario() {
		List objs = new ArrayList(0);
		try {
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			getListaConsultaFuncionario().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCoordenador() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaCoordenador().equals("nome")) {
				if (getValorConsultaCoordenador().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaCoordenadorPorNomeApresentarModal(getValorConsultaCoordenador(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCoordenador().equals("CPF")) {
				if (getValorConsultaCoordenador().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaCoordenadorPorCPFApresentarModal(getValorConsultaCoordenador(), false, getUsuarioLogado());
			}
			setListaConsultaCoordenador(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDados() {
		setCampoDescritivo("");
		setValorDescritivo("");
		setCodigoPessoa(0);
	}

	public void selecionarFiltro() {
		limparDados();
		setarFalseNosFiltros();
		if (getTipoPessoa().equals("aluno")) {
			setAluno(true);
			setMostrarCamposDescritivos(true);
		} else if (getTipoPessoa().equals("professor")) {
			setProfessor(true);
			setMostrarCamposDescritivos(true);
		} else if (getTipoPessoa().equals("coordenador")) {
			setCoordenador(true);
			setMostrarCamposDescritivos(true);
		} else if (getTipoPessoa().equals("funcionario")) {
			setFuncionario(true);
			setMostrarCamposDescritivos(true);
		}
	}

	private void setarFalseNosFiltros() {
		setAluno(false);
		setProfessor(false);
		setCoordenador(false);
		setFuncionario(false);
		setMostrarCamposDescritivos(false);
	}

	public String getDescricao() {
		if (getTipoPessoa().equals("aluno")) {
			return "Aluno";
		} else if (getTipoPessoa().equals("professor")) {
			return "Professor";
		} else if (getTipoPessoa().equals("coordenador")) {
			return "Coordenador";
		} else if (getTipoPessoa().equals("funcionario")) {
			return "Funcionário";
		}
		return "";
	}

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("professor", "Professor"));
		itens.add(new SelectItem("coordenador", "Coordenador"));
		itens.add(new SelectItem("funcionario", "Funcionário"));
		return itens;
	}

	public List getListaSelectItemTipoOrdenacaoRelatorio() throws Exception {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("destinatario", "Destinatário"));
		itens.add(new SelectItem("remetente", "Remetente"));
		itens.add(new SelectItem("assunto", "Assunto"));
		itens.add(new SelectItem("dataEnvio", "Data Envio"));
		itens.add(new SelectItem("dataLeitura", "Data Leitura"));
		return itens;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("nomeCurso", "Nome Curso"));
		return itens;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public List getTipoConsultaComboProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List getTipoConsultaComboCoordenador() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public ComunicacaoInternaRelVO getComunicacaoInternaRelVO() {
		if (comunicacaoInternaRelVO == null) {
			comunicacaoInternaRelVO = new ComunicacaoInternaRelVO();
		}
		return comunicacaoInternaRelVO;
	}

	public void setComunicacaoInternaRelVO(ComunicacaoInternaRelVO comunicacaoInternaRelVO) {
		this.comunicacaoInternaRelVO = comunicacaoInternaRelVO;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Boolean getAluno() {
		if (aluno == null) {
			aluno = Boolean.FALSE;
		}
		return aluno;
	}

	public void setAluno(Boolean aluno) {
		this.aluno = aluno;
	}

	public Boolean getProfessor() {
		if (professor == null) {
			professor = Boolean.FALSE;
		}
		return professor;
	}

	public void setProfessor(Boolean professor) {
		this.professor = professor;
	}

	public Boolean getCoordenador() {
		if (coordenador == null) {
			coordenador = Boolean.FALSE;
		}
		return coordenador;
	}

	public void setCoordenador(Boolean coordenador) {
		this.coordenador = coordenador;
	}

	public Boolean getFuncionario() {
		if (funcionario == null) {
			funcionario = Boolean.FALSE;
		}
		return funcionario;
	}

	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getMostrarCamposDescritivos() {
		if (mostrarCamposDescritivos == null) {
			mostrarCamposDescritivos = Boolean.FALSE;
		}
		return mostrarCamposDescritivos;
	}

	public void setMostrarCamposDescritivos(Boolean mostrarCamposDescritivos) {
		this.mostrarCamposDescritivos = mostrarCamposDescritivos;
	}

	public String getCampoDescritivo() {
		if (campoDescritivo == null) {
			campoDescritivo = "";
		}
		return campoDescritivo;
	}

	public void setCampoDescritivo(String campoDescritivo) {
		this.campoDescritivo = campoDescritivo;
	}

	public String getValorDescritivo() {
		if (valorDescritivo == null) {
			valorDescritivo = "";
		}
		return valorDescritivo;
	}

	public void setValorDescritivo(String valorDescritivo) {
		this.valorDescritivo = valorDescritivo;
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

	public Integer getCodigoPessoa() {
		if (codigoPessoa == null) {
			codigoPessoa = 0;
		}
		return codigoPessoa;
	}

	public void setCodigoPessoa(Integer codigoPessoa) {
		this.codigoPessoa = codigoPessoa;
	}

	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
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

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
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

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public String getCampoConsultaCoordenador() {
		if (campoConsultaCoordenador == null) {
			campoConsultaCoordenador = "";
		}
		return campoConsultaCoordenador;
	}

	public void setCampoConsultaCoordenador(String campoConsultaCoordenador) {
		this.campoConsultaCoordenador = campoConsultaCoordenador;
	}

	public String getValorConsultaCoordenador() {
		if (valorConsultaCoordenador == null) {
			valorConsultaCoordenador = "";
		}
		return valorConsultaCoordenador;
	}

	public void setValorConsultaCoordenador(String valorConsultaCoordenador) {
		this.valorConsultaCoordenador = valorConsultaCoordenador;
	}

	public List getListaConsultaCoordenador() {
		if (listaConsultaCoordenador == null) {
			listaConsultaCoordenador = new ArrayList(0);
		}
		return listaConsultaCoordenador;
	}

	public void setListaConsultaCoordenador(List listaConsultaCoordenador) {
		this.listaConsultaCoordenador = listaConsultaCoordenador;
	}

	public Boolean getFiltroLeituraRegistrada() {
		if (filtroLeituraRegistrada == null) {
			filtroLeituraRegistrada = Boolean.FALSE;
		}
		return filtroLeituraRegistrada;
	}

	public void setFiltroLeituraRegistrada(Boolean filtroLeituraRegistrada) {
		this.filtroLeituraRegistrada = filtroLeituraRegistrada;
	}

	public String getTipoOrdenacao() {
		if (tipoOrdenacao == null) {
			tipoOrdenacao = "";
		}
		return tipoOrdenacao;
	}

	public void setTipoOrdenacao(String tipoOrdenacao) {
		this.tipoOrdenacao = tipoOrdenacao;
	}
}
