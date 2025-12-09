package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaArquivoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ComunicacaoInternaArquivoInterfaceFacade {

	void incluirLista(ComunicacaoInternaVO obj, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	List<ComunicacaoInternaArquivoVO> consultarPorCodigoComunicacaoInterna(Integer valorConsulta,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
