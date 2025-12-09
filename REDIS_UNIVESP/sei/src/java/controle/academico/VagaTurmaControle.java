package controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.Turma;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas turmaForm.jsp turmaCons.jsp) com as funcionalidades da classe
 * <code>Turma</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Turma
 * @see TurmaVO
 */

@Controller("VagaTurmaControle")
@Scope("viewScope")
public class VagaTurmaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private VagaTurmaVO vagaTurmaVO;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemSituacaoTurma;
	private List<SelectItem> listaSelectItemSituacaoTipoTurma;
	private String situacaoTurma;
	private String situacaoTipoTurma;

	public VagaTurmaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("turma");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Turma</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setVagaTurmaVO(null);
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("vagaTurmaForm.xhtml");
	}

	public String editar() {
		try {
			setVagaTurmaVO((VagaTurmaVO) getRequestMap().get("vagaTurmaItem"));
			setVagaTurmaVO(getFacadeFactory().getVagaTurmaFacade().consultarPorChavePrimaria(getVagaTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFacadeFactory().getTurmaFacade().carregarDados(getVagaTurmaVO().getTurmaVO(), getUsuarioLogado());
			setControleConsulta(new ControleConsulta());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("vagaTurmaForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Turma</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			if(getVagaTurmaVO().getTurmaVO().getTurmaAgrupada()) {
				persistirVagaTurmaAgrupada(getVagaTurmaVO());
			}else {
				getVagaTurmaVO().setUsuarioResponsavel(getUsuarioLogadoClone());
				getFacadeFactory().getVagaTurmaFacade().persistir(getVagaTurmaVO(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void persistirVagaTurmaAgrupada(VagaTurmaVO vagaTurmaVO2) {
		try {
			getFacadeFactory().getVagaTurmaFacade().persistirVagaTurmaBase(vagaTurmaVO2, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox
	 * denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("turma")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getVagaTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), getSituacaoTurma(), getSituacaoTipoTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getVagaTurmaFacade().consultaRapidaPorCodigoTurma(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), getSituacaoTurma(), getSituacaoTipoTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getVagaTurmaFacade().consultaRapidaPorNomeDisciplina(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), getSituacaoTurma(), getSituacaoTipoTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoDisciplina")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getVagaTurmaFacade().consultaRapidaPorCodigoDisciplina(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), getSituacaoTurma(), getSituacaoTipoTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsulta().setListaConsulta(new ArrayList<VagaTurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("vagaTurmaCons.xhtml");
	}

	public void consultarTurma() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), getVagaTurmaVO().getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, false, getSituacaoTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			/*if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getControleConsulta().getValorConsulta(), getVagaTurmaVO().getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}*/
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCursoSituacaoTruma(getControleConsulta().getValorConsulta(), getVagaTurmaVO().getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getSituacaoTurma(), getUsuarioLogado()));
			}
			/*if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getControleConsulta().getValorConsulta(), getVagaTurmaVO().getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}*/
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsulta().setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaDisciplinaPorTurma() {
		try {
			List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOs = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultaRapidaPorTurma(getVagaTurmaVO().getTurmaVO().getCodigo(), getVagaTurmaVO().getTurmaVO().getCodigo(), getUsuarioLogado());
			for(VagaTurmaDisciplinaVO obj: vagaTurmaDisciplinaVOs){
				if(!getVagaTurmaVO().getVagaTurmaDisciplinaVOs().contains(obj)){					
					obj.setNrVagasMatricula(getVagaTurmaVO().getTurmaVO().getNrMaximoMatricula());
					obj.setNrMaximoMatricula(getVagaTurmaVO().getTurmaVO().getNrVagas());
					obj.setNrVagasMatriculaReposicao(getVagaTurmaVO().getTurmaVO().getNrVagasInclusaoReposicao());
					getVagaTurmaVO().getVagaTurmaDisciplinaVOs().add(obj);
				}
			}	
			for (Iterator<VagaTurmaDisciplinaVO> iterator = vagaTurmaDisciplinaVOs.iterator(); iterator.hasNext();) {
				VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO = iterator.next();
				if(!vagaTurmaDisciplinaVOs.contains(vagaTurmaDisciplinaVO)){
					iterator.remove();
				}				
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerVagaDisciplinaTurma() {
		try {
			getVagaTurmaVO().getVagaTurmaDisciplinaVOs().remove((VagaTurmaDisciplinaVO) getRequestMap().get("vagaTurmaDisciplinaItem"));
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			getVagaTurmaVO().setTurmaVO((TurmaVO) getRequestMap().get("turmaItem"));			
			setControleConsulta(new ControleConsulta());
			consultarTurmaDisciplinaPorTurma();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>TurmaVO</code> Após a exclusão ela automaticamente aciona a rotina para
	 * uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getVagaTurmaFacade().excluir(getVagaTurmaVO(), getUsuarioLogado());
			removerObjetoMemoria(this);
			setVagaTurmaVO(null);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarDisciplinasTurmaComBaseGradeCurricular() {
		try {
			if (!getVagaTurmaVO().getTurmaVO().getIntegral() && getVagaTurmaVO().getAno().length() > 0 && getVagaTurmaVO().getAno().length() < 4){
				throw new Exception("O campo ANO deve possuir 4 dígitos.");
			}
			consultarTurmaDisciplinaPorTurma();
			setMensagemID("msg_Turma_CarregarDisciplinasDaGrade");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("codigoDisciplina", "Código Disciplina"));
		itens.add(new SelectItem("nomeDisciplina", "Nome Disciplina"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("situacaoTurma", "Situação (Aberta/Fechada)"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("turma");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("vagaTurmaCons.xhtml");
	}

	public VagaTurmaVO getVagaTurmaVO() {
		if (vagaTurmaVO == null) {
			vagaTurmaVO = new VagaTurmaVO();
		}
		return vagaTurmaVO;
	}

	public void setVagaTurmaVO(VagaTurmaVO vagaTurmaVO) {
		this.vagaTurmaVO = vagaTurmaVO;
	}

	public boolean getIsApresentarAno() {
		if (Uteis.isAtributoPreenchido(getVagaTurmaVO().getTurmaVO()) && (!getVagaTurmaVO().getTurmaVO().getIntegral())) {
			return true;
		}
		getVagaTurmaVO().setAno("");
		getVagaTurmaVO().setSemestre("");
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (Uteis.isAtributoPreenchido(getVagaTurmaVO().getTurmaVO()) && (getVagaTurmaVO().getTurmaVO().getSemestral())) {
			return true;
		}
		getVagaTurmaVO().setSemestre("");
		return false;
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

	@PostConstruct
	public void inicializarDadosNavegacaoPagina() {
		if (context().getExternalContext().getSessionMap().containsKey("vagaTurma.turma")) {
			novo();
			getVagaTurmaVO().getTurmaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("vagaTurma.turma"));
			context().getExternalContext().getSessionMap().remove("vagaTurma.turma");
			getVagaTurmaVO().setAno((String) context().getExternalContext().getSessionMap().get("vagaTurma.ano"));
			context().getExternalContext().getSessionMap().remove("vagaTurma.ano");
			getVagaTurmaVO().setSemestre((String) context().getExternalContext().getSessionMap().get("vagaTurma.semestre"));
			context().getExternalContext().getSessionMap().remove("vagaTurma.semestre");
			VagaTurmaVO vaga;
			try {
				vaga = getFacadeFactory().getVagaTurmaFacade().consultaRapidaUnicidadeTurmaPorIdentificador(getVagaTurmaVO().getTurmaVO().getCodigo(), getVagaTurmaVO().getAno(), getVagaTurmaVO().getSemestre(), getVagaTurmaVO().getCodigo());
				if (Uteis.isAtributoPreenchido(vaga.getCodigo())) {
					vaga = getFacadeFactory().getVagaTurmaFacade().consultarPorChavePrimaria(vaga.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					getFacadeFactory().getTurmaFacade().carregarDados(vaga.getTurmaVO(), getUsuarioLogado());
					vaga.setNovoObj(Boolean.FALSE);
					setVagaTurmaVO(vaga);
				} else {
					getFacadeFactory().getTurmaFacade().carregarDados(getVagaTurmaVO().getTurmaVO(), getUsuarioLogado());
					consultarTurmaDisciplinaPorTurma();
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	public void realizarNavegacaoProgramacaoAula() {
		context().getExternalContext().getSessionMap().put("programacaoAula.turma", getVagaTurmaVO().getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("programacaoAula.ano", getVagaTurmaVO().getAno());
		context().getExternalContext().getSessionMap().put("programacaoAula.semestre", getVagaTurmaVO().getSemestre());
	}

	public List<SelectItem> getListaSelectItemSituacaoTipoTurma() {
		if (listaSelectItemSituacaoTipoTurma == null) {
			listaSelectItemSituacaoTipoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("", ""));
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("normal", "Normal"));
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("agrupada", "Agrupada"));
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("subturma", "Subturma"));
		}
		return listaSelectItemSituacaoTipoTurma;
	}

	public List<SelectItem> getListaSelectItemSituacaoTurma() {
		if (listaSelectItemSituacaoTurma == null) {
			listaSelectItemSituacaoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTurma.add(new SelectItem("", ""));
			listaSelectItemSituacaoTurma.add(new SelectItem("AB", "Aberta"));
			listaSelectItemSituacaoTurma.add(new SelectItem("FE", "Fechada"));
		}
		return listaSelectItemSituacaoTurma;
	}

	public String getSituacaoTurma() {
		if (situacaoTurma == null) {
			situacaoTurma = "";
		}
		return situacaoTurma;
	}

	public void setSituacaoTurma(String situacaoTurma) {
		this.situacaoTurma = situacaoTurma;
	}

	public String getSituacaoTipoTurma() {
		if (situacaoTipoTurma == null) {
			situacaoTipoTurma = "";
		}
		return situacaoTipoTurma;
	}

	public void setSituacaoTipoTurma(String situacaoTipoTurma) {
		this.situacaoTipoTurma = situacaoTipoTurma;
	}

	public void montarTurma() {
		try {
			if (!getVagaTurmaVO().getTurmaVO().getIdentificadorTurma().equals("")) {
				getVagaTurmaVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getVagaTurmaVO().getTurmaVO().getIdentificadorTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				if (getVagaTurmaVO().getTurmaVO().getSemestral()) {
					setSemestre(Uteis.getSemestreAtual());
					setAno(Uteis.getAnoDataAtual4Digitos());
				} else if (getVagaTurmaVO().getTurmaVO().getAnual()) {
					setSemestre("");
					setAno(Uteis.getAnoDataAtual4Digitos());
				} else {
					setSemestre("");
					setAno("");
				}
				consultarTurmaDisciplinaPorTurma();
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_turma"));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		getVagaTurmaVO().setTurmaVO(new TurmaVO());
		getVagaTurmaVO().setVagaTurmaDisciplinaVOs(new ArrayList<VagaTurmaDisciplinaVO>(0));
		setMensagemID("msg_entre_dados");
	}
	
	public boolean getApresentarCampoValorConsultaSomenteValorNumerico() {
		return getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("codigoDisciplina"); 
	}
	
	public void limparDadosConsulta() {
		getControleConsulta().setValorConsulta("");
		getControleConsulta().setListaConsulta(new ArrayList<VagaTurmaVO>(0));
		setMensagemID("msg_entre_prmconsulta");
	}
	
	private Integer nrVagaReplicar;
	private Integer nrMaximoVagaReplicar;
	private Integer nrVagaReposicaoReplicar;
	
	
	public void realizarReplicacaoNumeroVaga(String tipoVagaReplicar) {
		for(VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO: getVagaTurmaVO().getVagaTurmaDisciplinaVOs()) {
			if(tipoVagaReplicar.equals("nrVaga")) {
				vagaTurmaDisciplinaVO.setNrVagasMatricula(nrVagaReplicar);
			}else if(tipoVagaReplicar.equals("nrMaximoVaga")) { 
				vagaTurmaDisciplinaVO.setNrMaximoMatricula(nrMaximoVagaReplicar);
			}else {
				vagaTurmaDisciplinaVO.setNrVagasMatriculaReposicao(nrVagaReposicaoReplicar);
			}
		}		
	}

	public Integer getNrVagaReplicar() {
		if (nrVagaReplicar == null) {
			nrVagaReplicar = 0;
		}
		return nrVagaReplicar;
	}

	public void setNrVagaReplicar(Integer nrVagaReplicar) {
		this.nrVagaReplicar = nrVagaReplicar;
	}

	public Integer getNrMaximoVagaReplicar() {
		if (nrMaximoVagaReplicar == null) {
			nrMaximoVagaReplicar = 0;
		}
		return nrMaximoVagaReplicar;
	}

	public void setNrMaximoVagaReplicar(Integer nrMaximoVagaReplicar) {
		this.nrMaximoVagaReplicar = nrMaximoVagaReplicar;
	}

	public Integer getNrVagaReposicaoReplicar() {
		if (nrVagaReposicaoReplicar == null) {
			nrVagaReposicaoReplicar = 0;
		}
		return nrVagaReposicaoReplicar;
	}

	public void setNrVagaReposicaoReplicar(Integer nrVagaReposicaoReplicar) {
		this.nrVagaReposicaoReplicar = nrVagaReposicaoReplicar;
	}

	
}
