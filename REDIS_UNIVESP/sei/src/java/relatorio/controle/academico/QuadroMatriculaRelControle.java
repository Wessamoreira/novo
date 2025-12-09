package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.QuadroMatriculaRelVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.QuadroMatriculaRel;

@Controller("QuadroMatriculaRelControle")
@Scope("request")
@Lazy
public class QuadroMatriculaRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2404807958801510945L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
    protected List<SelectItem> listaUnidadeEnsino;
    protected boolean mostrarAnoSemestre;
    protected List<QuadroMatriculaRelVO> listaQuadroMatriculaRelVO;
    private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
    private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;

    public QuadroMatriculaRelControle() throws Exception {
        montarListaSelectItemUnidadeEnsino("");        
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<QuadroMatriculaRelVO> listaObjetos = new ArrayList<QuadroMatriculaRelVO>(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "QuadroMatriculaRelControle", "Inicializando Geração de Relatório Quadro Matrícula", "Emitindo Relatório");            
            getFacadeFactory().getQuadroMatriculaRelFacade().setDescricaoFiltros("");
            
            listaObjetos = getFacadeFactory().getQuadroMatriculaRelFacade().criarObjeto(getFiltroRelatorioAcademico(), getUnidadeEnsinoVO(), getListaQuadroMatriculaRelVO());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(QuadroMatriculaRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(QuadroMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Quadro de Matrícula");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(QuadroMatriculaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino("");
                
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "QuadroMatriculaRelControle", "Finalizando Geração de Relatório Quadro Matrícula", "Emitindo Relatório");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    

     public void montarListaSelectItemUnidadeEnsino(String prm) {
        List<UnidadeEnsinoVO> resultado = null;
        List<SelectItem> objs = null;
        try {
            objs = new ArrayList<SelectItem>(0);
            resultado = consultarUnidadeEnsinoPorNome(prm);
            objs.add(new SelectItem(0, ""));
            for (UnidadeEnsinoVO unidadeEnsino : resultado) {
                objs.add(new SelectItem(unidadeEnsino.getCodigo(), unidadeEnsino.getNome().toString()));
            }
            setListaUnidadeEnsino(objs);
        } catch (Exception e) {
            setListaUnidadeEnsino(new ArrayList<SelectItem>(0));            
        }finally{
            Uteis.liberarListaMemoria(resultado);
            
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

   
    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO
     *            the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the listaUnidadeEnsino
     */
    public List<SelectItem> getListaUnidadeEnsino() {
        if (listaUnidadeEnsino == null) {
            listaUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaUnidadeEnsino;
    }

    /**
     * @param listaUnidadeEnsino
     *            the listaUnidadeEnsino to set
     */
    public void setListaUnidadeEnsino(List<SelectItem> listaUnidadeEnsino) {
        this.listaUnidadeEnsino = listaUnidadeEnsino;
    }

  
    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#getListaQuadroMatriculaRelVO()
     */

    public List<QuadroMatriculaRelVO> getListaQuadroMatriculaRelVO() {
        if (listaQuadroMatriculaRelVO == null) {
            listaQuadroMatriculaRelVO = new ArrayList<QuadroMatriculaRelVO>(0);
        }
        return listaQuadroMatriculaRelVO;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#setListaQuadroMatriculaRelVO(java.util.List)
     */
    public void setListaQuadroMatriculaRelVO(List<QuadroMatriculaRelVO> listaQuadroMatriculaRelVO) {
        this.listaQuadroMatriculaRelVO = listaQuadroMatriculaRelVO;
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

  	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
  		this.listaSelectItemAno = listaSelectItemAno;
  	}
}
