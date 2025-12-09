/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.controle.academico.AtaResultadosFinaisRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelVO;

/**
 *
 * @author Philippe
 */
@Controller("TermoReconhecimentoDividaRelControle")
@Scope("viewScope")
@Lazy
public class TermoReconhecimentoDividaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -8136190775992643436L;

	private NegociacaoContaReceberVO negociacaoContaReceberVO;
    private ContaReceberVO contaReceberVO;
    private String observacaoHistorico;
    private List listaSelectItemRenegociacaoContaReceber;
    protected List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private MatriculaVO alunoVO;
    private String tipoRelatorio;
    private String tipoContaReceber;
    private List<SelectItem> listaSelectItemTipoPessoa;
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;
    private String tipoLayout;
    private List listaSelectItemTipoTextoPadrao;
    private Integer textoPadraoDeclaracao;
    private Boolean imprimirContrato;
    private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
    private Boolean marcarTodosTipoOrigem;
    
    private String observacaoComplementar;
    
    public TermoReconhecimentoDividaRelControle() {
		Map<String, String> mapResultado = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] {"observacaoComplementar"}, TermoReconhecimentoDividaRelControle.class.getSimpleName());
		if(mapResultado.containsKey("observacaoComplementar")) {
			setObservacaoComplementar(mapResultado.get("observacaoComplementar"));
		}
	}

	public void imprimirPDF() {
		String design = null;
		String titulo = null;
		List<TermoReconhecimentoDividaRelVO> listaObjetos = new ArrayList<TermoReconhecimentoDividaRelVO>(0);
		try {
			if(!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getCodigo())){
				throw new Exception("O campo RENEGOCIAÇÃO CONTA RECEBER deve ser informado.");
			}
			setNegociacaoContaReceberVO(getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorChavePrimaria(getNegociacaoContaReceberVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			titulo = "TERMO DE RECONHECIMENTO DE DÍVIDA COM ACORDO DE DÉBITO ADMINISTRATIVO (ART.784, III do CPC)";
			if (getTipoLayout().equals("TermoReconhecimentoDividaLayout3Rel")) {
				design = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().designIReportRelatorioLayout3();
				listaObjetos = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().criarObjetoLayout3(getNegociacaoContaReceberVO().getPessoa().getCodigo(), getNegociacaoContaReceberVO(), getObservacaoHistorico(), false, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			} else {
				design = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().designIReportRelatorio();
				listaObjetos = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().criarObjeto(getNegociacaoContaReceberVO().getPessoa().getCodigo(), getNegociacaoContaReceberVO(), getObservacaoHistorico(), false, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getObservacaoComplementar());
			}
			if (Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO()) && !listaObjetos.isEmpty()) {
				if (getTipoLayout().equals("TextoPadrao")) {
					TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					setCaminhoRelatorio(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().imprimirPorTextoPadrao(getAlunoVO(), (TermoReconhecimentoDividaRelVO) listaObjetos.get(0), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
					if(getCaminhoRelatorio().isEmpty()){
						setImprimirContrato(true);
						setFazerDownload(false);
					}else {
						setImprimirContrato(false);
						setFazerDownload(true);
					}
					setMensagemID("msg_relatorio_ok");
				} else {
					getSuperParametroRelVO().setTituloRelatorio(titulo);
					getSuperParametroRelVO().setNomeDesignIreport(design);
					getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().caminhoBaseRelatorio());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().caminhoBaseRelatorio());
					getSuperParametroRelVO().setListaObjetos(listaObjetos);
					getSuperParametroRelVO().adicionarParametro("dataAtualPorExtenso", Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(listaObjetos.get(0).getUnidadeEnsinoVO().getCidade().getNome(), listaObjetos.get(0).getUnidadeEnsinoVO().getCidade().getEstado().getSigla(), new Date(), true));
					realizarImpressaoRelatorio();
					setImprimirContrato(false);
					setFazerDownload(true);
				}
				persistirDadosPadroes();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	 public String getContrato() {
	        if (getImprimirContrato()) {
	        	removerObjetoMemoria(this);
	            return "abrirPopup('../../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
	        }else if(getFazerDownload()){
	        	removerObjetoMemoria(this);
	        	return getDownload();
	        }
	        return "";
	    }

    public void imprimirPDFContaReceber() {
    	String design = null;
    	String titulo = null;
    	List listaObjetos = new ArrayList(0);
    	try {
    		
    		design = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().designIReportRelatorioContaReceber();
    		titulo = "TERMO DE RECONHECIMENTO DE DÍVIDA COM ACORDO DE DÉBITO ADMINISTRATIVO (ART.784, III do CPC)";
    		listaObjetos = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().criarObjetoTermoReconhecimentoDividaContaReceber(getContaReceberVO(), getTipoContaReceber(), getUsuarioLogado(),getFiltroRelatorioFinanceiroVO());
    		if (getTipoLayout().equals("TextoPadrao")) {
				TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				setCaminhoRelatorio(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().imprimirPorTextoPadrao(getAlunoVO(), (TermoReconhecimentoDividaRelVO) listaObjetos.get(0), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			} else {
				if (!listaObjetos.isEmpty()) {
					getSuperParametroRelVO().setTituloRelatorio(titulo);
					getSuperParametroRelVO().setNomeDesignIreport(design);
					getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().caminhoBaseRelatorio());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().caminhoBaseRelatorio());
					getSuperParametroRelVO().setListaObjetos(listaObjetos);
					realizarImpressaoRelatorio();
					removerObjetoMemoria(this);
					setMensagemID("msg_relatorio_ok");
				} else {
					setMensagemID("msg_relatorio_sem_dados");
				}
			}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	} finally {
    	}
    	
    }
    
    
    
    public void imprimirPDFTermoReconhecimento() {
        String design = null;
        String titulo = null;
        List listaObjetos = new ArrayList(0);
        try {
            design = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().designIReportRelatorioLayout2();
            NegociacaoContaReceberVO negociacaoContaReceberVOLocal = getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorChavePrimaria(getNegociacaoContaReceberVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            setNegociacaoContaReceberVO(negociacaoContaReceberVOLocal);
            titulo = "TERMO DE RECONHECIMENTO DE DÍVIDA (ART.784, III do CPC)";
            listaObjetos = getFacadeFactory().getTermoReconhecimentoDividaRelFacade().criarObjetoLayout2(getNegociacaoContaReceberVO().getPessoa().getCodigo(), getNegociacaoContaReceberVO(), getObservacaoHistorico(), false, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (!getNegociacaoContaReceberVO().getCodigo().equals(0) && !listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                realizarImpressaoRelatorio();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        	removerObjetoMemoria(this);
        }
    }
    
    public String getVisualizarRelatorio() {
		return "abrirPopup('../../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
    }
    
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("RD", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<SelectItem> getListaSelectItemTipoPessoa(){
        if(listaSelectItemTipoPessoa == null){
            listaSelectItemTipoPessoa = new ArrayList<SelectItem>(2);
            listaSelectItemTipoPessoa.add(new SelectItem("AL", "ALUNO / "));
            listaSelectItemTipoPessoa.add(new SelectItem("RF", "RESPONSAVEL FINANCEIRO"));
            getNegociacaoContaReceberVO().setTipoPessoa("AL");
        }
        return listaSelectItemTipoPessoa;
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void persistirDadosPadroes() {
    	try {
    		if (getTipoLayout().equals("TextoPadrao")) {
    			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getObservacaoComplementar(),TermoReconhecimentoDividaRelControle.class.getSimpleName(), "observacaoComplementar", getUsuarioLogado());
    		}
    	} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getAlunoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getAlunoVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            if (getTipoRelatorio().equals("RENEGOCIACAO")) {
            	setAlunoVO(objAluno);
            	montarListaSelectItemRenegociacaoContaReceber(getAlunoVO().getMatricula());
            } else {
            	getContaReceberVO().setMatriculaAluno(objAluno);
            }
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        setAlunoVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
        setAlunoVO(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getAlunoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        if (getTipoRelatorio().equals("RENEGOCIACAO")) {
        	montarListaSelectItemRenegociacaoContaReceber(getAlunoVO().getMatricula());
        } else {
        	getContaReceberVO().setMatriculaAluno(getAlunoVO());
        }
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));
        setMensagemID("msg_dados_consultados");
    }

    public void limparDadosAluno() {
        setAlunoVO(new MatriculaVO());
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboTipoRelatorio() {
    	List<SelectItem> itens = new ArrayList<SelectItem>();
    	itens.add(new SelectItem("RENEGOCIACAO", "Renegociação"));
    	itens.add(new SelectItem("CONTA_RECEBER", "Conta a Receber"));
    	return itens;
    }
    
    public List<SelectItem> getTipoConsultaComboTipoContaReceber() {
    	List<SelectItem> itens = new ArrayList<SelectItem>();
    	itens.add(new SelectItem("TODAS", "Todas"));
    	itens.add(new SelectItem("A_VENCER", "À vencer"));
    	itens.add(new SelectItem("VENCIDAS", "Vencidas"));
    	return itens;
    }
    
	public void montarListaSelectItemTextoPadrao() {
		try {
	        if (getTipoLayout().equals("TextoPadrao")) {
	        	consultarListaSelectItemTipoTextoPadrao(0);
	        }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void montarListaSelectItemRenegociacaoContaReceber(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        setListaSelectItemRenegociacaoContaReceber(new ArrayList());
        try {
            if(getNegociacaoContaReceberVO().getTipoAluno()){
                resultadoConsulta = consultarRenegociacaoContaReceberPorMatriculaAluno(prm);
            }else{
                resultadoConsulta = getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorCodigoResponsavelFinanceiro(getNegociacaoContaReceberVO().getPessoa().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                NegociacaoContaReceberVO obj = (NegociacaoContaReceberVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getData_Apresentar() + " " + obj.getUnidadeEnsino().getNome() + " " + obj.getRequerente() + " " + obj.getValor()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemRenegociacaoContaReceber(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public List consultarRenegociacaoContaReceberPorMatriculaAluno(String matriculaPrm) throws Exception {
        List lista = getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorMatriculaMatricula(matriculaPrm, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        return lista;
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

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public MatriculaVO getAlunoVO() {
        if (alunoVO == null) {
            alunoVO = new MatriculaVO();
        }
        return alunoVO;
    }

    public void setAlunoVO(MatriculaVO alunoVO) {
        this.alunoVO = alunoVO;
    }

    public NegociacaoContaReceberVO getNegociacaoContaReceberVO() {
        if (negociacaoContaReceberVO == null) {
            negociacaoContaReceberVO = new NegociacaoContaReceberVO();
        }
        return negociacaoContaReceberVO;
    }

    public void setNegociacaoContaReceberVO(NegociacaoContaReceberVO negociacaoContaReceberVO) {
        this.negociacaoContaReceberVO = negociacaoContaReceberVO;
    }

    public String getObservacaoHistorico() {
        if (observacaoHistorico == null) {
            observacaoHistorico = "";
        }
        return observacaoHistorico;
    }

    public void setObservacaoHistorico(String observacaoHistorico) {
        this.observacaoHistorico = observacaoHistorico;
    }

    /**
     * @return the listaSelectItemRenegociacaoContaReceber
     */
    public List getListaSelectItemRenegociacaoContaReceber() {
        if (listaSelectItemRenegociacaoContaReceber == null) {
            listaSelectItemRenegociacaoContaReceber = new ArrayList();
        }
        return listaSelectItemRenegociacaoContaReceber;
    }

    /**
     * @param listaSelectItemRenegociacaoContaReceber the listaSelectItemRenegociacaoContaReceber to set
     */
    public void setListaSelectItemRenegociacaoContaReceber(List listaSelectItemRenegociacaoContaReceber) {
        this.listaSelectItemRenegociacaoContaReceber = listaSelectItemRenegociacaoContaReceber;
    }
    
    public void consultarResponsavelFinanceiro() {
        try {

            if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
            getListaConsultaResponsavelFinanceiro().clear();
            if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }

            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
        if (tipoConsultaComboResponsavelFinanceiro == null) {
            tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
        }
        return tipoConsultaComboResponsavelFinanceiro;
    }
    
    public void selecionarResponsavelFinanceiro() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
            getListaConsultaResponsavelFinanceiro().clear();
            if (getTipoRelatorio().equals("RENEGOCIACAO")) {
            	this.getNegociacaoContaReceberVO().setPessoa(obj);   
            	montarListaSelectItemRenegociacaoContaReceber(getAlunoVO().getMatricula());
            } else {
            	getContaReceberVO().setResponsavelFinanceiro(obj);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public List getListaTipoLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("TermoReconhecimentoDividaRel", "Layout 1"));
		itens.add(new SelectItem("TermoReconhecimentoDividaLayout3Rel", "Layout 3"));
		itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		return itens;
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

    public String getValorConsultaResponsavelFinanceiro() {
        if (valorConsultaResponsavelFinanceiro == null) {
            valorConsultaResponsavelFinanceiro = "";
        }
        return valorConsultaResponsavelFinanceiro;
    }

    public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
        this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
    }

    public String getCampoConsultaResponsavelFinanceiro() {
        if (campoConsultaResponsavelFinanceiro == null) {
            campoConsultaResponsavelFinanceiro = "";
        }
        return campoConsultaResponsavelFinanceiro;
    }

    public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
        this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
    }
    
    public void limparDadosResponsavelFinanceiro(){
        getNegociacaoContaReceberVO().getPessoa().setCodigo(0);
        getNegociacaoContaReceberVO().getPessoa().setNome("");
    }

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "RENEGOCIACAO";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String getTipoContaReceber() {
		if (tipoContaReceber == null) {
			tipoContaReceber = "TODAS";
		}
		return tipoContaReceber;
	}

	public void setTipoContaReceber(String tipoContaReceber) {
		this.tipoContaReceber = tipoContaReceber;
	}
	
	public boolean getApresentarDadosRenegociacao() {
		return getTipoRelatorio().equals("RENEGOCIACAO");
	}
	
	public boolean getApresentarComboBoxRenegociacao() {
		return getTipoRelatorio().equals("RENEGOCIACAO") && (!getAlunoVO().getAluno().getNome().equals("") || !getNegociacaoContaReceberVO().getPessoa().getNome().equals(""));
	}
	
	public boolean getApresentarDadosTipoContaReceber() {
		return getTipoRelatorio().equals("CONTA_RECEBER");
	}

	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
			contaReceberVO.setTipoPessoa("AL");
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public List getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoPessoa(List<SelectItem> listaSelectItemTipoPessoa) {
		this.listaSelectItemTipoPessoa = listaSelectItemTipoPessoa;
	}

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}
	
	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}

	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = Boolean.TRUE;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			filtroRelatorioFinanceiroVO.realizarMarcarTodasOrigens();
		} else {
			filtroRelatorioFinanceiroVO.realizarDesmarcarTodasOrigens();
		}

	}
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public String getObservacaoComplementar() {
		if (observacaoComplementar == null) {
			observacaoComplementar = "";
		}
		return observacaoComplementar;
	}

	public void setObservacaoComplementar(String observacaoComplementar) {
		this.observacaoComplementar = observacaoComplementar;
	}
}
