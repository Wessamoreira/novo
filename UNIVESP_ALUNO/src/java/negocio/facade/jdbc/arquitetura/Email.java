/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.arquitetura;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.interfaces.arquitetura.EmailFacadeInterface;

/**
 *
 * @author PEDRO
 */
@Repository
@Scope("singleton")
@Lazy
public class Email extends ControleAcesso implements EmailFacadeInterface {

    protected static String idEntidade;

    public Email() throws Exception {
        super();
        setIdEntidade("Email");
    }

    public static boolean validarDados(EmailVO obj) throws ConsistirException {
        if(obj == null) {
        	return false;
        }
    	if (obj.getAssunto().isEmpty()) {
        	return false;
            //throw new ConsistirException("O campo ASSUNTO (Email) deve ser informado.");
        }
        if (obj.getEmailDest().trim().isEmpty()) {
        	return false;
            //throw new ConsistirException("O campo EMAIL DESTINATÁRIO (Email) deve ser informado.");
        }
        if (obj.getEmailRemet().trim().isEmpty()) {
        	return false;
            //throw new ConsistirException("O campo EMAIL REMETENTE (Email) deve ser informado.");
        }
        if (obj.getMensagem().trim().isEmpty()) {
        	return false;
            //throw new ConsistirException("O campo MENSAGEM (Email) deve ser informado.");
        }
        if (!obj.getMultiplosDestinatarios() && obj.getNomeDest().trim().isEmpty()) {
        	return false;
            //throw new ConsistirException("O campo NOME DESTINATÁRIO (Email) deve ser informado.");
        }
        if (obj.getNomeRemet().trim().isEmpty()) {
        	return false;
            //throw new ConsistirException("O campo NOME REMETENTE (Email) deve ser informado.");
        }
        return true;



    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>CidadeVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CidadeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void incluir(final EmailVO obj) throws Exception {
        try {
            if (validarDados(obj)) {
	            Email.incluir(getIdEntidade());
	            final String sql = "INSERT INTO Email( emailDest, nomeDest, emailRemet, nomeRemet,  assunto, mensagem, caminhoAnexos, dataCadastro, anexarimagenspadrao, redefinirsenha, caminhologoemailcima, caminhologoemailbaixo, multiplosdestinatarios) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
	            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
	
	                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
	                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
	                    sqlInserir.setString(1, obj.getEmailDest());
	                    sqlInserir.setString(2, obj.getNomeDest());
	                    sqlInserir.setString(3, obj.getEmailRemet());
	                    sqlInserir.setString(4, obj.getNomeRemet());
	                    sqlInserir.setString(5, obj.getAssunto());
	                    sqlInserir.setString(6, Uteis.trocarLetraAcentuadaPorCodigoHtml(obj.getMensagem()));
	                    sqlInserir.setString(7, obj.getCaminhoAnexos());
	                    sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
	                    sqlInserir.setBoolean(9, obj.getAnexarImagensPadrao());
	                    sqlInserir.setBoolean(10, obj.getRedefinirSenha());
	                    sqlInserir.setString(11, obj.getCaminhoLogoEmailCima());
	                    sqlInserir.setString(12, obj.getCaminhoLogoEmailBaixo());
	                    sqlInserir.setBoolean(13, obj.getMultiplosDestinatarios());
	                    return sqlInserir;
	                }
	            }, new ResultSetExtractor<Object>() {
	
	                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
	                    if (arg0.next()) {
	                        obj.setNovoObj(Boolean.FALSE);
	                        return arg0.getInt("codigo");
	                    }
	                    return null;
	                }
	            }));
	
	
	            obj.setNovoObj(Boolean.FALSE);
            }
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>CidadeVO</code>. Sempre localiza o registro a ser excluído através
     * da chave primária da entidade. Primeiramente verifica a conexão com o
     * banco de dados e a permissão do usuário para realizar esta operacão na
     * entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CidadeVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void excluir(EmailVO obj) throws Exception {
        try {
            Email.excluir(getIdEntidade());
            String sql = "DELETE FROM Email WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void realizarGravacaoEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String mensagem, List<File> listaAnexos) throws Exception {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarGravacaoEmail(ComunicacaoInternaVO obj, List<File> listaAnexos, Boolean anexarImagensPadrao, UsuarioVO usuarioLogado, ProgressBarVO progressBarVO) throws Exception {
        ConfiguracaoGeralSistemaVO config = null;
        
        String caminhoAnexos = "";
        StringBuilder corpohtml = new StringBuilder();

        try {
        	config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
        	
            //Guardo os anexos na pasta temporaria para todos os destinatarios.
            if (listaAnexos != null) {
                for (File file : listaAnexos) {                	
                	if(!file.getAbsolutePath().contains(PastaBaseArquivoEnum.ANEXO_EMAIL.getValue())){
                		caminhoAnexos = caminhoAnexos + getFacadeFactory().getArquivoHelper().copiarArquivoPastaBaseAnexoEmail(file, PastaBaseArquivoEnum.ANEXO_EMAIL.getValue(), config) + ";";	
                	}else{
                		caminhoAnexos = caminhoAnexos + file.getAbsolutePath() +";";
                	}
                    
                }
            }
            //Alterando imagens Padrões
            if (anexarImagensPadrao) {
                executarAlteracaoNomeImagem(obj, corpohtml, obj.getMensagem());
            }
            // começo a salvar os emails para os destinatarios        
            for (ComunicadoInternoDestinatarioVO cidVO : obj.getComunicadoInternoDestinatarioVOs()) {
           	
            	String nomeDest = "";
                String emailDest = "";
                String emailDest2 = "";
                EmailVO emailVO = new EmailVO();
                PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = null;
                if(Uteis.isAtributoPreenchido(cidVO.getDestinatario().getCodigo())) {
                	if(!cidVO.getDestinatario().getListaPessoaEmailInstitucionalVO().isEmpty()) {
                		pessoaEmailInstitucionalVO =  cidVO.getDestinatario().getListaPessoaEmailInstitucionalVO().get(0);
                	}else {
                		pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(cidVO.getDestinatario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
                	}
                } 			
                if (!cidVO.getDestinatario().getCodigo().equals(0)) {
                    if (cidVO.getDestinatario().getEmail() == null || cidVO.getDestinatario().getEmail().trim().equals("")) {
                        cidVO.setDestinatario(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(cidVO.getDestinatario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
                    }
                    nomeDest = cidVO.getDestinatario().getNome();
                    if((Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO) && Uteis.isAtributoPreenchido(obj) && obj.getEnviarEmailInstitucional()  && !pessoaEmailInstitucionalVO.getEmail().trim().isEmpty())
                    	|| (Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO) && Uteis.isAtributoPreenchido(obj) && obj.getEnviarEmailInstitucional() && !pessoaEmailInstitucionalVO.getEmail().trim().isEmpty())) {
                    	emailDest = pessoaEmailInstitucionalVO.getEmail();
                    	if(obj.getEnviarEmail()) {
                    		emailDest2 = cidVO.getDestinatario().getEmail();                    		
                    	}
                    	if(Uteis.isAtributoPreenchido(emailDest2)) {
                    		emailDest = emailDest + " ; " + emailDest2;                    		
                    	}
                    } else {
                    	emailDest = cidVO.getDestinatario().getEmail();
                    	emailDest2 = cidVO.getDestinatario().getEmail2();
                    }
                } else if (!cidVO.getDestinatario().getNome().equals("")) {
                    nomeDest = cidVO.getDestinatario().getNome();
                    emailDest = cidVO.getEmail();
                } 
//                else if (!cidVO.getDestinatarioParceiro().getCodigo().equals(0)) {
//                    if (cidVO.getDestinatarioParceiro().getEmail() == null || cidVO.getDestinatarioParceiro().getEmail().trim().equals("")) {
//                        cidVO.setDestinatarioParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(cidVO.getDestinatarioParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
//                    }
//                    nomeDest = cidVO.getDestinatarioParceiro().getNome();
//                    emailDest = cidVO.getDestinatarioParceiro().getEmail();
//                }
                
                emailVO.setAnexarImagensPadrao(anexarImagensPadrao);
                if (!emailDest2.trim().equals("") && !emailDest.equals(emailDest2)  && !Uteis.isAtributoPreenchido(emailDest)) {
                	emailDest = emailDest + ";" + emailDest2;
                }
                
                emailVO.setEmailDest(emailDest.trim());
//                emailVO.setEmailDest2(emailDest2.trim());
                emailVO.setNomeDest(nomeDest);
                emailVO.setEmailRemet(obj.getResponsavel().getEmail().trim());
                if (emailVO.getEmailRemet().equals("")) {
                	emailVO.setEmailRemet(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioLogado).getResponsavelPadraoComunicadoInterno().getEmail());
                }
                if (emailVO.getEmailRemet().equals("")) {
                	emailVO.setEmailRemet(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioLogado).getEmailRemetente());                    
                }
                if(emailVO.getEmailRemet().equals("")) {
                	emailVO.setEmailRemet("envio@sistema.com");
                }
                emailVO.setNomeRemet(obj.getResponsavel().getNome());
                if (obj.getResponsavel().getNome().equals("")) {
                	emailVO.setEmailRemet(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioLogado).getResponsavelPadraoComunicadoInterno().getNome());
                }
                if (obj.getResponsavel().getNome().equals("")) {
                	emailVO.setNomeRemet("SISTEMA SEI");                	
                }
                emailVO.setAssunto(obj.getAssunto());
                emailVO.setCaminhoAnexos(caminhoAnexos);
                String html = corpohtml.toString();
                if(!html.contains("<html")) {
                	if(!html.contains("<body")) {
                		html = "<body>"+html+ "</body>";
                	}
                	html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head>"+html+"</html>";
                }
                emailVO.setMensagem(html);
                emailVO.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(emailVO.getMensagem(), cidVO.getDestinatario()));
                emailVO.setRedefinirSenha(obj.getRedefinirSenha());
                if (emailVO.getMensagem().contains("#NOMEALUNO")) {
                    emailVO.setMensagem(Uteis.trocarHashTag("#NOMEALUNO", emailVO.getNomeDest(), emailVO.getMensagem()));
                }
                
                if (Uteis.isAtributoPreenchido(obj.getCaminhoImagemPadraoCima()) && obj.getCaminhoImagemPadraoCima().contains("logoUnidadeEnsino")) {
                	emailVO.setCaminhoLogoEmailCima(obj.getCaminhoImagemPadraoCima());
                } else {
                	if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) 
                			&& Uteis.isAtributoPreenchido(config.getLocalUploadArquivoFixo())
                			&& Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima())){
                		emailVO.setCaminhoLogoEmailCima(config.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima());                	
                	}
                }
                if (Uteis.isAtributoPreenchido(obj.getCaminhoImagemPadraoBaixo()) && obj.getCaminhoImagemPadraoBaixo().contains("logoUnidadeEnsino")) {
                	emailVO.setCaminhoLogoEmailBaixo(obj.getCaminhoImagemPadraoBaixo());
                } else {
                	if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) 
                			&& Uteis.isAtributoPreenchido(config.getLocalUploadArquivoFixo())                		
                			&& Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo())){                	
                		emailVO.setCaminhoLogoEmailBaixo(config.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo());
                	}
                }
                
                try {
                	incluirVerificandoPossuiMaisDeUmEmail(emailVO);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                
                if(progressBarVO != null && progressBarVO.getForcarEncerramento()) {
    				throw new Exception("Encerramento forçado do envio do comunicado interno.");
    			}
                if(progressBarVO != null) {
                	progressBarVO.incrementar();	
                }            	
            }
        } catch (Exception e) {
            throw e;
        } finally {            
            corpohtml = null;
            config = null;
        }
    }

    public void realizarGravacaoEmail(TurmaVO turma, ComunicacaoInternaVO obj, List<File> listaAnexos, ConfiguracaoGeralSistemaVO config, Boolean anexarImagensPadrao, UsuarioVO usuarioLogado) throws Exception {
        String emailDest = "";
        String caminhoAnexos = "";
        StringBuilder corpohtml = new StringBuilder();
        try {
            //Guardo os anexos na pasta temporaria para todos os destinatarios.
            if (listaAnexos != null) {
                for (File file : listaAnexos) {
                    caminhoAnexos = caminhoAnexos + getFacadeFactory().getArquivoHelper().copiarArquivoPastaBaseAnexoEmail(file, PastaBaseArquivoEnum.ANEXO_EMAIL.getValue(), config) + ";";
                }
            }

            //Alterando imagens Padrões
            if (anexarImagensPadrao) {
                executarAlteracaoNomeImagem(obj, corpohtml, obj.getMensagem());
            }

            for (FuncionarioGrupoDestinatariosVO fgdVO : turma.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs()) {
                try {
                    EmailVO emailVO = new EmailVO();

                    if (!fgdVO.getFuncionario().getPessoa().getEmail().equals("")) {
                        emailDest = fgdVO.getFuncionario().getPessoa().getEmail();
                    }
                    if (!fgdVO.getFuncionario().getPessoa().getEmail2().equals("")) {
                        emailDest = fgdVO.getFuncionario().getPessoa().getEmail2();
                    }
                    emailVO.setAnexarImagensPadrao(anexarImagensPadrao);
                    emailVO.setEmailDest(emailDest.trim());
                    emailVO.setNomeDest(fgdVO.getFuncionario().getPessoa().getNome());
                    emailVO.setEmailRemet(config.getEmailRemetente().trim());
                    emailVO.setNomeRemet(obj.getResponsavel().getNome());
                    emailVO.setAssunto(obj.getAssunto());
                    emailVO.setCaminhoAnexos(caminhoAnexos);
                    if (emailVO.getAnexarImagensPadrao()) {
                        executarAlteracaoNomeImagem(obj, corpohtml, obj.getMensagem());
                    }
                    emailVO.setMensagem(corpohtml.toString());
                    if (emailVO.getMensagem().contains("#NOMEALUNO")) {
                        emailVO.setMensagem(Uteis.trocarHashTag("#NOMEALUNO", emailVO.getNomeDest(), emailVO.getMensagem()));
                    }
                    emailVO.setRedefinirSenha(obj.getRedefinirSenha());
                    if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) 
                    		&& Uteis.isAtributoPreenchido(config.getLocalUploadArquivoFixo())
                    		&& Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima())){
                    	emailVO.setCaminhoLogoEmailCima(config.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima());                	
                    }
                    if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) 
                    		&& Uteis.isAtributoPreenchido(config.getLocalUploadArquivoFixo())                		
                    		&& Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo())){                	
                    	emailVO.setCaminhoLogoEmailBaixo(config.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo());
                    }
                    incluirVerificandoPossuiMaisDeUmEmail(emailVO);
                } catch (Exception e) {
                   // //System.out.println("Msg não enviada para: Nome - " + fgdVO.getFuncionario().getPessoa().getNome() + " Email - " + fgdVO.getFuncionario().getPessoa().getEmail() + ", " + fgdVO.getFuncionario().getPessoa().getEmail2());
                    throw e;
                }
            }
        } catch (Exception e) {
           // //System.out.println("EmailFacade erro ao incluir email - " + e.getMessage());
            throw e;
        }

    }

    public void realizarGravacaoEmailPorSuspensaoMatricuala(ComunicacaoInternaVO obj, ConfiguracaoGeralSistemaVO confEmail, String emailDest, String nomeDest, Boolean anexarImagensPadrao) throws Exception {
        StringBuilder corpohtml = new StringBuilder();
        executarAlteracaoNomeImagem(obj, corpohtml, obj.getMensagem());
        EmailVO emailVO = new EmailVO();
        emailVO.setAnexarImagensPadrao(anexarImagensPadrao);
        emailVO.setEmailDest(emailDest.trim());
        emailVO.setNomeDest(nomeDest);
        emailVO.setEmailRemet(confEmail.getEmailRemetente());
        emailVO.setNomeRemet(confEmail.getEmailRemetente());
        emailVO.setAssunto(obj.getAssunto());
        emailVO.setCaminhoAnexos("");
        emailVO.setMensagem(corpohtml.toString());
        emailVO.setRedefinirSenha(obj.getRedefinirSenha());
        if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) 
        		&& Uteis.isAtributoPreenchido(confEmail.getLocalUploadArquivoFixo())
        		&& Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima())){
        	emailVO.setCaminhoLogoEmailCima(confEmail.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima());                	
        }
        if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) 
        		&& Uteis.isAtributoPreenchido(confEmail.getLocalUploadArquivoFixo())                		
        		&& Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo())){                	
        	emailVO.setCaminhoLogoEmailBaixo(confEmail.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo());
        }
        incluirVerificandoPossuiMaisDeUmEmail(emailVO);
    }

    public void executarAlteracaoNomeImagem(ComunicacaoInternaVO comunicacaoInterna, StringBuilder corpohtml, String mensagem) throws Exception {
        String sb = mensagem;
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
        
        if (comunicacaoInterna.getCaminhoImagemAvaliacao_1().contains(comunicacaoInterna.getImgAvaliacao_1())) {
            sb = sb.replace(comunicacaoInterna.getCaminhoImagemAvaliacao_1().replace("./", ""), "cid:" + comunicacaoInterna.getImgAvaliacao_1());
            if (sb.contains("./cid:" + comunicacaoInterna.getImgAvaliacao_1())) {
                sb = sb.replace("./cid:" + comunicacaoInterna.getImgAvaliacao_1(), "cid:" + comunicacaoInterna.getImgAvaliacao_1());
            }
        }
        
        if (comunicacaoInterna.getCaminhoImagemAvaliacao_2().contains(comunicacaoInterna.getImgAvaliacao_2())) {
        	sb = sb.replace(comunicacaoInterna.getCaminhoImagemAvaliacao_2().replace("./", ""), "cid:" + comunicacaoInterna.getImgAvaliacao_2());
        	if (sb.contains("./cid:" + comunicacaoInterna.getImgAvaliacao_2())) {
        		sb = sb.replace("./cid:" + comunicacaoInterna.getImgAvaliacao_2(), "cid:" + comunicacaoInterna.getImgAvaliacao_2());
        	}
        }
        if (comunicacaoInterna.getCaminhoImagemAvaliacao_3().contains(comunicacaoInterna.getImgAvaliacao_3())) {
        	sb = sb.replace(comunicacaoInterna.getCaminhoImagemAvaliacao_3().replace("./", ""), "cid:" + comunicacaoInterna.getImgAvaliacao_3());
        	if (sb.contains("./cid:" + comunicacaoInterna.getImgAvaliacao_3())) {
        		sb = sb.replace("./cid:" + comunicacaoInterna.getImgAvaliacao_3(), "cid:" + comunicacaoInterna.getImgAvaliacao_3());
        	}
        }
        if (comunicacaoInterna.getCaminhoImagemAvaliacao_4().contains(comunicacaoInterna.getImgAvaliacao_4())) {
        	sb = sb.replace(comunicacaoInterna.getCaminhoImagemAvaliacao_4().replace("./", ""), "cid:" + comunicacaoInterna.getImgAvaliacao_4());
        	if (sb.contains("./cid:" + comunicacaoInterna.getImgAvaliacao_4())) {
        		sb = sb.replace("./cid:" + comunicacaoInterna.getImgAvaliacao_4(), "cid:" + comunicacaoInterna.getImgAvaliacao_4());
        	}
        }
        
        corpohtml.append(sb);
        sb = null;
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>Cidade</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do
     * atributo idêntico ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>CidadeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Override
    public EmailVO consultarPorOrdemDeCadastro(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * from email  order by datacadastro limit 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return null;
        }
        return montarDados(tabelaResultado);
    }

    public Boolean consultarQtdEmailMaiorQueConfiguracao(Integer qtdConfiguracao, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT count(*) as qtd from email ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return false;
        }
        int qtd = tabelaResultado.getInt("qtd");
        if (qtdConfiguracao == 0) {
            return false;
        }
        if (qtd > qtdConfiguracao) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>CidadeVO</code>.
     *
     * @return O objeto da classe
     * <code>CidadeVO</code> com os dados devidamente montados.
     */
    public static EmailVO montarDados(SqlRowSet dadosSQL) throws Exception {
        EmailVO obj = new EmailVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setEmailDest(dadosSQL.getString("emailDest").trim());
        obj.setNomeDest(dadosSQL.getString("nomeDest"));
        obj.setEmailRemet(dadosSQL.getString("emailRemet").trim());
        obj.setNomeRemet(dadosSQL.getString("nomeRemet"));
        obj.setAssunto(dadosSQL.getString("assunto"));
        obj.setMensagem(dadosSQL.getString("mensagem"));
        obj.setCaminhoAnexos(dadosSQL.getString("caminhoAnexos"));
        obj.setDataCadastro(dadosSQL.getTimestamp("datacadastro"));
        obj.setRedefinirSenha(dadosSQL.getBoolean("redefinirsenha"));
        obj.setCaminhoLogoEmailCima(dadosSQL.getString("caminhologoemailcima"));
        obj.setCaminhoLogoEmailBaixo(dadosSQL.getString("caminhologoemailbaixo"));
        obj.setAnexarImagensPadrao(dadosSQL.getBoolean("anexarImagensPadrao"));
        obj.setMultiplosDestinatarios(dadosSQL.getBoolean("multiplosdestinatarios"));
        if (obj.getMultiplosDestinatarios()) {
			try {
	        	Map<String, List<String>> map = new ObjectMapper().readValue(obj.getEmailDest(), new TypeReference<Map<String, List<String>>>(){});
	        	if (Uteis.isAtributoPreenchido(map)) {
	        		obj.setEmailDestMultiplosDestinatarios(map.values()
	        				.stream().flatMap(Collection::stream).limit(10L).collect(Collectors.joining("; ")));
	        	}
			} catch (Exception e) {
				obj.setEmailDestMultiplosDestinatarios("");
			}
        }
        obj.setErro(dadosSQL.getString("erro"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public void incluirVerificandoPossuiMaisDeUmEmail(EmailVO emailVO) throws Exception {
        try {
            validarDados(emailVO);
            if (emailVO.getEmailDest().contains(";")) {
                String[] emails = emailVO.getEmailDest().split("\\;");
                String emailUnico = null;
                for (int i = 0; i < emails.length; i++) {
                    emailUnico = emails[i];
                    if (emailUnico != null && !emailUnico.equals("")) {
                        EmailVO novoEmailVO = (EmailVO) emailVO.clone();
                        novoEmailVO.setEmailDest(emailUnico.trim());
                        incluir(novoEmailVO);
                    }
                }
            } else {
                incluir(emailVO);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return Email.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Email.idEntidade = idEntidade;
    }
	
	public List<EmailVO> consultarEmailsRedefinirSenha(ConfiguracaoGeralSistemaVO config, boolean redefinirSenha) {
		Connection cnn = null;
		PreparedStatement st = null;
		SqlRowSet dadosSQL = null;
		ResultSet rs = null;
		boolean conexaoManual = false;
		try {
			StringBuilder sqlStr = new StringBuilder();
			List<EmailVO> listaEmail = new ArrayList<EmailVO>();
			Date data = new Date();
			data.setHours(23);
			data.setMinutes(59);
			data.setSeconds(59);
			data = Uteis.getDataPassada(data, 1);
			sqlStr.append("SELECT * FROM email ");
			sqlStr.append("WHERE datacadastro >= '").append(Uteis.getDataJDBC(data)).append(" 00:00:00' and redefinirsenha = true ");
			sqlStr.append(" ORDER BY datacadastro desc ");
			try {
//				dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			} catch (DataAccessException dae) {
				cnn = getConexaoManual();
				st = cnn.prepareStatement(sqlStr.toString());
				rs = st.executeQuery();
				conexaoManual = true;
			} catch (Exception e) {
				if (e.getMessage().contains("Could not get JDBC Connection")) {
					throw e;
				} else {
					throw e;
				}
			}
			sqlStr = null;
			if (conexaoManual) {
				while (rs.next()) {
					EmailVO obj = new EmailVO();
					obj.setCodigo(new Integer(rs.getInt("codigo")));
					obj.setEmailDest(rs.getString("emailDest").trim());
					if (obj.getEmailDest().contains("/")) {
						obj.setEmailDest(obj.getEmailDest().substring(0, obj.getEmailDest().indexOf("/")));
					} else if (obj.getEmailDest().contains(";")) {
						obj.setEmailDest(obj.getEmailDest().substring(0, obj.getEmailDest().indexOf(";")));
					}
					obj.setNomeDest(rs.getString("nomeDest"));
					obj.setEmailRemet(rs.getString("emailRemet").trim());
					obj.setNomeRemet(rs.getString("nomeRemet"));
					obj.setAssunto(rs.getString("assunto"));
					obj.setMensagem(rs.getString("mensagem"));
					obj.setCaminhoAnexos(rs.getString("caminhoAnexos"));
					obj.setAnexarImagensPadrao(rs.getBoolean("anexarImagensPadrao"));
					obj.setCaminhoLogoEmailCima(dadosSQL.getString("caminhologoemailcima"));
			        obj.setCaminhoLogoEmailBaixo(dadosSQL.getString("caminhologoemailbaixo"));
					obj.setDataCadastro(rs.getTimestamp("datacadastro"));
					obj.setNovoObj(Boolean.FALSE);
					listaEmail.add(obj);
				}
			} else {
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
					obj.setRedefinirSenha(dadosSQL.getBoolean("redefinirsenha"));
					obj.setCaminhoLogoEmailCima(dadosSQL.getString("caminhologoemailcima"));
			        obj.setCaminhoLogoEmailBaixo(dadosSQL.getString("caminhologoemailbaixo"));
					obj.setDataCadastro(dadosSQL.getTimestamp("datacadastro"));
					obj.setNovoObj(Boolean.FALSE);
					listaEmail.add(obj);
				}
			}
			return listaEmail;
		} catch (Exception ex) {
			return new ArrayList<EmailVO>();
		} finally {

		}
	}
	
	public static Connection getConexaoManual() throws Exception {
		Connection conn = null;
		try {
			String url = "jdbc:postgresql://localhost:5432/SEIBD";
			String username = "postgres";
			String password = "589pglg";
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			// System.out.println("Erro ThreadEmail.criarConexao: " +
			// e.getMessage());
			throw e;
		}
	}

	public List<SMSVO> consultarSMSs(ConfiguracaoGeralSistemaVO config, boolean redefinirSenha) {
		int hora = new Date().getHours();
		Connection cnn = null;
		PreparedStatement st = null;
		SqlRowSet dadosSQL = null;
		ResultSet rs = null;
		boolean conexaoManual = false;
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
				sqlStr.append(" ORDER BY datacadastro desc LIMIT " + config.getQtdEmailEnvio());
				try {
					dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				} catch (DataAccessException dae) {
					cnn = getConexaoManual();
					st = cnn.prepareStatement(sqlStr.toString());
					rs = st.executeQuery();
					conexaoManual = true;
				} catch (Exception e) {
					if (e.getMessage().contains("Could not get JDBC Connection")) {
						throw e;
					} else {
						throw e;
					}
				}
				sqlStr = null;
				if (conexaoManual) {
					while (rs.next()) {
						SMSVO obj = new SMSVO();
						obj.setCodigo(new Integer(rs.getInt("codigo")));
						obj.setNomeDest(rs.getString("nomeDest"));
						obj.setAssunto(rs.getString("assunto"));
						obj.setCelular(rs.getString("celular"));
						obj.setMensagem(rs.getString("mensagem"));
						obj.setDataCadastro(rs.getTimestamp("datacadastro"));
						obj.setNovoObj(Boolean.FALSE);
						listaSMS.add(obj);
					}
				} else {
					while (dadosSQL.next()) {
						SMSVO obj = new SMSVO();
						obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
						obj.setNomeDest(dadosSQL.getString("nomeDest"));
						obj.setAssunto(dadosSQL.getString("assunto"));
						obj.setMensagem(dadosSQL.getString("mensagem"));
						obj.setCelular(dadosSQL.getString("celular"));
						obj.setDataCadastro(dadosSQL.getTimestamp("datacadastro"));
						obj.setNovoObj(Boolean.FALSE);
						listaSMS.add(obj);
					}
				}
				return listaSMS;

			} catch (Exception ex) {
				return new ArrayList<SMSVO>();
			} finally {

			}
		} else {
			return new ArrayList<SMSVO>();
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void atualizarCampoErroEmail(final Integer codigo, final String mensagemErro) throws Exception {
		final String sql = "UPDATE email set erro= ? WHERE (codigo = ?)";
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, mensagemErro);
					sqlAlterar.setInt(2, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			if (e.getMessage().contains("Could not get JDBC Connection")) {
				throw e;
			} else {
				throw e;
			}
		} finally {
			
		}
	}

	public void realizarExclusaoEmail(EmailVO email) throws Exception {
		Connection cnn = null;
		PreparedStatement st = null;
		String sql = "DELETE FROM Email WHERE ((codigo = ?))";
		try {
			getConexao().getJdbcTemplate().update(sql, new Object[] { email.getCodigo() });
		} catch (DataAccessException dae) {
			cnn = getConexaoManual();
			st = cnn.prepareStatement(sql.toString());
			st.setInt(1, email.getCodigo());
			st.executeUpdate();
			// System.out.println("Erro ThreadEmail.realizarExclusaoEmail DataAccessException: "
			// + dae.getMessage());
		} catch (Exception e) {
			if (e.getMessage().contains("Could not get JDBC Connection")) {
				// System.out.println("Erro if ThreadEmail.realizarExclusaoEmail: Could not get JDBC Connection - "
				// + e.getMessage());
				throw e;
			} else {
				// System.out.println("RealizarExclusaoEmail erro ao enviar email"
				// + e.getMessage());
				throw e;
			}
		} finally {
			cnn = null;
			st = null;
		}
	}
	
	public Boolean consultarEmailRedefinirSenhaUsuario(PessoaVO pessoa) throws Exception {     	
        String sqlStr = "SELECT codigo from email where emaildest = '" + pessoa.getEmail() +"' and redefinirSenha = true and datacadastro >= current_date ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return false;
        } else {
        	return true;
        }        
    }
	
	@Override
	public String capturarCaminhosAnexosEnvioEmail(ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) {
		List<File> listaAnexos = new ArrayList<File>();
		String caminhoAnexos = "";
		try {
			if (comunicacaoInternaVO.getListaArquivosAnexo().size() >= 1) {
				Iterator<ArquivoVO> i = comunicacaoInternaVO.getListaArquivosAnexo().iterator();
				while (i.hasNext()) {
					ArquivoVO arq = i.next();
					File fileList = getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(arq, configuracaoGeralSistemaVO);
					listaAnexos.add(fileList);
				}
				for (File file2 : listaAnexos) {
					caminhoAnexos = caminhoAnexos + getFacadeFactory().getArquivoHelper().copiarArquivoPastaBaseAnexoEmail(file2, PastaBaseArquivoEnum.ARQUIVO_TMP.getValue(), configuracaoGeralSistemaVO) + ";";
				}
			} 
		} catch (Exception e) {
			return "";
		}
		return caminhoAnexos;
	}
	
    public List<EmailVO> consultarEmails(DataModelo dataModelo, String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT * from email where 1 = 1 ");
        List<Object> filtros =  realizarGeracaoClausulaWhere(sqlStr, assunto, dataEnvio, destinatario, emailDestinatario, remetente, emailRemetente);
        sqlStr.append(" order by codigo desc ");
        UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());        
        return montarDadosConsulta(tabelaResultado);
    }

    public List<EmailVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<EmailVO> vetResultado = new ArrayList<EmailVO>(0);
		while (tabelaResultado.next()) {
			EmailVO obj = new EmailVO();
			obj = montarDados(tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}   
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
	public void excluirPorEmailDestinarioEassunto(String emailDestinatario, String assunto, UsuarioVO usuario) {		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("DELETE FROM email ");	
		sqlStr.append(" WHERE emaildest = trim('").append(emailDestinatario).append("')");
		sqlStr.append(" AND assunto = trim('").append(assunto).append("')");
		getConexao().getJdbcTemplate().update(sqlStr.toString());
		
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void executarReagendamentoTodosEmails(UsuarioVO usuarioLogado, String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("update email set datacadastro = now() where datacadastro < (current_date - 1) ");
            List<Object> filtros =  realizarGeracaoClausulaWhere(sql, assunto, dataEnvio, destinatario, emailDestinatario, remetente, emailRemetente);
            getConexao().getJdbcTemplate().update(sql.toString(), filtros.toArray());
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Integer consultarTotalEmails(DataModelo dataModelo, String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT count(codigo) as qtde from email where 1 = 1 ");
        List<Object> filtros =  realizarGeracaoClausulaWhere(sqlStr, assunto, dataEnvio, destinatario, emailDestinatario, remetente, emailRemetente);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }
    
    private List<Object> realizarGeracaoClausulaWhere(StringBuilder sql, String assunto, Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) {
    	List<Object> filtros = new ArrayList<Object>(0);
    	if(Uteis.isAtributoPreenchido(assunto)) {
    		sql.append(" and sem_acentos(assunto) ilike sem_acentos(?) ");
    		filtros.add(assunto+PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(dataEnvio)) {
    		sql.append(" and dataCadastro::DATE = ? ");
    		filtros.add(Uteis.getDataJDBC(dataEnvio));
    	}
    	if(Uteis.isAtributoPreenchido(destinatario)) {
    		sql.append(" and sem_acentos(nomeDest) ilike sem_acentos(?) ");
    		filtros.add(destinatario+PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(emailDestinatario)) {
    		sql.append(" and sem_acentos(emailDest) ilike sem_acentos(?) ");
    		filtros.add(emailDestinatario+PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(remetente)) {
    		sql.append(" and sem_acentos(nomeRemet) ilike sem_acentos(?) ");
    		filtros.add(remetente+PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(emailRemetente)) {
    		sql.append(" and sem_acentos(emailRemet) ilike sem_acentos(?) ");
    		filtros.add(emailRemetente+PERCENT);
    	}
    	return filtros;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void executarTodosEmails(String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("DELETE FROM email where 1 = 1 ");
            List<Object> filtros =  realizarGeracaoClausulaWhere(sql, assunto, dataEnvio, destinatario, emailDestinatario, remetente, emailRemetente);
            getConexao().getJdbcTemplate().update(sql.toString(), filtros.toArray());
        } catch (Exception e) {
            throw e;
        }
    }
    
	public void realizarGravacaoEmailMultiplosDestinatarios(ComunicacaoInternaVO obj, List<File> listaAnexos, Boolean anexarImagensPadrao, UsuarioVO usuarioLogado, ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		String caminhoAnexos = "";
		StringBuilder corpohtml = new StringBuilder();
		try {
			if (Uteis.isAtributoPreenchido(listaAnexos)) {
				for (File file : listaAnexos) {
					if (!file.getAbsolutePath().contains(PastaBaseArquivoEnum.ANEXO_EMAIL.getValue())) {
						caminhoAnexos += getFacadeFactory().getArquivoHelper().copiarArquivoPastaBaseAnexoEmail(file, PastaBaseArquivoEnum.ANEXO_EMAIL.getValue(), configuracaoGeralSistemaVO) + ";";
					} else {
						caminhoAnexos += file.getAbsolutePath() + ";";
					}
				}
			}
			if (anexarImagensPadrao) {
				executarAlteracaoNomeImagem(obj, corpohtml, obj.getMensagem());
			}
			EmailVO emailVO = new EmailVO();
			emailVO.setMultiplosDestinatarios(true);
			emailVO.setAnexarImagensPadrao(anexarImagensPadrao);
			emailVO.setEmailRemet(obj.getResponsavel().getEmail().trim());
			if (emailVO.getEmailRemet().equals("")) {
				emailVO.setEmailRemet(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno().getEmail());
			}
			if (emailVO.getEmailRemet().equals("")) {
				emailVO.setEmailRemet(configuracaoGeralSistemaVO.getEmailRemetente());
			}
			if (emailVO.getEmailRemet().equals("")) {
				emailVO.setEmailRemet("envio@sistema.com");
			}
			emailVO.setNomeRemet(obj.getResponsavel().getNome());
			if (obj.getResponsavel().getNome().equals("")) {
				emailVO.setEmailRemet(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno().getNome());
			}
			if (obj.getResponsavel().getNome().equals("")) {
				emailVO.setNomeRemet("SISTEMA SEI");
			}
			emailVO.setAssunto(obj.getAssunto());
			emailVO.setCaminhoAnexos(caminhoAnexos);
			String html = corpohtml.toString();
			if (!html.contains("<html")) {
				if (!html.contains("<body")) {
					html = "<body>" + html + "</body>";
				}
				html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head>" + html + "</html>";
			}
			emailVO.setMensagem(html);
			if (Uteis.isAtributoPreenchido(obj.getCaminhoImagemPadraoCima()) && obj.getCaminhoImagemPadraoCima().contains("logoUnidadeEnsino")) {
				emailVO.setCaminhoLogoEmailCima(obj.getCaminhoImagemPadraoCima());
			} else {
				if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) && Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima())) {
					emailVO.setCaminhoLogoEmailCima(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailCima());
				}
			}
			if (Uteis.isAtributoPreenchido(obj.getCaminhoImagemPadraoBaixo()) && obj.getCaminhoImagemPadraoBaixo().contains("logoUnidadeEnsino")) {
				emailVO.setCaminhoLogoEmailBaixo(obj.getCaminhoImagemPadraoBaixo());
			} else {
				if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) && Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo())) {
					emailVO.setCaminhoLogoEmailBaixo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getUnidadeEnsino().getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/") + "/" + obj.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo());
				}
			}
			Map<String, List<String>> mapDestinatarioEmails = new HashMap<>();
			List<EmailVO> emailVOs = new ArrayList<>();
			int limiteDestinatarios = configuracaoGeralSistemaVO.getLimiteDestinatariosPorEmail();
			int qtdeDestinatarios = 0;
			boolean existeLimiteDestinatarios = Uteis.isAtributoPreenchido(limiteDestinatarios);
//			for (ComunicadoInternoDestinatarioVO cidVO : obj.getComunicadoInternoDestinatarioVOs()) {
//				List<String> emailsDestinatario = new ArrayList<>();
//				String nomeDest = "";
//				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = null;
//				if (Uteis.isAtributoPreenchido(cidVO.getDestinatario().getCodigo())) {
//					if (!cidVO.getDestinatario().getListaPessoaEmailInstitucionalVO().isEmpty()) {
//						pessoaEmailInstitucionalVO = cidVO.getDestinatario().getListaPessoaEmailInstitucionalVO().get(0);
//					} else {
//						pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(cidVO.getDestinatario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
//					}
//				}
//				if (Uteis.isAtributoPreenchido(cidVO.getDestinatario().getCodigo())) {
//					if (cidVO.getDestinatario().getEmail() == null || cidVO.getDestinatario().getEmail().trim().equals("")) {
//						cidVO.setDestinatario(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(cidVO.getDestinatario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
//					}
//					nomeDest = cidVO.getDestinatario().getNome();
//					if ((Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO) && Uteis.isAtributoPreenchido(obj) && obj.getEnviarEmailInstitucional() && !pessoaEmailInstitucionalVO.getEmail().trim().isEmpty()) || (Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO) && Uteis.isAtributoPreenchido(obj) && obj.getEnviarEmailInstitucional() && !pessoaEmailInstitucionalVO.getEmail().trim().isEmpty())) {
//						emailsDestinatario.add(pessoaEmailInstitucionalVO.getEmail());
//						if (obj.getEnviarEmail() && Uteis.isAtributoPreenchido(cidVO.getDestinatario().getEmail())) {
//							emailsDestinatario.add(cidVO.getDestinatario().getEmail());
//						}
//					} else {
//						emailsDestinatario.add(cidVO.getDestinatario().getEmail());
//						emailsDestinatario.add(cidVO.getDestinatario().getEmail2());
//					}
//				} else if (!cidVO.getDestinatario().getNome().equals("")) {
//					nomeDest = cidVO.getDestinatario().getNome();
//					emailsDestinatario.add(cidVO.getEmail());
////				} else if (!cidVO.getDestinatarioParceiro().getCodigo().equals(0)) {
////					if (cidVO.getDestinatarioParceiro().getEmail() == null || cidVO.getDestinatarioParceiro().getEmail().trim().equals("")) {
////						cidVO.setDestinatarioParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(cidVO.getDestinatarioParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
////					}
////					nomeDest = cidVO.getDestinatarioParceiro().getNome();
////					emailsDestinatario.add(cidVO.getDestinatarioParceiro().getEmail());
////				}
//				if (existeLimiteDestinatarios) {
//					List<String> collect = emailsDestinatario.stream().distinct().collect(Collectors.toList());
//					if (Uteis.isAtributoPreenchido(collect)) {
//						collect.removeIf(s -> !Uteis.isAtributoPreenchido(s));
//					}
//					if (qtdeDestinatarios == limiteDestinatarios || (qtdeDestinatarios + collect.size()) > limiteDestinatarios) {
//						emailVOs.add((EmailVO) emailVO.realizarMontagemJsonEmailMultiplosDestinatarios(mapDestinatarioEmails).clone());
//						emailVO.setEmailDest("");
//						mapDestinatarioEmails = new HashMap<>();
//						if (Uteis.isAtributoPreenchido(collect)) {
//							mapDestinatarioEmails.put(nomeDest, new ArrayList<>());
//							mapDestinatarioEmails.get(nomeDest).addAll(collect);
//						}
//						qtdeDestinatarios = collect.size();
//					} else {
//						if (Uteis.isAtributoPreenchido(collect)) {
//							mapDestinatarioEmails.put(nomeDest, new ArrayList<>());
//							mapDestinatarioEmails.get(nomeDest).addAll(collect);
//						}
//						qtdeDestinatarios += collect.size();
//					}
//				} else {
//					List<String> collect = emailsDestinatario.stream().distinct().collect(Collectors.toList());
//					if (Uteis.isAtributoPreenchido(collect)) {
//						collect.removeIf(s -> !Uteis.isAtributoPreenchido(s));
//					}
//					if (Uteis.isAtributoPreenchido(collect)) {
//						mapDestinatarioEmails.put(nomeDest, new ArrayList<>());
//						mapDestinatarioEmails.get(nomeDest).addAll(collect);
//					}
//				}
//			}
//			if (Uteis.isAtributoPreenchido(mapDestinatarioEmails)) {
//				emailVOs.add((EmailVO) emailVO.realizarMontagemJsonEmailMultiplosDestinatarios(mapDestinatarioEmails).clone());
//			}
//			try {
//				for (EmailVO e : emailVOs) {
//					incluir(e);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw e;
//			}
//			if (progressBarVO != null && progressBarVO.getForcarEncerramento()) {
//				throw new Exception("Encerramento forçado do envio do comunicado interno.");
//			}
//			if (progressBarVO != null) {
//				progressBarVO.incrementar();
//			}
		} catch (Exception e) {
			throw e;
		}
	}
}

