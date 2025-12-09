package controle.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.processosel.ResultadoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.processosel.ResultadoProcessoSeletivo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas resultadoProcessoSeletivoForm.jsp
 * resultadoProcessoSeletivoCons.jsp) com as funcionalidades da classe <code>ResultadoProcessoSeletivo</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see ResultadoProcessoSeletivo
 * @see ResultadoProcessoSeletivoVO
 */

@Controller("ResultadoProcessoSeletivoControle")
@Scope("viewScope")
public class ResultadoProcessoSeletivoControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private Boolean opcao1;
	private Boolean opcao2;
	private Boolean opcao3;	
	private ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO;
	private List<SelectItem> listaSelectItemDisciplinaProcSeletivo;
	private List<SelectItem> listaSelectItemSalaProcSeletivo;
	private List<InscricaoVO> listaConsultaInscricao;
	private List<ResultadoProcessoSeletivoVO> listaResultadoProcessoSeletivo;
	private String campoConsultaInscricao;
	private String valorConsultaInscricao;
	private List<SelectItem> listaSelectItemProcSeletivo;
	private List<SelectItem> listaSelectItemItemDataProcSeletivo;
	private InscricaoVO inscricaoVO;
	private String controleAbas;
	private Integer dataProva;
	private Boolean exibirResultadoPorProcessoSeletivo;
	private Boolean exibirResultadoPorProcessoSeletivoNotaDisciplina;
	private Integer sala;
	private InscricaoVO inscricaoNaoCompareceu;
	private DisciplinasProcSeletivoVO disciplinasProcSeletivoVO;
	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO;
	private List<InscricaoVO> listaInscricaoNaoComparecidosVOs;
	protected List listaSelectItemCursoOpcao;
	private InscricaoVO inscricaoRedacao;
	private Boolean possuiRedacao;
    private List<InscricaoVO> listaInscricaoComProvaRedacao;
	private  Boolean exibirQuanditadeAcertos;
	

	public ResultadoProcessoSeletivoControle() throws Exception {
		// obterUsuarioLogado();
		setOpcao1(Boolean.FALSE);
		setOpcao2(Boolean.FALSE);
		setOpcao3(Boolean.FALSE);
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}
	
	@PostConstruct
    public void realizarCarregamentoInscricaoVindoTelaFichaAluno() {
    	InscricaoVO obj = (InscricaoVO) context().getExternalContext().getSessionMap().get("inscricaoFichaAluno");
    	if (obj != null && !obj.getCodigo().equals(0)) {
    		try {
    			setInscricaoVO(new InscricaoVO());
    			setInscricaoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(obj, getUsuarioLogado()));
    			if (getInscricaoVO().getCursoOpcao1().getCodigo().intValue() != 0) {
    				setOpcao1(Boolean.TRUE);
    			} else {
    				setOpcao1(Boolean.FALSE);
    			}
    			if (getInscricaoVO().getCursoOpcao2().getCodigo().intValue() != 0) {
    				setOpcao2(Boolean.TRUE);
    			} else {
    				setOpcao2(Boolean.FALSE);
    			}
    			if (getInscricaoVO().getCursoOpcao3().getCodigo().intValue() != 0) {
    				setOpcao3(Boolean.TRUE);
    			} else {
    				setOpcao3(Boolean.FALSE);
    			}											
    			if (getInscricaoVO().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA")) {				
    				if (!Uteis.isAtributoPreenchido(getInscricaoVO().getGabaritoVO()) || getInscricaoVO().getGabaritoVO().getControlarGabaritoPorDisciplina()) {
    					setControleAbas("disciplinaProcSeletivo");
    				} else {
    					setControleAbas("notaGabaritoProcSeletivo");
    				}
    			} else {				
    				setControleAbas("disciplinaProcSeletivo");
    			}			
    			setExibirResultadoPorProcessoSeletivo(true);
    			setExibirResultadoPorProcessoSeletivoNotaDisciplina(false);
    			setMensagemID("msg_dados_editar");
    		} catch (Exception e) {
    			setInscricaoVO(new InscricaoVO());
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		} finally {
    			context().getExternalContext().getSessionMap().remove("inscricaoFichaAluno");
    		}
    	}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ResultadoProcessoSeletivo</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setInscricaoVO(new InscricaoVO());
		setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
		getInscricaoVO().getResultadoProcessoSeletivoVO().setDataRegistro(new Date());
		getInscricaoVO().getResultadoProcessoSeletivoVO().setResponsavel(getUsuarioLogadoClone());
		setListaSelectItemDisciplinaProcSeletivo(new ArrayList<SelectItem>(0));
		inicializarResponsavelUsuarioLogado();
		setResultadoDisciplinaProcSeletivoVO(new ResultadoDisciplinaProcSeletivoVO());
		getListaSelectItemItemDataProcSeletivo().clear();
		getListaSelectItemSalaProcSeletivo().clear();		
		setOpcao1(Boolean.FALSE);
		setOpcao2(Boolean.FALSE);
		setOpcao3(Boolean.FALSE);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
	}

	public void inicializarResponsavelUsuarioLogado() {
		try {
			getInscricaoVO().getResultadoProcessoSeletivoVO().setDataRegistro(new Date());
			getInscricaoVO().getResultadoProcessoSeletivoVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemProcSeletivo();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>ProcSeletivo</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>ProcSeletivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemProcSeletivo() {
		try {
			montarListaSelectItemProcSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>ProcSeletivo</code>.
	 */
	public void montarListaSelectItemProcSeletivo(String prm) throws Exception {
		List<ProcSeletivoVO> resultadoConsulta = null;
		Iterator<ProcSeletivoVO> i = null;
		try {
			resultadoConsulta = consultarProcSeletivoPorDescricao(prm);
			Ordenacao.ordenarListaDecrescente(resultadoConsulta, "dataFim");
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
				// if ((obj.getDataInicio().before(new Date()) && obj.getDataFim().after(new Date()) && obj.getDataFim().compareTo(new Date()) > 0)
				// || (obj.getDataInicioInternet().before(new Date()) && obj.getDataFimInternet().after(new Date()) &&
				// obj.getDataFimInternet().compareTo(new Date()) > 0)
				// || (obj.getCodigo().equals(inscricaoVO.getProcSeletivo().getCodigo()))) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				// }
			}
			setListaSelectItemProcSeletivo(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este atributo é uma lista (
	 * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<ProcSeletivoVO> consultarProcSeletivoPorDescricao(String descricaoPrm) throws Exception {
		return getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(descricaoPrm,  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ResultadoProcessoSeletivo</code> para alteração. O objeto desta
	 * classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ResultadoProcessoSeletivoVO obj = (ResultadoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("resultadoProcessoSeletivoItens");
			setInscricaoVO(new InscricaoVO());
			setInscricaoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(obj.getInscricao(), getUsuarioLogado()));
			if (getInscricaoVO().getCursoOpcao1().getCodigo().intValue() != 0) {
				setOpcao1(Boolean.TRUE);
			} else {
				setOpcao1(Boolean.FALSE);
			}
			if (getInscricaoVO().getCursoOpcao2().getCodigo().intValue() != 0) {
				setOpcao2(Boolean.TRUE);
			} else {
				setOpcao2(Boolean.FALSE);
			}
			if (getInscricaoVO().getCursoOpcao3().getCodigo().intValue() != 0) {
				setOpcao3(Boolean.TRUE);
			} else {
				setOpcao3(Boolean.FALSE);
			}											
			if (getInscricaoVO().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA")) {				
				if (!Uteis.isAtributoPreenchido(getInscricaoVO().getGabaritoVO()) || getInscricaoVO().getGabaritoVO().getControlarGabaritoPorDisciplina()) {
					setControleAbas("disciplinaProcSeletivo");
				} else {
					setControleAbas("notaGabaritoProcSeletivo");
				}
			} else {				
				setControleAbas("disciplinaProcSeletivo");
			}
			if(Uteis.isAtributoPreenchido(getInscricaoVO().getProvaProcessoSeletivoVO()) || !getCalculoMedia()) {
				setExibirQuanditadeAcertos(true);
			}
			getFacadeFactory().getProvaProcessoSeletivoFacade().realizarGeracaoGabarito(getInscricaoVO().getProvaProcessoSeletivoVO());		  
			setExibirResultadoPorProcessoSeletivo(false);
			setExibirResultadoPorProcessoSeletivoNotaDisciplina(false);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
		}
	}

	public void executarValidarResultadoPrimeiraSegundaETerceiraOpcao() throws Exception {
		if (getOpcao1() == true && getOpcao2() == false && getOpcao3() == false) {
			if (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_mediaInferiorAmediaMinimaParaAprovacao"));
			}
		}
		if (getOpcao1() == true && getOpcao2() == true && getOpcao3() == false) {
			if ((getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) || (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("AP"))) {
				throw new Exception(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_mediaInferiorAmediaMinimaParaAprovacao"));
			}
		}
		if (getOpcao1() == true && getOpcao2() == true && getOpcao3() == true) {
			if ((getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) || (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("AP")) || (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("AP"))) {
				throw new Exception(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_mediaInferiorAmediaMinimaParaAprovacao"));
			}
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ResultadoProcessoSeletivo</code>. Caso o objeto seja
	 * novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver
	 * alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			calcularMediaNotaLancadaManualmente();
			inicializarResponsavelUsuarioLogado();
			if (getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao")) {
				if (getInscricaoVO().getResultadoProcessoSeletivoVO().getSomatorioAcertos() > getInscricaoVO().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao() && (Uteis.isAtributoPreenchido(getInscricaoVO().getResultadoProcessoSeletivoVO().getNotaRedacao()) && getInscricaoVO().getResultadoProcessoSeletivoVO().getNotaRedacao() >= getInscricaoVO().getProcSeletivo().getNotaMinimaRedacao()) && !getInscricaoVO().getResultadoProcessoSeletivoVO().isAlgumaNotaAbaixoMinimo()) {
					if (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("RE") && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("RE") && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("RE")) {
						setMensagemDetalhada(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_mediaSuperiorAmediaMinimaParaAprovacao"));
						return;
					}
				}
				if (getInscricaoVO().getResultadoProcessoSeletivoVO().getSomatorioAcertos() < getInscricaoVO().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao() || !Uteis.isAtributoPreenchido(getInscricaoVO().getResultadoProcessoSeletivoVO().getNotaRedacao()) ||  getInscricaoVO().getResultadoProcessoSeletivoVO().getNotaRedacao()  < getInscricaoVO().getProcSeletivo().getNotaMinimaRedacao()) {
					executarValidarResultadoPrimeiraSegundaETerceiraOpcao();
				}
			}
			if (getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")) {
				if (getInscricaoVO().getResultadoProcessoSeletivoVO().getMediaNotasProcSeletivo() > getInscricaoVO().getProcSeletivo().getMediaMinimaAprovacao() && !getInscricaoVO().getResultadoProcessoSeletivoVO().isAlgumaNotaAbaixoMinimo()) {
					if (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("RE") && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("RE") && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("RE")) {
						setMensagemDetalhada(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_mediaSuperiorAmediaMinimaParaAprovacao"));
						return;
					}
				}
				if (getInscricaoVO().getResultadoProcessoSeletivoVO().getMediaNotasProcSeletivo() < getInscricaoVO().getProcSeletivo().getMediaMinimaAprovacao()) {
					executarValidarResultadoPrimeiraSegundaETerceiraOpcao();
				}
			}
			if (getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertos")) {
				if (getInscricaoVO().getResultadoProcessoSeletivoVO().getSomatorioAcertos() > getInscricaoVO().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao() && !getInscricaoVO().getResultadoProcessoSeletivoVO().isAlgumaNotaAbaixoMinimo()) {
					if (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("RE") && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("RE") && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao() != null && getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("RE")) {
						setMensagemDetalhada(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_mediaSuperiorAmediaMinimaParaAprovacao"));
						return;
					}
				}
				if (getInscricaoVO().getResultadoProcessoSeletivoVO().getSomatorioAcertos() < getInscricaoVO().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) {
					executarValidarResultadoPrimeiraSegundaETerceiraOpcao();
				}
			}			
			ResultadoProcessoSeletivoVO.validarDados(getInscricaoVO().getResultadoProcessoSeletivoVO());
			if(!getInscricaoVO().getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				setMensagemDetalhada(UteisJSF.internacionalizar("msg_ResultadoProcessoSeletivo_inscricaoDiferenteAtivo"));
				return;
			}
			
			if (getInscricaoVO().getResultadoProcessoSeletivoVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getResultadoProcessoSeletivoFacade().incluir(getInscricaoVO().getResultadoProcessoSeletivoVO(), getUsuarioLogado());
			} else {
				inicializarResponsavelUsuarioLogado();
				getFacadeFactory().getResultadoProcessoSeletivoFacade().alterar(getInscricaoVO().getResultadoProcessoSeletivoVO(), getUsuarioLogado(), true);
			}			
			setMensagemID("msg_dados_gravados");			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarResultadoPorProcessoSeletivo() {
		try {
			for (InscricaoVO inscricao : getListaConsultaInscricao()) {
				// ResultadoProcessoSeletivoVO.validarDados(inscricao.getResultadoProcessoSeletivoVO());
				boolean lancado = !inscricao.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("") || !inscricao.getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("") || !inscricao.getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("");
				if (inscricao.getNaoCompareceu()) {
					// Se marcado como nao compareceu entao a rotina abaixo altera a situacao da inscricao
					// e excluir qualquer resultado lançado, pois o SEI intepreta que uma inscricao sem
					// resultados é uma inscrição de um aluno que não compareceu.
					inscricao.setSituacaoInscricao(SituacaoInscricaoEnum.NAO_COMPARECEU);
					getFacadeFactory().getInscricaoFacade().alterarSituacaoInscricaoNaoCompareceu(inscricao, getUsuarioLogado());
				} else {
					if (lancado) {
						if (inscricao.getResultadoProcessoSeletivoVO().isNovoObj()) {
							getFacadeFactory().getResultadoProcessoSeletivoFacade().incluir(inscricao.getResultadoProcessoSeletivoVO(), getUsuarioLogado());
						} else {
							inicializarResponsavelUsuarioLogado();
							getFacadeFactory().getResultadoProcessoSeletivoFacade().alterar(inscricao.getResultadoProcessoSeletivoVO(), getUsuarioLogado(), true);
						}
					} else {
						//getFacadeFactory().getInscricaoFacade().atualizarSituacaoInscricao(inscricao, SituacaoInscricaoEnum.ATIVO, getUsuarioLogado());
					}
				}
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarResultadoPorDisciplina() {
		try {
			for (ResultadoProcessoSeletivoVO result : getListaResultadoProcessoSeletivo()) {
				boolean lancado = !result.getResultadoPrimeiraOpcao().equals("") || !result.getResultadoSegundaOpcao().equals("") || !result.getResultadoTerceiraOpcao().equals("");
				if (lancado) {
					if (result.isNovoObj()) {
						getFacadeFactory().getResultadoProcessoSeletivoFacade().incluir(result, getUsuarioLogado());
					} else {
						inicializarResponsavelUsuarioLogado();
						getFacadeFactory().getResultadoProcessoSeletivoFacade().alterar(result, getUsuarioLogado(), true);
					}
				}
			}
			getListaResultadoProcessoSeletivo().clear();
			setControleAbas("resultadoPorProcessoSeletivo");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String iniciarMatricula() {
		try {
			MatriculaVO matricula = new MatriculaVO();
			MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
			matricula.getInscricao().setCodigo(getInscricaoVO().getCodigo());
			getFacadeFactory().getMatriculaFacade().realizarMontagemDadosProcSeletivoMatricula(matricula, matriculaPeriodoVO, getUsuarioLogado());
			matricula.setGuiaAba("DadosBasicos");
			matricula.setFormaIngresso("PS");
			matricula.getInscricao().getProcSeletivo().setProcSeletivoDisciplinasProcSeletivoVOs(getFacadeFactory().getProcSeletivoDisciplinasProcSeletivoFacade().consultarDisciplinasProcSeletivoPorCodigoProcSeletivo(matricula.getInscricao().getCodigo(), false, getUsuarioLogado()));
			matriculaPeriodoVO.setNovoObj(true);
			context().getExternalContext().getSessionMap().put("resultadoProcessoSeletivoMatricula", matricula);
			context().getExternalContext().getSessionMap().put("resultadoProcessoSeletivoMatriculaPeriodo", matriculaPeriodoVO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAdministrativo/academico/renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ResultadoProcessoSeletivoCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<ResultadoProcessoSeletivoVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("candidato")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorNomeCandidatoComUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoInscricao")) {
				if (getControleConsulta().getValorConsulta().equals("") || !NumberUtils.isNumber(getControleConsulta().getValorConsulta())) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricaoComUnidadeEnsino(new Integer(valorInt),  getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ResultadoProcessoSeletivoVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			ProcSeletivoVO procSeletivoVO = getInscricaoVO().getProcSeletivo();
			getFacadeFactory().getResultadoProcessoSeletivoFacade().excluir(getInscricaoVO().getResultadoProcessoSeletivoVO(), getUsuarioLogado());
			for(InscricaoVO inscricaoVO: getListaConsultaInscricao()){
				if(inscricaoVO.getCodigo().equals(getInscricaoVO().getCodigo())){
					inscricaoVO.setResultadoProcessoSeletivo(0);
					inscricaoVO.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
					inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao("");
					inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao("");
					inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao("");
					inscricaoVO.setGabaritoVO(null);
					inscricaoVO.setProvaProcessoSeletivoVO(null);
					inscricaoVO.setChamada(0);
					inscricaoVO.setCandidatoConvocadoMatricula(false);
					inscricaoVO.setClassificacao(0);					
					break;
				}
			}
			setInscricaoVO(new InscricaoVO());
			getInscricaoVO().setProcSeletivo(procSeletivoVO);
			getListaConsulta().clear();
			setControleAbas("resultadoPorProcessoSeletivo");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if(getExibirResultadoPorProcessoSeletivoNotaDisciplina()){			
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoDisciplinaForm.xhtml");
		}
		if(getExibirResultadoPorProcessoSeletivo()){
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>ResultadoDisciplinaProcSeletivo</code> para o objeto
	 * <code>resultadoProcessoSeletivoVO</code> da classe <code>ResultadoProcessoSeletivo</code>
	 */
	public void adicionarResultadoDisciplinaProcSeletivo() throws Exception {
		try {
			if (!getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
				resultadoDisciplinaProcSeletivoVO.setResultadoProcessoSeletivo(getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo());
			}
			if (getResultadoDisciplinaProcSeletivoVO().getDisciplinaProcSeletivo().getCodigo().intValue() != 0) {
				Integer campoConsulta = getResultadoDisciplinaProcSeletivoVO().getDisciplinaProcSeletivo().getCodigo();
				DisciplinasProcSeletivoVO disciplinasProcSeletivo = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(campoConsulta, getUsuarioLogado());
				getResultadoDisciplinaProcSeletivoVO().setDisciplinaProcSeletivo(disciplinasProcSeletivo);
			}
			getInscricaoVO().getResultadoProcessoSeletivoVO().adicionarObjResultadoDisciplinaProcSeletivoVOs(getResultadoDisciplinaProcSeletivoVO(), getInscricaoVO());
			this.setResultadoDisciplinaProcSeletivoVO(new ResultadoDisciplinaProcSeletivoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ResultadoDisciplinaProcSeletivo</code> para edição pelo usuário.
	 */
	public void editarResultadoDisciplinaProcSeletivo() throws Exception {
		ResultadoDisciplinaProcSeletivoVO obj = (ResultadoDisciplinaProcSeletivoVO) context().getExternalContext().getRequestMap().get("resultadoDisciplinaProcSeletivoItens");
		setResultadoDisciplinaProcSeletivoVO(obj);
		setMensagemID("msg_dados_adicionados");
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ResultadoDisciplinaProcSeletivo</code> do objeto
	 * <code>resultadoProcessoSeletivoVO</code> da classe <code>ResultadoProcessoSeletivo</code>
	 */
	public void removerResultadoDisciplinaProcSeletivo() {
		try {
			ResultadoDisciplinaProcSeletivoVO obj = (ResultadoDisciplinaProcSeletivoVO) context().getExternalContext().getRequestMap().get("resultadoDisciplinaProcSeletivoItens");
			getInscricaoVO().getResultadoProcessoSeletivoVO().excluirObjResultadoDisciplinaProcSeletivoVOs(obj.getDisciplinaProcSeletivo().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
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

	public void inicializarDadosSubordinadoProcessoSeletivo() {
		getListaConsultaInscricao().clear();
		setPossuiRedacao(false);
		getListaResultadoProcessoSeletivo().clear();
		setDataProva(null);
		getInscricaoVO().getCursoOpcao1().setCodigo(null); 
		setSala(null);
		montarListaSelectItemCursoOpcao();
		montarListaSelectItemSalaProcSeletivo();
		montarListaSelectItemDataProcSeletivo();
	}

	public void montarListaSelectItemDataProcSeletivo() {
		try {
			getListaSelectItemItemDataProcSeletivo().clear();
			List<ItemProcSeletivoDataProvaVO> resultadoConsulta = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getInscricaoVO().getProcSeletivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemItemDataProcSeletivo().add(new SelectItem(0, ""));
			for (ItemProcSeletivoDataProvaVO obj : resultadoConsulta) {
				getListaSelectItemItemDataProcSeletivo().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
			}
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;

		}
	}

	public void montarListaSelectItemSalaProcSeletivo() {
		try {
			getListaConsultaInscricao().clear();
			getListaResultadoProcessoSeletivo().clear();
			montarListaSelectItemSalaProcSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;

		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DisciplinaProcSeletivo</code>.
	 */
	public void montarListaSelectItemSalaProcSeletivo(String prm) throws Exception {
		try {
			getListaSelectItemSalaProcSeletivo().clear();
			List<SalaLocalAulaVO> resultadoConsulta = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivoEDataAula(getInscricaoVO().getProcSeletivo().getCodigo(), getDataProva());
			if (!resultadoConsulta.isEmpty()) {
				Integer value = 0;
				String label = "Todas";
				getListaSelectItemSalaProcSeletivo().add(new SelectItem(value, label));
				for (SalaLocalAulaVO sala : resultadoConsulta) {
					getListaSelectItemSalaProcSeletivo().add(new SelectItem(sala.getCodigo(), sala.getLocalAula().getLocal() + " - " + sala.getSala()));
				}
			} else {
				getListaSelectItemSalaProcSeletivo().clear();
				Integer value = 0;
				String label = "Nenhum";
				getListaSelectItemSalaProcSeletivo().add(new SelectItem(value, label));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
		


	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DisciplinaProcSeletivo</code>.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemDisciplinaProcSeletivo(String prm) throws Exception {
		List<DisciplinasGrupoDisciplinaProcSeletivoVO> resultadoConsulta = getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().consultarPorInscricaoCandidato(getInscricaoVO().getCodigo(), false, getUsuarioLogado());
		setListaSelectItemDisciplinaProcSeletivo(new ArrayList<SelectItem>(0));
		for (DisciplinasGrupoDisciplinaProcSeletivoVO obj : resultadoConsulta) {
			getListaSelectItemDisciplinaProcSeletivo().add(new SelectItem(obj.getDisciplinasProcSeletivo().getCodigo(), obj.getDisciplinasProcSeletivo().getNome()));
		}
		Collections.sort(getListaSelectItemDisciplinaProcSeletivo(), new SelectItemOrdemValor());
	}

	public void montarListaSelectItemDisciplinaProcSeletivoAutomatico() throws Exception {
		List<DisciplinasGrupoDisciplinaProcSeletivoVO> resultadoConsulta = getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().consultarPorInscricaoCandidato(getInscricaoVO().getCodigo(), false, getUsuarioLogado());
		setListaSelectItemDisciplinaProcSeletivo(new ArrayList<SelectItem>(0));
		for (DisciplinasGrupoDisciplinaProcSeletivoVO d : resultadoConsulta) {
			if (!Uteis.isAtributoPreenchido(getInscricaoVO().getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO())) {
				getInscricaoVO().getResultadoProcessoSeletivoVO().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(d.getGrupoDisciplinaProcSeletivo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getListaSelectItemDisciplinaProcSeletivo().add(new SelectItem(d.getDisciplinasProcSeletivo().getCodigo(), d.getDisciplinasProcSeletivo().getNome()));
			getResultadoDisciplinaProcSeletivoVO().setDisciplinaProcSeletivo(d.getDisciplinasProcSeletivo());
			getResultadoDisciplinaProcSeletivoVO().setVariavelNota(d.getVariavelNota());
			getResultadoDisciplinaProcSeletivoVO().setNotaMinimaReprovadoImediato(d.getNotaMinimaReprovadoImediato());
			getResultadoDisciplinaProcSeletivoVO().setOrdemCriterioDesempate(d.getOrdemCriterioDesempate());
			preencherResultadoDeAcordoComGrupoDisciplinaConfigurado(getInscricaoVO(), getResultadoDisciplinaProcSeletivoVO());
			this.adicionarResultadoDisciplinaProcSeletivo();
		}
	}

	public void preencherResultadoDeAcordoComGrupoDisciplinaConfigurado(InscricaoVO insc, ResultadoDisciplinaProcSeletivoVO resultado) {
		GrupoDisciplinaProcSeletivoVO grupo = null;
		for (ProcSeletivoUnidadeEnsinoVO pro : insc.getProcSeletivo().getProcSeletivoUnidadeEnsinoVOs()) {
			if (grupo == null) {
				for (ProcSeletivoCursoVO curso : pro.getProcSeletivoCursoVOs()) {
					if (curso.getUnidadeEnsinoCurso().getCodigo().equals(insc.getCursoOpcao1().getCodigo())) {
						grupo = curso.getGrupoDisciplinaProcSeletivo();
						break;
					}
				}
			}
		}
		if (grupo != null) {
			insc.getResultadoProcessoSeletivoVO().setGrupoDisciplinaProcSeletivoVO(grupo);
			for (DisciplinasGrupoDisciplinaProcSeletivoVO discGrupo : grupo.getDisciplinasGrupoDisciplinaProcSeletivoVOs()) {
				if (discGrupo.getDisciplinasProcSeletivo().getCodigo().equals(resultado.getDisciplinaProcSeletivo().getCodigo())) {
					resultado.setVariavelNota(discGrupo.getVariavelNota());
					resultado.setNotaMinimaReprovadoImediato(discGrupo.getNotaMinimaReprovadoImediato());
					resultado.setOrdemCriterioDesempate(discGrupo.getOrdemCriterioDesempate());
				}
			}
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>DisciplinaProcSeletivo</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>DisciplinasProcSeletivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDisciplinaProcSeletivo() throws Exception {
		try {
			montarListaSelectItemDisciplinaProcSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
			throw e;
		}
	}

	public void habilitarResultadoOpcaoCurso() {
		getInscricaoVO().getProcSeletivo().getNrOpcoesCurso();
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>resultadoTerceiraOpcao</code>
	 */
	public List getListaSelectItemResultadoTerceiraOpcaoResultadoProcessoSeletivo() throws Exception {
		List objs = new ArrayList(0);
		Hashtable opcaoResultadoProcessoSeletivos = (Hashtable) Dominios.getOpcaoResultadoProcessoSeletivo();
		Enumeration keys = opcaoResultadoProcessoSeletivos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) opcaoResultadoProcessoSeletivos.get(value);
			objs.add(new SelectItem(value, label));
		}
		Ordenacao.ordenarLista(objs, "label");
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>resultadoSegundaOpcao</code>
	 */
	public List getListaSelectItemResultadoSegundaOpcaoResultadoProcessoSeletivo() throws Exception {
		List objs = new ArrayList(0);
		Hashtable opcaoResultadoProcessoSeletivos = (Hashtable) Dominios.getOpcaoResultadoProcessoSeletivo();
		Enumeration keys = opcaoResultadoProcessoSeletivos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) opcaoResultadoProcessoSeletivos.get(value);
			objs.add(new SelectItem(value, label));
		}
		Ordenacao.ordenarLista(objs, "label");
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>resultadoPrimeiraOpcao</code>
	 */
	public List getListaSelectItemResultadoPrimeiraOpcaoResultadoProcessoSeletivo() throws Exception {
		List objs = new ArrayList(0);
		Hashtable opcaoResultadoProcessoSeletivos = (Hashtable) Dominios.getOpcaoResultadoProcessoSeletivo();
		Enumeration keys = opcaoResultadoProcessoSeletivos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) opcaoResultadoProcessoSeletivos.get(value);
			objs.add(new SelectItem(value, label));
		}
		Ordenacao.ordenarLista(objs, "label");
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = getInscricaoVO().getResultadoProcessoSeletivoVO().getResponsavel().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getInscricaoVO().getResultadoProcessoSeletivoVO().getResponsavel().setNome(pessoa.getNome());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Inscricao</code> por meio de sua respectiva chave primária. Esta rotina é
	 * utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarInscricaoPorChavePrimaria() {
		try {
			setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(getInscricaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getInscricaoVO().setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(getInscricaoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(getInscricaoVO());
			inicializarDadosProcessoSeletivoInscricao();
			iniciarlizarDadosGabaritoProcessoSeletivoInscricao();
			if (getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
				montarListaSelectItemDisciplinaProcSeletivoAutomatico();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setOpcao1(Boolean.FALSE);
			setOpcao2(Boolean.FALSE);
			setOpcao3(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void consultarNomeOpcoesCursoCandidato() throws Exception {
		getInscricaoVO().getCursoOpcao1().setCurso(getFacadeFactory().getCursoFacade().consultarPorCodigoUnidadeEnsinoCurso(getInscricaoVO().getCursoOpcao1().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		if (getInscricaoVO().getCursoOpcao2().getCodigo() != 0) {
			getInscricaoVO().getCursoOpcao2().setCurso(getFacadeFactory().getCursoFacade().consultarPorCodigoUnidadeEnsinoCurso(getInscricaoVO().getCursoOpcao2().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		}
		if (getInscricaoVO().getCursoOpcao3().getCodigo() != 0) {
			getInscricaoVO().getCursoOpcao3().setCurso(getFacadeFactory().getCursoFacade().consultarPorCodigoUnidadeEnsinoCurso(getInscricaoVO().getCursoOpcao3().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		}
	}

	public void inicializarDadosProcessoSeletivoInscricao() throws Exception {
		if (getInscricaoVO().getCursoOpcao1().getCodigo().intValue() != 0) {
			setOpcao1(Boolean.TRUE);
		} else {
			setOpcao1(Boolean.FALSE);
		}
		if (getInscricaoVO().getCursoOpcao2().getCodigo().intValue() != 0) {
			setOpcao2(Boolean.TRUE);
		} else {
			setOpcao2(Boolean.FALSE);
		}
		if (getInscricaoVO().getCursoOpcao3().getCodigo().intValue() != 0) {
			setOpcao3(Boolean.TRUE);
		} else {
			setOpcao3(Boolean.FALSE);
		}
		if (getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
			montarListaSelectItemDisciplinaProcSeletivoAutomatico();
		}
		consultarNomeOpcoesCursoCandidato();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("candidato", "Candidato"));
		itens.add(new SelectItem("codigoInscricao", "Inscrição"));
		
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		// setListaConsulta(new ArrayList(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
	}

	public void consultarInscricao() {
		try {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			List<InscricaoVO> objs = new ArrayList<InscricaoVO>(0);
			if (getCampoConsultaInscricao().equals("codigo")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o CÓDIGO desejado.");
				}
				int valorInt = Integer.parseInt(getValorConsultaInscricao());
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("nomePessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
			getInscricaoVO().setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(inscricao);

			inicializarDadosProcessoSeletivoInscricao();
			iniciarlizarDadosGabaritoProcessoSeletivoInscricao();
			if (getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
				montarListaSelectItemDisciplinaProcSeletivoAutomatico();
			}
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
		} catch (Exception e) {
			setInscricaoVO(new InscricaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarInscricaoPorProcessoSeletivo() {
		try {
			InscricaoVO inscricao = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
			setInscricaoVO(new InscricaoVO());
			setExibirQuanditadeAcertos(false);
			setInscricaoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(inscricao, getUsuarioLogado()));
			if (getInscricaoVO().getCursoOpcao1().getCodigo().intValue() != 0) {
				setOpcao1(Boolean.TRUE);
			} else {
				setOpcao1(Boolean.FALSE);
			}
			if (getInscricaoVO().getCursoOpcao2().getCodigo().intValue() != 0) {
				setOpcao2(Boolean.TRUE);
			} else {
				setOpcao2(Boolean.FALSE);
			}
			if (getInscricaoVO().getCursoOpcao3().getCodigo().intValue() != 0) {
				setOpcao3(Boolean.TRUE);
			} else {
				setOpcao3(Boolean.FALSE);
			}											
			if (getInscricaoVO().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA")) {				
				if (!Uteis.isAtributoPreenchido(getInscricaoVO().getGabaritoVO()) || getInscricaoVO().getGabaritoVO().getControlarGabaritoPorDisciplina()) {
					setControleAbas("disciplinaProcSeletivo");
				} else {
					setControleAbas("notaGabaritoProcSeletivo");
				}
			} else {				
				setControleAbas("disciplinaProcSeletivo");
			}			
			if(Uteis.isAtributoPreenchido(getInscricaoVO().getProvaProcessoSeletivoVO()) || !getCalculoMedia()) {
				setExibirQuanditadeAcertos(true);
			}
			getFacadeFactory().getProvaProcessoSeletivoFacade().realizarGeracaoGabarito(getInscricaoVO().getProvaProcessoSeletivoVO());		  
			setExibirResultadoPorProcessoSeletivo(true);
			setExibirResultadoPorProcessoSeletivoNotaDisciplina(false);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setInscricaoVO(new InscricaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void iniciarlizarDadosGabaritoProcessoSeletivoInscricao() throws Exception {
		if (getInscricaoVO().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA")) {
			getFacadeFactory().getResultadoProcessoSeletivoFacade().inicializarDadosGabaritoResposta(getInscricaoVO().getResultadoProcessoSeletivoVO(), getUsuarioLogado());
			getInscricaoVO().setGabaritoVO(getInscricaoVO().getResultadoProcessoSeletivoVO().getInscricao().getGabaritoVO());
		}
	}

	public void calcularMediaLancamentoNotaRedacaoListaInscricao() {
		try {
			InscricaoVO inscricao = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
			if (inscricao.getResultadoProcessoSeletivoVO().getCodigo() > 0) {
				if(!inscricao.getNivelMontarDados().equals(NivelMontarDados.TODOS)){
					if (inscricao.getResultadoProcessoSeletivoVO().getNotaRedacao() == null) {
						inscricao.getResultadoProcessoSeletivoVO().setNotaRedacao(0.0);
					}
					Double nota = inscricao.getResultadoProcessoSeletivoVO().getNotaRedacao();
					inscricao.setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorChavePrimaria(inscricao.getResultadoProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					inscricao.setNivelMontarDados(NivelMontarDados.TODOS);
					inscricao.getResultadoProcessoSeletivoVO().setNotaRedacao(nota);
				}
				inscricao.getResultadoProcessoSeletivoVO().realizarCalculoAprovacaoCandidatoPorProva();
			}
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarResultadoProcessoSeletivo(InscricaoVO inscricao) {
		try {
			// caso já exista o ResultadoProcessoSeletivo será add a variavel consultarResultadoProcessoSeletivoVO
			// ResultadoProcessoSeletivoVO obj = new ResultadoProcessoSeletivoVO();
			// obj = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), true,
			// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			// setResultadoProcessoSeletivoVO(inscricao.getResultadoProcessoSeletivoVO());
			// obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			// ainda não possue ResultadoProcessoSeletivo gravado
		}
	}

	public List<SelectItem> getTipoConsultaComboInscricao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Nome do Candidato"));
		itens.add(new SelectItem("codigo", "Número da Inscrição"));
		return itens;
	}

	public void consultarCanditado() {
		try {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			if(getInscricaoVO().getProcSeletivo().getCodigo().equals(0)){
				throw new Exception("O campo PROCESSO SELETIVO deve ser informado.");
			}
			getInscricaoVO().setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getInscricaoVO().getProcSeletivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			
			setListaConsultaInscricao(getFacadeFactory().getInscricaoFacade().consultarCanditadoPorCodigoProcSeletivo(getInscricaoVO().getProcSeletivo(), getDataProva(),getInscricaoVO().getCursoOpcao1().getCodigo(), getSala(), true, getUsuarioLogado()));
//			if(getSala() > 0 && !getListaConsultaInscricao().isEmpty()){
//				getSala().setSala(getListaConsultaInscricao().get(0).getSala().getSala());
//			}
			realizarVerificacaoInscricaoPossuiProvaComRedacao();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getApresentarQuantidadeAcertosRedacao() {
		return getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao")  || getInscricaoVO().getPossuiRedacao() ;
	}

	public void adicionarResultadoProcessoSeletivo() {
		try {
			inicializarResponsavelUsuarioLogado();
			InscricaoVO insc = (InscricaoVO) getInscricaoVO().clone();
			getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(insc);
			getInscricaoVO().getResultadoProcessoSeletivoVO().adicionarObjResultadoProcessoSeletivoVOs(getInscricaoVO().getResultadoProcessoSeletivoVO(), getListaResultadoProcessoSeletivo());
			// setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			gravarResultadoPorDisciplina();
			for (Iterator<InscricaoVO> iterator = getListaConsultaInscricao().iterator(); iterator.hasNext();) {
				InscricaoVO inscricaoVO = iterator.next();
				if (inscricaoVO.getCodigo().equals(insc.getCodigo())) {
					inscricaoVO = insc;
					break;
				}
			}			
			setOpcao1(Boolean.FALSE);
			setOpcao2(Boolean.FALSE);
			setOpcao3(Boolean.FALSE);
			setInscricaoVO(new InscricaoVO());
			getInscricaoVO().setProcSeletivo(insc.getProcSeletivo());
//			inicializarDadosSubordinadoProcessoSeletivo();
//			consultarCanditado();
			setControleAbas("resultadoPorProcessoSeletivo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerResultadoProcessoSeletivo() throws Exception {
		ResultadoProcessoSeletivoVO obj = (ResultadoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("resultadoProcessoSeletivo");
		getInscricaoVO().getResultadoProcessoSeletivoVO().excluirObjResultadoProcessoSeletivoVOs(obj.getInscricao().getCodigo(), getListaResultadoProcessoSeletivo());
		setMensagemID("msg_dados_excluidos");
	}

	public void navegarAbaDisciplinaProcSeletivo() {
	}

	public List<SelectItem> getListaSelectItemDisciplinaProcSeletivo() {
		if(listaSelectItemDisciplinaProcSeletivo == null){
			listaSelectItemDisciplinaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemDisciplinaProcSeletivo);
	}

	public void setListaSelectItemDisciplinaProcSeletivo(List<SelectItem> listaSelectItemDisciplinaProcSeletivo) {
		this.listaSelectItemDisciplinaProcSeletivo = listaSelectItemDisciplinaProcSeletivo;
	}

	public ResultadoDisciplinaProcSeletivoVO getResultadoDisciplinaProcSeletivoVO() {
		if (resultadoDisciplinaProcSeletivoVO == null) {
			resultadoDisciplinaProcSeletivoVO = new ResultadoDisciplinaProcSeletivoVO();
		}
		return resultadoDisciplinaProcSeletivoVO;
	}

	public void setResultadoDisciplinaProcSeletivoVO(ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO) {
		this.resultadoDisciplinaProcSeletivoVO = resultadoDisciplinaProcSeletivoVO;
	}

	public Boolean getOpcao1() {
		return opcao1;
	}

	public void setOpcao1(Boolean opcao1) {
		this.opcao1 = opcao1;
	}

	public Boolean getOpcao2() {
		return opcao2;
	}

	public void setOpcao2(Boolean opcao2) {
		this.opcao2 = opcao2;
	}

	public Boolean getOpcao3() {
		return opcao3;
	}

	public void setOpcao3(Boolean opcao3) {
		this.opcao3 = opcao3;
	}
	
	public Boolean getIsInscricaoPermitida() {
		return Uteis.isAtributoPreenchido(getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo()) 
				&& (getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP") 
						|| getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("A2")  
						|| getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("A3"));
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

	public void setCampoConsultaInscricao(String campoConsultaInscricao) {
		this.campoConsultaInscricao = campoConsultaInscricao;
	}

	public String getCampoConsultaInscricao() {
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

	public Boolean getCalculoMedia() {
		return getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina");
	}

	public Boolean getDesbloquearSomatorioAcerto() {
		return  (!getInscricaoVO().getCodigo().equals(0)
				&& !getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina") 
				&& Uteis.isAtributoPreenchido(getInscricaoVO().getItemProcessoSeletivoDataProva().getCodigo()) 
				&& !getApresentarAbaGabarito()
				&& !getApresentarAbaNotasPorDisciplina()
				&& !getApresentarAbaProvaProcessoSeletivo());
	}
	
	public Boolean getDesbloquearMediaNota() {
		return  (!getInscricaoVO().getCodigo().equals(0)
				&& getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina") 
				&& Uteis.isAtributoPreenchido(getInscricaoVO().getItemProcessoSeletivoDataProva().getCodigo()) 
				&& !getApresentarAbaGabarito()
				&& !getApresentarAbaNotasPorDisciplina()
				&& !getApresentarAbaProvaProcessoSeletivo());
	}

	public String getCssSomatorioAcerto() {
		if (getDesbloquearSomatorioAcerto()) {
			return "camposObrigatorios";
		}
		return "camposSomenteLeitura";
	}
	
	public String getCssMediaNota() {
		if (getDesbloquearMediaNota()) {
			return "camposObrigatorios";
		}
		return "camposSomenteLeitura";
	}

	public List<SelectItem> getListaSelectItemProcSeletivo() {
		if (listaSelectItemProcSeletivo == null) {
			listaSelectItemProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemProcSeletivo);
	}

	public void setListaSelectItemProcSeletivo(List<SelectItem> listaSelectItemProcSeletivo) {
		this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
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

	public String getControleAbas() {
		if (controleAbas == null) {
			controleAbas = "";
		}
		return controleAbas;
	}

	public void setControleAbas(String controleAbas) {
		this.controleAbas = controleAbas;
	}

	public List<ResultadoProcessoSeletivoVO> getListaResultadoProcessoSeletivo() {
		if (listaResultadoProcessoSeletivo == null) {
			listaResultadoProcessoSeletivo = new ArrayList<ResultadoProcessoSeletivoVO>(0);
		}
		return listaResultadoProcessoSeletivo;
	}

	public void setListaResultadoProcessoSeletivo(List<ResultadoProcessoSeletivoVO> listaResultadoProcessoSeletivo) {
		this.listaResultadoProcessoSeletivo = listaResultadoProcessoSeletivo;
	}

	public List<SelectItem> getListaSelectItemSalaProcSeletivo() {
		if (listaSelectItemSalaProcSeletivo == null) {
			listaSelectItemSalaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSalaProcSeletivo;
	}

	public void setListaSelectItemSalaProcSeletivo(List<SelectItem> list) {
		this.listaSelectItemSalaProcSeletivo = list;
	}

	public boolean getApresentarComboBoxSala() {
		return !getInscricaoVO().getProcSeletivo().getCodigo().equals(0);
	}

	public List<SelectItem> getListaSelectItemItemDataProcSeletivo() {
		if (listaSelectItemItemDataProcSeletivo == null) {
			listaSelectItemItemDataProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemItemDataProcSeletivo;
	}

	public void setListaSelectItemItemDataProcSeletivo(List<SelectItem> listaSelectItemItemDataProcSeletivo) {
		this.listaSelectItemItemDataProcSeletivo = listaSelectItemItemDataProcSeletivo;
	}

	public Integer getDataProva() {
		if (dataProva == null) {
			dataProva = 0;
		}
		return dataProva;
	}

	public void setDataProva(Integer dataProva) {
		this.dataProva = dataProva;
	}

	public Boolean getApresentarAbaGabarito() {
		return !getInscricaoVO().getCodigo().equals(0) && getInscricaoVO().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA") && Uteis.isAtributoPreenchido(getInscricaoVO().getGabaritoVO());
	}

	public Boolean getApresentarAbaNotasPorDisciplina() {
		return !getInscricaoVO().getCodigo().equals(0) && !getInscricaoVO().getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVOs().isEmpty();
	}

	public Boolean getExibirResultadoPorProcessoSeletivo() {
		if (exibirResultadoPorProcessoSeletivo == null) {
			exibirResultadoPorProcessoSeletivo = Boolean.TRUE;
		}
		return exibirResultadoPorProcessoSeletivo;
	}

	public void setExibirResultadoPorProcessoSeletivo(Boolean exibirResultadoPorProcessoSeletivo) {
		this.exibirResultadoPorProcessoSeletivo = exibirResultadoPorProcessoSeletivo;
	}
	
	

	public void marcarInscricaoComoNaoCompareceu() {
		try {
			getInscricaoNaoCompareceu().setSituacaoInscricao(SituacaoInscricaoEnum.NAO_COMPARECEU);
			getFacadeFactory().getInscricaoFacade().alterarSituacaoInscricaoNaoCompareceu(getInscricaoNaoCompareceu(), getUsuarioLogado());
			getInscricaoNaoCompareceu().setResultadoProcessoSeletivoVO(null);
			   getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao("");
			   getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao("");
			   getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao("");
			   getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setInscricao(getInscricaoNaoCompareceu());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {			
			getInscricaoNaoCompareceu().setSituacaoInscricao(SituacaoInscricaoEnum.ATIVO);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			limparInscricaoNaoCompareceu();
		}
	}
	
	public void limparInscricaoNaoCompareceu(){
		setInscricaoNaoCompareceu(new InscricaoVO());
	}

	public void limparInscricaoRedacao(){
		setInscricaoRedacao(new InscricaoVO());
		
	}
	
	public void cancelarInscricaoComoNaoCompareceu() {
		try {
			getInscricaoNaoCompareceu().setSituacaoInscricao(SituacaoInscricaoEnum.ATIVO);
			getFacadeFactory().getInscricaoFacade().alterarSituacaoInscricaoNaoCompareceu(getInscricaoNaoCompareceu(), getUsuarioLogado());
			getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao("");
			getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao("");
			getInscricaoNaoCompareceu().getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao("");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {		
			getInscricaoNaoCompareceu().setSituacaoInscricao(SituacaoInscricaoEnum.NAO_COMPARECEU);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			limparInscricaoNaoCompareceu();
		}
	}

	private void executarMontagemDadosGabaritoOuProvaProcessoSeletivoInscricao() throws Exception {
		getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosGabaritoOuProvaProcessoSeletivoInscricao(getInscricaoVO(), getUsuarioLogado());
	}

	public Boolean getApresentarAbaProvaProcessoSeletivo() {
		return !getInscricaoVO().getCodigo().equals(0) && getInscricaoVO().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("PR") && Uteis.isAtributoPreenchido(getInscricaoVO().getProvaProcessoSeletivoVO());
	}

	public void calcularMediaProvaLancadoManualmente() {
		try {
			getFacadeFactory().getResultadoProcessoSeletivoFacade().executarCalculoAprovacaoCandidatoTipoProva(getInscricaoVO().getResultadoProcessoSeletivoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getSala() {
		if (sala == null) {
			sala = 0;
		}
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
	}

	private void executarVerificacaoHabilitarCampoNota() throws Exception {
		getFacadeFactory().getResultadoProcessoSeletivoFacade().executarVerificacaoHabilitarCampoNota(getInscricaoVO());
	}
	
	/**
	 * Responsável por executar o cálculo da média, seja ela do tipo Gabarito, seja do tipo Prova, ao ser lançado a nota individualmente.
	 * 
	 * @author Wellington - 16 de mar de 2016
	 */
	public void calcularMediaNotaLancadaManualmente() {
		try {
			getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarCalculoMediaNotaLancadaManualmente(getInscricaoVO().getResultadoProcessoSeletivoVO(), getUsuarioLogado());						
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void calcularMediaNotaDisciplinaLancadaManualmente() {
		try {			
			getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarCalculoMediaNotaLancadaManualmente(getResultadoProcessoSeletivoVO(), getUsuarioLogado());						
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * @return the inscricaoNaoCompareceu
	 */
	public InscricaoVO getInscricaoNaoCompareceu() {
		if (inscricaoNaoCompareceu == null) {
			inscricaoNaoCompareceu = new InscricaoVO();
		}
		return inscricaoNaoCompareceu;
	}

	/**
	 * @param inscricaoNaoCompareceu the inscricaoNaoCompareceu to set
	 */
	public void setInscricaoNaoCompareceu(InscricaoVO inscricaoNaoCompareceu) {
		this.inscricaoNaoCompareceu = inscricaoNaoCompareceu;
	}
	
	public Boolean getApresentarNotaMininaRedacao() {
		return getInscricaoVO().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao");
	}
	
	public List<SelectItem> tipoRegimeAprovacaoCombo;
	public List<SelectItem> getTipoRegimeAprovacaoCombo() {
		if(tipoRegimeAprovacaoCombo == null){
			tipoRegimeAprovacaoCombo = new ArrayList<SelectItem>(0);			
			tipoRegimeAprovacaoCombo.add(new SelectItem("notaPorDisciplina", "Nota por disciplina"));
			tipoRegimeAprovacaoCombo.add(new SelectItem("quantidadeAcertos", "Quantidade de acertos"));
			tipoRegimeAprovacaoCombo.add(new SelectItem("quantidadeAcertosRedacao", "Quantidade de acertos e Redação"));
		}
		return tipoRegimeAprovacaoCombo;
	}
	
	public String voltarInscricao(){
		try{
			ProcSeletivoVO procSeletivoVO = getInscricaoVO().getProcSeletivo();			
			if (!getExibirResultadoPorProcessoSeletivoNotaDisciplina()) {
				for (InscricaoVO inscricaoVO : getListaConsultaInscricao()) {
					if (inscricaoVO.getCodigo().equals(getInscricaoVO().getCodigo())) {
						if (inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo() > 0) {
							ResultadoProcessoSeletivoVO resultado = getFacadeFactory()
									.getResultadoProcessoSeletivoFacade()
									.consultarPorCodigoInscricao_ResultadoUnico(inscricaoVO.getCodigo(), false,
											Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
							resultado.setInscricao(inscricaoVO);
							inscricaoVO.setResultadoProcessoSeletivoVO(resultado);
						} else {
							inscricaoVO.setGabaritoVO(null);
							inscricaoVO.setProvaProcessoSeletivoVO(null);
							inscricaoVO.setChamada(0);
							inscricaoVO.setCandidatoConvocadoMatricula(false);
							inscricaoVO.setClassificacao(0);
							inscricaoVO.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
							inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoPrimeiraOpcao("");
							inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoSegundaOpcao("");
							inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoTerceiraOpcao("");
						}
					}
				}
			}
			setInscricaoVO(new InscricaoVO());
			getInscricaoVO().setProcSeletivo(procSeletivoVO);			
			setControleAbas("resultadoPorProcessoSeletivo");
			limparMensagem();
			if(getExibirResultadoPorProcessoSeletivoNotaDisciplina()){				
				return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoDisciplinaForm.xhtml");
			}
			if(getExibirResultadoPorProcessoSeletivo()){
				return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
		}
	}

	public DisciplinasProcSeletivoVO getDisciplinasProcSeletivoVO() {
		if(disciplinasProcSeletivoVO == null){
			disciplinasProcSeletivoVO = new DisciplinasProcSeletivoVO();
		}
		return disciplinasProcSeletivoVO;
	}

	public void setDisciplinasProcSeletivoVO(DisciplinasProcSeletivoVO disciplinasProcSeletivoVO) {
		this.disciplinasProcSeletivoVO = disciplinasProcSeletivoVO;
	}

	public String montarListaSelectItemLancamentoNotaPorDisciplina(){
		try{
			setExibirResultadoPorProcessoSeletivo(false);
			setExibirResultadoPorProcessoSeletivoNotaDisciplina(true);
			setDisciplinasProcSeletivoVO(null);
			getListaResultadoProcessoSeletivo().clear();
			limparMensagem();			
			List<DisciplinasProcSeletivoVO> disciplinasProcSeletivoVOs = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorProcSeletivo(getInscricaoVO().getProcSeletivo().getCodigo(), false, getUsuarioLogado());
			getListaSelectItemDisciplinaProcSeletivo().clear();
			setListaSelectItemDisciplinaProcSeletivo(UtilSelectItem.getListaSelectItem(disciplinasProcSeletivoVOs, "codigo", "nome"));
			if(getListaSelectItemDisciplinaProcSeletivo().size() == 1){
				getListaSelectItemDisciplinaProcSeletivo().clear();
			}
			realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina();
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoDisciplinaForm.xhtml");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoCons.xhtml");
		}
	}
	
	public void realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina(){
		try{
			limparMensagem();
			if(!Uteis.isAtributoPreenchido(getDisciplinasProcSeletivoVO().getCodigo())){
				getListaResultadoProcessoSeletivo().clear();
				setListaResultadoProcessoSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina(getListaConsultaInscricao(), getDisciplinasProcSeletivoVO(), getUsuarioLogado()));
			}else{
				setDisciplinasProcSeletivoVO(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(getDisciplinasProcSeletivoVO().getCodigo(), getUsuarioLogado()));
				setListaResultadoProcessoSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina(getListaConsultaInscricao(), getDisciplinasProcSeletivoVO(), getUsuarioLogado()));
			}
		}catch(ConsistirException e){
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void gravarLancamentoNotaPorDisciplina(){
		try{
			limparMensagem();
			getFacadeFactory().getResultadoProcessoSeletivoFacade().gravarLancamentoNotaPorDisciplina(getListaResultadoProcessoSeletivo(), getUsuarioLogado());		
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void gravarLancamentoNotaPorDisciplinaIndividual(){
		try{
			limparMensagem();
			getFacadeFactory().getResultadoProcessoSeletivoFacade().persistir(getResultadoProcessoSeletivoVO(), getUsuarioLogado(), true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarCalculoResultadoLancamentoNotaPorDisciplina(){
		try{
			limparMensagem();
			getFacadeFactory().getResultadoProcessoSeletivoFacade().gravarLancamentoNotaPorDisciplina(getListaResultadoProcessoSeletivo(), getUsuarioLogado());		
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String editarResultadoProcessoSeletivo(){
		try{		
			setInscricaoVO(new InscricaoVO());
			setInscricaoVO(getResultadoProcessoSeletivoVO().getInscricao());			
			getInscricaoVO().setResultadoProcessoSeletivoVO(getResultadoProcessoSeletivoVO());
			setExibirResultadoPorProcessoSeletivoNotaDisciplina(true);
			setExibirResultadoPorProcessoSeletivo(false);
			limparMensagem();
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
		}catch(Exception e){			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}
	
	public String voltarResultadoProcessoSeletivo(){
		setExibirResultadoPorProcessoSeletivoNotaDisciplina(false);
		setExibirResultadoPorProcessoSeletivo(true);
		consultarCanditado();
		return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoProcessoSeletivoForm.xhtml");
	}

	public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
		if(resultadoProcessoSeletivoVO == null){
			resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
		}
		return resultadoProcessoSeletivoVO;
	}

	public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
		this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
	}

	public Boolean getExibirResultadoPorProcessoSeletivoNotaDisciplina() {
		if(exibirResultadoPorProcessoSeletivoNotaDisciplina == null){
			exibirResultadoPorProcessoSeletivoNotaDisciplina = false;
		}
		return exibirResultadoPorProcessoSeletivoNotaDisciplina;
	}

	public void setExibirResultadoPorProcessoSeletivoNotaDisciplina(
			Boolean exibirResultadoPorProcessoSeletivoNotaDisciplina) {
		this.exibirResultadoPorProcessoSeletivoNotaDisciplina = exibirResultadoPorProcessoSeletivoNotaDisciplina;
	}
	
	
	public void editarCalcularMediaNotaDisciplinaLancadaManualmente() {
		try {
			getFacadeFactory().getResultadoProcessoSeletivoFacade().realizarCalculoMediaNotaLancadaManualmente((ResultadoProcessoSeletivoVO)getRequestMap().get("resultado"), getUsuarioLogado());						
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCandidato(){
		getListaConsultaInscricao().clear();
		getListaResultadoProcessoSeletivo().clear();
	}
	
	public void consultarCanditadoFiltrarDisciplina() {		
			consultarCanditado();		
			if(!getListaConsultaInscricao().isEmpty()){
				montarListaSelectItemLancamentoNotaPorDisciplina();
			}
	}
	
	public void consultarCandidatosNaoCompareceram() {
		setListaInscricaoNaoComparecidosVOs(getFacadeFactory().getInscricaoFacade().consultarInscricaoNaoCompareceuProcessamentoResultadoProcessoSeletivo(null, getInscricaoVO().getProcSeletivo().getCodigo(), getDataProva(), getSala()));
	}

	public List<InscricaoVO> getListaInscricaoNaoComparecidosVOs() {
		if (listaInscricaoNaoComparecidosVOs == null) {
			listaInscricaoNaoComparecidosVOs = new ArrayList<InscricaoVO>(0);
		}
		return listaInscricaoNaoComparecidosVOs;
	}

	public void setListaInscricaoNaoComparecidosVOs(List<InscricaoVO> listaInscricaoNaoComparecidosVOs) {
		this.listaInscricaoNaoComparecidosVOs = listaInscricaoNaoComparecidosVOs;
	}
	
	public void realizarRegistroInscricaoComoNaoCompareceu() {
		try {
			getFacadeFactory().getInscricaoFacade().realizarAlteracaoInscricaoNaoCompareceu(getListaInscricaoNaoComparecidosVOs(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			consultarCanditado();
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosListaInscricaoNaoCompareceu() {
		for (InscricaoVO obj : getListaInscricaoNaoComparecidosVOs()) {
			if (obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				obj.setSelecionar(true);
			}
		}
	}
	
	public void desmarcarTodosListaInscricaoNaoCompareceu() {
		for (InscricaoVO obj : getListaInscricaoNaoComparecidosVOs()) {
			if (obj.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				obj.setSelecionar(false);
			}
		}
	}
	
	public Integer getQtdeCandidatoNaoCompareceu() {
		return getListaInscricaoNaoComparecidosVOs().size();
	}
	
	public Integer getQtdeCandidatoSelecionadoNaoCompareceu() {
		Integer qtde = 0;
		for (InscricaoVO obj : getListaInscricaoNaoComparecidosVOs()) {
			if (obj.getSelecionar()) {
				qtde++;
			}
		}
		return qtde;
	}
	
	  
	  public void montarListaSelectItemCursoOpcao()   {
	    	List<ProcSeletivoCursoVO> resultadoConsulta = null;
	        boolean isExisteUnidadeEnsinoCursoCandidatoInscrito = false;
	        Iterator i = null;
	        try {
	            if ((getInscricaoVO().getProcSeletivo() == null)) {
	                List<SelectItem> objs = new ArrayList<SelectItem>(0);
	                setListaSelectItemCursoOpcao(objs);
	                return;
	            }
	            
	            resultadoConsulta  =  getFacadeFactory().getProcSeletivoCursoFacade().consultarPorCodigoProcSeletivoUnidadeEnsino(getInscricaoVO().getProcSeletivo().getCodigo(),0,getUsuarioLogado() );

	            i = resultadoConsulta.iterator();
	            List<SelectItem> objs = new ArrayList<SelectItem>(0);
	            objs.add(new SelectItem(0, ""));
	            while (i.hasNext()) {
	                ProcSeletivoCursoVO proc = (ProcSeletivoCursoVO) i.next();
	                if (!proc.getUnidadeEnsinoCurso().getCurso().getNivelEducacionalPosGraduacao()) {
						if (getInscricaoVO().getCursoOpcao1().getCodigo().equals(proc.getUnidadeEnsinoCurso().getCodigo())) {
							isExisteUnidadeEnsinoCursoCandidatoInscrito = true;
						} else if (getInscricaoVO().getCursoOpcao1().getCurso().getCodigo().equals(proc.getUnidadeEnsinoCurso().getCurso().getCodigo())
								&& getInscricaoVO().getCursoOpcao1().getTurno().getCodigo().equals(proc.getUnidadeEnsinoCurso().getTurno().getCodigo())) {
							getInscricaoVO().setCursoOpcao1(proc.getUnidadeEnsinoCurso());
							isExisteUnidadeEnsinoCursoCandidatoInscrito = true;
						}
	                    objs.add(new SelectItem(proc.getUnidadeEnsinoCurso().getCodigo(), proc.getUnidadeEnsinoCurso().getCurso().getNome() + " - " + proc.getUnidadeEnsinoCurso().getTurno().getNome()+" - "+ proc.getUnidadeEnsinoCurso().getNomeUnidadeEnsino()));
	                }
	            }
	            if(!getInscricaoVO().getNovoObj() && !isExisteUnidadeEnsinoCursoCandidatoInscrito && getInscricaoVO().getUnidadeEnsino().getCodigo().equals(getInscricaoVO().getCursoOpcao1().getUnidadeEnsino())){
	                objs.add(new SelectItem(getInscricaoVO().getCursoOpcao1().getCodigo(), getInscricaoVO().getCursoOpcao1().getCurso().getNome() + " - " + getInscricaoVO().getCursoOpcao1().getTurno().getNome()+" - "+getInscricaoVO().getCursoOpcao1().getNomeUnidadeEnsino()));
	            }
	            setListaSelectItemCursoOpcao(objs);
	          
	        } catch (Exception e) {
	        	setMensagemDetalhada("msg_erro", e.getMessage());
	        } finally {
	            Uteis.liberarListaMemoria(resultadoConsulta);
	            i = null;
	        }
	    }

	  
	    public List getListaSelectItemCursoOpcao() {
	        if (listaSelectItemCursoOpcao == null) {
	            listaSelectItemCursoOpcao = new ArrayList(0);
	        }
	        return (listaSelectItemCursoOpcao);
	    }

	    public void setListaSelectItemCursoOpcao(List listaSelectItemCursoOpcao) {
	        this.listaSelectItemCursoOpcao = listaSelectItemCursoOpcao;
	    }

	    
	    public void realizarimprimirRedacaoPDF() {
	    	if(Uteis.isAtributoPreenchido(getInscricaoRedacao().getResultadoProcessoSeletivoVO().getRedacao().trim())) {
	    		
	    		List<InscricaoVO> listaObjetosRedacao = new ArrayList<InscricaoVO>();
	    		listaObjetosRedacao.add(getInscricaoRedacao());
	    		gerarRelatorioPdf(listaObjetosRedacao);
	    	}else {
	    		 setMensagemID("msg_relatorio_sem_dados");
	    	}
	    }
	    public void realizarimprimirRedacaoTodosPDF() {
	    	gerarRelatorioPdf(getListaInscricaoComProvaRedacao());
	    }
	    public void gerarRelatorioPdf(List<InscricaoVO> listaObjetosRedacao)  {		
			try {				
				
				if (!listaObjetosRedacao.isEmpty()) {
					getSuperParametroRelVO().setTituloRelatorio("Redação");
					getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRedacao());
					getSuperParametroRelVO().setListaObjetos(listaObjetosRedacao);	
					getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());

					getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setNomeEmpresa("");
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setFiltros("");			
						
					realizarImpressaoRelatorio();					
					setMensagemID("msg_impressao_sucesso");
				} else {
	                setMensagemID("msg_relatorio_sem_dados");
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
			}
		}
	    
	    public String getDesignIReportRedacao() {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoRedacao.jrxml");
		}
	    
	    public String getCaminhoBaseRelatorio() {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
		}

		public InscricaoVO getInscricaoRedacao() {
			if(inscricaoRedacao == null ) {
				inscricaoRedacao= new InscricaoVO();
			}
			return inscricaoRedacao;
		}

		public void setInscricaoRedacao(InscricaoVO inscricaoRedacao) {
			this.inscricaoRedacao = inscricaoRedacao;
		}
		
		
		public boolean getPossuiRedacao() {
			if(possuiRedacao == null ) {
				possuiRedacao= Boolean.FALSE;
			}
			return possuiRedacao;
		}
		
		public void realizarVerificacaoInscricaoPossuiProvaComRedacao(){	
			setPossuiRedacao(false);
			setListaInscricaoComProvaRedacao(null);
			for(InscricaoVO inscricao : getListaConsultaInscricao()) {
				for(ProcessoSeletivoProvaDataVO prosDataProva : inscricao.getItemProcessoSeletivoDataProva().getProcessoSeletivoProvaDataVOs()) {
					if(prosDataProva.getProvaProcessoSeletivo().getPossuiRedacao()) {
						getListaInscricaoComProvaRedacao().add(inscricao);
						setPossuiRedacao(true);
						inscricao.setPossuiRedacao(true);
						break;
					}						
				}			
		    }		
		}

		public void setPossuiRedacao(Boolean possuiRedacao) {
			this.possuiRedacao = possuiRedacao;
		}

		public List<InscricaoVO> getListaInscricaoComProvaRedacao() {
			if(listaInscricaoComProvaRedacao == null) {
				listaInscricaoComProvaRedacao= new ArrayList<InscricaoVO>(0);
			}
			return listaInscricaoComProvaRedacao;
		}

		public void setListaInscricaoComProvaRedacao(List<InscricaoVO> listaInscricaoComProvaRedacao) {
			this.listaInscricaoComProvaRedacao = listaInscricaoComProvaRedacao;
		}
	
	public Boolean getExibirQuanditadeAcertos() {
		if (exibirQuanditadeAcertos == null) {
			exibirQuanditadeAcertos = Boolean.FALSE;
		}
		return exibirQuanditadeAcertos;
	}
	
	public void setExibirQuanditadeAcertos(Boolean exibirQuanditadeAcertos) {
		this.exibirQuanditadeAcertos = exibirQuanditadeAcertos;
	}
}
