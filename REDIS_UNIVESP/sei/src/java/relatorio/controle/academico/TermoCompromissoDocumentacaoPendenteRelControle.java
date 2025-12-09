package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.TermoCompromissoDocumentacaoPendenteRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoAbandonoCursoRel;
import relatorio.negocio.jdbc.academico.DeclaracaoSetranspRel;
import relatorio.negocio.jdbc.academico.TermoCompromissoDocumentacaoPendenteRel;

@SuppressWarnings("unchecked")
@Controller("TermoCompromissoDocumentacaoPendenteRelControle")
@Scope("viewScope")
@Lazy
public class TermoCompromissoDocumentacaoPendenteRelControle extends SuperControleRelatorio {

    private List<MatriculaVO> listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private MatriculaVO matriculaVO;

    public TermoCompromissoDocumentacaoPendenteRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

//    public void imprimirPDF() {
//        imprimirPDF(getMatriculaVO());
//    }

//    private void imprimirPDF(MatriculaVO matriculaVO) {
//        String design = null;
//        String nomeEntidade = null;
//        String titulo = null;
//        try {
//            registrarAtividadeUsuario(getUsuarioLogado(), "TermoCompromissoDocumentacaoPendenteRelControle", "Inicializando Geração de Relatório Termo Compromisso Documentação Pedente", "Emitindo Relatório");
//            getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().ValidarDados(getMatriculaVO());
////            
//            titulo = "Termo de Compromisso para Documentação Pendente";
//            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
//            design = TermoCompromissoDocumentacaoPendenteRel.getDesignIReportRelatorio();
////            apresentarRelatorioObjetos(TermoCompromissoDocumentacaoPendenteRel.getIdEntidade(),titulo,nomeEntidade,"","PDF","",design,getUsuarioLogado().getNome(),"",getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().criarObjeto(matriculaVO, getConfiguracaoGeralPadraoSistema(),getUsuarioLogado()), TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
//            setListaConsultaAluno(getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().criarObjeto(matriculaVO, getConfiguracaoGeralPadraoSistema(),getUsuarioLogado()));
//            if (!getListaConsultaAluno().isEmpty()) {
//                getSuperParametroRelVO().setTituloRelatorio(titulo);
//                getSuperParametroRelVO().setNomeDesignIreport(design);
//                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//                getSuperParametroRelVO().setSubReport_Dir(TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
//                getSuperParametroRelVO().setCaminhoBaseRelatorio(TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
//                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
//                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
////                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
////                getSuperParametroRelVO().setListaObjetos(getListaDeclaracao());
//                realizarImpressaoRelatorio();
//            removerObjetoMemoria(this);
//            registrarAtividadeUsuario(getUsuarioLogado(), "TermoCompromissoDocumentacaoPendenteRelControle", "Finalizando Geração de Relatório Termo Compromisso Documentação Pedente", "Emitindo Relatório");
//            setMensagemID("msg_relatorio_ok");
//
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        } finally {
//            design = null;
//            nomeEntidade = null;
//            titulo = null;
//        }
//    }
    
    public void imprimirPDF() {
        String titulo = null;
        String nomeEntidade = null;
        String design = null;
        List<TermoCompromissoDocumentacaoPendenteRelVO> listaTermoCompromissoDocumentacaoPendenteVos = null;
        try {
        	registrarAtividadeUsuario(getUsuarioLogado(), "TermoCompromissoDocumentacaoPendenteRelControle", "Inicializando Geração de Relatório Termo Compromisso Documentação Pedente", "Emitindo Relatório");
        	getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().ValidarDados(getMatriculaVO());
            titulo = "Termo de Compromisso para Documentação Pendente";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = TermoCompromissoDocumentacaoPendenteRel.getDesignIReportRelatorio();

            listaTermoCompromissoDocumentacaoPendenteVos = getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().criarObjeto(getMatriculaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());

            if (!listaTermoCompromissoDocumentacaoPendenteVos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(listaTermoCompromissoDocumentacaoPendenteVos);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

            //apresentarRelatorioObjetos(DeclaracaoAbandonoCursoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
            //		getFacadeFactory().getDeclaracaoAbandonoCursoRelFacade().getDescricaoFiltros(), getListaAbandonoCurso(), DeclaracaoAbandonoCursoRel.getCaminhoBaseRelatorio());
            //setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "TermoCompromissoDocumentacaoPendenteRelControle", "Finalizando Geração de Relatório Termo Compromisso Documentação Pedente", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            setListaConsultaAluno(null);
            removerObjetoMemoria(this);
        }
    }

    public void consultarAluno() {
        try {
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(),
                        false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("registroAcademico")) {
            	objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(),
            			false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(),
                        false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
        	if(getMatriculaVO().getMatricula().trim().isEmpty()){
        		setMatriculaVO(null);
        		limparMensagem();
        		return;
        	}
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            
            setMatriculaVO(objAluno);
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), getUnidadeEnsinoLogado().getCodigo(),
                NivelMontarDados.TODOS, getUsuarioLogado()));
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
    }

    public void limparDadosAluno() {
        setMatriculaVO(new MatriculaVO());
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
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

    public List<MatriculaVO> getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList<MatriculaVO>();
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }
}
