package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.ContaRecebidaVersoContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface ContaRecebidaVersoContaReceberRelInterfaceFacade {

	List<ContaRecebidaVersoContaReceberRelVO> realizarGeracaoRelatorio(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataTermino, Integer curso, Integer turma, String tipoPessoa, String matricula, Integer candidato, Integer funcionario, Integer parceiro, Integer fornecedor, Integer requerente, Integer responsavelFinanceiro, Integer contaCorrente, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPlanoFinanceiroCurso, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Boolean filtrarContasRecebidasDataRecebimento, Boolean filtrarContasRegistroCobranca, String ordenarPor, Integer tipoRequerimento, UsuarioVO usuarioVO, Boolean considerarUnidadeFinanceira) throws Exception;
	
}
