package jobs;

import java.io.File;
import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobEmailComunicacaoInterna extends ControleAcesso implements Runnable {

    private ConfiguracaoGeralSistemaVO config;
    private ComunicacaoInternaVO comunicacaoInterna;
    private String assunto;
    private String mensagem;
    private String mensagemSemHashTag;
    private String nomeUsuario;
    private File anexo;
    private UsuarioVO usuarioLogado;
    private Boolean anexarImagensPadrao;
    private List<File> listaFileCorpoMensagem;

    public JobEmailComunicacaoInterna(ConfiguracaoGeralSistemaVO config, ComunicacaoInternaVO comunicacaoInterna, String assunto, UsuarioVO usuarioLogado, File anexo, String mensagem, Boolean anexarImagensPadrao, List<File> listaFileCorpoMensagem) {
        this.config = config;
        this.comunicacaoInterna = comunicacaoInterna;
        this.assunto = assunto;
        this.nomeUsuario = usuarioLogado.getNome();
        this.anexo = anexo;
        this.mensagem = mensagem;
        this.usuarioLogado = usuarioLogado;
        this.anexarImagensPadrao = anexarImagensPadrao;
        this.listaFileCorpoMensagem = listaFileCorpoMensagem;
    }

    @Override
    public void run() {
        String ipServidor = config.getIpServidor();
        String mailServer = config.getSmptPadrao();
        String login = config.getLogin();
        String senha = config.getSenha();
        String portaSmtpPadrao = config.getPortaSmtpPadrao();
        Boolean servidorEmailUtilizaSSL = config.getServidorEmailUtilizaSSL();
        executarAlteracaoNomeImagem();
        try {
            for (ComunicadoInternoDestinatarioVO cidVO : comunicacaoInterna.getComunicadoInternoDestinatarioVOs()) {
                try {
                    String nome = "";
                    String email = "";

                    if (!cidVO.getDestinatario().getCodigo().equals(0)) {
                        if (cidVO.getDestinatario().getEmail() == null || cidVO.getDestinatario().getEmail().trim().equals("")) {
                            cidVO.setDestinatario(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(cidVO.getDestinatario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
                        }
                        nome = cidVO.getDestinatario().getNome();
                        email = cidVO.getDestinatario().getEmail();
                    } else if (!cidVO.getDestinatarioParceiro().getCodigo().equals(0)) {
                        if (cidVO.getDestinatarioParceiro().getEmail() == null || cidVO.getDestinatarioParceiro().getEmail().trim().equals("")) {
                            cidVO.setDestinatarioParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(cidVO.getDestinatarioParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
                        }
                        nome = cidVO.getDestinatarioParceiro().getNome();
                        email = cidVO.getDestinatarioParceiro().getEmail();
                    }


                    if (!email.equals("") && Uteis.getValidaEmail(email)) {
                        if (mensagem.contains("#NOMEALUNO")) {
                            mensagemSemHashTag = Uteis.trocarHashTag("#NOMEALUNO", nome, mensagem);
                        } else {
                            mensagemSemHashTag = mensagem;
                        }
                        if (Uteis.validarEnvioEmail(ipServidor)) {
                            if (anexo == null) {
                                Uteis.enviarEmail(email, nome, comunicacaoInterna.getResponsavel().getEmail(), comunicacaoInterna.getResponsavel().getNome(), assunto, mensagemSemHashTag, mailServer,
                                        login, senha, anexarImagensPadrao, listaFileCorpoMensagem, portaSmtpPadrao, servidorEmailUtilizaSSL);
                            } else {
                                Uteis.enviarEmailAnexo(email, nome, comunicacaoInterna.getResponsavel().getEmail(), comunicacaoInterna.getResponsavel().getNome(), assunto, mensagemSemHashTag,
                                        anexo.getName(), anexo, mailServer, login, senha, anexarImagensPadrao, listaFileCorpoMensagem, portaSmtpPadrao, servidorEmailUtilizaSSL);
                            }
                        }
                    } else {
                        throw new ConsistirException("O e-mail do destinatário é inválido.");
                    }
                } catch (Exception e) {
                    //System.out.println("Msg não enviada...");
                    //System.out.println("MENSAGEM => " + e.getMessage());
                    ;
                }
            }
        } catch (Exception e) {
            //System.out.println("JobEmailCI - > Exception: ");
            //System.out.println("SmtpPadrão: " + config.getSmptPadrao());
            //System.out.println("Login Smtp: " + config.getLogin());
            //System.out.println("Senha Smtp: " + config.getSenha());
            //System.out.println("Porta Smtp: " + config.getPortaSmtpPadrao());
            //System.out.println("Utiliza SSL: " + config.getServidorEmailUtilizaSSL());
            //System.out.println("******Erro no envio de email: " + e);
            //System.out.println();
        }
    }

    public void executarAlteracaoNomeImagem() {
        String sb = "";
        sb = mensagem;
        if (comunicacaoInterna.getCaminhoImagemPadraoBaixo().contains(comunicacaoInterna.getImgBaixo())) {
            sb = sb.replace(comunicacaoInterna.getCaminhoImagemPadraoBaixo().replace("./", ""), "cid:" + comunicacaoInterna.getImgBaixo());
            if (sb.contains("./cid:" + comunicacaoInterna.getImgBaixo())) {
                sb = sb.replace("./cid:" + comunicacaoInterna.getImgBaixo(), "cid:" + comunicacaoInterna.getImgBaixo());
            }
        }
        if (comunicacaoInterna.getCaminhoImagemPadraoCima().contains(comunicacaoInterna.getImgCima())) {
            sb = sb.replace(comunicacaoInterna.getCaminhoImagemPadraoCima().replace("./", ""), "cid:" + comunicacaoInterna.getImgCima());
            if (sb.contains("./cid:" + comunicacaoInterna.getImgCima())) {
                sb = sb.replace("./cid:" + comunicacaoInterna.getImgCima(), "cid:" + comunicacaoInterna.getImgCima());
            }
        }
        mensagem = sb;
        sb = null;
    }
}
