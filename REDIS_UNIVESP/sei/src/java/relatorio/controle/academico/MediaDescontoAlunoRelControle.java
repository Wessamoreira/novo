package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.MediaDescontoAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.MediaDescontoAlunoRel;

@SuppressWarnings("unchecked")
@Controller("MediaDescontoAlunoRelControle")
@Scope("viewScope")
@Lazy
public class MediaDescontoAlunoRelControle extends SuperControleRelatorio {

    protected List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO;
    private List listaDeclaracao;

    public MediaDescontoAlunoRelControle() {        
        setMensagemID("msg_entre_prmrelatorio");
    }

//    public void imprimirPDF() {
//        String titulo = null;
//        String nomeEmpresa = null;
//        String design = null;
//        List<MediaDescontoAlunoRelVO> listaMediaDescontosAlunoRelVO = null;
//        try {
//            getFacadeFactory().getMediaDescontoAlunoRelFacade().setDescricaoFiltros("");
//            titulo = "Relatório da Média dos descontos do Aluno";
//            nomeEmpresa = super.getUnidadeEnsinoLogado().getNome();
//            design = MediaDescontoAlunoRel.getDesignIReportRelatorio();
//
//            if (getMediaDescontoAlunoRelVO().getNomeAluno() != null) {
//                listaMediaDescontosAlunoRelVO = getFacadeFactory().getMediaDescontoAlunoRelFacade().criarObjeto(getMediaDescontoAlunoRelVO(), getUsuarioLogado());
//                apresentarRelatorioObjetos(MediaDescontoAlunoRel.getIdEntidade(), titulo, nomeEmpresa, "", "PDF", "", design, getUsuarioLogado().getNome(), getFacadeFactory().getMediaDescontoAlunoRelFacade().getDescricaoFiltros(),
//                        listaMediaDescontosAlunoRelVO, MediaDescontoAlunoRel.getCaminhoBaseRelatorio());
//                removerObjetoMemoria(this);
//                setMensagemID("msg_relatorio_ok");
//            }
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        } finally {
//            titulo = null;
//            nomeEmpresa = null;
//            design = null;
//            Uteis.liberarListaMemoria(listaMediaDescontosAlunoRelVO);
//        }
//
//    }
    
    public void imprimirPDF() {
        String titulo = null;
        String nomeEmpresa = null;
        String design = null;
        List<MediaDescontoAlunoRelVO> listaMediaDescontosAlunoRelVO = null;
        try {
        	MediaDescontoAlunoRel.validarDados(getMediaDescontoAlunoRelVO());
            getFacadeFactory().getMediaDescontoAlunoRelFacade().setDescricaoFiltros("");
            nomeEmpresa = super.getUnidadeEnsinoLogado().getNome();
            if (getMediaDescontoAlunoRelVO().getNomeAluno() != null) {
                listaMediaDescontosAlunoRelVO = getFacadeFactory().getMediaDescontoAlunoRelFacade().criarObjeto(getMediaDescontoAlunoRelVO(), getUsuarioLogado());
                if(!listaMediaDescontosAlunoRelVO.isEmpty()){
                	getSuperParametroRelVO().setNomeDesignIreport(MediaDescontoAlunoRel.getDesignIReportRelatorio());
    				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
    				getSuperParametroRelVO().setSubReport_Dir(MediaDescontoAlunoRel.getCaminhoBaseRelatorio());
    				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
    				getSuperParametroRelVO().setTituloRelatorio("Relatório da média dos descontos por Aluno");
    				getSuperParametroRelVO().setListaObjetos(listaMediaDescontosAlunoRelVO);
    				getSuperParametroRelVO().setCaminhoBaseRelatorio(MediaDescontoAlunoRel.getCaminhoBaseRelatorio());
    				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
    				realizarImpressaoRelatorio();
                    removerObjetoMemoria(this);
                    setMensagemID("msg_relatorio_ok");	
                }else{
                	setMensagemID("msg_relatorio_sem_dados");	
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEmpresa = null;
            design = null;
            Uteis.liberarListaMemoria(listaMediaDescontosAlunoRelVO);
        }

    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
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
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        MatriculaVO objAluno = null;
        try {
            objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMediaDescontoAlunoRelVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMediaDescontoAlunoRelVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            getMediaDescontoAlunoRelVO().setMatricula(objAluno.getMatricula());
            getMediaDescontoAlunoRelVO().setNomeAluno(objAluno.getAluno().getNome());
            setMediaDescontoAlunoRelVO(getFacadeFactory().getMediaDescontoAlunoRelFacade().consultarPorCodigoAluno(objAluno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaDeclaracao().isEmpty()) {
                getListaDeclaracao().add(getMediaDescontoAlunoRelVO());
            } else {
                getListaDeclaracao().set(0, getMediaDescontoAlunoRelVO());
            }
            setCampoConsultaAluno(null);
            setValorConsultaAluno(null);
            getListaConsultaAluno().clear();

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }finally{
            removerObjetoMemoria(objAluno);
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        getMediaDescontoAlunoRelVO().setMatricula(obj.getMatricula());
        getMediaDescontoAlunoRelVO().setNomeAluno(obj.getAluno().getNome());
        setMediaDescontoAlunoRelVO(getFacadeFactory().getMediaDescontoAlunoRelFacade().consultarPorCodigoAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        if (getListaDeclaracao().isEmpty()) {
            getListaDeclaracao().add(getMediaDescontoAlunoRelVO());
        } else {
            getListaDeclaracao().set(0, getMediaDescontoAlunoRelVO());
        }
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));

    }

    public void limparDadosAluno() {
        getMediaDescontoAlunoRelVO().setMatricula("");
        getMediaDescontoAlunoRelVO().setNomeAluno("");
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
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
    		listaConsultaAluno = new ArrayList<>();
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

    public MediaDescontoAlunoRelVO getMediaDescontoAlunoRelVO() {
        if (mediaDescontoAlunoRelVO == null) {
            mediaDescontoAlunoRelVO = new MediaDescontoAlunoRelVO();
        }
        return mediaDescontoAlunoRelVO;
    }

    public void setMediaDescontoAlunoRelVO(MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO) {
        this.mediaDescontoAlunoRelVO = mediaDescontoAlunoRelVO;
    }

//	/**
//	 * @param MediaDescontoAlunoVO
//	 *            the MediaDescontoAlunoVO to set
//	 */
//	public void setMediaDescontoAlunoVO(MediaDescontoAlunoVO mediaDescontoAlunoVO) {
//		this.mediaDescontoAlunoVO = mediaDescontoAlunoVO;
//	}
//
//	/**
//	 * @return the MediaDescontoAlunoVO
//	 */
//	public MediaDescontoAlunoVO getMediaDescontoAlunoVO() {
//		return mediaDescontoAlunoVO;
//	}
    /**
     * @param listaDeclaracao
     *            the listaDeclaracao to set
     */
    public void setListaDeclaracao(List listaDeclaracao) {
        this.listaDeclaracao = listaDeclaracao;
    }

    /**
     * @return the listaDeclaracao
     */
    public List getListaDeclaracao() {
        if(listaDeclaracao == null){
            listaDeclaracao = new ArrayList(0);
        }
        return listaDeclaracao;
    }
}
