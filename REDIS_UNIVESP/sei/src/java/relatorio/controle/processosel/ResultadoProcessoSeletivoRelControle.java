/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author Philippe
 */
@Controller("ResultadoProcessoSeletivoRelControle")
@Scope("viewScope")
@Lazy
public class ResultadoProcessoSeletivoRelControle extends SuperControleRelatorio {

	private List listaSelectItemProcessoSeletivo;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private ProcSeletivoVO processoSeletivoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String ordenacao;
	private Boolean apresentarNota;
	private String processoSeletivoApresentar;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
	private List<ProcSeletivoVO> processoSeletivoVOs;
	private Boolean marcarTodosProcessosSeletivos;
	private List listaSelectItemDataProva;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivo;
	private Boolean apresentarNotaPorDisciplina;
	private Date dataProvaInicio;
	private Date dataProvaFim;
	private String ano;
	private String semestre;
	private Boolean apresentarQuantidadeAcerto;
	private Boolean apresentarSituacaoResultadoProcessoSeletivo;
	private Boolean selecionarTodasOpcoesFiltroApresentacao;
	private List<InscricaoVO> listaConsultaInscricao;
	private String campoConsultaInscricao;
	private String tipoLayout;
	private String valorConsultaInscricao;
	private InscricaoVO inscricaoVO;
	private String descricaoInscricao;

	public ResultadoProcessoSeletivoRelControle() {
//		montarListaSelectItemProcessoSeletivo();
	}
	
	@PostConstruct
    public void realizarCarregamentoInscricaoVindoTelaFichaAluno() {
    	InscricaoVO obj = (InscricaoVO) context().getExternalContext().getSessionMap().get("inscricaoFichaAluno");
    	if (obj != null && !obj.getCodigo().equals(0)) {
    		try {
    			obj = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
    			setInscricaoVO(new InscricaoVO());
    			getInscricaoVO().setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
    			setInscricaoVO(obj);
    			setDescricaoInscricao(getInscricaoVO().getCodigo() + " - " + getInscricaoVO().getCandidato().getNome());
    			setProcessoSeletivoVO(obj.getProcSeletivo());
    			setAno(obj.getProcSeletivo().getAno());
    			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
    			setMensagemID("msg_dados_editar");
    		} catch (Exception e) {
    			setInscricaoVO(new InscricaoVO());
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		} finally {
    			context().getExternalContext().getSessionMap().remove("inscricaoFichaAluno");
    		}
    	}
	}
	
	@PostConstruct
	public void inicializarDados() {
		Map<String, String> campos = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(null, "resultadoProcessoSeletivoRel");
		if(campos != null && !campos.isEmpty()){
			setApresentarNota(campos.containsKey("apresentarNota") && Uteis.isAtributoPreenchido(campos.get("apresentarNota")) && campos.get("apresentarNota").equals("true"));
			setApresentarNotaPorDisciplina(campos.containsKey("apresentarNotaPorDisciplina") && Uteis.isAtributoPreenchido(campos.get("apresentarNotaPorDisciplina")) && campos.get("apresentarNotaPorDisciplina").equals("true"));
			setApresentarQuantidadeAcerto(campos.containsKey("apresentarQuantidadeAcerto") && Uteis.isAtributoPreenchido(campos.get("apresentarQuantidadeAcerto")) && campos.get("apresentarQuantidadeAcerto").equals("true"));
			setApresentarSituacaoResultadoProcessoSeletivo(campos.containsKey("apresentarSituacaoResultadoProcessoSeletivo") && Uteis.isAtributoPreenchido(campos.get("apresentarSituacaoResultadoProcessoSeletivo")) && campos.get("apresentarSituacaoResultadoProcessoSeletivo").equals("true"));
			setSelecionarTodasOpcoesFiltroApresentacao(campos.containsKey("selecionarTodasOpcoesFiltroApresentacao") && Uteis.isAtributoPreenchido(campos.get("selecionarTodasOpcoesFiltroApresentacao")) && campos.get("selecionarTodasOpcoesFiltroApresentacao").equals("true"));

			setOrdenacao(campos.containsKey("ordenacao")?campos.get("ordenacao"):"nomeCandidatoProcessoSeletivo");
			setSituacaoResultadoProcessoSeletivo(campos.containsKey("situacaoResultadoProcessoSeletivo") ? SituacaoResultadoProcessoSeletivoEnum.valueOf(campos.get("situacaoResultadoProcessoSeletivo")):SituacaoResultadoProcessoSeletivoEnum.APROVADO);
			
		}
	}

	public void imprimirPDF() {
		String design = null;
		String titulo = null;
		List listaObjetos = new ArrayList(0);
		try {
			design = getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().designIReportRelatorio();
			titulo = "RESULTADO PROCESSO SELETIVO";
			if (getTipoLayout().equals("layout1")) {
				design = getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().designIReportRelatorio();
				titulo = "RESULTADO PROCESSO SELETIVO";
			} else {
				design = getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().designIReportRelatorioFicha();
				titulo = "FICHA DO CANDIDATO";
				setApresentarNotaPorDisciplina(Boolean.TRUE);
				setApresentarNota(Boolean.TRUE);
			}
			listaObjetos = getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().criarObjeto(getProcessoSeletivoVOs(), getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getApresentarNota(), getApresentarNotaPorDisciplina(), getOrdenacao(), getSituacaoResultadoProcessoSeletivo(), getDataProvaInicio(), getDataProvaFim(), getAno(), getSemestre(), getApresentarQuantidadeAcerto(), getApresentarSituacaoResultadoProcessoSeletivo(), getInscricaoVO(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				realizarImpressaoRelatorio();
				persistirDadosPadroesGeracaoRelatorio();
				removerObjetoMemoria(this);
				consultarProcessoSeletivo();
				inicializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}
	
	public void persistirDadosPadroesGeracaoRelatorio() throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarNota() ? "true" : "false", "resultadoProcessoSeletivoRel", "apresentarNota", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarNotaPorDisciplina() ? "true" : "false", "resultadoProcessoSeletivoRel", "apresentarNotaPorDisciplina", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarQuantidadeAcerto() ? "true" : "false", "resultadoProcessoSeletivoRel", "apresentarQuantidadeAcerto", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarSituacaoResultadoProcessoSeletivo() ? "true" : "false", "resultadoProcessoSeletivoRel", "apresentarSituacaoResultadoProcessoSeletivo", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSelecionarTodasOpcoesFiltroApresentacao() ? "true" : "false", "resultadoProcessoSeletivoRel", "selecionarTodasOpcoesFiltroApresentacao", getUsuarioLogado());
		
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOrdenacao(), "resultadoProcessoSeletivoRel", "ordenacao", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSituacaoResultadoProcessoSeletivo().toString(), "resultadoProcessoSeletivoRel", "situacaoResultadoProcessoSeletivo", getUsuarioLogado());
	}

	public void imprimirExcel() {
		String design = null;
		String titulo = null;
		List listaObjetos = new ArrayList(0);
		try {
			design = getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().designIReportRelatorioExcel();
			titulo = "RESULTADO PROCESSO SELETIVO";
			listaObjetos = getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().criarObjeto(getProcessoSeletivoVOs(), getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getApresentarNota(), getApresentarNotaPorDisciplina(), getOrdenacao(), getSituacaoResultadoProcessoSeletivo(), getDataProvaInicio(), getDataProvaFim(), getAno(), getSemestre(), getApresentarQuantidadeAcerto(), getApresentarSituacaoResultadoProcessoSeletivo(), getInscricaoVO(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getResultadoProcessoSeletivoRelInterfaceFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				realizarImpressaoRelatorio();
				persistirDadosPadroesGeracaoRelatorio();
				removerObjetoMemoria(this);
				consultarProcessoSeletivo();
				inicializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void montarListaSelectItemProcessoSeletivo() {
		try {
			montarListaSelectItemProcessoSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void montarListaSelectItemProcessoSeletivo(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(prm,  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			Ordenacao.ordenarListaDecrescente(resultadoConsulta, "dataInicio");
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
			setListaSelectItemProcessoSeletivo(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public void montarListaSelectDataProva() throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (!getProcessoSeletivoVOs().isEmpty()) {
				resultadoConsulta = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorListaProcSeletivoComboBox(getProcessoSeletivoVOs(), getUsuarioLogado());
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(null, ""));
			while (i.hasNext()) {
				Date dataProva = (Date) i.next();
				objs.add(new SelectItem(dataProva, Uteis.getDataAno4Digitos(dataProva)));
			}
			setListaSelectItemDataProva(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			setUnidadeEnsinoCursoVO(obj);
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoOrdenacaoCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeCandidatoProcessoSeletivo", "Nome Candidato / Processo Seletivo"));
		itens.add(new SelectItem("classificacaoCandidatoProcessoSeletivo", "Classificação / Processo Seletivo"));
		itens.add(new SelectItem("processoSeletivoNomeCandidato", "Processo Seletivo / Nome Candidato"));
		itens.add(new SelectItem("processoSeletivoClassificacaoCandidato", "Processo Seletivo / Classificação"));
		return itens;
	}

	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurno() {
		try {
			setTurnoApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparProcessoSeletivo() {
		try {
			setProcessoSeletivoApresentar(null);
			setMarcarTodosProcessosSeletivos(false);
			marcarTodosProcessoSeletivoAction();
			limparUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarInscricao() {
		try {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			List<InscricaoVO> objs = new ArrayList<InscricaoVO>(0);
			if (getCampoConsultaInscricao().equals("codigo")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o C?DIGO desejado.");
				}
				int valorInt = Integer.parseInt(getValorConsultaInscricao());
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("nomePessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("cpfPessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o CPF do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCPFPessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaInscricao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarInscricao() {
		try {
			InscricaoVO inscricao = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
			inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(inscricao.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setInscricaoVO(new InscricaoVO());
			getInscricaoVO().setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			setInscricaoVO(inscricao);
			setDescricaoInscricao(getInscricaoVO().getCodigo() + " - " + getInscricaoVO().getCandidato().getNome());
//			getInscricaoVO().setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//			getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(inscricao);
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
		} catch (Exception e) {
			setDescricaoInscricao(null);
			setInscricaoVO(new InscricaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List getTipoLayoutCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("layout1", "Layout 1 - Resultado Processo Seletivo"));
		itens.add(new SelectItem("layout2", "Layout 2 - Ficha Candidado"));
		return itens;
	}
	
	public void limparInscricao() {
		try {
			setDescricaoInscricao(null);
			setInscricaoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboInscricao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("cpfPessoa", "CPF do Candidato"));
		itens.add(new SelectItem("nomePessoa", "Nome do Candidato"));
		itens.add(new SelectItem("codigo", "Número da Inscrição"));
		return itens;
	}
	
	public List getListaSelectItemProcessoSeletivo() {
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public ProcSeletivoVO getProcessoSeletivoVO() {
		if (processoSeletivoVO == null) {
			processoSeletivoVO = new ProcSeletivoVO();
		}
		return processoSeletivoVO;
	}

	public void setProcessoSeletivoVO(ProcSeletivoVO processoSeletivoVO) {
		this.processoSeletivoVO = processoSeletivoVO;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Boolean getApresentarNota() {
		if (apresentarNota == null) {
			apresentarNota = Boolean.FALSE;
		}
		return apresentarNota;
	}

	public void setApresentarNota(Boolean apresentarNota) {
		this.apresentarNota = apresentarNota;
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		if(unidadeEnsinoApresentar.length() > 75) {
			return unidadeEnsinoApresentar.substring(0, 75) + "...";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		if(cursoApresentar.length() > 55) {
			return cursoApresentar.substring(0, 55) + "...";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		if(turnoApresentar.length() > 50) {
		 	return turnoApresentar.substring(0, 50) + "...";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}
	
	@PostConstruct
	public void consultarProcessoSeletivo() {
		try {
			consultarProcessoSeletivoFiltroRelatorio("");
			verificarTodosProcessosSeletivoSelecionadas();
			setMarcarTodasUnidadeEnsino(Boolean.TRUE);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	public void consultarUnidadeEnsino() {
		try {
			if (!getProcessoSeletivoVOs().isEmpty()) {
				getUnidadeEnsinoVOs().clear();
				if (getUnidadeEnsinoLogado().getCodigo() > 0) {
					setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProcSeletivoComboBox(getProcessoSeletivoVOs(), true, getUsuarioLogado()));
					for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
						obj.setFiltrarUnidadeEnsino(true);
					}
				} else {
					setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProcSeletivoComboBox(getProcessoSeletivoVOs(), false, getUsuarioLogado()));
				}
				verificarTodasUnidadesSelecionadas();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		consultarCursoFiltroRelatorio();
		consultarTurnoFiltroRelatorio();
	}
	
	public void consultarCursoFiltroRelatorio() {
		try {
			if (getUnidadeEnsinoVOs().isEmpty()) {
				setCursoVOs(null);
				setTurnoVOs(null);
				return;
			}
			setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", "", null, getUnidadeEnsinoVOs(), getUsuarioLogado()));
		} catch (Exception e) {
			setCursoVOs(null);
			
		}
	}
	
	public void consultarTurnoFiltroRelatorio() {
		try {
			setTurnoVOs(getFacadeFactory().getTurnoFacade().consultarTurnoPorProcSeletivoUnidadeEnsinosComboBox(getProcessoSeletivoVOs(), getUnidadeEnsinoVOs(), getUsuarioLogado()));
		} catch (Exception e) {
			setTurnoVOs(null);
			
		}
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursoVOs().get(0).getNome());
				}
			}
		}
	}

	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		if (getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
				}
			}
			setTurnoApresentar(turno.toString());
		} else {
			if (!getTurnoVOs().isEmpty()) {
				if (getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					setTurnoApresentar(getTurnoVOs().get(0).getNome());
				}
			} else {
				setTurnoApresentar(turno.toString());
			}
		}
	}
	
	public void verificarTodosProcessosSeletivoSelecionadas() throws Exception {
		StringBuilder processoSeletivo = new StringBuilder();
		if (getProcessoSeletivoVOs().size() > 1) {
			for (ProcSeletivoVO obj : getProcessoSeletivoVOs()) {
				if (obj.getFiltrarProcessoSeletivo()) {
					processoSeletivo.append(obj.getDescricao().trim()).append("; ");
				}
			}
			setProcessoSeletivoApresentar(processoSeletivo.toString());
		} else {
			if (!getProcessoSeletivoVOs().isEmpty()) {
				if (getProcessoSeletivoVOs().get(0).getFiltrarProcessoSeletivo()) {
					setProcessoSeletivoApresentar(getProcessoSeletivoVOs().get(0).getDescricao());
				}
			}
		}
		consultarUnidadeEnsino();
		montarListaSelectDataProva();
	}
	
	public void consultarProcessoSeletivoFiltroRelatorio(String descricao) {
		try {
			getProcessoSeletivoVOs().clear();
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				setProcessoSeletivoVOs(getFacadeFactory().getProcSeletivoFacade().consultaRapidaComboBoxPorDescricaoUnidadeEnsino(descricao, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			} else {
				setProcessoSeletivoVOs(getFacadeFactory().getProcSeletivoFacade().consultaRapidaComboBoxProcessoSeletivoFaltandoLista(getProcessoSeletivoVOs(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList<UnidadeEnsinoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	public List<ProcSeletivoVO> getProcessoSeletivoVOs() {
		if (processoSeletivoVOs == null) {
			processoSeletivoVOs = new ArrayList<>(0);
		}
		return processoSeletivoVOs;
	}

	public void setProcessoSeletivoVOs(List<ProcSeletivoVO> processoSeletivoVOs) {
		this.processoSeletivoVOs = processoSeletivoVOs;
	}

	public String getProcessoSeletivoApresentar() {
		if (processoSeletivoApresentar == null) {
			processoSeletivoApresentar = "";
		}
		if(processoSeletivoApresentar.length() > 85) {
			return processoSeletivoApresentar.substring(0, 85) + "...";
		}
		return processoSeletivoApresentar;
	}

	public void setProcessoSeletivoApresentar(String processoSeletivoApresentar) {
		this.processoSeletivoApresentar = processoSeletivoApresentar;
	}
	
	public void marcarTodosProcessoSeletivoAction() throws Exception {
		for (ProcSeletivoVO procSeletivo : getProcessoSeletivoVOs()) {
			if (getMarcarTodosProcessosSeletivos()) {
				procSeletivo.setFiltrarProcessoSeletivo(Boolean.TRUE);
			} else {
				procSeletivo.setFiltrarProcessoSeletivo(Boolean.FALSE);
			}
		}
		verificarTodosProcessosSeletivoSelecionadas();
	}

	public Boolean getMarcarTodosProcessosSeletivos() {
		if (marcarTodosProcessosSeletivos == null) {
			marcarTodosProcessosSeletivos = Boolean.FALSE;
		}
		return marcarTodosProcessosSeletivos;
	}

	public void setMarcarTodosProcessosSeletivos(Boolean marcarTodosProcessosSeletivos) {
		this.marcarTodosProcessosSeletivos = marcarTodosProcessosSeletivos;
	}

	public List getListaSelectItemDataProva() {
		if (listaSelectItemDataProva == null) {
			listaSelectItemDataProva = new ArrayList(0);
		}
		return listaSelectItemDataProva;
	}

	public void setListaSelectItemDataProva(List listaSelectItemDataProva) {
		this.listaSelectItemDataProva = listaSelectItemDataProva;
	}

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
		if (itemProcSeletivoDataProvaVO == null) {
			itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProvaVO;
	}

	public void setItemProcSeletivoDataProvaVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
		this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
	}

	public Integer getColumnProcessoSeletivo() throws Exception {
		if (getProcessoSeletivoVOs().size() > 4) {
			return 4;
		}
		return getProcessoSeletivoVOs().size();
	}

	public Integer getElementProcessoSeletivo() throws Exception {
		return getProcessoSeletivoVOs().size();
	}

	public List<SelectItem> getListaSelectItemSituacaoResultadoProcessoSeletivo() {
		return SituacaoResultadoProcessoSeletivoEnum.getSituacaoResultadoProcessoSeletivo();
	}

	public SituacaoResultadoProcessoSeletivoEnum getSituacaoResultadoProcessoSeletivo() {
		if (situacaoResultadoProcessoSeletivo == null) {
			situacaoResultadoProcessoSeletivo = SituacaoResultadoProcessoSeletivoEnum.APROVADO;
		}
		return situacaoResultadoProcessoSeletivo;
	}

	public void setSituacaoResultadoProcessoSeletivo(SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivo) {
		this.situacaoResultadoProcessoSeletivo = situacaoResultadoProcessoSeletivo;
	}

	public Boolean getApresentarNotaPorDisciplina() {
		if (apresentarNotaPorDisciplina == null) {
			apresentarNotaPorDisciplina = Boolean.FALSE;
		}
		return apresentarNotaPorDisciplina;
	}

	public void setApresentarNotaPorDisciplina(Boolean apresentarNotaPorDisciplina) {
		this.apresentarNotaPorDisciplina = apresentarNotaPorDisciplina;
	}
	
	public Date getDataProvaInicio() {
		return dataProvaInicio;
	}

	public void setDataProvaInicio(Date dataProvaInicio) {
		this.dataProvaInicio = dataProvaInicio;
	}

	public Date getDataProvaFim() {
		return dataProvaFim;
	}

	public void setDataProvaFim(Date dataProvaFim) {
		this.dataProvaFim = dataProvaFim;
	}
	
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual4Digitos();
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

	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}
	
	public Boolean getApresentarQuantidadeAcerto() {
		if (apresentarQuantidadeAcerto == null) {
			apresentarQuantidadeAcerto = Boolean.FALSE;
		}
		return apresentarQuantidadeAcerto;
	}

	public void setApresentarQuantidadeAcerto(Boolean apresentarQuantidadeAcerto) {
		this.apresentarQuantidadeAcerto = apresentarQuantidadeAcerto;
	}

	public Boolean getApresentarSituacaoResultadoProcessoSeletivo() {
		if (apresentarSituacaoResultadoProcessoSeletivo == null) {
			apresentarSituacaoResultadoProcessoSeletivo = Boolean.FALSE;
		}
		return apresentarSituacaoResultadoProcessoSeletivo;
	}

	public void setApresentarSituacaoResultadoProcessoSeletivo(Boolean apresentarSituacaoResultadoProcessoSeletivo) {
		this.apresentarSituacaoResultadoProcessoSeletivo = apresentarSituacaoResultadoProcessoSeletivo;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodasSituacoesApresentacao() {
		if (getSelecionarTodasOpcoesFiltroApresentacao()) {
			realizarMarcacaoTodasSituacoes();
		} else {
			realizarDesmarcarTodasSituacoes();
		}
	}
	
	public void realizarMarcacaoTodasSituacoes() {
		setApresentarNota(true);
		setApresentarNotaPorDisciplina(true);
		setApresentarQuantidadeAcerto(true);
		setApresentarSituacaoResultadoProcessoSeletivo(true);
	}
	
	public void realizarDesmarcarTodasSituacoes() {
		setApresentarNota(false);
		setApresentarNotaPorDisciplina(false);
		setApresentarQuantidadeAcerto(false);
		setApresentarSituacaoResultadoProcessoSeletivo(false);
	}

	public Boolean getSelecionarTodasOpcoesFiltroApresentacao() {
		if (selecionarTodasOpcoesFiltroApresentacao == null) {
			selecionarTodasOpcoesFiltroApresentacao = Boolean.FALSE;
		}
		return selecionarTodasOpcoesFiltroApresentacao;
	}

	public void setSelecionarTodasOpcoesFiltroApresentacao(Boolean selecionarTodasOpcoesFiltroApresentacao) {
		this.selecionarTodasOpcoesFiltroApresentacao = selecionarTodasOpcoesFiltroApresentacao;
	}

	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoesApresentacao() {
		if (getSelecionarTodasOpcoesFiltroApresentacao()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	

	public void setListaConsultaInscricao(List<InscricaoVO> listaConsultaInscricao) {
		this.listaConsultaInscricao = listaConsultaInscricao;
	}

	public List<InscricaoVO> getListaConsultaInscricao() {
		if (listaConsultaInscricao == null) {
			listaConsultaInscricao = new ArrayList<InscricaoVO>(0);
		}
		return listaConsultaInscricao;
	}
	
	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}

	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}

	public void setCampoConsultaInscricao(String campoConsultaInscricao) {
		this.campoConsultaInscricao = campoConsultaInscricao;
	}

	public String getCampoConsultaInscricao() {
		if (campoConsultaInscricao == null) {
			campoConsultaInscricao = "nomeCandidato";
		}
		return campoConsultaInscricao;
	}

	public void setValorConsultaInscricao(String valorConsultaInscricao) {
		this.valorConsultaInscricao = valorConsultaInscricao;
	}

	public Boolean getDesabilitarInscricao() {
		return getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().intValue() != 0;
	}

	public String getValorConsultaInscricao() {
		return valorConsultaInscricao;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "layout1";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getDescricaoInscricao() {
		if (descricaoInscricao == null) {
			descricaoInscricao = "";
		}
		return descricaoInscricao;
	}

	public void setDescricaoInscricao(String descricaoInscricao) {
		this.descricaoInscricao = descricaoInscricao;
	}	
}
