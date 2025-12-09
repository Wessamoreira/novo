package negocio.facade.jdbc.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.elastic.ExcecaoElasticVO;
import negocio.comuns.elastic.IndiceElasticVO;
import negocio.comuns.elastic.IndiceVersaoElasticVO;
import negocio.comuns.elastic.SincronismoElasticVO;

public interface IndiceElasticInterfaceFacade {
	
	IndiceElasticVO consultarPorNome(String valorConsulta, UsuarioVO usuario) throws Exception;
	
	List<SincronismoElasticVO> consultarSincronismos() throws Exception;
	
	List<ExcecaoElasticVO> consultarExcecoesPesquisa() throws Exception;
	
	Integer consultarTotalExcecoesPesquisa() throws Exception;
	
	List<ExcecaoElasticVO> consultarExcecoesAgendamento() throws Exception;
	
	Integer consultarTotalExcecoesAgendamento() throws Exception;
	
	List<ExcecaoElasticVO> consultarExcecoesSincronismo() throws Exception;
	
	Integer consultarTotalExcecoesSincronismo() throws Exception;

	void executarCriacaoIndiceElastic(IndiceElasticVO indice, UsuarioVO usuario) throws Exception;

	void executarCriacaoIndiceVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception;

	void executarExclusaoIndiceElastic(IndiceElasticVO indice, UsuarioVO usuario) throws Exception;

	void executarExclusaoIndiceVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception;

	void executarAtivacaoIndiceVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception;
	
	void executarAgendamentoIndexacaoVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception;

}
