package jobs;

import java.io.File;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.utilitarias.Uteis;

public class JobEmail implements Runnable {

    private ConfiguracaoGeralSistemaVO config;
    private TurmaVO turma;
    private String corpoMensagem;
    private File anexo;
    private ComunicacaoInternaVO comunicacaoInterna;
    private Boolean anexarImagensPadrao;
    private List<File> listaFileCorpoMensagem;

    public JobEmail(ConfiguracaoGeralSistemaVO config, TurmaVO turma, ComunicacaoInternaVO comunicacaoInterna, String corpoMensagem, File anexo, Boolean anexarImagensPadrao, List<File> listaFileCorpoMensagem) {
        this.config = config;
        this.turma = turma;
        this.corpoMensagem = corpoMensagem;
        this.anexo = anexo;
        this.comunicacaoInterna = comunicacaoInterna;
        this.anexarImagensPadrao = anexarImagensPadrao;
        this.listaFileCorpoMensagem = listaFileCorpoMensagem;
    }

    @Override
    public void run() {
        String ipServidor = config.getIpServidor();
        String mailServer = config.getSmptPadrao();
        String emailRemetente = config.getEmailRemetente();
        String login = config.getLogin();
        String senha = config.getSenha();
        String portaSmtpPadrao = config.getPortaSmtpPadrao();
        Boolean servidorEmailUtilizaSSL = config.getServidorEmailUtilizaSSL();
        executarAlteracaoNomeImagem();
        for (FuncionarioGrupoDestinatariosVO fgdVO : turma.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs()) {
            try {
                if (!fgdVO.getFuncionario().getPessoa().getEmail().equals("")) {
                    try {

                        if (Uteis.validarEnvioEmail(ipServidor)) {
                            Uteis.enviarEmailAnexo(
                                    fgdVO.getFuncionario().getPessoa().getEmail(),
                                    fgdVO.getFuncionario().getPessoa().getNome(),
                                    emailRemetente,
                                    comunicacaoInterna.getResponsavel().getNome(),
                                    comunicacaoInterna.getAssunto(),
                                    corpoMensagem, anexo.getName(), anexo, mailServer, login, senha, anexarImagensPadrao, listaFileCorpoMensagem, portaSmtpPadrao, servidorEmailUtilizaSSL);
                        }
                    } catch (Exception e) {
                        //System.out.println("MENSAGEM => " + e.getMessage());

                    }
                }
                if (!fgdVO.getFuncionario().getPessoa().getEmail2().equals("")) {
                    try {
                        if (Uteis.validarEnvioEmail(ipServidor)) {
                            Uteis.enviarEmailAnexo(
                                    fgdVO.getFuncionario().getPessoa().getEmail2(),
                                    fgdVO.getFuncionario().getPessoa().getNome(),
                                    emailRemetente,
                                    comunicacaoInterna.getResponsavel().getNome(),
                                    comunicacaoInterna.getAssunto(),
                                    corpoMensagem, anexo.getName(), anexo, mailServer, login, senha, anexarImagensPadrao, listaFileCorpoMensagem, portaSmtpPadrao, servidorEmailUtilizaSSL);
                        }
                    } catch (Exception e) {
                        //System.out.println("MENSAGEM => " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                //System.out.println("Msg não enviada para: Nome - " + fgdVO.getFuncionario().getPessoa().getNome() + " Email - " + fgdVO.getFuncionario().getPessoa().getEmail() + ", " + fgdVO.getFuncionario().getPessoa().getEmail2());
                //System.out.println("MENSAGEM => " + e.getMessage());

            }
        }
    }

    public void executarAlteracaoNomeImagem() {
        String sb = "";
        sb = corpoMensagem;
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
        corpoMensagem = sb;
        sb = null;
    }
}
