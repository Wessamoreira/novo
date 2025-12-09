package controle.academico;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MotivoIndeferimentoDocumentoAlunoVO;
import negocio.comuns.utilitarias.Uteis;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller("MotivoIndeferimentoDocumentoAlunoControle")
@Scope("viewScope")
@Lazy
public class MotivoIndeferimentoDocumentoAlunoControle extends SuperControle {

    private static final long serialVersionUID = -7275901163094133826L;
    private MotivoIndeferimentoDocumentoAlunoVO motivoIndeferimentoDocumentoAlunoVO;
    private static final String TELA_CONS = "motivoIndeferimentoDocumentoAlunoCons.xhtml";
    private static final String TELA_FORM = "motivoIndeferimentoDocumentoAlunoForm.xhtml";
    private static final String CONTEXT = "motivoIndeferimentoDocumentoAlunoItens";

    public MotivoIndeferimentoDocumentoAlunoVO getMotivoIndeferimentoDocumentoAlunoVO() {
        if (motivoIndeferimentoDocumentoAlunoVO == null) {
            motivoIndeferimentoDocumentoAlunoVO = new MotivoIndeferimentoDocumentoAlunoVO();
        }
        return motivoIndeferimentoDocumentoAlunoVO;
    }

    public void setMotivoIndeferimentoDocumentoAlunoVO(MotivoIndeferimentoDocumentoAlunoVO motivoIndeferimentoDocumentoAlunoVO) {
        this.motivoIndeferimentoDocumentoAlunoVO = motivoIndeferimentoDocumentoAlunoVO;
    }

    public String consultar() {
        try {
            super.consultar();
            getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().consultar(getControleConsultaOtimizado(), getMotivoIndeferimentoDocumentoAlunoVO());
            setMensagemID(MSG_TELA.msg_dados_consultados.name());
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
        return "";
    }

    public String editar() {
        try {
            MotivoIndeferimentoDocumentoAlunoVO obj = (MotivoIndeferimentoDocumentoAlunoVO) context().getExternalContext().getRequestMap().get(CONTEXT);
            setMotivoIndeferimentoDocumentoAlunoVO(obj);
            setMensagemID(MSG_TELA.msg_dados_editar.name());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return "";
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
    }

    public String novo() {
        removerObjetoMemoria(this);
        setMotivoIndeferimentoDocumentoAlunoVO(new MotivoIndeferimentoDocumentoAlunoVO());
        setMensagemID(MSG_TELA.msg_entre_dados.name());
        return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
    }

    public void scrollerListener(DataScrollEvent dataScrollerEvent) {
        try {
            getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
            getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
            consultar();
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setControleConsultaOtimizado(new DataModelo());
        setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
        return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
    }

    public void persistir() {
        try {
            getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().persistir(getMotivoIndeferimentoDocumentoAlunoVO(), true, getUsuarioLogado());
            setMensagemID(MSG_TELA.msg_dados_gravados.name());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void excluir() {
        try {
            getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().excluir(getMotivoIndeferimentoDocumentoAlunoVO(), true, getUsuarioLogado());
            novo();
            setMensagemID(MSG_TELA.msg_dados_excluidos.name());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }
}