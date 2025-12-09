package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaVO;

public interface ContaReceberRegistroArquivoInterfaceFacade {

    public void incluirContaReceberRegistroArquivo(Integer registroArquivoPrm, List<ContaReceberRegistroArquivoVO> objetos, UsuarioVO usuario) throws Exception;

    public void alterar(final ContaReceberRegistroArquivoVO obj, UsuarioVO usuario) throws Exception;

    public List<ContaReceberRegistroArquivoVO> consultaRapidaPorRegistroArquivo(Integer registroArquivo, Integer qtde, Integer inicio, String pessoa, String nossoNumero, String nrDocumento, boolean controlarAcesso, UsuarioVO usuario, Boolean contaRecebidaDuplicidade) throws Exception;

    public Integer consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(Integer registroArquivo, String pessoa, String nossoNumero, String nrDocumento, boolean controlarAcesso, UsuarioVO usuario, Boolean contaRecebidaDuplicidade) throws Exception;
    
	void excluirPorCodigoRegistroArquivo(Integer codigoRegistroArquivo, UsuarioVO usuario) throws Exception;
	
	public boolean consultSeExisteContaReceberRegistroArquivoComSituacaoReceber(ControleCobrancaVO controleCobranca, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean verificarExistenciaContaReceberRecebidaDuplicidade(Integer contaReceber, Integer registroArquivoNaoConsiderar, Date dataRecebimento);

	/** 
	 * @author Wellington - 16 de set de 2015 
	 * @param contaReceberRegistroArquivoVOs
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void montarDadosAtualizacaoContaReceber(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, UsuarioVO usuarioVO) throws Exception;
	
	public List<ContaReceberRegistroArquivoVO> consultaRapidaPorRegistroArquivoDuplicidade(Integer registroArquivo, Integer qtde, Integer inicio, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void alterarContaReceberRegistroArquivoAReceberJaRecebida(Integer contaReceber, UsuarioVO usuario) throws Exception;

	void removerVinculoComMatricula(String matricula, UsuarioVO usuario) throws Exception;
	
	public void alterarUnidadeEnsino (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}
