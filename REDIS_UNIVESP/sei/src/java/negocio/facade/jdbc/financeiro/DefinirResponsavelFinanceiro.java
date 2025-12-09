package negocio.facade.jdbc.financeiro;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.DefinirResponsavelFinanceiroInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class DefinirResponsavelFinanceiro extends ControleAcesso implements DefinirResponsavelFinanceiroInterfaceFacade {

	protected static String idEntidade;

	public DefinirResponsavelFinanceiro() throws Exception {
		super();
		setIdEntidade("DefinirResponsavelFinanceiro");
	}

	public void executarDefinirAlterarResponsavelFinanceiro(List<ContaReceberVO> contaReceberVOs, PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(pessoaVO)) {
			throw new ConsistirException("É necessário selecionar o Responsável Financeiro.");
		}
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			if (contaReceberVO.getSelecionado() && !contaReceberVO.getResponsavelFinanceiro().getCodigo().equals(pessoaVO.getCodigo())) {
				if (pessoaVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor()) && contaReceberVO.getPessoa().getCodigo().equals(pessoaVO.getCodigo())) {
					contaReceberVO.setResponsavelFinanceiro(null);
					if(!contaReceberVO.getTipoOrigem().equals("BCC")){
						contaReceberVO.setTipoPessoa(TipoPessoa.ALUNO.getValor());
					}
				} else {
					contaReceberVO.setResponsavelFinanceiro(pessoaVO);
					if(!contaReceberVO.getTipoOrigem().equals("BCC")){
						contaReceberVO.setTipoPessoa(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());	
					}
				}
				if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
					getFacadeFactory().getContaReceberFacade().alterarPessoaContaReceberSituacaoAReceber(contaReceberVO, usuarioVO);
				} else {
					getFacadeFactory().getContaReceberFacade().alterarPessoaContaReceberSituacaoRecebido(contaReceberVO, usuarioVO);
				}
			}
		}
	}

	public static String getIdEntidade() {
		return DefinirResponsavelFinanceiro.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		DefinirResponsavelFinanceiro.idEntidade = idEntidade;
	}

}
