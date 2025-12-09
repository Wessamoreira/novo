package negocio.comuns.utilitarias;

import java.io.File;
/**
 * @author Pedro
 */
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.faces. context.FacesContext;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Part;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.http.HttpServletRequest;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.basico.PessoaVO; public class UteisEmail implements Serializable {

    private PessoaVO remetente;
    private String emailRemet;
    private String nomeRemet;
    private String assunto;
    private String smtpPadrao;
    private String loginServidorSmtp;
    private String senhaServidorSmtp;
    private Map<String, File> mapaAnexo = new HashMap<String, File>();
    private List<File> listaImagem = new ArrayList<File>();

    public UteisEmail() throws Exception {
        super();
    }

    public UteisEmail novo(String assunto, ConfiguracaoGeralSistemaVO config) throws Exception {
        inicializarDados(config);
        this.assunto = assunto;
        return this;
    }

    /**
     * - Recupera as configurações de email, obtidas em configuracaoSistema.
     * - Recupera o nome do remetente padrão, o nome da instituição educacional.
     * - Se necessário formata a mensagem utilizando o template HTML padrão.
     *
     * @throws Exception
     */
    private void inicializarDados(ConfiguracaoGeralSistemaVO config) throws Exception {
        this.remetente = null;
        this.assunto = "";
        remetente = null;
        mapaAnexo = new HashMap<String, File>();
        listaImagem = new ArrayList<File>();
        preencherConfiguracaoEmailPadrao(config);
    }

    public void preencherConfiguracaoEmailPadrao(ConfiguracaoGeralSistemaVO config) throws Exception {
        if (config == null) {
            throw new Exception("");
        }
        // Setando configurações de email padrão
        loginServidorSmtp = config.getLogin();
        senhaServidorSmtp = config.getSenha();
        smtpPadrao = config.getSmptPadrao();
        emailRemet = "";
        nomeRemet = "";


        if (smtpPadrao == null || loginServidorSmtp == null || senhaServidorSmtp == null || emailRemet == null) {
            // verifico mais uma vez se existe uma configuracao de e-mail
            throw new Exception("");
        }
    }

    /**
     * Adiciona um anexo ao email.
     *
     * @param nomeAnexo
     * @param anexo
     * @return
     */
    public UteisEmail addAnexo(String nomeAnexo, File anexo) {
        this.mapaAnexo.put(nomeAnexo, anexo);
        return this;
    }

    /**
     * Caso este método nao seja utilizado, o rementente será a instituição
     * educacional.
     *
     * @param remetente
     * @return
     */
    public UteisEmail setRemetente(PessoaVO remetente) {
        this.remetente = remetente;
        this.nomeRemet = remetente.getNome();
        this.emailRemet = remetente.getEmail();
        return this;
    }

    /**
     * Adiciona todos os arquivos contidos no diretorio informado na lista de
     * imagens.
     *
     * @param diretorioImagens
     * @return
     */
    public UteisEmail addImagensEmDiretorio(File diretorioImagens) {
        for (File file : diretorioImagens.listFiles()) {
            listaImagem.add(file);
        }
        return this;
    }

    /**
     * Adiciona um arquivo de imagem ao email.
     *
     * @param imagem
     * @return
     */
    public UteisEmail addImagem(File imagem) {
        this.listaImagem.add(imagem);
        return this;
    }

    /**
     * Método que envia o email.
     *
     * @throws ConsistirException
     * @throws Exception
     */
    public void enviarEmail(String emailDest, String nomeDest, String mensagem) throws Exception {
        boolean debug = false;
        Session session = null;
        try {

            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", smtpPadrao);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.timeout", "30000");
            Authenticator auth = new Authenticator() {

                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(loginServidorSmtp, senhaServidorSmtp);
                }
            };

            session = Session.getDefaultInstance(props, auth);
            session.setDebug(debug);
            MimeMessage message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(emailRemet, nomeRemet));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDest, nomeDest));
            message.setSubject(assunto);

            MimeMultipart mpRoot = new MimeMultipart("mixed");
            MimeMultipart mpContent = new MimeMultipart("alternative");
            MimeBodyPart contentPartRoot = new MimeBodyPart();
            contentPartRoot.setContent(mpContent);
            mpRoot.addBodyPart(contentPartRoot);

            //enviando html
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(mensagem, "text/html");
            mpContent.addBodyPart(mbp1);

            // enviando anexo
            if (!mapaAnexo.isEmpty()) {
                for (String nomeAnexo : mapaAnexo.keySet()) {
                    File anexo = mapaAnexo.get(nomeAnexo);

                    MimeBodyPart mbp2 = new MimeBodyPart();
                    DataSource fds = new FileDataSource(anexo);
                    mbp2.setDisposition(Part.ATTACHMENT);
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(nomeAnexo);
                    mpRoot.addBodyPart(mbp2);
                }
            }

            // adicionando as imagens do html
            if (!listaImagem.isEmpty()) {
                for (File imagem : listaImagem) {
                    BodyPart imagePart = new MimeBodyPart();
                    DataSource imgFds = new FileDataSource(imagem);
                    imagePart.setDataHandler(new DataHandler(imgFds));
                    imagePart.setHeader("Content-ID", "<" + imagem.getName() + ">");
                    mpContent.addBodyPart(imagePart);
                }
            }

            message.setContent(mpRoot);
            message.saveChanges();

            Transport.send(message);
        } catch (AuthenticationFailedException e) {
            throw new ConsistirException("As configurações de email estão incorretas, entre em contato com o administrador.");
        } catch (Exception e) {
            // tratar
            if (e.getMessage().contains("Access to default session denied")) {
                throw new Exception("");
            }
            throw e;
        }
    }

    /**
     * 
     * @param email
     * @return
     * 				true: Email valido <br>
     * 				false: Email invalido
     */
    public static boolean getValidEmail(String email) {
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(email);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    protected void limparRecursosMemoria() {
        this.emailRemet = null;
        this.nomeRemet = null;
        this.assunto = null;
        this.smtpPadrao = null;
        this.loginServidorSmtp = null;
        this.senhaServidorSmtp = null;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getEmailRemet() {
        return emailRemet;
    }

    public String getNomeRemet() {
        return nomeRemet;
    }

    public PessoaVO getRemetente() {
        return remetente;
    }

    public static String getURLAplicacao(String diretorio)  {
        return getURLAplicacao() + diretorio;
    }

    public static String getURLAplicacao()  {
         HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
         StringBuffer urlAplicacao = request.getRequestURL();
        String url = urlAplicacao.toString();
         url = url.substring(0, urlAplicacao.lastIndexOf("/"));
         url = url.replace("localhost", UteisEmail.getLocalhostIP());
         return url;
     }

    public static String getLocalhostIP() {
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
            // localHost.getHostName();
            return localHost.getHostAddress();
        } catch (UnknownHostException ex) {
            return "localhost";
        }
    }
}
