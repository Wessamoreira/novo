package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarNaoLocalizadaArquivoRetornoVO;

public interface ContaPagarNaoLocalizadaArquivoRetornoInterfaceFacade {

	void incluir(ContaPagarNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception;

	void alterar(ContaPagarNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception;

	public List consultarPorNossoNumero(Long valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ContaPagarNaoLocalizadaArquivoRetornoVO consultarPorNossoNumeroUnico(Long valorConsulta, Date dataProcessamento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorPeriodoDataVcto(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorTratada(Boolean tratada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
