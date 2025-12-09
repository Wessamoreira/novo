package negocio.interfaces.academico;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

public interface ConfiguracaoLayoutHistoricoInterfaceFacade {
	
	void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoLayoutHistoricoVO> consultarPorNivelEducacional(TipoNivelEducacional tipoNivelEducacional, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoLayoutHistoricoVO> consultarPorConfiguracaoHistorico(Integer  configuracaoHistorico, UsuarioVO usuarioVO) throws Exception;
	
	void adicionarConfiguracaoLayoutHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarInclusaoLayoutPadraoHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO, String caminhoBase)  throws Exception;

	ConfiguracaoLayoutHistoricoVO consultarPorChavePrimaria(Integer configuracaoLayoutHistorico, UsuarioVO usuarioVO)
			throws Exception;

	void validarDados(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO)
			throws ConsistirException;

	void excluir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	void adicionarLayout(FileUploadEvent uploadEvent, TipoRelatorioEnum tipoRelatorio, 
			ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	void removerLayout(ArquivoVO arquivoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO,
			UsuarioVO usuarioVO) throws Exception;

	void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void realizarDefinicaoArquivoPrincipalConfiguracaoLayoutHistorico(
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, ArquivoVO arquivoVO, UsuarioVO usuarioVO)
			throws Exception;

	void realizarDefinicaoLayoutPadraoConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	public ConfiguracaoLayoutHistoricoVO consultarPorChavePrimaria(Integer configuracaoLayoutHistorico, Boolean retornaException, UsuarioVO usuarioVO) throws Exception;
}
