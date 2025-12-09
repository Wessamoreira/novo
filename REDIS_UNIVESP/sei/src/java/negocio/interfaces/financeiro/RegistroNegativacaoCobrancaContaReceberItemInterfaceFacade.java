package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;


public interface RegistroNegativacaoCobrancaContaReceberItemInterfaceFacade {
    
	public void incluir(final RegistroNegativacaoCobrancaContaReceberItemVO obj, UsuarioVO usuario) throws Exception;
    
	public void alterarExclusao(final RegistroNegativacaoCobrancaContaReceberItemVO obj) throws Exception;
	
	public void incluirListaVOs(List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs, RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO, UsuarioVO usuarioLogado) throws Exception;
    
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarRegistroNegativacaoCobrancaContaReceberItem(AgenteNegativacaoCobrancaContaReceberVO agente, String situacaoContaReceber, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgenteNegativacaoCobrancaContaReceberEnum, Date dataInicio, Date dataFinal, String situacaoParcelaNegociada, String consultarPor, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoPessoa, MatriculaVO matriculaAluno, ParceiroVO parceiro, FuncionarioVO funcionario, PessoaVO responsavelFinanceiro, String situacaoRegistro, FornecedorVO fornecedorVO);
	
	public void excluirNegativacaoCobrancaListagemVOs(AgenteNegativacaoCobrancaContaReceberVO anccr, List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs, String motivo, boolean removerNegativacaoContaReceberViaIntegracao, boolean verificarAcesso, ConfiguracaoGeralSistemaVO config, UsuarioVO responsavel) throws Exception;
	
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> carregarHistoricoNegativacaoContaReceber(Integer contareceber);
	
	public Boolean verificarMatriculaPossuiNegativacaoCobranca(Integer pessoa);

	void estornarNegativacaoCobrancaListagemVOs(
			List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs, AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO,
			TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgenteNegativacaoCobrancaContaReceberEnum, UsuarioVO responsavel) throws Exception;

	void removerVinculoContaReceberRegistroNegativacaoContaReceber(Integer contaReceber, UsuarioVO usuarioVO)
			throws Exception;

	void correcaoSerasaApiGeo(String nossoNumero) throws Exception;
}
