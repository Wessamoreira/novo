package negocio.interfaces.academico;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface OfertaDisciplinaInterfaceFacade {
	
	void persistir(List<OfertaDisciplinaVO> ofertaDisciplinaVOs, UsuarioVO usuarioVO) throws Exception;
	
	List<OfertaDisciplinaVO> consultarPorAnoSemestre(String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	void excluir(OfertaDisciplinaVO ofertaDisciplinaVO, Boolean excluirDisciplinaIncluidaAluno, UsuarioVO usuarioVO) throws Exception;

	void adicionarDisciplina(OfertaDisciplinaVO ofertaDisciplinaVO, List<OfertaDisciplinaVO> ofertaDisciplinaVOs)
			throws Exception;

	List<OfertaDisciplinaVO> consultarDisciplina(String ano, String semestre, String campoConsulta,
			String valorConsulta) throws Exception;

	OfertaDisciplinaVO consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(Integer disciplina, String ano,
			String semestre) throws Exception;

	void upload(FileUploadEvent uploadEvent, List<OfertaDisciplinaVO> ofertaDisciplinaVOs,
			List<OfertaDisciplinaVO> listaErro, String ano, String semestre) throws Exception;

	void realizarInclusaoDisciplinaAluno(OfertaDisciplinaVO ofertaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	void consultarQtdeAlunoVinculadoDisciplina(OfertaDisciplinaVO ofertaDisciplinaVO);

	void realizarCriacaoTurmaOfertaDisciplina(String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
		
}
