package negocio.interfaces.protocolo;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoSituacaoDepartamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface TipoRequerimentoSituacaoDepartamentoInterfaceFacade {
	void incluir(final TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO, final UsuarioVO usuarioVO) throws Exception;
	void alterar(final TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO, final UsuarioVO usuarioVO) throws Exception;
	void excluirListaTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception;
	void incluirListaTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception;
	void alterarListaTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception;
	TipoRequerimentoSituacaoDepartamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;
	void validarDados(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO) throws ConsistirException;
	List<TipoRequerimentoSituacaoDepartamentoVO> consultarPorTipoRequerimentoDepartamento(Integer tipoRequerimentoDepartamento) throws Exception;
	List<TipoRequerimentoSituacaoDepartamentoVO> consultarPorTipoRequerimentoDepartamento(Integer tipoRequerimento, Integer departamento, Integer ordemTramite) throws Exception;
	List<TipoRequerimentoSituacaoDepartamentoVO> montarDadosConsulta(SqlRowSet rs) throws Exception;
	TipoRequerimentoSituacaoDepartamentoVO montarDados(SqlRowSet rs) throws Exception;
}
