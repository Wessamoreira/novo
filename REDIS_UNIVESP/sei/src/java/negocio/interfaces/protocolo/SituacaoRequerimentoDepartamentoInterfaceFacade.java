package negocio.interfaces.protocolo;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.SituacaoRequerimentoDepartamentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface SituacaoRequerimentoDepartamentoInterfaceFacade {
	
	void incluir(final SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO,final  UsuarioVO usuarioVO) throws Exception;
	void alterar(final SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO,final  UsuarioVO usuarioVO) throws Exception;
	void excluir(final SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO,final  UsuarioVO usuarioVO) throws Exception;
	SituacaoRequerimentoDepartamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;
	void validarDados(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) throws ConsistirException;
	void validarUnicidade(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) throws ConsistirException;
	List<SituacaoRequerimentoDepartamentoVO> consultarPorSituacao(String situacao, UsuarioVO usuarioVO) throws Exception;
	List<SituacaoRequerimentoDepartamentoVO> montarDadosConsulta(SqlRowSet rs) throws Exception;
	SituacaoRequerimentoDepartamentoVO montarDados(SqlRowSet rs) throws Exception;
}
