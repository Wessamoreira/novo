package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.PossivelResponsavelRequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoFuncionarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoSituacaoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoUnidadeEnsinoVO;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.utilitarias.ConsistirException;

public interface TipoRequerimentoDepartamentoInterfaceFacade {

	public TipoRequerimentoDepartamentoVO novo() throws Exception;
	public void incluir(TipoRequerimentoDepartamentoVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(TipoRequerimentoDepartamentoVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(TipoRequerimentoDepartamentoVO obj, UsuarioVO usuario) throws Exception;
	public List<TipoRequerimentoDepartamentoVO> consultarPorCodigoTipoRequerimento(Integer codigoTipoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public void excluirTipoRequerimentoDepartamentoVOs(Integer codigoTipoRequerimento, List<TipoRequerimentoDepartamentoVO> objetos,  UsuarioVO usuario) throws Exception;
	public void alterarTipoRequerimentoDepartamentoVOs(Integer codigoTipoRequerimento, List<TipoRequerimentoDepartamentoVO> objetos, UsuarioVO usuario) throws Exception;
	public void incluirTipoRequerimentoDepartamentoVOs(Integer codigoTipoRequerimento, List<TipoRequerimentoDepartamentoVO> objetos, UsuarioVO usuario) throws Exception;
	public TipoRequerimentoDepartamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;
	public void setIdEntidade(String idEntidade);
	List<PossivelResponsavelRequerimentoVO> consultarPossiveisResponsaveisTipoRequerimentoPorDepartamentoEUnidadeEnsino(Integer departamento, Integer cargo, Integer pessoaEspecifica, List<TipoRequerimentoDepartamentoFuncionarioVO> tipoRequerimentoDepartamentoFuncionarioVOs, TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavel, List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, Integer limit, Integer offset) throws Exception;
	void adicionarTipoRequerimentoDepartamentoFuncionario(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO) throws ConsistirException;
	void removerTipoRequerimentoDepartamentoFuncionario(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, List<UnidadeEnsinoVO> unidadeEnsinoVOsRemover) throws ConsistirException;
	void adicionarTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO) throws ConsistirException, Exception;
	void removerTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoDepartamentoFuncionarioVO) throws ConsistirException;
}