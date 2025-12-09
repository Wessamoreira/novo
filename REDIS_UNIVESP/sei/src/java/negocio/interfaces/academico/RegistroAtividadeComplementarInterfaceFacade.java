package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface RegistroAtividadeComplementarInterfaceFacade {

	public void incluir(RegistroAtividadeComplementarVO registroAtividadeComplementarVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterar(RegistroAtividadeComplementarVO registroAtividadeComplementarVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void excluir(RegistroAtividadeComplementarVO registroAtividadeComplementarVO, UsuarioVO usuarioVO) throws Exception;

	public List<RegistroAtividadeComplementarVO> consultar(String nomeEvento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String instituicaoResponsavel, String local, String matricula, Date dataInicio, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAtividadeComplementarVO> consultarPorCoordenador(Integer coordenador, String nomeEvento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String instituicaoResponsavel, String local, String matricula, Date dataInicio, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	RegistroAtividadeComplementarVO consultarPorChavePrimaria(Integer registroAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception;


	void realizarProcessamentoExcelPlanilhaAtividadeComplementar(FileUploadEvent uploadEvent, RegistroAtividadeComplementarVO registroAtividadeComplementarVO, List<RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs ,Integer unidadeEnsino, UsuarioVO usuario)
			throws Exception;
	
	void validarTipoAtividadeComplementar(List<RegistroAtividadeComplementarMatriculaVO> RegistroAtividadeComplementarMatriculaVOs, UsuarioVO usuarioVO) throws Exception;
}
