package negocio.interfaces.faturamento.nfe;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.LoteInutilizacaoVO;



/**
 *
 */
public interface InutilizacaoNotaInterfaceFacade {

	void inutilizarNota(LoteInutilizacaoVO loteInutulizacao, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception;
	
	void inutilizarNotaWebservice(LoteInutilizacaoVO loteInutilizacao, ConfiguracaoGeralSistemaVO confGeralSistemaWebserviceVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception;

	List<LoteInutilizacaoVO> buscarPorPerido(Date dataInicial, Date dataFinal, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception ;
	
	List<LoteInutilizacaoVO> buscarNumeroNota(Integer numeroNota,  int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception ;
}
