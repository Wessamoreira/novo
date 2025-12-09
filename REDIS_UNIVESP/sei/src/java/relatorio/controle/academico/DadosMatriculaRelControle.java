package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import controle.academico.VisaoAlunoControle;
import negocio.comuns.academico.MatriculaPeriodoVO;

@SuppressWarnings("unchecked")
@Controller("DadosMatriculaRelControle")
@Scope("viewScope")
@Lazy
public class DadosMatriculaRelControle extends SuperControleRelatorio {

    protected MatriculaPeriodoVO matriculaPeriodoVO;
    protected List listaSelectItemMatriculasAluno;
    protected boolean exibirMatricula;

    public DadosMatriculaRelControle() throws Exception {
        setListaSelectItemMatriculasAluno(new ArrayList(0));
        setMatriculaPeriodoVO(new MatriculaPeriodoVO());
        setExibirMatricula(false);
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void montarListaSelectItemMatriculaVisaoAluno() {
        try {
            List resultadoConsulta = consultarMatriculasPorAluno();
            setListaSelectItemMatriculasAluno(UtilSelectItem.getListaSelectItem(resultadoConsulta, "matricula", "matricula"));
        } catch (Exception e) {
            setListaSelectItemMatriculasAluno(new ArrayList(0));
        }
    }

    public List consultarMatriculasPorAluno() throws Exception {
//        List lista = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoa(super.getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false,
//                Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        List lista = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaNaoCancelada(super.getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
        return lista;
    }

    @PostConstruct
    public void novoVisaoAluno() throws Exception {
    	if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
        registrarAtividadeUsuario(getUsuarioLogado(), "DadosMatriculaRelControle", "Novo Visão Aluno", "Novo");
        VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
        if (visaoAlunoControle != null) {
            visaoAlunoControle.inicializarMenuPlanoDeEstudoAluno();
            getMatriculaPeriodoVO().getMatriculaVO().setMatricula(visaoAlunoControle.getMatricula().getMatricula());
            consultarDadosDoAlunoPelaMatricula();
        }
//        executarMetodoControle(VisaoAlunoControle.class.getSimpleName(), "inicializarPlanoDeEstudoAluno");
//        return Uteis.getCaminhoRedirecionamentoNavegacao("planoDeEstudoAluno.xhtml");
    	}
    }

    public Boolean getApresentarCampos() {
        if ((!getMatriculaPeriodoVO().getMatriculaVO().getMatricula().equals("0")) && (getMatriculaPeriodoVO().getMatriculaVO().getMatricula() != "")) {
            return true;
        }
        return false;
    }

    public void emitirDadosMatricula() {
        try {
            setExibirMatricula(true);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getImprimePlanoDeEstudo() {
        if (isExibirMatricula()) {
            return "abrirPopup('../DadosMatriculaSV?matriculaPeriodo=" + getMatriculaPeriodoVO().getCodigo() + "&nomeUnidadeEnsino=" + getUnidadeEnsinoLogado().getNome() + "&titulo=matricula', 'dadosMatricula', 780, 585)";
        }
        return "";
    }

    public void consultarDadosDoAlunoPelaMatricula() {
        try {
            if(!getMatriculaPeriodoVO().getMatriculaVO().getMatricula().equals("0")){
                setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(getMatriculaPeriodoVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            setMensagemDetalhada("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaPeriodoVO(new MatriculaPeriodoVO());
        }
    }

    public List getListaSelectItemMatriculasAluno() {
        return listaSelectItemMatriculasAluno;
    }

    public void setListaSelectItemMatriculasAluno(List listaSelectItemMatriculasAluno) {
        this.listaSelectItemMatriculasAluno = listaSelectItemMatriculasAluno;
    }

//    public MatriculaVO getMatriculaVO() {
//        return matriculaVO;
//    }
//
//    public void setMatriculaVO(MatriculaVO matriculaVO) {
//        this.matriculaVO = matriculaVO;
//    }

    public boolean isExibirMatricula() {
        return exibirMatricula;
    }

    public void setExibirMatricula(boolean exibirMatricula) {
        this.exibirMatricula = exibirMatricula;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if(matriculaPeriodoVO == null){
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }
}
