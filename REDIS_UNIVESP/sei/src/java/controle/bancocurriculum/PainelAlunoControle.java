/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.bancocurriculum;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

/**
 *
 * @author Philippe
 */
@Controller("PainelAlunoControle")
@Scope("viewScope")
@Lazy
public class PainelAlunoControle extends SuperControle {

    private List alunos;
    private String campoConsulta;
    private String valorConsulta;
    private String popUpAluno;

    public PainelAlunoControle() {
        Boolean realizarConsulta = (Boolean) context().getExternalContext().getSessionMap().get("realizarConsulta");
        if (realizarConsulta != null && realizarConsulta) {
            consultarAlunos();
            context().getExternalContext().getSessionMap().remove("realizarConsulta");
        }        
        Boolean candidatoSelecionado = (Boolean) context().getExternalContext().getSessionMap().get("candidatoSelecionado");
        if (candidatoSelecionado != null && candidatoSelecionado) {
            consultarAlunosSelecionados();
            context().getExternalContext().getSessionMap().remove("candidatoSelecionado");
        }
        Boolean candidatoContratado = (Boolean) context().getExternalContext().getSessionMap().get("candidatoContratado");
        if (candidatoContratado != null && candidatoContratado) {
            consultarAlunosContratados();
            context().getExternalContext().getSessionMap().remove("candidatoContratado");
        }
    }

    public void consultarAlunos() {
        List objs = new ArrayList(0);
        try {
            if (getCampoConsulta().equals("nomeAluno")) {
                objs = getFacadeFactory().getMatriculaPeriodoFacade().consultarAlunosParticipamBancoCurriculoPorNome(getValorConsulta(), getUsuarioLogado());
            } else {
                objs = getFacadeFactory().getMatriculaPeriodoFacade().consultarAlunosParticipamBancoCurriculoPorTurma(getValorConsulta(), getUsuarioLogado());
            }
            setAlunos(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunosSelecionados() {
        List objs = new ArrayList(0);
        try {
            objs = getFacadeFactory().getMatriculaPeriodoFacade().consultarAlunosParticipamBancoCurriculoSelecionadosPorTurma(getValorConsulta(), getUsuarioLogado());
            setAlunos(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunosContratados() {
        List objs = new ArrayList(0);
        try {
            objs = getFacadeFactory().getMatriculaPeriodoFacade().consultarAlunosParticipamBancoCurriculoContratadosPorTurma(getValorConsulta(), getUsuarioLogado());
            setAlunos(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void executarVisualizacaoFichaAluno() throws Exception {
        MatriculaPeriodoVO matPer = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
        getFacadeFactory().getPessoaFacade().carregarDados(matPer.getMatriculaVO().getAluno(), getUsuarioLogado());
        getFacadeFactory().getPessoaFacade().carregarDados(matPer.getMatriculaVO().getAluno(), getUsuarioLogado());
        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(matPer.getMatriculaVO().getAluno().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
        context().getExternalContext().getSessionMap().put("alunoFichaVO", matPer.getMatriculaVO().getAluno());
    }

    public void popUpAlunoApresentar() throws Exception {
        try {
            MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
            setPopUpAluno("abrirPopup('alunoForm.xhtml?pessoa=" + obj.getMatriculaVO().getAluno().getCodigo() + "','Aluno', 950, 595);");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String realizarAbrirRichCurriculumAluno() {
        MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
        context().getExternalContext().getSessionMap().put("pessoaVO", obj.getMatriculaVO().getAluno());
        return "panelCurriculum";
    }

    public void editarDadosPessoaVagas() throws Exception {
        try {
            PessoaVO pessoa = (PessoaVO) context().getExternalContext().getSessionMap().get("pessoaVO");
            pessoa = (getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(pessoa.getCodigo(), false, true, false, getUsuarioLogado()));
            pessoa.setNovoObj(Boolean.FALSE);
            if (pessoa.getQtdFilhos() > 0) {
                pessoa.setPossuiFilho(true);
            }
            setMensagemID("msg_dados_editar");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomeAluno", "Nome do Aluno"));
        itens.add(new SelectItem("turma", "Identificador da Turma"));
        return itens;
    }

    public Boolean getApresentarResultadoConsultaAlunos() {
        if (getAlunos().isEmpty()) {
            return false;
        }
        return true;
    }

    public List getAlunos() {
        if (alunos == null) {
            alunos = new ArrayList(0);
        }
        return alunos;
    }

    public void setAlunos(List alunos) {
        this.alunos = alunos;
    }

    public String getCampoConsulta() {
        if (campoConsulta == null) {
            campoConsulta = "";
        }
        return campoConsulta;
    }

    public void setCampoConsulta(String campoConsulta) {
        this.campoConsulta = campoConsulta;
    }

    public String getValorConsulta() {
        if (valorConsulta == null) {
            valorConsulta = "";
        }
        return valorConsulta;
    }

    public void setValorConsulta(String valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    /**
     * @return the popUpAluno
     */
    public String getPopUpAluno() {
        if (popUpAluno == null) {
            popUpAluno = "";
        }
        return popUpAluno;
    }

    /**
     * @param popUpAluno the popUpAluno to set
     */
    public void setPopUpAluno(String popUpAluno) {
        this.popUpAluno = popUpAluno;
    }
}
