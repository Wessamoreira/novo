package negocio.interfaces.academico;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.NotaConceitoIndicadorAvaliacaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface NotaConceitoIndicadorAvaliacaoInterfaceFacade {

	void persistir(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void excluir(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void inativar(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void ativar(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	List<NotaConceitoIndicadorAvaliacaoVO> consultar(String consultarPor, String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	NotaConceitoIndicadorAvaliacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void validarDados(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO) throws ConsistirException;

	void uploadArquivo(FileUploadEvent uploadEvent, NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
}
