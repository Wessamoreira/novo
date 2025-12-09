package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas controleCobrancaForm.jsp controleCobrancaCons.jsp) com as funcionalidades da classe <code>ControleCobranca</code>
 * . Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ControleCobranca
 * @see ControleCobrancaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.enumerador.MensagensErroRetornoRemessaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.TipoControleCobranca;

@Controller("ControleMovimentacaoRemessaControle")
@Scope("viewScope")
@Lazy
public class ControleMovimentacaoRemessaControle extends SuperControle implements Serializable {

    private UsuarioVO usuarioVO;
    private ControleCobrancaVO controleCobrancaVO;
    private Boolean arquivoProcessado;

    public ControleMovimentacaoRemessaControle() throws Exception {
        if (getControleConsulta().getCampoConsulta().equals("")) {
            getControleConsulta().setCampoConsulta("codigo");
        }
    }

    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setControleCobrancaVO(new ControleCobrancaVO());
        setArquivoProcessado(false);
        getControleCobrancaVO().setResponsavel(getUsuarioLogadoClone());
        getControleCobrancaVO().setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
        getControleCobrancaVO().setMovimentacaoRemessaRetorno(Boolean.TRUE);
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaForm.xhtml");
    }

    public String editar() {
        try {
            ControleCobrancaVO obj = (ControleCobrancaVO) context().getExternalContext().getRequestMap().get("controleCobrancaItens");
            obj.setNovoObj(Boolean.FALSE);
            setControleCobrancaVO(obj);
            setControleCobrancaVO(getControleCobrancaVO());
            getControleCobrancaVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), false));
            setArquivoProcessado(getControleCobrancaVO().getRegistroArquivoVO().getArquivoProcessado());
            for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs()) {
                List<String> motivosRejeicao_Apresentar = MensagensErroRetornoRemessaEnum.getMensagem(contaReceberRegistroArquivoVO.getMotivoRejeicao());
                contaReceberRegistroArquivoVO.setMotivoRejeicao_Apresentar(motivosRejeicao_Apresentar);
            }
            setMensagemID("msg_dados_editar");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaCons.xhtml");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaForm.xhtml");
    }

    public String gravar() {
        try {
            getFacadeFactory().getControleCobrancaFacade().incluir(getControleCobrancaVO(), null, getControleCobrancaVO().getRegistroArquivoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), null, null);
            for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivo : getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs()) {
                getFacadeFactory().getContaReceberFacade().alterarNossoNumeroBancoContasValidadas(contaReceberRegistroArquivo.getContaReceberVO().getCodigo(), contaReceberRegistroArquivo.getContaReceberVO().getNossoNumeroBanco(), getUsuarioLogado());
            }
            for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivo : getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoRejeitadasVOs()) {
                getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(contaReceberRegistroArquivo.getContaReceberVO().getCodigo(), getUsuarioLogado());
            }
            novo();
            setMensagemID("msg_dados_gravados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String excluir() {
        try {
            getFacadeFactory().getControleCobrancaFacade().excluir(controleCobrancaVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaForm.xhtml");
        }
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorCodigo(true, new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
                objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorDataProcessamento(true, getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeArquivo")) {
                objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorNomeArquivo(true, getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleMovimentacaoRemessaCons.xhtml");
        }
    }

    public String processarArquivo() {
        try {
            getControleCobrancaVO().setRegistroArquivoVO(getFacadeFactory().getControleMovimentacaoRemessaInterfaceFacade().processarArquivo(getControleCobrancaVO(), getCaminhoPastaArquivosCobranca(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioVO()));
            for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoRejeitadasVOs()) {
                List<String> motivosRejeicao_Apresentar = MensagensErroRetornoRemessaEnum.getMensagem(contaReceberRegistroArquivoVO.getMotivoRejeicao());
                contaReceberRegistroArquivoVO.setMotivoRejeicao_Apresentar(motivosRejeicao_Apresentar);
            }
            setArquivoProcessado(Boolean.TRUE);
            getControleCobrancaVO().getRegistroArquivoVO().setArquivoProcessado(Boolean.TRUE);
            getControleCobrancaVO().setDataProcessamento(new Date());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setArquivoProcessado(Boolean.FALSE);
            getControleCobrancaVO().getRegistroArquivoVO().setArquivoProcessado(Boolean.FALSE);
        }
        return "";
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("dataProcessamento", "Data do processamento"));
        itens.add(new SelectItem("nomeArquivo", "Nome do arquivo"));
        return itens;
    }

    public void upLoadArquivo(FileUploadEvent upload) {
    	UploadedFile item = upload.getUploadedFile();
        try {
            getControleCobrancaVO().setArquivo(item.getData());
            getControleCobrancaVO().setNomeArquivo(Uteis.getNomeArquivo(item.getName()));
        } catch (Exception e) {
            setMensagemID("");
            setMensagem("");
            setMensagemDetalhada(e.getMessage());
        } finally {
            upload = null;
            item = null;
        }
    }

    public boolean getIsArquivoSelecionado() {
        if ((getControleCobrancaVO().getNomeArquivo() != null && !getControleCobrancaVO().getNomeArquivo().equals(""))
                || (getControleCobrancaVO().getNomeArquivo() != null && !getControleCobrancaVO().getNomeArquivo().equals(""))) {
            return true;
        }
        return false;
    }

    public boolean getIsConsultaPorNumeroSelecionado() {
        if (getControleConsulta().getCampoConsulta().equals("codigo")) {
            return true;
        }
        return false;
    }

    public List<SelectItem> getListaSelectItemLayoutsBancos() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(Bancos.class, "codigo", "nome", true);
        for (int i = 0; i < lista.size(); i++) {
            if (!lista.get(i).getValue().equals(10) && !lista.get(i).getValue().equals(7)){
                lista.remove(i);
                i--;
            }
        }
        return lista;
    }

    public List<SelectItem> getListaSelectItemTipoControleCobranca() {
        return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoControleCobranca.class);
    }

    public boolean getIsConsultaPorNomeArquivoSelecionado() {
        if (getControleConsulta().getCampoConsulta().equals("nomeArquivo")) {
            return true;
        }
        return false;
    }

    public boolean getIsConsultaPorDataProcessamentoSelecionado() {
        if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
            return true;
        }
        return false;
    }

    public void limparCampoConsulta() {
        getControleConsulta().setValorConsulta("");
        getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
        getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
    }

    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = new UsuarioVO();
        }
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    public ControleCobrancaVO getControleCobrancaVO() {
        if (controleCobrancaVO == null) {
            controleCobrancaVO = new ControleCobrancaVO();
        }
        return controleCobrancaVO;
    }

    public void setControleCobrancaVO(ControleCobrancaVO controleCobrancaVO) {
        this.controleCobrancaVO = controleCobrancaVO;
    }

    public Boolean getArquivoProcessado() {
        if (arquivoProcessado == null) {
            arquivoProcessado = Boolean.FALSE;
        }
        return arquivoProcessado;
    }

    public void setArquivoProcessado(Boolean arquivoProcessado) {
        this.arquivoProcessado = arquivoProcessado;
    }

    public Boolean getPossibilidadeGravar() {
        if (getControleCobrancaVO().isNovoObj()) {
            return true;
        }
        return false;
    }
}
