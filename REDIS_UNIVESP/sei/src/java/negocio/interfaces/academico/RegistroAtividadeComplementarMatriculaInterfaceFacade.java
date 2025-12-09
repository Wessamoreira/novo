package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface RegistroAtividadeComplementarMatriculaInterfaceFacade {

	public void incluir(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterar(final RegistroAtividadeComplementarMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;
	
	public void excluir(Integer codigoRegistroAtividadeComplementar, UsuarioVO usuarioVO) throws Exception;

	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorRegistroAtividadeComplementar(Integer codigoRegistroAtividadeComplementar, String matricula, boolean controlarAcesso, Date dataInicio, Date dataFinal, UsuarioVO usuario) throws Exception;

	public List<RegistroAtividadeComplementarMatriculaVO> consultarListaMatriculaVOs(Integer curso, Integer codigoTurma, boolean turmaAgrupada, boolean turmaSubturma, String ano, String semestre, String situacao, String matricula, Integer codigoTipoAtividadeComplementar, Integer cargaHorariaEvento, Integer cargaHorariaConsiderada, boolean controlarAcesso, UsuarioVO usuario,String observacao, Date dataBases) throws Exception;
	
	public void incluirRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, Integer codigoRegistroAtividadeComplementar, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void alterarRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, Integer codigoRegistroAtividadeComplementar, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void removerRegistroAtividadeComplementarListaVOs(RegistroAtividadeComplementarMatriculaVO obj, List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, UsuarioVO usuarioVO) throws Exception;
	
	public List<RegistroAtividadeComplementarMatriculaVO> adicionarItemListaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs, List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs);

	public Integer consultarCargaHorariaRealizada(String matricula, Integer matrizCurricular, boolean obterCargaHorariaConsiderada) throws Exception;

	Boolean consultarPendenciaAtividadeComplementarPorMatricula(Integer pessoa, String matricula) throws Exception;

	void excluirArquivo(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	public Double consultarCargaHorariaConsideradaPorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO);
	
	List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
	
	void excluirComBaseNaMatricula(String matricula, UsuarioVO usuarioLogado) throws Exception;
	
	Boolean consultarSeExisteRegistroVinculadoATipoAtividadeComplementar(Integer gradecurricular, Integer tipoAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void alterarObservacao(RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception;

	void alterarSituacao(RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception;

	void realizarDeferimento(RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception;

	void realizarIndeferimento(RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception;

	List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatriculaSituacao(String matricula, SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatriculaEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Integer consultarQuantidadeAtividadeComplementarPorUsuarioAluno(String matricula) throws Exception;
	
	public Boolean realizarValidacaoIntegracaoAtividadeComplementar(String matricula, Integer matrizCurricular) throws Exception;
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultarAtividadeComplementarObrigatoriaMatriz(String matricula, Integer matrizCurricular) throws Exception;
	
	public void consultarRegistroAtividadeComplementarMatricula(String matricula, Integer matrizCurricular, StringBuilder sqlStr);
	
	public void consultarHorasMinimaAtividadeComplementar(String matricula, Integer matrizCurricular, StringBuilder sqlStr);
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultarRegistroAtividadeComplementarHistoricoPorMatricula(String matricula,Integer codigoGradeCurricular, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarCargaHorariaConsiderada(final RegistroAtividadeComplementarMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;
		
	List<RegistroAtividadeComplementarMatriculaVO> consultarRegistrosAprovadosPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
}

