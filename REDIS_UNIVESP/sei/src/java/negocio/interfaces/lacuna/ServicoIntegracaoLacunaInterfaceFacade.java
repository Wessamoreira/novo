package negocio.interfaces.lacuna;

import java.io.File;

import negocio.comuns.academico.DocumentoAssinadoVO;

public interface ServicoIntegracaoLacunaInterfaceFacade {

	public String iniciandoRestPKI(DocumentoAssinadoVO documentoAssinado, Integer ordemAssinatura) throws Exception;

	public File finalizandoRestPKI(DocumentoAssinadoVO documentoAssinado, Integer ordemAssinatura, String tokenLacuna) throws Exception;
}