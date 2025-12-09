package negocio.interfaces.academico;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoAtaResultadosFinaisVO;
import negocio.comuns.academico.ConfiguracaoLayoutAtaResultadosFinaisVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

public interface ConfiguracaoLayoutAtaResultadosFinaisInterfaceFacade {
	
	void persistir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoLayoutAtaResultadosFinaisVO> consultar(UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoLayoutAtaResultadosFinaisVO> consultarPorConfiguracaoAtaResultadosFinais(Integer  configuracaoAtaResultadosFinais, UsuarioVO usuarioVO) throws Exception;
	
	void adicionarConfiguracaoLayoutAtaResultadosFinaisVO(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception;

	void realizarInclusaoLayoutPadraoAtaResultadosFinais(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, String caminhoBase)  throws Exception;

	ConfiguracaoLayoutAtaResultadosFinaisVO consultarPorChavePrimaria(Integer configuracaoLayoutAtaResultadosFinais, UsuarioVO usuarioVO)
			throws Exception;

	void validarDados(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO)
			throws ConsistirException;

	void excluir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception;

	void adicionarLayout(FileUploadEvent uploadEvent, TipoRelatorioEnum tipoRelatorio, 
			ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO,
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception;

	void removerLayout(ArquivoVO arquivoVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO,
			UsuarioVO usuarioVO) throws Exception;

	void persistir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void realizarDefinicaoArquivoPrincipalConfiguracaoLayoutAtaResultadosFinais(
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, ArquivoVO arquivoVO, UsuarioVO usuarioVO)
			throws Exception;

	void realizarDefinicaoLayoutPadraoConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO,
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception;
}
