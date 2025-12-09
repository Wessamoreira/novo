/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.bancocurriculum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;
import negocio.comuns.financeiro.MarcadorVO;
import negocio.comuns.financeiro.TextoPadraoTagVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Controller("TextoPadraoBancoCurriculumControle")
@Scope("viewScope")
@Lazy
public class TextoPadraoBancoCurriculumControle extends SuperControle {

    private TextoPadraoBancoCurriculumVO textoPadraoBancoCurriculumVO;
    private Boolean existeTexto;

    public TextoPadraoBancoCurriculumControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Novo Texto Padrão Banco Curriculum", "Novo");
        setTextoPadraoBancoCurriculumVO(new TextoPadraoBancoCurriculumVO());
        getTextoPadraoBancoCurriculumVO().setTexto(getTextoPadraoBancoCurriculumVO().getMensagemComLayoutTextoPadrao());
        getTextoPadraoBancoCurriculumVO().setImgCima("cima_sei");
        getTextoPadraoBancoCurriculumVO().setImgBaixo("baixo_sei");
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoBancoCurriculumForm.xhtml");
    }

    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Inicializando Editar Texto Padrão Banco Curriculum", "Editando");
        TextoPadraoBancoCurriculumVO obj = (TextoPadraoBancoCurriculumVO) context().getExternalContext().getRequestMap().get("textoPadraoBancoCurriculumItens");
        setTextoPadraoBancoCurriculumVO(getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Finalizando Editar Texto Padrão Banco Curriculum", "Editando");
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoBancoCurriculumForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Parceiro</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getTextoPadraoBancoCurriculumVO().isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Inicializando Incluir Texto Padrão Banco Curriculum", "Incluindo");
                getFacadeFactory().getTextoPadraoBancoCurriculumFacade().incluir(getTextoPadraoBancoCurriculumVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Finalizando Incluir Texto Padrão Banco Curriculum", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Inicializando Alterar Texto Padrão Banco Curriculum", "Alterando");
                getFacadeFactory().getTextoPadraoBancoCurriculumFacade().alterar(getTextoPadraoBancoCurriculumVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Finalizando Alterar Texto Padrão Banco Curriculum", "Alterando");
            }
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String ativar() {
        try {
            getTextoPadraoBancoCurriculumVO().setSituacao("AT");
            getFacadeFactory().getTextoPadraoBancoCurriculumFacade().gravarSituacao(getTextoPadraoBancoCurriculumVO(), getUsuarioLogado());
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String cancelar() {
        try {
            getTextoPadraoBancoCurriculumVO().setSituacao("CA");
            getFacadeFactory().getTextoPadraoBancoCurriculumFacade().gravarSituacao(getTextoPadraoBancoCurriculumVO(), getUsuarioLogado());
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP VagasCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Inicializando Consultar Texto Padrão Banco Curriculum", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipo")) {
                objs = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipo(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Finalizando Consultar Texto Padrão Banco Curriculum", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoBancoCurriculumCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoBancoCurriculumCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ParceiroVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Inicializando Excluir Texto Padrão Banco Curriculum", "Excluindo");
            getFacadeFactory().getTextoPadraoBancoCurriculumFacade().excluir(getTextoPadraoBancoCurriculumVO(), getUsuarioLogado());
            setTextoPadraoBancoCurriculumVO(new TextoPadraoBancoCurriculumVO());
            setMensagemID("msg_dados_excluidos");
            registrarAtividadeUsuario(getUsuarioLogado(), "TextoPadraoBancoCurriculumControle", "Finalizando Excluir Texto Padrão Banco Curriculum", "Excluindo");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public List getListaSelectItemTipoVagas() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoVagas = (Hashtable) new Hashtable();
        Enumeration keys = tipoVagas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoVagas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("tipo", "Tipo"));
        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    public List getTipoConsultaComboSituacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("AT", "Ativada"));
        itens.add(new SelectItem("CA", "Cancelada"));
        itens.add(new SelectItem("EC", "Em construção"));
        return itens;
    }

    public List getTipoCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("boasVindasAluno", "Boas Vindas Aluno"));
        itens.add(new SelectItem("boasVindasParceiro", "Boas Vindas Parceiro"));
        itens.add(new SelectItem("notaAbaixoMedia", "Nota Abaixo da Média"));
        itens.add(new SelectItem("candidatoSelecionado", "Candidato Selecionado"));
        itens.add(new SelectItem("candidatoDesclassificado", "Candidato Desclassificado"));
        itens.add(new SelectItem("vagaEncerradaCandidato", "Vaga Encerrada para o Candidato"));
        itens.add(new SelectItem("vagaEncerradaParceiro", "Vaga Encerrada para o Parceiro"));
        itens.add(new SelectItem("alunoCandidatadoVaga", "Aluno Candidatado para Vaga"));
        itens.add(new SelectItem("vagaExpirada", "Expiração de Vaga"));
        itens.add(new SelectItem("iminenciaExpiracaoVaga", "Iminência de Expiração de Vaga"));
        itens.add(new SelectItem("iminenciaExpiracaoCurriculum", "Iminência de Expiração de Currículum"));
        itens.add(new SelectItem("notificarAlunoSugestaoVaga", "Notificar Aluno sobre Sugestão de Vaga"));
        return itens;
    }

    public List getListaSelectItemMarcadorAlunoBancoCurriculum() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadorAlunoBancoCurriculum();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public List getListaSelectItemMarcadorTituloBancoCurriculum() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadorTituloBancoCurriculum();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public List getListaSelectItemMarcadorEmpresaBancoCurriculum() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadorEmpresaBancoCurriculum();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public List getListaSelectItemMarcadorVagaBancoCurriculum() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadorVagaBancoCurriculum();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public void selecionarMarcadorAlunoBancoCurriculum() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorAlunoItens");
        String texto = adicionarMarcador(getTextoPadraoBancoCurriculumVO().getTexto(), obj.getTag());
        getTextoPadraoBancoCurriculumVO().setTexto(texto);
    }

    public void selecionarMarcadorTituloBancoCurriculum() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorTituloBancoCurriculumItens");
        String texto = adicionarMarcador(getTextoPadraoBancoCurriculumVO().getTexto(), obj.getTag());
        getTextoPadraoBancoCurriculumVO().setTexto(texto);
    }

    public void selecionarMarcadorEmpresaBancoCurriculum() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorUnidadeEnsinoItens");
        String texto = adicionarMarcador(getTextoPadraoBancoCurriculumVO().getTexto(), obj.getTag());
        getTextoPadraoBancoCurriculumVO().setTexto(texto);
    }

    public void selecionarMarcadorVagaBancoCurriculum() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorVagaItens");
        String texto = adicionarMarcador(getTextoPadraoBancoCurriculumVO().getTexto(), obj.getTag());
        getTextoPadraoBancoCurriculumVO().setTexto(texto);
    }

    public String adicionarMarcador(String texto, String marcador) {
        int parametro = texto.lastIndexOf("</p>");
        if (parametro == -1) {
            parametro = texto.lastIndexOf("</body>");
        }
        TextoPadraoTagVO p = new TextoPadraoTagVO();
        p.setTag(marcador);
        getTextoPadraoBancoCurriculumVO().getListaTagUtilizado().add(p);
        String textoAntes = texto.substring(0, parametro);
        String textoDepois = texto.substring(parametro, texto.length());
        texto = textoAntes + " " + marcador + textoDepois;
        return texto;
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoBancoCurriculumCons.xhtml");
    }

    public String adicionarMarcador() {
        setExisteTexto(Boolean.TRUE);
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoBancoCurriculumForm.xhtml");
    }

    public TextoPadraoBancoCurriculumVO getTextoPadraoBancoCurriculumVO() {
        if (textoPadraoBancoCurriculumVO == null) {
            textoPadraoBancoCurriculumVO = new TextoPadraoBancoCurriculumVO();
        }
        return textoPadraoBancoCurriculumVO;
    }

    public void setTextoPadraoBancoCurriculumVO(TextoPadraoBancoCurriculumVO textoPadraoBancoCurriculum) {
        this.textoPadraoBancoCurriculumVO = textoPadraoBancoCurriculum;
    }

    public boolean getAtivar() {
        if (getTextoPadraoBancoCurriculumVO().isNovoObj().booleanValue()) {
            return false;
        } else if (getTextoPadraoBancoCurriculumVO().getSituacao().equalsIgnoreCase("EC")) {
            return true;
        }

        return false;

    }

    public boolean getCancelar() {
        if (getTextoPadraoBancoCurriculumVO().getSituacao().equalsIgnoreCase("AT")) {
            return true;
        }
        return false;
    }

    public boolean getPossibilidadeGravar() {
        if (getTextoPadraoBancoCurriculumVO().getSituacao().equalsIgnoreCase("EC")) {
            return true;
        } else if (getTextoPadraoBancoCurriculumVO().isNovoObj().booleanValue()) {
            return true;
        }
        return false;

    }

    public boolean getPesquisaPorSituacao() {
        if (getControleConsulta().getCampoConsulta().equals("situacao")) {
            return true;
        }
        return false;
    }

    public boolean getPesquisaNaoPorSituacao() {
        if (getPesquisaPorSituacao()) {
            return false;
        }
        return true;
    }

    public Boolean getExisteTexto() {
        return existeTexto;
    }

    public void setExisteTexto(Boolean existeTexto) {
        this.existeTexto = existeTexto;
    }

    public boolean getEdicaoTipo() {
        if (getTextoPadraoBancoCurriculumVO().isNovoObj()) {
            return true;
        }
        return false;
    }

    public boolean getReadOnlyTipo() {
        if (getEdicaoTipo()) {
            return false;
        }
        return true;
    }
}
