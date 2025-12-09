package negocio.interfaces.academico;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.TrabalhoConclusaoCursoArquivoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.TipoArquivoTCCEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TrabalhoConclusaoCursoArquivoInterfaceFacade {

	void realizarPostagemArquivo(FileUploadEvent uploadEvent, TipoArquivoTCCEnum tipoArquivoTCCEnum, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	List<TrabalhoConclusaoCursoArquivoVO> consultarPorTCCEtapa(Integer tcc, EtapaTCCEnum etapaTCCEnum, TipoArquivoTCCEnum tipoArquivoTCCEnum, Integer limite, Integer offset) throws Exception;

	Integer consultarTotalRegistroPorTCCEtapa(Integer tcc, EtapaTCCEnum etapaTCCEnum, TipoArquivoTCCEnum tipoArquivoTCCEnum) throws Exception;
	
	void excluir(TrabalhoConclusaoCursoArquivoVO trabalhoConclusaoCursoArquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
}
