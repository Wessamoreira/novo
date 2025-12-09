/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.controle.bancocurriculum;


import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
/**
 *
 * @author Philippe
 */
@Controller("AlunosCandidatadosVagaRelControle")
@Scope("viewScope")
@Lazy
public class AlunosCandidatadosVagaRelControle extends SuperControleRelatorio{

    private PessoaVO pessoaVO;
    private CursoVO cursoVO;
    private TurmaVO turmaVO;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private List listaConsultaAluno;
    private String situacaoVaga;

    public void imprimirPDF() {
        List alunosCandidatadosVagaRelVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunosCandidatadosVagaRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
            alunosCandidatadosVagaRelVOs = getFacadeFactory().getAlunosCandidatadosVagaRelFacade().criarObjeto(getPessoaVO(), getCursoVO(), getTurmaVO(), getSituacaoVaga());
            if (!alunosCandidatadosVagaRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosCandidatadosVagaRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosCandidatadosVagaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Candidatos à vagas por aluno");
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setListaObjetos(alunosCandidatadosVagaRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosCandidatadosVagaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "AlunosCandidatadosVagaRelControle", "Finalizando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(alunosCandidatadosVagaRelVOs);
        }
    }

    public void imprimirExcel() {
        List alunosCandidatadosVagaRelVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunosCandidatadosVagaRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
            alunosCandidatadosVagaRelVOs = getFacadeFactory().getAlunosCandidatadosVagaRelFacade().criarObjeto(getPessoaVO(), getCursoVO(), getTurmaVO(), getSituacaoVaga());
            if (!alunosCandidatadosVagaRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosCandidatadosVagaRelFacade().designIReportRelatorioExcel());


                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosCandidatadosVagaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Candidatos à vagas por aluno");
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setListaObjetos(alunosCandidatadosVagaRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosCandidatadosVagaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "AlunosCandidatadosVagaRelControle", "Finalizando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(alunosCandidatadosVagaRelVOs);
        }
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nomeCurso")) {
                objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), 0, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCursoVO(obj);
            getListaConsultaCurso().clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            setMensagemID("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public List getTipoConsultaComboCurso() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("nomeCurso", "Nome do Curso"));
        return objs;
    }

    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), 0, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            getListaConsultaTurma().clear();
            this.setValorConsultaTurma("");
            this.setCampoConsultaTurma("");
            setMensagemID("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public List getTipoConsultaComboTurma() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("identificadorTurma", "Identificador da Turma"));
        return objs;
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaAluno().equals("nomeAluno")) {
                objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorConsultaAluno(), "AL", true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens");
            setPessoaVO(obj);
            getListaConsultaAluno().clear();
            this.setValorConsultaAluno("");
            this.setCampoConsultaAluno("");
            setMensagemID("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public List getTipoConsultaComboAluno() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("nomeAluno", "Nome do Aluno"));
        return objs;
    }

    public List getListaSelectitemSituacaoVagas() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("EC", "Em Construção"));
        itens.add(new SelectItem("AT", "Ativa"));
        itens.add(new SelectItem("EN", "Encerrada"));
        itens.add(new SelectItem("EX", "Expirada"));
        return itens;
    }

    public void limparCampoCurso(){
        setCursoVO(new CursoVO());
    }

    public void limparCampoTurma(){
        setTurmaVO(new TurmaVO());
    }

    public void limparCampoAluno(){
        setPessoaVO(new PessoaVO());
    }

    public PessoaVO getPessoaVO() {
        if (pessoaVO == null) {
            pessoaVO = new PessoaVO();
        }
        return pessoaVO;
    }

    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
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

    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
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

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public String getSituacaoVaga() {
        if (situacaoVaga == null) {
            situacaoVaga = "";
        }
        return situacaoVaga;
    }

    public void setSituacaoVaga(String situacaoVaga) {
        this.situacaoVaga = situacaoVaga;
    }
}
