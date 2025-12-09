package jobs;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import controle.basico.IntegracaoEADIPOGControle;
import negocio.comuns.academico.HistoricoVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobIntegracaoEADIPOG extends SuperFacadeJDBC implements Runnable {

	@Override
	public void run() {
		try {

			IntegracaoEADIPOGControle integracao = new IntegracaoEADIPOGControle();
			List<HistoricoVO> listaHist = integracao.getObjetoStatusAluno();
			Iterator i = listaHist.iterator();
			while (i.hasNext()) {
				HistoricoVO hist = (HistoricoVO) i.next();
				try {
					if (hist.getMediaFinal() != null && !hist.getSituacao().equals("NC") && !hist.getSituacao().equals("")) {
						getFacadeFactory().getPessoaEADIPOGFacade().processarStatusAluno(hist);
						try {
							HttpURLConnection request = (HttpURLConnection) new URL("http://ipog.estudar.online/moodle/servicos/notas.php").openConnection();

					            // Define que a conexão pode enviar informações e obtê-las de volta:
					            request.setDoOutput(true);
					            request.setDoInput(true);

					            // Define o content-type:
					            //request.setRequestProperty("Content-Type", "application/json");
					            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					            
					            String key = "key=0de0bef380eb81a00b686d3e31c9bd7c";
					            String disciplina = "&disciplina=16";
					            String matricula = "&matricula=" + hist.getMatricula().getMatricula();
					            String turma = "&turma=" + hist.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo();
					            String urlParameters = key + disciplina + matricula + turma;
					            
					            // Define o método da requisição:
					            request.setRequestMethod("POST");
					            DataOutputStream wr = new DataOutputStream(request.getOutputStream());					            
					    		wr.writeBytes(urlParameters);
					    		wr.flush();
					    		wr.close();					            
					            					            
					            // Conecta na URL:
					            request.connect();
					            request.toString();		
					            request.getResponseMessage(); 

						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						// c.setMsgErroAtualizacaoStatus("Status não atualizado -> "
						// + hist.getMediaFinal() + " - " +hist.getSituacao());
						// c.setErroAtualizacaoStatus(Boolean.TRUE);
						// getFacadeFactory().getPessoaEADIPOGFacade().alterarSituacao(c);
						// salva a msg de erro na tabela, para verificação do
						// mesmo e marca um boolean de erro.
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}