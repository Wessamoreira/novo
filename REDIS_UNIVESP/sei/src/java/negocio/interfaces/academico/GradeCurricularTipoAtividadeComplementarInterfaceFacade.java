package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.GradeCurricularTipoAtividadeComplementarVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface GradeCurricularTipoAtividadeComplementarInterfaceFacade {
	
	public GradeCurricularTipoAtividadeComplementarVO novo() throws Exception;
	
	public void incluir(GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO, UsuarioVO usuario) throws Exception;

	public void excluir(GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO, UsuarioVO usuario) throws Exception;

	public List<GradeCurricularTipoAtividadeComplementarVO> consultarPorCodigoGrade(int valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void adicionarGradeCurricularTipoAtividadeComplementarVOs(GradeCurricularTipoAtividadeComplementarVO obj, List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO,Integer TotalCargaHorariaAtividadeComplementargradeCurricular, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void removerGradeCurricularTipoAtividadeComplementarVOs(GradeCurricularTipoAtividadeComplementarVO obj, List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO) throws Exception;

	public void incluirGradeCurricularTipoAtividadeComplementarVOs(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception;
	
	public void excluirGradeCurricularTipoAtividadeComplementar(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs, Integer gradeCurricular, UsuarioVO usuario) throws Exception;
	
	public void alterarGradeCurricularTipoAtividadeComplementarVOs(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception;

	public Integer consultarCargaHorariaObrigatoria(Integer gradecurricular) throws Exception;

	TipoAtividadeComplementarVO consultarCargaHorasMaximaPermitidoPeriodoLetivoDoTipoAtividadeComplementar(TipoAtividadeComplementarVO obj, Integer curso, String matricula, PeriodicidadeEnum periodicidadeEnum, Date dataBase, Integer registroAtividadeComplementarMatriculaDesconsiderar) throws Exception;

	void realizarCopiaTipoAtividadeComplementarOutraGrade(Integer gradeCurricularClonar, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;
	
	public void validarTotalCargaHorariaAtividadeComplementar(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVO, Integer TotalCargaHorariaAtividadeComplementargradeCurricular) throws ConsistirException;
}
