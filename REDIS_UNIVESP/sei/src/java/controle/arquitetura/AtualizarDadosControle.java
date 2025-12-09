/**
 * CLASSE ALTERADA MANUALMENTE PARA:
 *   - Adicionar o atributo pessoa a entidade Usuário. Necessário para manter um vínculo entre usuário a pessoa real que está
 *     acessando o sistema.
 */
package controle.arquitetura;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * usuarioForm.jsp usuarioCons.jsp) com as funcionalidades da classe <code>Usuario</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Usuario
 * @see UsuarioVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ControleConsulta; @Controller("AtualizarDadosControle")
@Scope("request")
@Lazy
public class AtualizarDadosControle extends SuperControle implements Serializable {

    private UsuarioVO usuarioVO;
    private List listaTurmaFinalizada;
    private String valorParametro;
    private String mesReferencia;
    private String nomeDisciplina;
    private String nomeCurso;

    public AtualizarDadosControle() throws Exception {
        //obterUsuarioLogado();
        setListaTurmaFinalizada(new ArrayList(0));
        setControleConsulta(new ControleConsulta());
        novo();
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Usuario</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         removerObjetoMemoria(this);
        setUsuarioVO(new UsuarioVO());
        setValorParametro("X");
        setMesReferencia("");
        setNomeDisciplina("");
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    public void setarCondicaoPagamento() {
        try {
            getFacadeFactory().getMatriculaPeriodoFacade().processarRotinaSetarCondicaoPagamentoAluno(getUsuarioLogadoClone());
            setMensagemID("msg_ProcessamentoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void processarTurmaMatricula() {
        try {
            getFacadeFactory().getMatriculaFacade().processarRotinaSetarTurmaMatricula(getUsuarioLogadoClone());
            setMensagemID("msg_ProcessamentoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por remover as contas receber duplicadas de um determinado mês
     * hoje ela é utilizada para corrigir uma falta de controle de concorrência da rotina de geração de parcelas mês a mês.
     */
    public void processarContasReceberDuplicadas() {
        try {
            if (getMesReferencia() == null || getMesReferencia().equals("")) {
                throw new Exception("O campo mês referencia deve ser informado!");
            }
            getFacadeFactory().getContaReceberFacade().processarRotinaRemoverContaReceberDuplicada(getMesReferencia(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            setMensagemID("msg_ProcessamentoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por processar as matricula periodos vinculadas a uma turma X
     * e assim altera-las para as turma equivalentes sem X no nome
     */
    public void processarTurmaXMatriculaPeriodoTurmaDisciplina() {
        try {
            Hashtable hash = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().processarRotinaTurmaXMatriculaPeriodoTurmaDisciplina(valorParametro, getUsuarioLogado());
            setListaTurmaFinalizada(gerarList(hash));
            setMensagemID("msg_ProcessamentoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

     public void processarOrganizacaoHistoricosAlunos() {
        try {
            getFacadeFactory().getHistoricoFacade().processarOrganizacaoHistoricosAlunos(getNomeCurso(), getUsuarioLogado());
            setMensagemID("msg_ProcessamentoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public boolean getApresentarListaFinalizada() {
        if (getListaTurmaFinalizada() == null) {
            return false;
        } else if (getListaTurmaFinalizada().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    public List gerarList(Hashtable hash) {
        List lista = new ArrayList(0);
        Enumeration e = hash.elements();
        while (e.hasMoreElements()) {
            String value = (String)e.nextElement();
            lista.add(value);
        }
        return lista;
    }
    public UsuarioVO getUsuarioVO() {
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    /**
     * @return the listaTurmaFinalizada
     */
    public List getListaTurmaFinalizada() {
        return listaTurmaFinalizada;
    }

    /**
     * @param listaTurmaFinalizada the listaTurmaFinalizada to set
     */
    public void setListaTurmaFinalizada(List listaTurmaFinalizada) {
        this.listaTurmaFinalizada = listaTurmaFinalizada;
    }

    /**
     * @return the valorParametro
     */
    public String getValorParametro() {
        return valorParametro;
    }

    /**
     * @param valorParametro the valorParametro to set
     */
    public void setValorParametro(String valorParametro) {
        this.valorParametro = valorParametro;
    }

    /**
     * @return the mesReferencia
     */
    public String getMesReferencia() {
        return mesReferencia;
    }

    /**
     * @param mesReferencia the mesReferencia to set
     */
    public void setMesReferencia(String mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    /**
     * @return the nomeDisciplina
     */
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    /**
     * @param nomeDisciplina the nomeDisciplina to set
     */
    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

}
