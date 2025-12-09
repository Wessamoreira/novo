package jobs;

import java.io.File;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import javax.net.ssl.SSLException;
import javax.xml.bind.DatatypeConverter;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPSendFailedException;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import jobs.enumeradores.FornecedorSmsEnum;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import sms.FacilitaSms;
import sms.HumanSms;
import sms.LocaSms;
import sms.RobbuSms;

@Service("jobEnvioEmail")
public class JobEnvioEmail extends SuperFacadeJDBC implements Serializable {

	private static final long serialVersionUID = 1L;
	private String smtpPadrao;
	private String ipServidor;
	private String loginServidorSmtp;
	private String senhaServidorSmtp;
	private String portaSmtpPadrao;
	private String emailRemetente;
	private List<File> listaFileCorpoMensagem;
	private List<String> listaAnexosExcluir;
	private String codigoEmails;		
	private Session session;
	
	private void executarRegistroExecucaoJob(RegistroExecucaoJobVO registro) {
		try {
			getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registro);
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
		}
	}

	public void executarThread() {
		List<EmailVO> listaEmail = new ArrayList<>();
		RegistroExecucaoJobVO registro = new RegistroExecucaoJobVO();
		registro.setNome(JobsEnum.JOB_ENVIAR_EMAIL.getName());
		registro.setDataInicio(new Date());
		StringBuilder logExecucaoJobEmail = new StringBuilder();
		Stopwatch stopwatch = new Stopwatch();
		long id = new Date().getTime();		
		try {
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando thread com id " + id);
	        Calendar calendarStart = Calendar.getInstance();
	        calendarStart.setTime(new Date());
			stopwatch.start();
			AplicacaoControle.setDataExecucaoJobEmail(new Date());
			ConfiguracaoGeralSistemaVO config = null;
			try {
				config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
			} catch (Exception dae) {
			}
			if(config != null) {
				ipServidor = config.getIpServidor();
				smtpPadrao = config.getSmptPadrao();
				loginServidorSmtp = config.getLogin();
				emailRemetente = config.getEmailRemetente();
				senhaServidorSmtp = config.getSenha();
				portaSmtpPadrao = config.getPortaSmtpPadrao();
				listaAnexosExcluir = new ArrayList<String>();
				codigoEmails = "";
				if (!Uteis.validarEnvioEmail((ipServidor))) {
					throw new Exception(UteisJSF.internacionalizar("prt_JobEnvioEmail_ErroValidarEnvioEmail"));
				}
				listaEmail = consultarEmails(config, false, false);
				if (listaEmail.isEmpty()) {
					listaEmail = consultarEmails(config, true, false);
				}
				if (!listaEmail.isEmpty()) {
					realizarInicializacaoSessao(config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), config.getAtivarDebugEmail());
					enviarEmails( listaEmail, config, registro, logExecucaoJobEmail);
				}
				List<SMSVO> listaSMS = consultarSMSs(config, false);
				if (Uteis.isAtributoPreenchido(listaSMS)) {
					enviarSMSs(listaSMS, config);
				}
				Uteis.liberarListaMemoria(listaEmail);
				Uteis.liberarListaMemoria(listaSMS);
			}
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
			logExecucaoJobEmail.append("Envio de email encerrado com erro: ").append(e.getMessage()).append("\n");
		} finally {			
			session =  null;
	        Calendar calendarFinally = Calendar.getInstance();
	        calendarFinally.setTime(new Date());
			stopwatch.stop();
			registro.setDataTermino(new Date());
			if (stopwatch != null) {
				registro.setTempoExecucao(stopwatch.getElapsedTicks());
			}
			registro.setErro(logExecucaoJobEmail.toString());
			executarRegistroExecucaoJob(registro);
			stopwatch = null;
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Finalizando job com id " + id);
		}
	}

	public void enviarEmails( final List<EmailVO> listaEmail, final ConfiguracaoGeralSistemaVO config, RegistroExecucaoJobVO registro, StringBuilder logExecucaoJobEmail) {
		registro.setTotal(listaEmail.size());
		Stopwatch stopwatchEmail = new Stopwatch();
		stopwatchEmail.start();						
		try {
			if (!listaEmail.isEmpty() && Uteis.validarEnvioEmail(ipServidor)) {
				if(config.getHabilitarEnvioEmailAssincrono()) {				
				final ConsistirException ex = new ConsistirException();
				ProcessarParalelismo.executar(0, listaEmail.size(),  50, config.getTimeOutFilaEmail(), ex, new ProcessarParalelismo.Processo() {
					@Override
					public void run(int i) { 
						EmailVO email = listaEmail.get(i);
					try {
						AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando envio email " + email.getEmailDest());
						validarEmail(email);
						if (email.getMultiplosDestinatarios()) {
							enviarEmailMultiplosDestinatarios(email.getEmailDest(), email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(), email.getMensagem(), email.getCaminhoAnexos(), email.getAnexarImagensPadrao(), config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo(), null, config.getAtivarDebugEmail());
						} else {
							enviarEmail(email.getEmailDest(), email.getNomeDest(), email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(), email.getMensagem(), email.getCaminhoAnexos(), email.getAnexarImagensPadrao(), config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo(), null, config.getAtivarDebugEmail());
						}						//registro.setTotalSucesso(registro.getTotalSucesso() + 1);						
						try {
							realizarExclusaoEmail(email);
						} catch (Exception e) {
							e.printStackTrace();
							//logExecucaoJobEmail.append("Falha ao enviar email (").append(email.getEmailDest()).append("): ").append(e.getMessage()).append("\n");
						}
					} catch (Exception e) {
						ex.adicionarListaMensagemErro("Falha ao enviar email ("+(email.getEmailDest())+"): "+e.getMessage()+"\n");
						AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
						//registro.setTotalErro(registro.getTotalErro() + 1);
						//logExecucaoJobEmail.append("Falha ao enviar emails: ").append(e.getMessage()).append("\n");
						try {
							getFacadeFactory().getEmailFacade().atualizarCampoErroEmail(email.getCodigo(), e.getMessage());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Finalizando envio email " + email.getEmailDest());
				//	contador++;
				}
					});
				
				registro.setTotal(listaEmail.size());
				registro.setTotalSucesso(listaEmail.size()-ex.getListaMensagemErro().size());
				registro.setTotalErro(ex.getListaMensagemErro().size());
				ex.getListaMensagemErro().forEach(e -> logExecucaoJobEmail.append(e));
				registro.setErro(logExecucaoJobEmail.toString());
				}else {
					for(EmailVO email : listaEmail) {
					try {
						AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando envio email " + email.getEmailDest());
						validarEmail(email);
						if (email.getMultiplosDestinatarios()) {
							enviarEmailMultiplosDestinatarios(email.getEmailDest(), email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(), email.getMensagem(), email.getCaminhoAnexos(), email.getAnexarImagensPadrao(), config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo(), null, config.getAtivarDebugEmail());
						} else {
							enviarEmail(email.getEmailDest(), email.getNomeDest(), email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(), email.getMensagem(), email.getCaminhoAnexos(), email.getAnexarImagensPadrao(), config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo(), null, config.getAtivarDebugEmail());
						}	
						registro.setTotalSucesso(registro.getTotalSucesso() + 1);
						try {
							realizarExclusaoEmail(email);
						} catch (Exception e) {
							e.printStackTrace();
							logExecucaoJobEmail.append("Falha ao enviar email (").append(email.getEmailDest()).append("): ").append(e.getMessage()).append("\n");
						}
					} catch (Exception e) {
						AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
						registro.setTotalErro(registro.getTotalErro() + 1);
						logExecucaoJobEmail.append("Falha ao enviar emails: ").append(e.getMessage()).append("\n");
						try {
							getFacadeFactory().getEmailFacade().atualizarCampoErroEmail(email.getCodigo(), e.getMessage());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Finalizando envio email " + email.getEmailDest());
					}
				}
				
//				realizarExclusaoAnexo();
			}
		} catch (Exception e) {			
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
		} finally {
			smtpPadrao = null;
			loginServidorSmtp = null;
			emailRemetente = null;
			senhaServidorSmtp = null;
			portaSmtpPadrao = null;
			listaFileCorpoMensagem = null;
			listaAnexosExcluir = null;
			stopwatchEmail = null;			
		}
	}
	
	public void validarEmail(EmailVO email) throws Exception {
		if (!email.getMultiplosDestinatarios() && !Uteis.getValidaEmail(email.getEmailDest())) {
			realizarExclusaoEmail(email);
			throw new Exception("Email " + email.getEmailDest() + " inválido");
		}
	}

	public void enviarSMSs(final List<SMSVO> listaSMS, final ConfiguracaoGeralSistemaVO config) {
		try {
			if (!listaSMS.isEmpty() && Uteis.validarEnvioEmail(ipServidor)) {
				final ConsistirException ex = new ConsistirException();
				ProcessarParalelismo.executar(0, listaSMS.size(), 50, ex, new ProcessarParalelismo.Processo() {
					@Override
					public void run(int i) {
						SMSVO sms = listaSMS.get(i);
												try {
							// testar serviço utilizado -> facilita , human, loca, etc
							if (config.getFornecedorSMSEnum().equals(FornecedorSmsEnum.HUMANSMS)) {
								HumanSms.enviarMensagem(config, Uteis.formatarTelefoneParaEnvioSms(sms.getCelular()), sms.getMensagem());
								if (Uteis.isAtributoPreenchido(sms.getCodigo())) {
									alterarSMS(sms);
								}
							} else if (config.getFornecedorSMSEnum().equals(FornecedorSmsEnum.FACILITASMS)) { 
								FacilitaSms.enviarMensagem(config, Uteis.formatarTelefoneParaEnvioLocaSms(sms.getCelular()), sms.getMensagem());							
								if (Uteis.isAtributoPreenchido(sms.getCodigo())) {
									alterarSMS(sms);
								}
							} else if (config.getFornecedorSMSEnum().equals(FornecedorSmsEnum.LOCASMS)) {
								LocaSms.enviarMensagem(config, Uteis.formatarTelefoneParaEnvioLocaSms(sms.getCelular()), sms.getMensagem());
								if (Uteis.isAtributoPreenchido(sms.getCodigo())) {
									alterarSMS(sms);
								}
							} else if (config.getFornecedorSMSEnum().equals(FornecedorSmsEnum.ROBBUSMS)) {
								RobbuSms.enviarMensagem(config, sms);
								if (Uteis.isAtributoPreenchido(sms.getCodigo())) {
									alterarSMS(sms);
								}
							}
						} catch (Exception e) {
							ex.adicionarListaMensagemErro(e.getMessage());
							try {
								alterarSMSErro(sms, e.getMessage());
							} catch (Exception j) {
							}
						}

					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			smtpPadrao = null;
			loginServidorSmtp = null;
			emailRemetente = null;
			senhaServidorSmtp = null;
			portaSmtpPadrao = null;
			listaFileCorpoMensagem = null;
			listaAnexosExcluir = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSMSErro(final SMSVO sms, final String mensagemErro) throws Exception {
		final String sql = "UPDATE SMS set enviouSMS=false, mensagemsms=?  WHERE (codigo = ?)";
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, mensagemErro);
					sqlAlterar.setInt(2, sms.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (DataAccessException dae) {
		} catch (Exception e) {
			if (e.getMessage().contains("Could not get JDBC Connection")) {
				throw e;
			} else {
				throw e;
			}
		} finally {
			
		}
	}
	
	
	
	public void enviarEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String corpo, String caminhoAnexos, Boolean anexarImagensPadrao, Boolean servidorEmailUtilizaSSL, Boolean servidorEmailUtilizaTSL, String caminhoLogoEmailCima, String caminhoLogoEmailBaixo, Integer contador, Boolean ativarModoDebug) throws Exception {
		
		MimeMessage message = null;
		MimeMultipart multipart = null;
		BodyPart messageBodyPart = null;
//		boolean sessionNull = (session == null);
		try {
			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 iniciando");
			}
			if(session == null) {
				realizarInicializacaoSessao(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarModoDebug);
			}else {
				isSessionValid(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarModoDebug);
			}
			message = new MimeMessage(session);
			message.setSentDate(new Date());
			message.setFrom(new InternetAddress(emailRemetente, MimeUtility.encodeText(nomeRemet, "ISO-8859-1", "Q")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDest, MimeUtility.encodeText(nomeDest, "ISO-8859-1", "Q")));
			message.setSubject(MimeUtility.encodeText(assunto, "ISO-8859-1", "Q"));

			corpo = alterarNomeImagemEmail(corpo, caminhoLogoEmailCima, caminhoLogoEmailBaixo);
			multipart = new MimeMultipart("mixed");
			messageBodyPart = new MimeBodyPart();
			multipart.addBodyPart(messageBodyPart);
			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 preparando anexo");
			}
			if (anexarImagensPadrao && (corpo.contains("id=\"_x0000_i1025\"") || corpo.contains("id=\"_x0000_i1028\""))) {
				 criarFileCorpoMensagemEmail(multipart,  caminhoLogoEmailCima, caminhoLogoEmailBaixo);
			}
			criarFileAtendimentoCorpoMensagemEmail(multipart, corpo);
			corpo = incluirImagensComoCID(multipart, corpo);
			if (!caminhoAnexos.isEmpty() && !caminhoAnexos.trim().equals("\"")) {
				criarAnexoEmail(multipart, caminhoAnexos);
			}
			messageBodyPart.setContent(corpo, "text/html; charset=ISO-8859-1");

			if (servidorEmailUtilizaTSL) {
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);
			}

			message.setContent(multipart);
			message.saveChanges();

			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 enviando");
			}
//			long start = System.currentTimeMillis();
			Transport.send(message);
//			long elapsed = System.currentTimeMillis() - start;
//			if (elapsed >= 60000) {
//				executarRegistroExecucaoJob(JobsEnum.JOB_ENVIAR_EMAIL.getName(), new Date(), null, "Envio de email para " + emailDest + " demorou " + elapsed + " ms.");
//			}
			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 enviado");
			}
		} catch (AuthenticationFailedException e) {
			throw e;
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("Access to default session denied")) {
				throw new Exception("Ocorreu um erro durante o envio do e-mail. A permissão de acesso à sessão de email foi negada.");
			}
			throw e;
		} finally {			
			message = null;
			multipart = null;
			messageBodyPart = null;			
			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 finalizado");
			}
//			if(sessionNull) {
//				session =  null;
//			}
		}
	}

	private Authenticator getAuthenticator(final String userName, final String senha) {
		
		return new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, senha);
				}
			};
		
	}

	private void criarFileAtendimentoCorpoMensagemEmail(MimeMultipart multipart, String corpo) throws Exception {
		// BodyPart messageBodyPart
		listaFileCorpoMensagem = new ArrayList<File>();
		if (corpo.contains("cid:star1")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento" + File.separator + "star1.png"));
		}
		if (corpo.contains("cid:star2")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento" + File.separator + "star2.png"));
		}
		if (corpo.contains("cid:star3")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento" + File.separator + "star3.png"));
		}
		if (corpo.contains("cid:star4")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento" + File.separator + "star4.png"));
		}
		for (File imagem : listaFileCorpoMensagem) {
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(imagem);
			messageBodyPart.setDisposition(Part.INLINE);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<" + imagem.getName().substring(0, imagem.getName().lastIndexOf(".")) + ">");
			multipart.addBodyPart(messageBodyPart);
		}
	}

	private void criarAnexoEmail(MimeMultipart multipart, String caminhoAnexos) throws Exception {
		q:
		for (String caminhoArquivo : caminhoAnexos.split(";")) {
			File file = new File(caminhoArquivo);
			if (!file.exists()) {
				caminhoArquivo = caminhoArquivo.replace("anexoEmail", "arquivo"); 
				file = new File(caminhoArquivo);
				if (!file.exists()) {
					continue q;
				}
			}
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(file);
			messageBodyPart.setDisposition(Part.ATTACHMENT);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setFileName(file.getName());
			multipart.addBodyPart(messageBodyPart);
			if (caminhoArquivo.contains("anexoEmail") && !getListaAnexosExcluir().contains(caminhoArquivo)) {
				getListaAnexosExcluir().add(caminhoArquivo);
			}
		}
	}
		
	public synchronized void realizarInicializacaoSessao(Boolean servidorEmailUtilizaSSL, Boolean servidorEmailUtilizaTSL, Boolean ativarDebug) {
		Properties props =  System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", smtpPadrao);
		if (portaSmtpPadrao != null || !portaSmtpPadrao.equals("")) {
			props.put("mail.smtp.port", portaSmtpPadrao);
		}
		props.put("mail.smtp.auth", "true");
		/*
		 * Para TSL
		 */
		if (servidorEmailUtilizaTSL) {
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.mime.charset", "ISO-8859-1");
			props.put("mail.smtp.user", loginServidorSmtp);
			props.put("mail.smtp.password", senhaServidorSmtp);
			props.remove("mail.smtp.socketFactory.class");
//			props.remove("mail.smtp.port");
			props.put("mail.smtp.socketFactory.port", portaSmtpPadrao);
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.quitwait", "false");
			props.put("mail.smtp.ssl.protocols", "SSLv3 TLSv1");
			props.put("mail.smtp.ssl.trust",  "*");
		} else if (servidorEmailUtilizaSSL) {
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			props.remove("mail.smtp.starttls.enable");
			props.remove("mail.mime.charset");
			props.remove("mail.smtp.user");
			props.remove("mail.smtp.password");
			props.remove("mail.smtp.socketFactory.class");
			props.remove("mail.smtp.port");
			props.remove("mail.smtp.socketFactory.port");
			props.remove("mail.smtp.socketFactory.fallback");
			props.remove("mail.smtp.quitwait");
			props.remove("mail.smtp.ssl.protocols");
			props.remove("mail.smtp.ssl.trust");

			props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.socketFactory.port", portaSmtpPadrao);
		} else {
			props.remove("mail.smtp.socketFactory.class");
			props.remove("mail.smtp.socketFactory.fallback");
			props.remove("mail.smtp.socketFactory.port");
			props.remove("mail.smtp.starttls.enable");
			props.remove("mail.mime.charset");
			props.remove("mail.smtp.user");
			props.remove("mail.smtp.password");
//			props.remove("mail.smtp.quitwait");
			props.remove("mail.smtp.ssl.protocols");
			props.remove("mail.smtp.ssl.trust");
			/*
			 * Comentado por Wendel Rodrigues no dia 03/09/2014. Se não
			 * utilizar TSL ou SSL, as configurações de usuario, senha e
			 * porta não poderão ser removidas. Porque a mesma é utilizada
			 * para efetuar a autenticação no servidor de email.
			 */
			// props.remove("mail.smtp.starttls.enable");
			// props.remove("mail.mime.charset");
			// props.remove("mail.smtp.user");
			// props.remove("mail.smtp.password");
			// props.remove("mail.smtp.socketFactory.class");
			// props.remove("mail.smtp.port");
			// props.remove("mail.smtp.socketFactory.port");
			// props.remove("mail.smtp.socketFactory.fallback");
			// props.remove("mail.smtp.quitwait");
			// props.remove("mail.smtp.ssl.protocols");
			// props.remove("mail.smtp.socketFactory.class");
			// props.remove("mail.smtp.socketFactory.port");
			// props.remove("mail.smtp.socketFactory.fallback");
		}
		 
		// Timeout para estabelecer a conexão (em milissegundos)
		props.put("mail.smtp.connectiontimeout", "10000"); // 10 segundos
		 // Timeout para a leitura de resposta do servidor (em milissegundos)
		props.put("mail.smtp.timeout", "10000"); // 10 segundos
		// Para evitar o timeout no Transport.close()
		props.put("mail.smtp.quitwait", "false");
		session = Session.getInstance(props, getAuthenticator(loginServidorSmtp.trim(), senhaServidorSmtp));
		session.setDebug(ativarDebug);		
	}

	private String alterarNomeImagemEmail(String texto, String caminhoLogoEmailCima, String caminhoLogoEmailBaixo) throws Exception {
		File imagemCima = null;
		if (Uteis.isAtributoPreenchido(caminhoLogoEmailCima)) {
			imagemCima = new File(caminhoLogoEmailCima);
		} 
		if (imagemCima == null || (Uteis.isAtributoPreenchido(caminhoLogoEmailCima) && !imagemCima.exists())){
			imagemCima = new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "cima_sei.jpg");
		}
		if (texto.contains("id=\"_x0000_i1025\"")) {
			String parteCima1, parteCima2;
			parteCima1 = texto.substring(texto.indexOf("id=\"_x0000_i1025\""), texto.length());
			parteCima1 = parteCima1.substring(parteCima1.indexOf("src=\"")+5, parteCima1.length());
			parteCima2 = parteCima1.substring(0, parteCima1.indexOf("\""));
			texto = texto.replaceAll("alt=\"cima_sei\"", "");
			texto = texto.replaceAll("alt=\"\"", "");
			texto = texto.replaceAll(parteCima2, "cid:" + imagemCima.getName().substring(0, imagemCima.getName().lastIndexOf(".")));
		}
		File imagemBaixo = null;
		if (Uteis.isAtributoPreenchido(caminhoLogoEmailBaixo)) {
			imagemBaixo = new File(caminhoLogoEmailBaixo);
		}
		if (imagemBaixo == null || (Uteis.isAtributoPreenchido(caminhoLogoEmailBaixo) && !imagemBaixo.exists())){
			imagemBaixo = new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "baixo_sei.jpg");
		}
		if (texto.contains("id=\"_x0000_i1028\"")) {
			String parteBaixo1, parteBaixo2;
			parteBaixo1 = texto.substring(texto.indexOf("id=\"_x0000_i1028\""), texto.length());
			parteBaixo1 = parteBaixo1.substring(parteBaixo1.indexOf("src=\"")+5, parteBaixo1.length());
			parteBaixo2 = parteBaixo1.substring(0, parteBaixo1.indexOf("\""));			
			texto = texto.replaceAll("alt=\"baixo_sei\"", "");
			texto = texto.replaceAll("alt=\"\"", "");
			texto = texto.replaceAll(parteBaixo2, "cid:" + imagemBaixo.getName().substring(0, imagemBaixo.getName().lastIndexOf(".")));
		}
		return texto;
	}
	
	private void criarFileCorpoMensagemEmail(MimeMultipart multipart, String caminhoLogoEmailCima, String caminhoLogoEmailBaixo) throws Exception {
		// BodyPart messageBodyPart
		listaFileCorpoMensagem = new ArrayList<File>();
		if(Uteis.isAtributoPreenchido(caminhoLogoEmailCima)){			
			File imagem = new File(caminhoLogoEmailCima);
			if(imagem.exists()) {
			listaFileCorpoMensagem.add(imagem);			
		}else{
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "cima_sei.jpg"));	
		}
		}else{
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "cima_sei.jpg"));	
		}
		if(Uteis.isAtributoPreenchido(caminhoLogoEmailBaixo)){			
			File imagem = new File(caminhoLogoEmailBaixo);
			if(imagem.exists()) {
			listaFileCorpoMensagem.add(imagem);
		}else{
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "baixo_sei.jpg"));	
		}
		}else{
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "baixo_sei.jpg"));	
		}
		
		for (File imagem : listaFileCorpoMensagem) {
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(imagem);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setFileName(imagem.getName());
			messageBodyPart.setHeader("Content-ID", "<" + imagem.getName().substring(0, imagem.getName().lastIndexOf(".")) + ">");
			multipart.addBodyPart(messageBodyPart);
		}		
		Uteis.liberarListaMemoria(listaFileCorpoMensagem);
	}

	public List<EmailVO> consultarEmails(ConfiguracaoGeralSistemaVO config, boolean erro, boolean redefinirSenha) throws SQLException {
		SqlRowSet dadosSQL = null;
		try {
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando consulta de emails");
			List<EmailVO> listaEmail = new ArrayList<EmailVO>();
			try {
				StringBuilder sb = new StringBuilder("select * from email ");
				sb.append(" where datacadastro >= (current_date - 1) ");
				sb.append(" and redefinirSenha = false ");
				if(!erro) {
					sb.append(" and (erro = '' or  erro is null ) ");
				}else {
					sb.append(" and erro != '' ");
				}
				sb.append(" order by datacadastro limit ").append(config.getQtdEmailEnvio());
				dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			} catch (Exception e) {
				throw e;
			}
			while (dadosSQL.next()) {
				EmailVO obj = new EmailVO();
				obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
				obj.setEmailDest(dadosSQL.getString("emailDest").trim());
				if (obj.getEmailDest().contains("/")) {
					obj.setEmailDest(obj.getEmailDest().substring(0, obj.getEmailDest().indexOf("/")));
				} else if (obj.getEmailDest().contains(";")) {
					obj.setEmailDest(obj.getEmailDest().substring(0, obj.getEmailDest().indexOf(";")));
				}
				obj.setNomeDest(dadosSQL.getString("nomeDest"));
				obj.setEmailRemet(dadosSQL.getString("emailRemet").trim());
				obj.setNomeRemet(dadosSQL.getString("nomeRemet"));
				obj.setAssunto(dadosSQL.getString("assunto"));
				obj.setMensagem(dadosSQL.getString("mensagem"));
				obj.setCaminhoAnexos(dadosSQL.getString("caminhoAnexos"));
				obj.setAnexarImagensPadrao(dadosSQL.getBoolean("anexarImagensPadrao"));
				obj.setCaminhoLogoEmailCima(dadosSQL.getString("caminhologoemailcima"));
		        obj.setCaminhoLogoEmailBaixo(dadosSQL.getString("caminhologoemailbaixo"));
				obj.setDataCadastro(dadosSQL.getTimestamp("datacadastro"));
				obj.setMultiplosDestinatarios(dadosSQL.getBoolean("multiplosdestinatarios"));
				obj.setNovoObj(Boolean.FALSE);
				codigoEmails += obj.getCodigo()+",";
				listaEmail.add(obj);
			}
			return listaEmail;
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
			return new ArrayList<EmailVO>();
		} finally {
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Finalizando consulta de emails");
		}
	}

	@SuppressWarnings("deprecation")
	public List<SMSVO> consultarSMSs(ConfiguracaoGeralSistemaVO config, boolean redefinirSenha) throws SQLException {
		int hora = new Date().getHours();
		SqlRowSet dadosSQL = null;
		if (hora > 06 && hora < 24) {
			try {
				StringBuilder sqlStr = new StringBuilder();
				List<SMSVO> listaSMS = new ArrayList<SMSVO>();
				Date data = new Date();
				data.setHours(23);
				data.setMinutes(59);
				data.setSeconds(59);
				data = Uteis.getDataPassada(data, 1);
				sqlStr.append("SELECT * FROM SMS ");
				sqlStr.append("WHERE datacadastro >= '").append(Uteis.getDataJDBC(data)).append(" 00:00:00' and enviouSMS = false ");
				sqlStr.append(" and (mensagemsms = '' or mensagemsms is null) ");				
				sqlStr.append(" ORDER BY datacadastro desc LIMIT " + config.getQtdEmailEnvio());
				try {
					dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				} catch (Exception e) {
					if (e.getMessage().contains("Could not get JDBC Connection")) {
						throw e;
					} else {
						throw e;
					}
				}
				sqlStr = null;
				while (dadosSQL.next()) {
					SMSVO obj = new SMSVO();
					obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
					obj.setNomeDest(dadosSQL.getString("nomeDest"));
					obj.setAssunto(dadosSQL.getString("assunto"));
					obj.setMensagem(dadosSQL.getString("mensagem"));
					obj.setCelular(dadosSQL.getString("celular"));
					obj.setDataCadastro(dadosSQL.getTimestamp("datacadastro"));
					obj.setCodigoDest(dadosSQL.getString("codigoDest"));
					obj.setCpfDest(dadosSQL.getString("cpfDest"));
					obj.setMatriculaDest(dadosSQL.getString("matriculaDest"));
					obj.setNovoObj(Boolean.FALSE);
					listaSMS.add(obj);
				}
				return listaSMS;

			} catch (Exception ex) {
				return new ArrayList<SMSVO>();
			}
		} else {
			return new ArrayList<SMSVO>();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarExclusaoEmail(EmailVO email) throws Exception {
		String sql = "DELETE FROM Email WHERE ((codigo = ?))";
		try {
			getConexao().getJdbcTemplate().update(sql, new Object[] { email.getCodigo() });
		} catch (DataAccessException dae) {

		} catch (Exception e) {
			if (e.getMessage().contains("Could not get JDBC Connection")) {
				throw e;
			} else {
				throw e;
			}
		} finally {

		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSMS(final SMSVO sms) throws Exception {
		final String sql = "UPDATE SMS set enviouSMS=?  WHERE (codigo = ?)";
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setBoolean(1, Boolean.TRUE);
					sqlAlterar.setInt(2, sms.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (DataAccessException dae) {

		} catch (Exception e) {
			if (e.getMessage().contains("Could not get JDBC Connection")) {
				throw e;
			} else {
				throw e;
			}
		} finally {

		}
	}

	private void realizarExclusaoAnexo() throws Exception {
		if (getListaAnexosExcluir() != null && !getListaAnexosExcluir().isEmpty()) {
			verificarAnexoUsadoOutroEmail();
			for (String caminhoArquivo : getListaAnexosExcluir()) {
				File file = new File(caminhoArquivo);
				file.delete();
			}
		}
	}

	private void verificarAnexoUsadoOutroEmail() throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		SqlRowSet dadosSQL = null;
		if(codigoEmails == null || codigoEmails.trim().isEmpty()) {
			return;
		}
		try {
			sqlStr.append("SELECT caminhoanexos from email ");
			sqlStr.append("where (");
			for (String anexo : getListaAnexosExcluir()) {
				// sqlStr.append("( caminhoanexos = '").append(anexo.replaceAll("\\\\",
				// "'||E'\\\\\\\\'||'"));
				if (anexo.contains(File.separator)) {
					sqlStr.append("( caminhoanexos like '%").append(anexo.substring(anexo.lastIndexOf(File.separator) + 1, anexo.length()));
				} else if (anexo.contains("/")) {
					sqlStr.append("( caminhoanexos like '%").append(anexo.substring(anexo.lastIndexOf("/") + 1, anexo.length()));
				}
				if (anexo.equals(getListaAnexosExcluir().get(getListaAnexosExcluir().size() - 1))) {
					sqlStr.append("%')) ");
				} else {
					sqlStr.append("%') or  ");
				}
			}
			codigoEmails = codigoEmails.substring(0, codigoEmails.length() - 1);
			sqlStr.append(" and codigo not in(").append(codigoEmails).append(") ");
			sqlStr.append("GROUP BY caminhoanexos");
			try {
				dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			} catch (Exception e) {
				if (e.getMessage().contains("Could not get JDBC Connection")) {
					throw e;
				} else {
					throw e;
				}
			}
			sqlStr = null;
			String caminhosAnexoExcluir = "";
			while (dadosSQL.next()) {
				caminhosAnexoExcluir += (dadosSQL.getString("caminhoanexos")) + " ; ";
			}
			Iterator<String> i = getListaAnexosExcluir().iterator();
			while (i.hasNext()) {
				String anexo = (String) i.next();
				if (caminhosAnexoExcluir.contains(anexo)) {
					i.remove();
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			dadosSQL = null;
		}
	}

	public void realizarEnvioEmailRedefinirSenha() {
		ConfiguracaoGeralSistemaVO config = null;		
		try {
			while (true) {				
				try {
					config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				} catch (Exception e) {
				}
				ipServidor = config.getIpServidor();
				smtpPadrao = config.getSmptPadrao();
				loginServidorSmtp = config.getLogin();
				emailRemetente = config.getEmailRemetente();
				senhaServidorSmtp = config.getSenha();
				portaSmtpPadrao = config.getPortaSmtpPadrao();
				listaAnexosExcluir = new ArrayList<String>();
				codigoEmails = "";
				
				if (Uteis.validarEnvioEmail(ipServidor)) {
					List<EmailVO> listaEmail = consultarEmails(config, false, true);
					realizarInicializacaoSessao(config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), false);
					enviarEmails(listaEmail, config, new RegistroExecucaoJobVO(), new StringBuilder());
					Uteis.liberarListaMemoria(listaEmail);
				}
			}
		} catch (Exception ex) {
			session =  null;
		}
	}

	public void realizarTesteEnvioEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String corpo, Boolean servidorEmailUtilizaSSL, Boolean servidorEmailUtilizaTSL, String smtpPadrao, String ipServidor, String loginServidorSmtp, String senhaServidorSmtp, String portaSmtpPadrao) throws Exception {
		Session session =  null;
		try {
			ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
			if (smtpPadrao == null || smtpPadrao.trim().isEmpty()) {
				throw new Exception("O campo SMPT PADRÃO deve ser informado.");
			}
			if (portaSmtpPadrao == null || portaSmtpPadrao.trim().isEmpty()) {
				throw new Exception("O campo PORTA SMPT PADRÃO deve ser informado.");
			}
			if (emailRemet == null || emailRemet.trim().isEmpty()) {
				throw new Exception("O campo EMAIL REMETENTE deve ser informado.");
			}
			if (loginServidorSmtp == null || loginServidorSmtp.trim().isEmpty()) {
				throw new Exception("O campo LOGIN EMAIL deve ser informado.");
			}
			if (senhaServidorSmtp == null || senhaServidorSmtp.trim().isEmpty()) {
				throw new Exception("O campo SENHA deve ser informado.");
			}
			if (!Uteis.validarEnvioEmail(ipServidor)) {
				throw new Exception("O campo HOST NAME SERVIDOR é inválido.");
			}
			this.ipServidor = ipServidor;
			this.smtpPadrao = smtpPadrao;
			this.loginServidorSmtp = loginServidorSmtp;
			this.emailRemetente = emailRemet;
			this.senhaServidorSmtp = senhaServidorSmtp;
			this.portaSmtpPadrao = portaSmtpPadrao;
			realizarInicializacaoSessao(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, false);
			enviarEmail(emailDest, nomeDest, emailRemet, nomeRemet, "Teste Configuração Email", comunicacaoInternaVO.getMensagemComLayout("E-mail configurado com sucesso..."), "", true, servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, null, null, null, true);
			// enviarEmail(email.getEmailDest(), email.getNomeDest(),
			// email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(),
			// email.getMensagem(), email.getCaminhoAnexos(),
			// email.getAnexarImagensPadrao(),
			// config.getServidorEmailUtilizaSSL(),
			// config.getServidorEmailUtilizaTSL());

		} catch (AuthenticationFailedException e) {
			throw new Exception("O campo LOGIN EMAIL ou SENHA estão incorretos.");
		} catch (SMTPSendFailedException e) {
			throw new Exception("O e-mail de autenticação foi bloqueado pelo servidor de e-mail. Verifique se não foi excedido o limite diário de envio de e-mail ou se existe uma grande quantidade de e-mail que está sendo retornado.");
		} catch (MessagingException e) {
			if (e.getCause() instanceof UnknownHostException) {
				throw new Exception("O campo SMTP PADRÃO é inválido.");
			}
			if (e.getCause() instanceof SSLException) {
				throw new Exception("Esta configuração não utiliza autenticação SSL.");
			}
			if (e.getCause() instanceof SMTPAddressFailedException) {
				if (servidorEmailUtilizaTSL) {
					throw new Exception("Não foi possivel enviar email de teste para " + emailDest + ", talvez este não utiliza autenticação TSL.");
				}
				throw new Exception("Não foi possivel enviar email de teste para " + emailDest);
			}
			if (e.getMessage().contains("port")) {
				throw new Exception("O campo PORTA SMTP PADRÃO é inválido.");
			}
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			session = null;
		}
	}

	public static void enviarSMSNotificacaoEquipeOtimize(String mensagem){
		
	}
	
	public static void enviarSMSNotificacaoEquipeUNIRV(String mensagem){
		
	}
	
	public void enviarEmailEquipeUNIRV(String mensagem, ConfiguracaoGeralSistemaVO config) {
					
	}
	
	public String getSmtpPadrao() {
		return smtpPadrao;
	}

	public void setSmtpPadrao(String smtpPadrao) {
		this.smtpPadrao = smtpPadrao;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}

	public String getLoginServidorSmtp() {
		return loginServidorSmtp;
	}

	public void setLoginServidorSmtp(String loginServidorSmtp) {
		this.loginServidorSmtp = loginServidorSmtp;
	}

	public String getSenhaServidorSmtp() {
		return senhaServidorSmtp;
	}

	public void setSenhaServidorSmtp(String senhaServidorSmtp) {
		this.senhaServidorSmtp = senhaServidorSmtp;
	}

	public String getPortaSmtpPadrao() {
		return portaSmtpPadrao;
	}

	public void setPortaSmtpPadrao(String portaSmtpPadrao) {
		this.portaSmtpPadrao = portaSmtpPadrao;
	}

	public String getEmailRemetente() {
		return emailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}


	public List<String> getListaAnexosExcluir() {
		if (listaAnexosExcluir == null) {
			listaAnexosExcluir = new ArrayList<String>();
		}
		return listaAnexosExcluir;
	}

	public void setListaAnexosExcluir(List<String> listaAnexosExcluir) {
		this.listaAnexosExcluir = listaAnexosExcluir;
	}
	
	private static final Pattern imgRegExp  = Pattern.compile( "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>" );
	
	private String incluirImagensComoCID(MimeMultipart multipart, String  body) throws Exception {
		 final Matcher matcher = imgRegExp.matcher( body );
		   int i = 0;
		   while ( matcher.find() ) {
		      String src = matcher.group();
		      if ( body.indexOf( src ) != -1 && src.contains("src=") && !src.contains("src=\"cid:") && src.contains(",") ) {
		         String srcToken = "src=\"";
		         int x = src.indexOf( srcToken );
		         int y = src.indexOf( "\"", x + srcToken.length() );
		         String srcText = src.substring( x + srcToken.length(), y );
		         String data = "image/jpg";
		         if(src.contains("data:")) {
		        	 data = src.substring(src.indexOf("data:")+5, src.indexOf(";"));
		         }
		         if(!srcText.startsWith("cid:") && srcText.contains(",")){
		        	 String cid = "imageb64" + i;		        	 
		        	 String newSrc = src.replace( srcText, "cid:" + cid );
		        	 byte[] tile = DatatypeConverter.parseBase64Binary(srcText.split( "," )[1]);
		        	  MimeBodyPart messageBodyPart = new MimeBodyPart();
				      DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(tile, data));
				      messageBodyPart.setDisposition(Part.INLINE);
				      messageBodyPart.setDataHandler(dataHandler);
				      messageBodyPart.setHeader("Content-ID", "<" + cid + ">");
				      multipart.addBodyPart(messageBodyPart);
		        	 
		        	 body = body.replace( src, newSrc );
		        	 i++;
		         }
		      }
		   }
		   return body;
	}
	
	public void enviarEmailMultiplosDestinatarios(String emailDest, String emailRemet, String nomeRemet, String assunto, String corpo, String caminhoAnexos, Boolean anexarImagensPadrao, Boolean servidorEmailUtilizaSSL, Boolean servidorEmailUtilizaTSL, String caminhoLogoEmailCima, String caminhoLogoEmailBaixo, Integer contador, Boolean ativarModoDebug) throws Exception {
		MimeMessage message = null;
		MimeMultipart multipart = null;
		BodyPart messageBodyPart = null;
		try {
			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 iniciando");
			}
			if(session == null) {
				realizarInicializacaoSessao(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarModoDebug);
			}else {
				isSessionValid(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarModoDebug);
			}
			Map<String, List<String>> map = new ObjectMapper().readValue(emailDest, new TypeReference<Map<String, List<String>>>(){});
			if (Uteis.isAtributoPreenchido(map)) {
				message = new MimeMessage(session);
				message.setSentDate(new Date());
				message.setFrom(new InternetAddress(emailRemetente, MimeUtility.encodeText(nomeRemet, "ISO-8859-1", "Q")));
				if(map.entrySet().size() == 1) {					
					for (Entry<String, List<String>> entrada : map.entrySet()) {
						for (String email : entrada.getValue()) {
							if (Uteis.isAtributoPreenchido(email) && Uteis.getValidaEmail(email)) {
								message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.trim().toLowerCase(), MimeUtility.encodeText(entrada.getKey(), "ISO-8859-1", "Q")));								
							}
						}
						if(message.getAllRecipients() == null || message.getAllRecipients().length == 0) {
							return;
						}
					}					
				}else {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress("naoresponder@univesp.com", "UNIVESP"));
					for (Entry<String, List<String>> entrada : map.entrySet()) {
						for (String email : entrada.getValue()) {
							if (Uteis.isAtributoPreenchido(email) && Uteis.getValidaEmail(email)) {
								message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email.trim().toLowerCase(), MimeUtility.encodeText(entrada.getKey(), "ISO-8859-1", "Q")));
							}
						}
					}
				}
				message.setSubject(MimeUtility.encodeText(assunto, "ISO-8859-1", "Q"));
				corpo = alterarNomeImagemEmail(corpo, caminhoLogoEmailCima, caminhoLogoEmailBaixo);
				multipart = new MimeMultipart("mixed");
				messageBodyPart = new MimeBodyPart();
				multipart.addBodyPart(messageBodyPart);
				if (contador != null && contador.equals(1)) {
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 preparando anexo");
				}
				if (anexarImagensPadrao && (corpo.contains("id=\"_x0000_i1025\"") || corpo.contains("id=\"_x0000_i1028\""))) {
					criarFileCorpoMensagemEmail(multipart,  caminhoLogoEmailCima, caminhoLogoEmailBaixo);
				}
				criarFileAtendimentoCorpoMensagemEmail(multipart, corpo);
				corpo = incluirImagensComoCID(multipart, corpo);
				if (!caminhoAnexos.isEmpty() && !caminhoAnexos.trim().equals("\"")) {
					criarAnexoEmail(multipart, caminhoAnexos);
				}
				messageBodyPart.setContent(corpo, "text/html; charset=ISO-8859-1");
				
				if (servidorEmailUtilizaTSL) {
					MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
					mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
					mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
					mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
					mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
					mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
					CommandMap.setDefaultCommandMap(mc);
				}
				message.setContent(multipart);
				message.saveChanges();
				if (contador != null && contador.equals(1)) {
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 enviando");
				}
				Transport.send(message);
				if (contador != null && contador.equals(1)) {
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 enviado");
				}
			}
		} catch (AuthenticationFailedException e) {
			throw e;
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Erro ao enviar email ("+emailDest+"): "+e.getMessage());
			if (e.getMessage() != null && e.getMessage().contains("Access to default session denied")) {
				throw new Exception("Ocorreu um erro durante o envio do e-mail. A permissão de acesso à sessão de email foi negada.");
			}
			throw e;
		} finally {			
			message = null;
			multipart = null;
			messageBodyPart = null;			
			if (contador != null && contador.equals(1)) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Email 1 finalizado");
			}
		}
	}
	
	public synchronized void isSessionValid(Boolean servidorEmailUtilizaSSL, Boolean servidorEmailUtilizaTSL, Boolean ativarDebug) throws Exception {
        Transport transport = null;
        try {
        	if(session == null) {
        		realizarInicializacaoSessao(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarDebug);
        		return;
        	}
            // Obtém um objeto Transport para a sessão
            transport = session.getTransport();
            
            // Tenta se conectar. Se a conexão for bem-sucedida, a sessão é válida.
            // As credenciais são lidas da própria sessão, se já estiverem configuradas.
            transport.connect();
            
            
        } catch (AuthenticationFailedException e) {
        	AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Erro de conexão. A sessão pode não ser mais válida: "+ e.getMessage());
        	AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando uma nova sessao ");            
            realizarInicializacaoSessao(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarDebug);
        } catch (MessagingException e) {
        	AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Erro de conexão. A sessão pode não ser mais válida: "+ e.getMessage());
        	AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando uma nova sessao ");            
            realizarInicializacaoSessao(servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ativarDebug);
        } finally {
            if (transport != null) {
                try {
                    // Garante que o recurso de transporte seja fechado
                    transport.close();
                } catch (MessagingException e) {
                    System.err.println("Erro ao fechar o transporte: " + e.getMessage());
                }
            }
        }
    }

}