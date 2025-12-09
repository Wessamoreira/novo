package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.DeclaracaoImpostoRendaRelVO;

public interface DeclaracaoImpostoRendaRelInterfaceFacade {

	public List<DeclaracaoImpostoRendaRelVO> criarObjeto(DeclaracaoImpostoRendaRelVO obj, UsuarioVO usuario, String tipoPessoa, DeclaracaoImpostoRendaRelVO filtroTipoOrigem, Boolean apresentarDataPrevisaoRecebimentoVencimentoConta) throws Exception;

	public String designIReportRelatorio();

	public String caminhoBaseRelatorio();

	public void consultarPermissoesImpressaoDeclaracaoVisaoAluno(DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO,UsuarioVO usuarioVO) throws Exception;
	
	public List<DeclaracaoImpostoRendaRelVO>executarConsultaParametrizada(DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO, UsuarioVO usuario, String tipoPessoa, String ano) throws Exception;
	
}
