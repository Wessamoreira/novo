package negocio.interfaces.ead;

import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;

import java.util.List;


public interface ConteudoRegistroAcessoInterfaceFacade {
	

        
    ConteudoRegistroAcessoVO consultarConteudoUltimoRegistroAcessoPorMatriculaConteudo(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo) throws Exception;
    
    Double consultarTotalPontosAlunoAtingiuConteudo(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo);
    
    Integer consultarTotalAcessoAlunoRealizouConteudo(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo);
    
    Integer consultarTotalAcessoAlunoRealizouPagina(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo, Integer pagina) ;

	void incluir(String matricula, Integer conteudo, Integer unidadeConteudo, Integer pagina, Integer matriculaPeriodoTurmaDisciplina) throws Exception;

	List<ConteudoRegistroAcessoVO> consultarDataAcessoPontosTotalAcumuladoConteudo(Integer codigoMatriculaPeriodoTurmaDisciplina) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de jun de 2016 
	 * @param conteudo
	 * @return
	 * @throws Exception 
	 */
	Integer consultarQuantidadePaginasAcessou(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo) throws Exception;

	List<ConteudoRegistroAcessoVO> consultarPorGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO obj) throws Exception;
	
	public ConteudoRegistroAcessoVO consultarPorMatriculaConteudoUnidadePagina(String matricula, Integer conteudoUnidadePagina) throws Exception;

	Integer consultarQuantidadeRegistrosPorConteudoUnidadePaginaUnidadeConteudo(Integer conteudoUnidadePagina, Integer unidadeConteudo) throws Exception;
}
