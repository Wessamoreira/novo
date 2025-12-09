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
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
@Controller("LogTurmaControle")
@Scope("viewScope")
@Lazy
public class LogTurmaControle extends SuperControle {

    public LogTurmaControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("periodo")) {
                objs = getFacadeFactory().getLogTurmaFacade().consultarPorPeriodo(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            } else if (getControleConsulta().getCampoConsulta().equals("turma")) {
                objs = getFacadeFactory().getLogTurmaFacade().consultarPorIdentificadorTurma(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            } else if (getControleConsulta().getCampoConsulta().equals("curso")) {
                objs = getFacadeFactory().getLogTurmaFacade().consultarPorCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            } else if (getControleConsulta().getCampoConsulta().equals("acao")) {
                objs = getFacadeFactory().getLogTurmaFacade().consultarPorAcao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public List getTipoComboConsulta() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("turma", "Turma"));
        objs.add(new SelectItem("curso", "Curso"));
        objs.add(new SelectItem("acao", "Ação"));
        objs.add(new SelectItem("periodo", "Período"));
        return objs;
    }

    public List getTipoComboConsultaPorAcao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("inclusao", "Inclusão"));
        objs.add(new SelectItem("alteracao", "Alteração"));
        objs.add(new SelectItem("exclusao", "Exclusão"));
        objs.add(new SelectItem("atualizacaoDisciplinasAlunos", "Atualização das Disciplinas dos Aluno"));
        objs.add(new SelectItem("atualizacaoListaDisciplinas", "Atualização da Lista de Disciplinas"));
        return objs;
    }

    public Boolean getIsConsultaPeriodo() {
        if (getControleConsulta().getCampoConsulta().equals("periodo")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getIsConsultaAcao() {
        if (getControleConsulta().getCampoConsulta().equals("acao")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
