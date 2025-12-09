package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TiposRequerimentoRelatorio;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.OcorrenciasAlunosVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.OcorrenciasAlunosRel;


@Controller("OcorrenciasAlunosRelControle")
@Scope("viewScope")
@Lazy
public class OcorrenciasAlunosRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2539863207495571253L;
	private OcorrenciasAlunosVO ocorrenciasAlunosVO;
    private List<SelectItem> listaSelectItemTipoOcorrencia;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
    private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;
	private List<SelectItem> listaSelectItemOrdenarPor;
	private List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento;
	private String ordenarPor;
	
    public OcorrenciasAlunosRelControle() throws Exception {

        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemTipoOcorrencia();
        montarListaSelectItemMotivoCancelamentoTrancamento();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List listaObjetos = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "OcorrenciasAlunosRelControle", "Inicializando Geração de Relatório Ocorrências Alunos", "Emitindo Relatório");
//            OcorrenciasAlunosRel.validarDados(getOcorrenciasAlunosVO());
            getFacadeFactory().getOcorrenciasAlunosRelFacade().setDescricaoFiltros("");
            if(getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo() != null && getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo() != 0) {
                getOcorrenciasAlunosVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }else {
                getOcorrenciasAlunosVO().getUnidadeEnsino().setNome("Todas Unidades");
            }
            getFacadeFactory().getOcorrenciasAlunosRelFacade().validarDados(getFiltroRelatorioAcademico());
            listaObjetos = getFacadeFactory().getOcorrenciasAlunosRelFacade().criarObjeto(getFiltroRelatorioAcademico(), getOcorrenciasAlunosVO(), getOcorrenciasAlunosVO().getTipoOcorrencia(), getOrdenarPor());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(OcorrenciasAlunosRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(OcorrenciasAlunosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("OCORRÊNCIAS COM ALUNOS");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(OcorrenciasAlunosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().adicionarParametro("filtrarPorAnoSemestre", getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre());
                if(getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre()){
                	getSuperParametroRelVO().adicionarParametro("anoSemestre", getFiltroRelatorioAcademico().getAno()+"/0"+getFiltroRelatorioAcademico().getSemestre());                	
                }
                getSuperParametroRelVO().adicionarParametro("filtrarPorPeriodoData", getFiltroRelatorioAcademico().getFiltrarPorPeriodoData());
                if(getFiltroRelatorioAcademico().getFiltrarPorPeriodoData()){
                	getSuperParametroRelVO().adicionarParametro("dataInicio", Uteis.getData(getFiltroRelatorioAcademico().getDataInicio()));
                	getSuperParametroRelVO().adicionarParametro("dataTermino", Uteis.getData(getFiltroRelatorioAcademico().getDataTermino()));
                }
		if (!getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo().equals(0)) {
		    getOcorrenciasAlunosVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		    getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getOcorrenciasAlunosVO().getUnidadeEnsino());
		}
		 if(Uteis.isAtributoPreenchido(getOcorrenciasAlunosVO().getMotivoCancelamentoTrancamentoVO())
         		&& (getOcorrenciasAlunosVO().getTipoOcorrencia().equals("TD") || getOcorrenciasAlunosVO().getTipoOcorrencia().equals("CA")
         		 || getOcorrenciasAlunosVO().getTipoOcorrencia().equals("AC") || getOcorrenciasAlunosVO().getTipoOcorrencia().equals("TR"))){
         	getOcorrenciasAlunosVO().setMotivoCancelamentoTrancamentoVO(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(getOcorrenciasAlunosVO().getMotivoCancelamentoTrancamentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
         	getSuperParametroRelVO().adicionarParametro("motivoCancelamentoTrancamento",getOcorrenciasAlunosVO().getMotivoCancelamentoTrancamentoVO().getNome());
         }else{
         	getSuperParametroRelVO().adicionarParametro("motivoCancelamentoTrancamento", "");
         }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                montarListaSelectItemTipoOcorrencia();
                montarListaSelectItemMotivoCancelamentoTrancamento();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "OcorrenciasAlunosRelControle", "Finalizando Geração de Relatório Ocorrências Alunos", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void imprimirExcel() {
        List listaObjetos = new ArrayList(0);
        try {
        	registrarAtividadeUsuario(getUsuarioLogado(), "OcorrenciasAlunosRelControle", "Inicializando Geração de Relatório Ocorrências Alunos", "Emitindo Relatório");
//            OcorrenciasAlunosRel.validarDados(getOcorrenciasAlunosVO());
            getFacadeFactory().getOcorrenciasAlunosRelFacade().setDescricaoFiltros("");
            if(getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo() != null && getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo() != 0) {
                getOcorrenciasAlunosVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }else {
                getOcorrenciasAlunosVO().getUnidadeEnsino().setNome("Todas Unidades");
            }
            
            listaObjetos = getFacadeFactory().getOcorrenciasAlunosRelFacade().criarObjeto(getFiltroRelatorioAcademico(), getOcorrenciasAlunosVO(), getOcorrenciasAlunosVO().getTipoOcorrencia(), getOrdenarPor());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(OcorrenciasAlunosRel.getDesignIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(OcorrenciasAlunosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("OCORRÊNCIAS COM ALUNOS");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(OcorrenciasAlunosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().adicionarParametro("filtrarPorAnoSemestre", getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre());
                if(getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre()){
                	getSuperParametroRelVO().adicionarParametro("anoSemestre", getFiltroRelatorioAcademico().getAno()+"/0"+getFiltroRelatorioAcademico().getSemestre());                	
                }
                getSuperParametroRelVO().adicionarParametro("filtrarPorPeriodoData", getFiltroRelatorioAcademico().getFiltrarPorPeriodoData());
                if(getFiltroRelatorioAcademico().getFiltrarPorPeriodoData()){
                	getSuperParametroRelVO().adicionarParametro("dataInicio", Uteis.getData(getFiltroRelatorioAcademico().getDataInicio()));
                	getSuperParametroRelVO().adicionarParametro("dataTermino", Uteis.getData(getFiltroRelatorioAcademico().getDataTermino()));
                }                
                if (!getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo().equals(0)) {
                	getOcorrenciasAlunosVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getOcorrenciasAlunosVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getOcorrenciasAlunosVO().getUnidadeEnsino());
				}
                if(Uteis.isAtributoPreenchido(getOcorrenciasAlunosVO().getMotivoCancelamentoTrancamentoVO())
                		&& (getOcorrenciasAlunosVO().getTipoOcorrencia().equals("TD") || getOcorrenciasAlunosVO().getTipoOcorrencia().equals("CA")
                		 || getOcorrenciasAlunosVO().getTipoOcorrencia().equals("AC") || getOcorrenciasAlunosVO().getTipoOcorrencia().equals("TR"))){
                	getOcorrenciasAlunosVO().setMotivoCancelamentoTrancamentoVO(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(getOcorrenciasAlunosVO().getMotivoCancelamentoTrancamentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                	getSuperParametroRelVO().adicionarParametro("motivoCancelamentoTrancamento",getOcorrenciasAlunosVO().getMotivoCancelamentoTrancamentoVO().getNome());
                }else{
                	getSuperParametroRelVO().adicionarParametro("motivoCancelamentoTrancamento", "");
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                montarListaSelectItemTipoOcorrencia();
                montarListaSelectItemMotivoCancelamentoTrancamento();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "OcorrenciasAlunosRelControle", "Finalizando Geração de Relatório Ocorrências Alunos", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void montarListaSelectItemTipoOcorrencia() throws Exception {
        setListaSelectItemTipoOcorrencia(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TiposRequerimentoRelatorio.class, false));
        getListaSelectItemTipoOcorrencia().add(0, new SelectItem("TD", "Todos"));
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public OcorrenciasAlunosVO getOcorrenciasAlunosVO() {
        if (ocorrenciasAlunosVO == null) {
            ocorrenciasAlunosVO = new OcorrenciasAlunosVO();
        }
        return ocorrenciasAlunosVO;
    }

    public void setOcorrenciasAlunosVO(OcorrenciasAlunosVO ocorrenciasAlunosVO) {
        this.ocorrenciasAlunosVO = ocorrenciasAlunosVO;
    }

    public List<SelectItem> getListaSelectItemTipoOcorrencia() {
        if (listaSelectItemTipoOcorrencia == null) {
            listaSelectItemTipoOcorrencia = new ArrayList<SelectItem>();
        }
        return listaSelectItemTipoOcorrencia;
    }

    public void setListaSelectItemTipoOcorrencia(List<SelectItem> listaSelectItemTipoOcorrencia) {
        this.listaSelectItemTipoOcorrencia = listaSelectItemTipoOcorrencia;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }
    
    public List<SelectItem> getListaSelectItemTipoFiltroPeriodoAcademico(){
  		return TipoFiltroPeriodoAcademicoEnum.getListaSelectItem();				
  	}

  	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademico() {
  		if(filtroRelatorioAcademico == null){
  			filtroRelatorioAcademico = new FiltroRelatorioAcademicoVO();
  		}
  		return filtroRelatorioAcademico;
  	}

  	public void setFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademico) {
  		this.filtroRelatorioAcademico = filtroRelatorioAcademico;
  	}
  	
  	public List<SelectItem> getListaSelectItemSemestre() {
  		if(listaSelectItemSemestre == null){
  			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
  			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
  			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
  		}
  		return listaSelectItemSemestre;
  	}

  	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
  		this.listaSelectItemSemestre = listaSelectItemSemestre;
  	}

  	public List<SelectItem> getListaSelectItemAno() {
  		try {
  			if(listaSelectItemAno == null){
  				listaSelectItemAno = new ArrayList<SelectItem>(0);
  				List<String> anos = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnosMatriculaPeriodo();
  				for(String ano:anos){
  					listaSelectItemAno.add(new SelectItem(ano, ano));
  				}
  			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
  		return listaSelectItemAno;
  	}

  	public List<SelectItem> getListaSelectItemOrdenarPor() {
  		listaSelectItemOrdenarPor = new ArrayList<SelectItem>(0);
		listaSelectItemOrdenarPor.add(new SelectItem("data", "Data Ocorrência"));
		listaSelectItemOrdenarPor.add(new SelectItem("aluno", "Aluno"));
		listaSelectItemOrdenarPor.add(new SelectItem("turma", "Turma"));
  		return listaSelectItemOrdenarPor;
  	}
  	
  	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
  		this.listaSelectItemAno = listaSelectItemAno;
  	}
  	
  	
  	public void montarListaSelectItemMotivoCancelamentoTrancamento() {
		try {
			montarListaSelectItemMotivoCancelamentoTrancamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}  	  

	public void montarListaSelectItemMotivoCancelamentoTrancamento(String prm) throws Exception {
		try {
			List<MotivoCancelamentoTrancamentoVO> resultadoConsulta = consultarMotivoCancelamentoTrancamentoPorNomeAtivo(prm);
			setListaSelectItemMotivoCancelamentoTrancamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			throw e;
		}
	}

	public List<MotivoCancelamentoTrancamentoVO> consultarMotivoCancelamentoTrancamentoPorNomeAtivo(String nomePrm) throws Exception {
		return getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNomeAtivo(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}
	
	/**
	 * @return the listaSelectItemMotivoCancelamentoTrancamento
	 */
	public List<SelectItem> getListaSelectItemMotivoCancelamentoTrancamento() {
		if (listaSelectItemMotivoCancelamentoTrancamento == null) {
			listaSelectItemMotivoCancelamentoTrancamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemMotivoCancelamentoTrancamento;
	}

	/**
	 * @param listaSelectItemMotivoCancelamentoTrancamento
	 *            the listaSelectItemMotivoCancelamentoTrancamento to set
	 */
	public void setListaSelectItemMotivoCancelamentoTrancamento(List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento) {
		this.listaSelectItemMotivoCancelamentoTrancamento = listaSelectItemMotivoCancelamentoTrancamento;
	}

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "data";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}
}
