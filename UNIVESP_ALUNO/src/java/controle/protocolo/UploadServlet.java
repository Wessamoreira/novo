package controle.protocolo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.jakarta.JakartaServletDiskFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import controle.arquitetura.AplicacaoControle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocio.comuns.utilitarias.SpringUtil;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.JakartaServletDiskFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@WebServlet (name = "UploadServlet")
public class UploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private SpringUtil springUtil;
       
    public UploadServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
		JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);			
		List<DiskFileItem> items = upload.parseRequest(request);
		
		if (items.stream().anyMatch(i -> !i.isFormField()) && request.getParameter("caminhoBase") != null) {
			try {	
				AplicacaoControle aplicacaoControle = (AplicacaoControle) springUtil.getApplicationContext().getBean(AplicacaoControle.class);				
				String caminhoBase = aplicacaoControle.getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo();//request.getParameter("caminhoBase");
				if(!caminhoBase.endsWith(File.separator) && !request.getParameter("caminhoBase").startsWith("/")){
					caminhoBase += File.separator;
				}
				caminhoBase += request.getParameter("caminhoBase");
				caminhoBase = caminhoBase.replace("/", File.separator);
				
				String nomeArquivo = ""; 
				for (DiskFileItem fileItem : items) {
					nomeArquivo = new Date().getTime()+""+fileItem.getName().substring(fileItem.getName().lastIndexOf("."), fileItem.getName().length());
					fileItem.write(Path.of(caminhoBase+ File.separator+nomeArquivo));					
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
			
			 if (items.stream().anyMatch(i -> !i.isFormField())) {
		            try {
		 
		                for (DiskFileItem item : items) {
		                    if (!item.isFormField()) {
		                        item.write(Path.of(arquivoFoto));
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
