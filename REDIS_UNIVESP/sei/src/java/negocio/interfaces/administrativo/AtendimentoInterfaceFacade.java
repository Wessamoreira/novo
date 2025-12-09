package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoAtendimentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface AtendimentoInterfaceFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#excluir(negocio
	 * .comuns.administrativo.OuvidoriaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AtendimentoVO obj, String idEntidade, UsuarioVO usuario) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#persistir
	 * (negocio.comuns.administrativo.OuvidoriaVO,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AtendimentoVO obj, String idEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#validarDados
	 * (negocio.comuns.administrativo.OuvidoriaVO)
	 */
	public void validarDados(AtendimentoVO obj, UsuarioVO usuarioVO) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#consultar
	 * (java.lang.String, java.lang.String, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	public List<AtendimentoVO> consultar(String valorConsulta, String campoConsulta, TipoAtendimentoEnum tipagemAtedimento, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	public AtendimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception;

	
	public void realizarConsultarPessoaExistePorCPF(AtendimentoVO ouvidoriaVO, TipoAtendimentoEnum tipoAtendimento , String idEntidade, UsuarioVO usuarioLogado) throws Exception;

	void realizarValidacoesParaBuscarResponsavelAtendimento(AtendimentoVO atendimento, Integer codigoUnidadeEnisno, Boolean controlarAcesso, TipoAtendimentoEnum tipoAtendimento, String idEntidade, UsuarioVO usuario) throws Exception;
	
	void removerAtendimentoInteracaoSolicitanteVO(AtendimentoVO atendimentoVO, AtendimentoInteracaoSolicitanteVO obj) throws Exception;	

	List consultarPorCodigoPessoa(Integer valorConsulta, TipoAtendimentoEnum tipagemAtedimento, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception;

	void realizarRegistroInteracaoSolicitante(AtendimentoVO obj, AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, Boolean questionamentoOuvidor, String idEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void adicionarAtendimentoInteracaoSolicitanteVO(AtendimentoVO atendimentoVO, AtendimentoInteracaoSolicitanteVO obj, Boolean questionamentoOuvidor) throws Exception;	

	Integer consultarTotalRegistro(String valorConsulta, String campoConsulta, TipoAtendimentoEnum tipagemAtedimento) throws Exception;

	void adicionarAtendimentoInteracaoDepartamentoVO(AtendimentoVO atendimentoVO, AtendimentoInteracaoDepartamentoVO obj) throws Exception;

	void removerAtendimentoInteracaoDepartamentoVO(AtendimentoVO atendimentoVO, AtendimentoInteracaoDepartamentoVO obj) throws Exception;

	void realizarRegistroInteracaoDepartamento(AtendimentoVO obj, AtendimentoInteracaoDepartamentoVO atendimentoDepartamento, Boolean questionamentoOuvidor, String idEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception;

	void removerArquivoVO(AtendimentoVO atendimentoVO, ArquivoVO obj) throws Exception;

	void adicionarArquivoVO(AtendimentoVO atendimentoVO, ArquivoVO obj) throws Exception;

	void realizarFinalizacaoOuvidoria(AtendimentoVO obj, AtendimentoInteracaoSolicitanteVO atendimentoSolicitante,  String idEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void realizarAtualizacaoAtendimentoJaVisualizado(AtendimentoVO obj, String idEntidade, UsuarioVO usuario) throws Exception;

	Integer consultarQuantidadeAtendimentoJaVisualizadosPorCodigoPessoaPorTipoAtendimento(Integer codigoAtendimento, TipoAtendimentoEnum tipoAtendimentoEnum) throws Exception;	

	void realizarAvaliacaoOuvidoria(String avaliacao, Integer codAtendimento, String motivoAvaliacaoRuim) throws Exception;

	void realizarPersistenciaAvaliacaoAtendimento(AtendimentoVO obj, String idEntidade, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> realizarBuscaPorTodosReponsavelAtendimento(TipoAtendimentoEnum tipoAtendimento) throws Exception;

	Integer consultarTotalRegistro(Date dataInicial, Date dataTermino, Integer codigoResponsavelAtendimento, String nomeSolicitante, String CPFSolicitante, String emailSolicitante, Integer codigoTipoAtendimento, String quadroSituacao, TipoAtendimentoEnum tipagemAtedimento) throws Exception;

	HashMap<String, Object> consultar(Date dataInicial, Date dataTermino, Integer codigoResponsavelAtendimento, String nomeSolicitante, String CPFSolicitante, String emailSolicitante, Integer codigoTipoAtendimento, String quadroSituacao, TipoAtendimentoEnum tipagemAtedimento, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception;

}