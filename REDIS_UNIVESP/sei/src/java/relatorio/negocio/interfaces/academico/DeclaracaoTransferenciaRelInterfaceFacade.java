package relatorio.negocio.interfaces.academico;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DeclaracaoTransferenciaRelVO;

public interface DeclaracaoTransferenciaRelInterfaceFacade {

	public DeclaracaoTransferenciaRelVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public DeclaracaoTransferenciaRelVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception;

	public String imprimirDeclaracaoTransferencia(DeclaracaoTransferenciaRelVO declaracaoTransferenciaRelVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

}