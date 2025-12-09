package negocio.interfaces.administrativo;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;

public interface IntegracaoSymplictyInterfaceFacade {

	public String gerarArquivoTxt(ConfiguracaoGeralSistemaVO config) throws Exception;
	public Boolean enviarArquivoSFTP(String caminhoArquivo, ConfiguracaoGeralSistemaVO config) throws Exception;
//	public void incluirLogJobSymplicty(String erro, Boolean sucesso) throws Exception;
	public void executarJobSymplicty();

	
}
