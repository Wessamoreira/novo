package relatorio.negocio.interfaces.academico;

import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.InscricaoVO;
import relatorio.negocio.comuns.academico.DeclaracaoAprovacaoVestVO;

public interface DeclaracaoAprovacaoVestRelInterfaceFacade {

	public DeclaracaoAprovacaoVestVO consultarPorInscricaoCandidato(InscricaoVO inscricao, Integer nivelMontarDados) throws Exception;

	public DeclaracaoAprovacaoVestVO montarDados(PessoaVO pessoa, InscricaoVO inscricao) throws Exception;

}