package controle.financeiro;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas modeloBoletoForm.jsp
 * modeloBoletoCons.jsp) com as funcionalidades da classe <code>ModeloBoleto</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see ModeloBoleto
 * @see ModeloBoletoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("ModeloBoletoControle")
@Scope("viewScope")
@Lazy
public class ModeloBoletoControle extends SuperControle implements Serializable {

    private ModeloBoletoVO modeloBoletoVO;

    public ModeloBoletoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ModeloBoleto</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setModeloBoletoVO(new ModeloBoletoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ModeloBoleto</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        ModeloBoletoVO obj = (ModeloBoletoVO) context().getExternalContext().getRequestMap().get("modeloBoletoItens");
        obj.setNovoObj(Boolean.FALSE);
        setModeloBoletoVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ModeloBoleto</code>. Caso
     * o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (modeloBoletoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getModeloBoletoFacade().incluir(modeloBoletoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getModeloBoletoFacade().alterar(modeloBoletoVO, getUsuarioLogado());
                getAplicacaoControle().atualizarModeloBoletoConfiguracaoFinanceiraEmNivelAplicacao(modeloBoletoVO);
                getAplicacaoControle().atualizarModeloBoletoBancoAgenciaContaCorrente(modeloBoletoVO);
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ModeloBoletoCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getModeloBoletoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getModeloBoletoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ModeloBoletoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getModeloBoletoFacade().excluir(modeloBoletoVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoForm.xhtml");
        }
    }

    public void uploadImagem(FileUploadEvent event) throws Exception {
        try {
            getModeloBoletoVO().uploadImagem(event);
            setMensagemID("msg_imagem_carregada");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            event = null;
        }
    }

    public void criarBuffer(OutputStream out, Object data) throws Exception {
        byte[] foto = getModeloBoletoVO().getImagem();
        File arquivo = null;
        String extensaoArquivo = null;
        BufferedImage img = null;
        FileImageInputStream in = null;
        try {
            if (foto == null) {
                arquivo = new File(obterCaminhoWebImagem() + File.separator + "imagemModelo.png");
                in = new FileImageInputStream(arquivo);
                foto = new byte[(int) arquivo.length()];
                in.readFully(foto);
                extensaoArquivo = Uteis.getExtensaoDeArquivo(arquivo);
            } else {
                extensaoArquivo = getModeloBoletoVO().getExtensaoImagem();
            }

            img = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(foto)));
            ImageIO.write(img, extensaoArquivo, out);
        } catch (Exception e) {
        } finally {
            foto = null;
            arquivo = null;
            extensaoArquivo = null;
            if (in != null) {
                in.close();
            }
            img = null;
            in = null;
        }
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Número"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("modeloBoletoCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        modeloBoletoVO = null;
    }

    public void paint(OutputStream out, Object data) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(getModeloBoletoVO().getImagem())));
        ImageIO.write(bufferedImage, "jpg", out);
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public ModeloBoletoVO getModeloBoletoVO() {
        return modeloBoletoVO;
    }

    public void setModeloBoletoVO(ModeloBoletoVO modeloBoletoVO) {
        this.modeloBoletoVO = modeloBoletoVO;
    }
}
