package controle.protocolo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.utilitarias.SpringUtil;

public class UploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public UploadServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (ServletFileUpload.isMultipartContent(request) && request.getParameter("caminhoBase") != null) {
			try {	
				AplicacaoControle aplicacaoControle = (AplicacaoControle) SpringUtil.getApplicationContext().getBean(AplicacaoControle.class);				
				String caminhoBase = aplicacaoControle.getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo();//request.getParameter("caminhoBase");
				if(!caminhoBase.endsWith(File.separator) && !request.getParameter("caminhoBase").startsWith("/")){
					caminhoBase += File.separator;
				}
				caminhoBase += request.getParameter("caminhoBase");
				caminhoBase = caminhoBase.replace("/", File.separator);
				DiskFileItemFactory diskFileUpload = new DiskFileItemFactory();				
				List<FileItem> fileItems = new ServletFileUpload(diskFileUpload).parseRequest(request);
				File file = new File(caminhoBase);
				if(!file.exists()){
					file.mkdirs();
				}
				String nomeArquivo = ""; 
				for (FileItem fileItem : fileItems) {
					nomeArquivo = new Date().getTime()+""+fileItem.getName().substring(fileItem.getName().lastIndexOf("."), fileItem.getName().length());
					fileItem.write(new File(caminhoBase+ File.separator+nomeArquivo));					
				}
				String caminhoExterno = aplicacaoControle.getConfiguracaoGeralSistemaVO(0, null).getUrlExternoDownloadArquivo();
				if(!caminhoExterno.endsWith("/") && !request.getParameter("caminhoBase").startsWith("/")){
					caminhoExterno += "/";
				}
				caminhoExterno += request.getParameter("caminhoBase");
				if(!caminhoExterno.endsWith("/")){
					caminhoExterno += "/";
				}
				caminhoExterno += nomeArquivo;
				PrintWriter out =response.getWriter(); 
				out.println("<html><body>");
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.CKEDITOR.tools.callFunction(1, '"+caminhoExterno+"', '' );");
				out.println("</script>");
				out.println("</body></html>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
			HttpSession session = request.getSession(true);

			String arquivoFoto = (String) session.getAttribute("arquivoFoto");
			
			 if (ServletFileUpload.isMultipartContent(request)) {
		            try {
		                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		 
		                for (FileItem item : multiparts) {
		                    if (!item.isFormField()) {
		                        item.write(new File(arquivoFoto));
		                    }
		                }
		            } catch (Exception ex) {
		            }
		 
		        } 
//			ServletInputStream inputStream = request.getInputStream();
//
//			OutputStream outputStream = new FileOutputStream(arquivoFoto);
//			byte[] bytes = new byte[1024];
//
//			int len;
//			while ((len = inputStream.read(bytes)) > 0)
//				outputStream.write(bytes, 0, len);
//
//			outputStream.flush();
//			outputStream.close();
//			inputStream.close();
		}
	}

}
