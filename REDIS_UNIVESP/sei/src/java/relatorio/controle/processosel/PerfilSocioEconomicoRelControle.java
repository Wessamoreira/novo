package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.interfaces.processosel.PerfilSocioEconomicoRelInterfaceFacade;
import relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRel;

@Controller("PerfilSocioEconomicoRelControle")
@Scope("request")
@Lazy
public class PerfilSocioEconomicoRelControle extends SuperControleRelatorio {

    protected PerfilSocioEconomicoRelInterfaceFacade perfilSocioEconomicoRel;
    protected List listaSelectItemUnidadeEnsino;

    public PerfilSocioEconomicoRelControle() throws Exception {
        setPerfilSocioEconomicoRel(new PerfilSocioEconomicoRel());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirObjetoPDF() throws Exception {
        try {
            List listaRegistro = perfilSocioEconomicoRel.emitirRelatorio();
            String nomeRelatorio = PerfilSocioEconomicoRel.getIdEntidade();
            String titulo = "Perfil Sócio Ecônomico";
            String design = perfilSocioEconomicoRel.getDesignIReportRelatorio();
            apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "", "PDF", "/" + PerfilSocioEconomicoRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), "", listaRegistro,
                    perfilSocioEconomicoRel.getCaminhoBaseRelatorio());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void imprimirObjetoHTML() throws Exception {
        try {
            List listaRegistro = perfilSocioEconomicoRel.emitirRelatorio();
            String nomeRelatorio = PerfilSocioEconomicoRel.getIdEntidade();
            String titulo = "Perfil Sócio Ecônomico";
            String design = perfilSocioEconomicoRel.getDesignIReportRelatorio();
            apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "", "HTML", "/" + PerfilSocioEconomicoRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), "", listaRegistro,
                    perfilSocioEconomicoRel.getCaminhoBaseRelatorio());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
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
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, perfilSocioEconomicoRel.getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
        return lista;
    }

    public void inicializarListasSelectItemTodosComboBox() {

        montarListaSelectItemUnidadeEnsino();
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        Uteis.liberarListaMemoria(getListaSelectItemUnidadeEnsino());
        perfilSocioEconomicoRel = null;
    }

    public PerfilSocioEconomicoRelInterfaceFacade getPerfilSocioEconomicoRel() {
        return perfilSocioEconomicoRel;
    }

    public void setPerfilSocioEconomicoRel(PerfilSocioEconomicoRelInterfaceFacade perfilSocioEconomicoRel) {
        this.perfilSocioEconomicoRel = perfilSocioEconomicoRel;
    }
}
