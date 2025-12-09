package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProgramacaoFormaturaUnidadeEnsinoInterfaceFacade {
	
	void persistir(ProgramacaoFormaturaVO programacaoFormaturaVO, UsuarioVO usuarioVO) throws Exception;
	
	void incluir(ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	
	void excluir(Integer codigoProgramacao, UsuarioVO usuarioVO) throws Exception;
	
	Boolean consultarProgramacaoFormaturaUnidadeEnsinoVinculadoMatricula(Integer programacaoFormatura, Integer unidadeEnsino) throws Exception;
	
	List<ProgramacaoFormaturaUnidadeEnsinoVO> consultarPorProgramacaoFormatura(Integer programacaoFormatura) throws Exception;
	
	void carregarUnidadeEnsinoNaoSelecionado(ProgramacaoFormaturaVO programacaoFormaturaVO, Integer unidadeEnsinoLogado) throws Exception;
	
	List<ProgramacaoFormaturaUnidadeEnsinoVO> consultarPorProgramacaoMatricula(Integer programacaoMatricula) throws Exception;

}
