/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TitulacaoProfessorCursoVO;
import negocio.comuns.academico.TitulacaoQuantidadeFuncionariosVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
@Controller("TitulacaoProfessorCursoControle")
@Scope("request")
@Lazy
public class TitulacaoProfessorCursoControle extends SuperControle {

    private TitulacaoProfessorCursoVO titulacaoProfessorCursoVO;
    private TitulacaoQuantidadeFuncionariosVO titulacaoQuantidadeFuncionariosVO;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;

    public String novo() {
        removerObjetoMemoria(this);
        setTitulacaoProfessorCursoVO(new TitulacaoProfessorCursoVO());
        TitulacaoQuantidadeFuncionariosVO obj1 = new TitulacaoQuantidadeFuncionariosVO();
        obj1.setTitulacao("tecnico");
        obj1.setQuantidadeFuncionarios(0);
        getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().add(obj1);
        TitulacaoQuantidadeFuncionariosVO obj2 = new TitulacaoQuantidadeFuncionariosVO();
        obj2.setTitulacao("graduacao");
        obj2.setQuantidadeFuncionarios(0);
        getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().add(obj2);
        TitulacaoQuantidadeFuncionariosVO obj3 = new TitulacaoQuantidadeFuncionariosVO();
        obj3.setTitulacao("especializacao");
        obj3.setQuantidadeFuncionarios(0);
        getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().add(obj3);
        TitulacaoQuantidadeFuncionariosVO obj4 = new TitulacaoQuantidadeFuncionariosVO();
        obj4.setTitulacao("mestrado");
        obj4.setQuantidadeFuncionarios(0);
        getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().add(obj4);
        TitulacaoQuantidadeFuncionariosVO obj5 = new TitulacaoQuantidadeFuncionariosVO();
        obj5.setTitulacao("doutorado");
        obj5.setQuantidadeFuncionarios(0);
        getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().add(obj5);
        TitulacaoQuantidadeFuncionariosVO obj6 = new TitulacaoQuantidadeFuncionariosVO();
        obj6.setTitulacao("posDoutorado");
        obj6.setQuantidadeFuncionarios(0);
        getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().add(obj6);
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    public String editar() throws Exception {
        TitulacaoProfessorCursoVO obj = (TitulacaoProfessorCursoVO) context().getExternalContext().getRequestMap().get("titulacaoProfessorCurso");
        setTitulacaoProfessorCursoVO(getFacadeFactory().getTitulacaoProfessorCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        getTitulacaoProfessorCursoVO().setNovoObj(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    public String gravar() {
        try {
            if (getTitulacaoProfessorCursoVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getTitulacaoProfessorCursoFacade().incluir(getTitulacaoProfessorCursoVO());
            } else {
                getFacadeFactory().getTitulacaoProfessorCursoFacade().alterar(getTitulacaoProfessorCursoVO());
            }
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getTitulacaoProfessorCursoFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public List getTipoConsultaComboCurso() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("nomeCurso", "Nome do Curso"));
        return objs;
    }

    public List getListaSelectItemTitulacao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("tecnico", "Técnico"));
        objs.add(new SelectItem("graduacao", "Graduação"));
        objs.add(new SelectItem("especializacao", "Especialização"));
        objs.add(new SelectItem("mestrado", "Mestrado"));
        objs.add(new SelectItem("doutorado", "Doutorado"));
        objs.add(new SelectItem("posDoutorado", "Pós-Doutorado"));
        return objs;
    }

    public void adicionarTitulacaoFuncionarios() {
        try {
            int index = 0;
            for (TitulacaoQuantidadeFuncionariosVO objExistente : getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios()) {
                if (objExistente.getTitulacao().equals(getTitulacaoQuantidadeFuncionariosVO().getTitulacao())) {
                    if (getTitulacaoQuantidadeFuncionariosVO().getTitulacao().equals("tecnico")) {
                        getTitulacaoProfessorCursoVO().setQuantidadeTecnico(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    } else if (getTitulacaoQuantidadeFuncionariosVO().getTitulacao().equals("graduacao")) {
                        getTitulacaoProfessorCursoVO().setQuantidadeGraduacao(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    } else if (getTitulacaoQuantidadeFuncionariosVO().getTitulacao().equals("especializacao")) {
                        getTitulacaoProfessorCursoVO().setQuantidadeEspecializacao(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    } else if (getTitulacaoQuantidadeFuncionariosVO().getTitulacao().equals("mestrado")) {
                        getTitulacaoProfessorCursoVO().setQuantidadeMestrado(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    } else if (getTitulacaoQuantidadeFuncionariosVO().getTitulacao().equals("doutorado")) {
                        getTitulacaoProfessorCursoVO().setQuantidadeDoutorado(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    } else if (getTitulacaoQuantidadeFuncionariosVO().getTitulacao().equals("posDoutorado")) {
                        getTitulacaoProfessorCursoVO().setQuantidadePosDoutorado(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    }
                    TitulacaoQuantidadeFuncionariosVO obj = new TitulacaoQuantidadeFuncionariosVO();
                    obj.setTitulacao(getTitulacaoQuantidadeFuncionariosVO().getTitulacao());
                    obj.setQuantidadeFuncionarios(getTitulacaoQuantidadeFuncionariosVO().getQuantidadeFuncionarios());
                    getTitulacaoProfessorCursoVO().getListaTitulacaoQuantidadeFuncionarios().set(index, obj);
                    break;
                }
                index++;
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String excluir() {
        try {
            getFacadeFactory().getTitulacaoProfessorCursoFacade().excluir(getTitulacaoProfessorCursoVO(), getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
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
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("curso");
            getTitulacaoProfessorCursoVO().getTurma().setCurso(obj);
            getListaConsultaCurso().clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            setMensagemID("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }
    
    public TitulacaoProfessorCursoVO getTitulacaoProfessorCursoVO() {
        if (titulacaoProfessorCursoVO == null) {
            titulacaoProfessorCursoVO = new TitulacaoProfessorCursoVO();
        }
        return titulacaoProfessorCursoVO;
    }

    public void setTitulacaoProfessorCursoVO(TitulacaoProfessorCursoVO titulacaoProfessorCursoVO) {
        this.titulacaoProfessorCursoVO = titulacaoProfessorCursoVO;
    }

    public TitulacaoQuantidadeFuncionariosVO getTitulacaoQuantidadeFuncionariosVO() {
        if (titulacaoQuantidadeFuncionariosVO == null) {
            titulacaoQuantidadeFuncionariosVO = new TitulacaoQuantidadeFuncionariosVO();
        }
        return titulacaoQuantidadeFuncionariosVO;
    }

    public void setTitulacaoQuantidadeFuncionariosVO(TitulacaoQuantidadeFuncionariosVO titulacaoQuantidadeFuncionariosVO) {
        this.titulacaoQuantidadeFuncionariosVO = titulacaoQuantidadeFuncionariosVO;
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
}
