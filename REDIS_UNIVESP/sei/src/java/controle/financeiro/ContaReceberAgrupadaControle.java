package controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.enumerador.NivelAgrupamentoContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import relatorio.negocio.jdbc.financeiro.BoletoBancarioRel;

@Controller("ContaReceberAgrupadaControle")
@Scope("viewScope")
public class ContaReceberAgrupadaControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8103498354538590485L;

	private ContaReceberAgrupadaVO contaReceberAgrupadaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber;
	private TurmaVO turmaVO;
	private MatriculaVO matriculaVO;
	private PessoaVO responsavelFinanceiroVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	private List<SelectItem> listaSelectItemNivelAgrupamento;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemAno;
	private List<SelectItem> listaSelectItemMes;
	private Boolean agruparRequerimento;
	private Boolean agruparBiblioteca;
	private Boolean agruparContratoReceita;
	private Boolean agruparContaResponsavelFinanceiro;
	private Integer ano;
	private MesAnoEnum mes;
	private List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs;
	private ContaCorrenteVO contaCorrenteVO;
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemSituacaoContaReceber;
	private SituacaoContaReceber situacaoContaReceber;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<SelectItem> listaSelectItemOpcaoConsultaTurma;
	private String campoConsultaMatricula;
	private String valorConsultaMatricula;
	private List<SelectItem> listaSelectItemOpcaoConsultaMatricula;
	private String campoConsultaResponsavelFinanceiro;
	private String valorConsultaResponsavelFinanceiro;
	private List<SelectItem> listaSelectItemOpcaoConsultaResponsavelFinanceiro;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<SelectItem> listaSelectItemOpcaoConsultaCurso;
	private List<MatriculaVO> listaConsultaMatricula;
	private List<TurmaVO> listaConsultaTurma;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private List<PessoaVO> listaConsultaResponsavelFinanceiro;
	private Boolean emProcessamento;
	private List<ContaReceberVO> contaReceberVOs;
	private String tipoBoleto;	
	

	public ContaReceberAgrupadaControle() {
		super();
		getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsultaOtimizado().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));		
	}

	public void persistir() {
		try {
			getFacadeFactory().getContaReceberAgrupadaFacade().persistir(getContaReceberAgrupadaVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getContaReceberAgrupadaFacade().excluir(getContaReceberAgrupadaVO(), true, getUsuarioLogado());
			
			if (getEmProcessamento()) {
				int index = 0;
				for (ContaReceberAgrupadaVO obj : getContaReceberAgrupadaVOs()) {
					if(obj.getCodigo().equals(getContaReceberAgrupadaVO().getCodigo())){
						getContaReceberAgrupadaVOs().remove(index);
						break;
					}
					index++;
				}
				setContaReceberAgrupadaVO(new ContaReceberAgrupadaVO());
				setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
				return "novo";
			}else{
				setContaReceberAgrupadaVO(new ContaReceberAgrupadaVO());
				setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
				setControleConsultaOtimizado(new DataModelo());
				getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
				getControleConsultaOtimizado().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));		
			}
			
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaForm.xhtml");
		}

	}

	public void removerContaReceber() {
		try {
			getFacadeFactory().getContaReceberAgrupadaFacade().removerContaReceber(getContaReceberAgrupadaVO(), (ContaReceberVO) getRequestMap().get("contaReceberItens"));
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarContaReceber() {
		try {
			setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarContaReceberPorAptaAdicionarAgrupamento(getContaReceberAgrupadaVO().getUnidadeEnsino().getCodigo(), getContaReceberAgrupadaVO().getDataVencimento(), getContaReceberAgrupadaVO().getTipoPessoa(), getContaReceberAgrupadaVO().getMatricula().getMatricula(), getContaReceberAgrupadaVO().getPessoa().getCodigo(), getUsuarioLogado()));
			if (getContaReceberVOs().isEmpty()) {
				setMensagemID("msg_ContaReceberAgrupada_naoExisteContaReceberAptaAgrupamento", Uteis.ALERTA);
			} else {
				setMensagemID("msg_ContaReceberAgrupada_selecionarContaReceberAptaAgrupamento", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarContaReceber() {
		try {
			getFacadeFactory().getContaReceberAgrupadaFacade().adicionarContaReceber(getContaReceberAgrupadaVO(), (ContaReceberVO) getRequestMap().get("contaReceberItens"));
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void imprimirBoleto() {
		String titulo = null;
        List lista = null;
		try {
			getFacadeFactory().getContaReceberAgrupadaFacade().montarDadosContaCorrente(getContaReceberAgrupadaVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletosRelControle", "Inicializando Impressao PDF", "Emitindo Relatorio");
            titulo = "Recibo do Sacado";
            lista = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioListaContaAgrupada(false, getContaReceberAgrupadaVO(), null, null, null, null, null, null, null, null, null, "", null, getUsuarioLogado(), "", null, getConfiguracaoFinanceiroPadraoSistema(), null, false);
            if (!lista.isEmpty()) {
                String design = "";
                BoletoBancarioRelVO boletoBancarioRelVO = (BoletoBancarioRelVO) lista.get(0);
                if (getTipoBoleto().equals("boleto")) {
                    if (boletoBancarioRelVO.getBanco_nrbanco().equals("104")) {
                        design = BoletoBancarioRel.getDesignIReportRelatorioCaixaEconomica();
                    } else {
                        design = BoletoBancarioRel.getDesignIReportRelatorio();
                    }
                    apresentarRelatorioObjetos(BoletoBancarioRel.getIdEntidade(), titulo, getUnidadeEnsinoLogado().getNome(), "", "PDF", "", design,
                            getUsuarioLogado().getNome(), "", lista, BoletoBancarioRel.getCaminhoBaseRelatorio());

                } else if (getTipoBoleto().equals("boletoSegundo")) {
                    if (boletoBancarioRelVO.getBanco_nrbanco().equals("104")) {
                        design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundoCaixaEconomica();
                    } else {
                        design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundo();
                    }
                    apresentarRelatorioObjetos(BoletoBancarioRel.getIdEntidade(), titulo, getUnidadeEnsinoLogado().getNome(), "", "PDF", "", design,
                            getUsuarioLogado().getNome(), "", lista, BoletoBancarioRel.getCaminhoBaseRelatorio());

                } else {
                    design = BoletoBancarioRel.getDesignIReportRelatorioCarne();
                    apresentarRelatorioObjetos(BoletoBancarioRel.getIdEntidadeCarne(), titulo, getUnidadeEnsinoLogado().getNome(), "", "PDF", "",
                            design, getUsuarioLogado().getNome(), "", lista, BoletoBancarioRel.getCaminhoBaseRelatorio());
                }
                removerObjetoMemoria(this);
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "BoletosRelControle", "Finalizando Impressao PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarImprimirBoleto() {
		try {
			editar();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void regerarNossoNumero() {
		try {
			getFacadeFactory().getContaReceberAgrupadaFacade().montarDadosContaCorrente(getContaReceberAgrupadaVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getFacadeFactory().getContaReceberAgrupadaFacade().realizarCriacaoNossoNumero(getContaReceberAgrupadaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			setContaReceberAgrupadaVO(new ContaReceberAgrupadaVO());
			setContaReceberAgrupadaVO(getFacadeFactory().getContaReceberAgrupadaFacade().consultaPorChavePrimaria(((ContaReceberAgrupadaVO) getRequestMap().get("contaReceberAgrupadaItens")).getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			
			getContaReceberAgrupadaVO().setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarPorCodigoAgrupamento(getContaReceberAgrupadaVO().getCodigo(), NivelMontarDados.TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaForm.xhtml");
		}

	}

	public String novo() {
		getContaReceberAgrupadaVOs().clear();
		limparDadosUnidadeEnsino();
		setNivelAgrupamentoContaReceber(NivelAgrupamentoContaReceberEnum.UNIDADE_ENSINO);
		setEmProcessamento(true);
		setListaSelectItemContaCorrente(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaProcessamentoForm.xhtml");
	}

	public void realizarAgrupamentoContaReceber() {
		try {
			getContaReceberAgrupadaVOs().clear();
			setQtdeContaAgrupada(null);
			setContaReceberAgrupadaVOs(getFacadeFactory().getContaReceberAgrupadaFacade().realizarAgrupamentoContaReceber(getNivelAgrupamentoContaReceber(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO(), getMatriculaVO(), getTurmaVO(), getResponsavelFinanceiroVO(), getAgruparBiblioteca(), getAgruparRequerimento(), getAgruparContratoReceita(), getAgruparContaResponsavelFinanceiro(), getContaCorrenteVO(), getAno(), getMes(), getUsuarioLogado()));
			if(getContaReceberAgrupadaVOs().isEmpty()){
				setMensagemID("msg_ContaReceberAgrupada_naoExisteContaReceberParaAgrupamento", Uteis.ALERTA);
			}else{
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Integer getQtdeContaGerada() {
		return getContaReceberAgrupadaVOs().size();
	}

	public Integer qtdeContaAgrupada;

	public void setQtdeContaAgrupada(Integer qtdeContaAgrupada) {
		this.qtdeContaAgrupada = qtdeContaAgrupada;
	}

	public Integer getQtdeContaAgrupada() {
		if (qtdeContaAgrupada == null) {
			qtdeContaAgrupada = 0;
			for (ContaReceberAgrupadaVO obj : getContaReceberAgrupadaVOs()) {
				qtdeContaAgrupada += obj.getContaReceberVOs().size();
			}
		}
		return qtdeContaAgrupada;
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getContaReceberAgrupadaFacade().consultarContaReceberAgrupada(getUnidadeEnsinoLogado(), getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getSituacaoContaReceber(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberAgrupadaFacade().consultarTotalRegistroContaReceberAgrupada(getUnidadeEnsinoLogado(), getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getSituacaoContaReceber()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaCons.xhtml");
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public String irPaginaConsulta() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsultaOtimizado().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));		
		setEmProcessamento(false);
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaCons.xhtml");
	}

	public String voltarPaginaProcessamento() {
		setEmProcessamento(true);
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberAgrupadaCons.xhtml");
	}

	public void limparDadosUnidadeEnsino() {
		limparDadosCurso();
		limparDadosMatricula();
		limparDadosResponsavelFinanceiro();
		limparDadosTurma();
		setListaSelectItemContaCorrente(null);
	}

	public void limparDadosTurma() {
		getTurmaVO().setCodigo(0);
		getTurmaVO().setIdentificadorTurma("");
	}

	public void consultarTurmaPorIdentificadorTurma() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			limparDadosTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("curso")) {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
				unidadeEnsinoVOs.add(getUnidadeEnsinoVO());
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarRapidaPorNomeCurso(getValorConsultaCurso(), unidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				unidadeEnsinoVOs.clear();
				unidadeEnsinoVOs = null;
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) getRequestMap().get("turmaItens"));
			getListaConsultaTurma().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosMatricula() {
		getMatriculaVO().setMatricula("");
		getMatriculaVO().getAluno().setNome("");
		getMatriculaVO().getAluno().setCodigo(0);
	}

	public void consultarMatriculaPorMatricula() {
		try {
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			limparDadosMatricula();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarMatricula() {
		try {
			if (getCampoConsultaMatricula().equals("matricula")) {
				setListaConsultaMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaMatricula(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaMatricula().equals("nome")) {
				setListaConsultaMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaMatricula().equals("curso")) {
				setListaConsultaMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaMatricula(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarMatricula() {
		try {
			setMatriculaVO((MatriculaVO) getRequestMap().get("matriculaItens"));
			getListaConsultaMatricula().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosCurso() {
		getUnidadeEnsinoCursoVO().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setNome("");
		getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
		getUnidadeEnsinoCursoVO().getTurno().setNome("");
	}

	public void consultarCurso() {
		try {
			if (getCampoConsultaCurso().equals("nome")) {
				setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaCurso().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		try {
			setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) getRequestMap().get("cursoItens"));
			getListaConsultaCurso().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosResponsavelFinanceiro() {
		getResponsavelFinanceiroVO().setCodigo(0);
		getResponsavelFinanceiroVO().setNome("");
	}

	public void consultarResponsavelFinanceiro() {
		try {
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaResponsavelFinanceiro().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			setResponsavelFinanceiroVO((PessoaVO) getRequestMap().get("responsavelFinanceiroItens"));
			getListaConsultaResponsavelFinanceiro().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public ContaReceberAgrupadaVO getContaReceberAgrupadaVO() {
		if (contaReceberAgrupadaVO == null) {
			contaReceberAgrupadaVO = new ContaReceberAgrupadaVO();
		}
		return contaReceberAgrupadaVO;
	}

	public void setContaReceberAgrupadaVO(ContaReceberAgrupadaVO contaReceberAgrupadaVO) {
		this.contaReceberAgrupadaVO = contaReceberAgrupadaVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO.setCodigo(getUnidadeEnsinoLogado().getCodigo());
			unidadeEnsinoVO.setNome(getUnidadeEnsinoLogado().getNome());
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public NivelAgrupamentoContaReceberEnum getNivelAgrupamentoContaReceber() {
		if (nivelAgrupamentoContaReceber == null) {
			nivelAgrupamentoContaReceber = NivelAgrupamentoContaReceberEnum.UNIDADE_ENSINO;
		}
		return nivelAgrupamentoContaReceber;
	}

	public void setNivelAgrupamentoContaReceber(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber) {
		this.nivelAgrupamentoContaReceber = nivelAgrupamentoContaReceber;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public PessoaVO getResponsavelFinanceiroVO() {
		if (responsavelFinanceiroVO == null) {
			responsavelFinanceiroVO = new PessoaVO();
		}
		return responsavelFinanceiroVO;
	}

	public void setResponsavelFinanceiroVO(PessoaVO responsavelFinanceiroVO) {
		this.responsavelFinanceiroVO = responsavelFinanceiroVO;
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

	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>();
			listaSelectItemOpcaoConsulta.add(new SelectItem("SACADO", "Sacado"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("MATRICULA", "Matrícula"));			
			listaSelectItemOpcaoConsulta.add(new SelectItem("NOSSO_NUMERO", "Nosso Número Conta Agrugada"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("NOSSO_NUMERO_CONTA_RECEBER", "Nosso Número Conta Receber"));
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}

	public List<SelectItem> getListaSelectItemNivelAgrupamento() {
		if (listaSelectItemNivelAgrupamento == null) {
			listaSelectItemNivelAgrupamento = new ArrayList<SelectItem>(0);
			for (NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceberEnum : NivelAgrupamentoContaReceberEnum.values()) {
				listaSelectItemNivelAgrupamento.add(new SelectItem(nivelAgrupamentoContaReceberEnum.name(), nivelAgrupamentoContaReceberEnum.getValorApresentar()));
			}
		}
		return listaSelectItemNivelAgrupamento;
	}

	public void setListaSelectItemNivelAgrupamento(List<SelectItem> listaSelectItemNivelAgrupamento) {
		this.listaSelectItemNivelAgrupamento = listaSelectItemNivelAgrupamento;
	}

	public List<SelectItem> getListaSelectItemAno() {
		if (listaSelectItemAno == null) {
			listaSelectItemAno = new ArrayList<SelectItem>(0);
			Integer anoAtual = Integer.valueOf(Uteis.getAnoDataAtual4Digitos());
			listaSelectItemAno.add(new SelectItem(anoAtual, anoAtual.toString()));
			anoAtual++;
			listaSelectItemAno.add(new SelectItem(anoAtual, anoAtual.toString()));
			anoAtual++;
			listaSelectItemAno.add(new SelectItem(anoAtual, anoAtual.toString()));
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}

	public List<SelectItem> getListaSelectItemMes() {
		if (listaSelectItemMes == null) {
			listaSelectItemMes = new ArrayList<SelectItem>();
			for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
				listaSelectItemMes.add(new SelectItem(mesAnoEnum, mesAnoEnum.getMes()));
			}
		}
		return listaSelectItemMes;
	}

	public void setListaSelectItemMes(List<SelectItem> listaSelectItemMes) {
		this.listaSelectItemMes = listaSelectItemMes;
	}

	public Boolean getAgruparRequerimento() {
		if (agruparRequerimento == null) {
			agruparRequerimento = true;
		}
		return agruparRequerimento;
	}

	public void setAgruparRequerimento(Boolean agruparRequerimento) {
		this.agruparRequerimento = agruparRequerimento;
	}

	public Boolean getAgruparBiblioteca() {
		if (agruparBiblioteca == null) {
			agruparBiblioteca = true;
		}
		return agruparBiblioteca;
	}

	public void setAgruparBiblioteca(Boolean agruparBiblioteca) {
		this.agruparBiblioteca = agruparBiblioteca;
	}

	public Boolean getAgruparContratoReceita() {
		if (agruparContratoReceita == null) {
			agruparContratoReceita = true;
		}
		return agruparContratoReceita;
	}

	public void setAgruparContratoReceita(Boolean agruparContratoReceita) {
		this.agruparContratoReceita = agruparContratoReceita;
	}

	public List<ContaReceberAgrupadaVO> getContaReceberAgrupadaVOs() {
		if (contaReceberAgrupadaVOs == null) {
			contaReceberAgrupadaVOs = new ArrayList<ContaReceberAgrupadaVO>(0);
		}
		return contaReceberAgrupadaVOs;
	}

	public void setContaReceberAgrupadaVOs(List<ContaReceberAgrupadaVO> contaReceberAgrupadaVOs) {
		this.contaReceberAgrupadaVOs = contaReceberAgrupadaVOs;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}
	
	public void inicializarDadosContaCorrente() {
		setListaSelectItemContaCorrente(null);
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
			try {
				List<ContaCorrenteVO> contaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false, false, getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
				for (ContaCorrenteVO contaCorrenteVO : contaCorrenteVOs) {
					listaSelectItemContaCorrente.add(new SelectItem(contaCorrenteVO.getCodigo(), contaCorrenteVO.getBancoAgenciaContaCorrente()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> getListaSelectItemSituacaoContaReceber() {
		if (listaSelectItemSituacaoContaReceber == null) {
			listaSelectItemSituacaoContaReceber = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoContaReceber.add(new SelectItem(null, ""));
			listaSelectItemSituacaoContaReceber.add(new SelectItem(SituacaoContaReceber.A_RECEBER, SituacaoContaReceber.A_RECEBER.getDescricao()));
			listaSelectItemSituacaoContaReceber.add(new SelectItem(SituacaoContaReceber.RECEBIDO, SituacaoContaReceber.RECEBIDO.getDescricao()));
		}
		return listaSelectItemSituacaoContaReceber;
	}

	public void setListaSelectItemSituacaoContaReceber(List<SelectItem> listaSelectItemSituacaoContaReceber) {
		this.listaSelectItemSituacaoContaReceber = listaSelectItemSituacaoContaReceber;
	}

	public SituacaoContaReceber getSituacaoContaReceber() {

		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(SituacaoContaReceber situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public Boolean getAgruparContaResponsavelFinanceiro() {
		if (agruparContaResponsavelFinanceiro == null) {
			agruparContaResponsavelFinanceiro = false;
		}
		return agruparContaResponsavelFinanceiro;
	}

	public void setAgruparContaResponsavelFinanceiro(Boolean agruparContaResponsavelFinanceiro) {
		this.agruparContaResponsavelFinanceiro = agruparContaResponsavelFinanceiro;
	}

	public Integer getAno() {
		if (ano == null) {
			ano = Integer.valueOf(Uteis.getAnoDataAtual4Digitos());
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public MesAnoEnum getMes() {
		if (mes == null) {
			mes = MesAnoEnum.getEnum(Uteis.getMesDataAtual() + "");
		}
		return mes;
	}

	public void setMes(MesAnoEnum mes) {
		this.mes = mes;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "identificadorTurma";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaTurma() {
		if (listaSelectItemOpcaoConsultaTurma == null) {
			listaSelectItemOpcaoConsultaTurma = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaTurma.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			listaSelectItemOpcaoConsultaTurma.add(new SelectItem("curso", "Curso"));
		}
		return listaSelectItemOpcaoConsultaTurma;
	}

	public void setListaSelectItemOpcaoConsultaTurma(List<SelectItem> listaSelectItemOpcaoConsultaTurma) {
		this.listaSelectItemOpcaoConsultaTurma = listaSelectItemOpcaoConsultaTurma;
	}

	public String getCampoConsultaMatricula() {
		if (campoConsultaMatricula == null) {
			campoConsultaMatricula = "nome";
		}
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public String getValorConsultaMatricula() {
		if (valorConsultaMatricula == null) {
			valorConsultaMatricula = "";
		}
		return valorConsultaMatricula;
	}

	public void setValorConsultaMatricula(String valorConsultaMatricula) {
		this.valorConsultaMatricula = valorConsultaMatricula;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaMatricula() {
		if (listaSelectItemOpcaoConsultaMatricula == null) {
			listaSelectItemOpcaoConsultaMatricula = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaMatricula.add(new SelectItem("nome", "Aluno"));
			listaSelectItemOpcaoConsultaMatricula.add(new SelectItem("matricula", "Matrícula"));
			listaSelectItemOpcaoConsultaMatricula.add(new SelectItem("curso", "Curso"));
		}
		return listaSelectItemOpcaoConsultaMatricula;
	}

	public void setListaSelectItemOpcaoConsultaMatricula(List<SelectItem> listaSelectItemOpcaoConsultaMatricula) {
		this.listaSelectItemOpcaoConsultaMatricula = listaSelectItemOpcaoConsultaMatricula;
	}

	public String getCampoConsultaResponsavelFinanceiro() {
		if (campoConsultaResponsavelFinanceiro == null) {
			campoConsultaResponsavelFinanceiro = "nome";
		}
		return campoConsultaResponsavelFinanceiro;
	}

	public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
		this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
	}

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaResponsavelFinanceiro() {
		if (listaSelectItemOpcaoConsultaResponsavelFinanceiro == null) {
			listaSelectItemOpcaoConsultaResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
		}
		return listaSelectItemOpcaoConsultaResponsavelFinanceiro;
	}

	public void setListaSelectItemOpcaoConsultaResponsavelFinanceiro(List<SelectItem> listaSelectItemOpcaoConsultaResponsavelFinanceiro) {
		this.listaSelectItemOpcaoConsultaResponsavelFinanceiro = listaSelectItemOpcaoConsultaResponsavelFinanceiro;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "nome";
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

	public List<SelectItem> getListaSelectItemOpcaoConsultaCurso() {
		if (listaSelectItemOpcaoConsultaCurso == null) {
			listaSelectItemOpcaoConsultaCurso = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("nome", "Nome"));
		}
		return listaSelectItemOpcaoConsultaCurso;
	}

	public void setListaSelectItemOpcaoConsultaCurso(List<SelectItem> listaSelectItemOpcaoConsultaCurso) {
		this.listaSelectItemOpcaoConsultaCurso = listaSelectItemOpcaoConsultaCurso;
	}

	public List<MatriculaVO> getListaConsultaMatricula() {
		if (listaConsultaMatricula == null) {
			listaConsultaMatricula = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaMatricula;
	}

	public void setListaConsultaMatricula(List<MatriculaVO> listaConsultaMatricula) {
		this.listaConsultaMatricula = listaConsultaMatricula;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
	}

	public Boolean getEmProcessamento() {
		if (emProcessamento == null) {
			emProcessamento = false;
		}
		return emProcessamento;
	}

	public void setEmProcessamento(Boolean emProcessamento) {
		this.emProcessamento = emProcessamento;
	}

	public List<ContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	private List<SelectItem> tipoConsultaComboTipoBoleto;

	public List<SelectItem> getTipoConsultaComboTipoBoleto() {
		if (tipoConsultaComboTipoBoleto == null) {
			tipoConsultaComboTipoBoleto = new ArrayList<SelectItem>();
			tipoConsultaComboTipoBoleto.add(new SelectItem("boleto", "Boleto"));
			tipoConsultaComboTipoBoleto.add(new SelectItem("boletoSegundo", "Boleto (LayOut 2)"));
			tipoConsultaComboTipoBoleto.add(new SelectItem("carne", "Carnê"));
		}
		return tipoConsultaComboTipoBoleto;
	}

	public String getTipoBoleto() {
		if (tipoBoleto == null) {
			tipoBoleto = "boleto";
		}
		return tipoBoleto;
	}

	public void setTipoBoleto(String tipoBoleto) {
		this.tipoBoleto = tipoBoleto;
	}

	public Boolean getNivelCurso() {
		return getNivelAgrupamentoContaReceber().equals(NivelAgrupamentoContaReceberEnum.CURSO);
	}

	public Boolean getNivelTurma() {
		return getNivelAgrupamentoContaReceber().equals(NivelAgrupamentoContaReceberEnum.TURMA);
	}

	public Boolean getNivelAluno() {
		return getNivelAgrupamentoContaReceber().equals(NivelAgrupamentoContaReceberEnum.ALUNO);
	}

	public Boolean getNivelResponsavelFinanceiro() {
		return getNivelAgrupamentoContaReceber().equals(NivelAgrupamentoContaReceberEnum.RESPONSAVEL_FINANCEIRO);
	}

}
