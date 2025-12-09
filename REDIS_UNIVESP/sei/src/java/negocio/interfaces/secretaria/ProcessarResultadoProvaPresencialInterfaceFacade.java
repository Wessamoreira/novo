/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.processosel.enumeradores.TipoProcessamentoProvaPresencial;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;

/**
 * @author Carlos Eugênio
 *
 */
public interface ProcessarResultadoProvaPresencialInterfaceFacade {
	public ResultadoProcessamentoArquivoRespostaProvaPresencialVO realizarProcessamentoArquivoResposta(FileUploadEvent uploadEvent, TipoProcessamentoProvaPresencial tipoProcessamentoProvaPresencial,  GabaritoVO gabaritoVO, String ano, String semestre, Integer configuracaoAcademico, String periodicidadeCurso, Boolean realizarCalculoMediaLancamentoNota, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaProvaPresencialDisciplinaVO> realizarObtencaoListaPorSituacaoLocalizacao(MatriculaProvaPresencialVO matriculaProvaPresencialVO, SituacaoMatriculaProvaPresencialDisciplinaEnum situacaoMatriculaProvaPresencialDisciplinaEnum, UsuarioVO usuarioVO);

	public Boolean existeResultadoProcessamentoRespostaPorGabarito(GabaritoVO gabaritoVO) throws Exception;
}
