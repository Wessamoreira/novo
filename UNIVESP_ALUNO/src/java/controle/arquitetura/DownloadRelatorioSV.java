package controle.arquitetura;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Servlet implementation class DownloadSV
 */
@WebServlet(name="DownloadRelatorioSV")
public class DownloadRelatorioSV extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7097026357750460264L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadRelatorioSV() {
        super();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        File file = null;
        String arquivoGet = null;
        String arquivoPost = null;
        String arquivo = null;
        ByteArrayOutputStream arrayOutputStream = null;
        OutputStream out = null;
        String nomeFile = "";
        try {

            arquivoGet = (String) request.getParameter("relatorio");           
            arquivoPost = (String) request.getAttribute("relatorio");
            arquivo = "";
            LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");
            if(loginControle == null || !Uteis.isAtributoPreenchido(loginControle.getUsuario())) {
            	throw new Exception("Você está acessando uma área restrita ou não está autenticado no sistema.");
            }
            if (arquivoGet == null) {
            	if(arquivoPost.contains(File.separator)) {            		
            		if(arquivoPost.startsWith(loginControle.getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()) 
            				|| arquivoPost.startsWith(loginControle.getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo())
            				|| arquivoPost.replace("/", File.separator).startsWith(request.getServletPath()+"relatorio")
            				|| arquivoPost.replace("/", File.separator).startsWith(loginControle.getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo().replace("/", File.separator))) {
            			arquivo = arquivoPost;
            			nomeFile = arquivoPost.substring(arquivoPost.lastIndexOf(File.separator), arquivoPost.length());
            		}else {
            			throw new Exception("Você está acessando uma área restrita.");
            		}
            	}else {
            		arquivo = UteisJSF.getCaminhoWeb() + "relatorio/" + arquivoPost;
            		nomeFile = arquivoPost;
            	}
            } else {
            	if(arquivoGet.contains("/")) {
            		if(arquivoGet.startsWith(loginControle.getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()) 
            				|| arquivoGet.replace("/", File.separator).startsWith(request.getServletContext().getRealPath("")+"relatorio")
            				|| arquivoGet.startsWith(loginControle.getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo())
            				|| arquivoGet.replace("/", File.separator).startsWith(loginControle.getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo().replace("/", File.separator))) {
            			arquivo = arquivoGet;
            			nomeFile = arquivoGet.substring(arquivoGet.lastIndexOf("/")+1, arquivoGet.length());
            		}else {
            			throw new Exception("Você está acessando uma área restrita.");
            		}
            	}else {
            		arquivo = UteisJSF.getCaminhoWeb() + "relatorio/" + arquivoGet;
//            		System.out.println("Caminho do Relatorio: "+arquivo);
            		nomeFile = arquivoGet;
            	}
            }
            file = new File(arquivo);            
//            String nomeFile = arquivo.substring(arquivo.lastIndexOf(File.separator) + 1, arquivo.length());
            if (nomeFile.endsWith(".pdf")) {
                response.setHeader("Content-Disposition", "inline;filename=" + nomeFile);
                String userAgent = request.getHeader("User-Agent");
    			if(Uteis.ClienteMovel(userAgent)){
    				response.setHeader("Content-Disposition", "inline;filename=" + nomeFile);    				
    			}else {
    				response.setHeader("Content-Disposition", "attachment;filename=" + nomeFile);    				
    			}
                response.setContentType("application/pdf");
            } else if (nomeFile.endsWith(".xls")) {
                response.setHeader("Content-Disposition", "attachment;filename=" + nomeFile);
                response.setContentType("application/vnd.ms-excel");
            } else if (nomeFile.endsWith(".xlsx")) {
                response.setHeader("Content-Disposition", "attachment;filename=" + nomeFile);
                response.setContentType("application/vnd.openxmlformats");
            } else {
                response.setHeader("Content-Disposition", "filename=" + nomeFile);
                response.setContentType("application/octet-stream");
            }
            //request.getRequestDispatcher(response.encodeRedirectURL(request.getRequestURL().toString().replace("DownloadRelatorioSV","relatorio/"+arquivo))).forward(request, response);

            arrayOutputStream = criarArray(file);
            arrayOutputStream.flush();
            out = response.getOutputStream();
            out.write(arrayOutputStream.toByteArray());
            out.flush();

        } catch (Exception e) {
            throw e;
        } finally {
            file = null;
            arquivoGet = null;
            arquivoPost = null;
            arquivo = null;
            if (arrayOutputStream != null) {
                arrayOutputStream.close();
            }
            arrayOutputStream = null;
            nomeFile = null;
            if (out != null) {
                out.close();
            }
            out = null;
        }
    }

    private ByteArrayOutputStream criarArray(File arquivo) throws Exception {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fi = null;
        byte buffer[] = null;
        try {
            buffer = new byte[4096];
            int bytesRead = 0;
            fi = new FileInputStream(arquivo.getAbsolutePath());
            while ((bytesRead = fi.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, bytesRead);
            }
            arrayOutputStream.close();
            fi.close();
            return arrayOutputStream;
        } catch (Exception e) {
            throw e;
        } finally {
            fi = null;
            buffer = null;
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e.getCause());
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e.getCause());
        }
    }
}
