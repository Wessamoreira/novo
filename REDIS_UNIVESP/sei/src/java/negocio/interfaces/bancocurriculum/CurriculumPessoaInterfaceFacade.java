package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.CurriculumPessoaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CurriculumPessoaInterfaceFacade {

	void incluir(CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	void excluir(CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	List<CurriculumPessoaVO> consultarPorPessoa(Integer pessoa) throws Exception;
	
	void validarDados(CurriculumPessoaVO curriculumPessoaVO) throws ConsistirException;

	void incluirCurriculumPessoa(PessoaVO pessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
}
