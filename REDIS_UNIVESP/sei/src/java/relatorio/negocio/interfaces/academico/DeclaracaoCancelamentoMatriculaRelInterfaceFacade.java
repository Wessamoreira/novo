package relatorio.negocio.interfaces.academico;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DeclaracaoCancelamentoMatriculaVO;

public interface DeclaracaoCancelamentoMatriculaRelInterfaceFacade {

	public DeclaracaoCancelamentoMatriculaVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public DeclaracaoCancelamentoMatriculaVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception;

}