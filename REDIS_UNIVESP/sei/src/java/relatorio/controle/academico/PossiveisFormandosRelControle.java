package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.PossiveisFormandosRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.PossiveisFormandosRel;


@Controller("PossiveisFormandosRelControle")
@Scope("viewScope")
@Lazy
public class PossiveisFormandosRelControle extends SuperControleRelatorio {

      
    /**
	 * 
	 */
	private static final long serialVersionUID = 1598805857420187892L;
	private TurmaVO turmaVO;    
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List<TurmaVO> listaConsultaTurma;    
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private String campoFiltroPor;
    private List<SelectItem> listaSelectItemUnidadeEnsino;    
    private List<SelectItem> listaSelectItemPeriodicidade;
    private List<SelectItem> listaSelectItemLayout;
    private PeriodicidadeEnum periodicidade;
    private String ano;
    private String semestre;
    private String layout;
    private String unidadeEnsinoApresentar;
    

    public PossiveisFormandosRelControle() throws Exception {        
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<PossiveisFormandosRelVO> listaObjetos = new ArrayList<PossiveisFormandosRelVO>(0);
        try {
        	List<UnidadeEnsinoVO> unidades = getUnidadeEnsinoVOs().stream().filter(u -> u.getFiltrarUnidadeEnsino()).collect(Collectors.toList());
            registrarAtividadeUsuario(getUsuarioLogado(), "PossiveisFormandosRelControle", "Inicializando Geração de Relatório Possíveis Formandos", "Emitindo Relatório");            
            getFacadeFactory().getPossiveisFormandosRelFacade().validarDados(unidades, getPeriodicidade(), getAno(), getSemestre(),  getTurmaVO(), getCampoFiltroPor());
            
            listaObjetos = getFacadeFactory().getPossiveisFormandosRelFacade().criarObjeto(unidades, getPeriodicidade(), getAno(), getSemestre(), getCampoFiltroPor(), getTurmaVO(), getCursoVOs(), getTurnoVOs());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(PossiveisFormandosRel.getDesignIReportRelatorio(getLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(PossiveisFormandosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Possíveis Formandos");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(PossiveisFormandosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()).getNome());
                getSuperParametroRelVO().setCurso(getCursosApresentar());
                getSuperParametroRelVO().setTurno(getTurnosApresentar());
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                getSuperParametroRelVO().adicionarParametro("consultarPor", getCampoFiltroPor());
                getSuperParametroRelVO().adicionarParametro("periodicidade", getPeriodicidade().getValor());
                getSuperParametroRelVO().setAno(getAno());
                getSuperParametroRelVO().setSemestre(getSemestre());
                getSuperParametroRelVO().adicionarParametro("unidadeEnsinoFiltro", getUnidadeEnsinoApresentar());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLayout(),PossiveisFormandosRel.getIdEntidade(), "layout", getUsuarioLogado());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPeriodicidade().name(),PossiveisFormandosRel.getIdEntidade(), "periodicidade", getUsuarioLogado());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(),PossiveisFormandosRel.getIdEntidade(), "ano", getUsuarioLogado());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(),PossiveisFormandosRel.getIdEntidade(), "semestre", getUsuarioLogado());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCampoFiltroPor(),PossiveisFormandosRel.getIdEntidade(), "campoFiltroPor", getUsuarioLogado());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getUnidadeEnsinoVO().getCodigo().toString(),PossiveisFormandosRel.getIdEntidade(), "unidadeEnsino", getUsuarioLogado());
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio();
//                setUnidadesEnsino("");
                setCursosApresentar("");
                setTurnosApresentar("");
                removerObjetoMemoria(this);
//                montarListaSelectItemUnidadeEnsino();
                consultarUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "PossiveisFormandosRelControle", "Finalizando Geração de Relatório Possíveis Formandos", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    
    public void limparDadosCursoTurma() throws Exception {
        setTurmaVO(new TurmaVO());                              
        getListaConsultaTurma().clear();               
        if (getCampoFiltroPor().equals("unidadeEnsino") && (getIsExisteUnidadeEnsino() || getUnidadeEnsinoVO().getCodigo() != 0)) {
            montarListaTurnoPorUnidadeEnsino();
        }
    }

    public void limparIdentificador() {
        setTurmaVO(new TurmaVO());
        getListaConsultaTurma().clear();
    }

    
    public List<SelectItem> tipoConsultaComboFiltroPor;
    public List<SelectItem> getTipoConsultaComboFiltroPor() {
    	if(tipoConsultaComboFiltroPor == null){
    		tipoConsultaComboFiltroPor = new ArrayList<SelectItem>(0);    		
    		tipoConsultaComboFiltroPor.add(new SelectItem("curso", "Curso"));
    		tipoConsultaComboFiltroPor.add(new SelectItem("turma", "Turma"));
    	}
        return tipoConsultaComboFiltroPor;
    }  

    public void consultarCurso() {
        try {
//        	getUnidadeEnsinoVOs().clear();
//        	getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
//        	getUnidadeEnsinoVOs().add(getUnidadeEnsinoVO());        	
        	List<CursoVO> resultadoConsulta = getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", getPeriodicidade().getValor(), null, getUnidadeEnsinoVOs(), getUsuarioLogado());
        	for(CursoVO cursoVO: getCursoVOs()){
        		for(CursoVO c: resultadoConsulta){
        			if(c.getCodigo().equals(cursoVO.getCodigo())){
        				c.setCursoSelecionado(cursoVO.getCursoSelecionado());
        			}
        		}        	
        	}
        	setCursoVOs(resultadoConsulta);
        } catch (Exception e) {            
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }   
    
    public List<SelectItem> tipoConsultaComboTurma;
	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null){
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
        return tipoConsultaComboTurma;
    }

	public void consultarTurma() {
        try {
        	List<UnidadeEnsinoVO> lista = getUnidadeEnsinoVOs().stream().filter(uni -> uni.getFiltrarUnidadeEnsino()).collect(Collectors.toList());
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaResumidaPorIdentificador(getControleConsulta(), getControleConsultaOtimizado(), lista, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
            setListaConsultaTurma(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
    	getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
    	getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
    	consultarTurma();
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
  //      obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
        setTurmaVO(obj);
        setPeriodicidade(obj.getSemestral()?PeriodicidadeEnum.SEMESTRAL:obj.getAnual()?PeriodicidadeEnum.ANUAL:PeriodicidadeEnum.INTEGRAL);
 //     setUnidadesEnsinoApresentar(obj.getUnidadeEnsino().getNome());
        setCursosApresentar(obj.getCurso().getNome());
        setTurnosApresentar(obj.getTurno().getNome());        
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        setListaConsultaTurma(new ArrayList<>());
    }

	public List<SelectItem> tipoConsultaComboSemestre;

	public List<SelectItem> getTipoConsultaComboSemestre() {
		if (tipoConsultaComboSemestre == null) {
			tipoConsultaComboSemestre = new ArrayList<SelectItem>(0);
			tipoConsultaComboSemestre.add(new SelectItem("", ""));
			tipoConsultaComboSemestre.add(new SelectItem("1", "1º"));
			tipoConsultaComboSemestre.add(new SelectItem("2", "2º"));
		}
		return tipoConsultaComboSemestre;
	}

//	@PostConstruct
//    public void montarListaSelectItemUnidadeEnsino() {
//        try {                                     	
//
//        	setLayout(getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(PossiveisFormandosRel.getIdEntidade(), "layout", false, getUsuarioLogado()).getValor());
//        	String periodi = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(PossiveisFormandosRel.getIdEntidade(), "periodicidade", false, getUsuarioLogado()).getValor();
//        	if(!periodi.trim().isEmpty()){
//        		setPeriodicidade(PeriodicidadeEnum.valueOf(periodi));
//        	}        
//        	setAno(getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(PossiveisFormandosRel.getIdEntidade(), "ano", false, getUsuarioLogado()).getValor());
//        	setSemestre(getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(PossiveisFormandosRel.getIdEntidade(), "semestre", false, getUsuarioLogado()).getValor());
//        	setCampoFiltroPor(getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(PossiveisFormandosRel.getIdEntidade(), "campoFiltroPor", false, getUsuarioLogado()).getValor());
//        	String codUnidadeEnsino = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(PossiveisFormandosRel.getIdEntidade(), "unidadeEnsino", false, getUsuarioLogado()).getValor();        	
//            montarListaSelectItemUnidadeEnsino("");   
//            for(SelectItem item: getListaSelectItemUnidadeEnsino()){
//            	if(item.getValue().toString().equals(codUnidadeEnsino)){
//            		getUnidadeEnsinoVO().setCodigo(Integer.valueOf(codUnidadeEnsino));
//            	}
//            }
//            inicializarListaCursoETurno();
//            setMensagemID("");
//        } catch (Exception e) {
//           // //System.out.println("MENSAGEM => " + e.getMessage());;
//        }
//    }
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
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
	
	public void inicializarListaCursoETurno(){
		setTurmaVO(null);
		consultarCurso();
		montarListaTurnoPorUnidadeEnsino();
	}

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
    	List<UnidadeEnsinoVO> resultadoConsulta = null;
        try {
        	resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            getListaSelectItemUnidadeEnsino().clear();            
            for(UnidadeEnsinoVO obj: resultadoConsulta) {                
            	getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
            	if(getUnidadeEnsinoVO().getCodigo().equals(0)){
            		getUnidadeEnsinoVO().setCodigo(obj.getCodigo());
            	}
            }            
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            
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

    public void montarListaTurnoPorUnidadeEnsino() {    	
    	try{
    		getUnidadeEnsinoVOs().clear();
    		getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
    		getUnidadeEnsinoVOs().add(getUnidadeEnsinoVO());
    		setTurnoVOs(getFacadeFactory().getTurnoFacade().consultarTurnoUsadoMatricula(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX));
    	}catch(Exception e){
    		setMensagemID("msg_erro", e.getMessage());
    	}
    	
    }

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public String getCampoFiltroPor() {
        if (campoFiltroPor == null) {
            campoFiltroPor = "curso";
        }
        return campoFiltroPor;
    }

    public void setCampoFiltroPor(String campoFiltroPor) {
        this.campoFiltroPor = campoFiltroPor;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
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
            semestre = Uteis.getSemestreAtual();
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Boolean getIsFiltrarPorturma() {
        if (getCampoFiltroPor().equals("turma")) {
            return true;
        }
        return false;
    }
    
    public Boolean getIsFiltrarPorUnidadeEnsino() {
        if (getCampoFiltroPor().equals("unidadeEnsino")) {
            return true;
        }
        return false;
    }

    public Boolean getIsFiltrarPorCurso() {
        if (getCampoFiltroPor().equals("curso")) {
            return true;
        }
        return false;
    }
    

    public Boolean getIsFiltrarPorAno() {
        return !getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL);
    }

    public Boolean getIsFiltrarPorSemestre() {        
        return getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
    }

 

	/**
	 * @return the periodicidade
	 */
	public PeriodicidadeEnum getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}

	/**
	 * @return the listaSelectItemPeriodicidade
	 */
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>();
			for(PeriodicidadeEnum periodicidadeEnum: PeriodicidadeEnum.values()){
				listaSelectItemPeriodicidade.add(new SelectItem(periodicidadeEnum, periodicidadeEnum.getDescricao()));
			}			 
		}
		return listaSelectItemPeriodicidade;
	}

	/**
	 * @param listaSelectItemPeriodicidade the listaSelectItemPeriodicidade to set
	 */
	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	/**
	 * @return the layout
	 */
	public String getLayout() {
		if (layout == null) {
			layout = "PossiveisFormandosRel";
		}
		return layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return the listaSelectItemLayout
	 */
	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>();
			listaSelectItemLayout.add(new SelectItem("PossiveisFormandosRel", "Layout 1 - Alunos e Curso"));
			listaSelectItemLayout.add(new SelectItem("PossiveisFormandosLayout2Rel", "Layout 2 - Alunos, Contatos e Curso"));
		}
		return listaSelectItemLayout;
	}

	/**
	 * @param listaSelectItemLayout the listaSelectItemLayout to set
	 */
	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}
	
	public void limparDadosUnidadeEnsinoTurma() throws Exception {
        setTurmaVO(new TurmaVO());                              
        getListaConsultaTurma().clear();               
        if (getCampoFiltroPor().equals("unidadeEnsino") && (getIsExisteUnidadeEnsino() || getUnidadeEnsinoVO().getCodigo() != 0)) {
            montarListaTurnoPorUnidadeEnsino();
        }
    }
    
    public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
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
		consultarCursoFiltroRelatorio("");
		consultarTurnoFiltroRelatorio();
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			getListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	


	
}