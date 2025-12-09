package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ComunicadoDebitoDocumentosAlunoRel;

@SuppressWarnings("unchecked")
@Controller("ComunicadoDebitoDocumentosAlunoRelControle")
@Scope("viewScope")
@Lazy
public class ComunicadoDebitoDocumentosAlunoRelControle extends SuperControleRelatorio {

    protected MatriculaVO matriculaVO;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private List listaConsultaAluno;

    public ComunicadoDebitoDocumentosAlunoRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List objetos = null;
        String titulo = "COMUNICADO DOCUMENTAÇÃO PENDENTE DO ALUNO";
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        String design = ComunicadoDebitoDocumentosAlunoRel.getDesignIReportRelatorio();
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ComunicadoDebitoDocumentosAlunoRelControle", "Inicializando Geração de Relatório Comunicado Débito Documentos Aluno", "Emitindo Relatório");
            ComunicadoDebitoDocumentosAlunoRel.validarDados(getMatriculaVO());
            getFacadeFactory().getComunicadoDebitoDocumentosAlunoRelFacade().setDescricaoFiltros("");

            objetos = getFacadeFactory().getComunicadoDebitoDocumentosAlunoRelFacade().criarObjeto(getMatriculaVO(), getMatriculaPeriodoVO(),
                    getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            getSuperParametroRelVO().setCaminhoBaseRelatorio(ComunicadoDebitoDocumentosAlunoRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setListaObjetos(objetos);
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
            getSuperParametroRelVO().setTituloRelatorio(titulo);
            getSuperParametroRelVO().setNomeDesignIreport(design);
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setSubReport_Dir(ComunicadoDebitoDocumentosAlunoRel.getCaminhoBaseRelatorio());

//            apresentarRelatorioObjetos(ComunicadoDebitoDocumentosAlunoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
//                    getFacadeFactory().getComunicadoDebitoDocumentosAlunoRelFacade().getDescricaoFiltros(),
//                    objetos, ComunicadoDebitoDocumentosAlunoRel.getCaminhoBaseRelatorio());
            realizarImpressaoRelatorio();
            removerObjetoMemoria(this);
            if (!objetos.isEmpty()) {
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "ComunicadoDebitoDocumentosAlunoRelControle", "Finalizando Geração de Relatório Comunicado Débito Documentos Aluno", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(objetos);
            titulo = null;
            nomeEntidade = null;
            design = null;
        }
    }

    public void consultarAluno() {
        List objs = new ArrayList(0);
        try {
            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                	Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(
                		getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(
                		getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,getUsuarioLogado());
				
			}
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }

    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setMatriculaVO(obj);
        obj = null;
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
                        + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            this.setMatriculaVO(objAluno);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            this.setMatriculaVO(new MatriculaVO());
        }
    }

    public Boolean getApresentarCampos() {
        if ((getMatriculaVO().getAluno() != null) && (getMatriculaVO().getAluno().getCodigo() != 0)) {
            return true;
        }
        return false;
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
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

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
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

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if (matriculaPeriodoVO == null) {
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }
    
	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}
}
