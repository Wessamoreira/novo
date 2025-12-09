package negocio.interfaces.basico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface ConfiguracaoGEDInterfaceFacade {

	void persistir(ConfiguracaoGEDVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConfiguracaoGEDVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	List<ConfiguracaoGEDVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ConfiguracaoGEDVO> consultaRapidaPorCodigo(Integer codCidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void carregarDados(ConfiguracaoGEDVO obj, UsuarioVO usuario) throws Exception;

	void carregarDados(ConfiguracaoGEDVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	void alterarSeloAssinaturaEletronica(ConfiguracaoGEDVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	ConfiguracaoGEDVO consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Map<Integer, ConfiguracaoGEDVO> consultarConfiguracaoGED(UsuarioVO usuario) throws Exception;

	void realizarValidacaoTokenProvedorDeAssinatura(ConfiguracaoGEDVO obj, UsuarioVO usuarioVO);

	Boolean consultarExisteConfiguracaoGedComAtaColacaoGrau(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Map<Boolean, Boolean> consultarPermitirAlunoAssinarColacaoGrau(UsuarioVO usuario) throws Exception;

	ConfiguracaoGEDVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;

	ConfiguracaoGEDVO consultarPorUnidadeEnsinoUnica(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
    
}