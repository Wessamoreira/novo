package controle.sad;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas matriculaDWForm.jsp
 * matriculaDWCons.jsp) com as funcionalidades da classe <code>MatriculaDW</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see MatriculaDW
 * @see MatriculaDWVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.sad.MatriculaDWVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Controller("MatriculaDWControle")
@Scope("request")
@Lazy
public class MatriculaDWControle extends SuperControle implements Serializable {

    private MatriculaDWVO matriculaDWVO;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemProcessoMatricula;
    protected List listaSelectItemCurso;
    protected List listaSelectItemTurno;
    protected List listaSelectItemAreaConhecimento;

    public MatriculaDWControle() throws Exception {
        //obterUsuarioLogado();
        novo();
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>MatriculaDW</code> para edição pelo usuário
     * da aplicação.
     */
    public void novo() {
        setMatriculaDWVO(new MatriculaDWVO());
        consultarGraficoPizza();
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP MatriculaDWCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void consultarGraficoPizza() {
        try {
            SqlRowSet dadosSql = getFacadeFactory().getMatriculaDWFacade().consultaGeracaoRelatorioPizzaBarra(matriculaDWVO, false, getUsuarioLogado());
            getMatriculaDWVO().criarGraficoPizza(dadosSql);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {

            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarGraficoBarra() {
        try {
            SqlRowSet dadosSql = getFacadeFactory().getMatriculaDWFacade().consultaGeracaoRelatorioPizzaBarra(matriculaDWVO, false, getUsuarioLogado());
            getMatriculaDWVO().criarGraficoBarra(dadosSql);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {

            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarGraficoLinhaTempo() {
        try {
            SqlRowSet dadosSql = getFacadeFactory().getMatriculaDWFacade().consultaGeracaoRelatorioLinhaTempo(matriculaDWVO, false, getUsuarioLogado());
            getMatriculaDWVO().criarGraficoLinhaTempo(dadosSql);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getListaSelectMes() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem(0, ""));
        lista.add(new SelectItem(1, "Janeiro"));
        lista.add(new SelectItem(2, "Fevereiro"));
        lista.add(new SelectItem(3, "Março"));
        lista.add(new SelectItem(4, "Abril"));
        lista.add(new SelectItem(5, "Maio"));
        lista.add(new SelectItem(6, "Junho"));
        lista.add(new SelectItem(7, "Julho"));
        lista.add(new SelectItem(8, "Agosto"));
        lista.add(new SelectItem(9, "Setembro"));
        lista.add(new SelectItem(10, "Outubro"));
        lista.add(new SelectItem(11, "Novembro"));
        lista.add(new SelectItem(12, "Dezembro"));
        return lista;
    }

    public Integer getAnoAtual() {
        return Integer.parseInt(Uteis.getAnoDataAtual4Digitos());
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>nivelEducacional</code>
     */
    public List getListaSelectItemNivelEducacionalMatriculaDW() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", "--- Nivel Educacional ---"));
        Hashtable nivelEducacionalCursos = (Hashtable) Dominios.getNivelEducacionalCurso();
        Enumeration keys = nivelEducacionalCursos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) nivelEducacionalCursos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>AreaConhecimento</code>.
     */
    public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, "--- Área de Conhecimento ---"));
            while (i.hasNext()) {
                AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }

            setListaSelectItemAreaConhecimento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>AreaConhecimento</code>. Buscando todos os
     * objetos correspondentes a entidade <code>AreaConhecimento</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemAreaConhecimento() {
        try {
            montarListaSelectItemAreaConhecimento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turno</code>.
     */
    public void montarListaSelectItemTurno(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTurnoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, "--- Turno ---"));
            while (i.hasNext()) {
                TurnoVO obj = (TurnoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemTurno(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTurno() {
        try {
            montarListaSelectItemTurno("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarTurnoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Curso</code>.
     */
    public void montarListaSelectItemCurso(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, "--- Curso ---"));
            while (i.hasNext()) {
                CursoVO obj = (CursoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemCurso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Curso</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Curso</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCurso() {
        try {
            montarListaSelectItemCurso("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCursoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCursoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>PeriodoLetivo</code>.
     */
    public void montarListaSelectItemProcessoMatricula(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarProcessoMatriculaPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, "--- Processo de Matricula ---"));
            while (i.hasNext()) {
                ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemProcessoMatricula(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivo</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemProcessoMatricula() {
        try {
            montarListaSelectItemProcessoMatricula("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarProcessoMatriculaPorNome(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getProcessoMatriculaFacade().consultarPorDescricao(descricaoPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, "--- Unidade de Ensino ---"));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemProcessoMatricula();
        montarListaSelectItemCurso();
        montarListaSelectItemTurno();
        montarListaSelectItemAreaConhecimento();
    }

    public void limparNivelEducacional() {
        getMatriculaDWVO().setNivelEducacional("");
    }

    public void limparUnidadeEnsino() {
        getMatriculaDWVO().getUnidadeEnsino().setCodigo(0);
    }

    public void limparProcessoMatricula() {
        getMatriculaDWVO().getProcessoMatricula().setCodigo(0);
    }

    public void limparCurso() {
        getMatriculaDWVO().getCurso().setCodigo(0);
    }

    public void limparTurno() {
        getMatriculaDWVO().getTurno().setCodigo(0);
    }

    public void limparAreaConhecimento() {
        getMatriculaDWVO().getAreaConhecimento().setCodigo(0);
    }

    public void limparPeriodo() {
        getMatriculaDWVO().setData(null);
        getMatriculaDWVO().setDataFim(null);
    }

    public void limparAno() {
        getMatriculaDWVO().setAno(0);
    }

    public void limparMes() {
        getMatriculaDWVO().setMes(0);
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("mes", "Mês"));
        itens.add(new SelectItem("semestre", "Semestre"));
        itens.add(new SelectItem("ano", "Ano"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("descricaoPeriodoLetivo", "Periodo Letivo"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeAreaConhecimento", "Área de Conhecimento"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public List getListaSelectItemAreaConhecimento() {
        return (listaSelectItemAreaConhecimento);
    }

    public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
        this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
    }

    public List getListaSelectItemTurno() {
        return (listaSelectItemTurno);
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public List getListaSelectItemCurso() {
        return (listaSelectItemCurso);
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public List getListaSelectItemProcessoMatricula() {
        return listaSelectItemProcessoMatricula;
    }

    public void setListaSelectItemProcessoMatricula(List listaSelectItemProcessoMatricula) {
        this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public MatriculaDWVO getMatriculaDWVO() {
        return matriculaDWVO;
    }

    public void setMatriculaDWVO(MatriculaDWVO matriculaDWVO) {
        this.matriculaDWVO = matriculaDWVO;
    }
}
