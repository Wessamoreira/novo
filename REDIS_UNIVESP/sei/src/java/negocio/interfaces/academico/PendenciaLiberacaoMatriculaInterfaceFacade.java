package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PendenciaLiberacaoMatriculaVO;
import negocio.comuns.academico.enumeradores.MotivoSolicitacaoLiberacaoMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PendenciaLiberacaoMatriculaInterfaceFacade {

	void persistir(PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	 List<PendenciaLiberacaoMatriculaVO> consultarPorMatricula(String matricula) throws Exception;

	 public PendenciaLiberacaoMatriculaVO consultarPendenciaLiberacaoMatriculaPendentePorMatriculaEMotivo(String matricula, MotivoSolicitacaoLiberacaoMatriculaEnum motivoSolicitacaoMatriculaEnum, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
	 
	 public void indeferirLiberacaoPendenciaMatricula(PendenciaLiberacaoMatriculaVO obj, CancelamentoVO cancelamentoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
	 
	 public void deferirLiberacaoPendenciaMatricula(PendenciaLiberacaoMatriculaVO obj, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    
	 public void alterarSituacaoLiberacaoPendenciaMatricula(String matricula, UsuarioVO usuario) throws Exception;
	 
	 public Boolean verificarSeOutraPendenciaExistente(String matricula, Integer codigoPendenciaLiberacaoMatricula) throws Exception;
}
