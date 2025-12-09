/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.financeiro;

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
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.financeiro.BuscaCandidatoVagaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

/**
 *
 * @author Rogerio
 */
@Controller("BuscaCandidatoVagaControle")
@Scope("viewScope")
@Lazy
public class BuscaCandidatoVagaControle extends SuperControle implements Serializable {

    protected List listaConsultaCidade;
    protected String campoConsultaCidade;
    protected String valorConsultaCidade;
    protected List listaConsultaCurso;
    private List listaSelectItemAreaProfissional;
    private List listaSelectItemEstado;
    private List listaSelectItemCurso;
    private List listaSelectItemCidade;
    private Boolean apresentarComboCidade;
    protected List<VagasVO> listaConsultaVagas;
    protected String campoConsultaCurso;
    protected String valorConsultaCurso;
    protected BuscaCandidatoVagaVO buscaCandidatoVaga;
    private List<AreaProfissionalVO> listaConsultaAreaProfissional;

    public BuscaCandidatoVagaControle() {
        setControleConsulta(new ControleConsulta());
        montarListaSelectItemAreaProfissional();
        getBuscaCandidatoVaga().getCidade().getEstado().setCodigo(0);
        montarListaSelectItemEstado();
        montarListaSelectItemCurso();
//        iniciarObjetoConsulta();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void iniciarObjetoConsulta() {
        try {
            getBuscaCandidatoVaga().setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(getUsuarioLogado().getPessoa().getCidade().getCodigo(), true, getUsuarioLogado()));

            if (getBuscaCandidatoVaga().getCidade().getCodigo().equals(0)) {
                getBuscaCandidatoVaga().getCidade().setNome("Todos");
            }
            getBuscaCandidatoVaga().getCurso().setNome("Todos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String voltar() {
        this.setListaConsulta(null);
        setBuscaCandidatoVaga(new BuscaCandidatoVagaVO());
        getBuscaCandidatoVaga().getCurso().setNome("Todos");
        getBuscaCandidatoVaga().getCidade().setNome("Todos");
        return "consultar";
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaBuscaCandidatoVaga(getBuscaCandidatoVaga()));
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void consultarAluno() {
        try {
            if (getBuscaCandidatoVaga().getAreaProfissional().getCodigo() != 0) {
                getBuscaCandidatoVaga().getListaAreaProfissional().add(getBuscaCandidatoVaga().getAreaProfissional());
            }
            setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaBuscaCandidatoVaga(getBuscaCandidatoVaga()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarVagas() {
        try {
            BuscaCandidatoVagaVO obj = (BuscaCandidatoVagaVO) context().getExternalContext().getRequestMap().get("buscaCandidatoVagaItens");
            setBuscaCandidatoVaga(obj);

            setListaConsultaVagas(getFacadeFactory().getVagasFacade().consultarPorParceiro(getUsuarioLogado().getNome(), "", "AT", true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String editar() {
        return "";
    }

    public void realizarConsultaPessoa() {
        try {
            BuscaCandidatoVagaVO obj = (BuscaCandidatoVagaVO) context().getExternalContext().getRequestMap().get("buscaCandidatoVagaItens");
//            obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getPessoa().getCodigo(), false, true, false, getUsuarioLogado()));
            context().getExternalContext().getSessionMap().put("pessoaVO", obj.getPessoa());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por consultar Cidade <code>Cidade/code>.
     * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela
     * para o acionamento por meio requisições Ajax.
     */
    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("estado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getBuscaCandidatoVaga().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome("%" + getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_TODOS, Boolean.FALSE, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparListaConsultaCurso() {
        setListaConsultaCurso(new ArrayList(0));
    }

    public void limparDadosCurso() {
        getBuscaCandidatoVaga().setCurso(new CursoVO());
        getBuscaCandidatoVaga().getCurso().setNome("Todos");
    }

    public void limparDadosCidade() {
        getBuscaCandidatoVaga().setCidade(new CidadeVO());
        getBuscaCandidatoVaga().getCidade().setNome("Todos");
    }

    public void selecionarCurso() throws Exception {
        try {
            CursoVO cursoVO = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getBuscaCandidatoVaga().setCurso(cursoVO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getListaSelectItemSexoPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", "Todos"));
        Hashtable sexos = (Hashtable) Dominios.getSexo();
        Enumeration keys = sexos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) sexos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getTipoConsultaComboAreaProfissional() {
        List itens = new ArrayList(0);
//        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        return itens;
    }

    public void adicionarAreaProfissional() {
        try {

            AreaProfissionalVO obj = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalItens");
            getBuscaCandidatoVaga().setAreaProfissional(obj);
            if (!getBuscaCandidatoVaga().getListaAreaProfissional().contains(obj)) {
                getBuscaCandidatoVaga().getListaAreaProfissional().add(getBuscaCandidatoVaga().getAreaProfissional());
            }
            getListaConsultaAreaProfissional().remove(obj);
            getBuscaCandidatoVaga().setAreaProfissional(new AreaProfissionalVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerAreaProfissional() {
        AreaProfissionalVO areaProfissional = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalItens");
        for (AreaProfissionalVO obj : getBuscaCandidatoVaga().getListaAreaProfissional()) {
            if (obj.getDescricaoAreaProfissional().equals(areaProfissional.getDescricaoAreaProfissional())) {
                getBuscaCandidatoVaga().getListaAreaProfissional().remove(areaProfissional);
                return;
            }
        }
    }

    public void consultarAreaProfissional() {
        try {
            getListaConsultaAreaProfissional().clear();
            setListaConsultaAreaProfissional(getFacadeFactory().getAreaProfissionalFacade().consultar("AT", "situacao", false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparDadosAreaProfissional() {
        getBuscaCandidatoVaga().getListaAreaProfissional().clear();
    }

    public void adicionarFormacaoAcademica() {
        try {
            if (getBuscaCandidatoVaga().getFormacaoAcademica().getCurso().equals("")) {
                throw new Exception("O campo FORMAÇÃO ACADÊMICA deve ser informado.");
            }
            getBuscaCandidatoVaga().getListaFormacaoAcademica().add(getBuscaCandidatoVaga().getFormacaoAcademica());
            getBuscaCandidatoVaga().setFormacaoAcademica(new FormacaoAcademicaVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerFormacaoAcademica() {
        FormacaoAcademicaVO formacaoAcademica = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
        for (FormacaoAcademicaVO obj : getBuscaCandidatoVaga().getListaFormacaoAcademica()) {
            if (obj.getCurso().equals(formacaoAcademica.getCurso())) {
                getBuscaCandidatoVaga().getListaFormacaoAcademica().remove(formacaoAcademica);
                return;
            }
        }
    }

    public void adicionarFormacaoExtraCurricular() {
        try {
            if (getBuscaCandidatoVaga().getFormacaoExtraCurricular().getCurso().equals("")) {
                throw new Exception("O campo FORMAÇÃO EXTRA CURRICULAR deve ser informado.");
            }
            getBuscaCandidatoVaga().getListaFormacaoExtraCurricular().add(getBuscaCandidatoVaga().getFormacaoExtraCurricular());
            getBuscaCandidatoVaga().setFormacaoExtraCurricular(new FormacaoExtraCurricularVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerFormacaoExtraCurricular() {
        FormacaoExtraCurricularVO formacaoExtraCurricular = (FormacaoExtraCurricularVO) context().getExternalContext().getRequestMap().get("formacaoExtraCurricular");
        for (FormacaoExtraCurricularVO obj : getBuscaCandidatoVaga().getListaFormacaoExtraCurricular()) {
            if (obj.getCurso().equals(formacaoExtraCurricular.getCurso())) {
                getBuscaCandidatoVaga().getListaFormacaoExtraCurricular().remove(formacaoExtraCurricular);
                return;
            }
        }
    }

    public void realizarEnvioNotificacaoAluno() {
        try {
            List<TextoPadraoBancoCurriculumVO> listaTexto = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipo("notificarAlunoSugestaoVaga", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (listaTexto == null || listaTexto.isEmpty()) {
                throw new Exception(" Não existe Texto Padrão cadastro para o tipo: Notificar Aluno sobre Sugestão de Vaga.");
            }
            TextoPadraoBancoCurriculumVO textoPadrao = listaTexto.get(0);
            String texto = textoPadrao.getTexto();

            for (VagasVO obj : getListaConsultaVagas()) {
                if (obj.getNotificarAluno()) {
                    textoPadrao.substituirTagsTextoPadrao(textoPadrao, obj);
                    getFacadeFactory().getComunicacaoInternaFacade().executarNotificacaoMensagemPreDefinidaTextoPadrao(textoPadrao, getBuscaCandidatoVaga().getPessoa(), null, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
                    textoPadrao.setTexto(texto);
                }
            }
            setMensagemID("msg_vagas_sugeridas");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
     */
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("estado", "Estado"));


        return itens;


    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));


        return itens;


    }

    public List getTipoConsultaComboSalario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("Até R$999", "Até R$999,00"));
        itens.add(new SelectItem("R$1000 à R$1999", "De R$ 1000,00 até R$ 1999,00"));
        itens.add(new SelectItem("R$2000 à R$2999", "De R$ 2000,00 até R$ 2999,00"));
        itens.add(new SelectItem("R$3000 à R$3999", "De R$ 3000,00 até R$ 3999,00"));
        itens.add(new SelectItem("R$4000 à R$4999", "De R$ 4000,00 até R$ 4999,00"));
        itens.add(new SelectItem("R$5000 à R$5999", "De R$ 5000,00 até R$ 5999,00"));
        itens.add(new SelectItem("acima de R$6000", "Acima de R$ 6000,00"));


        return itens;


    }

    public List getTipoComboNivelIngles() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));


        return itens;


    }

    public List getTipoComboNivelFrances() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));


        return itens;


    }

    public List getTipoComboNivelEspanhol() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));


        return itens;


    }

    public List getTipoComboNivelOutrosIdiomas() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));


        return itens;


    }

    public void montarListaSelectItemAreaProfissional() {
        try {
            montarListaSelectItemAreaProfissional("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemAreaProfissional(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaProfissionalPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AreaProfissionalVO obj = (AreaProfissionalVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoAreaProfissional()));
            }
            setListaSelectItemAreaProfissional(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarAreaProfissionalPorDescricao(String prm) throws Exception {
        List lista = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo(prm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCurso() {
        try {
            montarListaSelectItemCurso("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemCurso(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CursoVO obj = (CursoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCurso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarCursoPorNome(String prm) throws Exception {
        List lista = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(prm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, Boolean.FALSE, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCidade() {
        try {
            if (getBuscaCandidatoVaga().getCidade().getEstado().getCodigo() != 0) {
                setApresentarComboCidade(Boolean.TRUE);
            } else {
                setApresentarComboCidade(Boolean.FALSE);
            }
            montarListaSelectItemCidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemCidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorEstado(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarCidadePorEstado(String prm) throws Exception {
        List lista = getFacadeFactory().getCidadeFacade().consultarPorCodigoEstado(getBuscaCandidatoVaga().getCidade().getEstado().getCodigo(), false, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemEstado() {
        try {
            montarListaSelectItemEstado("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemEstado(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarEstadoPorSigla(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                EstadoVO obj = (EstadoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemEstado(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarEstadoPorSigla(String prm) throws Exception {
        List lista = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(getConfiguracaoGeralPadraoSistema().getPaisPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public Integer getColumnAreaProfissional() {
        if (getBuscaCandidatoVaga().getListaAreaProfissional().size() > 4) {
            return 4;


        }
        return getBuscaCandidatoVaga().getListaAreaProfissional().size();


    }

    public Integer getElementAreaProfissional() {
        return getBuscaCandidatoVaga().getListaAreaProfissional().size();


    }

    public Integer getColumnFormacaoAcademica() {
        if (getBuscaCandidatoVaga().getListaFormacaoAcademica().size() > 4) {
            return 4;


        }
        return getBuscaCandidatoVaga().getListaFormacaoAcademica().size();


    }

    public Integer getElementFormacaoAcademica() {
        return getBuscaCandidatoVaga().getListaFormacaoAcademica().size();


    }

    public Integer getColumnFormacaoExtraCurricular() {
        if (getBuscaCandidatoVaga().getListaFormacaoExtraCurricular().size() > 4) {
            return 4;


        }
        return getBuscaCandidatoVaga().getListaFormacaoExtraCurricular().size();


    }

    public Integer getElementFormacaoExtraCurricular() {
        return getBuscaCandidatoVaga().getListaFormacaoExtraCurricular().size();


    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>escolaridade</code>
     */
    public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);




        return objs;
    }

    public String getCampoConsultaCidade() {
        return campoConsultaCidade;


    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;


    }

    public List getListaConsultaCidade() {
        return listaConsultaCidade;


    }

    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;


    }

    public String getValorConsultaCidade() {
        return valorConsultaCidade;


    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;


    }

    public BuscaCandidatoVagaVO getBuscaCandidatoVaga() {
        if (buscaCandidatoVaga == null) {
            buscaCandidatoVaga = new BuscaCandidatoVagaVO();


        }
        return buscaCandidatoVaga;


    }

    public void setBuscaCandidatoVaga(BuscaCandidatoVagaVO buscaCandidatoVaga) {
        this.buscaCandidatoVaga = buscaCandidatoVaga;


    }

    public String getCampoConsultaCurso() {
        return campoConsultaCurso;


    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;


    }

    public List getListaConsultaCurso() {
        return listaConsultaCurso;


    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;


    }

    public String getValorConsultaCurso() {
        return valorConsultaCurso;


    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;


    }

    public List<VagasVO> getListaConsultaVagas() {
        return listaConsultaVagas;


    }

    public void setListaConsultaVagas(List<VagasVO> listaConsultaVagas) {
        this.listaConsultaVagas = listaConsultaVagas;

    }

    public List<AreaProfissionalVO> getListaConsultaAreaProfissional() {
        if (listaConsultaAreaProfissional == null) {
            listaConsultaAreaProfissional = new ArrayList<AreaProfissionalVO>(0);
        }
        return listaConsultaAreaProfissional;
    }

    public void setListaConsultaAreaProfissional(List<AreaProfissionalVO> listaConsultaAreaProfissional) {
        this.listaConsultaAreaProfissional = listaConsultaAreaProfissional;
    }

    public List getListaSelectItemAreaProfissional() {
        if (listaSelectItemAreaProfissional == null) {
            listaSelectItemAreaProfissional = new ArrayList(0);
        }
        return listaSelectItemAreaProfissional;
    }

    public void setListaSelectItemAreaProfissional(List listaSelectItemAreaProfissional) {
        this.listaSelectItemAreaProfissional = listaSelectItemAreaProfissional;
    }

    public List getListaSelectItemEstado() {
        if (listaSelectItemEstado == null) {
            listaSelectItemEstado = new ArrayList(0);
        }
        return listaSelectItemEstado;
    }

    public void setListaSelectItemEstado(List listaSelectItemEstado) {
        this.listaSelectItemEstado = listaSelectItemEstado;
    }

    public List getListaSelectItemCidade() {
        if (listaSelectItemCidade == null) {
            listaSelectItemCidade = new ArrayList(0);
        }
        return listaSelectItemCidade;
    }

    public void setListaSelectItemCidade(List listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
    }

    public Boolean getApresentarComboCidade() {
        if (apresentarComboCidade == null) {
            apresentarComboCidade = Boolean.FALSE;
        }
        return apresentarComboCidade;
    }

    public void setApresentarComboCidade(Boolean apresentarComboCidade) {
        this.apresentarComboCidade = apresentarComboCidade;
    }

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }
}
